/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.Plugin
 *  org.jetbrains.annotations.NotNull
 */
package com.andrei1058.bedwars.configuration;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.configuration.ConfigManager;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class LevelsConfig
extends ConfigManager {
    public static LevelsConfig levels;

    private LevelsConfig() {
        super((Plugin)BedWars.plugin, "levels", BedWars.plugin.getDataFolder().toString());
    }

    public static void init() {
        levels = new LevelsConfig();
        levels.getYml().options().copyDefaults(true);
        if (levels.isFirstTime()) {
            levels.getYml().addDefault("levels.1.name", (Object)"&7[{number}\u2729] ");
            levels.getYml().addDefault("levels.1.rankup-cost", (Object)1000);
            levels.getYml().addDefault("levels.2.name", (Object)"&7[{number}\u2729] ");
            levels.getYml().addDefault("levels.2.rankup-cost", (Object)2000);
            levels.getYml().addDefault("levels.3.name", (Object)"&7[{number}\u2729] ");
            levels.getYml().addDefault("levels.3.rankup-cost", (Object)3000);
            levels.getYml().addDefault("levels.4.name", (Object)"&7[{number}\u2729] ");
            levels.getYml().addDefault("levels.4.rankup-cost", (Object)3500);
            levels.getYml().addDefault("levels.5-10.name", (Object)"&e[{number}\u2729] ");
            levels.getYml().addDefault("levels.5-10.rankup-cost", (Object)5000);
            levels.getYml().addDefault("levels.others.name", (Object)"&7[{number}\u2729] ");
            levels.getYml().addDefault("levels.others.rankup-cost", (Object)5000);
        }
        levels.getYml().addDefault("xp-rewards.per-minute", (Object)10);
        levels.getYml().addDefault("xp-rewards.per-teammate", (Object)5);
        levels.getYml().addDefault("xp-rewards.game-win", (Object)100);
        levels.getYml().addDefault("xp-rewards.bed-destroyed", (Object)15);
        levels.getYml().addDefault("xp-rewards.regular-kill", (Object)10);
        levels.getYml().addDefault("xp-rewards.final-kill", (Object)15);
        levels.getYml().addDefault("progress-bar.symbol", (Object)"\u25a0");
        levels.getYml().addDefault("progress-bar.unlocked-color", (Object)"&b");
        levels.getYml().addDefault("progress-bar.locked-color", (Object)"&7");
        levels.getYml().addDefault("progress-bar.format", (Object)"&8 [{progress}&8]");
        levels.save();
    }

    @NotNull
    public static String getLevelName(int level) {
        String name = levels.getYml().getString("levels." + level + ".name");
        if (name != null) {
            return name;
        }
        for (String key : levels.getYml().getConfigurationSection("levels").getKeys(false)) {
            int nr2;
            int nr1;
            String[] nrs;
            if (!key.contains("-") || (nrs = key.split("-")).length != 2) continue;
            try {
                nr1 = Integer.parseInt(nrs[0]);
                nr2 = Integer.parseInt(nrs[1]);
            } catch (Exception ex) {
                continue;
            }
            if (nr1 > level || level > nr2) continue;
            return levels.getYml().getString("levels." + key + ".name");
        }
        return levels.getYml().getString("levels.others.name");
    }

    public static int getNextCost(int level) {
        if (levels.getYml().get("levels." + level + ".rankup-cost") != null) {
            return levels.getYml().getInt("levels." + level + ".rankup-cost");
        }
        for (String key : levels.getYml().getConfigurationSection("levels").getKeys(false)) {
            int nr2;
            int nr1;
            String[] nrs;
            if (!key.contains("-") || (nrs = key.split("-")).length != 2) continue;
            try {
                nr1 = Integer.parseInt(nrs[0]);
                nr2 = Integer.parseInt(nrs[1]);
            } catch (Exception ex) {
                continue;
            }
            if (nr1 > level || level > nr2) continue;
            return levels.getYml().getInt("levels." + key + ".rankup-cost");
        }
        return levels.getYml().getInt("levels.others.rankup-cost");
    }
}

