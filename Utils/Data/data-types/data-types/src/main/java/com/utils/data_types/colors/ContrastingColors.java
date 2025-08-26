package com.utils.data_types.colors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContrastingColors {

	private static final String[] KELLY_COLOR_STRING_ARRAY = {
			"0xFFB300",
			"0x803E75",
			"0xFF6800",
			"0xA6BDD7",
			"0xC10020",
			"0xCEA262",
			"0x817066",
			"0x007D34",
			"0xF6768E",
			"0x00538A",
			"0xFF7A5C",
			"0x53377A",
			"0xFF8E00",
			"0xB32851",
			"0xF4C800",
			"0x7F180D",
			"0x93AA00",
			"0x593315",
			"0xF13A13",
			"0x232C16",
	};

	private final Map<String, String> nameToColorStringMap;
	private final List<String> nameList;

	public ContrastingColors() {

		nameToColorStringMap = new HashMap<>();
		nameList = new ArrayList<>();
	}

	public String computeContrastingColor(
			final String name) {

		String colorName = nameToColorStringMap.get(name);
		if (colorName == null) {

			int index = nameList.indexOf(name);
			if (index == -1) {

				index = nameList.size();
				nameList.add(name);
			}
			index = index % KELLY_COLOR_STRING_ARRAY.length;
			colorName = KELLY_COLOR_STRING_ARRAY[index];
		}
		return colorName;
	}

	public void addColor(
			final String name,
			final String colorName) {

		nameToColorStringMap.put(name, colorName);
	}
}
