package com.utils.data_types.data_items.objects.instant;

import java.time.Instant;

public final class FactoryDataItemInstant {

	private FactoryDataItemInstant() {
	}

	public static DataItemInstant newInstance(
			final Instant instant) {

		DataItemInstant dataItemInstant = null;
		if (instant != null) {
			dataItemInstant = new DataItemInstant(instant);
		}
		return dataItemInstant;
	}
}
