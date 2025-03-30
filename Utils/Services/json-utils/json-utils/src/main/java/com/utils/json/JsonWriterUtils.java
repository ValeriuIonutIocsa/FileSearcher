package com.utils.json;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.function.TriConsumer;

import com.utils.annotations.ApiMethod;

public final class JsonWriterUtils {

	private JsonWriterUtils() {
	}

	@ApiMethod
	public static <
			ObjectT> String objectToJsonString(
					final ObjectT object,
					final TriConsumer<ObjectT, Integer, JsonWriter> writeObjectTriConsumer,
					final FactoryJsonWriter factoryJsonWriter) {

		final ByteArrayOutputStream noIndentByteArrayOutputStream = new ByteArrayOutputStream();
		try (PrintStream printStream = new PrintStream(noIndentByteArrayOutputStream)) {

			final JsonWriter jsonWriter = factoryJsonWriter.newInstance(printStream);
			jsonWriter.writeObject(object, 0, writeObjectTriConsumer);
		}
		return noIndentByteArrayOutputStream.toString(StandardCharsets.UTF_8);
	}
}
