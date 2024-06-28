package com.utils.data_types.data_items.di_long;

public final class FactoryDataItemULongHex {

	private FactoryDataItemULongHex() {
	}

	public static DataItemULongHex newInstance(
			final Long value) {

		DataItemULongHex dataItemULongHex = null;
		if (value != null) {
			dataItemULongHex = newInstance((long) value);
		}
		return dataItemULongHex;
	}

	public static DataItemULongHex newInstance(
			final long value) {
		return new DataItemULongHex(value);
	}
}
