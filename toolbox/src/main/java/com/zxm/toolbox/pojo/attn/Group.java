package com.zxm.toolbox.pojo.attn;

import org.apache.poi.ss.util.CellAddress;

import java.util.Objects;

public class Group extends BasicBean {
	private int id;
	private String name;
	private CellAddress dateCell;
	private CellAddress startCell;
	private CellAddress dutyCell;
	private CellAddress timeCell;
	private CellAddress manHourCell;
	private CellAddress offCell;

	public Group() {
		super();
	}

	public Group(int id ,String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Group(int id, String groupName, CellAddress dateCell, CellAddress startCell) {
		super();
		this.id = id;
		this.name = groupName;
		this.dateCell = dateCell;
		this.startCell = startCell;
	}

	public Group(int id, String name, CellAddress dateCell, CellAddress startCell, CellAddress dutyCell, CellAddress timeCell, CellAddress manHourCell, CellAddress offCell) {
		this.id = id;
		this.name = name;
		this.dateCell = dateCell;
		this.startCell = startCell;
		this.dutyCell = dutyCell;
		this.timeCell = timeCell;
		this.manHourCell = manHourCell;
		this.offCell = offCell;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CellAddress getStartCell() {
		return startCell;
	}

	public void setStartCell(CellAddress startCell) {
		this.startCell = startCell;
	}

	public CellAddress getDateCell() {
		return dateCell;
	}

	public void setDateCell(CellAddress dateCell) {
		this.dateCell = dateCell;
	}

	public CellAddress getDutyCell() {
		return dutyCell;
	}

	public void setDutyCell(CellAddress dutyCell) {
		this.dutyCell = dutyCell;
	}

	public CellAddress getTimeCell() {
		return timeCell;
	}

	public void setTimeCell(CellAddress timeCell) {
		this.timeCell = timeCell;
	}

	public CellAddress getManHourCell() {
		return manHourCell;
	}

	public void setManHourCell(CellAddress manHourCell) {
		this.manHourCell = manHourCell;
	}

	public CellAddress getOffCell() {
		return offCell;
	}

	public void setOffCell(CellAddress offCell) {
		this.offCell = offCell;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Group group = (Group) o;

		if (id != group.id) return false;
		if (!Objects.equals(name, group.name)) return false;
		if (!Objects.equals(dateCell, group.dateCell)) return false;
		if (!Objects.equals(startCell, group.startCell)) return false;
		if (!Objects.equals(dutyCell, group.dutyCell)) return false;
		if (!Objects.equals(timeCell, group.timeCell)) return false;
		if (!Objects.equals(manHourCell, group.manHourCell)) return false;
		return Objects.equals(offCell, group.offCell);
	}

	@Override
	public int hashCode() {
		int result = id;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (dateCell != null ? dateCell.hashCode() : 0);
		result = 31 * result + (startCell != null ? startCell.hashCode() : 0);
		result = 31 * result + (dutyCell != null ? dutyCell.hashCode() : 0);
		result = 31 * result + (timeCell != null ? timeCell.hashCode() : 0);
		result = 31 * result + (manHourCell != null ? manHourCell.hashCode() : 0);
		result = 31 * result + (offCell != null ? offCell.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Group{" +
				"id=" + id +
				", name='" + name + '\'' +
				", dateCell=" + dateCell +
				", startCell=" + startCell +
				", dutyCell=" + dutyCell +
				", timeCell=" + timeCell +
				", manHourCell=" + manHourCell +
				", offCell=" + offCell +
				'}';
	}
}
