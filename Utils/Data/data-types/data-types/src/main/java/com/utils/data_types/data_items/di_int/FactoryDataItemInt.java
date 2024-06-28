package com.utils.data_types.data_items.di_int;

public final class FactoryDataItemInt {

	private FactoryDataItemInt() {
	}

	public static DataItemInt newInstance(
			final Integer value) {

		DataItemInt dataItemInt = null;
		if (value != null) {
			dataItemInt = newInstance((int) value);
		}
		return dataItemInt;
	}

	public static DataItemInt newInstance(
			final int value) {

		return new DataItemInt(value);
	}
}
