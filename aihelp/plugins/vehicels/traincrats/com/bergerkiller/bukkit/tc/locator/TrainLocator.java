package com.bergerkiller.bukkit.tc.locator;

import com.bergerkiller.bukkit.common.Task;
import com.bergerkiller.bukkit.common.component.LibraryComponent;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.bukkit.entity.Player;

public class TrainLocator implements LibraryComponent {
   private final TrainCarts plugin;
   private final Map<TrainLocator.Key, TrainLocatorEntry> locators = new HashMap();
   private Task updateTask;

   public TrainLocator(TrainCarts plugin) {
      this.plugin = plugin;
   }

   public void enable() {
      this.updateTask = new Task(this.plugin) {
         public void run() {
            int currentTime = CommonUtil.getServerTicks();
            Iterator iter = TrainLocator.this.locators.values().iterator();

            while(true) {
               while(iter.hasNext()) {
                  TrainLocatorEntry locator = (TrainLocatorEntry)iter.next();
                  if (TrainLocator.this.canLocate(locator.player, locator.member) && currentTime <= locator.timeoutTickTime) {
                     locator.update();
                  } else {
                     locator.despawn();
                     iter.remove();
                  }
               }

               if (TrainLocator.this.locators.isEmpty()) {
                  this.stop();
               }

               return;
            }
         }
      };
   }

   public void disable() {
      Task.stop(this.updateTask);
      this.updateTask = null;
   }

   public boolean canLocate(Player player, MinecartMember<?> member) {
      return player.isValid() && !member.isUnloaded() && player.getWorld() == member.getWorld();
   }

   public boolean isLocating(Player player, MinecartGroup group) {
      Iterator var3 = group.iterator();

      MinecartMember member;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         member = (MinecartMember)var3.next();
      } while(!this.isLocating(player, member));

      return true;
   }

   public boolean isLocating(Player player, MinecartMember<?> member) {
      return this.locators.containsKey(new TrainLocator.Key(player, member));
   }

   public boolean start(Player player, MinecartGroup group) {
      return this.start(player, (MinecartGroup)group, -1);
   }

   public boolean start(Player player, MinecartGroup group, int timeoutTicks) {
      boolean started = false;

      MinecartMember member;
      for(Iterator var5 = group.iterator(); var5.hasNext(); started |= this.start(player, member, timeoutTicks)) {
         member = (MinecartMember)var5.next();
      }

      return started;
   }

   public boolean start(Player player, MinecartMember<?> member) {
      return this.start(player, (MinecartMember)member, -1);
   }

   public boolean start(Player player, MinecartMember<?> member, int timeoutTicks) {
      if (!this.canLocate(player, member)) {
         return false;
      } else {
         if (this.locators.isEmpty()) {
            this.updateTask.start(1L, 1L);
         }

         TrainLocatorEntry locator = (TrainLocatorEntry)this.locators.computeIfAbsent(new TrainLocator.Key(player, member), (k) -> {
            return TrainLocatorEntry.create(k.player, k.member);
         });
         locator.timeoutTickTime = timeoutTicks >= 0 ? CommonUtil.getServerTicks() + timeoutTicks : Integer.MAX_VALUE;
         return true;
      }
   }

   public boolean stopAll(Player player) {
      boolean found = false;
      Iterator iter = this.locators.values().iterator();

      while(iter.hasNext()) {
         TrainLocatorEntry locator = (TrainLocatorEntry)iter.next();
         if (locator.player == player) {
            iter.remove();
            locator.despawn();
            found = true;
         }
      }

      if (this.locators.isEmpty()) {
         this.updateTask.stop();
      }

      return found;
   }

   public boolean stop(Player player, MinecartGroup group) {
      boolean stopped = false;

      MinecartMember member;
      for(Iterator var4 = group.iterator(); var4.hasNext(); stopped |= this.stop(player, member)) {
         member = (MinecartMember)var4.next();
      }

      return stopped;
   }

   public boolean stop(Player player, MinecartMember<?> member) {
      TrainLocatorEntry locator = (TrainLocatorEntry)this.locators.remove(new TrainLocator.Key(player, member));
      if (locator != null) {
         locator.despawn();
         if (this.locators.isEmpty()) {
            this.updateTask.stop();
         }

         return true;
      } else {
         return false;
      }
   }

   private static final class Key {
      private final Player player;
      private final MinecartMember<?> member;

      public Key(Player player, MinecartMember<?> member) {
         this.player = player;
         this.member = member;
      }

      public int hashCode() {
         return 31 * this.player.hashCode() + this.member.hashCode();
      }

      public boolean equals(Object o) {
         TrainLocator.Key k = (TrainLocator.Key)o;
         return this.player == k.player && this.member == k.member;
      }
   }
}
