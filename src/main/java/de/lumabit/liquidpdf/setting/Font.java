package de.lumabit.liquidpdf.setting;

public enum Font {
	ROBOTO_BOLD("Roboto-Bold.ttf"), //
	ROBOTO_BOLD_ITALIC("Roboto-BoldItalic.ttf"), //
	ROBOTO_REGULAR("Roboto-Regular.ttf"),
	ROBOTO_CONDENSED_BOLD("Roboto-Condensed-Bold.ttf"),
	ROBOTO_MEDIUM("Roboto-Medium.ttf"),

	BA_ICONS_DPL("ba-icons-dpl.ttf"),
	BA_ICONS_VERLAUF_EXTENDED("ba-icons-verlauf-extended.ttf");

	Font(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	String bezeichnung;

	public String getBezeichnung() {
		return this.bezeichnung;
	}
}
