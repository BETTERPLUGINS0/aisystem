/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Color
 *  org.bukkit.inventory.ItemStack
 */
package nl.sbdeveloper.vehiclesplus.utils;

import java.util.function.Function;
import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.libs.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;

public final class ColorUtil {
    private static final Function<String, String> FUNCTION = Bukkit.getBukkitVersion().contains("1.16") ? string -> net.md_5.bungee.api.ChatColor.translateAlternateColorCodes((char)'&', (String)string) : string -> ChatColor.translateAlternateColorCodes((char)'&', (String)string);

    public static String __(String string) {
        return FUNCTION.apply(string);
    }

    public static Color convertChatColorToColor(ChatColor chatColor) {
        switch (chatColor) {
            case DARK_BLUE: {
                return Color.fromRGB((int)45, (int)45, (int)140);
            }
            case BLACK: {
                return Color.fromRGB((int)10, (int)10, (int)10);
            }
            case DARK_RED: {
                return Color.fromRGB((int)140, (int)35, (int)35);
            }
            case DARK_GREEN: {
                return Color.fromRGB((int)70, (int)90, (int)40);
            }
            case DARK_PURPLE: {
                return Color.fromRGB((int)100, (int)35, (int)150);
            }
            case DARK_AQUA: {
                return Color.fromRGB((int)20, (int)120, (int)135);
            }
            case GRAY: {
                return Color.fromRGB((int)125, (int)125, (int)125);
            }
            case GREEN: {
                return Color.fromRGB((int)100, (int)175, (int)25);
            }
            case YELLOW: {
                return Color.fromRGB((int)240, (int)175, (int)20);
            }
            case BLUE: {
                return Color.fromRGB((int)35, (int)135, (int)200);
            }
            case LIGHT_PURPLE: {
                return Color.fromRGB((int)170, (int)50, (int)160);
            }
            case GOLD: {
                return Color.fromRGB((int)220, (int)100, (int)0);
            }
            case WHITE: {
                return Color.fromRGB((int)210, (int)210, (int)210);
            }
        }
        return null;
    }

    public static ChatColor convertColorToChatColor(Color color) {
        double d = Double.MAX_VALUE;
        ChatColor chatColor = null;
        for (ChatColor chatColor2 : ChatColor.values()) {
            if (!chatColor2.isColor()) continue;
            Color color2 = switch (chatColor2) {
                case ChatColor.DARK_BLUE -> Color.fromRGB((int)0, (int)0, (int)170);
                case ChatColor.BLACK -> Color.fromRGB((int)0, (int)0, (int)0);
                case ChatColor.DARK_RED -> Color.fromRGB((int)170, (int)0, (int)0);
                case ChatColor.DARK_GREEN -> Color.fromRGB((int)0, (int)170, (int)0);
                case ChatColor.DARK_PURPLE -> Color.fromRGB((int)170, (int)0, (int)170);
                case ChatColor.DARK_AQUA -> Color.fromRGB((int)0, (int)170, (int)170);
                case ChatColor.GRAY -> Color.fromRGB((int)170, (int)170, (int)170);
                case ChatColor.GREEN -> Color.fromRGB((int)85, (int)255, (int)85);
                case ChatColor.YELLOW -> Color.fromRGB((int)255, (int)255, (int)85);
                case ChatColor.BLUE -> Color.fromRGB((int)85, (int)85, (int)255);
                case ChatColor.LIGHT_PURPLE -> Color.fromRGB((int)255, (int)85, (int)255);
                case ChatColor.GOLD -> Color.fromRGB((int)255, (int)170, (int)0);
                case ChatColor.WHITE -> Color.fromRGB((int)255, (int)255, (int)255);
                default -> Color.WHITE;
            };
            double d2 = Math.pow(color.getRed() - color2.getRed(), 2.0) + Math.pow(color.getGreen() - color2.getGreen(), 2.0) + Math.pow(color.getBlue() - color2.getBlue(), 2.0);
            if (!(d2 < d)) continue;
            d = d2;
            chatColor = chatColor2;
        }
        return chatColor;
    }

    public static ItemStack convertChatColorToColoredGlassPane(ChatColor chatColor) {
        switch (chatColor) {
            case DARK_BLUE: {
                return XMaterial.BLUE_STAINED_GLASS_PANE.parseItem();
            }
            case BLACK: {
                return XMaterial.BLACK_STAINED_GLASS_PANE.parseItem();
            }
            case DARK_RED: {
                return XMaterial.RED_STAINED_GLASS_PANE.parseItem();
            }
            case DARK_GREEN: {
                return XMaterial.GREEN_STAINED_GLASS_PANE.parseItem();
            }
            case DARK_PURPLE: {
                return XMaterial.PURPLE_STAINED_GLASS_PANE.parseItem();
            }
            case DARK_AQUA: {
                return XMaterial.CYAN_STAINED_GLASS_PANE.parseItem();
            }
            case GRAY: {
                return XMaterial.LIGHT_GRAY_STAINED_GLASS_PANE.parseItem();
            }
            case GREEN: {
                return XMaterial.LIME_STAINED_GLASS_PANE.parseItem();
            }
            case YELLOW: {
                return XMaterial.YELLOW_STAINED_GLASS_PANE.parseItem();
            }
            case BLUE: {
                return XMaterial.LIGHT_BLUE_STAINED_GLASS_PANE.parseItem();
            }
            case LIGHT_PURPLE: {
                return XMaterial.MAGENTA_STAINED_GLASS_PANE.parseItem();
            }
            case GOLD: {
                return XMaterial.ORANGE_STAINED_GLASS_PANE.parseItem();
            }
            case WHITE: {
                return XMaterial.WHITE_STAINED_GLASS_PANE.parseItem();
            }
        }
        return null;
    }

    public static ItemStack convertChatColorToColoredConcrete(ChatColor chatColor) {
        switch (chatColor) {
            case DARK_BLUE: {
                return XMaterial.BLUE_CONCRETE.parseItem();
            }
            case BLACK: {
                return XMaterial.BLACK_CONCRETE.parseItem();
            }
            case DARK_RED: {
                return XMaterial.RED_CONCRETE.parseItem();
            }
            case DARK_GREEN: {
                return XMaterial.GREEN_CONCRETE.parseItem();
            }
            case DARK_PURPLE: {
                return XMaterial.PURPLE_CONCRETE.parseItem();
            }
            case DARK_AQUA: {
                return XMaterial.CYAN_CONCRETE.parseItem();
            }
            case GRAY: {
                return XMaterial.LIGHT_GRAY_CONCRETE.parseItem();
            }
            case GREEN: {
                return XMaterial.LIME_CONCRETE.parseItem();
            }
            case YELLOW: {
                return XMaterial.YELLOW_CONCRETE.parseItem();
            }
            case BLUE: {
                return XMaterial.LIGHT_BLUE_CONCRETE.parseItem();
            }
            case LIGHT_PURPLE: {
                return XMaterial.MAGENTA_CONCRETE.parseItem();
            }
            case GOLD: {
                return XMaterial.ORANGE_CONCRETE.parseItem();
            }
            case WHITE: {
                return XMaterial.WHITE_CONCRETE.parseItem();
            }
        }
        return null;
    }

    @Generated
    private ColorUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

