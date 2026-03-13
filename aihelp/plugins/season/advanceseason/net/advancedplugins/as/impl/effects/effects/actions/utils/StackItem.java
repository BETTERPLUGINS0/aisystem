/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.inventory.ItemStack
 */
package net.advancedplugins.as.impl.effects.effects.actions.utils;

import net.advancedplugins.as.impl.effects.effects.actions.utils.RollItemType;
import org.bukkit.inventory.ItemStack;

public class StackItem {
    public ItemStack i;
    public RollItemType rit;
    public int number;

    public StackItem(ItemStack itemStack, RollItemType rollItemType) {
        this.i = itemStack;
        this.rit = rollItemType;
    }

    public RollItemType getRollItemType() {
        return this.rit;
    }

    public ItemStack getItem() {
        return this.i;
    }

    public void setNumber(int n) {
        this.number = n;
    }
}

