/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package com.andrei1058.bedwars.shop.main;

import com.andrei1058.bedwars.api.language.Language;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class QuickBuyButton {
    private int slot;
    private ItemStack itemStack;
    private String namePath;
    private String lorePath;

    public QuickBuyButton(int slot, ItemStack itemStack, String namePath, String lorePath) {
        this.slot = slot;
        this.itemStack = itemStack;
        this.namePath = namePath;
        this.lorePath = lorePath;
    }

    public ItemStack getItemStack(Player player) {
        ItemStack i = this.itemStack.clone();
        ItemMeta im = i.getItemMeta();
        if (im != null) {
            im.setDisplayName(Language.getMsg(player, this.namePath));
            im.setLore(Language.getList(player, this.lorePath));
            i.setItemMeta(im);
        }
        return i;
    }

    public int getSlot() {
        return this.slot;
    }
}

