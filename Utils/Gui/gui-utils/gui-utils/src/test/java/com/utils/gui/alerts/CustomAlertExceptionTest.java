package com.utils.gui.alerts;

import org.junit.jupiter.api.Test;

import com.utils.gui.AbstractCustomApplicationTest;

class CustomAlertExceptionTest extends AbstractCustomApplicationTest {

	@Test
	void testShow() {

		final CustomAlertException customAlertException =
				new CustomAlertException("Title", "message", new Exception());
		customAlertException.showAndWait();
	}
}
