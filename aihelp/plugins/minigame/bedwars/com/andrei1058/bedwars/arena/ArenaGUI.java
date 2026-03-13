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
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryHolder
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.jetbrains.annotations.NotNull
 */
package com.andrei1058.bedwars.arena;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.configuration.Sounds;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class ArenaGUI {
    private static YamlConfiguration yml = BedWars.config.getYml();
    private static HashMap<UUID, Long> antiCalledTwice = new HashMap();

    public static void refreshInv(Player p, IArena arena, int players) {
        List<Object> arenas;
        if (p == null) {
            return;
        }
        if (p.getOpenInventory() == null) {
            return;
        }
        if (!(p.getOpenInventory().getTopInventory().getHolder() instanceof ArenaSelectorHolder)) {
            return;
        }
        ArenaSelectorHolder ash = (ArenaSelectorHolder)p.getOpenInventory().getTopInventory().getHolder();
        if (ash.getGroup().equalsIgnoreCase("default")) {
            arenas = new ArrayList<IArena>(Arena.getArenas());
        } else {
            arenas = new ArrayList();
            for (IArena a : Arena.getArenas()) {
                if (!a.getGroup().equalsIgnoreCase(ash.getGroup())) continue;
                arenas.add(a);
            }
        }
        arenas = Arena.getSorted(arenas);
        int arenaKey = 0;
        block6: for (Integer slot : ArenaGUI.getUsedSlots()) {
            ItemMeta im;
            String status;
            p.getOpenInventory().getTopInventory().setItem(slot.intValue(), new ItemStack(Material.AIR));
            if (arenaKey >= arenas.size()) continue;
            switch (((IArena)arenas.get(arenaKey)).getStatus()) {
                case waiting: {
                    status = "waiting";
                    break;
                }
                case playing: {
                    status = "playing";
                    break;
                }
                case starting: {
                    status = "starting";
                    break;
                }
                default: {
                    continue block6;
                }
            }
            ItemStack i = BedWars.nms.createItemStack(yml.getString("arena-gui.%path%.material".replace("%path%", status)), 1, (short)yml.getInt("arena-gui.%path%.data".replace("%path%", status)));
            if (yml.getBoolean("arena-gui.%path%.enchanted".replace("%path%", status))) {
                im = i.getItemMeta();
                im.addEnchant(Enchantment.LURE, 1, true);
                im.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
                i.setItemMeta(im);
            }
            im = i.getItemMeta();
            im.setDisplayName(Language.getMsg(p, Messages.ARENA_GUI_ARENA_CONTENT_NAME).replace("{name}", ((IArena)arenas.get(arenaKey)).getDisplayName()).replace("{map_name}", ((IArena)arenas.get(arenaKey)).getArenaName()));
            ArrayList<String> lore = new ArrayList<String>();
            for (String s : Language.getList(p, Messages.ARENA_GUI_ARENA_CONTENT_LORE)) {
                if (s.contains("{group}") && ((IArena)arenas.get(arenaKey)).getGroup().equalsIgnoreCase("default")) continue;
                lore.add(s.replace("{on}", String.valueOf(arena != null ? (arena == arenas.get(arenaKey) ? players : ((IArena)arenas.get(arenaKey)).getPlayers().size()) : ((IArena)arenas.get(arenaKey)).getPlayers().size())).replace("{max}", String.valueOf(((IArena)arenas.get(arenaKey)).getMaxPlayers())).replace("{status}", ((IArena)arenas.get(arenaKey)).getDisplayStatus(Language.getPlayerLanguage(p))).replace("{group}", ((IArena)arenas.get(arenaKey)).getDisplayGroup(p)));
            }
            im.setLore(lore);
            i.setItemMeta(im);
            i = BedWars.nms.addCustomData(i, "arena=" + ((IArena)arenas.get(arenaKey)).getArenaName());
            p.getOpenInventory().getTopInventory().setItem(slot.intValue(), i);
            ++arenaKey;
        }
        p.updateInventory();
    }

    public static void openGui(Player p, String group) {
        if (ArenaGUI.preventCalledTwice(p)) {
            return;
        }
        ArenaGUI.updateCalledTwice(p);
        int size = BedWars.config.getYml().getInt("arena-gui.settings.inv-size");
        if (size % 9 != 0) {
            size = 27;
        }
        if (size > 54) {
            size = 54;
        }
        ArenaSelectorHolder ash = new ArenaSelectorHolder(group);
        Inventory inv = Bukkit.createInventory((InventoryHolder)ash, (int)size, (String)Language.getMsg(p, Messages.ARENA_GUI_INV_NAME));
        String skippedSlotMaterial = BedWars.config.getString("arena-gui.%path%.material".replace("%path%", "skipped-slot"));
        if (!skippedSlotMaterial.equalsIgnoreCase("none") && !skippedSlotMaterial.equalsIgnoreCase("air")) {
            ItemStack i = BedWars.nms.createItemStack(skippedSlotMaterial, 1, (byte)BedWars.config.getInt("arena-gui.%path%.data".replace("%path%", "skipped-slot")));
            i = BedWars.nms.addCustomData(i, "RUNCOMMAND_bw join random");
            ItemMeta im = i.getItemMeta();
            assert (im != null);
            im.setDisplayName(ChatColor.translateAlternateColorCodes((char)'&', (String)Language.getMsg(p, Messages.ARENA_GUI_SKIPPED_ITEM_NAME).replaceAll("\\{serverIp}", BedWars.config.getString("server-ip")).replaceAll("\\{poweredBy}", BedWars.config.getString("powered-by"))));
            ArrayList<String> lore = new ArrayList<String>();
            for (String line : Language.getList(p, Messages.ARENA_GUI_SKIPPED_ITEM_LORE)) {
                line = line.replaceAll("\\{serverIp}", BedWars.config.getString("server-ip")).replaceAll("\\{poweredBy}", BedWars.config.getString("powered-by"));
                lore.add(line);
            }
            if (lore.size() > 0) {
                im.setLore(lore);
            }
            im.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
            i.setItemMeta(im);
            List<Integer> used = ArenaGUI.getUsedSlots();
            for (int x = 0; x < inv.getSize(); ++x) {
                if (used.contains(x)) continue;
                inv.setItem(x, i);
            }
        }
        p.openInventory(inv);
        ArenaGUI.refreshInv(p, null, 0);
        Sounds.playSound("arena-selector-open", p);
    }

    @NotNull
    private static List<Integer> getUsedSlots() {
        ArrayList<Integer> ls = new ArrayList<Integer>();
        for (String useSlot : BedWars.config.getString("arena-gui.settings.use-slots").split(",")) {
            try {
                int slot = Integer.parseInt(useSlot);
                ls.add(slot);
            } catch (Exception exception) {
                // empty catch block
            }
        }
        return ls;
    }

    private static boolean preventCalledTwice(@NotNull Player player) {
        return antiCalledTwice.getOrDefault(player.getUniqueId(), 0L) > System.currentTimeMillis();
    }

    private static void updateCalledTwice(@NotNull Player player) {
        if (antiCalledTwice.containsKey(player.getUniqueId())) {
            antiCalledTwice.replace(player.getUniqueId(), System.currentTimeMillis() + 2000L);
        } else {
            antiCalledTwice.put(player.getUniqueId(), System.currentTimeMillis() + 2000L);
        }
    }

    public static class ArenaSelectorHolder
    implements InventoryHolder {
        private String group;

        public ArenaSelectorHolder(String group) {
            this.group = group;
        }

        public String getGroup() {
            return this.group;
        }

        public Inventory getInventory() {
            return null;
        }
    }
}

