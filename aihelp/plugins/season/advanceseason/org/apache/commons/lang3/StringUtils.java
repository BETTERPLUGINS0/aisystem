/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.lang3;

import java.nio.charset.Charset;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import org.apache.commons.lang3.ArrayFill;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.Charsets;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.function.Suppliers;
import org.apache.commons.lang3.function.ToBooleanBiFunction;
import org.apache.commons.lang3.stream.LangCollectors;
import org.apache.commons.lang3.stream.Streams;

public class StringUtils {
    public static final String SPACE = " ";
    public static final String EMPTY = "";
    public static final String LF = "\n";
    public static final String CR = "\r";
    public static final int INDEX_NOT_FOUND = -1;
    private static final int PAD_LIMIT = 8192;
    private static final Pattern STRIP_ACCENTS_PATTERN = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

    public static String abbreviate(String string, int n) {
        return StringUtils.abbreviate(string, "...", 0, n);
    }

    public static String abbreviate(String string, int n, int n2) {
        return StringUtils.abbreviate(string, "...", n, n2);
    }

    public static String abbreviate(String string, String string2, int n) {
        return StringUtils.abbreviate(string, string2, 0, n);
    }

    public static String abbreviate(String string, String string2, int n, int n2) {
        if (StringUtils.isNotEmpty(string) && EMPTY.equals(string2) && n2 > 0) {
            return StringUtils.substring(string, 0, n2);
        }
        if (StringUtils.isAnyEmpty(string, string2)) {
            return string;
        }
        int n3 = string2.length();
        int n4 = n3 + 1;
        int n5 = n3 + n3 + 1;
        if (n2 < n4) {
            throw new IllegalArgumentException(String.format("Minimum abbreviation width is %d", n4));
        }
        int n6 = string.length();
        if (n6 <= n2) {
            return string;
        }
        if (n > n6) {
            n = n6;
        }
        if (n6 - n < n2 - n3) {
            n = n6 - (n2 - n3);
        }
        if (n <= n3 + 1) {
            return string.substring(0, n2 - n3) + string2;
        }
        if (n2 < n5) {
            throw new IllegalArgumentException(String.format("Minimum abbreviation width with offset is %d", n5));
        }
        if (n + n2 - n3 < n6) {
            return string2 + StringUtils.abbreviate(string.substring(n), string2, n2 - n3);
        }
        return string2 + string.substring(n6 - (n2 - n3));
    }

    public static String abbreviateMiddle(String string, String string2, int n) {
        if (StringUtils.isAnyEmpty(string, string2) || n >= string.length() || n < string2.length() + 2) {
            return string;
        }
        int n2 = n - string2.length();
        int n3 = n2 / 2 + n2 % 2;
        int n4 = string.length() - n2 / 2;
        return string.substring(0, n3) + string2 + string.substring(n4);
    }

    private static String appendIfMissing(String string, CharSequence charSequence, boolean bl, CharSequence ... charSequenceArray) {
        if (string == null || StringUtils.isEmpty(charSequence) || StringUtils.endsWith(string, charSequence, bl)) {
            return string;
        }
        if (ArrayUtils.isNotEmpty(charSequenceArray)) {
            for (CharSequence charSequence2 : charSequenceArray) {
                if (!StringUtils.endsWith(string, charSequence2, bl)) continue;
                return string;
            }
        }
        return string + charSequence;
    }

    public static String appendIfMissing(String string, CharSequence charSequence, CharSequence ... charSequenceArray) {
        return StringUtils.appendIfMissing(string, charSequence, false, charSequenceArray);
    }

    public static String appendIfMissingIgnoreCase(String string, CharSequence charSequence, CharSequence ... charSequenceArray) {
        return StringUtils.appendIfMissing(string, charSequence, true, charSequenceArray);
    }

    public static String capitalize(String string) {
        int n;
        int n2;
        int n3 = StringUtils.length(string);
        if (n3 == 0) {
            return string;
        }
        int n4 = string.codePointAt(0);
        if (n4 == (n2 = Character.toTitleCase(n4))) {
            return string;
        }
        int[] nArray = new int[n3];
        int n5 = 0;
        nArray[n5++] = n2;
        for (int i = Character.charCount(n4); i < n3; i += Character.charCount(n)) {
            n = string.codePointAt(i);
            nArray[n5++] = n;
        }
        return new String(nArray, 0, n5);
    }

    public static String center(String string, int n) {
        return StringUtils.center(string, n, ' ');
    }

    public static String center(String string, int n, char c2) {
        if (string == null || n <= 0) {
            return string;
        }
        int n2 = string.length();
        int n3 = n - n2;
        if (n3 <= 0) {
            return string;
        }
        string = StringUtils.leftPad(string, n2 + n3 / 2, c2);
        return StringUtils.rightPad(string, n, c2);
    }

    public static String center(String string, int n, String string2) {
        int n2;
        int n3;
        if (string == null || n <= 0) {
            return string;
        }
        if (StringUtils.isEmpty(string2)) {
            string2 = SPACE;
        }
        if ((n3 = n - (n2 = string.length())) <= 0) {
            return string;
        }
        string = StringUtils.leftPad(string, n2 + n3 / 2, string2);
        return StringUtils.rightPad(string, n, string2);
    }

    public static String chomp(String string) {
        if (StringUtils.isEmpty(string)) {
            return string;
        }
        if (string.length() == 1) {
            char c2 = string.charAt(0);
            if (c2 == '\r' || c2 == '\n') {
                return EMPTY;
            }
            return string;
        }
        int n = string.length() - 1;
        char c3 = string.charAt(n);
        if (c3 == '\n') {
            if (string.charAt(n - 1) == '\r') {
                --n;
            }
        } else if (c3 != '\r') {
            ++n;
        }
        return string.substring(0, n);
    }

    @Deprecated
    public static String chomp(String string, String string2) {
        return StringUtils.removeEnd(string, string2);
    }

    public static String chop(String string) {
        if (string == null) {
            return null;
        }
        int n = string.length();
        if (n < 2) {
            return EMPTY;
        }
        int n2 = n - 1;
        String string2 = string.substring(0, n2);
        char c2 = string.charAt(n2);
        if (c2 == '\n' && string2.charAt(n2 - 1) == '\r') {
            return string2.substring(0, n2 - 1);
        }
        return string2;
    }

    public static int compare(String string, String string2) {
        return StringUtils.compare(string, string2, true);
    }

    public static int compare(String string, String string2, boolean bl) {
        if (string == string2) {
            return 0;
        }
        if (string == null) {
            return bl ? -1 : 1;
        }
        if (string2 == null) {
            return bl ? 1 : -1;
        }
        return string.compareTo(string2);
    }

    public static int compareIgnoreCase(String string, String string2) {
        return StringUtils.compareIgnoreCase(string, string2, true);
    }

    public static int compareIgnoreCase(String string, String string2, boolean bl) {
        if (string == string2) {
            return 0;
        }
        if (string == null) {
            return bl ? -1 : 1;
        }
        if (string2 == null) {
            return bl ? 1 : -1;
        }
        return string.compareToIgnoreCase(string2);
    }

    public static boolean contains(CharSequence charSequence, CharSequence charSequence2) {
        if (charSequence == null || charSequence2 == null) {
            return false;
        }
        return CharSequenceUtils.indexOf(charSequence, charSequence2, 0) >= 0;
    }

    public static boolean contains(CharSequence charSequence, int n) {
        if (StringUtils.isEmpty(charSequence)) {
            return false;
        }
        return CharSequenceUtils.indexOf(charSequence, n, 0) >= 0;
    }

    public static boolean containsAny(CharSequence charSequence, char ... cArray) {
        if (StringUtils.isEmpty(charSequence) || ArrayUtils.isEmpty(cArray)) {
            return false;
        }
        int n = charSequence.length();
        int n2 = cArray.length;
        int n3 = n - 1;
        int n4 = n2 - 1;
        for (int i = 0; i < n; ++i) {
            char c2 = charSequence.charAt(i);
            for (int j = 0; j < n2; ++j) {
                if (cArray[j] != c2) continue;
                if (!Character.isHighSurrogate(c2)) {
                    return true;
                }
                if (j == n4) {
                    return true;
                }
                if (i >= n3 || cArray[j + 1] != charSequence.charAt(i + 1)) continue;
                return true;
            }
        }
        return false;
    }

    public static boolean containsAny(CharSequence charSequence, CharSequence charSequence2) {
        if (charSequence2 == null) {
            return false;
        }
        return StringUtils.containsAny(charSequence, CharSequenceUtils.toCharArray(charSequence2));
    }

    public static boolean containsAny(CharSequence charSequence, CharSequence ... charSequenceArray) {
        return StringUtils.containsAny(StringUtils::contains, charSequence, charSequenceArray);
    }

    private static boolean containsAny(ToBooleanBiFunction<CharSequence, CharSequence> toBooleanBiFunction, CharSequence charSequence, CharSequence ... charSequenceArray) {
        if (StringUtils.isEmpty(charSequence) || ArrayUtils.isEmpty(charSequenceArray)) {
            return false;
        }
        for (CharSequence charSequence2 : charSequenceArray) {
            if (!toBooleanBiFunction.applyAsBoolean(charSequence, charSequence2)) continue;
            return true;
        }
        return false;
    }

    public static boolean containsAnyIgnoreCase(CharSequence charSequence, CharSequence ... charSequenceArray) {
        return StringUtils.containsAny(StringUtils::containsIgnoreCase, charSequence, charSequenceArray);
    }

    public static boolean containsIgnoreCase(CharSequence charSequence, CharSequence charSequence2) {
        if (charSequence == null || charSequence2 == null) {
            return false;
        }
        int n = charSequence2.length();
        int n2 = charSequence.length() - n;
        for (int i = 0; i <= n2; ++i) {
            if (!CharSequenceUtils.regionMatches(charSequence, true, i, charSequence2, 0, n)) continue;
            return true;
        }
        return false;
    }

    public static boolean containsNone(CharSequence charSequence, char ... cArray) {
        if (charSequence == null || cArray == null) {
            return true;
        }
        int n = charSequence.length();
        int n2 = n - 1;
        int n3 = cArray.length;
        int n4 = n3 - 1;
        for (int i = 0; i < n; ++i) {
            char c2 = charSequence.charAt(i);
            for (int j = 0; j < n3; ++j) {
                if (cArray[j] != c2) continue;
                if (!Character.isHighSurrogate(c2)) {
                    return false;
                }
                if (j == n4) {
                    return false;
                }
                if (i >= n2 || cArray[j + 1] != charSequence.charAt(i + 1)) continue;
                return false;
            }
        }
        return true;
    }

    public static boolean containsNone(CharSequence charSequence, String string) {
        if (string == null) {
            return true;
        }
        return StringUtils.containsNone(charSequence, string.toCharArray());
    }

    public static boolean containsOnly(CharSequence charSequence, char ... cArray) {
        if (cArray == null || charSequence == null) {
            return false;
        }
        if (charSequence.length() == 0) {
            return true;
        }
        if (cArray.length == 0) {
            return false;
        }
        return StringUtils.indexOfAnyBut(charSequence, cArray) == -1;
    }

    public static boolean containsOnly(CharSequence charSequence, String string) {
        if (charSequence == null || string == null) {
            return false;
        }
        return StringUtils.containsOnly(charSequence, string.toCharArray());
    }

    public static boolean containsWhitespace(CharSequence charSequence) {
        if (StringUtils.isEmpty(charSequence)) {
            return false;
        }
        int n = charSequence.length();
        for (int i = 0; i < n; ++i) {
            if (!Character.isWhitespace(charSequence.charAt(i))) continue;
            return true;
        }
        return false;
    }

    private static void convertRemainingAccentCharacters(StringBuilder stringBuilder) {
        block17: for (int i = 0; i < stringBuilder.length(); ++i) {
            char c2 = stringBuilder.charAt(i);
            switch (c2) {
                case '\u0141': {
                    stringBuilder.setCharAt(i, 'L');
                    continue block17;
                }
                case '\u0142': {
                    stringBuilder.setCharAt(i, 'l');
                    continue block17;
                }
                case '\u0110': {
                    stringBuilder.setCharAt(i, 'D');
                    continue block17;
                }
                case '\u0111': {
                    stringBuilder.setCharAt(i, 'd');
                    continue block17;
                }
                case '\u0197': {
                    stringBuilder.setCharAt(i, 'I');
                    continue block17;
                }
                case '\u0268': {
                    stringBuilder.setCharAt(i, 'i');
                    continue block17;
                }
                case '\u1d7b': {
                    stringBuilder.setCharAt(i, 'I');
                    continue block17;
                }
                case '\u1da4': {
                    stringBuilder.setCharAt(i, 'i');
                    continue block17;
                }
                case '\u1da7': {
                    stringBuilder.setCharAt(i, 'I');
                    continue block17;
                }
                case '\u0244': {
                    stringBuilder.setCharAt(i, 'U');
                    continue block17;
                }
                case '\u0289': {
                    stringBuilder.setCharAt(i, 'u');
                    continue block17;
                }
                case '\u1d7e': {
                    stringBuilder.setCharAt(i, 'U');
                    continue block17;
                }
                case '\u1db6': {
                    stringBuilder.setCharAt(i, 'u');
                    continue block17;
                }
                case '\u0166': {
                    stringBuilder.setCharAt(i, 'T');
                    continue block17;
                }
                case '\u0167': {
                    stringBuilder.setCharAt(i, 't');
                    continue block17;
                }
            }
        }
    }

    public static int countMatches(CharSequence charSequence, char c2) {
        if (StringUtils.isEmpty(charSequence)) {
            return 0;
        }
        int n = 0;
        for (int i = 0; i < charSequence.length(); ++i) {
            if (c2 != charSequence.charAt(i)) continue;
            ++n;
        }
        return n;
    }

    public static int countMatches(CharSequence charSequence, CharSequence charSequence2) {
        if (StringUtils.isEmpty(charSequence) || StringUtils.isEmpty(charSequence2)) {
            return 0;
        }
        int n = 0;
        int n2 = 0;
        while ((n2 = CharSequenceUtils.indexOf(charSequence, charSequence2, n2)) != -1) {
            ++n;
            n2 += charSequence2.length();
        }
        return n;
    }

    public static <T extends CharSequence> T defaultIfBlank(T t, T t2) {
        return StringUtils.isBlank(t) ? t2 : t;
    }

    public static <T extends CharSequence> T defaultIfEmpty(T t, T t2) {
        return StringUtils.isEmpty(t) ? t2 : t;
    }

    public static String defaultString(String string) {
        return Objects.toString(string, EMPTY);
    }

    @Deprecated
    public static String defaultString(String string, String string2) {
        return Objects.toString(string, string2);
    }

    public static String deleteWhitespace(String string) {
        if (StringUtils.isEmpty(string)) {
            return string;
        }
        int n = string.length();
        char[] cArray = new char[n];
        int n2 = 0;
        for (int i = 0; i < n; ++i) {
            if (Character.isWhitespace(string.charAt(i))) continue;
            cArray[n2++] = string.charAt(i);
        }
        if (n2 == n) {
            return string;
        }
        if (n2 == 0) {
            return EMPTY;
        }
        return new String(cArray, 0, n2);
    }

    public static String difference(String string, String string2) {
        if (string == null) {
            return string2;
        }
        if (string2 == null) {
            return string;
        }
        int n = StringUtils.indexOfDifference((CharSequence)string, (CharSequence)string2);
        if (n == -1) {
            return EMPTY;
        }
        return string2.substring(n);
    }

    public static boolean endsWith(CharSequence charSequence, CharSequence charSequence2) {
        return StringUtils.endsWith(charSequence, charSequence2, false);
    }

    private static boolean endsWith(CharSequence charSequence, CharSequence charSequence2, boolean bl) {
        if (charSequence == null || charSequence2 == null) {
            return charSequence == charSequence2;
        }
        if (charSequence2.length() > charSequence.length()) {
            return false;
        }
        int n = charSequence.length() - charSequence2.length();
        return CharSequenceUtils.regionMatches(charSequence, bl, n, charSequence2, 0, charSequence2.length());
    }

    public static boolean endsWithAny(CharSequence charSequence, CharSequence ... charSequenceArray) {
        if (StringUtils.isEmpty(charSequence) || ArrayUtils.isEmpty(charSequenceArray)) {
            return false;
        }
        for (CharSequence charSequence2 : charSequenceArray) {
            if (!StringUtils.endsWith(charSequence, charSequence2)) continue;
            return true;
        }
        return false;
    }

    public static boolean endsWithIgnoreCase(CharSequence charSequence, CharSequence charSequence2) {
        return StringUtils.endsWith(charSequence, charSequence2, true);
    }

    public static boolean equals(CharSequence charSequence, CharSequence charSequence2) {
        if (charSequence == charSequence2) {
            return true;
        }
        if (charSequence == null || charSequence2 == null) {
            return false;
        }
        if (charSequence.length() != charSequence2.length()) {
            return false;
        }
        if (charSequence instanceof String && charSequence2 instanceof String) {
            return charSequence.equals(charSequence2);
        }
        int n = charSequence.length();
        for (int i = 0; i < n; ++i) {
            if (charSequence.charAt(i) == charSequence2.charAt(i)) continue;
            return false;
        }
        return true;
    }

    public static boolean equalsAny(CharSequence charSequence, CharSequence ... charSequenceArray) {
        if (ArrayUtils.isNotEmpty(charSequenceArray)) {
            for (CharSequence charSequence2 : charSequenceArray) {
                if (!StringUtils.equals(charSequence, charSequence2)) continue;
                return true;
            }
        }
        return false;
    }

    public static boolean equalsAnyIgnoreCase(CharSequence charSequence, CharSequence ... charSequenceArray) {
        if (ArrayUtils.isNotEmpty(charSequenceArray)) {
            for (CharSequence charSequence2 : charSequenceArray) {
                if (!StringUtils.equalsIgnoreCase(charSequence, charSequence2)) continue;
                return true;
            }
        }
        return false;
    }

    public static boolean equalsIgnoreCase(CharSequence charSequence, CharSequence charSequence2) {
        if (charSequence == charSequence2) {
            return true;
        }
        if (charSequence == null || charSequence2 == null) {
            return false;
        }
        if (charSequence.length() != charSequence2.length()) {
            return false;
        }
        return CharSequenceUtils.regionMatches(charSequence, true, 0, charSequence2, 0, charSequence.length());
    }

    @SafeVarargs
    public static <T extends CharSequence> T firstNonBlank(T ... TArray) {
        if (TArray != null) {
            for (T t : TArray) {
                if (!StringUtils.isNotBlank(t)) continue;
                return t;
            }
        }
        return null;
    }

    @SafeVarargs
    public static <T extends CharSequence> T firstNonEmpty(T ... TArray) {
        if (TArray != null) {
            for (T t : TArray) {
                if (!StringUtils.isNotEmpty(t)) continue;
                return t;
            }
        }
        return null;
    }

    public static byte[] getBytes(String string, Charset charset) {
        return string == null ? ArrayUtils.EMPTY_BYTE_ARRAY : string.getBytes(Charsets.toCharset(charset));
    }

    public static byte[] getBytes(String string, String string2) {
        return string == null ? ArrayUtils.EMPTY_BYTE_ARRAY : string.getBytes(Charsets.toCharsetName(string2));
    }

    public static String getCommonPrefix(String ... stringArray) {
        if (ArrayUtils.isEmpty(stringArray)) {
            return EMPTY;
        }
        int n = StringUtils.indexOfDifference(stringArray);
        if (n == -1) {
            if (stringArray[0] == null) {
                return EMPTY;
            }
            return stringArray[0];
        }
        if (n == 0) {
            return EMPTY;
        }
        return stringArray[0].substring(0, n);
    }

    public static String getDigits(String string) {
        if (StringUtils.isEmpty(string)) {
            return string;
        }
        int n = string.length();
        StringBuilder stringBuilder = new StringBuilder(n);
        for (int i = 0; i < n; ++i) {
            char c2 = string.charAt(i);
            if (!Character.isDigit(c2)) continue;
            stringBuilder.append(c2);
        }
        return stringBuilder.toString();
    }

    @Deprecated
    public static int getFuzzyDistance(CharSequence charSequence, CharSequence charSequence2, Locale locale) {
        if (charSequence == null || charSequence2 == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }
        if (locale == null) {
            throw new IllegalArgumentException("Locale must not be null");
        }
        String string = charSequence.toString().toLowerCase(locale);
        String string2 = charSequence2.toString().toLowerCase(locale);
        int n = 0;
        int n2 = 0;
        int n3 = Integer.MIN_VALUE;
        for (int i = 0; i < string2.length(); ++i) {
            char c2 = string2.charAt(i);
            boolean bl = false;
            while (n2 < string.length() && !bl) {
                char c3 = string.charAt(n2);
                if (c2 == c3) {
                    ++n;
                    if (n3 + 1 == n2) {
                        n += 2;
                    }
                    n3 = n2;
                    bl = true;
                }
                ++n2;
            }
        }
        return n;
    }

    public static <T extends CharSequence> T getIfBlank(T t, Supplier<T> supplier) {
        return (T)(StringUtils.isBlank(t) ? (CharSequence)Suppliers.get(supplier) : t);
    }

    public static <T extends CharSequence> T getIfEmpty(T t, Supplier<T> supplier) {
        return (T)(StringUtils.isEmpty(t) ? (CharSequence)Suppliers.get(supplier) : t);
    }

    @Deprecated
    public static double getJaroWinklerDistance(CharSequence charSequence, CharSequence charSequence2) {
        double d = 0.1;
        if (charSequence == null || charSequence2 == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }
        int[] nArray = StringUtils.matches(charSequence, charSequence2);
        double d2 = nArray[0];
        if (d2 == 0.0) {
            return 0.0;
        }
        double d3 = (d2 / (double)charSequence.length() + d2 / (double)charSequence2.length() + (d2 - (double)nArray[1]) / d2) / 3.0;
        double d4 = d3 < 0.7 ? d3 : d3 + Math.min(0.1, 1.0 / (double)nArray[3]) * (double)nArray[2] * (1.0 - d3);
        return (double)Math.round(d4 * 100.0) / 100.0;
    }

    @Deprecated
    public static int getLevenshteinDistance(CharSequence charSequence, CharSequence charSequence2) {
        int n;
        Object object;
        if (charSequence == null || charSequence2 == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }
        int n2 = charSequence.length();
        int n3 = charSequence2.length();
        if (n2 == 0) {
            return n3;
        }
        if (n3 == 0) {
            return n2;
        }
        if (n2 > n3) {
            object = charSequence;
            charSequence = charSequence2;
            charSequence2 = object;
            n2 = n3;
            n3 = charSequence2.length();
        }
        object = new int[n2 + 1];
        for (n = 0; n <= n2; ++n) {
            object[n] = n;
        }
        for (int i = 1; i <= n3; ++i) {
            Object object2 = object[0];
            char c2 = charSequence2.charAt(i - 1);
            object[0] = i;
            for (n = 1; n <= n2; ++n) {
                Object object3 = object[n];
                boolean bl = charSequence.charAt(n - 1) != c2;
                object[n] = Math.min(Math.min((int)(object[n - 1] + true), (int)(object[n] + true)), (int)(object2 + bl));
                object2 = object3;
            }
        }
        return (int)object[n2];
    }

    @Deprecated
    public static int getLevenshteinDistance(CharSequence charSequence, CharSequence charSequence2, int n) {
        int n2;
        Object object;
        if (charSequence == null || charSequence2 == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }
        if (n < 0) {
            throw new IllegalArgumentException("Threshold must not be negative");
        }
        int n3 = charSequence.length();
        int n4 = charSequence2.length();
        if (n3 == 0) {
            return n4 <= n ? n4 : -1;
        }
        if (n4 == 0) {
            return n3 <= n ? n3 : -1;
        }
        if (Math.abs(n3 - n4) > n) {
            return -1;
        }
        if (n3 > n4) {
            object = charSequence;
            charSequence = charSequence2;
            charSequence2 = object;
            n3 = n4;
            n4 = charSequence2.length();
        }
        object = new int[n3 + 1];
        Object object2 = new int[n3 + 1];
        int n5 = Math.min(n3, n) + 1;
        for (n2 = 0; n2 < n5; ++n2) {
            object[n2] = n2;
        }
        Arrays.fill((int[])object, n5, ((Object)object).length, Integer.MAX_VALUE);
        Arrays.fill(object2, Integer.MAX_VALUE);
        for (n2 = 1; n2 <= n4; ++n2) {
            int n6;
            char c2 = charSequence2.charAt(n2 - 1);
            object2[0] = n2;
            int n7 = Math.max(1, n2 - n);
            int n8 = n6 = n2 > Integer.MAX_VALUE - n ? n3 : Math.min(n3, n2 + n);
            if (n7 > n6) {
                return -1;
            }
            if (n7 > 1) {
                object2[n7 - 1] = Integer.MAX_VALUE;
            }
            for (int i = n7; i <= n6; ++i) {
                object2[i] = charSequence.charAt(i - 1) == c2 ? (int)object[i - 1] : 1 + Math.min(Math.min(object2[i - 1], (int)object[i]), (int)object[i - 1]);
            }
            Object object3 = object;
            object = object2;
            object2 = object3;
        }
        if (object[n3] <= n) {
            return (int)object[n3];
        }
        return -1;
    }

    public static int indexOf(CharSequence charSequence, CharSequence charSequence2) {
        if (charSequence == null || charSequence2 == null) {
            return -1;
        }
        return CharSequenceUtils.indexOf(charSequence, charSequence2, 0);
    }

    public static int indexOf(CharSequence charSequence, CharSequence charSequence2, int n) {
        if (charSequence == null || charSequence2 == null) {
            return -1;
        }
        return CharSequenceUtils.indexOf(charSequence, charSequence2, n);
    }

    public static int indexOf(CharSequence charSequence, int n) {
        if (StringUtils.isEmpty(charSequence)) {
            return -1;
        }
        return CharSequenceUtils.indexOf(charSequence, n, 0);
    }

    public static int indexOf(CharSequence charSequence, int n, int n2) {
        if (StringUtils.isEmpty(charSequence)) {
            return -1;
        }
        return CharSequenceUtils.indexOf(charSequence, n, n2);
    }

    public static int indexOfAny(CharSequence charSequence, char ... cArray) {
        if (StringUtils.isEmpty(charSequence) || ArrayUtils.isEmpty(cArray)) {
            return -1;
        }
        int n = charSequence.length();
        int n2 = n - 1;
        int n3 = cArray.length;
        int n4 = n3 - 1;
        for (int i = 0; i < n; ++i) {
            char c2 = charSequence.charAt(i);
            for (int j = 0; j < n3; ++j) {
                if (cArray[j] != c2) continue;
                if (i >= n2 || j >= n4 || !Character.isHighSurrogate(c2)) {
                    return i;
                }
                if (cArray[j + 1] != charSequence.charAt(i + 1)) continue;
                return i;
            }
        }
        return -1;
    }

    public static int indexOfAny(CharSequence charSequence, CharSequence ... charSequenceArray) {
        if (charSequence == null || charSequenceArray == null) {
            return -1;
        }
        int n = Integer.MAX_VALUE;
        for (CharSequence charSequence2 : charSequenceArray) {
            int n2;
            if (charSequence2 == null || (n2 = CharSequenceUtils.indexOf(charSequence, charSequence2, 0)) == -1 || n2 >= n) continue;
            n = n2;
        }
        return n == Integer.MAX_VALUE ? -1 : n;
    }

    public static int indexOfAny(CharSequence charSequence, String string) {
        if (StringUtils.isEmpty(charSequence) || StringUtils.isEmpty(string)) {
            return -1;
        }
        return StringUtils.indexOfAny(charSequence, string.toCharArray());
    }

    public static int indexOfAnyBut(CharSequence charSequence, char ... cArray) {
        if (StringUtils.isEmpty(charSequence) || ArrayUtils.isEmpty(cArray)) {
            return -1;
        }
        int n = charSequence.length();
        int n2 = n - 1;
        int n3 = cArray.length;
        int n4 = n3 - 1;
        block0: for (int i = 0; i < n; ++i) {
            char c2 = charSequence.charAt(i);
            for (int j = 0; j < n3; ++j) {
                if (cArray[j] == c2 && (i >= n2 || j >= n4 || !Character.isHighSurrogate(c2) || cArray[j + 1] == charSequence.charAt(i + 1))) continue block0;
            }
            return i;
        }
        return -1;
    }

    public static int indexOfAnyBut(CharSequence charSequence, CharSequence charSequence2) {
        if (StringUtils.isEmpty(charSequence) || StringUtils.isEmpty(charSequence2)) {
            return -1;
        }
        int n = charSequence.length();
        for (int i = 0; i < n; ++i) {
            boolean bl;
            char c2 = charSequence.charAt(i);
            boolean bl2 = bl = CharSequenceUtils.indexOf(charSequence2, c2, 0) >= 0;
            if (i + 1 < n && Character.isHighSurrogate(c2)) {
                char c3 = charSequence.charAt(i + 1);
                if (!bl || CharSequenceUtils.indexOf(charSequence2, c3, 0) >= 0) continue;
                return i;
            }
            if (bl) continue;
            return i;
        }
        return -1;
    }

    public static int indexOfDifference(CharSequence ... charSequenceArray) {
        int n;
        if (ArrayUtils.getLength(charSequenceArray) <= 1) {
            return -1;
        }
        boolean bl = false;
        boolean bl2 = true;
        int n2 = charSequenceArray.length;
        int n3 = Integer.MAX_VALUE;
        int n4 = 0;
        CharSequence[] charSequenceArray2 = charSequenceArray;
        int n5 = charSequenceArray2.length;
        for (n = 0; n < n5; ++n) {
            CharSequence charSequence = charSequenceArray2[n];
            if (charSequence == null) {
                bl = true;
                n3 = 0;
                continue;
            }
            bl2 = false;
            n3 = Math.min(charSequence.length(), n3);
            n4 = Math.max(charSequence.length(), n4);
        }
        if (bl2 || n4 == 0 && !bl) {
            return -1;
        }
        if (n3 == 0) {
            return 0;
        }
        int n6 = -1;
        for (n5 = 0; n5 < n3; ++n5) {
            n = charSequenceArray[0].charAt(n5);
            for (int i = 1; i < n2; ++i) {
                if (charSequenceArray[i].charAt(n5) == n) continue;
                n6 = n5;
                break;
            }
            if (n6 != -1) break;
        }
        if (n6 == -1 && n3 != n4) {
            return n3;
        }
        return n6;
    }

    public static int indexOfDifference(CharSequence charSequence, CharSequence charSequence2) {
        int n;
        if (charSequence == charSequence2) {
            return -1;
        }
        if (charSequence == null || charSequence2 == null) {
            return 0;
        }
        for (n = 0; n < charSequence.length() && n < charSequence2.length() && charSequence.charAt(n) == charSequence2.charAt(n); ++n) {
        }
        if (n < charSequence2.length() || n < charSequence.length()) {
            return n;
        }
        return -1;
    }

    public static int indexOfIgnoreCase(CharSequence charSequence, CharSequence charSequence2) {
        return StringUtils.indexOfIgnoreCase(charSequence, charSequence2, 0);
    }

    public static int indexOfIgnoreCase(CharSequence charSequence, CharSequence charSequence2, int n) {
        int n2;
        if (charSequence == null || charSequence2 == null) {
            return -1;
        }
        if (n < 0) {
            n = 0;
        }
        if (n > (n2 = charSequence.length() - charSequence2.length() + 1)) {
            return -1;
        }
        if (charSequence2.length() == 0) {
            return n;
        }
        for (int i = n; i < n2; ++i) {
            if (!CharSequenceUtils.regionMatches(charSequence, true, i, charSequence2, 0, charSequence2.length())) continue;
            return i;
        }
        return -1;
    }

    public static boolean isAllBlank(CharSequence ... charSequenceArray) {
        if (ArrayUtils.isEmpty(charSequenceArray)) {
            return true;
        }
        for (CharSequence charSequence : charSequenceArray) {
            if (!StringUtils.isNotBlank(charSequence)) continue;
            return false;
        }
        return true;
    }

    public static boolean isAllEmpty(CharSequence ... charSequenceArray) {
        if (ArrayUtils.isEmpty(charSequenceArray)) {
            return true;
        }
        for (CharSequence charSequence : charSequenceArray) {
            if (!StringUtils.isNotEmpty(charSequence)) continue;
            return false;
        }
        return true;
    }

    public static boolean isAllLowerCase(CharSequence charSequence) {
        if (StringUtils.isEmpty(charSequence)) {
            return false;
        }
        int n = charSequence.length();
        for (int i = 0; i < n; ++i) {
            if (Character.isLowerCase(charSequence.charAt(i))) continue;
            return false;
        }
        return true;
    }

    public static boolean isAllUpperCase(CharSequence charSequence) {
        if (StringUtils.isEmpty(charSequence)) {
            return false;
        }
        int n = charSequence.length();
        for (int i = 0; i < n; ++i) {
            if (Character.isUpperCase(charSequence.charAt(i))) continue;
            return false;
        }
        return true;
    }

    public static boolean isAlpha(CharSequence charSequence) {
        if (StringUtils.isEmpty(charSequence)) {
            return false;
        }
        int n = charSequence.length();
        for (int i = 0; i < n; ++i) {
            if (Character.isLetter(charSequence.charAt(i))) continue;
            return false;
        }
        return true;
    }

    public static boolean isAlphanumeric(CharSequence charSequence) {
        if (StringUtils.isEmpty(charSequence)) {
            return false;
        }
        int n = charSequence.length();
        for (int i = 0; i < n; ++i) {
            if (Character.isLetterOrDigit(charSequence.charAt(i))) continue;
            return false;
        }
        return true;
    }

    public static boolean isAlphanumericSpace(CharSequence charSequence) {
        if (charSequence == null) {
            return false;
        }
        int n = charSequence.length();
        for (int i = 0; i < n; ++i) {
            char c2 = charSequence.charAt(i);
            if (c2 == ' ' || Character.isLetterOrDigit(c2)) continue;
            return false;
        }
        return true;
    }

    public static boolean isAlphaSpace(CharSequence charSequence) {
        if (charSequence == null) {
            return false;
        }
        int n = charSequence.length();
        for (int i = 0; i < n; ++i) {
            char c2 = charSequence.charAt(i);
            if (c2 == ' ' || Character.isLetter(c2)) continue;
            return false;
        }
        return true;
    }

    public static boolean isAnyBlank(CharSequence ... charSequenceArray) {
        if (ArrayUtils.isEmpty(charSequenceArray)) {
            return false;
        }
        for (CharSequence charSequence : charSequenceArray) {
            if (!StringUtils.isBlank(charSequence)) continue;
            return true;
        }
        return false;
    }

    public static boolean isAnyEmpty(CharSequence ... charSequenceArray) {
        if (ArrayUtils.isEmpty(charSequenceArray)) {
            return false;
        }
        for (CharSequence charSequence : charSequenceArray) {
            if (!StringUtils.isEmpty(charSequence)) continue;
            return true;
        }
        return false;
    }

    public static boolean isAsciiPrintable(CharSequence charSequence) {
        if (charSequence == null) {
            return false;
        }
        int n = charSequence.length();
        for (int i = 0; i < n; ++i) {
            if (CharUtils.isAsciiPrintable(charSequence.charAt(i))) continue;
            return false;
        }
        return true;
    }

    public static boolean isBlank(CharSequence charSequence) {
        int n = StringUtils.length(charSequence);
        if (n == 0) {
            return true;
        }
        for (int i = 0; i < n; ++i) {
            if (Character.isWhitespace(charSequence.charAt(i))) continue;
            return false;
        }
        return true;
    }

    public static boolean isEmpty(CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }

    public static boolean isMixedCase(CharSequence charSequence) {
        if (StringUtils.isEmpty(charSequence) || charSequence.length() == 1) {
            return false;
        }
        boolean bl = false;
        boolean bl2 = false;
        int n = charSequence.length();
        for (int i = 0; i < n; ++i) {
            char c2 = charSequence.charAt(i);
            if (Character.isUpperCase(c2)) {
                bl = true;
            } else if (Character.isLowerCase(c2)) {
                bl2 = true;
            }
            if (!bl || !bl2) continue;
            return true;
        }
        return false;
    }

    public static boolean isNoneBlank(CharSequence ... charSequenceArray) {
        return !StringUtils.isAnyBlank(charSequenceArray);
    }

    public static boolean isNoneEmpty(CharSequence ... charSequenceArray) {
        return !StringUtils.isAnyEmpty(charSequenceArray);
    }

    public static boolean isNotBlank(CharSequence charSequence) {
        return !StringUtils.isBlank(charSequence);
    }

    public static boolean isNotEmpty(CharSequence charSequence) {
        return !StringUtils.isEmpty(charSequence);
    }

    public static boolean isNumeric(CharSequence charSequence) {
        if (StringUtils.isEmpty(charSequence)) {
            return false;
        }
        int n = charSequence.length();
        for (int i = 0; i < n; ++i) {
            if (Character.isDigit(charSequence.charAt(i))) continue;
            return false;
        }
        return true;
    }

    public static boolean isNumericSpace(CharSequence charSequence) {
        if (charSequence == null) {
            return false;
        }
        int n = charSequence.length();
        for (int i = 0; i < n; ++i) {
            char c2 = charSequence.charAt(i);
            if (c2 == ' ' || Character.isDigit(c2)) continue;
            return false;
        }
        return true;
    }

    public static boolean isWhitespace(CharSequence charSequence) {
        if (charSequence == null) {
            return false;
        }
        int n = charSequence.length();
        for (int i = 0; i < n; ++i) {
            if (Character.isWhitespace(charSequence.charAt(i))) continue;
            return false;
        }
        return true;
    }

    public static String join(boolean[] blArray, char c2) {
        if (blArray == null) {
            return null;
        }
        return StringUtils.join(blArray, c2, 0, blArray.length);
    }

    public static String join(boolean[] blArray, char c2, int n, int n2) {
        if (blArray == null) {
            return null;
        }
        if (n2 - n <= 0) {
            return EMPTY;
        }
        StringBuilder stringBuilder = new StringBuilder(blArray.length * 5 + blArray.length - 1);
        for (int i = n; i < n2; ++i) {
            stringBuilder.append(blArray[i]).append(c2);
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    public static String join(byte[] byArray, char c2) {
        if (byArray == null) {
            return null;
        }
        return StringUtils.join(byArray, c2, 0, byArray.length);
    }

    public static String join(byte[] byArray, char c2, int n, int n2) {
        if (byArray == null) {
            return null;
        }
        if (n2 - n <= 0) {
            return EMPTY;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = n; i < n2; ++i) {
            stringBuilder.append(byArray[i]).append(c2);
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    public static String join(char[] cArray, char c2) {
        if (cArray == null) {
            return null;
        }
        return StringUtils.join(cArray, c2, 0, cArray.length);
    }

    public static String join(char[] cArray, char c2, int n, int n2) {
        if (cArray == null) {
            return null;
        }
        if (n2 - n <= 0) {
            return EMPTY;
        }
        StringBuilder stringBuilder = new StringBuilder(cArray.length * 2 - 1);
        for (int i = n; i < n2; ++i) {
            stringBuilder.append(cArray[i]).append(c2);
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    public static String join(double[] dArray, char c2) {
        if (dArray == null) {
            return null;
        }
        return StringUtils.join(dArray, c2, 0, dArray.length);
    }

    public static String join(double[] dArray, char c2, int n, int n2) {
        if (dArray == null) {
            return null;
        }
        if (n2 - n <= 0) {
            return EMPTY;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = n; i < n2; ++i) {
            stringBuilder.append(dArray[i]).append(c2);
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    public static String join(float[] fArray, char c2) {
        if (fArray == null) {
            return null;
        }
        return StringUtils.join(fArray, c2, 0, fArray.length);
    }

    public static String join(float[] fArray, char c2, int n, int n2) {
        if (fArray == null) {
            return null;
        }
        if (n2 - n <= 0) {
            return EMPTY;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = n; i < n2; ++i) {
            stringBuilder.append(fArray[i]).append(c2);
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    public static String join(int[] nArray, char c2) {
        if (nArray == null) {
            return null;
        }
        return StringUtils.join(nArray, c2, 0, nArray.length);
    }

    public static String join(int[] nArray, char c2, int n, int n2) {
        if (nArray == null) {
            return null;
        }
        if (n2 - n <= 0) {
            return EMPTY;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = n; i < n2; ++i) {
            stringBuilder.append(nArray[i]).append(c2);
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    public static String join(Iterable<?> iterable, char c2) {
        return iterable != null ? StringUtils.join(iterable.iterator(), c2) : null;
    }

    public static String join(Iterable<?> iterable, String string) {
        return iterable != null ? StringUtils.join(iterable.iterator(), string) : null;
    }

    public static String join(Iterator<?> iterator, char c2) {
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return EMPTY;
        }
        return Streams.of(iterator).collect(LangCollectors.joining(StringUtils.toStringOrEmpty(String.valueOf(c2)), EMPTY, EMPTY, StringUtils::toStringOrEmpty));
    }

    public static String join(Iterator<?> iterator, String string) {
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return EMPTY;
        }
        return Streams.of(iterator).collect(LangCollectors.joining(StringUtils.toStringOrEmpty(string), EMPTY, EMPTY, StringUtils::toStringOrEmpty));
    }

    public static String join(List<?> list, char c2, int n, int n2) {
        if (list == null) {
            return null;
        }
        int n3 = n2 - n;
        if (n3 <= 0) {
            return EMPTY;
        }
        List<?> list2 = list.subList(n, n2);
        return StringUtils.join(list2.iterator(), c2);
    }

    public static String join(List<?> list, String string, int n, int n2) {
        if (list == null) {
            return null;
        }
        int n3 = n2 - n;
        if (n3 <= 0) {
            return EMPTY;
        }
        List<?> list2 = list.subList(n, n2);
        return StringUtils.join(list2.iterator(), string);
    }

    public static String join(long[] lArray, char c2) {
        if (lArray == null) {
            return null;
        }
        return StringUtils.join(lArray, c2, 0, lArray.length);
    }

    public static String join(long[] lArray, char c2, int n, int n2) {
        if (lArray == null) {
            return null;
        }
        if (n2 - n <= 0) {
            return EMPTY;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = n; i < n2; ++i) {
            stringBuilder.append(lArray[i]).append(c2);
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    public static String join(Object[] objectArray, char c2) {
        if (objectArray == null) {
            return null;
        }
        return StringUtils.join(objectArray, c2, 0, objectArray.length);
    }

    public static String join(Object[] objectArray, char c2, int n, int n2) {
        return StringUtils.join(objectArray, String.valueOf(c2), n, n2);
    }

    public static String join(Object[] objectArray, String string) {
        return objectArray != null ? StringUtils.join(objectArray, StringUtils.toStringOrEmpty(string), 0, objectArray.length) : null;
    }

    public static String join(Object[] objectArray, String string, int n, int n2) {
        return objectArray != null ? Streams.of(objectArray).skip(n).limit(Math.max(0, n2 - n)).collect(LangCollectors.joining(string, EMPTY, EMPTY, StringUtils::toStringOrEmpty)) : null;
    }

    public static String join(short[] sArray, char c2) {
        if (sArray == null) {
            return null;
        }
        return StringUtils.join(sArray, c2, 0, sArray.length);
    }

    public static String join(short[] sArray, char c2, int n, int n2) {
        if (sArray == null) {
            return null;
        }
        if (n2 - n <= 0) {
            return EMPTY;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = n; i < n2; ++i) {
            stringBuilder.append(sArray[i]).append(c2);
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    @SafeVarargs
    public static <T> String join(T ... TArray) {
        return StringUtils.join((Object[])TArray, null);
    }

    public static String joinWith(String string, Object ... objectArray) {
        if (objectArray == null) {
            throw new IllegalArgumentException("Object varargs must not be null");
        }
        return StringUtils.join(objectArray, string);
    }

    public static int lastIndexOf(CharSequence charSequence, CharSequence charSequence2) {
        if (charSequence == null) {
            return -1;
        }
        return CharSequenceUtils.lastIndexOf(charSequence, charSequence2, charSequence.length());
    }

    public static int lastIndexOf(CharSequence charSequence, CharSequence charSequence2, int n) {
        return CharSequenceUtils.lastIndexOf(charSequence, charSequence2, n);
    }

    public static int lastIndexOf(CharSequence charSequence, int n) {
        if (StringUtils.isEmpty(charSequence)) {
            return -1;
        }
        return CharSequenceUtils.lastIndexOf(charSequence, n, charSequence.length());
    }

    public static int lastIndexOf(CharSequence charSequence, int n, int n2) {
        if (StringUtils.isEmpty(charSequence)) {
            return -1;
        }
        return CharSequenceUtils.lastIndexOf(charSequence, n, n2);
    }

    public static int lastIndexOfAny(CharSequence charSequence, CharSequence ... charSequenceArray) {
        if (charSequence == null || charSequenceArray == null) {
            return -1;
        }
        int n = -1;
        for (CharSequence charSequence2 : charSequenceArray) {
            int n2;
            if (charSequence2 == null || (n2 = CharSequenceUtils.lastIndexOf(charSequence, charSequence2, charSequence.length())) <= n) continue;
            n = n2;
        }
        return n;
    }

    public static int lastIndexOfIgnoreCase(CharSequence charSequence, CharSequence charSequence2) {
        if (charSequence == null || charSequence2 == null) {
            return -1;
        }
        return StringUtils.lastIndexOfIgnoreCase(charSequence, charSequence2, charSequence.length());
    }

    public static int lastIndexOfIgnoreCase(CharSequence charSequence, CharSequence charSequence2, int n) {
        if (charSequence == null || charSequence2 == null) {
            return -1;
        }
        int n2 = charSequence2.length();
        int n3 = charSequence.length();
        if (n > n3 - n2) {
            n = n3 - n2;
        }
        if (n < 0) {
            return -1;
        }
        if (n2 == 0) {
            return n;
        }
        for (int i = n; i >= 0; --i) {
            if (!CharSequenceUtils.regionMatches(charSequence, true, i, charSequence2, 0, n2)) continue;
            return i;
        }
        return -1;
    }

    public static int lastOrdinalIndexOf(CharSequence charSequence, CharSequence charSequence2, int n) {
        return StringUtils.ordinalIndexOf(charSequence, charSequence2, n, true);
    }

    public static String left(String string, int n) {
        if (string == null) {
            return null;
        }
        if (n < 0) {
            return EMPTY;
        }
        if (string.length() <= n) {
            return string;
        }
        return string.substring(0, n);
    }

    public static String leftPad(String string, int n) {
        return StringUtils.leftPad(string, n, ' ');
    }

    public static String leftPad(String string, int n, char c2) {
        if (string == null) {
            return null;
        }
        int n2 = n - string.length();
        if (n2 <= 0) {
            return string;
        }
        if (n2 > 8192) {
            return StringUtils.leftPad(string, n, String.valueOf(c2));
        }
        return StringUtils.repeat(c2, n2).concat(string);
    }

    public static String leftPad(String string, int n, String string2) {
        if (string == null) {
            return null;
        }
        if (StringUtils.isEmpty(string2)) {
            string2 = SPACE;
        }
        int n2 = string2.length();
        int n3 = string.length();
        int n4 = n - n3;
        if (n4 <= 0) {
            return string;
        }
        if (n2 == 1 && n4 <= 8192) {
            return StringUtils.leftPad(string, n, string2.charAt(0));
        }
        if (n4 == n2) {
            return string2.concat(string);
        }
        if (n4 < n2) {
            return string2.substring(0, n4).concat(string);
        }
        char[] cArray = new char[n4];
        char[] cArray2 = string2.toCharArray();
        for (int i = 0; i < n4; ++i) {
            cArray[i] = cArray2[i % n2];
        }
        return new String(cArray).concat(string);
    }

    public static int length(CharSequence charSequence) {
        return charSequence == null ? 0 : charSequence.length();
    }

    public static String lowerCase(String string) {
        if (string == null) {
            return null;
        }
        return string.toLowerCase();
    }

    public static String lowerCase(String string, Locale locale) {
        if (string == null) {
            return null;
        }
        return string.toLowerCase(LocaleUtils.toLocale(locale));
    }

    private static int[] matches(CharSequence charSequence, CharSequence charSequence2) {
        int n;
        int n2;
        CharSequence charSequence3;
        CharSequence charSequence4;
        if (charSequence.length() > charSequence2.length()) {
            charSequence4 = charSequence;
            charSequence3 = charSequence2;
        } else {
            charSequence4 = charSequence2;
            charSequence3 = charSequence;
        }
        int n3 = Math.max(charSequence4.length() / 2 - 1, 0);
        int[] nArray = ArrayFill.fill(new int[charSequence3.length()], -1);
        boolean[] blArray = new boolean[charSequence4.length()];
        int n4 = 0;
        block0: for (int i = 0; i < charSequence3.length(); ++i) {
            char c2 = charSequence3.charAt(i);
            n2 = Math.min(i + n3 + 1, charSequence4.length());
            for (n = Math.max(i - n3, 0); n < n2; ++n) {
                if (blArray[n] || c2 != charSequence4.charAt(n)) continue;
                nArray[i] = n;
                blArray[n] = true;
                ++n4;
                continue block0;
            }
        }
        char[] cArray = new char[n4];
        char[] cArray2 = new char[n4];
        n2 = 0;
        for (n = 0; n < charSequence3.length(); ++n) {
            if (nArray[n] == -1) continue;
            cArray[n2] = charSequence3.charAt(n);
            ++n2;
        }
        n2 = 0;
        for (n = 0; n < charSequence4.length(); ++n) {
            if (!blArray[n]) continue;
            cArray2[n2] = charSequence4.charAt(n);
            ++n2;
        }
        n = 0;
        for (n2 = 0; n2 < cArray.length; ++n2) {
            if (cArray[n2] == cArray2[n2]) continue;
            ++n;
        }
        n2 = 0;
        for (int i = 0; i < charSequence3.length() && charSequence.charAt(i) == charSequence2.charAt(i); ++i) {
            ++n2;
        }
        return new int[]{n4, n / 2, n2, charSequence4.length()};
    }

    public static String mid(String string, int n, int n2) {
        if (string == null) {
            return null;
        }
        if (n2 < 0 || n > string.length()) {
            return EMPTY;
        }
        if (n < 0) {
            n = 0;
        }
        if (string.length() <= n + n2) {
            return string.substring(n);
        }
        return string.substring(n, n + n2);
    }

    public static String normalizeSpace(String string) {
        if (StringUtils.isEmpty(string)) {
            return string;
        }
        int n = string.length();
        char[] cArray = new char[n];
        int n2 = 0;
        int n3 = 0;
        boolean bl = true;
        for (int i = 0; i < n; ++i) {
            char c2 = string.charAt(i);
            boolean bl2 = Character.isWhitespace(c2);
            if (bl2) {
                if (n3 == 0 && !bl) {
                    cArray[n2++] = SPACE.charAt(0);
                }
                ++n3;
                continue;
            }
            bl = false;
            cArray[n2++] = c2 == '\u00a0' ? 32 : (int)c2;
            n3 = 0;
        }
        if (bl) {
            return EMPTY;
        }
        return new String(cArray, 0, n2 - (n3 > 0 ? 1 : 0)).trim();
    }

    public static int ordinalIndexOf(CharSequence charSequence, CharSequence charSequence2, int n) {
        return StringUtils.ordinalIndexOf(charSequence, charSequence2, n, false);
    }

    private static int ordinalIndexOf(CharSequence charSequence, CharSequence charSequence2, int n, boolean bl) {
        if (charSequence == null || charSequence2 == null || n <= 0) {
            return -1;
        }
        if (charSequence2.length() == 0) {
            return bl ? charSequence.length() : 0;
        }
        int n2 = 0;
        int n3 = bl ? charSequence.length() : -1;
        do {
            if ((n3 = bl ? CharSequenceUtils.lastIndexOf(charSequence, charSequence2, n3 - 1) : CharSequenceUtils.indexOf(charSequence, charSequence2, n3 + 1)) >= 0) continue;
            return n3;
        } while (++n2 < n);
        return n3;
    }

    public static String overlay(String string, String string2, int n, int n2) {
        if (string == null) {
            return null;
        }
        if (string2 == null) {
            string2 = EMPTY;
        }
        int n3 = string.length();
        if (n < 0) {
            n = 0;
        }
        if (n > n3) {
            n = n3;
        }
        if (n2 < 0) {
            n2 = 0;
        }
        if (n2 > n3) {
            n2 = n3;
        }
        if (n > n2) {
            int n4 = n;
            n = n2;
            n2 = n4;
        }
        return string.substring(0, n) + string2 + string.substring(n2);
    }

    private static String prependIfMissing(String string, CharSequence charSequence, boolean bl, CharSequence ... charSequenceArray) {
        if (string == null || StringUtils.isEmpty(charSequence) || StringUtils.startsWith(string, charSequence, bl)) {
            return string;
        }
        if (ArrayUtils.isNotEmpty(charSequenceArray)) {
            for (CharSequence charSequence2 : charSequenceArray) {
                if (!StringUtils.startsWith(string, charSequence2, bl)) continue;
                return string;
            }
        }
        return charSequence + string;
    }

    public static String prependIfMissing(String string, CharSequence charSequence, CharSequence ... charSequenceArray) {
        return StringUtils.prependIfMissing(string, charSequence, false, charSequenceArray);
    }

    public static String prependIfMissingIgnoreCase(String string, CharSequence charSequence, CharSequence ... charSequenceArray) {
        return StringUtils.prependIfMissing(string, charSequence, true, charSequenceArray);
    }

    public static String remove(String string, char c2) {
        if (StringUtils.isEmpty(string) || string.indexOf(c2) == -1) {
            return string;
        }
        char[] cArray = string.toCharArray();
        int n = 0;
        for (int i = 0; i < cArray.length; ++i) {
            if (cArray[i] == c2) continue;
            cArray[n++] = cArray[i];
        }
        return new String(cArray, 0, n);
    }

    public static String remove(String string, String string2) {
        if (StringUtils.isEmpty(string) || StringUtils.isEmpty(string2)) {
            return string;
        }
        return StringUtils.replace(string, string2, EMPTY, -1);
    }

    @Deprecated
    public static String removeAll(String string, String string2) {
        return RegExUtils.removeAll(string, string2);
    }

    public static String removeEnd(String string, String string2) {
        if (StringUtils.isEmpty(string) || StringUtils.isEmpty(string2)) {
            return string;
        }
        if (string.endsWith(string2)) {
            return string.substring(0, string.length() - string2.length());
        }
        return string;
    }

    public static String removeEndIgnoreCase(String string, String string2) {
        if (StringUtils.isEmpty(string) || StringUtils.isEmpty(string2)) {
            return string;
        }
        if (StringUtils.endsWithIgnoreCase(string, string2)) {
            return string.substring(0, string.length() - string2.length());
        }
        return string;
    }

    @Deprecated
    public static String removeFirst(String string, String string2) {
        return StringUtils.replaceFirst(string, string2, EMPTY);
    }

    public static String removeIgnoreCase(String string, String string2) {
        return StringUtils.replaceIgnoreCase(string, string2, EMPTY, -1);
    }

    @Deprecated
    public static String removePattern(String string, String string2) {
        return RegExUtils.removePattern(string, string2);
    }

    public static String removeStart(String string, char c2) {
        if (StringUtils.isEmpty(string)) {
            return string;
        }
        return string.charAt(0) == c2 ? string.substring(1) : string;
    }

    public static String removeStart(String string, String string2) {
        if (StringUtils.isEmpty(string) || StringUtils.isEmpty(string2)) {
            return string;
        }
        if (string.startsWith(string2)) {
            return string.substring(string2.length());
        }
        return string;
    }

    public static String removeStartIgnoreCase(String string, String string2) {
        if (string != null && StringUtils.startsWithIgnoreCase(string, string2)) {
            return string.substring(StringUtils.length(string2));
        }
        return string;
    }

    public static String repeat(char c2, int n) {
        if (n <= 0) {
            return EMPTY;
        }
        return new String(ArrayFill.fill(new char[n], c2));
    }

    public static String repeat(String string, int n) {
        if (string == null) {
            return null;
        }
        if (n <= 0) {
            return EMPTY;
        }
        int n2 = string.length();
        if (n == 1 || n2 == 0) {
            return string;
        }
        if (n2 == 1 && n <= 8192) {
            return StringUtils.repeat(string.charAt(0), n);
        }
        int n3 = n2 * n;
        switch (n2) {
            case 1: {
                return StringUtils.repeat(string.charAt(0), n);
            }
            case 2: {
                char c2 = string.charAt(0);
                char c3 = string.charAt(1);
                char[] cArray = new char[n3];
                for (int i = n * 2 - 2; i >= 0; --i) {
                    cArray[i] = c2;
                    cArray[i + 1] = c3;
                    --i;
                }
                return new String(cArray);
            }
        }
        StringBuilder stringBuilder = new StringBuilder(n3);
        for (int i = 0; i < n; ++i) {
            stringBuilder.append(string);
        }
        return stringBuilder.toString();
    }

    public static String repeat(String string, String string2, int n) {
        if (string == null || string2 == null) {
            return StringUtils.repeat(string, n);
        }
        String string3 = StringUtils.repeat(string + string2, n);
        return StringUtils.removeEnd(string3, string2);
    }

    public static String replace(String string, String string2, String string3) {
        return StringUtils.replace(string, string2, string3, -1);
    }

    public static String replace(String string, String string2, String string3, int n) {
        return StringUtils.replace(string, string2, string3, n, false);
    }

    private static String replace(String string, String string2, String string3, int n, boolean bl) {
        int n2;
        if (StringUtils.isEmpty(string) || StringUtils.isEmpty(string2) || string3 == null || n == 0) {
            return string;
        }
        if (bl) {
            string2 = string2.toLowerCase();
        }
        int n3 = 0;
        int n4 = n2 = bl ? StringUtils.indexOfIgnoreCase(string, string2, n3) : StringUtils.indexOf((CharSequence)string, string2, n3);
        if (n2 == -1) {
            return string;
        }
        int n5 = string2.length();
        int n6 = Math.max(string3.length() - n5, 0);
        StringBuilder stringBuilder = new StringBuilder(string.length() + (n6 *= n < 0 ? 16 : Math.min(n, 64)));
        while (n2 != -1) {
            stringBuilder.append(string, n3, n2).append(string3);
            n3 = n2 + n5;
            if (--n == 0) break;
            n2 = bl ? StringUtils.indexOfIgnoreCase(string, string2, n3) : StringUtils.indexOf((CharSequence)string, string2, n3);
        }
        stringBuilder.append(string, n3, string.length());
        return stringBuilder.toString();
    }

    @Deprecated
    public static String replaceAll(String string, String string2, String string3) {
        return RegExUtils.replaceAll(string, string2, string3);
    }

    public static String replaceChars(String string, char c2, char c3) {
        if (string == null) {
            return null;
        }
        return string.replace(c2, c3);
    }

    public static String replaceChars(String string, String string2, String string3) {
        if (StringUtils.isEmpty(string) || StringUtils.isEmpty(string2)) {
            return string;
        }
        if (string3 == null) {
            string3 = EMPTY;
        }
        boolean bl = false;
        int n = string3.length();
        int n2 = string.length();
        StringBuilder stringBuilder = new StringBuilder(n2);
        for (int i = 0; i < n2; ++i) {
            char c2 = string.charAt(i);
            int n3 = string2.indexOf(c2);
            if (n3 >= 0) {
                bl = true;
                if (n3 >= n) continue;
                stringBuilder.append(string3.charAt(n3));
                continue;
            }
            stringBuilder.append(c2);
        }
        if (bl) {
            return stringBuilder.toString();
        }
        return string;
    }

    public static String replaceEach(String string, String[] stringArray, String[] stringArray2) {
        return StringUtils.replaceEach(string, stringArray, stringArray2, false, 0);
    }

    private static String replaceEach(String string, String[] stringArray, String[] stringArray2, boolean bl, int n) {
        int n2;
        int n3;
        int n4;
        if (n < 0) {
            HashSet<String> hashSet = new HashSet<String>(Arrays.asList(stringArray));
            HashSet<String> hashSet2 = new HashSet<String>(Arrays.asList(stringArray2));
            hashSet.retainAll(hashSet2);
            if (!hashSet.isEmpty()) {
                throw new IllegalStateException("Aborting to protect against StackOverflowError - output of one loop is the input of another");
            }
        }
        if (StringUtils.isEmpty(string) || ArrayUtils.isEmpty(stringArray) || ArrayUtils.isEmpty(stringArray2) || ArrayUtils.isNotEmpty(stringArray) && n == -1) {
            return string;
        }
        int n5 = stringArray.length;
        int n6 = stringArray2.length;
        if (n5 != n6) {
            throw new IllegalArgumentException("Search and Replace array lengths don't match: " + n5 + " vs " + n6);
        }
        boolean[] blArray = new boolean[n5];
        int n7 = -1;
        int n8 = -1;
        for (n4 = 0; n4 < n5; ++n4) {
            if (blArray[n4] || StringUtils.isEmpty(stringArray[n4]) || stringArray2[n4] == null) continue;
            n3 = string.indexOf(stringArray[n4]);
            if (n3 == -1) {
                blArray[n4] = true;
                continue;
            }
            if (n7 != -1 && n3 >= n7) continue;
            n7 = n3;
            n8 = n4;
        }
        if (n7 == -1) {
            return string;
        }
        n4 = 0;
        int n9 = 0;
        for (int i = 0; i < stringArray.length; ++i) {
            if (stringArray[i] == null || stringArray2[i] == null || (n2 = stringArray2[i].length() - stringArray[i].length()) <= 0) continue;
            n9 += 3 * n2;
        }
        n9 = Math.min(n9, string.length() / 5);
        StringBuilder stringBuilder = new StringBuilder(string.length() + n9);
        while (n7 != -1) {
            for (n2 = n4; n2 < n7; ++n2) {
                stringBuilder.append(string.charAt(n2));
            }
            stringBuilder.append(stringArray2[n8]);
            n4 = n7 + stringArray[n8].length();
            n7 = -1;
            n8 = -1;
            for (n2 = 0; n2 < n5; ++n2) {
                if (blArray[n2] || StringUtils.isEmpty(stringArray[n2]) || stringArray2[n2] == null) continue;
                n3 = string.indexOf(stringArray[n2], n4);
                if (n3 == -1) {
                    blArray[n2] = true;
                    continue;
                }
                if (n7 != -1 && n3 >= n7) continue;
                n7 = n3;
                n8 = n2;
            }
        }
        n2 = string.length();
        for (int i = n4; i < n2; ++i) {
            stringBuilder.append(string.charAt(i));
        }
        String string2 = stringBuilder.toString();
        if (!bl) {
            return string2;
        }
        return StringUtils.replaceEach(string2, stringArray, stringArray2, bl, n - 1);
    }

    public static String replaceEachRepeatedly(String string, String[] stringArray, String[] stringArray2) {
        return StringUtils.replaceEach(string, stringArray, stringArray2, true, ArrayUtils.getLength(stringArray));
    }

    @Deprecated
    public static String replaceFirst(String string, String string2, String string3) {
        return RegExUtils.replaceFirst(string, string2, string3);
    }

    public static String replaceIgnoreCase(String string, String string2, String string3) {
        return StringUtils.replaceIgnoreCase(string, string2, string3, -1);
    }

    public static String replaceIgnoreCase(String string, String string2, String string3, int n) {
        return StringUtils.replace(string, string2, string3, n, true);
    }

    public static String replaceOnce(String string, String string2, String string3) {
        return StringUtils.replace(string, string2, string3, 1);
    }

    public static String replaceOnceIgnoreCase(String string, String string2, String string3) {
        return StringUtils.replaceIgnoreCase(string, string2, string3, 1);
    }

    @Deprecated
    public static String replacePattern(String string, String string2, String string3) {
        return RegExUtils.replacePattern(string, string2, string3);
    }

    public static String reverse(String string) {
        if (string == null) {
            return null;
        }
        return new StringBuilder(string).reverse().toString();
    }

    public static String reverseDelimited(String string, char c2) {
        Object[] objectArray = StringUtils.split(string, c2);
        ArrayUtils.reverse(objectArray);
        return StringUtils.join(objectArray, c2);
    }

    public static String right(String string, int n) {
        if (string == null) {
            return null;
        }
        if (n < 0) {
            return EMPTY;
        }
        if (string.length() <= n) {
            return string;
        }
        return string.substring(string.length() - n);
    }

    public static String rightPad(String string, int n) {
        return StringUtils.rightPad(string, n, ' ');
    }

    public static String rightPad(String string, int n, char c2) {
        if (string == null) {
            return null;
        }
        int n2 = n - string.length();
        if (n2 <= 0) {
            return string;
        }
        if (n2 > 8192) {
            return StringUtils.rightPad(string, n, String.valueOf(c2));
        }
        return string.concat(StringUtils.repeat(c2, n2));
    }

    public static String rightPad(String string, int n, String string2) {
        if (string == null) {
            return null;
        }
        if (StringUtils.isEmpty(string2)) {
            string2 = SPACE;
        }
        int n2 = string2.length();
        int n3 = string.length();
        int n4 = n - n3;
        if (n4 <= 0) {
            return string;
        }
        if (n2 == 1 && n4 <= 8192) {
            return StringUtils.rightPad(string, n, string2.charAt(0));
        }
        if (n4 == n2) {
            return string.concat(string2);
        }
        if (n4 < n2) {
            return string.concat(string2.substring(0, n4));
        }
        char[] cArray = new char[n4];
        char[] cArray2 = string2.toCharArray();
        for (int i = 0; i < n4; ++i) {
            cArray[i] = cArray2[i % n2];
        }
        return string.concat(new String(cArray));
    }

    public static String rotate(String string, int n) {
        if (string == null) {
            return null;
        }
        int n2 = string.length();
        if (n == 0 || n2 == 0 || n % n2 == 0) {
            return string;
        }
        StringBuilder stringBuilder = new StringBuilder(n2);
        int n3 = -(n % n2);
        stringBuilder.append(StringUtils.substring(string, n3));
        stringBuilder.append(StringUtils.substring(string, 0, n3));
        return stringBuilder.toString();
    }

    public static String[] split(String string) {
        return StringUtils.split(string, null, -1);
    }

    public static String[] split(String string, char c2) {
        return StringUtils.splitWorker(string, c2, false);
    }

    public static String[] split(String string, String string2) {
        return StringUtils.splitWorker(string, string2, -1, false);
    }

    public static String[] split(String string, String string2, int n) {
        return StringUtils.splitWorker(string, string2, n, false);
    }

    public static String[] splitByCharacterType(String string) {
        return StringUtils.splitByCharacterType(string, false);
    }

    private static String[] splitByCharacterType(String string, boolean bl) {
        if (string == null) {
            return null;
        }
        if (string.isEmpty()) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        char[] cArray = string.toCharArray();
        ArrayList<String> arrayList = new ArrayList<String>();
        int n = 0;
        int n2 = Character.getType(cArray[n]);
        for (int i = n + 1; i < cArray.length; ++i) {
            int n3 = Character.getType(cArray[i]);
            if (n3 == n2) continue;
            if (bl && n3 == 2 && n2 == 1) {
                int n4 = i - 1;
                if (n4 != n) {
                    arrayList.add(new String(cArray, n, n4 - n));
                    n = n4;
                }
            } else {
                arrayList.add(new String(cArray, n, i - n));
                n = i;
            }
            n2 = n3;
        }
        arrayList.add(new String(cArray, n, cArray.length - n));
        return arrayList.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
    }

    public static String[] splitByCharacterTypeCamelCase(String string) {
        return StringUtils.splitByCharacterType(string, true);
    }

    public static String[] splitByWholeSeparator(String string, String string2) {
        return StringUtils.splitByWholeSeparatorWorker(string, string2, -1, false);
    }

    public static String[] splitByWholeSeparator(String string, String string2, int n) {
        return StringUtils.splitByWholeSeparatorWorker(string, string2, n, false);
    }

    public static String[] splitByWholeSeparatorPreserveAllTokens(String string, String string2) {
        return StringUtils.splitByWholeSeparatorWorker(string, string2, -1, true);
    }

    public static String[] splitByWholeSeparatorPreserveAllTokens(String string, String string2, int n) {
        return StringUtils.splitByWholeSeparatorWorker(string, string2, n, true);
    }

    private static String[] splitByWholeSeparatorWorker(String string, String string2, int n, boolean bl) {
        if (string == null) {
            return null;
        }
        int n2 = string.length();
        if (n2 == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        if (string2 == null || EMPTY.equals(string2)) {
            return StringUtils.splitWorker(string, null, n, bl);
        }
        int n3 = string2.length();
        ArrayList<String> arrayList = new ArrayList<String>();
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        while (n6 < n2) {
            n6 = string.indexOf(string2, n5);
            if (n6 > -1) {
                if (n6 > n5) {
                    if (++n4 == n) {
                        n6 = n2;
                        arrayList.add(string.substring(n5));
                        continue;
                    }
                    arrayList.add(string.substring(n5, n6));
                    n5 = n6 + n3;
                    continue;
                }
                if (bl) {
                    if (++n4 == n) {
                        n6 = n2;
                        arrayList.add(string.substring(n5));
                    } else {
                        arrayList.add(EMPTY);
                    }
                }
                n5 = n6 + n3;
                continue;
            }
            arrayList.add(string.substring(n5));
            n6 = n2;
        }
        return arrayList.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
    }

    public static String[] splitPreserveAllTokens(String string) {
        return StringUtils.splitWorker(string, null, -1, true);
    }

    public static String[] splitPreserveAllTokens(String string, char c2) {
        return StringUtils.splitWorker(string, c2, true);
    }

    public static String[] splitPreserveAllTokens(String string, String string2) {
        return StringUtils.splitWorker(string, string2, -1, true);
    }

    public static String[] splitPreserveAllTokens(String string, String string2, int n) {
        return StringUtils.splitWorker(string, string2, n, true);
    }

    private static String[] splitWorker(String string, char c2, boolean bl) {
        if (string == null) {
            return null;
        }
        int n = string.length();
        if (n == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        ArrayList<String> arrayList = new ArrayList<String>();
        int n2 = 0;
        int n3 = 0;
        boolean bl2 = false;
        boolean bl3 = false;
        while (n2 < n) {
            if (string.charAt(n2) == c2) {
                if (bl2 || bl) {
                    arrayList.add(string.substring(n3, n2));
                    bl2 = false;
                    bl3 = true;
                }
                n3 = ++n2;
                continue;
            }
            bl3 = false;
            bl2 = true;
            ++n2;
        }
        if (bl2 || bl && bl3) {
            arrayList.add(string.substring(n3, n2));
        }
        return arrayList.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
    }

    private static String[] splitWorker(String string, String string2, int n, boolean bl) {
        if (string == null) {
            return null;
        }
        int n2 = string.length();
        if (n2 == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        ArrayList<String> arrayList = new ArrayList<String>();
        int n3 = 1;
        int n4 = 0;
        int n5 = 0;
        boolean bl2 = false;
        boolean bl3 = false;
        if (string2 == null) {
            while (n4 < n2) {
                if (Character.isWhitespace(string.charAt(n4))) {
                    if (bl2 || bl) {
                        bl3 = true;
                        if (n3++ == n) {
                            n4 = n2;
                            bl3 = false;
                        }
                        arrayList.add(string.substring(n5, n4));
                        bl2 = false;
                    }
                    n5 = ++n4;
                    continue;
                }
                bl3 = false;
                bl2 = true;
                ++n4;
            }
        } else if (string2.length() == 1) {
            char c2 = string2.charAt(0);
            while (n4 < n2) {
                if (string.charAt(n4) == c2) {
                    if (bl2 || bl) {
                        bl3 = true;
                        if (n3++ == n) {
                            n4 = n2;
                            bl3 = false;
                        }
                        arrayList.add(string.substring(n5, n4));
                        bl2 = false;
                    }
                    n5 = ++n4;
                    continue;
                }
                bl3 = false;
                bl2 = true;
                ++n4;
            }
        } else {
            while (n4 < n2) {
                if (string2.indexOf(string.charAt(n4)) >= 0) {
                    if (bl2 || bl) {
                        bl3 = true;
                        if (n3++ == n) {
                            n4 = n2;
                            bl3 = false;
                        }
                        arrayList.add(string.substring(n5, n4));
                        bl2 = false;
                    }
                    n5 = ++n4;
                    continue;
                }
                bl3 = false;
                bl2 = true;
                ++n4;
            }
        }
        if (bl2 || bl && bl3) {
            arrayList.add(string.substring(n5, n4));
        }
        return arrayList.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
    }

    public static boolean startsWith(CharSequence charSequence, CharSequence charSequence2) {
        return StringUtils.startsWith(charSequence, charSequence2, false);
    }

    private static boolean startsWith(CharSequence charSequence, CharSequence charSequence2, boolean bl) {
        if (charSequence == null || charSequence2 == null) {
            return charSequence == charSequence2;
        }
        int n = charSequence2.length();
        if (n > charSequence.length()) {
            return false;
        }
        return CharSequenceUtils.regionMatches(charSequence, bl, 0, charSequence2, 0, n);
    }

    public static boolean startsWithAny(CharSequence charSequence, CharSequence ... charSequenceArray) {
        if (StringUtils.isEmpty(charSequence) || ArrayUtils.isEmpty(charSequenceArray)) {
            return false;
        }
        for (CharSequence charSequence2 : charSequenceArray) {
            if (!StringUtils.startsWith(charSequence, charSequence2)) continue;
            return true;
        }
        return false;
    }

    public static boolean startsWithIgnoreCase(CharSequence charSequence, CharSequence charSequence2) {
        return StringUtils.startsWith(charSequence, charSequence2, true);
    }

    public static String strip(String string) {
        return StringUtils.strip(string, null);
    }

    public static String strip(String string, String string2) {
        string = StringUtils.stripStart(string, string2);
        return StringUtils.stripEnd(string, string2);
    }

    public static String stripAccents(String string) {
        if (StringUtils.isEmpty(string)) {
            return string;
        }
        StringBuilder stringBuilder = new StringBuilder(Normalizer.normalize(string, Normalizer.Form.NFKD));
        StringUtils.convertRemainingAccentCharacters(stringBuilder);
        return STRIP_ACCENTS_PATTERN.matcher(stringBuilder).replaceAll(EMPTY);
    }

    public static String[] stripAll(String ... stringArray) {
        return StringUtils.stripAll(stringArray, null);
    }

    public static String[] stripAll(String[] stringArray, String string) {
        int n2 = ArrayUtils.getLength(stringArray);
        if (n2 == 0) {
            return stringArray;
        }
        String[] stringArray2 = new String[n2];
        Arrays.setAll(stringArray2, n -> StringUtils.strip(stringArray[n], string));
        return stringArray2;
    }

    public static String stripEnd(String string, String string2) {
        int n = StringUtils.length(string);
        if (n == 0) {
            return string;
        }
        if (string2 == null) {
            while (n != 0 && Character.isWhitespace(string.charAt(n - 1))) {
                --n;
            }
        } else {
            if (string2.isEmpty()) {
                return string;
            }
            while (n != 0 && string2.indexOf(string.charAt(n - 1)) != -1) {
                --n;
            }
        }
        return string.substring(0, n);
    }

    public static String stripStart(String string, String string2) {
        int n;
        int n2 = StringUtils.length(string);
        if (n2 == 0) {
            return string;
        }
        if (string2 == null) {
            for (n = 0; n != n2 && Character.isWhitespace(string.charAt(n)); ++n) {
            }
        } else {
            if (string2.isEmpty()) {
                return string;
            }
            while (n != n2 && string2.indexOf(string.charAt(n)) != -1) {
                ++n;
            }
        }
        return string.substring(n);
    }

    public static String stripToEmpty(String string) {
        return string == null ? EMPTY : StringUtils.strip(string, null);
    }

    public static String stripToNull(String string) {
        if (string == null) {
            return null;
        }
        return (string = StringUtils.strip(string, null)).isEmpty() ? null : string;
    }

    public static String substring(String string, int n) {
        if (string == null) {
            return null;
        }
        if (n < 0) {
            n = string.length() + n;
        }
        if (n < 0) {
            n = 0;
        }
        if (n > string.length()) {
            return EMPTY;
        }
        return string.substring(n);
    }

    public static String substring(String string, int n, int n2) {
        if (string == null) {
            return null;
        }
        if (n2 < 0) {
            n2 = string.length() + n2;
        }
        if (n < 0) {
            n = string.length() + n;
        }
        if (n2 > string.length()) {
            n2 = string.length();
        }
        if (n > n2) {
            return EMPTY;
        }
        if (n < 0) {
            n = 0;
        }
        if (n2 < 0) {
            n2 = 0;
        }
        return string.substring(n, n2);
    }

    public static String substringAfter(String string, int n) {
        if (StringUtils.isEmpty(string)) {
            return string;
        }
        int n2 = string.indexOf(n);
        if (n2 == -1) {
            return EMPTY;
        }
        return string.substring(n2 + 1);
    }

    public static String substringAfter(String string, String string2) {
        if (StringUtils.isEmpty(string)) {
            return string;
        }
        if (string2 == null) {
            return EMPTY;
        }
        int n = string.indexOf(string2);
        if (n == -1) {
            return EMPTY;
        }
        return string.substring(n + string2.length());
    }

    public static String substringAfterLast(String string, int n) {
        if (StringUtils.isEmpty(string)) {
            return string;
        }
        int n2 = string.lastIndexOf(n);
        if (n2 == -1 || n2 == string.length() - 1) {
            return EMPTY;
        }
        return string.substring(n2 + 1);
    }

    public static String substringAfterLast(String string, String string2) {
        if (StringUtils.isEmpty(string)) {
            return string;
        }
        if (StringUtils.isEmpty(string2)) {
            return EMPTY;
        }
        int n = string.lastIndexOf(string2);
        if (n == -1 || n == string.length() - string2.length()) {
            return EMPTY;
        }
        return string.substring(n + string2.length());
    }

    public static String substringBefore(String string, int n) {
        if (StringUtils.isEmpty(string)) {
            return string;
        }
        int n2 = string.indexOf(n);
        if (n2 == -1) {
            return string;
        }
        return string.substring(0, n2);
    }

    public static String substringBefore(String string, String string2) {
        if (StringUtils.isEmpty(string) || string2 == null) {
            return string;
        }
        if (string2.isEmpty()) {
            return EMPTY;
        }
        int n = string.indexOf(string2);
        if (n == -1) {
            return string;
        }
        return string.substring(0, n);
    }

    public static String substringBeforeLast(String string, String string2) {
        if (StringUtils.isEmpty(string) || StringUtils.isEmpty(string2)) {
            return string;
        }
        int n = string.lastIndexOf(string2);
        if (n == -1) {
            return string;
        }
        return string.substring(0, n);
    }

    public static String substringBetween(String string, String string2) {
        return StringUtils.substringBetween(string, string2, string2);
    }

    public static String substringBetween(String string, String string2, String string3) {
        int n;
        if (!ObjectUtils.allNotNull(string, string2, string3)) {
            return null;
        }
        int n2 = string.indexOf(string2);
        if (n2 != -1 && (n = string.indexOf(string3, n2 + string2.length())) != -1) {
            return string.substring(n2 + string2.length(), n);
        }
        return null;
    }

    public static String[] substringsBetween(String string, String string2, String string3) {
        int n;
        int n2;
        if (string == null || StringUtils.isEmpty(string2) || StringUtils.isEmpty(string3)) {
            return null;
        }
        int n3 = string.length();
        if (n3 == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        int n4 = string3.length();
        int n5 = string2.length();
        ArrayList<String> arrayList = new ArrayList<String>();
        int n6 = 0;
        while (n6 < n3 - n4 && (n2 = string.indexOf(string2, n6)) >= 0 && (n = string.indexOf(string3, n2 += n5)) >= 0) {
            arrayList.add(string.substring(n2, n));
            n6 = n + n4;
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        return arrayList.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
    }

    public static String swapCase(String string) {
        int n;
        if (StringUtils.isEmpty(string)) {
            return string;
        }
        int n2 = string.length();
        int[] nArray = new int[n2];
        int n3 = 0;
        for (int i = 0; i < n2; i += Character.charCount(n)) {
            int n4 = string.codePointAt(i);
            n = Character.isUpperCase(n4) || Character.isTitleCase(n4) ? Character.toLowerCase(n4) : (Character.isLowerCase(n4) ? Character.toUpperCase(n4) : n4);
            nArray[n3++] = n;
        }
        return new String(nArray, 0, n3);
    }

    public static int[] toCodePoints(CharSequence charSequence) {
        if (charSequence == null) {
            return null;
        }
        if (charSequence.length() == 0) {
            return ArrayUtils.EMPTY_INT_ARRAY;
        }
        String string = charSequence.toString();
        int[] nArray = new int[string.codePointCount(0, string.length())];
        int n = 0;
        for (int i = 0; i < nArray.length; ++i) {
            nArray[i] = string.codePointAt(n);
            n += Character.charCount(nArray[i]);
        }
        return nArray;
    }

    public static String toEncodedString(byte[] byArray, Charset charset) {
        return new String(byArray, Charsets.toCharset(charset));
    }

    public static String toRootLowerCase(String string) {
        return string == null ? null : string.toLowerCase(Locale.ROOT);
    }

    public static String toRootUpperCase(String string) {
        return string == null ? null : string.toUpperCase(Locale.ROOT);
    }

    @Deprecated
    public static String toString(byte[] byArray, String string) {
        return new String(byArray, Charsets.toCharset(string));
    }

    private static String toStringOrEmpty(Object object) {
        return Objects.toString(object, EMPTY);
    }

    public static String trim(String string) {
        return string == null ? null : string.trim();
    }

    public static String trimToEmpty(String string) {
        return string == null ? EMPTY : string.trim();
    }

    public static String trimToNull(String string) {
        String string2 = StringUtils.trim(string);
        return StringUtils.isEmpty(string2) ? null : string2;
    }

    public static String truncate(String string, int n) {
        return StringUtils.truncate(string, 0, n);
    }

    public static String truncate(String string, int n, int n2) {
        if (n < 0) {
            throw new IllegalArgumentException("offset cannot be negative");
        }
        if (n2 < 0) {
            throw new IllegalArgumentException("maxWith cannot be negative");
        }
        if (string == null) {
            return null;
        }
        if (n > string.length()) {
            return EMPTY;
        }
        if (string.length() > n2) {
            int n3 = Math.min(n + n2, string.length());
            return string.substring(n, n3);
        }
        return string.substring(n);
    }

    public static String uncapitalize(String string) {
        int n;
        int n2;
        int n3 = StringUtils.length(string);
        if (n3 == 0) {
            return string;
        }
        int n4 = string.codePointAt(0);
        if (n4 == (n2 = Character.toLowerCase(n4))) {
            return string;
        }
        int[] nArray = new int[n3];
        int n5 = 0;
        nArray[n5++] = n2;
        for (int i = Character.charCount(n4); i < n3; i += Character.charCount(n)) {
            n = string.codePointAt(i);
            nArray[n5++] = n;
        }
        return new String(nArray, 0, n5);
    }

    public static String unwrap(String string, char c2) {
        if (StringUtils.isEmpty(string) || c2 == '\u0000' || string.length() == 1) {
            return string;
        }
        if (string.charAt(0) == c2 && string.charAt(string.length() - 1) == c2) {
            boolean bl = false;
            int n = string.length() - 1;
            return string.substring(1, n);
        }
        return string;
    }

    public static String unwrap(String string, String string2) {
        if (StringUtils.isEmpty(string) || StringUtils.isEmpty(string2) || string.length() < 2 * string2.length()) {
            return string;
        }
        if (StringUtils.startsWith(string, string2) && StringUtils.endsWith(string, string2)) {
            return string.substring(string2.length(), string.lastIndexOf(string2));
        }
        return string;
    }

    public static String upperCase(String string) {
        if (string == null) {
            return null;
        }
        return string.toUpperCase();
    }

    public static String upperCase(String string, Locale locale) {
        if (string == null) {
            return null;
        }
        return string.toUpperCase(LocaleUtils.toLocale(locale));
    }

    public static String valueOf(char[] cArray) {
        return cArray == null ? null : String.valueOf(cArray);
    }

    public static String wrap(String string, char c2) {
        if (StringUtils.isEmpty(string) || c2 == '\u0000') {
            return string;
        }
        return c2 + string + c2;
    }

    public static String wrap(String string, String string2) {
        if (StringUtils.isEmpty(string) || StringUtils.isEmpty(string2)) {
            return string;
        }
        return string2.concat(string).concat(string2);
    }

    public static String wrapIfMissing(String string, char c2) {
        boolean bl;
        if (StringUtils.isEmpty(string) || c2 == '\u0000') {
            return string;
        }
        boolean bl2 = string.charAt(0) != c2;
        boolean bl3 = bl = string.charAt(string.length() - 1) != c2;
        if (!bl2 && !bl) {
            return string;
        }
        StringBuilder stringBuilder = new StringBuilder(string.length() + 2);
        if (bl2) {
            stringBuilder.append(c2);
        }
        stringBuilder.append(string);
        if (bl) {
            stringBuilder.append(c2);
        }
        return stringBuilder.toString();
    }

    public static String wrapIfMissing(String string, String string2) {
        boolean bl;
        if (StringUtils.isEmpty(string) || StringUtils.isEmpty(string2)) {
            return string;
        }
        boolean bl2 = !string.startsWith(string2);
        boolean bl3 = bl = !string.endsWith(string2);
        if (!bl2 && !bl) {
            return string;
        }
        StringBuilder stringBuilder = new StringBuilder(string.length() + string2.length() + string2.length());
        if (bl2) {
            stringBuilder.append(string2);
        }
        stringBuilder.append(string);
        if (bl) {
            stringBuilder.append(string2);
        }
        return stringBuilder.toString();
    }

    @Deprecated
    public StringUtils() {
    }
}

