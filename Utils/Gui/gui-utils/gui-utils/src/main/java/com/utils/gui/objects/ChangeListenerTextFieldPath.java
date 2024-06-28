package com.utils.gui.objects;

import org.apache.commons.lang3.StringUtils;

import com.utils.gui.factories.BasicControlsFactories;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Control;
import javafx.scene.control.Tooltip;

public class ChangeListenerTextFieldPath implements ChangeListener<String> {

	private final Control control;

	public ChangeListenerTextFieldPath(
			final Control control) {

		this.control = control;
	}

	@Override
	public void changed(
			final ObservableValue<? extends String> observable,
			final String oldValue,
			final String newValue) {

		final Tooltip tooltip;
		if (StringUtils.isBlank(newValue)) {
			tooltip = null;
		} else {
			tooltip = BasicControlsFactories.getInstance().createTooltip(newValue);
		}
		control.setTooltip(tooltip);
	}
}
