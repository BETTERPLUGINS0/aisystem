/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.ChatColor
 *  org.bukkit.Color
 *  org.bukkit.Location
 *  org.bukkit.inventory.ItemStack
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.VehiclesPlusPluginManager;
import nl.sbdeveloper.vehiclesplus.api.VehiclesPlusAPI;
import nl.sbdeveloper.vehiclesplus.api.vehicles.HolderItemPosition;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.EquipablePart;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.PartTypeName;
import nl.sbdeveloper.vehiclesplus.api.vehicles.rims.RimDesign;
import nl.sbdeveloper.vehiclesplus.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@PartTypeName(value="wheel")
public class Wheel
extends EquipablePart {
    private String rimDesignId;
    private boolean steering;
    @JsonIgnore
    private float steeringOffset;

    public Wheel() {
        this(0.0, 0.0, 0.0, VehiclesPlusAPI.getRimDesign(VehiclesPlusPluginManager.getConfig().getDefaultRimDesignId()).orElseThrow(() -> new IllegalArgumentException("Default rimdesign not found!")), Color.GRAY);
    }

    public Wheel(double d, double d2, double d3, @NotNull RimDesign rimDesign, @NotNull Color color) {
        this(d, d2, d3, rimDesign, color, false, 0);
    }

    public Wheel(double d, double d2, double d3, @NotNull RimDesign rimDesign, @NotNull Color color, boolean bl, int n) {
        super(d, d2, d3, n, rimDesign.getSkin(), rimDesign.getPosition());
        this.setColor(color, false);
        this.rimDesignId = rimDesign.getName();
        this.steering = bl;
    }

    @Override
    public String asString() {
        return String.valueOf(ChatColor.GOLD) + "Rimdesign: " + String.valueOf(ChatColor.WHITE) + this.rimDesignId + "\n" + String.valueOf(ChatColor.GOLD) + "Steering: " + String.valueOf(ChatColor.WHITE) + this.steering + "\n";
    }

    @Override
    public Location applyExtraOffset(Location location) {
        location.setYaw(location.getYaw() + (float)this.rotationOffset + this.steeringOffset);
        return location;
    }

    public void setRimDesignId(String string) {
        this.rimDesignId = string;
        this.setItem(VehiclesPlusAPI.getRimDesign(string).orElseThrow(() -> new IllegalArgumentException("Rimdesign '" + string + "' not found!")).getSkin());
        this.updateHolder();
    }

    @Override
    @JsonIgnore
    public ItemStack getItem() {
        return super.getItem();
    }

    @Override
    @JsonIgnore
    public HolderItemPosition getPosition() {
        return super.getPosition();
    }

    @Override
    public ItemStack getPartGUIItem() {
        return new ItemBuilder(VehiclesPlusAPI.getRimDesign(this.rimDesignId).orElseThrow(() -> new IllegalArgumentException("Rimdesign '" + this.rimDesignId + "' not found!")).getSkin()).displayname(String.valueOf(ChatColor.GOLD) + this.getClass().getSimpleName()).lore(String.valueOf(net.md_5.bungee.api.ChatColor.GRAY) + "The wheel of a vehicle.").unbreakable().durability(1).hideAllFlags().getItemStack();
    }

    @Override
    public void despawnStand() {
        super.despawnStand();
        this.steeringOffset = 0.0f;
    }

    @Generated
    public String getRimDesignId() {
        return this.rimDesignId;
    }

    @Generated
    public boolean isSteering() {
        return this.steering;
    }

    @Generated
    public float getSteeringOffset() {
        return this.steeringOffset;
    }

    @Generated
    public void setSteering(boolean bl) {
        this.steering = bl;
    }

    @JsonIgnore
    @Generated
    public void setSteeringOffset(float f) {
        this.steeringOffset = f;
    }
}

