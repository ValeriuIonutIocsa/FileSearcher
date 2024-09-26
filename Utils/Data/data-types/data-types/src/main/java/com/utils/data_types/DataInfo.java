package com.utils.data_types;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import com.utils.data_types.table.TableColumnData;
import com.utils.string.StrUtils;

public record DataInfo(
		String option,
		String displayName,
		String xmlRootElementTagName,
		String xmlDataElementTagName,
		int totalColumnWidth,
		TableColumnData[] columnsData,
		TableColumnData[] columnsTable) implements Serializable {

	@Serial
	private static final long serialVersionUID = 3078827620097666118L;

	public DataInfo cloneOtherDisplayName(
			final String displayName) {

		return new DataInfo(option, displayName, xmlRootElementTagName, xmlDataElementTagName,
				-1, columnsData, columnsTable);
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public boolean equals(
			final Object o) {

		boolean result = false;
		if (o instanceof final DataInfo other) {
			result = Objects.equals(option, other.option);
		}
		return result;
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}
}
