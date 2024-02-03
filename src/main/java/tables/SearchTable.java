package tables;

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
		capacity = initialCapacity;
		rows = new Row[capacity];
	}

	@Override
	public List<Object> put(String key, List<Object> fields) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Object> get(String key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Object> remove(String key) {
		throw new UnsupportedOperationException();
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

