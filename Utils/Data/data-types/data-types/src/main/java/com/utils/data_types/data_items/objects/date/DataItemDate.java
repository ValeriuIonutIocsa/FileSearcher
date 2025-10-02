package com.utils.data_types.data_items.objects.date;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;

import com.utils.data_types.data_items.AbstractDataItem;
import com.utils.string.StrUtils;

public class DataItemDate extends AbstractDataItem<LocalDate> implements Comparable<DataItemDate> {

	private final LocalDate value;

	DataItemDate(
			final LocalDate value) {

		this.value = value;
	}

	@Override
	public void serializeToDataBase(
			final int index,
			final PreparedStatement preparedStatement) throws SQLException {

		final LocalDateTime localDateTime = value.atStartOfDay();
		final ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
		final Instant instant = zonedDateTime.toInstant();
		final Timestamp timestamp = Timestamp.from(instant);
		final Calendar calendar = Calendar.getInstance();
		preparedStatement.setTimestamp(index, timestamp, calendar);
	}

	@Override
	public int compareTo(
			final DataItemDate other) {

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

		return StrUtils.createDisplayDateString(value);
	}

	@Override
	public LocalDate getValue() {
		return value;
	}
}
