package apps;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
//import java.util.Arrays;
import java.util.List;

import model.Row;
import tables.HashTable;

@SuppressWarnings("unused")
public class Sandbox {

	private static int capacity = 521;
	public static void main(String[] args) {
		
		 
		HashTable test1 = new HashTable("Jaxons Favorite Animals", List.of("Species", "Class", "Color"));
		test1.put("Dog", List.of("Mammal", "Any"));
		test1.put("Lizard", List.of("Reptile", "Any"));
		test1.put("Frog", List.of("Amphibian", "Green"));
		test1.put("Cow", List.of("Mammal", "Brown"));
		test1.put("Shark", List.of("Fish", "Gray"));

		HashTable test2 = new HashTable("Landons Favorite Animals", List.of("Species", "Class", "Color"));
		test2.put("Dog", List.of("Mammal", "Any"));
		test2.put("Cat", List.of("Mammal", "Any"));
		test2.put("Falcon", List.of("Bird", "Any"));
		test2.put("Alligator", List.of("Reptile", "Green"));
		test2.put("Shark", List.of("Fish", "Gray"));
		
		
		System.out.print(test1 +"\n");
		System.out.print(test2 +"\n");
		
		test1.drop("Species", "Dog");
		System.out.print(test1);
		
	}

}
