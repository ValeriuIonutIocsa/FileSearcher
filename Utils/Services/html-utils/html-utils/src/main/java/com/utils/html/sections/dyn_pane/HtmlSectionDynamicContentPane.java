package com.utils.html.sections.dyn_pane;

import java.util.ArrayList;
import java.util.List;

import com.utils.html.sections.HtmlSection;
import com.utils.html.sections.HtmlSectionText;
import com.utils.html.sections.containers.AbstractHtmlSectionContainer;
import com.utils.html.sections.parents.HtmlSectionButton;
import com.utils.html.sections.parents.HtmlSectionDiv;
import com.utils.html.sections.parents.HtmlSectionSection;
import com.utils.log.Logger;

public class HtmlSectionDynamicContentPane extends AbstractHtmlSectionContainer {

	private final String paneId;

	private final List<DynamicContentPaneSection> dynamicContentPaneSectionList;

	public HtmlSectionDynamicContentPane(
			final String paneId) {

		this.paneId = paneId;

		dynamicContentPaneSectionList = new ArrayList<>();
	}

	@Override
	protected void fillHtmlSectionList(
			final List<HtmlSection> htmlSectionList) {

		final HtmlSectionDiv containerHtmlSectionDiv = new HtmlSectionDiv();
		containerHtmlSectionDiv.addAttributeClass("dynamicContentContainer");

		checkDynamicContentPaneSections();

		final HtmlSectionDiv sidebarHtmlSectionDiv = createSidebarHtmlSectionDiv();
		containerHtmlSectionDiv.addHtmlSection(sidebarHtmlSectionDiv);

		final HtmlSectionDiv paneHtmlSectionDiv = createPaneHtmlSectionDiv();
		containerHtmlSectionDiv.addHtmlSection(paneHtmlSectionDiv);

		htmlSectionList.add(containerHtmlSectionDiv);
	}

	private void checkDynamicContentPaneSections() {

		if (!dynamicContentPaneSectionList.isEmpty()) {

			int activeCount = 0;
			for (final DynamicContentPaneSection dynamicContentPaneSection : dynamicContentPaneSectionList) {

				final boolean active = dynamicContentPaneSection.isActive();
				if (active) {
					activeCount++;
				}
			}
			if (activeCount == 0) {

				final DynamicContentPaneSection firstDynamicContentPaneSection =
						dynamicContentPaneSectionList.getFirst();
				firstDynamicContentPaneSection.setActive(true);

			} else if (activeCount >= 2) {

				Logger.printError("dynamic content pane " + paneId + " has multiple active sections");
				for (final DynamicContentPaneSection dynamicContentPaneSection : dynamicContentPaneSectionList) {
					dynamicContentPaneSection.setActive(false);
				}
				final DynamicContentPaneSection firstDynamicContentPaneSection =
						dynamicContentPaneSectionList.getFirst();
				firstDynamicContentPaneSection.setActive(true);
			}
		}
	}

	private HtmlSectionDiv createSidebarHtmlSectionDiv() {

		final HtmlSectionDiv siderbarHtmlSectionDiv = new HtmlSectionDiv();
		siderbarHtmlSectionDiv.addAttributeClass("dynamicContentSidebar");

		for (int i = 0; i < dynamicContentPaneSectionList.size(); i++) {

			final DynamicContentPaneSection dynamicContentPaneSection = dynamicContentPaneSectionList.get(i);

			final HtmlSectionButton htmlSectionButton =
					createHtmlSectionButton(i, dynamicContentPaneSection);
			siderbarHtmlSectionDiv.addHtmlSection(htmlSectionButton);
		}

		return siderbarHtmlSectionDiv;
	}

	private HtmlSectionButton createHtmlSectionButton(
			final int sectionIndex,
			final DynamicContentPaneSection dynamicContentPaneSection) {

		final HtmlSectionButton htmlSectionButton = new HtmlSectionButton();

		final String buttonClass = createButtonClass();
		String buttonClassAttribute = "dynamicContentPaneButton " + buttonClass;
		final boolean active = dynamicContentPaneSection.isActive();
		if (active) {
			buttonClassAttribute += " active";
		}
		htmlSectionButton.addAttributeClass(buttonClassAttribute);

		final String buttonId = createButtonId(sectionIndex);
		htmlSectionButton.addAttributeId(buttonId);

		final String sectionClass = createSectionClass();
		final String sectionId = createSectionId(sectionIndex);

		final String onClick = "showSection('." + buttonClass + "', '." + sectionClass + "', " +
				"'" + buttonId + "', '" + sectionId + "')";
		htmlSectionButton.addAttributeOnClick(onClick);

		final String title = dynamicContentPaneSection.getTitle();
		htmlSectionButton.addHtmlSection(new HtmlSectionText(title));
		return htmlSectionButton;
	}

	private HtmlSectionDiv createPaneHtmlSectionDiv() {

		final HtmlSectionDiv paneHtmlSectionDiv = new HtmlSectionDiv();
		paneHtmlSectionDiv.addAttributeClass("dynamicContentPane");

		for (int i = 0; i < dynamicContentPaneSectionList.size(); i++) {

			final DynamicContentPaneSection dynamicContentPaneSection = dynamicContentPaneSectionList.get(i);

			final HtmlSectionSection htmlSectionSection =
					createHtmlSectionSection(i, dynamicContentPaneSection);
			paneHtmlSectionDiv.addHtmlSection(htmlSectionSection);
		}

		return paneHtmlSectionDiv;
	}

	private HtmlSectionSection createHtmlSectionSection(
			final int sectionIndex,
			final DynamicContentPaneSection dynamicContentPaneSection) {

		final HtmlSectionSection htmlSectionSection = new HtmlSectionSection();

		String sectionClassAttribute = "dynamicContentPaneSection " + createSectionClass();
		final boolean active = dynamicContentPaneSection.isActive();
		if (active) {
			sectionClassAttribute += " active";
		}
		htmlSectionSection.addAttributeClass(sectionClassAttribute);

		final String sectionId = createSectionId(sectionIndex);
		htmlSectionSection.addAttributeId(sectionId);

		final HtmlSection htmlSection = dynamicContentPaneSection.getHtmlSection();
		htmlSectionSection.addHtmlSection(htmlSection);

		return htmlSectionSection;
	}

	private String createButtonClass() {

		return "dynamicContentPane" + paneId + "Button";
	}

	private String createButtonId(
			final int sectionIndex) {

		final String buttonClass = createButtonClass();
		return buttonClass + sectionIndex;
	}

	private String createSectionClass() {

		return "dynamicContentPane" + paneId + "Section";
	}

	private String createSectionId(
			final int sectionIndex) {

		final String sectionClass = createSectionClass();
		return sectionClass + sectionIndex;
	}

	public void addDynamicContentSection(
			final boolean active,
			final String title,
			final HtmlSection htmlSection) {

		final DynamicContentPaneSection dynamicContentPaneSection =
				new DynamicContentPaneSection(active, title, htmlSection);
		dynamicContentPaneSectionList.add(dynamicContentPaneSection);
	}
}
