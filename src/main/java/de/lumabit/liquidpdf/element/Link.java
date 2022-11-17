package de.lumabit.liquidpdf.element;

import de.lumabit.liquidpdf.annotation.LiquidMapperReference;
import de.lumabit.liquidpdf.element.mapper.LinkMapper;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@LiquidMapperReference(mapper = LinkMapper.class)
public class Link extends Element {
    private String href;
}
