package de.lumabit.liquidpdf.drawer;

import de.lumabit.liquidpdf.model.LiquidDocument;
import de.lumabit.liquidpdf.model.LiquidElement;
import de.lumabit.liquidpdf.model.LiquidPage;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSInteger;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDStructureElement;
import org.apache.pdfbox.pdmodel.documentinterchange.taggedpdf.StandardStructureTypes;

public class PageDrawer {

    public PDDocument draw(PDDocument pdDocument, LiquidPage liquidPage) {
        pdDocument = addPage(pdDocument, liquidPage);
        addPdStructureElement(pdDocument, liquidPage.getLiquidDocument());
        for (LiquidElement liquidElement : liquidPage.getLiquidElements()) {
            new ElementDrawer().draw(pdDocument, pdDocument.getPage(pdDocument.getNumberOfPages() - 1), liquidElement);
        }
        return pdDocument;
    }

    private PDDocument addPage(PDDocument pdDocument, LiquidPage liquidPage) {
        PDPage pdPage = new PDPage();
        pdPage.setMediaBox(new PDRectangle(liquidPage.getWidth(), liquidPage.getHeight()));
        pdPage.getCOSObject().setItem(COSName.getPDFName("Tabs"), COSName.S);

        COSArray cosArray = new COSArray();
        cosArray.add(COSName.getPDFName("PDF"));
        cosArray.add(COSName.getPDFName("Text"));

        pdPage.setResources(new PDResources());
        pdPage.getResources().getCOSObject().setItem(COSName.PROC_SET, cosArray);
        pdPage.setRotation(0);
        // TODO: Nochmal prüfen ob das hier die richtige Nummer ist
        pdPage.getCOSObject().setItem(COSName.STRUCT_PARENTS, COSInteger.get(0));
        pdDocument.addPage(pdPage);
        return pdDocument;
    }

    private PDDocument addPdStructureElement(PDDocument pdDocument, LiquidDocument liquidDocument) {
        if (liquidDocument.getPdStructureElement() == null) {
            PDStructureElement pdStructureElement = new PDStructureElement(StandardStructureTypes.DOCUMENT, null);
            pdStructureElement.setTitle(liquidDocument.getTitle());
            pdStructureElement.setLanguage(liquidDocument.getLanguage());
            pdStructureElement.setPage(pdDocument.getPage(0));
            pdDocument.getDocumentCatalog().getStructureTreeRoot().appendKid(pdStructureElement);
            liquidDocument.setPdStructureElement(pdStructureElement);
        }
        return pdDocument;
    }
}
