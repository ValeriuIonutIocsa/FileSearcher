package com.utils.data_types.data_items.di_int;

import com.utils.string.StrUtils;

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

		return StrUtils.positiveIntToHexString(value);
	}
}
