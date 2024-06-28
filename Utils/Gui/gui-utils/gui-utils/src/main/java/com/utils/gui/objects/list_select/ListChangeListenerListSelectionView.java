package com.utils.gui.objects.list_select;

import javafx.collections.ListChangeListener;
import javafx.scene.control.TablePosition;

class ListChangeListenerListSelectionView<
		TablePositionT extends TablePosition<?, ?>>
		implements ListChangeListener<TablePositionT> {

	private final AbstractCustomListSelectionView<?, ?> customListSelectionView;

	ListChangeListenerListSelectionView(
			final AbstractCustomListSelectionView<?, ?> customListSelectionView) {

		this.customListSelectionView = customListSelectionView;
	}

	@Override
	public void onChanged(
			final Change<? extends TablePositionT> change) {
		customListSelectionView.updateButtons();
	}
}
