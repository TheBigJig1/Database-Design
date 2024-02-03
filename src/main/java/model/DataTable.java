package model;

public interface DataTable extends Table {
	public int capacity();

	public default boolean isFull() {
		
		if(this.size() == this.capacity()) {
			return true;
		} else {
			return false;
		}
	}

	public default double loadFactor() {
		
		return (double)this.size()/this.capacity();
		
	}
}
