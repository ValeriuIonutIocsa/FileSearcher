package com.utils.gui;

import java.util.List;

import javafx.scene.image.Image;

public interface CustomApplicationTest extends CustomApplication {

	Image createImageApp();

	void fillStylesheetList(
			List<String> stylesheetList);
}
