/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryHolder
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package com.andrei1058.bedwars.arena.spectator;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.arena.Arena;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TeleporterGUI {
    public static final String NBT_SPECTATOR_TELEPORTER_GUI_HEAD = "spectatorTeleporterGUIhead_";
    private static HashMap<Player, Inventory> refresh = new HashMap();

    public static void refreshInv(Player p, Inventory inv) {
        if (p.getOpenInventory() == null) {
            return;
        }
        IArena arena = Arena.getArenaByPlayer(p);
        if (arena == null) {
            p.closeInventory();
            return;
        }
        List<Player> players = arena.getPlayers();
        for (int i = 0; i < inv.getSize(); ++i) {
            if (i < players.size()) {
                inv.setItem(i, TeleporterGUI.createHead(players.get(i), p));
                continue;
            }
            inv.setItem(i, new ItemStack(Material.AIR));
        }
    }

    public static void openGUI(Player p) {
        int size;
        IArena arena = Arena.getArenaByPlayer(p);
        if (arena == null) {
            return;
        }
        int playerCount = arena.getPlayers().size();
        int n = size = playerCount % 9 == 0 ? playerCount : (int)Math.ceil((double)playerCount / 9.0) * 9;
        if (size > 54) {
            size = 54;
        }
        Inventory inv = Bukkit.createInventory((InventoryHolder)p, (int)size, (String)Language.getMsg(p, Messages.ARENA_SPECTATOR_TELEPORTER_GUI_NAME));
        TeleporterGUI.refreshInv(p, inv);
        refresh.put(p, inv);
        p.openInventory(inv);
    }

    public static HashMap<Player, Inventory> getRefresh() {
        return refresh;
    }

    public static void refreshAllGUIs() {
        for (Map.Entry<Player, Inventory> e : new HashMap<Player, Inventory>(TeleporterGUI.getRefresh()).entrySet()) {
            TeleporterGUI.refreshInv(e.getKey(), e.getValue());
        }
    }

    private static ItemStack createHead(Player targetPlayer, Player GUIholder) {
        ItemStack i = BedWars.nms.getPlayerHead(targetPlayer, null);
        ItemMeta im = i.getItemMeta();
        assert (im != null);
        IArena currentArena = Arena.getArenaByPlayer(targetPlayer);
        ITeam targetPlayerTeam = currentArena.getTeam(targetPlayer);
        im.setDisplayName(Language.getMsg(GUIholder, Messages.ARENA_SPECTATOR_TELEPORTER_GUI_HEAD_NAME).replace("{vPrefix}", BedWars.getChatSupport().getPrefix(targetPlayer)).replace("{vSuffix}", BedWars.getChatSupport().getSuffix(targetPlayer)).replace("{team}", targetPlayerTeam.getDisplayName(Language.getPlayerLanguage(GUIholder))).replace("{teamColor}", String.valueOf(targetPlayerTeam.getColor().chat())).replace("{player}", targetPlayer.getDisplayName()).replace("{playername}", targetPlayer.getName()));
        ArrayList<String> lore = new ArrayList<String>();
        String health = String.valueOf((double)((int)targetPlayer.getHealth() * 100) / targetPlayer.getHealthScale());
        for (String s : Language.getList(GUIholder, Messages.ARENA_SPECTATOR_TELEPORTER_GUI_HEAD_LORE)) {
            lore.add(s.replace("{health}", health).replace("{food}", String.valueOf(targetPlayer.getFoodLevel())));
        }
        im.setLore(lore);
        i.setItemMeta(im);
        return BedWars.nms.addCustomData(i, NBT_SPECTATOR_TELEPORTER_GUI_HEAD + targetPlayer.getName());
    }

    public static void closeGUI(Player p) {
        if (TeleporterGUI.getRefresh().containsKey(p)) {
            refresh.remove(p);
            p.closeInventory();
        }
    }
}

