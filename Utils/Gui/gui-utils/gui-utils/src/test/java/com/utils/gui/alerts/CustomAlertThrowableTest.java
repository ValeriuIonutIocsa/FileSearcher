package com.utils.gui.alerts;

import org.junit.jupiter.api.Test;

import com.utils.gui.AbstractCustomApplicationTest;

class CustomAlertThrowableTest extends AbstractCustomApplicationTest {

	@Test
	void testShow() {

		final CustomAlertThrowable customAlertThrowable =
				new CustomAlertThrowable("Title", "message", new Exception());
		customAlertThrowable.showAndWait();
	}
}
