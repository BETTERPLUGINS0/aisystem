package com.nisovin.shopkeepers.shopobjects.citizens;

import com.nisovin.shopkeepers.api.ShopkeepersPlugin;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.debug.DebugOptions;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.citizensnpcs.api.event.CitizensReloadEvent;
import net.citizensnpcs.api.event.NPCAddTraitEvent;
import net.citizensnpcs.api.event.NPCDespawnEvent;
import net.citizensnpcs.api.event.NPCRemoveEvent;
import net.citizensnpcs.api.event.NPCRemoveTraitEvent;
import net.citizensnpcs.api.event.NPCSpawnEvent;
import net.citizensnpcs.api.event.NPCTeleportEvent;
import net.citizensnpcs.api.event.NPCTraitCommandAttachEvent;
import net.citizensnpcs.api.event.PlayerCreateNPCEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.checker.nullness.qual.Nullable;

class CitizensListener implements Listener {
   private final CitizensShops citizensShops;
   private final CitizensListener.PendingTraitState pendingTraitAddition;

   CitizensListener(ShopkeepersPlugin plugin, CitizensShops citizensShops) {
      assert plugin != null && citizensShops != null;

      this.citizensShops = citizensShops;
      this.pendingTraitAddition = new CitizensListener.PendingTraitState(this, plugin) {
         protected void handleTrait(CitizensShopkeeperTrait trait, @Nullable Player player) {
            trait.onTraitAdded(player);
         }
      };
   }

   void onEnable() {
   }

   void onDisable() {
      this.pendingTraitAddition.reset();
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   void onPlayerCreateNPC(PlayerCreateNPCEvent event) {
      NPC npc = (NPC)Unsafe.assertNonNull(event.getNPC());
      Player creator = (Player)Unsafe.assertNonNull(event.getCreator());
      this.pendingTraitAddition.updateLastPlayer(npc, creator);
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   void onTraitAdded(NPCAddTraitEvent event) {
      NPC npc = event.getNPC();

      assert npc != null;

      Trait eventTrait = event.getTrait();
      Class<? extends Trait> traitClass = eventTrait.getClass();
      Trait trait = (Trait)Unsafe.nullable(npc.getTraitNullable(traitClass));
      if (trait != null) {
         if (trait == eventTrait) {
            if (trait instanceof CitizensShopkeeperTrait) {
               this.pendingTraitAddition.updatePendingTrait(npc, (CitizensShopkeeperTrait)trait);
            }

         }
      }
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   void onTraitAddedByPlayer(NPCTraitCommandAttachEvent event) {
      if (event.getCommandSender() instanceof Player) {
         Player player = (Player)Unsafe.castNonNull(event.getCommandSender());
         NPC npc = event.getNPC();
         Class<? extends Trait> traitClass = (Class)Unsafe.castNonNull(event.getTraitClass());
         Trait trait = (Trait)Unsafe.nullable(npc.getTraitNullable(traitClass));
         if (trait != null) {
            if (trait instanceof CitizensShopkeeperTrait) {
               assert trait == this.pendingTraitAddition.pendingTrait;

               this.pendingTraitAddition.updateLastPlayer(npc, player);
            }

         }
      }
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   void onTraitRemoved(NPCRemoveTraitEvent event) {
      this.pendingTraitAddition.reset();
      if (event.getTrait() instanceof CitizensShopkeeperTrait) {
         CitizensShopkeeperTrait shopkeeperTrait = (CitizensShopkeeperTrait)event.getTrait();
         shopkeeperTrait.onTraitDeleted((Player)null);
      }

   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   void onNPCRemoved(NPCRemoveEvent event) {
      this.pendingTraitAddition.reset();
      NPC npc = event.getNPC();
      CitizensShopkeeperTrait shopkeeperTrait = (CitizensShopkeeperTrait)Unsafe.cast(npc.getTraitNullable(CitizensShopkeeperTrait.class));
      if (shopkeeperTrait != null) {
         shopkeeperTrait.onTraitDeleted((Player)null);
      } else {
         List<? extends Shopkeeper> shopkeepers = this.citizensShops.getShopkeepers(npc);
         if (!shopkeepers.isEmpty()) {
            (new ArrayList(shopkeepers)).forEach((shopkeeper) -> {
               assert shopkeeper.getShopObject() instanceof SKCitizensShopObject;

               ((SKCitizensShopObject)shopkeeper.getShopObject()).onNPCDeleted((Player)null);
            });
         }
      }

   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   void onCitizensReloaded(CitizensReloadEvent event) {
      this.citizensShops.onCitizensReloaded();
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   void onNPCSpawned(NPCSpawnEvent event) {
      NPC npc = event.getNPC();

      assert npc != null;

      Shopkeeper shopkeeper = this.citizensShops.getShopkeeper(npc);
      if (shopkeeper != null) {
         Log.debug(DebugOptions.regularTickActivities, shopkeeper.getLogPrefix() + "Citizens NPC has been spawned.");
         SKCitizensShopObject shopObject = (SKCitizensShopObject)shopkeeper.getShopObject();
         Entity entity = npc.getEntity();

         assert entity != null;

         shopObject.setEntity(entity);
      }
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   void onNPCDepawn(NPCDespawnEvent event) {
      NPC npc = event.getNPC();

      assert npc != null;

      Shopkeeper shopkeeper = this.citizensShops.getShopkeeper(npc);
      if (shopkeeper != null) {
         Log.debug(DebugOptions.regularTickActivities, shopkeeper.getLogPrefix() + "Citizens NPC is despawned.");
         SKCitizensShopObject shopObject = (SKCitizensShopObject)shopkeeper.getShopObject();
         shopObject.setEntity((Entity)null);
      }
   }

   @EventHandler
   void onNPCTeleport(NPCTeleportEvent event) {
      NPC npc = event.getNPC();

      assert npc != null;

      Location toLocation = event.getTo();

      assert toLocation != null;

      this.updateShopkeeperLocations(npc, toLocation);
   }

   private void updateShopkeeperLocations(NPC npc, Location toLocation) {
      this.citizensShops.getShopkeepers(npc).forEach((shopkeeper) -> {
         assert shopkeeper.getShopObject() instanceof SKCitizensShopObject;

         SKCitizensShopObject shopObject = (SKCitizensShopObject)shopkeeper.getShopObject();
         shopObject.onNpcTeleport(toLocation);
      });
   }

   private abstract static class PendingTraitState {
      private final ShopkeepersPlugin plugin;
      @Nullable
      private UUID lastNPCId = null;
      @Nullable
      private UUID lastPlayerId = null;
      @Nullable
      private CitizensShopkeeperTrait pendingTrait = null;
      @Nullable
      private BukkitTask pendingTraitTask = null;

      PendingTraitState(ShopkeepersPlugin plugin) {
         assert plugin != null;

         this.plugin = plugin;
      }

      private void updateLastNPC(NPC npc) {
         assert npc != null;

         UUID npcId = npc.getUniqueId();
         if (this.lastNPCId != null && !this.lastNPCId.equals(npcId)) {
            this.reset();
         }

         this.lastNPCId = npcId;
      }

      void updatePendingTrait(NPC npc, CitizensShopkeeperTrait trait) {
         assert npc != null && trait != null;

         this.updateLastNPC(npc);
         this.handlePendingTrait();

         assert this.pendingTrait == null;

         this.pendingTrait = trait;
         this.tryHandlePendingTraitWithPlayer();
      }

      private void handlePendingTrait() {
         if (this.pendingTrait != null) {
            assert this.lastPlayerId == null;

            CitizensShopkeeperTrait trait = this.pendingTrait;
            this.pendingTrait = null;
            this.reset();
            this.handleTrait(trait, (Player)null);
         }

      }

      void updateLastPlayer(NPC npc, Player player) {
         assert npc != null && player != null;

         this.updateLastNPC(npc);
         if (this.lastPlayerId != null) {
            assert this.pendingTrait == null;

            this.reset();
         }

         this.lastPlayerId = player.getUniqueId();
         this.tryHandlePendingTraitWithPlayer();
      }

      private void tryHandlePendingTraitWithPlayer() {
         UUID lastPlayerId = this.lastPlayerId;
         if (lastPlayerId != null && this.pendingTrait != null) {
            CitizensShopkeeperTrait trait = this.pendingTrait;
            Player player = Bukkit.getPlayer(lastPlayerId);
            this.pendingTrait = null;
            this.reset();
            this.handleTrait(trait, player);
         } else {
            assert this.pendingTraitTask == null;

            if (this.pendingTraitTask == null || ((BukkitTask)Unsafe.assertNonNull(this.pendingTraitTask)).isCancelled()) {
               this.pendingTraitTask = Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                  this.pendingTraitTask = null;
                  this.reset();
               }, 1L);
            }
         }

      }

      void reset() {
         if (this.pendingTrait != null) {
            this.handlePendingTrait();

            assert this.pendingTrait == null;
         }

         this.lastNPCId = null;
         this.lastPlayerId = null;
         if (this.pendingTraitTask != null) {
            this.pendingTraitTask.cancel();
            this.pendingTraitTask = null;
         }

      }

      protected abstract void handleTrait(CitizensShopkeeperTrait var1, @Nullable Player var2);
   }
}
