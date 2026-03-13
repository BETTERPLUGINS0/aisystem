package com.nisovin.shopkeepers.shopkeeper.ticking;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.debug.DebugOptions;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.util.java.CyclicCounter;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.scheduler.BukkitRunnable;

public class ShopkeeperTicker {
   public static final int TICKING_PERIOD_TICKS = 20;
   public static final int TICKING_GROUPS = 4;
   private static final CyclicCounter tickingGroupCounter = new CyclicCounter(4);
   private final SKShopkeepersPlugin plugin;
   private final List<? extends ShopkeeperTicker.TickingGroup> tickingGroups;
   private final CyclicCounter activeTickingGroup;
   private boolean currentlyTicking;
   private boolean dirty;
   private final Map<AbstractShopkeeper, Boolean> pendingTickingChanges;

   public static int nextTickingGroup() {
      return tickingGroupCounter.getAndIncrement();
   }

   public ShopkeeperTicker(SKShopkeepersPlugin plugin) {
      List<ShopkeeperTicker.TickingGroup> tickingGroups = new ArrayList(4);

      for(int i = 0; i < 4; ++i) {
         tickingGroups.add(new ShopkeeperTicker.TickingGroup());
      }

      this.tickingGroups = tickingGroups;
      this.activeTickingGroup = new CyclicCounter(4);
      this.currentlyTicking = false;
      this.pendingTickingChanges = new LinkedHashMap();
      Validate.notNull(plugin, (String)"plugin is null");
      this.plugin = plugin;
   }

   public void onEnable() {
      tickingGroupCounter.reset();
      this.activeTickingGroup.setValue(0);
      this.startShopkeeperTickTask();
   }

   public void onDisable() {
      if (this.currentlyTicking) {
         this.currentlyTicking = false;
         this.dirty = false;
         this.tickingGroups.forEach(ShopkeeperTicker.TickingGroup::clear);
         this.pendingTickingChanges.clear();
      } else {
         this.ensureEmpty();
      }

   }

   private void ensureEmpty() {
      boolean anyNonEmptyTickingGroup = this.tickingGroups.stream().anyMatch((tickingGroup) -> {
         return !tickingGroup.getShopkeepers().isEmpty();
      });
      if (anyNonEmptyTickingGroup) {
         Log.warning("Some ticking shopkeepers were not properly unregistered!");
         this.tickingGroups.forEach(ShopkeeperTicker.TickingGroup::clear);
      }

      if (!this.pendingTickingChanges.isEmpty()) {
         Log.warning("Unexpected pending shopkeeper ticking changes!");
         this.pendingTickingChanges.clear();
      }

   }

   private ShopkeeperTicker.TickingGroup getTickingGroup(int tickingGroupIndex) {
      assert tickingGroupIndex >= 0 && tickingGroupIndex < this.tickingGroups.size();

      ShopkeeperTicker.TickingGroup tickingGroup = (ShopkeeperTicker.TickingGroup)this.tickingGroups.get(tickingGroupIndex);

      assert tickingGroup != null;

      return tickingGroup;
   }

   private ShopkeeperTicker.TickingGroup getTickingGroup(AbstractShopkeeper shopkeeper) {
      assert shopkeeper != null;

      int tickingGroupIndex = shopkeeper.getTickingGroup();
      return this.getTickingGroup(tickingGroupIndex);
   }

   public void startTicking(AbstractShopkeeper shopkeeper) {
      assert shopkeeper != null;

      if (!shopkeeper.isTicking()) {
         Log.debug(DebugOptions.shopkeeperActivation, () -> {
            String var10000 = shopkeeper.getLogPrefix();
            return var10000 + "Ticking started." + (this.currentlyTicking ? " (Deferred registration)" : "");
         });
         if (this.currentlyTicking) {
            this.pendingTickingChanges.put(shopkeeper, true);
         } else {
            this.addShopkeeper(shopkeeper);
         }

         try {
            shopkeeper.informStartTicking();
         } catch (Throwable var3) {
            Log.severe(shopkeeper.getLogPrefix() + "Error during ticking start!", var3);
         }

      }
   }

   public void stopTicking(AbstractShopkeeper shopkeeper) {
      assert shopkeeper != null;

      if (shopkeeper.isTicking()) {
         Log.debug(DebugOptions.shopkeeperActivation, () -> {
            String var10000 = shopkeeper.getLogPrefix();
            return var10000 + "Ticking stopped." + (this.currentlyTicking ? " (Deferred unregistration)" : "");
         });
         if (this.currentlyTicking) {
            this.pendingTickingChanges.put(shopkeeper, false);
         } else {
            this.removeShopkeeper(shopkeeper);
         }

         try {
            shopkeeper.informStopTicking();
         } catch (Throwable var3) {
            Log.severe(shopkeeper.getLogPrefix() + "Error during ticking stop!", var3);
         }

      }
   }

   private void addShopkeeper(AbstractShopkeeper shopkeeper) {
      assert shopkeeper != null;

      ShopkeeperTicker.TickingGroup tickingGroup = this.getTickingGroup(shopkeeper);

      assert tickingGroup != null;

      tickingGroup.addShopkeeper(shopkeeper);
   }

   private void removeShopkeeper(AbstractShopkeeper shopkeeper) {
      assert shopkeeper != null;

      ShopkeeperTicker.TickingGroup tickingGroup = this.getTickingGroup(shopkeeper);

      assert tickingGroup != null;

      tickingGroup.removeShopkeeper(shopkeeper);
   }

   private void startShopkeeperTickTask() {
      (new ShopkeeperTicker.ShopkeeperTickTask()).start();
   }

   private void tickShopkeepers() {
      this.dirty = false;
      this.currentlyTicking = true;
      ShopkeeperTicker.TickingGroup tickingGroup = this.getTickingGroup(this.activeTickingGroup.getValue());
      tickingGroup.getShopkeepers().forEach(this::tickShopkeeper);
      this.currentlyTicking = false;
      this.pendingTickingChanges.forEach((shopkeeper, isTicking) -> {
         if (isTicking) {
            this.addShopkeeper(shopkeeper);
         } else {
            this.removeShopkeeper(shopkeeper);
         }

      });
      this.pendingTickingChanges.clear();
      if (this.dirty) {
         this.plugin.getShopkeeperStorage().saveDelayed();
      }

      this.activeTickingGroup.getAndIncrement();
   }

   private void tickShopkeeper(AbstractShopkeeper shopkeeper) {
      assert shopkeeper != null;

      if (shopkeeper.isTicking()) {
         try {
            shopkeeper.tick();
         } catch (Throwable var3) {
            Log.severe(shopkeeper.getLogPrefix() + "Error during ticking!", var3);
         }

         if (shopkeeper.isDirty()) {
            this.dirty = true;
         }

      }
   }

   private static final class TickingGroup {
      private final Set<AbstractShopkeeper> shopkeepers = new LinkedHashSet();

      TickingGroup() {
      }

      Collection<? extends AbstractShopkeeper> getShopkeepers() {
         return this.shopkeepers;
      }

      void addShopkeeper(AbstractShopkeeper shopkeeper) {
         assert shopkeeper != null;

         this.shopkeepers.add(shopkeeper);
      }

      void removeShopkeeper(AbstractShopkeeper shopkeeper) {
         assert shopkeeper != null;

         this.shopkeepers.remove(shopkeeper);
      }

      void clear() {
         this.shopkeepers.clear();
      }
   }

   private final class ShopkeeperTickTask extends BukkitRunnable {
      private static final int PERIOD = 5;

      void start() {
         this.runTaskTimer(ShopkeeperTicker.this.plugin, 5L, 5L);
      }

      public void run() {
         ShopkeeperTicker.this.tickShopkeepers();
      }
   }
}
