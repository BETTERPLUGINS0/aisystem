package org.terraform.structure.warmoceanruins;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.structure.room.CubeRoom;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.CylinderBuilder;
import org.terraform.utils.GenUtils;
import org.terraform.utils.SphereBuilder;
import org.terraform.utils.blockdata.ChestBuilder;

public class WarmOceanDomeHutRoom extends WarmOceanBaseRoom {
   public WarmOceanDomeHutRoom(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      super.populate(data, room);
      Material decorator = (new Material[]{Material.POLISHED_DIORITE, Material.POLISHED_GRANITE, Material.POLISHED_ANDESITE})[this.rand.nextInt(3)];
      SimpleBlock centre = room.getCenterSimpleBlock(data);
      float radius = (float)Math.min(room.getWidthX(), room.getWidthZ()) / 3.0F;
      int cylSize = GenUtils.randInt(this.rand, 3, 5);
      (new CylinderBuilder(this.rand, centre.getDown(), new Material[]{Material.SMOOTH_SANDSTONE, Material.SANDSTONE})).setStartFromZero(true).setRadius(radius).setRY((float)(cylSize + 1)).setNoiseMagnitude(0.0F).setHardReplace(true).build();
      (new CylinderBuilder(this.rand, centre, new Material[]{Material.WATER})).setStartFromZero(true).setRadius(radius - 1.0F).setRY((float)cylSize).setNoiseMagnitude(0.0F).setHardReplace(true).build();
      (new SphereBuilder(this.rand, centre.getUp(cylSize - 1), new Material[]{Material.SMOOTH_SANDSTONE, Material.SANDSTONE})).setRadius(radius).setSphereType(SphereBuilder.SphereType.UPPER_SEMISPHERE).setSmooth(true).build();
      (new SphereBuilder(this.rand, centre.getUp(cylSize - 1), new Material[]{Material.WATER})).setRadius(radius - 1.0F).setSphereType(SphereBuilder.SphereType.UPPER_SEMISPHERE).setSmooth(true).setHardReplace(true).build();
      BlockFace entrance = BlockUtils.getDirectBlockFace(this.rand);
      (new ChestBuilder(Material.CHEST)).setFacing(entrance).setLootTable(TerraLootTable.UNDERWATER_RUIN_SMALL).apply(centre.getRelative(entrance.getOppositeFace(), (int)radius - 2));
      centre.getRelative(entrance, (int)radius).physicsSetType(Material.WATER, true);
      centre.getUp().getRelative(entrance, (int)radius).physicsSetType(Material.WATER, true);

      for(int i = 0; i < GenUtils.randInt(this.rand, 2, 4); ++i) {
         BlockUtils.replaceWaterSphere(i * room.getX() * room.getZ(), (float)GenUtils.randInt(2, 4), centre.getRelative(GenUtils.getSign(this.rand) * this.rand.nextInt((int)radius), 3 + this.rand.nextInt((int)radius), GenUtils.getSign(this.rand) * this.rand.nextInt((int)radius)));
      }

      centre.getUp((int)((float)cylSize + radius - 1.0F)).setType(decorator);
      BlockFace[] var12 = BlockUtils.directBlockFaces;
      int var9 = var12.length;

      for(int var10 = 0; var10 < var9; ++var10) {
         BlockFace face = var12[var10];
         if (face != entrance) {
            centre.getUp(cylSize / 2).getRelative(face, (int)radius).setType(decorator);
         }
      }

      centre.addEntity(EntityType.DROWNED);
   }

   public boolean canPopulate(@NotNull CubeRoom room) {
      return room.getWidthX() < 25;
   }
}
