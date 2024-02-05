package tables;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import model.DataTable;
import model.Row;


public class SearchTable implements DataTable {
	/*
	 * TODO: For Module 1, finish this stub.
	 */
	
	// 1.A complete
	private Row[] rows;
	private String name;
	private List<String> columns;
	private int degree; // Key + columns
	private int size; // Current number of rows
	private int capacity; // Max possible number of rows
	private static int initialCapacity = 16; // Initial Capacity
	private int fingerPrint; // Set fingerprint of table

	public SearchTable(String name, List<String> columns) {
		
		// 1.B complete
		this.name = name;
		this.columns = List.copyOf(columns);
		this.degree = columns.size();
		clear();

	}

	@Override
	public void clear() {
		// 1.C complete
		this.capacity = initialCapacity;
		rows = new Row[capacity];
		this.size = 0;
		this.fingerPrint = 0;
	}

	// 2.G complete
	@Override
	public List<Object> put(String key, List<Object> fields) {
		// Throw illegal argument if sizes do not match
		if(1+fields.size() != degree) {
			throw new IllegalArgumentException();
		}
		
		// Create new row
		Row next = new Row(key, fields);
		
		// Hit
		for(int i = 0; i < size; i++) {
			if(rows[i].key().equals(key)) {
				Row temp = rows[i];
				rows[i] = next;
				return temp.fields();
			}
		}
		
		// Miss
		if (size < capacity) {
            rows[size] = next;
        } else {
        	capacity = capacity * 2;
            rows = Arrays.copyOf(rows, capacity);
            rows[size] = next;
        }
        size++;
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
			if(rows[i].key().equals(key)) {
				Row temp = rows[i];
				rows[i] = rows[size];
				rows[size] = null;
				return temp.fields();
			}
		}
		
		// Miss
		return null;
		
	}

	@Override
	public int degree() {
		return this.degree;
	}

	@Override
	public int size() {
		return this.size;
	}

	@Override
	public boolean isEmpty() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int capacity() {
		return this.capacity;
	}

	@Override
	public double loadFactor() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int hashCode() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean equals(Object obj) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<Row> iterator() {
		return new Iterator<>() {


			@Override
			public boolean hasNext() {
				throw new UnsupportedOperationException();
			}

			@Override
			public Row next() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public String name() {
		return this.name;
	}

	@Override
	public List<String> columns() {
		return this.columns;
	}

	@Override
	public String toString() {
		throw new UnsupportedOperationException();
	}

}

