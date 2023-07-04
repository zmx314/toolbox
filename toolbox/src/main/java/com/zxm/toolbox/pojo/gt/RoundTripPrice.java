package com.zxm.toolbox.pojo.gt;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class RoundTripPrice {
	private int id;
	private String shippingCompany;
	private String ticketType;
	private LocalDate startDate;
	private LocalDate endDate;
	private BigDecimal price;

	public RoundTripPrice() {
		super();
	}

	public RoundTripPrice(int id, String shippingCompany, String ticketType, LocalDate startDate,
						  LocalDate endDate, BigDecimal value) {
		super();
		this.id = id;
		this.shippingCompany = shippingCompany;
		this.ticketType = ticketType;
		this.startDate = startDate;
		this.endDate = endDate;
		this.price = value;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getShippingCompany() {
		return shippingCompany;
	}

	public void setShippingCompany(String shippingCompany) {
		this.shippingCompany = shippingCompany;
	}

	public String getTicketType() {
		return ticketType;
	}

	public void setTicketType(String paxType) {
		this.ticketType = paxType;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		RoundTripPrice that = (RoundTripPrice) o;

		if (id != that.id) return false;
		if (!Objects.equals(shippingCompany, that.shippingCompany))
			return false;
		if (!Objects.equals(ticketType, that.ticketType)) return false;
		if (!Objects.equals(startDate, that.startDate)) return false;
		if (!Objects.equals(endDate, that.endDate)) return false;
		return Objects.equals(price, that.price);
	}

	@Override
	public int hashCode() {
		int result = id;
		result = 31 * result + (shippingCompany != null ? shippingCompany.hashCode() : 0);
		result = 31 * result + (ticketType != null ? ticketType.hashCode() : 0);
		result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
		result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
		result = 31 * result + (price != null ? price.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "RoundTripPrice{" +
				"id=" + id +
				", shippingCompany='" + shippingCompany + '\'' +
				", ticketType='" + ticketType + '\'' +
				", startDate=" + startDate +
				", endDate=" + endDate +
				", value=" + price +
				'}';
	}
}
