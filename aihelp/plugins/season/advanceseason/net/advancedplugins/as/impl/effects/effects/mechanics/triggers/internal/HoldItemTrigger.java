/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.event.inventory.InventoryOpenEvent
 *  org.bukkit.event.inventory.InventoryType$SlotType
 *  org.bukkit.event.player.PlayerDropItemEvent
 *  org.bukkit.event.player.PlayerItemBreakEvent
 *  org.bukkit.event.player.PlayerItemHeldEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerPickupItemEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.event.player.PlayerSwapHandItemsEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import net.advancedplugins.as.impl.effects.effects.actions.AdvancedTrigger;
import net.advancedplugins.as.impl.effects.effects.actions.utils.RollItemType;
import net.advancedplugins.as.impl.effects.effects.actions.utils.StackItem;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.Pair;
import net.advancedplugins.as.impl.utils.SchedulerUtils;
import net.advancedplugins.as.impl.utils.nbt.utils.MinecraftVersion;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class HoldItemTrigger
extends AdvancedTrigger {
    private static final HashSet<UUID> processQueue = new HashSet();
    private static HoldItemTrigger holdItemTrigger;
    private static Map<UUID, Pair<ItemStack, ItemStack>> openInvCache;

    public HoldItemTrigger() {
        super("HELD");
        this.setDescription("Active when person is holding an item");
        holdItemTrigger = this;
    }

    public void updateAll(LivingEntity livingEntity) {
        this.executeCheck(livingEntity, livingEntity.getEquipment().getItemInMainHand(), null);
        this.executeCheck(livingEntity, null, livingEntity.getEquipment().getItemInMainHand());
        this.executeOffhandCheck(livingEntity, livingEntity.getEquipment().getItemInOffHand(), null);
        this.executeOffhandCheck(livingEntity, null, livingEntity.getEquipment().getItemInOffHand());
    }

    public void executeCheck(LivingEntity livingEntity, ItemStack itemStack, ItemStack itemStack2) {
        if (!this.isEnabled()) {
            return;
        }
        this.executeCheck(livingEntity, itemStack, itemStack2, false);
    }

    public void executeOffhandCheck(LivingEntity livingEntity, ItemStack itemStack, ItemStack itemStack2) {
        if (!this.isEnabled()) {
            return;
        }
        this.executeCheck(livingEntity, itemStack, itemStack2, true);
    }

    private void executeCheck(LivingEntity livingEntity, ItemStack itemStack, ItemStack itemStack2, boolean bl) {
        if (livingEntity == null) {
            return;
        }
        double d = livingEntity.getHealth();
        boolean bl2 = this.runCheck(itemStack, livingEntity, bl, true);
        boolean bl3 = this.runCheck(itemStack2, livingEntity, bl, false);
        if (bl2 || bl3) {
            // empty if block
        }
    }

    public boolean runCheck(ItemStack itemStack, LivingEntity livingEntity, boolean bl, boolean bl2) {
        if (!ASManager.isValid(itemStack)) {
            return false;
        }
        RollItemType rollItemType = bl ? RollItemType.OFFHAND : RollItemType.HAND;
        this.executionBuilder().processVariables("%is offhand%;" + bl).setAttacker(livingEntity).setAttackerMain(true).setEvent(null).setItemType(rollItemType).setStackItem(new StackItem(itemStack, rollItemType)).setItem(itemStack).setRemoval(bl2).buildAndExecute();
        return true;
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void itemBreakEvent(PlayerItemBreakEvent playerItemBreakEvent) {
        Player player = playerItemBreakEvent.getPlayer();
        if (player.isDead() || !player.isValid()) {
            return;
        }
        ItemStack itemStack = playerItemBreakEvent.getBrokenItem();
        if (player.getItemInHand().equals((Object)itemStack)) {
            this.executeCheck((LivingEntity)player, itemStack, new ItemStack(Material.AIR));
        } else if (player.getInventory().getItemInOffHand().equals((Object)itemStack)) {
            this.executeOffhandCheck((LivingEntity)player, itemStack, new ItemStack(Material.AIR));
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onInvClick(InventoryClickEvent inventoryClickEvent) {
        Player player = (Player)inventoryClickEvent.getWhoClicked();
        int n = inventoryClickEvent.getSlot();
        int n2 = player.getInventory().getHeldItemSlot();
        ItemStack itemStack = inventoryClickEvent.getCurrentItem();
        ItemStack itemStack2 = inventoryClickEvent.getCursor();
        switch (inventoryClickEvent.getAction()) {
            case HOTBAR_SWAP: {
                if (n2 == inventoryClickEvent.getHotbarButton() && inventoryClickEvent.getHotbarButton() != -1) {
                    this.executeCheck((LivingEntity)player, player.getInventory().getItem(inventoryClickEvent.getHotbarButton()), itemStack);
                    break;
                }
                if (n == 40 && MinecraftVersion.getVersionNumber() >= 1160 && inventoryClickEvent.getClick() == ClickType.SWAP_OFFHAND) {
                    return;
                }
                if (MinecraftVersion.getVersionNumber() < 1160 || inventoryClickEvent.getClick() != ClickType.SWAP_OFFHAND) break;
                this.executeOffhandCheck((LivingEntity)player, player.getInventory().getItemInOffHand(), itemStack);
                break;
            }
            case HOTBAR_MOVE_AND_READD: {
                if (inventoryClickEvent.getHotbarButton() == -1) break;
                this.executeCheck((LivingEntity)player, player.getInventory().getItem(inventoryClickEvent.getHotbarButton()), itemStack);
                break;
            }
            case SWAP_WITH_CURSOR: {
                if (n2 != inventoryClickEvent.getHotbarButton() || inventoryClickEvent.getHotbarButton() == -1) break;
                this.executeCheck((LivingEntity)player, player.getInventory().getItem(inventoryClickEvent.getHotbarButton()), itemStack);
                break;
            }
            case MOVE_TO_OTHER_INVENTORY: {
                if (!(inventoryClickEvent.getInventory() instanceof PlayerInventory) && n2 != n) {
                    SchedulerUtils.runTaskLater(() -> this.executeCheck((LivingEntity)player, null, player.getItemInHand()));
                    break;
                }
                if (n2 != n) break;
                this.executeCheck((LivingEntity)player, itemStack, null);
            }
        }
        if (itemStack == null) {
            if (itemStack2 == null || n != n2) {
                return;
            }
            if (inventoryClickEvent.getSlotType().equals((Object)InventoryType.SlotType.QUICKBAR) && inventoryClickEvent.getHotbarButton() == -1) {
                this.executeCheck((LivingEntity)player, null, itemStack2);
            } else {
                this.executeCheck((LivingEntity)player, itemStack2, null);
            }
            return;
        }
        if (!(inventoryClickEvent.getClickedInventory() instanceof PlayerInventory)) {
            return;
        }
        if (n == n2) {
            this.executeCheck((LivingEntity)player, itemStack, itemStack2);
        }
        if (n == 40) {
            ItemStack itemStack3 = itemStack2.clone();
            ItemStack itemStack4 = player.getInventory().getItemInOffHand().clone();
            SchedulerUtils.runTaskLater(() -> {
                if (!itemStack3.equals((Object)player.getInventory().getItemInOffHand())) {
                    return;
                }
                this.executeOffhandCheck((LivingEntity)player, itemStack4, itemStack3);
            });
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onDrop(PlayerDropItemEvent playerDropItemEvent) {
        Player player = playerDropItemEvent.getPlayer();
        ItemStack itemStack = playerDropItemEvent.getItemDrop().getItemStack();
        ItemStack itemStack2 = player.getItemInHand();
        this.executeCheck((LivingEntity)player, itemStack, itemStack2);
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onPickup(PlayerPickupItemEvent playerPickupItemEvent) {
        int n;
        Player player = playerPickupItemEvent.getPlayer();
        int n2 = player.getInventory().firstEmpty();
        if (n2 == (n = player.getInventory().getHeldItemSlot())) {
            this.executeCheck((LivingEntity)player, null, playerPickupItemEvent.getItem().getItemStack());
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onItemSwitchEvent(PlayerItemHeldEvent playerItemHeldEvent) {
        Player player = playerItemHeldEvent.getPlayer();
        ItemStack itemStack = player.getInventory().getItem(playerItemHeldEvent.getPreviousSlot());
        ItemStack itemStack2 = player.getInventory().getItem(playerItemHeldEvent.getNewSlot());
        this.executeCheck((LivingEntity)player, itemStack, itemStack2);
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onSwapHand(PlayerSwapHandItemsEvent playerSwapHandItemsEvent) {
        Player player = playerSwapHandItemsEvent.getPlayer();
        ItemStack itemStack = playerSwapHandItemsEvent.getMainHandItem();
        ItemStack itemStack2 = playerSwapHandItemsEvent.getOffHandItem();
        this.executeCheck((LivingEntity)player, player.getInventory().getItemInMainHand(), itemStack);
        this.executeOffhandCheck((LivingEntity)player, player.getInventory().getItemInOffHand(), itemStack2);
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onInventoryOpen(InventoryOpenEvent inventoryOpenEvent) {
        HumanEntity humanEntity = inventoryOpenEvent.getPlayer();
        if (humanEntity instanceof Player) {
            Player player = (Player)humanEntity;
            humanEntity = player.getInventory();
            openInvCache.put(player.getUniqueId(), new Pair<ItemStack, ItemStack>(humanEntity.getItemInMainHand().clone(), humanEntity.getItemInOffHand().clone()));
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onInventoryClose(InventoryCloseEvent inventoryCloseEvent) {
        Player player;
        Object object = inventoryCloseEvent.getPlayer();
        if (object instanceof Player && (object = openInvCache.get((player = (Player)object).getUniqueId())) != null) {
            this.executeCheck((LivingEntity)player, (ItemStack)((Pair)object).getKey(), player.getInventory().getItemInMainHand());
            this.executeOffhandCheck((LivingEntity)player, (ItemStack)((Pair)object).getValue(), player.getInventory().getItemInOffHand());
            openInvCache.remove(player.getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        this.runCheck(player.getItemInHand(), (LivingEntity)player, false, true);
        this.runCheck(player.getInventory().getItemInOffHand(), (LivingEntity)player, true, true);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
        SchedulerUtils.runTaskLater(() -> {
            Player player = playerJoinEvent.getPlayer();
            this.runCheck(player.getItemInHand(), (LivingEntity)player, false, false);
            this.runCheck(player.getInventory().getItemInOffHand(), (LivingEntity)player, true, false);
        }, 20L);
    }

    public static HoldItemTrigger getHoldItemTrigger() {
        return holdItemTrigger;
    }

    static {
        openInvCache = new HashMap<UUID, Pair<ItemStack, ItemStack>>();
    }
}

