package com.utils.data_types.percentages;

public final class PercentageUtils {

	private PercentageUtils() {
	}

	public static boolean checkInvalidPercentage(
			final double d) {
		return Double.isNaN(d) || d < 0 || d > 100;
	}
}
