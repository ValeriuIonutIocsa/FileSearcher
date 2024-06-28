package com.utils.data_types.db;

import com.utils.string.StrUtils;

public record DatabaseTableColumn(
		String name,
		String type) {

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}
}
