/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.destroystokyo.paper.brigadier.BukkitBrigadierCommandSource
 *  com.destroystokyo.paper.event.brigadier.CommandRegisteredEvent
 *  com.mojang.brigadier.Command
 *  com.mojang.brigadier.suggestion.SuggestionProvider
 *  com.mojang.brigadier.tree.LiteralCommandNode
 *  org.bukkit.Bukkit
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 */
package co.aikar.commands;

import co.aikar.commands.ACFBrigadierManager;
import co.aikar.commands.LogLevel;
import co.aikar.commands.PaperCommandManager;
import co.aikar.commands.RegisteredCommand;
import co.aikar.commands.RootCommand;
import co.aikar.commands.UnstableAPI;
import com.destroystokyo.paper.brigadier.BukkitBrigadierCommandSource;
import com.destroystokyo.paper.event.brigadier.CommandRegisteredEvent;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.LiteralCommandNode;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

@Deprecated
@UnstableAPI
public class PaperBrigadierManager
implements Listener {
    private final PaperCommandManager manager;
    private final ACFBrigadierManager<BukkitBrigadierCommandSource> brigadierManager;

    public PaperBrigadierManager(Plugin plugin, PaperCommandManager paperCommandManager) {
        paperCommandManager.verifyUnstableAPI("brigadier");
        paperCommandManager.log(LogLevel.INFO, "Enabled Brigadier Support!");
        this.manager = paperCommandManager;
        this.brigadierManager = new ACFBrigadierManager(paperCommandManager);
        Bukkit.getPluginManager().registerEvents((Listener)this, plugin);
    }

    @EventHandler
    public void onCommandRegister(CommandRegisteredEvent<BukkitBrigadierCommandSource> commandRegisteredEvent) {
        RootCommand rootCommand = this.manager.getRootCommand(commandRegisteredEvent.getCommandLabel());
        if (rootCommand != null) {
            commandRegisteredEvent.setLiteral(this.brigadierManager.register(rootCommand, (LiteralCommandNode<BukkitBrigadierCommandSource>)commandRegisteredEvent.getLiteral(), (SuggestionProvider<BukkitBrigadierCommandSource>)commandRegisteredEvent.getBrigadierCommand(), (Command<BukkitBrigadierCommandSource>)commandRegisteredEvent.getBrigadierCommand(), this::checkPermRoot, this::checkPermSub));
        }
    }

    private boolean checkPermSub(RegisteredCommand registeredCommand, BukkitBrigadierCommandSource bukkitBrigadierCommandSource) {
        return registeredCommand.hasPermission(this.manager.getCommandIssuer(bukkitBrigadierCommandSource.getBukkitSender()));
    }

    private boolean checkPermRoot(RootCommand rootCommand, BukkitBrigadierCommandSource bukkitBrigadierCommandSource) {
        return rootCommand.hasAnyPermission(this.manager.getCommandIssuer(bukkitBrigadierCommandSource.getBukkitSender()));
    }
}

