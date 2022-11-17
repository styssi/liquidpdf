package de.lumabit.liquidpdf.element;

import de.lumabit.liquidpdf.annotation.LiquidMapperReference;
import de.lumabit.liquidpdf.element.mapper.PMapper;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@LiquidMapperReference(mapper = PMapper.class)
public class P extends Element {
}
