package com.utils.gui.objects.list_select;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.utils.concurrency.ThreadUtils;
import com.utils.gui.AbstractCustomApplicationTest;
import com.utils.gui.GuiUtils;

import javafx.scene.layout.StackPane;

class CustomListSelectionViewTestImplTest extends AbstractCustomApplicationTest {

	@Test
	void testLayout() {

		final List<Text> leftTableRowDataList = new ArrayList<>();
		leftTableRowDataList.add(new Text("abc"));
		leftTableRowDataList.add(new Text("bcd_e"));
		leftTableRowDataList.add(new Text("abc_bcd"));
		leftTableRowDataList.add(new Text("bca_dbc_edc"));

		final List<TextWithLength> rightTableRowDataList = new ArrayList<>();
		rightTableRowDataList.add(new TextWithLength("bcd"));

		final AbstractCustomListSelectionView<Text, TextWithLength> customListSelectionView =
				new CustomListSelectionViewTestImpl(leftTableRowDataList, rightTableRowDataList);

		GuiUtils.run(() -> {

			final StackPane stackPaneContainer = getStackPaneContainer();
			stackPaneContainer.getChildren().add(customListSelectionView.getRoot());
		});
		ThreadUtils.trySleep(Long.MAX_VALUE);
	}
}
