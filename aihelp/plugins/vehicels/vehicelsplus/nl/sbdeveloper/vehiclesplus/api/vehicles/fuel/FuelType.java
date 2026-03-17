/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.inventory.ItemStack
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.fuel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Generated;
import net.md_5.bungee.api.ChatColor;
import nl.sbdeveloper.vehiclesplus.api.nbt.NBTDataType;
import nl.sbdeveloper.vehiclesplus.handlers.StorageHandler;
import nl.sbdeveloper.vehiclesplus.storage.db.Savable;
import nl.sbdeveloper.vehiclesplus.utils.ColorUtil;
import nl.sbdeveloper.vehiclesplus.utils.ItemBuilder;
import org.bukkit.inventory.ItemStack;

public class FuelType
implements Savable {
    private String name;
    private ItemStack item;
    private double pricePerLiter;

    public String toString() {
        return String.valueOf(ChatColor.GOLD) + "Name: " + String.valueOf(ChatColor.WHITE) + this.name + "\n" + String.valueOf(ChatColor.GOLD) + "Price per liter: " + String.valueOf(ChatColor.WHITE) + this.pricePerLiter + "\n" + String.valueOf(ChatColor.GOLD) + "Item: " + String.valueOf(ChatColor.WHITE) + this.item.getType().name() + "\n";
    }

    @JsonIgnore
    public ItemStack getFuel() {
        return this.getFuel(1.0);
    }

    @JsonIgnore
    public ItemStack getFuel(double d) {
        return new ItemBuilder(this.item).displayname(ColorUtil.__("&c" + this.name + " &aFuel")).lore(ColorUtil.__("&cType: &a" + this.name), ColorUtil.__("&cAmount: &a" + d + "L")).hideAllFlags().applyNBT(readWriteItemNBT -> {
            readWriteItemNBT.setString(NBTDataType.FUEL_TYPE.name(), this.name);
            readWriteItemNBT.setDouble("liters", d);
        }).getItemStack();
    }

    @Override
    public void save() {
        StorageHandler.save(this, "fuels", this.name);
    }

    @Generated
    public FuelType(String string, ItemStack itemStack, double d) {
        this.name = string;
        this.item = itemStack;
        this.pricePerLiter = d;
    }

    @Generated
    protected FuelType() {
    }

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public ItemStack getItem() {
        return this.item;
    }

    @Generated
    public double getPricePerLiter() {
        return this.pricePerLiter;
    }

    @Generated
    public void setName(String string) {
        this.name = string;
    }

    @Generated
    public void setItem(ItemStack itemStack) {
        this.item = itemStack;
    }

    @Generated
    public void setPricePerLiter(double d) {
        this.pricePerLiter = d;
    }
}

