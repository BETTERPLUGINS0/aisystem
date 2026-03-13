package com.nisovin.shopkeepers.util.bukkit;

import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class MutableBlockLocation extends BlockLocation {
   public static MutableBlockLocation of(Block block) {
      Validate.notNull(block, (String)"block is null");
      return new MutableBlockLocation(block.getWorld().getName(), block.getX(), block.getY(), block.getZ());
   }

   public static MutableBlockLocation of(Location location) {
      Validate.notNull(location, (String)"location is null");
      World world = location.getWorld();
      String worldName = world != null ? world.getName() : null;
      return new MutableBlockLocation(worldName, location.getBlockX(), location.getBlockY(), location.getBlockZ());
   }

   public MutableBlockLocation() {
   }

   public MutableBlockLocation(int x, int y, int z) {
      super(x, y, z);
   }

   public MutableBlockLocation(@Nullable String worldName, int x, int y, int z) {
      super(worldName, x, y, z);
   }

   public MutableBlockLocation(double x, double y, double z) {
      super(x, y, z);
   }

   public MutableBlockLocation(@Nullable String worldName, double x, double y, double z) {
      super(worldName, x, y, z);
   }

   public void set(Block block) {
      Validate.notNull(block, (String)"block is null");
      this.set(block.getWorld().getName(), block.getX(), block.getY(), block.getZ());
   }

   public void set(BlockLocation blockLocation) {
      Validate.notNull(blockLocation, (String)"blockLocation is null");
      this.set(blockLocation.getWorldName(), blockLocation.getX(), blockLocation.getY(), blockLocation.getZ());
   }

   public void set(Location location) {
      Validate.notNull(location, (String)"location is null");
      World world = location.getWorld();
      String worldName = world != null ? world.getName() : null;
      this.set(worldName, location.getBlockX(), location.getBlockY(), location.getBlockZ());
   }

   public void set(@Nullable String worldName, int x, int y, int z) {
      this.setWorldName(worldName);
      this.setX(x);
      this.setY(y);
      this.setZ(z);
   }

   public void setWorldName(@Nullable String worldName) {
      super.setWorldName(worldName);
   }

   public void setX(int x) {
      super.setX(x);
   }

   public void setY(int y) {
      super.setY(y);
   }

   public void setZ(int z) {
      super.setZ(z);
   }

   public BlockLocation immutable() {
      return new BlockLocation(this.getWorldName(), this.getX(), this.getY(), this.getZ());
   }
}
