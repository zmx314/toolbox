package com.zxm.toolbox.pojo.gt;

import java.util.Comparator;

public class SGIdComparator implements Comparator<ShippingGroup> {

	@Override
	public int compare(ShippingGroup o1, ShippingGroup o2) {
		if (o1.getId() > o2.getId())
			return 1;
		else if (o1.getId() == o2.getId())
			return 0;
		else
			return -1;
	}
}
