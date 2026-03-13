package com.ryandw11.structure.threading;

import com.ryandw11.structure.structure.Structure;
import com.ryandw11.structure.structure.StructureHandler;
import com.ryandw11.structure.utils.Pair;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class CheckStructureList extends BukkitRunnable {
   public static final int MAX_STORED_STRUCTURES = 300;
   private final StructureHandler handler;

   public CheckStructureList(StructureHandler handler) {
      this.handler = handler;
   }

   public void run() {
      synchronized(this.handler.getSpawnedStructures()) {
         Set<Pair<Location, Long>> locationsToRemove = new HashSet();
         Iterator var3 = this.handler.getSpawnedStructures().entrySet().iterator();

         while(var3.hasNext()) {
            Entry<Pair<Location, Long>, Structure> entry = (Entry)var3.next();
            if ((double)(System.currentTimeMillis() - (Long)((Pair)entry.getKey()).getRight()) > 2.592E8D) {
               locationsToRemove.add((Pair)entry.getKey());
            } else if (this.handler.getSpawnedStructures().size() - locationsToRemove.size() > 300) {
               locationsToRemove.add((Pair)entry.getKey());
            }
         }

         this.handler.getSpawnedStructures().keySet().removeAll(locationsToRemove);
      }
   }
}
