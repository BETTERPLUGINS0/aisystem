/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.PotionMeta
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package com.andrei1058.bedwars.shop.main;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.shop.IBuyItem;
import com.andrei1058.bedwars.api.arena.team.TeamEnchant;
import com.andrei1058.bedwars.configuration.Sounds;
import com.andrei1058.bedwars.shop.main.ContentTier;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BuyItem
implements IBuyItem {
    private ItemStack itemStack;
    private boolean autoEquip = false;
    private boolean permanent = false;
    private boolean unbreakable = false;
    private boolean loaded = false;
    private final String upgradeIdentifier;

    public BuyItem(String path, YamlConfiguration yml, String upgradeIdentifier, ContentTier parent) {
        String[] stuff;
        String[] enchant;
        ItemMeta imm;
        ItemMeta im;
        BedWars.debug("Loading BuyItems: " + path);
        this.upgradeIdentifier = upgradeIdentifier;
        if (yml.get(path + ".material") == null) {
            BedWars.plugin.getLogger().severe("BuyItem: Material not set at " + path);
            return;
        }
        this.itemStack = BedWars.nms.createItemStack(yml.getString(path + ".material"), yml.get(path + ".amount") == null ? 1 : yml.getInt(path + ".amount"), (short)(yml.get(path + ".data") == null ? 1 : yml.getInt(path + ".data")));
        if (yml.get(path + ".name") != null && (im = this.itemStack.getItemMeta()) != null) {
            im.setDisplayName(ChatColor.translateAlternateColorCodes((char)'&', (String)("&r" + yml.getString(path + ".name"))));
            this.itemStack.setItemMeta(im);
        }
        if (yml.get(path + ".enchants") != null && this.itemStack.getItemMeta() != null) {
            imm = this.itemStack.getItemMeta();
            for (String enc : enchant = yml.getString(path + ".enchants").split(",")) {
                stuff = enc.split(" ");
                try {
                    Enchantment.getByName((String)stuff[0]);
                } catch (Exception ex) {
                    BedWars.plugin.getLogger().severe("BuyItem: Invalid enchants " + stuff[0] + " at: " + path + ".enchants");
                    continue;
                }
                int ieee = 1;
                if (stuff.length >= 2) {
                    try {
                        ieee = Integer.parseInt(stuff[1]);
                    } catch (Exception exx) {
                        BedWars.plugin.getLogger().severe("BuyItem: Invalid int " + stuff[1] + " at: " + path + ".enchants");
                        continue;
                    }
                }
                imm.addEnchant(Enchantment.getByName((String)stuff[0]), ieee, true);
            }
            this.itemStack.setItemMeta(imm);
        }
        if (yml.get(path + ".potion") != null && this.itemStack.getType() == Material.POTION) {
            if (yml.getString(path + ".potion-color") != null && !yml.getString(path + ".potion-color").isEmpty()) {
                this.itemStack = BedWars.nms.setTag(this.itemStack, "CustomPotionColor", yml.getString(path + ".potion-color"));
            }
            if ((imm = (PotionMeta)this.itemStack.getItemMeta()) != null) {
                for (String enc : enchant = yml.getString(path + ".potion").split(",")) {
                    stuff = enc.split(" ");
                    try {
                        PotionEffectType.getByName((String)stuff[0].toUpperCase());
                    } catch (Exception ex) {
                        BedWars.plugin.getLogger().severe("BuyItem: Invalid potion effect " + stuff[0] + " at: " + path + ".potion");
                        continue;
                    }
                    int duration = 50;
                    int amplifier = 1;
                    if (stuff.length >= 3) {
                        try {
                            duration = Integer.parseInt(stuff[1]);
                        } catch (Exception exx) {
                            BedWars.plugin.getLogger().severe("BuyItem: Invalid int (duration) " + stuff[1] + " at: " + path + ".potion");
                            continue;
                        }
                        try {
                            amplifier = Integer.parseInt(stuff[2]);
                        } catch (Exception exx) {
                            BedWars.plugin.getLogger().severe("BuyItem: Invalid int (amplifier) " + stuff[2] + " at: " + path + ".potion");
                            continue;
                        }
                    }
                    imm.addCustomEffect(new PotionEffect(PotionEffectType.getByName((String)stuff[0].toUpperCase()), duration * 20, amplifier), true);
                }
                this.itemStack.setItemMeta(imm);
            }
            this.itemStack = BedWars.nms.setTag(this.itemStack, "Potion", "minecraft:water");
            if (parent.getItemStack().getType() == Material.POTION && imm != null && !imm.getCustomEffects().isEmpty()) {
                ItemStack parentItemStack = parent.getItemStack();
                if (parentItemStack.getItemMeta() != null) {
                    PotionMeta potionMeta = (PotionMeta)parentItemStack.getItemMeta();
                    for (PotionEffect potionEffect : imm.getCustomEffects()) {
                        potionMeta.addCustomEffect(potionEffect, true);
                    }
                    parentItemStack.setItemMeta((ItemMeta)potionMeta);
                }
                parentItemStack = BedWars.nms.setTag(parentItemStack, "Potion", "minecraft:water");
                parent.setItemStack(parentItemStack);
            }
        }
        if (yml.get(path + ".auto-equip") != null) {
            this.autoEquip = yml.getBoolean(path + ".auto-equip");
        }
        if (yml.get(upgradeIdentifier + ".content-settings.is-permanent") != null) {
            this.permanent = yml.getBoolean(upgradeIdentifier + ".content-settings.is-permanent");
        }
        if (yml.get(upgradeIdentifier + ".content-settings.is-unbreakable") != null) {
            this.unbreakable = yml.getBoolean(upgradeIdentifier + ".content-settings.is-unbreakable");
        }
        this.loaded = true;
    }

    @Override
    public boolean isLoaded() {
        return this.loaded;
    }

    @Override
    public void give(Player player, IArena arena) {
        ItemStack i = this.itemStack.clone();
        BedWars.debug("Giving BuyItem: " + this.getUpgradeIdentifier() + " to: " + player.getName());
        if (this.autoEquip && BedWars.nms.isArmor(this.itemStack)) {
            Material m = i.getType();
            ItemMeta im = i.getItemMeta();
            if (arena.getTeam(player) == null) {
                BedWars.debug("Could not give BuyItem to " + player.getName() + " - TEAM IS NULL");
                return;
            }
            if (im != null) {
                for (TeamEnchant e : arena.getTeam(player).getArmorsEnchantments()) {
                    im.addEnchant(e.getEnchantment(), e.getAmplifier(), true);
                }
                if (this.permanent) {
                    BedWars.nms.setUnbreakable(im);
                }
                i.setItemMeta(im);
            }
            if (m == Material.LEATHER_HELMET || m == Material.CHAINMAIL_HELMET || m == Material.IRON_HELMET || m == Material.DIAMOND_HELMET || m == BedWars.nms.materialGoldenHelmet() || m == BedWars.nms.materialNetheriteHelmet()) {
                if (this.permanent) {
                    i = BedWars.nms.setShopUpgradeIdentifier(i, this.upgradeIdentifier);
                }
                player.getInventory().setHelmet(i);
            } else if (m == Material.LEATHER_CHESTPLATE || m == Material.CHAINMAIL_CHESTPLATE || m == Material.IRON_CHESTPLATE || m == Material.DIAMOND_CHESTPLATE || m == BedWars.nms.materialGoldenChestPlate() || m == BedWars.nms.materialNetheriteChestPlate() || m == BedWars.nms.materialElytra()) {
                if (this.permanent) {
                    i = BedWars.nms.setShopUpgradeIdentifier(i, this.upgradeIdentifier);
                }
                player.getInventory().setChestplate(i);
            } else if (m == Material.LEATHER_LEGGINGS || m == Material.CHAINMAIL_LEGGINGS || m == Material.IRON_LEGGINGS || m == Material.DIAMOND_LEGGINGS || m == BedWars.nms.materialGoldenLeggings() || m == BedWars.nms.materialNetheriteLeggings()) {
                if (this.permanent) {
                    i = BedWars.nms.setShopUpgradeIdentifier(i, this.upgradeIdentifier);
                }
                player.getInventory().setLeggings(i);
            } else {
                if (this.permanent) {
                    i = BedWars.nms.setShopUpgradeIdentifier(i, this.upgradeIdentifier);
                }
                player.getInventory().setBoots(i);
            }
            player.updateInventory();
            Sounds.playSound("shop-auto-equip", player);
            Bukkit.getScheduler().runTaskLater((Plugin)BedWars.plugin, () -> {
                if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                    for (Player p : arena.getPlayers()) {
                        BedWars.nms.hideArmor(player, p);
                    }
                }
            }, 20L);
            return;
        }
        ItemMeta im = i.getItemMeta();
        i = BedWars.nms.colourItem(i, arena.getTeam(player));
        if (im != null) {
            if (this.permanent) {
                BedWars.nms.setUnbreakable(im);
            }
            if (this.unbreakable) {
                BedWars.nms.setUnbreakable(im);
            }
            if (i.getType() == Material.BOW) {
                if (this.permanent) {
                    BedWars.nms.setUnbreakable(im);
                }
                for (TeamEnchant e : arena.getTeam(player).getBowsEnchantments()) {
                    im.addEnchant(e.getEnchantment(), e.getAmplifier(), true);
                }
            } else if (BedWars.nms.isSword(i) || BedWars.nms.isAxe(i)) {
                for (TeamEnchant e : arena.getTeam(player).getSwordsEnchantments()) {
                    im.addEnchant(e.getEnchantment(), e.getAmplifier(), true);
                }
            }
            i.setItemMeta(im);
        }
        if (this.permanent) {
            i = BedWars.nms.setShopUpgradeIdentifier(i, this.upgradeIdentifier);
        }
        if (BedWars.nms.isSword(i)) {
            for (ItemStack itm : player.getInventory().getContents()) {
                if (itm == null || itm.getType() == Material.AIR || !BedWars.nms.isSword(itm) || itm == i || !BedWars.nms.isCustomBedWarsItem(itm) || !BedWars.nms.getCustomData(itm).equals("DEFAULT_ITEM") || !(BedWars.nms.getDamage(itm) <= BedWars.nms.getDamage(i))) continue;
                player.getInventory().remove(itm);
            }
        }
        player.getInventory().addItem(new ItemStack[]{i});
        player.updateInventory();
    }

    @Override
    public String getUpgradeIdentifier() {
        return this.upgradeIdentifier;
    }

    @Override
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    @Override
    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public boolean isAutoEquip() {
        return this.autoEquip;
    }

    @Override
    public void setAutoEquip(boolean autoEquip) {
        this.autoEquip = autoEquip;
    }

    @Override
    public boolean isPermanent() {
        return this.permanent;
    }

    @Override
    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }

    @Override
    public boolean isUnbreakable() {
        return this.unbreakable;
    }

    @Override
    public void setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
    }
}

