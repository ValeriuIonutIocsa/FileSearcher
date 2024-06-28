package com.utils.data_types.data_items.di_long;

public class DataItemULongHex extends DataItemULong {

	DataItemULongHex(
			final long value) {
		super(value);
	}

	@Override
	public String createCopyString() {
		return toString();
	}

	@Override
	public String toString() {

		final String str;
		if (value != -1) {
			str = "0x" + Long.toUnsignedString(value, 16);
		} else {
			str = "";
		}
		return str;
	}
}
