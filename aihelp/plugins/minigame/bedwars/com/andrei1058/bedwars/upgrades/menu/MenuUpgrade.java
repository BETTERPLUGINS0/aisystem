/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package com.andrei1058.bedwars.upgrades.menu;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.events.upgrades.UpgradeBuyEvent;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.api.upgrades.MenuContent;
import com.andrei1058.bedwars.api.upgrades.TeamUpgrade;
import com.andrei1058.bedwars.api.upgrades.UpgradeAction;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.configuration.Sounds;
import com.andrei1058.bedwars.upgrades.UpgradesManager;
import com.andrei1058.bedwars.upgrades.menu.UpgradeTier;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MenuUpgrade
implements MenuContent,
TeamUpgrade {
    private String name;
    private List<UpgradeTier> tiers = new LinkedList<UpgradeTier>();

    public MenuUpgrade(String name) {
        this.name = name;
    }

    @Override
    public ItemStack getDisplayItem(Player player, ITeam team) {
        UpgradeTier ut;
        boolean highest;
        if (this.tiers.isEmpty()) {
            return new ItemStack(Material.BEDROCK);
        }
        int tier = -1;
        if (team.getTeamUpgradeTiers().containsKey(this.getName())) {
            tier = team.getTeamUpgradeTiers().get(this.getName());
        }
        boolean bl = highest = this.getTiers().size() == tier + 1 && team.getTeamUpgradeTiers().containsKey(this.getName());
        if (!highest) {
            ++tier;
        }
        boolean afford = UpgradesManager.getMoney(player, (ut = this.getTiers().get(tier)).getCurrency()) >= ut.getCost();
        ItemStack i = new ItemStack(this.tiers.get(tier).getDisplayItem());
        ItemMeta im = i.getItemMeta();
        if (im == null) {
            return i;
        }
        String color = !highest ? (afford ? Language.getMsg(player, Messages.FORMAT_UPGRADE_COLOR_CAN_AFFORD) : Language.getMsg(player, Messages.FORMAT_UPGRADE_COLOR_CANT_AFFORD)) : Language.getMsg(player, Messages.FORMAT_UPGRADE_COLOR_UNLOCKED);
        im.setDisplayName(Language.getMsg(player, Messages.UPGRADES_UPGRADE_TIER_ITEM_NAME.replace("{name}", this.getName().replace("upgrade-", "")).replace("{tier}", ut.getName())).replace("{color}", color));
        ArrayList<String> lore = new ArrayList<String>();
        String currencyMsg = UpgradesManager.getCurrencyMsg(player, ut);
        for (String s : Language.getList(player, Messages.UPGRADES_UPGRADE_TIER_ITEM_LORE.replace("{name}", this.getName().replace("upgrade-", "")))) {
            if (s.contains("{tier_")) {
                String result = s.replaceAll(".*_([0-9]+)_.*", "$1");
                String tierColor = Messages.FORMAT_UPGRADE_TIER_LOCKED;
                if (Integer.valueOf(result) - 1 <= team.getTeamUpgradeTiers().getOrDefault(this.getName(), -1)) {
                    tierColor = Messages.FORMAT_UPGRADE_TIER_UNLOCKED;
                }
                UpgradeTier upgradeTier = this.tiers.get(Integer.valueOf(result) - 1);
                lore.add(s.replace("{tier_" + result + "_cost}", String.valueOf(upgradeTier.getCost())).replace("{tier_" + result + "_currency}", currencyMsg).replace("{tier_" + result + "_color}", Language.getMsg(player, tierColor)));
                continue;
            }
            lore.add(s.replace("{color}", color));
        }
        if (highest) {
            lore.add(Language.getMsg(player, Messages.UPGRADES_LORE_REPLACEMENT_UNLOCKED).replace("{color}", color));
        } else if (afford) {
            lore.add(Language.getMsg(player, Messages.UPGRADES_LORE_REPLACEMENT_CLICK_TO_BUY).replace("{color}", color));
        } else {
            lore.add(Language.getMsg(player, Messages.UPGRADES_LORE_REPLACEMENT_INSUFFICIENT_MONEY).replace("{currency}", currencyMsg).replace("{color}", color));
        }
        im.setLore(lore);
        im.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        i.setItemMeta(im);
        return i;
    }

    @Override
    public void onClick(Player player, ClickType clickType, ITeam team) {
        int tier = -1;
        if (team.getTeamUpgradeTiers().containsKey(this.getName())) {
            tier = team.getTeamUpgradeTiers().get(this.getName());
        }
        if (this.getTiers().size() - 1 > tier) {
            UpgradeTier ut = this.getTiers().get(tier + 1);
            int money = UpgradesManager.getMoney(player, ut.getCurrency());
            if (money < ut.getCost()) {
                Sounds.playSound("shop-insufficient-money", player);
                player.sendMessage(Language.getMsg(player, Messages.SHOP_INSUFFICIENT_MONEY).replace("{currency}", UpgradesManager.getCurrencyMsg(player, ut)).replace("{amount}", String.valueOf(ut.getCost() - money)));
                player.closeInventory();
                return;
            }
            UpgradeBuyEvent event = new UpgradeBuyEvent(this, player, team);
            Bukkit.getPluginManager().callEvent((Event)event);
            if (event.isCancelled()) {
                return;
            }
            if (ut.getCurrency() == Material.AIR) {
                BedWars.getEconomy().buyAction(player, ut.getCost());
            } else {
                BedWars.getAPI().getShopUtil().takeMoney(player, ut.getCurrency(), ut.getCost());
            }
            if (team.getTeamUpgradeTiers().containsKey(this.getName())) {
                team.getTeamUpgradeTiers().replace(this.getName(), team.getTeamUpgradeTiers().get(this.getName()) + 1);
            } else {
                team.getTeamUpgradeTiers().put(this.getName(), 0);
            }
            Sounds.playSound("shop-bought", player);
            for (UpgradeAction a : ut.getUpgradeActions()) {
                a.onBuy(player, team);
            }
            for (Player p1 : team.getMembers()) {
                p1.sendMessage(Language.getMsg(p1, Messages.UPGRADES_UPGRADE_BOUGHT_CHAT).replace("{playername}", player.getName()).replace("{player}", player.getDisplayName()).replace("{upgradeName}", ChatColor.stripColor((String)Language.getMsg(p1, Messages.UPGRADES_UPGRADE_TIER_ITEM_NAME.replace("{name}", this.getName().replace("upgrade-", "")).replace("{tier}", ut.getName())))).replace("{color}", ""));
            }
            ImmutableMap<Integer, MenuContent> menuContentBySlot = UpgradesManager.getMenuForArena(Arena.getArenaByPlayer(player)).getMenuContentBySlot();
            Inventory inv = player.getOpenInventory().getTopInventory();
            for (Map.Entry entry : menuContentBySlot.entrySet()) {
                inv.setItem(((Integer)entry.getKey()).intValue(), ((MenuContent)entry.getValue()).getDisplayItem(player, team));
            }
        }
    }

    public boolean addTier(UpgradeTier upgradeTier) {
        for (UpgradeTier ut : this.tiers) {
            if (!ut.getName().equalsIgnoreCase(upgradeTier.getName())) continue;
            return false;
        }
        this.tiers.add(upgradeTier);
        return true;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getTierCount() {
        return this.tiers.size();
    }

    public List<UpgradeTier> getTiers() {
        return Collections.unmodifiableList(this.tiers);
    }
}

