/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Bukkit
 *  org.bukkit.Color
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.LeatherArmorMeta
 *  org.bukkit.inventory.meta.SkullMeta
 *  org.bukkit.util.Vector
 */
package com.magmaguy.magmacore.config;

import com.magmaguy.magmacore.util.ConfigurationLocation;
import com.magmaguy.magmacore.util.ItemStackGenerator;
import com.magmaguy.magmacore.util.Logger;
import com.magmaguy.magmacore.util.VersionChecker;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.Vector;

public abstract class CustomConfigFields {
    protected String filename;
    protected boolean isEnabled;
    protected FileConfiguration fileConfiguration;
    protected File file;

    public CustomConfigFields(String filename, boolean isEnabled) {
        this.filename = filename.contains(".yml") ? filename : filename + ".yml";
        this.isEnabled = isEnabled;
    }

    public CompletableFuture<Void> setEnabledAndSave(boolean enabled) {
        this.isEnabled = enabled;
        this.fileConfiguration.set("isEnabled", (Object)enabled);
        return CompletableFuture.runAsync(() -> {
            try {
                this.fileConfiguration.save(this.file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public abstract void processConfigFields();

    protected boolean configHas(String configKey) {
        return this.fileConfiguration.contains(configKey);
    }

    protected String processString(String path, String value, String pluginDefault, boolean forceWriteDefault) {
        if (!this.configHas(path)) {
            if (forceWriteDefault || !Objects.equals(value, pluginDefault)) {
                this.fileConfiguration.addDefault(path, (Object)value);
            }
            return value;
        }
        try {
            return this.fileConfiguration.getString(path);
        } catch (Exception ex) {
            Logger.warn("File " + this.filename + " has an incorrect entry for " + path);
            Logger.warn("Entry: " + value);
            return value;
        }
    }

    public List<Object> processList(String path, List<Object> value, List<Object> pluginDefault, boolean forceWriteDefault) {
        if (!this.configHas(path)) {
            if (forceWriteDefault || value != pluginDefault) {
                this.fileConfiguration.addDefault(path, value);
            }
            return value;
        }
        try {
            return new ArrayList<Object>(Objects.requireNonNull(this.fileConfiguration.getList(path)));
        } catch (Exception ex) {
            Logger.warn("File " + this.filename + " has an incorrect entry for " + path);
            Logger.warn("Entry: " + String.valueOf(value));
            return value;
        }
    }

    public List<String> processStringList(String path, List<String> value, List<String> pluginDefault, boolean forceWriteDefault) {
        if (!this.configHas(path)) {
            if (forceWriteDefault || value != pluginDefault) {
                this.fileConfiguration.addDefault(path, value);
            }
            return value;
        }
        try {
            return new ArrayList<String>(this.fileConfiguration.getStringList(path));
        } catch (Exception ex) {
            Logger.warn("File " + this.filename + " has an incorrect entry for " + path);
            Logger.warn("Entry: " + String.valueOf(value));
            return value;
        }
    }

    protected List<World> processWorldList(String path, List<World> value, List<World> pluginDefault, boolean forceWriteDefault) {
        if (!this.configHas(path)) {
            if (value != null && (forceWriteDefault || value != pluginDefault)) {
                this.processStringList(path, this.worldListToStringListConverter(value), this.worldListToStringListConverter(pluginDefault), forceWriteDefault);
            }
            return value;
        }
        try {
            List<String> validWorldStrings = this.processStringList(path, this.worldListToStringListConverter(pluginDefault), this.worldListToStringListConverter(value), forceWriteDefault);
            ArrayList<World> validWorlds = new ArrayList<World>();
            if (!validWorldStrings.isEmpty()) {
                for (String string : validWorldStrings) {
                    World world = Bukkit.getWorld((String)string);
                    if (world == null) continue;
                    validWorlds.add(world);
                }
            }
            return validWorlds;
        } catch (Exception ex) {
            Logger.warn("File " + this.filename + " has an incorrect entry for " + path);
            Logger.warn("Entry: " + String.valueOf(value));
            return value;
        }
    }

    private List<String> worldListToStringListConverter(List<World> pluginDefault) {
        if (pluginDefault == null) {
            return null;
        }
        ArrayList<String> newList = new ArrayList<String>();
        pluginDefault.forEach(element -> newList.add(element.getName()));
        return newList;
    }

    protected <T extends Enum<T>> List<T> processEnumList(String path, List<T> value, List<T> pluginDefault, Class<T> enumClass, boolean forceWriteDefault) {
        if (!this.configHas(path)) {
            if (forceWriteDefault || value != pluginDefault) {
                this.processStringList(path, this.enumListToStringListConverter(value), this.enumListToStringListConverter(pluginDefault), forceWriteDefault);
            }
            return value;
        }
        try {
            ArrayList newList = new ArrayList();
            List<String> stringList = this.processStringList(path, this.enumListToStringListConverter(value), this.enumListToStringListConverter(pluginDefault), forceWriteDefault);
            stringList.forEach(string -> {
                try {
                    newList.add(Enum.valueOf(enumClass, string.toUpperCase(Locale.ROOT)));
                } catch (Exception ex) {
                    if (string != null && string.toUpperCase(Locale.ROOT).endsWith("_SPEAR") && VersionChecker.serverVersionOlderThan(21, 11)) {
                        return;
                    }
                    Logger.warn(this.filename + " : Value " + string + " is not a valid for " + path + " ! This may be due to your server version, or due to an invalid value!");
                }
            });
            return newList;
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.warn("File " + this.filename + " has an incorrect entry for " + path);
            Logger.warn("Entry: " + String.valueOf(value));
            return value;
        }
    }

    private <T extends Enum<T>> List<String> enumListToStringListConverter(List<T> list) {
        if (list == null) {
            return Collections.emptyList();
        }
        ArrayList<String> newList = new ArrayList<String>();
        list.forEach(element -> newList.add(element.toString()));
        return newList;
    }

    protected int processInt(String path, int value, int pluginDefault, boolean forceWriteDefault) {
        if (!this.configHas(path)) {
            if (forceWriteDefault || value != pluginDefault) {
                this.fileConfiguration.addDefault(path, (Object)value);
            }
            return value;
        }
        try {
            return this.fileConfiguration.getInt(path);
        } catch (Exception ex) {
            Logger.warn("File " + this.filename + " has an incorrect entry for " + path);
            Logger.warn("Entry: " + value);
            return value;
        }
    }

    protected long processLong(String path, long value, long pluginDefault, boolean forceWriteDefault) {
        if (!this.configHas(path)) {
            if (forceWriteDefault || value != pluginDefault) {
                this.fileConfiguration.addDefault(path, (Object)value);
            }
            return value;
        }
        try {
            return this.fileConfiguration.getLong(path);
        } catch (Exception ex) {
            Logger.warn("File " + this.filename + " has an incorrect entry for " + path);
            Logger.warn("Entry: " + value);
            return value;
        }
    }

    protected double processDouble(String path, double value, double pluginDefault, boolean forceWriteDefault) {
        if (!this.configHas(path)) {
            if (forceWriteDefault || value != pluginDefault) {
                this.fileConfiguration.addDefault(path, (Object)value);
            }
            return value;
        }
        try {
            return this.fileConfiguration.getDouble(path);
        } catch (Exception ex) {
            Logger.warn("File " + this.filename + " has an incorrect entry for " + path);
            Logger.warn("Entry: " + value);
            return value;
        }
    }

    protected Double processDouble(String path, Double value, Double pluginDefault, boolean forceWriteDefault) {
        if (!this.configHas(path)) {
            if (forceWriteDefault || !Objects.equals(value, pluginDefault)) {
                this.fileConfiguration.addDefault(path, (Object)value);
            }
            return value;
        }
        try {
            return this.fileConfiguration.getDouble(path);
        } catch (Exception ex) {
            Logger.warn("File " + this.filename + " has an incorrect entry for " + path);
            Logger.warn("Entry: " + value);
            return value;
        }
    }

    protected boolean processBoolean(String path, boolean value, boolean pluginDefault, boolean forceWriteDefault) {
        if (!this.configHas(path)) {
            if (forceWriteDefault || value != pluginDefault) {
                this.fileConfiguration.addDefault(path, (Object)value);
            }
            return value;
        }
        try {
            return this.fileConfiguration.getBoolean(path);
        } catch (Exception ex) {
            Logger.warn("File " + this.filename + " has an incorrect entry for " + path);
            Logger.warn("Entry: " + value);
            return value;
        }
    }

    public <T extends Enum<T>> T processEnum(String path, T value, T pluginDefault, Class<T> enumClass, boolean forceWriteDefault) {
        if (!this.configHas(path)) {
            if (forceWriteDefault || value != pluginDefault) {
                String valueString = null;
                if (value != null) {
                    valueString = value.toString().toUpperCase(Locale.ROOT);
                }
                String pluginDefaultString = null;
                if (pluginDefault != null) {
                    pluginDefaultString = pluginDefault.toString().toUpperCase(Locale.ROOT);
                }
                this.processString(path, valueString, pluginDefaultString, forceWriteDefault);
            }
            return value;
        }
        try {
            String rawValue = this.fileConfiguration.getString(path).toUpperCase(Locale.ROOT);
            if (!VersionChecker.serverVersionOlderThan(21, 9) && rawValue.equals("CHAIN")) {
                return Enum.valueOf(enumClass, "IRON_CHAIN");
            }
            return Enum.valueOf(enumClass, rawValue);
        } catch (Exception ex) {
            String rawValue = this.fileConfiguration.getString(path);
            if (rawValue != null && rawValue.toUpperCase(Locale.ROOT).endsWith("_SPEAR") && VersionChecker.serverVersionOlderThan(21, 11)) {
                return pluginDefault;
            }
            Logger.warn("File " + this.filename + " has an incorrect entry for " + path);
            Logger.warn("Entry: " + rawValue);
            value = null;
            if (value == null) {
                return pluginDefault;
            }
            return value;
        }
    }

    public ItemStack processItemStack(String path, ItemStack value, ItemStack pluginDefault, boolean forceWriteDefault) {
        if (!this.configHas(path)) {
            if (forceWriteDefault || value != pluginDefault) {
                this.processString(path, this.itemStackDeserializer(value), this.itemStackDeserializer(pluginDefault), forceWriteDefault);
            }
            return value;
        }
        try {
            String materialString = this.processString(path, this.itemStackDeserializer(value), this.itemStackDeserializer(pluginDefault), forceWriteDefault);
            if (materialString == null) {
                return null;
            }
            if (materialString.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {
                ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta skullMeta = (SkullMeta)playerHead.getItemMeta();
                skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer((UUID)UUID.fromString(materialString)));
                playerHead.setItemMeta((ItemMeta)skullMeta);
                return playerHead;
            }
            String baseMaterial = materialString.contains(":") ? materialString.split(":")[0] : materialString;
            Material material = Material.getMaterial((String)(baseMaterial = CustomConfigFields.mapLegacyMaterialName(baseMaterial)));
            if (material == null) {
                if (baseMaterial.endsWith("_SPEAR") && VersionChecker.serverVersionOlderThan(21, 11)) {
                    return null;
                }
                throw new IllegalArgumentException("Unknown material: " + baseMaterial);
            }
            if (materialString.contains(":")) {
                ItemStack itemStack = ItemStackGenerator.generateItemStack(material);
                if (baseMaterial.contains("leather_") || baseMaterial.contains("LEATHER_")) {
                    LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta)itemStack.getItemMeta();
                    leatherArmorMeta.setColor(Color.fromRGB((int)Integer.parseInt(materialString.split(":")[1], 16)));
                    itemStack.setItemMeta((ItemMeta)leatherArmorMeta);
                } else {
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.setCustomModelData(Integer.valueOf(Integer.parseInt(materialString.split(":")[1])));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            }
            return ItemStackGenerator.generateItemStack(material);
        } catch (Exception ex) {
            Logger.warn("File " + this.filename + " has an incorrect entry for " + path);
            Logger.warn("Entry: " + String.valueOf(value));
            return value;
        }
    }

    private static String mapLegacyMaterialName(String material) {
        if (material.startsWith("GOLD_")) {
            return "GOLDEN_" + material.substring(5);
        }
        if (material.startsWith("CHAIN_")) {
            return "CHAINMAIL_" + material.substring(6);
        }
        return material;
    }

    public Map<String, Object> processMap(String path, Map<String, Object> value) {
        if (!this.configHas(path) && value != null) {
            this.fileConfiguration.addDefaults(value);
        }
        if (this.fileConfiguration.get(path) == null) {
            return Collections.emptyMap();
        }
        return this.fileConfiguration.getConfigurationSection(path).getValues(false);
    }

    public Map<String, Object> processMapWithKey(String path, Map<String, Object> value) {
        if (!this.configHas(path) && value != null) {
            this.fileConfiguration.addDefault(path, value);
            this.fileConfiguration.createSection(path, value);
        }
        if (this.fileConfiguration.get(path) == null) {
            return Collections.emptyMap();
        }
        return this.fileConfiguration.getConfigurationSection(path).getValues(false);
    }

    public ConfigurationSection processConfigurationSection(String path, Map<String, Object> value) {
        if (!this.configHas(path) && value != null) {
            this.fileConfiguration.addDefaults(value);
        }
        ConfigurationSection newValue = this.fileConfiguration.getConfigurationSection(path);
        return newValue;
    }

    private String itemStackDeserializer(ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        }
        return itemStack.getType().toString();
    }

    protected Location processLocation(String path, Location value, String pluginDefault, boolean forceWriteDefault) {
        if (!this.configHas(path)) {
            if (forceWriteDefault || !Objects.equals(value, pluginDefault)) {
                this.fileConfiguration.addDefault(path, (Object)ConfigurationLocation.deserialize(value));
            }
            return value;
        }
        try {
            return ConfigurationLocation.serialize(this.fileConfiguration.getString(path));
        } catch (Exception ex) {
            Logger.warn("File " + this.filename + " has an incorrect entry for " + path);
            Logger.warn("Entry: " + String.valueOf(value));
            return null;
        }
    }

    protected Vector processVector(String path, Vector value, Vector pluginDefault, boolean forceWriteDefault) {
        if (!this.configHas(path)) {
            if ((forceWriteDefault || !Objects.equals(value, pluginDefault)) && pluginDefault != null) {
                String vectorString = value.getX() + "," + value.getY() + "," + value.getZ();
                this.fileConfiguration.addDefault(path, (Object)vectorString);
            }
            return value;
        }
        try {
            String string = this.fileConfiguration.getString(path);
            if (string == null) {
                return null;
            }
            String[] strings = string.split(",");
            if (strings.length < 3) {
                Logger.warn("File " + this.filename + " has an incorrect entry for " + path);
                return null;
            }
            return new Vector(Double.parseDouble(strings[0]), Double.parseDouble(strings[1]), Double.parseDouble(strings[2]));
        } catch (Exception ex) {
            Logger.warn("File " + this.filename + " has an incorrect entry for " + path);
            Logger.warn("Entry: " + String.valueOf(value));
            return null;
        }
    }

    @Generated
    public String getFilename() {
        return this.filename;
    }

    @Generated
    public boolean isEnabled() {
        return this.isEnabled;
    }

    @Generated
    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    @Generated
    public FileConfiguration getFileConfiguration() {
        return this.fileConfiguration;
    }

    @Generated
    public void setFileConfiguration(FileConfiguration fileConfiguration) {
        this.fileConfiguration = fileConfiguration;
    }

    @Generated
    public File getFile() {
        return this.file;
    }

    @Generated
    public void setFile(File file) {
        this.file = file;
    }
}

