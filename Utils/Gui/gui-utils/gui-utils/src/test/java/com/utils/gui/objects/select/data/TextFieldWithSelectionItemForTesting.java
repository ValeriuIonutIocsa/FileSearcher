package com.utils.gui.objects.select.data;

import java.io.Serial;

import com.utils.data_types.data_items.DataItem;
import com.utils.data_types.data_items.objects.FactoryDataItemObjectComparable;
import com.utils.data_types.table.TableColumnData;

public class TextFieldWithSelectionItemForTesting implements TextFieldWithSelectionItem {

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

	public TextFieldWithSelectionItemForTesting(
			final String name,
			final String description) {

		this.name = name;
		this.description = description;
	}

	@Override
	public String createTextFieldValue() {

		return name;
	}

	@Override
	public boolean checkMatchesTextFieldValue(
			final String currentTextFieldValue) {

		return currentTextFieldValue != null && currentTextFieldValue.equals(name);
	}
}
