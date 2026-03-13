/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.inventory.ItemStack
 */
package net.advancedplugins.as.impl.utils.abilities;

import org.bukkit.inventory.ItemStack;

public class DropsSettings {
    private boolean smelt = false;
    private boolean addToInventory = false;
    private boolean silkTouch = false;
    private boolean defaultDrops = true;
    private int dropsMultiplier = 1;
    private boolean breakBlocks = false;
    private boolean dropExp = true;
    private boolean durabilityDamage = true;
    private ItemStack tool = null;
    private boolean treeFellerEvent = false;
    private boolean terraformEvent = false;
    private int dropExpAmount = 0;

    public boolean isSmelt() {
        return this.smelt;
    }

    public boolean isAddToInventory() {
        return this.addToInventory;
    }

    public boolean isSilkTouch() {
        return this.silkTouch;
    }

    public boolean isDefaultDrops() {
        return this.defaultDrops;
    }

    public int getDropsMultiplier() {
        return this.dropsMultiplier;
    }

    public boolean isBreakBlocks() {
        return this.breakBlocks;
    }

    public boolean isDropExp() {
        return this.dropExp;
    }

    public boolean isDurabilityDamage() {
        return this.durabilityDamage;
    }

    public ItemStack getTool() {
        return this.tool;
    }

    public boolean isTreeFellerEvent() {
        return this.treeFellerEvent;
    }

    public boolean isTerraformEvent() {
        return this.terraformEvent;
    }

    public int getDropExpAmount() {
        return this.dropExpAmount;
    }

    public void setSmelt(boolean bl) {
        this.smelt = bl;
    }

    public void setAddToInventory(boolean bl) {
        this.addToInventory = bl;
    }

    public void setSilkTouch(boolean bl) {
        this.silkTouch = bl;
    }

    public void setDefaultDrops(boolean bl) {
        this.defaultDrops = bl;
    }

    public void setDropsMultiplier(int n) {
        this.dropsMultiplier = n;
    }

    public void setBreakBlocks(boolean bl) {
        this.breakBlocks = bl;
    }

    public void setDropExp(boolean bl) {
        this.dropExp = bl;
    }

    public void setDurabilityDamage(boolean bl) {
        this.durabilityDamage = bl;
    }

    public void setTool(ItemStack itemStack) {
        this.tool = itemStack;
    }

    public void setTreeFellerEvent(boolean bl) {
        this.treeFellerEvent = bl;
    }

    public void setTerraformEvent(boolean bl) {
        this.terraformEvent = bl;
    }

    public void setDropExpAmount(int n) {
        this.dropExpAmount = n;
    }
}

