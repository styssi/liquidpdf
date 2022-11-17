package de.lumabit.liquidpdf.drawer;

import de.lumabit.liquidpdf.model.LiquidElement;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

public class PDrawer extends ElementDrawer {

    @Override
    public <T extends LiquidElement> PDDocument drawElement(PDDocument pdDocument, PDPage pdPage, T liquidElement) {
        return super.drawText(pdDocument, pdPage, liquidElement);
    }
}
