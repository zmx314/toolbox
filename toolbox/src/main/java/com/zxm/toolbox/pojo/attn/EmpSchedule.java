package com.zxm.toolbox.pojo.attn;

import com.zxm.toolbox.exception.attn.DuplicatePostException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EmpSchedule {
	private Employee employee;
	private List<String> schedule;
	// 出勤天数
	private double dutyDays;
	//出勤小时数
	private double dutyTime;
	//其中算加班费小时数
	private double extraTime;
	//其中核算补休小时数
	private double compensatoryTime;
	//休息天数
	private double offDays;
	//休息天数中工龄假天数
	private double annualLeaveDays;
	//休息天数中补休的天数
	private double compensatoryDays;
	//休息天数中育儿假天数
	private double parentalLeaveDays;
	//休息天数中护理假天数
	private double nursingLeaveDays;
	//休息天数中产假天数
	private double maternityDays;
	//休息天数中哺乳假天数
	private double breastfeedingDays;
	//累计剩余补休的小时数
	private double compensatoryLeft;
	//累计剩余工龄假天数
	private double annualLeaveLeft;
	//累计剩余育儿假天数
	private double parentalLeaveLeft;
	//累计剩余护理假天数
	private double nursingLeaveLeft;

	public EmpSchedule() {
		super();
		this.employee = new Employee();
		this.schedule = new ArrayList<>();
	}

	public EmpSchedule(Employee employee) {
		super();
		this.employee = employee;
		this.schedule = new ArrayList<>();
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public List<String> getSchedule() {
		return schedule;
	}

	public void setSchedule(List<String> schedule) {
		this.schedule = schedule;
	}

	public double getDutyDays() {
		return dutyDays;
	}

	public void setDutyDays(double dutyDays) {
		this.dutyDays = dutyDays;
	}

	public double getDutyTime() {
		return dutyTime;
	}

	public void setDutyTime(double dutyTime) {
		this.dutyTime = dutyTime;
	}

	public double getExtraTime() {
		return extraTime;
	}

	public void setExtraTime(double extraTime) {
		this.extraTime = extraTime;
	}

	public double getCompensatoryTime() {
		return compensatoryTime;
	}

	public void setCompensatoryTime(double compensatoryTime) {
		this.compensatoryTime = compensatoryTime;
	}

	public double getOffDays() {
		return offDays;
	}

	public void setOffDays(double offDays) {
		this.offDays = offDays;
	}

	public double getAnnualLeaveDays() {
		return annualLeaveDays;
	}

	public void setAnnualLeaveDays(double annualLeaveDays) {
		this.annualLeaveDays = annualLeaveDays;
	}

	public double getCompensatoryDays() {
		return compensatoryDays;
	}

	public void setCompensatoryDays(double compensatoryDays) {
		this.compensatoryDays = compensatoryDays;
	}

	public double getMaternityDays() {
		return maternityDays;
	}

	public void setMaternityDays(double maternityDays) {
		this.maternityDays = maternityDays;
	}

	public double getBreastfeedingDays() {
		return breastfeedingDays;
	}

	public void setBreastfeedingDays(double breastfeedingDays) {
		this.breastfeedingDays = breastfeedingDays;
	}

	public double getCompensatoryLeft() {
		return compensatoryLeft;
	}

	public void setCompensatoryLeft(double compensatoryLeft) {
		this.compensatoryLeft = compensatoryLeft;
	}

	public double getAnnualLeaveLeft() {
		return annualLeaveLeft;
	}

	public void setAnnualLeaveLeft(double annualLeaveLeft) {
		this.annualLeaveLeft = annualLeaveLeft;
	}

	public double getParentalLeaveDays() {
		return parentalLeaveDays;
	}

	public void setParentalLeaveDays(double parentalLeaveDays) {
		this.parentalLeaveDays = parentalLeaveDays;
	}

	public double getNursingLeaveDays() {
		return nursingLeaveDays;
	}

	public void setNursingLeaveDays(double nursingLeaveDays) {
		this.nursingLeaveDays = nursingLeaveDays;
	}

	public double getParentalLeaveLeft() {
		return parentalLeaveLeft;
	}

	public void setParentalLeaveLeft(double parentalLeaveLeft) {
		this.parentalLeaveLeft = parentalLeaveLeft;
	}

	public double getNursingLeaveLeft() {
		return nursingLeaveLeft;
	}

	public void setNursingLeaveLeft(double nursingLeaveLeft) {
		this.nursingLeaveLeft = nursingLeaveLeft;
	}

	/**
	 * 统计员工的出勤天数
	 *
	 * @param postList 所有岗位
	 */
	public void count(List<Post> postList) throws DuplicatePostException {
		Map<String, Post> map = new HashMap<>();
		for (Post p : postList){
			if (!map.containsKey(p.getPostName())){
				map.put(p.getPostName(), p);
			} else {
				if(p.equals(map.get(p.getPostName())))
					continue;
				else
					throw new DuplicatePostException(p, map.get(p.getPostName()));
			}
		}
		Map<String, Post> postMap = postList.stream().collect(Collectors.toMap(Post::getPostName, (post) -> post));
		dutyDays = 0;
		for (String s : schedule) {
			if (postMap.get(s).getIsDuty())
				this.dutyDays += 1;
			else
				this.offDays += 1;
			switch (s) {
				case "工":
					this.annualLeaveDays += 1;
					break;
				case "补":
					this.compensatoryDays += 1;
					break;
				case "育":
					this.parentalLeaveDays += 1;
					break;
				case "护":
					this.nursingLeaveDays += 1;
					break;
				case "产假":
					this.maternityDays += 1;
					break;
				case "哺":
					this.breastfeedingDays += 1;
					break;
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(annualLeaveDays);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(annualLeaveLeft);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(breastfeedingDays);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(compensatoryDays);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(compensatoryLeft);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(compensatoryTime);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(dutyDays);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(dutyTime);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((employee == null) ? 0 : employee.hashCode());
		temp = Double.doubleToLongBits(extraTime);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(maternityDays);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(offDays);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((schedule == null) ? 0 : schedule.hashCode());
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
		EmpSchedule other = (EmpSchedule) obj;
		if (Double.doubleToLongBits(annualLeaveDays) != Double.doubleToLongBits(other.annualLeaveDays))
			return false;
		if (Double.doubleToLongBits(annualLeaveLeft) != Double.doubleToLongBits(other.annualLeaveLeft))
			return false;
		if (Double.doubleToLongBits(breastfeedingDays) != Double.doubleToLongBits(other.breastfeedingDays))
			return false;
		if (Double.doubleToLongBits(compensatoryDays) != Double.doubleToLongBits(other.compensatoryDays))
			return false;
		if (Double.doubleToLongBits(compensatoryLeft) != Double.doubleToLongBits(other.compensatoryLeft))
			return false;
		if (Double.doubleToLongBits(compensatoryTime) != Double.doubleToLongBits(other.compensatoryTime))
			return false;
		if (Double.doubleToLongBits(dutyDays) != Double.doubleToLongBits(other.dutyDays))
			return false;
		if (Double.doubleToLongBits(dutyTime) != Double.doubleToLongBits(other.dutyTime))
			return false;
		if (employee == null) {
			if (other.employee != null)
				return false;
		} else if (!employee.equals(other.employee))
			return false;
		if (Double.doubleToLongBits(extraTime) != Double.doubleToLongBits(other.extraTime))
			return false;
		if (Double.doubleToLongBits(maternityDays) != Double.doubleToLongBits(other.maternityDays))
			return false;
		if (Double.doubleToLongBits(offDays) != Double.doubleToLongBits(other.offDays))
			return false;
		if (schedule == null) {
			return other.schedule == null;
		} else return schedule.equals(other.schedule);
	}

	@Override
	public String toString() {
		return "[ EmpSchedule:\n"
				+ "employee=\t" + employee + "\n"
				+ "schedule=\t" + schedule + "\n"
				+ "dutyDays=\t" + dutyDays + "\n"
				+ "dutyTime=\t" + dutyTime + "\n"
				+ "extraTime=\t" + extraTime + "\n"
				+ "compensatoryTime=\t" + compensatoryTime + "\n"
				+ "offDays=\t" + offDays + "\n"
				+ "annualLeaveDays=\t" + annualLeaveDays + "\n"
				+ "compensatoryDays=\t" + compensatoryDays + "\n"
				+ "maternityDays=\t" + maternityDays + "\n"
				+ "breastfeedingDays=\t" + breastfeedingDays + "\n"
				+ "compensatoryLeft=\t" + compensatoryLeft + "\n"
				+ "annualLeaveLeft=\t" + annualLeaveLeft + "\n"
				+ "]";
	}

}
