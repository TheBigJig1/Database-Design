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
			word,length,part_of_speech
			"dog",3,"noun"
			"plaid",5,"adjective"
			"defenstrate",12,"verb"
			""");
		
		System.out.println(test.toString());
	}

}
