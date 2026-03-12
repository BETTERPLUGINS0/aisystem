package me.SuperRonanCraft.BetterRTP.references.rtpinfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.references.customEvents.RTP_TeleportPostEvent;
import me.SuperRonanCraft.BetterRTP.references.database.DatabaseHandler;
import me.SuperRonanCraft.BetterRTP.references.database.DatabaseQueue;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.RTPWorld;
import me.SuperRonanCraft.BetterRTP.versions.AsyncHandler;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

public class QueueHandler implements Listener {
   boolean loaded = false;
   private final QueueGenerator generator = new QueueGenerator();

   public static boolean isEnabled() {
      return BetterRTP.getInstance().getSettings().isQueueEnabled();
   }

   public void registerEvents(BetterRTP pl) {
      PluginManager pm = pl.getServer().getPluginManager();
      pm.registerEvents(this, pl);
   }

   public void unload() {
      this.generator.unload();
   }

   public void load() {
      this.loaded = false;
      this.generator.load();
   }

   @EventHandler
   public void onRTP(RTP_TeleportPostEvent e) {
      remove(e.getLocation());
   }

   public static QueueData getRandomAsync(RTPWorld rtpWorld) {
      List<QueueData> queueData = getApplicableAsync(rtpWorld);
      if (queueData.size() <= 2 && !BetterRTP.getInstance().getQueue().generator.generating) {
         BetterRTP.getInstance().getQueue().generator.generate(rtpWorld);
      }

      if (!queueData.isEmpty()) {
         QueueData randomQueue = (QueueData)queueData.get((new Random()).nextInt(queueData.size()));
         queueData.clear();
         return randomQueue;
      } else {
         return null;
      }
   }

   public static List<QueueData> getApplicableAsync(RTPWorld rtpWorld) {
      List<QueueData> available = new ArrayList();
      if (!isEnabled()) {
         return available;
      } else {
         List<QueueData> queueData = DatabaseHandler.getQueue().getInRange(new DatabaseQueue.QueueRangeData(rtpWorld));
         Iterator var3 = queueData.iterator();

         while(var3.hasNext()) {
            QueueData data = (QueueData)var3.next();
            if (Objects.equals(data.getLocation().getWorld().getName(), rtpWorld.getWorld().getName())) {
               switch(rtpWorld.getShape()) {
               case CIRCLE:
                  if (isInCircle(data.location, rtpWorld)) {
                     available.add(data);
                  }
                  break;
               case SQUARE:
               default:
                  if (isInSquare(data.location, rtpWorld)) {
                     available.add(data);
                  }
               }
            }
         }

         return available;
      }
   }

   public static void remove(Location loc) {
      if (isEnabled()) {
         AsyncHandler.async(() -> {
            if (DatabaseHandler.getQueue().removeLocation(loc)) {
               BetterRTP.debug("-Removed a queue " + loc);
            }

         });
      }
   }

   public static boolean isInCircle(Location loc, RTPWorld rtpWorld) {
      int center_x = rtpWorld.getCenterX();
      int center_z = rtpWorld.getCenterZ();
      int radius = rtpWorld.getMaxRadius();
      int radius_min = rtpWorld.getMinRadius();
      int x = loc.getBlockX();
      int z = loc.getBlockZ();
      int square_dist = (center_x - x) * 2 + (center_z - z) * 2;
      return square_dist <= radius * 2 && square_dist >= radius_min * 2;
   }

   public static boolean isInSquare(Location loc, RTPWorld rtpWorld) {
      int radius_max = rtpWorld.getMaxRadius();
      int radius_min = rtpWorld.getMinRadius();
      int x = loc.getBlockX() - rtpWorld.getCenterX();
      int z = loc.getBlockZ() - rtpWorld.getCenterZ();
      return (Math.abs(x) >= radius_min || Math.abs(z) >= radius_min) && Math.abs(x) <= radius_max && Math.abs(z) <= radius_max;
   }
}
