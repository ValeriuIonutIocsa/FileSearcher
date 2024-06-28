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

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.utils.gui.charts.sunburst.events.ChartDataEvent;
import com.utils.gui.charts.sunburst.events.ChartDataEvent.EventType;
import com.utils.gui.charts.sunburst.events.ChartDataEventListener;
import com.utils.string.StrUtils;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class ChartData implements Comparable<ChartData> {

	private final ChartDataEvent updateEvent = new ChartDataEvent(EventType.UPDATE, this);
	private final ChartDataEvent finishedEvent = new ChartDataEvent(EventType.FINISHED, this);
	private String name;
	private double value;
	private double oldValue;
	private Color fillColor;
	private Color strokeColor;
	private Color textColor;
	private Instant timestamp;
	private boolean animated;
	private long animationDuration;
	private final List<ChartDataEventListener> listenerList = new CopyOnWriteArrayList<>();
	private final DoubleProperty currentValue;
	private final Timeline timeline;

	public ChartData() {
		this("", 0, Color.rgb(233, 30, 99), Color.TRANSPARENT, Color.BLACK, Instant.now(), true, 800);
	}

	public ChartData(
			final String name) {
		this(name, 0, Color.rgb(233, 30, 99), Color.TRANSPARENT, Color.BLACK, Instant.now(), true, 800);
	}

	public ChartData(
			final double value) {
		this("", value, Color.rgb(233, 30, 99), Color.TRANSPARENT, Color.BLACK, Instant.now(), true, 800);
	}

	public ChartData(
			final double value,
			final Instant timestamp) {
		this("", value, Color.rgb(233, 30, 99), Color.TRANSPARENT, Color.BLACK, timestamp, true, 800);
	}

	public ChartData(
			final String name,
			final Color fillColor) {
		this(name, 0, fillColor, Color.TRANSPARENT, Color.BLACK, Instant.now(), true, 800);
	}

	public ChartData(
			final String name,
			final double value) {
		this(name, value, Color.rgb(233, 30, 99), Color.TRANSPARENT, Color.BLACK, Instant.now(), true, 800);
	}

	public ChartData(
			final String name,
			final double value,
			final Instant timestamp) {
		this(name, value, Color.rgb(233, 30, 99), Color.TRANSPARENT, Color.BLACK, timestamp, true, 800);
	}

	public ChartData(
			final String name,
			final double value,
			final Color fillColor) {
		this(name, value, fillColor, Color.TRANSPARENT, Color.BLACK, Instant.now(), true, 800);
	}

	public ChartData(
			final String name,
			final double value,
			final Color fillColor,
			final Color textColor) {
		this(name, value, fillColor, Color.TRANSPARENT, textColor, Instant.now(), true, 800);
	}

	public ChartData(
			final String name,
			final double value,
			final Color fillColor,
			final Instant timestamp) {
		this(name, value, fillColor, Color.TRANSPARENT, Color.BLACK, timestamp, true, 800);
	}

	public ChartData(
			final String name,
			final double value,
			final Color fillColor,
			final Color textColor,
			final Instant timestamp) {
		this(name, value, fillColor, Color.TRANSPARENT, textColor, timestamp, true, 800);
	}

	public ChartData(
			final String name,
			final double value,
			final Color fillColor,
			final Instant timestamp,
			final boolean animated,
			final long animationDuration) {
		this(name, value, fillColor, Color.TRANSPARENT, Color.BLACK, timestamp, animated, animationDuration);
	}

	public ChartData(
			final String name,
			final double value,
			final Color fillColor,
			final Color textColor,
			final Instant timestamp,
			final boolean animated,
			final long animationDuration) {
		this(name, value, fillColor, Color.TRANSPARENT, textColor, timestamp, animated, animationDuration);
	}

	public ChartData(
			final String name,
			final double value,
			final Color fillColor,
			final Color strokeColor,
			final Color textColor,
			final Instant timestamp,
			final boolean animated,
			final long animationDuration) {

		this.name = name;
		this.value = value;
		oldValue = 0;
		this.fillColor = fillColor;
		this.strokeColor = strokeColor;
		this.textColor = textColor;
		this.timestamp = timestamp;
		currentValue = new DoublePropertyBase(this.value) {

			@Override
			protected void invalidated() {
				oldValue = ChartData.this.value;
				ChartData.this.value = get();
				fireChartDataEvent(updateEvent);
			}

			@Override
			public Object getBean() {
				return ChartData.this;
			}

			@Override
			public String getName() {
				return "currentValue";
			}
		};
		timeline = new Timeline();
		this.animated = animated;
		this.animationDuration = animationDuration;

		timeline.setOnFinished(e -> fireChartDataEvent(finishedEvent));
	}

	public String getName() {
		return name;
	}

	public void setName(
			final String name) {

		this.name = name;
		fireChartDataEvent(updateEvent);
	}

	public double getValue() {
		return value;
	}

	public void setValue(
			final double value) {

		if (animated) {
			timeline.stop();
			final KeyValue kv1 = new KeyValue(currentValue, this.value, Interpolator.EASE_BOTH);
			final KeyValue kv2 = new KeyValue(currentValue, value, Interpolator.EASE_BOTH);
			final KeyFrame kf1 = new KeyFrame(Duration.ZERO, kv1);
			final KeyFrame kf2 = new KeyFrame(Duration.millis(animationDuration), kv2);
			timeline.getKeyFrames().setAll(kf1, kf2);
			timeline.play();
		} else {
			oldValue = this.value;
			this.value = value;
			fireChartDataEvent(finishedEvent);
		}
	}

	public double getOldValue() {
		return oldValue;
	}

	public Color getFillColor() {
		return fillColor;
	}

	public void setFillColor(
			final Color color) {

		fillColor = color;
		fireChartDataEvent(updateEvent);
	}

	public Color getStrokeColor() {
		return strokeColor;
	}

	public void setStrokeColor(
			final Color color) {

		strokeColor = color;
		fireChartDataEvent(updateEvent);
	}

	public Color getTextColor() {
		return textColor;
	}

	public void setTextColor(
			final Color color) {

		textColor = color;
		fireChartDataEvent(updateEvent);
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(
			final Instant timestamp) {

		this.timestamp = timestamp;
		fireChartDataEvent(updateEvent);
	}

	public ZonedDateTime getTimestampAdDateTime() {
		return getTimestampAsDateTime(ZoneId.systemDefault());
	}

	public ZonedDateTime getTimestampAsDateTime(
			final ZoneId zoneId) {
		return ZonedDateTime.ofInstant(timestamp, zoneId);
	}

	public LocalDate getTimestampAsLocalDate() {
		return getTimestampAsLocalDate(ZoneId.systemDefault());
	}

	public LocalDate getTimestampAsLocalDate(
			final ZoneId zoneId) {
		return getTimestampAsDateTime(zoneId).toLocalDate();
	}

	public boolean isAnimated() {
		return animated;
	}

	public void setAnimated(
			final boolean animated) {
		this.animated = animated;
	}

	public long getAnimationDuration() {
		return animationDuration;
	}

	public void setAnimationDuration(
			final long duration) {
		animationDuration = clamp(10, 10_000, duration);
	}

	@Override
	public int compareTo(
			final ChartData chartData) {
		return Double.compare(getValue(), chartData.getValue());
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public boolean equals(
			final Object obj) {

		final boolean result;
		if (getClass().isInstance(obj)) {
			result = compareTo(getClass().cast(obj)) == 0;
		} else {
			result = false;
		}
		return result;
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	private static long clamp(
			final long min,
			final long max,
			final long value) {

		final long result;
		if (value < min) {
			result = min;
		} else {
			result = Math.min(value, max);
		}
		return result;
	}

	public void setOnChartDataEvent(
			final ChartDataEventListener chartDataEventListener) {

		addChartDataEventListener(chartDataEventListener);
	}

	public void addChartDataEventListener(
			final ChartDataEventListener chartDataEventListener) {

		if (!listenerList.contains(chartDataEventListener)) {
			listenerList.add(chartDataEventListener);
		}
	}

	public void removeChartDataEventListener(
			final ChartDataEventListener chartDataEventListener) {
		listenerList.remove(chartDataEventListener);
	}

	public void fireChartDataEvent(
			final ChartDataEvent chartDataEvent) {

		for (final ChartDataEventListener listener : listenerList) {
			listener.onChartDataEvent(chartDataEvent);
		}
	}
}
