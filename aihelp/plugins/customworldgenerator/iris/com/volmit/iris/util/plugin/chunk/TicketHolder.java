package com.volmit.iris.util.plugin.chunk;

import com.volmit.iris.Iris;
import com.volmit.iris.engine.data.cache.Cache;
import com.volmit.iris.util.collection.KMap;
import lombok.NonNull;
import org.bukkit.Chunk;
import org.bukkit.World;

public class TicketHolder {
   private final World world;
   private final KMap<Long, Long> tickets = new KMap();

   public TicketHolder(@NonNull World world) {
      if (var1 == null) {
         throw new NullPointerException("world is marked non-null but is null");
      } else {
         this.world = var1;
      }
   }

   public void addTicket(@NonNull Chunk chunk) {
      if (var1 == null) {
         throw new NullPointerException("chunk is marked non-null but is null");
      } else if (var1.getWorld() == this.world) {
         this.addTicket(var1.getX(), var1.getZ());
      }
   }

   public void addTicket(int x, int z) {
      this.tickets.compute(Cache.key(var1, var2), (var3, var4) -> {
         if (var4 == null) {
            this.world.addPluginChunkTicket(var1, var2, Iris.instance);
            return 1L;
         } else {
            return var4 + 1L;
         }
      });
   }

   public boolean removeTicket(@NonNull Chunk chunk) {
      if (var1 == null) {
         throw new NullPointerException("chunk is marked non-null but is null");
      } else {
         return var1.getWorld() != this.world ? false : this.removeTicket(var1.getX(), var1.getZ());
      }
   }

   public boolean removeTicket(int x, int z) {
      return this.tickets.compute(Cache.key(var1, var2), (var3, var4) -> {
         if (var4 == null) {
            return null;
         } else if (var4 = var4 - 1L <= 0L) {
            this.world.removePluginChunkTicket(var1, var2, Iris.instance);
            return null;
         } else {
            return var4;
         }
      }) == null;
   }
}
