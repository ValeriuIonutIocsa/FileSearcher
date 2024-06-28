package com.utils.gui.objects.search_and_filter;

import java.util.Collection;

import com.utils.annotations.ApiMethod;
import com.utils.gui.objects.tables.table_view.CustomTable;
import com.utils.string.regex.custom_patterns.CustomPatterns;

public interface SearchAndFilterTable extends CustomTable {

	@ApiMethod
	void searchTable(
			int searchColumnIndex,
			CustomPatterns searchCustomPatterns);

	@ApiMethod
	void filterTable(
			FilterType filterType,
			int filterColumnIndex,
			CustomPatterns filterCustomPatterns);

	@ApiMethod
	Collection<String> getTableColumnNames();
}
