package de.lumabit.liquidpdf.liquidelement;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDStructureElement;

import java.util.Calendar;
import java.util.List;

@Getter
@Setter
@Builder
public class LiquidDocument {

    private List<LiquidPage> liquidPages;
    private Calendar created;
    private int MCID = 0;
    private PDStructureElement pdStructureElement;

    // geerbt von Document
    private String language;
    private String title;
    private String producer;

}
