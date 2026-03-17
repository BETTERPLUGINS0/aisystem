/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.util.Vector
 */
package me.zombie_striker.qav.attachments;

import java.util.List;
import me.zombie_striker.qav.VehicleEntity;
import me.zombie_striker.qav.api.QualityArmoryVehicles;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public abstract class Attachment {
    private final String name;
    private final List<String> lore;
    private final int id;
    private final Material material;
    private final Vector vector;

    public Attachment(String string, int n, Material material, Vector vector) {
        this(string, null, n, material, vector);
    }

    public Attachment(String string, List<String> list, int n, Material material, Vector vector) {
        this.name = string;
        this.lore = list;
        this.id = n;
        this.material = material;
        this.vector = vector;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public int getId() {
        return this.id;
    }

    public Material getMaterial() {
        return this.material;
    }

    public Vector getVector() {
        return this.vector;
    }

    public ItemStack build() {
        return QualityArmoryVehicles.getAttachmentItemStack(this);
    }

    public abstract void animate(VehicleEntity var1, ArmorStand var2);
}

