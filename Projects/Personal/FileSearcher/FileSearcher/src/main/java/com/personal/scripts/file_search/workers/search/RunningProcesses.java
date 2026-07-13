package com.personal.scripts.file_search.workers.search;

import com.utils.gui.alerts.CustomAlertWarning;
import com.utils.log.Logger;
import javafx.scene.Scene;

public class RunningProcesses {

	private Process runningProcess;

	public void stop(
			final Scene scene) {

		Logger.printNewLine();
		Logger.printProgress("stopping search");

		if (runningProcess == null) {
			new CustomAlertWarning(scene, "cannot stop process",
					"currently there are no running processes").showAndWait();

		} else {
			runningProcess.destroy();

			if (runningProcess.isAlive()) {
				runningProcess.destroyForcibly();
			}
		}
	}

	public void setRunningProcess(
			final Process runningProcess) {
		this.runningProcess = runningProcess;
	}
}
