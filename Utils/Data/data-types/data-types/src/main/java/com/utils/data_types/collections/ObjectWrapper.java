package com.utils.data_types.collections;

import com.utils.string.StrUtils;

public class ObjectWrapper<
		ObjectT> {

	private ObjectT value;

	public ObjectWrapper() {
	}

	public ObjectWrapper(
			final ObjectT value) {

		this.value = value;
	}

	public boolean isNull() {
		return value == null;
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	public void setValue(
			final ObjectT value) {
		this.value = value;
	}

	public ObjectT getValue() {
		return value;
	}
}
