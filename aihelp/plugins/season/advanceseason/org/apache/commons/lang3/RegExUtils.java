/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.lang3;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.ObjectUtils;

public class RegExUtils {
    public static Pattern dotAll(String string) {
        return Pattern.compile(string, 32);
    }

    public static Matcher dotAllMatcher(String string, String string2) {
        return RegExUtils.dotAll(string).matcher(string2);
    }

    public static String removeAll(String string, Pattern pattern) {
        return RegExUtils.replaceAll(string, pattern, "");
    }

    public static String removeAll(String string, String string2) {
        return RegExUtils.replaceAll(string, string2, "");
    }

    public static String removeFirst(String string, Pattern pattern) {
        return RegExUtils.replaceFirst(string, pattern, "");
    }

    public static String removeFirst(String string, String string2) {
        return RegExUtils.replaceFirst(string, string2, "");
    }

    public static String removePattern(String string, String string2) {
        return RegExUtils.replacePattern(string, string2, "");
    }

    public static String replaceAll(String string, Pattern pattern, String string2) {
        if (ObjectUtils.anyNull(string, pattern, string2)) {
            return string;
        }
        return pattern.matcher(string).replaceAll(string2);
    }

    public static String replaceAll(String string, String string2, String string3) {
        if (ObjectUtils.anyNull(string, string2, string3)) {
            return string;
        }
        return string.replaceAll(string2, string3);
    }

    public static String replaceFirst(String string, Pattern pattern, String string2) {
        if (string == null || pattern == null || string2 == null) {
            return string;
        }
        return pattern.matcher(string).replaceFirst(string2);
    }

    public static String replaceFirst(String string, String string2, String string3) {
        if (string == null || string2 == null || string3 == null) {
            return string;
        }
        return string.replaceFirst(string2, string3);
    }

    public static String replacePattern(String string, String string2, String string3) {
        if (ObjectUtils.anyNull(string, string2, string3)) {
            return string;
        }
        return RegExUtils.dotAllMatcher(string2, string).replaceAll(string3);
    }

    @Deprecated
    public RegExUtils() {
    }
}

