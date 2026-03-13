/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package com.andrei1058.bedwars.shop.main;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.shop.IBuyItem;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.language.Language;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BuyCommand
implements IBuyItem {
    private final List<String> asPlayer = new ArrayList<String>();
    private final List<String> asConsole = new ArrayList<String>();
    private final String upgradeIdentifier;

    public BuyCommand(String path, YamlConfiguration yml, String upgradeIdentifier) {
        BedWars.debug("Loading BuyCommand: " + path);
        this.upgradeIdentifier = upgradeIdentifier;
        for (Object cmd : yml.getStringList(path + ".as-console")) {
            if (((String)cmd).startsWith("/")) {
                cmd = ((String)cmd).replaceFirst("/", "");
            }
            this.asConsole.add((String)cmd);
        }
        for (Object cmd : yml.getStringList(path + ".as-player")) {
            if (!((String)cmd).startsWith("/")) {
                cmd = "/" + (String)cmd;
            }
            this.asPlayer.add((String)cmd);
        }
    }

    @Override
    public boolean isLoaded() {
        return true;
    }

    @Override
    public void give(Player player, IArena arena) {
        BedWars.debug("Giving BuyCMD: " + this.getUpgradeIdentifier() + " to: " + player.getName());
        String playerName = player.getName();
        String playerUUID = player.getUniqueId().toString();
        ITeam team = arena.getTeam(player);
        String teamName = team == null ? "null" : team.getName();
        String teamDisplay = team == null ? "null" : team.getDisplayName(Language.getPlayerLanguage(player));
        String teamColor = team == null ? ChatColor.WHITE.toString() : team.getColor().chat().toString();
        String arenaIdentifier = arena.getArenaName();
        String arenaWorld = arena.getWorldName();
        String arenaDisplay = arena.getDisplayName();
        String arenaGroup = arena.getGroup();
        for (String playerCmd : this.asPlayer) {
            player.chat(playerCmd.replace("{player}", playerName).replace("{player_uuid}", playerUUID).replace("{team}", teamName).replace("{team_display}", teamDisplay).replace("{team_color}", teamColor).replace("{arena}", arenaIdentifier).replace("{arena_world}", arenaWorld).replace("{arena_display}", arenaDisplay).replace("{arena_group}", arenaGroup));
        }
        for (String consoleCmd : this.asConsole) {
            Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)consoleCmd.replace("{player}", playerName).replace("{player_uuid}", playerUUID).replace("{team}", teamName).replace("{team_display}", teamDisplay).replace("{team_color}", teamColor).replace("{arena}", arenaIdentifier).replace("{arena_world}", arenaWorld).replace("{arena_display}", arenaDisplay).replace("{arena_group}", arenaGroup));
        }
    }

    @Override
    public String getUpgradeIdentifier() {
        return this.upgradeIdentifier;
    }

    @Override
    public ItemStack getItemStack() {
        return null;
    }

    @Override
    public void setItemStack(ItemStack itemStack) {
    }

    @Override
    public boolean isAutoEquip() {
        return false;
    }

    @Override
    public void setAutoEquip(boolean autoEquip) {
    }

    @Override
    public boolean isPermanent() {
        return false;
    }

    @Override
    public void setPermanent(boolean permanent) {
    }

    @Override
    public boolean isUnbreakable() {
        return false;
    }

    @Override
    public void setUnbreakable(boolean unbreakable) {
    }
}

