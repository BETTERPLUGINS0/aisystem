package com.ryandw11.structure.schematic;

import com.ryandw11.structure.CustomStructures;
import com.ryandw11.structure.api.StructureSpawnEvent;
import com.ryandw11.structure.api.holder.StructureSpawnHolder;
import com.ryandw11.structure.bottomfill.BottomFillProvider;
import com.ryandw11.structure.io.BlockTag;
import com.ryandw11.structure.structure.Structure;
import com.ryandw11.structure.structure.properties.MaskProperty;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.mask.MaskIntersection;
import com.sk89q.worldedit.function.mask.MaskUnion;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import me.ryandw11.ods.ObjectDataStructure;
import me.ryandw11.ods.tags.IntTag;
import me.ryandw11.ods.tags.ListTag;
import me.ryandw11.ods.tags.ObjectTag;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

public class SchematicHandler {
   private SchematicHandler() {
   }

   public static void placeSchematic(Location loc, String filename, boolean useAir, Structure structure, int iteration) throws IOException, WorldEditException {
      CustomStructures plugin = CustomStructures.getInstance();
      if (iteration > structure.getStructureLimitations().getIterationLimit()) {
         plugin.getLogger().severe("Critical Error: StackOverflow detected. Automatically terminating the spawning of the structure.");
         plugin.getLogger().severe("The structure '" + structure.getName() + "' has spawned too many sub structure via recursion.");
      } else {
         String var10002 = String.valueOf(plugin.getDataFolder());
         File schematicFile = new File(var10002 + "/schematics/" + filename);
         if (!schematicFile.exists() && iteration == 0) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&b[&aCustomStructures&b] &cA fatal error has occurred! Please check the console for errors."));
            plugin.getLogger().warning("Error: The schematic " + filename + " does not exist!");
            plugin.getLogger().warning("If this is your first time using this plugin you need to put a schematic in the schematic folder.");
            plugin.getLogger().warning("Then add it into the config.");
            plugin.getLogger().warning("If you need help look at the wiki: https://github.com/ryandw11/CustomStructures/wiki or contact Ryandw11 on spigot!");
            plugin.getLogger().warning("The plugin will now disable to prevent damage to the server.");
            Bukkit.getPluginManager().disablePlugin(plugin);
         } else if (!schematicFile.exists()) {
            plugin.getLogger().warning("Error: The schematic " + filename + " does not exist!");
            throw new RuntimeException("Cannot find schematic file!");
         } else {
            ClipboardFormat format = ClipboardFormats.findByFile(schematicFile);
            if (format == null) {
               plugin.getLogger().warning("Invalid schematic format for schematic " + filename + "!");
               plugin.getLogger().warning("Please create a valid schematic using the in-game commands!");
            } else {
               ClipboardReader reader = format.getReader(new FileInputStream(schematicFile));

               Clipboard clipboard;
               try {
                  clipboard = reader.read();
               } catch (Throwable var23) {
                  if (reader != null) {
                     try {
                        reader.close();
                     } catch (Throwable var21) {
                        var23.addSuppressed(var21);
                     }
                  }

                  throw var23;
               }

               if (reader != null) {
                  reader.close();
               }

               ClipboardHolder ch = new ClipboardHolder(clipboard);
               AffineTransform transform = new AffineTransform();
               double rotY = Math.toDegrees(structure.getBaseRotation());
               if (structure.getStructureProperties().isRandomRotation() && iteration == 0) {
                  rotY = (double)((new Random()).nextInt(4) * 90);
                  transform = transform.rotateY(rotY);
                  ch.setTransform(ch.getTransform().combine(transform));
               } else if (iteration != 0) {
                  rotY = Math.toDegrees(structure.getSubSchemRotation());
                  transform = transform.rotateY(rotY);
                  ch.setTransform(ch.getTransform().combine(transform));
               }

               EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(BukkitAdapter.adapt((World)Objects.requireNonNull(loc.getWorld())), -1);

               try {
                  Mask sourceMask = null;
                  if (structure.getSourceMaskProperties().getUnionType() == MaskProperty.MaskUnion.AND) {
                     sourceMask = new MaskIntersection(structure.getSourceMaskProperties().getMasks(clipboard));
                  } else if (structure.getSourceMaskProperties().getUnionType() == MaskProperty.MaskUnion.OR) {
                     sourceMask = new MaskUnion(structure.getSourceMaskProperties().getMasks(clipboard));
                  }

                  Mask targetMask = null;
                  if (structure.getTargetMaskProperties().getUnionType() == MaskProperty.MaskUnion.AND) {
                     targetMask = new MaskIntersection(structure.getTargetMaskProperties().getMasks(editSession));
                  } else if (structure.getSourceMaskProperties().getUnionType() == MaskProperty.MaskUnion.OR) {
                     targetMask = new MaskUnion(structure.getTargetMaskProperties().getMasks(editSession));
                  }

                  editSession.setMask((Mask)targetMask);
                  Operation operation = ch.createPaste(editSession).to(BlockVector3.at(loc.getX(), loc.getY(), loc.getZ())).maskSource((Mask)sourceMask).ignoreAirBlocks(!useAir).build();
                  Operations.complete(operation);
                  if (plugin.getConfig().getBoolean("debug")) {
                     plugin.getLogger().info(String.format("(%s) Created an instance of %s at %s, %s, %s with rotation %s", loc.getWorld().getName(), filename, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), rotY));
                  }
               } catch (Throwable var24) {
                  if (editSession != null) {
                     try {
                        editSession.close();
                     } catch (Throwable var22) {
                        var24.addSuppressed(var22);
                     }
                  }

                  throw var24;
               }

               if (editSession != null) {
                  editSession.close();
               }

               if (structure.getBottomSpaceFill().isEnabled()) {
                  Location minLoc = SchematicLocationUtils.getMinimumLocation(clipboard, loc, rotY);
                  Location maxLoc = SchematicLocationUtils.getMaximumLocation(clipboard, loc, rotY);
                  int lowX = Math.min(minLoc.getBlockX(), maxLoc.getBlockX());
                  int lowY = Math.min(minLoc.getBlockY(), maxLoc.getBlockY());
                  int lowZ = Math.min(minLoc.getBlockZ(), maxLoc.getBlockZ());
                  int highX = Math.max(minLoc.getBlockX(), maxLoc.getBlockX());
                  int highY = Math.max(minLoc.getBlockY(), maxLoc.getBlockY());
                  int highZ = Math.max(minLoc.getBlockZ(), maxLoc.getBlockZ());
                  BottomFillProvider.provide().performFill(structure, loc, new Location(minLoc.getWorld(), (double)lowX, (double)lowY, (double)lowZ), new Location(minLoc.getWorld(), (double)highX, (double)highY, (double)highZ), transform);
               }

               Bukkit.getScheduler().runTaskLater(plugin, () -> {
                  List<Location> containersAndSignsLocations = new ArrayList();
                  Location maxLoc;
                  if (structure.isCompiled()) {
                     String var10004 = String.valueOf(plugin.getDataFolder());
                     ObjectDataStructure ods = new ObjectDataStructure(new File(var10004 + "/schematics/" + structure.getCompiledSchematic()));
                     ListTag<ObjectTag> containers = (ListTag)ods.get("containers");
                     ListTag<ObjectTag> signs = (ListTag)ods.get("signs");
                     maxLoc = SchematicLocationUtils.getMinimumLocation(clipboard, loc, 0.0D);
                     Location maximumPoint = SchematicLocationUtils.getMaximumLocation(clipboard, loc, 0.0D);
                     int minX = Math.min(maxLoc.getBlockX(), maximumPoint.getBlockX());
                     int minY = Math.min(maxLoc.getBlockY(), maximumPoint.getBlockY());
                     int minZ = Math.min(maxLoc.getBlockZ(), maximumPoint.getBlockZ());
                     Iterator var17 = containers.getValue().iterator();

                     ObjectTag sign;
                     while(var17.hasNext()) {
                        sign = (ObjectTag)var17.next();
                        ((List)containersAndSignsLocations).add(SchematicLocationUtils.rotateAround((new BlockTag(sign)).getLocation(loc.getWorld()).add((double)minX, (double)minY, (double)minZ), loc, rotY));
                     }

                     var17 = signs.getValue().iterator();

                     while(var17.hasNext()) {
                        sign = (ObjectTag)var17.next();
                        ((List)containersAndSignsLocations).add(SchematicLocationUtils.rotateAround((new BlockTag(sign)).getLocation(loc.getWorld()).add((double)minX, (double)minY, (double)minZ), loc, rotY));
                     }
                  } else {
                     containersAndSignsLocations = getContainersAndSignsLocations(ch.getClipboard(), loc, rotY, structure);
                  }

                  Iterator var19 = ((List)containersAndSignsLocations).iterator();

                  while(var19.hasNext()) {
                     Location location = (Location)var19.next();
                     if (location.getBlock().getState() instanceof Container) {
                        LootTableReplacer.replaceContainerContent(structure, location);
                     }

                     if (location.getBlock().getState() instanceof Sign) {
                        Location minLoc = SchematicLocationUtils.getMinimumLocation(clipboard, loc, rotY);
                        maxLoc = SchematicLocationUtils.getMaximumLocation(clipboard, loc, rotY);
                        SchematicSignReplacer.processAndReplaceSign(location, minLoc, maxLoc, structure, rotY);
                     }

                     if (location.getBlock().getState() instanceof Sign) {
                        SchematicSignReplacer.replaceSignWithSchematic(location, structure, iteration);
                     }
                  }

                  replaceBlocks(clipboard, loc, rotY, structure);
                  if (iteration < 1) {
                     StructureSpawnHolder structureSpawnHolder = new StructureSpawnHolder(SchematicLocationUtils.getMinimumLocation(clipboard, loc, 0.0D), SchematicLocationUtils.getMaximumLocation(clipboard, loc, 0.0D), (List)containersAndSignsLocations);
                     StructureSpawnEvent structureSpawnEvent = new StructureSpawnEvent(structure, loc, rotY, structureSpawnHolder);
                     Bukkit.getServer().getPluginManager().callEvent(structureSpawnEvent);
                  }

               }, Math.round(structure.getStructureLimitations().getReplacementBlocksDelay() * 20.0D));
            }
         }
      }
   }

   public static void placeSchematic(Location loc, String filename, boolean useAir, Structure structure) throws IOException, WorldEditException {
      placeSchematic(loc, filename, useAir, structure, 0);
   }

   public static boolean createSchematic(String name, Player player, World w, boolean compile) {
      CustomStructures plugin = CustomStructures.getInstance();

      try {
         WorldEditPlugin worldEditPlugin = (WorldEditPlugin)Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");

         assert worldEditPlugin != null;

         Region selection = worldEditPlugin.getSession(player).getSelection(BukkitAdapter.adapt(w));
         CuboidRegion region = new CuboidRegion(selection.getWorld(), selection.getMinimumPoint(), selection.getMaximumPoint());
         BlockArrayClipboard clipboard = new BlockArrayClipboard(region);
         clipboard.setOrigin(BlockVector3.at(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()));

         try {
            EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(selection.getWorld(), -1);

            try {
               ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(editSession, region, clipboard, region.getMinimumPoint());
               Operations.complete(forwardExtentCopy);
            } catch (Throwable var17) {
               if (editSession != null) {
                  try {
                     editSession.close();
                  } catch (Throwable var14) {
                     var17.addSuppressed(var14);
                  }
               }

               throw var17;
            }

            if (editSession != null) {
               editSession.close();
            }
         } catch (WorldEditException var18) {
            var18.printStackTrace();
         }

         String var10002 = String.valueOf(plugin.getDataFolder());
         File file = new File(var10002 + File.separator + "schematics" + File.separator + name + ".schem");

         try {
            ClipboardWriter writer = BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(new FileOutputStream(file));

            try {
               writer.write(clipboard);
            } catch (Throwable var15) {
               if (writer != null) {
                  try {
                     writer.close();
                  } catch (Throwable var13) {
                     var15.addSuppressed(var13);
                  }
               }

               throw var15;
            }

            if (writer != null) {
               writer.close();
            }
         } catch (IOException var16) {
            var16.printStackTrace();
         }

         if (compile) {
            compileSchem(player.getLocation(), selection, name);
         }

         return true;
      } catch (IncompleteRegionException var19) {
         return false;
      }
   }

   public static boolean compileOnly(String name, Player player, World w) {
      try {
         WorldEditPlugin worldEditPlugin = (WorldEditPlugin)Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");

         assert worldEditPlugin != null;

         Region selection = worldEditPlugin.getSession(player).getSelection(BukkitAdapter.adapt(w));
         compileSchem(player.getLocation(), selection, name);
         return true;
      } catch (IncompleteRegionException var5) {
         return false;
      }
   }

   private static void compileSchem(Location loc, Region reg, String name) {
      CustomStructures plugin = CustomStructures.getInstance();
      IntTag intTag = new IntTag("ver", 1);
      ListTag<BlockTag> containers = new ListTag("containers", new ArrayList());
      ListTag<BlockTag> signs = new ListTag("signs", new ArrayList());
      List<Location> locations = new ArrayList();
      Location minLoc = new Location(loc.getWorld(), (double)reg.getMinimumPoint().getX(), (double)reg.getMinimumPoint().getY(), (double)reg.getMinimumPoint().getZ());

      for(int x = reg.getMinimumPoint().getX(); x <= reg.getMaximumPoint().getX(); ++x) {
         for(int y = reg.getMinimumPoint().getY(); y <= reg.getMaximumPoint().getY(); ++y) {
            for(int z = reg.getMinimumPoint().getZ(); z <= reg.getMaximumPoint().getZ(); ++z) {
               Location location = new Location(loc.getWorld(), (double)x, (double)y, (double)z);
               Block block = location.getBlock();
               BlockState blockState = location.getBlock().getState();
               if (blockState instanceof Container) {
                  if (blockState instanceof Chest) {
                     Chest chestBlockState = (Chest)blockState;
                     InventoryHolder holder = chestBlockState.getInventory().getHolder();
                     if (holder instanceof DoubleChest) {
                        DoubleChest doubleChest = (DoubleChest)holder;
                        Location leftSideLocation = ((Chest)doubleChest.getLeftSide()).getLocation();
                        Location rightSideLocation = ((Chest)doubleChest.getRightSide()).getLocation();
                        Location roundedLocation = new Location(location.getWorld(), Math.floor(location.getX()), Math.floor(location.getY()), Math.floor(location.getZ()));
                        if (leftSideLocation.distance(roundedLocation) < 1.0D) {
                           if (SchematicLocationUtils.isNotAlreadyIn(locations, rightSideLocation)) {
                              locations.add(roundedLocation);
                              containers.addTag(new BlockTag(Material.CHEST, location.subtract(minLoc)));
                           }
                        } else if (rightSideLocation.distance(roundedLocation) < 1.0D && SchematicLocationUtils.isNotAlreadyIn(locations, leftSideLocation)) {
                           locations.add(roundedLocation);
                           containers.addTag(new BlockTag(Material.CHEST, location.subtract(minLoc)));
                        }
                     } else if (holder instanceof Chest) {
                        locations.add(location);
                        containers.addTag(new BlockTag(Material.CHEST, location.subtract(minLoc)));
                     }
                  } else {
                     locations.add(location);
                     containers.addTag(new BlockTag(block.getType(), location.subtract(minLoc)));
                  }
               } else if (blockState instanceof Sign) {
                  locations.add(location);
                  signs.addTag(new BlockTag(block.getType(), location.subtract(minLoc)));
               }
            }
         }
      }

      String var10004 = String.valueOf(plugin.getDataFolder());
      ObjectDataStructure ods = new ObjectDataStructure(new File(var10004 + File.separator + "schematics" + File.separator + name + ".cschem"));
      ods.save(Arrays.asList(intTag, containers, signs));
      if (plugin.isDebug()) {
         plugin.getLogger().info("Successfully compiled the schematic: " + name);
      }

   }

   private static void replaceBlocks(Clipboard clipboard, Location pasteLocation, double rotation, Structure structure) {
      if (!structure.getStructureLimitations().getBlockReplacement().isEmpty()) {
         Location minLoc = SchematicLocationUtils.getMinimumLocation(clipboard, pasteLocation, rotation);
         Location maxLoc = SchematicLocationUtils.getMaximumLocation(clipboard, pasteLocation, rotation);
         int lowX = Math.min(minLoc.getBlockX(), maxLoc.getBlockX());
         int lowY = Math.min(minLoc.getBlockY(), maxLoc.getBlockY());
         int lowZ = Math.min(minLoc.getBlockZ(), maxLoc.getBlockZ());

         for(int x = 0; x <= Math.abs(minLoc.getBlockX() - maxLoc.getBlockX()); ++x) {
            for(int y = 0; y <= Math.abs(minLoc.getBlockY() - maxLoc.getBlockY()); ++y) {
               for(int z = 0; z <= Math.abs(minLoc.getBlockZ() - maxLoc.getBlockZ()); ++z) {
                  Block block = ((World)Objects.requireNonNull(pasteLocation.getWorld())).getBlockAt(lowX + x, lowY + y, lowZ + z);
                  if (structure.getStructureLimitations().getBlockReplacement().containsKey(block.getType())) {
                     block.setType((Material)structure.getStructureLimitations().getBlockReplacement().get(block.getType()));
                     block.getState().update();
                  }
               }
            }
         }

      }
   }

   private static List<Location> getContainersAndSignsLocations(Clipboard clipboard, Location pasteLocation, double rotation, Structure structure) {
      Location minLoc = SchematicLocationUtils.getMinimumLocation(clipboard, pasteLocation, rotation);
      Location maxLoc = SchematicLocationUtils.getMaximumLocation(clipboard, pasteLocation, rotation);
      List<Location> locations = new ArrayList();
      int lowX = Math.min(minLoc.getBlockX(), maxLoc.getBlockX());
      int lowY = Math.min(minLoc.getBlockY(), maxLoc.getBlockY());
      int lowZ = Math.min(minLoc.getBlockZ(), maxLoc.getBlockZ());

      for(int x = 0; x <= Math.abs(minLoc.getBlockX() - maxLoc.getBlockX()); ++x) {
         for(int y = 0; y <= Math.abs(minLoc.getBlockY() - maxLoc.getBlockY()); ++y) {
            for(int z = 0; z <= Math.abs(minLoc.getBlockZ() - maxLoc.getBlockZ()); ++z) {
               Location location = new Location(pasteLocation.getWorld(), (double)(lowX + x), (double)(lowY + y), (double)(lowZ + z));
               Block block = location.getBlock();
               BlockState blockState = location.getBlock().getState();
               if (blockState instanceof Container) {
                  if (blockState instanceof Chest) {
                     InventoryHolder holder = ((Chest)blockState).getInventory().getHolder();
                     if (holder instanceof DoubleChest) {
                        DoubleChest doubleChest = (DoubleChest)holder;
                        Location leftSideLocation = ((Chest)doubleChest.getLeftSide()).getLocation();
                        Location rightSideLocation = ((Chest)doubleChest.getRightSide()).getLocation();
                        Location roundedLocation = new Location(location.getWorld(), Math.floor(location.getX()), Math.floor(location.getY()), Math.floor(location.getZ()));
                        if (leftSideLocation.distance(roundedLocation) < 1.0D) {
                           if (SchematicLocationUtils.isNotAlreadyIn(locations, rightSideLocation)) {
                              locations.add(roundedLocation);
                           }
                        } else if (rightSideLocation.distance(roundedLocation) < 1.0D && SchematicLocationUtils.isNotAlreadyIn(locations, leftSideLocation)) {
                           locations.add(roundedLocation);
                        }
                     } else if (holder instanceof Chest) {
                        locations.add(location);
                     }
                  } else {
                     locations.add(location);
                  }
               } else if (blockState instanceof Sign) {
                  locations.add(location);
               } else if (!structure.getStructureLimitations().getBlockReplacement().isEmpty() && structure.getStructureLimitations().getBlockReplacement().containsKey(block.getType())) {
                  block.setType((Material)structure.getStructureLimitations().getBlockReplacement().get(block.getType()));
                  block.getState().update();
               }
            }
         }
      }

      return locations;
   }
}
