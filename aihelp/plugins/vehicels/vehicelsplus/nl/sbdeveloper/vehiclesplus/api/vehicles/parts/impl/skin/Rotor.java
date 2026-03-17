/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Color
 *  org.bukkit.Location
 *  org.bukkit.inventory.ItemStack
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.skin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.api.vehicles.HolderItemPosition;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.EquipablePart;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.PartTypeName;
import nl.sbdeveloper.vehiclesplus.libs.xseries.XMaterial;
import nl.sbdeveloper.vehiclesplus.utils.ColorUtil;
import nl.sbdeveloper.vehiclesplus.utils.ItemBuilder;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

@PartTypeName(value="rotor")
public class Rotor
extends EquipablePart {
    @JsonIgnore
    private double spin = 0.0;

    public Rotor() {
        super(-0.3, 0.95, 0.0, new ItemBuilder(XMaterial.LEATHER_CHESTPLATE.parseItem()).customModelData(2, itemBuilder -> itemBuilder.durability(2).unbreakable()).armorColor(Color.GRAY).getItemStack(), HolderItemPosition.HEAD);
    }

    public Rotor(double d, double d2, double d3, ItemStack itemStack, HolderItemPosition holderItemPosition) {
        super(d, d2, d3, itemStack, holderItemPosition);
    }

    @Override
    public Location applyExtraOffset(Location location) {
        location.setYaw(this.holder.getLocation().getYaw());
        return super.applyExtraOffset(location);
    }

    @Override
    public ItemStack getPartGUIItem() {
        return new ItemBuilder(XMaterial.LEATHER_BOOTS).displayname(ColorUtil.__("&6Rotor")).lore(ColorUtil.__("&7This is the rotor of the helicopter."), ColorUtil.__("&7It will spin when the helicopter is moving.")).unbreakable().durability(7).hideAllFlags().getItemStack();
    }

    public void setSpin(double d) {
        double d2 = Math.PI * 2;
        if ((d %= d2) < 0.0) {
            d += d2;
        }
        this.spin = d;
    }

    public void addSpin(double d) {
        this.setSpin(this.spin + d);
    }

    @Override
    public String asString() {
        return "";
    }

    @Generated
    public double getSpin() {
        return this.spin;
    }
}

