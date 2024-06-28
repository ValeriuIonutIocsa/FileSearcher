package com.utils.data_types.data_items.di_int;

public final class FactoryDataItemUInt {

	private FactoryDataItemUInt() {
	}

	public static DataItemUInt newInstance(
			final Integer value) {

		DataItemUInt dataItemUInt = null;
		if (value != null) {
			dataItemUInt = newInstance((int) value);
		}
		return dataItemUInt;
	}

	public static DataItemUInt newInstance(
			final int value) {

		return new DataItemUInt(value);
	}
}
