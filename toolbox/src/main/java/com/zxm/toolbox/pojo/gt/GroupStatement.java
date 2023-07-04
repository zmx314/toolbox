package com.zxm.toolbox.pojo.gt;

import java.time.LocalDateTime;
import java.util.Map;

public class GroupStatement {
	// 团体号
	private int groupId;
	// 团体名称
	private String groupName;
	// 航班时间
	private LocalDateTime ferryDeparture;
	// 航线
	private String waterWay;
	// 各票型明细
	private Map<String, TicketDetail> ticketDetail;
	// 出票时间
	private LocalDateTime printTime;
	// 船公司
	private String shippingCompany;

	public GroupStatement() {
		super();
	}

	public GroupStatement(int groupId, String groupName, LocalDateTime ferryDeparture, String waterWay,
						  Map<String, TicketDetail> ticketDetail, LocalDateTime printTime, String shippingCompany) {
		super();
		this.groupId = groupId;
		this.groupName = groupName;
		this.ferryDeparture = ferryDeparture;
		this.waterWay = waterWay;
		this.ticketDetail = ticketDetail;
		this.printTime = printTime;
		this.shippingCompany = shippingCompany;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public LocalDateTime getFerryDeparture() {
		return ferryDeparture;
	}

	public void setFerryDeparture(LocalDateTime ferryDeparture) {
		this.ferryDeparture = ferryDeparture;
	}

	public String getWaterWay() {
		return waterWay;
	}

	public void setWaterWay(String waterWay) {
		this.waterWay = waterWay;
	}

	public Map<String, TicketDetail> getTicketDetail() {
		return ticketDetail;
	}

	public void setTicketDetail(Map<String, TicketDetail> ticketDetail) {
		this.ticketDetail = ticketDetail;
	}

	public LocalDateTime getPrintTime() {
		return printTime;
	}

	public void setPrintTime(LocalDateTime printTime) {
		this.printTime = printTime;
	}

	public String getShippingCompany() {
		return shippingCompany;
	}

	public void setShippingCompany(String shippingCompany) {
		this.shippingCompany = shippingCompany;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ferryDeparture == null) ? 0 : ferryDeparture.hashCode());
		result = prime * result + groupId;
		result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
		result = prime * result + ((printTime == null) ? 0 : printTime.hashCode());
		result = prime * result + ((shippingCompany == null) ? 0 : shippingCompany.hashCode());
		result = prime * result + ((ticketDetail == null) ? 0 : ticketDetail.hashCode());
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
		GroupStatement other = (GroupStatement) obj;
		if (ferryDeparture == null) {
			if (other.ferryDeparture != null)
				return false;
		} else if (!ferryDeparture.equals(other.ferryDeparture))
			return false;
		if (groupId != other.groupId)
			return false;
		if (groupName == null) {
			if (other.groupName != null)
				return false;
		} else if (!groupName.equals(other.groupName))
			return false;
		if (printTime == null) {
			if (other.printTime != null)
				return false;
		} else if (!printTime.equals(other.printTime))
			return false;
		if (shippingCompany == null) {
			if (other.shippingCompany != null)
				return false;
		} else if (!shippingCompany.equals(other.shippingCompany))
			return false;
		if (ticketDetail == null) {
			return other.ticketDetail == null;
		} else return ticketDetail.equals(other.ticketDetail);
	}

	@Override
	public String toString() {
		return "GroupTicketRecord [groupId=" + groupId + ", groupName=" + groupName + ", ferryDeparture="
				+ ferryDeparture + ", ticketDetail=" + ticketDetail + ", printTime="
				+ printTime + ", shippingCompany=" + shippingCompany + "]";
	}

}
