package com.utils.gui.alerts;

import org.junit.jupiter.api.Test;

import com.utils.gui.AbstractCustomApplicationTest;
import com.utils.log.Logger;

import javafx.scene.control.ButtonType;

class CustomAlertConfirmTest extends AbstractCustomApplicationTest {

	@Test
	void testShow() {

		final CustomAlertConfirm customAlertConfirm =
				new CustomAlertConfirm("Title", "message", ButtonType.NO, ButtonType.YES);
		customAlertConfirm.showAndWait();

		final ButtonType result = customAlertConfirm.getResult();
		Logger.printLine("result: " + result);
	}
}
