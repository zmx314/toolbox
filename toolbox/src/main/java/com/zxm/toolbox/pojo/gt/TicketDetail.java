package com.zxm.toolbox.pojo.gt;

import java.math.BigDecimal;
import java.util.Comparator;

public class TicketDetail implements Comparator<TicketDetail> {
	private String type;
	private BigDecimal price;
	private int num;

	public TicketDetail() {
		super();
	}

	public TicketDetail(String type, BigDecimal price, int num) {
		super();
		this.type = type;
		this.price = price;
		this.num = num;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + num;
		result = prime * result + ((price == null) ? 0 : price.hashCode());
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
		TicketDetail other = (TicketDetail) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (num != other.num)
			return false;
		if (price == null) {
			return other.price == null;
		} else return price.equals(other.price);
	}

	@Override
	public String toString() {
		return "TicketType [type=" + type + ", price=" + price + ", num=" + num + "]";
	}

	@Override
	public int compare(TicketDetail o1, TicketDetail o2) {
		return Integer.compare(o1.getType().compareTo(o2.getType()), 0);
	}

}
