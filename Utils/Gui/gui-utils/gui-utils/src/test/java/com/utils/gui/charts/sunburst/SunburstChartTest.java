package com.utils.gui.charts.sunburst;

import org.junit.jupiter.api.Test;

import com.utils.concurrency.ThreadUtils;
import com.utils.gui.AbstractCustomApplicationTest;
import com.utils.gui.GuiUtils;
import com.utils.gui.charts.sunburst.tree.TreeNode;
import com.utils.gui.factories.BasicControlsFactories;
import com.utils.string.StrUtils;

import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

class SunburstChartTest extends AbstractCustomApplicationTest {

	private static final Color PETROL_0 = Color.rgb(0, 96, 100);
	private static final Color PETROL_1 = Color.rgb(0, 151, 167);
	private static final Color PETROL_2 = Color.rgb(0, 188, 212);
	private static final Color PINK_0 = Color.rgb(136, 14, 79);
	private static final Color PINK_1 = Color.rgb(194, 24, 91);
	private static final Color PINK_2 = Color.rgb(233, 30, 99);
	private static final Color YELLOW_0 = Color.rgb(245, 127, 23);
	private static final Color YELLOW_1 = Color.rgb(251, 192, 45);
	private static final Color YELLOW_2 = Color.rgb(255, 235, 59);
	private static final Color GREEN_0 = Color.rgb(27, 94, 32);
	private static final Color GREEN_1 = Color.rgb(56, 142, 60);
	private static final Color GREEN_2 = Color.rgb(76, 175, 80);

	@Test
	void testLayout() {

		final TreeNode treeNodeRoot;
		final int input = StrUtils.tryParsePositiveInt("0");
		if (input == 1) {
			treeNodeRoot = createTreeNode1();
		} else {
			treeNodeRoot = createTreeNode();
		}

		final GridPane gridPane = new GridPane();

		final SunburstChart sunburstChart = SunburstChartBuilder.create()
				.prefSize(750, 750)
				.tree(treeNodeRoot)
				.textOrientation(TextOrientation.TANGENT)
				.useColorFromParent(false)
				.visibleData(VisibleData.NAME)
				.backgroundColor(null)
				.textColor(Color.WHITE)
				.autoTextColor(true)
				.useChartDataTextColor(true)
				.decimals(2)
				.build();
		GuiUtils.addToGridPane(gridPane, sunburstChart, 0, 0, 1, 1,
				Pos.CENTER, Priority.NEVER, Priority.NEVER, 10, 10, 10, 10);

		final Button buttonShowEffect = BasicControlsFactories.getInstance().createButton("Show Effect");
		buttonShowEffect.setFont(Font.font("Helvetica", FontWeight.BOLD, 16));
		buttonShowEffect.setOnAction(event -> showEffect(sunburstChart));
		GuiUtils.addToGridPane(gridPane, buttonShowEffect, 1, 0, 1, 1,
				Pos.CENTER, Priority.NEVER, Priority.NEVER, 10, 10, 10, 10);

		GuiUtils.run(() -> {

			final StackPane stackPaneContainer = getStackPaneContainer();
			stackPaneContainer.getChildren().add(gridPane);
		});
		ThreadUtils.trySleep(Long.MAX_VALUE);
	}

	private static TreeNode createTreeNode1() {

		final TreeNode treeNodeRoot = new TreeNode(new ChartData("ROOT"));

		final TreeNode first = new TreeNode(new ChartData("1st", 1, PETROL_0), treeNodeRoot);
		new TreeNode(new ChartData("2nd", 2, PINK_0), treeNodeRoot);

		new TreeNode(new ChartData("Jan", 1, PETROL_1), first);

		return treeNodeRoot;
	}

	private static TreeNode createTreeNode() {

		final TreeNode treeNodeRoot = new TreeNode(new ChartData("ROOT"));

		final TreeNode first = new TreeNode(new ChartData("1st", 8.3, PETROL_0), treeNodeRoot);
		final TreeNode second = new TreeNode(new ChartData("2nd", 2.2, PINK_0), treeNodeRoot);
		final TreeNode third = new TreeNode(new ChartData("3rd", 1.4, YELLOW_0), treeNodeRoot);
		final TreeNode fourth = new TreeNode(new ChartData("4th", 1.2, GREEN_0), treeNodeRoot);

		new TreeNode(new ChartData("Jan", 3.5, PETROL_1), first);
		final TreeNode feb = new TreeNode(new ChartData("Feb", 3.1, PETROL_1), first);
		new TreeNode(new ChartData("Mar", 1.7, PETROL_1), first);
		new TreeNode(new ChartData("Apr", 1.1, PINK_1), second);
		final TreeNode may = new TreeNode(new ChartData("May", 0.8, PINK_1), second);
		final TreeNode jun = new TreeNode(new ChartData("Jun", 0.3, PINK_1), second);
		new TreeNode(new ChartData("Jul", 0.7, YELLOW_1), third);
		new TreeNode(new ChartData("Aug", 0.6, YELLOW_1), third);
		new TreeNode(new ChartData("Sep", 0.1, YELLOW_1), third);
		new TreeNode(new ChartData("Oct", 0.5, GREEN_1), fourth);
		new TreeNode(new ChartData("Nov", 0.4, GREEN_2), fourth);
		new TreeNode(new ChartData("Dec", 0.3, GREEN_1), fourth);

		new TreeNode(new ChartData("Week 5", 1.2, PETROL_2), feb);
		new TreeNode(new ChartData("Week 6", 0.8, PETROL_2), feb);
		new TreeNode(new ChartData("Week 7", 0.6, PETROL_2), feb);
		new TreeNode(new ChartData("Week 8", 0.5, PETROL_2), feb);

		new TreeNode(new ChartData("Week 17", 1.2, YELLOW_2), may);
		new TreeNode(new ChartData("Week 18", 0.8, PINK_2), may);
		new TreeNode(new ChartData("Week 19", 0.6, YELLOW_2), may);
		new TreeNode(new ChartData("Week 20", 0.5, PINK_2), may);

		new TreeNode(new ChartData("Week 21", 1.2, PINK_2), jun);
		new TreeNode(new ChartData("Week 22", 0.8, YELLOW_2), jun);
		new TreeNode(new ChartData("Week 23", 0.6, PINK_2), jun);
		new TreeNode(new ChartData("Week 24", 0.5, YELLOW_2), jun);

		return treeNodeRoot;
	}

	private static void showEffect(
			final SunburstChart sunburstChart) {

		final TreeNode treeNodeRoot = sunburstChart.getRoot();

		final TreeNode treeNodeFirstChild = treeNodeRoot.getChildren().getFirst();
		final Path path = treeNodeFirstChild.getPath();
		final FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.25), path);
		fadeTransition.setFromValue(1.0);
		fadeTransition.setToValue(0.0);
		fadeTransition.setCycleCount(10);
		fadeTransition.play();
		fadeTransition.setOnFinished(event -> path.setOpacity(1.0));
	}
}
