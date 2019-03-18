package com.gx.ksw.util;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 文本字符串的处理
 */
public class TxtUtil {
	/**
	 * 把传入的内容整理成字体串集合
	 *
	 * @param content
	 * @return
	 */
	public static List<String> converToString(String content) {
		for (String s : Arrays.asList("，", "。")) {
			content = content.replace(s, "#");
		}
		String[] split = content.split("#");
		return Arrays.stream(split).collect(Collectors.toList());
	}

	/**
	 * 提取关键字库和关键字中的关键字
	 *
	 * @param content
	 * @return
	 */
	public static List<String> parserKeyWord(String content) {
		// 判断是关键字库
		List<String> keywords;
		//wanghuidong modify 2018-12-19 优化前台字符串拼接,不再存在此情况.都是关键字,关键字...格式
		if (content.endsWith("】")) {
			List<String> stringList = extractMessage(content);
			String collect = stringList.stream().collect(Collectors.joining(","));
			keywords = Arrays.asList(collect.split(","));
		} else { // 判断是关键字
			keywords = Arrays.asList(content.split(","));
		}
		//wanghuidong modify 2018-12-17 对关键字的集合按照关键字长度倒序排序
		// Collections.sort(keywords, new SortByLengthComparator());
		// sgx 2018-12-24采用lambda表达式替代以前的继承接口的写法
		Collections.sort(keywords, (a, b) -> b.length() - a.length());
		return keywords;
	}

	/**
	 * 提取中括号中内容，忽略中括号中的中括号
	 *
	 * @param msg
	 * @return
	 */
	public static List<String> extractMessage(String msg) {
		List<String> list = new ArrayList<String>();
		int start = 0;
		int startFlag = 0;
		int endFlag = 0;
		for (int i = 0; i < msg.length(); i++) {
			if (msg.charAt(i) == '【') {
				startFlag++;
				if (startFlag == endFlag + 1) {
					start = i;
				}
			} else if (msg.charAt(i) == '】') {
				endFlag++;
				if (endFlag == startFlag) {
					list.add(msg.substring(start + 1, i));
				}
			}
		}
		return list;
	}

	public static List<String> listBoostrapTree(String boostrapCheck) {
		String str = "nodes,text,nodeId,parentId,selectable,state,checked,disabled,expanded,selected";
		List<String> collect = Stream.of(str.split(",")).sorted().collect(Collectors.toList());

		Pattern p1 = Pattern.compile("\"(.*?)\"");
		Matcher m = p1.matcher(boostrapCheck);
		ArrayList<String> list = new ArrayList<>();
		while (m.find()) {
			list.add(m.group().trim().replace("\"", ""));
		}
		System.out.println(list.toString());
		List<String> stringList = list.stream().distinct().sorted()
				.filter(s -> collect
						.stream()
						.noneMatch(s1 -> Objects.equals(s1, s))
				)
				.collect(Collectors.toList());
		return stringList;
	}

}
