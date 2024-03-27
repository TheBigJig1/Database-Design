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

	public default void union(Table param){
		if(this.degree() != param.degree()){
			throw new IllegalArgumentException();
		}

		for(Row target: param){
			this.put(target.key(), target.fields());
		}
	}

	public default void intersect(Table param){
		if(this.degree() != param.degree()){
			throw new IllegalArgumentException();
		}
		List<String> needRemoved = new ArrayList<>();

		for(Row target: this){
			if(!param.contains(target.key())){
				needRemoved.add(target.key());
			}
		}

		for(String thisKey: needRemoved){
			this.remove(thisKey);
		}
	}

	public default void minus(Table param){
		if(this.degree() != param.degree()){
			throw new IllegalArgumentException();
		}
		List<String> needRemoved = new ArrayList<>();

		for(Row target: this){
			if(param.contains(target.key())){
				needRemoved.add(target.key());
			}
		}

		for(String thisKey: needRemoved){
			this.remove(thisKey);
		}
	}

	public default void keep(String column, Object target){
		HashTable temp = this.filter(column, target);
		this.intersect(temp);
	}

	public default void drop(String column, Object target){
		HashTable temp = this.filter(column, target);
		this.minus(temp);
	}
	
	public default HashTable filter(String column, Object target) {
		// K2
		if(target == null) {
			throw new IllegalArgumentException();
		}
		
		// K1
		boolean run = true;
		
		for(int j = 0; j < degree(); j++) {
			if(this.columns().get(j).equals(column)) {
				run = false;
				break;
			}
		}
		
		if(run == true) {
			throw new IllegalArgumentException();
		}
		
		// K3
		HashTable hash_parition = new HashTable(this.name()+"_partition", this.columns());
		
		
		Iterator<Row> it = this.iterator();
		
		for(int i = 0; i < size(); i++) {
			
			Row row = it.next();
			String par = target.toString();
			
			if(row.key().equals(par)) {
				hash_parition.put(row.key(), row.fields());
			}
			
			for(var col : row.fields()) {
				if(col == null) {
					continue;
				} else{
					String p = col.toString();
					if(p.equals(par)) {
						hash_parition.put(row.key(), row.fields());
					}
				}
			}
		}
		
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
			if(col instanceof String && col.toString().length() > 15){
				sb.append(String.format("| %-15s ", col.toString().substring(0,12) + "..."));
			} else {
				sb.append(String.format("| %-15s ", col));
			}
		}
		sb.append(" |\n");
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
					// Truncates string when too large
					String str = row.key();
					if(str.length() > 15) {
						str = str.substring(0,12) + "...";
					}
					sb.append(String.format("| %-15s ", str));
				} catch (Exception e) {
					throw new IllegalStateException("Key type not recognized");
				}
				
				for(int j = 0; j < degree()-1; j++) {
					// Prints null if the column element is null
					if(cols.get(j) == null){
						sb.append(String.format("|                 ", cols.get(j)));
						break;
					}
					
					if(cols.get(j) instanceof Integer){
						int value = Integer.parseInt(cols.get(j).toString());
						sb.append(String.format("| %15d ", value));
					}

					if(cols.get(j) instanceof Double){
						double value = Double.parseDouble(cols.get(j).toString());
						sb.append(String.format("| %15.2f ", value));
					}

					if(cols.get(j) instanceof Boolean){
						sb.append(String.format("| %-15s ", cols.get(j).toString()));
					}

					if(cols.get(j) instanceof String){
						String str = (String)cols.get(j);
						if(str.length() > 15) {
							str = str.substring(0,12) + "...";
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
