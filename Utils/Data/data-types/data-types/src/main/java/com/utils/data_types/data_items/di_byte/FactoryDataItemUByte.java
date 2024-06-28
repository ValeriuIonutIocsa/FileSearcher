package com.utils.data_types.data_items.di_byte;

public final class FactoryDataItemUByte {

	private FactoryDataItemUByte() {
	}

	public static DataItemUByte newInstance(
			final Byte value) {

		DataItemUByte dataItemUByte = null;
		if (value != null) {
			dataItemUByte = newInstance((byte) value);
		}
		return dataItemUByte;
	}

	public static DataItemUByte newInstance(
			final byte value) {
		return new DataItemUByte(value);
	}
}
