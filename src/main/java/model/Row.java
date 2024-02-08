package model;

import java.util.List;

public record Row(String key, List<Object> fields) {

	// 1.E complete
	@Override
	public String toString() {
		return key + ":" + fields;
	}
	
	// 3.N complete
	@Override
	public int hashCode() {
        
		return (this.key.hashCode() ^ this.fields.hashCode());
	}
	
}