package com.utils.data_types.data_items.di_byte;

public final class FactoryDataItemUByteHex {

	private FactoryDataItemUByteHex() {
	}

	public static DataItemUByteHex newInstance(
			final Byte value) {

		DataItemUByteHex dataItemUByteHex = null;
		if (value != null) {
			dataItemUByteHex = newInstance((byte) value);
		}
		return dataItemUByteHex;
	}

	public static DataItemUByteHex newInstance(
			final byte value) {
		return new DataItemUByteHex(value);
	}
}
