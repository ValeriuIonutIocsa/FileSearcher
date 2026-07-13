package com.utils.gui.workers;

public abstract class AbstractGuiWorkerDisableNone extends AbstractGuiWorker {

	protected AbstractGuiWorkerDisableNone() {

		super(new ControlDisablerNone());
	}
}
