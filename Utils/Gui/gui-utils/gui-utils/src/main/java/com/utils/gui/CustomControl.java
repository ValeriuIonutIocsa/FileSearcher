package com.utils.gui;

import javafx.event.EventTarget;

public interface CustomControl<
		EventTargetT extends EventTarget> {

	EventTargetT getRoot();
}
