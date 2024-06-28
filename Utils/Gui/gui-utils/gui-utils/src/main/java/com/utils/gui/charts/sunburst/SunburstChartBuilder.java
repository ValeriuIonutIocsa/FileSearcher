/*
 * Copyright (c) 2018 by Gerrit Grunwald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.utils.gui.charts.sunburst;

import java.util.HashMap;
import java.util.Map;

import com.utils.gui.charts.sunburst.tree.TreeNode;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Dimension2D;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;

public final class SunburstChartBuilder {

	private final Map<String, Property<?>> properties = new HashMap<>();

	public static SunburstChartBuilder create() {
		return new SunburstChartBuilder();
	}

	private SunburstChartBuilder() {
	}

	public SunburstChartBuilder tree(
			final TreeNode treeNode) {

		properties.put("tree", new SimpleObjectProperty<>(treeNode));
		return this;
	}

	public SunburstChartBuilder visibleData(
			final VisibleData visibleData) {

		properties.put("visibleData", new SimpleObjectProperty<>(visibleData));
		return this;
	}

	public SunburstChartBuilder textOrientation(
			final TextOrientation orientation) {

		properties.put("textOrientation", new SimpleObjectProperty<>(orientation));
		return this;
	}

	public SunburstChartBuilder backgroundColor(
			final Color color) {

		properties.put("backgroundColor", new SimpleObjectProperty<>(color));
		return this;
	}

	public SunburstChartBuilder textColor(
			final Color color) {

		properties.put("textColor", new SimpleObjectProperty<>(color));
		return this;
	}

	public SunburstChartBuilder useColorFromParent(
			final boolean use) {

		properties.put("useColorFromParent", new SimpleBooleanProperty(use));
		return this;
	}

	public SunburstChartBuilder decimals(
			final int decimals) {

		properties.put("decimals", new SimpleIntegerProperty(decimals));
		return this;
	}

	public SunburstChartBuilder autoTextColor(
			final boolean automatic) {

		properties.put("autoTextColor", new SimpleBooleanProperty(automatic));
		return this;
	}

	public SunburstChartBuilder brightTextColor(
			final Color color) {

		properties.put("brightTextColor", new SimpleObjectProperty<>(color));
		return this;
	}

	public SunburstChartBuilder darkTextColor(
			final Color color) {

		properties.put("darkTextColor", new SimpleObjectProperty<>(color));
		return this;
	}

	public SunburstChartBuilder useChartDataTextColor(
			final boolean use) {

		properties.put("useChartDataTextColor", new SimpleBooleanProperty(use));
		return this;
	}

	public SunburstChartBuilder prefSize(
			final double width,
			final double height) {

		properties.put("prefSize", new SimpleObjectProperty<>(new Dimension2D(width, height)));
		return this;
	}

	public SunburstChartBuilder minSize(
			final double width,
			final double height) {

		properties.put("minSize", new SimpleObjectProperty<>(new Dimension2D(width, height)));
		return this;
	}

	public SunburstChartBuilder maxSize(
			final double width,
			final double height) {

		properties.put("maxSize", new SimpleObjectProperty<>(new Dimension2D(width, height)));
		return this;
	}

	public SunburstChartBuilder prefWidth(
			final double prefWidth) {

		properties.put("prefWidth", new SimpleDoubleProperty(prefWidth));
		return this;
	}

	public SunburstChartBuilder prefHeight(
			final double prefHeight) {

		properties.put("prefHeight", new SimpleDoubleProperty(prefHeight));
		return this;
	}

	public SunburstChartBuilder minWidth(
			final double minWidth) {

		properties.put("minWidth", new SimpleDoubleProperty(minWidth));
		return this;
	}

	public SunburstChartBuilder minHeight(
			final double minHeight) {

		properties.put("minHeight", new SimpleDoubleProperty(minHeight));
		return this;
	}

	public SunburstChartBuilder maxWidth(
			final double maxWidth) {

		properties.put("maxWidth", new SimpleDoubleProperty(maxWidth));
		return this;
	}

	public SunburstChartBuilder maxHeight(
			final double maxHeight) {

		properties.put("maxHeight", new SimpleDoubleProperty(maxHeight));
		return this;
	}

	public SunburstChartBuilder scaleX(
			final double scaleX) {

		properties.put("scaleX", new SimpleDoubleProperty(scaleX));
		return this;
	}

	public SunburstChartBuilder scaleY(
			final double scaleY) {

		properties.put("scaleY", new SimpleDoubleProperty(scaleY));
		return this;
	}

	public SunburstChartBuilder layoutX(
			final double layoutX) {

		properties.put("layoutX", new SimpleDoubleProperty(layoutX));
		return this;
	}

	public SunburstChartBuilder layoutY(
			final double layoutY) {

		properties.put("layoutY", new SimpleDoubleProperty(layoutY));
		return this;
	}

	public SunburstChartBuilder translateX(
			final double translateX) {

		properties.put("translateX", new SimpleDoubleProperty(translateX));
		return this;
	}

	public SunburstChartBuilder translateY(
			final double translateY) {

		properties.put("translateY", new SimpleDoubleProperty(translateY));
		return this;
	}

	public SunburstChartBuilder padding(
			final Insets insets) {

		properties.put("padding", new SimpleObjectProperty<>(insets));
		return this;
	}

	public SunburstChart build() {

		final SunburstChart sunburstChart;
		if (properties.containsKey("tree")) {
			sunburstChart = new SunburstChart((TreeNode) ((ObjectProperty<?>) properties.get("tree")).get());
		} else {
			sunburstChart = new SunburstChart();
		}
		for (final Map.Entry<String, Property<?>> mapEntry : properties.entrySet()) {

			final String key = mapEntry.getKey();
			final Property<?> property = properties.get(key);

			if ("prefSize".equals(key)) {
				final Dimension2D dim = (Dimension2D) ((ObjectProperty<?>) property).get();
				sunburstChart.setPrefSize(dim.getWidth(), dim.getHeight());
			} else if ("minSize".equals(key)) {
				final Dimension2D dim = (Dimension2D) ((ObjectProperty<?>) property).get();
				sunburstChart.setMinSize(dim.getWidth(), dim.getHeight());
			} else if ("maxSize".equals(key)) {
				final Dimension2D dim = (Dimension2D) ((ObjectProperty<?>) property).get();
				sunburstChart.setMaxSize(dim.getWidth(), dim.getHeight());
			} else if ("prefWidth".equals(key)) {
				sunburstChart.setPrefWidth(((DoubleProperty) property).get());
			} else if ("prefHeight".equals(key)) {
				sunburstChart.setPrefHeight(((DoubleProperty) property).get());
			} else if ("minWidth".equals(key)) {
				sunburstChart.setMinWidth(((DoubleProperty) property).get());
			} else if ("minHeight".equals(key)) {
				sunburstChart.setMinHeight(((DoubleProperty) property).get());
			} else if ("maxWidth".equals(key)) {
				sunburstChart.setMaxWidth(((DoubleProperty) property).get());
			} else if ("maxHeight".equals(key)) {
				sunburstChart.setMaxHeight(((DoubleProperty) property).get());
			} else if ("scaleX".equals(key)) {
				sunburstChart.setScaleX(((DoubleProperty) property).get());
			} else if ("scaleY".equals(key)) {
				sunburstChart.setScaleY(((DoubleProperty) property).get());
			} else if ("layoutX".equals(key)) {
				sunburstChart.setLayoutX(((DoubleProperty) property).get());
			} else if ("layoutY".equals(key)) {
				sunburstChart.setLayoutY(((DoubleProperty) property).get());
			} else if ("translateX".equals(key)) {
				sunburstChart.setTranslateX(((DoubleProperty) property).get());
			} else if ("translateY".equals(key)) {
				sunburstChart.setTranslateY(((DoubleProperty) property).get());
			} else if ("padding".equals(key)) {
				sunburstChart.setPadding((Insets) ((ObjectProperty<?>) property).get());
			} else if ("visibleData".equals(key)) {
				sunburstChart.setVisibleData((VisibleData) ((ObjectProperty<?>) property).get());
			} else if ("textOrientation".equals(key)) {
				sunburstChart.setTextOrientation((TextOrientation) ((ObjectProperty<?>) property).get());
			} else if ("backgroundColor".equals(key)) {
				sunburstChart.setBackgroundColor((Color) ((ObjectProperty<?>) property).get());
			} else if ("textColor".equals(key)) {
				sunburstChart.setTextColor((Color) ((ObjectProperty<?>) property).get());
			} else if ("useColorFromParent".equals(key)) {
				sunburstChart.setUseColorFromParent(((BooleanProperty) property).get());
			} else if ("decimals".equals(key)) {
				sunburstChart.setDecimals(((IntegerProperty) property).get());
			} else if ("autoTextColor".equals(key)) {
				sunburstChart.setAutoTextColor(((BooleanProperty) property).get());
			} else if ("brightTextColor".equals(key)) {
				sunburstChart.setBrightTextColor((Color) ((ObjectProperty<?>) property).get());
			} else if ("darkTextColor".equals(key)) {
				sunburstChart.setDarkTextColor((Color) ((ObjectProperty<?>) property).get());
			} else if ("useChartDataTextColor".equals(key)) {
				sunburstChart.setUseChartDataTextColor(((BooleanProperty) property).get());
			}
		}
		return sunburstChart;
	}
}
