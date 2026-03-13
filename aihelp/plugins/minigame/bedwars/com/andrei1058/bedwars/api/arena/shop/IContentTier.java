/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 */
package com.andrei1058.bedwars.api.arena.shop;

import com.andrei1058.bedwars.api.arena.shop.IBuyItem;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface IContentTier {
    public int getPrice();

    public Material getCurrency();

    public void setCurrency(Material var1);

    public void setPrice(int var1);

    public void setItemStack(ItemStack var1);

    public void setBuyItemsList(List<IBuyItem> var1);

    public ItemStack getItemStack();

    public int getValue();

    public List<IBuyItem> getBuyItemsList();
}

