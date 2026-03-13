/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.libs.sidebar;

import com.andrei1058.bedwars.libs.sidebar.PlayerTab;
import org.bukkit.entity.Player;

public interface VersionedTabGroup
extends PlayerTab {
    public void sendCreateToPlayer(Player var1);

    public void sendUserCreateToReceivers(Player var1);

    public void sendUpdateToReceivers();

    public void sendRemoveToReceivers();

    public boolean refreshContent();
}

