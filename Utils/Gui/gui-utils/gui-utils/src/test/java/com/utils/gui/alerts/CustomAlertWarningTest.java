package com.utils.gui.alerts;

import org.junit.jupiter.api.Test;

import com.utils.gui.AbstractCustomApplicationTest;

class CustomAlertWarningTest extends AbstractCustomApplicationTest {

	@Test
	void testShow() {

		final CustomAlertWarning customAlertWarning =
				new CustomAlertWarning("Title", "message");
		customAlertWarning.showAndWait();
	}
}
