package com.zxm.toolbox.pojo.attn;

import java.util.ArrayList;
import java.util.List;

public class DutyRoster {
	// 排班表分组
	private Group group;
	// 排班表年份
	private int year;
	// 排班表月份
	private int month;
	// 排班表中所有员工的排班记录
	private List<EmpSchedule> esList;
	// 排班表中包含的所有员工的名字
	private List<String> namesInSchedule;
	// 排班表中已经配置好的岗位信息
	private List<Post> withinPosts;
	// 排班表中包含的所有岗位的名字
	private List<String> postsInSchedule;
	// 排班表中还未配置相关参数信息的岗位的名字
	private List<String> withoutPosts;

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public List<EmpSchedule> getEsList() {
		return esList;
	}

	public void setEsList(List<EmpSchedule> esList) {
		this.esList = esList;
	}

	public List<Post> getWithinPosts() {
		return withinPosts;
	}

	public void setWithinPosts(List<Post> withinPosts) {
		this.withinPosts = withinPosts;
	}

	public List<String> getPostsInSchedule() {
		return postsInSchedule;
	}

	public void setPostsInSchedule(List<String> postsInSchedule) {
		this.postsInSchedule = postsInSchedule;
	}

	public List<String> getWithoutPosts() {
		return withoutPosts;
	}

	public void setWithoutPosts(List<String> withoutPosts) {
		this.withoutPosts = withoutPosts;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((esList == null) ? 0 : esList.hashCode());
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		result = prime * result + ((withinPosts == null) ? 0 : withinPosts.hashCode());
		result = prime * result + month;
		result = prime * result + year;
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
		DutyRoster other = (DutyRoster) obj;
		if (esList == null) {
			if (other.esList != null)
				return false;
		} else if (!esList.equals(other.esList))
			return false;
		if (group == null) {
			if (other.group != null)
				return false;
		} else if (!group.equals(other.group))
			return false;
		if (withinPosts == null) {
			if (other.withinPosts != null)
				return false;
		} else if (!withinPosts.equals(other.withinPosts))
			return false;
		if (month != other.month)
			return false;
		return year == other.year;
	}

	@Override
	public String toString() {
		return "DutyRoster:\n"
				+ "group=\t" + group + "\n"
				+ "year=\t" + year + "\n"
				+ "month=\t" + month + "\n"
				+ "esList=\t" + esList + "\n"
				+ "withinPosts=\t" + withinPosts + "\n"
				+ "postsInSchedule=" + postsInSchedule + "\n"
				+ "]";
	}

	/**
	 * 统计排班记录中包含的所有岗位的名字
	 */
	public void countPostsInSchedule() {
		postsInSchedule = new ArrayList<>();
		for (EmpSchedule es : esList) {
			for (String postName : es.getSchedule()) {
				if (!postsInSchedule.contains(postName)) {
					postsInSchedule.add(postName);
				}
			}
		}
	}

	/**
	 * 统计排班记录中还没有配置相关信息的岗位名字
	 */
	public void countWithoutPost() {
		withoutPosts = new ArrayList<>();
		List<String> withinPostNames = new ArrayList<>();
		for (Post post : withinPosts) {
			withinPostNames.add(post.getPostName());
		}
		for (String postName : postsInSchedule) {
			if (!withinPostNames.contains(postName)) {
				withoutPosts.add(postName);
			}
		}
	}
}
