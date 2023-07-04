package com.zxm.toolbox.pojo.gt;

import java.util.Date;

public class Ticket {
		// 订单号
		private String orderId;
		// 船票号
		private String tktNo;
		// 出发日期
		private Date departDate;
		// 出发时间
		private String departTime;
		// 出发港
		private String departPortName;
		// 到达港
		private String arrivePortName;
		// 舱位名
		private String className;
		// 座位号
		private int seatNo;
		// 旅客姓名
		private String paxName;
		
		public Ticket() {
			super();
		}
		public Ticket(String tktNo) {
			super();
			this.tktNo = tktNo;
		}
		public String getOrderId() {
			return orderId;
		}
		public void setOrderId(String orderId) {
			this.orderId = orderId;
		}
		public String getTktNo() {
			return tktNo;
		}
		public void setTktNo(String tktNo) {
			this.tktNo = tktNo;
		}
		public Date getDepartDate() {
			return departDate;
		}
		public void setDepartDate(Date departDate) {
			this.departDate = departDate;
		}
		public String getDepartTime() {
			return departTime;
		}
		public void setDepartTime(String departTime) {
			this.departTime = departTime;
		}
		public String getDepartPortName() {
			return departPortName;
		}
		public void setDepartPortName(String departPortName) {
			this.departPortName = departPortName;
		}
		public String getArrivePortName() {
			return arrivePortName;
		}
		public void setArrivePortName(String arrivePortName) {
			this.arrivePortName = arrivePortName;
		}
		public String getClassName() {
			return className;
		}
		public void setClassName(String className) {
			this.className = className;
		}
		public int getSeatNo() {
			return seatNo;
		}
		public void setSeatNo(int seatNo) {
			this.seatNo = seatNo;
		}
		public String getPaxName() {
			return paxName;
		}
		public void setPaxName(String paxName) {
			this.paxName = paxName;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((arrivePortName == null) ? 0 : arrivePortName.hashCode());
			result = prime * result + ((className == null) ? 0 : className.hashCode());
			result = prime * result + ((departDate == null) ? 0 : departDate.hashCode());
			result = prime * result + ((departPortName == null) ? 0 : departPortName.hashCode());
			result = prime * result + ((departTime == null) ? 0 : departTime.hashCode());
			result = prime * result + ((orderId == null) ? 0 : orderId.hashCode());
			result = prime * result + ((paxName == null) ? 0 : paxName.hashCode());
			result = prime * result + seatNo;
			result = prime * result + ((tktNo == null) ? 0 : tktNo.hashCode());
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
			Ticket other = (Ticket) obj;
			if (arrivePortName == null) {
				if (other.arrivePortName != null)
					return false;
			} else if (!arrivePortName.equals(other.arrivePortName))
				return false;
			if (className == null) {
				if (other.className != null)
					return false;
			} else if (!className.equals(other.className))
				return false;
			if (departDate == null) {
				if (other.departDate != null)
					return false;
			} else if (!departDate.equals(other.departDate))
				return false;
			if (departPortName == null) {
				if (other.departPortName != null)
					return false;
			} else if (!departPortName.equals(other.departPortName))
				return false;
			if (departTime == null) {
				if (other.departTime != null)
					return false;
			} else if (!departTime.equals(other.departTime))
				return false;
			if (orderId == null) {
				if (other.orderId != null)
					return false;
			} else if (!orderId.equals(other.orderId))
				return false;
			if (paxName == null) {
				if (other.paxName != null)
					return false;
			} else if (!paxName.equals(other.paxName))
				return false;
			if (seatNo != other.seatNo)
				return false;
			if (tktNo == null) {
				if (other.tktNo != null)
					return false;
			} else if (!tktNo.equals(other.tktNo))
				return false;
			return true;
		}
		@Override
		public String toString() {
			return "Ticket [orderId=" + orderId + ", tktNo=" + tktNo + ", departDate=" + departDate + ", departTime="
					+ departTime + ", departPortName=" + departPortName + ", arrivePortName=" + arrivePortName
					+ ", className=" + className + ", seatNo=" + seatNo + ", paxName=" + paxName + "]";
		}
		
}
