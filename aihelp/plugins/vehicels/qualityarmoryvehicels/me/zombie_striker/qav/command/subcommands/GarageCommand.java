/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.command.subcommands;

import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.MessagesConfig;
import me.zombie_striker.qav.command.QAVCommand;
import me.zombie_striker.qav.command.SubCommand;
import me.zombie_striker.qav.menu.GarageMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GarageCommand
extends SubCommand {
    public GarageCommand(QAVCommand qAVCommand) {
        super(qAVCommand);
    }

    @Override
    public String getName() {
        return "garage";
    }

    @Override
    public String getDescription(@NotNull CommandSender commandSender) {
        if (!Main.enableGarage) {
            return null;
        }
        return MessagesConfig.subcommand_garage;
    }

    @Override
    public void perform(CommandSender commandSender, String[] stringArray) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(Main.prefix + MessagesConfig.COMMANDMESSAGES_ONLY_PLAYERs);
            return;
        }
        new GarageMenu((Player)commandSender, (Player)commandSender).open();
    }
}

