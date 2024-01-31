package tables;

import java.util.Iterator;
import java.util.List;

import model.DataTable;
import model.Row;

// Need to ask how I sync my github with the template
// For example, all the new src/test/config or Harness or Module1

public class SearchTable implements DataTable {
	/*
	 * TODO: For Module 1, finish this stub.
	 */

	public SearchTable(String name, List<String> columns) {
		// To make an unmodifiable copy of the columns, the method 
		// i'm looking for is in the List class.  As in: List.xxxx
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
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
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isEmpty() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int capacity() {
		throw new UnsupportedOperationException();
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
		throw new UnsupportedOperationException();
	}

	@Override
	public List<String> columns() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		throw new UnsupportedOperationException();
	}
}