package tables;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.HexFormat;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;

import model.FileTable;
import model.Row;

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

			Files.write(metadataPath.resolve("columns"), columns);

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
		throw new UnsupportedOperationException();
	}

	@Override
	public void flush() {
		throw new UnsupportedOperationException();
	}

	private static void writeInt(Path path, int i) {
		try (
			var out = new ObjectOutputStream(Files.newOutputStream(path));
		) {
			out.writeInt(i);
			out.flush();

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static int readInt(Path path) {
		try (
			var in = new ObjectInputStream(Files.newInputStream(path));
		) {
			var i = in.readInt();
			return i;

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static void writeRow(Path path, Row row) {
		try (
			var out = new ObjectOutputStream(Files.newOutputStream(path));
		) {
			Path parentDir = path.getParent();
			if(parentDir != null){
				Files.createDirectories(parentDir);
			}

			out.writeObject(row);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static Row readRow(Path path) {
		try (
			var in = new ObjectInputStream(Files.newInputStream(path));
		) {
			return (Row) in.readObject();

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

	@SuppressWarnings("unused")
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
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Object> get(String key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Object> remove(String key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int degree() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int hashCode() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean equals(Object obj) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<Row> iterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String name() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<String> columns() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		throw new UnsupportedOperationException();
	}
}
