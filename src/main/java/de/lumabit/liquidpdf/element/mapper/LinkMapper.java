package de.lumabit.liquidpdf.element.mapper;

import de.lumabit.liquidpdf.element.Element;
import de.lumabit.liquidpdf.element.Link;
import de.lumabit.liquidpdf.liquidelement.LiquidElement;
import de.lumabit.liquidpdf.liquidelement.LiquidLink;
import de.lumabit.liquidpdf.liquidelement.LiquidPage;

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
