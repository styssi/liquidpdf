package de.lumabit.liquidpdf.element.mapper;

import de.lumabit.liquidpdf.element.Document;
import de.lumabit.liquidpdf.liquidelement.LiquidDocument;

import java.util.Calendar;

public class DocumentMapper {
    public static LiquidDocument mapDocument(Document document) {
        LiquidDocument liquidDocument = LiquidDocument.builder()
                .created(Calendar.getInstance())
                .language(document.getLanguage())
                .producer(document.getProducer())
                .title(document.getTitle())
                .build();
        liquidDocument.setLiquidPages(PageMapper.mapPages(document.getPages(), liquidDocument));
        return liquidDocument;
    }
}
