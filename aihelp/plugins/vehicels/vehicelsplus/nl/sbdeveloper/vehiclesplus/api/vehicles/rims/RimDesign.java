/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.inventory.ItemStack
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.rims;

import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.api.vehicles.HolderItemPosition;
import nl.sbdeveloper.vehiclesplus.handlers.StorageHandler;
import nl.sbdeveloper.vehiclesplus.storage.db.Savable;
import org.bukkit.inventory.ItemStack;

public class RimDesign
implements Savable {
    private String name;
    private ItemStack skin;
    private HolderItemPosition position;
    private float price;

    @Override
    public void save() {
        StorageHandler.save(this, "rims", this.name);
    }

    @Generated
    public RimDesign(String string, ItemStack itemStack, HolderItemPosition holderItemPosition, float f) {
        this.name = string;
        this.skin = itemStack;
        this.position = holderItemPosition;
        this.price = f;
    }

    @Generated
    protected RimDesign() {
    }

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public ItemStack getSkin() {
        return this.skin;
    }

    @Generated
    public HolderItemPosition getPosition() {
        return this.position;
    }

    @Generated
    public float getPrice() {
        return this.price;
    }

    @Generated
    public void setName(String string) {
        this.name = string;
    }

    @Generated
    public void setSkin(ItemStack itemStack) {
        this.skin = itemStack;
    }

    @Generated
    public void setPosition(HolderItemPosition holderItemPosition) {
        this.position = holderItemPosition;
    }

    @Generated
    public void setPrice(float f) {
        this.price = f;
    }
}

