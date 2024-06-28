package com.utils.gui.alerts;

import org.junit.jupiter.api.Test;

import com.utils.gui.AbstractCustomApplicationTest;
import com.utils.log.Logger;

class CustomTextInputDialogTest extends AbstractCustomApplicationTest {

	@Test
	void testShow() {

		final CustomTextInputDialog customTextInputDialog =
				new CustomTextInputDialog("Title", "message", "default value");
		customTextInputDialog.showAndWait();

		final String result = customTextInputDialog.getResult();
		Logger.printLine("result: " + result);
	}
}
