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
import tables.XMLTable;

@SuppressWarnings("unused")
public class Sandbox {

	public static final Path basePath = Paths.get("db", "tables");
	public static Path XMLTable;
	public static Document doc;
	public static void main(String[] args) {
		
		String name = "test";
		List<String> columnsList = Arrays.asList("ExKey", "ExField1", "ExField2", "ExField3");

		//XMLTable("Example", columnsList);

		
	}

}
