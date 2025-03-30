package com.utils.json;

import java.io.PrintStream;

public final class FactoryJsonWriterNoIndent implements FactoryJsonWriter {

	public static final FactoryJsonWriterNoIndent INSTANCE = new FactoryJsonWriterNoIndent();

	private FactoryJsonWriterNoIndent() {
	}

	@Override
	public JsonWriter newInstance(
			final PrintStream printStream) {

		return new JsonWriter("", "", "", printStream);
	}
}
