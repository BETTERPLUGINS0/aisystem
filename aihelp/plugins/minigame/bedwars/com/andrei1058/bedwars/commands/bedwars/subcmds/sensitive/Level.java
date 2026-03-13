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
import com.andrei1058.bedwars.api.events.player.PlayerXpGainEvent;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.Misc;
import com.andrei1058.bedwars.arena.SetupSession;
import com.andrei1058.bedwars.configuration.LevelsConfig;
import com.andrei1058.bedwars.configuration.Permissions;
import java.util.List;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Level
extends SubCommand {
    public Level(ParentCommand parent, String name) {
        super(parent, name);
        this.setPermission(Permissions.PERMISSION_LEVEL);
        this.setPriority(10);
        this.showInList(true);
        this.setDisplayInfo(Misc.msgHoverClick("\u00a76 \u25aa \u00a77/" + this.getParent().getName() + " " + this.getSubCommandName() + " \u00a78      - \u00a7eclick for details", "\u00a7fManage a player level.", "/" + this.getParent().getName() + " " + this.getSubCommandName(), ClickEvent.Action.RUN_COMMAND));
    }

    @Override
    public boolean execute(String[] args, CommandSender s) {
        if (args.length == 0) {
            this.sendSubCommands(s, BedWars.getAPI());
            return true;
        }
        if (args[0].equalsIgnoreCase("setlevel")) {
            int level;
            if (args.length != 3) {
                s.sendMessage(String.valueOf(ChatColor.GOLD) + " \u25aa " + String.valueOf(ChatColor.GRAY) + "Usage: /bw level setLevel \u00a7o<player> <level>");
                return true;
            }
            Player pl = Bukkit.getPlayer((String)args[1]);
            if (pl == null) {
                s.sendMessage(String.valueOf(ChatColor.RED) + " \u25aa " + String.valueOf(ChatColor.GRAY) + "Player not found!");
                return true;
            }
            try {
                level = Integer.parseInt(args[2]);
            } catch (Exception e) {
                s.sendMessage(String.valueOf(ChatColor.RED) + "Level must be an integer!");
                return true;
            }
            BedWars.getAPI().getLevelsUtil().setLevel(pl, level);
            int nextLevelCost = LevelsConfig.levels.getYml().get("levels." + level + ".rankup-cost") == null ? LevelsConfig.levels.getInt("levels.others.rankup-cost") : LevelsConfig.levels.getInt("levels." + level + ".rankup-cost");
            String levelName = LevelsConfig.levels.getYml().get("levels." + level + ".name") == null ? LevelsConfig.levels.getYml().getString("levels.others.name") : LevelsConfig.levels.getYml().getString("levels." + level + ".name");
            BedWars.plugin.getServer().getScheduler().runTaskAsynchronously((Plugin)BedWars.plugin, () -> {
                BedWars.getRemoteDatabase().setLevelData(pl.getUniqueId(), level, 0, levelName, nextLevelCost);
                s.sendMessage(String.valueOf(ChatColor.GOLD) + " \u25aa " + String.valueOf(ChatColor.GRAY) + pl.getName() + " level was set to: " + args[2]);
                s.sendMessage(String.valueOf(ChatColor.GOLD) + " \u25aa " + String.valueOf(ChatColor.GRAY) + "The player may need to rejoin to see it updated.");
            });
        } else if (args[0].equalsIgnoreCase("givexp")) {
            int amount;
            if (args.length != 3) {
                s.sendMessage(String.valueOf(ChatColor.GOLD) + " \u25aa " + String.valueOf(ChatColor.GRAY) + "Usage: /bw level giveXp \u00a7o<player> <amount>");
                return true;
            }
            Player pl = Bukkit.getPlayer((String)args[1]);
            if (pl == null) {
                s.sendMessage(String.valueOf(ChatColor.RED) + " \u25aa " + String.valueOf(ChatColor.GRAY) + "Player not found!");
                return true;
            }
            try {
                amount = Integer.parseInt(args[2]);
            } catch (Exception e) {
                s.sendMessage(String.valueOf(ChatColor.RED) + "Amount must be an integer!");
                return true;
            }
            BedWars.getAPI().getLevelsUtil().addXp(pl, amount, PlayerXpGainEvent.XpSource.OTHER);
            BedWars.plugin.getServer().getScheduler().runTaskAsynchronously((Plugin)BedWars.plugin, () -> {
                Object[] data = BedWars.getRemoteDatabase().getLevelData(pl.getUniqueId());
                BedWars.getRemoteDatabase().setLevelData(pl.getUniqueId(), (Integer)data[0], (Integer)data[1] + amount, (String)data[2], (Integer)data[3]);
                s.sendMessage(String.valueOf(ChatColor.GOLD) + " \u25aa " + String.valueOf(ChatColor.GRAY) + args[2] + " xp was given to: " + pl.getName());
                s.sendMessage(String.valueOf(ChatColor.GOLD) + " \u25aa " + String.valueOf(ChatColor.GRAY) + "The player may need to rejoin to see it updated.");
            });
        } else {
            this.sendSubCommands(s, BedWars.getAPI());
        }
        return true;
    }

    private void sendSubCommands(CommandSender s, com.andrei1058.bedwars.api.BedWars api) {
        if (s instanceof Player) {
            Player p = (Player)s;
            p.spigot().sendMessage((BaseComponent)Misc.msgHoverClick("\u00a76 \u25aa \u00a77/" + this.getParent().getName() + " " + this.getSubCommandName() + " setLevel \u00a7o<player> <level>", "Set a player level.", "/" + this.getParent().getName() + " " + this.getSubCommandName() + " setLevel", ClickEvent.Action.SUGGEST_COMMAND));
            p.spigot().sendMessage((BaseComponent)Misc.msgHoverClick("\u00a76 \u25aa \u00a77/" + this.getParent().getName() + " " + this.getSubCommandName() + " giveXp \u00a7o<player> <amount>", "Give Xp to a player.", "/" + this.getParent().getName() + " " + this.getSubCommandName() + " giveXp", ClickEvent.Action.SUGGEST_COMMAND));
        } else {
            s.sendMessage(String.valueOf(ChatColor.GOLD) + "bw level setLevel <player> <level>");
            s.sendMessage(String.valueOf(ChatColor.GOLD) + "bw level giveXp <player> <amount>");
        }
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
        if (Arena.isInArena(p)) {
            return false;
        }
        if (SetupSession.isInSetupSession(p.getUniqueId())) {
            return false;
        }
        return this.hasPermission(s);
    }
}

