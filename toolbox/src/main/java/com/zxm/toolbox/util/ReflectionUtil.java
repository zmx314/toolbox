package com.zxm.toolbox.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflectionUtil {
	/**
	 * 获取对象的所有属性名
	 * @param obj 对象
	 * @return 所有属性名
	 */
	public static List<String> getFieldName(Object obj) {
		Field[] fields = obj.getClass().getDeclaredFields();
		int fieldsLength = fields.length; 
		List<String> fieldNames = new ArrayList<String>();
		for (int i = 0; i < fieldsLength; i++) {
			fieldNames.add(fields[i].getName());
		}
		return fieldNames;
	}

	/**
	 * 根据属性名获取属性值
	 * @param fieldName 属性名
	 * @param obj 对象
	 * @return 属性值Object对象
	 */
	public static Object getFieldValueByName(String fieldName, Object obj) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getter = "get" + firstLetter + fieldName.substring(1);
			Method method = obj.getClass().getMethod(getter, new Class[] {});
			Object value = method.invoke(obj, new Object[] {});
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			;
			return null;
		}
	}

	/**
	 * 获取对象的所有属性的信息
	 * @param obj 对象
	 * @return 该对象所有属性的信息，每个Map中存放一个属性的类型、名称、值，key分别为type、name、value。
	 */
	public static List<Map<String, Object>> getFiledsInfo(Object obj) {
		Field[] fields = obj.getClass().getDeclaredFields();
		int fieldsLength = fields.length;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> infoMap = null;
		for (int i = 0; i < fieldsLength; i++) {
			infoMap = new HashMap<String, Object>();
			infoMap.put("type", fields[i].getType().toString());
			infoMap.put("name", fields[i].getName());
			infoMap.put("value", getFieldValueByName(fields[i].getName(), obj));
			list.add(infoMap);
		}
		return list;
	}

	/**
	 * 获取对象的所有属性的值
	 * @param obj 对象
	 * @return Object数组
	 */
	public static Object[] getFiledValues(Object obj) {
		List<String> fieldNames = getFieldName(obj);
		int fieldNamesLength = fieldNames.size();
		Object[] value = new Object[fieldNamesLength];
		for (int i = 0; i < fieldNamesLength; i++) {
			value[i] = getFieldValueByName(fieldNames.get(i), obj);
		}
		return value;
	}
}
