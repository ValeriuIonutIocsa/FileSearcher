package com.personal.scripts.file_search.workers.search.engine.type;

public final class FactorySearchEngineType {

	private static final SearchEngineType[] VALUES = SearchEngineType.values();

	private FactorySearchEngineType() {
	}

	public static SearchEngineType computeSearchEngineType(
			final String paramSearchEngineTypeName) {

		SearchEngineType resultSearchEngineType = null;
		for (final SearchEngineType searchEngineType : VALUES) {

			final String searchEngineTypeName = searchEngineType.name();
			if (searchEngineTypeName.equals(paramSearchEngineTypeName)) {
				resultSearchEngineType = searchEngineType;
			}
		}
		return resultSearchEngineType;
	}
}
