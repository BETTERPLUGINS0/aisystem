package org.terraform.utils;

import java.util.ArrayList;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.data.Wall;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.utils.blockdata.StairBuilder;

public class StairwayBuilder {
   private final Material[] stairTypes;
   private boolean carveAirSpace = true;
   private BlockFace stairDirection;
   private Material[] downTypes;
   private boolean stopAtWater;
   private int stopAtY;
   private boolean angled;
   private int maxExtensionForward;
   private boolean upwardsCarveUntilNotSolid;
   private boolean upwardsCarveUntilSolid;

   public StairwayBuilder(@NotNull Material... stairTypes) {
      this.stairDirection = BlockFace.DOWN;
      this.stopAtWater = false;
      this.stopAtY = -32768;
      this.angled = false;
      this.maxExtensionForward = 10;
      this.upwardsCarveUntilNotSolid = true;
      this.upwardsCarveUntilSolid = false;
      this.stairTypes = stairTypes;
      ArrayList<Material> downTypes = new ArrayList();
      Material[] var3 = stairTypes;
      int var4 = stairTypes.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Material mat = var3[var5];
         Material toAdd = Material.matchMaterial(mat.toString().replace("_STAIRS", ""));
         if (toAdd != null) {
            downTypes.add(toAdd);
         }
      }

      this.downTypes = new Material[downTypes.size()];

      for(int i = 0; i < downTypes.size(); ++i) {
         this.downTypes[i] = (Material)downTypes.get(i);
      }

   }

   @NotNull
   public StairwayBuilder setStopAtY(int y) {
      this.stopAtY = y;
      return this;
   }

   @NotNull
   public StairwayBuilder setCarveAirSpace(boolean carveAirSpace) {
      this.carveAirSpace = carveAirSpace;
      return this;
   }

   @NotNull
   public StairwayBuilder setUpwardsCarveUntilSolid(boolean carve) {
      this.upwardsCarveUntilSolid = carve;
      return this;
   }

   @NotNull
   public StairwayBuilder setUpwardsCarveUntilNotSolid(boolean carve) {
      this.upwardsCarveUntilNotSolid = carve;
      return this;
   }

   @NotNull
   public StairwayBuilder setDownTypes(Material... mat) {
      this.downTypes = mat;
      return this;
   }

   @NotNull
   public StairwayBuilder build(@NotNull Wall start) {
      int threshold;
      BlockFace extensionDir;
      Material stairType;
      if (this.stairDirection == BlockFace.DOWN) {
         threshold = 5;

         for(extensionDir = start.getDirection(); this.continueCondition(start); start = start.getRelative(extensionDir).getDown()) {
            if (threshold == 0) {
               start.setType(this.downTypes);
               start.getDown().downUntilSolid(new Random(), this.downTypes);
               extensionDir = BlockUtils.getTurnBlockFace(new Random(), extensionDir);
               start = start.getRelative(extensionDir);
            }

            stairType = this.stairTypes[(new Random()).nextInt(this.stairTypes.length)];
            if (stairType.toString().endsWith("STAIRS")) {
               (new StairBuilder(stairType)).setFacing(extensionDir.getOppositeFace()).apply(start);
            } else {
               start.setType(stairType);
            }

            start.getDown().downUntilSolid(new Random(), this.downTypes);
            if (this.angled) {
               --threshold;
            }
         }

         if (this.stopAtWater && start.get().getType() != Material.OAK_SLAB && BlockUtils.isWet(start.get())) {
            for(int i = 0; i < this.maxExtensionForward && !start.isSolid(); ++i) {
               start.downUntilSolid(new Random(), this.downTypes);
               start = start.getFront();
            }
         }
      } else if (this.stairDirection == BlockFace.UP) {
         threshold = 5;

         for(extensionDir = start.getDirection(); this.continueCondition(start); start = start.getRelative(extensionDir).getUp()) {
            if (threshold == 0) {
               start = start.getDown();
               if (this.carveAirSpace) {
                  start.getUp().Pillar(3, new Random(), new Material[]{Material.AIR});
               }

               start.setType(this.downTypes);
               start.getDown().downUntilSolid(new Random(), this.downTypes);
               extensionDir = BlockUtils.getTurnBlockFace(new Random(), extensionDir);
               start = start.getRelative(extensionDir).getUp();
            }

            stairType = this.stairTypes[(new Random()).nextInt(this.stairTypes.length)];
            if (stairType.toString().endsWith("STAIRS")) {
               (new StairBuilder(this.stairTypes)).setFacing(extensionDir).apply(start);
            } else {
               start.setType(stairType);
            }

            start.getDown().downUntilSolid(new Random(), this.downTypes);
            if (this.carveAirSpace) {
               start.getUp().Pillar(3, new Random(), new Material[]{Material.AIR});
               start.getUp(2).getRelative(extensionDir).setType(Material.AIR);
            }

            if (this.angled) {
               --threshold;
            }
         }
      } else {
         TerraformGeneratorPlugin.logger.error("StairwayBuilder was told to spawn stairway with non up/down stair direction!");
      }

      return this;
   }

   private boolean continueCondition(@NotNull Wall target) {
      if (this.stairDirection == BlockFace.DOWN) {
         if (this.stopAtY != -32768 && target.getY() == this.stopAtY) {
            return false;
         } else if (this.stopAtWater && BlockUtils.isWet(target.get())) {
            return false;
         } else {
            return !target.isSolid();
         }
      } else if (this.stopAtY != -32768 && target.getY() == this.stopAtY + 1) {
         return false;
      } else if (this.upwardsCarveUntilNotSolid) {
         return target.isSolid();
      } else if (this.upwardsCarveUntilSolid) {
         return !target.isSolid();
      } else {
         return true;
      }
   }

   @NotNull
   public StairwayBuilder setMaxExtensionForward(int extension) {
      this.maxExtensionForward = extension;
      return this;
   }

   @NotNull
   public StairwayBuilder setStairwayDirection(BlockFace stairDirection) {
      this.stairDirection = stairDirection;
      return this;
   }

   @NotNull
   public StairwayBuilder setStopAtWater(boolean stopAtWater) {
      this.stopAtWater = stopAtWater;
      return this;
   }

   @NotNull
   public StairwayBuilder setAngled(boolean angled) {
      this.angled = angled;
      return this;
   }
}
