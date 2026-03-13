package me.casperge.realisticseasons.data.chunksaver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.season.SeasonChunk;
import org.bukkit.Bukkit;

public class ChunkDataHandler {
   private RealisticSeasons main;
   private AsyncDataSaver saver;
   AtomicBoolean isActive = new AtomicBoolean(false);

   public ChunkDataHandler(final RealisticSeasons var1) {
      this.main = var1;
      this.saver = new AsyncDataSaver(var1);
      if (var1.getSettings().canCheckUnloadedChunks && var1.getSettings().modifyBlocks) {
         int var2 = var1.getSettings().delayPerAsyncChunk;
         if (this.isActive.get()) {
            return;
         }

         this.isActive.set(true);
         Bukkit.getScheduler().runTaskTimerAsynchronously(var1, new Runnable() {
            public void run() {
               ChunkDataHandler.this.saver.tick();
               final ArrayList var1x = new ArrayList();
               Set var2 = ChunkDataHandler.this.saver.getWorlds();
               if (var2 != null) {
                  Iterator var3 = ChunkDataHandler.this.saver.getWorlds().iterator();

                  while(var3.hasNext()) {
                     String var4 = (String)var3.next();
                     SeasonChunk var5 = ChunkDataHandler.this.saver.getRandomChunk(var4);
                     if (var5 != null) {
                        var1x.add(var5);
                     }
                  }

                  Bukkit.getScheduler().runTask(var1, new Runnable() {
                     public void run() {
                        Iterator var1xx = var1x.iterator();

                        while(var1xx.hasNext()) {
                           SeasonChunk var2 = (SeasonChunk)var1xx.next();
                           var1.getSeasonManager().checkChunk(var2);
                        }

                        ChunkDataHandler.this.isActive.set(false);
                     }
                  });
               }
            }
         }, (long)var2, (long)var2);
      }

   }

   public void logChunk(SeasonChunk var1) {
      this.saver.saveChunk(var1);
   }

   public void remove(SeasonChunk var1) {
      this.saver.deleteChunk(var1);
   }
}
