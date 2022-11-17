package de.lumabit.liquidpdf.input;

import de.lumabit.liquidpdf.annotation.LiquidMapperReference;
import de.lumabit.liquidpdf.input.mapper.PMapper;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@LiquidMapperReference(mapper = PMapper.class)
public class P extends Element {
}
