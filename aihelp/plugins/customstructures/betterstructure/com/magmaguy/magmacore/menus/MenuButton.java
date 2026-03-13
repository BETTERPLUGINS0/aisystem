/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package com.magmaguy.magmacore.menus;

import com.magmaguy.magmacore.util.ItemStackGenerator;
import java.util.List;
import lombok.Generated;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class MenuButton {
    private ItemStack itemStack = null;

    public MenuButton(Material material, String name, List<String> lore) {
        this.itemStack = ItemStackGenerator.generateItemStack(material, name, lore);
    }

    public MenuButton(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public MenuButton() {
    }

    public abstract void onClick(Player var1);

    @Generated
    public ItemStack getItemStack() {
        return this.itemStack;
    }
}

