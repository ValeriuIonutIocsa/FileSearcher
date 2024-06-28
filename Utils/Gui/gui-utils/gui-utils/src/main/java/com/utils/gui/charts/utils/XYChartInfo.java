/*
 * Copyright 2013 Jason Winnebeck
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

package com.utils.gui.charts.utils;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Region;

/**
 * XYChartInfo provides information about areas in an {@link XYChart}. Most of the methods deal with locating components
 * of the chart in the coordinate space of the reference node. The reference node could be the chart itself or an
 * ancestor of the chart.
 * <p>
 * There is a current limitation of this class in that there must not be scaling or rotation transformations between the
 * reference node and the chart's axes. Therefore, the reference node is best when it is the chart itself or an
 * immediate parent of the chart.
 * 
 * @param <T1>
 * @param <T2>
 *
 * @author Jason Winnebeck
 */
public class XYChartInfo<
		T1,
		T2> {

	private final XYChart<T1, T2> chart;
	private final Node referenceNode;

	/**
	 * Constructs the XYChartInfo to find chart information in the reference node's coordinate system.
	 */
	XYChartInfo(
			final XYChart<T1, T2> chart,
			final Node referenceNode) {

		this.chart = chart;
		this.referenceNode = referenceNode;
	}

	/**
	 * Constructs the XYChartInfo to find chart information in the chart's coordinate system.
	 */
	public XYChartInfo(
			final XYChart<T1, T2> chart) {
		this(chart, chart);
	}

	public XYChart<T1, T2> getChart() {
		return chart;
	}

	public Node getReferenceNode() {
		return referenceNode;
	}

	/**
	 * Given graphical coordinates in the reference's coordinate system, returns x and y axis value as a point via the
	 * {@link Axis#getValueForDisplay(double)} and {@link Axis#toNumericValue(Object)} methods.
	 */
	Point2D getDataCoordinates(
			final double x,
			final double y) {

		final Axis<T1> xAxis = chart.getXAxis();
		final Axis<T2> yAxis = chart.getYAxis();

		final double xStart = JFXUtil.getXShift(xAxis, referenceNode);
		final double yStart = JFXUtil.getYShift(yAxis, referenceNode);

		return new Point2D(
				xAxis.toNumericValue(xAxis.getValueForDisplay(x - xStart)),
				yAxis.toNumericValue(yAxis.getValueForDisplay(y - yStart)));
	}

	/**
	 * Given graphical coordinates in the reference's coordinate system, returns x and y axis value as a point via the
	 * {@link Axis#getValueForDisplay(double)} and {@link Axis#toNumericValue(Object)} methods.
	 *
	 * @param minX
	 *            lower X value (upper left point)
	 * @param minY
	 *            lower Y value (upper left point)
	 * @param maxX
	 *            upper X value (bottom right point)
	 * @param maxY
	 *            upper Y value (bottom right point)
	 */
	Rectangle2D getDataCoordinates(
			final double minX,
			final double minY,
			final double maxX,
			final double maxY) {

		if (minX > maxX || minY > maxY) {
			throw new IllegalArgumentException("min > max for X and/or Y");
		}

		final Axis<T1> xAxis = chart.getXAxis();
		final Axis<T2> yAxis = chart.getYAxis();

		final double xStart = JFXUtil.getXShift(xAxis, referenceNode);
		final double yStart = JFXUtil.getYShift(yAxis, referenceNode);

		final double minDataX = xAxis.toNumericValue(xAxis.getValueForDisplay(minX - xStart));
		final double maxDataX = xAxis.toNumericValue(xAxis.getValueForDisplay(maxX - xStart));

		// The "low" Y data value is actually at the maxY graphical location as Y graphical axis gets
		// larger as you go down on the screen.
		final double minDataY = yAxis.toNumericValue(yAxis.getValueForDisplay(maxY - yStart));
		final double maxDataY = yAxis.toNumericValue(yAxis.getValueForDisplay(minY - yStart));

		return new Rectangle2D(minDataX,
				minDataY,
				maxDataX - minDataX,
				maxDataY - minDataY);
	}

	/**
	 * Returns true if the given x and y coordinate in the reference's coordinate system is in the chart's plot area,
	 * based on the xAxis and yAxis locations. This method works regardless of the started/stopped state.
	 */
	boolean isInPlotArea(
			final double x,
			final double y) {
		return getPlotArea().contains(x, y);
	}

	/**
	 * Returns the plot area in the reference's coordinate space.
	 */
	Rectangle2D getPlotArea() {

		final Axis<T1> xAxis = chart.getXAxis();
		final Axis<T2> yAxis = chart.getYAxis();

		final double xStart = JFXUtil.getXShift(xAxis, referenceNode);
		final double yStart = JFXUtil.getYShift(yAxis, referenceNode);

		// If the direct method to get the width (which is based on its Node dimensions) is not found to
		// be appropriate, an alternative method is commented.
		// double width = xAxis.getDisplayPosition( xAxis.toRealValue( xAxis.getUpperBound() ) );
		final double width = xAxis.getWidth();
		// double height = yAxis.getDisplayPosition( yAxis.toRealValue( yAxis.getLowerBound() ) );
		final double height = yAxis.getHeight();

		return new Rectangle2D(xStart, yStart, width, height);
	}

	/**
	 * Returns the X axis area in the reference's coordinate space.
	 */
	Rectangle2D getXAxisArea() {
		return getControlArea(chart.getXAxis());
	}

	/**
	 * Returns the Y axis area in the reference's coordinate space.
	 */
	Rectangle2D getYAxisArea() {
		return getControlArea(chart.getYAxis());
	}

	private Rectangle2D getControlArea(
			final Region childRegion) {

		final double xStart = JFXUtil.getXShift(childRegion, referenceNode);
		final double yStart = JFXUtil.getYShift(childRegion, referenceNode);

		return new Rectangle2D(xStart, yStart, childRegion.getWidth(), childRegion.getHeight());
	}
}
