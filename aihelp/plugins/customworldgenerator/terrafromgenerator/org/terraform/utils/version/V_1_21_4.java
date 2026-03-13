package org.terraform.utils.version;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;

public class V_1_21_4 {
   public static Biome PALE_GARDEN;
   public static BlockData PALE_HANGING_MOSS;
   public static BlockData PALE_HANGING_MOSS_TIP;
   public static Material PALE_MOSS;
   public static Material PALE_OAK_LOG;
   public static Material PALE_OAK_WOOD;
   public static Material PALE_OAK_LEAVES;
   public static Material CLOSED_EYEBLOSSOM;
   public static BlockData CREAKING_HEART;
   public static Material PALE_MOSS_CARPET;

   static {
      PALE_GARDEN = Version.VERSION.isAtLeast(Version.v1_21_4) ? Biome.valueOf("PALE_GARDEN") : Biome.DARK_FOREST;
      PALE_HANGING_MOSS = Version.VERSION.isAtLeast(Version.v1_21_4) ? Bukkit.createBlockData("minecraft:pale_hanging_moss[tip=false]") : Bukkit.createBlockData(Material.VINE);
      PALE_HANGING_MOSS_TIP = Version.VERSION.isAtLeast(Version.v1_21_4) ? Bukkit.createBlockData("minecraft:pale_hanging_moss[tip=true]") : Bukkit.createBlockData(Material.VINE);
      PALE_MOSS = Version.VERSION.isAtLeast(Version.v1_21_4) ? Material.valueOf("PALE_MOSS_BLOCK") : Material.MOSS_BLOCK;
      PALE_OAK_LOG = Version.VERSION.isAtLeast(Version.v1_21_4) ? Material.valueOf("PALE_OAK_LOG") : Material.DARK_OAK_LOG;
      PALE_OAK_WOOD = Version.VERSION.isAtLeast(Version.v1_21_4) ? Material.valueOf("PALE_OAK_WOOD") : Material.DARK_OAK_WOOD;
      PALE_OAK_LEAVES = Version.VERSION.isAtLeast(Version.v1_21_4) ? Material.valueOf("PALE_OAK_LEAVES") : Material.DARK_OAK_LEAVES;
      CLOSED_EYEBLOSSOM = Version.VERSION.isAtLeast(Version.v1_21_4) ? Material.valueOf("CLOSED_EYEBLOSSOM") : Material.POPPY;
      CREAKING_HEART = Version.VERSION.isAtLeast(Version.v1_21_4) ? (Version.VERSION.isAtLeast(Version.v1_21_5) ? Bukkit.createBlockData("minecraft:creaking_heart[creaking_heart_state=awake,natural=true]") : Bukkit.createBlockData("minecraft:creaking_heart[active=true,natural=true]")) : Bukkit.createBlockData(Material.DARK_OAK_WOOD);
      PALE_MOSS_CARPET = Version.VERSION.isAtLeast(Version.v1_21_4) ? Material.valueOf("PALE_MOSS_CARPET") : Material.MOSS_CARPET;
   }
}
