package tables;

import java.util.Iterator;
import java.util.List;

import model.DataTable;
import model.Row;

public class HashTable implements DataTable {
	/*
	 * TODO: For Modules 2 and 3, finish this stub.
	 */
	// Private Fields
	private Row[] rows;
	private String name;
	private List<String> columns;
	private int degree;
	private int size;
	private int capacity;
	private int fingerPrint;
	private final int initialCapacity = 521;

	public HashTable(String name, List<String> columns) {
		this.name  = name;
		this.columns = List.copyOf(columns);
		degree = columns.size();
		clear();
	}

	@Override
	public void clear() {
		capacity = initialCapacity;
		rows = new Row[capacity];
		size = 0;
		fingerPrint = 0;
	}

	private int hashFunction(String key) {
		String salt = key + "Jaxon Fielding";
		int hash = 0;
		
		for(int i = 0; i < salt.length(); i++) {
			hash += Math.pow(7, salt.length()-i) * (salt.charAt(i));
		}
		
		return Math.floorMod(hash, capacity);
	}

	@Override
	public List<Object> put(String key, List<Object> fields) {
		if(fields.size()+1 != degree) {
			throw new IllegalArgumentException();
		}
		
		int i = hashFunction(key);
		int origIndex = i;
		boolean fullLoop = true;
		Row make = new Row(key, List.copyOf(fields));
		
		while(rows[i] != null) {
			// Hit 
			if(rows[i].key().equals(key)) {
				Row temp = rows[i];
				rows[i] = make;
				fingerPrint -= temp.hashCode();
				fingerPrint += make.hashCode();
				return temp.fields();
			}
			// Linear probe
			i++;
			if(i%capacity == origIndex && !fullLoop) {
				throw new IllegalStateException();
			}
			fullLoop = false;
		}
		
		// Miss
		rows[i] = make;
		size++;
		fingerPrint += make.hashCode();
		return null;
		
	}

	@Override
	public List<Object> get(String key) {
		int i = hashFunction(key);
		int origIndex = i;
		boolean fullLoop = true;
		
		while(rows[i] != null) {
			if(rows[i].key().equals(key)) {
				return rows[i].fields();
			}
			i++;
			if(i%capacity == origIndex && !fullLoop) {
				throw new IllegalStateException();
			}
			fullLoop = false;
		}
		
		return null;
		
	}

	@Override
	public List<Object> remove(String key) {
		throw new UnsupportedOperationException();
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
	public int capacity() {
		return capacity;
	}

	@Override
	public int hashCode() {
		return fingerPrint;
	}

	@Override
	public boolean equals(Object obj) {
		
		if(obj instanceof Object[][] && obj.hashCode() == this.fingerPrint) {
			return true;
		} else {
			return false;
		}
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
		return name;
	}

	@Override
	public List<String> columns() {
		return columns;
	}

	@Override
	public String toString() {
		throw new UnsupportedOperationException();
	}
}
