package de.lumabit.liquidpdf;

import de.lumabit.liquidpdf.drawing.DocumentDrawer;
import de.lumabit.liquidpdf.element.Document;
import de.lumabit.liquidpdf.liquidelement.liquidmapper.DocumentMapper;
import de.lumabit.liquidpdf.validation.DocumentValidator;
import lombok.Builder;

import java.io.InputStream;

@Builder
public class PdfGenerator {

    private Document document;

    public InputStream generate() {
        DocumentValidator.validate(document);

        // anreichern um Informationen wo die Elemente gezeichnet werden müssen
        // draw and tag elements
        DocumentDrawer documentDrawer = new DocumentDrawer();
        return documentDrawer.draw(DocumentMapper.mapDocument(document));
    }
}
