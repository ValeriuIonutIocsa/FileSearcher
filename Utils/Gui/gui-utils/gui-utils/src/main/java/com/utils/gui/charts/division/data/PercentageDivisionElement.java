package com.utils.gui.charts.division.data;

import com.utils.string.StrUtils;

import javafx.scene.paint.Color;

public record PercentageDivisionElement(
		String name,
		double percentage,
		Color color) {

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}
}
