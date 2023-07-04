package com.zxm.toolbox.service;

import com.zxm.toolbox.pojo.gt.GroupStatement;
import com.zxm.toolbox.pojo.gt.TicketDetail;
import com.zxm.toolbox.util.PoiUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupStatementLoadService {
	/**
	 * 从团体明细表中读取团体明细数据
	 * 
	 * @param file 原始团体记录文件
	 * @return 团体记录列表
	 */
	public static List<GroupStatement> loadGroupStatementFile(File file) {
		System.out.println("开始读取团体明细表文件");
		// 用来存放读取到的数据
		List<GroupStatement> list = new ArrayList<>();
		Sheet sheet = PoiUtils.loadSheet(file);
		if (sheet == null) {
			return null;
		}
		Row row;
		// 团体号
		int groupId;
		// 团体名称
		String groupName;
		// 航班时间
		LocalDateTime ferryDeparture;
		// 航线
		String waterWay;
		// 票型
		String ticketType;
		// 票价
		BigDecimal ticketPrice;
		// 票数
		Map<String, TicketDetail> ttd;
		int ticketNum;
		// 出票时间
		LocalDateTime printTime;
		// 船公司
		String shippingCompany;
		// 获取工作表最大行数
		int rowsCount = sheet.getLastRowNum();
		Cell cell;
		HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		// 读取数据
		try {
			for (int r = 1; r < rowsCount; r++) {
				row = sheet.getRow(r);
				if (row == null)
					break;

				cell = row.getCell(9);
				if (cell == null)
					break;
				groupId = Integer.parseInt(dataFormatter.formatCellValue(cell));

				cell = row.getCell(10);
				if (cell == null || !cell.getCellType().equals(CellType.STRING))
					break;
				groupName = cell.getStringCellValue();

				cell = row.getCell(3);
				if (cell == null || !cell.getCellType().equals(CellType.STRING))
					break;
				ferryDeparture = LocalDateTime.parse(cell.getStringCellValue(), formatter);

				cell = row.getCell(2);
				if (cell == null || !cell.getCellType().equals(CellType.STRING))
					break;
				waterWay = cell.getStringCellValue();

				cell = row.getCell(12);
				if (cell == null || !cell.getCellType().equals(CellType.STRING))
					break;
				ticketType = cell.getStringCellValue();

				cell = row.getCell(6);
				ticketPrice = new BigDecimal(dataFormatter.formatCellValue(cell));

				cell = row.getCell(4);
				ticketNum = Integer.parseInt(dataFormatter.formatCellValue(cell));

				ttd = new HashMap<>();
				ttd.put(ticketType, new TicketDetail(ticketType, ticketPrice, ticketNum));

				cell = row.getCell(11);
				if (cell == null || !cell.getCellType().equals(CellType.NUMERIC))
					break;
				printTime = cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

				cell = row.getCell(16);
				if (cell == null || !cell.getCellType().equals(CellType.STRING))
					break;
				shippingCompany = row.getCell(16).getStringCellValue();

				list.add(new GroupStatement(groupId, groupName, ferryDeparture, waterWay, ttd, printTime,
						shippingCompany));
			}
		} catch (NumberFormatException e) {
			System.out.println("读取团体明细表过程中发生数据格式化异常，读取团体明细任务已终止");
		} catch (Exception e) {
			System.out.println("读取团体明细表过程中发生异常，读取团体明细任务已终止");
			return null;
		}
		System.out.println("成功读取团体明细数据");
		return list;
	}

}
