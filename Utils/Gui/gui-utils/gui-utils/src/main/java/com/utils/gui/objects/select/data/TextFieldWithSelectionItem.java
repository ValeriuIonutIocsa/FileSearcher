package com.utils.gui.objects.select.data;

import com.utils.data_types.table.TableRowData;

public interface TextFieldWithSelectionItem extends TableRowData {

	String createTextFieldValue();

	boolean checkMatchesTextFieldValue(
			String currentTextFieldValue);
}
