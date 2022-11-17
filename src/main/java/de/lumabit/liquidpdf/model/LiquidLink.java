package de.lumabit.liquidpdf.model;

import de.lumabit.liquidpdf.annotation.LiquidDrawerReference;
import de.lumabit.liquidpdf.drawer.LinkDrawer;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@LiquidDrawerReference(drawer = LinkDrawer.class)
public class LiquidLink extends LiquidElement {
    private String href;
}
