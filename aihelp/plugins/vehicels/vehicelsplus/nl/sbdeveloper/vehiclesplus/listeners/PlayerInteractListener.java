/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Color
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.command.CommandSender
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.ItemStack
 */
package nl.sbdeveloper.vehiclesplus.listeners;

import java.util.Map;
import java.util.Optional;
import nl.sbdeveloper.vehiclesplus.api.VehiclesPlusAPI;
import nl.sbdeveloper.vehiclesplus.api.events.impl.VehicleGiveEvent;
import nl.sbdeveloper.vehiclesplus.api.garages.Garage;
import nl.sbdeveloper.vehiclesplus.api.nbt.NBTColorAdapter;
import nl.sbdeveloper.vehiclesplus.api.vehicles.VehicleModel;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.skin.Skin;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBT;
import nl.sbdeveloper.vehiclesplus.libs.xseries.XMaterial;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import nl.sbdeveloper.vehiclesplus.utils.ColorUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener
implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent playerInteractEvent) {
        if (playerInteractEvent.getAction().name().contains("RIGHT_CLICK") && playerInteractEvent.getItem() != null && XMaterial.PAPER.isSimilar(playerInteractEvent.getItem())) {
            ItemStack itemStack = playerInteractEvent.getItem();
            Pair pair = NBT.get(itemStack, readableItemNBT -> {
                String string = readableItemNBT.getString("vehicle");
                Color color = readableItemNBT.hasTag("color") ? NBTColorAdapter.INSTANCE.deserialize(readableItemNBT.getString("color")) : null;
                return Pair.of(string, color);
            });
            Optional<VehicleModel> optional = VehiclesPlusAPI.getVehicleModel((String)pair.getLeft());
            if (optional.isEmpty()) {
                throw new IllegalStateException("Vehicle model on voucher not found: " + (String)pair.getLeft());
            }
            VehicleModel vehicleModel = optional.get();
            Garage garage = VehiclesPlusAPI.getPersonalGarage((OfflinePlayer)playerInteractEvent.getPlayer());
            VehicleGiveEvent vehicleGiveEvent = new VehicleGiveEvent(vehicleModel, (CommandSender)playerInteractEvent.getPlayer(), garage, VehicleGiveEvent.Source.VOUCHER);
            Bukkit.getPluginManager().callEvent((Event)vehicleGiveEvent);
            if (vehicleGiveEvent.isCancelled()) {
                return;
            }
            VehiclesPlusAPI.createVehicle(vehicleModel, garage, storageVehicle -> {
                if (pair.getRight() != null) {
                    try {
                        storageVehicle.getParts(Skin.class).forEach(skin -> skin.setColor((Color)pair.getRight(), false));
                    } catch (IllegalStateException illegalStateException) {
                        playerInteractEvent.getPlayer().sendMessage(Locale.getMessage(PluginMessage.COMMANDS_VEHICLES_GIVE_NOCOLOR));
                        return null;
                    }
                }
                return storageVehicle;
            });
            playerInteractEvent.getPlayer().sendMessage(Locale.getMessage(PluginMessage.COMMANDS_VEHICLES_GIVE_ADDED, (Map<String, String>)Map.of((Object)"%vehicle%", (Object)vehicleModel.getDisplayNameColored(), (Object)"%garage%", (Object)ColorUtil.__(garage.getDisplayName()))));
        }
    }
}

