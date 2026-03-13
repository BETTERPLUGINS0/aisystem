/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package com.andrei1058.bedwars.upgrades.menu;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.api.upgrades.EnemyBaseEnterTrap;
import com.andrei1058.bedwars.api.upgrades.MenuContent;
import com.andrei1058.bedwars.upgrades.UpgradesManager;
import java.util.ArrayList;
import java.util.Collections;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MenuTrapSlot
implements MenuContent {
    private ItemStack displayItem;
    private String name;
    private int trap;

    public MenuTrapSlot(String name, ItemStack displayItem) {
        this.displayItem = BedWars.nms.addCustomData(displayItem, "MCONT_" + name);
        this.name = name;
        Language.saveIfNotExists(Messages.UPGRADES_TRAP_SLOT_ITEM_NAME_PATH + name.replace("trap-slot-", ""), "&cName not set");
        Language.saveIfNotExists(Messages.UPGRADES_TRAP_SLOT_ITEM_LORE1_PATH + name.replace("trap-slot-", ""), Collections.singletonList("&cLore1 not set"));
        Language.saveIfNotExists(Messages.UPGRADES_TRAP_SLOT_ITEM_LORE2_PATH + name.replace("trap-slot-", ""), Collections.singletonList("&cLore2 not set"));
        this.trap = UpgradesManager.getConfiguration().getInt(name + ".trap");
        if (this.trap < 0) {
            this.trap = 0;
        }
        if (this.trap != 0) {
            --this.trap;
        }
    }

    @Override
    public ItemStack getDisplayItem(Player player, ITeam team) {
        ItemStack i = this.displayItem.clone();
        EnemyBaseEnterTrap ebe = null;
        if (!team.getActiveTraps().isEmpty() && team.getActiveTraps().size() > this.trap) {
            ebe = team.getActiveTraps().get(this.trap);
        }
        if (ebe != null) {
            i = ebe.getItemStack().clone();
        }
        i.setAmount(this.trap + 1);
        ItemMeta im = i.getItemMeta();
        if (im == null) {
            return i;
        }
        im.setDisplayName(Language.getMsg(player, Messages.UPGRADES_TRAP_SLOT_ITEM_NAME_PATH + this.name.replace("trap-slot-", "")).replace("{name}", Language.getMsg(player, ebe == null ? Messages.MEANING_NO_TRAP : ebe.getNameMsgPath())).replace("{color}", Language.getMsg(player, ebe == null ? Messages.FORMAT_UPGRADE_COLOR_CANT_AFFORD : Messages.FORMAT_UPGRADE_COLOR_UNLOCKED)));
        ArrayList<String> lore = new ArrayList<String>();
        if (ebe == null) {
            String curr;
            int cost = UpgradesManager.getConfiguration().getInt(team.getArena().getArenaName().toLowerCase() + "-upgrades-settings.trap-start-price");
            if (cost == 0) {
                cost = UpgradesManager.getConfiguration().getInt("default-upgrades-settings.trap-start-price");
            }
            if ((curr = UpgradesManager.getConfiguration().getString(team.getArena().getArenaName().toLowerCase() + "-upgrades-settings.trap-currency")) == null) {
                curr = UpgradesManager.getConfiguration().getString("default-upgrades-settings.trap-currency");
            }
            String currency = UpgradesManager.getCurrencyMsg(player, cost, curr);
            if (!team.getActiveTraps().isEmpty()) {
                int multiplier = UpgradesManager.getConfiguration().getInt(team.getArena().getArenaName().toLowerCase() + "-upgrades-settings.trap-increment-price");
                if (multiplier == 0) {
                    multiplier = UpgradesManager.getConfiguration().getInt("default-upgrades-settings.trap-increment-price");
                }
                cost += team.getActiveTraps().size() * multiplier;
            }
            for (String s : Language.getList(player, Messages.UPGRADES_TRAP_SLOT_ITEM_LORE1_PATH + this.name.replace("trap-slot-", ""))) {
                lore.add(s.replace("{cost}", String.valueOf(cost)).replace("{currency}", currency));
            }
            lore.add("");
            for (String s : Language.getList(player, Messages.UPGRADES_TRAP_SLOT_ITEM_LORE2_PATH + this.name.replace("trap-slot-", ""))) {
                lore.add(s.replace("{cost}", String.valueOf(cost)).replace("{currency}", currency));
            }
        } else {
            lore.addAll(Language.getList(player, ebe.getLoreMsgPath()));
            lore.addAll(Language.getList(player, Messages.UPGRADES_TRAP_SLOT_ITEM_LORE1_PATH + this.name.replace("trap-slot-", "")));
        }
        im.setLore(lore);
        im.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        i.setItemMeta(im);
        return i;
    }

    @Override
    public void onClick(Player player, ClickType clickType, ITeam team) {
    }

    @Override
    public String getName() {
        return this.name;
    }
}

