/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.inventory.ItemStack
 */
package com.andrei1058.bedwars.api.upgrades;

import com.andrei1058.bedwars.api.arena.team.ITeam;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public interface MenuContent {
    public ItemStack getDisplayItem(Player var1, ITeam var2);

    public void onClick(Player var1, ClickType var2, ITeam var3);

    public String getName();
}

