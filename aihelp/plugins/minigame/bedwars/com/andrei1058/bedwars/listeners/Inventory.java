/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.event.inventory.InventoryAction
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.event.inventory.InventoryEvent
 *  org.bukkit.event.inventory.InventoryType$SlotType
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.potion.PotionEffectType
 */
package com.andrei1058.bedwars.listeners;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.events.gameplay.GameStateChangeEvent;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.api.server.ServerType;
import com.andrei1058.bedwars.api.server.SetupType;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.SetupSession;
import java.util.Objects;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public class Inventory
implements Listener {
    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        SetupSession ss;
        Player p = (Player)e.getPlayer();
        if (BedWars.nms.getInventoryName((InventoryEvent)e).equalsIgnoreCase(SetupSession.getInvName()) && (ss = SetupSession.getSession(p.getUniqueId())) != null && ss.getSetupType() == null) {
            ss.cancel();
        }
    }

    @EventHandler
    public void onCommandItemClick(InventoryClickEvent e) {
        ItemStack i;
        if (e.getAction() == InventoryAction.HOTBAR_SWAP && e.getClick() == ClickType.NUMBER_KEY && e.getHotbarButton() > -1 && (i = e.getWhoClicked().getInventory().getItem(e.getHotbarButton())) != null && Inventory.isCommandItem(i)) {
            e.setCancelled(true);
            return;
        }
        if (e.getCursor() != null && e.getCursor().getType() != Material.AIR) {
            if (e.getClickedInventory() == null) {
                if (Inventory.isCommandItem(e.getCursor())) {
                    e.getWhoClicked().closeInventory();
                    e.setCancelled(true);
                }
            } else if (e.getClickedInventory().getType() != e.getWhoClicked().getInventory().getType()) {
                if (Inventory.isCommandItem(e.getCursor())) {
                    e.getWhoClicked().closeInventory();
                    e.setCancelled(true);
                }
            } else if (Inventory.isCommandItem(e.getCursor())) {
                e.setCancelled(true);
            }
        }
        if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
            if (e.getClickedInventory() == null) {
                if (Inventory.isCommandItem(e.getCurrentItem())) {
                    e.getWhoClicked().closeInventory();
                    e.setCancelled(true);
                }
            } else if (e.getClickedInventory().getType() != e.getWhoClicked().getInventory().getType()) {
                if (Inventory.isCommandItem(e.getCurrentItem())) {
                    e.getWhoClicked().closeInventory();
                    e.setCancelled(true);
                }
            } else if (Inventory.isCommandItem(e.getCurrentItem())) {
                e.setCancelled(true);
            }
        }
        if (e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY && Inventory.isCommandItem(e.getCurrentItem())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getSlotType() == InventoryType.SlotType.ARMOR && Arena.getArenaByPlayer((Player)e.getWhoClicked()) != null && e.getWhoClicked().hasPotionEffect(PotionEffectType.INVISIBILITY)) {
            e.getWhoClicked().closeInventory();
            for (Player pl : e.getWhoClicked().getWorld().getPlayers()) {
                BedWars.nms.hideArmor((Player)e.getWhoClicked(), pl);
            }
        }
        if (e.getCurrentItem() == null) {
            return;
        }
        if (e.getCurrentItem().getType() == Material.AIR) {
            return;
        }
        Player p = (Player)e.getWhoClicked();
        ItemStack i = e.getCurrentItem();
        IArena a = Arena.getArenaByPlayer(p);
        if (a != null) {
            if (BedWars.nms.getInventoryName((InventoryEvent)e).equals(Language.getMsg(p, Messages.PLAYER_STATS_GUI_INV_NAME).replace("{playername}", p.getName()).replace("{player}", p.getDisplayName()))) {
                e.setCancelled(true);
                return;
            }
            if (e.getSlotType() == InventoryType.SlotType.ARMOR) {
                e.setCancelled(true);
                return;
            }
        }
        if (!i.hasItemMeta()) {
            return;
        }
        if (!i.getItemMeta().hasDisplayName()) {
            return;
        }
        if (BedWars.getServerType() == ServerType.MULTIARENA && e.getWhoClicked().getLocation().getWorld().getName().equalsIgnoreCase(BedWars.getLobbyWorld())) {
            e.setCancelled(true);
        }
        if (SetupSession.isInSetupSession(p.getUniqueId()) && BedWars.nms.getInventoryName((InventoryEvent)e).equalsIgnoreCase(SetupSession.getInvName())) {
            SetupSession ss = SetupSession.getSession(p.getUniqueId());
            if (e.getSlot() == SetupSession.getAdvancedSlot()) {
                Objects.requireNonNull(ss).setSetupType(SetupType.ADVANCED);
            } else if (e.getSlot() == SetupSession.getAssistedSlot()) {
                Objects.requireNonNull(ss).setSetupType(SetupType.ASSISTED);
            }
            if (!Objects.requireNonNull(ss).startSetup()) {
                ss.getPlayer().sendMessage(String.valueOf(ChatColor.RED) + "Could not start setup session. Pleas check the console.");
            }
            p.closeInventory();
            return;
        }
        if (a != null && a.isSpectator(p)) {
            e.setCancelled(true);
            return;
        }
    }

    private static boolean isCommandItem(ItemStack i) {
        String[] customData;
        if (i == null) {
            return false;
        }
        if (i.getType() == Material.AIR) {
            return false;
        }
        if (BedWars.nms.isCustomBedWarsItem(i) && (customData = BedWars.nms.getCustomData(i).split("_")).length >= 2) {
            return customData[0].equals("RUNCOMMAND");
        }
        return false;
    }

    @EventHandler
    public void onGameEnd(GameStateChangeEvent e) {
        if (e.getNewState() != GameState.restarting) {
            return;
        }
        e.getArena().getPlayers().forEach(HumanEntity::closeInventory);
    }
}

