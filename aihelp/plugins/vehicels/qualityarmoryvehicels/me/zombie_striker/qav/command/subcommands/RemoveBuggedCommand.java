/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.command.subcommands;

import java.util.Collection;
import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.MessagesConfig;
import me.zombie_striker.qav.api.QualityArmoryVehicles;
import me.zombie_striker.qav.command.QAVCommand;
import me.zombie_striker.qav.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RemoveBuggedCommand
extends SubCommand {
    public RemoveBuggedCommand(QAVCommand qAVCommand) {
        super(qAVCommand);
    }

    @Override
    public String getName() {
        return "removeBugged";
    }

    @Override
    @Nullable
    public String getDescription(@NotNull CommandSender commandSender) {
        if (commandSender.hasPermission("qualityarmoryvehicles.removebugged")) {
            return " : Removes all bugged vehicles";
        }
        return null;
    }

    @Override
    public void perform(CommandSender commandSender, String[] stringArray) {
        if (!commandSender.hasPermission("qualityarmoryvehicles.removebugged")) {
            commandSender.sendMessage(Main.prefix + MessagesConfig.COMMANDMESSAGES_NO_PERM);
            return;
        }
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(Main.prefix + MessagesConfig.COMMANDMESSAGES_ONLY_PLAYERs);
            return;
        }
        Player player = (Player)commandSender;
        Collection collection = player.getWorld().getEntitiesByClass(ArmorStand.class);
        collection.stream().filter(QualityArmoryVehicles::isVehicle).filter(armorStand -> QualityArmoryVehicles.getVehicleEntityByEntity((Entity)armorStand) == null).forEach(Entity::remove);
        commandSender.sendMessage(Main.prefix + MessagesConfig.COMMANDMESSAGES_REMOVE_BUGGED);
    }
}

