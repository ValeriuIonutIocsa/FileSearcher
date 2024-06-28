package com.utils.gui.colors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.paint.Color;

public class ContrastingColors {

	private static final Color[] KELLY_COLORS = {
			Color.web("0xFFB300"),
			Color.web("0x803E75"),
			Color.web("0xFF6800"),
			Color.web("0xA6BDD7"),
			Color.web("0xC10020"),
			Color.web("0xCEA262"),
			Color.web("0x817066"),
			Color.web("0x007D34"),
			Color.web("0xF6768E"),
			Color.web("0x00538A"),
			Color.web("0xFF7A5C"),
			Color.web("0x53377A"),
			Color.web("0xFF8E00"),
			Color.web("0xB32851"),
			Color.web("0xF4C800"),
			Color.web("0x7F180D"),
			Color.web("0x93AA00"),
			Color.web("0x593315"),
			Color.web("0xF13A13"),
			Color.web("0x232C16"),
	};

	private final Map<String, Color> colorsByNameMap;
	private final List<String> names;

	public ContrastingColors() {

		colorsByNameMap = new HashMap<>();
		names = new ArrayList<>();
	}

	public Color computeContrastingColor(
			final String name) {

		Color color = colorsByNameMap.get(name);
		if (color == null) {

			int index = names.indexOf(name);
			if (index == -1) {
				index = names.size();
				names.add(name);
			}
			index = index % KELLY_COLORS.length;
			color = KELLY_COLORS[index];
		}
		return color;
	}

	public void addColor(
			final String name,
			final Color color) {
		colorsByNameMap.put(name, color);
	}
}
