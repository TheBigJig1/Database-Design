package tables;

// Bring up why my pass rate doesnt show up in the console 
// 

// Turn in information is around 43:17 on module 0 Q 4 video
// To tag, go to history and right click on most recent commit
// To submit everything.  Remotes -> origin -> Push: in popup window verify new tag and commit 
// On ecampus, submit the 'link of the main page of my origin repository' as a text submission
// Include screenshot of: unit test pass rate, IDE with visible full history of project, package explorer, github commits and tags pages

import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;

import model.Row;
import model.Table;

public class LookupTable implements Table {
	/*
	 * TODO: For Module 0, test and debug
	 * the errors in this implementation.
	 */

	private Row[] rows;
	private String name;
	private List<String> columns;
	private int degree;

	// TODO: This constructor has 2 initialization errors.
	// I think columns is still one of them
	public LookupTable(String name, List<String> columns) {
		this.name = name;
		this.columns = columns;
		clear();
	}

	@Override
	public void clear() {
		rows = new Row[52];
	}

	// TODO: This helper method has 1 logic error.
	// Something is wrong with my c - 'A' call I think and im not totally sure what
	private int indexOf(String key) {
		if (key.length() < 1) {
			throw new IllegalArgumentException("Key must be at least 1 character");
		}

		char c = key.charAt(0);
		if (c >= 'a' && c <= 'z') {
			return c - 'a';
		} else if (c >= 'A' && c <= 'Z') {
			return c - 'A';
		} else {
			throw new IllegalArgumentException("Key must start with a lowercase or uppercase letter");
		}
	}

	@Override
	public List<Object> put(String key, List<Object> fields) {
		
		// I believe this is the missing guard condition
		
		if(fields == null) {
			throw new IllegalArgumentException("Fields cannot be null");
		}
		if (1 + fields.size() < degree) {
			throw new IllegalArgumentException("Row is too narrow");
		}

		int i = indexOf(key);

		Row make = new Row(key, fields);

		if (rows[i] != null) {
			Row old = rows[i];
			rows[i] = make;
			return old.fields();
		}
		
		rows[i] = make;
		return null;
	}

	@Override
	public List<Object> get(String key) {
		int i = indexOf(key);

		// I believe this code has fixed the error
		
		if (rows[i] != null) {
			return null;
		}
		
		return rows[i].fields(); //assumes a hit.  Never checks for a miss.  Needs similar logic from previous method
	}

	// TODO: This method has 1 result error.
	// Compare to "put" method.  Put method creates temp of old array before changing so it can return old.
	// Remove needs this implemenation
	// In unit test, will show up as returning new value instead of old value
	@Override
	public List<Object> remove(String key) {
		int i = indexOf(key);

		if (rows[i] != null) {
			rows[i] = null;
			return rows[i].fields();
		}

		return null;
	}

	@Override
	public int degree() {
		int deg = 0;
		
		for(String col: columns) {
			deg++;
		}
		
		return deg;
	}

	// TODO: This method has 1 logic error.
	@Override
	public int size() {
		int size = 0;
		for (Row row: rows) {
			if(row != null) {
				size++;
			}
		}
		return size;
	}

	// TODO: This method has 1 assignment error.
	@Override
	public int hashCode() {
		int fingerprint = 0;
		for (Row row: rows) {
			if (row != null) {
				fingerprint += row.key().hashCode() ^ row.fields().hashCode();
			}
		}
		return fingerprint;
	}

	@Override
	public boolean equals(Object obj) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<Row> iterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String name() {
		return name;
	}

	// TODO: This method has 1 result error.
	@Override
	public List<String> columns() {
		return null;
	}

	@Override
	public String toString() {
		var sj = new StringJoiner(", ", name() + "<" + columns().get(0) + "=" + columns().subList(1, degree) + ">{", "}");
		for (var row: rows) {
			if (row != null) {
				sj.add(row.key() + "=" + row.fields());
			}
		}
		return sj.toString();
	}
}