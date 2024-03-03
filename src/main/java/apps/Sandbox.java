package apps;


//import java.util.Arrays;
import java.util.List;
import tables.HashTable;

public class Sandbox {
	public static void main(String[] args) {
		
		 
		 HashTable test1 = new HashTable("Jaxon Transcript", List.of("Class Name", "Credit Hours", "Semester", "Grade"));
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
		 
		 System.out.print(test1.filter("Grade", "B"));
		
	}
}
