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
		
		CSVTable test = CSVTable.fromText("example1", """
			word,length,vvvvvvvvvvvvvvvvvvvv,used_in_language,gay_people
			"dog",3,"noun",true,"yes"
			"plaid",5,"adjective",true,"no"
			"defenstrate",12,"verb",false,"yes"
			""");
		
		System.out.println(test.toString());
	}

}
