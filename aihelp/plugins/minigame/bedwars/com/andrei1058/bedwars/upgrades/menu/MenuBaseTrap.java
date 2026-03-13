/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.potion.PotionEffectType
 */
package com.andrei1058.bedwars.upgrades.menu;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.events.upgrades.UpgradeBuyEvent;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.api.upgrades.EnemyBaseEnterTrap;
import com.andrei1058.bedwars.api.upgrades.MenuContent;
import com.andrei1058.bedwars.api.upgrades.TeamUpgrade;
import com.andrei1058.bedwars.api.upgrades.TrapAction;
import com.andrei1058.bedwars.configuration.Sounds;
import com.andrei1058.bedwars.upgrades.UpgradesManager;
import com.andrei1058.bedwars.upgrades.trapaction.DisenchantAction;
import com.andrei1058.bedwars.upgrades.trapaction.PlayerEffectAction;
import com.andrei1058.bedwars.upgrades.trapaction.RemoveEffectAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

public class MenuBaseTrap
implements MenuContent,
EnemyBaseEnterTrap,
TeamUpgrade {
    private ItemStack displayItem;
    private String name;
    private int cost;
    private Material currency;
    private List<TrapAction> trapActions = new ArrayList<TrapAction>();

    public MenuBaseTrap(String name, ItemStack displayItem, int cost, Material currency) {
        this.displayItem = BedWars.nms.addCustomData(displayItem, "MCONT_" + name);
        this.name = name;
        String nPath = name.replace("base-trap-", "");
        Language.saveIfNotExists(Messages.UPGRADES_BASE_TRAP_ITEM_NAME_PATH + nPath, "&cName not set");
        Language.saveIfNotExists(Messages.UPGRADES_BASE_TRAP_ITEM_LORE_PATH + nPath, Collections.singletonList("&cLore not set"));
        if (UpgradesManager.getConfiguration().getBoolean(name + ".custom-announce")) {
            Language.saveIfNotExists(Messages.UPGRADES_TRAP_CUSTOM_MSG + nPath, "Edit path: " + Messages.UPGRADES_TRAP_CUSTOM_MSG + nPath);
            Language.saveIfNotExists(Messages.UPGRADES_TRAP_CUSTOM_TITLE + nPath, "Edit path: " + Messages.UPGRADES_TRAP_CUSTOM_TITLE + nPath);
            Language.saveIfNotExists(Messages.UPGRADES_TRAP_CUSTOM_SUBTITLE + nPath, "Edit path: " + Messages.UPGRADES_TRAP_CUSTOM_SUBTITLE + nPath);
        }
        this.cost = cost;
        this.currency = currency;
        block33: for (String action : UpgradesManager.getConfiguration().getYml().getStringList(name + ".receive")) {
            String[] type = action.trim().split(":");
            if (type.length < 2) continue;
            String[] data = type[1].trim().toLowerCase().split(",");
            TrapAction ua = null;
            switch (type[0].trim().toLowerCase()) {
                case "player-effect": {
                    if (data.length < 4) {
                        BedWars.plugin.getLogger().warning("Invalid " + type[0] + " at upgrades2: " + name);
                        continue block33;
                    }
                    PotionEffectType pe = PotionEffectType.getByName((String)data[0].toUpperCase());
                    if (pe == null) {
                        BedWars.plugin.getLogger().warning("Invalid potion effect " + data[0] + " at upgrades2: " + name);
                        continue block33;
                    }
                    PlayerEffectAction.ApplyType applyType = null;
                    switch (data[3].toLowerCase()) {
                        case "team": {
                            applyType = PlayerEffectAction.ApplyType.TEAM;
                            break;
                        }
                        case "base": {
                            applyType = PlayerEffectAction.ApplyType.BASE;
                            break;
                        }
                        case "enemy": 
                        case "enemies": {
                            applyType = PlayerEffectAction.ApplyType.ENEMY;
                        }
                    }
                    if (applyType == null) {
                        BedWars.plugin.getLogger().warning("Invalid apply type " + data[3] + " at upgrades2: " + name);
                        continue block33;
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
                case "disenchant-item": {
                    if (data.length < 2) {
                        BedWars.plugin.getLogger().warning("Invalid " + type[0] + " at upgrades2: " + name);
                        continue block33;
                    }
                    Enchantment re = Enchantment.getByName((String)data[0].toUpperCase());
                    if (re == null) {
                        BedWars.plugin.getLogger().warning("Invalid enchantment " + data[0] + " at upgrades2: " + name);
                        continue block33;
                    }
                    DisenchantAction.ApplyType da = null;
                    switch (data[1].toLowerCase()) {
                        case "sword": {
                            da = DisenchantAction.ApplyType.SWORD;
                            break;
                        }
                        case "armor": {
                            da = DisenchantAction.ApplyType.ARMOR;
                            break;
                        }
                        case "bow": {
                            da = DisenchantAction.ApplyType.BOW;
                        }
                    }
                    if (da == null) {
                        BedWars.plugin.getLogger().warning("Invalid apply type " + data[3] + " at upgrades2: " + name);
                        continue block33;
                    }
                    ua = new DisenchantAction(re, da);
                    break;
                }
                case "remove-effect": {
                    if (data.length < 1) {
                        BedWars.plugin.getLogger().warning("Invalid " + type[0] + " at upgrades2: " + name);
                        continue block33;
                    }
                    PotionEffectType pet = PotionEffectType.getByName((String)data[0].toUpperCase());
                    if (pet == null) {
                        BedWars.plugin.getLogger().warning("Invalid potion effect " + data[0] + " at upgrades2: " + name);
                        continue block33;
                    }
                    ua = new RemoveEffectAction(pet);
                }
            }
            if (ua == null) continue;
            this.trapActions.add(ua);
        }
    }

    @Override
    public ItemStack getDisplayItem(Player player, ITeam team) {
        ItemStack i;
        ItemMeta im;
        int cost;
        Material currency = this.currency;
        if (this.currency == null) {
            String st = UpgradesManager.getConfiguration().getYml().getString(team.getArena().getGroup().toLowerCase() + "-upgrades-settings.trap-currency");
            if (st == null) {
                st = UpgradesManager.getConfiguration().getYml().getString("default-upgrades-settings.trap-currency");
            }
            currency = Material.valueOf((String)st.toUpperCase());
        }
        if ((cost = this.cost) == 0) {
            int multiplier = team.getActiveTraps().size();
            int incrementer = UpgradesManager.getConfiguration().getYml().getInt(team.getArena().getGroup().toLowerCase() + "-upgrades-settings.trap-increment-price");
            if (incrementer == 0) {
                incrementer = UpgradesManager.getConfiguration().getYml().getInt("default-upgrades-settings.trap-increment-price");
            }
            if ((cost = UpgradesManager.getConfiguration().getYml().getInt(team.getArena().getGroup().toLowerCase() + "-upgrades-settings.trap-start-price")) == 0) {
                cost = UpgradesManager.getConfiguration().getYml().getInt("default-upgrades-settings.trap-start-price");
            }
            cost += multiplier * incrementer;
        }
        if ((im = (i = this.displayItem.clone()).getItemMeta()) != null) {
            boolean afford = UpgradesManager.getMoney(player, currency) >= cost;
            String color = afford ? Language.getMsg(player, Messages.FORMAT_UPGRADE_COLOR_CAN_AFFORD) : Language.getMsg(player, Messages.FORMAT_UPGRADE_COLOR_CANT_AFFORD);
            im.setDisplayName(Language.getMsg(player, Messages.UPGRADES_BASE_TRAP_ITEM_NAME_PATH + this.name.replace("base-trap-", "")).replace("{color}", color));
            List<String> lore = Language.getList(player, Messages.UPGRADES_BASE_TRAP_ITEM_LORE_PATH + this.name.replace("base-trap-", ""));
            String currencyMsg = UpgradesManager.getCurrencyMsg(player, cost, currency);
            lore.add(Language.getMsg(player, Messages.FORMAT_UPGRADE_TRAP_COST).replace("{cost}", String.valueOf(cost)).replace("{currency}", currencyMsg).replace("{currencyColor}", String.valueOf(UpgradesManager.getCurrencyColor(currency))));
            lore.add("");
            if (afford) {
                lore.add(Language.getMsg(player, Messages.UPGRADES_LORE_REPLACEMENT_CLICK_TO_BUY).replace("{color}", color));
            } else {
                lore.add(Language.getMsg(player, Messages.UPGRADES_LORE_REPLACEMENT_INSUFFICIENT_MONEY).replace("{currency}", currencyMsg).replace("{color}", color));
            }
            im.setLore(lore);
            im.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
            i.setItemMeta(im);
        }
        return i;
    }

    @Override
    public void onClick(Player player, ClickType clickType, ITeam team) {
        int money;
        int cost;
        int queueLimit = UpgradesManager.getConfiguration().getInt(team.getArena().getGroup().toLowerCase() + "-upgrades-settings.trap-queue-limit");
        if (queueLimit == 0) {
            queueLimit = UpgradesManager.getConfiguration().getInt("default-upgrades-settings.trap-queue-limit");
        }
        if (queueLimit <= team.getActiveTraps().size()) {
            player.sendMessage(Language.getMsg(player, Messages.UPGRADES_TRAP_QUEUE_LIMIT));
            return;
        }
        Material currency = this.currency;
        if (this.currency == null) {
            String st = UpgradesManager.getConfiguration().getYml().getString(team.getArena().getGroup().toLowerCase() + "-upgrades-settings.trap-currency");
            if (st == null) {
                st = UpgradesManager.getConfiguration().getYml().getString("default-upgrades-settings.trap-currency");
            }
            currency = Material.valueOf((String)st.toUpperCase());
        }
        if ((cost = this.cost) == 0) {
            int multiplier = team.getActiveTraps().size();
            int incrementer = UpgradesManager.getConfiguration().getYml().getInt(team.getArena().getGroup().toLowerCase() + "-upgrades-settings.trap-increment-price");
            if (incrementer == 0) {
                incrementer = UpgradesManager.getConfiguration().getYml().getInt("default-upgrades-settings.trap-increment-price");
            }
            if ((cost = UpgradesManager.getConfiguration().getYml().getInt(team.getArena().getGroup().toLowerCase() + "-upgrades-settings.trap-start-price")) == 0) {
                cost = UpgradesManager.getConfiguration().getYml().getInt("default-upgrades-settings.trap-start-price");
            }
            cost += multiplier * incrementer;
        }
        if ((money = UpgradesManager.getMoney(player, currency)) < cost) {
            Sounds.playSound("shop-insufficient-money", player);
            player.sendMessage(Language.getMsg(player, Messages.SHOP_INSUFFICIENT_MONEY).replace("{currency}", UpgradesManager.getCurrencyMsg(player, cost, currency)).replace("{amount}", String.valueOf(cost - money)));
            player.closeInventory();
            return;
        }
        UpgradeBuyEvent event = new UpgradeBuyEvent(this, player, team);
        Bukkit.getPluginManager().callEvent((Event)event);
        if (event.isCancelled()) {
            return;
        }
        if (currency == Material.AIR) {
            BedWars.getEconomy().buyAction(player, money);
        } else {
            BedWars.getAPI().getShopUtil().takeMoney(player, currency, cost);
        }
        Sounds.playSound("shop-bought", player);
        team.getActiveTraps().add(this);
        for (Player arenaPlayer : team.getArena().getPlayers()) {
            if (team.isMember(arenaPlayer) || team.getArena().isReSpawning(arenaPlayer) || !(arenaPlayer.getLocation().distance(team.getBed()) <= (double)team.getArena().getIslandRadius())) continue;
            team.getActiveTraps().remove(0).trigger(team, arenaPlayer);
            break;
        }
        for (Player p1 : team.getMembers()) {
            p1.sendMessage(Language.getMsg(p1, Messages.UPGRADES_UPGRADE_BOUGHT_CHAT).replace("{playername}", player.getName()).replace("{player}", player.getDisplayName()).replace("{upgradeName}", ChatColor.stripColor((String)Language.getMsg(p1, Messages.UPGRADES_BASE_TRAP_ITEM_NAME_PATH + this.getName().replace("base-trap-", "")).replace("{color}", ""))));
        }
        UpgradesManager.getMenuForArena(team.getArena()).open(player);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getTierCount() {
        return this.trapActions.size();
    }

    @Override
    public String getNameMsgPath() {
        return Messages.UPGRADES_BASE_TRAP_ITEM_NAME_PATH + this.name.replace("base-trap-", "");
    }

    @Override
    public String getLoreMsgPath() {
        return Messages.UPGRADES_BASE_TRAP_ITEM_LORE_PATH + this.name.replace("base-trap-", "");
    }

    @Override
    public ItemStack getItemStack() {
        return this.displayItem;
    }

    @Override
    public void trigger(ITeam trapTeam, Player player) {
        Sound sound = null;
        if (UpgradesManager.getConfiguration().getYml().get(this.name + ".sound") != null) {
            try {
                sound = Sound.valueOf((String)UpgradesManager.getConfiguration().getYml().getString(this.name + ".sound"));
            } catch (Exception exception) {
                // empty catch block
            }
        }
        if (!Sounds.playSound(sound, trapTeam.getMembers())) {
            Sounds.playSound("trap-sound", trapTeam.getMembers());
        }
        ITeam enemyTeam = trapTeam.getArena().getTeam(player);
        this.trapActions.forEach(t -> t.onTrigger(player, enemyTeam, trapTeam));
        if (UpgradesManager.getConfiguration().getBoolean(this.name + ".custom-announce")) {
            String name2 = this.name.replace("base-trap-", "");
            String color = trapTeam.getArena().getTeam(player) == null ? "" : trapTeam.getArena().getTeam(player).getColor().chat().toString();
            for (Player p : trapTeam.getMembers()) {
                String trapName = ChatColor.stripColor((String)Language.getMsg(p, this.getNameMsgPath())).replace("{color}", "");
                String enemy = trapTeam.getArena().getTeam(player) == null ? "NULL" : trapTeam.getArena().getTeam(player).getDisplayName(Language.getPlayerLanguage(p));
                p.sendMessage(Language.getMsg(p, Messages.UPGRADES_TRAP_CUSTOM_MSG + name2).replace("{trap}", trapName).replace("{player}", player.getName()).replace("{team}", enemy).replace("{color}", color));
                BedWars.nms.sendTitle(p, Language.getMsg(p, Messages.UPGRADES_TRAP_CUSTOM_TITLE + name2).replace("{trap}", trapName).replace("{player}", player.getName()).replace("{team}", enemy).replace("{color}", color), Language.getMsg(p, Messages.UPGRADES_TRAP_CUSTOM_SUBTITLE + name2).replace("{trap}", trapName).replace("{player}", player.getName()).replace("{team}", enemy).replace("{color}", color), 15, 35, 10);
            }
        } else {
            for (Player p : trapTeam.getMembers()) {
                String trapName = ChatColor.stripColor((String)Language.getMsg(p, this.getNameMsgPath())).replace("{color}", "");
                p.sendMessage(Language.getMsg(p, Messages.UPGRADES_TRAP_DEFAULT_MSG).replace("{trap}", trapName));
                BedWars.nms.sendTitle(p, Language.getMsg(p, Messages.UPGRADES_TRAP_DEFAULT_TITLE).replace("{trap}", trapName), Language.getMsg(p, Messages.UPGRADES_TRAP_DEFAULT_SUBTITLE).replace("{trap}", trapName), 15, 35, 10);
            }
        }
    }
}

