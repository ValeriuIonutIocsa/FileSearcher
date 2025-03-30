package com.personal.scripts.file_search;

import com.utils.gui.workers.ControlDisablerAll;

import javafx.scene.Scene;

public class ControlDisablerFileSearcher extends ControlDisablerAll {

	private final VBoxFileSearcher vBoxFileSearcher;

	public ControlDisablerFileSearcher(
			final Scene scene,
			final VBoxFileSearcher vBoxFileSearcher) {

		super(scene);

		this.vBoxFileSearcher = vBoxFileSearcher;
	}

	@Override
	public void setControlsDisabled(
			final boolean b) {

		super.setControlsDisabled(b);

		if (vBoxFileSearcher != null && b) {

			vBoxFileSearcher.enableStopSearchButton();
		}
	}
}
