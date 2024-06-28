package com.utils.gui.objects.select;

import com.utils.gui.CustomControl;

import javafx.scene.layout.HBox;

public interface HBoxTextFieldWithSelection extends CustomControl<HBox> {

	void configureValue(
			String value);

	String computeValue();
}
