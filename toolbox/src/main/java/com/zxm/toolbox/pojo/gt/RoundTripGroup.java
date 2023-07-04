package com.zxm.toolbox.pojo.gt;

public class RoundTripGroup {
	private GroupStatement departureGroup;
	private GroupStatement returnGroup;

	public RoundTripGroup(GroupStatement gtrToGo, GroupStatement gtrForReturn) {
		super();
		this.departureGroup = gtrToGo;
		this.returnGroup = gtrForReturn;
	}

	public GroupStatement getDepartureGroup() {
		return departureGroup;
	}

	public void setDepartureGroup(GroupStatement departureGroup) {
		this.departureGroup = departureGroup;
	}

	public GroupStatement getReturnGroup() {
		return returnGroup;
	}

	public void setReturnGroup(GroupStatement returnGroup) {
		this.returnGroup = returnGroup;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((returnGroup == null) ? 0 : returnGroup.hashCode());
		result = prime * result + ((departureGroup == null) ? 0 : departureGroup.hashCode());
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
		RoundTripGroup other = (RoundTripGroup) obj;
		if (returnGroup == null) {
			if (other.returnGroup != null)
				return false;
		} else if (!returnGroup.equals(other.returnGroup))
			return false;
		if (departureGroup == null) {
			return other.departureGroup == null;
		} else return departureGroup.equals(other.departureGroup);
	}

	@Override
	public String toString() {
		return "RoundTripGroupTicketRecord \n[gtrToGo=" + departureGroup + "\ngtrForReturn=" + returnGroup + "]";
	}

}
