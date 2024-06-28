package com.utils.gui.objects;

import com.utils.gui.alerts.CustomAlertConfirm;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;

public class CustomMenuBar extends MenuBar {

	public CustomMenuBar(
			final ReadOnlyObjectProperty<Scene> sceneProperty) {

		final javafx.scene.control.Menu menuFile = new javafx.scene.control.Menu("File");

		final javafx.scene.control.MenuItem menuItemExit = new javafx.scene.control.MenuItem("Exit");
		menuItemExit.setOnAction(event -> {
			final ButtonType buttonType = closeWindowConfirmationDialog(sceneProperty.get());
			if (buttonType == null || ButtonType.YES.equals(buttonType)) {
				sceneProperty.get().getWindow().hide();
			}
		});
		menuFile.getItems().add(menuItemExit);

		getMenus().add(menuFile);
	}

	public static ButtonType closeWindowConfirmationDialog(
			final Scene scene) {

		final ButtonType buttonType;
		if (Cursor.WAIT.equals(scene.getCursor())) {
			final CustomAlertConfirm customAlertConfirm = new CustomAlertConfirm("Are you sure you wish to quit?",
					"The application is working. The process cannot complete successfully if you quit.",
					ButtonType.NO, ButtonType.YES);
			customAlertConfirm.showAndWait();
			buttonType = customAlertConfirm.getResult();
		} else {
			buttonType = null;
		}
		return buttonType;
	}
}
