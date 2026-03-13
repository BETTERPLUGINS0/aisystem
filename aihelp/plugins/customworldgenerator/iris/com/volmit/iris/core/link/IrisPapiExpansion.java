package com.volmit.iris.core.link;

import com.volmit.iris.Iris;
import com.volmit.iris.core.tools.IrisToolbelt;
import com.volmit.iris.engine.object.IrisBiome;
import com.volmit.iris.engine.platform.PlatformChunkGenerator;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class IrisPapiExpansion extends PlaceholderExpansion {
   @NotNull
   public String getIdentifier() {
      return "iris";
   }

   @NotNull
   public String getAuthor() {
      return "Volmit Software";
   }

   @NotNull
   public String getVersion() {
      return Iris.instance.getDescription().getVersion();
   }

   public boolean persist() {
      return true;
   }

   public String onRequest(OfflinePlayer player, String p) {
      Location var3 = null;
      PlatformChunkGenerator var4 = null;
      if (var1.isOnline() && var1.getPlayer() != null) {
         var3 = var1.getPlayer().getLocation().add(0.0D, 2.0D, 0.0D);
         var4 = IrisToolbelt.access(var3.getWorld());
      }

      if (var2.equalsIgnoreCase("biome_name")) {
         if (var4 != null) {
            return this.getBiome(var4, var3).getName();
         }
      } else if (var2.equalsIgnoreCase("biome_id")) {
         if (var4 != null) {
            return this.getBiome(var4, var3).getLoadKey();
         }
      } else if (var2.equalsIgnoreCase("biome_file")) {
         if (var4 != null) {
            return this.getBiome(var4, var3).getLoadFile().getPath();
         }
      } else if (var2.equalsIgnoreCase("region_name")) {
         if (var4 != null) {
            return var4.getEngine().getRegion(var3).getName();
         }
      } else if (var2.equalsIgnoreCase("region_id")) {
         if (var4 != null) {
            return var4.getEngine().getRegion(var3).getLoadKey();
         }
      } else if (var2.equalsIgnoreCase("region_file")) {
         if (var4 != null) {
            return var4.getEngine().getRegion(var3).getLoadFile().getPath();
         }
      } else if (var2.equalsIgnoreCase("terrain_slope")) {
         if (var4 != null) {
            String var10000 = String.valueOf(var4.getEngine().getComplex().getSlopeStream().get(var3.getX(), var3.getZ()));
            return var10000.makeConcatWithConstants<invokedynamic>(var10000);
         }
      } else if (var2.equalsIgnoreCase("terrain_height")) {
         if (var4 != null) {
            float var5 = (float)var4.getEngine().getHeight(var3.getBlockX(), var3.getBlockZ());
            return Math.round(var5).makeConcatWithConstants<invokedynamic>(Math.round(var5));
         }
      } else if (var2.equalsIgnoreCase("world_mode")) {
         if (var4 != null) {
            return var4.isStudio() ? "Studio" : "Production";
         }
      } else if (var2.equalsIgnoreCase("world_seed")) {
         if (var4 != null) {
            return var4.getEngine().getSeedManager().getSeed().makeConcatWithConstants<invokedynamic>(var4.getEngine().getSeedManager().getSeed());
         }
      } else if (var2.equalsIgnoreCase("world_speed") && var4 != null) {
         return var4.getEngine().getGeneratedPerSecond() + "/s";
      }

      return null;
   }

   private IrisBiome getBiome(PlatformChunkGenerator a, Location l) {
      return var1.getEngine().getBiome(var2.getBlockX(), var2.getBlockY() - var2.getWorld().getMinHeight(), var2.getBlockZ());
   }
}
