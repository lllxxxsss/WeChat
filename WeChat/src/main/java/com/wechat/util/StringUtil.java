package com.wechat.util;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 * 字符串工具类
 * @author GJ
 *
 */
public class StringUtil {
	
	/**
	 * 判断对象或对象数组中每一个对象是否为空: 对象为null，字符序列长度为0，集合类、Map为empty
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNullOrEmpty(Object obj) {
		if (obj == null)
			return true;

		if (obj instanceof CharSequence)
			return ((CharSequence) obj).length() == 0;

		if (obj instanceof Collection)
			return ((Collection) obj).isEmpty();

		if (obj instanceof Map)
			return ((Map) obj).isEmpty();

		if (obj instanceof Object[]) {
			Object[] object = (Object[]) obj;
			boolean empty = true;
			for (int i = 0; i < object.length; i++)
				if (!isNullOrEmpty(object[i])) {
					empty = false;
					break;
				}
			return empty;
		}
		return false;
	}
	
	/**
	 * 获取UUID
	 * @return
	 * @throws Exception
	 */
	public static String getUUID() {

		String uuid = UUID.randomUUID().toString(); // 获取UUID并转化为String对象
		uuid = uuid.replace("-", "");
		return uuid;
	}
}
