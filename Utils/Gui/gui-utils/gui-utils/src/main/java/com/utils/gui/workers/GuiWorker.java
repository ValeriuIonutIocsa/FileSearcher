package com.utils.gui.workers;

import javafx.scene.Scene;

public interface GuiWorker extends Runnable {

	void start();

	Scene getScene();
}
