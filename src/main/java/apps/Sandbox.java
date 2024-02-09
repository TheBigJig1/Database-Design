package apps;

import java.util.List;

import tables.SearchTable;

public class Sandbox {
	public static void main(String[] args) {
		/*
		 * TODO: Modify as needed to debug
		 * or demonstrate arbitrary code.
		 */

		SearchTable test1 = new SearchTable("Animals", List.of("Key", "1", "2", "3", "4"));
		test1.put("Dog", List.of("mammal", "4", "5", "6"));
		test1.put("Cat", List.of("mammal", "4", "5", "6"));
		test1.put("Lizard", List.of("Reptile", "4", "5", "6"));
		
		System.out.print(test1);
	}
}
