package com.dfsek.terra.bukkit;

import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import com.dfsek.terra.lib.paperlib.PaperLib;
import org.bukkit.Location;

public class BukkitEntity implements Entity {
   private final org.bukkit.entity.Entity entity;

   public BukkitEntity(org.bukkit.entity.Entity entity) {
      this.entity = entity;
   }

   public org.bukkit.entity.Entity getHandle() {
      return this.entity;
   }

   public Vector3 position() {
      return BukkitAdapter.adapt(this.entity.getLocation().toVector());
   }

   public void position(Vector3 location) {
      PaperLib.teleportAsync(this.entity, BukkitAdapter.adapt(location).toLocation(this.entity.getWorld()));
   }

   public void world(ServerWorld world) {
      Location newLoc = this.entity.getLocation().clone();
      newLoc.setWorld(BukkitAdapter.adapt(world));
      PaperLib.teleportAsync(this.entity, newLoc);
   }

   public ServerWorld world() {
      return BukkitAdapter.adapt(this.entity.getWorld());
   }
}
