package com.utils.gui;

import java.util.ArrayList;
import java.util.List;

import org.testfx.framework.junit5.ApplicationTest;

import com.utils.gui.factories.LayoutControlsFactories;
import com.utils.gui.workers.AbstractGuiWorker;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public abstract class AbstractCustomApplicationTest
		extends ApplicationTest implements CustomApplicationTest {

	private Stage stage;

	private StackPane stackPaneContainer;

	@Override
	public void start(
			final Stage primaryStage) {

		AbstractGuiWorker.setCustomApplication(this);

		primaryStage.setOnShown(event -> showSecondaryStage());
		primaryStage.show();
	}

	private void showSecondaryStage() {

		final Stage stage = new Stage();
		this.stage = stage;

		stage.setTitle("Test Application");
		final Image imageApp = createImageApp();
		GuiUtils.setAppIcon(stage, imageApp);

		stackPaneContainer = LayoutControlsFactories.getInstance().createStackPane(Pos.CENTER_LEFT);
		final Scene scene = new Scene(stackPaneContainer, 1280, 800);

		final List<String> stylesheetList = new ArrayList<>();
		fillStylesheetList(stylesheetList);
		scene.getStylesheets().addAll(stylesheetList);

		stage.setScene(scene);

		stage.setOnShown(event -> scene.getRoot().requestFocus());
		stage.setOnHidden(event -> Runtime.getRuntime().halt(0));

		stage.show();
	}

	@Override
	public Image createImageApp() {
		return null;
	}

	@Override
	public void fillStylesheetList(
			final List<String> stylesheetList) {
	}

	@Override
	public void setControlsDisabled(
			final boolean b) {

		stackPaneContainer.setDisable(b);
	}

	@Override
	public Scene computeScene() {

		return stage.getScene();
	}

	protected StackPane getStackPaneContainer() {
		return stackPaneContainer;
	}
}
