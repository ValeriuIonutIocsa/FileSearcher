package com.utils.data_types.data_items.di_boolean;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.utils.data_types.data_items.AbstractDataItem;

public class DataItemBoolean extends AbstractDataItem<Boolean> implements Comparable<DataItemBoolean> {

	private final boolean value;

	DataItemBoolean(
			final boolean value) {

		this.value = value;
	}

	@Override
	public void serializeToDataBase(
			final int index,
			final PreparedStatement preparedStatement) throws SQLException {
		preparedStatement.setBoolean(index, value);
	}

	@Override
	public int compareTo(
			final DataItemBoolean other) {
		return Boolean.compare(value, other.value);
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
		return String.valueOf(value);
	}

	@Override
	public Boolean getValue() {
		return value;
	}
}
