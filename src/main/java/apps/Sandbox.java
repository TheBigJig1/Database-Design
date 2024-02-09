package apps;

import java.util.List;

import tables.SearchTable;

public class Sandbox {
	public static void main(String[] args) {
		/*
		 * TODO: Modify as needed to debug
		 * or demonstrate arbitrary code.
		 */

		SearchTable test1 = new SearchTable("Animals", List.of("Name", "species", "breed", "age"));
		test1.put("Buddy", List.of("Dog", "Beagle", "12"));
		test1.put("Tilly", List.of("Dog", "Foxhound", "13"));
		test1.put("Ash", List.of("Dog", "Labrador Retriver", "8"));
		test1.put("Felix", List.of("Cat", "Bombeii", "6"));
		
		System.out.print(test1);
	}
}
