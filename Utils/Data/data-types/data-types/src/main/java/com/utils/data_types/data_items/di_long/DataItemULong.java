package com.utils.data_types.data_items.di_long;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.utils.data_types.data_items.AbstractDataItem;
import com.utils.string.StrUtils;

public class DataItemULong extends AbstractDataItem<Long> implements Comparable<DataItemULong> {

	final long value;

	DataItemULong(
			final long value) {

		this.value = value;
	}

	@Override
	public void serializeToDataBase(
			final int index,
			final PreparedStatement preparedStatement) throws SQLException {

		preparedStatement.setLong(index, value);
	}

	@Override
	public String createCopyString() {

		return StrUtils.positiveLongToString(value, false);
	}

	@Override
	public int compareTo(
			final DataItemULong other) {

		return Long.compare(value, other.value);
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
		return StrUtils.positiveLongToString(value, true);
	}

	@Override
	public Long getValue() {
		return value;
	}
}
