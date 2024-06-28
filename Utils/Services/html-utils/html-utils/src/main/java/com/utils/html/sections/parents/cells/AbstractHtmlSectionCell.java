package com.utils.html.sections.parents.cells;

import com.utils.html.sections.parents.AbstractHtmlSectionParent;
import com.utils.xml.stax.XmlStAXWriter;

public abstract class AbstractHtmlSectionCell extends AbstractHtmlSectionParent {

	private int colSpan;
	private int rowSpan;

	AbstractHtmlSectionCell(
			final String tagName) {

		super(tagName);
	}

	@Override
	protected void writeAttributes(
			final XmlStAXWriter xmlStAXWriter) {

		super.writeAttributes(xmlStAXWriter);

		if (colSpan > 1) {
			xmlStAXWriter.writeAttribute("colspan", String.valueOf(colSpan));
		}
		if (rowSpan > 1) {
			xmlStAXWriter.writeAttribute("rowspan", String.valueOf(rowSpan));
		}
	}

	public AbstractHtmlSectionCell addColSpan(
			final int colSpan) {

		this.colSpan = colSpan;
		return this;
	}

	public AbstractHtmlSectionCell addRowSpan(
			final int rowSpan) {

		this.rowSpan = rowSpan;
		return this;
	}
}
