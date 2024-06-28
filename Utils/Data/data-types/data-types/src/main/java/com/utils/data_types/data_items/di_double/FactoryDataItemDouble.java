package com.utils.data_types.data_items.di_double;

public final class FactoryDataItemDouble {

	private FactoryDataItemDouble() {
	}

	public static DataItemDouble newInstance(
			final Double value,
			final int digits) {

		DataItemDouble dataItemDouble = null;
		if (value != null) {
			dataItemDouble = newInstance((double) value, digits);
		}
		return dataItemDouble;
	}

	public static DataItemDouble newInstance(
			final double value,
			final int digits) {

		return new DataItemDouble(value, digits, null);
	}

    public static DataItemDouble newInstance(
            final double value,
            final int digits,
            final String defaultValue) {

        return new DataItemDouble(value, digits, defaultValue);
    }
}
