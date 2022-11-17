package de.lumabit.liquidpdf.element;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class Link extends Element {
    private String href;
}
