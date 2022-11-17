package de.lumabit.liquidpdf.drawing;

import de.lumabit.liquidpdf.liquidelement.LiquidElement;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

public class PDrawer extends ElementDrawer {

    @Override
    public PDDocument drawElement(PDDocument pdDocument, PDPage pdPage, LiquidElement liquidElement) {
        return super.drawText(pdDocument, pdPage, liquidElement);
    }
}
