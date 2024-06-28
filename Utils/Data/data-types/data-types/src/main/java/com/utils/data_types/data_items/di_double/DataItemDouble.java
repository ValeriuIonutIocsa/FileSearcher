package com.utils.data_types.data_items.di_double;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;

import com.utils.data_types.data_items.AbstractDataItem;
import com.utils.string.StrUtils;

public class DataItemDouble extends AbstractDataItem<Double> implements Comparable<DataItemDouble> {

	private final double value;
	private final int digits;
	private final String defaultString;

	DataItemDouble(
			final double value,
			final int digits,
			final String defaultString) {

		this.value = value;
		this.digits = digits;
		this.defaultString = defaultString;
	}

	@Override
	public void serializeToDataBase(
			final int index,
			final PreparedStatement preparedStatement) throws SQLException {

		preparedStatement.setDouble(index, value);
	}

	@Override
	public String createCopyString() {

		String str = StrUtils.doubleToString(value, 0, digits, false);
		if (defaultString != null && StringUtils.isBlank(str)) {
			str = defaultString;
		}
		return str;
	}

	@Override
	public int compareTo(
			final DataItemDouble other) {

		final int result;
		if (Double.isNaN(value)) {
			if (Double.isNaN(other.value)) {
				result = 0;
			} else {
				result = -1;
			}
		} else {
			if (Double.isNaN(other.value)) {
				result = 1;
			} else {
				result = Double.compare(value, other.value);
			}
		}
		return result;
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public boolean equals(
			final Object obj) {

		final boolean result;
		if (getClass().isInstance(obj)) {
			result = compareTo(getClass().cast(obj)) == 0;
		} else {
			result = false;
		}
		return result;
	}

	@Override
	public String toString() {

		String str = StrUtils.doubleToString(value, 0, digits, true);
		if (defaultString != null && StringUtils.isBlank(str)) {
			str = defaultString;
		}
		return str;
	}

	@Override
	public Double getValue() {
		return value;
	}
}
