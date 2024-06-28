package com.utils.gui.workers;

import java.time.Instant;

import com.utils.gui.GuiUtils;
import com.utils.log.Logger;
import com.utils.string.StrUtils;
import com.utils.string.exc.SilentException;

import javafx.scene.Cursor;
import javafx.scene.Scene;

public abstract class AbstractGuiWorker extends Thread implements GuiWorker {

	private final Scene scene;
	private final ControlDisabler controlDisabler;

	public AbstractGuiWorker(
			final Scene scene,
			final ControlDisabler controlDisabler) {

		this.scene = scene;
		this.controlDisabler = controlDisabler;
	}

	@Override
	public void run() {

		final Instant start = Instant.now();
		try {
			GuiUtils.run(() -> setControlsDisabled(true));
			work();

		} catch (final SilentException ignored) {
		} catch (final Exception exc) {
			Logger.printException(exc);
			try {
				error();
			} catch (final Exception exc2) {
				Logger.printException(exc2);
			}

		} finally {
			GuiUtils.run(() -> {

				try {
					finish();
					setControlsDisabled(false);
					Logger.printFinishMessage(start);

				} catch (final Exception exc2) {
					Logger.printException(exc2);
				}
			});
		}
	}

	private void setControlsDisabled(
			final boolean b) {

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

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	@Override
	public Scene getScene() {
		return scene;
	}
}
