package me.casperge.realisticseasons.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.season.Season;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

public class BlockUtils {
   RealisticSeasons main;

   public BlockUtils(RealisticSeasons var1) {
      this.main = var1;
   }

   public static ItemStack getSkullFromURL(String var0) {
      ItemStack var1 = new ItemStack(Material.PLAYER_HEAD, 1);
      SkullMeta var2 = (SkullMeta)var1.getItemMeta();
      PlayerProfile var3 = Bukkit.createPlayerProfile(UUID.randomUUID());
      PlayerTextures var4 = var3.getTextures();

      try {
         URL var5 = new URL(var0);
         var4.setSkin(var5);
         var3.setTextures(var4);
         var2.setOwnerProfile(var3);
         var1.setItemMeta(var2);
      } catch (MalformedURLException var7) {
         var7.printStackTrace();
      }

      return var1;
   }

   public static Skull getSkullStateFromURL(String var0, Skull var1) {
      PlayerProfile var2 = Bukkit.getServer().createPlayerProfile(UUID.randomUUID(), "rs");
      PlayerTextures var3 = var2.getTextures();

      try {
         URL var4 = new URL(var0);
         var3.setSkin(var4);
         var2.setTextures(var3);
         var1.setOwnerProfile(var2);
      } catch (MalformedURLException var6) {
         var6.printStackTrace();
      }

      return var1;
   }

   public boolean isSnowable(Block var1) {
      if (var1.getType().isOccluding() && var1.getType().isSolid() && var1.getType() != Material.WATER && var1.getType() != Material.LAVA && var1.getType() != Material.FARMLAND && var1.getType() != Material.ICE && !var1.getType().toString().contains("STAIR") && !var1.getType().toString().contains("SLAB") && !var1.getType().toString().contains("FENCE") && var1.getType() != Material.CRIMSON_NYLIUM && var1.getType() != Material.WARPED_NYLIUM && var1.getType() != Material.BARRIER || var1.getType().toString().toLowerCase().contains("leave")) {
         if (this.main.hasTerra && this.main.getSettings().terraGenSnowDisabled && var1.getType().toString().toLowerCase().contains("leave")) {
            return false;
         }

         if (!this.main.getSettings().spawnSnowOnLeaves && var1.getType().toString().toLowerCase().contains("leave")) {
            return false;
         }

         Block var2 = var1.getRelative(0, 1, 0);
         if (var2.getType() == Material.AIR && var2.getType() != Material.SNOW) {
            return true;
         }
      }

      return false;
   }

   public static boolean canLeafDrop(Block var0) {
      return var0.getType().isOccluding() && var0.getType().isSolid() && var0.getType() != Material.WATER && var0.getType() != Material.LAVA && var0.getType() != Material.FARMLAND && var0.getType() != Material.ICE && !var0.getType().toString().contains("STAIR") && !var0.getType().toString().contains("SLAB") && !var0.getType().toString().contains("FENCE") || var0.getType().toString().toLowerCase().contains("leave") && var0.getType() != Material.CRIMSON_NYLIUM && var0.getType() != Material.WARPED_NYLIUM;
   }

   public boolean affectBlockInWinter(World var1, int var2, int var3, int var4) {
      Block var5 = var1.getBlockAt(var2, var3, var4);
      return this.affectBlockInWinter(var5);
   }

   public boolean isSnowing(Location var1) {
      RealisticSeasons var2 = RealisticSeasons.getInstance();
      return var1.getWorld().hasStorm() && var2.getSeasonManager().getSeason(var1.getWorld()) == Season.WINTER ? var2.getBlockUtils().affectBlockInWinter(var1.getWorld(), var1.getBlockX(), var1.getBlockY(), var1.getBlockZ()) : false;
   }

   public boolean affectBlockInWinter(Block var1) {
      String var2 = this.main.getNMSUtils().getBiomeName(var1.getLocation());
      if (ChunkUtils.checkBiome(ChunkUtils.BiomeType.AFFECTINWINTER, var2)) {
         return false;
      } else {
         return !this.main.getSettings().keepNaturalSnow || !(var1.getTemperature() < 0.15D);
      }
   }

   public boolean affectFlora(Block var1) {
      String var2 = this.main.getNMSUtils().getBiomeName(var1.getLocation());
      return !ChunkUtils.checkBiome(ChunkUtils.BiomeType.AFFECTFLORA, var2);
   }

   public void placePuddleBlock(Block var1) {
      this.main.getNMSUtils().setBlockInNMSChunk(var1.getWorld(), var1.getX(), var1.getY(), var1.getZ(), 40, (byte)0, false);
   }
}
