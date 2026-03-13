/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package com.magmaguy.betterstructures.chests;

import com.magmaguy.betterstructures.config.treasures.TreasureConfigFields;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ChestEntry {
    private final Material material;
    private final double weight;
    private final int minAmount;
    private final int maxAmount;
    private final ItemStack itemStack;
    private final boolean procedurallyGeneratedEnchantments;
    private final TreasureConfigFields treasureConfigFields;

    public ChestEntry(Material material, double chance, int minAmount, int maxAmount, ItemStack itemStack, boolean procedurallyGeneratedEnchantments, TreasureConfigFields treasureConfigFields) {
        this.material = material;
        this.weight = chance;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.itemStack = itemStack;
        this.procedurallyGeneratedEnchantments = procedurallyGeneratedEnchantments;
        this.treasureConfigFields = treasureConfigFields;
    }

    public ItemStack rollEntry() {
        int amount = this.minAmount != this.maxAmount ? ThreadLocalRandom.current().nextInt(this.minAmount, this.maxAmount + 1) : this.minAmount;
        try {
            if (this.material != null) {
                ItemStack itemStack = new ItemStack(this.material, amount);
                if (!this.procedurallyGeneratedEnchantments) {
                    return itemStack;
                }
                List<TreasureConfigFields.ConfigurationEnchantment> configurationEnchantmentList = this.treasureConfigFields.getEnchantmentSettings().get(this.material);
                if (configurationEnchantmentList == null || configurationEnchantmentList.isEmpty()) {
                    return itemStack;
                }
                ItemMeta itemMeta = itemStack.getItemMeta();
                for (TreasureConfigFields.ConfigurationEnchantment configurationEnchantment : configurationEnchantmentList) {
                    configurationEnchantment.rollEnchantment(itemMeta);
                }
                itemStack.setItemMeta(itemMeta);
                return itemStack;
            }
            ItemStack finalItemStack = this.itemStack.clone();
            finalItemStack.setAmount(amount);
            return finalItemStack;
        } catch (Exception e) {
            return null;
        }
    }

    public double getWeight() {
        return this.weight;
    }
}

