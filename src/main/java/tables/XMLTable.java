package tables;

import java.io.FileWriter;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.FileTable;
import model.Row;
import model.Table;

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
				tableColumns.addElement("Column").addText(column);
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
		if(degree()-1 != fields.size()){
			throw new IllegalArgumentException("Incorrect degree");
		}

		Element root = doc.getRootElement();
		Element rowsElement = root.element("Rows");
		List<Element> rows = rowsElement.elements("Row");

		List<String> oldFieldString = null;

		// Linear search for the row with the given key
		for (Element row : rows) {
			if (row.attributeValue("Key").equals(key)) {
				// On a hit, remove the old row and store its fields
				List<Element> columns = row.elements("Field");
				for (Element column : columns) {
					oldFieldString = new ArrayList<String>();
					oldFieldString.add(column.getText());
				}
				row.detach();
				break;
			}
		}
	
		// Add the new row
		Element newRow = rowsElement.addElement("Row");
		newRow.addAttribute("Key", key);
		for (Object field : fields) {
			newRow.addElement("Field").addText(field.toString());
		}

		flush();

		if(oldFieldString == null){
			return null;
		}
		// Parse the List of fields to a List of Objects
		List<Object> returnFields = new ArrayList<Object>();
		for(String temp : oldFieldString) {
			if (temp.equalsIgnoreCase("true") || temp.equalsIgnoreCase("false")) {
				returnFields.add(Boolean.parseBoolean(temp));
			} else if (temp.equalsIgnoreCase("null")) {
				returnFields.add(null);
			} else {
				try {
					returnFields.add(Integer.parseInt(temp));
				} catch (NumberFormatException e) {
					try {
						returnFields.add(Double.parseDouble(temp));
					} catch (NumberFormatException ex) {
						returnFields.add(temp);
					}
				}
			}
		}

		return returnFields;
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
		Element root = doc.getRootElement();
		Element rowsElement = root.element("Rows");
		List<Element> rowElements = rowsElement.elements("Row");
		return rowElements.size();
	}

	@Override
	public int hashCode() {
		// Create a new list
		int fingerprint = 0;

		Element root = doc.getRootElement();

		List<Element> Rows = root.elements("Rows");
		Iterator<Element> fieldNames = Rows.iterator();

		while(fieldNames.hasNext()){
			Element nextData = fieldNames.next();
			List<Object> fields = new ArrayList<Object>();

			String key = nextData.attributeValue("key");

			//Row temp = new Row(key, );
			//fingerprint += temp.hashCode();
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
		return toTabularView(false);
	}
}
