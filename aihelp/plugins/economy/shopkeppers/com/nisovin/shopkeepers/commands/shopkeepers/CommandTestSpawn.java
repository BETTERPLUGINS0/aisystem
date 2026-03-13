package com.nisovin.shopkeepers.commands.shopkeepers;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.util.ChunkCoords;
import com.nisovin.shopkeepers.commands.lib.CommandException;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.arguments.BoundedIntegerArgument;
import com.nisovin.shopkeepers.commands.lib.commands.PlayerCommand;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.java.MathUtils;
import com.nisovin.shopkeepers.util.java.TimeUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

class CommandTestSpawn extends PlayerCommand {
   private static final String ARGUMENT_REPETITIONS = "repetitions";
   private final SKShopkeepersPlugin plugin;

   CommandTestSpawn(SKShopkeepersPlugin plugin) {
      super("testSpawn");
      this.plugin = plugin;
      this.setPermission("shopkeeper.debug");
      this.setDescription(Text.of("Measures the time it takes to respawn the active shopkeepers within the current chunk."));
      this.setHiddenInParentHelp(true);
      this.addArgument((new BoundedIntegerArgument("repetitions", 1, 1000)).orDefaultValue(10));
   }

   protected void execute(CommandInput input, CommandContextView context) throws CommandException {
      Player player = (Player)input.getSender();
      int repetitions = (Integer)context.get("repetitions");

      assert repetitions >= 1;

      ChunkCoords chunkCoords = new ChunkCoords(player.getLocation());
      Collection<? extends AbstractShopkeeper> chunkShopkeepers = this.plugin.getShopkeeperRegistry().getShopkeepersInChunk(chunkCoords);
      String var10001;
      if (chunkShopkeepers.isEmpty()) {
         var10001 = String.valueOf(ChatColor.RED);
         player.sendMessage(var10001 + "There are no shopkeepers in this chunk (" + chunkCoords.getChunkX() + "," + chunkCoords.getChunkZ() + ")!");
      } else {
         List<AbstractShopkeeper> activeShopkeepers = new ArrayList(chunkShopkeepers.size());
         chunkShopkeepers.forEach((shopkeeper) -> {
            if (shopkeeper.getShopObject().isActive()) {
               activeShopkeepers.add(shopkeeper);
            }

         });
         if (activeShopkeepers.isEmpty()) {
            var10001 = String.valueOf(ChatColor.RED);
            player.sendMessage(var10001 + "There are no active shopkeepers in this chunk (" + chunkCoords.getChunkX() + "," + chunkCoords.getChunkZ() + ")!");
         } else {
            player.sendMessage(String.valueOf(ChatColor.GREEN) + "Measuring the time it takes to respawn the active shopkeepers within this chunk ...");
            long startTimeNanos = System.nanoTime();
            long[] despawnTimesNanos = new long[repetitions];
            long[] spawnTimesNanos = new long[repetitions];
            int failedToSpawn = 0;

            for(int i = 0; i < repetitions; ++i) {
               CommandTestSpawn.Result result = testSpawn(activeShopkeepers);
               despawnTimesNanos[i] = result.despawnTimeNanos;
               spawnTimesNanos[i] = result.spawnTimeNanos;
               failedToSpawn += result.failedToSpawn;
            }

            double avgDespawnTimeMillis = TimeUtils.convert(MathUtils.average(despawnTimesNanos), TimeUnit.NANOSECONDS, TimeUnit.MILLISECONDS);
            double avgDespawnTimePerShopkeeperMillis = avgDespawnTimeMillis / (double)activeShopkeepers.size();
            double maxDespawnTimeMillis = TimeUtils.convert((double)MathUtils.max(despawnTimesNanos), TimeUnit.NANOSECONDS, TimeUnit.MILLISECONDS);
            double maxDespawnTimePerShopkeeperMillis = maxDespawnTimeMillis / (double)activeShopkeepers.size();
            double avgSpawnTimeMillis = TimeUtils.convert(MathUtils.average(spawnTimesNanos), TimeUnit.NANOSECONDS, TimeUnit.MILLISECONDS);
            double avgSpawnTimePerShopkeeperMillis = avgSpawnTimeMillis / (double)activeShopkeepers.size();
            double maxSpawnTimeMillis = TimeUtils.convert((double)MathUtils.max(spawnTimesNanos), TimeUnit.NANOSECONDS, TimeUnit.MILLISECONDS);
            double maxSpawnTimePerShopkeeperMillis = maxSpawnTimeMillis / (double)activeShopkeepers.size();
            double totalDurationMillis = TimeUtils.convert((double)(System.nanoTime() - startTimeNanos), TimeUnit.NANOSECONDS, TimeUnit.MILLISECONDS);
            var10001 = String.valueOf(ChatColor.GREEN);
            player.sendMessage(var10001 + "Shopkeepers: " + String.valueOf(ChatColor.YELLOW) + activeShopkeepers.size() + (chunkShopkeepers.size() > activeShopkeepers.size() ? " / " + chunkShopkeepers.size() : "") + String.valueOf(ChatColor.GREEN) + "   Repetitions: " + String.valueOf(ChatColor.YELLOW) + repetitions + String.valueOf(ChatColor.GREEN) + "   Total duration: " + String.valueOf(ChatColor.YELLOW) + TextUtils.format(totalDurationMillis) + " ms");
            if (failedToSpawn > 0) {
               var10001 = String.valueOf(ChatColor.RED);
               player.sendMessage(var10001 + "  Failed to respawn " + String.valueOf(ChatColor.YELLOW) + failedToSpawn + String.valueOf(ChatColor.RED) + " shopkeepers. The results might be inaccurate.");
            }

            var10001 = String.valueOf(ChatColor.GRAY);
            player.sendMessage(var10001 + "  Despawn times (avg | avg per | max | max per): " + String.valueOf(ChatColor.WHITE) + TextUtils.format(avgDespawnTimeMillis) + " ms" + String.valueOf(ChatColor.GRAY) + " | " + String.valueOf(ChatColor.WHITE) + TextUtils.formatPrecise(avgDespawnTimePerShopkeeperMillis) + " ms" + String.valueOf(ChatColor.GRAY) + " | " + String.valueOf(ChatColor.WHITE) + TextUtils.format(maxDespawnTimeMillis) + " ms" + String.valueOf(ChatColor.GRAY) + " | " + String.valueOf(ChatColor.WHITE) + TextUtils.formatPrecise(maxDespawnTimePerShopkeeperMillis) + " ms");
            var10001 = String.valueOf(ChatColor.GRAY);
            player.sendMessage(var10001 + "  Spawn times (avg | avg per | max | max per): " + String.valueOf(ChatColor.WHITE) + TextUtils.format(avgSpawnTimeMillis) + " ms" + String.valueOf(ChatColor.GRAY) + " | " + String.valueOf(ChatColor.WHITE) + TextUtils.formatPrecise(avgSpawnTimePerShopkeeperMillis) + " ms" + String.valueOf(ChatColor.GRAY) + " | " + String.valueOf(ChatColor.WHITE) + TextUtils.format(maxSpawnTimeMillis) + " ms" + String.valueOf(ChatColor.GRAY) + " | " + String.valueOf(ChatColor.WHITE) + TextUtils.formatPrecise(maxSpawnTimePerShopkeeperMillis) + " ms");
         }
      }
   }

   private static CommandTestSpawn.Result testSpawn(Collection<? extends AbstractShopkeeper> shopkeepers) {
      CommandTestSpawn.Result result = new CommandTestSpawn.Result();
      long despawnStartNanos = System.nanoTime();
      Iterator var4 = shopkeepers.iterator();

      while(var4.hasNext()) {
         AbstractShopkeeper shopkeeper = (AbstractShopkeeper)var4.next();
         shopkeeper.getShopObject().despawn();
      }

      result.despawnTimeNanos = System.nanoTime() - despawnStartNanos;
      long spawnStartNanos = System.nanoTime();
      int failedToSpawn = 0;
      Iterator var7 = shopkeepers.iterator();

      while(var7.hasNext()) {
         AbstractShopkeeper shopkeeper = (AbstractShopkeeper)var7.next();
         boolean success = shopkeeper.getShopObject().spawn();
         if (!success) {
            ++failedToSpawn;
         }
      }

      result.spawnTimeNanos = System.nanoTime() - spawnStartNanos;
      result.failedToSpawn = failedToSpawn;
      return result;
   }

   private static class Result {
      long despawnTimeNanos;
      long spawnTimeNanos;
      int failedToSpawn = 0;
   }
}
