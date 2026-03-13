package org.terraform.structure.village.plains;

import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Bell;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.block.data.type.Bell.Attachment;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.data.Wall;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.schematic.TerraSchematic;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomPopulatorAbstract;
import org.terraform.structure.villagehouse.farmhouse.FarmhouseSchematicParser;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.WoodUtils;

public class PlainsVillageTownhallPopulator extends RoomPopulatorAbstract {
   private final TerraformWorld tw;
   private int elevation;

   public PlainsVillageTownhallPopulator(TerraformWorld tw, Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
      this.tw = tw;
      this.elevation = GenUtils.randInt(this.rand, 2, 4);
   }

   public void setElevation(int elevation) {
      this.elevation = elevation;
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      int x = room.getX();
      int z = room.getZ();
      int y = GenUtils.getHighestGround(data, x, z);

      try {
         BiomeBank biome = this.tw.getBiomeBank(x, z);
         y += this.elevation;
         TerraSchematic farmHouse = TerraSchematic.load("farmhouse", new SimpleBlock(data, x, y, z));
         farmHouse.parser = new FarmhouseSchematicParser(biome, this.rand, data);
         BlockFace face = BlockUtils.getDirectBlockFace(this.rand);
         if (room instanceof DirectionalCubeRoom) {
            face = ((DirectionalCubeRoom)room).getDirection();
         }

         farmHouse.setFace(face);
         farmHouse.apply();
         TerraformGeneratorPlugin.logger.info("Spawning farmhouse at " + x + "," + y + "," + z + " with rotation of " + String.valueOf(farmHouse.getFace()));
         data.addEntity(x, y + 1, z, EntityType.VILLAGER);
         data.addEntity(x, y + 1, z, EntityType.VILLAGER);
         data.addEntity(x, y + 1, z, EntityType.CAT);

         int nz;
         for(int nx = -9; nx <= 9; ++nx) {
            for(nz = -9; nz <= 9; ++nz) {
               if (!data.getType(x + nx, y - 1, z + nz).toString().contains("PLANKS") && !data.getType(x + nx, y - 1, z + nz).toString().contains("STONE_BRICKS")) {
                  if (data.getType(x + nx, y - 1, z + nz).toString().contains("LOG")) {
                     BlockUtils.setDownUntilSolid(x + nx, y - 2, z + nz, data, data.getType(x + nx, y - 1, z + nz));
                  }
               } else {
                  BlockUtils.setDownUntilSolid(x + nx, y - 2, z + nz, data, Material.COBBLESTONE, Material.COBBLESTONE, Material.COBBLESTONE, Material.MOSSY_COBBLESTONE);
               }
            }
         }

         Wall w = (new Wall(new SimpleBlock(data, x, y - 1, z), farmHouse.getFace())).getRight();

         for(nz = 0; nz < 7; ++nz) {
            w = w.getFront();
         }

         while(!w.isSolid() || w.getType().toString().contains("PLANKS")) {
            Stairs stairs = (Stairs)Bukkit.createBlockData((Material)GenUtils.randChoice(this.rand, Material.COBBLESTONE_STAIRS, Material.COBBLESTONE_STAIRS, Material.COBBLESTONE_STAIRS, Material.MOSSY_COBBLESTONE_STAIRS));
            stairs.setFacing(w.getDirection().getOppositeFace());
            w.getRight().setBlockData(stairs);
            w.setBlockData(stairs);
            w.getLeft().setBlockData(stairs);
            w.getLeft(2).getUp().downUntilSolid(this.rand, new Material[]{WoodUtils.getWoodForBiome(biome, WoodUtils.WoodType.LOG)});
            w.getLeft(2).getUp(2).setType((Material)GenUtils.randChoice(this.rand, Material.COBBLESTONE_WALL, Material.COBBLESTONE_WALL, Material.COBBLESTONE_WALL, Material.MOSSY_COBBLESTONE_WALL));
            w.getRight(2).getUp().downUntilSolid(this.rand, new Material[]{WoodUtils.getWoodForBiome(biome, WoodUtils.WoodType.LOG)});
            w.getRight(2).getUp(2).setType((Material)GenUtils.randChoice(this.rand, Material.COBBLESTONE_WALL, Material.COBBLESTONE_WALL, Material.COBBLESTONE_WALL, Material.MOSSY_COBBLESTONE_WALL));
            w = w.getFront().getDown();
         }

         Bell bell = (Bell)Bukkit.createBlockData(Material.BELL);
         bell.setAttachment(Attachment.SINGLE_WALL);
         bell.setFacing(w.getDirection().getOppositeFace());
         w.getLeft(2).getUp(2).setBlockData(bell);
         Wall entrance = w.getGround();

         for(int maxDepth = 5; entrance.getType() != Material.DIRT_PATH && maxDepth > 0; --maxDepth) {
            if (BlockUtils.isDirtLike(entrance.getType())) {
               entrance.setType(Material.DIRT_PATH);
            }

            Wall leftPath = entrance.getLeft().getGround();
            Wall rightPath = entrance.getRight().getGround();
            if (BlockUtils.isDirtLike(leftPath.getType())) {
               leftPath.setType(Material.DIRT_PATH);
            }

            if (BlockUtils.isDirtLike(rightPath.getType())) {
               rightPath.setType(Material.DIRT_PATH);
            }

            entrance = entrance.getFront().getGround();
         }
      } catch (Throwable var15) {
         TerraformGeneratorPlugin.logger.error("Something went wrong trying to place farmhouse at " + x + "," + y + "," + z + "!");
         TerraformGeneratorPlugin.logger.stackTrace(var15);
      }

   }

   public boolean canPopulate(@NotNull CubeRoom room) {
      return room.isHuge();
   }
}
