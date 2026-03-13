package libs.com.ryderbelserion.vital.paper.util.structures;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import libs.com.ryderbelserion.vital.common.VitalAPI;
import libs.com.ryderbelserion.vital.common.api.Provider;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.structure.Structure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StructureManager implements IStructureManager {
   private final Set<Location> postStructurePasteBlocks = new HashSet();
   private final Set<Location> preStructurePasteBlocks = new HashSet();
   private final VitalAPI api = Provider.getApi();
   private final ComponentLogger logger;
   private final boolean isVerbose;
   private File file;
   private Structure structure;
   private boolean doNotApply;
   private final JavaPlugin plugin;

   public StructureManager(@NotNull JavaPlugin plugin) {
      this.logger = this.api.getComponentLogger();
      this.isVerbose = this.api.isVerbose();
      this.file = null;
      this.structure = null;
      this.doNotApply = false;
      this.plugin = plugin;
   }

   public void applyStructure(@Nullable File file) {
      if (file == null) {
         this.doNotApply = true;
      } else {
         this.file = file;
         this.structure = (Structure)CompletableFuture.supplyAsync(() -> {
            try {
               return this.plugin.getServer().getStructureManager().loadStructure(this.file);
            } catch (IOException var2) {
               if (this.isVerbose) {
                  this.logger.error("Failed to load structure: {}!", this.file.getName(), var2);
               }

               return null;
            }
         }).join();
      }
   }

   @NotNull
   public final org.bukkit.structure.StructureManager getStructureManager() {
      return this.plugin.getServer().getStructureManager();
   }

   public void saveStructure(@Nullable File file, @Nullable Location one, @Nullable Location two, boolean includeEntities) {
      if (!this.doNotApply) {
         if (file != null && one != null && two != null) {
            this.structure.fill(one, two, includeEntities);

            try {
               this.getStructureManager().saveStructure(file, this.structure);
            } catch (IOException var6) {
               if (this.isVerbose) {
                  this.logger.error("Failed to save structure to: {}!", file.getName(), var6);
               }
            }

         }
      }
   }

   public void pasteStructure(@Nullable Location location, boolean storeBlocks) {
      if (!this.doNotApply) {
         if (location != null) {
            try {
               if (storeBlocks) {
                  this.getBlocks(location);
               }

               Location clonedLocation = location.clone().subtract(2.0D, 0.0D, 2.0D);
               this.structure.place(clonedLocation, false, StructureRotation.NONE, Mirror.NONE, 0, 1.0F, ThreadLocalRandom.current());
               if (storeBlocks) {
                  this.getStructureBlocks(clonedLocation);
               }
            } catch (Exception var4) {
               if (this.isVerbose) {
                  this.logger.error("Could not paste structure", var4);
               }
            }

         }
      }
   }

   public void removeStructure() {
      if (!this.doNotApply) {
         this.postStructurePasteBlocks.forEach((block) -> {
            if (block.getBlock().getType() != Material.AIR) {
               Location location = block.toBlockLocation();
               location.getBlock().setType(Material.AIR, true);
            }

         });
      }
   }

   private void getStructureBlocks(@NotNull Location location) {
      for(int x = 0; (double)x < this.getStructureX(); ++x) {
         for(int y = 0; (double)y < this.getStructureY(); ++y) {
            for(int z = 0; (double)z < this.getStructureZ(); ++z) {
               Block relativeLocation = location.getBlock().getRelative(x, y, z);
               this.postStructurePasteBlocks.add(relativeLocation.getLocation());
               this.postStructurePasteBlocks.forEach((block) -> {
                  Location blockLoc = block.toBlockLocation();
                  blockLoc.getBlock().getState().update();
               });
            }
         }
      }

   }

   @NotNull
   public final Set<Location> getBlocks(@Nullable Location location) {
      if (this.doNotApply) {
         return Collections.emptySet();
      } else if (location == null) {
         return this.getNearbyBlocks();
      } else {
         for(int x = 0; (double)x < this.getStructureX(); ++x) {
            for(int y = 0; (double)y < this.getStructureY(); ++y) {
               for(int z = 0; (double)z < this.getStructureZ(); ++z) {
                  Block relativeLocation = location.getBlock().getRelative(x, y, z).getLocation().subtract(2.0D, 0.0D, 2.0D).getBlock();
                  this.preStructurePasteBlocks.add(relativeLocation.getLocation());
               }
            }
         }

         return this.getNearbyBlocks();
      }
   }

   public final double getStructureX() {
      return this.doNotApply ? 0.0D : this.structure.getSize().getX();
   }

   public final double getStructureY() {
      return this.doNotApply ? 0.0D : this.structure.getSize().getY();
   }

   public final double getStructureZ() {
      return this.doNotApply ? 0.0D : this.structure.getSize().getZ();
   }

   @NotNull
   public final Set<Location> getNearbyBlocks() {
      return Collections.unmodifiableSet(this.preStructurePasteBlocks);
   }

   @NotNull
   public final List<Material> getBlockBlacklist() {
      return Lists.newArrayList(new Material[]{Material.OAK_SIGN, Material.SPRUCE_SIGN, Material.BIRCH_SIGN, Material.JUNGLE_SIGN, Material.ACACIA_SIGN, Material.CHERRY_SIGN, Material.DARK_OAK_SIGN, Material.MANGROVE_SIGN, Material.BAMBOO_SIGN, Material.CRIMSON_SIGN, Material.WARPED_SIGN, Material.OAK_HANGING_SIGN, Material.SPRUCE_HANGING_SIGN, Material.BIRCH_HANGING_SIGN, Material.JUNGLE_HANGING_SIGN, Material.ACACIA_HANGING_SIGN, Material.CHERRY_HANGING_SIGN, Material.DARK_OAK_HANGING_SIGN, Material.MANGROVE_HANGING_SIGN, Material.BAMBOO_HANGING_SIGN, Material.CRIMSON_HANGING_SIGN, Material.WARPED_HANGING_SIGN, Material.STONE_BUTTON, Material.POLISHED_BLACKSTONE_BUTTON, Material.OAK_BUTTON, Material.SPRUCE_BUTTON, Material.BIRCH_BUTTON, Material.JUNGLE_BUTTON, Material.ACACIA_BUTTON, Material.CHERRY_BUTTON, Material.DARK_OAK_BUTTON, Material.MANGROVE_BUTTON, Material.BAMBOO_BUTTON, Material.CRIMSON_BUTTON, Material.WARPED_BUTTON});
   }

   public void createStructure() {
      this.structure = this.getStructureManager().createStructure();
   }

   @NotNull
   public final File getStructureFile() {
      return this.file;
   }
}
