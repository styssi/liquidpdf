package de.lumabit.liquidpdf;

import de.lumabit.liquidpdf.element.Document;
import de.lumabit.liquidpdf.element.Link;
import de.lumabit.liquidpdf.element.P;
import de.lumabit.liquidpdf.element.Page;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;

public class Main {

    private static final Log LOGGER = LogFactory.getLog(Main.class);

    private static final String VERSION = "02";
    private static final String FEATURE = "Document and Pages";

    public static void main(String[] args) throws IOException {
        InputStream inputStream = PdfGenerator.builder()
                .document(Document.builder()
                        .addPage(Page.builder()
                                .addElement(P.builder()
                                        .text("Hallo irgendwann getrennt werden muss.")
                                        .build())
                                .addElement(Link.builder()
                                        .text("Hallo das ist ein ganz langer Text, der irgendwann getrennt werden muss. Hallo das ist ein ganz langer Text, der irgendwann getrennt werden muss. Hallo das ist ein ganz langer Text, der irgendwann getrennt werden muss.")
                                        .href("https://www.web.de")
                                        .build())
                                .build())
//                        .addPage(Page.builder()
//                                .addElement(Element.builder()
//                                        .text("Hallo das ist ein ganz langer Text, der irgendwann getrennt werden muss. Hallo das ist ein ganz langer Text, der irgendwann getrennt werden muss. Hallo das ist ein ganz langer Text, der irgendwann getrennt werden muss.")
//                                        .build())
//                                .addElement(Element.builder()
//                                        .text("Hallo das ist ein ganz langer Text, der irgendwann getrennt werden muss. Hallo das ist ein ganz langer Text, der irgendwann getrennt werden muss. Hallo das ist ein ganz langer Text, der irgendwann getrennt werden muss.")
//                                        .build())
//                                .build())
                        .build())
                .build().generate();


        new File("versions\\" + VERSION).mkdirs();
        inputStreamToFile(inputStream, "versions\\" + VERSION + "\\" + FEATURE);
    }

    protected static void inputStreamToFile(InputStream inputStream, String filename) {
        try {
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            OutputStream pdf = new FileOutputStream(new File(filename + ".pdf"));
            pdf.write(buffer);
            pdf.close();
            OutputStream txt = new FileOutputStream(new File(filename + ".txt"));
            txt.write(buffer);
            txt.close();
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }
}
