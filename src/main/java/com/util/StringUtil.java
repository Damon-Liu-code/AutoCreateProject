package com.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;


/**
 * 字符串工具类
 * @author 
 *
 */
public class StringUtil {

	/**
	 * 判断是否是空
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str){
		if(str==null||"".equals(str.trim())){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 判断是否不是空
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str){
		if((str!=null)&&!"".equals(str.trim())){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 格式化模糊查询
	 * @param str
	 * @return
	 */
	public static String formatLike(String str){
		if(isNotEmpty(str)){
			return "%"+str+"%";
		}else{
			return null;
		}
	}

	public static String convertToPinyin(String chineseText) {
		// 创建拼音转换输出格式对象
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);

		StringBuilder pinyinBuilder = new StringBuilder();
		try {
			// 遍历中文字符串的每个字符
			for (char c : chineseText.toCharArray()) {
				// 判断是否为中文字符
				if (Character.toString(c).matches("[\\u4E00-\\u9FA5]+")) {
					// 将中文字符转换为拼音，并添加到拼音字符串中
					String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c, format);
					if (pinyinArray != null && pinyinArray.length > 0) {
						pinyinBuilder.append(pinyinArray[0]);
					}
				} else {
					// 非中文字符直接添加到拼音字符串中
					pinyinBuilder.append(c);
				}
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}

		return pinyinBuilder.toString();
	}

}
