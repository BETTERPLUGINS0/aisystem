package me.casperge.realisticseasons.runnables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.event.SeasonRefreshChunkEvent;
import me.casperge.realisticseasons.season.SeasonChunk;
import me.casperge.realisticseasons.utils.ChunkUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class ChunkRefresher {
   public World w;
   public RealisticSeasons main;
   public WeakHashMap<Player, Collection<SeasonChunk>> updateQueue;
   public BukkitTask task;
   public BukkitTask pretask;
   private Random r = new Random();
   private int viewDistance;

   public ChunkRefresher(final RealisticSeasons var1, World var2) {
      this.w = var2;
      this.main = var1;
      List var3 = var2.getPlayers();
      this.viewDistance = Bukkit.getServer().getViewDistance();
      final ArrayList var4 = new ArrayList();
      Iterator var5 = var3.iterator();

      while(var5.hasNext()) {
         Player var6 = (Player)var5.next();
         Chunk var7 = var6.getLocation().getChunk();
         var4.add(var7.getX() + "," + var7.getZ() + "," + var7.getWorld().getName() + "," + var6.getUniqueId().toString());
      }

      Bukkit.getScheduler().runTaskLaterAsynchronously(var1, new Runnable() {
         public void run() {
            final HashMap var1x = new HashMap();
            Iterator var2 = var4.iterator();

            while(var2.hasNext()) {
               String var3 = (String)var2.next();
               String[] var4x = var3.split(",");
               int var5 = Integer.valueOf(var4x[0]);
               int var6 = Integer.valueOf(var4x[1]);
               String var7 = var4x[2];
               Collection var8 = ChunkUtils.aroundString(var5, var6, var7, ChunkRefresher.this.viewDistance);
               var1x.put(var4x[3], var8);
            }

            ChunkRefresher.this.pretask = Bukkit.getScheduler().runTaskTimer(var1.getPlugin(), new Runnable() {
               WeakHashMap<Player, Collection<SeasonChunk>> queue = new WeakHashMap();

               public void run() {
                  if (!var1x.isEmpty()) {
                     Entry var1xx = (Entry)var1x.entrySet().iterator().next();
                     Collection var2 = (Collection)var1xx.getValue();
                     String var3 = (String)var1xx.getKey();
                     Player var4x = Bukkit.getPlayer(UUID.fromString(var3));
                     if (var4x != null) {
                        if (!var2.isEmpty()) {
                           String var5 = (String)var2.iterator().next();
                           if (ChunkUtils.isChunkLoadedString(var5)) {
                              Chunk var6 = ChunkUtils.chunkFromString(var5);
                              if (var6 != null) {
                                 if (this.queue.containsKey(var4x)) {
                                    Collection var7 = (Collection)this.queue.get(var4x);
                                    var7.add(new SeasonChunk(var6.getWorld().getName(), var6.getX(), var6.getZ(), 0L));
                                    this.queue.replace(var4x, var7);
                                 } else {
                                    ArrayList var8 = new ArrayList();
                                    var8.add(new SeasonChunk(var6.getWorld().getName(), var6.getX(), var6.getZ(), 0L));
                                    this.queue.put(var4x, var8);
                                 }
                              }
                           }

                           var2.remove(var5);
                           var1x.replace(var3, var2);
                        } else {
                           var1x.remove(var3);
                        }
                     } else {
                        var1x.remove(var3);
                     }
                  } else {
                     ChunkRefresher.this.pretask.cancel();
                     ChunkRefresher.this.updateQueue = this.queue;
                     ChunkRefresher.this.runRefresher();
                  }

               }
            }, 1L, 2L);
         }
      }, 1L);
   }

   public void runRefresher() {
      this.task = Bukkit.getScheduler().runTaskTimer(this.main.getPlugin(), new Runnable() {
         public void run() {
            if (!ChunkRefresher.this.updateQueue.isEmpty()) {
               ArrayList var1 = new ArrayList(ChunkRefresher.this.updateQueue.keySet());
               Player var2 = (Player)var1.get(ChunkRefresher.this.r.nextInt(var1.size()));
               Collection var3 = (Collection)ChunkRefresher.this.updateQueue.get(var2);
               if (!var3.isEmpty() && var2.isOnline()) {
                  Iterator var4 = var3.iterator();
                  SeasonChunk var5 = null;
                  if (var4.hasNext()) {
                     var5 = (SeasonChunk)var4.next();
                  }

                  var3.remove(var5);
                  ChunkRefresher.this.updateQueue.replace(var2, var3);
                  if (var2.getWorld() == var5.getWorld() && (new Location(var5.getWorld(), (double)(var5.getX() * 16), var2.getLocation().getY(), (double)(var5.getZ() * 16))).distance(var2.getLocation()) < (double)(ChunkRefresher.this.viewDistance * 18) && var5.getWorld().isChunkLoaded(var5.getX(), var5.getZ())) {
                     SeasonRefreshChunkEvent var6 = new SeasonRefreshChunkEvent(var5.getChunk(), var2);
                     Bukkit.getPluginManager().callEvent(var6);
                     if (!var6.isCancelled()) {
                        ChunkRefresher.this.main.getNMSUtils().refreshChunk(var2, var5.getChunk());
                     }
                  }
               } else {
                  ChunkRefresher.this.updateQueue.remove(var2);
               }
            } else {
               ChunkRefresher.this.main.getSeasonManager().refreshers.remove(ChunkRefresher.this.w);
               ChunkRefresher.this.task.cancel();
            }

         }
      }, 0L, 2L);
   }
}
