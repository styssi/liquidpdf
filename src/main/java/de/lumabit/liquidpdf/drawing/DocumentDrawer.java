package de.lumabit.liquidpdf.drawing;

import de.lumabit.liquidpdf.exception.LiquidPdfException;
import de.lumabit.liquidpdf.liquidelement.LiquidDocument;
import de.lumabit.liquidpdf.liquidelement.LiquidPage;
import de.lumabit.liquidpdf.setting.Font;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PageLayout;
import org.apache.pdfbox.pdmodel.common.COSObjectable;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.pdmodel.common.PDNumberTreeNode;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDMarkInfo;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDParentTreeValue;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDStructureTreeRoot;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.viewerpreferences.PDViewerPreferences;
import org.apache.xmpbox.XMPMetadata;
import org.apache.xmpbox.XmpConstants;
import org.apache.xmpbox.schema.*;
import org.apache.xmpbox.type.BadFieldValueException;
import org.apache.xmpbox.xml.XmpSerializer;

import javax.xml.transform.TransformerException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class DocumentDrawer {

    public InputStream draw(LiquidDocument liquidDocument) {
        try {

            PDDocument pdDocument = new PDDocument();
            addInfo(pdDocument, liquidDocument);
            addTypeCatalog(pdDocument, liquidDocument);
            embedFonts(pdDocument);
            // pdDocument = loadDrawableImage(pdDocument, document);

            for (LiquidPage liquidPage : liquidDocument.getLiquidPages()) {
                new PageDrawer().draw(pdDocument, liquidPage);
            }

            PDNumberTreeNode parentTree = generateParentTree(liquidDocument);
            enrichTypeStructTreeRoot(pdDocument, parentTree);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            pdDocument.save(outputStream);
            pdDocument.close();
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException e) {
            throw new LiquidPdfException("Generiertes PDF Dokument konnte nicht geschlosses werden", e);
        }
    }

    private PDDocument addInfo(PDDocument pdDocument, LiquidDocument liquidDocument) {
        PDDocumentInformation documentInformation = pdDocument.getDocumentInformation();
        documentInformation.setTitle(liquidDocument.getTitle());
        documentInformation.setCreationDate(liquidDocument.getCreated());
        documentInformation.setProducer(liquidDocument.getProducer());
        documentInformation.setTrapped("Unknown");
        return pdDocument;
    }

    /**
     * Zu dem PdDocument alle PdDocumentCatalog Metainformationen hinzufügen, die aktuell schon bekannt sein
     *
     * @param liquidDocument
     * @return
     */
    private PDDocument addTypeCatalog(PDDocument pdDocument, LiquidDocument liquidDocument) {
        try {
            PDDocumentCatalog pdDocumentCatalog = pdDocument.getDocumentCatalog();

            pdDocumentCatalog.setVersion("1.4");
            pdDocumentCatalog.setLanguage(liquidDocument.getLanguage());
            pdDocumentCatalog.setPageLayout(PageLayout.SINGLE_PAGE);

            // TODO: Add Pages Type
//            pdDocument = addTypePages(pdDocument, pdDocumentCatalog, liquidDocument);
            pdDocument = addTypeMetadata(pdDocument, pdDocumentCatalog, liquidDocument);
            pdDocument = addTypeViewerPreferences(pdDocument, pdDocumentCatalog);
            pdDocument = addTypeAcroForm(pdDocument, pdDocumentCatalog);
            pdDocument = addTypeStructTreeRoot(pdDocument, pdDocumentCatalog);
            pdDocument = addTypeOutputIntent(pdDocument, pdDocumentCatalog);
            pdDocument = addTypeMarkInfo(pdDocument, pdDocumentCatalog);

        } catch (BadFieldValueException | IOException | TransformerException e) {
            throw new LiquidPdfException("Probleme beim erstellen des pdDocument", e);
        }
        return pdDocument;
    }

    /**
     * Den Typ Metadata zu dem PDF hinzufügen, damit PDF A1A konform und barrierefrei wird.
     * <p>
     * Output in PDF
     * <pre>
     *  <?xpacket begin="﻿" id="W5M0MpCehiHzreSzNTczkc9d"?><x:xmpmeta xmlns:x="adobe:ns:meta/">
     *    <rdf:RDF xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
     *    ...
     * </pre>
     *
     * @param pdDocument
     * @param pdDocumentCatalog
     * @param liquidDocument
     * @return
     * @throws BadFieldValueException
     * @throws IOException
     * @throws TransformerException
     */
    private PDDocument addTypeMetadata(PDDocument pdDocument, PDDocumentCatalog pdDocumentCatalog, LiquidDocument liquidDocument)
            throws BadFieldValueException, IOException, TransformerException {
        XMPMetadata xmpMetadata = XMPMetadata.createXMPMetadata();
        xmpMetadata.setEndXPacket(XmpConstants.DEFAULT_XPACKET_END);

        xmpMetadata = generateTypeMetadataPdfSchema(xmpMetadata, liquidDocument);
        xmpMetadata = generateTypeMetadataXmpSchema(xmpMetadata, liquidDocument);
        xmpMetadata = generateTypeMetadataDcSchema(xmpMetadata, liquidDocument);
        xmpMetadata = generateTypeMetadataPdfaidSchema(xmpMetadata);
        xmpMetadata = generateTypeMetadataPdfaEtensionSchema(xmpMetadata);

        // write collected metadata to pdf
        PDMetadata metadataStream = new PDMetadata(pdDocument);
        pdDocumentCatalog.setMetadata(metadataStream);
        XmpSerializer serializer = new XmpSerializer();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        serializer.serialize(xmpMetadata, baos, true);
        metadataStream.importXMPMetadata(baos.toByteArray());
        return pdDocument;
    }

    private XMPMetadata generateTypeMetadataPdfSchema(XMPMetadata xmpMetadata, LiquidDocument liquidDocument) {
        AdobePDFSchema pdfSchema = xmpMetadata.createAndAddAdobePDFSchema();
        pdfSchema.setProducer(liquidDocument.getProducer());
        return xmpMetadata;
    }

    private XMPMetadata generateTypeMetadataXmpSchema(XMPMetadata xmpMetadata, LiquidDocument liquidDocument) {
        XMPBasicSchema basicSchema = xmpMetadata.createAndAddXMPBasicSchema();
        basicSchema.setCreateDate(liquidDocument.getCreated());
        return xmpMetadata;
    }

    private XMPMetadata generateTypeMetadataDcSchema(XMPMetadata xmpMetadata, LiquidDocument liquidDocument) {
        DublinCoreSchema dcSchema = xmpMetadata.createAndAddDublinCoreSchema();
        dcSchema.setTitle(liquidDocument.getTitle());
        dcSchema.addCreator(liquidDocument.getProducer());
        return xmpMetadata;
    }

    private XMPMetadata generateTypeMetadataPdfaidSchema(XMPMetadata xmpMetadata) throws BadFieldValueException {
        PDFAIdentificationSchema pdfaid = xmpMetadata.createAndAddPFAIdentificationSchema();
        pdfaid.setConformance("A");
        pdfaid.setPart(1);
        return xmpMetadata;
    }

    private XMPMetadata generateTypeMetadataPdfaEtensionSchema(XMPMetadata xmpMetadata) {
        xmpMetadata.createAndAddPDFAExtensionSchemaWithDefaultNS();
        xmpMetadata.getPDFExtensionSchema().addNamespace("http://www.aiim.org/pdfa/ns/schema#", "pdfaSchema");
        xmpMetadata.getPDFExtensionSchema().addNamespace("http://www.aiim.org/pdfa/ns/property#", "pdfaProperty");
        xmpMetadata.getPDFExtensionSchema().addNamespace("http://www.aiim.org/pdfua/ns/id/", "pdfuaid");
        XMPSchema uaSchema = new XMPSchema(XMPMetadata.createXMPMetadata(), "pdfaSchema", "pdfaSchema", "pdfaSchema");
        uaSchema.setTextPropertyValue("schema", "PDF/UA Universal Accessibility Schema");
        uaSchema.setTextPropertyValue("namespaceURI", "http://www.aiim.org/pdfua/ns/id/");
        uaSchema.setTextPropertyValue("prefix", "pdfuaid");
        XMPSchema uaProp = new XMPSchema(XMPMetadata.createXMPMetadata(), "pdfaProperty", "pdfaProperty", "pdfaProperty");
        uaProp.setTextPropertyValue("name", "part");
        uaProp.setTextPropertyValue("valueType", "Integer");
        uaProp.setTextPropertyValue("category", "internal");
        uaProp.setTextPropertyValue("description", "Indicates, which part of ISO 14289 standard is followed");
        uaSchema.addUnqualifiedSequenceValue("property", uaProp);
        xmpMetadata.getPDFExtensionSchema().addBagValue("schemas", uaSchema);
        xmpMetadata.getPDFExtensionSchema().setPrefix("pdfuaid");
        xmpMetadata.getPDFExtensionSchema().setTextPropertyValue("part", "1");
        return xmpMetadata;
    }

    private PDDocument addTypeViewerPreferences(PDDocument pdDocument, PDDocumentCatalog pdDocumentCatalog) {
        pdDocumentCatalog.setViewerPreferences(new PDViewerPreferences(new COSDictionary()));
        pdDocumentCatalog.getViewerPreferences().setDisplayDocTitle(true);
        return pdDocument;
    }

    private PDDocument addTypeAcroForm(PDDocument pdDocument, PDDocumentCatalog pdDocumentCatalog) {
        PDAcroForm pdAcroForm = new PDAcroForm(pdDocument);
        pdDocumentCatalog.setAcroForm(pdAcroForm);
        return pdDocument;
    }

    /**
     * Aktuell wird hier die RoleMap hinzugefügt. Nach der Generierung des Dokuments wird die StructTreeRoot noch um Attribute erweitert. Mal schauen ob man das alles hier reinpacken kann.
     * TODO
     *
     * @param pdDocument
     * @param pdDocumentCatalog
     * @return
     */
    private PDDocument addTypeStructTreeRoot(PDDocument pdDocument, PDDocumentCatalog pdDocumentCatalog) {
        PDStructureTreeRoot structureTreeRoot = new PDStructureTreeRoot();
        final String FIGURE = "Figure";
        final String NOTE = "Note";
        final String SPAN = "Span";
        HashMap<String, String> roleMap = new HashMap<>();
        roleMap.put("Annotation", SPAN);
        roleMap.put("Artifact", "P");
        roleMap.put("Bibliography", "BibEntry");
        roleMap.put("Chart", FIGURE);
        roleMap.put("Diagram", FIGURE);
        roleMap.put("DropCap", FIGURE);
        roleMap.put("EndNote", NOTE);
        roleMap.put("FootNote", NOTE);
        roleMap.put("InlineShape", FIGURE);
        roleMap.put("Outline", SPAN);
        roleMap.put("Strikeout", SPAN);
        roleMap.put("Subscript", SPAN);
        roleMap.put("Superscript", SPAN);
        roleMap.put("Underline", SPAN);
        structureTreeRoot.setRoleMap(roleMap);
        pdDocumentCatalog.setStructureTreeRoot(structureTreeRoot);
        return pdDocument;
    }


    /**
     * Output Intent fügt Informationen bzgl. des verwendeten Farbenspektrums zu dem PDF Dokument hinzu
     *
     * @param pdDocument
     * @param pdDocumentCatalog
     * @return
     * @throws IOException
     */
    private PDDocument addTypeOutputIntent(PDDocument pdDocument, PDDocumentCatalog pdDocumentCatalog) throws IOException {
        final String COLOR_PROFILE_INFO = "sRGB IEC61966-2.1";
        InputStream colorProfile = Thread.currentThread().getContextClassLoader().getResourceAsStream("colorprofiles/sRGB2014.icc");
        PDOutputIntent intent = new PDOutputIntent(pdDocument, colorProfile);
        intent.setInfo(COLOR_PROFILE_INFO);
        intent.setOutputCondition(COLOR_PROFILE_INFO);
        intent.setOutputConditionIdentifier(COLOR_PROFILE_INFO);
        intent.setRegistryName("http://www.color.org");
        pdDocumentCatalog.addOutputIntent(intent);
        return pdDocument;
    }

    private PDDocument addTypeMarkInfo(PDDocument pdDocument, PDDocumentCatalog pdDocumentCatalog) {
        PDMarkInfo markInfo = new PDMarkInfo();
        markInfo.setMarked(true);
        pdDocumentCatalog.setMarkInfo(markInfo);
        return pdDocument;
    }

    private PDDocument embedFonts(PDDocument pdDocument) {
        EmbeddedFont.fonts = new HashMap<>();
        for (int i = 0; i < Font.values().length; i++) {
            try {
                InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("fonts/" + Font.values()[i].getBezeichnung());
                PDType0Font pdType0Font = PDType0Font.load(pdDocument, inputStream, true);
                EmbeddedFont.fonts.put(Font.values()[i], pdType0Font);
            } catch (IOException e) {
                throw new LiquidPdfException("Probleme mit Laden von Font " + Font.values()[i].name(), e);
            }
        }
        return pdDocument;
    }

    private PDNumberTreeNode generateParentTree(LiquidDocument liquidDocument) {
        COSArray structElemems = new COSArray();
        Map<Integer, COSObjectable> numbers = new HashMap<>();

        liquidDocument.getLiquidPages().forEach(p -> {
            p.getLiquidElements().forEach(e -> {
                if (e.getPdStructureElement() != null) {
                    structElemems.add(e.getPdStructureElement());
                }
                if (e.getPdObjectReference() != null) {
                    // TODO: Make generic
                    numbers.put(1, e.getPdStructureElement());
                }
            });
        });

        numbers.put(0, structElemems);

        PDNumberTreeNode parentTree = new PDNumberTreeNode(PDParentTreeValue.class);
        parentTree.setNumbers(numbers);
        return parentTree;
    }

    private PDDocument enrichTypeStructTreeRoot(PDDocument pdDocument, PDNumberTreeNode parentTree) {
        // TODO: Generisch setzen
        pdDocument.getDocumentCatalog().getStructureTreeRoot().setParentTreeNextKey(2);
        pdDocument.getDocumentCatalog().getStructureTreeRoot().setParentTree(parentTree);
        return pdDocument;
    }
}
