package com.utils.data_types.data_items.di_byte_array;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.utils.data_types.data_items.AbstractDataItem;
import com.utils.string.converters.ConverterByteArray;

public class DataItemByteArray extends AbstractDataItem<byte[]> implements Comparable<DataItemByteArray> {

	private final byte[] bytes;

	DataItemByteArray(
			final byte[] bytes) {

		this.bytes = bytes;
	}

	@Override
	public void serializeToDataBase(
			final int index,
			final PreparedStatement preparedStatement) throws SQLException {
		preparedStatement.setBytes(index, bytes);
	}

	@Override
	public String createCopyString() {
		return ConverterByteArray.byteArrayToString(bytes);
	}

	@Override
	public int compareTo(
			final DataItemByteArray other) {
		return Integer.compare(bytes.length, other.bytes.length);
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
		return "byte[" + bytes.length + "]";
	}

	@Override
	public byte[] getValue() {
		return bytes;
	}
}
