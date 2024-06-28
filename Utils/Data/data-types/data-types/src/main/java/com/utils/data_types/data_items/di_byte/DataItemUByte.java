package com.utils.data_types.data_items.di_byte;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.utils.data_types.data_items.AbstractDataItem;
import com.utils.string.StrUtils;

public class DataItemUByte extends AbstractDataItem<Byte> implements Comparable<DataItemUByte> {

	final byte value;

	DataItemUByte(
			final byte value) {

		this.value = value;
	}

	@Override
	public void serializeToDataBase(
			final int index,
			final PreparedStatement preparedStatement) throws SQLException {
		preparedStatement.setByte(index, value);
	}

	@Override
	public String createCopyString() {

		final int unsignedIntValue = Byte.toUnsignedInt(value);
		return StrUtils.positiveIntToString(unsignedIntValue, false);
	}

	@Override
	public int compareTo(
			final DataItemUByte other) {
		return Integer.compareUnsigned(Byte.toUnsignedInt(value), Byte.toUnsignedInt(other.value));
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

		final int unsignedIntValue = Byte.toUnsignedInt(value);
		return StrUtils.positiveIntToString(unsignedIntValue, true);
	}

	@Override
	public Byte getValue() {
		return value;
	}
}
