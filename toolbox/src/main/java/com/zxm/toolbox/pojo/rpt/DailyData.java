package com.zxm.toolbox.pojo.rpt;

import java.io.File;

import com.zxm.toolbox.util.PoiUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;

public class DailyData {
	private String date;
	private double departShip;
	private double departPax;
	private double arriveShip;
	private double arrivePax;

	public DailyData(File file) {
		Sheet sheet;
		CellAddress address;
		Cell cell;
		sheet = PoiUtils.loadSheet(file);
		
		address = new CellAddress("A2");
		cell = sheet.getRow(address.getRow()).getCell(address.getColumn());
		String temp = cell.getStringCellValue();
		temp = temp.replace("日期：", "");
		this.setDate(temp);
		
		
		address = new CellAddress("E5");
		cell = sheet.getRow(address.getRow()).getCell(address.getColumn());
		this.setArriveShip(cell.getNumericCellValue());
		
		address = new CellAddress("F5");
		cell = sheet.getRow(address.getRow()).getCell(address.getColumn());
		this.setArrivePax(cell.getNumericCellValue());
		
		address = new CellAddress("I5");
		cell = sheet.getRow(address.getRow()).getCell(address.getColumn());
		this.setDepartShip(cell.getNumericCellValue());
		
		address = new CellAddress("J5");
		cell = sheet.getRow(address.getRow()).getCell(address.getColumn());
		this.setDepartPax(cell.getNumericCellValue());
	}
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getDepartShip() {
		return departShip;
	}

	public void setDepartShip(double departShip) {
		this.departShip = departShip;
	}

	public double getDepartPax() {
		return departPax;
	}

	public void setDepartPax(double departPax) {
		this.departPax = departPax;
	}

	public double getArriveShip() {
		return arriveShip;
	}

	public void setArriveShip(double arriveShip) {
		this.arriveShip = arriveShip;
	}

	public double getArrivePax() {
		return arrivePax;
	}

	public void setArrivePax(double arrivePax) {
		this.arrivePax = arrivePax;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(arrivePax);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(arriveShip);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		temp = Double.doubleToLongBits(departPax);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(departShip);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		DailyData other = (DailyData) obj;
		if (Double.doubleToLongBits(arrivePax) != Double.doubleToLongBits(other.arrivePax))
			return false;
		if (Double.doubleToLongBits(arriveShip) != Double.doubleToLongBits(other.arriveShip))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (Double.doubleToLongBits(departPax) != Double.doubleToLongBits(other.departPax))
			return false;
		return Double.doubleToLongBits(departShip) == Double.doubleToLongBits(other.departShip);
	}
	@Override
	public String toString() {
		return "DailyData [date=" + date + ", departShip=" + departShip + ", departPax=" + departPax
				+ ", arriveShip=" + arriveShip + ", arrivePax=" + arrivePax + "]";
	}

}
