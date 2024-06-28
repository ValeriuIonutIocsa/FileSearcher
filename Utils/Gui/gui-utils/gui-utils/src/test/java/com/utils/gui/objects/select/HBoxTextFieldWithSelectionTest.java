package com.utils.gui.objects.select;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.utils.concurrency.ThreadUtils;
import com.utils.gui.AbstractCustomApplicationTest;
import com.utils.gui.GuiUtils;
import com.utils.gui.data.Dimensions;
import com.utils.gui.factories.LayoutControlsFactories;
import com.utils.gui.objects.select.data.TextFieldWithSelectionItemForTesting;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

class HBoxTextFieldWithSelectionTest extends AbstractCustomApplicationTest {

	@Test
	void testLayout() {

		final GridPane gridPane = LayoutControlsFactories.getInstance().createGridPane();

		final List<TextFieldWithSelectionItemForTesting> textFieldWithSelectionItemForTestingList =
				new ArrayList<>();
		textFieldWithSelectionItemForTestingList.add(new TextFieldWithSelectionItemForTesting("Name1", "Desc1"));
		textFieldWithSelectionItemForTestingList.add(new TextFieldWithSelectionItemForTesting("Name2", "Desc2"));
		textFieldWithSelectionItemForTestingList.add(new TextFieldWithSelectionItemForTesting("Name3", "Desc3"));

		final HBoxTextFieldWithSelection hBoxTextFieldWithSelection =
				new HBoxTextFieldWithSelectionImpl<>("test items", new Dimensions(300, 500, -1, -1, 300, 500),
						TextFieldWithSelectionItemForTesting.COLUMNS, textFieldWithSelectionItemForTestingList,
						"Name2");
		GuiUtils.addToGridPane(gridPane, hBoxTextFieldWithSelection.getRoot(), 0, 0, 1, 1,
				Pos.CENTER_LEFT, Priority.NEVER, Priority.NEVER, 7, 7, 7, 7);

		GuiUtils.run(() -> {

			final StackPane stackPaneContainer = getStackPaneContainer();
			stackPaneContainer.getChildren().add(gridPane);
		});
		ThreadUtils.trySleep(Long.MAX_VALUE);
	}
}
