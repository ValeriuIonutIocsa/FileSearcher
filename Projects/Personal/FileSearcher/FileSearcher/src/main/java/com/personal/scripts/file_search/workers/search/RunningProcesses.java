package com.personal.scripts.file_search.workers.search;

import com.utils.gui.alerts.CustomAlertWarning;
import com.utils.log.Logger;

public class RunningProcesses {

	private Process runningProcess;

	public void stop() {

		Logger.printNewLine();
		Logger.printProgress("stopping search");

		if (runningProcess == null) {
			new CustomAlertWarning("cannot stop process",
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
