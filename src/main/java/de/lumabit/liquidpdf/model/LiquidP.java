package de.lumabit.liquidpdf.model;

import de.lumabit.liquidpdf.annotation.LiquidDrawerReference;
import de.lumabit.liquidpdf.drawer.PDrawer;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@LiquidDrawerReference(drawer = PDrawer.class)
public class LiquidP extends LiquidElement {
}
