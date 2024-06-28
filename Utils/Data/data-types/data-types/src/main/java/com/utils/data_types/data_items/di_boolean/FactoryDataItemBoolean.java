package com.utils.data_types.data_items.di_boolean;

public final class FactoryDataItemBoolean {

	private FactoryDataItemBoolean() {
	}

	public static DataItemBoolean newInstance(
			final Boolean value) {

		DataItemBoolean dataItemBoolean = null;
		if (value != null) {
			dataItemBoolean = newInstance((boolean) value);
		}
		return dataItemBoolean;
	}

	public static DataItemBoolean newInstance(
			final boolean value) {
		return new DataItemBoolean(value);
	}
}
