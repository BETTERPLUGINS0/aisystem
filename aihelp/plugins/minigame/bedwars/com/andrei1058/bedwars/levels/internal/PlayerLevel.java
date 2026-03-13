/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.event.Event
 *  org.bukkit.plugin.Plugin
 */
package com.andrei1058.bedwars.levels.internal;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.events.player.PlayerLevelUpEvent;
import com.andrei1058.bedwars.api.events.player.PlayerXpGainEvent;
import com.andrei1058.bedwars.configuration.LevelsConfig;
import java.text.NumberFormat;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

public class PlayerLevel {
    private UUID uuid;
    private int level;
    private int nextLevelCost;
    private String levelName;
    private int currentXp;
    private String progressBar;
    private String requiredXp;
    private String formattedCurrentXp;
    private boolean modified = false;
    private static ConcurrentHashMap<UUID, PlayerLevel> levelByPlayer = new ConcurrentHashMap();

    public PlayerLevel(UUID player, int level, int currentXp) {
        this.uuid = player;
        this.setLevelName(level);
        this.setNextLevelCost(level, true);
        if (level < 1) {
            level = 1;
        }
        if (currentXp < 0) {
            currentXp = 0;
        }
        this.level = level;
        this.currentXp = currentXp;
        this.updateProgressBar();
        if (!levelByPlayer.containsKey(player)) {
            levelByPlayer.put(player, this);
        }
    }

    public void setLevelName(int level) {
        this.levelName = ChatColor.translateAlternateColorCodes((char)'&', (String)LevelsConfig.getLevelName(level)).replace("{number}", String.valueOf(level));
    }

    public void setNextLevelCost(int level, boolean initialize) {
        if (!initialize) {
            this.modified = true;
        }
        this.nextLevelCost = LevelsConfig.getNextCost(level);
    }

    public void lazyLoad(int level, int currentXp) {
        this.modified = false;
        if (level < 1) {
            level = 1;
        }
        if (currentXp < 0) {
            currentXp = 0;
        }
        this.setLevelName(level);
        this.setNextLevelCost(level, true);
        this.level = level;
        this.currentXp = currentXp;
        this.updateProgressBar();
        this.modified = false;
    }

    private void updateProgressBar() {
        double l1 = (double)(this.nextLevelCost - this.currentXp) / (double)this.nextLevelCost * 10.0;
        int locked = (int)l1;
        int unlocked = 10 - locked;
        if (locked < 0 || unlocked < 0) {
            locked = 10;
            unlocked = 0;
        }
        this.progressBar = ChatColor.translateAlternateColorCodes((char)'&', (String)LevelsConfig.levels.getString("progress-bar.format").replace("{progress}", LevelsConfig.levels.getString("progress-bar.unlocked-color") + String.valueOf(new char[unlocked]).replace("\u0000", LevelsConfig.levels.getString("progress-bar.symbol")) + LevelsConfig.levels.getString("progress-bar.locked-color") + String.valueOf(new char[locked]).replace("\u0000", LevelsConfig.levels.getString("progress-bar.symbol"))));
        this.requiredXp = this.formatNumber(this.nextLevelCost);
        this.formattedCurrentXp = this.formatNumber(this.currentXp);
    }

    public int getLevel() {
        return this.level;
    }

    public int getNextLevelCost() {
        return this.nextLevelCost;
    }

    public static PlayerLevel getLevelByPlayer(UUID player) {
        return levelByPlayer.getOrDefault(player, new PlayerLevel(player, 1, 0));
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getLevelName() {
        return this.levelName;
    }

    public int getCurrentXp() {
        return this.currentXp;
    }

    public String getProgress() {
        return this.progressBar;
    }

    public String getFormattedRequiredXp() {
        return this.requiredXp;
    }

    public void addXp(int xp, PlayerXpGainEvent.XpSource source) {
        if (xp < 0) {
            return;
        }
        this.currentXp += xp;
        this.upgradeLevel();
        this.updateProgressBar();
        Bukkit.getPluginManager().callEvent((Event)new PlayerXpGainEvent(Bukkit.getPlayer((UUID)this.uuid), xp, source));
        this.modified = true;
    }

    public void setXp(int currentXp) {
        if (currentXp <= 0) {
            currentXp = 0;
        }
        this.currentXp = currentXp;
        this.upgradeLevel();
        this.updateProgressBar();
        this.modified = true;
    }

    public void setLevel(int level) {
        this.level = level;
        this.nextLevelCost = LevelsConfig.getNextCost(level);
        this.levelName = ChatColor.translateAlternateColorCodes((char)'&', (String)LevelsConfig.getLevelName(level)).replace("{number}", String.valueOf(level));
        this.requiredXp = this.nextLevelCost >= 1000 ? (this.nextLevelCost % 1000 == 0 ? this.nextLevelCost / 1000 + "k" : (double)this.nextLevelCost / 1000.0 + "k") : String.valueOf(this.nextLevelCost);
        this.updateProgressBar();
        this.modified = true;
    }

    public String getFormattedCurrentXp() {
        return this.formattedCurrentXp;
    }

    public void upgradeLevel() {
        if (this.currentXp >= this.nextLevelCost) {
            this.currentXp -= this.nextLevelCost;
            ++this.level;
            this.nextLevelCost = LevelsConfig.getNextCost(this.level);
            this.levelName = ChatColor.translateAlternateColorCodes((char)'&', (String)LevelsConfig.getLevelName(this.level)).replace("{number}", String.valueOf(this.level));
            this.requiredXp = this.formatNumber(this.nextLevelCost);
            this.formattedCurrentXp = this.formatNumber(this.currentXp);
            Bukkit.getPluginManager().callEvent((Event)new PlayerLevelUpEvent(Bukkit.getPlayer((UUID)this.getUuid()), this.level, this.nextLevelCost));
            this.modified = true;
        }
    }

    private String formatNumber(int score) {
        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(0);
        if (score >= 1000) {
            return format.format((double)score / 1000.0) + "k";
        }
        return format.format(score);
    }

    public int getPlayerLevel() {
        return this.level;
    }

    public void destroy() {
        levelByPlayer.remove(this.uuid);
        this.updateDatabase();
    }

    public void updateDatabase() {
        if (this.modified) {
            Bukkit.getScheduler().runTaskAsynchronously((Plugin)BedWars.plugin, () -> BedWars.getRemoteDatabase().setLevelData(this.uuid, this.level, this.currentXp, LevelsConfig.getLevelName(this.level), this.nextLevelCost));
            this.modified = false;
        }
    }
}

