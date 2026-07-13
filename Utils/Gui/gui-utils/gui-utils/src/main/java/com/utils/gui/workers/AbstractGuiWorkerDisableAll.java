package com.utils.gui.workers;

public abstract class AbstractGuiWorkerDisableAll extends AbstractGuiWorker {

	protected AbstractGuiWorkerDisableAll() {

		super(new ControlDisablerAll());
	}
}
