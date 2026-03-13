/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.commands.bedwars.subcmds.regular;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.command.ParentCommand;
import com.andrei1058.bedwars.api.command.SubCommand;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.SetupSession;
import com.andrei1058.bedwars.commands.bedwars.MainCommand;
import com.andrei1058.bedwars.configuration.Sounds;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CmdJoin
extends SubCommand {
    public CmdJoin(ParentCommand parent, String name) {
        super(parent, name);
        this.setPriority(19);
        this.showInList(false);
        this.setDisplayInfo(MainCommand.createTC("\u00a76 \u25aa \u00a77/" + MainCommand.getInstance().getName() + " join \u00a7e<random/ arena/ groupName>", "/" + this.getParent().getName() + " " + this.getSubCommandName(), "\u00a7fJoin an arena by name or by group.\n\u00a7f/bw join random - join random arena."));
    }

    @Override
    public boolean execute(String[] args, CommandSender s) {
        if (s instanceof ConsoleCommandSender) {
            return false;
        }
        Player p = (Player)s;
        if (args.length < 1) {
            s.sendMessage(Language.getMsg(p, Messages.COMMAND_JOIN_USAGE));
            return true;
        }
        if (args[0].equalsIgnoreCase("random")) {
            if (!Arena.joinRandomArena(p)) {
                s.sendMessage(Language.getMsg(p, Messages.COMMAND_JOIN_NO_EMPTY_FOUND));
                Sounds.playSound("join-denied", p);
            } else {
                Sounds.playSound("join-allowed", p);
            }
            return true;
        }
        if (MainCommand.isArenaGroup(args[0]) || args[0].contains("+")) {
            if (!Arena.joinRandomFromGroup(p, args[0])) {
                s.sendMessage(Language.getMsg(p, Messages.COMMAND_JOIN_NO_EMPTY_FOUND));
                Sounds.playSound("join-denied", p);
            } else {
                Sounds.playSound("join-allowed", p);
            }
            return true;
        }
        if (Arena.getArenaByName(args[0]) != null) {
            if (Arena.getArenaByName(args[0]).addPlayer(p, false)) {
                Sounds.playSound("join-allowed", p);
            } else {
                Sounds.playSound("join-denied", p);
            }
            return true;
        }
        if (Arena.getArenaByIdentifier(args[0]) != null) {
            if (Arena.getArenaByIdentifier(args[0]).addPlayer(p, false)) {
                Sounds.playSound("join-allowed", p);
            } else {
                Sounds.playSound("join-denied", p);
            }
            return true;
        }
        s.sendMessage(Language.getMsg(p, Messages.COMMAND_JOIN_GROUP_OR_ARENA_NOT_FOUND).replace("{name}", args[0]));
        return true;
    }

    @Override
    public List<String> getTabComplete() {
        ArrayList<String> tab = new ArrayList<String>(BedWars.config.getYml().getStringList("arenaGroups"));
        for (IArena arena : Arena.getArenas()) {
            tab.add(arena.getArenaName());
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

