package tables;

import java.nio.file.*;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> put(String key, List<Object> fields) {
		
		if(degree()-1 != fields.size()){
			throw new IllegalArgumentException("Incorrect degree");
		}

		ObjectNode dataNode = (ObjectNode) rootNode.get("Data");

		// Hit
		if(dataNode.get(key) != null){

			JsonNode removedNode = dataNode.remove(key);
			@SuppressWarnings("rawtypes")
			List temp = mapper.convertValue(removedNode.get(key), List.class);

			dataNode.putPOJO(key, fields);

			flush();
			return temp;
		}

		// Miss
		dataNode.putPOJO(key, fields);

		flush();
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> get(String key) {

		ObjectNode dataNode = (ObjectNode) rootNode.get("Data");

		if(dataNode.get(key) != null){

			JsonNode temp = dataNode.get(key);
			@SuppressWarnings("rawtypes")
			List fields = mapper.convertValue(temp.get(key), List.class);
			
			return fields;
		}

		return null;
	}

	@Override
	public List<Object> remove(String key) {

		ObjectNode dataNode = (ObjectNode) rootNode.get("Data");

		if(dataNode.get(key) != null){

			JsonNode temp = dataNode.remove(key);
			@SuppressWarnings("rawtypes")
			List fields = mapper.convertValue(temp.get(key), List.class);
			
			return fields;
		}

		return null;
	}

	@Override
	public int degree() {
		List<String> s = columns();
		return s.size();
	}

	@Override
	public int size() {
		return rootNode.get("Data").size();
	}

	@Override
	public int hashCode() {
		// Create a new list
		int fingerprint = 0;

		JsonNode dataNode = rootNode.get("Data");

		Iterator<String> fieldNames = dataNode.fieldNames();
		while(fieldNames.hasNext()){
			String nextNode = fieldNames.next();
			fingerprint += nextNode.hashCode() ^ mapper.convertValue(dataNode.get(nextNode), List.class).hashCode();
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
			// Create a dataNode as a starting point
			var dataNode = rootNode.get("Data");

			// Create iterator
			Iterator<Map.Entry<String, JsonNode>> fields = dataNode.fields();
			
			// Initalize list to return iterator on
			List<Row> newList = new ArrayList<Row>();

			while(fields.hasNext()){
				
				Map.Entry<String, JsonNode> field = fields.next();

				String key = field.getKey();
				List<Object> fieldList  = new ArrayList<Object>();
           	 	
				ArrayNode tempFields = (ArrayNode) dataNode.get(key);

				// Add columns to the node
				for (JsonNode fieldx : tempFields) {
					fieldList.add(fieldx.asText());
				}
				
            	Row row = new Row(key, fieldList);

           	 	// Add the row to the list
           		newList.add(row);
			}

			return newList.iterator();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String name() {
		return (JSONTable.getFileName().toString()).substring(0, (JSONTable.getFileName().toString()).length()-5);
	}

	@Override
	public List<String> columns() {
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
	}

	@Override
	public String toString() {
		return toTabularView(false);
	}
}
