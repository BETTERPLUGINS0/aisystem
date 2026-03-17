/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package nl.sbdeveloper.vehiclesplus.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import java.util.Map;
import nl.sbdeveloper.vehiclesplus.api.vehicles.fuel.FuelType;
import nl.sbdeveloper.vehiclesplus.handlers.EconomyAdapter;
import nl.sbdeveloper.vehiclesplus.inventories.fuel.FuelGUI;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandAlias(value="fuel|vpfuel|vpf|fuels")
@Description(value="{@@vehiclesplus.commands.fuel.descriptions.main}")
public class FuelCommand
extends BaseCommand {
    @HelpCommand
    @CatchUnknown
    @Default
    public static void onHelp(CommandSender commandSender, CommandHelp commandHelp) {
        commandHelp.showHelp();
    }

    @Subcommand(value="give")
    @Description(value="{@@vehiclesplus.commands.fuel.descriptions.give}")
    @CommandPermission(value="vp.fuel.give")
    @CommandCompletion(value="@players @fueltypes @nothing")
    public void give(CommandSender commandSender, OnlinePlayer onlinePlayer, FuelType fuelType, @Default(value="1") double d) {
        onlinePlayer.getPlayer().getInventory().addItem(new ItemStack[]{fuelType.getFuel(d)});
        commandSender.sendMessage(Locale.getMessage(PluginMessage.COMMANDS_FUEL_GIVE));
    }

    @Subcommand(value="buy")
    @Description(value="{@@vehiclesplus.commands.fuel.descriptions.buy}")
    @CommandPermission(value="vp.fuel.buy")
    @CommandCompletion(value="@fueltypes @nothing")
    public void buy(Player player, FuelType fuelType, @Default(value="1") double d) {
        if (!EconomyAdapter.withdraw(player, fuelType.getPricePerLiter() * d)) {
            return;
        }
        player.getInventory().addItem(new ItemStack[]{fuelType.getFuel(d)});
        player.sendMessage(Locale.getMessage(PluginMessage.COMMANDS_FUEL_BUY, (Map<String, String>)Map.of((Object)"%liters%", (Object)String.valueOf(d), (Object)"%type%", (Object)fuelType.getName())));
    }

    @Subcommand(value="shop")
    @Description(value="{@@vehiclesplus.commands.fuel.descriptions.shop}")
    @CommandPermission(value="vp.fuel.shop")
    public void shop(Player player) {
        if (!EconomyAdapter.isLoaded()) {
            player.sendMessage(Locale.getMessage(PluginMessage.GENERAL_ERRORS_NOECONOMY));
            return;
        }
        new FuelGUI(player);
    }

    @Subcommand(value="shop")
    @Description(value="{@@vehiclesplus.commands.fuel.descriptions.shop}")
    @CommandPermission(value="vp.fuel.shop.others")
    @CommandCompletion(value="@players")
    public void shop(CommandSender commandSender, OnlinePlayer onlinePlayer) {
        if (!EconomyAdapter.isLoaded()) {
            commandSender.sendMessage(Locale.getMessage(PluginMessage.GENERAL_ERRORS_NOECONOMY));
            return;
        }
        new FuelGUI(onlinePlayer.getPlayer());
    }
}

