/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package com.andrei1058.bedwars.upgrades;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.api.upgrades.MenuContent;
import com.andrei1058.bedwars.api.upgrades.UpgradesIndex;
import com.andrei1058.bedwars.arena.Misc;
import com.andrei1058.bedwars.configuration.UpgradesConfig;
import com.andrei1058.bedwars.upgrades.listeners.InventoryListener;
import com.andrei1058.bedwars.upgrades.listeners.UpgradeOpenListener;
import com.andrei1058.bedwars.upgrades.menu.InternalMenu;
import com.andrei1058.bedwars.upgrades.menu.MenuBaseTrap;
import com.andrei1058.bedwars.upgrades.menu.MenuCategory;
import com.andrei1058.bedwars.upgrades.menu.MenuSeparator;
import com.andrei1058.bedwars.upgrades.menu.MenuTrapSlot;
import com.andrei1058.bedwars.upgrades.menu.MenuUpgrade;
import com.andrei1058.bedwars.upgrades.menu.UpgradeTier;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class UpgradesManager {
    private static final LinkedList<UUID> upgradeViewers = new LinkedList();
    private static final HashMap<String, MenuContent> menuContentByName = new HashMap();
    private static final HashMap<String, UpgradesIndex> menuByName = new HashMap();
    private static final HashMap<IArena, UpgradesIndex> customMenuForArena = new HashMap();
    private static UpgradesConfig upgrades;

    private UpgradesManager() {
    }

    public static void init() {
        File oldFile = new File(BedWars.plugin.getDataFolder(), "/upgrades.yml");
        oldFile.delete();
        upgrades = new UpgradesConfig("upgrades2", BedWars.plugin.getDataFolder().getPath());
        Iterator iterator = upgrades.getYml().getConfigurationSection("").getKeys(false).iterator();
        while (iterator.hasNext()) {
            String index;
            String name = index = (String)iterator.next();
            if (index.startsWith("upgrade-")) {
                if (UpgradesManager.getMenuContent(name) != null || UpgradesManager.loadUpgrade(name)) continue;
                Bukkit.getLogger().log(Level.WARNING, "Could not load upgrade: " + name);
                continue;
            }
            if (index.startsWith("separator-")) {
                if (UpgradesManager.getMenuContent(name) != null || UpgradesManager.loadSeparator(name)) continue;
                Bukkit.getLogger().log(Level.WARNING, "Could not load separator: " + name);
                continue;
            }
            if (index.startsWith("category-")) {
                if (UpgradesManager.getMenuContent(name) != null || UpgradesManager.loadCategory(name)) continue;
                Bukkit.getLogger().log(Level.WARNING, "Could not load category: " + name);
                continue;
            }
            if (index.startsWith("base-trap-")) {
                if (UpgradesManager.getMenuContent(name) != null || UpgradesManager.loadBaseTrap(name)) continue;
                Bukkit.getLogger().log(Level.WARNING, "Could not base trap: " + name);
                continue;
            }
            if (!index.endsWith("-upgrades-settings") || (name = index.replace("-upgrades-settings", "")).isEmpty() || UpgradesManager.loadMenu(name)) continue;
            Bukkit.getLogger().log(Level.WARNING, "Could not load menu: " + name);
        }
        BedWars.registerEvents(new InventoryListener(), new UpgradeOpenListener());
    }

    public static boolean isWatchingUpgrades(UUID uuid) {
        return upgradeViewers.contains(uuid);
    }

    public static void setWatchingUpgrades(UUID uuid) {
        if (!upgradeViewers.contains(uuid)) {
            upgradeViewers.add(uuid);
        }
    }

    public static void removeWatchingUpgrades(UUID uuid) {
        upgradeViewers.remove(uuid);
    }

    public static boolean loadMenu(String groupName) {
        if (!upgrades.getYml().isSet(groupName + "-upgrades-settings.menu-content")) {
            return false;
        }
        if (menuByName.containsKey(groupName.toLowerCase())) {
            return false;
        }
        InternalMenu um = new InternalMenu(groupName);
        for (String component : upgrades.getYml().getStringList(groupName + "-upgrades-settings.menu-content")) {
            String[] data = component.split(",");
            if (data.length <= 1) continue;
            MenuContent mc = UpgradesManager.getMenuContent(data[0]);
            if (data[0].startsWith("category-")) {
                if (mc == null && UpgradesManager.loadCategory(data[0])) {
                    mc = UpgradesManager.getMenuContent(data[0]);
                }
            } else if (data[0].startsWith("upgrade-")) {
                if (mc == null && UpgradesManager.loadUpgrade(data[0])) {
                    mc = UpgradesManager.getMenuContent(data[0]);
                }
            } else if (data[0].startsWith("trap-slot-")) {
                if (mc == null && UpgradesManager.loadTrapSlot(data[0])) {
                    mc = UpgradesManager.getMenuContent(data[0]);
                }
            } else if (data[0].startsWith("separator-")) {
                if (mc == null && UpgradesManager.loadSeparator(data[0])) {
                    mc = UpgradesManager.getMenuContent(data[0]);
                }
            } else if (data[0].startsWith("base-trap-") && mc == null && UpgradesManager.loadBaseTrap(data[0])) {
                mc = UpgradesManager.getMenuContent(data[0]);
            }
            if (mc == null) continue;
            for (int i = 1; i < data.length; ++i) {
                if (!Misc.isNumber(data[i])) continue;
                um.addContent(mc, Integer.parseInt(data[i]));
            }
        }
        menuByName.put(groupName.toLowerCase(), um);
        BedWars.debug("Registering upgrade menu: " + groupName);
        return true;
    }

    private static boolean loadCategory(String name) {
        if (name == null) {
            return false;
        }
        if (!name.startsWith("category-")) {
            return false;
        }
        if (upgrades.getYml().get(name) == null) {
            return false;
        }
        if (UpgradesManager.getMenuContent(name) != null) {
            return false;
        }
        MenuCategory uc = new MenuCategory(name, UpgradesManager.createDisplayItem(name));
        for (String component : upgrades.getYml().getStringList(name + ".category-content")) {
            String[] data = component.split(",");
            if (data.length <= 1) continue;
            MenuContent mc = null;
            if (data[0].startsWith("category-")) {
                mc = UpgradesManager.getMenuContent(data[0]);
                if (mc == null && UpgradesManager.loadCategory(data[0])) {
                    mc = UpgradesManager.getMenuContent(data[0]);
                }
            } else if (data[0].startsWith("upgrade-")) {
                mc = UpgradesManager.getMenuContent(data[0]);
                if (mc == null && UpgradesManager.loadUpgrade(data[0])) {
                    mc = UpgradesManager.getMenuContent(data[0]);
                }
            } else if (data[0].startsWith("trap-slot-")) {
                mc = UpgradesManager.getMenuContent(data[0]);
                if (mc == null && UpgradesManager.loadTrapSlot(data[0])) {
                    mc = UpgradesManager.getMenuContent(data[0]);
                }
            } else if (data[0].startsWith("separator-")) {
                mc = UpgradesManager.getMenuContent(data[0]);
                if (mc == null && UpgradesManager.loadSeparator(data[0])) {
                    mc = UpgradesManager.getMenuContent(data[0]);
                }
            } else if (data[0].startsWith("base-trap-") && (mc = UpgradesManager.getMenuContent(data[0])) == null && UpgradesManager.loadBaseTrap(data[0])) {
                mc = UpgradesManager.getMenuContent(data[0]);
            }
            if (mc == null) continue;
            for (int i = 1; i < data.length; ++i) {
                if (!Misc.isNumber(data[i])) continue;
                uc.addContent(mc, Integer.parseInt(data[i]));
            }
        }
        menuContentByName.put(name.toLowerCase(), uc);
        BedWars.debug("Registering upgrade: " + name);
        return true;
    }

    private static boolean loadUpgrade(String name) {
        if (name == null) {
            return false;
        }
        if (!name.startsWith("upgrade-")) {
            return false;
        }
        if (upgrades.getYml().get(name) == null) {
            return false;
        }
        if (upgrades.getYml().get(name + ".tier-1") == null) {
            return false;
        }
        if (UpgradesManager.getMenuContent(name) != null) {
            return false;
        }
        MenuUpgrade mu = new MenuUpgrade(name);
        for (String s : upgrades.getYml().getConfigurationSection(name).getKeys(false)) {
            if (!s.startsWith("tier-")) continue;
            if (upgrades.getYml().get(name + "." + s + ".receive") == null) {
                BedWars.debug("Could not load Upgrade " + name + " tier: " + s + ". Receive not set.");
                continue;
            }
            if (upgrades.getYml().get(name + "." + s + ".display-item") == null) {
                BedWars.debug("Could not load Upgrade " + name + " tier: " + s + ". Display item not set.");
                continue;
            }
            if (upgrades.getYml().get(name + "." + s + ".cost") == null) {
                BedWars.debug("Could not load Upgrade " + name + " tier: " + s + ". Cost not set.");
                continue;
            }
            if (upgrades.getYml().get(name + "." + s + ".currency") == null) {
                BedWars.debug("Could not load Upgrade " + name + " tier: " + s + ". Currency not set.");
                continue;
            }
            UpgradeTier ut = new UpgradeTier(name, s, UpgradesManager.createDisplayItem(name + "." + s), upgrades.getYml().getInt(name + "." + s + ".cost"), UpgradesManager.getCurrency(upgrades.getYml().getString(name + "." + s + ".currency")));
            if (mu.addTier(ut)) continue;
            Bukkit.getLogger().log(Level.WARNING, "Could not load tier: " + s + " at upgrade: " + name);
        }
        BedWars.debug("Registering upgrade: " + name);
        menuContentByName.put(name.toLowerCase(), mu);
        return true;
    }

    private static boolean loadSeparator(String name) {
        if (name == null) {
            return false;
        }
        if (!name.startsWith("separator-")) {
            return false;
        }
        if (upgrades.getYml().get(name) == null) {
            return false;
        }
        if (UpgradesManager.getMenuContent(name) != null) {
            return false;
        }
        MenuSeparator ms = new MenuSeparator(name, UpgradesManager.createDisplayItem(name));
        menuContentByName.put(name.toLowerCase(), ms);
        BedWars.debug("Registering upgrade: " + name);
        return true;
    }

    private static boolean loadTrapSlot(String name) {
        if (name == null) {
            return false;
        }
        if (!name.startsWith("trap-slot-")) {
            return false;
        }
        if (upgrades.getYml().get(name) == null) {
            return false;
        }
        if (UpgradesManager.getMenuContent(name) != null) {
            return false;
        }
        MenuTrapSlot mts = new MenuTrapSlot(name, UpgradesManager.createDisplayItem(name));
        menuContentByName.put(name.toLowerCase(), mts);
        BedWars.debug("Registering upgrade: " + name);
        return true;
    }

    private static boolean loadBaseTrap(String name) {
        if (name == null) {
            return false;
        }
        if (!name.startsWith("base-trap-")) {
            return false;
        }
        if (upgrades.getYml().get(name) == null) {
            return false;
        }
        if (upgrades.getYml().get(name + ".receive") == null) {
            BedWars.debug("Could not load BaseTrap. Receive not set.");
            return false;
        }
        if (upgrades.getYml().get(name + ".display-item") == null) {
            BedWars.debug("Could not load BaseTrap. Display item not set.");
            return false;
        }
        MenuBaseTrap bt = new MenuBaseTrap(name, UpgradesManager.createDisplayItem(name), upgrades.getYml().getInt(name + ".cost"), UpgradesManager.getCurrency(upgrades.getYml().getString(name + ".currency")));
        BedWars.debug("Registering upgrade: " + name);
        menuContentByName.put(name.toLowerCase(), bt);
        return true;
    }

    public static int getMoney(Player player, Material currency) {
        if (currency == Material.AIR) {
            double amount = BedWars.getEconomy().getMoney(player);
            return amount % 2.0 == 0.0 ? (int)amount : (int)(amount - 1.0);
        }
        return BedWars.getAPI().getShopUtil().calculateMoney(player, currency);
    }

    public static Material getCurrency(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        return BedWars.getAPI().getShopUtil().getCurrency(name);
    }

    public static MenuContent getMenuContent(ItemStack item) {
        if (item == null) {
            return null;
        }
        String identifier = BedWars.nms.getCustomData(item);
        if (identifier == null) {
            return null;
        }
        if (identifier.equals("null")) {
            return null;
        }
        if (!identifier.startsWith("MCONT_")) {
            return null;
        }
        if ((identifier = identifier.replaceFirst("MCONT_", "")).isEmpty()) {
            return null;
        }
        return menuContentByName.getOrDefault(identifier.toLowerCase(), null);
    }

    public static MenuContent getMenuContent(String identifier) {
        return menuContentByName.getOrDefault(identifier.toLowerCase(), null);
    }

    public static void setCustomMenuForArena(IArena arena, UpgradesIndex menu) {
        if (!customMenuForArena.containsKey(arena)) {
            customMenuForArena.put(arena, menu);
            BedWars.debug("Registering custom menu for arena: " + arena.getArenaName() + ". Using index: " + menu.getName());
        } else {
            BedWars.debug("Overriding custom menu for arena: " + arena.getArenaName() + ". Using index: " + menu.getName() + " Old index: " + customMenuForArena.get(arena).getName());
            customMenuForArena.replace(arena, menu);
        }
    }

    public static UpgradesIndex getMenuForArena(IArena arena) {
        if (customMenuForArena.containsKey(arena)) {
            return customMenuForArena.get(arena);
        }
        return menuByName.getOrDefault(arena.getGroup().toLowerCase(), menuByName.get("default"));
    }

    private static ItemStack createDisplayItem(String path) {
        Material m;
        try {
            m = Material.valueOf((String)upgrades.getYml().getString(path + ".display-item.material"));
        } catch (Exception e) {
            m = Material.BEDROCK;
        }
        ItemStack i = new ItemStack(m, Integer.parseInt(upgrades.getYml().getString(path + ".display-item.amount")), (short)upgrades.getYml().getInt(path + ".display-item.data"));
        if (upgrades.getYml().getBoolean(path + ".display-item.enchanted")) {
            i.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            ItemMeta im = i.getItemMeta();
            if (im != null) {
                im.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
                i.setItemMeta(im);
            }
        }
        return i;
    }

    public static String getCurrencyMsg(Player p, UpgradeTier ut) {
        String c = "";
        switch (ut.getCurrency()) {
            case IRON_INGOT: {
                c = ut.getCost() == 1 ? Messages.MEANING_IRON_SINGULAR : Messages.MEANING_IRON_PLURAL;
                break;
            }
            case GOLD_INGOT: {
                c = ut.getCost() == 1 ? Messages.MEANING_GOLD_SINGULAR : Messages.MEANING_GOLD_PLURAL;
                break;
            }
            case EMERALD: {
                c = ut.getCost() == 1 ? Messages.MEANING_EMERALD_SINGULAR : Messages.MEANING_EMERALD_PLURAL;
                break;
            }
            case DIAMOND: {
                c = ut.getCost() == 1 ? Messages.MEANING_DIAMOND_SINGULAR : Messages.MEANING_DIAMOND_PLURAL;
                break;
            }
            case AIR: {
                c = ut.getCost() == 1 ? Messages.MEANING_VAULT_SINGULAR : Messages.MEANING_VAULT_PLURAL;
            }
        }
        return Language.getMsg(p, c);
    }

    public static String getCurrencyMsg(Player p, int money, String currency) {
        String c;
        if (currency == null) {
            return Language.getMsg(p, money == 1 ? Messages.MEANING_VAULT_SINGULAR : Messages.MEANING_VAULT_PLURAL);
        }
        switch (currency.toLowerCase()) {
            case "iron": {
                c = money == 1 ? Messages.MEANING_IRON_SINGULAR : Messages.MEANING_IRON_PLURAL;
                break;
            }
            case "gold": {
                c = money == 1 ? Messages.MEANING_GOLD_SINGULAR : Messages.MEANING_GOLD_PLURAL;
                break;
            }
            case "emerald": {
                c = money == 1 ? Messages.MEANING_EMERALD_SINGULAR : Messages.MEANING_EMERALD_PLURAL;
                break;
            }
            case "diamond": {
                c = money == 1 ? Messages.MEANING_DIAMOND_SINGULAR : Messages.MEANING_DIAMOND_PLURAL;
                break;
            }
            default: {
                c = money == 1 ? Messages.MEANING_VAULT_SINGULAR : Messages.MEANING_VAULT_PLURAL;
            }
        }
        return Language.getMsg(p, c);
    }

    public static String getCurrencyMsg(Player p, int money, Material currency) {
        String c;
        switch (currency) {
            case IRON_INGOT: {
                c = money == 1 ? Messages.MEANING_IRON_SINGULAR : Messages.MEANING_IRON_PLURAL;
                break;
            }
            case GOLD_INGOT: {
                c = money == 1 ? Messages.MEANING_GOLD_SINGULAR : Messages.MEANING_GOLD_PLURAL;
                break;
            }
            case EMERALD: {
                c = money == 1 ? Messages.MEANING_EMERALD_SINGULAR : Messages.MEANING_EMERALD_PLURAL;
                break;
            }
            case DIAMOND: {
                c = money == 1 ? Messages.MEANING_DIAMOND_SINGULAR : Messages.MEANING_DIAMOND_PLURAL;
                break;
            }
            default: {
                c = money == 1 ? Messages.MEANING_VAULT_SINGULAR : Messages.MEANING_VAULT_PLURAL;
            }
        }
        return Language.getMsg(p, c);
    }

    public static ChatColor getCurrencyColor(Material currency) {
        switch (currency) {
            case DIAMOND: {
                return ChatColor.AQUA;
            }
            case GOLD_INGOT: {
                return ChatColor.GOLD;
            }
            case IRON_INGOT: {
                return ChatColor.WHITE;
            }
            case EMERALD: {
                return ChatColor.GREEN;
            }
        }
        return ChatColor.DARK_GREEN;
    }

    public static UpgradesConfig getConfiguration() {
        return upgrades;
    }
}

