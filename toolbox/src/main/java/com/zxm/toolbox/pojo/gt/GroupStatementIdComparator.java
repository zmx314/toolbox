package com.zxm.toolbox.pojo.gt;

import java.util.Comparator;

public class GroupStatementIdComparator implements Comparator<GroupStatement> {

	@Override
	public int compare(GroupStatement o1, GroupStatement o2) {
		return Integer.compare(o1.getGroupId(), o2.getGroupId());
	}
}
