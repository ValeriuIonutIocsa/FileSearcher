package com.utils.gui.alerts;

import org.junit.jupiter.api.Test;

import com.utils.gui.AbstractCustomApplicationTest;

import javafx.scene.Scene;

class CustomAlertInfoTest extends AbstractCustomApplicationTest {

	@Test
	void testShow() {

		final Scene scene = computeScene();
		final CustomAlertInfo customAlertInfo =
				new CustomAlertInfo(scene, "Title", "message");
		customAlertInfo.showAndWait();
	}
}
