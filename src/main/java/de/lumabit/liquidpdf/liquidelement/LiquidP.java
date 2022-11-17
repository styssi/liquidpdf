package de.lumabit.liquidpdf.liquidelement;

import de.lumabit.liquidpdf.annotation.LiquidDrawerReference;
import de.lumabit.liquidpdf.drawing.PDrawer;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@LiquidDrawerReference(drawer = PDrawer.class)
public class LiquidP extends LiquidElement {
}
