package com.utils.xml.stax.safe;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.utils.io.PathUtils;
import com.utils.io.WriterUtils;
import com.utils.log.Logger;
import com.utils.string.StrUtils;

class XmlSafeParserTest {

	@Test
	void testWork() {

		final String xmlFilePathString;
		final int input = StrUtils.tryParsePositiveInt("1");
		if (input == 1) {
			xmlFilePathString = "D:\\casdev\\td5\\0g\\h06\\000\\0GH06_0U0_000_t1_v3500_SharedCom\\" +
					"_TC37x_non_OTA_NORMAL\\out\\SPAInstrumentation\\Results\\RuntimeMeasurementResults.xml";

		} else {
			throw new RuntimeException();
		}

		final String processedXmlFileContents = XmlSafeParser.work(xmlFilePathString);
		Assertions.assertNotNull(processedXmlFileContents);

		Logger.printNewLine();

		final String processedXmlFilePathString = PathUtils.appendFileNameSuffix(xmlFilePathString, "_PROCESSED");
		Logger.printProgress("writing processed XML file:");
		Logger.printLine(processedXmlFilePathString);

		WriterUtils.tryStringToFile(processedXmlFileContents, StandardCharsets.UTF_8, processedXmlFilePathString);
	}
}
