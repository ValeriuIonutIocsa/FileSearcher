package com.utils.gui.objects.select.data;

import java.io.Serial;

import com.utils.data_types.data_items.DataItem;
import com.utils.data_types.data_items.objects.FactoryDataItemObjectComparable;
import com.utils.data_types.table.TableColumnData;

public class TextInputControlWithSelectionItemForTesting implements TextInputControlWithSelectionItem {

	@Serial
	private static final long serialVersionUID = 7706800536600688010L;

	public static final TableColumnData[] COLUMNS = {
			new TableColumnData("Name", "Name", 0.1, Double.NaN, Double.NaN),
			new TableColumnData("Description", "Description", 0.1, Double.NaN, Double.NaN)
	};

	@Override
	public DataItem<?>[] getTableViewDataItemArray() {

		return new DataItem<?>[] {
				FactoryDataItemObjectComparable.newInstance(name),
				FactoryDataItemObjectComparable.newInstance(description)
		};
	}

	private final String name;
	private final String description;

	public TextInputControlWithSelectionItemForTesting(
			final String name,
			final String description) {

		this.name = name;
		this.description = description;
	}

	@Override
	public String createTextInputControlValue() {

		return name;
	}

	@Override
	public boolean checkMatchesTextInputControlValue(
			final String currentTextInputControlValue) {

		return currentTextInputControlValue != null && currentTextInputControlValue.equals(name);
	}
}
