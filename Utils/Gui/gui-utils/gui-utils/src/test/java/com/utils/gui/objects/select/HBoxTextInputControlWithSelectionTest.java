package com.utils.gui.objects.select;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.utils.concurrency.ThreadUtils;
import com.utils.gui.AbstractCustomApplicationTest;
import com.utils.gui.GuiUtils;
import com.utils.gui.data.Dimensions;
import com.utils.gui.factories.LayoutControlsFactories;
import com.utils.gui.objects.select.data.TextInputControlWithSelectionItemForTesting;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

class HBoxTextInputControlWithSelectionTest extends AbstractCustomApplicationTest {

	@Test
	void testLayout() {

		final GridPane gridPane = LayoutControlsFactories.getInstance().createGridPane();

		final List<TextInputControlWithSelectionItemForTesting> textInputControlWithSelectionItemForTestingList =
				new ArrayList<>();
		textInputControlWithSelectionItemForTestingList.add(
				new TextInputControlWithSelectionItemForTesting("Name1", "Desc1"));
		textInputControlWithSelectionItemForTestingList.add(
				new TextInputControlWithSelectionItemForTesting("Name2", "Desc2"));
		textInputControlWithSelectionItemForTestingList.add(
				new TextInputControlWithSelectionItemForTesting("Name3", "Desc3"));

		final HBoxTextInputControlWithSelection hBoxTextInputControlWithSelection =
				new HBoxTextInputControlWithSelectionImpl<>("test items", new Dimensions(300, 500, -1, -1, 300, 500),
						TextInputControlWithSelectionItemForTesting.COLUMNS,
						textInputControlWithSelectionItemForTestingList, "Name2", false, null);
		GuiUtils.addToGridPane(gridPane, hBoxTextInputControlWithSelection.getRoot(), 0, 0, 1, 1,
				Pos.CENTER_LEFT, Priority.NEVER, Priority.NEVER, 7, 7, 7, 7);

		GuiUtils.run(() -> {

			final StackPane stackPaneContainer = getStackPaneContainer();
			stackPaneContainer.getChildren().add(gridPane);
		});
		ThreadUtils.trySleep(Long.MAX_VALUE);
	}
}
