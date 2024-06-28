package com.utils.data_types.collections;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Map;

import com.utils.annotations.ApiMethod;

public final class CollectionUtils {

	private CollectionUtils() {
	}

	@ApiMethod
	public static int size(
			final Collection<?> collection) {

		final int size;
		if (collection != null) {
			size = collection.size();
		} else {
			size = 0;
		}
		return size;
	}

	@ApiMethod
	public static int size(
			final Map<?, ?> map) {

		final int size;
		if (map != null) {
			size = map.size();
		} else {
			size = 0;
		}
		return size;
	}

	@ApiMethod
	public static boolean isNotNullOrEmpty(
			final Collection<?> collection) {
		return collection != null && !collection.isEmpty();
	}

	@ApiMethod
	public static boolean isNotNullOrEmpty(
			final Map<?, ?> map) {
		return map != null && !map.isEmpty();
	}

	@ApiMethod
	public static boolean isNullOrEmpty(
			final Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	@ApiMethod
	public static boolean isNullOrEmpty(
			final Map<?, ?> map) {
		return map == null || map.isEmpty();
	}

	@ApiMethod
	public static void appendJoined(
			final Collection<?> collection,
			final String separator,
			final StringBuilder stringBuilder) {

		int index = 0;
		for (final Object element : collection) {

			stringBuilder.append(element);
			if (index < collection.size() - 1) {
				stringBuilder.append(separator);
			}
			index++;
		}
	}

	@ApiMethod
	public static void appendJoined(
			final Collection<?> collection,
			final char separator,
			final StringBuilder stringBuilder) {

		int index = 0;
		for (final Object element : collection) {

			stringBuilder.append(element);
			if (index < collection.size() - 1) {
				stringBuilder.append(separator);
			}
			index++;
		}
	}

	@ApiMethod
	public static void printJoined(
			final Collection<?> collection,
			final String separator,
			final PrintStream printStream) {

		int index = 0;
		for (final Object element : collection) {

			printStream.print(element);
			if (index < collection.size() - 1) {
				printStream.print(separator);
			}
			index++;
		}
	}

	@ApiMethod
	public static void printJoined(
			final Collection<?> collection,
			final char separator,
			final PrintStream printStream) {

		int index = 0;
		for (final Object element : collection) {

			printStream.print(element);
			if (index < collection.size() - 1) {
				printStream.print(separator);
			}
			index++;
		}
	}
}
