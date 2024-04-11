package apps;


import java.io.File;
import java.nio.file.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.*;

import model.Row;
import model.Table;
import tables.HashTable;
import tables.CSVTable;

@SuppressWarnings("unused")
public class Sandbox {

	private static final Path basePath = Paths.get("db", "tables");
	private static Path JSONTable;
	private static ObjectNode rootNode;
	private static final JsonMapper mapper = new JsonMapper();
	public static void main(String[] args) {
		
		String name = "test";
		List<String> columns = Arrays.asList("ExKey", "ExField1", "ExField2", "ExField3");

		JSONTable(name, columns);

		
	}

	public static void JSONTable(String name, List<String> columns) {
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
			ObjectNode metadataNode = rootNode.putObject("MetaData");
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

	public static void flush() {
		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(JSONTable.toFile(), rootNode);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

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

}
