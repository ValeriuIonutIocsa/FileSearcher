package com.utils.data_types.data_items.di_int;

public class DataItemUIntHex extends DataItemUInt {

	DataItemUIntHex(
			final int value) {

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
			str = "0x" + Integer.toUnsignedString(value, 16);
		} else {
			str = "";
		}
		return str;
	}
}
