package com.utils.gui.objects.scroll_pane_zoom;

import java.util.ArrayList;
import java.util.List;

import com.utils.gui.AbstractCustomControl;
import com.utils.gui.GuiUtils;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class ScrollPaneZoom extends AbstractCustomControl<ScrollPane> {

	private static final double SCALE_DELTA = 1.1;

	private final Region zoomTarget;
	private final boolean zoomX;
	private final boolean zoomY;
	private final boolean zoomOnScroll;
	private final boolean mouseDrag;
	private final List<ScrollPaneZoom> linkedScrollPaneZoomList = new ArrayList<>();

	private ScrollPane scrollPane;
	private Group group;

	public ScrollPaneZoom(
			final Region zoomTarget,
			final boolean zoomX,
			final boolean zoomY,
			final boolean zoomOnScroll,
			final boolean mouseDrag) {

		this.zoomTarget = zoomTarget;
		this.zoomX = zoomX;
		this.zoomY = zoomY;
		this.zoomOnScroll = zoomOnScroll;
		this.mouseDrag = mouseDrag;
	}

	@Override
	protected ScrollPane createRoot() {

		scrollPane = new ScrollPane();

		group = new Group(zoomTarget);

		final StackPane content = new StackPane(group);
		group.layoutBoundsProperty().addListener((
				observable,
				oldBounds,
				newBounds) -> {
			content.setMinWidth(newBounds.getWidth());
			content.setMinHeight(newBounds.getHeight());
		});

		scrollPane.setContent(content);
		scrollPane.viewportBoundsProperty().addListener((
				observable,
				oldBounds,
				newBounds) -> {
			final double width = newBounds.getWidth();
			final double height = newBounds.getHeight();
			content.setPrefSize(width, height);
		});

		if (zoomOnScroll) {
			content.setOnScroll(scrollEvent -> {
				scrollEvent.consume();
				final double deltaY = scrollEvent.getDeltaY();
				final Point2D mousePosition = new Point2D(scrollEvent.getX(), scrollEvent.getY());
				zoom(deltaY, mousePosition);
				for (final ScrollPaneZoom linkedScrollPaneZoom : linkedScrollPaneZoomList) {
					linkedScrollPaneZoom.zoom(deltaY, mousePosition);
				}
			});
		}

		final ObjectProperty<Point2D> lastMouseCoordinates = new SimpleObjectProperty<>();
		content.setOnMousePressed(mouseEvent -> {

			lastMouseCoordinates.set(new Point2D(mouseEvent.getX(), mouseEvent.getY()));
			if (GuiUtils.isDoubleClick(mouseEvent)) {

				resetZoom();
				for (final ScrollPaneZoom linkedScrollPaneZoom : linkedScrollPaneZoomList) {
					linkedScrollPaneZoom.resetZoom();
				}
			}
		});

		if (mouseDrag) {
			content.setOnMouseDragged(event -> {

				final double deltaX = event.getX() - lastMouseCoordinates.get().getX();
				final double deltaY = event.getY() - lastMouseCoordinates.get().getY();
				mouseDragged(deltaX, deltaY);
				for (final ScrollPaneZoom linkedScrollPaneZoom : linkedScrollPaneZoomList) {
					linkedScrollPaneZoom.mouseDragged(deltaX, deltaY);
				}
			});
		}

		return scrollPane;
	}

	private void mouseDragged(
			final double deltaX,
			final double deltaY) {

		final ScrollPane scrollPane = getRoot();
		final Node scrollPaneContent = scrollPane.getContent();

		final double extraWidth = scrollPaneContent.getLayoutBounds().getWidth() -
				scrollPane.getViewportBounds().getWidth();
		final double deltaH = deltaX * (scrollPane.getHmax() - scrollPane.getHmin()) / extraWidth;
		final double desiredH = scrollPane.getHvalue() - deltaH;
		scrollPane.setHvalue(Math.max(0, Math.min(scrollPane.getHmax(), desiredH)));

		final double extraHeight = scrollPaneContent.getLayoutBounds().getHeight() -
				scrollPane.getViewportBounds().getHeight();
		final double deltaV = deltaY * (scrollPane.getHmax() - scrollPane.getHmin()) / extraHeight;
		final double desiredV = scrollPane.getVvalue() - deltaV;
		scrollPane.setVvalue(Math.max(0, Math.min(scrollPane.getVmax(), desiredV)));
	}

	private void zoom(
			final double delta,
			final Point2D mousePosition) {

		if (delta != 0) {

			final double scaleFactor;
			if (delta > 0) {
				scaleFactor = SCALE_DELTA;
			} else {
				scaleFactor = 1 / SCALE_DELTA;
			}

			final Bounds groupBounds = group.getLayoutBounds();
			final Bounds viewportBounds = scrollPane.getViewportBounds();

			final double valX = scrollPane.getHvalue() *
					(groupBounds.getWidth() - viewportBounds.getWidth());
			final double valY = scrollPane.getVvalue() *
					(groupBounds.getHeight() - viewportBounds.getHeight());

			final Point2D posInZoomTarget = zoomTarget.parentToLocal(group.parentToLocal(mousePosition));
			final Point2D scrollPositionAdjustment = zoomTarget.getLocalToParentTransform()
					.deltaTransform(posInZoomTarget.multiply(scaleFactor - 1));

			if (zoomX) {
				zoomTarget.setScaleX(zoomTarget.getScaleX() * scaleFactor);
			}
			if (zoomY) {
				zoomTarget.setScaleY(zoomTarget.getScaleY() * scaleFactor);
			}

			scrollPane.layout();

			final Bounds newGroupBounds = group.getLayoutBounds();
			if (zoomX) {
				scrollPane.setHvalue((valX + scrollPositionAdjustment.getX()) /
						(newGroupBounds.getWidth() - viewportBounds.getWidth()));
			}
			if (zoomY) {
				scrollPane.setVvalue((valY + scrollPositionAdjustment.getY()) /
						(newGroupBounds.getHeight() - viewportBounds.getHeight()));
			}
		}
	}

	private void resetZoom() {

		zoomTarget.setScaleX(1);
		zoomTarget.setScaleY(1);
	}

	public List<ScrollPaneZoom> getLinkedScrollPaneZoomList() {
		return linkedScrollPaneZoomList;
	}
}
