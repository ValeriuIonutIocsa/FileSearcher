package com.utils.gui.workers;

import java.time.Instant;

import com.utils.annotations.ApiMethod;
import com.utils.gui.CustomApplication;
import com.utils.gui.GuiUtils;
import com.utils.log.Logger;
import com.utils.string.StrUtils;
import com.utils.string.exc.SilentException;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.stage.Window;

public abstract class AbstractGuiWorker extends Thread implements GuiWorker {

	private static CustomApplication customApplication;

	private final ControlDisabler controlDisabler;

	public AbstractGuiWorker(
			final ControlDisabler controlDisabler) {

		this.controlDisabler = controlDisabler;
	}

	@Override
	public void run() {

		final Instant start = Instant.now();
		try {
			GuiUtils.run(() -> setControlsDisabled(true));
			work();

		} catch (final SilentException ignored) {
		} catch (final Throwable throwable) {
			Logger.printThrowable(throwable);
			try {
				error();
			} catch (final Throwable errorThrowable) {
				Logger.printThrowable(errorThrowable);
			}

		} finally {
			GuiUtils.run(() -> {

				try {
					finish();
					setControlsDisabled(false);
					Logger.printFinishMessage(start);

				} catch (final Throwable errorThrowable) {
					Logger.printThrowable(errorThrowable);
				}
			});
		}
	}

	private void setControlsDisabled(
			final boolean b) {

		final Scene scene = computeScene();
		if (scene != null) {

			final Cursor cursor;
			if (b) {
				cursor = Cursor.WAIT;
			} else {
				cursor = Cursor.DEFAULT;
			}
			scene.setCursor(cursor);
		}
		if (controlDisabler != null) {
			controlDisabler.setControlsDisabled(b);
		}
	}

	protected abstract void work() throws Exception;

	protected abstract void error();

	protected abstract void finish();

	public static Window computeWindow() {

		Window window = null;
		final Scene scene = computeScene();
		if (scene != null) {
			window = scene.getWindow();
		}
		return window;
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	@ApiMethod
	public static Scene computeScene() {

		Scene scene = null;
		final CustomApplication customApplication = getCustomApplication();
		if (customApplication == null) {
			Logger.printError("current application is not set in GUI worker");
		} else {
			scene = customApplication.computeScene();
		}
		return scene;
	}

	@ApiMethod
	public static void setCustomApplication(
			final CustomApplication customApplication) {
		AbstractGuiWorker.customApplication = customApplication;
	}

	@ApiMethod
	public static CustomApplication getCustomApplication() {
		return customApplication;
	}
}
