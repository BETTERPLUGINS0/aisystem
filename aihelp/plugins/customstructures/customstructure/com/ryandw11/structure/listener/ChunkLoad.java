package com.ryandw11.structure.listener;

import com.ryandw11.structure.CustomStructures;
import com.ryandw11.structure.utils.StructurePicker;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

public class ChunkLoad implements Listener {
   private final CustomStructures plugin = CustomStructures.getInstance();

   @EventHandler
   public void onChunkLoad(ChunkLoadEvent e) {
      if (CustomStructures.enabled) {
         boolean newChunk = this.plugin.getConfig().contains("new_chunks") && !this.plugin.getConfig().getBoolean("new_chunks");
         if (newChunk || e.isNewChunk()) {
            Block b = e.getChunk().getBlock(8, 5, 8);

            try {
               StructurePicker s = new StructurePicker(b, e.getChunk(), CustomStructures.getInstance());
               s.runTaskTimer(CustomStructures.plugin, 1L, 10L);
            } catch (RuntimeException var5) {
            }

         }
      }
   }
}
