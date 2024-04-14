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

		List<Object> returnFields = null;

		// Linear search for the row with the given key
		for (Element row : rows) {
			if (row.attributeValue("Key").equals(key)) {
				// On a hit, remove the old row and store its fields
				List<Element> cols = row.elements("Field");
				returnFields = new ArrayList<Object>();
				for(Element obj : cols){
					if(obj.attributeValue("Type").equals("String")){
						returnFields.add(obj.toString());
					} else if(obj.attributeValue("Type").equals("Boolean")){
						returnFields.add(Boolean.parseBoolean(obj.toString()));
					} else if(obj.attributeValue("Type").equals("Null")){
						returnFields.add(null);
					} else if(obj.attributeValue("Type").equals("Integer")){
						returnFields.add(Integer.parseInt(obj.toString()));
					}
				}
				row.detach();
				break;
			}
		}
	
		// Add the new row
		Element newRow = rowsElement.addElement("Row");
		newRow.addAttribute("Key", key);

		for (Object obj : fields) {
			Element tempField = newRow.addElement("Field");
			tempField.addText(obj.toString());

			if(obj instanceof String){
				tempField.addAttribute("Type", "String");
			} else if(obj instanceof Boolean){
				tempField.addAttribute("Type", "Boolean");
			}  else if(obj instanceof Integer){
				tempField.addAttribute("Type", "Integer");
			} else {
				tempField.addAttribute("Type", "Null");
			}
		}

		flush();

		return returnFields;
	}

	@Override
	public List<Object> get(String key) {

		Element root = doc.getRootElement();
		Element rowsElement = root.element("Rows");
		List<Element> rows = rowsElement.elements("Row");

		for(Element target : rows) {
			if(target.attributeValue("Key").equals(key)){
				List<Element> fields = target.elements("Field");
				List<Object> returnFields = new ArrayList<Object>();
				for(Element obj : fields){
					if(obj.attributeValue("Type").equals("String")){
						returnFields.add(obj.toString());
					} else if(obj.attributeValue("Type").equals("Boolean")){
						returnFields.add(Boolean.parseBoolean(obj.toString()));
					} else if(obj.attributeValue("Type").equals("Null")){
						returnFields.add(null);
					} else if(obj.attributeValue("Type").equals("Integer")){
						returnFields.add(Integer.parseInt(obj.toString()));
					}
				}
				return returnFields;
			}
		}

		return null;
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
		Element x = root.element("Rows");
		List<Element> rows = x.elements("Row");

		Iterator<Element> fieldNames = rows.iterator();

		while(fieldNames.hasNext()){
			Element el = fieldNames.next();
			List<Object> rowFields = new ArrayList<Object>();
			List<Element> fields = el.elements("Field");

			String key = el.attributeValue("key");
			if(key == null){
				continue;
			}

			for(Element obj : fields){
				if(obj.attributeValue("type").equals("String")){
					rowFields.add(obj.toString());
				} else if(obj.attributeValue("type").equals("Boolean")){
					rowFields.add(Boolean.parseBoolean(obj.toString()));
				} else if(obj.attributeValue("type").equals("Null")){
					rowFields.add(null);
				} else if(obj.attributeValue("type").equals("Integer")){
					rowFields.add(Integer.parseInt(obj.toString()));
				}
			}
			
			Row temp = new Row(key, rowFields);
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
