package com.gx.ksw.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;

/**
 * EXCEL的基础的处理类
 */
public class POIUtil {
	/**
	 * 根据Cell类型设置数据,返回cell的值
	 *
	 * @param cell
	 * @return
	 */
	public static String getCellFormatValue(Cell cell) {
		// 原样输出cell中的内容
//		DataFormatter formatter = new DataFormatter();
//		formatter.formatCellValue(cell)
		String str;
		switch (cell.getCellType()) {
			case STRING:
				str = cell.getRichStringCellValue().getString();
				break;
			case NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					str = String.valueOf(cell.getDateCellValue());
				} else {
					str = String.valueOf(cell.getNumericCellValue());
				}
				break;
			case BOOLEAN:
				str = String.valueOf(cell.getBooleanCellValue());
				break;
			case FORMULA:
				try {
					str = String.valueOf(cell.getCellFormula());
				} catch (IllegalStateException e) {
					str = String.valueOf(cell.getNumericCellValue());
				}
				break;
			case BLANK:
				str = "";
				break;
			case ERROR:
				str = "";
				break;
			default:
				str = String.valueOf(cell.getStringCellValue());
		}
		if (str.length() > 0) {
			return str;
		}
		return str;
	}
}
