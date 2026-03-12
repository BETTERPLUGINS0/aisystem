package me.SuperRonanCraft.BetterRTP.references.rtpinfo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.lib.folialib.wrapper.task.WrappedTask;
import me.SuperRonanCraft.BetterRTP.lib.paperlib.PaperLib;
import me.SuperRonanCraft.BetterRTP.player.commands.RTP_SETUP_TYPE;
import me.SuperRonanCraft.BetterRTP.player.rtp.RTP;
import me.SuperRonanCraft.BetterRTP.references.database.DatabaseHandler;
import me.SuperRonanCraft.BetterRTP.references.helpers.HelperRTP;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.RTPWorld;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.WorldCustom;
import me.SuperRonanCraft.BetterRTP.versions.AsyncHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

public class QueueGenerator {
   boolean loaded = false;
   public static final int queueMax = 32;
   public static final int queueMin = 2;
   private final int queueMaxAttempts = 50;
   boolean generating;
   private WrappedTask task;

   public void unload() {
      if (this.task != null) {
         this.task.cancel();
      }

   }

   public void load() {
      this.unload();
      this.loaded = false;
      this.generate((RTPWorld)null);
   }

   void generate(@Nullable RTPWorld rtpWorld) {
      if (QueueHandler.isEnabled()) {
         AsyncHandler.asyncLater(() -> {
            if (!DatabaseHandler.getQueue().isLoaded()) {
               this.generate(rtpWorld);
            } else {
               this.loaded = true;
               BetterRTP.debug("Attempting to queue up some more safe locations...");
               this.queueGenerator(new QueueGenerator.ReQueueData(rtpWorld, 32, 2, 0, "noone", 0));
            }
         }, 10L);
      }
   }

   private void queueGenerator(QueueGenerator.ReQueueData data) {
      this.generating = true;
      this.task = AsyncHandler.asyncLater(() -> {
         int newCountx;
         if (data.rtpWorld != null) {
            List<QueueData> applicablex = QueueHandler.getApplicableAsync(data.rtpWorld);
            String type = "rtp_" + (data.rtpWorld.getID() != null ? data.rtpWorld.getID() : data.rtpWorld.getWorld().getName());
            newCountx = data.lastType.equalsIgnoreCase(type) ? data.lastCount : applicablex.size();
            int attemptx = data.lastType.equalsIgnoreCase(type) ? data.attempts + 1 : 0;
            if (newCountx < 2 && applicablex.size() < 32) {
               if (attemptx > 50) {
                  BetterRTP.debug("Max attempts to create a Queue reached for " + type + " (amount: " + applicablex.size() + ")");
                  return;
               }

               this.addQueue(data.rtpWorld, type, new QueueGenerator.ReQueueData(data.rtpWorld, 32, 2, newCountx, type, attemptx));
               return;
            }

            if (data.lastType.equalsIgnoreCase(type)) {
               BetterRTP.debug("Queue max reached for " + type + " (amount: " + applicablex.size() + ") lastCount: " + data.lastCount);
            }
         }

         RTP_SETUP_TYPE[] var14 = RTP_SETUP_TYPE.values();
         int var15 = var14.length;

         label80:
         for(newCountx = 0; newCountx < var15; ++newCountx) {
            RTP_SETUP_TYPE setup = var14[newCountx];
            HashMap<String, RTPWorld> map = getFromSetup(setup);
            if (map != null) {
               Iterator var7 = map.entrySet().iterator();

               while(true) {
                  while(true) {
                     if (!var7.hasNext()) {
                        continue label80;
                     }

                     Entry<String, RTPWorld> rtpWorldEntry = (Entry)var7.next();
                     RTPWorld world = (RTPWorld)rtpWorldEntry.getValue();
                     String typex = getId(setup, (String)rtpWorldEntry.getKey());
                     List<QueueData> applicable = QueueHandler.getApplicableAsync(world);
                     int newCount = data.lastType.equalsIgnoreCase(typex) ? data.lastCount : applicable.size();
                     int attempt = data.lastType.equalsIgnoreCase(typex) ? data.attempts + 1 : 0;
                     if (newCount < 2 && applicable.size() < 32) {
                        if (attempt <= 50) {
                           this.addQueue(world, typex, new QueueGenerator.ReQueueData((RTPWorld)null, 32, 2, newCount, typex, attempt));
                           return;
                        }

                        BetterRTP.debug("Max attempts to create a Queue reached for " + typex + " (amount: " + applicable.size() + ")");
                     } else if (data.lastType.equalsIgnoreCase(typex)) {
                        BetterRTP.debug("Max queue reached for " + typex + " (amount: " + applicable.size() + ") lastCount: " + data.lastCount);
                     }
                  }
               }
            }
         }

         this.generating = false;
         BetterRTP.debug("Queueing paused, max queue limit reached!");
      }, 20L);
   }

   private static HashMap<String, RTPWorld> getFromSetup(RTP_SETUP_TYPE type) {
      switch(type) {
      case LOCATION:
         return BetterRTP.getInstance().getRTP().getRTPworldLocations();
      case CUSTOM_WORLD:
         return BetterRTP.getInstance().getRTP().getRTPcustomWorld();
      case DEFAULT:
         HashMap<String, RTPWorld> list = new HashMap();
         RTP rtp = BetterRTP.getInstance().getRTP();
         Iterator var3 = Bukkit.getWorlds().iterator();

         while(var3.hasNext()) {
            World world = (World)var3.next();
            if (!rtp.getDisabledWorlds().contains(world.getName()) && !rtp.getRTPcustomWorld().containsKey(world.getName())) {
               list.put(world.getName(), new WorldCustom(world, rtp.getRTPdefaultWorld()));
            }
         }

         return list;
      default:
         return null;
      }
   }

   private static String getId(RTP_SETUP_TYPE type, String id) {
      switch(type) {
      case LOCATION:
         return "location_" + id;
      case CUSTOM_WORLD:
         return "custom_" + id;
      case DEFAULT:
         return "default_" + id;
      default:
         return "unknown_" + id;
      }
   }

   private void addQueue(RTPWorld rtpWorld, String id, QueueGenerator.ReQueueData reQueueData) {
      Location loc = RandomLocation.generateLocation(rtpWorld);
      if (loc != null) {
         AsyncHandler.sync(() -> {
            PaperLib.getChunkAtAsync(loc).thenAccept((v) -> {
               Location safeLoc = RandomLocation.getSafeLocation(HelperRTP.getWorldType(rtpWorld.getWorld()), loc.getWorld(), loc, rtpWorld.getMinY(), rtpWorld.getMaxY(), rtpWorld.getBiomes());
               if (safeLoc != null) {
                  AsyncHandler.async(() -> {
                     QueueData data = DatabaseHandler.getQueue().addQueue(safeLoc);
                     if (data != null) {
                        String _x = String.valueOf(data.getLocation().getBlockX());
                        String _y = String.valueOf(data.getLocation().getBlockY());
                        String _z = String.valueOf(data.getLocation().getBlockZ());
                        String _world = data.getLocation().getWorld().getName();
                        BetterRTP.debug("Queue position generated: id= " + id + ", database_ID= " + data.database_id + ", location= x: " + _x + ", y: " + _y + ", z: " + _z + ", world: " + _world);
                     } else {
                        BetterRTP.debug("Database error occurred for a queue when trying to save: " + safeLoc);
                     }

                     this.queueGenerator(reQueueData);
                  });
               } else {
                  this.queueGenerator(reQueueData);
               }

            });
         });
      } else {
         BetterRTP.debug("Queue position wasn't able to generate a location!");
         this.queueGenerator(reQueueData);
      }

   }

   static class ReQueueData {
      RTPWorld rtpWorld;
      int queueMax;
      int queueMin;
      int lastCount;
      int attempts;
      String lastType;

      ReQueueData(RTPWorld rtpWorld, int queueMax, int queueMin, int lastCount, String lastType, int attempts) {
         this.rtpWorld = rtpWorld;
         this.queueMax = queueMax;
         this.queueMin = queueMin;
         this.lastCount = lastCount;
         this.lastType = lastType;
         this.attempts = attempts;
      }
   }
}
