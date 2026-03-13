package org.terraform.structure.pyramid;

import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.block.data.type.Slab.Type;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;
import org.terraform.schematic.SchematicParser;
import org.terraform.schematic.TerraSchematic;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomPopulatorAbstract;
import org.terraform.utils.BlockUtils;

public class ElderGuardianChamber extends RoomPopulatorAbstract {
   public ElderGuardianChamber(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      SimpleBlock base = new SimpleBlock(data, room.getX(), room.getY() + 1, room.getZ());
      BlockFace[] var4 = BlockUtils.directBlockFaces;
      int var5 = var4.length;

      int var6;
      BlockFace face;
      for(var6 = 0; var6 < var5; ++var6) {
         face = var4[var6];
         this.placeStatue(base.getRelative(face, 4), face.getOppositeFace());
      }

      var4 = BlockUtils.xzDiagonalPlaneBlockFaces;
      var5 = var4.length;

      for(var6 = 0; var6 < var5; ++var6) {
         face = var4[var6];
         this.placePillar(new Wall(base.getRelative(face, 6)), room.getHeight() - 1);
      }

      SimpleBlock center = new SimpleBlock(data, room.getX(), room.getY(), room.getZ());
      center.setType(Material.BLUE_TERRACOTTA);
      BlockFace[] var13 = BlockUtils.xzDiagonalPlaneBlockFaces;
      var6 = var13.length;

      BlockFace face;
      int var15;
      for(var15 = 0; var15 < var6; ++var15) {
         face = var13[var15];
         center.getRelative(face).setType(Material.ORANGE_TERRACOTTA);
         (new Wall(center.getRelative(face).getRelative(face).getUp())).Pillar(room.getHeight(), this.rand, new Material[]{Material.CUT_SANDSTONE});
      }

      var13 = BlockUtils.directBlockFaces;
      var6 = var13.length;

      for(var15 = 0; var15 < var6; ++var15) {
         face = var13[var15];
         center.getRelative(face).getRelative(face).setType(Material.ORANGE_TERRACOTTA);
      }

      SimpleBlock ceiling = new SimpleBlock(data, room.getX(), room.getY() + room.getHeight(), room.getZ());
      ceiling.setType(Material.BLUE_TERRACOTTA);
      BlockFace[] var16 = BlockUtils.xzDiagonalPlaneBlockFaces;
      var15 = var16.length;

      BlockFace face;
      int var17;
      for(var17 = 0; var17 < var15; ++var17) {
         face = var16[var17];
         ceiling.getRelative(face).setType(Material.ORANGE_TERRACOTTA);
      }

      var16 = BlockUtils.directBlockFaces;
      var15 = var16.length;

      for(var17 = 0; var17 < var15; ++var17) {
         face = var16[var17];
         ceiling.getRelative(face).getRelative(face).setType(Material.ORANGE_TERRACOTTA);
      }

      if (TConfig.c.STRUCTURES_PYRAMID_SPAWN_ELDER_GUARDIAN) {
         SimpleBlock cageCenter = center.getUp(11);
         this.placeElderGuardianCage(cageCenter);
      }

      Iterator var19 = room.getFourWalls(data, 0).entrySet().iterator();

      Entry entry;
      int length;
      Wall w;
      while(var19.hasNext()) {
         entry = (Entry)var19.next();
         w = (Wall)entry.getKey();

         for(length = 0; length < (Integer)entry.getValue(); ++length) {
            if (length % 2 == 0 && length != 0 && length != (Integer)entry.getValue() - 1) {
               w.getUp(4).Pillar(10, this.rand, new Material[]{Material.CHISELED_RED_SANDSTONE});
            }

            w = w.getLeft();
         }
      }

      var19 = room.getFourWalls(data, 1).entrySet().iterator();

      while(var19.hasNext()) {
         entry = (Entry)var19.next();
         w = ((Wall)entry.getKey()).getRelative(0, room.getHeight() - 2, 0);
         length = (Integer)entry.getValue();

         for(int j = 0; j < length; ++j) {
            Stairs stair = (Stairs)Bukkit.createBlockData(Material.RED_SANDSTONE_STAIRS);
            stair.setFacing(w.getDirection().getOppositeFace());
            stair.setHalf(Half.TOP);
            w.setBlockData(stair);
            w = w.getLeft();
         }
      }

   }

   private void placeElderGuardianCage(@NotNull SimpleBlock cageCenter) {
      cageCenter.getDown(2).setType(Material.CUT_SANDSTONE);
      cageCenter.getUp(2).setType(Material.CUT_SANDSTONE);
      BlockFace[] var2 = BlockUtils.directBlockFaces;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         BlockFace face = var2[var4];
         Wall w = new Wall(cageCenter, face);
         w.getFront(2).setType(Material.CHISELED_SANDSTONE);
         w.getFront(2).getLeft().setType(Material.CHISELED_SANDSTONE);
         w.getFront(2).getRight().setType(Material.CHISELED_SANDSTONE);
         Stairs stair = (Stairs)Bukkit.createBlockData(Material.SANDSTONE_STAIRS);
         stair.setFacing(face.getOppositeFace());
         w.getFront(2).getUp().setBlockData(stair);
         stair = (Stairs)Bukkit.createBlockData(Material.SANDSTONE_STAIRS);
         stair.setFacing(face.getOppositeFace());
         stair.setHalf(Half.TOP);
         w.getFront(2).getDown().setBlockData(stair);
         w.getFront().getUp(2).setType(Material.CUT_SANDSTONE_SLAB);
         Slab slab = (Slab)Bukkit.createBlockData(Material.CUT_SANDSTONE_SLAB);
         slab.setType(Type.TOP);
         w.getFront().getDown(2).setBlockData(slab);
      }

      cageCenter.getPopData().addEntity(cageCenter.getX(), cageCenter.getY(), cageCenter.getZ(), EntityType.ELDER_GUARDIAN);
   }

   private void placePillar(@NotNull Wall base, int height) {
      BlockFace[] var3 = BlockUtils.xzDiagonalPlaneBlockFaces;
      int var4 = var3.length;

      int var5;
      BlockFace face;
      for(var5 = 0; var5 < var4; ++var5) {
         face = var3[var5];
         base.getRelative(face).Pillar(height, this.rand, new Material[]{Material.CUT_SANDSTONE, Material.SMOOTH_SANDSTONE});
      }

      var3 = BlockUtils.directBlockFaces;
      var4 = var3.length;

      for(var5 = 0; var5 < var4; ++var5) {
         face = var3[var5];
         base.getRelative(face).Pillar(height, true, this.rand, new Material[]{Material.CUT_SANDSTONE, Material.CHISELED_SANDSTONE, Material.AIR, Material.AIR, Material.AIR, Material.CHISELED_SANDSTONE});
      }

      base.Pillar(height, this.rand, new Material[]{Material.CHISELED_RED_SANDSTONE});
   }

   private void placeStatue(SimpleBlock base, BlockFace dir) {
      try {
         TerraSchematic schema = TerraSchematic.load("pharoah-statue", base);
         schema.parser = new SchematicParser();
         schema.setFace(dir);
         schema.apply();
      } catch (Throwable var4) {
         TerraformGeneratorPlugin.logger.stackTrace(var4);
      }

   }

   public boolean canPopulate(CubeRoom room) {
      return true;
   }
}
