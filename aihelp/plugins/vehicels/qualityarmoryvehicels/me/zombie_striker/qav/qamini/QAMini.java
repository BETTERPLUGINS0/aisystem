/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerPickupItemEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package me.zombie_striker.qav.qamini;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.MessagesConfig;
import me.zombie_striker.qav.api.QualityArmoryVehicles;
import me.zombie_striker.qav.customitemmanager.AbstractItem;
import me.zombie_striker.qav.customitemmanager.CustomItemManager;
import me.zombie_striker.qav.customitemmanager.MaterialStorage;
import me.zombie_striker.qav.util.xseries.reflection.XReflection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class QAMini
implements Listener {
    private static final String CALCTEXT = ChatColor.BLACK + "qa:";
    public static List<MaterialStorage> registeredItems = new ArrayList<MaterialStorage>();
    public static List<String> namesToBypass = new ArrayList<String>();
    public static List<UUID> resourcepackReq = new ArrayList<UUID>();
    public static HashMap<UUID, Long> sentResourcepack = new HashMap();
    public static boolean sendOnJoin = true;
    public static boolean sendTitleOnJoin = true;
    public static boolean shouldSend = true;
    public static boolean overrideURL = false;
    public static boolean kickIfDeny = true;
    public static boolean verboseLogging = false;
    public static String S_ITEM_VARIENTS_NEW = "Variant";

    public static boolean isVersionHigherThan(int n, int n2) {
        return XReflection.supports(n2);
    }

    public static boolean isSolid(Block block, Location location) {
        Material material = block.getType();
        if (material.name().endsWith("CARPET")) {
            return false;
        }
        if (material.name().contains("LEAVE")) {
            return true;
        }
        if (material.name().contains("SLAB") || material.name().contains("STEP")) {
            return !(location.getY() - (double)location.getBlockY() > 0.5 && block.getData() == 0 || location.getY() - (double)location.getBlockY() <= 0.5 && block.getData() == 1);
        }
        if (material.name().contains("BED_") || material.name().contains("_BED") || material.name().contains("DAYLIGHT_DETECTOR")) {
            return !(location.getY() - (double)location.getBlockY() > 0.5);
        }
        if (material.name().contains("GLASS")) {
            return true;
        }
        if (material.isOccluding()) {
            return true;
        }
        if (material.name().contains("STAIR")) {
            if (block.getData() < 4 && location.getY() - (double)location.getBlockY() < 0.5) {
                return true;
            }
            if (block.getData() >= 4 && location.getY() - (double)location.getBlockY() > 0.5) {
                return true;
            }
            switch (block.getData()) {
                case 0: 
                case 4: {
                    return location.getX() - (0.5 + (double)location.getBlockX()) > 0.0;
                }
                case 1: 
                case 5: {
                    return location.getX() - (0.5 + (double)location.getBlockX()) < 0.0;
                }
                case 2: 
                case 6: {
                    return location.getZ() - (0.5 + (double)location.getBlockZ()) > 0.0;
                }
                case 3: 
                case 7: {
                    return location.getZ() - (0.5 + (double)location.getBlockZ()) < 0.0;
                }
            }
        }
        return false;
    }

    public static void DEBUG(String string) {
        Main.DEBUG("[QAVehicles]" + string);
    }

    public static int findSafeSpot(ItemStack itemStack, boolean bl, boolean bl2) {
        return QAMini.findSafeSpot(itemStack.getType(), itemStack.getDurability(), bl, bl2);
    }

    public static int findSafeSpot(Material material, int n, boolean bl, boolean bl2) {
        int n2 = n;
        if (bl2) {
            ArrayList<Integer> arrayList = new ArrayList<Integer>();
            for (MaterialStorage materialStorage : registeredItems) {
                if (materialStorage.getMat() != material || materialStorage.getData() > n2 != bl) continue;
                arrayList.add(materialStorage.getData());
            }
            if (bl) {
                for (int i = n2 + 1; i < n2 + 1000; ++i) {
                    if (arrayList.contains(i)) continue;
                    return i;
                }
            } else {
                for (int i = n2 - 1; i > 0; --i) {
                    if (arrayList.contains(i)) continue;
                    return i;
                }
            }
            return 0;
        }
        for (MaterialStorage materialStorage : registeredItems) {
            if (materialStorage.getMat() != material || materialStorage.getData() > n2 != bl) continue;
            n2 = materialStorage.getData();
        }
        return n2;
    }

    public static int getCalculatedExtraDurib(ItemStack itemStack) {
        if (!itemStack.hasItemMeta() || !itemStack.getItemMeta().hasLore() || Objects.requireNonNull(itemStack.getItemMeta().getLore()).isEmpty()) {
            return -1;
        }
        List list = itemStack.getItemMeta().getLore();
        for (String string : list) {
            if (!string.startsWith(CALCTEXT)) continue;
            return Integer.parseInt(string.split(CALCTEXT)[1]);
        }
        return -1;
    }

    public static ItemStack addCalulatedExtraDurib(ItemStack itemStack, int n) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        ArrayList<String> arrayList = itemMeta.getLore();
        if (arrayList == null) {
            arrayList = new ArrayList<String>();
        } else if (QAMini.getCalculatedExtraDurib(itemStack) != -1) {
            itemStack = QAMini.removeCalculatedExtra(itemStack);
        }
        arrayList.add(CALCTEXT + n);
        itemMeta.setLore(arrayList);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack decrementCalculatedExtra(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        List list = itemStack.getItemMeta().getLore();
        for (int i = 0; i < (list != null ? list.size() : 0); ++i) {
            if (!((String)list.get(i)).startsWith(CALCTEXT)) continue;
            list.set(i, CALCTEXT + "" + (Integer.parseInt(((String)list.get(i)).split(CALCTEXT)[1]) - 1));
        }
        itemMeta.setLore(list);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack removeCalculatedExtra(ItemStack itemStack) {
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore()) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            List list = itemStack.getItemMeta().getLore();
            for (int i = 0; i < (list != null ? list.size() : 0); ++i) {
                if (!((String)list.get(i)).startsWith(CALCTEXT)) continue;
                list.remove(i);
            }
            itemMeta.setLore(list);
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

    public static boolean isCustomItemNextId(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }
        try {
            if (CustomItemManager.getItemType("vehicles") instanceof AbstractItem) {
                return false;
            }
        } catch (Error | Exception throwable) {
            // empty catch block
        }
        ArrayList<MaterialStorage> arrayList = new ArrayList<MaterialStorage>();
        arrayList.addAll(registeredItems);
        for (MaterialStorage materialStorage : arrayList) {
            if (materialStorage.getMat() != itemStack.getType() || materialStorage.getData() != itemStack.getDurability() + 1 || materialStorage.hasVariant()) continue;
            return true;
        }
        return false;
    }

    public static void sendResourcepack(final Player player, final boolean bl) {
        if (namesToBypass.contains(player.getName()) || resourcepackReq.contains(player.getUniqueId())) {
            return;
        }
        new BukkitRunnable(){

            public void run() {
                if (namesToBypass.contains(player.getName())) {
                    resourcepackReq.add(player.getUniqueId());
                    return;
                }
                if (bl) {
                    try {
                        player.sendTitle(MessagesConfig.RESOURCEPACK_TITLE, MessagesConfig.RESOURCEPACK_SUBTITLE);
                    } catch (Error error) {
                        player.sendMessage(MessagesConfig.RESOURCEPACK_TITLE);
                        player.sendMessage(MessagesConfig.RESOURCEPACK_SUBTITLE);
                    }
                }
                if (MessagesConfig.RESOURCEPACK_CRASH.length() > 2) {
                    player.sendMessage(Main.prefix + MessagesConfig.RESOURCEPACK_CRASH);
                }
                new BukkitRunnable(){

                    public void run() {
                        try {
                            player.setResourcePack(CustomItemManager.getResourcepack(player));
                            if (!QAMini.isVersionHigherThan(1, 9)) {
                                resourcepackReq.add(player.getUniqueId());
                                sentResourcepack.put(player.getUniqueId(), System.currentTimeMillis());
                            }
                            resourcepackReq.add(player.getUniqueId());
                        } catch (Exception exception) {
                            // empty catch block
                        }
                    }
                }.runTaskLater((Plugin)QualityArmoryVehicles.getPlugin(), (long)(20 * (bl ? 1 : 5)));
            }
        }.runTaskLater((Plugin)QualityArmoryVehicles.getPlugin(), 0L);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent blockBreakEvent) {
        if (blockBreakEvent.isCancelled()) {
            return;
        }
        blockBreakEvent.getPlayer().getItemInHand();
        if (QualityArmoryVehicles.isVehicleByItem(blockBreakEvent.getPlayer().getItemInHand())) {
            blockBreakEvent.setCancelled(true);
            return;
        }
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onBlockBreakMonitor(final BlockBreakEvent blockBreakEvent) {
        if (blockBreakEvent.isCancelled()) {
            return;
        }
        if (blockBreakEvent.getPlayer().getItemInHand() != null && !blockBreakEvent.getPlayer().getItemInHand().getType().equals((Object)Material.AIR)) {
            if (CustomItemManager.isUsingCustomData()) {
                return;
            }
            int n = QAMini.getCalculatedExtraDurib(blockBreakEvent.getPlayer().getItemInHand());
            if (n != -1) {
                ItemStack itemStack = blockBreakEvent.getPlayer().getItemInHand();
                blockBreakEvent.getBlock().breakNaturally(itemStack);
                blockBreakEvent.setCancelled(true);
                final ItemStack itemStack2 = n > 0 ? QAMini.decrementCalculatedExtra(itemStack) : QAMini.removeCalculatedExtra(itemStack);
                new BukkitRunnable(){

                    public void run() {
                        blockBreakEvent.getPlayer().setItemInHand(itemStack2);
                    }
                }.runTaskLater((Plugin)QualityArmoryVehicles.getPlugin(), 1L);
            }
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent playerPickupItemEvent) {
        if (playerPickupItemEvent.isCancelled()) {
            return;
        }
        if (QualityArmoryVehicles.isVehicleByItem(playerPickupItemEvent.getItem().getItemStack()) && shouldSend && !namesToBypass.contains(playerPickupItemEvent.getPlayer().getName()) && !resourcepackReq.contains(playerPickupItemEvent.getPlayer().getUniqueId())) {
            QAMini.sendResourcepack(playerPickupItemEvent.getPlayer(), true);
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onClickMONITOR(PlayerInteractEvent playerInteractEvent) {
        if (playerInteractEvent.getPlayer().getItemInHand() != null && !QualityArmoryVehicles.isVehicleByItem(playerInteractEvent.getPlayer().getItemInHand())) {
            ItemStack itemStack;
            int n;
            QAMini.DEBUG("Item is not any valid item - mainhand");
            if (!CustomItemManager.isUsingCustomData() && QAMini.isCustomItemNextId(playerInteractEvent.getPlayer().getItemInHand())) {
                QAMini.DEBUG("A player is using a non-gun item, but may reach the textures of one!");
                n = QAMini.findSafeSpot(playerInteractEvent.getPlayer().getItemInHand(), true, overrideURL) + (overrideURL ? 0 : 3);
                QAMini.DEBUG("Safe Durib= " + n + "! ORG " + playerInteractEvent.getPlayer().getItemInHand().getDurability());
                itemStack = playerInteractEvent.getPlayer().getItemInHand();
                itemStack.setDurability((short)n);
                itemStack = QAMini.addCalulatedExtraDurib(itemStack, n - playerInteractEvent.getPlayer().getItemInHand().getDurability());
                playerInteractEvent.getPlayer().setItemInHand(itemStack);
            }
            if (playerInteractEvent.getPlayer().getInventory().getItemInOffHand() != null && !QualityArmoryVehicles.isVehicleByItem(playerInteractEvent.getPlayer().getInventory().getItemInOffHand())) {
                QAMini.DEBUG("Item is not any valid item - offhand");
                if (QAMini.isCustomItemNextId(playerInteractEvent.getPlayer().getInventory().getItemInOffHand())) {
                    QAMini.DEBUG("A player is using a non-gun item, but may reach the textures of one!");
                    n = QAMini.findSafeSpot(playerInteractEvent.getPlayer().getInventory().getItemInOffHand(), true, overrideURL) + (overrideURL ? 0 : 3);
                    QAMini.DEBUG("Safe Durib= " + n + "! ORG " + playerInteractEvent.getPlayer().getInventory().getItemInOffHand().getDurability());
                    itemStack = playerInteractEvent.getPlayer().getInventory().getItemInOffHand();
                    itemStack.setDurability((short)n);
                    itemStack = QAMini.addCalulatedExtraDurib(itemStack, n - playerInteractEvent.getPlayer().getInventory().getItemInOffHand().getDurability());
                    playerInteractEvent.getPlayer().getInventory().setItemInOffHand(itemStack);
                }
            }
        }
    }

    @EventHandler
    public void onClick(final PlayerInteractEvent playerInteractEvent) {
        ItemStack itemStack;
        ItemStack itemStack2;
        int n;
        QAMini.DEBUG("InteractEvent Called");
        try {
            if (QualityArmoryVehicles.isVehicleByItem(playerInteractEvent.getPlayer().getItemInHand()) && !playerInteractEvent.getPlayer().getItemInHand().getItemMeta().isUnbreakable()) {
                QAMini.DEBUG("A player is using a breakable item that reached being a gun!");
                n = QAMini.findSafeSpot(playerInteractEvent.getPlayer().getItemInHand(), true, overrideURL) + (overrideURL ? 0 : 3);
                QAMini.DEBUG("Safe Durib= " + n + "! ORG " + playerInteractEvent.getPlayer().getItemInHand().getDurability());
                itemStack2 = playerInteractEvent.getPlayer().getItemInHand();
                itemStack2.setDurability((short)n);
                playerInteractEvent.getPlayer().setItemInHand(itemStack2);
            }
        } catch (Error | Exception throwable) {
            // empty catch block
        }
        try {
            if (QualityArmoryVehicles.isVehicleByItem(playerInteractEvent.getPlayer().getInventory().getItemInOffHand()) && !playerInteractEvent.getPlayer().getInventory().getItemInOffHand().getItemMeta().isUnbreakable()) {
                QAMini.DEBUG("A player is using a breakable item that reached being a gun!");
                n = QAMini.findSafeSpot(playerInteractEvent.getPlayer().getInventory().getItemInOffHand(), true, overrideURL) + (overrideURL ? 0 : 3);
                QAMini.DEBUG("Safe Durib= " + n + "! ORG " + playerInteractEvent.getPlayer().getInventory().getItemInOffHand().getDurability());
                itemStack2 = playerInteractEvent.getPlayer().getInventory().getItemInOffHand();
                itemStack2.setDurability((short)n);
                playerInteractEvent.getPlayer().getInventory().setItemInOffHand(itemStack2);
            }
        } catch (Error | Exception throwable) {
            // empty catch block
        }
        if (!(QualityArmoryVehicles.isVehicleByItem(playerInteractEvent.getPlayer().getItemInHand()) || (itemStack = playerInteractEvent.getPlayer().getInventory().getItemInOffHand()) != null && QualityArmoryVehicles.isVehicleByItem(itemStack))) {
            return;
        }
        if (kickIfDeny && sentResourcepack.containsKey(playerInteractEvent.getPlayer().getUniqueId()) && System.currentTimeMillis() - sentResourcepack.get(playerInteractEvent.getPlayer().getUniqueId()) >= 3000L) {
            playerInteractEvent.setCancelled(true);
            playerInteractEvent.getPlayer().kickPlayer(ChatColor.translateAlternateColorCodes((char)'&', (String)"&c You have been kicked because you did not accept the resourcepack. \n&f If you want to rejoin the server, edit the server entry and set \"Resourcepack Prompts\" to \"Accept\" or \"Prompt\"'"));
            return;
        }
        if (playerInteractEvent.getItem() != null) {
            final ItemStack itemStack3 = playerInteractEvent.getItem();
            final int n2 = playerInteractEvent.getPlayer().getInventory().getHeldItemSlot();
            if (!QAMini.isVersionHigherThan(1, 9)) {
                ItemStack itemStack4 = null;
                try {
                    itemStack4 = playerInteractEvent.getPlayer().getInventory().getItemInOffHand();
                } catch (Error | Exception throwable) {
                    // empty catch block
                }
                final ItemStack itemStack5 = itemStack4;
                new BukkitRunnable(){

                    public void run() {
                        if (itemStack3.getDurability() != playerInteractEvent.getPlayer().getItemInHand().getDurability() && n2 == playerInteractEvent.getPlayer().getInventory().getHeldItemSlot() && playerInteractEvent.getPlayer().getItemInHand() != null && playerInteractEvent.getPlayer().getItemInHand().getType() == itemStack3.getType()) {
                            try {
                                if (itemStack5 != null && itemStack5.getDurability() == playerInteractEvent.getPlayer().getItemInHand().getDurability()) {
                                    return;
                                }
                            } catch (Error | Exception throwable) {
                                // empty catch block
                            }
                            playerInteractEvent.getPlayer().setItemInHand(itemStack3);
                            QAMini.DEBUG("The item in the player's hand changed! Origin " + itemStack3.getDurability() + " New " + playerInteractEvent.getPlayer().getItemInHand().getDurability());
                        }
                    }
                }.runTaskLater((Plugin)QualityArmoryVehicles.getPlugin(), 0L);
            }
            if (shouldSend && !resourcepackReq.contains(playerInteractEvent.getPlayer().getUniqueId())) {
                QAMini.DEBUG("Player does not have resourcepack!");
                QAMini.sendResourcepack(playerInteractEvent.getPlayer(), true);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent) {
        resourcepackReq.remove(playerQuitEvent.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent playerJoinEvent) {
        if (Bukkit.getVersion().contains("1.7")) {
            Bukkit.broadcastMessage((String)(Main.prefix + " QualityArmory does not support versions older than 1.9, and may crash clients"));
            Bukkit.broadcastMessage((String)"Since there is no reason to stay on outdated updates, (1.7 and 1.8 has quite a number of exploits) update your server.");
            if (shouldSend) {
                shouldSend = false;
                Bukkit.broadcastMessage((String)(Main.prefix + ChatColor.RED + " Disabling resourcepack."));
            }
        }
        if (sendOnJoin) {
            QAMini.sendResourcepack(playerJoinEvent.getPlayer(), sendTitleOnJoin);
        } else {
            for (ItemStack itemStack : playerJoinEvent.getPlayer().getInventory().getContents()) {
                if (itemStack == null || !QualityArmoryVehicles.isVehicleByItem(itemStack)) continue;
                if (!shouldSend || resourcepackReq.contains(playerJoinEvent.getPlayer().getUniqueId())) break;
                new BukkitRunnable(){

                    public void run() {
                        QAMini.sendResourcepack(playerJoinEvent.getPlayer(), false);
                    }
                }.runTaskLater((Plugin)QualityArmoryVehicles.getPlugin(), 0L);
                break;
            }
        }
    }
}

