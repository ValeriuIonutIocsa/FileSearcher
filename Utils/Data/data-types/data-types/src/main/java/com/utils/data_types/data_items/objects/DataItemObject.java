package com.utils.data_types.data_items.objects;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.utils.data_types.data_items.AbstractDataItem;
import com.utils.log.Logger;

public class DataItemObject<
		ObjectT> extends AbstractDataItem<ObjectT>
		implements Comparable<DataItemObject<?>> {

	protected final ObjectT value;
	private final int maxLength;

	public DataItemObject(
			final ObjectT value,
			final int maxLength) {

		this.value = value;
		this.maxLength = maxLength;
	}

	@Override
	public void serializeToDataBase(
			final int index,
			final PreparedStatement preparedStatement) throws SQLException {

		final String serializedDataBaseString = createSerializedDataBaseString(index);
		preparedStatement.setString(index, serializedDataBaseString);
	}

	private String createSerializedDataBaseString(
			final int index) {

		String valueString = value.toString();
		if (valueString.length() > maxLength) {

			Logger.printWarning("value exceeds max length of the database column " + index + ":" +
					System.lineSeparator() + valueString);
			valueString = valueString.substring(0, maxLength);
		}
		return valueString;
	}

	@Override
	public int compareTo(
			final DataItemObject<?> other) {

		final String str = toString();
		final String otherStr = other.toString();
		return str.compareTo(otherStr);
	}

	@Override
	public int hashCode() {

		return value.hashCode();
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
		return value.toString();
	}

	@Override
	public ObjectT getValue() {
		return value;
	}
}
