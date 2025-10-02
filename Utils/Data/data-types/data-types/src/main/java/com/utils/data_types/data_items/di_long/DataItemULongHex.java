package com.utils.data_types.data_items.di_long;

import com.utils.string.StrUtils;

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

		return StrUtils.positiveLongToHexString(value);
	}
}
