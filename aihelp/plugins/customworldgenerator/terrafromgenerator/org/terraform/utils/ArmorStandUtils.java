package org.terraform.utils;

import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.coregen.populatordata.PopulatorDataPostGen;
import org.terraform.data.SimpleBlock;

public class ArmorStandUtils {
   public static void placeArmorStand(@NotNull SimpleBlock target, @NotNull BlockFace facing, @NotNull Random rand) {
      placeArmorStand(target, facing, ArmorStandUtils.ArmorStandQuality.rollQuality(rand));
   }

   public static void placeArmorStand(@NotNull SimpleBlock target, @NotNull BlockFace facing, @NotNull ArmorStandUtils.ArmorStandQuality quality) {
      PopulatorDataAbstract var4 = target.getPopData();
      if (var4 instanceof PopulatorDataPostGen) {
         PopulatorDataPostGen postGen = (PopulatorDataPostGen)var4;
         ArmorStand stand = (ArmorStand)postGen.getWorld().spawnEntity(new Location(postGen.getWorld(), (double)((float)target.getX() + 0.5F), (double)target.getY(), (double)((float)target.getZ() + 0.5F)), EntityType.ARMOR_STAND);
         stand.setRotation(BlockUtils.yawFromBlockFace(facing), 0.0F);
         quality.apply(stand);
      }

   }

   public static enum ArmorStandQuality {
      LEATHER(new Material[]{Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS}),
      IRON(new Material[]{Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS}),
      GOLD(new Material[]{Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS}),
      DIAMOND(new Material[]{Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS});

      private final Material[] pieces;

      private ArmorStandQuality(Material... param3) {
         this.pieces = pieces;
      }

      @NotNull
      public static ArmorStandUtils.ArmorStandQuality rollQuality(@NotNull Random rand) {
         int weight = rand.nextInt(100);
         if (weight > 95) {
            return DIAMOND;
         } else if (weight > 60) {
            return IRON;
         } else {
            return weight > 30 ? GOLD : LEATHER;
         }
      }

      public void apply(@NotNull ArmorStand entity) {
         entity.getEquipment().setHelmet(new ItemStack(this.pieces[0]));
         entity.getEquipment().setChestplate(new ItemStack(this.pieces[1]));
         entity.getEquipment().setLeggings(new ItemStack(this.pieces[2]));
         entity.getEquipment().setBoots(new ItemStack(this.pieces[3]));
      }

      // $FF: synthetic method
      private static ArmorStandUtils.ArmorStandQuality[] $values() {
         return new ArmorStandUtils.ArmorStandQuality[]{LEATHER, IRON, GOLD, DIAMOND};
      }
   }
}
