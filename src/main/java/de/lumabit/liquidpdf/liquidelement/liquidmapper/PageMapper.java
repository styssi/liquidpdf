package de.lumabit.liquidpdf.liquidelement.liquidmapper;

import de.lumabit.liquidpdf.element.Page;
import de.lumabit.liquidpdf.liquidelement.LiquidDocument;
import de.lumabit.liquidpdf.liquidelement.LiquidPage;

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
        liquidPage.setLiquidElements(ElementMapper.mapElements(page.getElements(), liquidPage));
        return liquidPage;
    }
}
