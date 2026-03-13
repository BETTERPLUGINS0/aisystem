/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package com.andrei1058.bedwars.halloween.shop;

import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.shop.IBuyItem;
import com.andrei1058.bedwars.api.arena.shop.IContentTier;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.shop.ShopCache;
import com.andrei1058.bedwars.shop.main.CategoryContent;
import com.andrei1058.bedwars.shop.main.ShopCategory;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PumpkinContent
extends CategoryContent {
    private final int slot;

    public PumpkinContent(ShopCategory father) {
        super(null, null, null, null, father);
        int finalI;
        int foundSlot = -1;
        int i = 19;
        while (i < 26) {
            finalI = i++;
            if (!father.getCategoryContentList().stream().noneMatch(categoryContent -> categoryContent.getSlot() == finalI)) continue;
            foundSlot = i;
            break;
        }
        if (foundSlot == -1) {
            i = 28;
            while (i < 35) {
                finalI = i++;
                if (!father.getCategoryContentList().stream().noneMatch(categoryContent -> categoryContent.getSlot() == finalI)) continue;
                foundSlot = i;
                break;
            }
        }
        if (foundSlot == -1) {
            i = 37;
            while (i < 44) {
                finalI = i++;
                if (!father.getCategoryContentList().stream().noneMatch(categoryContent -> categoryContent.getSlot() == finalI)) continue;
                foundSlot = i;
                break;
            }
        }
        this.slot = foundSlot;
        this.setLoaded(this.slot != -1);
        if (!this.isLoaded()) {
            return;
        }
        OneTier pumpkinTier = new OneTier();
        this.getContentTiers().add(pumpkinTier);
    }

    @Override
    public String getIdentifier() {
        return "halloween-special-pumpkin";
    }

    @Override
    public int getSlot() {
        return this.slot;
    }

    @Override
    public boolean isPermanent() {
        return false;
    }

    @Override
    public ItemStack getItemStack(Player player) {
        IContentTier tier = this.getContentTiers().get(0);
        ItemStack pumpkin = tier.getItemStack();
        boolean canAfford = PumpkinContent.calculateMoney(player, tier.getCurrency()) >= tier.getPrice();
        String translatedCurrency = Language.getMsg(player, PumpkinContent.getCurrencyMsgPath(tier));
        String buyStatus = !canAfford ? Language.getMsg(player, "shop-lore-status-cant-afford").replace("{currency}", translatedCurrency) : Language.getMsg(player, "shop-lore-status-can-buy");
        ChatColor cColor = PumpkinContent.getCurrencyColor(tier.getCurrency());
        pumpkin.setAmount(12);
        ItemMeta itemMeta = pumpkin.getItemMeta();
        itemMeta.setDisplayName(String.valueOf(ChatColor.GOLD) + String.valueOf(ChatColor.BOLD) + "Happy Halloween!");
        itemMeta.setLore(Arrays.asList("", String.valueOf(cColor) + String.valueOf(tier.getPrice()) + " " + String.valueOf(cColor) + translatedCurrency, " ", buyStatus));
        pumpkin.setItemMeta(itemMeta);
        return pumpkin;
    }

    @Override
    public ItemStack getItemStack(Player player, ShopCache shopCache) {
        return this.getItemStack(player);
    }

    private static class OneTier
    implements IContentTier {
        private OneTier() {
        }

        @Override
        public int getPrice() {
            return 4;
        }

        @Override
        public Material getCurrency() {
            return Material.IRON_INGOT;
        }

        @Override
        public void setCurrency(Material currency) {
        }

        @Override
        public void setPrice(int price) {
        }

        @Override
        public void setItemStack(ItemStack itemStack) {
        }

        @Override
        public void setBuyItemsList(List<IBuyItem> buyItemsList) {
        }

        @Override
        public ItemStack getItemStack() {
            return new ItemStack(Material.PUMPKIN, 12);
        }

        @Override
        public int getValue() {
            return 4;
        }

        @Override
        public List<IBuyItem> getBuyItemsList() {
            return Collections.singletonList(new FinalItem());
        }
    }

    private static class FinalItem
    implements IBuyItem {
        private FinalItem() {
        }

        @Override
        public boolean isLoaded() {
            return true;
        }

        @Override
        public void give(Player player, IArena arena) {
            player.getInventory().addItem(new ItemStack[]{new ItemStack(Material.PUMPKIN, 12)});
        }

        @Override
        public String getUpgradeIdentifier() {
            return null;
        }

        @Override
        public ItemStack getItemStack() {
            return null;
        }

        @Override
        public void setItemStack(ItemStack itemStack) {
        }

        @Override
        public boolean isAutoEquip() {
            return false;
        }

        @Override
        public void setAutoEquip(boolean autoEquip) {
        }

        @Override
        public boolean isPermanent() {
            return false;
        }

        @Override
        public void setPermanent(boolean permanent) {
        }

        @Override
        public boolean isUnbreakable() {
            return false;
        }

        @Override
        public void setUnbreakable(boolean unbreakable) {
        }
    }
}

