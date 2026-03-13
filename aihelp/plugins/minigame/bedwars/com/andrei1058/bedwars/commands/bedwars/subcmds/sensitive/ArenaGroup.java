/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.chat.ClickEvent$Action
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.command.ParentCommand;
import com.andrei1058.bedwars.api.command.SubCommand;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.Misc;
import com.andrei1058.bedwars.arena.SetupSession;
import com.andrei1058.bedwars.commands.bedwars.MainCommand;
import com.andrei1058.bedwars.configuration.ArenaConfig;
import com.andrei1058.bedwars.configuration.Permissions;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ArenaGroup
extends SubCommand {
    public ArenaGroup(ParentCommand parent, String name) {
        super(parent, name);
        this.setPriority(8);
        this.showInList(true);
        this.setPermission(Permissions.PERMISSION_ARENA_GROUP);
        this.setDisplayInfo(Misc.msgHoverClick("\u00a76 \u25aa \u00a77/" + this.getParent().getName() + " " + this.getSubCommandName() + " \u00a78- \u00a7eclick for details", "\u00a7fManage arena groups.", "/" + this.getParent().getName() + " " + this.getSubCommandName(), ClickEvent.Action.RUN_COMMAND));
    }

    @Override
    public boolean execute(String[] args, CommandSender s) {
        if (s instanceof ConsoleCommandSender) {
            return false;
        }
        Player p = (Player)s;
        if (!MainCommand.isLobbySet(p)) {
            return true;
        }
        if (!(args.length >= 2 || args.length >= 1 && args[0].equalsIgnoreCase("list"))) {
            this.sendArenaGroupCmdList(p);
        } else if (args[0].equalsIgnoreCase("create")) {
            if (args[0].contains("+")) {
                p.sendMessage("\u00a7c\u25aa \u00a77" + args[0] + " mustn't contain this symbol: " + String.valueOf(ChatColor.RED) + "+");
                return true;
            }
            List groups = BedWars.config.getYml().getStringList("arenaGroups") == null ? new ArrayList() : BedWars.config.getYml().getStringList("arenaGroups");
            if (groups.contains(args[1])) {
                p.sendMessage("\u00a7c\u25aa \u00a77This group already exists!");
                return true;
            }
            groups.add(args[1]);
            BedWars.config.set("arenaGroups", groups);
            p.sendMessage("\u00a76 \u25aa \u00a77Group created!");
        } else if (args[0].equalsIgnoreCase("remove")) {
            List groups = BedWars.config.getYml().getStringList("arenaGroups") == null ? new ArrayList() : BedWars.config.getYml().getStringList("arenaGroups");
            if (!groups.contains(args[1])) {
                p.sendMessage("\u00a7c\u25aa \u00a77This group doesn't exist!");
                return true;
            }
            groups.remove(args[1]);
            BedWars.config.set("arenaGroups", groups);
            p.sendMessage("\u00a76 \u25aa \u00a77Group deleted!");
        } else if (args[0].equalsIgnoreCase("list")) {
            List groups = BedWars.config.getYml().getStringList("arenaGroups") == null ? new ArrayList() : BedWars.config.getYml().getStringList("arenaGroups");
            p.sendMessage("\u00a77Available arena groups:");
            p.sendMessage("\u00a76 \u25aa \u00a7fDefault");
            for (String gs : groups) {
                p.sendMessage("\u00a76 \u25aa \u00a7f" + gs);
            }
        } else if (args[0].equalsIgnoreCase("set")) {
            if (args.length < 3) {
                this.sendArenaGroupCmdList(p);
                return true;
            }
            if (BedWars.config.getYml().get("arenaGroups") != null) {
                if (BedWars.config.getYml().getStringList("arenaGroups").contains(args[2])) {
                    File arena = new File(BedWars.plugin.getDataFolder(), "/Arenas/" + args[1] + ".yml");
                    if (!arena.exists()) {
                        p.sendMessage("\u00a7c\u25aa \u00a77Arena " + args[1] + " doesn't exist!");
                        return true;
                    }
                    ArenaConfig cm = new ArenaConfig((Plugin)BedWars.plugin, args[1], BedWars.plugin.getDataFolder().getPath() + "/Arenas");
                    cm.set("group", args[2]);
                    if (Arena.getArenaByName(args[1]) != null) {
                        Arena.getArenaByName(args[1]).setGroup(args[2]);
                    }
                    p.sendMessage("\u00a76 \u25aa \u00a77" + args[1] + " was added to the group: " + args[2]);
                } else {
                    p.sendMessage("\u00a76 \u25aa \u00a77There isn't any group called: " + args[2]);
                    Bukkit.dispatchCommand((CommandSender)p, (String)"/bw list");
                }
            } else {
                p.sendMessage("\u00a76 \u25aa \u00a77There isn't any group called: " + args[2]);
                Bukkit.dispatchCommand((CommandSender)p, (String)"/bw list");
            }
        } else {
            this.sendArenaGroupCmdList(p);
        }
        return true;
    }

    @Override
    public List<String> getTabComplete() {
        return Arrays.asList("create", "remove", "list", "set");
    }

    private void sendArenaGroupCmdList(Player p) {
        p.spigot().sendMessage((BaseComponent)Misc.msgHoverClick("\u00a76 \u25aa \u00a77/" + this.getParent().getName() + " " + this.getSubCommandName() + " create \u00a7o<groupName>", "Create an arena group. More details on our wiki.", "/" + this.getParent().getName() + " " + this.getSubCommandName() + " create", ClickEvent.Action.SUGGEST_COMMAND));
        p.spigot().sendMessage((BaseComponent)Misc.msgHoverClick("\u00a76 \u25aa \u00a77/" + this.getParent().getName() + " " + this.getSubCommandName() + " list", "View available groups.", "/" + this.getParent().getName() + " " + this.getSubCommandName() + " list", ClickEvent.Action.RUN_COMMAND));
        p.spigot().sendMessage((BaseComponent)Misc.msgHoverClick("\u00a76 \u25aa \u00a77/" + this.getParent().getName() + " " + this.getSubCommandName() + " remove \u00a7o<groupName>", "Remove an arena group. More details on our wiki.", "/" + this.getParent().getName() + " " + this.getSubCommandName() + " remove", ClickEvent.Action.SUGGEST_COMMAND));
        p.spigot().sendMessage((BaseComponent)Misc.msgHoverClick("\u00a76 \u25aa \u00a77/" + this.getParent().getName() + " " + this.getSubCommandName() + " \u00a7r\u00a77set \u00a7o<arenaName> <groupName>", "Set the arena group. More details on our wiki.", "/" + this.getParent().getName() + " " + this.getSubCommandName() + " set", ClickEvent.Action.SUGGEST_COMMAND));
    }

    @Override
    public boolean canSee(CommandSender s, com.andrei1058.bedwars.api.BedWars api) {
        if (s instanceof ConsoleCommandSender) {
            return false;
        }
        Player p = (Player)s;
        if (Arena.isInArena(p)) {
            return false;
        }
        if (SetupSession.isInSetupSession(p.getUniqueId())) {
            return false;
        }
        return this.hasPermission(s);
    }
}

