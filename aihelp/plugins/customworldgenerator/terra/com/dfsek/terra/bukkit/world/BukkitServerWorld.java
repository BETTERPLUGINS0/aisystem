package com.dfsek.terra.bukkit.world;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.Chunk;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.bukkit.BukkitEntity;
import com.dfsek.terra.bukkit.generator.BukkitChunkGeneratorWrapper;
import com.dfsek.terra.bukkit.world.block.state.BukkitBlockEntity;
import com.dfsek.terra.bukkit.world.entity.BukkitEntityType;
import org.bukkit.Location;
import org.bukkit.World;

public class BukkitServerWorld implements ServerWorld {
   private final World delegate;

   public BukkitServerWorld(World delegate) {
      this.delegate = delegate;
   }

   public Entity spawnEntity(double x, double y, double z, EntityType entityType) {
      return new BukkitEntity(this.delegate.spawnEntity(new Location(this.delegate, x, y, z), ((BukkitEntityType)entityType).getHandle()));
   }

   public void setBlockState(int x, int y, int z, BlockState data, boolean physics) {
      this.delegate.getBlockAt(x, y, z).setBlockData(BukkitAdapter.adapt(data), physics);
   }

   public long getSeed() {
      return this.delegate.getSeed();
   }

   public int getMaxHeight() {
      return this.delegate.getMaxHeight();
   }

   public Chunk getChunkAt(int x, int z) {
      return BukkitAdapter.adapt(this.delegate.getChunkAt(x, z));
   }

   public BlockState getBlockState(int x, int y, int z) {
      return BukkitAdapter.adapt(this.delegate.getBlockAt(x, y, z).getBlockData());
   }

   public BlockEntity getBlockEntity(int x, int y, int z) {
      return BukkitBlockEntity.newInstance(this.delegate.getBlockAt(x, y, z).getState());
   }

   public int getMinHeight() {
      return this.delegate.getMinHeight();
   }

   public ChunkGenerator getGenerator() {
      return ((BukkitChunkGeneratorWrapper)this.delegate.getGenerator()).getHandle();
   }

   public BiomeProvider getBiomeProvider() {
      return ((BukkitChunkGeneratorWrapper)this.delegate.getGenerator()).getPack().getBiomeProvider();
   }

   public ConfigPack getPack() {
      return ((BukkitChunkGeneratorWrapper)this.delegate.getGenerator()).getPack();
   }

   public World getHandle() {
      return this.delegate;
   }

   public int hashCode() {
      return this.delegate.hashCode();
   }

   public boolean equals(Object obj) {
      if (obj instanceof BukkitServerWorld) {
         BukkitServerWorld other = (BukkitServerWorld)obj;
         return other.getHandle().equals(this.delegate);
      } else {
         return false;
      }
   }

   public String toString() {
      return this.delegate.toString();
   }
}
