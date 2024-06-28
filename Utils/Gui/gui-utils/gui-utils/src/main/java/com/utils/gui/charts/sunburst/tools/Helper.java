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

package com.utils.gui.charts.sunburst.tools;

import com.utils.annotations.ApiMethod;

import javafx.scene.paint.Color;

public final class Helper {

	private Helper() {
	}

	@ApiMethod
	public static int clamp(
			final int min,
			final int max,
			final int value) {

		final int result;
		if (value < min) {
			result = min;
		} else {
			result = Math.min(value, max);
		}
		return result;
	}

	@ApiMethod
	public static double clamp(
			final double min,
			final double max,
			final double value) {

		final double result;
		if (value < min) {
			result = min;
		} else {
			result = Math.min(value, max);
		}
		return result;
	}

	@ApiMethod
	public static double[] colorToYUV(
			final Color color) {

		final double weightFactorRed = 0.299;
		final double weightFactorGreen = 0.587;
		final double weightFactorBlue = 0.144;
		final double uMax = 0.436;
		final double vMax = 0.615;
		final double y = clamp(0, 1, weightFactorRed * color.getRed() + weightFactorGreen * color.getGreen() +
				weightFactorBlue * color.getBlue());
		final double u = clamp(-uMax, uMax, uMax * ((color.getBlue() - y) / (1 - weightFactorBlue)));
		final double v = clamp(-vMax, vMax, vMax * ((color.getRed() - y) / (1 - weightFactorRed)));
		return new double[] { y, u, v };
	}

	@ApiMethod
	public static boolean isBright(
			final Color color) {
		return Double.compare(colorToYUV(color)[0], 0.5) >= 0.0;
	}

	@ApiMethod
	public static boolean isDark(
			final Color color) {
		return colorToYUV(color)[0] < 0.5;
	}
}
