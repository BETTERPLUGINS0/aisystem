package com.bergerkiller.bukkit.tc.debug;

import com.bergerkiller.bukkit.common.bases.IntVector3;
import com.bergerkiller.bukkit.common.inventory.CommonItemStack;
import com.bergerkiller.bukkit.common.nbt.CommonTagCompound;
import com.bergerkiller.bukkit.common.offline.OfflineWorld;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.wrappers.HumanHand;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.signactions.mutex.MutexZone;
import com.bergerkiller.bukkit.tc.signactions.mutex.MutexZoneCache;
import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.WeakHashMap;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DebugTool {
   private static final WeakHashMap<Player, DebugTool.DebounceLogic> debounce = new WeakHashMap();

   public static void showMutexZones(TrainCarts traincarts, Player player) {
      Location loc = player.getEyeLocation();
      List<MutexZone> zones = MutexZoneCache.findNearbyZones(OfflineWorld.of(loc.getWorld()), new IntVector3(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), 32);
      if (!zones.isEmpty()) {
         Random r = new Random();
         Iterator var5 = zones.iterator();

         while(var5.hasNext()) {
            MutexZone zone = (MutexZone)var5.next();
            if (zone.slot.isAnonymous()) {
               r.setSeed(zone.showDebugColorSeed());
            } else {
               r.setSeed((long)zone.slot.getName().hashCode());
            }

            Color awt_color = Color.getHSBColor(r.nextFloat(), 1.0F, 1.0F);
            org.bukkit.Color color = org.bukkit.Color.fromRGB(awt_color.getRed(), awt_color.getGreen(), awt_color.getBlue());
            zone.showDebug(player, color);
         }

      }
   }

   public static boolean updateToolItem(Player player, CommonItemStack item) {
      return updateToolItem(player, item.toBukkit());
   }

   public static boolean updateToolItem(Player player, ItemStack item) {
      CommonItemStack inMainHand = CommonItemStack.of(HumanHand.getItemInMainHand(player));
      if (!inMainHand.isEmpty() && inMainHand.hasCustomData() && inMainHand.getCustomData().containsKey("TrainCartsDebug")) {
         HumanHand.setItemInMainHand(player, item);
         return true;
      } else {
         return false;
      }
   }

   public static boolean onDebugInteract(TrainCarts traincarts, Player player, Block clickedBlock, ItemStack item, boolean isRightClick) {
      return onDebugInteract(traincarts, player, clickedBlock, CommonItemStack.of(item), isRightClick);
   }

   public static boolean onDebugInteract(TrainCarts traincarts, Player player, Block clickedBlock, CommonItemStack item, boolean isRightClick) {
      if (!item.isEmpty() && item.hasCustomData()) {
         CommonTagCompound tag = item.getCustomData();
         String debugType = (String)tag.getValue("TrainCartsDebug", String.class);
         if (debugType == null) {
            return false;
         } else if (!Permission.DEBUG_COMMAND_DEBUG.has(player)) {
            if (debounce(player)) {
               player.sendMessage(ChatColor.RED + "No permission to use this item!");
            }

            return true;
         } else {
            Optional<DebugToolType> match = DebugToolTypeRegistry.match(debugType);
            if (!match.isPresent()) {
               if (debounce(player)) {
                  player.sendMessage(ChatColor.RED + "Item has an unknown debug mode: " + debugType);
               }

               return true;
            } else {
               ((DebugToolType)match.get()).loadMetadata(tag);
               if (!isRightClick && !((DebugToolType)match.get()).handlesLeftClick()) {
                  return false;
               } else {
                  if (debounce(player)) {
                     ((DebugToolType)match.get()).onBlockInteract(traincarts, player, clickedBlock, item, isRightClick);
                  }

                  return true;
               }
            }
         }
      } else {
         return false;
      }
   }

   private static boolean debounce(Player player) {
      return ((DebugTool.DebounceLogic)debounce.computeIfAbsent(player, DebugTool.DebounceLogic::new)).check();
   }

   private static final class DebounceLogic {
      private int lastActivation = 0;
      private int clickStart = 0;

      public DebounceLogic(Player player) {
      }

      public boolean check() {
         int ticks = CommonUtil.getServerTicks();
         int timeSinceActivation = ticks - this.lastActivation;
         this.lastActivation = ticks;
         if (timeSinceActivation > 10) {
            this.clickStart = ticks;
            return true;
         } else if (timeSinceActivation == 0) {
            return false;
         } else {
            return ticks - this.clickStart > 10;
         }
      }
   }
}
