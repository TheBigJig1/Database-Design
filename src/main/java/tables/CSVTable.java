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
			List<String> row = new ArrayList<String>();
			row.add(headerString);

			Files.write(csv, row);

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

	// 2.K tested and throws error on clear unit test
	private static Row decodeRow(String record) {

		String[] f = record.split(",");
		String key = f[0];
		List<Object> fields = new ArrayList<Object>();
		
		for(int i = 1; i < f.length; i++){
			String temp = f[i].trim();
			fields.add(decodeField(temp));
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

	// 2.I tested and throws error on clear Unit test
	private static Object decodeField(String field) {
		if(field.equalsIgnoreCase("null")){
			return null;
		}
		if(field.substring(0,1).equals("\"") && field.substring(field.length()-1,field.length()).equals("\"")){
			return field.substring(1, field.length()-1);
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
			// Try to parse to double
			try{
				return Double.parseDouble(field);
			} catch (Exception f){
				// Do nothing and leave
			}
		}
		
		throw new IllegalArgumentException("The given field is unrecognized.");
	}

	@Override
	public List<Object> put(String key, List<Object> fields) {
		// Read lines from csv table
		List<String> records = new ArrayList<String>();
		try {
			records  = Files.readAllLines(csv);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		// Find degree of this table, check if it matches degree of input
		String s = records.get(0);
		int degree = Arrays.asList(s.split(",")).size()-1;

		if(degree != fields.size()){
			throw new IllegalArgumentException("Incorrect degree");
		}

		// Encode new record
		String newRec = encodeRow(key, fields);
		Row temp = null;

		// Lin traverse and decode rows searching for key
		for(int i = 1; i < records.size(); i++){
			String tarkey = decodeRow(records.get(i)).key();
			// Hit
			if(key.equals(tarkey)){
				temp = decodeRow(records.get(i));
				records.remove(i);
				records.add(1, newRec);
				break;
			}
		}

		// Miss
		records.add(1, newRec);
		
		// Write to csv files
		try {
			Files.write(csv, records);
		} catch (Exception e) {
			throw new RuntimeException("Cannot write records to CSV File in Put method");
		}

		if(temp == null){
			return null;
		}
		return temp.fields();
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
	public int hashCode(){
		int fingerprint = 0;
		List<String> rows = new ArrayList<String>();

		try {
			rows  = Files.readAllLines(csv);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		for(String target : rows){
			Row temp = decodeRow(target);
			fingerprint += temp.hashCode();
		}

		return fingerprint;
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
