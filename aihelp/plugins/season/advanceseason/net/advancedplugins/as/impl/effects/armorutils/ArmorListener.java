/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.block.Container
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.event.inventory.InventoryAction
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryDragEvent
 *  org.bukkit.event.inventory.InventoryType
 *  org.bukkit.event.inventory.InventoryType$SlotType
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerItemBreakEvent
 *  org.bukkit.event.player.PlayerRespawnEvent
 *  org.bukkit.inventory.EntityEquipment
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 */
package net.advancedplugins.as.impl.effects.armorutils;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import net.advancedplugins.as.impl.effects.armorutils.ArmorEquipEvent;
import net.advancedplugins.as.impl.effects.armorutils.ArmorType;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.SchedulerUtils;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.HooksHandler;
import net.advancedplugins.as.impl.utils.hooks.plugins.ItemsAdderHook;
import net.advancedplugins.as.impl.utils.nbt.utils.MinecraftVersion;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ArmorListener
implements Listener {
    private final List<String> blockedMaterials = Arrays.asList("DISPENSER", "DROPPER", "DOOR", "TRAP_DOOR", "GATE", "WALL_SIGN", "SIGN_POST", "CHEST", "ENDER_CHEST", "BEACON", "DOUBLE_CHEST", "TRAPPED_CHEST", "ANVIL", "LEVER", "CAULDRON", "FENCE", "CRAFTING_TABLE", "WORKBENCH", "SHULKER", "CAKE");
    private final ArrayDeque<ArmorEquipEvent> eventQueue = new ArrayDeque();

    public ArmorListener() {
        this.runEventQueue();
    }

    private void addToQueue(ArmorEquipEvent armorEquipEvent) {
        ItemStack itemStack = armorEquipEvent.getOldArmorPiece();
        ItemStack itemStack2 = armorEquipEvent.getNewArmorPiece();
        armorEquipEvent.setOldArmorPiece(itemStack != null ? itemStack.clone() : null);
        armorEquipEvent.setNewArmorPiece(itemStack2 != null ? itemStack2.clone() : null);
        this.eventQueue.add(armorEquipEvent);
    }

    private void runEventQueue() {
        SchedulerUtils.runTaskTimer(() -> {
            while (!this.eventQueue.isEmpty()) {
                ItemsAdderHook itemsAdderHook;
                ArmorEquipEvent armorEquipEvent = this.eventQueue.poll();
                ItemStack itemStack = armorEquipEvent.getOldArmorPiece();
                if (armorEquipEvent.getType() == null || armorEquipEvent.getType().getEquipmentSlot() == null) continue;
                ItemStack itemStack2 = armorEquipEvent.getPlayer().getEquipment().getItem(armorEquipEvent.getType().getEquipmentSlot());
                if (armorEquipEvent.getMethod() != ArmorEquipEvent.EquipMethod.HOTBAR && HooksHandler.isEnabled(HookPlugin.ITEMSADDER) && !ASManager.isAir(itemStack) && !ASManager.isAir(itemStack2) && (itemsAdderHook = (ItemsAdderHook)HooksHandler.getHook(HookPlugin.ITEMSADDER)).isCustomItem(itemStack)) {
                    armorEquipEvent.setOldArmorPiece(armorEquipEvent.getNewArmorPiece());
                    armorEquipEvent.setNewArmorPiece(itemStack);
                    itemStack = armorEquipEvent.getOldArmorPiece();
                }
                if (Objects.equals(itemStack, itemStack2) || ASManager.isAir(itemStack) && ASManager.isAir(itemStack2)) continue;
                Bukkit.getPluginManager().callEvent((Event)armorEquipEvent);
            }
        }, 0L, 3L);
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public final void onInventoryClick(InventoryClickEvent inventoryClickEvent) {
        boolean bl = false;
        boolean bl2 = false;
        if (inventoryClickEvent.getClick() == ClickType.SHIFT_LEFT || inventoryClickEvent.getClick() == ClickType.SHIFT_RIGHT) {
            bl = true;
        }
        if (inventoryClickEvent.getClick() == ClickType.NUMBER_KEY) {
            bl2 = true;
        }
        if (inventoryClickEvent.getSlotType() != InventoryType.SlotType.ARMOR && inventoryClickEvent.getSlotType() != InventoryType.SlotType.QUICKBAR && inventoryClickEvent.getSlotType() != InventoryType.SlotType.CONTAINER) {
            return;
        }
        if (inventoryClickEvent.getClickedInventory() != null && inventoryClickEvent.getClickedInventory().getType() != InventoryType.PLAYER) {
            return;
        }
        if (inventoryClickEvent.getInventory().getType() != InventoryType.CRAFTING && inventoryClickEvent.getInventory().getType() != InventoryType.PLAYER) {
            return;
        }
        if (!(inventoryClickEvent.getWhoClicked() instanceof Player)) {
            return;
        }
        ArmorType armorType = ArmorType.matchType(bl ? inventoryClickEvent.getCurrentItem() : inventoryClickEvent.getCursor());
        if (inventoryClickEvent.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD && armorType == null) {
            armorType = ArmorType.matchType(inventoryClickEvent.getCurrentItem());
        }
        if (inventoryClickEvent.getAction() == InventoryAction.SWAP_WITH_CURSOR) {
            armorType = ArmorType.matchType(inventoryClickEvent.getCursor());
        }
        if (!bl && armorType != null && inventoryClickEvent.getRawSlot() != armorType.getSlot()) {
            return;
        }
        if (bl) {
            Object object;
            Object object2;
            armorType = ArmorType.matchType(inventoryClickEvent.getCurrentItem());
            if (armorType == null && HooksHandler.isEnabled(HookPlugin.ITEMSADDER)) {
                ItemStack itemStack;
                ItemStack itemStack2;
                ItemsAdderHook itemsAdderHook = (ItemsAdderHook)HooksHandler.getHook(HookPlugin.ITEMSADDER);
                if (itemsAdderHook.isCustomItem((ItemStack)(object2 = inventoryClickEvent.getWhoClicked().getEquipment().getHelmet()))) {
                    this.addToQueue(new ArmorEquipEvent((Player)inventoryClickEvent.getWhoClicked(), ArmorEquipEvent.EquipMethod.SHIFT_CLICK, ArmorType.HELMET, null, (ItemStack)object2));
                }
                if (itemsAdderHook.isCustomItem((ItemStack)(object = inventoryClickEvent.getWhoClicked().getEquipment().getChestplate()))) {
                    this.addToQueue(new ArmorEquipEvent((Player)inventoryClickEvent.getWhoClicked(), ArmorEquipEvent.EquipMethod.SHIFT_CLICK, ArmorType.CHESTPLATE, null, (ItemStack)object));
                }
                if (itemsAdderHook.isCustomItem(itemStack2 = inventoryClickEvent.getWhoClicked().getEquipment().getLeggings())) {
                    this.addToQueue(new ArmorEquipEvent((Player)inventoryClickEvent.getWhoClicked(), ArmorEquipEvent.EquipMethod.SHIFT_CLICK, ArmorType.LEGGINGS, null, itemStack2));
                }
                if (itemsAdderHook.isCustomItem(itemStack = inventoryClickEvent.getWhoClicked().getEquipment().getBoots())) {
                    this.addToQueue(new ArmorEquipEvent((Player)inventoryClickEvent.getWhoClicked(), ArmorEquipEvent.EquipMethod.SHIFT_CLICK, ArmorType.BOOTS, null, itemStack));
                }
            }
            if (armorType != null) {
                boolean bl3 = true;
                if (inventoryClickEvent.getRawSlot() == armorType.getSlot()) {
                    if (inventoryClickEvent.getWhoClicked().getInventory().firstEmpty() == -1) {
                        inventoryClickEvent.setCancelled(true);
                        return;
                    }
                    bl3 = false;
                }
                if (armorType == ArmorType.HELMET && bl3 == ASManager.isAir(inventoryClickEvent.getWhoClicked().getInventory().getHelmet()) || armorType == ArmorType.CHESTPLATE && bl3 == ASManager.isAir(inventoryClickEvent.getWhoClicked().getInventory().getChestplate()) || armorType == ArmorType.LEGGINGS && bl3 == ASManager.isAir(inventoryClickEvent.getWhoClicked().getInventory().getLeggings()) || armorType == ArmorType.BOOTS && bl3 == ASManager.isAir(inventoryClickEvent.getWhoClicked().getInventory().getBoots())) {
                    if (bl3) {
                        object2 = inventoryClickEvent.getCurrentItem().clone();
                        object = new ArmorEquipEvent((Player)inventoryClickEvent.getWhoClicked(), ArmorEquipEvent.EquipMethod.SHIFT_CLICK, armorType, null, (ItemStack)object2);
                        this.addToQueue((ArmorEquipEvent)((Object)object));
                    } else {
                        object2 = new ArmorEquipEvent((Player)inventoryClickEvent.getWhoClicked(), ArmorEquipEvent.EquipMethod.SHIFT_CLICK, armorType, inventoryClickEvent.getCurrentItem(), null);
                        this.addToQueue((ArmorEquipEvent)((Object)object2));
                        if (((ArmorEquipEvent)((Object)object2)).isCancelled()) {
                            inventoryClickEvent.setCancelled(true);
                        }
                    }
                }
            }
        } else {
            Object object;
            if (MinecraftVersion.getVersionNumber() >= 1160 && inventoryClickEvent.getAction() == InventoryAction.HOTBAR_SWAP && inventoryClickEvent.getClick() == ClickType.SWAP_OFFHAND) {
                EntityEquipment entityEquipment = inventoryClickEvent.getWhoClicked().getEquipment();
                if (entityEquipment == null) {
                    return;
                }
                ItemStack itemStack = null;
                ItemStack itemStack3 = entityEquipment.getItemInOffHand();
                switch (inventoryClickEvent.getRawSlot()) {
                    case 5: {
                        itemStack = entityEquipment.getHelmet();
                        armorType = ArmorType.HELMET;
                        break;
                    }
                    case 6: {
                        itemStack = entityEquipment.getChestplate();
                        armorType = ArmorType.CHESTPLATE;
                        break;
                    }
                    case 7: {
                        itemStack = entityEquipment.getLeggings();
                        armorType = ArmorType.LEGGINGS;
                        break;
                    }
                    case 8: {
                        itemStack = entityEquipment.getBoots();
                        armorType = ArmorType.BOOTS;
                        break;
                    }
                    case 45: {
                        return;
                    }
                }
                if (itemStack == null) {
                    itemStack = new ItemStack(Material.AIR);
                }
                ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent((Player)inventoryClickEvent.getWhoClicked(), ArmorEquipEvent.EquipMethod.OFFHAND_SWAP, armorType, itemStack, itemStack3);
                this.addToQueue(armorEquipEvent);
                if (armorEquipEvent.isCancelled()) {
                    inventoryClickEvent.setCancelled(true);
                }
                return;
            }
            if (inventoryClickEvent.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD) {
                ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent((Player)inventoryClickEvent.getWhoClicked(), ArmorEquipEvent.EquipMethod.HOTBAR_SWAP, armorType, inventoryClickEvent.getCurrentItem(), null);
                this.addToQueue(armorEquipEvent);
                if (armorEquipEvent.isCancelled()) {
                    inventoryClickEvent.setCancelled(true);
                }
                return;
            }
            ItemStack itemStack = inventoryClickEvent.getCursor();
            ItemStack itemStack4 = inventoryClickEvent.getCurrentItem();
            if (bl2) {
                if (inventoryClickEvent.getClickedInventory().getType() == InventoryType.PLAYER) {
                    object = inventoryClickEvent.getClickedInventory().getItem(inventoryClickEvent.getHotbarButton());
                    if (!ASManager.isAir(object)) {
                        armorType = ArmorType.matchType(object);
                        itemStack = object;
                        itemStack4 = inventoryClickEvent.getClickedInventory().getItem(inventoryClickEvent.getSlot());
                    } else {
                        armorType = ArmorType.matchType(!ASManager.isAir(inventoryClickEvent.getCurrentItem()) ? inventoryClickEvent.getCurrentItem() : inventoryClickEvent.getCursor());
                    }
                }
            } else if (ASManager.isAir(inventoryClickEvent.getCursor()) && !ASManager.isAir(inventoryClickEvent.getCurrentItem())) {
                armorType = ArmorType.matchType(inventoryClickEvent.getCurrentItem());
            }
            if (armorType != null && inventoryClickEvent.getRawSlot() == armorType.getSlot()) {
                object = ArmorEquipEvent.EquipMethod.PICK_DROP;
                if (inventoryClickEvent.getAction() == InventoryAction.HOTBAR_SWAP || bl2) {
                    object = ArmorEquipEvent.EquipMethod.HOTBAR_SWAP;
                }
                ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent((Player)inventoryClickEvent.getWhoClicked(), (ArmorEquipEvent.EquipMethod)((Object)object), armorType, itemStack4, itemStack);
                this.addToQueue(armorEquipEvent);
                if (armorEquipEvent.isCancelled()) {
                    inventoryClickEvent.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void playerInteractEvent(PlayerInteractEvent playerInteractEvent) {
        if (playerInteractEvent.getAction() == Action.PHYSICAL) {
            return;
        }
        if (playerInteractEvent.getAction() == Action.RIGHT_CLICK_AIR || playerInteractEvent.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (playerInteractEvent.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (playerInteractEvent.getClickedBlock().getType().isInteractable() || playerInteractEvent.isCancelled()) {
                    if (!(playerInteractEvent.getClickedBlock().getState() instanceof Container) || playerInteractEvent.getPlayer().isSneaking()) {
                        ItemStack itemStack = playerInteractEvent.getItem();
                        ArmorType armorType = ArmorType.getClosest((LivingEntity)playerInteractEvent.getPlayer(), itemStack);
                        if (armorType == null) {
                            return;
                        }
                        ItemStack itemStack2 = armorType.get((LivingEntity)playerInteractEvent.getPlayer());
                        ArmorEquipEvent string = new ArmorEquipEvent(playerInteractEvent.getPlayer(), ArmorEquipEvent.EquipMethod.HOTBAR_SWAP, armorType, itemStack2, itemStack);
                        this.addToQueue(string);
                    }
                    return;
                }
                if (playerInteractEvent.getClickedBlock() != null) {
                    Material material = playerInteractEvent.getClickedBlock().getType();
                    for (String object2 : this.blockedMaterials) {
                        if (!material.name().contains(object2)) continue;
                        return;
                    }
                }
            }
            if (playerInteractEvent.isCancelled() && playerInteractEvent.getAction() != Action.RIGHT_CLICK_AIR) {
                return;
            }
            if (playerInteractEvent.getItem() != null) {
                boolean bl = playerInteractEvent.getItem().getType().name().endsWith("HEAD") || playerInteractEvent.getItem().getType().name().endsWith("SKULL") && playerInteractEvent.getItem().hasItemMeta();
            } else {
                boolean bl = false;
            }
            if (playerInteractEvent.getItem() != null && playerInteractEvent.getItem().getType().isBlock() && playerInteractEvent.getPlayer().getTargetBlock(null, 5).getType().isSolid()) {
                return;
            }
            Player player = playerInteractEvent.getPlayer();
            ArmorType armorType = ArmorType.matchType(playerInteractEvent.getItem());
            if (armorType != null) {
                String string = playerInteractEvent.getItem().getType().name();
                if (string.contains("HEAD") || string.contains("SKULL")) {
                    return;
                }
                ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(player, ArmorEquipEvent.EquipMethod.HOTBAR, armorType, player.getInventory().getItem(armorType.getEquipmentSlot()), playerInteractEvent.getItem());
                this.addToQueue(armorEquipEvent);
                if (armorEquipEvent.isCancelled()) {
                    playerInteractEvent.setCancelled(true);
                    player.updateInventory();
                }
            }
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void inventoryDrag(InventoryDragEvent inventoryDragEvent) {
        ArmorType armorType = ArmorType.matchType(inventoryDragEvent.getOldCursor());
        if (inventoryDragEvent.getRawSlots().isEmpty()) {
            return;
        }
        if (!(inventoryDragEvent.getInventory() instanceof PlayerInventory)) {
            return;
        }
        if (armorType != null && armorType.getSlot() == inventoryDragEvent.getRawSlots().stream().findFirst().orElse(0).intValue()) {
            ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent((Player)inventoryDragEvent.getWhoClicked(), ArmorEquipEvent.EquipMethod.DRAG, armorType, null, inventoryDragEvent.getOldCursor());
            this.addToQueue(armorEquipEvent);
            if (armorEquipEvent.isCancelled()) {
                inventoryDragEvent.setResult(Event.Result.DENY);
                inventoryDragEvent.setCancelled(true);
            }
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void itemBreakEvent(PlayerItemBreakEvent playerItemBreakEvent) {
        ArmorType armorType = ArmorType.matchType(playerItemBreakEvent.getBrokenItem());
        if (armorType != null) {
            Player player = playerItemBreakEvent.getPlayer();
            ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(player, ArmorEquipEvent.EquipMethod.BROKE, armorType, playerItemBreakEvent.getBrokenItem(), null);
            Bukkit.getPluginManager().callEvent((Event)armorEquipEvent);
            if (armorEquipEvent.isCancelled()) {
                ItemStack itemStack = playerItemBreakEvent.getBrokenItem().clone();
                itemStack.setAmount(1);
                itemStack.setDurability((short)(itemStack.getDurability() - 1));
                switch (armorType) {
                    case HELMET: {
                        player.getInventory().setHelmet(itemStack);
                        break;
                    }
                    case CHESTPLATE: {
                        player.getInventory().setChestplate(itemStack);
                        break;
                    }
                    case LEGGINGS: {
                        player.getInventory().setLeggings(itemStack);
                        break;
                    }
                    case BOOTS: {
                        player.getInventory().setBoots(itemStack);
                    }
                }
            }
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void playerDeathEvent(PlayerRespawnEvent playerRespawnEvent) {
        Player player = playerRespawnEvent.getPlayer();
        ArmorType.getArmorContents((LivingEntity)player).forEach((armorType, itemStack) -> {
            if (!ASManager.isValid(itemStack)) {
                return;
            }
            Bukkit.getPluginManager().callEvent((Event)new ArmorEquipEvent(player, ArmorEquipEvent.EquipMethod.DEATH, (ArmorType)((Object)armorType), (ItemStack)itemStack, null));
        });
    }
}

