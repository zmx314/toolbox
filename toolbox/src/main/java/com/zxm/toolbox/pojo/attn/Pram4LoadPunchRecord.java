package com.zxm.toolbox.pojo.attn;

public class Pram4LoadPunchRecord {
	private int year;
	private int month;
	private int nameColIndex;
	private int dateTimeColIndex;

	public Pram4LoadPunchRecord() {
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getNameColIndex() {
		return nameColIndex;
	}

	public void setNameColIndex(int nameColIndex) {
		this.nameColIndex = nameColIndex;
	}

	public int getDateTimeColIndex() {
		return dateTimeColIndex;
	}

	public void setDateTimeColIndex(int dateTimeColIndex) {
		this.dateTimeColIndex = dateTimeColIndex;
	}
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Pram4LoadPunchRecord that = (Pram4LoadPunchRecord) o;

		if (year != that.year) return false;
		if (month != that.month) return false;
		if (nameColIndex != that.nameColIndex) return false;
		return dateTimeColIndex == that.dateTimeColIndex;
	}

	@Override
	public int hashCode() {
		int result = year;
		result = 31 * result + month;
		result = 31 * result + nameColIndex;
		result = 31 * result + dateTimeColIndex;
		return result;
	}

	@Override
	public String toString() {
		return "Pram4LoadPunchRecord{" +
				"year=" + year +
				", month=" + month +
				", nameColIndex=" + nameColIndex +
				", dateTimeColIndex=" + dateTimeColIndex +
				'}';
	}
}
