/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.Indyuce.mmoitems.MMOItems
 *  net.Indyuce.mmoitems.api.item.mmoitem.MMOItem
 *  org.bukkit.Material
 *  org.bukkit.block.Container
 *  org.bukkit.configuration.MemorySection
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.loot.LootContext
 *  org.bukkit.loot.LootContext$Builder
 *  org.bukkit.loot.LootTables
 */
package com.magmaguy.betterstructures.chests;

import com.magmaguy.betterstructures.chests.ChestEntry;
import com.magmaguy.betterstructures.config.treasures.TreasureConfigFields;
import com.magmaguy.betterstructures.util.ItemStackSerialization;
import com.magmaguy.betterstructures.util.WeighedProbability;
import com.magmaguy.magmacore.util.Logger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.configuration.MemorySection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTables;

public class ChestContents {
    private final List<ChestRarity> chestRarities = new ArrayList<ChestRarity>();
    private final TreasureConfigFields treasureConfigFields;

    public ChestContents(TreasureConfigFields treasureConfigFields) {
        this.treasureConfigFields = treasureConfigFields;
        if (treasureConfigFields.getRawLoot() == null) {
            return;
        }
        this.processRarities(treasureConfigFields.getRawLoot());
    }

    private Material getMaterial(String string) {
        try {
            return Material.getMaterial((String)string.toUpperCase(Locale.ROOT));
        } catch (Exception exception) {
            Logger.warn("Invalid material detected! Problematic entry: " + string + " in configuration file " + this.treasureConfigFields.getFilename());
            return null;
        }
    }

    private double getWeight(String string) {
        try {
            return Double.parseDouble(string);
        } catch (Exception exception) {
            Logger.warn("Invalid double value detected! Problematic entry: " + string + " in configuration file " + this.treasureConfigFields.getFilename());
            return -1.0;
        }
    }

    private void processRarities(Map<String, Object> rawChestEntries) {
        for (Map.Entry<String, Object> entry : rawChestEntries.entrySet()) {
            double weight = -1.0;
            List<ChestEntry> chestEntries = null;
            block9: for (Map.Entry innerEntry : ((MemorySection)entry.getValue()).getValues(false).entrySet()) {
                switch (((String)innerEntry.getKey()).toLowerCase(Locale.ROOT)) {
                    case "weight": {
                        weight = this.getWeight(innerEntry.getValue().toString());
                        continue block9;
                    }
                    case "items": {
                        chestEntries = this.processEntries((List)innerEntry.getValue());
                        continue block9;
                    }
                }
                Logger.warn("Failed to read key " + (String)innerEntry.getKey() + " for configuration file " + this.treasureConfigFields.getFilename());
            }
            if (!(weight > 0.0) || chestEntries == null) continue;
            this.chestRarities.add(new ChestRarity(this, weight, chestEntries));
        }
    }

    private ItemStack getSerializedItemStack(Map<String, Object> deserializedItemStack, String string) {
        try {
            return ItemStackSerialization.serializeItem(deserializedItemStack);
        } catch (Exception ex) {
            Logger.warn("Invalid serialized value detected! Problematic entry: " + string + " for configuration file " + this.treasureConfigFields.getFilename());
            ex.printStackTrace();
            return null;
        }
    }

    private boolean getProcedurallyGeneratedEnchantments(String string) {
        try {
            return Boolean.parseBoolean(string);
        } catch (Exception ex) {
            Logger.warn("Invalid boolean value detected! Problematic entry: " + string + " for configuration file " + this.treasureConfigFields.getFilename());
            ex.printStackTrace();
            return false;
        }
    }

    private ItemStack getMMOItemsItemStack(String string) {
        try {
            String[] args2 = string.split("@");
            MMOItems mmo = MMOItems.plugin;
            MMOItem mmoitem = mmo.getMMOItem(mmo.getTypes().get(args2[0]), args2[1]);
            if (mmoitem == null) {
                throw new NullPointerException("mmo item is null");
            }
            return mmoitem.newBuilder().build();
        } catch (Exception ex) {
            Logger.warn("Invalid mmo item detected! Problematic entry: " + string + " in " + this.treasureConfigFields.getFilename());
            return null;
        }
    }

    private List<ChestEntry> processEntries(List<Map<String, ?>> rawChestEntries) {
        ArrayList<ChestEntry> chestEntries = new ArrayList<ChestEntry>();
        for (Map<String, ?> rawChestEntry : rawChestEntries) {
            Material material = null;
            int minAmount = -1;
            int maxAmount = -1;
            double weight = -1.0;
            boolean procedurallyGeneratedEnchantments = false;
            ItemStack itemStack = null;
            for (Map.Entry<String, ?> entry : rawChestEntry.entrySet()) {
                String value = entry.getValue().toString();
                switch (entry.getKey().toLowerCase(Locale.ROOT)) {
                    case "material": {
                        material = this.getMaterial(value);
                        break;
                    }
                    case "amount": {
                        try {
                            if (value.contains("-")) {
                                String[] amounts = value.split("-");
                                minAmount = Integer.parseInt(amounts[0]);
                                maxAmount = Integer.parseInt(amounts[1]);
                                break;
                            }
                            maxAmount = minAmount = Integer.parseInt(value);
                        } catch (Exception exception) {
                            Logger.warn("Invalid amount detected! Problematic entry: " + value + " in file " + this.treasureConfigFields.getFilename());
                        }
                        break;
                    }
                    case "weight": {
                        weight = this.getWeight(value);
                        break;
                    }
                    case "mmoitem": 
                    case "mmoitems": {
                        itemStack = this.getMMOItemsItemStack(value);
                        break;
                    }
                    case "serialized": {
                        itemStack = this.getSerializedItemStack((Map)entry.getValue(), value);
                        break;
                    }
                    case "procedurallygenerateenchantments": {
                        procedurallyGeneratedEnchantments = this.getProcedurallyGeneratedEnchantments(value);
                        break;
                    }
                    case "info": {
                        break;
                    }
                    default: {
                        Logger.warn("Failed to read key " + entry.getKey() + " for configuration file " + this.treasureConfigFields.getFilename());
                    }
                }
            }
            if (material == null && itemStack == null) continue;
            ChestEntry chestEntry = new ChestEntry(material, weight, minAmount, maxAmount, itemStack, procedurallyGeneratedEnchantments, this.treasureConfigFields);
            chestEntries.add(chestEntry);
        }
        return chestEntries;
    }

    public void rollChestContents(Container chest) {
        LootTables vanillaTreasure;
        if (!this.chestRarities.isEmpty()) {
            this.rollCustomLoot(chest);
        }
        if ((vanillaTreasure = this.treasureConfigFields.getVanillaTreasure()) != null) {
            this.rollVanillaLoot(chest, vanillaTreasure);
        }
    }

    private void rollCustomLoot(Container chest) {
        int i;
        int amount = (int)Math.max(Math.ceil(ThreadLocalRandom.current().nextGaussian(this.treasureConfigFields.getMean(), this.treasureConfigFields.getStandardDeviation())), 0.0);
        ++amount;
        HashMap<Integer, Double> weightsMap = new HashMap<Integer, Double>();
        for (i = this.chestRarities.size() - 1; i >= 0; --i) {
            weightsMap.put(i, this.chestRarities.get((int)i).chestWeight);
        }
        for (i = 0; i < amount; ++i) {
            ItemStack itemStack = this.chestRarities.get(WeighedProbability.pickWeightedProbability(weightsMap)).rollLoot();
            if (itemStack == null) continue;
            this.placeItemInChest(chest, itemStack);
        }
    }

    private void rollVanillaLoot(Container chest, LootTables lootTable) {
        LootContext lootContext = new LootContext.Builder(chest.getLocation()).build();
        Collection loot = lootTable.getLootTable().populateLoot((Random)ThreadLocalRandom.current(), lootContext);
        for (ItemStack itemStack : loot) {
            if (itemStack == null || itemStack.getType() == Material.AIR) continue;
            this.placeItemInChest(chest, itemStack);
        }
    }

    private void placeItemInChest(Container chest, ItemStack itemStack) {
        for (int counter = 0; counter < 100; ++counter) {
            int randomizedIndex = ThreadLocalRandom.current().nextInt(0, chest.getSnapshotInventory().getSize());
            if (chest.getSnapshotInventory().getItem(randomizedIndex) != null) continue;
            chest.getSnapshotInventory().setItem(randomizedIndex, itemStack);
            break;
        }
    }

    public List<ChestRarity> getChestRarities() {
        return this.chestRarities;
    }

    private class ChestRarity {
        private final double chestWeight;
        private final List<ChestEntry> chestEntries;

        public ChestRarity(ChestContents chestContents, double chestWeight, List<ChestEntry> chestEntries) {
            this.chestEntries = chestEntries;
            this.chestWeight = chestWeight;
        }

        public ItemStack rollLoot() {
            HashMap<Integer, Double> weightsMap = new HashMap<Integer, Double>();
            for (int i = this.chestEntries.size() - 1; i >= 0; --i) {
                weightsMap.put(i, this.chestEntries.get(i).getWeight());
            }
            return this.chestEntries.get(WeighedProbability.pickWeightedProbability(weightsMap)).rollEntry();
        }
    }
}

