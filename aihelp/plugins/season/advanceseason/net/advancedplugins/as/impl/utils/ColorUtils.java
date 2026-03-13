/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  org.jetbrains.annotations.NotNull
 */
package net.advancedplugins.as.impl.utils;

import java.awt.Color;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.advancedplugins.as.impl.utils.nbt.utils.MinecraftVersion;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public class ColorUtils {
    private static final Pattern hexPattern = Pattern.compile("\\{#[a-fA-F0-9]{6}}");
    private static final Pattern normalPattern = Pattern.compile("([\u00a7&])[0-9a-fA-Fk-orK-OR]");
    private static final char AMPERSAND_CHAR = '&';
    private static final String COLOR_CODES = "0123456789AaBbCcDdEeFfKkLlMmNnOoRr";
    private static final String FORMAT_CODES = "KkLlMmMnOo";

    public static String format(String string) {
        if (string == null || string.isEmpty()) {
            return string;
        }
        if (MinecraftVersion.getVersionNumber() >= 1160) {
            string = ColorUtils.gradient(string);
            Matcher matcher = hexPattern.matcher(string);
            while (matcher.find()) {
                String string2 = string.substring(matcher.start(), matcher.end());
                string = StringUtils.replace(string, string2, String.valueOf(ChatColor.of((String)string2.replace("{", "").replace("}", ""))));
                matcher = hexPattern.matcher(string);
            }
        }
        return ChatColor.translateAlternateColorCodes((char)'&', (String)string);
    }

    public static List<String> format(List<String> list) {
        list.replaceAll(ColorUtils::format);
        return list;
    }

    public static String stripColor(@NotNull String string) {
        return ChatColor.stripColor((String)string);
    }

    public static List<String> stripColor(List<String> list) {
        list.replaceAll(ColorUtils::stripColor);
        return list;
    }

    public static String getLastColor(String string) {
        String string2 = "";
        Matcher matcher = hexPattern.matcher(string);
        while (matcher.find()) {
            string2 = string.substring(matcher.start(), matcher.end());
        }
        Matcher matcher2 = normalPattern.matcher(string);
        while (matcher2.find()) {
            string2 = string.substring(matcher2.start(), matcher2.end());
        }
        return string2;
    }

    private static String gradient(String string) {
        while (string.contains("<gradient") && string.contains("</gradient>")) {
            int n;
            int n2 = string.indexOf("<gradient");
            char[] cArray = string.toCharArray();
            StringBuilder stringBuilder = new StringBuilder();
            for (n = 0; n < n2 - 1; ++n) {
                if (cArray[n] == '&' && COLOR_CODES.indexOf(cArray[n + 1]) > -1) {
                    if (FORMAT_CODES.indexOf(cArray[n + 1]) > -1) {
                        stringBuilder.append('&').append(cArray[n + 1]);
                    } else {
                        stringBuilder = new StringBuilder();
                    }
                }
                if (cArray[n] != '{' || cArray[n + 1] != '#') continue;
                stringBuilder = new StringBuilder();
            }
            n = 10;
            if (stringBuilder.length() > 0) {
                n += stringBuilder.length();
            }
            int n3 = string.indexOf("#", n2);
            int n4 = string.indexOf("#", n3 + 1);
            String string2 = string.substring(n3, n3 + 7);
            String string3 = string.substring(n4, n4 + 7);
            String string4 = string.substring(string.indexOf(">", n2) + 1, string.indexOf("</gradient>"));
            String string5 = string.substring(n2, string.indexOf(">", n2) + 1);
            String string6 = "</gradient>";
            Object object = string4;
            for (int i = 0; i < string4.length(); ++i) {
                object = ((String)object).substring(0, i * n) + ColorUtils.gradient(string3, string2, (float)i / (float)(string4.length() - 1)) + String.valueOf(stringBuilder) + ((String)object).substring(i * n);
            }
            Object object2 = string5 + string4 + string6;
            object2 = ((String)object2).replace("?", "\\?");
            string = string.replaceFirst((String)object2, (String)object);
        }
        return string;
    }

    private static String gradient(String string, String string2, float f) {
        try {
            Color color = Color.decode(string);
            Color color2 = Color.decode(string2);
            float f2 = 1.0f - f;
            Color color3 = new Color((float)color.getRed() / 255.0f * f + (float)color2.getRed() / 255.0f * f2, (float)color.getGreen() / 255.0f * f + (float)color2.getGreen() / 255.0f * f2, (float)color.getBlue() / 255.0f * f + (float)color2.getBlue() / 255.0f * f2);
            return String.format("{#%02x%02x%02x}", color3.getRed(), color3.getGreen(), color3.getBlue());
        } catch (Exception exception) {
            return "{#FFFFFF}";
        }
    }
}

