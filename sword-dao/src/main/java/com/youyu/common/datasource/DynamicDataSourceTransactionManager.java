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

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;

import javax.sql.DataSource;

/**
 * 动态数据源事务管理器
 * @author xiongchengwei
 */
public class DynamicDataSourceTransactionManager extends DataSourceTransactionManager {

	public DynamicDataSourceTransactionManager(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	protected void doBegin(Object transaction, TransactionDefinition definition) {

		// 设置数据源
		boolean readOnly = definition.isReadOnly();
		// 只读事务到读库
		if (readOnly) {
			DynamicDataSourceHolder.putDataSource(DynamicDataSourceGlobal.READ);
		} else {
			DynamicDataSourceHolder.putDataSource(DynamicDataSourceGlobal.WRITE);
		}
		super.doBegin(transaction, definition);
	}

	@Override
	protected void doCleanupAfterCompletion(Object transaction) {
		super.doCleanupAfterCompletion(transaction);
		// 清理本地线程的数据源
		DynamicDataSourceHolder.clearDataSource();
	}
}
