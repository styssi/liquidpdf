package de.lumabit.liquidpdf.liquidelement.liquidmapper;

import de.lumabit.liquidpdf.element.Element;
import de.lumabit.liquidpdf.liquidelement.LiquidElement;
import de.lumabit.liquidpdf.liquidelement.LiquidPage;

import java.util.ArrayList;
import java.util.List;

public class ElementMapper {

    public static List<LiquidElement> mapElements(List<Element> elements, LiquidPage liquidPage) {
        List<LiquidElement> liquidElements = new ArrayList<>();
        elements.forEach(e -> liquidElements.add(mapElement(e, liquidPage)));
        return liquidElements;
    }

    public static LiquidElement mapElement(Element element, LiquidPage liquidPage) {
        LiquidElement liquidElement = LiquidElement.builder()
                .liquidDocument(liquidPage.getLiquidDocument())
                .liquidPage(liquidPage)
                .text(element.getText())
                .build();
        return liquidElement;
    }


}
