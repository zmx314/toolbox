package com.zxm.toolbox.pojo.gt;

import java.util.Comparator;

public class GTRDepartureTimeComparator implements Comparator<GroupStatement> {

	@Override
	public int compare(GroupStatement o1, GroupStatement o2) {
		if (o1.getFerryDeparture().isAfter(o2.getFerryDeparture()))
			return 1;
		else if (o1.getFerryDeparture().equals(o2.getFerryDeparture()))
			return 0;
		else
			return -1;
	}

}
