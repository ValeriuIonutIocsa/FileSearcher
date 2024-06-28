package com.utils.data_types.data_items.di_int;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.utils.data_types.data_items.AbstractDataItem;
import com.utils.string.StrUtils;

public class DataItemUInt extends AbstractDataItem<Integer> implements Comparable<DataItemUInt> {

	final int value;

	DataItemUInt(
			final int value) {

		this.value = value;
	}

	@Override
	public void serializeToDataBase(
			final int index,
			final PreparedStatement preparedStatement) throws SQLException {

		preparedStatement.setInt(index, value);
	}

	@Override
	public String createCopyString() {

		return StrUtils.positiveIntToString(value, false);
	}

	@Override
	public int compareTo(
			final DataItemUInt other) {

		return Integer.compare(value, other.value);
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

		return StrUtils.positiveIntToString(value, true);
	}

	@Override
	public Integer getValue() {
		return value;
	}
}
