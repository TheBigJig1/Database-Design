package tables;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.HexFormat;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;

import model.FileTable;
import model.Row;
import model.Table;

public class BinaryTable implements FileTable {
	
	private static final Path basePath = Paths.get("db", "tables");
	private Path root;
	private Path dataPath;
	private Path metadataPath;

	private static final boolean BYTES_MODE = false;
	private static final boolean ZIP_MODE = false;

	public BinaryTable(String name, List<String> columns) {
		try {
			root = basePath.resolve(name);
			Files.createDirectories(root);

			dataPath = root.resolve("Data");
			metadataPath = root.resolve("Metadata");

			Files.createDirectories(dataPath);
			Files.createDirectories(metadataPath);

			Files.write(metadataPath.resolve("Columns"), columns);

		} catch (Exception e){ 
			throw new RuntimeException(e);
		}
	}

	public BinaryTable(String name) {
		root = basePath.resolve(name);
		if (Files.notExists(root)){
			throw new IllegalArgumentException("Missing table: " + name);
		}

		dataPath = root.resolve("Data");
		metadataPath = root.resolve("Metadata");
	}

	@Override
	public void clear() {
		try {
			Files.walk(dataPath)
				.skip(1)
				.sorted(Comparator.reverseOrder())
				.forEach(path -> {
					try {
						Files.delete(path);
					} catch (IOException e) {
						throw new IllegalStateException(e);
					}
			});
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}

		writeInt(metadataPath.resolve("Size"), 0);
		writeInt(metadataPath.resolve("Fingerprint"), 0);

	}

	@Override
	public void flush() {
		throw new UnsupportedOperationException();
	}

	private static void writeInt(Path path, int i) {
		try (
			var out = new ObjectOutputStream(Files.newOutputStream(path));
		) {

			if(BYTES_MODE) {
				var bytes = ByteBuffer.allocate(4).putInt(i).array();
				Files.write(path, bytes);
			} else {
				out.writeInt(i);
				out.flush();
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static int readInt(Path path) {
		try {
			if(BYTES_MODE) {
				var bytes = Files.readAllBytes(path);
				var buf = ByteBuffer.wrap(bytes).getInt();
				return buf;
			} else {
				try (var in = new ObjectInputStream(Files.newInputStream(path))) {
					var i = in.readInt();
					return i;
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static void writeRow(Path path, Row row) {
		try {
			if(path.getParent() != null){
				Files.createDirectories(path.getParent());
			}

			if(BYTES_MODE) {
				var bytes = row.getBytes();
				Files.write(path, bytes);
			} else {
				var out = new ObjectOutputStream(Files.newOutputStream(path));
				
				out.writeObject(row);
				out.close();
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static Row readRow(Path path) {
		try {
			if(BYTES_MODE){
				var bytes = Files.readAllBytes(path);
				return Row.fromBytes(bytes);
			} else {
				try (var in = new ObjectInputStream(Files.newInputStream(path))) {
					return (Row) in.readObject();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void deleteRow(Path path) {
		try {
			Files.delete(path);
			
			if(path.getParent().getNameCount() == 0){
				Files.delete(path.getParent());
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private String digestFunction(String key) {
		try {
			var sha1 = MessageDigest.getInstance("SHA-1");

			sha1.update("salt-".getBytes());
			sha1.update(key.getBytes());

			var digest = sha1.digest();

			var hex = HexFormat.of();
			return hex.formatHex(digest);

		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(e);
		}
	}

	private Path pathOf(String digest) {
		String prefix = digest.substring(0, 1);
		String suffix = digest.substring(2);
		return dataPath.resolve(prefix).resolve(suffix);
	}

	@Override
	public List<Object> put(String key, List<Object> fields) {
		if(degree()-1 != fields.size()){
			throw new IllegalArgumentException("Incorrect degree");
		}

		String digest = digestFunction(key);
		Path keyPath = pathOf(digest);
		int curSize = readInt(metadataPath.resolve("Size"));
		int curFingerprint = readInt(metadataPath.resolve("Fingerprint"));

		Row obj = new Row(key, fields);

		// Hit
		if(Files.exists(keyPath)){
			Row temp = readRow(keyPath);
			writeRow(keyPath, obj);

			writeInt(metadataPath.resolve("Fingerprint"), curFingerprint - temp.hashCode() + obj.hashCode());

			return temp.fields();
		}

		// Miss
		writeRow(keyPath, obj);
		writeInt(metadataPath.resolve("Size"), curSize + 1);
		writeInt(metadataPath.resolve("Fingerprint"), curFingerprint + obj.hashCode());

		return null;
	}

	@Override
	public List<Object> get(String key) {
		// Get keypath
		String digest = digestFunction(key);
		Path keyPath = pathOf(digest);

		// Check if file exists
		if(Files.exists(keyPath)){
			Row temp = readRow(keyPath);
			return temp.fields();
		} else {
			return null;
		}
	}

	@Override
	public List<Object> remove(String key) {
		// Get keypath
		String digest = digestFunction(key);
		Path keyPath = pathOf(digest);
		int curSize = readInt(metadataPath.resolve("Size"));
		int curFingerprint = readInt(metadataPath.resolve("Fingerprint"));

		// Check if file exists
		if(Files.exists(keyPath)){
			// create temp row, remove row
			Row temp = readRow(keyPath);
			deleteRow(keyPath);

			// Update size/fingerprint
			writeInt(metadataPath.resolve("Size"), curSize - 1);
			writeInt(metadataPath.resolve("Fingerprint"), curFingerprint - temp.hashCode());

			return temp.fields();
		} else {
			return null;
		}
	}

	@Override
	public int degree() {
		return columns().size();
	}

	@Override
	public int size() {
		return readInt(metadataPath.resolve("Size"));
	}

	@Override
	public int hashCode() {
		return readInt(metadataPath.resolve("Fingerprint"));
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Table && obj.hashCode() == this.hashCode()) {
			return true;
		} else {
			return false;
		}
	}

		@Override
		public Iterator<Row> iterator() {
			try {
				return Files.walk(dataPath)
					.filter(Files::isRegularFile)
	            	.map(BinaryTable::readRow)
	            	.iterator();
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}

	@Override
	public String name() {
		return root.getFileName().toString();
	}

	@Override
	public List<String> columns() {
		Path colsPath = metadataPath.resolve("Columns");

		try {
			List<String> columns = Files.readAllLines(colsPath);
			return columns;
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public String toString() {
		return toTabularView(false);
	}
}
