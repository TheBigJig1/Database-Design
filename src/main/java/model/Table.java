package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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

	public default String toTabularView(boolean sorted) {
		StringBuilder sb = new StringBuilder();
		List<Row> rowList = new ArrayList<Row>();
		sb.append(String.format("| %-15s |\n", name()));
		StringBuilder seperator = new StringBuilder();
		boolean isNumber;
		
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
				
				try {
					double value = Double.parseDouble(row.key());
					sb.append(String.format("| %15.2f ", value));
				} catch (NumberFormatException e) {
					String str = row.key();
					if(str.length() > 15) {
						str = str.substring(0,15);
					}
					sb.append(String.format("| %-15s ", row.key()));
				}
				
				for(int j = 0; j < degree()-1; j++) {
					if(cols.get(j) == null){
						sb.append(String.format("|                 ", cols.get(j)));
					}
					
					try {
						double value = Double.parseDouble((String) cols.get(j));
						sb.append(String.format("| %15.2f ", value));
					} catch (NumberFormatException e) {
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
		/*
		 * for(int j = 0; j < degree()-1; j++) {
					sb.append(String.format("| %-15s ", cols.get(j)));
				}
		 */
		
		String finalView = sb.toString();
		return finalView;
	}
}
