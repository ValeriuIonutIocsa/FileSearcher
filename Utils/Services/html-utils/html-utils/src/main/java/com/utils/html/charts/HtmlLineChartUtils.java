package com.utils.html.charts;

import java.util.List;

import com.utils.annotations.ApiMethod;

public final class HtmlLineChartUtils {

	private HtmlLineChartUtils() {
	}

	@ApiMethod
	public static String createChartLabels(
			final List<String> labelList) {

		final StringBuilder sbChartLabels = new StringBuilder();
		for (final String label : labelList) {

			if (!sbChartLabels.isEmpty()) {
				sbChartLabels.append(", ");
			}
			sbChartLabels.append('\'').append(label).append('\'');
		}
		return sbChartLabels.toString();
	}

	@ApiMethod
	public static String createChartValues(
			final List<? extends Number> valueList) {

		final StringBuilder sbChartValues = new StringBuilder();
		for (final Number value : valueList) {

			if (!sbChartValues.isEmpty()) {
				sbChartValues.append(", ");
			}
			sbChartValues.append(value);
		}
		return sbChartValues.toString();
	}
}
