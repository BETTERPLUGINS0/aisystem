package com.nisovin.shopkeepers.commands.shopkeepers;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopobjects.ShopObject;
import com.nisovin.shopkeepers.api.util.ChunkCoords;
import com.nisovin.shopkeepers.commands.lib.Command;
import com.nisovin.shopkeepers.commands.lib.CommandException;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.arguments.FirstOfArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.LiteralArgument;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.shopkeeper.activation.ShopkeeperChunkActivator;
import com.nisovin.shopkeepers.shopkeeper.registry.SKShopkeeperRegistry;
import com.nisovin.shopkeepers.shopkeeper.spawning.ShopkeeperSpawner;
import com.nisovin.shopkeepers.shopobjects.entity.base.EntityAI;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.taskqueue.TaskQueueStatistics;
import com.nisovin.shopkeepers.util.timer.Timings;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;

class CommandCheck extends Command {
   private static final String ARGUMENT_CHUNKS = "chunks";
   private static final String ARGUMENT_ACTIVE = "active";
   private final SKShopkeepersPlugin plugin;
   private final SKShopkeeperRegistry shopkeeperRegistry;
   private final ShopkeeperSpawner shopkeeperSpawner;
   private final ShopkeeperChunkActivator chunkActivator;

   CommandCheck(SKShopkeepersPlugin plugin) {
      super("check");
      this.plugin = plugin;
      this.shopkeeperRegistry = plugin.getShopkeeperRegistry();
      this.shopkeeperSpawner = this.shopkeeperRegistry.getShopkeeperSpawner();
      this.chunkActivator = this.shopkeeperRegistry.getChunkActivator();
      this.setPermission("shopkeeper.debug");
      this.setDescription(Text.of("Shows various debugging information."));
      this.setHiddenInParentHelp(true);
      this.addArgument((new FirstOfArgument("context", Arrays.asList(new LiteralArgument("chunks"), new LiteralArgument("active")), true)).optional());
   }

   protected void execute(CommandInput input, CommandContextView context) throws CommandException {
      CommandSender sender = input.getSender();
      boolean isConsole = sender instanceof ConsoleCommandSender;
      boolean listChunks = context.has("chunks");
      boolean listActive = context.has("active");
      EntityAI entityAI = this.plugin.getEntityShops().getEntityAI();
      int totalChunksWithShopkeepers = this.shopkeeperRegistry.getWorldsWithShopkeepers().stream().map((worldNamex) -> {
         return this.shopkeeperRegistry.getShopkeepersByChunks(worldNamex);
      }).mapToInt((byChunk) -> {
         return byChunk.size();
      }).sum();
      sender.sendMessage(String.valueOf(ChatColor.YELLOW) + "All shopkeepers:");
      int var10001 = this.shopkeeperRegistry.getAllShopkeepers().size();
      sender.sendMessage("  Total: " + var10001 + "    (Virtual: " + this.shopkeeperRegistry.getVirtualShopkeepers().size() + ")");
      var10001 = this.plugin.getShopkeeperStorage().getUnsavedDirtyShopkeepersCount();
      sender.sendMessage("  Unsaved dirty | deleted | dirty storage: " + var10001 + " | " + this.plugin.getShopkeeperStorage().getUnsavedDeletedShopkeepersCount() + " | " + this.plugin.getShopkeeperStorage().isDirty());
      sender.sendMessage("  Chunks with shopkeepers: " + totalChunksWithShopkeepers);
      sender.sendMessage("    With active AI: " + entityAI.getActiveAIChunksCount());
      sender.sendMessage("    With active gravity: " + entityAI.getActiveGravityChunksCount());
      sender.sendMessage("  Active shopkeepers: " + this.shopkeeperRegistry.getActiveShopkeepers().size());
      sender.sendMessage("    With AI: " + entityAI.getEntityCount());
      sender.sendMessage("    With active AI: " + entityAI.getActiveAIEntityCount());
      sender.sendMessage("    With active gravity: " + entityAI.getActiveGravityEntityCount());
      TaskQueueStatistics spawnQueueStatistics = this.shopkeeperSpawner.getSpawnQueueStatistics();
      var10001 = spawnQueueStatistics.getPendingCount();
      sender.sendMessage("  Pending shopkeeper spawns | max: " + var10001 + " | " + spawnQueueStatistics.getMaxPendingCount());
      Timings chunkActivationTimings = this.chunkActivator.getChunkActivationTimings();
      double avgChunkActivationTimings = chunkActivationTimings.getAverageTimeMillis();
      double maxChunkActivationTimings = chunkActivationTimings.getMaxTimeMillis();
      String var54 = TextUtils.format(avgChunkActivationTimings);
      sender.sendMessage("  Chunk activation timings (avg | max | cnt): " + var54 + " ms | " + TextUtils.format(maxChunkActivationTimings) + " ms | " + chunkActivationTimings.getCounter());
      double avgTotalAITimings = entityAI.getTotalTimings().getAverageTimeMillis();
      double maxTotalAITiming = entityAI.getTotalTimings().getMaxTimeMillis();
      var10001 = Settings.entityBehaviorTickPeriod;
      sender.sendMessage("  Total AI timings (per " + var10001 + " ticks) (avg | max): " + TextUtils.format(avgTotalAITimings) + " ms | " + TextUtils.format(maxTotalAITiming) + " ms");
      double avgAIActivationTimings = entityAI.getActivationTimings().getAverageTimeMillis();
      double maxAIActivationTiming = entityAI.getActivationTimings().getMaxTimeMillis();
      var54 = TextUtils.format(avgAIActivationTimings);
      sender.sendMessage("    AI activation timings (per 30 ticks) (avg | max): " + var54 + " ms | " + TextUtils.format(maxAIActivationTiming) + " ms");
      double avgGravityTimings = entityAI.getGravityTimings().getAverageTimeMillis();
      double maxGravityTiming = entityAI.getGravityTimings().getMaxTimeMillis();
      var10001 = Settings.entityBehaviorTickPeriod;
      sender.sendMessage("    Gravity timings (per " + var10001 + " ticks) (avg | max): " + TextUtils.format(avgGravityTimings) + " ms | " + TextUtils.format(maxGravityTiming) + " ms");
      double avgAITimings = entityAI.getAITimings().getAverageTimeMillis();
      double maxAITiming = entityAI.getAITimings().getMaxTimeMillis();
      var10001 = Settings.entityBehaviorTickPeriod;
      sender.sendMessage("    AI timings (per " + var10001 + " ticks) (avg | max): " + TextUtils.format(avgAITimings) + " ms | " + TextUtils.format(maxAITiming) + " ms");
      Iterator var31 = Bukkit.getWorlds().iterator();

      while(true) {
         int worldTotalShopkeepers;
         Map shopkeepersByChunks;
         do {
            do {
               do {
                  if (!var31.hasNext()) {
                     if (isConsole && listActive) {
                        sender.sendMessage("All active shopkeepers:");
                        var31 = this.shopkeeperRegistry.getActiveShopkeepers().iterator();

                        while(var31.hasNext()) {
                           Shopkeeper shopkeeper = (Shopkeeper)var31.next();
                           ShopObject shopObject = shopkeeper.getShopObject();
                           if (shopObject.isActive()) {
                              Location location = (Location)Unsafe.assertNonNull(shopObject.getLocation());
                              var54 = shopkeeper.getLocatedLogPrefix();
                              sender.sendMessage(var54 + "Active (" + TextUtils.getLocationString(location) + ")");
                           } else {
                              sender.sendMessage(shopkeeper.getLocatedLogPrefix() + "INACTIVE");
                           }
                        }
                     }

                     if (!isConsole && (listChunks || listActive)) {
                        sender.sendMessage("More information is printed when the command is run from console.");
                     }

                     return;
                  }

                  World world = (World)var31.next();
                  String worldName = world.getName();
                  Chunk[] worldLoadedChunks = world.getLoadedChunks();
                  int chunksWithLoadedEntities = 0;
                  List<Entity> worldEntities = world.getEntities();
                  int worldDeadEntities = 0;
                  int worldInvalidEntities = 0;
                  Iterator var39 = worldEntities.iterator();

                  while(var39.hasNext()) {
                     Entity entity = (Entity)var39.next();
                     if (entity.isDead()) {
                        ++worldDeadEntities;
                     }

                     if (!entity.isValid()) {
                        ++worldInvalidEntities;
                     }
                  }

                  int worldDeadEntitiesInChunks = 0;
                  int worldInvalidEntitiesInChunks = 0;
                  Chunk[] var41 = worldLoadedChunks;
                  int worldActiveChunks = worldLoadedChunks.length;

                  int worldShopkeepersInActiveChunks;
                  int worldShopkeepersInLoadedChunks;
                  for(worldShopkeepersInActiveChunks = 0; worldShopkeepersInActiveChunks < worldActiveChunks; ++worldShopkeepersInActiveChunks) {
                     Chunk chunk = var41[worldShopkeepersInActiveChunks];
                     if (chunk.isEntitiesLoaded()) {
                        ++chunksWithLoadedEntities;
                        Entity[] var45 = chunk.getEntities();
                        worldShopkeepersInLoadedChunks = var45.length;

                        for(int var47 = 0; var47 < worldShopkeepersInLoadedChunks; ++var47) {
                           Entity entity = var45[var47];
                           if (entity.isDead()) {
                              ++worldDeadEntitiesInChunks;
                           }

                           if (!entity.isValid()) {
                              ++worldInvalidEntitiesInChunks;
                           }
                        }
                     }
                  }

                  worldTotalShopkeepers = this.shopkeeperRegistry.getShopkeepersInWorld(worldName).size();
                  worldActiveChunks = this.shopkeeperRegistry.getActiveChunks(worldName).size();
                  worldShopkeepersInActiveChunks = this.shopkeeperRegistry.getActiveShopkeepers(worldName).size();
                  int worldChunksWithShopkeepers = 0;
                  int worldLoadedChunksWithShopkeepers = 0;
                  worldShopkeepersInLoadedChunks = 0;
                  shopkeepersByChunks = this.shopkeeperRegistry.getShopkeepersByChunks(worldName);
                  Iterator var64 = shopkeepersByChunks.entrySet().iterator();

                  while(var64.hasNext()) {
                     Entry<? extends ChunkCoords, ? extends Collection<? extends Shopkeeper>> chunkEntry = (Entry)var64.next();
                     ChunkCoords chunkCoords = (ChunkCoords)chunkEntry.getKey();
                     Collection<? extends Shopkeeper> chunkShopkeepers = (Collection)chunkEntry.getValue();
                     ++worldChunksWithShopkeepers;
                     if (chunkCoords.isChunkLoaded()) {
                        ++worldLoadedChunksWithShopkeepers;
                        worldShopkeepersInLoadedChunks += chunkShopkeepers.size();
                     }
                  }

                  var54 = String.valueOf(ChatColor.YELLOW);
                  sender.sendMessage(var54 + "Shopkeepers in world '" + world.getName() + "':");
                  sender.sendMessage("  Total: " + worldTotalShopkeepers);
                  sender.sendMessage("  Entities | invalid | dead: " + worldEntities.size() + " | " + worldInvalidEntities + " | " + worldDeadEntities);
                  sender.sendMessage("  Entities in chunks (invalid | dead): " + worldInvalidEntitiesInChunks + " | " + worldDeadEntitiesInChunks);
                  sender.sendMessage("  Loaded chunks: " + worldLoadedChunks.length + " (with loaded entities: " + chunksWithLoadedEntities + ")");
                  if (worldTotalShopkeepers > 0) {
                     sender.sendMessage("  Chunks with shopkeepers | loaded | active: " + worldChunksWithShopkeepers + " | " + worldLoadedChunksWithShopkeepers + " | " + worldActiveChunks);
                     sender.sendMessage("  Shopkeepers in chunks (loaded | active): " + worldShopkeepersInLoadedChunks + " | " + worldShopkeepersInActiveChunks);
                  }
               } while(!isConsole);
            } while(!listChunks);
         } while(worldTotalShopkeepers <= 0);

         sender.sendMessage("  Listing of all chunks with shopkeepers:");
         int line = 0;
         Iterator var66 = shopkeepersByChunks.entrySet().iterator();

         while(var66.hasNext()) {
            Entry<? extends ChunkCoords, ? extends Collection<? extends Shopkeeper>> chunkEntry = (Entry)var66.next();
            ChunkCoords chunkCoords = (ChunkCoords)chunkEntry.getKey();
            Collection<? extends Shopkeeper> chunkShopkeepers = (Collection)chunkEntry.getValue();
            ++line;
            ChatColor lineColor = line % 2 == 0 ? ChatColor.WHITE : ChatColor.GRAY;
            var54 = String.valueOf(lineColor);
            sender.sendMessage("    (" + var54 + chunkCoords.getChunkX() + "," + chunkCoords.getChunkZ() + String.valueOf(ChatColor.RESET) + ") [" + (chunkCoords.isChunkLoaded() ? String.valueOf(ChatColor.GREEN) + "loaded" : String.valueOf(ChatColor.DARK_GRAY) + "unloaded") + String.valueOf(ChatColor.RESET) + " | " + (this.shopkeeperRegistry.isChunkActive(chunkCoords) ? String.valueOf(ChatColor.GREEN) + "active" : String.valueOf(ChatColor.DARK_GRAY) + "inactive") + String.valueOf(ChatColor.RESET) + "]: " + chunkShopkeepers.size());
         }
      }
   }
}
