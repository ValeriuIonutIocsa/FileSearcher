package com.utils.json;

import java.io.PrintStream;

public final class FactoryJsonWriterRegular implements FactoryJsonWriter {

	public static final FactoryJsonWriterRegular INSTANCE = new FactoryJsonWriterRegular();

	private FactoryJsonWriterRegular() {
	}

	@Override
	public JsonWriter newInstance(
			final PrintStream printStream) {

		return new JsonWriter("    ", " ", System.lineSeparator(), printStream);
	}
}
