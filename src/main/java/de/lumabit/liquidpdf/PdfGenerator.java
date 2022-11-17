package de.lumabit.liquidpdf;

import de.lumabit.liquidpdf.drawer.DocumentDrawer;
import de.lumabit.liquidpdf.input.Document;
import de.lumabit.liquidpdf.input.mapper.DocumentMapper;
import de.lumabit.liquidpdf.input.validation.DocumentValidator;
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
