/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.event.inventory.InventoryAction
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.event.inventory.InventoryDragEvent
 *  org.bukkit.event.inventory.InventoryOpenEvent
 *  org.bukkit.event.inventory.InventoryType
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.event.server.PluginDisableEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package nl.sbdeveloper.vehiclesplus.libs.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import nl.sbdeveloper.vehiclesplus.libs.inventory.SmartInventory;
import nl.sbdeveloper.vehiclesplus.libs.inventory.content.InventoryContents;
import nl.sbdeveloper.vehiclesplus.libs.inventory.opener.ChestInventoryOpener;
import nl.sbdeveloper.vehiclesplus.libs.inventory.opener.InventoryOpener;
import nl.sbdeveloper.vehiclesplus.libs.inventory.opener.SpecialInventoryOpener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class InventoryManager {
    private JavaPlugin plugin;
    private PluginManager pluginManager;
    private Map<UUID, SmartInventory> inventories;
    private Map<UUID, InventoryContents> contents;
    private List<InventoryOpener> defaultOpeners;
    private List<InventoryOpener> openers;

    public InventoryManager(JavaPlugin javaPlugin) {
        this.plugin = javaPlugin;
        this.pluginManager = Bukkit.getPluginManager();
        this.inventories = new HashMap<UUID, SmartInventory>();
        this.contents = new HashMap<UUID, InventoryContents>();
        this.defaultOpeners = Arrays.asList(new ChestInventoryOpener(), new SpecialInventoryOpener());
        this.openers = new ArrayList<InventoryOpener>();
    }

    public void init() {
        this.pluginManager.registerEvents((Listener)new InvListener(), (Plugin)this.plugin);
        new InvTask().runTaskTimer((Plugin)this.plugin, 1L, 1L);
    }

    public Optional<InventoryOpener> findOpener(InventoryType inventoryType) {
        Optional<InventoryOpener> optional = this.openers.stream().filter(inventoryOpener -> inventoryOpener.supports(inventoryType)).findAny();
        if (!optional.isPresent()) {
            optional = this.defaultOpeners.stream().filter(inventoryOpener -> inventoryOpener.supports(inventoryType)).findAny();
        }
        return optional;
    }

    public void registerOpeners(InventoryOpener ... inventoryOpenerArray) {
        this.openers.addAll(Arrays.asList(inventoryOpenerArray));
    }

    public List<Player> getOpenedPlayers(SmartInventory smartInventory) {
        ArrayList<Player> arrayList = new ArrayList<Player>();
        this.inventories.forEach((uUID, smartInventory2) -> {
            if (smartInventory.equals(smartInventory2)) {
                arrayList.add(Bukkit.getPlayer((UUID)uUID));
            }
        });
        return arrayList;
    }

    public Optional<SmartInventory> getInventory(Player player) {
        return Optional.ofNullable(this.inventories.get(player.getUniqueId()));
    }

    protected void setInventory(Player player, SmartInventory smartInventory) {
        if (smartInventory == null) {
            this.inventories.remove(player.getUniqueId());
        } else {
            this.inventories.put(player.getUniqueId(), smartInventory);
        }
    }

    public Optional<InventoryContents> getContents(Player player) {
        return Optional.ofNullable(this.contents.get(player.getUniqueId()));
    }

    protected void setContents(Player player, InventoryContents inventoryContents) {
        if (inventoryContents == null) {
            this.contents.remove(player.getUniqueId());
        } else {
            this.contents.put(player.getUniqueId(), inventoryContents);
        }
    }

    public void handleInventoryOpenError(SmartInventory smartInventory, Player player, Exception exception) {
        smartInventory.close(player);
        Bukkit.getLogger().log(Level.SEVERE, "Error while opening SmartInventory:", exception);
    }

    public void handleInventoryUpdateError(SmartInventory smartInventory, Player player, Exception exception) {
        smartInventory.close(player);
        Bukkit.getLogger().log(Level.SEVERE, "Error while updating SmartInventory:", exception);
    }

    class InvTask
    extends BukkitRunnable {
        InvTask() {
        }

        public void run() {
            new HashMap<UUID, SmartInventory>(InventoryManager.this.inventories).forEach((uUID, smartInventory) -> {
                Player player = Bukkit.getPlayer((UUID)uUID);
                try {
                    smartInventory.getProvider().update(player, (InventoryContents)InventoryManager.this.contents.get(uUID));
                } catch (Exception exception) {
                    InventoryManager.this.handleInventoryUpdateError((SmartInventory)smartInventory, player, exception);
                }
            });
        }
    }

    class InvListener
    implements Listener {
        InvListener() {
        }

        @EventHandler(priority=EventPriority.LOW)
        public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {
            Player player = (Player)inventoryClickEvent.getWhoClicked();
            if (!InventoryManager.this.inventories.containsKey(player.getUniqueId())) {
                return;
            }
            Inventory inventory = inventoryClickEvent.getClickedInventory();
            if (inventory == player.getOpenInventory().getBottomInventory()) {
                if (inventoryClickEvent.getAction() == InventoryAction.COLLECT_TO_CURSOR || inventoryClickEvent.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                    inventoryClickEvent.setCancelled(true);
                    return;
                }
                if (inventoryClickEvent.getAction() == InventoryAction.NOTHING && inventoryClickEvent.getClick() != ClickType.MIDDLE) {
                    inventoryClickEvent.setCancelled(true);
                    return;
                }
            }
            if (inventory == player.getOpenInventory().getTopInventory()) {
                inventoryClickEvent.setCancelled(true);
                int n = inventoryClickEvent.getSlot() / 9;
                int n2 = inventoryClickEvent.getSlot() % 9;
                if (n < 0 || n2 < 0) {
                    return;
                }
                SmartInventory smartInventory = (SmartInventory)InventoryManager.this.inventories.get(player.getUniqueId());
                if (n >= smartInventory.getRows() || n2 >= smartInventory.getColumns()) {
                    return;
                }
                smartInventory.getListeners().stream().filter(inventoryListener -> inventoryListener.getType() == InventoryClickEvent.class).forEach(inventoryListener -> inventoryListener.accept(inventoryClickEvent));
                ((InventoryContents)InventoryManager.this.contents.get(player.getUniqueId())).get(n, n2).ifPresent(clickableItem -> clickableItem.run(inventoryClickEvent));
                player.updateInventory();
            }
        }

        @EventHandler(priority=EventPriority.LOW)
        public void onInventoryDrag(InventoryDragEvent inventoryDragEvent) {
            Player player = (Player)inventoryDragEvent.getWhoClicked();
            if (!InventoryManager.this.inventories.containsKey(player.getUniqueId())) {
                return;
            }
            SmartInventory smartInventory = (SmartInventory)InventoryManager.this.inventories.get(player.getUniqueId());
            Iterator iterator = inventoryDragEvent.getRawSlots().iterator();
            while (iterator.hasNext()) {
                int n = (Integer)iterator.next();
                if (n >= player.getOpenInventory().getTopInventory().getSize()) continue;
                inventoryDragEvent.setCancelled(true);
                break;
            }
            smartInventory.getListeners().stream().filter(inventoryListener -> inventoryListener.getType() == InventoryDragEvent.class).forEach(inventoryListener -> inventoryListener.accept(inventoryDragEvent));
        }

        @EventHandler(priority=EventPriority.LOW)
        public void onInventoryOpen(InventoryOpenEvent inventoryOpenEvent) {
            Player player = (Player)inventoryOpenEvent.getPlayer();
            if (!InventoryManager.this.inventories.containsKey(player.getUniqueId())) {
                return;
            }
            SmartInventory smartInventory = (SmartInventory)InventoryManager.this.inventories.get(player.getUniqueId());
            smartInventory.getListeners().stream().filter(inventoryListener -> inventoryListener.getType() == InventoryOpenEvent.class).forEach(inventoryListener -> inventoryListener.accept(inventoryOpenEvent));
        }

        @EventHandler(priority=EventPriority.LOW)
        public void onInventoryClose(InventoryCloseEvent inventoryCloseEvent) {
            Player player = (Player)inventoryCloseEvent.getPlayer();
            if (!InventoryManager.this.inventories.containsKey(player.getUniqueId())) {
                return;
            }
            SmartInventory smartInventory = (SmartInventory)InventoryManager.this.inventories.get(player.getUniqueId());
            smartInventory.getListeners().stream().filter(inventoryListener -> inventoryListener.getType() == InventoryCloseEvent.class).forEach(inventoryListener -> inventoryListener.accept(inventoryCloseEvent));
            if (smartInventory.isCloseable()) {
                inventoryCloseEvent.getInventory().clear();
                InventoryManager.this.inventories.remove(player.getUniqueId());
                InventoryManager.this.contents.remove(player.getUniqueId());
            } else {
                Bukkit.getScheduler().runTask((Plugin)InventoryManager.this.plugin, () -> player.openInventory(inventoryCloseEvent.getInventory()));
            }
        }

        @EventHandler(priority=EventPriority.LOW)
        public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
            Player player = playerQuitEvent.getPlayer();
            if (!InventoryManager.this.inventories.containsKey(player.getUniqueId())) {
                return;
            }
            SmartInventory smartInventory = (SmartInventory)InventoryManager.this.inventories.get(player.getUniqueId());
            smartInventory.getListeners().stream().filter(inventoryListener -> inventoryListener.getType() == PlayerQuitEvent.class).forEach(inventoryListener -> inventoryListener.accept(playerQuitEvent));
            InventoryManager.this.inventories.remove(player.getUniqueId());
            InventoryManager.this.contents.remove(player.getUniqueId());
        }

        @EventHandler(priority=EventPriority.LOW)
        public void onPluginDisable(PluginDisableEvent pluginDisableEvent) {
            new HashMap<UUID, SmartInventory>(InventoryManager.this.inventories).forEach((uUID, smartInventory) -> {
                smartInventory.getListeners().stream().filter(inventoryListener -> inventoryListener.getType() == PluginDisableEvent.class).forEach(inventoryListener -> inventoryListener.accept(pluginDisableEvent));
                smartInventory.close(Bukkit.getPlayer((UUID)uUID));
            });
            InventoryManager.this.inventories.clear();
            InventoryManager.this.contents.clear();
        }
    }
}

