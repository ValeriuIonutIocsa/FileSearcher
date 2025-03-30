package com.utils.json;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.utils.log.Logger;
import com.utils.test.TestInputUtils;

class JsonWriterUtilsTest {

	@Test
	void testObjectToJsonString() {

		final boolean testBoolean = TestInputUtils.parseTestInputBoolean("true");
		final TestJsonObject testJsonObject = new TestJsonObject(testBoolean);

		final String jsonString = JsonWriterUtils.objectToJsonString(testJsonObject,
				TestJsonObject::writeToJson, FactoryJsonWriterRegular.INSTANCE);
		Assertions.assertFalse(StringUtils.isBlank(jsonString));

		Logger.printNewLine();
		Logger.printLine(jsonString);
	}
}
