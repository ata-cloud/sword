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

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * The class provides common methods to copy value between simple Bean to codyy base model.
 *
 * @author Liheng(liheng@codyy.com, liheeng@gmail.com)
 * @date 2017/9/25
 */
public class BaseBeanUtils {

	public static final String ENUM_METHOD_VALUE_OF = "valueOf";

	public static final String ENUM_METHOD_BY_NAME = "byName";

	public static final String ENUM_METHOD_BY_CODE = "byCode";

	public static final String ENUM_METHOD_NAME = "name";

	public static final String ENUM_METHOD_GET_CODE = "getCode";

	private static DateConverter dateConverter = new DateConverter();

	static {
		dateConverter.setUseLocaleFormat(false);
		dateConverter.setLocale(Locale.CHINESE);
		dateConverter.setPatterns(
				new String[] { DateUtil.YYYY_MM_DD, DateUtil.YYYY_MM_DD_HHMMSS, DateUtil.YYYYMMDD, DateUtil.YYYYMMDD_HHMMSS, DateUtil.YYYYMMDDHHMMSS });
	}

	/**
	 * Recursively copy source bean object into target object based on property name.
	 *
	 * @param source
	 * @param targetClass
	 * @param copyProcesor
	 * @param <S>
	 * @param <T>
	 * @return
	 */
	public static <S, T> T copy(S source, Class<T> targetClass, BeanCopyProcessor<S, T> copyProcesor) {
		T targetObj = copy(source, targetClass);
		if (source != null && copyProcesor != null) {
			copyProcesor.copy(source, targetObj);
		}

		return targetObj;
	}

	public static <S, T> T copyWithLambda(S source, Class<T> targetClass, BeanConvert beanConvert) {
		T targetObj = copy(source, targetClass);
		if (source != null && beanConvert != null) {
			beanConvert.convert(source, targetObj);
		}

		return targetObj;
	}
	/**
	 * Recursively copy source bean object into target object based on property name.
	 *
	 * @param source
	 * @param targetClass
	 * @param <T>
	 * @return <code>null</code> if fail to copy.
	 */
	public static <T> T copy(Object source, Class<T> targetClass) {
		if (source == null) {
			return null;
		}

		Assert.notNull(source, "Source must not be null");
		Assert.notNull(targetClass, "Target must not be null");

		// If the target class doesn't contain any property, return source
		// object if it is assignable to target or return null.
		if (String.class.isAssignableFrom(targetClass) || Number.class.isAssignableFrom(targetClass) || Date.class.isAssignableFrom(targetClass)
				|| Enum.class.isAssignableFrom(targetClass) || Boolean.class.isAssignableFrom(targetClass)) {
			if (targetClass.isAssignableFrom(source.getClass())) {
				return (T) source;
			} else {
				return null;
			}
		}

		// Create target object.
		T target = null;
		try {
			target = targetClass.newInstance();
		} catch (Throwable ex) {
			throw new FatalBeanException("Could not copy property '" + targetClass.getName() + "' from source to target", ex);
		}

		// Copy properties from source to target recursively.
		copyProperties(source, target);

		return target;
	}

	public static <T> T copyWithLambda(Object source, Class<T> targetClass) {
		return copy( source, targetClass);
	}
	/**
	 * Recursively copy propeties between source object and target object based on property name.
	 *
	 * @param source
	 * @param target
	 * @param copyProcesor
	 */
	public static void copyProperties(Object source, Object target, BeanCopyProcessor<Object, Object> copyProcesor) {
		copyProperties(source, target);
		if (copyProcesor != null) {
			copyProcesor.copy(source, target);
		}
	}

	/**
	 * Recursively copy propeties between source object and target object based on property name.
	 *
	 * @param source
	 * @param target
	 */
	public static void copyProperties(Object source, Object target) {
		if (source == null) {
			return;
		}
		Class<?> targetClass = (Class<?>) target.getClass();
		PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(targetClass);

		try {

			for (PropertyDescriptor targetPd : targetPds) {
				Method writeMethod = targetPd.getWriteMethod();
				// if (writeMethod != null && (ignoreList == null ||
				// !ignoreList.contains(targetPd.getName()))) {
				if (writeMethod != null) {
					PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
					if (sourcePd != null) {
						Method readMethod = sourcePd.getReadMethod();
						if (readMethod != null) {

							Object value = null;
							try {
								if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
									readMethod.setAccessible(true);
								}
								value = readMethod.invoke(source);

								if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
									writeMethod.setAccessible(true);
								}
							} catch (InvocationTargetException ex) {
								throw new FatalBeanException("Could not copy property '" + targetPd.getName() + "' from source to target", ex);
							}

							Class tgtPdClass = writeMethod.getParameterTypes()[0];
							Class srcPdClass = readMethod.getReturnType();
							boolean isAssignable = ClassUtils.isAssignable(tgtPdClass, srcPdClass);

							copyProperty(target, targetPd, writeMethod, value, readMethod, isAssignable);
						}
					}
				}
			}

		} catch (Throwable ex) {
			throw new FatalBeanException("Could not copy property '" + targetClass.getName() + "' from source to target", ex);
		}
	}

	/**
	 * Copy list.
	 *
	 * @param sourceList
	 * @param targetClass
	 * @param copyProcesor
	 * @param <S>
	 * @param <T>
	 * @return
	 */
	public static <S, T> List<T> copy(List<S> sourceList, Class<T> targetClass, BeanCopyProcessor<S, T> copyProcesor) {
		if (sourceList == null) {
			return null;
		}
		List<T> list = new ArrayList<T>();
		T targetObj = null;
		for (Object o : sourceList) {
			if (copyProcesor != null) {
				targetObj = copy((S) o, targetClass, copyProcesor);
			} else {
				targetObj = copy(o, targetClass);
			}
			list.add(targetObj);
		}
		return list;
	}


	public static <S, T> List<T> copyWithLambda(List<S> sourceList, Class<T> targetClass, BeanConvert beanConvert) {
		if (sourceList == null) {
			return null;
		}
		List<T> list = new ArrayList<T>();
		T targetObj = null;
		for (Object o : sourceList) {
			if (beanConvert != null) {
				targetObj = copyWithLambda((S) o, targetClass, beanConvert);
			} else {
				targetObj = copyWithLambda(o, targetClass);
			}
			list.add(targetObj);
		}
		return list;
	}

	/**
	 * Copy list.
	 *
	 * @param sourceList
	 * @param targetClass
	 * @param <S>
	 * @param <T>
	 * @return
	 */
	public static <S, T> List<T> copy(List<S> sourceList, Class<T> targetClass) {
		return copy(sourceList, targetClass, (BeanCopyProcessor<S, T>) null);
	}

	/**
	 * Copy map.
	 *
	 * @param sourceMap
	 * @param targetClass
	 * @param <T>
	 * @return
	 */
	public static <T> Map<String, T> copy(Map<String, ?> sourceMap, Class<T> targetClass) {
		return copy(sourceMap, targetClass, null);
	}

	/**
	 * Copy map.
	 *
	 * @param sourceMap
	 * @param targetClass
	 * @param copyProcesor
	 * @param <T>
	 * @return
	 */
	public static <T> Map<String, T> copy(Map<String, ?> sourceMap, Class<T> targetClass, BeanCopyProcessor<Object, T> copyProcesor) {
		if (sourceMap == null) {
			return null;
		}
		Map<String, T> map = new HashMap<>();
		T targetObj = null;
		for (Map.Entry<String, ?> entry : sourceMap.entrySet()) {
			if (copyProcesor != null) {
				targetObj = map.put(entry.getKey(), copy(entry.getValue(), targetClass, copyProcesor));
			} else {
				targetObj = map.put(entry.getKey(), copy(entry.getValue(), targetClass));
			}
		}
		return map;
	}

	private static void copyProperty(Object target, PropertyDescriptor targetPd, Method writeMethod, Object value, Method readMethod, boolean isAssignable)
			throws InvocationTargetException, IllegalAccessException, ClassNotFoundException {
		Class srcPdClass = readMethod.getReturnType();
		Class tgtPdClass = writeMethod.getParameterTypes()[0];

		// If target property descriptor is Object type or generic type, then
		// use type of source property descriptor as target tyep.
		if (tgtPdClass.getName().equalsIgnoreCase("java.lang.Object")) {
			tgtPdClass = srcPdClass;
		}
		if (isAssignable) {

			// List - List
			if (List.class.isAssignableFrom(srcPdClass) && List.class.isAssignableFrom(tgtPdClass)) {
				Class srcPdGenicClass = Class.forName(((ParameterizedType) readMethod.getGenericReturnType()).getActualTypeArguments()[0].getTypeName());
				Class tgtPdGenicClass = Class
						.forName(((ParameterizedType) writeMethod.getGenericParameterTypes()[0]).getActualTypeArguments()[0].getTypeName());
				writeMethod.invoke(target, copyListToList(srcPdGenicClass, tgtPdGenicClass, (List) value));
			}
			// Map - Map
			else if (Map.class.isAssignableFrom(srcPdClass) && Map.class.isAssignableFrom(tgtPdClass)) {
				Class srcPdGenicClass = Class.forName(((ParameterizedType) readMethod.getGenericReturnType()).getActualTypeArguments()[1].getTypeName());
				Class tgtPdGenicClass = Class
						.forName(((ParameterizedType) writeMethod.getGenericParameterTypes()[0]).getActualTypeArguments()[1].getTypeName());
				writeMethod.invoke(target, copyMapToMap(srcPdGenicClass, tgtPdGenicClass, (Map) value));
			} else {
				copyCommonProperty(target, targetPd, tgtPdClass, writeMethod, value);
			}
		} else {
			// java.util.Date cases, include sql date, sql time and sql
			// timestamp cases.
			if (Date.class.isAssignableFrom(srcPdClass) || Date.class.isAssignableFrom(tgtPdClass)) {
				copyDate(target, targetPd, writeMethod, value, srcPdClass, tgtPdClass);
			}
			// Enum - String
			if (Enum.class.isAssignableFrom(srcPdClass) && String.class.isAssignableFrom(tgtPdClass)) {
				copyEnumToString(target, targetPd, writeMethod, getValueOfEnum(srcPdClass, value));
			}
			// String - Enum
			else if (String.class.isAssignableFrom(srcPdClass) && Enum.class.isAssignableFrom(tgtPdClass) && value != null) {
				copyStringToEnum(target, targetPd, writeMethod, getEnumObj(tgtPdClass, value));
			}
			// Number - String
			else if (Number.class.isAssignableFrom(srcPdClass) && String.class.isAssignableFrom(tgtPdClass)) {
				copyNumberToString(target, targetPd, writeMethod, (Number) value);
			}
			// String - Number
			else if (String.class.isAssignableFrom(srcPdClass) && Number.class.isAssignableFrom(tgtPdClass)) {
				copyStringToNumber(target, targetPd, writeMethod, value, tgtPdClass);
			}

			// Boolean - String
			else if (Boolean.class.isAssignableFrom(srcPdClass) && String.class.isAssignableFrom(tgtPdClass)) {
				copyBooleanToString(target, targetPd, writeMethod, value);
			}
			// String - Boolean
			else if (String.class.isAssignableFrom(srcPdClass) && Boolean.class.isAssignableFrom(tgtPdClass)) {
				copyStringToBoolean(target, targetPd, writeMethod, value);
			}
			// Map - Bean(Object)
			else if (Map.class.isAssignableFrom(srcPdClass) && BeanUtils.getPropertyDescriptors(tgtPdClass).length > 1) {
				writeMethod.invoke(target, value == null ? null : copyMapToBean(srcPdClass, readMethod, tgtPdClass, (Map<?, ?>) value));
			}
			// Bean(Object) - Map
			else if (BeanUtils.getPropertyDescriptors(srcPdClass).length > 1 && Map.class.isAssignableFrom(tgtPdClass)) {
				writeMethod.invoke(value == null ? null : copyBeanToMap(srcPdClass, value));
			}
		}
	}

	private static void copyBooleanToString(Object target, PropertyDescriptor targetPd, Method writeMethod, Object value) {
		try {
			writeMethod.invoke(target, value == null ? null : ((Boolean) value).toString());
		} catch (Throwable ex) {
			throw new FatalBeanException("Could not copy property '" + targetPd.getName() + "' from source to target", ex);
		}
	}

	private static void copyStringToBoolean(Object target, PropertyDescriptor targetPd, Method writeMethod, Object value) {
		try {
			writeMethod.invoke(target, value == null ? null : Boolean.valueOf(value.toString()));
		} catch (Throwable ex) {
			throw new FatalBeanException("Could not copy property '" + targetPd.getName() + "' from source to target", ex);
		}
	}

	private static void copyDate(Object target, PropertyDescriptor targetPd, Method writeMethod, Object value, Class srcPdClass, Class tgtPdClass) {
		// java.util.Date - String
		if (org.apache.commons.lang.StringUtils.equals(Date.class.getName(), srcPdClass.getName()) && String.class.isAssignableFrom(tgtPdClass)) {
			try {
				writeMethod.invoke(target, convertDateToString(writeMethod, (Date) value));
			} catch (Throwable ex) {
				throw new FatalBeanException("Could not copy property '" + targetPd.getName() + "' from source to target", ex);
			}
		}
		// String - java.util.Date
		else if (String.class.isAssignableFrom(srcPdClass) && org.apache.commons.lang.StringUtils.equals(Date.class.getName(), tgtPdClass.getName())) {
			try {
				writeMethod.invoke(target, dateConverter.convertToType(Date.class, value));
			} catch (Throwable ex) {
				throw new FatalBeanException("Could not copy property '" + targetPd.getName() + "' from source to target", ex);
			}
		}
		// java.sql.Date.class - String
		else if (org.apache.commons.lang.StringUtils.equals(java.sql.Date.class.getName(), srcPdClass.getName()) && String.class.isAssignableFrom(tgtPdClass)) {
			try {
				writeMethod.invoke(target, convertDateToString(writeMethod, (Date) value));
			} catch (Throwable ex) {
				throw new FatalBeanException("Could not copy property '" + targetPd.getName() + "' from source to target", ex);
			}
		}
		// Strig - java.sql.Date.class
		else if (String.class.isAssignableFrom(srcPdClass) && org.apache.commons.lang.StringUtils.equals(java.sql.Date.class.getName(), tgtPdClass.getName())) {
			try {
				writeMethod.invoke(target, dateConverter.convertToType(java.sql.Date.class, value));
			} catch (Throwable ex) {
				throw new FatalBeanException("Could not copy property '" + targetPd.getName() + "' from source to target", ex);
			}
		}
		// java.sql.Time.class - String
		else if (org.apache.commons.lang.StringUtils.equals(java.sql.Time.class.getName(), srcPdClass.getName()) && String.class.isAssignableFrom(tgtPdClass)) {
			try {
				writeMethod.invoke(target, convertDateToString(writeMethod, (Date) value));
			} catch (Throwable ex) {
				throw new FatalBeanException("Could not copy property '" + targetPd.getName() + "' from source to target", ex);
			}
		}
		// Strig - java.sql.Time.class
		else if (String.class.isAssignableFrom(srcPdClass) && org.apache.commons.lang.StringUtils.equals(java.sql.Time.class.getName(), tgtPdClass.getName())) {
			try {
				writeMethod.invoke(target, dateConverter.convertToType(java.sql.Time.class, value));
			} catch (Throwable ex) {
				throw new FatalBeanException("Could not copy property '" + targetPd.getName() + "' from source to target", ex);
			}
		}
		// java.sql.Timestamp.class - String
		else if (org.apache.commons.lang.StringUtils.equals(java.sql.Timestamp.class.getName(), srcPdClass.getName())
				&& String.class.isAssignableFrom(tgtPdClass)) {
			try {
				writeMethod.invoke(target, convertDateToString(writeMethod, (Date) value));
			} catch (Throwable ex) {
				throw new FatalBeanException("Could not copy property '" + targetPd.getName() + "' from source to target", ex);
			}
		}
		// Strig - java.sql.Timestamp.class
		else if (String.class.isAssignableFrom(srcPdClass)
				&& org.apache.commons.lang.StringUtils.equals(java.sql.Timestamp.class.getName(), tgtPdClass.getName())) {
			try {
				writeMethod.invoke(target, dateConverter.convertToType(java.sql.Timestamp.class, value));
			} catch (Throwable ex) {
				throw new FatalBeanException("Could not copy property '" + targetPd.getName() + "' from source to target", ex);
			}
		}
	}

	private static <T, R extends Date> String convertDateToString(Method writeMethod, R value) throws Throwable {
		if (value == null) {
			return null;
		}

		RegexpMatch patternAnno = null;
		Annotation[][] annotations = writeMethod.getParameterAnnotations();
		if (annotations.length > 0) {
			for (Annotation a : annotations[0]) {
				if (a instanceof RegexpMatch) {
					patternAnno = (RegexpMatch) a;
				}
			}
		}
		String dateStr = null;
		if (patternAnno != null) {
			dateStr = dateConverter.convertToString(value, patternAnno.regexp());
		} else {
			dateStr = dateConverter.convertToString(value);
		}
		return dateStr;
	}

	private static Map<String, Object> copyMapToMap(Class srcPdClass, Class tgtPdClass, Map<String, Object> value) {
		if (value == null) {
			return null;
		}

		Map<String, Object> tgtMap = new HashMap<>();
		for (Map.Entry<String, Object> entry : value.entrySet()) {
			tgtMap.put(entry.getKey(), copy(entry.getValue(), tgtPdClass));
		}
		return tgtMap;
	}

	private static List<?> copyListToList(Class srcPdClass, Class tgtPdClass, List value) {
		if (value == null) {
			return null;
		}

		List<Object> tgtList = new ArrayList<>();
		for (Iterator<?> iter = value.iterator(); iter.hasNext();) {
			tgtList.add(copy(iter.next(), tgtPdClass));
		}
		return tgtList;
	}

	private static Map<?, ?> copyBeanToMap(Class srcClass, Object bean) {
		if (bean == null) {
			return null;
		}

		Map<String, Object> map = new HashMap<>();
		PropertyDescriptor[] sourcePds = BeanUtils.getPropertyDescriptors(srcClass);
		for (PropertyDescriptor sourcePd : sourcePds) {
			if (sourcePd != null) {
				Method readMethod = sourcePd.getReadMethod();
				if (readMethod != null) {

					Object value = null;
					try {
						if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
							readMethod.setAccessible(true);
						}
						value = readMethod.invoke(bean);

						map.put(sourcePd.getName(), value);

					} catch (Throwable ex) {
						throw new FatalBeanException("Could not copy property '" + srcClass.getName() + "' from source to target", ex);
					}

				}
			}
		}
		return map;
	}

	private static Object copyMapToBean(Class srcClass, Method readMethod, Class targetClass, Map<?, ?> valueMap) {
		if (valueMap == null) {
			return null;
		}

		try {
			Object target = targetClass.newInstance();

			PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(targetClass);
			for (PropertyDescriptor targetPd : targetPds) {
				Method writeMethod = targetPd.getWriteMethod();
				// if (writeMethod != null && (ignoreList == null ||
				// !ignoreList.contains(targetPd.getName()))) {
				if (writeMethod != null) {
					if (valueMap.containsKey(targetPd.getName())) {
						Object value = valueMap.get(targetPd.getName());

						Class tgtPdClass = writeMethod.getParameterTypes()[0];
						Class srcPdClass = value.getClass();
						boolean isAssignable = ClassUtils.isAssignable(tgtPdClass, srcPdClass);
						copyProperty(target, targetPd, writeMethod, value, readMethod, isAssignable);
					}
				}
			}

			return target;
		} catch (Throwable ex) {
			throw new FatalBeanException("Could not copy property '" + targetClass.getName() + "' from source to target", ex);
		}
	}

	private static void copyCommonProperty(Object target, PropertyDescriptor targetPd, Class tgtPdClass, Method writeMethod, Object value) {
		try {
			writeMethod.invoke(target, copy(value, tgtPdClass));
		} catch (Throwable ex) {
			throw new FatalBeanException("Could not copy property '" + targetPd.getName() + "' from source to target", ex);
		}
	}




	private static void copyStringToNumber(Object target, PropertyDescriptor targetPd, Method writeMethod, Object value, Class tgtPdClass) {
		try {
			Object tgtObj = null;
			if (value == null) {
				writeMethod.invoke(target, tgtObj);
			} else {
				if (Integer.class.isAssignableFrom(tgtPdClass)) {
					tgtObj = Integer.valueOf(value.toString());
				} else if (Long.class.isAssignableFrom(tgtPdClass)) {
					tgtObj = Long.valueOf(value.toString());
				} else if (Float.class.isAssignableFrom(tgtPdClass)) {
					tgtObj = Float.valueOf(value.toString());
				} else if (Double.class.isAssignableFrom(tgtPdClass)) {
					tgtObj = Double.valueOf(value.toString());
				} else if (BigInteger.class.isAssignableFrom(tgtPdClass)) {
					tgtObj = new BigInteger(value.toString());
				} else if (BigDecimal.class.isAssignableFrom(tgtPdClass)) {
					tgtObj = new BigDecimal(value.toString());
				}
				writeMethod.invoke(target, tgtObj);
			}
		} catch (Throwable ex) {
			throw new FatalBeanException("Could not copy property '" + targetPd.getName() + "' from source to target", ex);
		}
	}

	private static void copyNumberToString(Object target, PropertyDescriptor targetPd, Method writeMethod, Number value) {
		try {
			writeMethod.invoke(target, value != null ? value.toString() : null);
		} catch (Throwable ex) {
			throw new FatalBeanException("Could not copy property '" + targetPd.getName() + "' from source to target", ex);
		}
	}

	private static Enum getEnumObj(Class tgtPdClass, Object value) {

		// Get string value of enum object.
		Method tgtMethod = null;
		try {
			tgtMethod = tgtPdClass.getMethod(ENUM_METHOD_BY_CODE, String.class);
		} catch (Exception e) {
			// Do nothing.
		}
		if (ObjectUtils.isEmpty(tgtMethod)) {
			try {
				tgtMethod = tgtPdClass.getMethod(ENUM_METHOD_BY_NAME, String.class);
			} catch (Exception e) {
				// Do nothing.
			}
		}
		if (ObjectUtils.isEmpty(tgtMethod)) {
			try {
				tgtMethod = tgtPdClass.getMethod(ENUM_METHOD_VALUE_OF, String.class);
			} catch (Exception e) {
				// Do nothing.
			}
		}

		try {
			if (!ObjectUtils.isEmpty(tgtMethod)) {
				return (Enum) tgtMethod.invoke(tgtPdClass, value);
			}
		} catch (Throwable ex) {
			throw new FatalBeanException("Could not copy property '" + tgtPdClass.getName() + "' from source to target", ex);
		}
		return null;
	}

	private static void copyStringToEnum(Object target, PropertyDescriptor targetPd, Method writeMethod, Enum enumObj) {
		try {
			writeMethod.invoke(target, enumObj);
		} catch (Throwable ex) {
			throw new FatalBeanException("Could not copy property '" + targetPd.getName() + "' from source to target", ex);
		}
	}

	private static String getValueOfEnum(Class srcPdClass, Object value) {
		if (value == null) {
			return null;
		}

		// Get string value of enum object.
		Method srcPdMethod = null;
		try {
			srcPdMethod = srcPdClass.getMethod(ENUM_METHOD_GET_CODE);
		} catch (Throwable e) {
			// DO NOTHING.
		}

		if (ObjectUtils.isEmpty(srcPdMethod)) {
			try {
				srcPdMethod = srcPdClass.getMethod(ENUM_METHOD_NAME);
			} catch (Throwable e) {
				// DO NOTHING.
			}
		}

		if (ObjectUtils.isEmpty(srcPdMethod)) {
			return null;
		}

		try {
			return (String) srcPdMethod.invoke(value);

		} catch (Throwable ex) {
			throw new FatalBeanException("Could not copy property '" + srcPdClass.getName() + "' from source to target", ex);
		}

	}

	private static void copyEnumToString(Object target, PropertyDescriptor targetPd, Method writeMethod, String value) {
		try {
			writeMethod.invoke(target, value);
		} catch (Throwable ex) {
			throw new FatalBeanException("Could not copy property '" + targetPd.getName() + "' from source to target", ex);
		}
	}

	private static void copyProperties(Object source, Object target, Class<?> editable, String... ignoreProperties) throws BeansException {

		Assert.notNull(source, "Source must not be null");
		Assert.notNull(target, "Target must not be null");

		Class<?> actualEditable = target.getClass();
		if (editable != null) {
			if (!editable.isInstance(target)) {
				throw new IllegalArgumentException(
						"Target class [" + target.getClass().getName() + "] not assignable to Editable class [" + editable.getName() + "]");
			}
			actualEditable = editable;
		}

		PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);
		List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);

		for (PropertyDescriptor targetPd : targetPds) {
			Method writeMethod = targetPd.getWriteMethod();
			if (writeMethod != null && (ignoreList == null || !ignoreList.contains(targetPd.getName()))) {
				PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
				if (sourcePd != null) {
					Method readMethod = sourcePd.getReadMethod();
					if (readMethod != null) {
						Class targetClass = writeMethod.getParameterTypes()[0];
						Class sourceClass = readMethod.getReturnType();
						boolean isAssignable = ClassUtils.isAssignable(targetClass, sourceClass);

						if (isAssignable) {
							try {
								if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
									readMethod.setAccessible(true);
								}
								Object value = readMethod.invoke(source);
								if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
									writeMethod.setAccessible(true);
								}
								writeMethod.invoke(target, value);
							} catch (Throwable ex) {
								throw new FatalBeanException("Could not copy property '" + targetPd.getName() + "' from source to target", ex);
							}
						}
					}
				}
			}
		}
	}

	public static interface BeanCopyProcessor<S, T> {
		public void copy(S s, T t);
	}
}
