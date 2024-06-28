package com.utils.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class ApplicationJavaFxHelloWorld extends Application {

	@Override
	public void start(
			final Stage primaryStage) {

		final VBox vBoxRoot = createVBoxRoot();

		final Scene scene = new Scene(vBoxRoot, 300, 250);

		primaryStage.setTitle("Hello World!");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private static VBox createVBoxRoot() {

		final VBox vBoxRoot = new VBox();

		final Rectangle rectangle = new Rectangle(100, 70);
		rectangle.setFill(Color.RED);
		VBox.setMargin(rectangle, new Insets(10));
		vBoxRoot.getChildren().add(rectangle);

		return vBoxRoot;
	}

	public static void main(
			final String[] args) {
		launch(args);
	}
}
