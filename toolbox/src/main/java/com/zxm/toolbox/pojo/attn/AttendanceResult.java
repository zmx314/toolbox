package com.zxm.toolbox.pojo.attn;

import java.util.List;

public class AttendanceResult {
	private Employee employee;
	private List<EmpDailyResult> pppdResults;
	
	public AttendanceResult() {
		super();
	}

	public AttendanceResult(Employee employee) {
		super();
		this.employee = employee;
	}
	
	public AttendanceResult(Employee employee, List<EmpDailyResult> pppdResults) {
		super();
		this.employee = employee;
		this.pppdResults = pppdResults;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public List<EmpDailyResult> getPppdResults() {
		return pppdResults;
	}

	public void setPppdResults(List<EmpDailyResult> pppdResults) {
		this.pppdResults = pppdResults;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((employee == null) ? 0 : employee.hashCode());
		result = prime * result + ((pppdResults == null) ? 0 : pppdResults.hashCode());
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
		AttendanceResult other = (AttendanceResult) obj;
		if (employee == null) {
			if (other.employee != null)
				return false;
		} else if (!employee.equals(other.employee))
			return false;
		if (pppdResults == null) {
			return other.pppdResults == null;
		} else return pppdResults.equals(other.pppdResults);
	}

	@Override
	public String toString() {
		System.out.println(this.getEmployee().getName());
		for(EmpDailyResult ps:this.getPppdResults()) {
			System.out.println(ps.toString());
		}
		return null;
	}
	
}
