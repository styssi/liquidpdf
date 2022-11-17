package de.lumabit.liquidpdf.input;

import de.lumabit.liquidpdf.annotation.LiquidMapperReference;
import de.lumabit.liquidpdf.input.mapper.LinkMapper;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@LiquidMapperReference(mapper = LinkMapper.class)
public class Link extends Element {
    private String href;
}
