/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.inventory.ItemStack
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.parts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.Optional;
import lombok.Generated;
import net.md_5.bungee.api.ChatColor;
import nl.sbdeveloper.vehiclesplus.VehiclesPlusPluginManager;
import nl.sbdeveloper.vehiclesplus.api.VehiclesPlusAPI;
import nl.sbdeveloper.vehiclesplus.api.nbt.NBTDataType;
import nl.sbdeveloper.vehiclesplus.api.stands.ArmorStandBuilder;
import nl.sbdeveloper.vehiclesplus.api.stands.ArmorStandName;
import nl.sbdeveloper.vehiclesplus.api.vehicles.Vehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.SpawnedVehicle;
import nl.sbdeveloper.vehiclesplus.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, property="type")
public abstract class Part
implements Cloneable {
    @JsonProperty(index=1)
    protected double xOffset;
    @JsonProperty(index=2)
    protected double yOffset;
    @JsonProperty(index=3)
    protected double zOffset;
    @JsonProperty(index=4)
    protected int rotationOffset = 0;
    @JsonIgnore
    protected ArmorStand holder = null;
    @JsonIgnore
    private boolean isAddon = false;

    public Part(double d, double d2, double d3) {
        this(d, d2, d3, 0);
    }

    public Part(double d, double d2, double d3, int n) {
        this.xOffset = d;
        this.yOffset = d2;
        this.zOffset = d3;
        this.rotationOffset = n;
    }

    public void spawnStand(Location location, Vehicle vehicle, boolean bl) {
        ArmorStandBuilder armorStandBuilder = new ArmorStandBuilder(location, this.xOffset, this.yOffset, this.zOffset, this.rotationOffset).setGravity(false).setVisible(false).setInvulnerable(true).setCustomName(ArmorStandName.VP_PART).applyMetaData(NBTDataType.V_UUID, vehicle.getStorageVehicle().getUuid()).applyMetaData(NBTDataType.V_PART_DATA, this);
        if (bl) {
            armorStandBuilder = armorStandBuilder.setPersistent(true).setRemoveWhenFarAway(false);
        }
        this.holder = armorStandBuilder.getArmorStand();
    }

    public void despawnStand() {
        if (this.holder == null) {
            return;
        }
        this.holder.remove();
        this.holder = null;
    }

    @JsonIgnore
    public Optional<SpawnedVehicle> getOwningVehicle() {
        Optional<SpawnedVehicle> optional = VehiclesPlusAPI.getVehicleFromPart(this.holder);
        if (optional.isEmpty()) {
            VehiclesPlusPluginManager.getVehiclesPlusPlugin().getLogger().warning("Detected a VehiclesPlus part not connected to a valid vehicle, despawning it.");
            this.despawnStand();
        }
        return optional;
    }

    @JsonIgnore
    public boolean isSpawned() {
        return this.holder != null;
    }

    public Location applyExtraOffset(Location location) {
        location.setYaw(location.getYaw() + (float)this.rotationOffset);
        return location;
    }

    public void refresh(Location location) {
        if (this.holder == null) {
            return;
        }
        Location location2 = LocationUtil.calculateOffset(location, this.xOffset, this.yOffset, this.zOffset);
        this.holder.teleport(new Location(location.getWorld(), location2.getX(), location2.getY(), location2.getZ(), location2.getYaw() + (float)this.rotationOffset, 0.0f));
    }

    @JsonIgnore
    public abstract ItemStack getPartGUIItem();

    public abstract String asString();

    public String toString() {
        return String.valueOf(ChatColor.GOLD) + "X offset: " + String.valueOf(ChatColor.WHITE) + this.xOffset + "\n" + String.valueOf(ChatColor.GOLD) + "Y offset: " + String.valueOf(ChatColor.WHITE) + this.yOffset + "\n" + String.valueOf(ChatColor.GOLD) + "Z offset: " + String.valueOf(ChatColor.WHITE) + this.zOffset + "\n" + String.valueOf(ChatColor.GOLD) + "Rotation offset: " + String.valueOf(ChatColor.WHITE) + this.rotationOffset + "\n" + String.valueOf(ChatColor.GOLD) + "Is addon: " + String.valueOf(ChatColor.WHITE) + this.isAddon + "\n" + this.asString();
    }

    public Part clone() {
        try {
            Part part = (Part)super.clone();
            part.holder = null;
            return part;
        } catch (CloneNotSupportedException cloneNotSupportedException) {
            throw new AssertionError();
        }
    }

    @Generated
    public Part() {
    }

    @Generated
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof Part)) {
            return false;
        }
        Part part = (Part)object;
        if (!part.canEqual(this)) {
            return false;
        }
        if (Double.compare(this.getXOffset(), part.getXOffset()) != 0) {
            return false;
        }
        if (Double.compare(this.getYOffset(), part.getYOffset()) != 0) {
            return false;
        }
        if (Double.compare(this.getZOffset(), part.getZOffset()) != 0) {
            return false;
        }
        if (this.getRotationOffset() != part.getRotationOffset()) {
            return false;
        }
        if (this.isAddon() != part.isAddon()) {
            return false;
        }
        ArmorStand armorStand = this.getHolder();
        ArmorStand armorStand2 = part.getHolder();
        return !(armorStand == null ? armorStand2 != null : !armorStand.equals(armorStand2));
    }

    @Generated
    protected boolean canEqual(Object object) {
        return object instanceof Part;
    }

    @Generated
    public int hashCode() {
        int n = 59;
        int n2 = 1;
        long l = Double.doubleToLongBits(this.getXOffset());
        n2 = n2 * 59 + (int)(l >>> 32 ^ l);
        long l2 = Double.doubleToLongBits(this.getYOffset());
        n2 = n2 * 59 + (int)(l2 >>> 32 ^ l2);
        long l3 = Double.doubleToLongBits(this.getZOffset());
        n2 = n2 * 59 + (int)(l3 >>> 32 ^ l3);
        n2 = n2 * 59 + this.getRotationOffset();
        n2 = n2 * 59 + (this.isAddon() ? 79 : 97);
        ArmorStand armorStand = this.getHolder();
        n2 = n2 * 59 + (armorStand == null ? 43 : armorStand.hashCode());
        return n2;
    }

    @Generated
    public double getXOffset() {
        return this.xOffset;
    }

    @JsonProperty(index=1)
    @Generated
    public void setXOffset(double d) {
        this.xOffset = d;
    }

    @Generated
    public double getYOffset() {
        return this.yOffset;
    }

    @JsonProperty(index=2)
    @Generated
    public void setYOffset(double d) {
        this.yOffset = d;
    }

    @Generated
    public double getZOffset() {
        return this.zOffset;
    }

    @JsonProperty(index=3)
    @Generated
    public void setZOffset(double d) {
        this.zOffset = d;
    }

    @Generated
    public int getRotationOffset() {
        return this.rotationOffset;
    }

    @JsonProperty(index=4)
    @Generated
    public void setRotationOffset(int n) {
        this.rotationOffset = n;
    }

    @Generated
    public ArmorStand getHolder() {
        return this.holder;
    }

    @Generated
    public boolean isAddon() {
        return this.isAddon;
    }

    @JsonIgnore
    @Generated
    public void setAddon(boolean bl) {
        this.isAddon = bl;
    }
}

