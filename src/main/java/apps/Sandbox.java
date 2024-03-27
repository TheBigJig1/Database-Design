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
import tables.HashTable;
import tables.CSVTable;

@SuppressWarnings("unused")
public class Sandbox {

	private static int capacity = 521;
	public static void main(String[] args) {
		
		CSVTable test = new CSVTable("Name", List.of("name", "sport"));

		String s = "baseball, tRue,\"Babe Ruth\",4.293 , 17, Null, FALsE, \"H20`.@je\"";
		System.out.println(s+"\n");
		
	}

}
