package tables;

import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import model.FileTable;
import model.Row;

public class CSVTable implements FileTable {
	
	private static final Path basePath = Paths.get("db", "tables");
	private Path csv;

	public CSVTable(String name, List<String> columns) {
		System.out.println("test");
		try{
			Files.createDirectories(basePath);
			csv = basePath.resolve(name + ".csv");

			if(Files.notExists(csv)){
				Files.createFile(csv);
			}

			String headerString = String.join(",", columns);
			List<String> records = new ArrayList<String>();
			records.add(headerString);

			Files.write(csv, records);

		} catch(Exception e){
			throw new RuntimeException(e);
		}

	}

	public CSVTable(String name) {
		csv = basePath.resolve(name + ".csv");

		if(!Files.exists(csv)){
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void clear() {
		try{
			List<String> records = Files.readAllLines(csv);
			List<String> header = new ArrayList<String>();

			header.add(records.get(0));

			Files.write(csv, header);

		} catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	// 2.J tested and works
	@SuppressWarnings("unused")
	private static String encodeRow(String key, List<Object> fields) {
		
		List<String> encodedFields = new ArrayList<>();

		for(Object target : fields){
			encodedFields.add(encodeField(target));
		}

		return String.join(",", key, String.join(",", encodedFields));
	}

	// 2.K
	private static Row decodeRow(String record) {

		String[] f = record.split(",");
		String key = f[0];
		List<Object> fields = new ArrayList<Object>();
		
		for(int i = 1; i < f.length; i++){
			fields.add(decodeField(f[i]));
		}

		return new Row(key, fields);

	}

	// 2.H tested and works
	private static String encodeField(Object obj) {
		if(obj.equals(null)){
			return "null";
		}
		if(obj instanceof String){
			return "\"" + obj + "\"";
		}
		if((obj instanceof Boolean) || (obj instanceof Integer) || (obj instanceof Double)){
			return obj.toString();
		}
		
		throw new IllegalArgumentException("The given object is unsupported.");
	}

	// 2.I
	private static Object decodeField(String field) {
		if(field.equals("null")){
			return null;
		}
		if(field.substring(0,1).equals("\"")){
			return field.substring(1, field.length()-2);
		}
		if(field.equalsIgnoreCase("true")){
			return true;
		}
		if(field.equalsIgnoreCase("false")){
			return false;
		}
		try {
			return Integer.parseInt(field);
		} catch (Exception e){
			try{
				return Double.parseDouble(field);
			} catch (Exception f){
				throw new IllegalArgumentException("The given field is unrecognized.");
			}
		}
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
		List<String> s = columns();
		return s.size();
	}

	@Override
	public int size() {
		try{
			List<String> records = Files.readAllLines(csv);

			return records.size()-1;
		} catch(Exception e){
			throw new RuntimeException(e);
		}
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
		return (csv.getFileName().toString()).substring(0, (csv.getFileName().toString()).length()-4);
	}

	@Override
	public List<String> columns() {
		try{
			List<String> records = Files.readAllLines(csv);
			String s = records.get(0);

			return Arrays.asList(s.split(","));

		} catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		throw new UnsupportedOperationException();
	}

	public static CSVTable fromText(String name, String text) {
		throw new UnsupportedOperationException();
	}
}
