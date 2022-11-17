package de.lumabit.liquidpdf.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDObjectReference;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDStructureElement;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;

@SuperBuilder
@Getter
@Setter
public class LiquidElement {

    private LiquidPage liquidPage;
    private LiquidDocument liquidDocument;

    private PDStructureElement pdStructureElement;
    private PDStructureElement parentElement;
    private PDAnnotation pdAnnotation;
    private PDObjectReference pdObjectReference;

    private float x;
    private float y;

    // geerbt von Element
    private String text;

    public int getGlobalMCID() {
        return liquidDocument.getMCID();
    }

    public void increaseGlobalMCID() {
        liquidDocument.setMCID(liquidDocument.getMCID() + 1);
    }

}
