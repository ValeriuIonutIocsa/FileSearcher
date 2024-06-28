package com.personal.scripts.file_search;

import java.io.Serial;

import com.utils.data_types.data_items.DataItem;
import com.utils.data_types.data_items.objects.FactoryDataItemObjectComparable;
import com.utils.gui.objects.select.data.TextFieldWithSelectionItem;
import com.utils.string.StrUtils;

class SelectionItem implements TextFieldWithSelectionItem {

	@Serial
	private static final long serialVersionUID = -9026151149096389293L;

	@Override
	public DataItem<?>[] getTableViewDataItemArray() {

		return new DataItem<?>[] {
				FactoryDataItemObjectComparable.newInstance(text)
		};
	}

	private final String text;

	SelectionItem(
			final String text) {

		this.text = text;
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	@Override
	public String createTextFieldValue() {
		return text;
	}

	@Override
	public boolean checkMatchesTextFieldValue(
			final String currentTextFieldValue) {

		return text.equals(currentTextFieldValue);
	}
}
