package model;

import java.util.List;
import java.util.Collections;
import java.io.Serializable;
import java.util.ArrayList;

public record Row(String key, List<Object> fields) implements Comparable<Row>, Serializable{
	public Row {
		if (fields != null){
			fields = Collections.unmodifiableList(new ArrayList<>(fields));
		}
	}
	
	// 1.E complete
	@Override
	public String toString() {
		return key + ":" + fields;
	}
	
	// 3.N complete
	@Override
	public int hashCode() {
		return (key.hashCode() ^ fields.hashCode());
	}

	@Override
	public int compareTo(Row param) {
		return key.compareTo(param.key());
	}
}