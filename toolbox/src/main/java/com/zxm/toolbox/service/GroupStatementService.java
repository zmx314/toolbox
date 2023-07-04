package com.zxm.toolbox.service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.zxm.toolbox.dao.ShippingGroupDao;
import com.zxm.toolbox.util.MyEntry;
import com.zxm.toolbox.dao.DaoFactory;
import com.zxm.toolbox.pojo.gt.GroupStatementIdComparator;
import com.zxm.toolbox.pojo.gt.GroupStatement;
import com.zxm.toolbox.pojo.gt.Keyword;
import com.zxm.toolbox.pojo.gt.RoundTripGroup;
import com.zxm.toolbox.pojo.gt.RoundTripPrice;
import com.zxm.toolbox.pojo.gt.ShippingCompany;
import com.zxm.toolbox.pojo.gt.ShippingGroup;
import com.zxm.toolbox.pojo.gt.TicketDetail;

public class GroupStatementService {

	/**
	 * 获取团体明细表的开始日期
	 * @param groupStatements 读取到的团体明细
	 * @return 团体明细表打印时间最早值
	 */
	public static LocalDateTime getStartDate(List<GroupStatement> groupStatements) {
		if (groupStatements == null || groupStatements.isEmpty())
			return null;
		groupStatements.sort(new GroupStatementIdComparator());
		return groupStatements.get(0).getPrintTime();
	}

	/**
	 * 根据关键词配置信息，获取旅行社的标准名字
	 * 因为有的旅行社，系统里面记录的好几个名字，其实是一家旅行社
	 * 所以此方法是根据旅行社名字的关键词，把包含同一个关键词的旅行社名字修改成统一的名字
	 * @param travelAgency 团体名字
	 * @param keywords 关键词
	 * @return 团体的统一名字
	 */
	public static String getStandardName(String travelAgency, List<Keyword> keywords) {
		String key;
		for (Keyword keyword : keywords) {
			key = keyword.getKey();
			if (travelAgency.contains(key))
				return keyword.getFullName();
		}
		return null;
	}

	/**
	 * 修改团体记录列表里面的团体名称
	 * @param records 团体记录
	 */
	public static void modifyGroupName(List<GroupStatement> records) {
		List<Keyword> keywords = DaoFactory.getInstance().getKeywordDao().findAll();
		String standardName;
		for (GroupStatement record : records) {
			standardName = getStandardName(record.getGroupName(), keywords);
			if (standardName != null)
				record.setGroupName(standardName);
		}
	}

	/*
	 * 比较两个Double类型数据是否相等 d1 - d2 > -0.000001 && d1 - d2 < 0.000001
	 */
	public static boolean notRoundTripPrice(GroupStatement gs, List<RoundTripPrice> prices) {
		BigDecimal recordPrice;
		String ticketType;
		if(gs.getTicketDetail().get("团-成人") != null) {
			recordPrice = gs.getTicketDetail().get("团-成人").getPrice();
			ticketType = "团-成人";
		} else if(gs.getTicketDetail().get("团-自由行") != null) {
			recordPrice = gs.getTicketDetail().get("团-自由行").getPrice();
			ticketType = "团-自由行";
		} else
			return true;
//		System.out.println("团体票记录的票价：" + recordPrice);
		LocalDateTime departure = gs.getFerryDeparture();
		BigDecimal roundPrice;
		for (RoundTripPrice ptp : prices) {
			if (departure.isAfter(ptp.getStartDate().atTime(LocalTime.MIN)) &&
					departure.isBefore(ptp.getEndDate().atTime(LocalTime.MAX)) &&
					ticketType.equals(ptp.getTicketType())) {
				roundPrice = ptp.getPrice();
//				System.out.println("来回套票的票价：" + roundPrice);
				return recordPrice.compareTo(roundPrice) != 0;
			}
		}
		return true;
	}

	/**
	 * 把团体记录里面团体号一样的记录合并成一个团体记录
	 * @param records 原始团体记录
	 */
	public static void mergeStatementRecord(List<GroupStatement> records) {
		Comparator<GroupStatement> c = new GroupStatementIdComparator();
		records.sort(c);
		int flag = 0;
		GroupStatement gs1, gs2;
		while (flag + 1 != records.size()) {
			for (int i = flag; i < flag + 1; i++) {
				if (flag + 1 == records.size())
					break;
				gs1 = records.get(flag);
				gs2 = records.get(flag + 1);

				if (gs1.getGroupId() == gs2.getGroupId()
						&& gs1.getShippingCompany().equals(gs2.getShippingCompany())) {
					gs1.getTicketDetail().putAll(gs2.getTicketDetail());
					records.remove(flag + 1);
				} else
					flag++;
			}
		}
	}

	/**
	 * 根据配置的分组信息和航线信息，统计团体记录中包含的所有航线组，同时统计未配置的航线
	 * @param records 团体记录
	 * @return 统计到的航线组名称构成的List对象
	 */
	public static List<ShippingGroup> countGroup(List<GroupStatement> records, List<String> withoutCompanies) {
		List<ShippingCompany> companyList = DaoFactory.getInstance().getShippingCompanyDao().findAll();
		List<String> companyNames = new ArrayList<>();
		List<String> groupNames = new ArrayList<>();
		// 航线名，分组名
		Map<String, String> map = new HashMap<>();
		for (ShippingCompany sc : companyList) {
			if (!map.containsKey(sc.getName())) {
				map.put(sc.getName(), sc.getGroupName());
			}
		}
		String tempStr;
		for (GroupStatement statement : records) {
			tempStr = statement.getShippingCompany();
			if (!companyNames.contains(tempStr))
				companyNames.add(tempStr);
		}
		for (String companyName : companyNames) {
			if (map.get(companyName) == null) {
				withoutCompanies.add(companyName);
				continue;
			}
			if (!groupNames.contains(map.get(companyName))) {
				groupNames.add(map.get(companyName));
			}
		}
		List<ShippingGroup> groups = new ArrayList<>();
		ShippingGroupDao dao = DaoFactory.getInstance().getShippingGroupDao();
		for (String name : groupNames)
			groups.add(dao.findByName(name));
		return groups;
	}

	/**
	 * 校验分组的配置信息是否完整，所有分组必须包含去程的航线配置信息，
	 * 如果是其他分组（单去单回、单来回、单去单回来回），则必须包含回程的航线配置信息
	 * @param group 航线组对象
	 * @return true完整，false不完整
	 */
	public static boolean isGroupCompleted(ShippingGroup group) {
		List<ShippingCompany> list = DaoFactory.getInstance()
				.getShippingCompanyDao().findByGroupName(group.getName());
		boolean flag = false;
		for (ShippingCompany sc : list) {
			if (sc.getType().equals("去程")) {
				flag = true;
				break;
			}
		}
		if (!flag)
			return false;
		if (group.getType().equals("单去")) {
			return true;
		} else {
			flag = false;
			for (ShippingCompany sc : list) {
				if (sc.getType().equals("回程")) {
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

	/**
	 * 合并团体记录列表里面指定的两个团体号对应的团体记录，两个团体记录合并后，把次团体号对应的团体记录删除
	 * @param records 团体记录列表
	 * @param mainId 主团体号
	 * @param secondaryId 次团体号
	 */
	public static void mergeStatementRecord(List<GroupStatement> records, int mainId, int secondaryId) {
		int mainIndex = 0;
		int secondaryIndex = 0;
		if (mainId == secondaryId)
			return;
		Map<String, TicketDetail> ttdMap1, ttdMap2;
		// 从团体记录里面查找团体号为mainId的索引号
		for (int i = 0; i < records.size(); i++) {
			if (records.get(i).getGroupId() == mainId) {
				mainIndex = i;
				break;
			}
		}
		// 从团体记录里面查找团体号为secondaryId的索引号
		for (int i = 0; i < records.size(); i++) {
			if (records.get(i).getGroupId() == secondaryId) {
				secondaryIndex = i;
				break;
			}
		}
		// 把团体号码mainId的团体记录和团体号码secondaryId的团体记录合并
		for (GroupStatement gtr : records) {
			if (gtr.getGroupId() == secondaryId) {
				ttdMap1 = records.get(mainIndex).getTicketDetail();
				ttdMap2 = records.get(secondaryIndex).getTicketDetail();
				for (Entry<String, TicketDetail> entry : ttdMap2.entrySet()) {
					if (ttdMap1.containsKey(entry.getKey()))
						ttdMap1.get(entry.getKey())
								.setNum(ttdMap1.get(entry.getKey()).getNum() + entry.getValue().getNum());
					else
						ttdMap1.put(entry.getKey(), entry.getValue());
				}
				break;
			}
		}
		//删除团体记录里面团体号为secondaryId的团体记录
		records.remove(secondaryIndex);
	}

	// 此方法无用
	public static void simpleMerge(List<GroupStatement> records) {
		Duration temp;
		GroupStatement gtr1, gtr2;
		List<MyEntry> list = new ArrayList<>();
		for (int i = 0, length = records.size(); i < length; i++) {
			gtr1 = records.get(i);
			if (gtr1.getTicketDetail().get("成人") == null) {
				for (GroupStatement record : records) {
					gtr2 = record;
					if (gtr1.getGroupId() != gtr2.getGroupId()
							&& gtr1.getShippingCompany().equals(gtr2.getShippingCompany())
							&& gtr2.getTicketDetail().get("成人") != null) {
//						System.out.println("i\t" + gtr1.getGroupId() + "\tj\t" + gtr2.getGroupId());
						temp = Duration.between(gtr1.getFerryDeparture(), gtr2.getFerryDeparture());
						if (gtr2.getGroupName().equals(gtr1.getGroupName()) && temp.isZero()) {
							list.add(new MyEntry(gtr1.getGroupId(), gtr2.getGroupId()));
						}
					}
				}
			}
		}
		for (int i = list.size() - 1; i >= 0; i--) {
			System.out.println(list.get(i));
			mergeStatementRecord(records, list.get(i).getKey(), list.get(i).getValue());
		}
	}

	/**
	 * 从团体记录里里面选取一个航线组的团体记录
	 * @param records 团体记录
	 * @param groupName 航线组名字
	 * @return 选中的团体记录列表
	 */
	public static List<GroupStatement> selectOneShippingGroup(List<GroupStatement> records, String groupName) {
		List<ShippingCompany> companies = DaoFactory.getInstance().getShippingCompanyDao().findByGroupName(groupName);
		List<String> companyNameList = new ArrayList<>();
		for (ShippingCompany company : companies) {
			companyNameList.add(company.getName());
		}
		List<GroupStatement> selectedRecords = new ArrayList<>();
		for (GroupStatement gtr : records) {
			if (companyNameList.contains(gtr.getShippingCompany())) {
				selectedRecords.add(gtr);
			}
		}
		return selectedRecords;
	}

	/**
	 * 从团体记录列表里面选取指定船属公司的团体记录
	 * @param records 原始团体记录
	 * @param shippingCompanyName 船属公司名
	 * @return 团体记录列表
	 */
	public static List<GroupStatement> selectOneShippingCompanyRecords(
			List<GroupStatement> records, String shippingCompanyName) {
		List<GroupStatement> list = new ArrayList<>();
		for (GroupStatement gtr : records) {
			if (gtr.getShippingCompany().equals(shippingCompanyName)) {
				list.add(gtr);
			}
		}
		return list;
	}

	/**
	 * 从团体记录里面选取去程船属公司的团体记录
	 * 回程航线的船属公司名字都包含“回程”，所以只要船属公司名字里面不含“回程”两个字就是去程的航线
	 * @param records 原始团体记录
	 * @return 去程航线的团体记录
	 */
	public static List<GroupStatement> selectDepartRecord(List<GroupStatement> records) {
		List<GroupStatement> list = new ArrayList<>();
		for (GroupStatement gtr : records) {
			if (!gtr.getShippingCompany().contains("回程"))
				list.add(gtr);
		}
		return list;
	}

	/**
	 * 从团体记录里面选取回程船属公司的团体记录
	 * 只要船属公司名字里面含“回程”两个字就是回程的航线
	 * @param records 原始团体记录
	 * @return 回程航线的团体记录
	 */
	public static List<GroupStatement> selectReturnRecord(List<GroupStatement> records) {
		List<GroupStatement> list = new ArrayList<>();
		for (GroupStatement gtr : records) {
			if (gtr.getShippingCompany().contains("回程"))
				list.add(gtr);
		}
		return list;
	}

	/**
	 * 此方法用来处理每天团体记录的分开任务，适用于只包含来回套票的航线组，比如九洲岛线
	 * @param dailyRecs 每日团体记录
	 * @param roundRecs 来回程团体记录列表
	 * @param wrongRecs 错误的团体记录（找不到匹配成套票的团体记录）
	 * @param groupName 航线组名称
	 */
	public static void separateDailyRecords(
			List<GroupStatement> dailyRecs,
			List<RoundTripGroup> roundRecs,
			List<GroupStatement> wrongRecs,
			String groupName) {
		String departCompany = null;
		// 每日去程团记录
		List<GroupStatement> dailyDepartRecs = new ArrayList<>();
		// 每日回程团体记录
		List<GroupStatement> dailyReturnRecs = new ArrayList<>();
		GroupStatement gs1, gs2;
		Map<String, TicketDetail> ttdMap1, ttdMap2;
		Entry<String, TicketDetail> entry;
		boolean returnRecordExist;
		List<ShippingCompany> companies = DaoFactory.getInstance().getShippingCompanyDao().findByGroupName(groupName);
		for (ShippingCompany company : companies) {
			if (company.getType().equals("去程")) {
				departCompany = company.getName();
			}
		}
		// 把去程和回程的团体分开
		for (GroupStatement gs : dailyRecs) {
			if (gs.getShippingCompany().equals(departCompany)) {
				dailyDepartRecs.add(gs);
			} else {
				dailyReturnRecs.add(gs);
			}
		}
		// 查找来回套票
		for (int j = 0; j < dailyDepartRecs.size(); j++) {
			gs1 = dailyDepartRecs.get(j);
			returnRecordExist = false;
			for (int k = 0; k < dailyReturnRecs.size(); k++) {
				gs2 = dailyReturnRecs.get(k);
				// 旅行社名字不一致
				if (!gs1.getGroupName().equals(gs2.getGroupName()))
					continue;
				ttdMap1 = gs1.getTicketDetail();
				ttdMap2 = gs2.getTicketDetail();
				// 票型数量不一样
				if (ttdMap1.size() != ttdMap2.size())
					continue;
				// 票型数量一致时一一比对各个票型和票数
				for (Entry<String, TicketDetail> stringTicketDetailEntry : ttdMap1.entrySet()) {
					entry = stringTicketDetailEntry;
					if (ttdMap2.get(entry.getKey()) == null) {
						returnRecordExist = false;
						break;
					}
					if (entry.getValue().getNum() == ttdMap2.get(entry.getKey()).getNum()) {
						returnRecordExist = true;
					} else {
						returnRecordExist = false;
						break;
					}
				}
				// 来回程记录匹配
				if (returnRecordExist) {
					roundRecs.add(new RoundTripGroup(gs1, gs2));
					dailyDepartRecs.remove(j);
					dailyReturnRecs.remove(k);
					j--;
					break;
				}
				if (dailyDepartRecs.size() != 0)
					wrongRecs.addAll(dailyDepartRecs);
				if (dailyReturnRecs.size() != 0)
					wrongRecs.addAll(dailyReturnRecs);
			}
		}
	}

	/**
	 * 此方法只适用于只包含来回套票的航线组，比如九洲岛线
	 * @param recs      团体记录
	 * @param roundRecs 来回程团体记录列表
	 * @param wrongRecs 错误的团体记录（找不到匹配成套票的团体记录）
	 * @param groupName    航线组名称
	 */
	public static void separateRecords(
			List<GroupStatement> recs,
			List<RoundTripGroup> roundRecs,
			List<GroupStatement> wrongRecs,
			String groupName) {
		// 每日团体记录
		List<GroupStatement> dailyRecs = new ArrayList<>();
		GroupStatement gs1,gs2;
		for (int i = 0; i < recs.size(); i++) {
			gs1 = recs.get(i);
			dailyRecs.add(gs1);
			// 如果到了团体记录的末尾，表示一天的记录读取完了，要把这一天的团体记录按来回程分开
			if (i + 1 == recs.size()){
				separateDailyRecords(dailyRecs, roundRecs, wrongRecs, groupName);
				dailyRecs = new ArrayList<>();
				continue;
			}

			gs2 = recs.get(i + 1);
			// 如果当前团体记录和下一个团体记录的打印时间不在同一天，表示一天的记录读取完了，要把这一天的团体记录按来回程分开
			if(gs1.getPrintTime().getDayOfMonth() != gs2.getPrintTime().getDayOfMonth()) {
				separateDailyRecords(dailyRecs, roundRecs, wrongRecs, groupName);
				dailyRecs = new ArrayList<>();
			}
		}
	}

	/**
	 * 此方法只适用于单去单回来回类型的航线组，比如香港线
	 * @param recs 全部团体记录
	 * @param roundRecs 来回团体记录
	 * @param departRecs 去程团体记录
	 * @param returnRecs 回程团体记录
	 * @param groupName  航线组名称
	 */
	public static void separateRecords(
			List<GroupStatement> recs,
			List<RoundTripGroup> roundRecs,
			List<GroupStatement> departRecs,
			List<GroupStatement> returnRecs,
			String groupName) {
		String departCompany = null;
		String returnCompany = null;
		// 每日团体记录
		List<GroupStatement> dailyRecs = new ArrayList<>();
		// 每日去程团记录
		List<GroupStatement> dailyDepartRecs = new ArrayList<>();
		// 每日回程团体记录
		List<GroupStatement> dailyReturnRecs = new ArrayList<>();
		List<RoundTripPrice> departPrices, returnPrices;
		GroupStatement gs1,gs2;
		Map<String, TicketDetail> ttdMap1, ttdMap2;
		Entry<String, TicketDetail> entry;
		boolean returnRecordExist;
		List<ShippingCompany> companies = DaoFactory.getInstance().getShippingCompanyDao().findByGroupName(groupName);
		// 查找航线组中去程船属公司名字
		for (ShippingCompany company : companies) {
			if (company.getType().equals("去程"))
				departCompany = company.getName();
		}
		// 查找套票中去程票价
		departPrices = DaoFactory.getInstance().getRoundTripPriceDao()
				.findByShippingCompanyName(departCompany);
		// 查找航线组中回程船属公司名字
		for (ShippingCompany company : companies) {
			if (company.getType().equals("回程"))
				returnCompany = company.getName();
		}
		// 查找套票中回程票价
		returnPrices = DaoFactory.getInstance().getRoundTripPriceDao()
				.findByShippingCompanyName(returnCompany);

		for (int i = 0; i < recs.size(); i++) {
			gs1 = recs.get(i);
			dailyRecs.add(gs1);
			if (i + 1 == recs.size()
					|| gs1.getPrintTime().getDayOfMonth() != recs.get(i + 1).getPrintTime().getDayOfMonth()) {
				// 把去程和回程的团体分开
				for (GroupStatement gtr : dailyRecs) {
					if (gtr.getShippingCompany().equals(departCompany)) {
						dailyDepartRecs.add(gtr);
					} else {
						dailyReturnRecs.add(gtr);
					}
				}
				// 查找来回套票
				for (int j = 0; j < dailyDepartRecs.size(); j++) {
					gs1 = dailyDepartRecs.get(j);
					for (int k = 0; k < dailyReturnRecs.size(); k++) {
						gs2 = dailyReturnRecs.get(k);
						// 旅行社名字不一致
						if (!gs1.getGroupName().equals(gs2.getGroupName()))
							continue;
//						System.out.println("旅行社名字一致");
						ttdMap1 = gs1.getTicketDetail();
						ttdMap2 = gs2.getTicketDetail();
						// 票型数量不一样
						if (ttdMap1.size() != ttdMap2.size())
							continue;
//						System.out.println("票型种类的数量一致");
						// 团-成人、团-自由行的票价不是来回套票的票价
						if (notRoundTripPrice(gs1, departPrices))
							continue;
//						System.out.println("去程是来回套票票价");
						if (!notRoundTripPrice(gs2, returnPrices))
							continue;
//						System.out.println("回程是来回套票票价");
						// 票型数量一致时一一比对各个票型和票数
						Iterator<Entry<String, TicketDetail>> it = ttdMap1.entrySet().iterator();
						returnRecordExist = true;
						while (it.hasNext()) {
							entry = it.next();
							String typeName;
							typeName = entry.getKey();
							TicketDetail td2 = ttdMap2.get(typeName);
							if(td2 == null) {
								returnRecordExist = false;
								break;
							}
							if (entry.getValue().getNum() != ttdMap2.get(typeName).getNum()) {
								returnRecordExist = false;
								break;
							}
						}
//						System.out.println(returnRecordExist);
						// 来回程记录匹配
						if (returnRecordExist) {
							roundRecs.add(new RoundTripGroup(gs1, gs2));
							dailyDepartRecs.remove(j);
							dailyReturnRecs.remove(k);
							j--;
							break;
						}
					}
				}

				if (dailyDepartRecs.size() != 0)
					departRecs.addAll(dailyDepartRecs);
				if (dailyReturnRecs.size() != 0)
					returnRecs.addAll(dailyReturnRecs);

				dailyRecs = new ArrayList<>();
				dailyDepartRecs = new ArrayList<>();
				dailyReturnRecs = new ArrayList<>();
			}
		}
	}

}
