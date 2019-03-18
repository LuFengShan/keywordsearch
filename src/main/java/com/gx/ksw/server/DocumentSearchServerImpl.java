package com.gx.ksw.server;

import com.gx.ksw.entities.KeyWordMapContent;
import com.gx.ksw.util.POIUtil;
import com.gx.ksw.util.TxtUtil;
import io.swagger.annotations.Api;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 文件内容检索服务层
 *
 * @author sgx
 * @version V1.0
 */
@Service
public class DocumentSearchServerImpl {
	private static Logger log = LoggerFactory.getLogger(DocumentSearchServerImpl.class);

	/**
	 * 封装数据
	 *
	 * @param file         要检索的文件
	 * @param keyWordGroup 关键字集合
	 * @return Map<关键字数据集合> list> map;
	 */
	public Map<String, List<KeyWordMapContent>> searchFile(MultipartFile file, String keyWordGroup) {
		// 1. 获取关键字集合
		List<String> keyWords;
		//wanghuidong modify 2018-12-19 优化前台字符串拼接,不再存在此情况.都是关键字,关键字...格式
		if (keyWordGroup.startsWith("[{")) {
			keyWords = TxtUtil.listBoostrapTree(keyWordGroup);
		} else {
			keyWords = TxtUtil.parserKeyWord(keyWordGroup);
		}
		// 1. 根据文件的路径，判断文件的后缀，取出类型
		String filePath = file.getOriginalFilename();
		String ext = filePath.substring(filePath.lastIndexOf("."));
		log.info("文件路径 ： " + filePath + "，文件后缀 ： " + ext);
		// 2. 根据是excel还是word来取出值
		Workbook wb;
		List<KeyWordMapContent> list = null;
		try {
			InputStream is = file.getInputStream();
			switch (ext) {
				case ".xls":
					wb = new HSSFWorkbook(is);
					list = excelSearch(wb, keyWords);
					break;
				case ".et":
					wb = new HSSFWorkbook(is);
					list = excelSearch(wb, keyWords);
					break;
				case ".xlsx":
					wb = new XSSFWorkbook(is);
					list = excelSearch(wb, keyWords);
					break;
				case ".pdf":
					list = pdfSearch(is, keyWords);
					break;
				case ".doc":
					list = docSearch(is, keyWords);
					break;
				case ".docx":
					list = docxSearch(is, keyWords);
					break;
				case ".wps":
					list = docSearch(is, keyWords);
					break;
				default: // 默认是txt文件
					list = txtSearch(is, keyWords);
			}
		} catch (FileNotFoundException e) {
			log.error("FileNotFoundException", e);
		} catch (IOException e) {
			log.error("IOException", e);
		}
		// 根据关键字来分组
		Map<String, List<KeyWordMapContent>> collect = list.stream()
				.collect(Collectors.groupingBy(KeyWordMapContent::getKeyWord));
		return collect;
	}

	/**
	 * 处理word-03中的搜索
	 *
	 * @param is
	 * @param keyWords
	 * @return
	 * @throws IOException
	 */
	public List<KeyWordMapContent> docSearch(InputStream is, List<String> keyWords) throws IOException {
		// 1. 读取文件
		HWPFDocument doc = new HWPFDocument(is);
		WordExtractor extractor = new WordExtractor(doc);
		String content = extractor.getText();
		content = content.replace("\r\n", "，");
		content = content.replace("\t", "，");
		// 4. text方式复用
		Stream<String> stream = TxtUtil.converToString(content).stream();
		List<KeyWordMapContent> keyWordMapContents = keyWordMapContentList(stream, keyWords);
		extractor.close();
		doc.close();
		return keyWordMapContents;
	}

	/**
	 * 读取docx文件
	 *
	 * @author sgx
	 * POI在读写word docx文件时是通过xwpf模块来进行的，其核心是XWPFDocument。一个XWPFDocument代表一个docx文档，其可以用来读docx文档，也可以用来写docx文档。
	 * XWPFDocument中主要包含下面这几种对象：
	 * XWPFParagraph：代表一个段落。
	 * XWPFRun：代表具有相同属性的一段文本。
	 * XWPFTable：代表一个表格。
	 * XWPFTableRow：表格的一行。
	 * XWPFTableCell：表格对应的一个单元格。
	 */
	public List<KeyWordMapContent> docxSearch(InputStream is, List<String> keyWords) throws IOException {
		// 1. 读取文件
		XWPFDocument docx = new XWPFDocument(is);
		XWPFWordExtractor extractor = new XWPFWordExtractor(docx);
		String text = extractor.getText(); // 提取所有的文本（段落加表格中的）
		text = text.replace("\n", "，");
		text = text.replace("\t", "，");
		Stream<String> stream = TxtUtil.converToString(text).stream();
		List<KeyWordMapContent> keyWordMapContents = keyWordMapContentList(stream, keyWords);
		extractor.close();
		docx.close();
		return keyWordMapContents;
	}

	/**
	 * pdf检索
	 *
	 * @param is
	 * @param keyWords
	 * @return
	 * @throws IOException
	 */
	public List<KeyWordMapContent> pdfSearch(InputStream is, List<String> keyWords) throws IOException {
		KeyWordMapContent kmc;
//		RandomAccessRead accessRead = new RandomAccessFile(new File(
//				"C:\\Users\\guangxudadi\\Desktop\\test1.pdf"), "rw");
//		PDFParser parser = new PDFParser(accessRead); // 创建PDF解析器
//		parser.parse(); // 执行PDF解析过程
		PDDocument pdfdocument = PDDocument.load(is); // 获取解析器的PDF文档对象
//		PDFTextStripper pdfstripper = new PDFTextStripper(); // 生成PDF文档内容剥离器
//		String contenttxt = pdfstripper.getText(pdfdocument); // 利用剥离器获取文档
//		System.out.println(contenttxt);

		// 获取页码
		int pages = pdfdocument.getNumberOfPages();

		// 读文本内容
		PDFTextStripper stripper;
		// 设置按顺序输出
		List<KeyWordMapContent> keyWordMapContents;
		List<KeyWordMapContent> list = new ArrayList<>(2000);
		//wanghuidong modify 2018-12-19 读取文本,检索文本,逻辑更改
		for (int i = 1; i < pages + 1; i++) {
			stripper = new PDFTextStripper();
			stripper.setSortByPosition(true);
			stripper.setStartPage(i);
			stripper.setEndPage(i);
			// 1.取出本页的内容
			String content = stripper.getText(pdfdocument);
			//2.wanghuidong modify 将文本中的换行符替换为空,将换行的文字拼接,以检测关键字换行的情况
			content = content.replaceAll(System.getProperty("line.separator"), "");
			//3.wanghuidong modify 将文本使用txt文件的处理方式,按标点符号分割处理
			Stream<String> stream = TxtUtil.converToString(content).stream();
			//原处理逻辑 2.根据换行符来分割内容content,成字符数组
			//String[] split = content.split(System.getProperty("line.separator"));
			//4.wanghuidong modify 对关键字和内容进行映射匹配
			keyWordMapContents = keyWordMapContentList(stream, keyWords);
			// 4.在包含关键字的句子前追加上标识“第*页”
			for (KeyWordMapContent k : keyWordMapContents) {
				if (!k.getContent().startsWith("第")
						&& !Objects.equals("页：", k.getContent().substring(2, 3))) {
					k.setContent("第" + i + "页：" + k.getContent());
				}
				// 5.把封装成型后的内容追加到最终集合结果中
				list.add(k);
			}
		}

//		Map<String, List<KeyWordMapContent>> collect = list.stream()
//				.collect(Collectors.groupingBy(KeyWordMapContent::getKeyWord));
//		collect.entrySet().stream().forEach(System.out::println);
		pdfdocument.close();
		// accessRead.close();

		return list;
	}

	/**
	 * 处理EXCEL
	 *
	 * @param wb
	 * @return
	 */
	public List<KeyWordMapContent> excelSearch(Workbook wb, List<String> keyWords) {
		List<KeyWordMapContent> list = new ArrayList<>(1000);
		KeyWordMapContent kmc;
		// 遍历整个excel把所有的关键字的数据存起来
		String sheetName;
		String cellFormatValue;
		for (Sheet sheet : wb) {
			// 获取sheet的名字
			sheetName = sheet.getSheetName();
			for (Row row : sheet) {
				for (Cell cell : row) {
					// Do something here
					cellFormatValue = POIUtil.getCellFormatValue(cell);
					for (String k : keyWords) {
						if (cellFormatValue.contains(k)) {
							kmc = new KeyWordMapContent(k,
									sheetName + ": " + cell.getAddress().toString() + " 单元格");
							list.add(kmc);
						}
					}
				}
			}
		}
		return list;
	}

	/**
	 * <p>检索后缀为txt文件中的关键字</p>
	 *
	 * @param is       输入流
	 * @param keyWords 关键字集合
	 * @return
	 * @throws IOException
	 * @version 1.0
	 * @since 1.0
	 */
	public List<KeyWordMapContent> txtSearch(InputStream is, List<String> keyWords) throws IOException {
		// 1. 开始读取txt文件中的内容
		StringBuilder sb = new StringBuilder();
		InputStreamReader inputReader = new InputStreamReader(is);
		BufferedReader bf = new BufferedReader(inputReader);
		// 1.1 按行读取字符串
		String str;
		while ((str = bf.readLine()) != null) {
			sb.append(str);
		}
		// 1.2 读取txt文件完毕
		// 2 把内容拆分完毕,筛选出包含关键字的句子, 存入关键字和内容的映射对象
		Stream<String> stream = TxtUtil.converToString(sb.toString()).stream();
		List<KeyWordMapContent> keyWordMapContents = keyWordMapContentList(stream, keyWords);
		return keyWordMapContents;
	}

	/**
	 * <p>分标出包含关键字的句子</p>
	 *
	 * @param stream   分析完成的句子的集合流（list.stream();）
	 * @param keyWords 关键字集合
	 * @return List<KeyWordMapContent>
	 */
	public List<KeyWordMapContent> keyWordMapContentList(Stream<String> stream, List<String> keyWords) {
		List<KeyWordMapContent> list = new ArrayList<>(1000);
		long count = stream
				.filter(con -> {
					// 如果这句话没有关键字，我们就返回false
					boolean flag = false;
					//临时存储标记后的关键字提示文本,避免多关键字底色冲突重合
					String conTemp = "";
					// 如果这句话包含关键字，我们就返回true
					for (String key : keyWords) {
						if (con.contains(key)) {
							//wanghuidong modify 2018-12-07 替换文本中的关键字为黄色底纹
							conTemp = con.replaceAll(key, "<span " +
									"style='background-color:#ffff00'>" + key + "</span>");
							// 如果有关键字，把关键字和对应的内容存入集合中
							list.add(new KeyWordMapContent(key, conTemp));
							flag = true;
						}
					}
					return flag;
				}).count(); // 结束流
		log.info("包含关键字的句子数量 ： " + count);
		return list;
	}


	/**
	 * 对 PDF文档中每页内容处理
	 * 对关键字截取前10后10长度字符串.
	 * 以下逻辑未使用,留作备份
	 *
	 * @param a        pdf文档中每页内容
	 * @param keyWords 检索的关键字
	 * @return 返回每一个关键字对应的前后内容
	 * 仅为方法逻辑代码,代码待优化
	 */
	public List<KeyWordMapContent> pageContentProcess(String a, List<String> keyWords) {
		List<KeyWordMapContent> returnList = new ArrayList<>(1000);
		//临时存储标记后的关键字提示文本,避免多关键字底色冲突重合
		String conTemp = "";
		for (String b : keyWords) {//遍历关键字
			String[] c = a.split(b);//按照关键字分割
			String d = "";//临时存放拼接内容
			if (a.contains(b)) {//存在关键字
				for (int i = 1; i < c.length; i++) {//遍历分割后的每段文字
					if (i < c.length) {//不遍历最后一个数组内容,拼接关键字后面的内容
						if (c[i].length() < 10) {//关键字后面的内容不足10位拼接
							d = b + c[i];
						} else {//关键字后面的内容>10位长度,截取文字拼接
							d = b + c[i].substring(0, 10);
						}
					}
					if (c[i - 1].length() < 10) {//关键字前面的内容不足10位拼接
						d = c[i - 1] + d;
					} else {//关键字前面的内容>10位长度,截取文字拼接
						d = c[i - 1].substring(0, 10) + d;
					}
					//wanghuidong modify 2018-12-07 替换文本中的关键字为黄色底纹
					conTemp = d.replaceAll(b, "<span " + "style='background-color:#ffff00'>" + b + "</span>");
					//如果有关键字，把关键字和对应的内容存入集合中
					returnList.add(new KeyWordMapContent(b, conTemp));
				}
				//处理结尾部分为关键字的字符串
				if (a.lastIndexOf(b) + b.length() == a.length()) {
					String end10 = "";
					String end = "";
					if (a.lastIndexOf(b) >= 10) {//文本内容长度>=10位
						//截取文字末尾的10位
						end10 = a.substring(a.lastIndexOf(b) - 10 + b.length(), a.length() - b.length());
					} else {
						end10 = a.substring(0, a.length() - b.length());
					}
					//截取后的10位字符串依然存在关键字,则截取关键字后面的部分
					if (end10.contains(b)) {
						end = end10.substring(end10.lastIndexOf(b) + b.length()) + b;
					}
					//wanghuidong modify 2018-12-07 替换文本中的关键字为黄色底纹
					conTemp = end.replaceAll(b, "<span style='background-color:#ffff00'>" + b +
							"</span>");
					//如果有关键字，把关键字和对应的内容存入集合中
					returnList.add(new KeyWordMapContent(b, conTemp));
				}
			}
		}
		//log.info("包含关键字的句子数量 ： " + count);
		return returnList;
	}

	public static void main(String[] args) {
		String a = "铁塔这是铁塔测试文字铁塔不知道这样铁塔了多少次呢铁塔";
		List<String> keyWords = new ArrayList<>();
		keyWords.add("铁塔");
		keyWords.add("测试");
		keyWords.add("内蒙古");
		keyWords.add("少");
		List<KeyWordMapContent> returnList = new ArrayList<>(1000);
		//临时存储标记后的关键字提示文本,避免多关键字底色冲突重合
		String conTemp = "";
		for (String b : keyWords) {//遍历关键字
			String[] c = a.split(b);//按照关键字分割
			String d = "";//临时存放拼接内容
			if (a.contains(b)) {//存在关键字
				for (int i = 1; i < c.length; i++) {//遍历分割后的每段文字
					if (i < c.length) {//不遍历最后一个数组内容,拼接关键字后面的内容
						if (c[i].length() < 10) {//关键字后面的内容不足10位拼接
							d = b + c[i];
						} else {//关键字后面的内容>10位长度,截取文字拼接
							d = b + c[i].substring(0, 10);
						}
					}
					if (c[i - 1].length() < 10) {//关键字前面的内容不足10位拼接
						d = c[i - 1] + d;
					} else {//关键字前面的内容>10位长度,截取文字拼接
						d = c[i - 1].substring(0, 10) + d;
					}
					System.out.println("关键字<" + b + ">的检索结果:" + d);
					//wanghuidong modify 2018-12-07 替换文本中的关键字为黄色底纹
					conTemp = d.replaceAll(b, "<span " + "style='background-color:#ffff00'>" + b + "</span>");
					//如果有关键字，把关键字和对应的内容存入集合中
					returnList.add(new KeyWordMapContent(b, conTemp));
				}
				//处理结尾部分为关键字的字符串
				if (a.lastIndexOf(b) + b.length() == a.length()) {
					String end10 = "";
					if (a.lastIndexOf(b) >= 10) {//文本内容长度>=10位
						//截取文字末尾的10位
						end10 = a.substring(a.lastIndexOf(b) - 10 + b.length(), a.length() - b.length());
					} else {
						end10 = a.substring(0, a.length() - b.length());
					}
					//截取后的10位字符串依然存在关键字,则截取关键字后面的部分
					if (end10.contains(b)) {
						d = end10.substring(end10.lastIndexOf(b) + b.length()) + b;
					}
					System.out.println("结尾关键字的检索结果:" + d);
					//wanghuidong modify 2018-12-07 替换文本中的关键字为黄色底纹
					conTemp = d.replaceAll(b, "<span style='background-color:#ffff00'>" + b +
							"</span>");
					//如果有关键字，把关键字和对应的内容存入集合中
					returnList.add(new KeyWordMapContent(b, conTemp));
				}
			}
		}
	}
}
