package com.utils.gui.factories;

import com.utils.annotations.ApiMethod;

public final class BasicControlsFactories {

	private static BasicControlsFactory instance = new BasicControlsFactoryImpl();

	private BasicControlsFactories() {
	}

	@ApiMethod
	public static void setInstance(
			final BasicControlsFactory instance) {
		BasicControlsFactories.instance = instance;
	}

	@ApiMethod
	public static BasicControlsFactory getInstance() {
		return instance;
	}
}
