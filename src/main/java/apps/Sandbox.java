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

public class Sandbox {

	private static int capacity = 521;
	public static void main(String[] args) {
		
		 
		 /*HashTable test1 = new HashTable("Jaxon Transcript", List.of("Class Name", "Credit Hours", "Semester", "Grade"));
		 test1.put("CS210", List.of("4", "Spring 2024", "A"));
		 test1.put("CS350", List.of("3", "Spring 2024", "A"));
		 test1.put("CPE310", List.of("3", "Spring 2024", "A"));
		 test1.put("CPE10L", List.of("1", "Spring 2024", "A"));
		 test1.put("MATH441", List.of("3", "Spring 2024", "A"));
		 test1.put("MATH261", List.of("4", "Fall 2023", "B"));
		 test1.put("CS111", List.of("3", "Fall 2023", "A"));
		 test1.put("CS111L", List.of("1", "Fall 2023", "A"));
		 test1.put("EE223", List.of("3", "Fall 2023", "B"));
		 test1.put("EE223L", List.of("1", "Fall 2023", "A"));
		 test1.put("CPE271", List.of("3", "Fall 2023", "B"));
		 test1.put("CPE271L", List.of("1", "Fall 2023", "B"));
		 
		 
		 System.out.print(test1 +"\n");
		 
		 System.out.print(test1.filter("Grade", "B"));*/

		String key = "Jaxon Fielding";
		List<String> fields = List.of("CS210", "4", "Spring 2024", "A");

		int i = hashFunction1(key); 
		int c = hashFunction2(key);
		ArrayList<Integer> probes = new ArrayList<Integer>();
		ArrayList<Integer> test = new ArrayList<Integer>();

		for(int j = 0; j < capacity; j++){
			// Linear Probe
			i = (i + c*3) % capacity;

			probes.add(i);
		}

		Collections.sort(probes);

		for(int j = 0; j < capacity; j++){

			test.add(j);

		}

		

		for(int j = 0; j < capacity; j++){

			int po = probes.get(j) - test.get(j);
			if(po != 0){
				System.out.print("Fail at index: " + po);
			}

		}
		
	}

	private static int hashFunction2(String key) {
		String salt = key + "Jaxon Fielding";
		long hash = 0;
		
		for(int i = 0; i < salt.length(); i++) {
			hash += Math.pow(3, salt.length()-i) * (salt.charAt(i));
		}
		
		return 1 + Math.floorMod(hash, capacity-1);
	}

	private static int hashFunction1(String key){
		String salt = "Jaxon Fielding";
		
		try{ 
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(salt.getBytes());
			md.update(key.getBytes());

			BigInteger bigNum = new BigInteger(md.digest());

			return (Math.floorMod(bigNum.intValue(), capacity));

		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("SHA-256 not found");
		}
	}

}
