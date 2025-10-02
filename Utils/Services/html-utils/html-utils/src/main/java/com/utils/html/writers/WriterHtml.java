package com.utils.html.writers;

public interface WriterHtml {

	void writeToFile(
			String outputPathString);

	String writeToString();
}
