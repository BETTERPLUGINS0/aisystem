package org.terraform.data;

import java.util.Objects;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.utils.BlockUtils;

public class Wall extends SimpleBlock {
   private final BlockFace direction;

   public Wall(@NotNull SimpleBlock block, BlockFace dir) {
      super(block.getPopData(), block.getX(), block.getY(), block.getZ());
      this.direction = dir;
   }

   public Wall(@NotNull PopulatorDataAbstract data, @NotNull SimpleLocation loc, BlockFace dir) {
      super(data, loc.getX(), loc.getY(), loc.getZ());
      this.direction = dir;
   }

   public Wall(@NotNull SimpleBlock block) {
      super(block.getPopData(), block.getX(), block.getY(), block.getZ());
      this.direction = BlockFace.NORTH;
   }

   public Wall(@NotNull PopulatorDataAbstract data, int x, int y, int z, BlockFace dir) {
      super(data, x, y, z);
      this.direction = dir;
   }

   @NotNull
   public Wall clone() {
      return new Wall(this.popData, this.getX(), this.getY(), this.getZ(), this.direction);
   }

   @NotNull
   public Wall getAtY(int y) {
      return new Wall(this.popData, this.getX(), y, this.getZ(), this.direction);
   }

   @NotNull
   public Wall getLeft() {
      return new Wall(this.getRelative(BlockUtils.getAdjacentFaces(this.direction)[0]), this.direction);
   }

   @NotNull
   public Wall getUp() {
      return new Wall(super.getUp(), this.direction);
   }

   @NotNull
   public Wall getUp(int i) {
      return new Wall(super.getUp(i), this.direction);
   }

   @NotNull
   public Wall getGround() {
      return new Wall(super.getGround(), this.direction);
   }

   @NotNull
   public Wall getGroundOrDry() {
      return new Wall(super.getGroundOrDry(), this.direction);
   }

   @NotNull
   public Wall getGroundOrSeaLevel() {
      return new Wall(super.getGroundOrSeaLevel(), this.direction);
   }

   public Wall findCeiling(int cutoff) {
      SimpleBlock sb = super.findCeiling(cutoff);
      return sb == null ? null : new Wall(sb, this.direction);
   }

   public Wall findFloor(int cutoff) {
      SimpleBlock sb = super.findFloor(cutoff);
      return sb == null ? null : new Wall(sb, this.direction);
   }

   public Wall findNearestAirPocket(int cutoff) {
      SimpleBlock sb = super.findNearestAirPocket(cutoff);
      return sb == null ? null : new Wall(sb, this.direction);
   }

   public Wall findStonelikeFloor(int cutoff) {
      SimpleBlock sb = super.findStonelikeFloor(cutoff);
      return sb == null ? null : new Wall(sb, this.direction);
   }

   public Wall findStonelikeCeiling(int cutoff) {
      SimpleBlock sb = super.findStonelikeCeiling(cutoff);
      return sb == null ? null : new Wall((SimpleBlock)Objects.requireNonNull(super.findStonelikeCeiling(cutoff)), this.direction);
   }

   @Nullable
   public Wall findRight(int cutoff) {
      for(Wall ceil = this.getRight(); cutoff > 0; ceil = ceil.getRight()) {
         if (ceil.isSolid()) {
            return ceil;
         }

         --cutoff;
      }

      return null;
   }

   @Nullable
   public Wall findDir(@NotNull BlockFace face, int cutoff) {
      for(Wall ceil = this.getRelative(face); cutoff > 0; ceil = ceil.getRelative(face)) {
         if (ceil.isSolid()) {
            return ceil;
         }

         --cutoff;
      }

      return null;
   }

   @Nullable
   public Wall findLeft(int cutoff) {
      for(Wall ceil = this.getLeft(); cutoff > 0; ceil = ceil.getLeft()) {
         if (ceil.isSolid()) {
            return ceil;
         }

         --cutoff;
      }

      return null;
   }

   public Wall getLeft(int it) {
      if (it < 0) {
         return this.getRight(-it);
      } else {
         Wall w = this;

         for(int i = 0; i < it; ++i) {
            w = w.getLeft();
         }

         return w;
      }
   }

   @NotNull
   public Wall getRight() {
      return new Wall(this.getRelative(BlockUtils.getAdjacentFaces(this.direction)[1]), this.direction);
   }

   public Wall getRight(int it) {
      if (it < 0) {
         return this.getLeft(-it);
      } else {
         Wall w = this;

         for(int i = 0; i < it; ++i) {
            w = w.getRight();
         }

         return w;
      }
   }

   /** @deprecated */
   @Deprecated
   @NotNull
   public SimpleBlock get() {
      return this;
   }

   @NotNull
   public Wall getRear() {
      return new Wall(super.getRelative(this.direction.getOppositeFace()), this.direction);
   }

   @NotNull
   public Wall flip() {
      return new Wall(this, this.direction.getOppositeFace());
   }

   public Wall getRear(int it) {
      if (it < 0) {
         return this.getFront(-it);
      } else {
         Wall w = this.clone();

         for(int i = 0; i < it; ++i) {
            w = w.getRear();
         }

         return w;
      }
   }

   @NotNull
   public Wall getFront() {
      return new Wall(super.getRelative(this.direction), this.direction);
   }

   public Wall getFront(int it) {
      if (it < 0) {
         return this.getRear(-it);
      } else {
         Wall w = this.clone();

         for(int i = 0; i < it; ++i) {
            w = w.getFront();
         }

         return w;
      }
   }

   public BlockFace getDirection() {
      return this.direction;
   }

   @NotNull
   public Wall getDown(int i) {
      return new Wall(super.getDown(i), this.direction);
   }

   @NotNull
   public Wall getDown() {
      return new Wall(super.getDown(), this.direction);
   }

   @NotNull
   public Wall getRelative(int x, int y, int z) {
      return new Wall(super.getRelative(x, y, z), this.direction);
   }

   @NotNull
   public Wall getRelative(@NotNull BlockFace face) {
      return new Wall(super.getRelative(face), this.direction);
   }

   @NotNull
   public Wall getRelative(@NotNull BlockFace face, int depth) {
      return new Wall(super.getRelative(face, depth), this.direction);
   }

   public boolean equals(Object obj) {
      return super.equals(obj);
   }

   public void lootTableChest(TerraLootTable table) {
      this.get().getPopData().lootTableChest(this.getX(), this.getY(), this.getZ(), table);
   }
}
