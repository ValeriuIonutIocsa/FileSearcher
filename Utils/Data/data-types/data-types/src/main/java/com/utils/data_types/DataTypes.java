package com.utils.data_types;

import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import com.utils.annotations.ApiMethod;

public final class DataTypes {

	private DataTypes() {
	}

	@ApiMethod
	public static int[] intRange(
			final int upperBound) {

		final int[] range = new int[upperBound];
		for (int i = 0; i < upperBound; i++) {
			range[i] = i;
		}
		return range;
	}

	@ApiMethod
	public static void readFully(
			final ReadableByteChannel readableByteChannel,
			final ByteBuffer byteBuffer) throws Exception {

		byteBuffer.rewind();
		final int read = readableByteChannel.read(byteBuffer);
		if (read != -1) {

			if (read == byteBuffer.limit()) {
				byteBuffer.flip();
			}
		}
	}

	@ApiMethod
	public static void skipLittleEndianBase128(
			final ByteBuffer byteBuffer) {

		try {
			byte b;
			do {
				b = byteBuffer.get();
			} while ((b & 0x80) != 0);

		} catch (final Throwable ignored) {
		}
	}

	@ApiMethod
	public static long parseUnsignedLittleEndianBase128(
			final ByteBuffer byteBuffer) {

		long value = 0;
		try {
			byte b;
			int shift = 0;

			while (true) {

				b = byteBuffer.get();
				value |= ((long) (b & 0x7f)) << shift;
				if ((b & 0x80) == 0) {
					break;
				}
				shift += 7;
			}

		} catch (final Throwable ignored) {
		}
		return value;
	}

	@ApiMethod
	public static int parseSignedLittleEndianBase128(
			final ByteBuffer byteBuffer) {

		int value = 0;
		try {
			for (int i = 0; i < 5; i++) {

				final byte b = byteBuffer.get();
				value |= (b & 0x7f) << (7 * i);
				if ((b & 0x80) == 0) {
					final int s = 32 - (7 * (i + 1));
					value = (value << s) >> s;
					break;
				}
			}

		} catch (final Throwable ignored) {
		}
		return value;
	}

	@ApiMethod
	public static void skipNullTerminatedString(
			final ByteBuffer byteBuffer) {

		final int positionStart = byteBuffer.position();
		int positionEnd = positionStart;
		final byte[] byteBufferArray = byteBuffer.array();
		while (byteBufferArray[positionEnd] != 0) {
			positionEnd++;
		}

		final int length = positionEnd - positionStart;
		byteBuffer.position(byteBuffer.position() + length + 1);
	}

	@ApiMethod
	public static String parseNullTerminatedString(
			final ByteBuffer byteBuffer) {

		final int positionStart = byteBuffer.position();
		int positionEnd = positionStart;
		final byte[] byteBufferArray = byteBuffer.array();
		while (positionEnd < byteBufferArray.length - 1 && byteBufferArray[positionEnd] != 0) {
			positionEnd++;
		}

		final int length = positionEnd - positionStart;
		byteBuffer.position(byteBuffer.position() + length + 1);
		return new String(byteBufferArray, positionStart, length, StandardCharsets.UTF_8);
	}

	@ApiMethod
	public static Double[] createZeroDoubleArray(
			final int elementCount) {

		final Double[] array = new Double[elementCount];
		Arrays.fill(array, 0.0);
		return array;
	}
}
