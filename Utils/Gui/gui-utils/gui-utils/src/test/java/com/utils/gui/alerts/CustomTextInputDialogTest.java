package com.utils.gui.alerts;

import org.junit.jupiter.api.Test;

import com.utils.gui.AbstractCustomApplicationTest;
import com.utils.log.Logger;

import javafx.scene.Scene;

class CustomTextInputDialogTest extends AbstractCustomApplicationTest {

	@Test
	void testShow() {

		final Scene scene = computeScene();
		final CustomTextInputDialog customTextInputDialog =
				new CustomTextInputDialog(scene, "Title", "message", "default value");
		customTextInputDialog.showAndWait();

		final String result = customTextInputDialog.getResult();
		Logger.printLine("result: " + result);
	}
}
