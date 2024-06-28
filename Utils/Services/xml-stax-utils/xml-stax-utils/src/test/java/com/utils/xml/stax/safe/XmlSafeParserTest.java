package com.utils.xml.stax.safe;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.utils.log.Logger;
import com.utils.string.StrUtils;

class XmlSafeParserTest {

	@Test
	void testWork() {

		final String xmlFilePathString;
		final int input = StrUtils.tryParsePositiveInt("1");
		if (input == 1) {
			xmlFilePathString = "D:\\casdev\\td5\\0g\\h06\\000\\0GH06_0U0_000_t1_v3500\\" +
					"_TC37x_non_OTA_NORMAL\\out\\SPAInstrumentation\\Results_Real\\" +
					"RunnableMeasurementResults_TestSafeParsing.xml";

		} else {
			throw new RuntimeException();
		}

		final String processedXmlFileContents = XmlSafeParser.work(xmlFilePathString);
		Assertions.assertNotNull(processedXmlFileContents);

		Logger.printNewLine();
		Logger.printLine(processedXmlFileContents);
	}
}
