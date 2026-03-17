/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.ChatColor
 *  org.bukkit.Color
 *  org.bukkit.Location
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.LeatherArmorMeta
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.parts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.api.vehicles.HolderItemPosition;
import nl.sbdeveloper.vehiclesplus.api.vehicles.Vehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.Part;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.jetbrains.annotations.Nullable;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.EXTERNAL_PROPERTY, property="type")
public abstract class EquipablePart
extends Part {
    private ItemStack item;
    private HolderItemPosition position;

    public EquipablePart(double d, double d2, double d3, ItemStack itemStack, HolderItemPosition holderItemPosition) {
        this(d, d2, d3, 0, itemStack, holderItemPosition);
    }

    public EquipablePart(double d, double d2, double d3, int n, ItemStack itemStack, HolderItemPosition holderItemPosition) {
        super(d, d2, d3, n);
        this.item = itemStack;
        this.position = holderItemPosition;
    }

    @JsonIgnore
    public boolean isColorable() {
        return this.item.hasItemMeta() && this.item.getItemMeta() instanceof LeatherArmorMeta;
    }

    @JsonIgnore
    @Nullable
    public Color getColor() {
        if (!this.item.hasItemMeta() || !(this.item.getItemMeta() instanceof LeatherArmorMeta)) {
            return null;
        }
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta)this.item.getItemMeta();
        if (leatherArmorMeta == null) {
            return null;
        }
        return leatherArmorMeta.getColor();
    }

    public void setColor(Color color, boolean bl) {
        if (!this.item.hasItemMeta() || !(this.item.getItemMeta() instanceof LeatherArmorMeta)) {
            throw new IllegalStateException("Item is not colorable");
        }
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta)this.item.getItemMeta();
        if (leatherArmorMeta == null) {
            return;
        }
        leatherArmorMeta.setColor(color);
        this.item.setItemMeta((ItemMeta)leatherArmorMeta);
        if (bl) {
            this.updateHolder();
        }
    }

    public void updateHolder() {
        if (this.holder == null) {
            return;
        }
        this.position.setItem(this.holder, this.item);
    }

    @Override
    public void spawnStand(Location location, Vehicle vehicle, boolean bl) {
        super.spawnStand(location, vehicle, bl);
        this.updateHolder();
    }

    @Override
    public String toString() {
        Color color = this.getColor();
        return super.toString() + String.valueOf(ChatColor.GOLD) + "Item: " + String.valueOf(ChatColor.WHITE) + this.item.getType().name() + "\n" + (String)(color != null ? String.valueOf(ChatColor.GOLD) + "Color: " + String.valueOf(ChatColor.WHITE) + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + "\n" : "");
    }

    @Override
    public EquipablePart clone() {
        EquipablePart equipablePart = (EquipablePart)super.clone();
        equipablePart.item = this.item.clone();
        return equipablePart;
    }

    @Generated
    public EquipablePart() {
    }

    @Generated
    public ItemStack getItem() {
        return this.item;
    }

    @Generated
    public HolderItemPosition getPosition() {
        return this.position;
    }

    @Override
    @Generated
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof EquipablePart)) {
            return false;
        }
        EquipablePart equipablePart = (EquipablePart)object;
        if (!equipablePart.canEqual(this)) {
            return false;
        }
        if (!super.equals(object)) {
            return false;
        }
        ItemStack itemStack = this.getItem();
        ItemStack itemStack2 = equipablePart.getItem();
        if (itemStack == null ? itemStack2 != null : !itemStack.equals(itemStack2)) {
            return false;
        }
        HolderItemPosition holderItemPosition = this.getPosition();
        HolderItemPosition holderItemPosition2 = equipablePart.getPosition();
        return !(holderItemPosition == null ? holderItemPosition2 != null : !((Object)((Object)holderItemPosition)).equals((Object)holderItemPosition2));
    }

    @Override
    @Generated
    protected boolean canEqual(Object object) {
        return object instanceof EquipablePart;
    }

    @Override
    @Generated
    public int hashCode() {
        int n = 59;
        int n2 = super.hashCode();
        ItemStack itemStack = this.getItem();
        n2 = n2 * 59 + (itemStack == null ? 43 : itemStack.hashCode());
        HolderItemPosition holderItemPosition = this.getPosition();
        n2 = n2 * 59 + (holderItemPosition == null ? 43 : ((Object)((Object)holderItemPosition)).hashCode());
        return n2;
    }

    @Generated
    protected void setItem(ItemStack itemStack) {
        this.item = itemStack;
    }
}

