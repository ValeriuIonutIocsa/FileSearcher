package com.utils.gui.alerts;

import org.junit.jupiter.api.Test;

import com.utils.gui.AbstractCustomApplicationTest;

import javafx.scene.Scene;

class CustomAlertThrowableTest extends AbstractCustomApplicationTest {

	@Test
	void testShow() {

		final Scene scene = computeScene();
		final CustomAlertThrowable customAlertThrowable =
				new CustomAlertThrowable(scene, "Title", "message", new Exception());
		customAlertThrowable.showAndWait();
	}
}
