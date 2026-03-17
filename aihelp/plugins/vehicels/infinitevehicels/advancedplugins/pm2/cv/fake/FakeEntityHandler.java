package advancedplugins.pm2.cv.fake;

import advancedplugins.pm2.cv.api.event.VehicleMoveEvent;
import advancedplugins.pm2.cv.api.util.Run;
import advancedplugins.pm2.cv.api.vehicle.VehicleSeat;
import advancedplugins.pm2.cv.handler.PluginHandlerAdapter;
import advancedplugins.pm2.cv.handler.PluginHandlerOptions;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.jetbrains.annotations.NotNull;

@PluginHandlerOptions(
   eventListener = true
)
public final class FakeEntityHandler extends PluginHandlerAdapter implements Listener {
   private final Set<FakeEntity<?, ?>> instances = ConcurrentHashMap.newKeySet();
   private final Map<UUID, Set<FakeEntity<?, ?>>> instancesByWorld = new ConcurrentHashMap();

   void register(FakeEntity<?, ?> instance) {
      if (this.instances.add(var1)) {
         ((Set)this.instancesByWorld.computeIfAbsent(var1.world.getUID(), (var0) -> {
            return ConcurrentHashMap.newKeySet();
         })).add(var1);
         Iterator var2 = var1.world.getPlayers().iterator();

         while(var2.hasNext()) {
            Player var3 = (Player)var2.next();
            if (!this.isFarAway(var3, var1)) {
               var1.show(var3);
            }
         }
      }

   }

   void unregister(FakeEntity<?, ?> instance) {
      if (this.instances.remove(var1)) {
         var1.hide();
      }

      Set var2 = (Set)this.instancesByWorld.get(var1.world.getUID());
      if (var2 != null) {
         var2.remove(var1);
      }

   }

   void onLocationSet(@NotNull FakeEntity<?, ?> instance) {
      if (var1.registered) {
         int var2 = Location.locToBlock(var1.lastX) >> 4;
         int var3 = Location.locToBlock(var1.lastZ) >> 4;
         int var4 = Location.locToBlock(var1.x) >> 4;
         int var5 = Location.locToBlock(var1.z) >> 4;
         if (var2 != var4 || var3 != var5) {
            Iterator var6 = var1.world.getPlayers().iterator();

            while(var6.hasNext()) {
               Player var7 = (Player)var6.next();
               if (this.isFarAway(var7, var1)) {
                  var1.hide(var7, false);
               } else {
                  var1.show(var7);
               }
            }
         }

      }
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onJoin(PlayerJoinEvent event) {
      Player var2 = var1.getPlayer();
      Set var3 = (Set)this.instancesByWorld.get(var2.getWorld().getUID());
      if (var3 != null) {
         Iterator var4 = var3.iterator();

         while(var4.hasNext()) {
            FakeEntity var5 = (FakeEntity)var4.next();
            if (!this.isFarAway(var2, var5)) {
               var5.show(var2);
            }
         }

      }
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onQuit(PlayerQuitEvent event) {
      this.instances.forEach((var1x) -> {
         if (var1x.showGroup != null) {
            var1x.showGroup.resetProcessors(var1.getPlayer());
         }

         var1x.hide(var1.getPlayer(), true);
      });
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   public void onChangeWorld(PlayerChangedWorldEvent event) {
      World var2 = var1.getFrom();
      Player var3 = var1.getPlayer();
      Run.syncDelayed(() -> {
         Iterator var3x = this.instances.iterator();

         while(var3x.hasNext()) {
            FakeEntity var4 = (FakeEntity)var3x.next();
            if (Objects.equals(var4.getWorld(), var3.getWorld())) {
               if (!this.isFarAway(var3, var4)) {
                  var4.show(var3);
               }
            } else if (Objects.equals(var4.getWorld(), var2)) {
               var4.hide(var3, false);
            }
         }

      }, 10L);
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   public void onWorldUnload(WorldUnloadEvent event) {
      Set var2 = (Set)this.instancesByWorld.remove(var1.getWorld().getUID());
      if (var2 != null) {
         var2.forEach(FakeEntity::unregister);
      }

   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   public void onMove(PlayerMoveEvent event) {
      this.processOnMove(var1);
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   public void onMove(PlayerTeleportEvent event) {
      this.processOnMove(var1);
   }

   @EventHandler
   public void onVehicleMove(VehicleMoveEvent event) {
      Location var2 = var1.getTo();
      Iterator var3 = var1.getVehicle().getSeats().iterator();

      while(var3.hasNext()) {
         VehicleSeat var4 = (VehicleSeat)var3.next();
         Entity var6 = var4.getPassenger();
         if (var6 instanceof Player) {
            Player var5 = (Player)var6;
            this.processOnMove(new PlayerMoveEvent(var5, var1.getFrom(), var2));
         }
      }

   }

   private void processOnMove(PlayerMoveEvent event) {
      Location var2 = var1.getFrom();
      Location var3 = var1.getTo();
      if (var3 != null) {
         if (var2.getBlockX() >> 4 != var3.getBlockX() >> 4 || var2.getBlockZ() >> 4 != var3.getBlockZ() >> 4) {
            if (var1 instanceof PlayerTeleportEvent) {
               if (Objects.equals(var3.getWorld(), var2.getWorld())) {
                  Run.syncDelayed(() -> {
                     this.processOnMove0(var1);
                  });
               }
            } else {
               this.processOnMove0(var1);
            }

         }
      }
   }

   private void processOnMove0(PlayerMoveEvent event) {
      Player var2 = var1.getPlayer();
      Set var3 = (Set)this.instancesByWorld.get(var2.getWorld().getUID());
      if (var3 != null && var3.size() != 0) {
         Location var4 = var1.getTo();
         Location var5 = var1.getFrom();
         Iterator var6 = var3.iterator();

         while(var6.hasNext()) {
            FakeEntity var7 = (FakeEntity)var6.next();
            if (this.isFarAway(var4 != null ? var4 : var5, var7)) {
               var7.hide(var2, false);
            } else {
               var7.show(var2);
            }
         }

      }
   }

   private boolean isFarAway(@NotNull Player player, @NotNull FakeEntity<?, ?> entity) {
      return this.isFarAway(var1.getLocation(), var2);
   }

   private boolean isFarAway(@NotNull Location location, @NotNull FakeEntity<?, ?> entity) {
      int var3 = Location.locToBlock(var2.getX()) >> 4;
      int var4 = Location.locToBlock(var2.getZ()) >> 4;
      int var5 = var1.getBlockX() >> 4;
      int var6 = var1.getBlockZ() >> 4;
      int var7 = var2.getHideFarAwayChunks();
      if (var2.showGroup != null) {
         var7 = var2.showGroup.hideFarAwayChunks;
      }

      return Math.abs(var3 - var5) >= var7 || Math.abs(var4 - var6) >= var7;
   }
}
