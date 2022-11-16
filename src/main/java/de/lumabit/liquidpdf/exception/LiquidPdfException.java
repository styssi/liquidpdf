package de.lumabit.liquidpdf.exception;

public class LiquidPdfException extends RuntimeException {
    public LiquidPdfException(String message) {
        super(message);
    }

    public LiquidPdfException(String message, Throwable e) {
        super(message, e);
    }

}
