package com.utils.gui.workers;

import com.utils.gui.CustomApplication;
import com.utils.log.Logger;

public class ControlDisablerCustom implements ControlDisabler {

	@Override
	public void setControlsDisabled(
			final boolean b) {

		final CustomApplication customApplication = AbstractGuiWorker.getCustomApplication();
		if (customApplication == null) {
			Logger.printError("current application is not set in GUI worker");
		} else {
			customApplication.setControlsDisabled(b);
		}
	}
}
