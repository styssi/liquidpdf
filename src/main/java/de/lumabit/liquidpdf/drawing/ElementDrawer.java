package de.lumabit.liquidpdf.drawing;

import de.lumabit.liquidpdf.exception.LiquidPdfException;
import de.lumabit.liquidpdf.liquidelement.LiquidElement;
import de.lumabit.liquidpdf.setting.Font;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDStructureElement;
import org.apache.pdfbox.pdmodel.documentinterchange.markedcontent.PDMarkedContent;
import org.apache.pdfbox.pdmodel.documentinterchange.markedcontent.PDPropertyList;
import org.apache.pdfbox.pdmodel.documentinterchange.taggedpdf.StandardStructureTypes;

import java.io.IOException;

public class ElementDrawer {

    protected PDPageContentStream contentStream;


    public PDDocument draw(PDDocument pdDocument, PDPage pdPage, LiquidElement liquidElement) {

        // TODO: Testcode zum hinzufügen von Links
        drawText(pdDocument, pdPage, liquidElement);
        return pdDocument;
    }

    public PDDocument drawText(PDDocument pdDocument, PDPage pdPage, LiquidElement liquidElement) {
        try {
            // Neues Structure Element erstellen und dem Parent hinzufügen
            PDStructureElement parentStructureElement = liquidElement.getLiquidDocument().getPdStructureElement();
            PDStructureElement pdStructureElement = new PDStructureElement(StandardStructureTypes.P, parentStructureElement);
            pdStructureElement.setPage(pdPage);
            parentStructureElement.appendKid(pdStructureElement);

            // Text mit Markierung erstellen und dem gerade erstellen Structure Element als Kind anhängen
            COSDictionary markedContentDictionary = new COSDictionary();
            markedContentDictionary.setInt(COSName.MCID, liquidElement.getGlobalMCID());
            markedContentDictionary.setItem(COSName.PG, pdPage.getCOSObject());
            markedContentDictionary.setItem(COSName.P, parentStructureElement.getCOSObject());
            contentStream = new PDPageContentStream(pdDocument, pdPage, PDPageContentStream.AppendMode.APPEND, false);
            contentStream.beginMarkedContent(COSName.P, PDPropertyList.create(markedContentDictionary));
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 50);
            contentStream.setFont(EmbeddedFont.fonts.get(Font.ROBOTO_REGULAR), 10);
//            contentStream.setNonStrokingColor(getCurrentTextColor(textFragment));
            contentStream.showText(liquidElement.getText());
            contentStream.endText();
            contentStream.endMarkedContent();
            contentStream.close();
            pdStructureElement.appendKid(new PDMarkedContent(COSName.P, markedContentDictionary));

            // pdStructureElement wird hier gesetzt, damit es am Ende dem PDF ParentTree hinzugefügt werden kann
            liquidElement.setPdStructureElement(pdStructureElement);

            liquidElement.increaseGlobalMCID();
            return pdDocument;
        } catch (IOException e) {
            throw new LiquidPdfException("SOS", e);
        }
    }

    protected PDStructureElement addStructureElement(PDDocument pdDocument, PDPage pdPage, LiquidElement liquidElement) {
        PDStructureElement pdStructureElement = new PDStructureElement(StandardStructureTypes.P, liquidElement.getLiquidDocument().getPdStructureElement());
        pdStructureElement.setPage(pdPage);

//        if (image != null) {
//            PDLayoutAttributeObject layoutAttributeObject = new PDLayoutAttributeObject();
//            layoutAttributeObject.setBBox(new PDRectangle(getStartX(), getStartY() - image.getHeight(), image.getWidth(), image.getHeight()));
//            layoutAttributeObject.setPlacement(PDLayoutAttributeObject.PLACEMENT_BLOCK);
//            layoutAttributeObject.setWidth(image.getWidth());
//            layoutAttributeObject.setHeight(image.getHeight());
//            pdStructureElement.addAttribute(layoutAttributeObject);
//            pdStructureElement.setAlternateDescription(image.getAlternateDescription());
//        }
        liquidElement.getLiquidDocument().getPdStructureElement().appendKid(pdStructureElement);
        return pdStructureElement;
    }
}
