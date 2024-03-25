package apps;


import java.io.File;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
//import java.util.Arrays;
import java.util.List;

import model.Row;
import tables.HashTable;

@SuppressWarnings("unused")
public class Sandbox {

	private static int capacity = 521;
	public static void main(String[] args) {
		
		 
		/*HashTable test1 = new HashTable("Jaxons Favorite Animals", List.of("Species", "Class", "Color"));
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
		System.out.print(test1);*/

		//var list1 = List.of("adnd", "14", "true");	// multiple lines 
		//var list2 = List.of("dahd, 17, true", "ijif, 19, false");	// dat is 2x3
		//Files.write(csv, list1);
		//var list3 = File.readAllLines(csv);
		//System.out.println("\"" + "string" + "\"");

		String s = "baseball,true,\"Babe Ruth\",3";
		System.out.println(s);

		Row temp = decodeRow(s);

		System.out.print(temp.toString());
		
	}

	private static Row decodeRow(String record) {

		String[] f = record.split(",");
		String key = f[0];
		List<Object> fields = new ArrayList<Object>();
		
		for(int i = 1; i < f.length; i++){
			fields.add(decodeField(f[i]));
		}

		return new Row(key, fields);

	}

	private static Object decodeField(String field) {
		if(field.equals("null")){
			return null;
		}
		if(field.substring(0,1).equals("\"")){
			return field.substring(1, field.length()-1);
		}
		if(field.equalsIgnoreCase("true")){
			return true;
		}
		if(field.equalsIgnoreCase("false")){
			return false;
		}
		try {
			return Integer.parseInt(field);
		} catch (Exception e){
			try{
				return Double.parseDouble(field);
			} catch (Exception f){
				throw new IllegalArgumentException("The given field is unrecognized.");
			}
		}
	}

}
