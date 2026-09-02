package com.utils.html;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.utils.annotations.ApiMethod;
import com.utils.string.converters.ConverterByteArray;

public final class HtmlUtils {

	private HtmlUtils() {
	}

	@ApiMethod
	public static Element embedImage(
			final Element element,
			final byte[] imageFileBytes) {

		final Document document = element.getOwnerDocument();
		final Element imgElement = document.createElement("img");

		final String src = createImgSrc(imageFileBytes);
		imgElement.setAttribute("src", src);

		element.appendChild(imgElement);
		return imgElement;
	}

	@ApiMethod
	public static String createImgSrc(
			final byte[] imageFileBytes) {

		final String encodedImage = ConverterByteArray.byteArrayToString(imageFileBytes);
		return "data:image/png;base64," + encodedImage;
	}

	@ApiMethod
	public static String javaFxColorToHtmlColor(
			final String colorString) {
		return "#" + colorString.substring(2, 8);
	}
}
