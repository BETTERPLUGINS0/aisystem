package com.volmit.iris.engine.framework;

import org.bukkit.Chunk;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public interface EngineWorldManager {
   void close();

   double getEnergy();

   int getEntityCount();

   int getChunkCount();

   double getEntitySaturation();

   void onTick();

   void onSave();

   void onBlockBreak(BlockBreakEvent e);

   void onBlockPlace(BlockPlaceEvent e);

   void onChunkLoad(Chunk e, boolean generated);

   void onChunkUnload(Chunk e);

   void chargeEnergy();

   void teleportAsync(PlayerTeleportEvent e);
}
