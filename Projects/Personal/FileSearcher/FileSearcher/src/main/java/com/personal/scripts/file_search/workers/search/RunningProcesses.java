package com.personal.scripts.file_search.workers.search;

import com.utils.gui.alerts.CustomAlertWarning;
import com.utils.log.Logger;

public class RunningProcesses {

	private Process runningProcess;
	private boolean processStopped;

	public void stop() {

		Logger.printNewLine();
		Logger.printProgress("stopping search");

		if (runningProcess == null) {
			new CustomAlertWarning("cannot stop process",
					"currently there are no running processes").showAndWait();

		} else {
			processStopped = true;
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

	public void setProcessStopped(
			final boolean processStopped) {
		this.processStopped = processStopped;
	}

	public boolean isProcessStopped() {
		return processStopped;
	}
}
