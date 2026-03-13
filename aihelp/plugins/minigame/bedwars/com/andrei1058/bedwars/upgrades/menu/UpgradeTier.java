/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.potion.PotionEffectType
 */
package com.andrei1058.bedwars.upgrades.menu;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.api.upgrades.UpgradeAction;
import com.andrei1058.bedwars.upgrades.UpgradesManager;
import com.andrei1058.bedwars.upgrades.upgradeaction.DispatchCommand;
import com.andrei1058.bedwars.upgrades.upgradeaction.DragonAction;
import com.andrei1058.bedwars.upgrades.upgradeaction.EnchantItemAction;
import com.andrei1058.bedwars.upgrades.upgradeaction.GeneratorEditAction;
import com.andrei1058.bedwars.upgrades.upgradeaction.PlayerEffectAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public class UpgradeTier {
    private ItemStack displayItem;
    private String name;
    private List<UpgradeAction> upgradeActions = new ArrayList<UpgradeAction>();
    private int cost;
    private Material currency;

    public UpgradeTier(String parentName, String name, ItemStack displayItem, int cost, Material currency) {
        this.displayItem = BedWars.nms.addCustomData(displayItem, "MCONT_" + parentName);
        this.name = name;
        Language.saveIfNotExists(Messages.UPGRADES_UPGRADE_TIER_ITEM_NAME.replace("{name}", parentName.replace("upgrade-", "")).replace("{tier}", name), "&cName not set");
        Language.saveIfNotExists(Messages.UPGRADES_UPGRADE_TIER_ITEM_LORE.replace("{name}", parentName.replace("upgrade-", "")).replace("{tier}", name), Collections.singletonList("&cLore not set"));
        this.cost = cost;
        this.currency = currency;
        block55: for (String action : UpgradesManager.getConfiguration().getYml().getStringList(parentName + "." + name + ".receive")) {
            String[] type = action.trim().split(":");
            if (type.length < 2) continue;
            String[] data = type[1].trim().toLowerCase().split(",");
            UpgradeAction ua = null;
            switch (type[0].trim().toLowerCase()) {
                case "enchant-item": {
                    if (data.length < 3) {
                        BedWars.plugin.getLogger().warning("Invalid " + type[0] + " at upgrades2: " + parentName + "." + name);
                        continue block55;
                    }
                    Enchantment e = Enchantment.getByName((String)data[0].toUpperCase());
                    if (e == null) {
                        BedWars.plugin.getLogger().warning("Invalid enchantment " + data[0].toUpperCase() + " at upgrades2: " + parentName + "." + name);
                        continue block55;
                    }
                    EnchantItemAction.ApplyType apply = null;
                    switch (data[2].toLowerCase()) {
                        case "sword": {
                            apply = EnchantItemAction.ApplyType.SWORD;
                            break;
                        }
                        case "armor": {
                            apply = EnchantItemAction.ApplyType.ARMOR;
                            break;
                        }
                        case "bow": {
                            apply = EnchantItemAction.ApplyType.BOW;
                        }
                    }
                    if (apply == null) {
                        BedWars.plugin.getLogger().warning("Invalid apply type " + data[2] + " at upgrades2: " + parentName + "." + name);
                        continue block55;
                    }
                    int amplifier = 1;
                    try {
                        amplifier = Integer.parseInt(data[1]);
                    } catch (Exception exception) {
                        // empty catch block
                    }
                    ua = new EnchantItemAction(e, amplifier, apply);
                    break;
                }
                case "player-effect": {
                    if (data.length < 4) {
                        BedWars.plugin.getLogger().warning("Invalid " + type[0] + " at upgrades2: " + parentName + "." + name);
                        continue block55;
                    }
                    PotionEffectType pe = PotionEffectType.getByName((String)data[0].toUpperCase());
                    if (pe == null) {
                        BedWars.plugin.getLogger().warning("Invalid potion effect " + data[0] + " at upgrades2: " + parentName + "." + name);
                        continue block55;
                    }
                    PlayerEffectAction.ApplyType applyType = null;
                    switch (data[3].toLowerCase()) {
                        case "team": {
                            applyType = PlayerEffectAction.ApplyType.TEAM;
                            break;
                        }
                        case "base": {
                            applyType = PlayerEffectAction.ApplyType.BASE;
                        }
                    }
                    if (applyType == null) {
                        BedWars.plugin.getLogger().warning("Invalid apply type " + data[3] + " at upgrades2: " + parentName + "." + name);
                        continue block55;
                    }
                    int amp = 1;
                    int time = 0;
                    try {
                        amp = Integer.parseInt(data[1]);
                        time = Integer.parseInt(data[2]);
                    } catch (Exception exception) {
                        // empty catch block
                    }
                    ua = new PlayerEffectAction(pe, amp, time, applyType);
                    break;
                }
                case "generator-edit": {
                    int limit;
                    int amount;
                    int spawn;
                    if (data.length < 4) {
                        BedWars.plugin.getLogger().warning("Invalid " + type[0] + " at upgrades2: " + parentName + "." + name);
                        continue block55;
                    }
                    GeneratorEditAction.ApplyType genType = null;
                    switch (data[0].toLowerCase()) {
                        case "gold": 
                        case "g": {
                            genType = GeneratorEditAction.ApplyType.GOLD;
                            break;
                        }
                        case "iron": 
                        case "i": {
                            genType = GeneratorEditAction.ApplyType.IRON;
                            break;
                        }
                        case "emerald": 
                        case "e": {
                            genType = GeneratorEditAction.ApplyType.EMERALD;
                        }
                    }
                    if (genType == null) {
                        BedWars.plugin.getLogger().warning("Invalid generator type " + data[0] + " at upgrades2: " + parentName + "." + name);
                    }
                    try {
                        spawn = Integer.parseInt(data[1]);
                        amount = Integer.parseInt(data[2]);
                        limit = Integer.parseInt(data[3]);
                    } catch (Exception ex) {
                        BedWars.plugin.getLogger().warning("Invalid generator configuration " + data[0] + " at upgrades2: " + parentName + "." + name);
                        continue block55;
                    }
                    ua = new GeneratorEditAction(genType, amount, spawn, limit);
                    break;
                }
                case "dragon": {
                    int dragons;
                    if (data.length < 1) {
                        BedWars.plugin.getLogger().warning("Invalid " + type[0] + " at upgrades2: " + parentName + "." + name);
                        continue block55;
                    }
                    try {
                        dragons = Integer.parseInt(data[0]);
                    } catch (Exception exc) {
                        BedWars.plugin.getLogger().warning("Invalid dragon amount at upgrades2: " + parentName + "." + name);
                        continue block55;
                    }
                    ua = new DragonAction(dragons);
                    break;
                }
                case "command": {
                    DispatchCommand.CommandType cmdType;
                    if (data.length < 2) {
                        BedWars.plugin.getLogger().warning("Invalid " + type[0] + " at upgrades2: " + parentName + "." + name);
                        continue block55;
                    }
                    try {
                        cmdType = DispatchCommand.CommandType.valueOf(data[0].toUpperCase());
                    } catch (Exception exception) {
                        BedWars.plugin.getLogger().warning("Invalid command type " + data[0] + " at upgrades2: " + parentName + "." + name);
                        continue block55;
                    }
                    data = type[1].split(",");
                    ua = new DispatchCommand(cmdType, data[1]);
                }
            }
            if (ua == null) continue;
            this.upgradeActions.add(ua);
        }
    }

    public ItemStack getDisplayItem() {
        return this.displayItem;
    }

    public String getName() {
        return this.name;
    }

    public int getCost() {
        return this.cost;
    }

    public Material getCurrency() {
        return this.currency;
    }

    public List<UpgradeAction> getUpgradeActions() {
        return this.upgradeActions;
    }
}

