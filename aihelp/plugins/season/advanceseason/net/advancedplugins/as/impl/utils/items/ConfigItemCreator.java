/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Color
 *  org.bukkit.FireworkEffect
 *  org.bukkit.Material
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.FireworkEffectMeta
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.jetbrains.annotations.Nullable
 */
package net.advancedplugins.as.impl.utils.items;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.ColorUtils;
import net.advancedplugins.as.impl.utils.MathUtils;
import net.advancedplugins.as.impl.utils.Pair;
import net.advancedplugins.as.impl.utils.SkullCreator;
import net.advancedplugins.as.impl.utils.VanillaEnchants;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.HooksHandler;
import net.advancedplugins.as.impl.utils.hooks.plugins.ItemsAdderHook;
import net.advancedplugins.as.impl.utils.items.ItemBuilder;
import net.advancedplugins.as.impl.utils.items.ItemFlagFix;
import net.advancedplugins.as.impl.utils.nbt.utils.MinecraftVersion;
import net.advancedplugins.as.impl.utils.text.Text;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

public class ConfigItemCreator {
    private static HashMap<String, String> defaultPaths = null;

    public static ItemStack fromConfigSection(String string, String string2, Map<String, String> map, Map<String, String> map2, JavaPlugin javaPlugin) {
        File file = new File(javaPlugin.getDataFolder().getAbsolutePath() + File.separator + string);
        if (!file.exists()) {
            ConfigItemCreator.sendError("Unknown file!", string, null, null);
            return new ItemStack(Material.AIR);
        }
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        return ConfigItemCreator.fromConfigSection((FileConfiguration)yamlConfiguration, string2, map, map2);
    }

    public static ItemStack fromConfigSection(FileConfiguration fileConfiguration, ItemStack itemStack, String string, Map<String, String> map, Map<String, String> map2) {
        return ConfigItemCreator.fromConfigSection(fileConfiguration.getConfigurationSection(""), itemStack, string, map, map2, null);
    }

    public static ItemStack fromConfigSection(ConfigurationSection configurationSection, ItemStack itemStack, String string, Map<String, String> map, Map<String, String> map2, Player player) {
        return ConfigItemCreator.fromConfigSection(configurationSection, null, itemStack, string, null, map, map2, null, player);
    }

    public static ItemStack fromConfigSection(ConfigurationSection configurationSection, @Nullable ConfigurationSection configurationSection2, ItemStack itemStack, String string, String string2, Map<String, String> map, Map<String, String> map2, @Nullable Map<String, String> map3, Player player) {
        boolean bl;
        String string3;
        String string4;
        String[] stringArray;
        Object object;
        Object object2;
        Map map4 = (Map)defaultPaths.clone();
        String string5 = "config";
        Map map5 = (Map)defaultPaths.clone();
        if (map3 != null && !map3.isEmpty()) {
            map5.putAll(map3);
        }
        if (map2 != null && !map2.isEmpty()) {
            map4.putAll(map2);
        }
        ItemBuilder itemBuilder = new ItemBuilder(itemStack);
        String string6 = itemBuilder.toItemStack().getType().name();
        if (configurationSection.contains(string + "." + (String)map4.get("name"))) {
            object2 = ConfigItemCreator.format(configurationSection.getString(string + "." + (String)map4.get("name"), null), map, player);
            itemBuilder.setName((String)object2);
        } else if (configurationSection2 != null && configurationSection2.contains(string2 + "." + (String)map5.get("name"))) {
            object2 = ConfigItemCreator.format(configurationSection2.getString(string2 + "." + (String)map5.get("name"), null), map, player);
            itemBuilder.setName((String)object2);
        }
        if (configurationSection.contains(string + "." + (String)map4.get("lore"))) {
            object2 = ConfigItemCreator.format(configurationSection.getStringList(string + "." + (String)map4.get("lore")), map, player);
            itemBuilder.setLore((List<String>)object2);
        } else if (configurationSection2 != null && configurationSection2.contains(string2 + "." + (String)map5.get("lore"))) {
            object2 = ConfigItemCreator.format(configurationSection2.getStringList(string2 + "." + (String)map5.get("lore")), map, player);
            itemBuilder.setLore((List<String>)object2);
        }
        if (configurationSection.contains(string + "." + (String)map4.get("item-flags")) || configurationSection2 != null && configurationSection2.contains(string2 + "." + (String)map5.get("item-flags"))) {
            object2 = configurationSection.contains(string + "." + (String)map4.get("item-flags")) ? configurationSection.getStringList(string + "." + (String)map4.get("item-flags")) : (configurationSection2 != null ? configurationSection2.getStringList(string2 + "." + (String)map5.get("item-flags")) : new ArrayList<String>());
            object = ConfigItemCreator.format(object2, map, player);
            if (!object.isEmpty() && object.get(0).equalsIgnoreCase("all")) {
                itemBuilder.addItemFlag(ItemFlagFix.hideAllAttributes());
            } else {
                stringArray = object.iterator();
                while (stringArray.hasNext()) {
                    string4 = (String)stringArray.next();
                    boolean bl2 = false;
                    string3 = string4.toUpperCase(Locale.ROOT);
                    for (ItemFlag itemFlag : ItemFlagFix.hideAllAttributes()) {
                        if (!itemFlag.name().equals(string3)) continue;
                        bl2 = true;
                        break;
                    }
                    if (!bl2) {
                        ConfigItemCreator.sendError("Specified ItemFlag doesn't exist!", string5, string, string4);
                        continue;
                    }
                    itemBuilder.addItemFlag(ItemFlag.valueOf((String)string4));
                }
            }
        }
        if (configurationSection.contains(string + "." + (String)map4.get("armor-trim"))) {
            object2 = configurationSection.getString(string + "." + (String)map4.get("armor-trim")).split(";");
            object = object2[0];
            stringArray = object2[1];
            itemBuilder.setArmorTrim((String)object, (String)stringArray);
        } else if (configurationSection2 != null && configurationSection2.contains(string2 + "." + (String)map5.get("armor-trim"))) {
            object2 = configurationSection2.getString(string2 + "." + (String)map5.get("armor-trim")).split(";");
            object = object2[0];
            stringArray = object2[1];
            itemBuilder.setArmorTrim((String)object, (String)stringArray);
        }
        if (configurationSection.contains(string + "." + (String)map4.get("custom-model-data"))) {
            if (MinecraftVersion.getVersionNumber() >= 1140) {
                int n = configurationSection.getInt(string + "." + (String)map4.get("custom-model-data"));
                itemBuilder.setCustomModelData(n);
            }
        } else if (configurationSection2 != null && configurationSection2.contains(string2 + "." + (String)map5.get("custom-model-data")) && MinecraftVersion.getVersionNumber() >= 1140) {
            int n = configurationSection2.getInt(string2 + "." + (String)map5.get("custom-model-data"));
            itemBuilder.setCustomModelData(n);
        }
        if (configurationSection.contains(string + "." + (String)map4.get("unbreakable"))) {
            itemBuilder.setUnbreakable(configurationSection.getBoolean(string + "." + (String)map4.get("unbreakable")));
        } else if (configurationSection2 != null && configurationSection2.contains(string2 + "." + (String)map5.get("unbreakable"))) {
            itemBuilder.setUnbreakable(configurationSection2.getBoolean(string2 + "." + (String)map5.get("unbreakable")));
        }
        if (configurationSection.contains(string + "." + (String)map4.get("enchantments")) || configurationSection2 != null && configurationSection2.contains(string2 + "." + (String)map5.get("enchantments"))) {
            ArrayList<String> arrayList = configurationSection.contains(string + "." + (String)map4.get("enchantments")) ? configurationSection.getStringList(string + "." + (String)map4.get("enchantments")) : (configurationSection2 != null ? configurationSection2.getStringList(string2 + "." + (String)map5.get("enchantments")) : new ArrayList<String>());
            object = ConfigItemCreator.format(arrayList, map, player);
            stringArray = object.iterator();
            while (stringArray.hasNext()) {
                string4 = stringArray.next();
                Pair<String, Integer> pair = ASManager.parseEnchantment(string4);
                if (pair == null) continue;
                string3 = VanillaEnchants.displayNameToEnchant(pair.getKey());
                if (string3 == null) {
                    ConfigItemCreator.sendError("Specified vanilla enchantment doesn't exist!", string5, string, pair.getKey());
                    continue;
                }
                itemBuilder.addUnsafeEnchantment((Enchantment)string3, pair.getValue());
            }
        }
        if (configurationSection.contains(string + "." + (String)map4.get("custom-enchantments")) || configurationSection2 != null && configurationSection2.contains(string2 + "." + (String)map5.get("custom-enchantments"))) {
            ArrayList<String> arrayList = configurationSection.contains(string + "." + (String)map4.get("custom-enchantments")) ? configurationSection.getStringList(string + "." + (String)map4.get("custom-enchantments")) : (configurationSection2 != null ? configurationSection2.getStringList(string2 + "." + (String)map5.get("custom-enchantments")) : new ArrayList<String>());
            object = ConfigItemCreator.format(arrayList, map, player);
            stringArray = object.iterator();
            while (stringArray.hasNext()) {
                string4 = stringArray.next();
                Pair<String, Integer> pair = ASManager.parseEnchantment(string4);
                if (pair == null) continue;
                itemBuilder.addCustomEnchantment(pair.getKey(), pair.getValue());
            }
        }
        if ((string6.contains("LEATHER_") || string6.contains("FIREWORK_STAR")) && (configurationSection.contains(string + "." + (String)map4.get("rgb-color")) || configurationSection2 != null && configurationSection2.contains(string2 + "." + (String)map5.get("rgb-color")))) {
            String string7 = configurationSection.contains(string + "." + (String)map4.get("rgb-color")) ? configurationSection.getString(string + "." + (String)map4.get("rgb-color")) : (configurationSection2 != null ? configurationSection2.getString(string2 + "." + (String)map5.get("rgb-color")) : null);
            object = ConfigItemCreator.format(string7, map, player);
            stringArray = ((String)object).split(";");
            if (stringArray.length != 3) {
                ConfigItemCreator.sendError("RGB color must contain 3 values in the format \"255;255;255\"!", string5, string, object);
                return new ItemStack(Material.AIR);
            }
            if (!(MathUtils.isInteger(stringArray[0]) && MathUtils.isInteger(stringArray[1]) && MathUtils.isInteger(stringArray[2]))) {
                ConfigItemCreator.sendError("RGB values must be between 0-255!", string5, string, stringArray[0]);
                return new ItemStack(Material.AIR);
            }
            int n = MathUtils.clamp(Integer.parseInt(stringArray[0]), 0, 255);
            int n2 = MathUtils.clamp(Integer.parseInt(stringArray[1]), 0, 255);
            int n3 = MathUtils.clamp(Integer.parseInt(stringArray[2]), 0, 255);
            Color color = Color.fromRGB((int)n, (int)n2, (int)n3);
            if (string6.contains("LEATHER_")) {
                itemBuilder.setColor(color);
            } else if (string6.contains("FIREWORK_STAR")) {
                FireworkEffectMeta fireworkEffectMeta = (FireworkEffectMeta)itemBuilder.getItemMeta();
                fireworkEffectMeta.setEffect(FireworkEffect.builder().withColor(color).build());
            }
        }
        if (configurationSection.contains(string + "." + (String)map4.get("force-glow"))) {
            boolean bl3 = configurationSection.getBoolean(string + "." + (String)map4.get("force-glow"));
            if (bl3) {
                itemBuilder.setGlowing(true);
            }
        } else if (configurationSection2 != null && configurationSection2.contains(string2 + "." + (String)map5.get("force-glow")) && (bl = configurationSection2.getBoolean(string2 + "." + (String)map5.get("force-glow")))) {
            itemBuilder.setGlowing(true);
        }
        return itemBuilder.toItemStack();
    }

    public static ItemStack fromConfigSection(FileConfiguration fileConfiguration, String string, Map<String, String> map, Map<String, String> map2) {
        return ConfigItemCreator.fromConfigSection(fileConfiguration.getConfigurationSection(""), string, map, map2);
    }

    public static ItemStack fromConfigSection(ConfigurationSection configurationSection, String string, Map<String, String> map, Map<String, String> map2) {
        return ConfigItemCreator.fromConfigSection(configurationSection, string, map, map2, null);
    }

    public static ItemStack fromConfigSection(ConfigurationSection configurationSection, ConfigurationSection configurationSection2, String string, String string2, Map<String, String> map, Map<String, String> map2, Map<String, String> map3) {
        return ConfigItemCreator.fromConfigSection(configurationSection, configurationSection2, string, string2, map, map2, map3, null);
    }

    public static ItemStack fromConfigSection(ConfigurationSection configurationSection, String string, Map<String, String> map, Map<String, String> map2, Player player) {
        return ConfigItemCreator.fromConfigSection(configurationSection, null, string, null, map, map2, null, player);
    }

    public static ItemStack fromConfigSection(ConfigurationSection configurationSection, @Nullable ConfigurationSection configurationSection2, String string, @Nullable String string2, Map<String, String> map, Map<String, String> map2, @Nullable Map<String, String> map3, Player player) {
        String string3;
        String string4;
        Object object;
        String string5;
        String string6 = "config";
        Map map4 = (Map)defaultPaths.clone();
        Map map5 = (Map)defaultPaths.clone();
        if (map3 != null && !map3.isEmpty()) {
            map5.putAll(map3);
        }
        if (map2 != null && !map2.isEmpty()) {
            map4.putAll(map2);
        }
        String string7 = configurationSection.contains(string + "." + (String)map4.get("type")) ? configurationSection.getString(string + "." + (String)map4.get("type"), null) : (configurationSection2 != null ? configurationSection2.getString(string2 + "." + (String)map5.get("type")) : null);
        String string8 = string5 = string7 != null ? ConfigItemCreator.format(string7, map, player) : null;
        byte by = (byte)(configurationSection.contains(string + "." + (String)map4.get("id")) ? configurationSection.getInt(string + "." + (String)map4.get("id")) : (configurationSection2 != null ? configurationSection2.getInt(string2 + "." + (String)map5.get("id")) : 0));
        int n = MathUtils.clamp(ASManager.parseInt(configurationSection.contains(string + "." + (String)map4.get("amount")) ? configurationSection.getString(string + "." + (String)map4.get("amount"), "1") : (configurationSection2 != null ? configurationSection2.getString(string2 + "." + (String)map5.get("amount"), "1") : "1"), 1), 1, 64);
        Object object2 = configurationSection.contains(string + ".advanced-heads") ? configurationSection.get(string + ".advanced-heads") : (object = configurationSection2 != null ? configurationSection2.get(string2 + ".advanced-heads") : null);
        String string9 = configurationSection.contains(string + "." + (String)map4.get("head")) ? configurationSection.getString(string + "." + (String)map4.get("head")) : (string4 = configurationSection2 != null ? configurationSection2.getString(string2 + "." + (String)map5.get("head")) : null);
        String string10 = configurationSection.contains(string + "." + (String)map4.get("itemsadder")) ? configurationSection.getString(string + "." + (String)map4.get("itemsadder")) : (string3 = configurationSection2 != null ? configurationSection2.getString(string2 + "." + (String)map5.get("itemsadder")) : null);
        ItemStack itemStack = string3 != null ? ((ItemsAdderHook)HooksHandler.getHook(HookPlugin.ITEMSADDER)).getByName(string3) : (string4 != null ? SkullCreator.itemFromBase64(string4) : ASManager.matchMaterial(string5, n, by));
        if (itemStack == null) {
            ConfigItemCreator.sendError("Specified material doesn't exist!", string6, string, string5);
            return new ItemStack(Material.AIR);
        }
        return ConfigItemCreator.fromConfigSection(configurationSection, configurationSection2, itemStack, string, string2, map, map2, map5, player);
    }

    private static String format(String string, Map<String, String> map, Player player) {
        string = ConfigItemCreator.placeholders(string, map, player);
        string = ColorUtils.format(string);
        return string;
    }

    private static List<String> format(List<String> list, Map<String, String> map, Player player) {
        return ColorUtils.format(ConfigItemCreator.placeholders(list, map, player));
    }

    private static String placeholders(String string, Map<String, String> map, Player player) {
        if (map != null && string != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                int n;
                StringBuilder stringBuilder;
                String[] stringArray;
                String string2;
                if (string.contains("%description%") && entry.getKey().contains("%description%") && entry.getValue().contains("\n")) {
                    string2 = string;
                    string = "";
                    stringArray = entry.getValue().split("\n");
                    stringBuilder = new StringBuilder(string);
                    for (n = 0; n < stringArray.length; ++n) {
                        stringBuilder.append(string2.replace(entry.getKey(), stringArray[n]));
                        if (n + 1 == stringArray.length) continue;
                        stringBuilder.append("\n");
                    }
                    string = stringBuilder.toString();
                    continue;
                }
                if (string.contains("%level-description%") && entry.getKey().contains("%level-description%") && entry.getValue().contains("\n")) {
                    string2 = string;
                    string = "";
                    stringArray = entry.getValue().split("\n");
                    stringBuilder = new StringBuilder(string);
                    for (n = 0; n < stringArray.length; ++n) {
                        stringBuilder.append(string2.replace(entry.getKey(), stringArray[n]));
                        if (n + 1 == stringArray.length) continue;
                        stringBuilder.append("\n");
                    }
                    string = stringBuilder.toString();
                    continue;
                }
                if (entry.getValue() == null) continue;
                string = string.replace(entry.getKey(), entry.getValue());
            }
        }
        if (string != null && player != null) {
            return Text.parsePapi(string, (OfflinePlayer)player);
        }
        return string;
    }

    private static List<String> placeholders(List<String> list, Map<String, String> map, Player player) {
        ArrayList<String> arrayList = new ArrayList<String>();
        if (map != null) {
            for (String string2 : list) {
                if ((string2 = ConfigItemCreator.placeholders(string2, map, player)).contains("\n")) {
                    String[] stringArray = string2.split("\\n");
                    String string3 = "";
                    for (String string4 : stringArray) {
                        arrayList.add(string3 + string4);
                        string3 = ColorUtils.getLastColor(string4);
                    }
                    continue;
                }
                arrayList.add(string2);
            }
        } else {
            if (player != null) {
                return new ArrayList<String>(list.stream().map(string -> Text.parsePapi(string, (OfflinePlayer)player)).toList());
            }
            return list;
        }
        return arrayList;
    }

    private static String addPunctuation(String object) {
        boolean bl;
        boolean bl2 = bl = ((String)object).endsWith(".") || ((String)object).endsWith("!") || ((String)object).endsWith("?");
        if (!bl) {
            object = (String)object + ".";
        }
        return object;
    }

    private static void sendError(String string, String string2, String string3, Object object) {
        Bukkit.getLogger().severe("Something went wrong while creating an item! " + ConfigItemCreator.addPunctuation(string) + " File: " + string2 + "  Config Path: " + string3 + "  Value: " + String.valueOf(object));
    }

    public static void setDefaultPaths(HashMap<String, String> hashMap) {
        defaultPaths = hashMap;
    }

    public static HashMap<String, String> getDefaultPaths() {
        return defaultPaths;
    }

    static {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("type", "type");
        hashMap.put("id", "id");
        hashMap.put("amount", "amount");
        hashMap.put("name", "name");
        hashMap.put("lore", "lore");
        hashMap.put("item-flags", "item-flags");
        hashMap.put("custom-model-data", "custom-model-data");
        hashMap.put("force-glow", "force-glow");
        hashMap.put("enchantments", "enchantments");
        hashMap.put("custom-enchantments", "custom-enchantments");
        hashMap.put("rgb-color", "rgb-color");
        hashMap.put("itemsadder", "itemsadder");
        hashMap.put("armor-trim", "armor-trim");
        hashMap.put("unbreakable", "unbreakable");
        hashMap.put("head", "head");
        hashMap.put("owner", "head");
        ConfigItemCreator.setDefaultPaths(hashMap);
    }
}

