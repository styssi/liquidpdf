package de.lumabit.liquidpdf.liquidelement;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class LiquidPage {

    private LiquidDocument liquidDocument;
    private List<LiquidElement> liquidElements;

    // geerbt von Page
    private float width;
    private float height;

}
