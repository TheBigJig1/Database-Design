package apps;


import java.io.File;
import java.io.FileWriter;
import java.nio.file.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.*;

import model.Row;
import model.Table;
import tables.HashTable;
import tables.CSVTable;

@SuppressWarnings("unused")
public class Sandbox {

	public static final Path basePath = Paths.get("db", "tables");
	public static Path XMLTable;
	public static Document doc;
	public static void main(String[] args) {
		
		String name = "test";
		List<String> columnsList = Arrays.asList("ExKey", "ExField1", "ExField2", "ExField3");

		XMLTable("Example", columnsList);

		List<String> cols = columns();

		System.out.println("Columns:");
		for(String col : cols){
			System.out.println(col);
		}
		System.out.println("Done");
		
	}

	public static void XMLTable(String name, List<String> columns) {
		try{
			// Create a base directory in DB 
			Files.createDirectories(basePath);
			XMLTable = basePath.resolve(name + ".xml");

			// Create the file if it does not already exist
			if(Files.notExists(XMLTable)){
				Files.createFile(XMLTable);
			}

			doc = DocumentHelper.createDocument();
			var root = doc.addElement("Table");
			var tableColumns = root.addElement("Columns");
			root.addElement("Rows");

			for(String column : columns){
				tableColumns.addElement("Column").addText(column);
			}

			flush();

		} catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	public interface Flushable {
		void flush();
	}

	public static void flush() {
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			var writer = new XMLWriter(new FileWriter(XMLTable.toFile()), format);
			writer.write(doc);
			writer.close();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	public static List<String> columns() {
		// Create list to return
		List<String> columnNames = new ArrayList<String>();

		// get the root and the columns Element
		Element root = doc.getRootElement();
		Element columnsElement = root.element("Columns");

		List<Element> columns = columnsElement.elements("Column");

		// Add columns to the node
		for (Element col : columns) {
			columnNames.add(col.getText());
		}
		
		return columnNames;
	}

}
