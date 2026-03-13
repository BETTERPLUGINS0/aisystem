package org.terraform.data;

import com.google.gson.annotations.SerializedName;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Random;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.coregen.populatordata.IPopulatorDataPhysicsCapable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.coregen.populatordata.PopulatorDataPostGen;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class SimpleBlock {
   @NotNull
   final PopulatorDataAbstract popData;
   @SerializedName("w")
   private final int x;
   @SerializedName("w")
   private final int y;
   @SerializedName("w")
   private final int z;

   public SimpleBlock(@NotNull Location loc) {
      this.popData = new PopulatorDataPostGen(loc.getChunk());
      this.x = loc.getBlockX();
      this.y = loc.getBlockY();
      this.z = loc.getBlockZ();
   }

   public SimpleBlock(@NotNull PopulatorDataAbstract data, @NotNull Vector loc) {
      this.popData = data;
      this.x = (int)Math.round(loc.getX());
      this.y = (int)Math.round(loc.getY());
      this.z = (int)Math.round(loc.getZ());
   }

   public SimpleBlock(@NotNull PopulatorDataAbstract data, int x, int y, int z) {
      this.popData = data;
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public SimpleBlock(@NotNull PopulatorDataAbstract data, @NotNull SimpleLocation sLoc) {
      this.popData = data;
      this.x = sLoc.getX();
      this.y = sLoc.getY();
      this.z = sLoc.getZ();
   }

   public SimpleBlock(@NotNull PopulatorDataAbstract data, @NotNull Location loc) {
      this.popData = data;
      this.x = (int)loc.getX();
      this.y = (int)loc.getY();
      this.z = (int)loc.getZ();
   }

   public SimpleBlock(@NotNull PopulatorDataAbstract data, @NotNull Block b) {
      this.popData = data;
      this.x = b.getX();
      this.y = b.getY();
      this.z = b.getZ();
   }

   public void pathTowards(int width, int maxLength, @NotNull SimpleBlock target, Material... types) {
      BlockFace dir = BlockFace.NORTH;
      int max = -1;
      if (target.getX() - this.getX() > max) {
         dir = BlockFace.EAST;
      } else if (this.getX() - target.getX() > max) {
         dir = BlockFace.WEST;
      } else if (this.getZ() - target.getZ() > max) {
         dir = BlockFace.NORTH;
      } else if (target.getZ() - this.getZ() > max) {
         dir = BlockFace.SOUTH;
      }

      SimpleBlock base = this;

      for(int i = 0; i < maxLength && base.lsetType(types); ++i) {
         for(int w = 0; w < width; ++w) {
            BlockFace[] var10 = BlockUtils.getAdjacentFaces(dir);
            int var11 = var10.length;

            for(int var12 = 0; var12 < var11; ++var12) {
               BlockFace adj = var10[var12];
               base.getRelative(adj).setType(types);
            }
         }

         base = base.getRelative(dir);
      }

   }

   @NotNull
   public SimpleLocation getLoc() {
      return new SimpleLocation(this.x, this.y, this.z);
   }

   @NotNull
   public SimpleBlock getAtY(int y) {
      return new SimpleBlock(this.popData, this.x, y, this.z);
   }

   public double distanceSquared(@NotNull SimpleBlock other) {
      float selfX = (float)this.x;
      float selfY = (float)this.y;
      float selfZ = (float)this.z;
      float oX = (float)other.x;
      float oY = (float)other.y;
      float oZ = (float)other.z;
      return Math.pow((double)(selfX - oX), 2.0D) + Math.pow((double)(selfY - oY), 2.0D) + Math.pow((double)(selfZ - oZ), 2.0D);
   }

   public boolean isConnected(SimpleBlock other) {
      BlockFace[] var2 = BlockUtils.sixBlockFaces;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         BlockFace face = var2[var4];
         if (this.getRelative(face).equals(other)) {
            return true;
         }
      }

      return false;
   }

   public double distance(@NotNull SimpleBlock other) {
      return Math.sqrt(this.distanceSquared(other));
   }

   public boolean sameLocation(@NotNull SimpleBlock other) {
      return other.x == this.x && other.y == this.y && other.z == this.z;
   }

   @NotNull
   public Vector toVector() {
      return new Vector(this.x, this.y, this.z);
   }

   @NotNull
   public SimpleBlock untilSolid(@NotNull BlockFace face) {
      SimpleBlock rel;
      for(rel = this.getRelative(face); !rel.isSolid(); rel = rel.getRelative(face)) {
      }

      return rel;
   }

   public boolean lsetType(@NotNull Material type) {
      if (!this.isSolid()) {
         this.setType(type);
         return true;
      } else {
         return false;
      }
   }

   public void lsetBlockData(BlockData data) {
      if (!this.isSolid()) {
         this.setBlockData(data);
      }

   }

   @Nullable
   public BlockData getBlockData() {
      return this.popData.getBlockData(this.x, this.y, this.z);
   }

   public void setBlockData(BlockData dat) {
      if (this.popData.getType(this.x, this.y, this.z) == Material.WATER && dat instanceof Waterlogged) {
         Waterlogged wl = (Waterlogged)dat;
         wl.setWaterlogged(true);
      }

      this.popData.setBlockData(this.x, this.y, this.z, dat);
   }

   public void RSolSetBlockData(BlockData data) {
      if (this.isSolid()) {
         this.setBlockData(data);
      }

   }

   @NotNull
   public SimpleBlock getRelative(int nx, int ny, int nz) {
      return new SimpleBlock(this.popData, this.x + nx, this.y + ny, this.z + nz);
   }

   @NotNull
   public SimpleBlock getRelative(@NotNull Vector v) {
      return new SimpleBlock(this.popData, (int)Math.round((double)this.x + v.getX()), (int)Math.round((double)this.y + v.getY()), (int)Math.round((double)this.z + v.getZ()));
   }

   @NotNull
   public String getCoords() {
      return this.x + "," + this.y + "," + this.z;
   }

   @NotNull
   public SimpleBlock getRelative(@NotNull BlockFace face) {
      return new SimpleBlock(this.popData, this.x + face.getModX(), this.y + face.getModY(), this.z + face.getModZ());
   }

   @NotNull
   public SimpleBlock getRelative(@NotNull BlockFace face, int count) {
      return new SimpleBlock(this.popData, this.x + face.getModX() * count, this.y + face.getModY() * count, this.z + face.getModZ() * count);
   }

   public void addEntity(EntityType type) {
      this.popData.addEntity(this.x, this.y, this.z, type);
   }

   public int countAdjacentsThatMatchType(@NotNull BlockFace[] faces, @NotNull Material... types) {
      int i = 0;
      BlockFace[] var4 = faces;
      int var5 = faces.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         BlockFace face = var4[var6];
         Material[] var8 = types;
         int var9 = types.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            Material type = var8[var10];
            if (this.getRelative(face).getType() == type) {
               ++i;
            }
         }
      }

      return i;
   }

   public boolean doAdjacentsMatchType(@NotNull BlockFace[] faces, @NotNull Material... types) {
      BlockFace[] var3 = faces;
      int var4 = faces.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         BlockFace face = var3[var5];
         Material[] var7 = types;
         int var8 = types.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            Material type = var7[var9];
            if (this.getRelative(face).getType() == type) {
               return true;
            }
         }
      }

      return false;
   }

   public void replaceAdjacentNonLiquids(@NotNull BlockFace[] faces, Material liquid, Material... types) {
      BlockFace[] var4 = faces;
      int var5 = faces.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         BlockFace face = var4[var6];
         if (!this.getRelative(face).isSolid() && this.getRelative(face).getType() != liquid) {
            this.getRelative(face).setType(types);
         }
      }

   }

   public boolean hasAdjacentSolid(@NotNull BlockFace[] faces) {
      BlockFace[] var2 = faces;
      int var3 = faces.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         BlockFace face = var2[var4];
         if (this.getRelative(face).isSolid()) {
            return true;
         }
      }

      return false;
   }

   public int getChunkX() {
      return this.x >> 4;
   }

   public int getChunkZ() {
      return this.z >> 4;
   }

   @NotNull
   public SimpleChunkLocation getSChunk(String world) {
      return new SimpleChunkLocation(world, this.getChunkX(), this.getChunkZ());
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public int getZ() {
      return this.z;
   }

   @NotNull
   public Material getType() {
      return this.popData.getType(this.x, this.y, this.z);
   }

   public void setType(@NotNull Material type) {
      BlockData l;
      if (this.popData.getType(this.x, this.y, this.z) == Material.WATER) {
         l = Bukkit.createBlockData(type);
         if (l instanceof Waterlogged) {
            Waterlogged wl = (Waterlogged)l;
            wl.setWaterlogged(true);
         }

         this.popData.setBlockData(this.x, this.y, this.z, l);
      } else {
         this.popData.setType(this.x, this.y, this.z, type);
      }

      if (Tag.LEAVES.isTagged(type)) {
         l = Bukkit.createBlockData(type);
         if (l instanceof Leaves) {
            Leaves leaves = (Leaves)l;
            leaves.setPersistent(true);
         }

         this.setBlockData(l);
      }

   }

   public void setType(Material... types) {
      this.setType((Material)GenUtils.randChoice((Object[])types));
   }

   public boolean isAir() {
      return this.popData.getType(this.x, this.y, this.z) == Material.AIR || this.popData.getType(this.x, this.y, this.z) == Material.CAVE_AIR;
   }

   public boolean isSolid() {
      return this.popData.getType(this.x, this.y, this.z).isSolid();
   }

   public void physicsSetType(@NotNull Material type, boolean updatePhysics) {
      if (this.popData instanceof IPopulatorDataPhysicsCapable) {
         BlockData l;
         if (this.popData.getType(this.x, this.y, this.z) == Material.WATER) {
            l = Bukkit.createBlockData(type);
            if (l instanceof Waterlogged) {
               Waterlogged wl = (Waterlogged)l;
               wl.setWaterlogged(true);
            }

            ((IPopulatorDataPhysicsCapable)this.popData).setBlockData(this.x, this.y, this.z, l, updatePhysics);
         } else {
            ((IPopulatorDataPhysicsCapable)this.popData).setType(this.x, this.y, this.z, type, updatePhysics);
         }

         if (Tag.LEAVES.isTagged(type)) {
            l = Bukkit.createBlockData(type);
            if (l instanceof Leaves) {
               Leaves leaves = (Leaves)l;
               leaves.setPersistent(true);
            }

            ((IPopulatorDataPhysicsCapable)this.popData).setBlockData(this.x, this.y, this.z, l, updatePhysics);
         }
      } else {
         this.setType(type);
      }

   }

   public void physicsSetBlockData(BlockData dat, boolean updatePhysics) {
      if (this.popData instanceof IPopulatorDataPhysicsCapable) {
         if (this.popData.getType(this.x, this.y, this.z) == Material.WATER && dat instanceof Waterlogged) {
            Waterlogged wl = (Waterlogged)dat;
            wl.setWaterlogged(true);
         }

         ((IPopulatorDataPhysicsCapable)this.popData).setBlockData(this.x, this.y, this.z, dat, updatePhysics);
      } else {
         this.setBlockData(dat);
      }

   }

   public boolean lsetType(Material... types) {
      return this.lsetType((Material)GenUtils.randChoice((Object[])types));
   }

   public void RSolSetType(@NotNull Material type) {
      if (this.isSolid()) {
         this.setType(type);
      }

   }

   @NotNull
   public PopulatorDataAbstract getPopData() {
      return this.popData;
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + this.popData.getTerraformWorld().hashCode();
      result = 31 * result + this.x;
      result = 31 * result + this.y;
      result = 31 * result + this.z;
      return result;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof SimpleBlock)) {
         return false;
      } else {
         SimpleBlock other = (SimpleBlock)obj;
         return Objects.equals(this.popData.getTerraformWorld(), other.getPopData().getTerraformWorld()) && this.x == other.x && this.z == other.z && this.y == other.y;
      }
   }

   @NotNull
   public SimpleBlock getGround() {
      return new SimpleBlock(this.popData, this.x, GenUtils.getHighestGround(this.popData, this.x, this.z), this.z);
   }

   @NotNull
   public SimpleBlock getGroundOrSeaLevel() {
      int y = GenUtils.getHighestGround(this.popData, this.x, this.z);
      if (y < TerraformGenerator.seaLevel) {
         y = TerraformGenerator.seaLevel;
      }

      return new SimpleBlock(this.popData, this.x, y, this.z);
   }

   @NotNull
   public SimpleBlock getGroundOrDry() {
      int y;
      for(y = GenUtils.getHighestGround(this.popData, this.x, this.z); y < TerraformGeneratorPlugin.injector.getMaxY() && (BlockUtils.isWet(this.getAtY(y + 1)) || Tag.ICE.isTagged(this.getAtY(y + 1).getType())); ++y) {
      }

      return new SimpleBlock(this.popData, this.x, y, this.z);
   }

   @NotNull
   public SimpleBlock getUp() {
      return this.getRelative(0, 1, 0);
   }

   @NotNull
   public SimpleBlock getUp(int i) {
      return this.getRelative(0, i, 0);
   }

   @Nullable
   public SimpleBlock findCeiling(int cutoff) {
      for(SimpleBlock ceil = this.getUp(); cutoff > 0; ceil = ceil.getUp()) {
         if (ceil.isSolid() && ceil.getType() != Material.LANTERN) {
            return ceil;
         }

         --cutoff;
      }

      return null;
   }

   @Nullable
   public SimpleBlock findFloor(int cutoff) {
      for(SimpleBlock floor = this.getDown(); cutoff > 0 && floor.getY() >= TerraformGeneratorPlugin.injector.getMinY(); floor = floor.getDown()) {
         if (floor.isSolid() && floor.getType() != Material.LANTERN) {
            return floor;
         }

         --cutoff;
      }

      return null;
   }

   @Nullable
   public SimpleBlock findAirPocket(int cutoff) {
      for(SimpleBlock floor = this.getDown(); cutoff > 0 && floor.getY() >= TerraformGeneratorPlugin.injector.getMinY(); floor = floor.getDown()) {
         if (!floor.isSolid()) {
            return floor;
         }

         --cutoff;
      }

      return null;
   }

   @Nullable
   public SimpleBlock findNearestAirPocket(int cutoff) {
      SimpleBlock rel;
      if (this.isSolid()) {
         for(rel = this.getUp(); cutoff > 0; rel = rel.getUp()) {
            if (!rel.isSolid()) {
               return rel;
            }

            --cutoff;
         }

         return null;
      } else {
         rel = this.findFloor(cutoff);
         if (rel != null) {
            rel = rel.getUp();
         }

         return rel;
      }
   }

   @Nullable
   public SimpleBlock findStonelikeFloor(int cutoff) {
      for(SimpleBlock floor = this.getDown(); cutoff > 0 && floor.getY() >= TerraformGeneratorPlugin.injector.getMinY(); floor = floor.getDown()) {
         if (BlockUtils.isStoneLike(floor.getType())) {
            return floor;
         }

         --cutoff;
      }

      return null;
   }

   @Nullable
   public SimpleBlock findStonelikeCeiling(int cutoff) {
      for(SimpleBlock ceil = this.getUp(); cutoff > 0; ceil = ceil.getUp()) {
         if (BlockUtils.isStoneLike(ceil.getType())) {
            return ceil;
         }

         --cutoff;
      }

      return null;
   }

   public void Pillar(int height, Material... types) {
      Random rand = new Random();

      for(int i = 0; i < height; ++i) {
         this.getRelative(0, i, 0).setType((Material)GenUtils.randChoice(rand, types));
      }

   }

   public void fluidize() {
      Material fluid = Material.AIR;
      if (!BlockUtils.isWet(this)) {
         BlockFace[] var2 = BlockUtils.directBlockFaces;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            BlockFace face = var2[var4];
            if (this.getRelative(face).getType() == Material.WATER) {
               fluid = Material.WATER;
            } else if (this.getRelative(face).getType() == Material.LAVA) {
               fluid = Material.LAVA;
            }
         }
      } else {
         fluid = Material.WATER;
      }

      this.setType(fluid);
   }

   public void Pillar(int height, @NotNull Random rand, Material... types) {
      for(int i = 0; i < height; ++i) {
         this.getRelative(0, i, 0).setType((Material)GenUtils.randChoice(rand, types));
      }

   }

   public void Pillar(int height, boolean pattern, @NotNull Random rand, @NotNull Material... types) {
      for(int i = 0; i < height; ++i) {
         if (!Arrays.equals(new Material[]{Material.BARRIER}, types)) {
            if (!pattern) {
               this.getRelative(0, i, 0).setType((Material)GenUtils.randChoice(rand, types));
            } else if (types[i % types.length] != Material.BARRIER) {
               this.getRelative(0, i, 0).setType(types[i % types.length]);
            }
         }
      }

   }

   public void CorrectMultipleFacing(int height) {
      for(int i = 0; i < height; ++i) {
         BlockUtils.correctSurroundingMultifacingData(this.getRelative(0, i, 0));
      }

   }

   public void LPillar(int height, Material... types) {
      this.LPillar(height, false, new Random(), types);
   }

   public int LPillar(int height, @NotNull Random rand, Material... types) {
      return this.LPillar(height, false, rand, types);
   }

   public int LPillar(int height, boolean pattern, @NotNull Random rand, @NotNull Material... types) {
      for(int i = 0; i < height; ++i) {
         if (this.getRelative(0, i, 0).isSolid()) {
            return i;
         }

         if (!Arrays.equals(new Material[]{Material.BARRIER}, types)) {
            if (!pattern) {
               this.getRelative(0, i, 0).setType((Material)GenUtils.randChoice(rand, types));
            } else {
               this.getRelative(0, i, 0).setType(types[i % types.length]);
            }
         }
      }

      return height;
   }

   public void RPillar(int height, @NotNull Random rand, Material... types) {
      for(int i = 0; i < height; ++i) {
         if (!this.getRelative(0, i, 0).isSolid()) {
            this.getRelative(0, i, 0).setType((Material)GenUtils.randChoice(rand, types));
         }
      }

   }

   public void ReplacePillar(int height, Material... types) {
      for(int i = 0; i < height; ++i) {
         if (this.getRelative(0, i, 0).isSolid()) {
            this.getRelative(0, i, 0).setType((Material)GenUtils.randChoice((Object[])types));
         }
      }

   }

   public void CAPillar(int height, @NotNull Random rand, Material... types) {
      for(int i = 0; i < height; ++i) {
         if (this.getRelative(0, i, 0).getType() != Material.CAVE_AIR) {
            this.getRelative(0, i, 0).setType((Material)GenUtils.randChoice(rand, types));
         }
      }

   }

   public void waterlog(int height) {
      for(int i = 0; i < height; ++i) {
         SimpleBlock rel = this.getRelative(0, i, 0);
         BlockData var5 = rel.getBlockData();
         if (var5 instanceof Waterlogged) {
            Waterlogged log = (Waterlogged)var5;
            log.setWaterlogged(true);
            rel.setBlockData(log);
         }
      }

   }

   public int downUntilSolid(@NotNull Random rand, Material... types) {
      int depth = 0;

      for(int y = this.y; y > TerraformGeneratorPlugin.injector.getMinY() && !this.getRelative(0, -depth, 0).isSolid(); --y) {
         this.getRelative(0, -depth, 0).setType((Material)GenUtils.randChoice(rand, types));
         ++depth;
      }

      return depth;
   }

   public int blockfaceUntilSolid(int maxDepth, @NotNull Random rand, @NotNull BlockFace face, Material... types) {
      int depth;
      for(depth = 0; depth <= maxDepth && !this.getRelative(face).isSolid(); ++depth) {
         this.getRelative(face).setType((Material)GenUtils.randChoice(rand, types));
      }

      return depth;
   }

   public int blockface(int maxDepth, @NotNull Random rand, @NotNull BlockFace face, Material... types) {
      int depth;
      for(depth = 0; depth <= maxDepth; ++depth) {
         this.getRelative(face).setType((Material)GenUtils.randChoice(rand, types));
      }

      return depth;
   }

   public void downPillar(int h, Material... types) {
      this.downPillar(new Random(), h, types);
   }

   public void downPillar(@NotNull Random rand, int h, Material... types) {
      int depth = 0;

      for(int y = this.y; y > TerraformGeneratorPlugin.injector.getMinY() && depth < h; --y) {
         this.getRelative(0, -depth, 0).setType((Material)GenUtils.randChoice(rand, types));
         ++depth;
      }

   }

   public void downLPillar(@NotNull Random rand, int h, Material... types) {
      int depth = 0;

      for(int y = this.y; y > TerraformGeneratorPlugin.injector.getMinY() && depth < h && !this.getRelative(0, -depth, 0).isSolid(); --y) {
         this.getRelative(0, -depth, 0).setType((Material)GenUtils.randChoice(rand, types));
         ++depth;
      }

   }

   public void downRPillar(@NotNull Random rand, int h, Material... types) {
      int depth = 0;

      for(int y = this.y; y > TerraformGeneratorPlugin.injector.getMinY() && depth < h; --y) {
         if (!this.getRelative(0, -depth, 0).isSolid()) {
            this.getRelative(0, -depth, 0).setType((Material)GenUtils.randChoice(rand, types));
         }

         ++depth;
      }

   }

   public void directionalLPillar(@NotNull Random rand, @NotNull BlockFace face, int h, Material... types) {
      int depth = 0;

      for(int y = this.y; y > TerraformGeneratorPlugin.injector.getMinY() && depth < h && !this.getRelative(face, depth).isSolid(); --y) {
         this.getRelative(face, depth).setType((Material)GenUtils.randChoice(rand, types));
         ++depth;
      }

   }

   @NotNull
   public SimpleBlock getDown(int i) {
      return this.getRelative(0, -i, 0);
   }

   @NotNull
   public SimpleBlock getDown() {
      return this.getRelative(0, -1, 0);
   }

   @NotNull
   public String toString() {
      return this.x + "," + this.y + "," + this.z;
   }

   public void rsetType(@NotNull EnumSet<Material> toReplace, Material... type) {
      this.popData.rsetType(this.toVector(), toReplace, type);
   }

   public void rsetBlockData(@NotNull EnumSet<Material> toReplace, BlockData data) {
      this.popData.rsetBlockData(this.toVector(), toReplace, data);
   }
}
