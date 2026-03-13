/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.jetbrains.annotations.NotNull
 */
package net.advancedplugins.as.impl.utils.items;

import com.google.common.collect.HashMultimap;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.advancedplugins.as.impl.utils.nbt.utils.MinecraftVersion;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class ItemFlagFix {
    private static final UUID FIX_UUID = UUID.fromString("90787d5e-1940-4722-a91e-f0ba37f7c29d");

    public static void fix(ItemStack itemStack) {
        if (!itemStack.hasItemMeta()) {
            return;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        ItemFlagFix.fix(itemMeta);
        itemStack.setItemMeta(itemMeta);
    }

    public static void fix(ItemMeta itemMeta) {
        try {
            if (!MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
                return;
            }
            HashMultimap hashMultimap = itemMeta.getAttributeModifiers();
            if (hashMultimap == null) {
                hashMultimap = HashMultimap.create();
                itemMeta.setAttributeModifiers(hashMultimap);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @NotNull
    public static ItemFlag[] hideAllAttributes() {
        ArrayList arrayList = new ArrayList(List.of((Object)ItemFlag.HIDE_ATTRIBUTES, (Object)ItemFlag.HIDE_DESTROYS, (Object)ItemFlag.HIDE_ENCHANTS, (Object)ItemFlag.HIDE_PLACED_ON, (Object)ItemFlag.HIDE_UNBREAKABLE));
        if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
            arrayList.add(ItemFlag.valueOf((String)"HIDE_ADDITIONAL_TOOLTIP"));
        } else {
            arrayList.add(ItemFlag.valueOf((String)"HIDE_POTION_EFFECTS"));
        }
        if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_19_R3)) {
            arrayList.add(ItemFlag.valueOf((String)"HIDE_ARMOR_TRIM"));
        }
        return (ItemFlag[])arrayList.toArray(ItemFlag[]::new);
    }
}

