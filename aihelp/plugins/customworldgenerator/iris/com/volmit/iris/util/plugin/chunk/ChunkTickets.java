package com.volmit.iris.util.plugin.chunk;

import com.volmit.iris.Iris;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class ChunkTickets implements Listener {
   private final Map<World, TicketHolder> holders = new HashMap();

   public ChunkTickets() {
      Iris.instance.registerListener(this);
      Bukkit.getWorlds().forEach((var1) -> {
         this.holders.put(var1, new TicketHolder(var1));
      });
   }

   public TicketHolder getHolder(@NonNull World world) {
      if (var1 == null) {
         throw new NullPointerException("world is marked non-null but is null");
      } else {
         return (TicketHolder)this.holders.get(var1);
      }
   }

   public void addTicket(@NonNull Chunk chunk) {
      if (var1 == null) {
         throw new NullPointerException("chunk is marked non-null but is null");
      } else {
         this.addTicket(var1.getWorld(), var1.getX(), var1.getZ());
      }
   }

   public void addTicket(@NonNull World world, int x, int z) {
      if (var1 == null) {
         throw new NullPointerException("world is marked non-null but is null");
      } else {
         TicketHolder var4 = this.getHolder(var1);
         if (var4 != null) {
            var4.addTicket(var2, var3);
         }

      }
   }

   public boolean removeTicket(@NonNull Chunk chunk) {
      if (var1 == null) {
         throw new NullPointerException("chunk is marked non-null but is null");
      } else {
         return this.removeTicket(var1.getWorld(), var1.getX(), var1.getZ());
      }
   }

   public boolean removeTicket(@NonNull World world, int x, int z) {
      if (var1 == null) {
         throw new NullPointerException("world is marked non-null but is null");
      } else {
         TicketHolder var4 = this.getHolder(var1);
         return var4 != null ? var4.removeTicket(var2, var3) : false;
      }
   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void on(@NonNull WorldLoadEvent event) {
      if (var1 == null) {
         throw new NullPointerException("event is marked non-null but is null");
      } else {
         this.holders.put(var1.getWorld(), new TicketHolder(var1.getWorld()));
      }
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void on(@NonNull WorldUnloadEvent event) {
      if (var1 == null) {
         throw new NullPointerException("event is marked non-null but is null");
      } else {
         this.holders.remove(var1.getWorld());
      }
   }
}
