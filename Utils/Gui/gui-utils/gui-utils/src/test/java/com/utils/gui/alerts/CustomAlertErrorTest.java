package com.utils.gui.alerts;

import org.junit.jupiter.api.Test;

import com.utils.gui.AbstractCustomApplicationTest;

class CustomAlertErrorTest extends AbstractCustomApplicationTest {

	@Test
	void testShow() {

		final CustomAlertError customAlertError =
				new CustomAlertError("Title", "message");
		customAlertError.showAndWait();
	}
}
