/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 */
package co.aikar.commands;

import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.BukkitCommandExecutionContext;
import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.CommandCompletions;
import co.aikar.commands.CommandContexts;
import co.aikar.commands.PaperAsyncTabCompleteHandler;
import co.aikar.commands.PaperBrigadierManager;
import co.aikar.commands.PaperCommandCompletions;
import co.aikar.commands.PaperCommandContexts;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class PaperCommandManager
extends BukkitCommandManager {
    private boolean brigadierAvailable;

    public PaperCommandManager(Plugin plugin) {
        super(plugin);
        try {
            Class.forName("com.destroystokyo.paper.event.server.AsyncTabCompleteEvent");
            plugin.getServer().getPluginManager().registerEvents((Listener)new PaperAsyncTabCompleteHandler(this), plugin);
        } catch (ClassNotFoundException classNotFoundException) {
            // empty catch block
        }
        try {
            Class.forName("com.destroystokyo.paper.event.brigadier.CommandRegisteredEvent");
            this.brigadierAvailable = true;
        } catch (ClassNotFoundException classNotFoundException) {
            // empty catch block
        }
    }

    @Override
    public void enableUnstableAPI(String string) {
        super.enableUnstableAPI(string);
        if ("brigadier".equals(string) && this.brigadierAvailable) {
            new PaperBrigadierManager(this.plugin, this);
        }
    }

    @Override
    public synchronized CommandContexts<BukkitCommandExecutionContext> getCommandContexts() {
        if (this.contexts == null) {
            this.contexts = new PaperCommandContexts(this);
        }
        return this.contexts;
    }

    @Override
    public synchronized CommandCompletions<BukkitCommandCompletionContext> getCommandCompletions() {
        if (this.completions == null) {
            this.completions = new PaperCommandCompletions(this);
        }
        return this.completions;
    }
}

