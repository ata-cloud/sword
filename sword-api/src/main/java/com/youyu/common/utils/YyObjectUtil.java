/**
 * 
 */
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
package com.youyu.common.utils;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;


public class YyObjectUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(YyObjectUtil.class);
	// private static final Logger LOGGER = Logger.getLogger(YyObjectUtil.class);

	/**
	 * 将对象中的值拷贝到map中
	 *
	 * @param object
	 * @return Map
	 */
	public static Map build(Object object) {
		if (null == object) {
			return null;
		}
		if (object instanceof Map) {
			return (Map) object;
		}
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			PropertyDescriptor pds[] = PropertyUtils.getPropertyDescriptors(object.getClass());
			for (PropertyDescriptor pd : pds) {
				Method read = pd.getReadMethod();
				if (read != null) {
					Object value = read.invoke(object);
					if (value != null && value.getClass() != Class.class) {
						map.put(pd.getDisplayName(), value);
					}
				}
			}
			return map;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 *
	 * @param obj 判断对象
	 * @param fieldNameList 对象的属性名称 list
	 * @return
	 */
	public static boolean isEmptys(Object obj, List<String> fieldNameList) throws Exception {
		if (obj == null || fieldNameList == null || fieldNameList.size() == 0) {
			return true;
		}
		for (String fieldName : fieldNameList) {
			Object fieldValue = getFieldValue(obj, fieldName);
			if (isEmpty(fieldValue)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isEmptys(Object... objs) {
		for (Object obj : objs) {
			if (isEmpty(obj)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isEmpty(Object obj) {
		if (obj == null) {
			return true;
		}

		if (obj.getClass().isArray()) {
			return Array.getLength(obj) == 0;
		}
		if (obj instanceof CharSequence) {
			return ((CharSequence) obj).length() == 0;
		}
		if (obj instanceof Collection) {
			return ((Collection) obj).isEmpty();
		}
		if (obj instanceof Map) {
			return ((Map) obj).isEmpty();
		}
		if (obj instanceof String) {
			return "".equals(obj);
		}
		if (obj instanceof Long) {
			return 0L == ((Long) obj).longValue();
		}
		if (obj instanceof Integer) {
			return 0 == ((Integer) obj).intValue();
		}

		// else
		return false;
	}

	/**
	 * 批量将对象中的值拷贝到map中
	 *
	 * @param  objects
	 * @return List<Map>
	 */
	public static List<Map> buildBatch(List<? extends Object> objects) {
		List<Map> list = new ArrayList<Map>();
		for (Object object : objects) {
			Map map = build(object);
			if (null != map) {
				list.add(map);
			}
		}
		return list;
	}

	private static Object getFieldValue(Object object, String fieldName) throws Exception {
		PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(object, fieldName);
		if (propertyDescriptor != null) {
			Method reader = propertyDescriptor.getReadMethod();
			if (reader != null) {
				Object value = reader.invoke(object);
				if (value != null && value.getClass() != Class.class) {
					return value;
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param entityClass
	 * @param object
	 * @return
	 */
	public static <T> T build(Class<T> entityClass, Object object) {

		if (null == entityClass || null == object) {
			return null;
		}
		if (object instanceof Map) {
			return buildFromMap(entityClass, (Map) object);
		}

		try {
			T obj = entityClass.newInstance();

			PropertyDescriptor pd1[] = PropertyUtils.getPropertyDescriptors(entityClass);
			PropertyDescriptor pd2[] = PropertyUtils.getPropertyDescriptors(object);

			Map<String, PropertyDescriptor> map = new HashMap<String, PropertyDescriptor>();
			for (PropertyDescriptor pd : pd1) {
				map.put(pd.getDisplayName(), pd);
			}
			for (PropertyDescriptor pd : pd2) {
				PropertyDescriptor _pd = map.get(pd.getDisplayName());
				if (null != _pd) {
					Method write = _pd.getWriteMethod();
					Method read = pd.getReadMethod();
					if (null != write && null != read && _pd.getPropertyType().isAssignableFrom(pd.getPropertyType())) {
						Object value = read.invoke(object);
						if (null == value) {
							continue;
						}
						write.invoke(obj, value);
					}
				}
			}
			return obj;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 
	 * @param entityClass
	 * @param
	 * @return
	 */
	public static <T> T buildFromMap(Class<T> entityClass, Map object) {

		if (null == entityClass || null == object) {
			return null;
		}

		try {
			T obj = entityClass.newInstance();

			PropertyDescriptor pd1[] = PropertyUtils.getPropertyDescriptors(entityClass);
			Map<String, PropertyDescriptor> map = new HashMap<String, PropertyDescriptor>();
			for (PropertyDescriptor pd : pd1) {
				map.put(pd.getDisplayName(), pd);
			}

			for (Object key : object.keySet()) {
				Object value = object.get(key);
				if (key == null || value == null) {
					continue;
				}
				PropertyDescriptor _pd = map.get(key.toString());
				if (null != _pd) {
					Method write = _pd.getWriteMethod();
					if (null != write && _pd.getPropertyType().isAssignableFrom(value.getClass())) {
						write.invoke(obj, value);
					}
				}
			}
			return obj;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 
	 * @param entityClass
	 * @param objects
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static <T> List<T> buildBatch(Class<T> entityClass, List objects) {
		List<T> list = new ArrayList<T>();
		for (Object object : objects) {
			T t = build(entityClass, object);
			if (null != t) {
				list.add(t);
			}
		}

		return list;
	}

	public static String listToString(List<String> list, String separator) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i)).append(separator);
		}
		return sb.toString().substring(0, sb.toString().length() - 1);
	}

}
