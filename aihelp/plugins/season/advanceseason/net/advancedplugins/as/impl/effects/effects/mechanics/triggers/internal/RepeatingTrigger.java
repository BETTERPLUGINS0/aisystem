/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.player.PlayerChangedWorldEvent
 *  org.bukkit.event.player.PlayerDropItemEvent
 *  org.bukkit.event.player.PlayerItemHeldEvent
 *  org.bukkit.event.player.PlayerPickupItemEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.effects.effects.abilities.AdvancedAbility;
import net.advancedplugins.as.impl.effects.effects.actions.ActionExecution;
import net.advancedplugins.as.impl.effects.effects.actions.AdvancedTrigger;
import net.advancedplugins.as.impl.effects.effects.actions.utils.RollItemType;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.UserRepeaters;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class RepeatingTrigger
extends AdvancedTrigger {
    private static final HashMap<UUID, UserRepeaters> repeaters = new HashMap();
    public static final Cache<UUID, Map<String, Long>> COOLDOWN_QUEUE = CacheBuilder.newBuilder().expireAfterWrite(10L, TimeUnit.MINUTES).build();
    private static RepeatingTrigger trigger;

    public RepeatingTrigger() {
        super("REPEATING");
        this.setDescription("Repeating activation of effects");
        trigger = this;
    }

    public void activate(LivingEntity livingEntity, RollItemType rollItemType, ItemStack itemStack, Event event) {
        if (!this.isEnabled()) {
            return;
        }
        EffectsHandler.debug("[REPEATING] Starting check for: " + String.valueOf(livingEntity.getType()));
        if (itemStack == null) {
            return;
        }
        ActionExecution actionExecution = this.executionBuilder().setAttacker(livingEntity).setAttackerMain(true).setEvent(event).setItemType(rollItemType).setItem(itemStack).asRepeating().setSkipCooldown(true).skipChances().skipConditions().build();
        actionExecution.build();
        EffectsHandler.debug("[REPEATING] Found effects for " + String.valueOf(livingEntity.getType()) + " " + String.valueOf(itemStack.getType()) + ": " + String.valueOf(actionExecution.getEffects()));
        if (actionExecution.getEffects().isEmpty()) {
            return;
        }
        AdvancedAbility advancedAbility = actionExecution.getEffects().getFirst();
        if (!advancedAbility.getTypes().contains(this.getTriggerName())) {
            return;
        }
        EffectsHandler.debug("Adding repeating item: " + String.valueOf(itemStack.getType()) + " " + advancedAbility.getName() + ": s" + advancedAbility.getRepeatingDelay());
        this.addRepeatingItem(livingEntity.getUniqueId(), rollItemType, actionExecution.getEffects(), event, itemStack);
    }

    public void stopAll(LivingEntity livingEntity) {
        UserRepeaters userRepeaters = RepeatingTrigger.getRepeaters().get(livingEntity.getUniqueId());
        if (userRepeaters == null) {
            return;
        }
        userRepeaters.itemRunnables.values().stream().flatMap(Collection::stream).forEach(BukkitTask::cancel);
        userRepeaters.itemRunnables.clear();
    }

    public void stop(LivingEntity livingEntity, RollItemType rollItemType, Event event) {
        UserRepeaters userRepeaters = RepeatingTrigger.getRepeaters().get(livingEntity.getUniqueId());
        if (userRepeaters == null) {
            return;
        }
        List<BukkitTask> list = userRepeaters.itemRunnables.get(rollItemType.getSlot());
        if (list == null || list.isEmpty()) {
            return;
        }
        list.forEach(BukkitTask::cancel);
        list.clear();
        userRepeaters.itemRunnables.remove(rollItemType.getSlot());
    }

    private void addRepeatingItem(UUID uUID2, final RollItemType rollItemType, LinkedList<AdvancedAbility> linkedList, final Event event, final ItemStack itemStack) {
        UserRepeaters userRepeaters = RepeatingTrigger.getRepeaters().computeIfAbsent(uUID2, uUID -> new UserRepeaters());
        Player player = Bukkit.getPlayer((UUID)uUID2);
        if (player == null) {
            player = Bukkit.getEntity((UUID)uUID2);
        }
        if (!(player instanceof LivingEntity)) {
            return;
        }
        final LivingEntity livingEntity = (LivingEntity)player;
        if (livingEntity.getEquipment() == null) {
            return;
        }
        Map<String, Long> map = COOLDOWN_QUEUE.getIfPresent(uUID2);
        ArrayList<BukkitTask> arrayList = new ArrayList<BukkitTask>();
        for (final AdvancedAbility advancedAbility : linkedList) {
            Long l = null;
            if (map != null && map.containsKey(advancedAbility.getNameNoLevel())) {
                if (map.get(advancedAbility.getNameNoLevel()) <= System.currentTimeMillis()) {
                    map.remove(advancedAbility.getNameNoLevel());
                } else {
                    l = (map.get(advancedAbility.getNameNoLevel()) - System.currentTimeMillis()) / 50L;
                }
            }
            if (map == null || !map.containsKey(advancedAbility.getNameNoLevel())) {
                COOLDOWN_QUEUE.get(uUID2, HashMap::new).put(advancedAbility.getNameNoLevel(), System.currentTimeMillis() + (long)advancedAbility.getRepeatingDelay() * 1000L);
            }
            long l2 = l != null ? l : (advancedAbility.isRepeatingInstantApply() ? 0L : (long)advancedAbility.getRepeatingDelay() * 20L);
            BukkitTask bukkitTask = new BukkitRunnable(){

                public void run() {
                    if (!ASManager.itemStackEquals(itemStack, livingEntity.getEquipment().getItem(rollItemType.getSlot()), false)) {
                        this.cancel();
                        return;
                    }
                    RepeatingTrigger.this.executionBuilder().setAttacker(livingEntity).setAttackerMain(true).setEvent(event).setItemType(rollItemType).setItem(itemStack).asRepeating().only(advancedAbility).buildAndExecute();
                }
            }.runTaskTimer((Plugin)EffectsHandler.getInstance(), l2, (long)advancedAbility.getRepeatingDelay() * 20L);
            arrayList.add(bukkitTask);
        }
        userRepeaters.itemRunnables.put(rollItemType.getSlot(), arrayList);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent) {
        this.stopAll((LivingEntity)playerQuitEvent.getPlayer());
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onWorldChange(PlayerChangedWorldEvent playerChangedWorldEvent) {
        this.stopAll((LivingEntity)playerChangedWorldEvent.getPlayer());
    }

    @EventHandler(ignoreCancelled=true)
    public void onItemChange(PlayerItemHeldEvent playerItemHeldEvent) {
        Player player = playerItemHeldEvent.getPlayer();
        ItemStack itemStack = player.getInventory().getItem(playerItemHeldEvent.getPreviousSlot());
        ItemStack itemStack2 = player.getInventory().getItem(playerItemHeldEvent.getNewSlot());
        RollItemType rollItemType = RollItemType.getHand(player, itemStack);
        RollItemType rollItemType2 = RollItemType.getHand(player, itemStack2);
        if (rollItemType != null) {
            this.stop((LivingEntity)player, rollItemType, (Event)playerItemHeldEvent);
        }
        if (rollItemType2 != null) {
            this.activate((LivingEntity)player, rollItemType2, itemStack2, (Event)playerItemHeldEvent);
        }
    }

    @EventHandler(ignoreCancelled=true)
    public void onItemDrop(PlayerDropItemEvent playerDropItemEvent) {
        Player player = playerDropItemEvent.getPlayer();
        ItemStack itemStack = playerDropItemEvent.getItemDrop().getItemStack();
        RollItemType rollItemType = RollItemType.getHand(player, itemStack);
        this.stop((LivingEntity)player, rollItemType, (Event)playerDropItemEvent);
    }

    @EventHandler(ignoreCancelled=true)
    public void onItemPickUp(PlayerPickupItemEvent playerPickupItemEvent) {
        ItemStack itemStack;
        Player player = playerPickupItemEvent.getPlayer();
        RollItemType rollItemType = RollItemType.getHand(player, itemStack = playerPickupItemEvent.getItem().getItemStack());
        if (rollItemType == null) {
            return;
        }
        this.activate((LivingEntity)player, rollItemType, itemStack, (Event)playerPickupItemEvent);
    }

    public static HashMap<UUID, UserRepeaters> getRepeaters() {
        return repeaters;
    }

    public static RepeatingTrigger getTrigger() {
        return trigger;
    }
}

