package com.utils.json;

import java.io.PrintStream;

public interface FactoryJsonWriter {

	JsonWriter newInstance(
			PrintStream printStream);
}
