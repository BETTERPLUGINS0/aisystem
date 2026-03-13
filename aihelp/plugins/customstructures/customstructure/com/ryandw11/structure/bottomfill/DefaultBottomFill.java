package com.ryandw11.structure.bottomfill;

import com.ryandw11.structure.CustomStructures;
import com.ryandw11.structure.structure.Structure;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class DefaultBottomFill extends BukkitRunnable implements BottomFillImpl {
   private Structure structure;
   private Location spawnLocation;
   private Material fillMaterial;
   private int minY;
   private Queue<BlockVector2> groundPlane;

   public void performFill(Structure structure, Location spawnLocation, Location minLoc, Location maxLoc, AffineTransform transform) {
      Optional<Material> fillMaterial = structure.getBottomSpaceFill().getFillMaterial(spawnLocation.getBlock().getBiome());
      if (fillMaterial.isPresent()) {
         this.fillMaterial = (Material)fillMaterial.get();
         this.structure = structure;
         this.spawnLocation = spawnLocation;
         this.groundPlane = new LinkedList();
         this.minY = minLoc.getBlockY();
         Bukkit.getScheduler().runTaskAsynchronously(CustomStructures.getInstance(), () -> {
            String var10002 = String.valueOf(CustomStructures.getInstance().getDataFolder());
            File file = new File(var10002 + "/schematics/" + structure.getSchematic());
            ClipboardFormat format = ClipboardFormats.findByFile(file);
            if (format == null) {
               CustomStructures.getInstance().getLogger().warning("Invalid schematic format for schematic " + structure.getSchematic());
               CustomStructures.getInstance().getLogger().warning("Please create a valid schematic using the in-game commands");
            } else {
               try {
                  ClipboardReader reader = format.getReader(new FileInputStream(file));

                  try {
                     Clipboard clipboard = reader.read();
                     int oX = spawnLocation.getBlockX();
                     int oY = spawnLocation.getBlockY();
                     int oZ = spawnLocation.getBlockZ();
                     int clipboardMinY = clipboard.getMinimumPoint().getBlockY();

                     for(int x = clipboard.getMinimumPoint().getBlockX(); x <= clipboard.getMaximumPoint().getBlockX(); ++x) {
                        for(int z = clipboard.getMinimumPoint().getBlockZ(); z <= clipboard.getMaximumPoint().getBlockZ(); ++z) {
                           if (clipboard.getBlock(BlockVector3.at(x, clipboardMinY, z)).getBlockType().getMaterial().isMovementBlocker()) {
                              BlockVector3 groundPoint = BlockVector3.at(x, clipboardMinY, z);
                              groundPoint = groundPoint.subtract(clipboard.getOrigin());
                              Vector3 transformed = transform.apply(groundPoint.toVector3());
                              groundPoint = transformed.add((double)oX, (double)oY, (double)oZ).toBlockPoint();
                              this.groundPlane.add(groundPoint.toBlockVector2());
                           }
                        }
                     }
                  } catch (Throwable var17) {
                     if (reader != null) {
                        try {
                           reader.close();
                        } catch (Throwable var16) {
                           var17.addSuppressed(var16);
                        }
                     }

                     throw var17;
                  }

                  if (reader != null) {
                     reader.close();
                  }
               } catch (FileNotFoundException var18) {
                  CustomStructures.getInstance().getLogger().warning("Cannot find schematic file " + file.getPath());
                  CustomStructures.getInstance().getLogger().warning("Bottom fill will not be applied to structure " + structure.getName());
                  return;
               } catch (IOException var19) {
                  CustomStructures.getInstance().getLogger().warning("Some unknown error occurs while reading " + file.getPath());
                  CustomStructures.getInstance().getLogger().warning("Bottom fill will not be applied to structure " + structure.getName());
                  return;
               }

               Bukkit.getScheduler().runTask(CustomStructures.getInstance(), () -> {
                  this.runTaskTimer(CustomStructures.getInstance(), 0L, 2L);
               });
            }
         });
      }
   }

   public void run() {
      World world = this.spawnLocation.getWorld();
      if (world == null) {
         CustomStructures.getInstance().getLogger().warning("The world in which the structure " + this.structure.getName() + " spawns is not loaded");
         CustomStructures.getInstance().getLogger().warning("Bottom fill will not be applied to structure " + this.structure.getName());
         this.cancel();
      } else {
         for(int i = 0; i < 8; ++i) {
            BlockVector2 groundPoint = (BlockVector2)this.groundPlane.poll();
            if (groundPoint == null) {
               this.cancel();
               return;
            }

            int y = this.minY - 1;
            int x = groundPoint.getBlockX();
            int z = groundPoint.getBlockZ();

            for(int j = 0; j < 64; ++j) {
               boolean shouldFill = world.getBlockAt(x, y, z).isEmpty() || CustomStructures.getInstance().getBlockIgnoreManager().getBlocks().contains(world.getBlockAt(x, y, z).getType()) || this.structure.getStructureProperties().shouldIgnoreWater() && world.getBlockAt(x, y, z).getType() == Material.WATER;
               if (!shouldFill) {
                  break;
               }

               world.getBlockAt(x, y--, z).setType(this.fillMaterial);
            }
         }

      }
   }
}
