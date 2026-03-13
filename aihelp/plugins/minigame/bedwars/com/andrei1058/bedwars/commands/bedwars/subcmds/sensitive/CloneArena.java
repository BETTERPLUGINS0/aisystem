/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.ClickEvent$Action
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.command.ParentCommand;
import com.andrei1058.bedwars.api.command.SubCommand;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.Misc;
import com.andrei1058.bedwars.arena.SetupSession;
import com.andrei1058.bedwars.commands.bedwars.MainCommand;
import com.andrei1058.bedwars.configuration.Permissions;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.md_5.bungee.api.chat.ClickEvent;
import org.apache.commons.io.FileUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CloneArena
extends SubCommand {
    public CloneArena(ParentCommand parent, String name) {
        super(parent, name);
        this.setPriority(7);
        this.showInList(true);
        this.setPermission(Permissions.PERMISSION_CLONE);
        this.setDisplayInfo(Misc.msgHoverClick("\u00a76 \u25aa \u00a77/" + this.getParent().getName() + " " + this.getSubCommandName() + " \u00a76<worldName> <newName>", "\u00a7fClone an existing arena.", "/" + this.getParent().getName() + " " + this.getSubCommandName(), ClickEvent.Action.SUGGEST_COMMAND));
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
        if (args.length != 2) {
            p.sendMessage("\u00a7c\u25aa \u00a77Usage: \u00a7o/" + this.getParent().getName() + " " + this.getSubCommandName() + " <mapName> <newArena>");
            return true;
        }
        if (!BedWars.getAPI().getRestoreAdapter().isWorld(args[0])) {
            p.sendMessage("\u00a7c\u25aa \u00a77" + args[0] + " doesn't exist!");
            return true;
        }
        File yml1 = new File(BedWars.plugin.getDataFolder(), "/Arenas/" + args[0] + ".yml");
        File yml2 = new File(BedWars.plugin.getDataFolder(), "/Arenas/" + args[1] + ".yml");
        if (!yml1.exists()) {
            p.sendMessage("\u00a7c\u25aa \u00a77" + args[0] + " doesn't exist!");
            return true;
        }
        if (BedWars.getAPI().getRestoreAdapter().isWorld(args[1]) && yml2.exists()) {
            p.sendMessage("\u00a7c\u25aa \u00a77" + args[1] + " already exist!");
            return true;
        }
        if (args[1].contains("+")) {
            p.sendMessage("\u00a7c\u25aa \u00a77" + args[1] + " mustn't contain this symbol: " + String.valueOf(ChatColor.RED) + "+");
            return true;
        }
        if (Arena.getArenaByName(args[0]) != null) {
            p.sendMessage("\u00a7c\u25aa \u00a77Please disable " + args[0] + " first!");
            return true;
        }
        BedWars.getAPI().getRestoreAdapter().cloneArena(args[0], args[1]);
        if (yml1.exists()) {
            try {
                FileUtils.copyFile(yml1, yml2, true);
            } catch (IOException e) {
                e.printStackTrace();
                p.sendMessage("\u00a7c\u25aa \u00a77An error occurred while copying the map's config. Check the console.");
            }
        }
        p.sendMessage("\u00a76 \u25aa \u00a77Done :D.");
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

