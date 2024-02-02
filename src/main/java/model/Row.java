package model;

import java.util.List;

// Implement a custom version of the toString method

public record Row(String key, List<Object> fields) {

	// 1.E complete
	@Override
	public String toString() {
		return key + ":" + fields;
	}
	
}