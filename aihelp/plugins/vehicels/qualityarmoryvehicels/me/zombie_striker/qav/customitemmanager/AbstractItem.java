/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 */
package me.zombie_striker.qav.customitemmanager;

import java.io.File;
import me.zombie_striker.qav.customitemmanager.MaterialStorage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractItem {
    public abstract ItemStack getItem(Material var1, int var2, int var3);

    public abstract ItemStack getItem(MaterialStorage var1);

    public abstract boolean isCustomItem(ItemStack var1);

    public abstract void initItems(File var1);
}

