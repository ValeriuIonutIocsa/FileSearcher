package com.utils.gui.objects.browse;

import org.apache.commons.lang3.StringUtils;

import com.utils.annotations.ApiMethod;
import com.utils.gui.AbstractCustomControl;
import com.utils.gui.GuiUtils;
import com.utils.gui.factories.BasicControlsFactories;
import com.utils.gui.factories.LayoutControlsFactories;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class HBoxBrowsePath extends AbstractCustomControl<HBox> {

	private final String name;

	private final TextField pathTextField;

	private String initialDirectoryPathString;
	private final BrowsePath browsePath;

	HBoxBrowsePath(
			final String name,
			final String initialValue,
			final String initialDirectoryPathString,
			final BrowsePath browsePath) {

		this.name = name;
		this.initialDirectoryPathString = initialDirectoryPathString;
		this.browsePath = browsePath;

		pathTextField = BasicControlsFactories.getInstance().createTextField("");
		if (StringUtils.isNotBlank(initialValue)) {
			pathTextField.setText(initialValue);
		}
	}

	@Override
	protected HBox createRoot() {

		final HBox hBoxRoot = LayoutControlsFactories.getInstance().createHBox();

		browsePath.createInputTypeControls(hBoxRoot);

		GuiUtils.addToHBox(hBoxRoot, pathTextField,
				Pos.CENTER, Priority.ALWAYS, 0, 0, 0, 0);

		final Button buttonBrowse = BasicControlsFactories.getInstance().createButton("...");
		buttonBrowse.setOnAction(event -> browse());
		GuiUtils.addToHBox(hBoxRoot, buttonBrowse,
				Pos.CENTER, Priority.NEVER, 0, 0, 0, 7);

		return hBoxRoot;
	}

	private void browse() {

		final String existingPathString = computePathString();
		final Scene scene = getRoot().getScene();
		final String pathString = browsePath.browsePath(
				name, existingPathString, initialDirectoryPathString, scene);
		if (StringUtils.isNotBlank(pathString)) {
			pathTextField.setText(pathString);
		}
	}

	@ApiMethod
	public String computePathString() {

		return GuiUtils.computeTextInputControlPathString(pathTextField);
	}

	@ApiMethod
	public void setInitialDirectoryPathString(
			final String initialDirectoryPathString) {
		this.initialDirectoryPathString = initialDirectoryPathString;
	}

	@ApiMethod
	public TextField getPathTextField() {
		return pathTextField;
	}
}
