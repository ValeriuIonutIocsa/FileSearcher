package com.personal.scripts.file_search.hist;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import com.personal.scripts.file_search.FileSearcherUtils;
import com.personal.scripts.file_search.workers.search.engine.type.FactorySearchEngineType;
import com.personal.scripts.file_search.workers.search.engine.type.SearchEngineType;
import com.utils.io.IoUtils;
import com.utils.io.PathUtils;
import com.utils.io.StreamUtils;
import com.utils.io.folder_creators.FactoryFolderCreator;
import com.utils.log.Logger;
import com.utils.string.StrUtils;

public final class SavedOptionsFile {

	public static final SavedOptionsFile INSTANCE = new SavedOptionsFile();

	private SearchEngineType searchEngineType;
	private boolean caseSensitivePathPattern;
	private boolean useRegex;
	private boolean caseSensitive;

	private SavedOptionsFile() {

		searchEngineType = SearchEngineType.RG;
		caseSensitivePathPattern = true;
		useRegex = false;
		caseSensitive = true;
	}

	public void parse() {

		try {
			final Properties properties = new Properties();
			final String filePathString = createFilePathString();
			if (IoUtils.fileExists(filePathString)) {

				try (InputStream inputStream = StreamUtils.openInputStream(filePathString)) {

					properties.load(inputStream);

					final String searchEngineTypeName = properties.getProperty("searchEngineTypeName");
					searchEngineType = FactorySearchEngineType.computeSearchEngineType(searchEngineTypeName);

					final String caseSensitivePathPatternString = properties.getProperty("caseSensitivePathPattern");
					caseSensitivePathPattern = Boolean.parseBoolean(caseSensitivePathPatternString);

					final String useRegexString = properties.getProperty("useRegex");
					useRegex = Boolean.parseBoolean(useRegexString);

					final String caseSensitiveString = properties.getProperty("caseSensitive");
					caseSensitive = Boolean.parseBoolean(caseSensitiveString);
				}
			}

		} catch (final Exception exc) {
			Logger.printException(exc);
		}
	}

	public void save() {

		final Properties properties = new Properties();
		final String filePathString = createFilePathString();
		final boolean createParentDirectoriesSuccess = FactoryFolderCreator.getInstance()
				.createParentDirectories(filePathString, false, true);
		if (createParentDirectoriesSuccess) {

			try (OutputStream outputStream = StreamUtils.openOutputStream(filePathString)) {

				properties.setProperty("searchEngineTypeName", searchEngineType.name());
				properties.setProperty("caseSensitivePathPattern", Boolean.toString(caseSensitivePathPattern));
				properties.setProperty("useRegex", Boolean.toString(useRegex));
				properties.setProperty("caseSensitive", Boolean.toString(caseSensitive));

				properties.store(outputStream, "");

			} catch (final Exception exc) {
				Logger.printException(exc);
			}
		}
	}

	private static String createFilePathString() {

		final String appFolderPathString = FileSearcherUtils.createAppFolderPathString();
		return PathUtils.computePath(appFolderPathString, "SavedOptions.properties");
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	public void setSearchEngineType(
			final SearchEngineType searchEngineType) {
		this.searchEngineType = searchEngineType;
	}

	public SearchEngineType getSearchEngineType() {
		return searchEngineType;
	}

	public void setCaseSensitivePathPattern(
			final boolean caseSensitivePathPattern) {
		this.caseSensitivePathPattern = caseSensitivePathPattern;
	}

	public boolean isCaseSensitivePathPattern() {
		return caseSensitivePathPattern;
	}

	public void setUseRegex(
			final boolean useRegex) {
		this.useRegex = useRegex;
	}

	public boolean isUseRegex() {
		return useRegex;
	}

	public void setCaseSensitive(
			final boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}
}
