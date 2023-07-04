package com.zxm.toolbox.pojo.gt;

public class Keyword {
	private int id;
	private String key;
	private String fullName;

	public Keyword() {
		super();
	}

	public Keyword(int id,String name, String fullName) {
		super();
		this.id = id;
		this.key = name;
		this.fullName = fullName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Keyword keyword = (Keyword) o;

		if (id != keyword.id) return false;
		if (key != null ? !key.equals(keyword.key) : keyword.key != null) return false;
		return fullName != null ? fullName.equals(keyword.fullName) : keyword.fullName == null;
	}

	@Override
	public int hashCode() {
		int result = id;
		result = 31 * result + (key != null ? key.hashCode() : 0);
		result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Keyword{" +
				"id=" + id +
				", key='" + key + '\'' +
				", fullName='" + fullName + '\'' +
				'}';
	}
}
