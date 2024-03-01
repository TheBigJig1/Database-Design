package tables;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import model.DataTable;
import model.Row;
import model.Table;

public class HashTable implements DataTable {
	
	// Private Fields
	private Row[] rows;
	private String name;
	private List<String> columns;
	private int degree;
	private int size;
	private int capacity;
	private int fingerPrint;
	private final static int initialCapacity = 521;

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

	private int hashFunction2(String key) {
		String salt = key + "Jaxon Fielding";
		int hash = 0;
		
		for(int i = 0; i < salt.length(); i++) {
			hash += Math.pow(7, salt.length()-i) * (salt.charAt(i));
		}
		
		return 1 + Math.floorMod(hash, capacity-1);
	}

	private int hashFunction1(String key){
		String salt = "Jaxon Fielding";
		
		try{ 
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(salt.getBytes());
			md.update(key.getBytes());

			md.digest();
			BigInteger bigNum = new BigInteger(md.digest());

			return Math.floorMod(bigNum.intValue(), capacity);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("SHA-256 not found");
		}
	}

	@Override
	public List<Object> put(String key, List<Object> fields) {
	    if (fields.size() + 1 != degree) {
	        throw new IllegalArgumentException("Incorrect degree.");
	    }
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	    int i = hashFunction2(key); 
	    int origIndex = 5*i;
	    
	    Row make = new Row(key, Collections.unmodifiableList(fields));

	    do {
	    	// Check if the slot is empty or has the key we're looking for
	    	if (rows[i] == null || rows[i].key().equals(key)) {
	    		// Hit or empty slot found
	    		Row Old = rows[i];
	    		rows[i] = make;
	    		if (Old != null) { 
	    			// Hit
	    			fingerPrint -= Old.hashCode();
	    			fingerPrint += make.hashCode();
	    			return Old.fields();
	    		} else {
	    			// Miss
	    			size++;
	    			fingerPrint += make.hashCode();
	    			return null;
	            }
	        }
	        // Linear probing
	        i = (i + 1) % capacity;
	    }  while (i != origIndex);

	    throw new IllegalStateException("HashTable is full");
	}

	@Override
	public List<Object> get(String key) {
		int i = hashFunction2(key);
		int origIndex = i;
		boolean fullLoop = false;
		
		do {
			if (rows[i] != null && rows[i].key().equals(key)) {
				return rows[i].fields(); 
			}
			i = (i + 1) % capacity;
			if (i == origIndex && fullLoop) {
				throw new IllegalStateException("Full loop detected without finding the key");
			}
	    } while (i != origIndex);
		
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
		if(obj instanceof Table && obj.hashCode() == this.fingerPrint) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Iterator<Row> iterator() {
		return new Iterator<>() {
			private int currentIndex = 0;

			@Override
			public boolean hasNext() {
				while(currentIndex < capacity && rows[currentIndex] == null) {
					currentIndex++;
				}
				return currentIndex < capacity;
				
			}

			@Override
			public Row next() {
				if(hasNext() == false) {
					throw new NoSuchElementException();
				} else {
					int oldIndex = currentIndex;
					currentIndex++;
					
					while(currentIndex < capacity && rows[currentIndex] == null) {
						currentIndex++;
					}
					
					return rows[oldIndex];
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
		return toTabularView(false);
	}
}
