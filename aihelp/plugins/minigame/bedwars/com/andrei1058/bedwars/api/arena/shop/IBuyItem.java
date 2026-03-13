/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package com.andrei1058.bedwars.api.arena.shop;

import com.andrei1058.bedwars.api.arena.IArena;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IBuyItem {
    public boolean isLoaded();

    public void give(Player var1, IArena var2);

    public String getUpgradeIdentifier();

    public ItemStack getItemStack();

    public void setItemStack(ItemStack var1);

    public boolean isAutoEquip();

    public void setAutoEquip(boolean var1);

    public boolean isPermanent();

    public void setPermanent(boolean var1);

    public boolean isUnbreakable();

    public void setUnbreakable(boolean var1);
}

