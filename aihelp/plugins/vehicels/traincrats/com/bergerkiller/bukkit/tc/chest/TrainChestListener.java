package com.bergerkiller.bukkit.tc.chest;

import com.bergerkiller.bukkit.common.collections.EntityMap;
import com.bergerkiller.bukkit.common.inventory.CommonItemStack;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.wrappers.HumanHand;
import com.bergerkiller.bukkit.tc.Localization;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.TCListener;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.MinecartMemberStore;
import com.bergerkiller.bukkit.tc.controller.spawnable.SpawnableGroup;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class TrainChestListener implements Listener {
   private static final int INTERACT_TIMEOUT_TICKS = 5;
   private final TrainCarts plugin;
   private final EntityMap<Player, Integer> ticksSinceLastAction = new EntityMap();

   public TrainChestListener(TrainCarts plugin) {
      this.plugin = plugin;
   }

   private boolean spamCheck(Player player) {
      int currentTick = CommonUtil.getServerTicks();

      boolean var4;
      try {
         Integer t = (Integer)this.ticksSinceLastAction.get(player);
         var4 = t == null || currentTick - t >= 5;
      } finally {
         this.ticksSinceLastAction.put(player, currentTick);
      }

      return var4;
   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = false
   )
   public void onPlayerInteract(PlayerInteractEvent event) {
      if (!TrainCarts.isWorldDisabled(event.getPlayer().getWorld())) {
         if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            CommonItemStack heldItem = CommonItemStack.of(HumanHand.getItemInMainHand(event.getPlayer()));
            if (TrainChestItemUtil.isItem(heldItem)) {
               event.setUseInteractedBlock(Result.DENY);
               event.setUseItemInHand(Result.DENY);
               event.setCancelled(true);
               if (this.spamCheck(event.getPlayer())) {
                  if (!Permission.COMMAND_STORAGE_CHEST_USE.has(event.getPlayer())) {
                     Localization.CHEST_NOPERM.message(event.getPlayer(), new String[0]);
                  } else {
                     SpawnableGroup group = TrainChestItemUtil.getSpawnableGroup(this.plugin, heldItem);
                     TrainChestItemUtil.SpawnOptions spawnOptions = new TrainChestItemUtil.SpawnOptions(event.getPlayer());
                     spawnOptions.initialSpeed = TrainChestItemUtil.getSpeed(heldItem);
                     spawnOptions.tryExtendTrains = !event.getPlayer().isSneaking();
                     TrainChestItemUtil.SpawnResult result;
                     if (group == null) {
                        result = TrainChestItemUtil.SpawnResult.FAIL_EMPTY;
                     } else if (!group.checkSpawnPermissions(event.getPlayer())) {
                        result = TrainChestItemUtil.SpawnResult.FAIL_NO_PERM;
                     } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        result = TrainChestItemUtil.spawnAtBlock(group, event.getClickedBlock(), spawnOptions);
                        if (result == TrainChestItemUtil.SpawnResult.FAIL_NORAIL) {
                           result = TrainChestItemUtil.spawnLookingAt(group, event.getPlayer(), event.getPlayer().getEyeLocation(), spawnOptions);
                           if (result == TrainChestItemUtil.SpawnResult.FAIL_NORAIL_LOOK) {
                              result = TrainChestItemUtil.SpawnResult.FAIL_NORAIL;
                           }
                        }
                     } else if (event.getAction() == Action.RIGHT_CLICK_AIR) {
                        result = TrainChestItemUtil.spawnLookingAt(group, event.getPlayer(), event.getPlayer().getEyeLocation(), spawnOptions);
                     } else {
                        result = TrainChestItemUtil.SpawnResult.FAIL_NORAIL_LOOK;
                     }

                     if (result == TrainChestItemUtil.SpawnResult.SUCCESS && TrainChestItemUtil.isFiniteSpawns(heldItem)) {
                        if (TrainChestItemUtil.isLocked(heldItem)) {
                           HumanHand.setItemInMainHand(event.getPlayer(), heldItem.clone().subtractAmount(1).toBukkit());
                        } else {
                           heldItem = heldItem.clone();
                           TrainChestItemUtil.clear(heldItem);
                           HumanHand.setItemInMainHand(event.getPlayer(), heldItem.toBukkit());
                        }
                     }

                     if (result.hasMessage()) {
                        String customSpawnMessage = null;
                        if (result == TrainChestItemUtil.SpawnResult.SUCCESS) {
                           customSpawnMessage = TrainChestItemUtil.getSpawnMessage(heldItem);
                        }

                        if (customSpawnMessage == null) {
                           result.getLocale().message(event.getPlayer(), new String[0]);
                        } else if (!customSpawnMessage.isEmpty()) {
                           event.getPlayer().sendMessage(customSpawnMessage);
                        }
                     }

                     if (result == TrainChestItemUtil.SpawnResult.SUCCESS) {
                        TrainChestItemUtil.playSoundSpawn(event.getPlayer());
                     }

                  }
               }
            }
         }
      }
   }

   @EventHandler(
      priority = EventPriority.LOWEST,
      ignoreCancelled = true
   )
   public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
      CommonItemStack heldItem = CommonItemStack.of(HumanHand.getItemInMainHand(event.getPlayer()));
      if (TrainChestItemUtil.isItem(heldItem)) {
         event.setCancelled(true);
         if (this.spamCheck(event.getPlayer())) {
            if (!Permission.COMMAND_STORAGE_CHEST_USE.has(event.getPlayer())) {
               Localization.CHEST_NOPERM.message(event.getPlayer(), new String[0]);
            } else if (TrainChestItemUtil.isLocked(heldItem)) {
               Localization.CHEST_LOCKED.message(event.getPlayer(), new String[0]);
            } else if (!TrainChestItemUtil.isEmpty(heldItem) && TrainChestItemUtil.isFiniteSpawns(heldItem)) {
               Localization.CHEST_FULL.message(event.getPlayer(), new String[0]);
            } else {
               MinecartMember<?> member = MinecartMemberStore.getFromEntity(event.getRightClicked());
               if (member != null && !member.isUnloaded() && member.getGroup() != null) {
                  if (!member.getProperties().hasOwnership(event.getPlayer())) {
                     Localization.EDIT_NOTOWNED.message(event.getPlayer(), new String[0]);
                  } else {
                     heldItem = heldItem.clone();
                     TrainChestItemUtil.store(heldItem, member.getGroup());
                     HumanHand.setItemInMainHand(event.getPlayer(), heldItem.toBukkit());
                     Localization.CHEST_PICKUP.message(event.getPlayer(), new String[0]);
                     TrainChestItemUtil.playSoundStore(event.getPlayer());
                     if (!event.getPlayer().isSneaking() || TrainChestItemUtil.isFiniteSpawns(heldItem)) {
                        boolean wasCancelled = TCListener.cancelNextDrops;

                        try {
                           TCListener.cancelNextDrops = true;
                           member.getGroup().destroy();
                        } finally {
                           TCListener.cancelNextDrops = wasCancelled;
                        }
                     }

                  }
               }
            }
         }
      }
   }
}
