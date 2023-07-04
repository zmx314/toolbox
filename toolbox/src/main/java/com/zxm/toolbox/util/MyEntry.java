package com.zxm.toolbox.util;

import java.util.Comparator;

public class MyEntry implements Comparator<MyEntry>{
	private int key;
	private int value;

	public MyEntry(int key, int value) {
		super();
		this.key = key;
		this.value = value;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + key;
		result = prime * result + value;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MyEntry other = (MyEntry) obj;
		if (key != other.key)
			return false;
		if (value != other.value)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MyEntry [key=" + key + ", value=" + value + "]";
	}

	@Override
	public int compare(MyEntry o1, MyEntry o2) {
		if (o1.getKey() > o2.getKey())
			return 1;
		else if (o1.getKey() == o2.getKey())
			return 0;
		else
			return -1;
	}

}
