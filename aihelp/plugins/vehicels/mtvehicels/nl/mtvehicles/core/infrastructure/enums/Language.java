/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.mtvehicles.core.infrastructure.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import nl.mtvehicles.core.infrastructure.annotations.LanguageSpecific;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;

@LanguageSpecific
public enum Language {
    EN("English"),
    NL("Nederlands"),
    ES("Espa\u00f1ol"),
    CS("\u010ce\u0161tina"),
    DE("Deutsch"),
    CN("\u4e2d\u570b\u4eba"),
    TR("T\u00fcrk"),
    JA("\u65e5\u672c\u8a9e"),
    HE("\u05e2\u05b4\u05d1\u05e8\u05b4\u05d9\u05ea"),
    RU("\u0420\u0443\u0441\u0441\u043a\u0438\u0439"),
    FR("Fran\u00e7ais"),
    TH("\u0e20\u0e32\u0e29\u0e32\u0e44\u0e17\u0e22"),
    GR("\u0395\u03bb\u03bb\u03b7\u03bd\u03b9\u03ba\u03ae"),
    CUSTOM("Custom language");

    private final String languageName;

    private Language(String languageName) {
        this.languageName = languageName;
    }

    public String getLanguageName() {
        return this.languageName;
    }

    public String getLanguageCode() {
        if (this.equals((Object)CUSTOM)) {
            return ConfigModule.secretSettings.getMessagesLanguage();
        }
        return this.toString().toLowerCase(Locale.ROOT);
    }

    @Deprecated
    public static String[] getAllLanguages() {
        return (String[])Language.getAllLanguageCodes().toArray();
    }

    public static List<String> getAllLanguageCodes() {
        return Arrays.stream(Language.values()).map(Enum::toString).map(String::toLowerCase).filter(code -> !code.equals("custom")).collect(Collectors.toList());
    }

    public static boolean isSupported(String languageCode) {
        List<String> languages = Language.getAllLanguageCodes();
        return languages.contains(languageCode.toLowerCase(Locale.ROOT));
    }
}

