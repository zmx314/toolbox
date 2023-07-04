package com.zxm.toolbox.util;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ooxml.POIXMLException;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class PoiUtils {
	/**
	 * 判断文件是否有无效的Excel文件
	 * @param file 文件
	 * @return 无效则返回true，有效则返回false
	 */
	public static boolean invalidExcelFile(File file) {
		if (FileUtil.invalidFile(file))
			return true;
		if(!FileUtil.EXCEL_SUFFIXES.contains(FileUtil.getExtension(file))) {
			System.out.println("无效的文件类型，要求Excel文件：" + file.getAbsolutePath());
			return true;
		}
		return false;
	}

	/**
	 * 判断文件是否有无效的XLS文件
	 * @param file 文件
	 * @return 无效则返回true，有效则返回false
	 */
	public static boolean invalidXlsFile(File file) {
		if (FileUtil.invalidFile(file))
			return true;
		if(!file.getName().endsWith(".xls")) {
			System.out.println("无效的文件类型，要求.xls文件：" + file.getAbsolutePath());
			return true;
		}
		return false;
	}

	/**
	 * 判断文件是否有无效的XLSX文件
	 * @param file 文件
	 * @return 无效则返回true，有效则返回false
	 */
	public static boolean invalidXlsxFile(File file) {
		if (FileUtil.invalidFile(file))
			return true;
		if(!file.getName().endsWith(".xlsx")) {
			System.out.println("无效的文件类型，要求.xlsx文件：" + file.getAbsolutePath());
			return true;
		}
		return false;
	}

	/**
	 * 从.xlsx文件中读取工作本对象
	 * @param file .xlsx文件
	 * @return 工作本对象，发生异常时返回null
	 */
	public static XSSFWorkbook loadXSSFWorkbook(File file) {
		if(invalidXlsxFile(file))
			return null;
		XSSFWorkbook wb;
		try {
			InputStream in = new FileInputStream(file);
			BufferedInputStream bin = new BufferedInputStream(in);
			wb = new XSSFWorkbook(bin);
			in.close();
			bin.close();
			return wb;
		} catch (FileNotFoundException e) {
			System.out.println("找不到文件异常：" + file.getAbsolutePath());
			return null;
		} catch (SecurityException e) {
			System.out.println("文件拒绝访问异常");
			return null;
		} catch (IOException e) {
			System.out.println("读取文件时发生IO异常，读取文件失败！");
			return null;
		} catch (EncryptedDocumentException e) {
			System.out.println("加密的文档异常");
			return null;
		} catch (POIXMLException e) {
			System.out.println("无效的OOXML数据异常");
			return null;
		}
	}

	/**
	 * 从.xlsx文件中读取工作本对象
	 * @param file .xlsx文件
	 * @return 工作本对象，发生异常时返回null
	 */
	public static HSSFWorkbook loadHSSFWorkbook(File file) {
		if(invalidXlsFile(file))
			return null;
		HSSFWorkbook wb;
		try {
			InputStream in = new FileInputStream(file);
			BufferedInputStream bin = new BufferedInputStream(in);
			wb = new HSSFWorkbook(bin);
			in.close();
			bin.close();
			return wb;
		} catch (FileNotFoundException e) {
			System.out.println("找不到文件异常：" + file.getAbsolutePath());
			return null;
		} catch (SecurityException e) {
			System.out.println("文件拒绝访问异常");
			return null;
		} catch (IOException e) {
			System.out.println("读取文件时发生IO异常，读取文件失败！");
			return null;
		} catch (EncryptedDocumentException e) {
			System.out.println("加密的文档异常");
			return null;
		}
	}

	/**
	 * 从Excel文件中读取工作本对象
	 * @param file Excel文件
	 * @return 工作本对象，发生异常时返回null
	 */
	public static Workbook loadWorkbook(File file) {
		if(invalidExcelFile(file))
			return null;
		Workbook wb;
		try {
			InputStream in = new FileInputStream(file);
			BufferedInputStream bin = new BufferedInputStream(in);
			ZipSecureFile.setMinInflateRatio(-1.0d);
			wb = file.getPath().endsWith(".xls") ? new HSSFWorkbook(bin) : new XSSFWorkbook(bin);
			in.close();
			bin.close();
			return wb;
		} catch (FileNotFoundException e) {
			System.out.println("找不到文件异常：" + file.getAbsolutePath());
			return null;
		} catch (SecurityException e) {
			System.out.println("文件拒绝访问异常");
			return null;
		} catch (EncryptedDocumentException e) {
			System.out.println("加密的文档异常");
			return null;
		}catch (IOException e) {
			System.out.println("读取文件时发生IO异常，读取文件失败");
			return null;
		}
	}

	/**
	 * 从加密Excel文件中读取工作本对象
	 * @param file 文件对象
	 * @param password 密码
	 * @return 工作本对象，发生异常时返回null
	 */
	public static Workbook loadEncryptedWorkbook(File file, String password) {
		Workbook wb;
		try {
			InputStream inp = new FileInputStream(file);
			if (file.getPath().endsWith(".xls")) {
				Biff8EncryptionKey.setCurrentUserPassword(password);
				wb = WorkbookFactory.create(inp);
				inp.close();
			}else {
				POIFSFileSystem pfs = new POIFSFileSystem(inp);
				EncryptionInfo encInfo = new EncryptionInfo(pfs);
				Decryptor decryptor = Decryptor.getInstance(encInfo);
				if (decryptor.verifyPassword(password)) {
					wb = new XSSFWorkbook(decryptor.getDataStream(pfs));
				}else
					return null;
			}
			wb.close();
			return wb;
		} catch (FileNotFoundException e) {
			System.out.println("找不到文件！");
			return null;
		} catch (IOException e) {
			System.out.println("文件读取过程发生异常，文件读取过程已终止");
			return null;
		} catch (EncryptedDocumentException e) {
			System.out.println("加密的文档异常！");
			return null;
		} catch (GeneralSecurityException e) {
			System.out.println("密码无效或证书错误！");
			return null;
		}
	}

	/**
	 * 从Excel文件获取第一个工作表，Excel不能加密
	 * @param file Excel文件
	 * @return Sheet 工作表对象，不存在，则返回null
	 */
	public static Sheet loadSheet(File file) {
		return loadSheet(file, 0);
	}

	/**
	 * 从文件加载指定索引序号的工作表
	 * @param file Excel文件
	 * @param index 索引序号，从0开始
	 * @return 工作表对象，不存在，则返回null
	 */
	public static Sheet loadSheet(File file, int index) {
		Workbook wb = PoiUtils.loadWorkbook(file);
		if (wb == null)
			return null;
		return wb.getSheetAt(index);
	}

	/**
	 * 从Excel文件读取指定名字的的工作表
	 * @param file Excel文件
	 * @param sheetName 工作表名
	 * @return 工作表对象，不存在，则返回null
	 */
	public static Sheet loadSheet(File file, String sheetName) {
		Workbook wb = loadWorkbook(file);
		if (wb == null)
			return null;
		return wb.getSheet(sheetName);
	}

	/**
	 * 从文件中读取指定单元格数据
	 * @param sheet 工作表对象
	 * @param cellAddress 指定单元格地址
	 * @return Cell对象，不存在，则返回null
	 */
	public static Cell loadSpecialCell(Sheet sheet, CellAddress cellAddress) {
		if (sheet == null)
			return null;
		Row row = sheet.getRow(cellAddress.getRow());
		if (row == null)
			return null;
		return row.getCell(cellAddress.getColumn());
	}

	/**
	 * 从文件中读取指定单元格数据
	 * @param file file对象
	 * @param index 工作表索引号，索引号从0开始
	 * @param cellAddress 指定单元格地址
	 * @return Cell对象，不存在，则返回null
	 */
	public static Cell loadSpecialCell(File file, int index, CellAddress cellAddress) {
		Sheet sheet = loadSheet(file, index);
		return loadSpecialCell(sheet, cellAddress);
	}

	/**
	 * 从文件中读取指定单元格数据
	 * @param file file对象
	 * @param sheetName 工作表索引号，索引号从0开始
	 * @param cellAddress 指定单元格地址
	 * @return Cell对象，不存在，则返回null
	 */
	public static Cell loadSpecialCell(File file, String sheetName, CellAddress cellAddress) {
		Sheet sheet = loadSheet(file, sheetName);
		return loadSpecialCell(sheet, cellAddress);
	}

	/**
	 * 读取Excel文件里面的所有工作表名字
	 * @param wb 工作本对象
	 * @return 工作表名列表
	 */
	public static List<String> loadSheetNames(Workbook wb) {
		List<String> list = new ArrayList<>();
		if(wb == null)
			return list;
		int numberOfSheets = wb.getNumberOfSheets();
		for (int i = 0; i < numberOfSheets; i++)
			list.add(wb.getSheetName(i));
		return list;
	}

	/**
	 * 读取Excel文件里面的所有工作表名字
	 * @param file Excel文件
	 * @return 工作表名列表
	 */
	public static List<String> loadSheetNames(File file) {
		return loadSheetNames(loadWorkbook(file));
	}

	/**
	 * 文本单元格样式
	 * @param wb XSSFWorkbook对象
	 * @return CellStyle对象
	 */
	public static CellStyle createTextStyle(XSSFWorkbook wb) {
		CellStyle cellStyle = wb.createCellStyle();
		XSSFFont font;
		// 字体样式
		font = wb.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 9);
		cellStyle.setFont(font);
		// 边框样式
		cellStyle.setBorderBottom(BorderStyle.THIN); // 下边框
		cellStyle.setBorderLeft(BorderStyle.THIN);// 左边框
		cellStyle.setBorderTop(BorderStyle.THIN);// 上边框
		cellStyle.setBorderRight(BorderStyle.THIN);// 右边框
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		return cellStyle;
	}

	/**
	 * 整数单元格样式
	 * @param wb XSSFWorkbook对象
	 * @return CellStyle对象
	 */
	public static CellStyle createNumericStyle(XSSFWorkbook wb, int num) {
		CellStyle cellStyle = wb.createCellStyle();
		XSSFFont font;
		XSSFDataFormat df = wb.createDataFormat();
		switch (num) {
			case 0:
				cellStyle.setDataFormat(df.getFormat("#,#0"));
				break;
			case 1:
				cellStyle.setDataFormat(df.getFormat("#,#0.0"));
				break;
			case 2:
				cellStyle.setDataFormat(df.getFormat("#,#0.00"));
				break;
			case 3:
				cellStyle.setDataFormat(df.getFormat("#,#0.000"));
				break;
			case 4:
				cellStyle.setDataFormat(df.getFormat("#,#0.0000"));
				break;
		}
		// 字体样式
		font = wb.createFont();
		font.setFontName("宋体");
		font.setFontHeight(9);
		cellStyle.setFont(font);
		// 边框样式
		cellStyle.setBorderBottom(BorderStyle.THIN); // 下边框
		cellStyle.setBorderLeft(BorderStyle.THIN);// 左边框
		cellStyle.setBorderTop(BorderStyle.THIN);// 上边框
		cellStyle.setBorderRight(BorderStyle.THIN);// 右边框
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setAlignment(HorizontalAlignment.RIGHT);
		return cellStyle;
	}

	/**
	 * 根据单元格地址返回工作表中单元格对象
	 * @param sheet 工作表对象
	 * @param address 单元格地址
	 * @return 单元格，如果不存在则创建单元格
	 */
	public static Cell getCell(Sheet sheet, String address){
		if (address == null)
			return null;
		return getCell(sheet, new CellAddress(address));
	}

	/**
	 * 根据单元格地址返回工作表中单元格对象
	 * @param sheet 工作表对象
	 * @param address 单元格地址
	 * @return 单元格，如果不存在则创建单元格
	 */
	public static Cell getCell(Sheet sheet, CellAddress address){
		if (sheet == null || address == null)
			return null;
		Row row = sheet.getRow(address.getRow());
		if (row == null)
			row = sheet.createRow(address.getRow());
		Cell cell = row.getCell(address.getColumn());
		if (cell == null)
			cell = row.createCell(address.getColumn());
		return cell;
	}

	/**
	 * 设置列宽自适应，此方法对于公式单元格会失效
	 *
	 * @param sheet 工作表对象
	 * @param size  列宽大小
	 */
	public static void setColumnAutoSize(Sheet sheet, int size) {
		for (int columnNum = 0; columnNum < size; columnNum++) {
			int columnWidth = sheet.getColumnWidth(columnNum) / 256;
			for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
				Row currentRow;
				// 当前行未被使用过
				if (sheet.getRow(rowNum) == null) {
					currentRow = sheet.createRow(rowNum);
				} else {
					currentRow = sheet.getRow(rowNum);
				}

				if (currentRow.getCell(columnNum) != null) {
					Cell currentCell = currentRow.getCell(columnNum);
					if (currentCell.getCellType() == CellType.STRING) {
						int length = currentCell.getStringCellValue().getBytes().length;
						if (columnWidth < length) {
							columnWidth = length;
						}
					}
				}
			}
			sheet.setColumnWidth(columnNum, columnWidth * 256);
		}
	}
}
