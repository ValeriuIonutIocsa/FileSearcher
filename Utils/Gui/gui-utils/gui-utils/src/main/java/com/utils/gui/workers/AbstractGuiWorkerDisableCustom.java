package com.utils.gui.workers;

public abstract class AbstractGuiWorkerDisableCustom extends AbstractGuiWorker {

	protected AbstractGuiWorkerDisableCustom() {

		super(new ControlDisablerCustom());
	}
}
