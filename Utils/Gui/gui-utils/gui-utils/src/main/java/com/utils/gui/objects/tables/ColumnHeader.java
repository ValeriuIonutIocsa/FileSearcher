package com.utils.gui.objects.tables;

import com.utils.gui.GuiUtils;
import com.utils.gui.factories.LayoutControlsFactories;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumnBase;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class ColumnHeader {

	private final Label labelColumnHeader;

	public ColumnHeader(
			final TableColumnBase<?, ?> tableColumn) {

		final StackPane stackPaneColumnHeader = LayoutControlsFactories.getInstance().createStackPane(Pos.CENTER);
		stackPaneColumnHeader.prefWidthProperty().bind(tableColumn.widthProperty());

		labelColumnHeader = createLabelColumnHeader(tableColumn);
		labelColumnHeader.prefWidthProperty().bind(stackPaneColumnHeader.prefWidthProperty());
		GuiUtils.addToStackPane(stackPaneColumnHeader, labelColumnHeader, Pos.CENTER, 0, 0, 0, 0);

		tableColumn.setGraphic(stackPaneColumnHeader);
	}

	private static Label createLabelColumnHeader(
			final TableColumnBase<?, ?> tableColumn) {

		final String columnName = tableColumn.getText();
		final Label labelColumnHeader = new Label(columnName);
		labelColumnHeader.setWrapText(true);
		labelColumnHeader.setPadding(new Insets(1));
		labelColumnHeader.setTextAlignment(TextAlignment.CENTER);
		return labelColumnHeader;
	}

	public void setFilterGraphic() {
		labelColumnHeader.setBorder(new Border(new BorderStroke(
				Color.DARKORANGE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
				new BorderWidths(0, 0, 2, 0), new Insets(0))));
	}

	public void removeFilterGraphic() {
		labelColumnHeader.setBorder(null);
	}

	public String getColumnName() {
		return labelColumnHeader.getText();
	}
}
