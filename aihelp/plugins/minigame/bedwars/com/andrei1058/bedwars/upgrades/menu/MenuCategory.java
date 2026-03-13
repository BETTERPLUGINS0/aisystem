/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package com.andrei1058.bedwars.upgrades.menu;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.api.upgrades.MenuContent;
import com.andrei1058.bedwars.upgrades.UpgradesManager;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MenuCategory
implements MenuContent {
    private ItemStack displayItem;
    private String name;
    private HashMap<Integer, MenuContent> menuContentBySlot = new HashMap();

    public MenuCategory(String name, ItemStack displayItem) {
        this.name = name;
        this.displayItem = BedWars.nms.addCustomData(displayItem, "MCONT_" + name);
        Language.saveIfNotExists(Messages.UPGRADES_CATEGORY_GUI_NAME_PATH + name.replace("category-", ""), "&8" + name);
        Language.saveIfNotExists(Messages.UPGRADES_CATEGORY_ITEM_NAME_PATH + name.replace("category-", ""), "&cName not set");
        Language.saveIfNotExists(Messages.UPGRADES_CATEGORY_ITEM_LORE_PATH + name.replace("category-", ""), Collections.singletonList("&cLore not set"));
    }

    public boolean addContent(MenuContent content, int slot) {
        if (this.menuContentBySlot.get(slot) != null) {
            return false;
        }
        this.menuContentBySlot.put(slot, content);
        return true;
    }

    @Override
    public ItemStack getDisplayItem(Player player, ITeam team) {
        ItemStack i = new ItemStack(this.displayItem);
        ItemMeta im = i.getItemMeta();
        if (im != null) {
            im.setDisplayName(Language.getMsg(player, Messages.UPGRADES_CATEGORY_ITEM_NAME_PATH + this.name.replace("category-", "")));
            List<String> lore = Language.getList(player, Messages.UPGRADES_CATEGORY_ITEM_LORE_PATH + this.name.replace("category-", ""));
            if (this.name.equalsIgnoreCase("traps")) {
                int queueLimit = UpgradesManager.getConfiguration().getInt(team.getArena().getGroup().toLowerCase() + "-upgrades-settings.trap-queue-limit");
                if (queueLimit == 0) {
                    queueLimit = UpgradesManager.getConfiguration().getInt("default-upgrades-settings.trap-queue-limit");
                }
                if (queueLimit == team.getActiveTraps().size()) {
                    lore.add("");
                    lore.add(Language.getMsg(player, Messages.UPGRADES_TRAP_QUEUE_LIMIT));
                }
            }
            im.setLore(lore);
            i.setItemMeta(im);
        }
        return i;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void onClick(Player player, ClickType clickType, ITeam team) {
        if (this.name.equalsIgnoreCase("category-traps")) {
            int queueLimit = UpgradesManager.getConfiguration().getInt(team.getArena().getGroup().toLowerCase() + "-upgrades-settings.trap-queue-limit");
            if (queueLimit == 0) {
                queueLimit = UpgradesManager.getConfiguration().getInt("default-upgrades-settings.trap-queue-limit");
            }
            if (queueLimit <= team.getActiveTraps().size()) {
                player.sendMessage(Language.getMsg(player, Messages.UPGRADES_TRAP_QUEUE_LIMIT));
                return;
            }
        }
        Inventory inv = Bukkit.createInventory(null, (int)45, (String)Language.getMsg(player, Messages.UPGRADES_CATEGORY_GUI_NAME_PATH + this.name.replace("category-", "")));
        for (Map.Entry<Integer, MenuContent> entry : this.menuContentBySlot.entrySet()) {
            inv.setItem(entry.getKey().intValue(), entry.getValue().getDisplayItem(player, team));
        }
        player.openInventory(inv);
        UpgradesManager.setWatchingUpgrades(player.getUniqueId());
    }
}

