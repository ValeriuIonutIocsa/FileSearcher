package com.utils.data_types.data_items.di_byte_array;

public final class FactoryDataItemByteArray {

	private FactoryDataItemByteArray() {
	}

	public static DataItemByteArray newInstance(
			final byte[] bytes) {

		DataItemByteArray dataItemByteArray = null;
		if (bytes != null) {
			dataItemByteArray = new DataItemByteArray(bytes);
		}
		return dataItemByteArray;
	}
}
