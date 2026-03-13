/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.BaseComponent
 *  org.bukkit.command.CommandSender
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.magmaguy.betterstructures.listeners;

import com.magmaguy.betterstructures.MetadataHandler;
import com.magmaguy.betterstructures.config.DefaultConfig;
import com.magmaguy.magmacore.util.Logger;
import com.magmaguy.magmacore.util.SpigotMessage;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class FirstTimeSetupWarner
implements Listener {
    @EventHandler
    public void onPlayerLogin(final PlayerJoinEvent event) {
        if (DefaultConfig.isSetupDone()) {
            return;
        }
        if (!event.getPlayer().hasPermission("betterstructures.*")) {
            return;
        }
        new BukkitRunnable(this){

            public void run() {
                if (!event.getPlayer().isOnline()) {
                    return;
                }
                Logger.sendSimpleMessage((CommandSender)event.getPlayer(), "&8&m----------------------------------------------------");
                Logger.sendMessage((CommandSender)event.getPlayer(), "&fInitial setup message:");
                Logger.sendSimpleMessage((CommandSender)event.getPlayer(), "&7Welcome to BetterStructures! &c&lIt looks like you have not set up BetterStructures yet!");
                event.getPlayer().spigot().sendMessage(new BaseComponent[]{SpigotMessage.simpleMessage("&2To install BetterStructures, click here: "), SpigotMessage.commandHoverMessage("&a/betterstructures initialize", "&7Click to open the BetterStructures first-time setup menu.", "/betterstructures initialize")});
                event.getPlayer().spigot().sendMessage(new BaseComponent[]{SpigotMessage.simpleMessage("&7You can get support over at "), SpigotMessage.hoverLinkMessage("&9&nhttps://discord.gg/9f5QSka", "&7Click to open the BetterStructures support Discord.", "https://discord.gg/9f5QSka")});
                event.getPlayer().spigot().sendMessage(new BaseComponent[]{SpigotMessage.simpleMessage("&cPick an option in "), SpigotMessage.commandHoverMessage("&a/betterstructures setup", "&7Click to open the BetterStructures setup menu.", "/betterstructures setup"), SpigotMessage.simpleMessage(" &cto permanently dismiss this message!")});
                Logger.sendSimpleMessage((CommandSender)event.getPlayer(), "&8&m----------------------------------------------------");
            }
        }.runTaskLater(MetadataHandler.PLUGIN, 200L);
    }
}

