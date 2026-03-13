/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerCommandPreprocessEvent
 */
package com.andrei1058.bedwars.listeners;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.configuration.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CmdProcess
implements Listener {
    @EventHandler
    public void onCmd(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        if (e.getMessage().equals("/party sethome")) {
            p.sendMessage(Language.getMsg(p, Messages.COMMAND_NOT_ALLOWED_IN_GAME));
            e.setCancelled(true);
        }
        if (e.getMessage().equals("/party home")) {
            p.sendMessage(Language.getMsg(p, Messages.COMMAND_NOT_ALLOWED_IN_GAME));
            e.setCancelled(true);
        }
        if (p.hasPermission(Permissions.PERMISSION_COMMAND_BYPASS)) {
            return;
        }
        String[] cmd = e.getMessage().replaceFirst("/", "").split(" ");
        if (cmd.length == 0) {
            return;
        }
        if (Arena.isInArena(p) && !BedWars.config.getList("allowed-commands").contains(cmd[0])) {
            p.sendMessage(Language.getMsg(p, Messages.COMMAND_NOT_ALLOWED_IN_GAME));
            e.setCancelled(true);
        }
    }
}

