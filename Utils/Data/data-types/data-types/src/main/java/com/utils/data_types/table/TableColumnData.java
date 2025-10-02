package com.utils.data_types.table;

import java.io.Serial;
import java.io.Serializable;

public record TableColumnData(
		String name,
		String serializeName,
		double widthWeight,
		double minWidth,
		double maxWidth) implements Serializable {

	@Serial
	private static final long serialVersionUID = 1248266274147407246L;

	public double computeWidthRatio(
			final double widthWeightSum) {

		return widthWeight / widthWeightSum;
	}

	@Override
	public String toString() {
		return name;
	}
}
