package com.utils.app_info;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

class FactoryAppInfoTest {

	@TestFactory
	List<DynamicTest> testFormatTitle() {

		final List<DynamicTest> dynamicTestList = new ArrayList<>();
		final List<Integer> testCaseList = Arrays.asList(0, 1, 2, 3);
		if (testCaseList.contains(1)) {
			dynamicTestList.add(DynamicTest.dynamicTest("1 (camel case)", () -> testFormatTitleCommon(
					"T1AccessFrequencyMeasurement", "T1 Access Frequency Measurement")));
		}
		if (testCaseList.contains(2)) {
			dynamicTestList.add(DynamicTest.dynamicTest("2 (camel case)", () -> testFormatTitleCommon(
					"PTU_CNF_SEL", "PTU CNF SEL")));
		}
		if (testCaseList.contains(3)) {
			dynamicTestList.add(DynamicTest.dynamicTest("3 (camel case)", () -> testFormatTitleCommon(
					"AllocCtrl_T1AccessFrequencyMeasurement_ProjectAnalyzer",
					"Alloc Ctrl T1 Access Frequency Measurement Project Analyzer")));
		}
		return dynamicTestList;
	}

	private static void testFormatTitleCommon(
			final String title,
			final String expectedFormattedTitle) {

		final String formattedTitle = FactoryAppInfo.formatTitle(title);
		Assertions.assertEquals(expectedFormattedTitle, formattedTitle);
	}
}
