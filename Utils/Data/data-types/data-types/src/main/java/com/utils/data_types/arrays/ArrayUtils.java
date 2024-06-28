package com.utils.data_types.arrays;

import java.util.List;

public final class ArrayUtils {

	private ArrayUtils() {
	}

	public static int[] listToArray(
			final List<Integer> intList) {

		final int[] intArray = new int[intList.size()];
		for (int i = 0; i < intList.size(); i++) {

			final int n = intList.get(i);
			intArray[i] = n;
		}
		return intArray;
	}
}
