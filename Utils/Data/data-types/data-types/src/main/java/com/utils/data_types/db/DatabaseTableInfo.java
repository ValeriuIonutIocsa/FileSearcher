package com.utils.data_types.db;

import com.utils.string.StrUtils;

public record DatabaseTableInfo(
		String name,
		DatabaseTableColumn[] databaseTableColumnArray,
		String[] constraintArray) {

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}
}
