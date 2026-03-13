/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.api.upgrades;

import com.andrei1058.bedwars.api.upgrades.MenuContent;
import com.google.common.collect.ImmutableMap;
import org.bukkit.entity.Player;

public interface UpgradesIndex {
    public String getName();

    public void open(Player var1);

    public boolean addContent(MenuContent var1, int var2);

    public int countTiers();

    public ImmutableMap<Integer, MenuContent> getMenuContentBySlot();
}

