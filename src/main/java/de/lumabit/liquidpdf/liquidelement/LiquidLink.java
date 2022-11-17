package de.lumabit.liquidpdf.liquidelement;

import de.lumabit.liquidpdf.annotation.LiquidDrawerReference;
import de.lumabit.liquidpdf.drawing.LinkDrawer;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@LiquidDrawerReference(drawer = LinkDrawer.class)
public class LiquidLink extends LiquidElement {
    private String href;
}
