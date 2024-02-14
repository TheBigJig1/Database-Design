package apps;

import java.util.Arrays;
import java.util.List;

import tables.SearchTable;

public class Sandbox {
	public static void main(String[] args) {
		/*
		 * TODO: Modify as needed to debug
		 * or demonstrate arbitrary code.
		 */

		SearchTable test1 = new SearchTable("Sports List", List.of("Sport", "Players per team", "Game Length"));
		test1.put("Lacrosse", List.of("10", "60 minutes"));
		test1.put("Hockey", List.of("6", "60 minutes"));
		test1.put("Basketball", List.of("5", "String is 28 characters long"));
		test1.put("Soccer", List.of("10", "90 minutes"));
		test1.put("Football", Arrays.asList("10", "60 minutes"));
		test1.put("Baseball", Arrays.asList("10", null));
		test1.put("Softball", Arrays.asList("10", null));
		test1.put("Swimming", List.of("1", "Varies"));
		
		
		System.out.print(test1);
		
	}
}
