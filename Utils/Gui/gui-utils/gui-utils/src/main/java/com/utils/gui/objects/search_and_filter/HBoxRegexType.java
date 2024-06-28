package com.utils.gui.objects.search_and_filter;

import com.utils.gui.AbstractCustomControl;
import com.utils.gui.GuiUtils;
import com.utils.gui.factories.BasicControlsFactories;
import com.utils.gui.factories.LayoutControlsFactories;
import com.utils.gui.help.StackPaneHelp;
import com.utils.gui.objects.search_and_filter.help.HelperHBoxRegexType;
import com.utils.gui.objects.search_and_filter.help.HelperHBoxRegexTypeRegular;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class HBoxRegexType extends AbstractCustomControl<HBox> {

	private static HelperHBoxRegexType helperHBoxRegexType =
			new HelperHBoxRegexTypeRegular();

	private final ToggleGroup toggleGroup;

	HBoxRegexType() {

		toggleGroup = new ToggleGroup();
	}

	@Override
	protected HBox createRoot() {

		final HBox hBoxRoot = LayoutControlsFactories.getInstance().createHBox();

		final Label labelPatternType =
				BasicControlsFactories.getInstance().createLabel("pattern type:", "bold");
		GuiUtils.addToHBox(hBoxRoot, labelPatternType,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 0);

		GuiUtils.addToHBox(hBoxRoot, new Region(),
				Pos.CENTER_LEFT, Priority.ALWAYS, 0, 0, 0, 0);

		final ToggleButton toggleButtonSimple =
				BasicControlsFactories.getInstance().createToggleButton("simple", null, toggleGroup);
		GuiUtils.addToHBox(hBoxRoot, toggleButtonSimple,
				Pos.CENTER, Priority.NEVER, 0, 0, 0, 7);

		final StackPaneHelp stackPaneHelpSimple = new StackPaneHelp(mouseEvent -> {
			if (GuiUtils.isLeftClick(mouseEvent)) {
				showHelpSimple();
			}
		});
		GuiUtils.addToHBox(hBoxRoot, stackPaneHelpSimple.getRoot(),
				Pos.CENTER_LEFT, Priority.NEVER, 0, 7, 0, 7);

		GuiUtils.addToHBox(hBoxRoot, new Region(),
				Pos.CENTER_LEFT, Priority.ALWAYS, 0, 0, 0, 0);

		final ToggleButton toggleButtonGlobRegex =
				BasicControlsFactories.getInstance().createToggleButton("glob regex", null, toggleGroup);
		GuiUtils.addToHBox(hBoxRoot, toggleButtonGlobRegex,
				Pos.CENTER, Priority.NEVER, 0, 0, 0, 7);

		final StackPaneHelp stackPaneHelpGlob = new StackPaneHelp(mouseEvent -> {
			if (GuiUtils.isLeftClick(mouseEvent)) {
				showHelpGlob();
			}
		});
		GuiUtils.addToHBox(hBoxRoot, stackPaneHelpGlob.getRoot(),
				Pos.CENTER_LEFT, Priority.NEVER, 0, 7, 0, 7);

		GuiUtils.addToHBox(hBoxRoot, new Region(),
				Pos.CENTER_LEFT, Priority.ALWAYS, 0, 0, 0, 0);

		final ToggleButton toggleButtonUnixRegex =
				BasicControlsFactories.getInstance().createToggleButton("unix regex", null, toggleGroup);
		GuiUtils.addToHBox(hBoxRoot, toggleButtonUnixRegex,
				Pos.CENTER, Priority.NEVER, 0, 0, 0, 7);

		final StackPaneHelp stackPaneHelpRegex = new StackPaneHelp(mouseEvent -> {
			if (GuiUtils.isLeftClick(mouseEvent)) {
				showHelpUnixRegex();
			}
		});
		GuiUtils.addToHBox(hBoxRoot, stackPaneHelpRegex.getRoot(),
				Pos.CENTER_LEFT, Priority.NEVER, 0, 7, 0, 7);

		GuiUtils.addToHBox(hBoxRoot, new Region(),
				Pos.CENTER_LEFT, Priority.ALWAYS, 0, 0, 0, 0);

		toggleButtonSimple.setSelected(true);

		return hBoxRoot;
	}

	private void showHelpSimple() {
		helperHBoxRegexType.showHelpSimple(getRoot().getScene());
	}

	private void showHelpGlob() {
		helperHBoxRegexType.showHelpGlob(getRoot().getScene());
	}

	private void showHelpUnixRegex() {
		helperHBoxRegexType.showHelpUnixRegex(getRoot().getScene());
	}

	int getPatternType() {

		int patternType = -1;
		final Toggle selectedToggle = toggleGroup.getSelectedToggle();
		if (selectedToggle != null) {
			patternType = toggleGroup.getToggles().indexOf(selectedToggle);
		}
		return patternType;
	}

	public static void setHelperHBoxRegexType(
			final HelperHBoxRegexType helperHBoxRegexType) {
		HBoxRegexType.helperHBoxRegexType = helperHBoxRegexType;
	}
}
