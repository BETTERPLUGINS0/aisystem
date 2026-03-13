package org.terraform.utils.blockdata;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

public class CommonMat {
   public static BlockData BEDROCK;
   public static BlockData AIR;
   public static BlockData CAVE_AIR;
   public static BlockData WATER;
   public static BlockData STONE;
   public static BlockData DEEPSLATE;

   static {
      BEDROCK = Bukkit.createBlockData(Material.BEDROCK);
      AIR = Bukkit.createBlockData(Material.AIR);
      CAVE_AIR = Bukkit.createBlockData(Material.CAVE_AIR);
      WATER = Bukkit.createBlockData(Material.WATER);
      STONE = Bukkit.createBlockData(Material.STONE);
      DEEPSLATE = Bukkit.createBlockData(Material.DEEPSLATE);
   }
}
