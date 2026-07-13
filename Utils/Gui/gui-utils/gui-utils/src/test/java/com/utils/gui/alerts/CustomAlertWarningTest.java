package com.utils.gui.alerts;

import org.junit.jupiter.api.Test;

import com.utils.gui.AbstractCustomApplicationTest;

import javafx.scene.Scene;

class CustomAlertWarningTest extends AbstractCustomApplicationTest {

	@Test
	void testShow() {

		final Scene scene = computeScene();
		final CustomAlertWarning customAlertWarning =
				new CustomAlertWarning(scene, "Title", "message");
		customAlertWarning.showAndWait();
	}
}
