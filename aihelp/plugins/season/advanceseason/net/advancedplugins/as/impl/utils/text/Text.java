/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  me.clip.placeholderapi.PlaceholderAPI
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.Bukkit
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.command.CommandSender
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package net.advancedplugins.as.impl.utils.text;

import com.google.common.collect.Lists;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import me.clip.placeholderapi.PlaceholderAPI;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.HooksHandler;
import net.advancedplugins.as.impl.utils.nbt.utils.MinecraftVersion;
import net.advancedplugins.as.impl.utils.text.Replace;
import net.advancedplugins.as.impl.utils.text.Replacer;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Text {
    private static final char SECTION_CHAR = '\u00a7';
    private static final char AMPERSAND_CHAR = '&';
    private static final String COLOR_CODES = "0123456789AaBbCcDdEeFfKkLlMmNnOoRr";
    private static final String FORMAT_CODES = "KkLlMmMnOo";
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)\u00a7[0-9A-FK-OR]");
    public static boolean RENDER_GRADIENT = true;
    private static Pattern HEX_PATTERN = Pattern.compile("\\{#[a-fA-F0-9]{6}}");

    public static void useProperHexPattern() {
        HEX_PATTERN = Pattern.compile("#[a-fA-F0-9]{6}");
    }

    public static void sendMessage(CommandSender commandSender, String string) {
        Supplier<String> supplier = () -> {
            if (commandSender instanceof OfflinePlayer && Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                return PlaceholderAPI.setPlaceholders((OfflinePlayer)((OfflinePlayer)commandSender), (String)string);
            }
            return string;
        };
        commandSender.sendMessage(Text.modify(supplier.get()));
    }

    public static String parsePapi(String string, OfflinePlayer offlinePlayer) {
        if (HooksHandler.getHook(HookPlugin.PLACEHOLDERAPI) != null) {
            return PlaceholderAPI.setPlaceholders((OfflinePlayer)offlinePlayer, (String)string);
        }
        return string;
    }

    public static List<String> parsePapi(List<String> list, OfflinePlayer offlinePlayer) {
        if (HooksHandler.getHook(HookPlugin.PLACEHOLDERAPI) != null) {
            return list.stream().map(string -> PlaceholderAPI.setPlaceholders((OfflinePlayer)offlinePlayer, (String)string)).collect(Collectors.toList());
        }
        return list;
    }

    public static void sendMessage(Collection<CommandSender> collection, String string) {
        for (CommandSender commandSender : collection) {
            String[] stringArray;
            for (String string2 : stringArray = string.split("\n")) {
                Text.sendMessage(commandSender, string2);
            }
        }
    }

    public static String modify(String string) {
        return Text.modify(string, null);
    }

    public static String modify(String string, Replace replace) {
        return Text.modify(string, replace, true);
    }

    public static String modify(String string, Replace replace, boolean bl) {
        if (HooksHandler.getHook(HookPlugin.PLACEHOLDERAPI) != null && string != null && bl) {
            string = string.replace("%player", "%playertemp").replace("%luckperms", "%luckpermstemp");
            string = Text.parsePapi(string, null);
            string = string.replace("%playertemp", "%player").replace("%luckpermstemp", "%luckperms");
        }
        return string == null ? null : Text.renderColorCodes(replace == null ? string : ((Replacer)replace.apply(new Replacer())).applyTo(string));
    }

    public static List<String> modify(List<String> list) {
        return Text.modify(list, null);
    }

    public static List<String> modify(List<String> list, Replace replace) {
        return Text.modify(list, replace, true);
    }

    public static List<String> modify(List<String> list, Replace replace, boolean bl) {
        if (list == null) {
            return null;
        }
        ArrayList<String> arrayList = Lists.newArrayList();
        for (String string : list) {
            String string2 = Text.modify(string, replace, bl);
            arrayList.addAll(Arrays.stream(string2.split("<new>")).collect(Collectors.toList()));
        }
        return arrayList;
    }

    public static ItemStack modify(ItemStack itemStack, Replace replace) {
        ItemStack itemStack2 = itemStack.clone();
        ItemMeta itemMeta = itemStack2.getItemMeta();
        if (itemMeta == null) {
            return itemStack2;
        }
        itemMeta.setDisplayName(Text.modify(itemMeta.getDisplayName(), replace));
        itemMeta.setLore(Text.modify(itemMeta.getLore(), replace));
        itemStack2.setItemMeta(itemMeta);
        return itemStack2;
    }

    public static String decolorize(String string) {
        return string == null ? null : Text.unrenderColorCodes(string);
    }

    private static String unrenderColorCodes(String string) {
        return string == null ? null : STRIP_COLOR_PATTERN.matcher(string).replaceAll("");
    }

    public static String renderColorCodes(String string) {
        if (MinecraftVersion.getVersion().getVersionId() >= 1160) {
            string = RENDER_GRADIENT ? Text.gradient(string) : string;
        }
        Object object = HEX_PATTERN.matcher(string);
        while (((Matcher)object).find()) {
            String string2 = string.substring(((Matcher)object).start(), ((Matcher)object).end());
            string = StringUtils.replace(string, string2, String.valueOf(ChatColor.of((String)string2.replace("{", "").replace("}", ""))));
            object = HEX_PATTERN.matcher(string);
        }
        object = string.toCharArray();
        for (int i = 0; i < ((Object)object).length - 1; ++i) {
            if (object[i] != 38 || COLOR_CODES.indexOf((int)object[i + 1]) <= -1) continue;
            object[i] = 167;
            object[i + 1] = Character.toLowerCase((char)object[i + 1]);
        }
        return new String((char[])object);
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
                object = ((String)object).substring(0, i * n) + Text.gradient(string3, string2, (float)i / (float)(string4.length() - 1)) + String.valueOf(stringBuilder) + ((String)object).substring(i * n);
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

