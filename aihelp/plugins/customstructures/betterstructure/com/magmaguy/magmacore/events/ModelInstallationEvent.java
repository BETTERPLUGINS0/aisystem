/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.magmaguy.freeminecraftmodels.commands.ReloadCommand
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package com.magmaguy.magmacore.events;

import com.magmaguy.freeminecraftmodels.commands.ReloadCommand;
import com.magmaguy.magmacore.util.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ModelInstallationEvent
extends Event {
    private static final HandlerList handlers = new HandlerList();

    public ModelInstallationEvent() {
        Logger.info("Models have been installed!");
        ReloadCommand.reloadPlugin((CommandSender)Bukkit.getConsoleSender());
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}

