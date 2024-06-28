package com.utils.gui.alerts;

import org.junit.jupiter.api.Test;

import com.utils.gui.AbstractCustomApplicationTest;

class CustomAlertInfoTest extends AbstractCustomApplicationTest {

	@Test
	void testShow() {

		final CustomAlertInfo customAlertInfo =
				new CustomAlertInfo("Title", "message");
		customAlertInfo.showAndWait();
	}
}
