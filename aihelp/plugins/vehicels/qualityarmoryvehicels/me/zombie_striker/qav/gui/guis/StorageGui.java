/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.inventory.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.gui.guis;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import me.zombie_striker.qav.gui.components.InteractionModifier;
import me.zombie_striker.qav.gui.guis.BaseGui;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class StorageGui
extends BaseGui {
    public StorageGui(int n, @NotNull String string, @NotNull Set<InteractionModifier> set) {
        super(n, string, set);
    }

    @Deprecated
    public StorageGui(int n, @NotNull String string) {
        super(n, string);
    }

    @Deprecated
    public StorageGui(@NotNull String string) {
        super(1, string);
    }

    @NotNull
    public @NotNull Map<@NotNull Integer, @NotNull ItemStack> addItem(@NotNull ItemStack ... itemStackArray) {
        return Collections.unmodifiableMap(this.getInventory().addItem(itemStackArray));
    }

    public Map<@NotNull Integer, @NotNull ItemStack> addItem(@NotNull List<ItemStack> list) {
        return this.addItem(list.toArray(new ItemStack[0]));
    }

    @Override
    public void open(@NotNull HumanEntity humanEntity) {
        if (humanEntity.isSleeping()) {
            return;
        }
        this.populateGui();
        humanEntity.openInventory(this.getInventory());
    }
}

