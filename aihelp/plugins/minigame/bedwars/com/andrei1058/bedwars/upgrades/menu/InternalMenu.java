/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 */
package com.andrei1058.bedwars.upgrades.menu;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.api.upgrades.EnemyBaseEnterTrap;
import com.andrei1058.bedwars.api.upgrades.MenuContent;
import com.andrei1058.bedwars.api.upgrades.TeamUpgrade;
import com.andrei1058.bedwars.api.upgrades.UpgradesIndex;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.upgrades.UpgradesManager;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InternalMenu
implements UpgradesIndex {
    private String name;
    private HashMap<Integer, MenuContent> menuContentBySlot = new HashMap();

    public InternalMenu(String groupName) {
        this.name = groupName.toLowerCase();
        Language.saveIfNotExists(Messages.UPGRADES_MENU_GUI_NAME_PATH + groupName.toLowerCase(), "&8Upgrades & Traps");
    }

    @Override
    public void open(Player player) {
        IArena a = Arena.getArenaByPlayer(player);
        if (a == null) {
            return;
        }
        if (!a.isPlayer(player)) {
            return;
        }
        ITeam team = a.getTeam(player);
        if (team == null) {
            return;
        }
        if (!BedWars.getAPI().getArenaUtil().isPlaying(player)) {
            return;
        }
        Inventory inv = Bukkit.createInventory(null, (int)45, (String)Language.getMsg(player, Messages.UPGRADES_MENU_GUI_NAME_PATH + this.name));
        for (Map.Entry<Integer, MenuContent> entry : this.menuContentBySlot.entrySet()) {
            inv.setItem(entry.getKey().intValue(), entry.getValue().getDisplayItem(player, team));
        }
        player.openInventory(inv);
        UpgradesManager.setWatchingUpgrades(player.getUniqueId());
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean addContent(MenuContent content, int slot) {
        if (this.menuContentBySlot.get(slot) != null) {
            return false;
        }
        this.menuContentBySlot.put(slot, content);
        return true;
    }

    @Override
    public int countTiers() {
        int count = 0;
        for (MenuContent content : this.menuContentBySlot.values()) {
            if (!(content instanceof TeamUpgrade) || content instanceof EnemyBaseEnterTrap) continue;
            TeamUpgrade tu = (TeamUpgrade)((Object)content);
            count += tu.getTierCount();
        }
        return count;
    }

    @Override
    public ImmutableMap<Integer, MenuContent> getMenuContentBySlot() {
        return ImmutableMap.copyOf(this.menuContentBySlot);
    }
}

