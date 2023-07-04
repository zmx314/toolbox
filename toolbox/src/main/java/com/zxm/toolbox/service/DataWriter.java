package com.zxm.toolbox.service;

import com.zxm.toolbox.pojo.rpt.DailyData;
import com.zxm.toolbox.resources.PropertiesFactory;
import com.zxm.toolbox.util.FxUtil;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DataWriter {
	public static void export(File file, List<DailyData> data) throws IOException {
		System.out.println("正在导出Excel文件……");
		if (!file.exists())
			if(!file.createNewFile())
				return;
		FileOutputStream fileOut = new FileOutputStream(file);
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet("Sheet1");

		int rowNum = 0;
		int column;
		Row row;
		Cell cell;
		
		row = sheet.createRow(rowNum++);
		cell = row.createCell(0);
		cell.setCellValue("日期");
		
		row = sheet.createRow(rowNum++);
		cell = row.createCell(0);
		cell.setCellValue("横琴至蛇口班次数");
		
		row = sheet.createRow(rowNum++);
		cell = row.createCell(0);
		cell.setCellValue("横琴至蛇口人数");
		
		row = sheet.createRow(rowNum++);
		cell = row.createCell(0);
		cell.setCellValue("蛇口至横琴班次数");
		
		row = sheet.createRow(rowNum);
		cell = row.createCell(0);
		cell.setCellValue("蛇口至横琴人数");
		
		sheet = wb.getSheet("Sheet1");
		column = 1;
		for (DailyData daily: data) {
			rowNum = 0;
			row = sheet.getRow(rowNum++);
			cell = row.createCell(column);
			cell.setCellValue(daily.getDate());
			row = sheet.getRow(rowNum++);
			cell = row.createCell(column);
			cell.setCellValue(daily.getDepartShip());
			row = sheet.getRow(rowNum++);
			cell = row.createCell(column);
			cell.setCellValue(daily.getDepartPax());
			row = sheet.getRow(rowNum++);
			cell = row.createCell(column);
			cell.setCellValue(daily.getArriveShip());
			row = sheet.getRow(rowNum);
			cell = row.createCell(column);
			cell.setCellValue(daily.getArrivePax());
			column++;
		}
		
		wb.write(fileOut);
		fileOut.close();
		wb.close();
		System.out.println("导出Excel文件成功！");
	}

	public static void createPdfForBoardingCard (File file, List<File> images) throws IOException {
		int cols = Integer.parseInt(
				PropertiesFactory.getInstance().getSettingProps().getValue("BoardingCardsColsInPdf"));
		PDDocument doc = new PDDocument();
		PDPage page;
		PDPageContentStream contentStream;
		PDImageXObject pdImg;
		float heightInPaper = 840f;
		float maxHeight = 421f * cols;
		for (File img : images) {
			// 创建自定义的Rectangle，用来创建自定义大小的页面
			// PDRectangle rectangle = new PDRectangle(2490, 3600);
			// page = new PDPage(rectangle);
			page = new PDPage(PDRectangle.A4);
			doc.addPage(page);
			contentStream = new PDPageContentStream(doc, page);
			pdImg = PDImageXObject.createFromFile(img.getAbsolutePath(), doc);
			contentStream.drawImage(pdImg, 1, 1+ (1 - pdImg.getHeight()/maxHeight)*heightInPaper, 593f, 840f * pdImg.getHeight()/maxHeight);
			contentStream.close();
		}
		//设置文件信息
		PDDocumentInformation docInfo = doc.getDocumentInformation();
		docInfo.setAuthor("张学敏");
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		docInfo.setCreationDate(calendar);
		docInfo.setTitle("乘船券");
		doc.save(file);
		doc.close();
		FxUtil.showInfoAlert("乘船券文件已成功导出！");
	}
}
