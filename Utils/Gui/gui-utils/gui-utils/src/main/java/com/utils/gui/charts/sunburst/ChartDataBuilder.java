package com.utils.gui.charts.sunburst;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import com.utils.gui.charts.sunburst.events.ChartDataEventListener;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;

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

public class ChartDataBuilder {

	private final Map<String, Property<?>> properties = new HashMap<>();

	protected ChartDataBuilder() {
	}

	public static ChartDataBuilder create() {
		return new ChartDataBuilder();
	}

	public final ChartDataBuilder name(
			final String name) {
		properties.put("name", new SimpleStringProperty(name));
		return this;
	}

	public final ChartDataBuilder value(
			final double value) {
		properties.put("value", new SimpleDoubleProperty(value));
		return this;
	}

	public final ChartDataBuilder timestamp(
			final Instant timestamp) {
		properties.put("timestamp", new SimpleObjectProperty<>(timestamp));
		return this;
	}

	public final ChartDataBuilder timestamp(
			final ZonedDateTime timestamp) {
		properties.put("timestamp", new SimpleObjectProperty<>(timestamp.toInstant()));
		return this;
	}

	public final ChartDataBuilder fillColor(
			final Color color) {
		properties.put("fillColor", new SimpleObjectProperty<>(color));
		return this;
	}

	public final ChartDataBuilder strokeColor(
			final Color color) {
		properties.put("strokeColor", new SimpleObjectProperty<>(color));
		return this;
	}

	public final ChartDataBuilder textColor(
			final Color color) {
		properties.put("textColor", new SimpleObjectProperty<>(color));
		return this;
	}

	public final ChartDataBuilder animated(
			final boolean animated) {
		properties.put("animated", new SimpleBooleanProperty(animated));
		return this;
	}

	public final ChartDataBuilder onChartDataEvent(
			final ChartDataEventListener chartDataEventListener) {
		properties.put("onChartDataEvent", new SimpleObjectProperty<>(chartDataEventListener));
		return this;
	}

	public final ChartData build() {

		final ChartData chartData = new ChartData();
		for (final Map.Entry<String, Property<?>> mapEntry : properties.entrySet()) {

			final String key = mapEntry.getKey();
			final Property<?> property = mapEntry.getValue();

			if ("name".equals(key)) {
				chartData.setName(((StringProperty) property).get());
			} else if ("value".equals(key)) {
				chartData.setValue(((DoubleProperty) property).get());
			} else if ("timestamp".equals(key)) {
				chartData.setTimestamp((Instant) ((ObjectProperty<?>) property).get());
			} else if ("fillColor".equals(key)) {
				chartData.setFillColor((Color) ((ObjectProperty<?>) property).get());
			} else if ("strokeColor".equals(key)) {
				chartData.setStrokeColor((Color) ((ObjectProperty<?>) property).get());
			} else if ("textColor".equals(key)) {
				chartData.setTextColor((Color) ((ObjectProperty<?>) property).get());
			} else if ("animated".equals(key)) {
				chartData.setAnimated(((BooleanProperty) property).get());
			} else if ("onChartDataEvent".equals(key)) {
				chartData.setOnChartDataEvent((ChartDataEventListener) ((ObjectProperty<?>) property).get());
			}
		}
		return chartData;
	}
}
