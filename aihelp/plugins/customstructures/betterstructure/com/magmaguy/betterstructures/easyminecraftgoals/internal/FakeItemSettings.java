/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Display$Billboard
 *  org.bukkit.inventory.ItemStack
 */
package com.magmaguy.betterstructures.easyminecraftgoals.internal;

import org.bukkit.entity.Display;
import org.bukkit.inventory.ItemStack;

public class FakeItemSettings {
    private ItemStack itemStack;
    private Display.Billboard billboard = Display.Billboard.FIXED;
    private float scale = 1.0f;
    private float viewRange = 1.0f;
    private boolean glowing = false;
    private String customName = null;
    private boolean customNameVisible = false;

    public FakeItemSettings() {
    }

    public FakeItemSettings(FakeItemSettings other) {
        this.itemStack = other.itemStack != null ? other.itemStack.clone() : null;
        this.billboard = other.billboard;
        this.scale = other.scale;
        this.viewRange = other.viewRange;
        this.glowing = other.glowing;
        this.customName = other.customName;
        this.customNameVisible = other.customNameVisible;
    }

    public boolean hasCustomName() {
        return this.customName != null && !this.customName.isEmpty();
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public Display.Billboard getBillboard() {
        return this.billboard;
    }

    public float getScale() {
        return this.scale;
    }

    public float getViewRange() {
        return this.viewRange;
    }

    public boolean isGlowing() {
        return this.glowing;
    }

    public String getCustomName() {
        return this.customName;
    }

    public boolean isCustomNameVisible() {
        return this.customNameVisible;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public void setBillboard(Display.Billboard billboard) {
        this.billboard = billboard;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void setViewRange(float viewRange) {
        this.viewRange = viewRange;
    }

    public void setGlowing(boolean glowing) {
        this.glowing = glowing;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public void setCustomNameVisible(boolean customNameVisible) {
        this.customNameVisible = customNameVisible;
    }
}

