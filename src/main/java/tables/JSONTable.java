package tables;

import java.nio.file.*;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import model.FileTable;
import model.Row;
import model.Table;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.*;

public class JSONTable implements FileTable {

	// Fields
	private static final Path basePath = Paths.get("db", "tables");
	private Path JSONTable;
	private ObjectNode rootNode;
	private static final JsonMapper mapper = new JsonMapper();
	
	public JSONTable(String name, List<String> columns) {
		try{
			// Create a base directory in DB 
			Files.createDirectories(basePath);
			JSONTable = basePath.resolve(name + ".json");

			// Create the file if it does not already exist
			if(Files.notExists(JSONTable)){
				Files.createFile(JSONTable);
			}

			// Initialize RootNode
			rootNode = mapper.createObjectNode();

			// Put a new ObjectNode at the "Data" property of the rootNode
			ObjectNode metadataNode = rootNode.putObject("Metadata");
			rootNode.putObject("Data");

			ArrayNode columnNamesNode = metadataNode.putArray("Column_names");
			for(String column : columns){
				columnNamesNode.add(column);
			}

			//mapper.writeValue(JSONTable.toFile(), rootNode);

			flush();

		} catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	public JSONTable(String name) {
		try {
			JSONTable = basePath.resolve(name + ".json");
	
			if(!Files.exists(JSONTable)){
				throw new IllegalArgumentException();
			}
	
			// Use the mapper to read the root object node from the file
			rootNode = (ObjectNode) mapper.readTree(JSONTable.toFile());
	
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void clear() {
		try {
			ObjectNode newDataNode = mapper.createObjectNode();

			rootNode.set("Data", newDataNode);

			mapper.writeValue(JSONTable.toFile(), rootNode);
		} catch (Exception e){
			throw new RuntimeException(e);
		}
	}

	@Override
	public void flush() {
		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(JSONTable.toFile(), rootNode);
		} catch (Exception e) {
			throw new RuntimeException(e);
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
			List<String> records = Files.readAllLines(JSONTable);
			return records.size()-1;
		} catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	@Override
	public int hashCode() {
		// Create a new list
		int fingerprint = 0;
		List<String> rows = new ArrayList<String>();

		// Write all the lines to the file
		try {
			rows  = Files.readAllLines(JSONTable);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		// Add the fingerprint for each string decoded to a row
		for(int i = 1; i < rows.size(); i++){
			
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
		throw new UnsupportedOperationException();
	}

	@Override
	public String name() {
		return (JSONTable.getFileName().toString()).substring(0, (JSONTable.getFileName().toString()).length()-5);
	}

	@Override
	public List<String> columns() {
		try{
			// Create list to return
			List<String> columnNames = new ArrayList<String>();

			// retrieve the arrayNode column_names from the metaData node
			JsonNode metaNode = rootNode.get("Metadata");
			ArrayNode columnNamesNode = (ArrayNode) metaNode.get("Column_names");

			// Add columns to the node
			for (JsonNode columnNameNode : columnNamesNode) {
				columnNames.add(columnNameNode.asText());
			}
			
			return columnNames;

		} catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		return toTabularView(false);
	}
}
