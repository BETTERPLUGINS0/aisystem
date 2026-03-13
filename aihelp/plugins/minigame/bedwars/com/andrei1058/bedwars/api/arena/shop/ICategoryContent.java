/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package com.andrei1058.bedwars.api.arena.shop;

import com.andrei1058.bedwars.api.arena.shop.IContentTier;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ICategoryContent {
    public int getSlot();

    public ItemStack getItemStack(Player var1);

    public boolean hasQuick(Player var1);

    public boolean isPermanent();

    public boolean isDowngradable();

    public String getIdentifier();

    public List<IContentTier> getContentTiers();
}

