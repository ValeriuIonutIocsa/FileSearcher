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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.utils.gui.charts.sunburst.tools.Helper;
import com.utils.gui.charts.sunburst.tree.TreeNode;
import com.utils.gui.factories.BasicControlsFactories;
import com.utils.string.StrUtils;

import javafx.beans.DefaultProperty;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.IntegerPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

@DefaultProperty("children")
public class SunburstChart extends Region {

	private static final double PREFERRED_WIDTH = 250;
	private static final double PREFERRED_HEIGHT = 250;
	private static final double MINIMUM_WIDTH = 50;
	private static final double MINIMUM_HEIGHT = 50;
	private static final double MAXIMUM_WIDTH = 2048;
	private static final double MAXIMUM_HEIGHT = 2048;
	private static final Color BRIGHT_TEXT_COLOR = Color.WHITE;
	private static final Color DARK_TEXT_COLOR = Color.BLACK;
	private double size;
	private double centerX;
	private double centerY;
	private Pane segmentPane;
	private Canvas chartCanvas;
	private GraphicsContext chartCtx;
	private Pane pane;
	private final Paint backgroundPaint;
	private final Paint borderPaint;
	private final double borderWidth;
	private final List<Path> segments;
	private VisibleData visibleData;
	private ObjectProperty<VisibleData> visibleDataProperty;
	private TextOrientation textOrientation;
	private ObjectProperty<TextOrientation> textOrientationProperty;
	private Color backgroundColor;
	private ObjectProperty<Color> backgroundColorProperty;
	private Color textColor;
	private ObjectProperty<Color> textColorProperty;
	private boolean useColorFromParent;
	private BooleanProperty useColorFromParentProperty;
	private int decimals;
	private IntegerProperty decimalsProperty;
	private boolean autoTextColor;
	private BooleanProperty autoTextColorProperty;
	private Color brightTextColor;
	private ObjectProperty<Color> brightTextColorProperty;
	private Color darkTextColor;
	private ObjectProperty<Color> darkTextColorProperty;
	private boolean useChartDataTextColor;
	private BooleanProperty useChartDataTextColorProperty;
	private String formatString;
	private TreeNode tree;
	private TreeNode root;
	private int maxLevel;
	private final Map<Integer, List<TreeNode>> levelMap;
	private final InvalidationListener sizeListener;

	SunburstChart() {
		this(new TreeNode(new ChartData()));
	}

	SunburstChart(
			final TreeNode treeNode) {

		backgroundPaint = Color.TRANSPARENT;
		borderPaint = Color.TRANSPARENT;
		borderWidth = 0d;
		segments = new ArrayList<>(64);
		visibleData = VisibleData.NAME;
		textOrientation = TextOrientation.TANGENT;
		backgroundColor = Color.WHITE;
		textColor = Color.BLACK;
		useColorFromParent = false;
		decimals = 0;
		autoTextColor = true;
		brightTextColor = BRIGHT_TEXT_COLOR;
		darkTextColor = DARK_TEXT_COLOR;
		useChartDataTextColor = false;
		formatString = "%.0f";
		tree = treeNode;
		levelMap = new HashMap<>(8);
		sizeListener = o -> resize();
		initGraphics();
		registerListeners();
	}

	private void initGraphics() {

		if (Double.compare(getPrefWidth(), 0.0) <= 0 || Double.compare(getPrefHeight(), 0.0) <= 0 ||
				Double.compare(getWidth(), 0.0) <= 0 || Double.compare(getHeight(), 0.0) <= 0) {
			if (getPrefWidth() > 0 && getPrefHeight() > 0) {
				setPrefSize(getPrefWidth(), getPrefHeight());
			} else {
				setPrefSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);
			}
		}

		segmentPane = new Pane();

		chartCanvas = new Canvas(PREFERRED_WIDTH, PREFERRED_HEIGHT);
		chartCanvas.setMouseTransparent(true);

		chartCtx = chartCanvas.getGraphicsContext2D();

		pane = new Pane(segmentPane, chartCanvas);
		pane.setBackground(new Background(new BackgroundFill(backgroundPaint, CornerRadii.EMPTY, Insets.EMPTY)));
		pane.setBorder(new Border(new BorderStroke(borderPaint, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
				new BorderWidths(borderWidth))));

		getChildren().setAll(pane);

		prepareData();
	}

	private void registerListeners() {

		widthProperty().addListener(sizeListener);
		heightProperty().addListener(sizeListener);
	}

	@Override
	protected double computeMinWidth(
			final double height) {
		return MINIMUM_WIDTH;
	}

	@Override
	protected double computeMinHeight(
			final double width) {
		return MINIMUM_HEIGHT;
	}

	@Override
	protected double computeMaxWidth(
			final double height) {
		return MAXIMUM_WIDTH;
	}

	@Override
	protected double computeMaxHeight(
			final double width) {
		return MAXIMUM_HEIGHT;
	}

	public void dispose() {

		widthProperty().removeListener(sizeListener);
		heightProperty().removeListener(sizeListener);
	}

	/**
	 * Returns the data that should be visualized in the chart segments
	 *
	 * @return the data that should be visualized in the chart segments
	 */
	public VisibleData getVisibleData() {

		final VisibleData visibleData;
		if (null == visibleDataProperty) {
			visibleData = this.visibleData;
		} else {
			visibleData = visibleDataProperty.get();
		}
		return visibleData;
	}

	/**
	 * Defines the data that should be visualized in the chart segments
	 */
	public void setVisibleData(
			final VisibleData visibleData) {

		if (null == visibleDataProperty) {
			this.visibleData = visibleData;
			redraw();
		} else {
			visibleDataProperty.set(visibleData);
		}
	}

	public ObjectProperty<VisibleData> visibleDataProperty() {

		if (null == visibleDataProperty) {

			visibleDataProperty = new ObjectPropertyBase<>(visibleData) {

				@Override
				protected void invalidated() {
					redraw();
				}

				@Override
				public Object getBean() {
					return SunburstChart.this;
				}

				@Override
				public String getName() {
					return "visibleData";
				}
			};
			visibleData = null;
		}
		return visibleDataProperty;
	}

	/**
	 * Returns the orientation the text will be drawn in the segments
	 *
	 * @return the orientation the text will be drawn in the segments
	 */
	public TextOrientation getTextOrientation() {

		final TextOrientation textOrientation;
		if (null == textOrientationProperty) {
			textOrientation = this.textOrientation;
		} else {
			textOrientation = textOrientationProperty.get();
		}
		return textOrientation;
	}

	/**
	 * Defines the orientation the text will be drawn in the segments
	 */
	public void setTextOrientation(
			final TextOrientation textOrientation) {

		if (null == textOrientationProperty) {
			this.textOrientation = textOrientation;
			redraw();
		} else {
			textOrientationProperty.set(textOrientation);
		}
	}

	public ObjectProperty<TextOrientation> textOrientationProperty() {

		if (null == textOrientationProperty) {

			textOrientationProperty = new ObjectPropertyBase<>(textOrientation) {

				@Override
				protected void invalidated() {
					redraw();
				}

				@Override
				public Object getBean() {
					return SunburstChart.this;
				}

				@Override
				public String getName() {
					return "textOrientation";
				}
			};
			textOrientation = null;
		}
		return textOrientationProperty;
	}

	/**
	 * Returns the color that will be used to fill the background of the chart
	 *
	 * @return the color that will be used to fill the background of the chart
	 */
	public Color getBackgroundColor() {

		final Color backgroundColor;
		if (backgroundColorProperty == null) {
			backgroundColor = this.backgroundColor;
		} else {
			backgroundColor = backgroundColorProperty.get();
		}
		return backgroundColor;
	}

	/**
	 * Defines the color that will be used to fill the background of the chart
	 */
	public void setBackgroundColor(
			final Color color) {

		if (null == backgroundColorProperty) {
			backgroundColor = color;
			redraw();
		} else {
			backgroundColorProperty.set(color);
		}
	}

	public ObjectProperty<Color> backgroundColorProperty() {

		if (null == backgroundColorProperty) {

			backgroundColorProperty = new ObjectPropertyBase<>(backgroundColor) {

				@Override
				protected void invalidated() {
					redraw();
				}

				@Override
				public Object getBean() {
					return SunburstChart.this;
				}

				@Override
				public String getName() {
					return "backgroundColor";
				}
			};
			backgroundColor = null;
		}
		return backgroundColorProperty;
	}

	/**
	 * Returns the color that will be used to draw text in segments if useChartDataTextColor == false
	 *
	 * @return the color that will be used to draw text in segments if useChartDataTextColor == false
	 */
	public Color getTextColor() {

		final Color textColor;
		if (null == textColorProperty) {
			textColor = this.textColor;
		} else {
			textColor = textColorProperty.get();
		}
		return textColor;
	}

	/**
	 * Defines the color that will be used to draw text in segments if useChartDataTextColor == false
	 */
	public void setTextColor(
			final Color color) {

		if (null == textColorProperty) {
			textColor = color;
			redraw();
		} else {
			textColorProperty.set(color);
		}
	}

	public ObjectProperty<Color> textColorProperty() {

		if (null == textColorProperty) {

			textColorProperty = new ObjectPropertyBase<>(textColor) {

				@Override
				protected void invalidated() {
					redraw();
				}

				@Override
				public Object getBean() {
					return SunburstChart.this;
				}

				@Override
				public String getName() {
					return "textColor";
				}
			};
			textColor = null;
		}
		return textColorProperty;
	}

	/**
	 * Returns true if the color of all chart segments in one group should be filled with the color of the groups root
	 * node or by the color defined in the chart data elements
	 */
	public boolean isUseColorFromParent() {

		final boolean useColorFromParent;
		if (null == useColorFromParentProperty) {
			useColorFromParent = this.useColorFromParent;
		} else {
			useColorFromParent = useColorFromParentProperty.get();
		}
		return useColorFromParent;
	}

	/**
	 * Defines if the color of all chart segments in one group should be filled with the color of the groups root node
	 * or by the color defined in the chart data elements
	 */
	public void setUseColorFromParent(
			final boolean use) {

		if (null == useColorFromParentProperty) {
			useColorFromParent = use;
			redraw();
		} else {
			useColorFromParentProperty.set(use);
		}
	}

	public BooleanProperty useColorFromParentProperty() {
		if (null == useColorFromParentProperty) {
			useColorFromParentProperty = new BooleanPropertyBase(useColorFromParent) {

				@Override
				protected void invalidated() {
					redraw();
				}

				@Override
				public Object getBean() {
					return SunburstChart.this;
				}

				@Override
				public String getName() {
					return "useColorFromParent";
				}
			};
		}
		return useColorFromParentProperty;
	}

	/**
	 * Returns the number of decimals that will be used to format the values in the tooltip.
	 */
	public int getDecimals() {

		final int decimals;
		if (null == decimalsProperty) {
			decimals = this.decimals;
		} else {
			decimals = decimalsProperty.get();
		}
		return decimals;
	}

	/**
	 * Defines the number of decimals that will be used to format the values in the tooltip.
	 */
	public void setDecimals(
			final int decimals) {

		if (null == decimalsProperty) {
			this.decimals = Helper.clamp(0, 5, decimals);
			formatString = "%." + this.decimals + "f";
			redraw();
		} else {
			decimalsProperty.set(decimals);
		}
	}

	public IntegerProperty decimalsProperty() {

		if (null == decimalsProperty) {
			decimalsProperty = new IntegerPropertyBase(decimals) {

				@Override
				protected void invalidated() {
					set(Helper.clamp(0, 5, get()));
					formatString = "%." + get() + "f";
					redraw();
				}

				@Override
				public Object getBean() {
					return SunburstChart.this;
				}

				@Override
				public String getName() {
					return "decimals";
				}
			};
		}
		return decimalsProperty;
	}

	/**
	 * Returns true if the text color of the chart data should be adjusted according to the chart data fill color. e.g.
	 * if the fill color is dark the text will be set to the defined brightTextColor and vice versa.
	 *
	 * @return true if the text color of the chart data should be adjusted according to the chart data fill color
	 */
	public boolean isAutoTextColor() {

		final boolean autoTextColor;
		if (null == autoTextColorProperty) {
			autoTextColor = this.autoTextColor;
		} else {
			autoTextColor = autoTextColorProperty.get();
		}
		return autoTextColor;
	}

	/**
	 * Defines if the text color of the chart data should be adjusted according to the chart data fill color.
	 */
	public void setAutoTextColor(
			final boolean automatic) {

		if (null == autoTextColorProperty) {
			autoTextColor = automatic;
			adjustTextColors();
			redraw();
		} else {
			autoTextColorProperty.set(automatic);
		}
	}

	public BooleanProperty autoTextColorProperty() {

		if (null == autoTextColorProperty) {

			autoTextColorProperty = new BooleanPropertyBase(autoTextColor) {

				@Override
				protected void invalidated() {
					adjustTextColors();
					redraw();
				}

				@Override
				public Object getBean() {
					return SunburstChart.this;
				}

				@Override
				public String getName() {
					return "autoTextColor";
				}
			};
		}
		return autoTextColorProperty;
	}

	/**
	 * Returns the color that will be used by the autoTextColor feature as the bright text on dark segment fill colors
	 *
	 * @return the color that will be used by the autoTextColor feature as the bright text on dark segment fill colors
	 */
	public Color getBrightTextColor() {

		final Color brightTextColor;
		if (null == brightTextColorProperty) {
			brightTextColor = this.brightTextColor;
		} else {
			brightTextColor = brightTextColorProperty.get();
		}
		return brightTextColor;
	}

	/**
	 * Defines the color that will be used by the autoTextColor feature as the bright text on dark segment fill colors.
	 */
	public void setBrightTextColor(
			final Color color) {

		if (null == brightTextColorProperty) {

			brightTextColor = color;
			if (isAutoTextColor()) {
				adjustTextColors();
				redraw();
			}

		} else {
			brightTextColorProperty.set(color);
		}
	}

	public ObjectProperty<Color> brightTextColorProperty() {

		if (null == brightTextColorProperty) {

			brightTextColorProperty = new ObjectPropertyBase<>(brightTextColor) {

				@Override
				protected void invalidated() {
					if (isAutoTextColor()) {
						adjustTextColors();
						redraw();
					}
				}

				@Override
				public Object getBean() {
					return SunburstChart.this;
				}

				@Override
				public String getName() {
					return "brightTextColor";
				}
			};
			brightTextColor = null;
		}
		return brightTextColorProperty;
	}

	/**
	 * Returns the color that will be used by the autoTextColor feature as the dark text on bright segment fill colors
	 *
	 * @return the color that will be used by the autoTextColor feature as the dark text on bright segment fill colors
	 */
	public Color getDarkTextColor() {

		final Color darkTextColor;
		if (null == darkTextColorProperty) {
			darkTextColor = this.darkTextColor;
		} else {
			darkTextColor = darkTextColorProperty.get();
		}
		return darkTextColor;
	}

	/**
	 * Defines the color that will be used by the autoTextColor feature as the dark text on bright segment fill colors.
	 */
	public void setDarkTextColor(
			final Color color) {

		if (null == darkTextColorProperty) {

			darkTextColor = color;
			if (isAutoTextColor()) {
				adjustTextColors();
				redraw();
			}

		} else {
			darkTextColorProperty.set(color);
		}
	}

	public ObjectProperty<Color> darkTextColorProperty() {

		if (null == darkTextColorProperty) {

			darkTextColorProperty = new ObjectPropertyBase<>(darkTextColor) {

				@Override
				protected void invalidated() {
					if (isAutoTextColor()) {
						adjustTextColors();
						redraw();
					}
				}

				@Override
				public Object getBean() {
					return SunburstChart.this;
				}

				@Override
				public String getName() {
					return "darkTextColor";
				}
			};
			darkTextColor = null;
		}
		return darkTextColorProperty;
	}

	/**
	 * Returns true if the text color of the ChartData elements should be used to fill the text in the segments
	 *
	 * @return true if the text color of the segments will be taken from the ChartData elements
	 */
	public boolean isUseChartDataTextColor() {

		final boolean useChartDataTextColor;
		if (null == useChartDataTextColorProperty) {
			useChartDataTextColor = this.useChartDataTextColor;
		} else {
			useChartDataTextColor = useChartDataTextColorProperty.get();
		}
		return useChartDataTextColor;
	}

	/**
	 * Defines if the text color of the segments should be taken from the ChartData elements.
	 */
	public void setUseChartDataTextColor(
			final boolean use) {

		if (null == useChartDataTextColorProperty) {
			useChartDataTextColor = use;
			redraw();
		} else {
			useChartDataTextColorProperty.set(use);
		}
	}

	public BooleanProperty getUseChartDataTextColor() {

		if (null == useChartDataTextColorProperty) {

			useChartDataTextColorProperty = new BooleanPropertyBase(useChartDataTextColor) {

				@Override
				protected void invalidated() {
					redraw();
				}

				@Override
				public Object getBean() {
					return SunburstChart.this;
				}

				@Override
				public String getName() {
					return "useChartDataTextColor";
				}
			};
		}
		return useChartDataTextColorProperty;
	}

	/**
	 * Defines the root element of the tree.
	 */
	public void setTree(
			final TreeNode treeNode) {

		tree = treeNode;
		prepareData();
		if (isAutoTextColor()) {
			adjustTextColors();
		}
		drawChart();
	}

	private void adjustTextColors() {

		final Color brightColor = getBrightTextColor();
		final Color darkColor = getDarkTextColor();
		root.stream().forEach(node -> {

			final ChartData data = node.getData();
			final boolean darkFillColor = Helper.isDark(data.getFillColor());
			final boolean darkTextColor = Helper.isDark(data.getTextColor());
			if (darkFillColor && darkTextColor) {
				data.setTextColor(brightColor);
			}
			if (!darkFillColor && !darkTextColor) {
				data.setTextColor(darkColor);
			}
		});
	}

	private void prepareData() {

		root = tree.getTreeRoot();
		maxLevel = root.getMaxLevel();

		levelMap.clear();
		for (int i = 0; i <= maxLevel; i++) {
			levelMap.put(i, new ArrayList<>());
		}
		root.stream().forEach(node -> levelMap.get(node.getDepth()).add(node));

		for (int level = 1; level < maxLevel; level++) {

			final List<TreeNode> treeNodeList = levelMap.get(level);
			treeNodeList.stream()
					.filter(node -> node.getChildren().isEmpty())
					.forEach(node -> node.addNode(new TreeNode(
							new ChartData("", 0, Color.TRANSPARENT, Color.TRANSPARENT), node)));
		}
	}

	private void drawChart() {

		levelMap.clear();
		for (int i = 0; i <= maxLevel; i++) {
			levelMap.put(i, new ArrayList<>());
		}
		root.stream().forEach(node -> levelMap.get(node.getDepth()).add(node));
		final double ringStepSize = size * 0.8 / maxLevel;
		final double ringRadiusStep = ringStepSize * 0.5;
		final double barWidth = ringStepSize * 0.49;
		final double textRadiusStep = size * 0.4 / maxLevel;
		final Color bkgColor = getBackgroundColor();
		final Color textColor = getTextColor();
		final TextOrientation textOrientation = getTextOrientation();
		final double maxTextWidth = barWidth * 0.9;

		chartCtx.clearRect(0, 0, size, size);
		chartCtx.setFill(Color.TRANSPARENT);
		chartCtx.fillRect(0, 0, size, size);

		chartCtx.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
		chartCtx.setTextBaseline(VPos.CENTER);
		chartCtx.setTextAlign(TextAlignment.CENTER);
		chartCtx.setLineCap(StrokeLineCap.BUTT);

		segments.clear();

		for (int level = 1; level <= maxLevel; level++) {

			final List<TreeNode> nodesAtLevel = levelMap.get(level);
			final double outerRadius = ringRadiusStep * level + barWidth * 0.5;
			final double innerRadius = outerRadius - barWidth;

			double segmentStartAngle;
			double segmentEndAngle = 0;
			for (final TreeNode node : nodesAtLevel) {

				final ChartData segmentData = node.getData();
				final double segmentAngle = node.getParentAngle() * node.getPercentage();
				final Color segmentColor;
				if (isUseColorFromParent()) {
					segmentColor = node.getMyRoot().getData().getFillColor();
				} else {
					segmentColor = segmentData.getFillColor();
				}

				segmentStartAngle = 90 + segmentEndAngle;
				segmentEndAngle -= segmentAngle;

				// Only draw if segment fill color is not TRANSPARENT
				if (!Color.TRANSPARENT.equals(segmentData.getFillColor())) {
					final double value = segmentData.getValue();

					segments.add(createSegment(-segmentStartAngle, -segmentStartAngle + segmentAngle, innerRadius,
							outerRadius, segmentColor, bkgColor, node));

					final VisibleData visibleData = getVisibleData();
					if (visibleData != VisibleData.NONE && segmentAngle > textOrientation.getMaxAngle()) {
						final double radText = Math.toRadians(segmentStartAngle - (segmentAngle * 0.5));
						final double cosText = Math.cos(radText);
						final double sinText = Math.sin(radText);
						final double textRadius = textRadiusStep * level;
						final double textX = centerX + textRadius * cosText;
						final double textY = centerY - textRadius * sinText;

						if (isUseChartDataTextColor()) {
							chartCtx.setFill(segmentData.getTextColor());
						} else {
							chartCtx.setFill(textColor);
						}

						chartCtx.save();
						chartCtx.translate(textX, textY);

						rotateContextForText(chartCtx, segmentStartAngle, -(segmentAngle * 0.5), textOrientation);

						if (visibleData == VisibleData.VALUE) {
							final String valueString = String.format(Locale.US, formatString, value);
							chartCtx.fillText(valueString, 0, 0, maxTextWidth);

						} else if (visibleData == VisibleData.NAME) {
							final String name = segmentData.getName();
							chartCtx.fillText(name, 0, 0, maxTextWidth);

						} else if (visibleData == VisibleData.NAME_VALUE) {
							final String name = segmentData.getName();
							final String valueString = String.format(Locale.US, formatString, value);
							final String nameAndValueString = name + " (" + valueString + ")";
							chartCtx.fillText(nameAndValueString, 0, 0, maxTextWidth);
						}
						chartCtx.restore();
					}
				}
			}
		}

		segmentPane.getChildren().setAll(segments);
	}

	private Path createSegment(
			final double startAngle,
			final double endAngle,
			final double innerRadius,
			final double outerRadius,
			final Color fill,
			final Color stroke,
			final TreeNode treeNode) {

		final double startAngleRad = Math.toRadians(startAngle + 90);
		final double endAngleRad = Math.toRadians(endAngle + 90);
		final boolean largeAngle = Math.abs(endAngle - startAngle) > 180.0;

		final double x1 = centerX + innerRadius * Math.sin(startAngleRad);
		final double y1 = centerY - innerRadius * Math.cos(startAngleRad);

		final double x2 = centerX + outerRadius * Math.sin(startAngleRad);
		final double y2 = centerY - outerRadius * Math.cos(startAngleRad);

		final double x3 = centerX + outerRadius * Math.sin(endAngleRad);
		final double y3 = centerY - outerRadius * Math.cos(endAngleRad);

		final double x4 = centerX + innerRadius * Math.sin(endAngleRad);
		final double y4 = centerY - innerRadius * Math.cos(endAngleRad);

		final MoveTo moveTo1 = new MoveTo(x1, y1);
		final LineTo lineTo2 = new LineTo(x2, y2);
		final ArcTo arcTo3 = new ArcTo(outerRadius, outerRadius, 0, x3, y3, largeAngle, true);
		final LineTo lineTo4 = new LineTo(x4, y4);
		final ArcTo arcTo1 = new ArcTo(innerRadius, innerRadius, 0, x1, y1, largeAngle, false);

		final Path path = new Path(moveTo1, lineTo2, arcTo3, lineTo4, arcTo1);
		path.setFill(fill);
		path.setStroke(stroke);
		createTooltip(path, treeNode);
		treeNode.setPath(path);

		return path;
	}

	private static void createTooltip(
			final Path path,
			final TreeNode treeNode) {

		final ChartData chartData = treeNode.getData();
		final String name = chartData.getName();
		final double percentage = treeNode.getPercentageOfTotal();
		final String percentageString = StrUtils.doubleToPercentageString(percentage, 2);
		final String tooltipText = name + " (" + percentageString + ")";
		final Tooltip tooltip = BasicControlsFactories.getInstance().createTooltip(tooltipText);
		tooltip.setFont(Font.font("Helvetica", 12));
		Tooltip.install(path, tooltip);
	}

	private static void rotateContextForText(
			final GraphicsContext graphicsContext,
			final double startAngle,
			final double angle,
			final TextOrientation orientation) {

		if (orientation == TextOrientation.TANGENT) {
			if ((360 - startAngle - angle) % 360 > 90 && (360 - startAngle - angle) % 360 < 270) {
				graphicsContext.rotate((180 - startAngle - angle) % 360);
			} else {
				graphicsContext.rotate((360 - startAngle - angle) % 360);
			}

		} else if (orientation == TextOrientation.ORTHOGONAL) {
			if ((360 - startAngle - angle - 90) % 360 > 90 && (360 - startAngle - angle - 90) % 360 < 270) {
				graphicsContext.rotate((90 - startAngle - angle) % 360);
			} else {
				graphicsContext.rotate((270 - startAngle - angle) % 360);
			}
		}
	}

	private void resize() {

		final double width = getWidth() - getInsets().getLeft() - getInsets().getRight();
		final double height = getHeight() - getInsets().getTop() - getInsets().getBottom();
		size = Math.min(width, height);

		if (width > 0 && height > 0) {
			pane.setMaxSize(size, size);
			pane.setPrefSize(size, size);
			pane.relocate((getWidth() - size) * 0.5, (getHeight() - size) * 0.5);

			segmentPane.setPrefSize(size, size);

			chartCanvas.setWidth(size);
			chartCanvas.setHeight(size);

			final double center = size * 0.5;
			centerX = center;
			centerY = center;

			redraw();
		}
	}

	private void redraw() {

		pane.setBackground(new Background(new BackgroundFill(backgroundPaint, CornerRadii.EMPTY, Insets.EMPTY)));
		pane.setBorder(new Border(new BorderStroke(borderPaint, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
				new BorderWidths(borderWidth / PREFERRED_WIDTH * size))));

		final Color backgroundColor = getBackgroundColor();
		if (backgroundColor != null) {
			segmentPane.setBackground(new Background(
					new BackgroundFill(backgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
		} else {
			segmentPane.setBackground(Background.EMPTY);
		}
		segmentPane.setManaged(true);
		segmentPane.setVisible(true);

		drawChart();
	}

	public TreeNode getRoot() {
		return root;
	}
}
