package com.utils.data_types.table;

import java.io.Serial;
import java.io.Serializable;

public class TableColumnData implements Serializable {

	@Serial
    private static final long serialVersionUID = 1248266274147407246L;

	private final String name;
	private final String serializeName;
	private final double widthWeight;
	private final double minWidth;
	private final double maxWidth;

	public TableColumnData(
			final String name,
			final String serializeName,
			final double widthWeight) {
		this(name, serializeName, widthWeight, Double.NaN, Double.NaN);
	}

	public TableColumnData(
			final String name,
			final String serializeName,
			final Double widthWeight,
			final double minWidth,
			final double maxWidth) {

		this.name = name;
		this.serializeName = serializeName;
		this.widthWeight = widthWeight;
		this.minWidth = minWidth;
		this.maxWidth = maxWidth;
	}

	public double computeWidthRatio(
			final double widthWeightSum) {
		return widthWeight / widthWeightSum;
	}

	@Override
	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	public String getSerializeName() {
		return serializeName;
	}

	public double getWidthWeight() {
		return widthWeight;
	}

	public double getMinWidth() {
		return minWidth;
	}

	public double getMaxWidth() {
		return maxWidth;
	}
}
