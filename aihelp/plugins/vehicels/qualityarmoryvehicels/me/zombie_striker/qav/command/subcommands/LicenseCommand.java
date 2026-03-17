/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.command.subcommands;

import me.zombie_striker.qav.command.QAVCommand;
import me.zombie_striker.qav.command.SubCommand;
import me.zombie_striker.qav.premium.PremiumHandler;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LicenseCommand
extends SubCommand {
    public LicenseCommand(QAVCommand qAVCommand) {
        super(qAVCommand);
    }

    @Override
    public String getName() {
        return "license";
    }

    @Override
    @Nullable
    public String getDescription(@NotNull CommandSender commandSender) {
        return null;
    }

    @Override
    public void perform(CommandSender commandSender, String[] stringArray) {
        PremiumHandler.sendMessage(commandSender);
    }
}

