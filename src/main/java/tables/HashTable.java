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
	private int contamination;
	private int capacityCounter;
	private final static int[] primes = {97, 199, 457, 1021, 2053, 4133, 9311};
	private final static int initialCapacity = 41;
	private final static double loadFactorBound = 0.75;
	private final static Row TOMBSTONE = new Row(null, null);

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
		contamination = 0;
		capacityCounter = 0;
	}

	private int hashFunction2(String key) {
		String salt = key + "Jaxon Fielding";
		long hash = 0;
		
		for(int i = 0; i < salt.length(); i++) {
			hash += Math.pow(3, salt.length()-i) * (salt.charAt(i));
		}
		
		return 1 + Math.floorMod(hash, capacity-1);
	}

	private int hashFunction1(String key){
		String salt = "Jaxon Fielding";
		
		try{ 
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(salt.getBytes());
			md.update(key.getBytes());

			BigInteger bigNum = new BigInteger(md.digest());

			return (Math.floorMod(bigNum.intValue(), capacity));

		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("SHA-256 not found");
		}
	}

	@Override
	public List<Object> put(String key, List<Object> fields) {
	    if (fields.size() + 1 != degree) {
	        throw new IllegalArgumentException("Incorrect degree.");
	    }
		
	    int i = hashFunction1(key); 
		int c = hashFunction2(key);
		int tombstoneRecycleIndex = -1;
	    
	    Row make = new Row(key, Collections.unmodifiableList(fields));

		for(int j = 0; j < capacity; j++){

			// Linear Probe
			i = (i + 7*c) % capacity;

			// Check Load Factor
			if(loadFactor() > loadFactorBound){
				rehash(primes[capacityCounter]);
				capacityCounter++;
			}

			// Tombstone Check
			if(rows[i] == TOMBSTONE){
				if(tombstoneRecycleIndex < 0){
					tombstoneRecycleIndex  = i;
				}
				continue;
			}
			
			// Miss
			if(rows[i] == null){
				if(tombstoneRecycleIndex > 0){
					rows[tombstoneRecycleIndex] = make;
					contamination--;
				} else {
					rows[i] = make;
				}
				fingerPrint += make.hashCode();
				size++;
	    		return null;
			}

			// Hit
			if(rows[i].key() != null && rows[i].key().equals(key)){
				Row temp = rows[i];
				if(tombstoneRecycleIndex > 0){
					rows[tombstoneRecycleIndex] = make;
					rows[i] = TOMBSTONE;
				} else {
					rows[i] = make;
				}
				fingerPrint -= temp.hashCode();
				fingerPrint += make.hashCode();
	    		return temp.fields();
			}
		}

	    throw new IllegalStateException("HashTable is full");
	}

	@Override
	public List<Object> get(String key) {
		int i = hashFunction1(key);
		int c = hashFunction2(key);
		int origIndex = i;
		
		do {
			if (rows[i] != null && rows[i].key() != null && rows[i].key().equals(key)) {
				return rows[i].fields(); 
			}
			i = (i + c) % capacity;
			
	    } while (i != origIndex);
		
		return null;
		
	}

	@Override
	public List<Object> remove(String key) {
		int i = hashFunction1(key);
		int c = hashFunction2(key);
		int origIndex = i;
		boolean fullLoop = false;
		
		do {
			if (rows[i] != null && rows[i].key() != null && rows[i].key().equals(key)) {
				Row temp = rows[i];
				rows[i] = TOMBSTONE;
				size--;
				contamination++;
				fingerPrint -= temp.hashCode();
				return temp.fields();
			}
			i = (i + c) % capacity;
			if (i == origIndex && fullLoop) {
				break;
			}
	    } while (i != origIndex);
		
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
			private int i = 0;

			@Override
			public boolean hasNext() {
				while(i < capacity && (rows[i] == TOMBSTONE || rows[i] == null)) {
					i++;
				}
				return i < capacity;
				
			}

			@Override
			public Row next() {
				if(hasNext() == false) {
					throw new NoSuchElementException();
				} else {
					int oldIndex = i;
					i++;
					
					while(i < capacity && (rows[i] == TOMBSTONE || rows[i] == null)) {
						i++;
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
	public double loadFactor() {
		
		return (double)(size()+contamination)/capacity();
		
	}

	private void rehash(int newCapacity) {
		Row[] oldRows = rows;
		capacity = newCapacity;
		rows = new Row[capacity];
		size = 0;
		contamination = 0;
		fingerPrint = 0;

		for (Row row : oldRows) {
			if (row != null && row != TOMBSTONE) {
				put(row.key(), row.fields());
			}
		}
		
		System.gc();

	}

	@Override
	public String toString() {
		return toTabularView(false);
	}
}
