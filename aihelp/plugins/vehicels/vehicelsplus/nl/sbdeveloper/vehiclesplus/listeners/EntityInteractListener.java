/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerInteractAtEntityEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.MetadataValue
 */
package nl.sbdeveloper.vehiclesplus.listeners;

import co.aikar.commands.MessageKeys;
import java.util.Optional;
import nl.sbdeveloper.vehiclesplus.api.VehiclesPlusAPI;
import nl.sbdeveloper.vehiclesplus.api.events.impl.VehicleClickEvent;
import nl.sbdeveloper.vehiclesplus.api.events.impl.VehicleRefuelEvent;
import nl.sbdeveloper.vehiclesplus.api.garages.Garage;
import nl.sbdeveloper.vehiclesplus.api.garages.permissions.GaragePermissions;
import nl.sbdeveloper.vehiclesplus.api.nbt.NBTColorAdapter;
import nl.sbdeveloper.vehiclesplus.api.nbt.NBTDataType;
import nl.sbdeveloper.vehiclesplus.api.stands.ArmorStandName;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.DrivableVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.PersistentVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.SpawnedVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.StorageVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.EquipablePart;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.Part;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.Wheel;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.seat.Seat;
import nl.sbdeveloper.vehiclesplus.api.vehicles.statics.VehicleStatics;
import nl.sbdeveloper.vehiclesplus.inventories.vehicles.DrivableVehicleGUI;
import nl.sbdeveloper.vehiclesplus.inventories.vehicles.PersistentVehicleGUI;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBT;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.iface.ReadWriteItemNBT;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.utils.ColorUtil;
import nl.sbdeveloper.vehiclesplus.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;

public class EntityInteractListener
implements Listener {
    @EventHandler
    public void onClick(PlayerInteractAtEntityEvent playerInteractAtEntityEvent) {
        if (!(playerInteractAtEntityEvent.getRightClicked() instanceof ArmorStand)) {
            return;
        }
        ArmorStand armorStand = (ArmorStand)playerInteractAtEntityEvent.getRightClicked();
        if (armorStand.getCustomName() == null || !armorStand.getCustomName().startsWith("VP_")) {
            return;
        }
        playerInteractAtEntityEvent.setCancelled(true);
        if (armorStand.getCustomName().equalsIgnoreCase(ArmorStandName.VP_PART.name())) {
            this.handlePartInteraction(playerInteractAtEntityEvent, armorStand);
        } else if (armorStand.getCustomName().equalsIgnoreCase(ArmorStandName.VP_HOLDER.name())) {
            this.handleHolderInteraction(playerInteractAtEntityEvent, armorStand);
        }
    }

    private void handlePartInteraction(PlayerInteractAtEntityEvent playerInteractAtEntityEvent, ArmorStand armorStand) {
        boolean bl;
        if (!armorStand.hasMetadata(NBTDataType.V_PART_DATA.name())) {
            return;
        }
        Part part = (Part)((MetadataValue)armorStand.getMetadata(NBTDataType.V_PART_DATA.name()).get(0)).value();
        if (part == null) {
            return;
        }
        Optional<SpawnedVehicle> optional = part.getOwningVehicle();
        if (optional.isEmpty()) {
            return;
        }
        SpawnedVehicle spawnedVehicle = optional.get();
        if (spawnedVehicle instanceof DrivableVehicle && this.fireVehicleClickEvent((DrivableVehicle)spawnedVehicle, playerInteractAtEntityEvent)) {
            return;
        }
        ItemStack itemStack = playerInteractAtEntityEvent.getPlayer().getInventory().getItemInMainHand();
        if (!itemStack.getType().name().contains("AIR") && (bl = NBT.modify(itemStack, readWriteItemNBT -> {
            if (readWriteItemNBT.hasTag(NBTDataType.FUEL_TYPE.name())) {
                this.refuelVehicle(playerInteractAtEntityEvent, spawnedVehicle, itemStack, (ReadWriteItemNBT)readWriteItemNBT);
                return true;
            }
            if (readWriteItemNBT.hasTag(NBTDataType.ADDON_PAINT_COLOR.name())) {
                this.applyColorToPart(playerInteractAtEntityEvent, part, itemStack, (ReadWriteItemNBT)readWriteItemNBT);
                return true;
            }
            if (readWriteItemNBT.hasTag(NBTDataType.ADDON_WHEEL_PART.name())) {
                this.replaceWheel(playerInteractAtEntityEvent, part, itemStack, (ReadWriteItemNBT)readWriteItemNBT);
                return true;
            }
            return false;
        }).booleanValue())) {
            return;
        }
        if (part instanceof Seat) {
            this.handleSeatInteraction(playerInteractAtEntityEvent, (Seat)part);
        }
    }

    private void refuelVehicle(PlayerInteractAtEntityEvent playerInteractAtEntityEvent, SpawnedVehicle spawnedVehicle, ItemStack itemStack, ReadWriteItemNBT readWriteItemNBT) {
        double d;
        String string = readWriteItemNBT.getString(NBTDataType.FUEL_TYPE.name());
        double d2 = readWriteItemNBT.getDouble("liters");
        VehicleRefuelEvent vehicleRefuelEvent = new VehicleRefuelEvent(spawnedVehicle, string, d2);
        Bukkit.getPluginManager().callEvent((Event)vehicleRefuelEvent);
        if (vehicleRefuelEvent.isCancelled()) {
            return;
        }
        if (!spawnedVehicle.getVehicleModel().getFuel().getTypeId().equals(string)) {
            playerInteractAtEntityEvent.getPlayer().sendMessage(String.valueOf(ChatColor.RED) + "This vehicle cannot be refueled with " + string + "!");
            return;
        }
        VehicleStatics vehicleStatics = spawnedVehicle.getStorageVehicle().getStatics();
        double d3 = vehicleStatics.getCurrentFuel();
        double d4 = Math.min(d3 + d2, d = spawnedVehicle.getVehicleModel().getFuelTank().getMax().doubleValue());
        if (d4 == d3) {
            playerInteractAtEntityEvent.getPlayer().sendMessage(String.valueOf(ChatColor.RED) + "The fuel tank is already full!");
            return;
        }
        playerInteractAtEntityEvent.getPlayer().getInventory().removeItem(new ItemStack[]{new ItemBuilder(itemStack).amount(1).getItemStack()});
        vehicleStatics.setCurrentFuel(d4);
        playerInteractAtEntityEvent.getPlayer().sendMessage(String.valueOf(ChatColor.GREEN) + "Refueled " + d2 + " liters of " + string + "!");
    }

    private void handleHolderInteraction(PlayerInteractAtEntityEvent playerInteractAtEntityEvent, ArmorStand armorStand) {
        Optional<SpawnedVehicle> optional = VehiclesPlusAPI.getVehicleFromHolder(armorStand);
        if (optional.isEmpty()) {
            return;
        }
        DrivableVehicle drivableVehicle = optional.get().getAsDrivableVehicle();
        if (drivableVehicle == null || this.fireVehicleClickEvent(drivableVehicle, playerInteractAtEntityEvent)) {
            return;
        }
        if (playerInteractAtEntityEvent.getPlayer().isSneaking() && drivableVehicle.getStatics().getCurrentSpeed() == 0.0f) {
            this.openVehicleGUI(playerInteractAtEntityEvent, drivableVehicle);
        }
    }

    private boolean fireVehicleClickEvent(DrivableVehicle drivableVehicle, PlayerInteractAtEntityEvent playerInteractAtEntityEvent) {
        VehicleClickEvent vehicleClickEvent = new VehicleClickEvent(drivableVehicle, playerInteractAtEntityEvent.getPlayer());
        Bukkit.getPluginManager().callEvent((Event)vehicleClickEvent);
        return vehicleClickEvent.isCancelled();
    }

    private void handleSeatInteraction(PlayerInteractAtEntityEvent playerInteractAtEntityEvent, Seat seat) {
        Optional<SpawnedVehicle> optional = seat.getOwningVehicle();
        if (optional.isEmpty()) {
            return;
        }
        SpawnedVehicle spawnedVehicle = optional.get();
        if (spawnedVehicle.getStatics().getCurrentSpeed() != 0.0f) {
            return;
        }
        if (playerInteractAtEntityEvent.getPlayer().isSneaking()) {
            this.openVehicleGUI(playerInteractAtEntityEvent, spawnedVehicle);
        } else if (this.canEnterSeat(playerInteractAtEntityEvent, seat)) {
            seat.enter(playerInteractAtEntityEvent.getPlayer());
        }
    }

    private boolean canEnterSeat(PlayerInteractAtEntityEvent playerInteractAtEntityEvent, Seat seat) {
        Optional<SpawnedVehicle> optional = seat.getOwningVehicle();
        if (optional.isEmpty()) {
            return false;
        }
        SpawnedVehicle spawnedVehicle = optional.get();
        boolean bl = spawnedVehicle.getVehicleModel().isAllowedToRide(playerInteractAtEntityEvent.getPlayer());
        boolean bl2 = spawnedVehicle.getVehicleModel().isAllowedToDrive(playerInteractAtEntityEvent.getPlayer());
        if (!seat.isSteer() && !bl || seat.isSteer() && !bl2) {
            playerInteractAtEntityEvent.getPlayer().sendMessage(Locale.getMessage(MessageKeys.PERMISSION_DENIED));
            return false;
        }
        return true;
    }

    private void applyColorToPart(PlayerInteractAtEntityEvent playerInteractAtEntityEvent, Part part, ItemStack itemStack, ReadWriteItemNBT readWriteItemNBT) {
        if (!(part instanceof EquipablePart) || !((EquipablePart)part).isColorable()) {
            return;
        }
        Optional<SpawnedVehicle> optional = part.getOwningVehicle();
        if (optional.isEmpty()) {
            return;
        }
        SpawnedVehicle spawnedVehicle = optional.get();
        ((EquipablePart)part).setColor(NBTColorAdapter.INSTANCE.deserialize(readWriteItemNBT.getString(NBTDataType.ADDON_PAINT_COLOR.name())), true);
        spawnedVehicle.getStorageVehicle().save();
        playerInteractAtEntityEvent.getPlayer().getInventory().removeItem(new ItemStack[]{new ItemBuilder(itemStack).amount(1).getItemStack()});
        playerInteractAtEntityEvent.getPlayer().sendMessage(String.valueOf(ChatColor.GREEN) + "Color applied to " + part.getClass().getSimpleName() + "!");
    }

    private void replaceWheel(PlayerInteractAtEntityEvent playerInteractAtEntityEvent, Part part, ItemStack itemStack, ReadWriteItemNBT readWriteItemNBT) {
        if (!(part instanceof Wheel)) {
            return;
        }
        Optional<SpawnedVehicle> optional = part.getOwningVehicle();
        if (optional.isEmpty()) {
            return;
        }
        StorageVehicle storageVehicle = optional.get().getStorageVehicle();
        VehiclesPlusAPI.getRimDesign(((Wheel)part).getRimDesignId()).ifPresent(rimDesign -> {
            ((Wheel)part).setRimDesignId(readWriteItemNBT.getString(NBTDataType.ADDON_WHEEL_PART.name()));
            storageVehicle.save();
            playerInteractAtEntityEvent.getPlayer().getInventory().removeItem(new ItemStack[]{new ItemBuilder(itemStack).amount(1).getItemStack()});
            ItemStack itemStack2 = new ItemBuilder(rimDesign.getSkin()).displayname(ColorUtil.__("&fWheel")).lore(ColorUtil.__("&cType: &f" + rimDesign.getName())).applyNBT(readWriteItemNBT -> readWriteItemNBT.setString(NBTDataType.ADDON_WHEEL_PART.name(), rimDesign.getName())).getItemStack();
            playerInteractAtEntityEvent.getPlayer().getInventory().addItem(new ItemStack[]{itemStack2});
            playerInteractAtEntityEvent.getPlayer().sendMessage(String.valueOf(ChatColor.GREEN) + "Wheel changed! The old wheel has been added to your inventory.");
        });
    }

    private void openVehicleGUI(PlayerInteractAtEntityEvent playerInteractAtEntityEvent, SpawnedVehicle spawnedVehicle) {
        if (spawnedVehicle instanceof PersistentVehicle && playerInteractAtEntityEvent.getPlayer().hasPermission("vp.persistent")) {
            new PersistentVehicleGUI(playerInteractAtEntityEvent.getPlayer(), (PersistentVehicle)spawnedVehicle);
        } else if (spawnedVehicle instanceof DrivableVehicle) {
            Optional<Garage> optional = VehiclesPlusAPI.getGarage(spawnedVehicle);
            if (optional.isEmpty()) {
                return;
            }
            Garage garage = optional.get();
            GaragePermissions garagePermissions = garage.getRole(playerInteractAtEntityEvent.getPlayer()).getPermissions();
            new DrivableVehicleGUI(playerInteractAtEntityEvent.getPlayer(), (DrivableVehicle)spawnedVehicle, garagePermissions);
        }
    }
}

