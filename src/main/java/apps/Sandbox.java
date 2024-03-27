package apps;


import java.io.File;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import model.Row;
import model.Table;
import tables.HashTable;
import tables.CSVTable;

@SuppressWarnings("unused")
public class Sandbox {

	private static int capacity = 521;
	public static void main(String[] args) {
		
		Table test = new CSVTable("example2");
		test.put("JuJu_Smith_Schuster",List.of("\"Patriots\"","17","20.5","True"));
		test.put("Eli_Manning",List.of("\"Giants\"","10","22.9","False"));
		test.put("Jason_Kelce",List.of("\"Eagles\"","62","null","True"));
		test.put("Travis_Kelce",List.of("\"Cheifs\"","87","null","False"));
		test.put("DK_Metcalf",List.of("\"Seahawks\"","14","8.3","True"));
		
		System.out.println(test.toString());
	}

}
