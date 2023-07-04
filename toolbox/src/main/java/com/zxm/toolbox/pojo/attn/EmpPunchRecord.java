package com.zxm.toolbox.pojo.attn;

import java.util.Calendar;
import java.util.List;

public class EmpPunchRecord {
	private Employee employee;
	private List<Calendar> clockIO;
	
	public EmpPunchRecord(Employee employee) {
		super();
		this.employee = employee;
	}
	public EmpPunchRecord(Employee employee, List<Calendar> clockIO) {
		super();
		this.employee = employee;
		this.clockIO = clockIO;
	}
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public List<Calendar> getClockIO() {
		return clockIO;
	}
	public void setClockIO(List<Calendar> clockIO) {
		this.clockIO = clockIO;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clockIO == null) ? 0 : clockIO.hashCode());
		result = prime * result + ((employee == null) ? 0 : employee.hashCode());
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
		EmpPunchRecord other = (EmpPunchRecord) obj;
		if (clockIO == null) {
			if (other.clockIO != null)
				return false;
		} else if (!clockIO.equals(other.clockIO))
			return false;
		if (employee == null) {
			return other.employee == null;
		} else return employee.equals(other.employee);
	}
	@Override
	public String toString() {
		return "ActualAttendance [employee=" + employee + ", clockIO=" + clockIO + "]";
	}
	
}
