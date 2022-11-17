package de.lumabit.liquidpdf.input.mapper;

import de.lumabit.liquidpdf.input.Element;
import de.lumabit.liquidpdf.input.Link;
import de.lumabit.liquidpdf.model.LiquidElement;
import de.lumabit.liquidpdf.model.LiquidLink;
import de.lumabit.liquidpdf.model.LiquidPage;

public class LinkMapper extends ElementMapper {

    @Override
    public <T extends Element, L extends LiquidElement> L mapElement(LiquidPage liquidPage, T element) {
        Link linkElement = (Link) element;
        LiquidLink liquidLink = LiquidLink.builder()
                .liquidDocument(liquidPage.getLiquidDocument())
                .liquidPage(liquidPage)
                .href(((Link) element).getHref())
                .text(linkElement.getText())
                .build();
        return (L) liquidLink;
    }
}
