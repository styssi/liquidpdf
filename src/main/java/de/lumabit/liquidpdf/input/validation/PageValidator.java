package de.lumabit.liquidpdf.input.validation;

import de.lumabit.liquidpdf.input.Page;

import java.util.List;

public class PageValidator {
    public static void validate(List<Page> pages) {
        pages.forEach(p -> {
            p.getElements().forEach(e -> {
                ElementValidator.validate(e);
            });
        });
    }


}
