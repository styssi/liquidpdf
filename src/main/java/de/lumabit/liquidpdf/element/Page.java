package de.lumabit.liquidpdf.element;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import java.util.LinkedList;
import java.util.List;

@Builder
@Getter
@Setter(AccessLevel.PRIVATE)
public class Page {
    List<Element> elements;

    // Optionale Attribute mit Default Values
    @Builder.Default
    private float width = PDRectangle.A4.getWidth();
    @Builder.Default
    private float height = PDRectangle.A4.getHeight();

    public static class PageBuilder {
        List<Element> elements = new LinkedList<>();

        public <T extends Element> PageBuilder addElement(final T element) {
            this.elements.add(element);
            return this;
        }

        public <T extends Element> PageBuilder addElements(final List<T> elements) {
            this.elements.addAll(elements);
            return this;
        }
    }
}