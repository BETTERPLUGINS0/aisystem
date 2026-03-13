package com.dfsek.terra.bukkit.world.block.state;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import com.dfsek.terra.bukkit.world.block.data.BukkitBlockState;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.Sign;

public class BukkitBlockEntity implements BlockEntity {
   private final BlockState delegate;

   protected BukkitBlockEntity(BlockState block) {
      this.delegate = block;
   }

   public static BukkitBlockEntity newInstance(BlockState block) {
      if (block instanceof Container) {
         return new BukkitContainer((Container)block);
      } else if (block instanceof Sign) {
         return new BukkitSign((Sign)block);
      } else {
         return (BukkitBlockEntity)(block instanceof CreatureSpawner ? new BukkitMobSpawner((CreatureSpawner)block) : new BukkitBlockEntity(block));
      }
   }

   public BlockState getHandle() {
      return this.delegate;
   }

   public boolean update(boolean applyPhysics) {
      return this.delegate.update(true, applyPhysics);
   }

   public Vector3 getPosition() {
      return BukkitAdapter.adapt(this.delegate.getBlock().getLocation().toVector());
   }

   public int getX() {
      return this.delegate.getX();
   }

   public int getY() {
      return this.delegate.getY();
   }

   public int getZ() {
      return this.delegate.getZ();
   }

   public com.dfsek.terra.api.block.state.BlockState getBlockState() {
      return BukkitBlockState.newInstance(this.delegate.getBlockData());
   }
}
