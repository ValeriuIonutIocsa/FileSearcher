package com.utils.gui.objects.patterns;

import org.apache.commons.lang3.StringUtils;

import com.utils.gui.AbstractCustomControl;
import com.utils.gui.GuiUtils;
import com.utils.gui.factories.BasicControlsFactories;
import com.utils.gui.factories.LayoutControlsFactories;
import com.utils.string.regex.FactoryPatterns;
import com.utils.string.regex.PatternWithCase;
import com.utils.string.regex.Patterns;

import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class GridPaneEditPatterns extends AbstractCustomControl<GridPane> {

	private final String displayName;

	private final TextField inclusionPatternTextField;
	private final CheckBox inclusionPatternCaseSensitiveCheckBox;
	private final TextField exclusionPatternTextField;
	private final CheckBox exclusionPatternCaseSensitiveCheckBox;

	public GridPaneEditPatterns(
			final String displayName) {

		this.displayName = displayName;

		inclusionPatternTextField = BasicControlsFactories.getInstance().createTextField("");
		inclusionPatternCaseSensitiveCheckBox = BasicControlsFactories.getInstance().createCheckBox("");
		exclusionPatternTextField = BasicControlsFactories.getInstance().createTextField("");
		exclusionPatternCaseSensitiveCheckBox = BasicControlsFactories.getInstance().createCheckBox("");
	}

	@Override
	protected GridPane createRoot() {

		final GridPane rootGridPane = LayoutControlsFactories.getInstance().createGridPane();
		int row = 0;

		final Label inclusionPatternLabel = BasicControlsFactories.getInstance()
				.createLabel(displayName + " inclusion pattern:", "bold");
		GuiUtils.addToGridPane(rootGridPane, inclusionPatternLabel, 0, row, 1, 1,
				Pos.CENTER_LEFT, Priority.NEVER, Priority.NEVER, 7, 0, 0, 7);

		GuiUtils.addToGridPane(rootGridPane, inclusionPatternTextField, 1, row, 1, 1,
				Pos.CENTER_LEFT, Priority.NEVER, Priority.ALWAYS, 7, 0, 0, 7);

		final Label inclusionPatternCaseSensitiveLabel = BasicControlsFactories.getInstance()
				.createLabel("case sensitive:", "bold");
		GuiUtils.addToGridPane(rootGridPane, inclusionPatternCaseSensitiveLabel, 2, row, 1, 1,
				Pos.CENTER_LEFT, Priority.NEVER, Priority.NEVER, 7, 0, 0, 7);

		GuiUtils.addToGridPane(rootGridPane, inclusionPatternCaseSensitiveCheckBox, 3, row, 1, 1,
				Pos.CENTER_LEFT, Priority.NEVER, Priority.NEVER, 7, 7, 0, 7);
		row++;

		final Label exclusionPatternLabel = BasicControlsFactories.getInstance()
				.createLabel(displayName + " exclusion pattern:", "bold");
		GuiUtils.addToGridPane(rootGridPane, exclusionPatternLabel, 0, row, 1, 1,
				Pos.CENTER_LEFT, Priority.NEVER, Priority.NEVER, 7, 0, 7, 7);

		GuiUtils.addToGridPane(rootGridPane, exclusionPatternTextField, 1, row, 1, 1,
				Pos.CENTER_LEFT, Priority.NEVER, Priority.ALWAYS, 7, 0, 7, 7);

		final Label exclusionPatternCaseSensitiveLabel = BasicControlsFactories.getInstance()
				.createLabel("case sensitive:", "bold");
		GuiUtils.addToGridPane(rootGridPane, exclusionPatternCaseSensitiveLabel, 2, row, 1, 1,
				Pos.CENTER_LEFT, Priority.NEVER, Priority.NEVER, 7, 0, 7, 7);

		GuiUtils.addToGridPane(rootGridPane, exclusionPatternCaseSensitiveCheckBox, 3, row, 1, 1,
				Pos.CENTER_LEFT, Priority.NEVER, Priority.NEVER, 7, 7, 7, 7);

		return rootGridPane;
	}

	public void resetAll(
			final Patterns patterns) {

		String inclusionPatternString = "";
		boolean inclusionPatternCaseSensitive = false;
		String exclusionPatternString = "";
		boolean exclusionPatternCaseSensitive = false;
		if (patterns != null) {

			final PatternWithCase inclusionPattern = patterns.getInclusionPattern();
			if (inclusionPattern != null) {

				inclusionPatternString = inclusionPattern.getPatternString();
				inclusionPatternCaseSensitive = inclusionPattern.isCaseSensitive();
			}

			final PatternWithCase exclusionPattern = patterns.getExclusionPattern();
			if (exclusionPattern != null) {

				exclusionPatternString = exclusionPattern.getPatternString();
				exclusionPatternCaseSensitive = exclusionPattern.isCaseSensitive();
			}
		}

		inclusionPatternTextField.setText(inclusionPatternString);
		inclusionPatternCaseSensitiveCheckBox.setSelected(inclusionPatternCaseSensitive);
		exclusionPatternTextField.setText(exclusionPatternString);
		exclusionPatternCaseSensitiveCheckBox.setSelected(exclusionPatternCaseSensitive);
	}

	public Patterns saveAll() {

		Patterns patterns = null;

		final String inclusionPatternString = inclusionPatternTextField.getText();
		final boolean inclusionPatternCaseSensitive = inclusionPatternCaseSensitiveCheckBox.isSelected();
		final String exclusionPatternString = exclusionPatternTextField.getText();
		final boolean exclusionPatternCaseSensitive = exclusionPatternCaseSensitiveCheckBox.isSelected();

		if (StringUtils.isNotBlank(inclusionPatternString)) {

			patterns = FactoryPatterns.newInstance(inclusionPatternString, inclusionPatternCaseSensitive,
					exclusionPatternString, exclusionPatternCaseSensitive);
		}

		return patterns;
	}
}
