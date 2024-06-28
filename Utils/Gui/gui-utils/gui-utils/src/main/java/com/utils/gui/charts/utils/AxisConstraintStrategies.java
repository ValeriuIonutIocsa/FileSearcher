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

import com.utils.string.StrUtils;

/**
 * AxisConstraintStrategies contains default implementations of {@link AxisConstraintStrategy}.
 */
public final class AxisConstraintStrategies {

	private static final AxisConstraintStrategy DEFAULT = new DefaultAxisConstraintStrategy();

	private static final AxisConstraintStrategy IGNORE_OUTSIDE_CHART =
			new IgnoreOutsideChartAxisConstraintStrategy();

	private AxisConstraintStrategies() {
	}

	/**
	 * The default strategy restricts to horizontal axis if event is in the X axis, vertical if in Y axis, else allows
	 * both axes.
	 */
	public static AxisConstraintStrategy getDefault() {
		return DEFAULT;
	}

	/**
	 * Restricts to horizontal axis if event is in the X axis, vertical if in Y axis, allows both if in the plot area,
	 * or none if in none of those areas.
	 */
	static AxisConstraintStrategy getIgnoreOutsideChart() {
		return IGNORE_OUTSIDE_CHART;
	}

	/**
	 * Returns a strategy that always returns the given {@link AxisConstraint}.
	 */
	public static AxisConstraintStrategy getFixed(
			final AxisConstraint constraint) {
		return new FixedAxisConstraintStrategy(constraint);
	}

	private static final class DefaultAxisConstraintStrategy implements AxisConstraintStrategy {

		@Override
		public AxisConstraint getConstraint(
				final ChartInputContext context) {

			final AxisConstraint axisConstraint;
			if (context.isInXAxis()) {
				axisConstraint = AxisConstraint.HORIZONTAL;
			} else if (context.isInYAxis()) {
				axisConstraint = AxisConstraint.VERTICAL;
			} else {
				axisConstraint = AxisConstraint.BOTH;
			}
			return axisConstraint;
		}
	}

	private static final class IgnoreOutsideChartAxisConstraintStrategy implements AxisConstraintStrategy {

		@Override
		public AxisConstraint getConstraint(
				final ChartInputContext context) {

			final AxisConstraint axisConstraint;
			if (context.isInXAxis()) {
				axisConstraint = AxisConstraint.HORIZONTAL;
			} else if (context.isInYAxis()) {
				axisConstraint = AxisConstraint.VERTICAL;
			} else if (context.isInPlotArea()) {
				axisConstraint = AxisConstraint.BOTH;
			} else {
				axisConstraint = AxisConstraint.NONE;
			}
			return axisConstraint;
		}
	}

	private record FixedAxisConstraintStrategy(
			AxisConstraint constraint) implements AxisConstraintStrategy {

		@Override
		public String toString() {
			return StrUtils.reflectionToString(this);
		}

		@Override
		public AxisConstraint getConstraint(
				final ChartInputContext context) {
			return constraint;
		}
	}
}
