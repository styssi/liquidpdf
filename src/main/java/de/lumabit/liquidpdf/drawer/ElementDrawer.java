package de.lumabit.liquidpdf.drawer;

import de.lumabit.liquidpdf.annotation.LiquidDrawerReference;
import de.lumabit.liquidpdf.exception.LiquidPdfException;
import de.lumabit.liquidpdf.model.LiquidElement;
import de.lumabit.liquidpdf.model.LiquidLink;
import de.lumabit.liquidpdf.input.Font;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDAttributeObject;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDObjectReference;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDStructureElement;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.Revisions;
import org.apache.pdfbox.pdmodel.documentinterchange.markedcontent.PDMarkedContent;
import org.apache.pdfbox.pdmodel.documentinterchange.markedcontent.PDPropertyList;
import org.apache.pdfbox.pdmodel.documentinterchange.taggedpdf.PDLayoutAttributeObject;
import org.apache.pdfbox.pdmodel.documentinterchange.taggedpdf.StandardStructureTypes;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionURI;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDBorderStyleDictionary;

import java.awt.*;
import java.io.IOException;

public class ElementDrawer {

    protected PDPageContentStream contentStream;


    public <T extends LiquidElement> PDDocument draw(PDDocument pdDocument, PDPage pdPage, T liquidElement) {
        try {
            LiquidDrawerReference elementDrawerClazz = liquidElement.getClass().getAnnotation(LiquidDrawerReference.class);
            ElementDrawer elementDrawer = (ElementDrawer) elementDrawerClazz.drawer().getDeclaredConstructor().newInstance();
            elementDrawer.drawElement(pdDocument, pdPage, liquidElement);
        } catch (Exception e) {

        }
        return pdDocument;
    }

    protected <T extends LiquidElement> PDDocument drawElement(PDDocument pdDocument, PDPage pdPage, T liquidElement) {
        throw new LiquidPdfException("Die Funktion drawElement wurde nicht implementiert");
    }

    public <T extends LiquidElement> PDDocument drawLink(PDDocument pdDocument, PDPage pdPage, T liquidElement) {
        try {
            LiquidLink liquidLinkElement = (LiquidLink) liquidElement;

            // Neues Structure Element erstellen und dem Parent hinzuf??gen
            PDStructureElement parentStructureElement = liquidLinkElement.getLiquidDocument().getPdStructureElement();
            PDStructureElement pdStructureElement = new PDStructureElement(StandardStructureTypes.LINK, parentStructureElement);
            pdStructureElement.setPage(pdPage);
            parentStructureElement.appendKid(pdStructureElement);

            // Text mit Markierung erstellen und dem gerade erstellen Structure Element als Kind anh??ngen
            COSDictionary markedContentDictionary = new COSDictionary();
            markedContentDictionary.setInt(COSName.MCID, liquidLinkElement.getGlobalMCID());
            markedContentDictionary.setItem(COSName.PG, pdPage.getCOSObject());
            markedContentDictionary.setItem(COSName.P, parentStructureElement.getCOSObject());
            contentStream = new PDPageContentStream(pdDocument, pdPage, PDPageContentStream.AppendMode.APPEND, false);
            contentStream.beginMarkedContent(COSName.P, PDPropertyList.create(markedContentDictionary));
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 50);
            contentStream.setFont(EmbeddedFont.fonts.get(Font.ROBOTO_REGULAR), 10);
//            contentStream.setNonStrokingColor(getCurrentTextColor(textFragment));
            contentStream.showText(liquidLinkElement.getText());
            contentStream.endText();
            contentStream.endMarkedContent();
            contentStream.close();
            pdStructureElement.appendKid(new PDMarkedContent(COSName.P, markedContentDictionary));

            // pdStructureElement wird hier gesetzt, damit es am Ende dem PDF ParentTree hinzugef??gt werden kann
            liquidLinkElement.setPdStructureElement(pdStructureElement);

            liquidLinkElement.increaseGlobalMCID();


            PDAnnotationLink pdAnnotationLink = createPdAnnotationLink(pdPage, liquidLinkElement, 50, 50, 400, 100, liquidLinkElement.getHref());
            pdPage.getAnnotations().add(pdAnnotationLink);

            PDObjectReference pdObjectReference = new PDObjectReference();
            pdObjectReference.setReferencedObject(pdAnnotationLink);

            liquidLinkElement.setPdObjectReference(pdObjectReference);
            pdStructureElement.appendKid(pdObjectReference);

            PDLayoutAttributeObject pdLayoutAttributeObject = new PDLayoutAttributeObject();
            pdLayoutAttributeObject.setPlacement(PDLayoutAttributeObject.PLACEMENT_BLOCK);


            Revisions<PDAttributeObject> revisions = new Revisions();
            revisions.addObject(pdLayoutAttributeObject, 0);

            pdStructureElement.setAttributes(revisions);

            return pdDocument;
        } catch (IOException e) {
            throw new LiquidPdfException("SOS", e);
        }
    }

    private PDAnnotationLink createPdAnnotationLink(PDPage pdPage, LiquidLink liquidLink, float startX, float startY, int textWidth, int lineHeight, String uri) throws IOException {
        PDRectangle rect = new PDRectangle();
        rect.setLowerLeftX(startX);
        rect.setLowerLeftY(startY - 1);
        rect.setUpperRightX(startX + textWidth);
        rect.setUpperRightY(startY + lineHeight + 2);


        PDAnnotationLink pdAnnotationLink = new PDAnnotationLink();
        // Border color
        final Color color = Color.black;
        final float[] components = new float[]{color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f};
        pdAnnotationLink.setColor(new PDColor(components, PDDeviceRGB.INSTANCE));

        // border style
        final PDBorderStyleDictionary linkBorder = new PDBorderStyleDictionary();
        linkBorder.setStyle(PDBorderStyleDictionary.STYLE_UNDERLINE);
        linkBorder.setWidth(1);
        pdAnnotationLink.setBorderStyle(linkBorder);

        PDActionURI action = new PDActionURI();
        action.setURI(liquidLink.getHref());
        pdAnnotationLink.setAction(action);
        pdAnnotationLink.setRectangle(rect);
        pdAnnotationLink.setPage(pdPage);

        pdAnnotationLink.setHidden(false);
        pdAnnotationLink.setInvisible(false);
        pdAnnotationLink.setNoView(false);
        pdAnnotationLink.setPrinted(true);
        pdAnnotationLink.setContents("Link 2");

        pdAnnotationLink.setStructParent(1);

        return pdAnnotationLink;
    }

    public <T extends LiquidElement> PDDocument drawText(PDDocument pdDocument, PDPage pdPage, T liquidElement) {
        try {
            // Neues Structure Element erstellen und dem Parent hinzuf??gen
            PDStructureElement parentStructureElement = liquidElement.getLiquidDocument().getPdStructureElement();
            PDStructureElement pdStructureElement = new PDStructureElement(StandardStructureTypes.P, parentStructureElement);
            pdStructureElement.setPage(pdPage);
            parentStructureElement.appendKid(pdStructureElement);

            // Text mit Markierung erstellen und dem gerade erstellen Structure Element als Kind anh??ngen
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

            // pdStructureElement wird hier gesetzt, damit es am Ende dem PDF ParentTree hinzugef??gt werden kann
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
