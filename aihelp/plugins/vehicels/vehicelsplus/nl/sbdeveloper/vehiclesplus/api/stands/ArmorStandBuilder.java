/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Location
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package nl.sbdeveloper.vehiclesplus.api.stands;

import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.VehiclesPlusPluginManager;
import nl.sbdeveloper.vehiclesplus.api.nbt.NBTDataType;
import nl.sbdeveloper.vehiclesplus.api.stands.ArmorStandName;
import nl.sbdeveloper.vehiclesplus.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class ArmorStandBuilder {
    private final ArmorStand armorStand;

    public ArmorStandBuilder(Location location) {
        this(location, 0.0, 0.0, 0.0);
    }

    public ArmorStandBuilder(Location location, double d, double d2, double d3) {
        this(location, d, d2, d3, 0);
    }

    public ArmorStandBuilder(Location location, double d, double d2, double d3, int n) {
        Location location2 = LocationUtil.calculateOffset(location, d, d2, d3);
        this.armorStand = (ArmorStand)location.getWorld().spawn(new Location(location.getWorld(), location2.getX(), location2.getY(), location2.getZ(), location2.getYaw() + (float)n, 0.0f), ArmorStand.class);
    }

    public ArmorStandBuilder setPersistent(boolean bl) {
        if (this.armorStand == null) {
            return null;
        }
        this.armorStand.setPersistent(bl);
        return this;
    }

    public ArmorStandBuilder setRemoveWhenFarAway(boolean bl) {
        if (this.armorStand == null) {
            return null;
        }
        this.armorStand.setRemoveWhenFarAway(bl);
        return this;
    }

    public ArmorStandBuilder setGravity(boolean bl) {
        if (this.armorStand == null) {
            return null;
        }
        this.armorStand.setGravity(bl);
        return this;
    }

    public ArmorStandBuilder setVisible(boolean bl) {
        if (this.armorStand == null) {
            return null;
        }
        this.armorStand.setVisible(bl);
        return this;
    }

    public ArmorStandBuilder setInvulnerable(boolean bl) {
        if (this.armorStand == null) {
            return null;
        }
        this.armorStand.setInvulnerable(bl);
        return this;
    }

    public ArmorStandBuilder setCustomName(ArmorStandName armorStandName) {
        if (this.armorStand == null) {
            return null;
        }
        this.armorStand.setCustomName(armorStandName.name());
        return this;
    }

    public ArmorStandBuilder setHelmet(ItemStack itemStack) {
        if (this.armorStand == null || this.armorStand.getEquipment() == null) {
            return null;
        }
        this.armorStand.getEquipment().setHelmet(itemStack);
        return this;
    }

    public ArmorStandBuilder applyMetaData(NBTDataType nBTDataType, Object object) {
        if (this.armorStand == null) {
            return null;
        }
        this.armorStand.setMetadata(nBTDataType.name(), (MetadataValue)new FixedMetadataValue((Plugin)VehiclesPlusPluginManager.getVehiclesPlusPlugin(), object));
        return this;
    }

    @Generated
    public ArmorStand getArmorStand() {
        return this.armorStand;
    }
}

