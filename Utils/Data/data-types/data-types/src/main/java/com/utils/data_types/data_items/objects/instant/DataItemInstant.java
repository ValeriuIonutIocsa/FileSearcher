package com.utils.data_types.data_items.objects.instant;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;

import com.utils.data_types.data_items.AbstractDataItem;
import com.utils.string.StrUtils;

public class DataItemInstant extends AbstractDataItem<Instant> implements Comparable<DataItemInstant> {

	private final Instant value;

	DataItemInstant(
			final Instant value) {

		this.value = value;
	}

	@Override
	public void serializeToDataBase(
			final int index,
			final PreparedStatement preparedStatement) throws SQLException {

		final Timestamp timestamp = Timestamp.from(value);
		final Calendar calendar = Calendar.getInstance();
		preparedStatement.setTimestamp(index, timestamp, calendar);
	}

	@Override
	public int compareTo(
			final DataItemInstant other) {

		return value.compareTo(other.value);
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

		return StrUtils.createDisplayDateTimeString(value);
	}

	@Override
	public Instant getValue() {
		return value;
	}
}
