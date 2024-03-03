package tables;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import model.DataTable;
import model.Row;


public class SearchTable implements DataTable {
	
	
	// 1.A & 3.K complete
	private Row[] rows;
	private String name;
	private List<String> columns;
	private int degree; // Key + columns
	private int size; // Current number of rows
	private int capacity; // Max possible number of rows
	private final int initialCapacity = 16; // Initial Capacity
	private int fingerPrint; // Set fingerprint of table

	public SearchTable(String name, List<String> columns) {
		
		// 1.B complete
		this.name = name;
		this.columns = List.copyOf(columns);
		degree = columns.size();
		clear();

	}

	// 1.C & 3.L complete
	@Override
	public void clear() {
		capacity = initialCapacity;
		rows = new Row[capacity];
		size = 0;
		fingerPrint = 0;
	}

	// 2.G complete
	@Override
	public List<Object> put(String key, List<Object> fields) {
		// Throw illegal argument if sizes do not match
		if(1+fields.size() != degree) {
			throw new IllegalArgumentException("Row size does not match");
		}
		
		// Create new row
		Row make = new Row(key, fields);
		
		// Hit
		for(int i = 0; i < size; i++) {
			if(rows[i].key().equals(key)) {
				Row temp = rows[i];
				rows[i] = make;
				fingerPrint -= temp.hashCode();
				fingerPrint += make.hashCode();
				return temp.fields();
			}
		}
		
		// Miss
		if (size < capacity) {
            rows[size] = make;
        } else {
        	capacity = capacity * 2;
            rows = Arrays.copyOf(rows, capacity);
            rows[size] = make;
        }
		size++;
        fingerPrint += make.hashCode();
        return null;
		
	}

	// 2.H complete
	@Override
	public List<Object> get(String key) {
		// Linear search for key
		for(int i = 0; i < size; i++) {
			// Hit
			if(rows[i].key().equals(key)) {
				return rows[i].fields();
			}
		}
		
		// Miss
		return null;
	}

	// 2.I complete
	@Override
	public List<Object> remove(String key) {
		// Hit
		for(int i = 0; i < size; i++) {
			if(rows[i] != null && rows[i].key().equals(key)) {
				Row temp = rows[i];
				rows[i] = rows[size-1];
				rows[size-1] = null;
				fingerPrint -= temp.hashCode();
				size--;
				return temp.fields();
			}
		}
		
		// Miss
		return null;
		
	}

	@Override
	public int degree() {
		return degree;
	}

	@Override
	public int size() {
		return size;
	}

	
	@Override
	public boolean isEmpty() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int capacity() {
		return capacity;
	}

	/*
	@Override
	public double loadFactor() {
		throw new UnsupportedOperationException();
	}*/

	@Override
	public int hashCode() {
		return fingerPrint;
	}

	@Override
	public boolean equals(Object obj) {
		// Checks if obj is a table of any kind
		if(obj instanceof Object[][]) {
			if(obj.hashCode() == this.hashCode()){
				return true;
			}
		}
		
		return false;
	}

	@Override
	public Iterator<Row> iterator() {
		return new Iterator<>() {
			private int currentIndex = 0;

			@Override
			public boolean hasNext() {
				return currentIndex < size;
			}

			@Override
			public Row next() {
				if(hasNext() == false) {
					throw new NoSuchElementException();
				} else {
					currentIndex++;
					return rows[currentIndex-1];
				}
			}
		};
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public List<String> columns() {
		return columns;
	}

	@Override
	public String toString() {
		return toTabularView(true);
	}

}

