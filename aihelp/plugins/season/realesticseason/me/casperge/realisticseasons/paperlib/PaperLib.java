package me.casperge.realisticseasons.paperlib;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import me.casperge.realisticseasons.paperlib.environments.CraftBukkitEnvironment;
import me.casperge.realisticseasons.paperlib.environments.Environment;
import me.casperge.realisticseasons.paperlib.environments.PaperEnvironment;
import me.casperge.realisticseasons.paperlib.environments.SpigotEnvironment;
import me.casperge.realisticseasons.paperlib.features.blockstatesnapshot.BlockStateSnapshotResult;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.Plugin;

public class PaperLib {
   private static Environment ENVIRONMENT = initialize();

   private PaperLib() {
   }

   private static Environment initialize() {
      try {
         Class.forName("com.destroystokyo.paper.PaperConfig");
         return new PaperEnvironment();
      } catch (ClassNotFoundException var3) {
         try {
            Class.forName("org.spigotmc.SpigotConfig");
            return new SpigotEnvironment();
         } catch (ClassNotFoundException var2) {
            return new CraftBukkitEnvironment();
         }
      }
   }

   @Nonnull
   public static Environment getEnvironment() {
      return ENVIRONMENT;
   }

   public static void setCustomEnvironment(@Nonnull Environment var0) {
      ENVIRONMENT = var0;
   }

   @Nonnull
   public static CompletableFuture<Boolean> teleportAsync(@Nonnull Entity var0, @Nonnull Location var1) {
      return ENVIRONMENT.teleport(var0, var1, TeleportCause.PLUGIN);
   }

   @Nonnull
   public static CompletableFuture<Boolean> teleportAsync(@Nonnull Entity var0, @Nonnull Location var1, TeleportCause var2) {
      return ENVIRONMENT.teleport(var0, var1, var2);
   }

   @Nonnull
   public static CompletableFuture<Chunk> getChunkAtAsync(@Nonnull Location var0) {
      return getChunkAtAsync(var0.getWorld(), var0.getBlockX() >> 4, var0.getBlockZ() >> 4, true);
   }

   @Nonnull
   public static CompletableFuture<Chunk> getChunkAtAsync(@Nonnull Location var0, boolean var1) {
      return getChunkAtAsync(var0.getWorld(), var0.getBlockX() >> 4, var0.getBlockZ() >> 4, var1);
   }

   @Nonnull
   public static CompletableFuture<Chunk> getChunkAtAsync(@Nonnull World var0, int var1, int var2) {
      return getChunkAtAsync(var0, var1, var2, true);
   }

   @Nonnull
   public static CompletableFuture<Chunk> getChunkAtAsync(@Nonnull World var0, int var1, int var2, boolean var3) {
      return ENVIRONMENT.getChunkAtAsync(var0, var1, var2, var3, false);
   }

   @Nonnull
   public static CompletableFuture<Chunk> getChunkAtAsync(@Nonnull World var0, int var1, int var2, boolean var3, boolean var4) {
      return ENVIRONMENT.getChunkAtAsync(var0, var1, var2, var3, var4);
   }

   @Nonnull
   public static CompletableFuture<Chunk> getChunkAtAsyncUrgently(@Nonnull World var0, int var1, int var2, boolean var3) {
      return ENVIRONMENT.getChunkAtAsync(var0, var1, var2, var3, true);
   }

   public static boolean isChunkGenerated(@Nonnull Location var0) {
      return isChunkGenerated(var0.getWorld(), var0.getBlockX() >> 4, var0.getBlockZ() >> 4);
   }

   public static boolean isChunkGenerated(@Nonnull World var0, int var1, int var2) {
      return ENVIRONMENT.isChunkGenerated(var0, var1, var2);
   }

   @Nonnull
   public static BlockStateSnapshotResult getBlockState(@Nonnull Block var0, boolean var1) {
      return ENVIRONMENT.getBlockState(var0, var1);
   }

   public static CompletableFuture<Location> getBedSpawnLocationAsync(@Nonnull Player var0, boolean var1) {
      return ENVIRONMENT.getBedSpawnLocationAsync(var0, var1);
   }

   public static boolean isVersion(int var0) {
      return ENVIRONMENT.isVersion(var0);
   }

   public static boolean isVersion(int var0, int var1) {
      return ENVIRONMENT.isVersion(var0, var1);
   }

   public static int getMinecraftVersion() {
      return ENVIRONMENT.getMinecraftVersion();
   }

   public static int getMinecraftPatchVersion() {
      return ENVIRONMENT.getMinecraftPatchVersion();
   }

   public static int getMinecraftPreReleaseVersion() {
      return ENVIRONMENT.getMinecraftPreReleaseVersion();
   }

   public static boolean isSpigot() {
      return ENVIRONMENT.isSpigot();
   }

   public static boolean isPaper() {
      return ENVIRONMENT.isPaper();
   }

   public static void suggestPaper(@Nonnull Plugin var0) {
      suggestPaper(var0, Level.INFO);
   }

   public static void suggestPaper(@Nonnull Plugin var0, @Nonnull Level var1) {
      if (!isPaper()) {
         String var2 = "paperlib.shown-benefits";
         String var3 = var0.getDescription().getName();
         Logger var4 = var0.getLogger();
         var4.log(var1, "====================================================");
         var4.log(var1, " " + var3 + " works better if you use Paper ");
         var4.log(var1, " as your server software. ");
         if (System.getProperty("paperlib.shown-benefits") == null) {
            System.setProperty("paperlib.shown-benefits", "1");
            var4.log(var1, "  ");
            var4.log(var1, " Paper offers significant performance improvements,");
            var4.log(var1, " bug fixes, security enhancements and optional");
            var4.log(var1, " features for server owners to enhance their server.");
            var4.log(var1, "  ");
            var4.log(var1, " Paper includes Timings v2, which is significantly");
            var4.log(var1, " better at diagnosing lag problems over v1.");
            var4.log(var1, "  ");
            var4.log(var1, " All of your plugins should still work, and the");
            var4.log(var1, " Paper community will gladly help you fix any issues.");
            var4.log(var1, "  ");
            var4.log(var1, " Join the Paper Community @ https://papermc.io");
         }

         var4.log(var1, "====================================================");
      }
   }
}
