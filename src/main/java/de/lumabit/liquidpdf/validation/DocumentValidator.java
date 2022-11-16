package de.lumabit.liquidpdf.validation;

import de.lumabit.liquidpdf.element.Document;
import de.lumabit.liquidpdf.exception.LiquidPdfException;

public class DocumentValidator {
    /**
     * is the document structure build up in a valid way
     * @param document
     */
    public static void validate(Document document) {
        validateDocument(document);
        PageValidator.validate(document.getPages());
    }

    private static void validateDocument(Document document) {
        validateDocumentExists(document);
        validateDocumentHasPages(document);
    }

    private static void validateDocumentExists(Document document) {
        if (document == null) {
            throw new LiquidPdfException("Dem 'PdfGenerator' wurde kein 'Docment' hinzugefügt.");
        }
    }

    private static void validateDocumentHasPages(Document document) {
        if (document.getPages() == null || document.getPages().isEmpty()) {
            throw new LiquidPdfException("Dem 'Document' wurden keine 'Pages' hinzugefügt.");
        }
    }


}
