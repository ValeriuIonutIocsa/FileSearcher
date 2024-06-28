package com.utils.data_types.data_items.di_int;

public final class FactoryDataItemUIntHex {

	private FactoryDataItemUIntHex() {
	}

	public static DataItemUIntHex newInstance(
			final Integer value) {

		DataItemUIntHex dataItemUIntHex = null;
		if (value != null) {
			dataItemUIntHex = newInstance((int) value);
		}
		return dataItemUIntHex;
	}

	public static DataItemUIntHex newInstance(
			final int value) {

		return new DataItemUIntHex(value);
	}
}
