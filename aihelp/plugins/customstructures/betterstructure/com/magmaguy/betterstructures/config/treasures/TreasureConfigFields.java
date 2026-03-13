/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.NamespacedKey
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.MemorySection
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.loot.LootTables
 */
package com.magmaguy.betterstructures.config.treasures;

import com.magmaguy.betterstructures.MetadataHandler;
import com.magmaguy.betterstructures.chests.ChestContents;
import com.magmaguy.betterstructures.util.DefaultChestContents;
import com.magmaguy.magmacore.config.CustomConfigFields;
import com.magmaguy.magmacore.util.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.loot.LootTables;

public class TreasureConfigFields
extends CustomConfigFields {
    private final Map<Material, List<ConfigurationEnchantment>> enchantmentSettings = new HashMap<Material, List<ConfigurationEnchantment>>();
    private final List<String> seenInvalidKeys = new ArrayList<String>();
    private Map<String, Object> rawLoot = new HashMap<String, Object>();
    private Map<String, Object> rawEnchantmentSettings = new HashMap<String, Object>();
    private ChestContents chestContents = null;
    private double mean = 4.0;
    private double standardDeviation = 3.0;
    private LootTables vanillaTreasure = null;

    public TreasureConfigFields(String filename, boolean isEnabled) {
        super(filename, isEnabled);
    }

    @Override
    public void processConfigFields() {
        this.isEnabled = this.processBoolean("isEnabled", this.isEnabled, true, true);
        this.rawLoot = this.processMapWithKey("items", this.rawLoot);
        this.rawEnchantmentSettings = this.processMapWithKey("procedurallyGeneratedItemSettings", DefaultChestContents.generateProcedurallyGeneratedItems());
        this.mean = this.processDouble("mean", this.mean, 4.0, true);
        this.standardDeviation = this.processDouble("standardDeviation", this.standardDeviation, 3.0, true);
        this.vanillaTreasure = this.parseVanillaTreasure(this.processString("vanillaTreasure", null, null, false));
        this.chestContents = new ChestContents(this);
        this.parseEnchantmentSettings();
    }

    private LootTables parseVanillaTreasure(String vanillaTreasureString) {
        if (vanillaTreasureString == null || vanillaTreasureString.isEmpty()) {
            return null;
        }
        try {
            return LootTables.valueOf((String)vanillaTreasureString.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            Logger.warn("Invalid vanillaTreasure value '" + vanillaTreasureString + "' in configuration file " + this.filename + ". Please use a valid LootTables enum value (e.g., BURIED_TREASURE, SHIPWRECK_TREASURE, etc.)");
            return null;
        }
    }

    private void parseEnchantmentSettings() {
        for (Map.Entry<String, Object> stringObjectEntry : this.rawEnchantmentSettings.entrySet()) {
            Material material = Material.matchMaterial((String)stringObjectEntry.getKey());
            if (material == null) {
                Logger.warn("Incorrect material entry for enchantment settings of the configuration file " + this.filename);
                continue;
            }
            ArrayList<ConfigurationEnchantment> configurationEnchantments = new ArrayList<ConfigurationEnchantment>();
            Map enchantments = ((MemorySection)stringObjectEntry.getValue()).getValues(false);
            for (Map.Entry enchantmentsEntry : enchantments.entrySet()) {
                Enchantment enchantment = Enchantment.getByKey((NamespacedKey)NamespacedKey.minecraft((String)((String)enchantmentsEntry.getKey())));
                if (enchantment == null && !this.seenInvalidKeys.contains(enchantmentsEntry.getKey())) {
                    Logger.info("Failed to get valid enchantment from key " + (String)enchantmentsEntry.getKey() + " in configuration file " + this.filename + " ! This is almost certainly because another plugin is using enchantments that are pretending to be vanilla Minecraft enchantments, when they aren't, and doing so in a way that doesn't allow items to be enchanted via normal means. This enchantment will be ignored for generating items, you can ignore this warning if you didn't plan to use this enchantment in the first place. Warnings about this specific enchantment will now be suppressed.");
                    this.seenInvalidKeys.add((String)enchantmentsEntry.getKey());
                    continue;
                }
                int minLevel = 1;
                int maxLevel = 1;
                double chance = 0.0;
                block12: for (Map.Entry enchantmentValue : ((ConfigurationSection)enchantmentsEntry.getValue()).getValues(false).entrySet()) {
                    switch (((String)enchantmentValue.getKey()).toLowerCase(Locale.ROOT)) {
                        case "minlevel": {
                            minLevel = Integer.parseInt(enchantmentValue.getValue().toString());
                            continue block12;
                        }
                        case "maxlevel": {
                            maxLevel = Integer.parseInt(enchantmentValue.getValue().toString());
                            continue block12;
                        }
                        case "chance": {
                            chance = Double.parseDouble(enchantmentValue.getValue().toString());
                            continue block12;
                        }
                    }
                    Logger.warn("Invalid key for setting " + (String)enchantmentValue.getKey() + " in file " + this.filename);
                }
                configurationEnchantments.add(new ConfigurationEnchantment(this, enchantment, minLevel, maxLevel, chance));
            }
            this.enchantmentSettings.put(material, configurationEnchantments);
        }
    }

    public void addChestEntry(Map<String, Object> entry, String rarity, Player player) {
        List mapList = ((ConfigurationSection)this.rawLoot.get(rarity)).getMapList("items");
        mapList.add(entry);
        this.fileConfiguration.set("items." + rarity, (Object)Map.of((Object)"weight", (Object)((ConfigurationSection)this.rawLoot.get(rarity)).getDouble("weight"), (Object)"items", (Object)mapList));
        try {
            this.fileConfiguration.save(this.file);
        } catch (Exception ex) {
            player.sendMessage("[BetterStructures] Failed to save entry to file! Report this to the developer.");
            return;
        }
        MetadataHandler.PLUGIN.onDisable();
        MetadataHandler.PLUGIN.onLoad();
        MetadataHandler.PLUGIN.onEnable();
        player.sendMessage("[BetterStructures] Reloaded plugin to add chest entry! It should now be live.");
    }

    public Map<Material, List<ConfigurationEnchantment>> getEnchantmentSettings() {
        return this.enchantmentSettings;
    }

    public Map<String, Object> getRawLoot() {
        return this.rawLoot;
    }

    public void setRawLoot(Map<String, Object> rawLoot) {
        this.rawLoot = rawLoot;
    }

    public void setRawEnchantmentSettings(Map<String, Object> rawEnchantmentSettings) {
        this.rawEnchantmentSettings = rawEnchantmentSettings;
    }

    public ChestContents getChestContents() {
        return this.chestContents;
    }

    public void setChestContents(ChestContents chestContents) {
        this.chestContents = chestContents;
    }

    public double getMean() {
        return this.mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getStandardDeviation() {
        return this.standardDeviation;
    }

    public void setStandardDeviation(double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public LootTables getVanillaTreasure() {
        return this.vanillaTreasure;
    }

    public void setVanillaTreasure(LootTables vanillaTreasure) {
        this.vanillaTreasure = vanillaTreasure;
    }

    public class ConfigurationEnchantment {
        private final Enchantment enchantment;
        private final int minLevel;
        private final int maxLevel;
        private final double chance;

        public ConfigurationEnchantment(TreasureConfigFields this$0, Enchantment enchantment, int minLevel, int maxLevel, double chance) {
            this.enchantment = enchantment;
            this.minLevel = minLevel;
            this.maxLevel = maxLevel;
            this.chance = chance;
        }

        public void rollEnchantment(ItemMeta itemMeta) {
            if (ThreadLocalRandom.current().nextDouble() >= this.chance) {
                return;
            }
            int level = ThreadLocalRandom.current().nextInt(this.minLevel, this.maxLevel + 1);
            if (itemMeta != null && this.enchantment != null) {
                itemMeta.addEnchant(this.enchantment, level, true);
            }
        }
    }
}

