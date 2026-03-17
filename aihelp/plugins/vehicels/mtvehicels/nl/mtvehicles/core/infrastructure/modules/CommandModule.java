/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.PluginCommand
 *  org.bukkit.command.TabCompleter
 */
package nl.mtvehicles.core.infrastructure.modules;

import java.util.HashMap;
import lombok.Generated;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.commands.VehicleSubCommandManager;
import nl.mtvehicles.core.commands.VehicleTabCompleterManager;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

public class CommandModule {
    private static CommandModule instance;
    public static HashMap<String, MTVSubCommand> subcommands;

    public CommandModule() {
        PluginCommand pluginCommand = Main.instance.getCommand("minetopiavehicles");
        if (pluginCommand != null) {
            pluginCommand.setExecutor((CommandExecutor)new VehicleSubCommandManager());
            pluginCommand.setTabCompleter((TabCompleter)new VehicleTabCompleterManager());
        }
        VehicleTabCompleterManager.loadVehicleList();
    }

    @Generated
    public static CommandModule getInstance() {
        return instance;
    }

    @Generated
    public static void setInstance(CommandModule instance) {
        CommandModule.instance = instance;
    }

    static {
        subcommands = new HashMap();
    }
}

