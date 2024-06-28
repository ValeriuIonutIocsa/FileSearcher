package com.utils.data_types.data_items.di_byte;

public class DataItemUByteHex extends DataItemUByte {

	DataItemUByteHex(
			final byte value) {
		super(value);
	}

	@Override
	public String createCopyString() {
		return toString();
	}

	@Override
	public String toString() {

		final int unsignedIntValue = Byte.toUnsignedInt(value);
		return "0x" + Integer.toUnsignedString(unsignedIntValue, 16);
	}
}
