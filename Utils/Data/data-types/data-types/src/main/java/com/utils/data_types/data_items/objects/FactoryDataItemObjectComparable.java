package com.utils.data_types.data_items.objects;

public final class FactoryDataItemObjectComparable {

	private FactoryDataItemObjectComparable() {
	}

	public static <
			ObjectT extends Comparable<ObjectT>> DataItemObjectComparable<ObjectT> newInstance(
					final ObjectT value) {
		return newInstance(value, Integer.MAX_VALUE);
	}

	public static <
			ObjectT extends Comparable<ObjectT>> DataItemObjectComparable<ObjectT> newInstance(
					final ObjectT value,
					final int maxLength) {

		DataItemObjectComparable<ObjectT> dataItemObjectComparable = null;
		if (value != null) {
			dataItemObjectComparable = new DataItemObjectComparable<>(value, maxLength);
		}
		return dataItemObjectComparable;
	}
}
