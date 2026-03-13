/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package com.andrei1058.bedwars.api.upgrades;

import com.andrei1058.bedwars.api.arena.team.ITeam;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface EnemyBaseEnterTrap {
    public String getNameMsgPath();

    public String getLoreMsgPath();

    public ItemStack getItemStack();

    public void trigger(ITeam var1, Player var2);
}

