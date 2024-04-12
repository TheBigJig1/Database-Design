package tables;

import java.io.FileWriter;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.FileTable;
import model.Row;

import org.dom4j.*;
import org.dom4j.io.*;

public class XMLTable implements FileTable {
	
	private static final Path basePath = Paths.get("db", "tables");
	private Path XMLTable;
	private Document doc;

	public XMLTable(String name, List<String> columns) {
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
				tableColumns.addElement(column);
			}

			flush();

		} catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	public XMLTable(String name) {
		try {
			XMLTable = basePath.resolve(name + ".xml");
	
			if(!Files.exists(XMLTable)){
				throw new IllegalArgumentException();
			}
	
			doc = new SAXReader().read(XMLTable.toFile());
	
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void clear() {
		Element root = doc.getRootElement();
		Element rows = root.element("Rows");

		rows.clearContent();
		flush();
	}

	@Override
	public void flush() {
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			var writer = new XMLWriter(new FileWriter(XMLTable.toFile()), format);
			writer.write(doc);
			writer.close();
		} catch (Exception e) {
			throw new IllegalStateException(e);
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
		return (XMLTable.getFileName().toString()).substring(0, (XMLTable.getFileName().toString()).length()-4);
	}

	@Override
	public List<String> columns() {
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

	@Override
	public String toString() {
		throw new UnsupportedOperationException();
	}
}
