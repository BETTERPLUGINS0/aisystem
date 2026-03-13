package com.ryandw11.structure.commands.cstruct;

import com.ryandw11.structure.CustomStructures;
import com.ryandw11.structure.commands.SubCommand;
import com.ryandw11.structure.ignoreblocks.IgnoreBlocks;
import com.ryandw11.structure.structure.Structure;
import com.ryandw11.structure.structure.properties.BlockLevelLimit;
import com.ryandw11.structure.structure.properties.StructureYSpawning;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestSpawnCommand implements SubCommand {
   private final CustomStructures plugin;

   public TestSpawnCommand(CustomStructures plugin) {
      this.plugin = plugin;
   }

   public boolean subCommand(CommandSender sender, Command cmd, String s, String[] args) {
      if (args.length != 1) {
         sender.sendMessage(String.valueOf(ChatColor.RED) + "Invalid arguments. /cstruct testspawn {name}");
         return true;
      } else if (!(sender instanceof Player)) {
         sender.sendMessage("This command is for players only!");
         return true;
      } else {
         Player p = (Player)sender;
         if (!p.hasPermission("customstructures.test.spawn")) {
            p.sendMessage(String.valueOf(ChatColor.RED) + "You do not have permission for this command.");
            return true;
         } else {
            Structure structure = this.plugin.getStructureHandler().getStructure(args[0]);
            if (structure == null) {
               p.sendMessage(String.valueOf(ChatColor.RED) + "That structure does not exist!");
               return true;
            } else {
               p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b=================[&6" + structure.getName() + "&b]================="));
               this.psuedoCalculate(p, structure, p.getLocation().getBlock(), p.getLocation().getChunk());
               p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b=================[&6" + structure.getName() + "&b]================="));
               return true;
            }
         }
      }
   }

   void psuedoCalculate(Player p, Structure structure, Block bl, Chunk ch) {
      IgnoreBlocks ignoreBlocks = this.plugin.getBlockIgnoreManager();
      StructureYSpawning structureSpawnSettings = structure.getStructureLocation().getSpawnSettings();
      bl = structureSpawnSettings.getHighestBlock(bl.getLocation());
      this.canSpawn(p, structure, bl, ch);
      if (structure.getStructureProperties().isIgnoringPlants() && ignoreBlocks.getBlocks().contains(bl.getType())) {
         for(int i = bl.getY(); i >= 4; --i) {
            if (!ignoreBlocks.getBlocks().contains(ch.getBlock(8, i, 8).getType()) && !ch.getBlock(8, i, 8).getType().isAir()) {
               bl = ch.getBlock(8, i, 8);
               break;
            }
         }
      }

      if (structureSpawnSettings.isCalculateSpawnYFirst()) {
         bl = ch.getBlock(8, structureSpawnSettings.getHeight(bl.getLocation()), 8);
         this.quickSendMessage(p, "&aSpawn Y Value: " + bl.getY());
      }

      if (!structure.getStructureLimitations().hasWhitelistBlock(bl)) {
         this.quickSendMessage(p, String.format("&cFailed Block Limitation! Cannot spawn on %s! (Whitelist Defined)", bl.getType()));
      } else if (structure.getStructureLimitations().hasBlacklistBlock(bl)) {
         this.quickSendMessage(p, String.format("&cFailed Block Limitation! Cannot spawn on %s! (Blacklist Defined)", bl.getType()));
      } else if (!structure.getStructureProperties().canSpawnInWater() && bl.getType() == Material.WATER) {
         this.quickSendMessage(p, "&cFailed Water test! Cannot spawn in the water!");
      } else if (!structure.getStructureProperties().canSpawnInLavaLakes() && bl.getType() == Material.LAVA) {
         this.quickSendMessage(p, "&cFailed Lava test! Cannot spawn in the lava!");
      } else {
         if (!structureSpawnSettings.isCalculateSpawnYFirst()) {
            bl = ch.getBlock(8, structureSpawnSettings.getHeight(bl.getLocation()), 8);
            this.quickSendMessage(p, "&aSpawn Y Value: " + bl.getY());
         }

         if (structure.getStructureLimitations().getWorldHeightRestriction() != -1 && bl.getLocation().getY() > (double)(ch.getWorld().getMaxHeight() - structure.getStructureLimitations().getWorldHeightRestriction())) {
            this.quickSendMessage(p, "&cFailed World Height Restriction!");
         } else {
            if (structure.getStructureLimitations().getBlockLevelLimit().isEnabled()) {
               BlockLevelLimit limit = structure.getStructureLimitations().getBlockLevelLimit();
               int total;
               int error;
               if (limit.getMode().equalsIgnoreCase("flat")) {
                  for(total = limit.getX1() + bl.getX(); total <= limit.getX2() + bl.getX(); ++total) {
                     for(error = limit.getZ1() + bl.getZ(); error <= limit.getZ2() + bl.getZ(); ++error) {
                        Block top = ch.getWorld().getBlockAt(total, bl.getY() + 1, error);
                        Block bottom = ch.getWorld().getBlockAt(total, bl.getY() - 1, error);
                        if (!top.getType().isAir() && !ignoreBlocks.getBlocks().contains(top.getType())) {
                           if (this.plugin.isDebug()) {
                              p.sendMessage(String.valueOf(top.getLocation()) + " || TOP FAIL");
                              p.sendMessage(String.valueOf(top.getType()) + " || TOP FAIL");
                           }

                           this.quickSendMessage(p, "&cFailed Flat Block Level Limit test! The ground is not flat!");
                           return;
                        }

                        if (bottom.getType().isAir()) {
                           if (this.plugin.isDebug()) {
                              p.sendMessage(String.valueOf(bottom.getLocation()) + " || BOTTOM FAIL");
                           }

                           this.quickSendMessage(p, "&cFailed Flat Block Level Limit test! The ground is not flat!");
                           return;
                        }
                     }
                  }
               } else if (limit.getMode().equalsIgnoreCase("flat_error")) {
                  total = 0;
                  error = 0;

                  for(int x = limit.getX1() + bl.getX(); x <= limit.getX2() + bl.getX(); ++x) {
                     for(int z = limit.getZ1() + bl.getZ(); z <= limit.getZ2() + bl.getZ(); ++z) {
                        Block top = ch.getWorld().getBlockAt(x, bl.getY() + 1, z);
                        Block bottom = ch.getWorld().getBlockAt(x, bl.getY() - 1, z);
                        if (!top.getType().isAir() && !ignoreBlocks.getBlocks().contains(top.getType())) {
                           ++error;
                        }

                        if (bottom.getType().isAir()) {
                           ++error;
                        }

                        total += 2;
                     }
                  }

                  if (this.plugin.isDebug()) {
                     p.sendMessage("Percent Failure: " + (double)error / (double)total + " / " + limit.getError());
                  }

                  if ((double)error / (double)total > limit.getError()) {
                     this.quickSendMessage(p, "&cFailed Flat Error Block Level Limit test! The ground is not flat enough!");
                  }
               }
            }

         }
      }
   }

   void canSpawn(Player p, Structure structure, Block block, Chunk chunk) {
      if (!structure.getStructureLocation().canSpawnInWorld(chunk.getWorld())) {
         this.quickSendMessage(p, "&cFailed world test! Cannot spawn in current world!");
      }

      if (Math.abs(block.getX()) < structure.getStructureLocation().getXLimitation()) {
         this.quickSendMessage(p, "&cFailed X Limitation test! Cannot spawn this close to (0, 0)!");
      }

      if (Math.abs(block.getZ()) < structure.getStructureLocation().getZLimitation()) {
         this.quickSendMessage(p, "&cFailed Z Limitation test! Cannot spawn this close to (0, 0)!");
      }

      if (!CustomStructures.getInstance().getStructureHandler().validDistance(structure, block.getLocation())) {
         this.quickSendMessage(p, "&cFailed Distance Limitation test! Cannot spawn this close to another structure!");
      }

      if (!CustomStructures.getInstance().getStructureHandler().validSameDistance(structure, block.getLocation())) {
         this.quickSendMessage(p, "&cFailed Distance Limitation test! Cannot spawn this close to the same structure!");
      }

      if (ThreadLocalRandom.current().nextInt(0, structure.getProbabilityDenominator() + 1) > structure.getProbabilityNumerator()) {
         this.quickSendMessage(p, String.format("&eDid not spawn by probability! (%d/%d chance)", structure.getProbabilityNumerator(), structure.getProbabilityDenominator()));
      }

      if (!structure.getStructureLocation().hasBiome(block.getBiome())) {
         this.quickSendMessage(p, "&cFailed Biome test! Cannot spawn in this biome!");
      }

   }

   void quickSendMessage(Player p, String msg) {
      p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
   }
}
