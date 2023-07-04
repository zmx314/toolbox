package com.zxm.toolbox.pojo.gt;

import com.zxm.toolbox.pojo.attn.BasicBean;

public class ShippingGroup extends BasicBean {
	private int id;
	private String name;
	private String type;
	private String isCompanySeparate;

	public ShippingGroup() {
		super();
	}

	public ShippingGroup(int id, String name){ super(id, name);}

	public ShippingGroup(String name, String type, String isCompanySeparate) {
		super();
		this.name = name;
		this.type = type;
		this.isCompanySeparate = isCompanySeparate;
	}
	public ShippingGroup(int id, String name, String type, String isCompanySeparate) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.isCompanySeparate = isCompanySeparate;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIsCompanySeparate() {
		return isCompanySeparate;
	}

	public void setIsCompanySeparate(String isCompanySeparate) {
		this.isCompanySeparate = isCompanySeparate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((isCompanySeparate == null) ? 0 : isCompanySeparate.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		ShippingGroup other = (ShippingGroup) obj;
		if (id != other.id)
			return false;
		if (isCompanySeparate == null) {
			if (other.isCompanySeparate != null)
				return false;
		} else if (!isCompanySeparate.equals(other.isCompanySeparate))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			return other.type == null;
		} else return type.equals(other.type);
	}

	@Override
	public String toString() {
		return "ShippingGroup [id=" + id + ", name=" + name + ", type=" + type + ", isCompanySeparate="
				+ isCompanySeparate + "]";
	}

}
