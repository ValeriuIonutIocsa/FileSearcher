package com.utils.json;

class TestJsonObject {

	private final boolean testBoolean;

	TestJsonObject(
			final boolean testBoolean) {

		this.testBoolean = testBoolean;
	}

	void writeToJson(
			final int indentCount,
			final JsonWriter jsonWriter) {

		jsonWriter.writeStringAttribute("testBoolean", String.valueOf(testBoolean), true, indentCount);
		jsonWriter.writeStringAttribute("testAttribute1", "testValue1", true, indentCount);
		jsonWriter.writeStringAttribute("testAttribute2", "testValue2", true, indentCount);
		jsonWriter.writeStringAttribute("testAttribute3", "testValue3", false, indentCount);
	}
}
