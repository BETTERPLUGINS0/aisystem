/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.lang3;

import java.util.Objects;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

public class CharUtils {
    private static final String[] CHAR_STRING_ARRAY = new String[128];
    private static final char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    public static final char LF = '\n';
    public static final char CR = '\r';
    public static final char NUL = '\u0000';

    public static int compare(char c2, char c3) {
        return c2 - c3;
    }

    public static boolean isAscii(char c2) {
        return c2 < '\u0080';
    }

    public static boolean isAsciiAlpha(char c2) {
        return CharUtils.isAsciiAlphaUpper(c2) || CharUtils.isAsciiAlphaLower(c2);
    }

    public static boolean isAsciiAlphaLower(char c2) {
        return c2 >= 'a' && c2 <= 'z';
    }

    public static boolean isAsciiAlphanumeric(char c2) {
        return CharUtils.isAsciiAlpha(c2) || CharUtils.isAsciiNumeric(c2);
    }

    public static boolean isAsciiAlphaUpper(char c2) {
        return c2 >= 'A' && c2 <= 'Z';
    }

    public static boolean isAsciiControl(char c2) {
        return c2 < ' ' || c2 == '\u007f';
    }

    public static boolean isAsciiNumeric(char c2) {
        return c2 >= '0' && c2 <= '9';
    }

    public static boolean isAsciiPrintable(char c2) {
        return c2 >= ' ' && c2 < '\u007f';
    }

    public static char toChar(Character c2) {
        return Objects.requireNonNull(c2, "ch").charValue();
    }

    public static char toChar(Character c2, char c3) {
        return c2 != null ? c2.charValue() : c3;
    }

    public static char toChar(String string) {
        Validate.notEmpty(string, "The String must not be empty", new Object[0]);
        return string.charAt(0);
    }

    public static char toChar(String string, char c2) {
        return StringUtils.isEmpty(string) ? c2 : string.charAt(0);
    }

    @Deprecated
    public static Character toCharacterObject(char c2) {
        return Character.valueOf(c2);
    }

    public static Character toCharacterObject(String string) {
        return StringUtils.isEmpty(string) ? null : Character.valueOf(string.charAt(0));
    }

    public static int toIntValue(char c2) {
        if (!CharUtils.isAsciiNumeric(c2)) {
            throw new IllegalArgumentException("The character " + c2 + " is not in the range '0' - '9'");
        }
        return c2 - 48;
    }

    public static int toIntValue(char c2, int n) {
        return CharUtils.isAsciiNumeric(c2) ? c2 - 48 : n;
    }

    public static int toIntValue(Character c2) {
        return CharUtils.toIntValue(CharUtils.toChar(c2));
    }

    public static int toIntValue(Character c2, int n) {
        return c2 != null ? CharUtils.toIntValue(c2.charValue(), n) : n;
    }

    public static String toString(char c2) {
        if (c2 < CHAR_STRING_ARRAY.length) {
            return CHAR_STRING_ARRAY[c2];
        }
        return String.valueOf(c2);
    }

    public static String toString(Character c2) {
        return c2 != null ? CharUtils.toString(c2.charValue()) : null;
    }

    public static String unicodeEscaped(char c2) {
        return "\\u" + HEX_DIGITS[c2 >> 12 & 0xF] + HEX_DIGITS[c2 >> 8 & 0xF] + HEX_DIGITS[c2 >> 4 & 0xF] + HEX_DIGITS[c2 & 0xF];
    }

    public static String unicodeEscaped(Character c2) {
        return c2 != null ? CharUtils.unicodeEscaped(c2.charValue()) : null;
    }

    @Deprecated
    public CharUtils() {
    }

    static {
        ArrayUtils.setAll(CHAR_STRING_ARRAY, n -> String.valueOf((char)n));
    }
}

