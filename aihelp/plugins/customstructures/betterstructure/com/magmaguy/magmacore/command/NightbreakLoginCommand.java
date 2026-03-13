/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.magmaguy.magmacore.command;

import com.magmaguy.magmacore.command.AdvancedCommand;
import com.magmaguy.magmacore.command.CommandData;
import com.magmaguy.magmacore.command.SenderType;
import com.magmaguy.magmacore.command.arguments.ListStringCommandArgument;
import com.magmaguy.magmacore.nightbreak.NightbreakAccount;
import com.magmaguy.magmacore.util.Logger;
import java.util.ArrayList;
import org.bukkit.plugin.java.JavaPlugin;

public class NightbreakLoginCommand
extends AdvancedCommand {
    private final JavaPlugin plugin;

    public NightbreakLoginCommand(JavaPlugin plugin) {
        super(new ArrayList<String>());
        this.setUsage("/nightbreaklogin <token>");
        this.setDescription("Register your Nightbreak account token for DLC access");
        this.setSenderType(SenderType.ANY);
        this.setPermission("nightbreak.login");
        this.plugin = plugin;
        this.addArgument("token", new ListStringCommandArgument("<token>"));
    }

    @Override
    public void execute(CommandData commandData) {
        NightbreakAccount account;
        if (!commandData.getCommandSender().hasPermission("nightbreak.login")) {
            Logger.sendMessage(commandData.getCommandSender(), "&cYou don't have permission to use this command.");
            return;
        }
        String[] args2 = commandData.getArgs();
        if (args2.length < 1 || args2[0] == null || args2[0].isEmpty()) {
            Logger.sendMessage(commandData.getCommandSender(), "&cUsage: /nightbreaklogin <token>");
            Logger.sendMessage(commandData.getCommandSender(), "&7Get your token at: &9https://nightbreak.io/account");
            return;
        }
        String token = args2[0];
        if (!token.startsWith("nbk_")) {
            Logger.sendMessage(commandData.getCommandSender(), "&eWarning: Token doesn't appear to be a Nightbreak token (should start with 'nbk_').");
            Logger.sendMessage(commandData.getCommandSender(), "&eProceeding anyway...");
        }
        if ((account = NightbreakAccount.registerToken(this.plugin, token)) != null) {
            Logger.sendMessage(commandData.getCommandSender(), "&aNightbreak token registered successfully!");
            Logger.sendMessage(commandData.getCommandSender(), "&7Your token has been saved to the MagmaCore shared config folder.");
            Logger.sendMessage(commandData.getCommandSender(), "&7All MagmaGuy plugins will now have access to Nightbreak DLC features.");
        } else {
            Logger.sendMessage(commandData.getCommandSender(), "&cFailed to save Nightbreak token. Check console for errors.");
        }
    }
}

