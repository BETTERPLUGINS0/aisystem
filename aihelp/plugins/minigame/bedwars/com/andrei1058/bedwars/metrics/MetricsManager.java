/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.andrei1058.bedwars.metrics;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.libs.bstats.bukkit.Metrics;
import com.andrei1058.bedwars.libs.bstats.charts.SimplePie;
import com.andrei1058.bedwars.support.citizens.JoinNPC;
import java.util.concurrent.Callable;

public class MetricsManager {
    private static MetricsManager instance;
    private final Metrics metrics;

    private MetricsManager(BedWars plugin) {
        this.metrics = new Metrics(plugin, 1885);
        this.metrics.addCustomChart(new SimplePie("server_type", () -> BedWars.getServerType().toString()));
        this.metrics.addCustomChart(new SimplePie("default_language", () -> Language.getDefaultLanguage().getIso()));
        this.metrics.addCustomChart(new SimplePie("auto_scale", () -> String.valueOf(BedWars.autoscale)));
        this.metrics.addCustomChart(new SimplePie("party_adapter", () -> BedWars.getParty().getClass().getName()));
        this.metrics.addCustomChart(new SimplePie("chat_adapter", () -> BedWars.getChatSupport().getClass().getName()));
        this.metrics.addCustomChart(new SimplePie("level_adapter", () -> BedWars.getLevelSupport().getClass().getName()));
        this.metrics.addCustomChart(new SimplePie("db_adapter", () -> BedWars.getRemoteDatabase().getClass().getName()));
        this.metrics.addCustomChart(new SimplePie("map_adapter", () -> BedWars.getAPI().getRestoreAdapter().getClass().getName()));
        this.metrics.addCustomChart(new SimplePie("citizens_support", () -> String.valueOf(JoinNPC.isCitizensSupport())));
    }

    public Metrics getMetrics() {
        return this.metrics;
    }

    public static void appendPie(String id, Callable<String> callable) {
        if (null == instance) {
            throw new RuntimeException("Metrics manager is not initialized!");
        }
        instance.getMetrics().addCustomChart(new SimplePie(id, callable));
    }

    public static void initService(BedWars plugin) {
        if (null != instance) {
            return;
        }
        instance = new MetricsManager(plugin);
    }
}

