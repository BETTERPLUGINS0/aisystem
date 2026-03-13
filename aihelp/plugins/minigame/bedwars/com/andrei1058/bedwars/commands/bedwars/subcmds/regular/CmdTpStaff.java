/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.commands.bedwars.subcmds.regular;

import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.command.ParentCommand;
import com.andrei1058.bedwars.api.command.SubCommand;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.support.paper.TeleportManager;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class CmdTpStaff
extends SubCommand {
    public CmdTpStaff(ParentCommand parent, String name) {
        super(parent, name);
        this.setPermission("bw.tp");
        this.showInList(false);
    }

    @Override
    public boolean execute(String[] args, CommandSender s) {
        if (!(s instanceof Player)) {
            return true;
        }
        Player p2 = (Player)s;
        if (args.length != 1) {
            s.sendMessage(Language.getMsg(p2, Messages.COMMAND_TP_USAGE));
            return true;
        }
        if (!this.hasPermission((CommandSender)p2)) {
            p2.sendMessage(Language.getMsg(p2, Messages.COMMAND_FORCESTART_NO_PERM));
            return true;
        }
        Player p = Bukkit.getPlayer((String)args[0]);
        if (p == null) {
            s.sendMessage(Language.getMsg(p2, Messages.COMMAND_TP_PLAYER_NOT_FOUND));
            return true;
        }
        IArena a = Arena.getArenaByPlayer(p);
        IArena a2 = Arena.getArenaByPlayer(p2);
        if (a == null) {
            s.sendMessage(Language.getMsg(p2, Messages.COMMAND_TP_NOT_IN_ARENA));
            return true;
        }
        if (a.getStatus() == GameState.playing) {
            if (a2 != null) {
                if (a2.isPlayer(p2)) {
                    a2.removePlayer(p2, false);
                }
                if (a2.isSpectator(p2)) {
                    if (a2.getArenaName().equals(a.getArenaName())) {
                        TeleportManager.teleport((Entity)p2, p.getLocation());
                        return true;
                    }
                    a2.removeSpectator(p2, false);
                }
            }
            a.addSpectator(p2, false, p.getLocation());
        } else {
            s.sendMessage(Language.getMsg((Player)s, Messages.COMMAND_TP_NOT_STARTED));
        }
        return true;
    }

    @Override
    public List<String> getTabComplete() {
        ArrayList<String> players = new ArrayList<String>();
        for (IArena a : Arena.getArenas()) {
            for (Player p : a.getPlayers()) {
                players.add(p.getName());
            }
            for (Player p : a.getSpectators()) {
                players.add(p.getName());
            }
        }
        return players;
    }
}

