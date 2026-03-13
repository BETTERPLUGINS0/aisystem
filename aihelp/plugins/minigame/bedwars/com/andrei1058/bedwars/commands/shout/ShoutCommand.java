/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.command.defaults.BukkitCommand
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.commands.shout;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.arena.Arena;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class ShoutCommand
extends BukkitCommand {
    private static HashMap<UUID, Long> shoutCooldown = new HashMap();

    public ShoutCommand(String name) {
        super(name);
    }

    public boolean execute(CommandSender s, String st, String[] args) {
        if (s instanceof ConsoleCommandSender) {
            return true;
        }
        Player p = (Player)s;
        IArena a = Arena.getArenaByPlayer(p);
        if (a == null || a.isSpectator(p)) {
            p.sendMessage(Language.getMsg(p, Messages.COMMAND_NOT_FOUND_OR_INSUFF_PERMS));
            return true;
        }
        StringBuilder sb = new StringBuilder();
        for (String ar : args) {
            sb.append(ar).append(" ");
        }
        p.chat("!" + sb.toString());
        return false;
    }

    public static void updateShout(Player player) {
        if (player.hasPermission("bw.shout.bypass")) {
            return;
        }
        if (shoutCooldown.containsKey(player.getUniqueId())) {
            shoutCooldown.replace(player.getUniqueId(), System.currentTimeMillis() + (long)BedWars.config.getInt("shout-cmd-cooldown") * 1000L);
        } else {
            shoutCooldown.put(player.getUniqueId(), System.currentTimeMillis() + (long)BedWars.config.getInt("shout-cmd-cooldown") * 1000L);
        }
    }

    public static boolean isShoutCooldown(Player player) {
        if (player.hasPermission("bw.shout.bypass")) {
            return false;
        }
        if (!shoutCooldown.containsKey(player.getUniqueId())) {
            return false;
        }
        return shoutCooldown.get(player.getUniqueId()) > System.currentTimeMillis();
    }

    public static double getShoutCooldown(Player p) {
        return (float)(shoutCooldown.get(p.getUniqueId()) - System.currentTimeMillis()) / 1000.0f;
    }

    public static boolean isShout(Player p) {
        if (!shoutCooldown.containsKey(p.getUniqueId())) {
            return false;
        }
        return shoutCooldown.get(p.getUniqueId()) + 1000L > System.currentTimeMillis();
    }
}

