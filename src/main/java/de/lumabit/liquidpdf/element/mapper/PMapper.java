package de.lumabit.liquidpdf.element.mapper;

import de.lumabit.liquidpdf.element.Element;
import de.lumabit.liquidpdf.liquidelement.LiquidElement;
import de.lumabit.liquidpdf.liquidelement.LiquidP;
import de.lumabit.liquidpdf.liquidelement.LiquidPage;

public class PMapper extends ElementMapper {

    @Override
    public <T extends Element, L extends LiquidElement> L mapElement(LiquidPage liquidPage, T element) {
        return (L) LiquidP.builder()
                .liquidDocument(liquidPage.getLiquidDocument())
                .liquidPage(liquidPage)
                .text(element.getText())
                .build();
    }
}
