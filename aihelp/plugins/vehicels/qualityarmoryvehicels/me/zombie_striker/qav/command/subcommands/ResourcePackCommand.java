/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.command.subcommands;

import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.MessagesConfig;
import me.zombie_striker.qav.command.QAVCommand;
import me.zombie_striker.qav.command.SubCommand;
import me.zombie_striker.qav.customitemmanager.CustomItemManager;
import me.zombie_striker.qav.util.ForksUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ResourcePackCommand
extends SubCommand {
    public ResourcePackCommand(QAVCommand qAVCommand) {
        super(qAVCommand);
    }

    @Override
    public String getName() {
        return "getresourcepack";
    }

    @Override
    @Nullable
    public String getDescription(@NotNull CommandSender commandSender) {
        return "Returns the resourcepack link";
    }

    @Override
    public void perform(CommandSender commandSender, String[] stringArray) {
        String string = CustomItemManager.getResourcepack(commandSender instanceof Player ? (Player)commandSender : null);
        ForksUtil.sendComponent(commandSender, Main.prefix + " " + MessagesConfig.COMMANDMESSAGES_TEXTURE, null, string == null ? "" : string);
    }
}

