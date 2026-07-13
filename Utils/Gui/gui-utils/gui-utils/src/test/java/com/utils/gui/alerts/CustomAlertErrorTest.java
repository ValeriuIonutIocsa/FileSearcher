package com.utils.gui.alerts;

import org.junit.jupiter.api.Test;

import com.utils.gui.AbstractCustomApplicationTest;

import javafx.scene.Scene;

class CustomAlertErrorTest extends AbstractCustomApplicationTest {

	@Test
	void testShow() {

		final Scene scene = computeScene();
		final CustomAlertError customAlertError =
				new CustomAlertError(scene, "Title", "message");
		customAlertError.showAndWait();
	}
}
