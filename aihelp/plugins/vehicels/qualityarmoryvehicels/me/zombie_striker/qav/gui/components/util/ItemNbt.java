/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.inventory.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.gui.components.util;

import me.zombie_striker.qav.gui.components.nbt.LegacyNbt;
import me.zombie_striker.qav.gui.components.nbt.NbtWrapper;
import me.zombie_striker.qav.gui.components.nbt.Pdc;
import me.zombie_striker.qav.gui.components.util.VersionHelper;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class ItemNbt {
    private static final NbtWrapper nbt = ItemNbt.selectNbt();

    public static ItemStack setString(@NotNull ItemStack itemStack, @NotNull String string, @NotNull String string2) {
        return nbt.setString(itemStack, string, string2);
    }

    public static String getString(@NotNull ItemStack itemStack, @NotNull String string) {
        return nbt.getString(itemStack, string);
    }

    public static ItemStack setBoolean(@NotNull ItemStack itemStack, @NotNull String string, boolean bl) {
        return nbt.setBoolean(itemStack, string, bl);
    }

    public static ItemStack removeTag(@NotNull ItemStack itemStack, @NotNull String string) {
        return nbt.removeTag(itemStack, string);
    }

    private static NbtWrapper selectNbt() {
        if (VersionHelper.IS_PDC_VERSION) {
            return new Pdc();
        }
        return new LegacyNbt();
    }
}

