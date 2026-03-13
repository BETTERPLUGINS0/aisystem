/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.ClickEvent$Action
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.command.ParentCommand;
import com.andrei1058.bedwars.api.command.SubCommand;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.Misc;
import com.andrei1058.bedwars.arena.SetupSession;
import com.andrei1058.bedwars.commands.bedwars.MainCommand;
import com.andrei1058.bedwars.configuration.Permissions;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class EnableArena
extends SubCommand {
    public EnableArena(ParentCommand parent, String name) {
        super(parent, name);
        this.setDisplayInfo(Misc.msgHoverClick("\u00a76 \u25aa \u00a77/" + this.getParent().getName() + " " + this.getSubCommandName() + " \u00a76<worldName>", "\u00a7fEnable an arena.", "/" + this.getParent().getName() + " " + this.getSubCommandName() + " ", ClickEvent.Action.SUGGEST_COMMAND));
        this.showInList(true);
        this.setPriority(5);
        this.setPermission(Permissions.PERMISSION_ARENA_ENABLE);
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
        if (args.length != 1) {
            p.sendMessage("\u00a7c\u25aa \u00a77Usage: \u00a7o/" + this.getParent().getName() + " enableRotation <mapName>");
            return true;
        }
        if (!BedWars.getAPI().getRestoreAdapter().isWorld(args[0])) {
            p.sendMessage("\u00a7c\u25aa \u00a77" + args[0] + " doesn't exist!");
            return true;
        }
        for (IArena mm : Arena.getEnableQueue()) {
            if (!mm.getArenaName().equalsIgnoreCase(args[0])) continue;
            p.sendMessage("\u00a7c\u25aa \u00a77This arena is already in the enable queue!");
            return true;
        }
        IArena aa = Arena.getArenaByName(args[0]);
        if (aa != null) {
            p.sendMessage("\u00a7c\u25aa \u00a77This arena is already enabled!");
            return true;
        }
        p.sendMessage("\u00a76 \u25aa \u00a77Enabling arena...");
        new Arena(args[0], p);
        return true;
    }

    @Override
    public List<String> getTabComplete() {
        ArrayList<String> tab = new ArrayList<String>();
        File dir = new File(BedWars.plugin.getDataFolder(), "/Arenas");
        if (dir.exists()) {
            File[] fls = dir.listFiles();
            for (File fl : Objects.requireNonNull(fls)) {
                if (!fl.isFile() || !fl.getName().contains(".yml")) continue;
                tab.add(fl.getName().replace(".yml", ""));
            }
        }
        return tab;
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

