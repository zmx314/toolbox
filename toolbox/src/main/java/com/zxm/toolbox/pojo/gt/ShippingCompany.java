package com.zxm.toolbox.pojo.gt;

import com.zxm.toolbox.pojo.attn.BasicBean;

public class ShippingCompany extends BasicBean {
	private int id;
	private String name;
	private int groupId;
	private String groupName;
	private String type;

	public ShippingCompany() {
		super();
	}

	public ShippingCompany(int id, String name) {
		super(id, name);
	}

	public ShippingCompany(String name, ShippingGroup group, String type) {
		super();
		this.name = name;
		this.groupId = group.getId();
		this.groupName = group.getName();
		this.type = type;
	}
	public ShippingCompany(int id, String name, ShippingGroup group, String type) {
		super();
		this.id = id;
		this.name = name;
		this.groupId = group.getId();
		this.groupName = group.getName();
		this.type = type;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		ShippingCompany that = (ShippingCompany) o;

		if (id != that.id) return false;
		if (groupId != that.groupId) return false;
		if (name != null ? !name.equals(that.name) : that.name != null) return false;
		if (groupName != null ? !groupName.equals(that.groupName) : that.groupName != null) return false;
		return type != null ? type.equals(that.type) : that.type == null;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + id;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + groupId;
		result = 31 * result + (groupName != null ? groupName.hashCode() : 0);
		result = 31 * result + (type != null ? type.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "ShippingCompany{" +
				"id=" + id +
				", name='" + name + '\'' +
				", groupId=" + groupId +
				", groupName='" + groupName + '\'' +
				", type='" + type + '\'' +
				'}';
	}

}
