package de.lumabit.liquidpdf.input.mapper;

import de.lumabit.liquidpdf.input.Page;
import de.lumabit.liquidpdf.model.LiquidDocument;
import de.lumabit.liquidpdf.model.LiquidPage;

import java.util.ArrayList;
import java.util.List;

public class PageMapper {
    public static List<LiquidPage> mapPages(List<Page> pages, LiquidDocument liquidDocument) {
        List<LiquidPage> liquidPages = new ArrayList<>();
        pages.forEach(p -> liquidPages.add(mapPage(p, liquidDocument)));
        return liquidPages;
    }

    public static LiquidPage mapPage(Page page, LiquidDocument liquidDocument) {
        LiquidPage liquidPage = LiquidPage.builder()
                .liquidDocument(liquidDocument)
                .height(page.getHeight())
                .width(page.getWidth())
                .build();
        liquidPage.setLiquidElements(new ElementMapper().map(liquidPage, page.getElements()));
        return liquidPage;
    }
}
