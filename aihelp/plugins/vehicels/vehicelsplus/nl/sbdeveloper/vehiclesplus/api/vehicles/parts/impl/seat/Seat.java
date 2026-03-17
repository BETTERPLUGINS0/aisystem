/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.inventory.ItemStack
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.seat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Optional;
import lombok.Generated;
import net.md_5.bungee.api.ChatColor;
import nl.sbdeveloper.vehiclesplus.api.events.impl.VehicleEnterEvent;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.DrivableVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.SpawnedVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.Part;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.PartTypeName;
import nl.sbdeveloper.vehiclesplus.libs.xseries.XMaterial;
import nl.sbdeveloper.vehiclesplus.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@PartTypeName(value="seat")
public class Seat
extends Part {
    private static final ItemStack defaultGUIItem = new ItemBuilder(XMaterial.DIAMOND_HOE).unbreakable().durability(1).hideAllFlags().getItemStack();
    private boolean steer;
    private ItemStack GUIItem;
    @JsonIgnore
    private boolean occupied;

    public Seat() {
        this.occupied = false;
        this.GUIItem = defaultGUIItem;
    }

    public Seat(double d, double d2, double d3, boolean bl) {
        this(d, d2, d3, 0, bl, defaultGUIItem);
    }

    public Seat(double d, double d2, double d3, boolean bl, @NotNull ItemStack itemStack) {
        this(d, d2, d3, 0, bl, itemStack);
    }

    public Seat(double d, double d2, double d3, int n, boolean bl) {
        this(d, d2, d3, n, bl, defaultGUIItem);
    }

    public Seat(double d, double d2, double d3, int n, boolean bl, @NotNull ItemStack itemStack) {
        super(d, d2, d3, n);
        this.steer = bl;
        this.GUIItem = itemStack;
    }

    public void enter(Player player) {
        Optional<SpawnedVehicle> optional = this.getOwningVehicle();
        if (optional.isEmpty()) {
            return;
        }
        DrivableVehicle drivableVehicle = optional.get().getAsDrivableVehicle();
        if (drivableVehicle == null) {
            return;
        }
        VehicleEnterEvent vehicleEnterEvent = new VehicleEnterEvent(drivableVehicle, player, this);
        Bukkit.getPluginManager().callEvent((Event)vehicleEnterEvent);
        if (vehicleEnterEvent.isCancelled()) {
            return;
        }
        if (drivableVehicle.getStatics().getCurrentSpeed() != 0.0f) {
            return;
        }
        if (drivableVehicle.isLocked()) {
            player.sendMessage(String.valueOf(ChatColor.RED) + "This vehicle is locked.");
            return;
        }
        if (this.occupied) {
            player.sendMessage(String.valueOf(ChatColor.RED) + "This seat is occupied.");
            return;
        }
        if (this.steer) {
            try {
                String string = drivableVehicle.getVehicleModel().getSounds().getStart().getSound();
                player.playSound(this.holder.getLocation(), string, 1.0f, 1.0f);
            } catch (IllegalArgumentException illegalArgumentException) {
                // empty catch block
            }
        }
        this.holder.addPassenger((Entity)player);
        this.occupied = true;
    }

    @JsonIgnore
    public Optional<Player> getPassenger() {
        return this.holder.getPassengers().isEmpty() ? Optional.empty() : this.holder.getPassengers().stream().filter(Player.class::isInstance).map(Player.class::cast).findFirst();
    }

    @Override
    public ItemStack getPartGUIItem() {
        return new ItemBuilder(XMaterial.DIAMOND_HOE).displayname(String.valueOf(ChatColor.GOLD) + "Seat").lore(String.valueOf(ChatColor.GRAY) + "For players to sit on.").unbreakable().durability(1).hideAllFlags().getItemStack();
    }

    @Override
    public String asString() {
        return String.valueOf(ChatColor.GOLD) + "Driver: " + String.valueOf(ChatColor.WHITE) + this.steer + "\n" + String.valueOf(ChatColor.GOLD) + "GUI Item: " + String.valueOf(ChatColor.WHITE) + this.GUIItem.getType().name() + "\n";
    }

    @Override
    public void despawnStand() {
        if (this.occupied) {
            this.getPassenger().ifPresent(player -> this.holder.removePassenger((Entity)player));
        }
        super.despawnStand();
        this.occupied = false;
    }

    @Generated
    public boolean isSteer() {
        return this.steer;
    }

    @Generated
    public ItemStack getGUIItem() {
        return this.GUIItem;
    }

    @Generated
    public boolean isOccupied() {
        return this.occupied;
    }

    @Generated
    public void setSteer(boolean bl) {
        this.steer = bl;
    }

    @Generated
    public void setGUIItem(ItemStack itemStack) {
        this.GUIItem = itemStack;
    }

    @JsonIgnore
    @Generated
    public void setOccupied(boolean bl) {
        this.occupied = bl;
    }
}

