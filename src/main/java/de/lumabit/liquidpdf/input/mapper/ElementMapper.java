package de.lumabit.liquidpdf.input.mapper;

import de.lumabit.liquidpdf.annotation.LiquidMapperReference;
import de.lumabit.liquidpdf.input.Element;
import de.lumabit.liquidpdf.exception.LiquidPdfException;
import de.lumabit.liquidpdf.model.LiquidElement;
import de.lumabit.liquidpdf.model.LiquidPage;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ElementMapper {

    public <T extends Element, L extends LiquidElement> List<L> map(LiquidPage liquidPage, List<T> elements) {
        List<L> liquidElements = new ArrayList<>();
        elements.forEach(e -> liquidElements.add(map(liquidPage, e)));
        return liquidElements;
    }

    public <T extends Element, L extends LiquidElement> L map(LiquidPage liquidPage, T element) {
        try {
            LiquidMapperReference elementMapperClazz = element.getClass().getAnnotation(LiquidMapperReference.class);
            ElementMapper elementMapper = (ElementMapper) elementMapperClazz.mapper().getDeclaredConstructor().newInstance();
            L liquidElement = elementMapper.mapElement(liquidPage, element);
            return liquidElement;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new LiquidPdfException("Die Funktion mapElement wurde im Mapper nicht implementiert");
        }
    }

    protected <T extends Element, L extends LiquidElement> L mapElement(LiquidPage liquidPage, T element) {
        throw new LiquidPdfException("Die Funktion mapElement wurde im Mapper nicht implementiert");
    }


}
