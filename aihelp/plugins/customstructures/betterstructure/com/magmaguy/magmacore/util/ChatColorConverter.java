/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.ChatColor
 */
package com.magmaguy.magmacore.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.ChatColor;

public class ChatColorConverter {
    private static final Pattern HEX_PATTERN = Pattern.compile("&?#([A-Fa-f0-9]{6})");
    private static final Pattern HEX_TAG_PATTERN = Pattern.compile("<#([A-Fa-f0-9]{6})>");
    private static final Pattern GRADIENT_PATTERN = Pattern.compile("<(?:gradient|g):(#[A-Fa-f0-9]{6}(?::#[A-Fa-f0-9]{6})+)>(.*?)</(?:gradient|g)>");
    private static final Pattern RAINBOW_PATTERN = Pattern.compile("<(?:rainbow|r)(?::(\\d+))?>(.*?)</(?:rainbow|r)>");

    private ChatColorConverter() {
    }

    public static String convert(String string) {
        if (string == null) {
            return "";
        }
        string = ChatColorConverter.processGradients(string);
        string = ChatColorConverter.processRainbow(string);
        string = ChatColorConverter.processHexTags(string);
        string = ChatColorConverter.processHexColors(string);
        return ChatColor.translateAlternateColorCodes((char)'&', (String)string);
    }

    public static List<String> convert(List<?> list) {
        if (list == null) {
            return new ArrayList<String>();
        }
        ArrayList<String> convertedList = new ArrayList<String>();
        for (Object value : list) {
            convertedList.add(ChatColorConverter.convert(String.valueOf(value)));
        }
        return convertedList;
    }

    private static String processGradients(String string) {
        Matcher matcher = GRADIENT_PATTERN.matcher(string);
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            String colorsStr = matcher.group(1);
            String text = matcher.group(2);
            String[] colorHexes = colorsStr.split(":");
            Color[] colors = new Color[colorHexes.length];
            for (int i = 0; i < colorHexes.length; ++i) {
                colors[i] = ChatColorConverter.hexToColor(colorHexes[i]);
            }
            String gradientText = ChatColorConverter.applyGradient(text, colors);
            matcher.appendReplacement(result, Matcher.quoteReplacement(gradientText));
        }
        matcher.appendTail(result);
        return result.toString();
    }

    private static String processRainbow(String string) {
        Matcher matcher = RAINBOW_PATTERN.matcher(string);
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            String saturationStr = matcher.group(1);
            String text = matcher.group(2);
            float saturation = 1.0f;
            if (saturationStr != null) {
                saturation = (float)Math.max(0, Math.min(100, Integer.parseInt(saturationStr))) / 100.0f;
            }
            String rainbowText = ChatColorConverter.applyRainbow(text, saturation);
            matcher.appendReplacement(result, Matcher.quoteReplacement(rainbowText));
        }
        matcher.appendTail(result);
        return result.toString();
    }

    private static String processHexTags(String string) {
        Matcher matcher = HEX_TAG_PATTERN.matcher(string);
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            String hex = matcher.group(1);
            String replacement = ChatColorConverter.hexToChatColor(hex);
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);
        return result.toString();
    }

    private static String processHexColors(String string) {
        Matcher matcher = HEX_PATTERN.matcher(string);
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            String hex = matcher.group(1);
            String replacement = ChatColorConverter.hexToChatColor(hex);
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);
        return result.toString();
    }

    public static String applyGradient(String text, Color ... colors) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        if (colors == null || colors.length == 0) {
            return text;
        }
        if (colors.length == 1) {
            return ChatColorConverter.hexToChatColor(ChatColorConverter.colorToHex(colors[0])) + text;
        }
        StringBuilder result = new StringBuilder();
        int length = text.length();
        String strippedText = ChatColorConverter.stripColorCodes(text);
        int strippedLength = strippedText.length();
        if (strippedLength == 0) {
            return text;
        }
        if (strippedLength == 1) {
            return ChatColorConverter.hexToChatColor(ChatColorConverter.colorToHex(colors[0])) + text;
        }
        int colorIndex = 0;
        int segmentLength = colors.length > 1 ? strippedLength / (colors.length - 1) : strippedLength;
        for (int i = 0; i < length; ++i) {
            char next;
            char c = text.charAt(i);
            if (c == '&' && i + 1 < length && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(next = text.charAt(i + 1)) != -1) {
                result.append(c).append(next);
                ++i;
                continue;
            }
            if (c == '\u00a7' && i + 1 < length && "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx".indexOf(next = text.charAt(i + 1)) != -1) {
                result.append(c).append(next);
                ++i;
                continue;
            }
            float position = (float)colorIndex / (float)(strippedLength - 1);
            float scaledPosition = position * (float)(colors.length - 1);
            int colorIdx1 = Math.min((int)scaledPosition, colors.length - 1);
            int colorIdx2 = Math.min(colorIdx1 + 1, colors.length - 1);
            float localRatio = scaledPosition - (float)colorIdx1;
            Color interpolated = ChatColorConverter.interpolateColor(colors[colorIdx1], colors[colorIdx2], localRatio);
            result.append(ChatColorConverter.hexToChatColor(ChatColorConverter.colorToHex(interpolated))).append(c);
            ++colorIndex;
        }
        return result.toString();
    }

    public static String applyRainbow(String text, float saturation) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        String strippedText = ChatColorConverter.stripColorCodes(text);
        int strippedLength = strippedText.length();
        if (strippedLength == 0) {
            return text;
        }
        int colorIndex = 0;
        int length = text.length();
        for (int i = 0; i < length; ++i) {
            char next;
            char c = text.charAt(i);
            if (c == '&' && i + 1 < length && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(next = text.charAt(i + 1)) != -1) {
                result.append(c).append(next);
                ++i;
                continue;
            }
            float hue = (float)colorIndex / (float)strippedLength;
            Color color = Color.getHSBColor(hue, saturation, 1.0f);
            result.append(ChatColorConverter.hexToChatColor(ChatColorConverter.colorToHex(color))).append(c);
            ++colorIndex;
        }
        return result.toString();
    }

    private static String hexToChatColor(String hex) {
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }
        try {
            return net.md_5.bungee.api.ChatColor.of((String)("#" + hex)).toString();
        } catch (Exception e) {
            return ChatColorConverter.findNearestLegacyColor(ChatColorConverter.hexToColor("#" + hex)).toString();
        }
    }

    private static Color hexToColor(String hex) {
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }
        return new Color(Integer.parseInt(hex.substring(0, 2), 16), Integer.parseInt(hex.substring(2, 4), 16), Integer.parseInt(hex.substring(4, 6), 16));
    }

    private static String colorToHex(Color color) {
        return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
    }

    private static Color interpolateColor(Color color1, Color color2, float ratio) {
        ratio = Math.max(0.0f, Math.min(1.0f, ratio));
        int r = (int)((float)color1.getRed() + ratio * (float)(color2.getRed() - color1.getRed()));
        int g = (int)((float)color1.getGreen() + ratio * (float)(color2.getGreen() - color1.getGreen()));
        int b = (int)((float)color1.getBlue() + ratio * (float)(color2.getBlue() - color1.getBlue()));
        return new Color(r, g, b);
    }

    private static String stripColorCodes(String text) {
        if (text == null) {
            return "";
        }
        return text.replaceAll("&[0-9A-Fa-fK-Ok-oRr]", "").replaceAll("\u00a7[0-9A-Fa-fK-Ok-oRr]", "");
    }

    private static ChatColor findNearestLegacyColor(Color color) {
        ChatColor nearest = ChatColor.WHITE;
        double minDistance = Double.MAX_VALUE;
        int[][] legacyColors = new int[][]{{0, 0, 0}, {0, 0, 170}, {0, 170, 0}, {0, 170, 170}, {170, 0, 0}, {170, 0, 170}, {255, 170, 0}, {170, 170, 170}, {85, 85, 85}, {85, 85, 255}, {85, 255, 85}, {85, 255, 255}, {255, 85, 85}, {255, 85, 255}, {255, 255, 85}, {255, 255, 255}};
        ChatColor[] colors = new ChatColor[]{ChatColor.BLACK, ChatColor.DARK_BLUE, ChatColor.DARK_GREEN, ChatColor.DARK_AQUA, ChatColor.DARK_RED, ChatColor.DARK_PURPLE, ChatColor.GOLD, ChatColor.GRAY, ChatColor.DARK_GRAY, ChatColor.BLUE, ChatColor.GREEN, ChatColor.AQUA, ChatColor.RED, ChatColor.LIGHT_PURPLE, ChatColor.YELLOW, ChatColor.WHITE};
        for (int i = 0; i < legacyColors.length; ++i) {
            double distance = Math.sqrt(Math.pow(color.getRed() - legacyColors[i][0], 2.0) + Math.pow(color.getGreen() - legacyColors[i][1], 2.0) + Math.pow(color.getBlue() - legacyColors[i][2], 2.0));
            if (!(distance < minDistance)) continue;
            minDistance = distance;
            nearest = colors[i];
        }
        return nearest;
    }
}

