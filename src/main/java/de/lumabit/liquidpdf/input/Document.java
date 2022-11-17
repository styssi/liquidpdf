package de.lumabit.liquidpdf.input;

import lombok.Builder;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

@Builder
@Getter
public class Document {
    private List<Page> pages;

    // Optionale Attribute mit Default Values
    @Builder.Default
    private String language = "de-DE";
    @Builder.Default
    private String title = "UNTITLED";
    @Builder.Default
    private String producer = "Bundesagentur für Arbeit";


    public static class DocumentBuilder {
        private List<Page> pages = new LinkedList<>();

        public DocumentBuilder addPage(final Page page) {
            this.pages.add(page);
            return this;
        }
    }
}
