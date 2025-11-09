package com.utils.gui.objects.select.data;

import com.utils.data_types.table.TableRowData;

public interface TextInputControlWithSelectionItem extends TableRowData {

	String createTextInputControlValue();

	boolean checkMatchesTextInputControlValue(
			String currentTextInputControlValue);
}
