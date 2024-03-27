package tables;

import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import model.FileTable;
import model.Row;
import model.Table;

public class CSVTable implements FileTable {
	
	private static final Path basePath = Paths.get("db", "tables");
	private Path csv;

	public CSVTable(String name, List<String> columns) {
		try{
			// Create a base directory in DB 
			Files.createDirectories(basePath);
			csv = basePath.resolve(name + ".csv");

			// Create the file if it does not already exist
			if(Files.notExists(csv)){
				Files.createFile(csv);
			}

			// Add the joined string to a row of Strings
			String headerString = String.join(",", columns);
			List<String> row = new ArrayList<String>();
			row.add(headerString);

			// Write list to the csv files
			Files.write(csv, row);

		} catch(Exception e){
			throw new RuntimeException(e);
		}

	}

	public CSVTable(String name) {
		// intialize the path field
		csv = basePath.resolve(name + ".csv");

		if(!Files.exists(csv)){
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void clear() {
		try{
			// Read all lines to a list of records
			List<String> records = Files.readAllLines(csv);

			// Add only to header to the list of records
			List<String> header = new ArrayList<String>();
			header.add(records.get(0));

			// Write the list with only the header to the file
			Files.write(csv, header);

		} catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	private static String encodeRow(String key, List<Object> fields) {
		// Create an empty list
		List<String> encodedFields = new ArrayList<>();

		// Add encode the fields and add to list
		for(Object target : fields){
			encodedFields.add(encodeField(target));
		}

		// Return a string of the key and fields seperated by commas
		return String.join(",", key, String.join(",", encodedFields));
	}

	private static Row decodeRow(String record) {

		// Split the string into an array of strings
		String[] f = record.split(",");
		String key = f[0];
		List<Object> fields = new ArrayList<Object>();
		
		// Exlcuding the key, trim the strings, decode them, add to list of fields
		for(int i = 1; i < f.length; i++){
			String temp = f[i].trim();
			fields.add(decodeField(temp));
		}

		// Return a new row of key and fields
		return new Row(key, fields);
	}

	private static String encodeField(Object obj) {
		// Check if obj is an instance of any supported fields
		if(obj == (null)){
			return "null";
		}
		if(obj instanceof String){
			return "\"" + obj + "\"";
		}
		if((obj instanceof Boolean) || (obj instanceof Integer) || (obj instanceof Double)){

			return obj.toString();
		}
		
		throw new IllegalArgumentException("The given object is unsupported: " + obj.toString());
	}

	private static Object decodeField(String field) {
		// Check if string field is one of the supported instances
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
		
		throw new IllegalArgumentException("The given field " + field + "is unrecognized: ");
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
		if(temp == null){
			records.add(1, newRec);
		}
		
		// Write to csv files
		try {
			Files.write(csv, records);
		} catch (Exception e) {
			throw new RuntimeException("Cannot write records to CSV File in Put method");
		}

		// Return null or temp
		if(temp == null){
			return null;
		}
		return temp.fields();
	}

	@Override
	public List<Object> get(String key) {

		// Read all lines into list of records
		List<String> records = new ArrayList<String>();
		try {
			records  = Files.readAllLines(csv);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		String temp = null;
		// Lin traverse and decode rows searching for key
		for(int i = 1; i < records.size(); i++){
			String tarkey = decodeRow(records.get(i)).key();

			// Hit
			if(key.equals(tarkey)){
				temp = records.get(i);
				records.remove(i);
				records.add(1, temp);
			}
		}

		// Write to csv files
		try {
			Files.write(csv, records);
		} catch (Exception e) {
			throw new RuntimeException("Cannot write records to CSV File in Put method");
		}

		// Return null or temp
		if(temp == null){
			return null;
		}
		return decodeRow(temp).fields();

	}

	@Override
	public List<Object> remove(String key) {
		// Read all lines to a list of records
		List<String> records = new ArrayList<String>();
		try {
			records  = Files.readAllLines(csv);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		Row temp = null;
		// Lin traverse and decode rows searching for key
		for(int i = 1; i < records.size(); i++){
			String tarkey = decodeRow(records.get(i)).key();

			// Hit
			if(key.equals(tarkey)){
				temp = decodeRow(records.get(i));
				records.remove(i);
			}
		}

		// Write to csv files
		try {
			Files.write(csv, records);
		} catch (Exception e) {
			throw new RuntimeException("Cannot write records to CSV File in Put method");
		}

		// Return null or temp
		if(temp == null){
			return null;
		}
		return temp.fields();
	}

	@Override
	public int degree() {
		List<String> s = columns();
		return s.size();
	}

	@Override
	public int size() {
		// Return size 
		try{
			List<String> records = Files.readAllLines(csv);
			return records.size()-1;
		} catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	@Override
	public int hashCode(){
		// Create a new list
		int fingerprint = 0;
		List<String> rows = new ArrayList<String>();

		// Write all the lines to the file
		try {
			rows  = Files.readAllLines(csv);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		// Add the fingerprint for each string decoded to a row
		for(int i = 1; i < rows.size(); i++){
			Row temp = decodeRow(rows.get(i));
			fingerprint += temp.hashCode();
		}

		return fingerprint;
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
			// Read all lines from the csv to a list.  Remove the header.  Create empty list
			List<String> records  = Files.readAllLines(csv);
			records.remove(0);
			List<Row> newList = new ArrayList<Row>();

			// Excluding the header, decode  all lines from old list of strings. Add to new list of rows
			for(String target : records){
				newList.add(decodeRow(target));
			}

			return newList.iterator();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String name() {
		// Return the name of the file exluding the .csv suffix
		return (csv.getFileName().toString()).substring(0, (csv.getFileName().toString()).length()-4);
	}

	@Override
	public List<String> columns() {
		try{
			// Read all lines into a list of strings
			List<String> records = Files.readAllLines(csv);
			String s = records.get(0);

			// Return the header as an array of strings
			return Arrays.asList(s.split(","));

		} catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		return toTabularView(false);
	}

	public static CSVTable fromText(String name, String text) {
		try{
			// create base directory if needed
			Files.createDirectories(basePath);
			Path localCSV = basePath.resolve(name + ".csv");

			if(Files.notExists(localCSV)){
				Files.createFile(localCSV);
			}

			// Write the bytes of a string to the files
			Files.write(localCSV, text.getBytes());

			// Return the table retrieved from the 1-ary constructor
			return new CSVTable(name);

		} catch(Exception e){
			throw new RuntimeException(e);
		}
	}
}
