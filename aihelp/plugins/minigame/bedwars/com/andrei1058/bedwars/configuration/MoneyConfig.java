/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.Plugin
 */
package com.andrei1058.bedwars.configuration;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.configuration.ConfigManager;
import org.bukkit.plugin.Plugin;

public class MoneyConfig
extends ConfigManager {
    public static MoneyConfig money;

    private MoneyConfig() {
        super((Plugin)BedWars.plugin, "rewards", BedWars.plugin.getDataFolder().toString());
    }

    public static void init() {
        money = new MoneyConfig();
        money.getYml().options().copyDefaults(true);
        money.getYml().addDefault("money-rewards.per-minute", (Object)5);
        money.getYml().addDefault("money-rewards.per-teammate", (Object)30);
        money.getYml().addDefault("money-rewards.game-win", (Object)90);
        money.getYml().addDefault("money-rewards.bed-destroyed", (Object)60);
        money.getYml().addDefault("money-rewards.final-kill", (Object)40);
        money.getYml().addDefault("money-rewards.regular-kill", (Object)10);
        money.save();
    }
}

