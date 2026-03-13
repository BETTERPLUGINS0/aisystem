package org.terraform.structure.catacombs;

import java.util.Iterator;
import java.util.Objects;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.coregen.populatordata.PopulatorDataSpigotAPI;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.data.Wall;
import org.terraform.main.config.TConfig;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomPopulatorAbstract;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.SphereBuilder;
import org.terraform.utils.blockdata.StairBuilder;

public class CatacombsStandardPopulator extends RoomPopulatorAbstract {
   public CatacombsStandardPopulator(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      int[] lowerCorner = room.getLowerCorner(0);
      int[] upperCorner = room.getUpperCorner(0);
      float maxTotalDiff = (float)room.getWidthX() / 2.0F + (float)room.getWidthZ() / 2.0F;
      int y = room.getY();
      (new SphereBuilder(this.rand, room.getCenterSimpleBlock(data), CatacombsPathPopulator.pathMaterial)).setRX((float)room.getWidthX() / 2.0F).setRZ((float)room.getWidthZ() / 2.0F).setRY((float)room.getWidthX() / 3.0F).setHardReplace(true).setSphereType(SphereBuilder.SphereType.LOWER_SEMISPHERE).build();

      int x;
      SimpleBlock ceiling;
      for(x = lowerCorner[0]; x <= upperCorner[0]; ++x) {
         for(int z = lowerCorner[1]; z <= upperCorner[1]; ++z) {
            if (this.rand.nextInt(5) < 4) {
               data.setType(x, y, z, (Material)GenUtils.randChoice((Object[])CatacombsPathPopulator.pathMaterial));
            } else if (!data.getType(x, y, z).isSolid()) {
               data.setType(x, y, z, (Material)GenUtils.randChoice((Object[])(Material.STONE, Material.ANDESITE, Material.CRACKED_STONE_BRICKS)));
            }

            ceiling = (new SimpleBlock(data, x, y + 1, z)).findCeiling(room.getHeight() + 1);
            if (ceiling != null) {
               float maxDownExtend = (float)(room.getHeight() - 4);
               int xDiffFromCent = Math.abs(x - room.getX()) + this.rand.nextInt(2);
               int zDiffFromCent = Math.abs(z - room.getZ()) + this.rand.nextInt(2);
               int extend = Math.round((float)(xDiffFromCent + zDiffFromCent) / maxTotalDiff * maxDownExtend);
               ceiling.getDown().downLPillar(new Random(), extend, Material.STONE, Material.ANDESITE, Material.CRACKED_STONE_BRICKS);
               if (TConfig.areDecorationsEnabled() && this.rand.nextInt(10) == 0) {
                  ceiling.getDown(extend + 1).getRelative(BlockUtils.getDirectBlockFace(this.rand)).lsetType(Material.COBWEB);
               }
            }
         }
      }

      Iterator var14 = room.getFourWalls(data, 0).entrySet().iterator();

      while(var14.hasNext()) {
         Entry<Wall, Integer> entry = (Entry)var14.next();
         Wall w = (Wall)entry.getKey();

         for(int i = 0; i < (Integer)entry.getValue(); ++i) {
            w.getRear().ReplacePillar(room.getHeight(), new Material[]{Material.STONE, Material.ANDESITE});
            if (this.rand.nextInt(8) == 4) {
               Wall target = w.getUp(this.rand.nextInt(room.getHeight()) + 1).getRear();
               if (target.isSolid()) {
                  target.ReplacePillar(1, new Material[]{Material.POLISHED_ANDESITE});
                  target.getRight().ReplacePillar(1, new Material[]{Material.POLISHED_ANDESITE});
               }
            }

            if ((w.getUp(2).getLeft().getRear().isAir() || w.getUp(2).getRight().getRear().isAir()) && w.getUp(2).getRear().isSolid()) {
               (new StairBuilder(new Material[]{Material.STONE_BRICK_STAIRS, Material.MOSSY_STONE_BRICK_STAIRS, Material.COBBLESTONE_STAIRS})).setHalf(Half.TOP).setFacing(w.getDirection().getOppositeFace()).apply(w.getUp(2));
               BlockUtils.placeCandle(w.getUp(3), GenUtils.randInt(1, 4), this.lightCandles());
            }

            w = w.getLeft();
         }
      }

      for(x = 0; x < 1 + this.rand.nextInt(3); ++x) {
         int[] coords = room.randomCoords(this.rand, 1);
         ceiling = new SimpleBlock(data, coords[0], room.getY() + 1, coords[2]);
         ceiling = ceiling.getAtY(room.getY() + 1);
         if (data instanceof PopulatorDataSpigotAPI) {
            Skeleton e = (Skeleton)((PopulatorDataSpigotAPI)data).lr.spawnEntity(new Location(((TerraformWorld)Objects.requireNonNull(data.getTerraformWorld())).getWorld(), (double)ceiling.getX() + 0.5D, (double)ceiling.getY() + 0.3D, (double)ceiling.getZ() + 0.5D), EntityType.SKELETON);
            ((EntityEquipment)Objects.requireNonNull(e.getEquipment())).setItemInMainHand(new ItemStack(Material.IRON_SWORD));
            e.setPersistent(true);
         }
      }

   }

   protected void spawnHangingChains(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      if (TConfig.areDecorationsEnabled()) {
         for(int i = 3; i <= 10; ++i) {
            int[] coords = room.randomCoords(this.rand, 1);
            SimpleBlock target = new SimpleBlock(data, coords[0], room.getY() + 1, coords[2]);
            target = target.findCeiling(room.getHeight());
            if (target != null && target.getY() - room.getY() >= 4) {
               target.getDown().downLPillar(new Random(), GenUtils.randInt(3, 5), Material.CHAIN);
            }
         }

      }
   }

   protected boolean lightCandles() {
      return true;
   }

   public boolean canPopulate(CubeRoom room) {
      return true;
   }
}
