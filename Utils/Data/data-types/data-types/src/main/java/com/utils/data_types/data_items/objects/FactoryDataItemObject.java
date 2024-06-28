package com.utils.data_types.data_items.objects;

public final class FactoryDataItemObject {

	private FactoryDataItemObject() {
	}

	public static <
			ObjectT> DataItemObject<ObjectT> newInstance(
					final ObjectT value) {
		return newInstance(value, Integer.MAX_VALUE);
	}

	public static <
			ObjectT> DataItemObject<ObjectT> newInstance(
					final ObjectT value,
					final int maxLength) {

		DataItemObject<ObjectT> dataItemObject = null;
		if (value != null) {
			dataItemObject = new DataItemObject<>(value, maxLength);
		}
		return dataItemObject;
	}
}
