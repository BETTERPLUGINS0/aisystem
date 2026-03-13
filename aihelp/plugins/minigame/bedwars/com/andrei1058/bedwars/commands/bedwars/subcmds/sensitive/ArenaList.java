/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.ClickEvent$Action
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.NotNull
 */
package com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.command.ParentCommand;
import com.andrei1058.bedwars.api.command.SubCommand;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.Misc;
import com.andrei1058.bedwars.arena.SetupSession;
import com.andrei1058.bedwars.commands.bedwars.MainCommand;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ArenaList
extends SubCommand {
    private static final int ARENAS_PER_PAGE = 10;

    public ArenaList(ParentCommand parent, String name) {
        super(parent, name);
        this.setPriority(3);
        this.showInList(true);
        this.setDisplayInfo(Misc.msgHoverClick("\u00a76 \u25aa \u00a77/" + MainCommand.getInstance().getName() + " " + this.getSubCommandName() + (String)(this.getArenas().size() == 0 ? " \u00a7c(0 set)" : " \u00a7a(" + this.getArenas().size() + " set)"), "\u00a7fShow available arenas", "/" + MainCommand.getInstance().getName() + " " + this.getSubCommandName(), ClickEvent.Action.RUN_COMMAND));
    }

    @Override
    public boolean execute(String[] args, CommandSender s) {
        if (s instanceof ConsoleCommandSender) {
            return false;
        }
        Player p = (Player)s;
        int page = 1;
        if (args.length >= 1) {
            try {
                page = Integer.parseInt(args[0]);
                if (page < 0) {
                    page = 1;
                }
            } catch (Exception exception) {
                // empty catch block
            }
        }
        int start = (page - 1) * 10;
        ArrayList<IArena> arenas = new ArrayList<IArena>(Arena.getArenas());
        if (arenas.size() <= start) {
            page = 1;
            start = 0;
        }
        p.sendMessage(ArenaList.color(" \n&1|| &3" + BedWars.plugin.getName() + "&7 Instantiated games: \n "));
        if (arenas.isEmpty()) {
            p.sendMessage(String.valueOf(ChatColor.RED) + "No arenas to display.");
            return true;
        }
        int limit = Math.min(arenas.size(), start + 10);
        arenas.subList(start, limit).forEach(arena -> {
            String gameState = arena.getDisplayStatus(Language.getPlayerLanguage(p));
            String msg = ArenaList.color("ID: &e" + arena.getWorldName() + " &fG: &e" + arena.getDisplayGroup(p) + " &fP: &e" + (arena.getPlayers().size() + arena.getSpectators().size()) + " &fS: " + gameState + " &fWl: &e" + (Bukkit.getWorld((String)arena.getWorldName()) != null));
            p.sendMessage(msg);
        });
        p.sendMessage(" ");
        if (arenas.size() > 10 * page) {
            p.sendMessage(String.valueOf(ChatColor.GRAY) + "Type /" + String.valueOf(ChatColor.GREEN) + MainCommand.getInstance().getName() + " arenaList " + ++page + String.valueOf(ChatColor.GRAY) + " for next page.");
        }
        return true;
    }

    @Override
    public List<String> getTabComplete() {
        return null;
    }

    @NotNull
    private List<String> getArenas() {
        ArrayList<String> arene = new ArrayList<String>();
        File dir = new File(BedWars.plugin.getDataFolder(), "/Arenas");
        if (dir.exists()) {
            for (File f : Objects.requireNonNull(dir.listFiles())) {
                if (!f.isFile() || !f.getName().contains(".yml")) continue;
                arene.add(f.getName().replace(".yml", ""));
            }
        }
        return arene;
    }

    @Override
    public boolean canSee(CommandSender s, com.andrei1058.bedwars.api.BedWars api) {
        if (s instanceof Player) {
            Player p = (Player)s;
            if (Arena.isInArena(p)) {
                return false;
            }
            if (SetupSession.isInSetupSession(p.getUniqueId())) {
                return false;
            }
        }
        return this.hasPermission(s);
    }

    private static String color(String msg) {
        return ChatColor.translateAlternateColorCodes((char)'&', (String)msg);
    }
}

