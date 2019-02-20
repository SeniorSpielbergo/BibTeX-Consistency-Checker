package de.david_wille.bibtexconsistencychecker.util;

public class BCCSpecialCharacterHandling {
	
	private static final String LATEX_SZ = "{\\ss}";
	
	private static final String LATEX_AE_SMALL = "{\\\"{a}}";
	private static final String LATEX_OE_SMALL = "{\\\"{o}}";
	private static final String LATEX_UE_SMALL = "{\\\"{u}}";
	private static final String LATEX_A_AIGU_SMALL = "{\\'{a}}";
	private static final String LATEX_E_AIGU_SMALL = "{\\'{e}}";
	private static final String LATEX_I_AIGU_SMALL = "{\\'{i}}";
	private static final String LATEX_O_AIGU_SMALL = "{\\'{o}}";
	private static final String LATEX_U_AIGU_SMALL = "{\\'{u}}";
	private static final String LATEX_A_CIRCONFLEXE_SMALL = "{\\^{a}}";
	private static final String LATEX_E_CIRCONFLEXE_SMALL = "{\\^{e}}";
	private static final String LATEX_I_CIRCONFLEXE_SMALL = "{\\^{i}}";
	private static final String LATEX_O_CIRCONFLEXE_SMALL = "{\\^{o}}";
	private static final String LATEX_A_GRAVE_SMALL = "{\\`{a}}";
	private static final String LATEX_E_GRAVE_SMALL = "{\\`{e}}";
	private static final String LATEX_I_GRAVE_SMALL = "{\\`{i}}";
	private static final String LATEX_O_GRAVE_SMALL = "{\\`{o}}";
	private static final String LATEX_A_TILDE_SMALL = "{\\~{a}}";
	private static final String LATEX_N_TILDE_SMALL = "{\\~{n}}";
	private static final String LATEX_U_TILDE_SMALL = "{\\~{u}}";
	private static final String LATEX_C_CEDILLE_SMALL = "{\\c{c}}";
	private static final String LATEX_C_HACEK_SMALL = "{\\v{c}}";
	private static final String LATEX_S_HACEK_SMALL = "{\\v{s}}";
	private static final String LATEX_A_OGONEK_SMALL = "{\\k{a}}";
	private static final String LATEX_A_BREVIS_SMALL = "{\\u{a}}";
	private static final String LATEX_CRAZY_L_SMALL = "{\\l}";
	private static final String LATEX_NORWEGIAN_O_SMALL = "{\\fontfamily{cmr}\\selectfont{\\o}}";
	private static final String AE_SMALL = "ae";
	private static final String OE_SMALL = "oe";
	private static final String UE_SMALL = "ue";
	private static final String A_SMALL = "a";
	private static final String C_SMALL = "c";
	private static final String E_SMALL = "e";
	private static final String I_SMALL = "i";
	private static final String L_SMALL = "l";
	private static final String N_SMALL = "n";
	private static final String O_SMALL = "o";
	private static final String U_SMALL = "u";
	private static final String S_SMALL = "s";
	private static final String DOUBLE_S_SMALL = "ss";

	private static final String LATEX_AE_BIG = "{\\\"{A}}";
	private static final String LATEX_OE_BIG = "{\\\"{O}}";
	private static final String LATEX_UE_BIG = "{\\\"{U}}";
	private static final String LATEX_A_AIGU_BIG = "{\\\'{A}}";
	private static final String LATEX_E_AIGU_BIG = "{\\\'{E}}";
	private static final String LATEX_O_AIGU_BIG = "{\\\'{O}}";
	private static final String LATEX_S_AIGU_BIG = "{\\\'{S}}";
	private static final String LATEX_A_CIRCONFLEXE_BIG = "{\\^{A}}";
	private static final String LATEX_E_CIRCONFLEXE_BIG = "{\\^{E}}";
	private static final String LATEX_I_CIRCONFLEXE_BIG = "{\\^{I}}";
	private static final String LATEX_O_CIRCONFLEXE_BIG = "{\\^{O}}";
	private static final String LATEX_A_GRAVE_BIG = "{\\`{A}}";
	private static final String LATEX_E_GRAVE_BIG = "{\\`{E}}";
	private static final String LATEX_I_GRAVE_BIG = "{\\`{I}}";
	private static final String LATEX_O_GRAVE_BIG = "{\\`{O}}";
	private static final String LATEX_A_OGONEK_BIG = "{\\k{A}}";
	private static final String LATEX_NORWEGIAN_O_BIG = "{\\fontfamily{cmr}\\selectfont{\\O}}";
	private static final String LATEX_HYPHENATION = "{\\-}";
	private static final String LATEX_HYPHEN = "{\\hyphen}";
	private static final String AE_BIG = "Ae";
	private static final String OE_BIG = "Oe";
	private static final String UE_BIG = "Ue";
	private static final String A_BIG = "A";
	private static final String E_BIG = "E";
	private static final String I_BIG = "E";
	private static final String O_BIG = "O";
	private static final String S_BIG = "S";
	
	private static final String ESCAPED_HASH = "\\#";
	
	private static final String EMPTY_STRING = "";
	
	public static String replaceSpecialLatexCharacters(String value) {
		if (value.contains(ESCAPED_HASH)) {
			value = value.replace(ESCAPED_HASH, EMPTY_STRING);
		}
		if (value.contains(LATEX_HYPHENATION)) {
			value = value.replace(LATEX_HYPHENATION, EMPTY_STRING);
		}
		if (value.contains(LATEX_HYPHEN)) {
			value = value.replace(LATEX_HYPHEN, EMPTY_STRING);
		}
		if (value.contains(LATEX_SZ)) {
			value = value.replace(LATEX_SZ, DOUBLE_S_SMALL);
		}
		if (value.contains(LATEX_AE_SMALL)) {
			value = value.replace(LATEX_AE_SMALL, AE_SMALL);
		}
		if (value.contains(LATEX_OE_SMALL)) {
			value = value.replace(LATEX_OE_SMALL, OE_SMALL);
		}
		if (value.contains(LATEX_UE_SMALL)) {
			value = value.replace(LATEX_UE_SMALL, UE_SMALL);
		}
		if (value.contains(LATEX_A_AIGU_SMALL)) {
			value = value.replace(LATEX_A_AIGU_SMALL, A_SMALL);
		}
		if (value.contains(LATEX_E_AIGU_SMALL)) {
			value = value.replace(LATEX_E_AIGU_SMALL, E_SMALL);
		}
		if (value.contains(LATEX_I_AIGU_SMALL)) {
			value = value.replace(LATEX_I_AIGU_SMALL, I_SMALL);
		}
		if (value.contains(LATEX_O_AIGU_SMALL)) {
			value = value.replace(LATEX_O_AIGU_SMALL, O_SMALL);
		}
		if (value.contains(LATEX_U_AIGU_SMALL)) {
			value = value.replace(LATEX_U_AIGU_SMALL, U_SMALL);
		}
		if (value.contains(LATEX_A_CIRCONFLEXE_SMALL)) {
			value = value.replace(LATEX_A_CIRCONFLEXE_SMALL, A_SMALL);
		}
		if (value.contains(LATEX_E_CIRCONFLEXE_SMALL)) {
			value = value.replace(LATEX_E_CIRCONFLEXE_SMALL, E_SMALL);
		}
		if (value.contains(LATEX_I_CIRCONFLEXE_SMALL)) {
			value = value.replace(LATEX_I_CIRCONFLEXE_SMALL, I_SMALL);
		}
		if (value.contains(LATEX_O_CIRCONFLEXE_SMALL)) {
			value = value.replace(LATEX_O_CIRCONFLEXE_SMALL, O_SMALL);
		}
		if (value.contains(LATEX_A_GRAVE_SMALL)) {
			value = value.replace(LATEX_A_GRAVE_SMALL, A_SMALL);
		}
		if (value.contains(LATEX_E_GRAVE_SMALL)) {
			value = value.replace(LATEX_E_GRAVE_SMALL, E_SMALL);
		}
		if (value.contains(LATEX_I_GRAVE_SMALL)) {
			value = value.replace(LATEX_I_GRAVE_SMALL, I_SMALL);
		}
		if (value.contains(LATEX_O_GRAVE_SMALL)) {
			value = value.replace(LATEX_O_GRAVE_SMALL, O_SMALL);
		}
		if (value.contains(LATEX_A_TILDE_SMALL)) {
			value = value.replace(LATEX_A_TILDE_SMALL, A_SMALL);
		}
		if (value.contains(LATEX_N_TILDE_SMALL)) {
			value = value.replace(LATEX_N_TILDE_SMALL, N_SMALL);
		}
		if (value.contains(LATEX_U_TILDE_SMALL)) {
			value = value.replace(LATEX_U_TILDE_SMALL, U_SMALL);
		}
		if (value.contains(LATEX_C_CEDILLE_SMALL)) {
			value = value.replace(LATEX_C_CEDILLE_SMALL, C_SMALL);
		}
		if (value.contains(LATEX_C_HACEK_SMALL)) {
			value = value.replace(LATEX_C_HACEK_SMALL, C_SMALL);
		}
		if (value.contains(LATEX_S_HACEK_SMALL)) {
			value = value.replace(LATEX_S_HACEK_SMALL, S_SMALL);
		}
		if (value.contains(LATEX_A_OGONEK_SMALL)) {
			value = value.replace(LATEX_A_OGONEK_SMALL, A_SMALL);
		}
		if (value.contains(LATEX_A_BREVIS_SMALL)) {
			value = value.replace(LATEX_A_BREVIS_SMALL, A_SMALL);
		}
		if (value.contains(LATEX_CRAZY_L_SMALL)) {
			value = value.replace(LATEX_CRAZY_L_SMALL, L_SMALL);
		}
		if (value.contains(LATEX_NORWEGIAN_O_SMALL)) {
			value = value.replace(LATEX_NORWEGIAN_O_SMALL, O_SMALL);
		}
		if (value.contains(LATEX_AE_BIG)) {
			value = value.replace(LATEX_AE_BIG, AE_BIG);
		}
		if (value.contains(LATEX_OE_BIG)) {
			value = value.replace(LATEX_OE_BIG, OE_BIG);
		}
		if (value.contains(LATEX_UE_BIG)) {
			value = value.replace(LATEX_UE_BIG, UE_BIG);
		}
		if (value.contains(LATEX_A_AIGU_BIG)) {
			value = value.replace(LATEX_A_AIGU_BIG, A_BIG);
		}
		if (value.contains(LATEX_E_AIGU_BIG)) {
			value = value.replace(LATEX_E_AIGU_BIG, E_BIG);
		}
		if (value.contains(LATEX_O_AIGU_BIG)) {
			value = value.replace(LATEX_O_AIGU_BIG, O_BIG);
		}
		if (value.contains(LATEX_S_AIGU_BIG)) {
			value = value.replace(LATEX_S_AIGU_BIG, S_BIG);
		}
		if (value.contains(LATEX_A_CIRCONFLEXE_BIG)) {
			value = value.replace(LATEX_A_CIRCONFLEXE_BIG, A_BIG);
		}
		if (value.contains(LATEX_E_CIRCONFLEXE_BIG)) {
			value = value.replace(LATEX_E_CIRCONFLEXE_BIG, E_BIG);
		}
		if (value.contains(LATEX_I_CIRCONFLEXE_BIG)) {
			value = value.replace(LATEX_I_CIRCONFLEXE_BIG, I_BIG);
		}
		if (value.contains(LATEX_O_CIRCONFLEXE_BIG)) {
			value = value.replace(LATEX_O_CIRCONFLEXE_BIG, O_BIG);
		}
		if (value.contains(LATEX_A_GRAVE_BIG)) {
			value = value.replace(LATEX_A_GRAVE_BIG, A_BIG);
		}
		if (value.contains(LATEX_E_GRAVE_BIG)) {
			value = value.replace(LATEX_E_GRAVE_BIG, E_BIG);
		}
		if (value.contains(LATEX_I_GRAVE_BIG)) {
			value = value.replace(LATEX_I_GRAVE_BIG, I_BIG);
		}
		if (value.contains(LATEX_O_GRAVE_BIG)) {
			value = value.replace(LATEX_O_GRAVE_BIG, O_BIG);
		}
		if (value.contains(LATEX_A_OGONEK_BIG)) {
			value = value.replace(LATEX_A_OGONEK_BIG, A_BIG);
		}
		if (value.contains(LATEX_NORWEGIAN_O_BIG)) {
			value = value.replace(LATEX_NORWEGIAN_O_BIG, O_BIG);
		}
		
		return value;
	}

}
