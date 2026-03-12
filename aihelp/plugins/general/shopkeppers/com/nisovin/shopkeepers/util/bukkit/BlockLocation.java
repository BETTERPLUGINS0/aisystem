package com.nisovin.shopkeepers.util.bukkit;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.util.ChunkCoords;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import org.checkerframework.checker.nullness.qual.Nullable;

public class BlockLocation {
   private static final Vector VECTOR_ZERO = new Vector();
   private static final Vector BLOCK_CENTER_OFFSET = new Vector(0.5D, 0.5D, 0.5D);
   public static final BlockLocation EMPTY = new BlockLocation();
   @Nullable
   private String worldName;
   private int x;
   private int y;
   private int z;

   public static BlockLocation of(Block block) {
      Validate.notNull(block, (String)"block is null");
      return new BlockLocation(block.getWorld().getName(), block.getX(), block.getY(), block.getZ());
   }

   public static BlockLocation of(Location location) {
      Validate.notNull(location, (String)"location is null");
      World world = location.getWorld();
      String worldName = world != null ? world.getName() : null;
      return new BlockLocation(worldName, location.getBlockX(), location.getBlockY(), location.getBlockZ());
   }

   public static int toBlock(double coordinate) {
      return Location.locToBlock(coordinate);
   }

   public BlockLocation() {
      this((String)null, 0, 0, 0);
   }

   public BlockLocation(int x, int y, int z) {
      this((String)null, x, y, z);
   }

   public BlockLocation(@Nullable String worldName, int x, int y, int z) {
      validateWorldName(worldName);
      this.worldName = worldName;
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public BlockLocation(double x, double y, double z) {
      this((String)null, x, y, z);
   }

   public BlockLocation(@Nullable String worldName, double x, double y, double z) {
      this(worldName, toBlock(x), toBlock(y), toBlock(z));
   }

   private static void validateWorldName(@Nullable String worldName) {
      Validate.isTrue(worldName == null || !worldName.isEmpty(), "worldName is empty");
   }

   @Nullable
   public final String getWorldName() {
      return this.worldName;
   }

   public final boolean hasWorldName() {
      return this.worldName != null;
   }

   protected void setWorldName(@Nullable String worldName) {
      validateWorldName(worldName);
      this.worldName = worldName;
   }

   public final int getX() {
      return this.x;
   }

   protected void setX(int x) {
      this.x = x;
   }

   public final int getY() {
      return this.y;
   }

   protected void setY(int y) {
      this.y = y;
   }

   public final int getZ() {
      return this.z;
   }

   protected void setZ(int z) {
      this.z = z;
   }

   public final boolean isEmpty() {
      return !this.hasWorldName() && this.x == 0 && this.y == 0 && this.z == 0;
   }

   @Nullable
   public final World getWorld() {
      return !this.hasWorldName() ? null : Bukkit.getWorld((String)Unsafe.assertNonNull(this.worldName));
   }

   @Nullable
   public final Block getBlock() {
      World world = this.getWorld();
      return world == null ? null : this.getBlock(world);
   }

   public final Block getBlock(World world) {
      return this.getBlockAtOffset(world, 0, 0, 0);
   }

   public final Block getBlockAtOffset(World world, int offsetX, int offsetY, int offsetZ) {
      Validate.notNull(world, (String)"world");
      return world.getBlockAt(this.x + offsetX, this.y + offsetY, this.z + offsetZ);
   }

   @Nullable
   public final ChunkCoords getChunkCoords() {
      return !this.hasWorldName() ? null : ChunkCoords.fromBlock((String)Unsafe.assertNonNull(this.worldName), this.x, this.z);
   }

   public final boolean matches(@Nullable String worldName, int x, int y, int z) {
      if (this.x != x) {
         return false;
      } else if (this.y != y) {
         return false;
      } else if (this.z != z) {
         return false;
      } else {
         return Objects.equals(this.worldName, worldName);
      }
   }

   public final boolean matches(@Nullable Block block) {
      return block == null ? false : this.matches(block.getWorld().getName(), block.getX(), block.getY(), block.getZ());
   }

   public final boolean isSameWorld(Location location) {
      Validate.notNull(location, (String)"location is null");
      World world = location.getWorld();
      String otherWorldName = world != null ? world.getName() : null;
      return Objects.equals(this.worldName, otherWorldName);
   }

   public final double getDistanceSquared(Location location) {
      return this.getDistanceSquared(VECTOR_ZERO, location);
   }

   public final double getDistanceSquared(Vector offset, Location location) {
      Validate.notNull(offset, (String)"offset is null");
      if (!this.isSameWorld(location)) {
         return Double.MAX_VALUE;
      } else {
         double dx = (double)this.getX() + offset.getX() - location.getX();
         double dy = (double)this.getY() + offset.getY() - location.getY();
         double dz = (double)this.getZ() + offset.getZ() - location.getZ();
         return dx * dx + dy * dy + dz * dz;
      }
   }

   public final double getBlockCenterDistanceSquared(Location location) {
      return this.getDistanceSquared(BLOCK_CENTER_OFFSET, location);
   }

   public final MutableBlockLocation mutableCopy() {
      return new MutableBlockLocation(this.worldName, this.x, this.y, this.z);
   }

   public BlockLocation immutable() {
      return this;
   }

   public final String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(this.getClass().getSimpleName());
      builder.append(" [worldName=");
      builder.append(this.worldName);
      builder.append(", x=");
      builder.append(this.x);
      builder.append(", y=");
      builder.append(this.y);
      builder.append(", z=");
      builder.append(this.z);
      builder.append("]");
      return builder.toString();
   }

   public final int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.worldName != null ? this.worldName.hashCode() : 0);
      result = 31 * result + this.x;
      result = 31 * result + this.y;
      result = 31 * result + this.z;
      return result;
   }

   public final boolean equals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof BlockLocation)) {
         return false;
      } else {
         BlockLocation other = (BlockLocation)obj;
         return this.matches(other.worldName, other.x, other.y, other.z);
      }
   }
}
