package com.zxm.toolbox.service;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.zxm.toolbox.pojo.gt.Ticket;

import com.zxm.toolbox.util.PoiUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;

public class TicketRecordLoader {
	// 订单号列号
	public static String ORDER_NO_COL = "E";
	// 船票号列号
	public static String TKT_NO_COL = "F";
	// 出发日期列号
	public static String DEPART_DATE_COL = "B";
	// 出发时间列号
	public static String  DEPART_TIME_COL = "C";
	// 出发港列号
	public static String  DEPART_PORT_COL = "P";
	// 到达港列号
	public static String  ARRIVE_PORT_COL = "Q";
	// 舱位名列号
	public static String  CLASS_NAME_COL = "M";
	// 座位号列号
	public static String  SEAT_NO_COL = "O";
	// 旅客姓名列号
	public static String PAX_NAME_COL = "AB";

	/**
	 * 从打卡记录文件读取打卡记录
	 * 
	 * @param file 打卡记录文件
	 */
	public static List<Ticket> loadTktRecords(File file) {
		if (PoiUtils.invalidExcelFile(file))
			return null;
		System.out.println("开始读取文件");
		// 用来存放读取到的数据
		List<Ticket> result;
		Sheet sheet = PoiUtils.loadSheet(file);
		// 没有成功读取到工作表
		if (sheet == null)
			return null;
		Row row;
		Cell cell;
		
		// 获取工作表最大行数
		int rowsCount = sheet.getLastRowNum();
		// 如果最大行数为0
		if (rowsCount == 0)
			return null;
		result = new ArrayList<>();
		
		String tempStr;
		Ticket tkt;
		DecimalFormat intFormat = new DecimalFormat("#");
		// 读取数据
		for (int r = 1; r < rowsCount; r++) {
			row = sheet.getRow(r);
			if(row == null)
				break;
			tkt = new Ticket();
			tempStr = ORDER_NO_COL + 1;
			cell = row.getCell(new CellAddress(tempStr).getColumn());
			tkt.setOrderId(cell == null? null : cell.getStringCellValue());
			
			tempStr = TKT_NO_COL + 1;
			cell = row.getCell(new CellAddress(tempStr).getColumn());
			tkt.setTktNo(cell == null? null : cell.getStringCellValue());
			
			tempStr = DEPART_DATE_COL + 1;
			cell = row.getCell(new CellAddress(tempStr).getColumn());
			tkt.setDepartDate(cell == null ? null : cell.getDateCellValue());
			
			tempStr = DEPART_TIME_COL + 1;
			cell = row.getCell(new CellAddress(tempStr).getColumn());
			tkt.setDepartTime(cell == null? null : cell.getStringCellValue());
			
			tempStr = DEPART_PORT_COL + 1;
			cell = row.getCell(new CellAddress(tempStr).getColumn());
			tkt.setDepartPortName(cell == null? null : cell.getStringCellValue());
			
			tempStr = ARRIVE_PORT_COL + 1;
			cell = row.getCell(new CellAddress(tempStr).getColumn());
			tkt.setArrivePortName(cell == null? null : cell.getStringCellValue());
			
			tempStr = CLASS_NAME_COL + 1;
			cell = row.getCell(new CellAddress(tempStr).getColumn());
			tkt.setClassName(cell == null? null : cell.getStringCellValue());
			
			tempStr = SEAT_NO_COL + 1;
			cell = row.getCell(new CellAddress(tempStr).getColumn());
			tkt.setSeatNo(cell == null ? 0 : Integer.parseInt(intFormat.format(cell.getNumericCellValue())));
			
			tempStr = PAX_NAME_COL + 1;
			cell = row.getCell(new CellAddress(tempStr).getColumn());
			tkt.setPaxName(cell == null? null : cell.getStringCellValue());
			
			result.add(tkt);
		}
		System.out.println("成功读取文件");
		return result;
	}

}
