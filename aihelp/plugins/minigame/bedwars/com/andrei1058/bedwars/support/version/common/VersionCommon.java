/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 */
package com.andrei1058.bedwars.support.version.common;

import com.andrei1058.bedwars.api.BedWars;
import com.andrei1058.bedwars.api.server.VersionSupport;
import com.andrei1058.bedwars.listeners.Interact_1_13Plus;
import com.andrei1058.bedwars.listeners.ItemDropPickListener;
import com.andrei1058.bedwars.listeners.PlayerDropPick_1_11Minus;
import com.andrei1058.bedwars.listeners.SwapItem;
import com.andrei1058.bedwars.shop.defaultrestore.ShopItemRestoreListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class VersionCommon {
    public static BedWars api;

    public VersionCommon(VersionSupport versionSupport) {
        api = (BedWars)Bukkit.getServicesManager().getRegistration(BedWars.class).getProvider();
        if (versionSupport.getVersion() > 1) {
            this.registerListeners(versionSupport.getPlugin(), new SwapItem(), new ItemDropPickListener.ArrowCollect());
        }
        if (versionSupport.getVersion() < 5) {
            this.registerListeners(versionSupport.getPlugin(), new ItemDropPickListener.PlayerPickup(), new ShopItemRestoreListener.PlayerPickup(), new PlayerDropPick_1_11Minus(api));
        }
        if (versionSupport.getVersion() > 5) {
            this.registerListeners(versionSupport.getPlugin(), new ShopItemRestoreListener.EntityDrop(), new Interact_1_13Plus(), new ItemDropPickListener.EntityDrop());
        }
        if (versionSupport.getVersion() > 4) {
            this.registerListeners(versionSupport.getPlugin(), new ItemDropPickListener.EntityPickup(), new ShopItemRestoreListener.EntityPickup());
        }
        this.registerListeners(versionSupport.getPlugin(), new ItemDropPickListener.PlayerDrop(), new ShopItemRestoreListener.PlayerDrop());
        this.registerListeners(versionSupport.getPlugin(), new ShopItemRestoreListener.DefaultRestoreInvClose());
    }

    private void registerListeners(Plugin plugin, Listener ... listener) {
        for (Listener l : listener) {
            plugin.getServer().getPluginManager().registerEvents(l, plugin);
        }
    }
}

