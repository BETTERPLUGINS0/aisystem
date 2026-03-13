/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.chat.ClickEvent$Action
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.World
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive.setup;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.command.ParentCommand;
import com.andrei1058.bedwars.api.command.SubCommand;
import com.andrei1058.bedwars.arena.Misc;
import com.andrei1058.bedwars.arena.SetupSession;
import com.andrei1058.bedwars.configuration.Permissions;
import com.andrei1058.bedwars.support.paper.TeleportManager;
import java.util.List;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class Save
extends SubCommand {
    public Save(ParentCommand parent, String name) {
        super(parent, name);
        this.setArenaSetupCommand(true);
        this.setPermission(Permissions.PERMISSION_SETUP_ARENA);
    }

    @Override
    public boolean execute(String[] args, CommandSender s) {
        if (s instanceof ConsoleCommandSender) {
            return false;
        }
        Player p = (Player)s;
        SetupSession ss = SetupSession.getSession(p.getUniqueId());
        if (ss == null) {
            return false;
        }
        for (Entity e : p.getWorld().getEntities()) {
            if (e.getType() != EntityType.ARMOR_STAND) continue;
            e.remove();
        }
        if (Bukkit.getWorld((String)BedWars.getLobbyWorld()) != null) {
            TeleportManager.teleport((Entity)p, Bukkit.getWorld((String)BedWars.getLobbyWorld()).getSpawnLocation());
        } else {
            TeleportManager.teleport((Entity)p, ((World)Bukkit.getWorlds().get(0)).getSpawnLocation());
        }
        ss.done();
        p.sendMessage(ss.getPrefix() + "Arena changes saved!");
        p.sendMessage(ss.getPrefix() + "You can now enable it using:");
        p.spigot().sendMessage((BaseComponent)Misc.msgHoverClick(String.valueOf(ChatColor.GOLD) + "/" + this.getParent().getName() + " enableArena " + ss.getWorldName() + String.valueOf(ChatColor.GRAY) + " (click to enable)", String.valueOf(ChatColor.GREEN) + "Enable this arena.", "/" + this.getParent().getName() + " enableArena " + ss.getWorldName(), ClickEvent.Action.RUN_COMMAND));
        return true;
    }

    @Override
    public List<String> getTabComplete() {
        return null;
    }

    @Override
    public boolean canSee(CommandSender s, com.andrei1058.bedwars.api.BedWars api) {
        if (s instanceof ConsoleCommandSender) {
            return false;
        }
        Player p = (Player)s;
        if (!SetupSession.isInSetupSession(p.getUniqueId())) {
            return false;
        }
        return this.hasPermission(s);
    }
}

