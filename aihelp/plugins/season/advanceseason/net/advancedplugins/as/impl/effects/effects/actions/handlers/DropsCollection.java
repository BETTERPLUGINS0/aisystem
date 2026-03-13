/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.inventory.ItemStack
 */
package net.advancedplugins.as.impl.effects.effects.actions.handlers;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.inventory.ItemStack;

public class DropsCollection {
    private final List<ItemStack> items = new ArrayList<ItemStack>();
    private final List<ItemStack> parsedItems = new ArrayList<ItemStack>();

    public List<ItemStack> getItems() {
        return this.items;
    }

    public List<ItemStack> getParsedItems() {
        return this.parsedItems;
    }
}

