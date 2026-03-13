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
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.command.ParentCommand;
import com.andrei1058.bedwars.api.command.SubCommand;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.Misc;
import com.andrei1058.bedwars.arena.SetupSession;
import com.andrei1058.bedwars.commands.bedwars.MainCommand;
import com.andrei1058.bedwars.configuration.Permissions;
import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class DisableArena
extends SubCommand {
    public DisableArena(ParentCommand parent, String name) {
        super(parent, name);
        this.setPriority(6);
        this.showInList(true);
        this.setDisplayInfo(Misc.msgHoverClick("\u00a76 \u25aa \u00a77/" + this.getParent().getName() + " " + this.getSubCommandName() + " \u00a76<worldName>", "\u00a7fDisable an arena.\nThis will remove the players \n\u00a7ffrom the arena before disabling.", "/" + this.getParent().getName() + " " + this.getSubCommandName() + " ", ClickEvent.Action.SUGGEST_COMMAND));
        this.setPermission(Permissions.PERMISSION_ARENA_DISABLE);
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
            p.sendMessage("\u00a7c\u25aa \u00a77Usage: \u00a7o/" + this.getParent().getName() + " " + this.getSubCommandName() + " <mapName>");
            return true;
        }
        if (!BedWars.getAPI().getRestoreAdapter().isWorld(args[0])) {
            p.sendMessage("\u00a7c\u25aa \u00a77" + args[0] + " doesn't exist!");
            return true;
        }
        IArena a = Arena.getArenaByName(args[0]);
        if (a == null) {
            p.sendMessage("\u00a7c\u25aa \u00a77This arena is disabled yet!");
            return true;
        }
        if (a.getStatus() == GameState.playing) {
            p.sendMessage("\u00a76 \u25aa \u00a77There is a game running on this Arena, please disable after the game!");
            return true;
        }
        p.sendMessage("\u00a76 \u25aa \u00a77Disabling arena...");
        a.disable();
        return true;
    }

    @Override
    public List<String> getTabComplete() {
        ArrayList<String> tab = new ArrayList<String>();
        for (IArena a : Arena.getArenas()) {
            tab.add(a.getArenaName());
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

