/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.util.EulerAngle
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.skin;

import lombok.Generated;
import net.md_5.bungee.api.ChatColor;
import nl.sbdeveloper.vehiclesplus.api.vehicles.HolderItemPosition;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.PartTypeName;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.skin.Skin;
import nl.sbdeveloper.vehiclesplus.libs.xseries.XMaterial;
import nl.sbdeveloper.vehiclesplus.utils.ItemBuilder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

@PartTypeName(value="bikeskin")
public class BikeSkin
extends Skin {
    private transient int wheelieOffset;

    public BikeSkin() {
    }

    public BikeSkin(double d, double d2, double d3, ItemStack itemStack, HolderItemPosition holderItemPosition) {
        super(d, d2, d3, itemStack, holderItemPosition);
    }

    public void applyWheelieOffset() {
        EulerAngle eulerAngle = this.holder.getHeadPose();
        this.holder.setHeadPose(new EulerAngle(eulerAngle.getX() - Math.toRadians(this.wheelieOffset), eulerAngle.getY(), eulerAngle.getZ()));
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public ItemStack getPartGUIItem() {
        return new ItemBuilder(XMaterial.LEATHER_BOOTS).unbreakable().durability(2).displayname(String.valueOf(ChatColor.GOLD) + "Bike Skin").lore(String.valueOf(ChatColor.GRAY) + "The main body of a bike.", String.valueOf(ChatColor.GRAY) + "This part supports a wheelie.").hideAllFlags().getItemStack();
    }

    @Generated
    public int getWheelieOffset() {
        return this.wheelieOffset;
    }

    @Generated
    public void setWheelieOffset(int n) {
        this.wheelieOffset = n;
    }
}

