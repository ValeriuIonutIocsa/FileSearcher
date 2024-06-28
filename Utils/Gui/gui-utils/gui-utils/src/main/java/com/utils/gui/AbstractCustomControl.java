package com.utils.gui;

import com.utils.string.StrUtils;

import javafx.event.EventTarget;

public abstract class AbstractCustomControl<
		EventTargetT extends EventTarget> implements CustomControl<EventTargetT> {

	private EventTargetT root;

	protected abstract EventTargetT createRoot();

	@Override
	public final EventTargetT getRoot() {

		if (root == null) {
			root = createRoot();
		}
		return root;
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}
}
