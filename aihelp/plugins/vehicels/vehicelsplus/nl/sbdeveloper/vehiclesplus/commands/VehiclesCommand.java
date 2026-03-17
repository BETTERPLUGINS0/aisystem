/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Color
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
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
import java.util.Optional;
import nl.sbdeveloper.vehiclesplus.api.VehiclesPlusAPI;
import nl.sbdeveloper.vehiclesplus.api.events.impl.VehicleGiveEvent;
import nl.sbdeveloper.vehiclesplus.api.garages.Garage;
import nl.sbdeveloper.vehiclesplus.api.nbt.NBTColorAdapter;
import nl.sbdeveloper.vehiclesplus.api.vehicles.VehicleModel;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.SpawnedVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.StorageVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.skin.Skin;
import nl.sbdeveloper.vehiclesplus.handlers.EconomyAdapter;
import nl.sbdeveloper.vehiclesplus.inventories.vehicles.shop.VehicleShopGUI;
import nl.sbdeveloper.vehiclesplus.libs.xseries.XMaterial;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import nl.sbdeveloper.vehiclesplus.utils.ColorUtil;
import nl.sbdeveloper.vehiclesplus.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

@CommandAlias(value="vehicles|vehicle|vp|v")
@Description(value="{@@vehiclesplus.commands.vehicles.descriptions.main}")
public class VehiclesCommand
extends BaseCommand {
    private static final String toSpawn = "Spawn ID: %%__USER__%%";
    private static final String toSpawnPersistent = "Spawn persistent ID: %%__RESOURCE__%%";
    private static final String toHelp = "You received the code: %%__LICENSE_2__%%";
    private static final String voucher = "Your voucher code: %%__PHRASE_1__%%";
    private static final String shop = "Welcome to the shop: %%__PHRASE_2__%%";

    @HelpCommand
    @CatchUnknown
    @Default
    public static void onHelp(CommandSender commandSender, CommandHelp commandHelp) {
        commandHelp.showHelp();
    }

    @Subcommand(value="spawn")
    @Description(value="{@@vehiclesplus.commands.vehicles.descriptions.spawn}")
    @CommandPermission(value="vp.spawn.admin")
    @CommandCompletion(value="@vehiclemodels @range:255 @range:255 @range:255")
    public void spawn(Player player, VehicleModel vehicleModel, @Default(value="-1") int n, @Default(value="-1") int n2, @Default(value="-1") int n3) {
        StorageVehicle storageVehicle2 = VehiclesPlusAPI.createVehicle(vehicleModel, VehiclesPlusAPI.getPersonalGarage((OfflinePlayer)player), storageVehicle -> {
            if (n != -1 && n2 != -1 && n3 != -1) {
                if (n > 255 || n2 > 255 || n3 > 255) {
                    player.sendMessage(Locale.getMessage(PluginMessage.COMMANDS_VEHICLES_GIVE_INVALIDCOLOR));
                    return null;
                }
                try {
                    storageVehicle.getParts(Skin.class).forEach(skin -> skin.setColor(Color.fromRGB((int)n, (int)n2, (int)n3), false));
                } catch (IllegalStateException illegalStateException) {
                    player.sendMessage(Locale.getMessage(PluginMessage.COMMANDS_VEHICLES_GIVE_NOCOLOR));
                    return null;
                }
            }
            return storageVehicle;
        });
        if (storageVehicle2 == null) {
            return;
        }
        storageVehicle2.spawn(player, false);
        player.sendMessage(Locale.getMessage(PluginMessage.COMMANDS_VEHICLES_SPAWN_SPAWNED, (Map<String, String>)Map.of((Object)"%vehicle%", (Object)vehicleModel.getDisplayNameColored())));
    }

    @Subcommand(value="spawnpersistent")
    @Description(value="{@@vehiclesplus.commands.vehicles.descriptions.spawnpersistent}")
    @CommandPermission(value="vp.spawnpersistent.admin")
    @CommandCompletion(value="@vehiclemodels @range:255 @range:255 @range:255")
    public void spawnPersistent(Player player, VehicleModel vehicleModel, @Default(value="-1") int n, @Default(value="-1") int n2, @Default(value="-1") int n3) {
        StorageVehicle storageVehicle2 = VehiclesPlusAPI.createVehicle(vehicleModel, storageVehicle -> {
            if (n != -1 && n2 != -1 && n3 != -1) {
                if (n > 255 || n2 > 255 || n3 > 255) {
                    player.sendMessage(Locale.getMessage(PluginMessage.COMMANDS_VEHICLES_GIVE_INVALIDCOLOR));
                    return null;
                }
                try {
                    storageVehicle.getParts(Skin.class).forEach(skin -> skin.setColor(Color.fromRGB((int)n, (int)n2, (int)n3), false));
                } catch (IllegalStateException illegalStateException) {
                    player.sendMessage(Locale.getMessage(PluginMessage.COMMANDS_VEHICLES_GIVE_NOCOLOR));
                    return null;
                }
            }
            return storageVehicle;
        });
        if (storageVehicle2 == null) {
            return;
        }
        storageVehicle2.spawnPersistent(player.getLocation());
        player.sendMessage(Locale.getMessage(PluginMessage.COMMANDS_VEHICLES_SPAWNPERSISTENT_SPAWNED, (Map<String, String>)Map.of((Object)"%vehicle%", (Object)vehicleModel.getDisplayNameColored())));
    }

    @Subcommand(value="give")
    @Description(value="{@@vehiclesplus.commands.vehicles.descriptions.give}")
    @CommandPermission(value="vp.give")
    @CommandCompletion(value="@garages @vehiclemodels @range:255 @range:255 @range:255")
    public void give(CommandSender commandSender, String string, VehicleModel vehicleModel, @Default(value="-1") int n, @Default(value="-1") int n2, @Default(value="-1") int n3) {
        Optional<Garage> optional = VehiclesPlusAPI.getGarage(string);
        if (optional.isEmpty()) {
            commandSender.sendMessage(Locale.getMessage(PluginMessage.COMMANDS_VEHICLES_GIVE_INVALIDGARAGE));
            return;
        }
        Garage garage = optional.get();
        VehicleGiveEvent vehicleGiveEvent = new VehicleGiveEvent(vehicleModel, commandSender, garage, VehicleGiveEvent.Source.COMMAND);
        Bukkit.getPluginManager().callEvent((Event)vehicleGiveEvent);
        if (vehicleGiveEvent.isCancelled()) {
            return;
        }
        VehiclesPlusAPI.createVehicle(vehicleModel, garage, storageVehicle -> {
            if (n != -1 && n2 != -1 && n3 != -1) {
                if (n > 255 || n2 > 255 || n3 > 255) {
                    commandSender.sendMessage(Locale.getMessage(PluginMessage.COMMANDS_VEHICLES_GIVE_INVALIDCOLOR));
                    return null;
                }
                try {
                    storageVehicle.getParts(Skin.class).forEach(skin -> skin.setColor(Color.fromRGB((int)n, (int)n2, (int)n3), false));
                } catch (IllegalStateException illegalStateException) {
                    commandSender.sendMessage(Locale.getMessage(PluginMessage.COMMANDS_VEHICLES_GIVE_NOCOLOR));
                    return null;
                }
            }
            return storageVehicle;
        });
        commandSender.sendMessage(Locale.getMessage(PluginMessage.COMMANDS_VEHICLES_GIVE_ADDED, (Map<String, String>)Map.of((Object)"%vehicle%", (Object)vehicleModel.getDisplayNameColored(), (Object)"%garage%", (Object)ColorUtil.__(garage.getDisplayName()))));
    }

    @Subcommand(value="getvoucher")
    @Description(value="{@@vehiclesplus.commands.vehicles.descriptions.getvoucher}")
    @CommandPermission(value="vp.voucher")
    @CommandCompletion(value="@vehiclemodels @range:255 @range:255 @range:255")
    public void giveVoucher(Player player, VehicleModel vehicleModel, @Default(value="-1") int n, @Default(value="-1") int n2, @Default(value="-1") int n3) {
        ItemStack itemStack = new ItemBuilder(XMaterial.PAPER).displayname(Locale.getMessage(PluginMessage.COMMANDS_VEHICLES_GETVOUCHER_ITEM_NAME)).lore(Locale.getMessage(PluginMessage.COMMANDS_VEHICLES_GETVOUCHER_ITEM_LORE, (Map<String, String>)Map.of((Object)"%vehicle%", (Object)vehicleModel.getDisplayNameColored(), (Object)"%color%", (Object)(n == -1 ? "none" : Color.fromRGB((int)n, (int)n2, (int)n3).toString())))).applyNBT(readWriteItemNBT -> {
            readWriteItemNBT.setString("vehicle", vehicleModel.getId());
            if (n != -1 && n2 != -1 && n3 != -1) {
                readWriteItemNBT.setString("color", NBTColorAdapter.INSTANCE.serialize(Color.fromRGB((int)n, (int)n2, (int)n3)));
            }
        }).getItemStack();
        player.sendMessage(Locale.getMessage(PluginMessage.COMMANDS_VEHICLES_GETVOUCHER_RECEIVED, (Map<String, String>)Map.of((Object)"%vehicle%", (Object)vehicleModel.getDisplayNameColored())));
        player.getInventory().addItem(new ItemStack[]{itemStack});
    }

    @Subcommand(value="shop")
    @Description(value="{@@vehiclesplus.commands.vehicles.descriptions.shop}")
    @CommandPermission(value="vp.shop")
    public void shop(Player player) {
        if (!EconomyAdapter.isLoaded()) {
            player.sendMessage(Locale.getMessage(PluginMessage.GENERAL_ERRORS_NOECONOMY));
            return;
        }
        new VehicleShopGUI(player);
    }

    @Subcommand(value="shop")
    @Description(value="{@@vehiclesplus.commands.vehicles.descriptions.shop}")
    @CommandPermission(value="vp.shop.others")
    @CommandCompletion(value="@players")
    public void shop(CommandSender commandSender, OnlinePlayer onlinePlayer) {
        if (!EconomyAdapter.isLoaded()) {
            commandSender.sendMessage(Locale.getMessage(PluginMessage.GENERAL_ERRORS_NOECONOMY));
            return;
        }
        new VehicleShopGUI(onlinePlayer.getPlayer());
    }

    @Subcommand(value="repair")
    @Description(value="{@@vehiclesplus.commands.vehicles.descriptions.repair}")
    @CommandPermission(value="vp.repair.admin")
    @CommandCompletion(value="@vehiclemodels")
    public void repair(CommandSender commandSender, OnlinePlayer onlinePlayer) {
        Optional<SpawnedVehicle> optional = VehiclesPlusAPI.getVehicle(onlinePlayer.getPlayer());
        if (optional.isEmpty()) {
            commandSender.sendMessage(Locale.getMessage(PluginMessage.COMMANDS_VEHICLES_REPAIR_NOVEHICLE));
            return;
        }
        SpawnedVehicle spawnedVehicle = optional.get();
        spawnedVehicle.getStatics().setCurrentHealth(spawnedVehicle.getVehicleModel().getHealth());
        commandSender.sendMessage(Locale.getMessage(PluginMessage.COMMANDS_VEHICLES_REPAIR_REPAIRED, (Map<String, String>)Map.of((Object)"%vehicle%", (Object)spawnedVehicle.getVehicleModel().getDisplayNameColored())));
    }
}

