package com.dfsek.terra.bukkit.world.block.data;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.state.properties.Property;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import org.bukkit.block.data.BlockData;

public class BukkitBlockState implements BlockState {
   private final BlockData delegate;

   protected BukkitBlockState(BlockData delegate) {
      this.delegate = delegate;
   }

   public static BlockState newInstance(BlockData bukkitData) {
      return new BukkitBlockState(bukkitData);
   }

   public BlockData getHandle() {
      return this.delegate;
   }

   public boolean matches(BlockState data) {
      return this.delegate.getMaterial() == ((BukkitBlockState)data).getHandle().getMaterial();
   }

   public <T extends Comparable<T>> boolean has(Property<T> property) {
      return false;
   }

   public <T extends Comparable<T>> T get(Property<T> property) {
      return null;
   }

   public <T extends Comparable<T>> BlockState set(Property<T> property, T value) {
      return null;
   }

   public BlockType getBlockType() {
      return BukkitAdapter.adapt(this.delegate.getMaterial());
   }

   public String getAsString(boolean properties) {
      return this.delegate.getAsString(!properties);
   }

   public boolean isAir() {
      return this.delegate.getMaterial().isAir();
   }
}
