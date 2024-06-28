package com.utils.data_types.data_items.di_long;

public final class FactoryDataItemULong {

	private FactoryDataItemULong() {
	}

	public static DataItemULong newInstance(
			final Long value) {

		DataItemULong dataItemULong = null;
		if (value != null) {
			dataItemULong = newInstance((long) value);
		}
		return dataItemULong;
	}

	public static DataItemULong newInstance(
			final long value) {
		return new DataItemULong(value);
	}
}
