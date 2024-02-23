package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import tables.HashTable;

public interface Table extends Iterable<Row> {
	public void clear();

	public List<Object> put(String key, List<Object> fields);

	public List<Object> get(String key);

	public List<Object> remove(String key);

	//2.J complete
	public default boolean contains(String key) {
		if(this.get(key) != null) {
			return true; 
		} else {
			return false;
		}
	}

	public int degree();

	public int size();

	public default boolean isEmpty() {
		if(size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode();

	@Override
	public boolean equals(Object obj);

	@Override
	public Iterator<Row> iterator();

	public String name();

	public List<String> columns();

	@Override
	public String toString();
	
	public default HashTable filter(String column, Object target) {
		// K1
		boolean run = true;
		
		for(int i = 0; i < size(); i++) {
			for(int j = 0; j < degree(); j++) {
				if(this.columns().get(j).equals(column)) {
					run = false;
					break;
				}
			}
		}
		
		if(run == true) {
			throw new IllegalArgumentException();
		}

		
		// K2
		if(target == null) {
			throw new IllegalArgumentException();
		}
		
		// K3
		HashTable hash_parition = new HashTable(this.name(), this.columns());
		
		// Traverse each row of this table (not the partition) using the iterator. For each row traversed:
		Iterator<Row> it = this.iterator();
		
		// 		Check if the row contains the target value in the given column. If the column is the key, check the key. If it is a field,
		//		check the field at the index in the list of fields corresponding to the index of the column in the list of columns.
		Row row = it.next();
		
		if(row.key().equals(target)) {
			
		}
		
		for(int i = 0; i < degree(); i++) {
			//if(row.)
		}
		
		// 		If the value in the given column of the row isn’t null and equals the target when compared as strings, include the row
		// 		in the partition by calling put on the partition and passing it the corresponding key and list of fields.
		// 		Otherwise, exclude the row from the partition by just skipping it and traversing to the next row.
		
		// Return the resulting partition, even if it is empty.
		return hash_parition;
		
	}

	public default String toTabularView(boolean sorted) {
		StringBuilder sb = new StringBuilder();
		List<Row> rowList = new ArrayList<Row>();
		sb.append(String.format("| %-15s |\n", name()));
		StringBuilder seperator = new StringBuilder();
		
		for(Row row: this) {
			rowList.add(row);
		}
		
		for(int i = 0; i < degree(); i++) {
			seperator.append("------------------");
		}
		
		if(sorted) {
			Collections.sort(rowList);
		}
		
		sb.append(seperator.toString());
		sb.append("--\n");
		
		for(Object col: this.columns()) {
			sb.append(String.format("| %-15s ", col));
		}
		sb.append("|\n");
		sb.append(seperator.toString());
		sb.append("--\n");
		
		
		for(int i = 0; i <= 2*size(); i++) {
			
			if(i%2 == 0) {
				sb.append(seperator.toString());
				sb.append("--\n");
			} else {
				Row row = rowList.get(i/2);
				List<Object> cols = new ArrayList<>(row.fields());
				
				// Try catch for the key of each row
				try {
					// Checks if value is a number, parses to double if so
					double value = Double.parseDouble(row.key());
					sb.append(String.format("| %15.2f ", value));
				} catch (NumberFormatException e) {
					// Truncates string when too large
					String str = row.key();
					if(str.length() > 15) {
						str = str.substring(0,15);
					}
					sb.append(String.format("| %-15s ", row.key()));
				}
				
				for(int j = 0; j < degree()-1; j++) {
					// Prints null if the column element is null
					if(cols.get(j) == null){
						sb.append(String.format("|                 ", cols.get(j)));
						break;
					}
					
					// Try catch for each element of each row
					try {
						// Checks if value is a number, parses to double if so
						double value = Double.parseDouble((String) cols.get(j));
						sb.append(String.format("| %15.2f ", value));
					} catch (NumberFormatException e) {
						// Truncates string when too large
						String str = (String)cols.get(j);
						if(str.length() > 15) {
							str = str.substring(0,15);
						}
						sb.append(String.format("| %-15s ", str));
					}
				}
				
				sb.append(" |\n");
			}
			
		}
		
		String finalView = sb.toString();
		return finalView;
	}
}
