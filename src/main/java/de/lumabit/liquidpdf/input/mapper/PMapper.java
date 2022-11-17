package de.lumabit.liquidpdf.input.mapper;

import de.lumabit.liquidpdf.input.Element;
import de.lumabit.liquidpdf.model.LiquidElement;
import de.lumabit.liquidpdf.model.LiquidP;
import de.lumabit.liquidpdf.model.LiquidPage;

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
