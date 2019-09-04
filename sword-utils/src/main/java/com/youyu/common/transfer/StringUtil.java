/*
 *    Copyright 2018-2019 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.youyu.common.transfer;

import org.apache.commons.lang.StringUtils;

import java.text.MessageFormat;
import java.util.List;
import java.util.Random;

public class StringUtil {
	public static final String STR_NUM_PREFIX = "00000000000000000000000000000000000000000000";

	// 去除 数字0.字母O,o防止阅读歧义
	public static char[] CHAR_ARRAY = new char[] { '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'm', 'n',
			'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S',
			'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

	/**
	 * 生成length位长度的随机数 generateRandomStr
	 *
	 * @param length
	 */
	public static String generateRandomStr(int length) {
		char[] charArr = new char[length];
		Random rand = new Random();// 创建Random类的对象rand
		for (int i = 0; i < length; i++) {
			charArr[i] = CHAR_ARRAY[rand.nextInt(CHAR_ARRAY.length - 1)];
		}
		return String.valueOf(charArr);
	}

	/**
	 * 将包含{0}，{1}等占位符的String 替换对应的参数 返回替换后的字符串
	 * formatParamterString
	 * @param paramterString
	 * @param paramters
	 * @return
	 */
	public static String formatParamterString(String paramterString, Object[] paramters) {
		return MessageFormat.format(paramterString, paramters);
	}

	/**
	 * 统计某字符串sub在 text中出现的次数
	 * @param text
	 * @param sub
	 * @return
	 */
	public static int countSubstr(String text, String sub) {
		if (StringUtils.isBlank(text) || StringUtils.isBlank(sub)) {
			return 0;
		}
		int count = 0, start = 0;
		while ((start = text.indexOf(sub, start)) >= 0) {
			start += sub.length();
			count++;
		}
		return count;
	}

	/**
	 * Generate number string with specified width and append '0' if number width is short than specified width,
	 * otherwise return number string with actual width.
	 *
	 * @param number
	 * @param width
	 * @return
	 */
	public static String generateNumberStr(Number number, int width) {
		String numStr = number.toString();
		if (numStr.length() > width) {
			return numStr;
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append(STR_NUM_PREFIX);
			sb.append(numStr);
			return sb.substring(sb.length() - width);
		}
	}

	public static String listToString(List<String> list) {
		if (list == null) {
			return null;
		}
		StringBuilder result = new StringBuilder();
		boolean first = true;
		// 第一个前面不拼接","
		for (String string : list) {
			if (first) {
				first = false;
			} else {
				result.append(",");
			}
			result.append(string);
		}
		return result.toString();
	}
}
