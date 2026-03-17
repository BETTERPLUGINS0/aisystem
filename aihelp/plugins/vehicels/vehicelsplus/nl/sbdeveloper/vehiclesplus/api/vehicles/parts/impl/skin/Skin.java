/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.inventory.ItemStack
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.skin;

import net.md_5.bungee.api.ChatColor;
import nl.sbdeveloper.vehiclesplus.api.vehicles.HolderItemPosition;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.EquipablePart;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.PartTypeName;
import nl.sbdeveloper.vehiclesplus.libs.xseries.XMaterial;
import nl.sbdeveloper.vehiclesplus.utils.ItemBuilder;
import org.bukkit.inventory.ItemStack;

@PartTypeName(value="skin")
public class Skin
extends EquipablePart {
    public Skin() {
    }

    public Skin(double d, double d2, double d3, ItemStack itemStack, HolderItemPosition holderItemPosition) {
        super(d, d2, d3, itemStack, holderItemPosition);
    }

    @Override
    public ItemStack getPartGUIItem() {
        return new ItemBuilder(XMaterial.LEATHER_BOOTS).unbreakable().durability(1).displayname(String.valueOf(ChatColor.GOLD) + "Skin").lore(String.valueOf(ChatColor.GRAY) + "The main body of a vehicle.").hideAllFlags().getItemStack();
    }

    @Override
    public String asString() {
        return "";
    }
}

