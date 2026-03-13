/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.api.server;

import com.andrei1058.bedwars.api.configuration.ConfigManager;
import com.andrei1058.bedwars.api.server.SetupType;
import org.bukkit.entity.Player;

public interface ISetupSession {
    public String getWorldName();

    public Player getPlayer();

    public SetupType getSetupType();

    public ConfigManager getConfig();

    public void teleportPlayer();

    public void close();
}

