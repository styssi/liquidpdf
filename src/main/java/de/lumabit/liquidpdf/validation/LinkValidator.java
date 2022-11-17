package de.lumabit.liquidpdf.validation;

import de.lumabit.liquidpdf.element.Element;
import de.lumabit.liquidpdf.element.Link;
import de.lumabit.liquidpdf.exception.LiquidPdfException;

public class LinkValidator {
    public void validate(Element element) {
        Link link = (Link) element;
        if (link.getHref() == null || link.getHref().isEmpty()) {
            throw new LiquidPdfException("Dem 'Link' muss ein 'href' hinzugefügt werden.");
        }
    }
}
