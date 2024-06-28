package com.utils.gui.factories;

import com.utils.annotations.ApiMethod;

public final class LayoutControlsFactories {

	private static LayoutControlsFactory instance = new LayoutControlsFactoryImpl();

	private LayoutControlsFactories() {
	}

	@ApiMethod
	public static void setInstance(
			final LayoutControlsFactory instance) {
		LayoutControlsFactories.instance = instance;
	}

	@ApiMethod
	public static LayoutControlsFactory getInstance() {
		return instance;
	}
}
