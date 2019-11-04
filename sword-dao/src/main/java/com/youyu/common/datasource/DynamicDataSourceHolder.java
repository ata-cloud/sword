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
package com.youyu.common.datasource;

/**
 * 动态数据源持有者
 *
 * @author xiongchengwei
 */
public final class DynamicDataSourceHolder {

	/**
	 * 动态数据源存储
	 */
	private static final ThreadLocal<DynamicDataSourceGlobal> DYNAMIC_DATA_SOURCE_GLOBAL_THREAD_LOCAL = new ThreadLocal<>();

	private DynamicDataSourceHolder() {
		//
	}

	/**
	 * 存放数据源
	 * @param dataSource 数据源
	 */
	public static void putDataSource(DynamicDataSourceGlobal dataSource) {
		DYNAMIC_DATA_SOURCE_GLOBAL_THREAD_LOCAL.set(dataSource);
	}

	/**
	 * 获取数据源
	 *
	 * @return the data source
	 */
	public static DynamicDataSourceGlobal getDataSource() {
		return DYNAMIC_DATA_SOURCE_GLOBAL_THREAD_LOCAL.get();
	}

	/**
	 * 清除数据源
	 */
	public static void clearDataSource() {
		DYNAMIC_DATA_SOURCE_GLOBAL_THREAD_LOCAL.remove();
	}

}
