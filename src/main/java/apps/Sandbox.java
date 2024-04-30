package apps;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

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
import tables.JSONTable;
import tables.BinaryTable;

@SuppressWarnings("unused")
public class Sandbox {
	public static void main(String[] args) {
		
		String name = "Football";
		List<String> columnsList = Arrays.asList("Name", "Team", "Position", "Yards/Game");

		
		@SuppressWarnings("resource")
		Table example = new BinaryTable(name, columnsList);
		example.clear();

		List<Object> row1 = Arrays.asList("Ravens", "Linebacker", null);
		List<Object> row2 = Arrays.asList("Colts", "Kickers", 45);
		List<Object> row3 = Arrays.asList("WVU", "Quarterback", 180);


		example.put("Ray_Lewis", row1);
		example.put("Pat_McAfee", row2);
		example.put("Garrett_Greene", row3);
		example.put("Ray_Lewis", row1);
		
		System.out.println("\n" + example.toString());
		

	}

}
