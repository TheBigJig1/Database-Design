package model;

import java.util.List;
import java.util.Collections;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public record Row(String key, List<Object> fields) implements Comparable<Row>, Serializable{
	public Row {
		if (fields != null){
			fields = Collections.unmodifiableList(new ArrayList<>(fields));
		}
	}
	
	@Override
	public String toString() {
		return key + ":" + fields;
	}
	
	@Override
	public int hashCode() {
		return (key.hashCode() ^ fields.hashCode());
	}

	@Override
	public int compareTo(Row param) {
		return key.compareTo(param.key());
	}

	public byte[] getBytes() {

		List<Object> rowList = List.of(key, fields);
		int totalBytes = calculateTotalBytes(rowList);

		ByteBuffer buffer = ByteBuffer.allocate(totalBytes);

		for (Object obj : rowList) {
			if (obj instanceof String) {
				String str = (String) obj;
				buffer.put((byte) str.length());
				buffer.put(str.getBytes());
			} else if (obj instanceof Integer) {
				buffer.put((byte) -1);
				buffer.putInt((Integer) obj);
			} else if (obj instanceof Double) {
				buffer.put((byte) -2);
				buffer.putDouble((Double) obj);
			} else if (obj instanceof Boolean) {
				buffer.put((byte) (((Boolean) obj) ? -3 : -4));
			} else if (obj == null) {
				buffer.put((byte) -5);
			}
		}
	
		return buffer.array();
	}

	public static Row fromBytes(byte[] bytes) {

		List<Object> rowList = new ArrayList<Object>();
		ByteBuffer buffer = ByteBuffer.wrap(bytes);

		while (buffer.hasRemaining()) {
			byte tag = buffer.get();
	
			if (tag >= 0) {
				byte[] strBytes = new byte[tag];
				buffer.get(strBytes);
				rowList.add(new String(strBytes));
			} else if (tag == -1) {
				rowList.add(buffer.getInt());
			} else if (tag == -2) {
				rowList.add(buffer.getDouble());
			} else if (tag == -3) {
				rowList.add(true);
			} else if (tag == -4) {
				rowList.add(false);
			} else if (tag == -5) {
				rowList.add(null);
			}
		}
	
		String key = (String) rowList.remove(0);
		return new Row(key, rowList);
	}

	public int calculateTotalBytes(List<Object> target){

		int sum = 0;

		for(Object obj : target){
			if (obj instanceof String) {
				sum += 1 + ((String) obj).length();
			} else if (obj instanceof Integer) {
				sum += 1 + 4;
			} else if (obj instanceof Double) {
				sum += 1 + 8;
			} else if (obj instanceof Boolean || obj == null) {
				sum += 1;
			} else {
				sum += 0;
			}
		}

		return sum;
	}
}