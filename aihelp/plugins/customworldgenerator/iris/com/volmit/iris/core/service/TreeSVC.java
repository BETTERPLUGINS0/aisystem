package com.volmit.iris.core.service;

import com.volmit.iris.Iris;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.core.tools.IrisToolbelt;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.object.IObjectPlacer;
import com.volmit.iris.engine.object.IrisBiome;
import com.volmit.iris.engine.object.IrisDimension;
import com.volmit.iris.engine.object.IrisObject;
import com.volmit.iris.engine.object.IrisObjectPlacement;
import com.volmit.iris.engine.object.IrisRegion;
import com.volmit.iris.engine.object.IrisTreeModes;
import com.volmit.iris.engine.object.IrisTreeSize;
import com.volmit.iris.engine.object.TileData;
import com.volmit.iris.engine.platform.PlatformChunkGenerator;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.data.Cuboid;
import com.volmit.iris.util.data.IrisCustomData;
import com.volmit.iris.util.math.BlockPosition;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.plugin.IrisService;
import com.volmit.iris.util.scheduling.J;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;
import org.bukkit.Bukkit;
import org.bukkit.HeightMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Sapling;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.StructureGrowEvent;

public class TreeSVC implements IrisService {
   private boolean block = false;

   public void onEnable() {
   }

   public void onDisable() {
   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void on(StructureGrowEvent event) {
      if (!this.block && !var1.isCancelled()) {
         Iris.debug(this.getClass().getName() + " received a structure grow event");
         if (!IrisToolbelt.isIrisWorld(var1.getWorld())) {
            Iris.debug(this.getClass().getName() + " passed grow event off to vanilla since not an Iris world");
         } else {
            final PlatformChunkGenerator var2 = IrisToolbelt.access(var1.getWorld());
            if (var2 == null) {
               Iris.debug(this.getClass().getName() + " passed it off to vanilla because could not get IrisAccess for this world");
               Iris.reportError(new NullPointerException(var1.getWorld().getName() + " could not be accessed despite being an Iris world"));
            } else {
               final Engine var3 = var2.getEngine();
               if (var3 == null) {
                  Iris.debug(this.getClass().getName() + " passed it off to vanilla because could not get Engine for this world");
                  Iris.reportError(new NullPointerException(var1.getWorld().getName() + " could not be accessed despite being an Iris world"));
               } else {
                  IrisDimension var4 = var3.getDimension();
                  if (var4 == null) {
                     Iris.debug(this.getClass().getName() + " passed it off to vanilla because could not get Dimension for this world");
                     Iris.reportError(new NullPointerException(var1.getWorld().getName() + " could not be accessed despite being an Iris world"));
                  } else if (!var4.getTreeSettings().isEnabled()) {
                     Iris.debug(this.getClass().getName() + " cancelled because tree overrides are disabled");
                  } else {
                     BlockData var5 = var1.getLocation().getBlock().getBlockData().clone();
                     Cuboid var6 = this.getSaplings(var1.getLocation(), (var1x) -> {
                        return var1x instanceof Sapling && var1x.getMaterial().equals(var5.getMaterial());
                     }, var1.getWorld());
                     String var10000 = String.valueOf(var1.getLocation());
                     Iris.debug("Sapling grew @ " + var10000 + " for " + var1.getSpecies().name() + " usedBoneMeal is " + var1.isFromBonemeal());
                     int var12 = var6.getSizeX();
                     Iris.debug("Sapling plane is: " + var12 + " by " + var6.getSizeZ());
                     IrisObjectPlacement var7 = this.getObjectPlacement(var2, var1.getLocation(), var1.getSpecies(), new IrisTreeSize(1, 1));
                     if (var7 == null) {
                        Iris.debug(this.getClass().getName() + " had options but did not manage to find objectPlacements for them");
                     } else {
                        var6.forEach((var0) -> {
                           var0.setType(Material.AIR);
                        });
                        IrisObject var8 = (IrisObject)var2.getData().getObjectLoader().load((String)var7.getPlace().getRandom(RNG.r));
                        final KList var9 = new KList();
                        final KMap var10 = new KMap();
                        IObjectPlacer var11 = new IObjectPlacer(this) {
                           public int getHighest(int x, int z, IrisData data) {
                              return var1.getWorld().getHighestBlockYAt(var1x, var2x);
                           }

                           public int getHighest(int x, int z, IrisData data, boolean ignoreFluid) {
                              return var1.getWorld().getHighestBlockYAt(var1x, var2x, var4 ? HeightMap.OCEAN_FLOOR : HeightMap.WORLD_SURFACE);
                           }

                           public void set(int x, int y, int z, BlockData d) {
                              Block var5 = var1.getWorld().getBlockAt(var1x, var2x, var3x);
                              BlockState var6 = var5.getState();
                              if (var4 instanceof IrisCustomData) {
                                 IrisCustomData var7 = (IrisCustomData)var4;
                                 var6.setBlockData(var7.getBase());
                              } else {
                                 var6.setBlockData(var4);
                              }

                              var9.add(var5.getState());
                              var10.put(new Location(var1.getWorld(), (double)var1x, (double)var2x, (double)var3x), var4);
                           }

                           public BlockData get(int x, int y, int z) {
                              return var1.getWorld().getBlockAt(var1x, var2x, var3x).getBlockData();
                           }

                           public boolean isPreventingDecay() {
                              return true;
                           }

                           public boolean isCarved(int x, int y, int z) {
                              return false;
                           }

                           public boolean isSolid(int x, int y, int z) {
                              return this.get(var1x, var2x, var3x).getMaterial().isSolid();
                           }

                           public boolean isUnderwater(int x, int z) {
                              return false;
                           }

                           public int getFluidHeight() {
                              return var2.getEngine().getDimension().getFluidHeight();
                           }

                           public boolean isDebugSmartBore() {
                              return false;
                           }

                           public void setTile(int xx, int yy, int zz, TileData tile) {
                           }

                           public <T> void setData(int xx, int yy, int zz, T data) {
                           }

                           public <T> T getData(int xx, int yy, int zz, Class<T> t) {
                              return null;
                           }

                           public Engine getEngine() {
                              return var3;
                           }
                        };
                        var8.place(var6.getCenter().getBlockX(), var6.getCenter().getBlockY() + var8.getH() / 2, var6.getCenter().getBlockZ(), var11, var7, RNG.r, ((PlatformChunkGenerator)Objects.requireNonNull(var2)).getData());
                        var1.setCancelled(true);
                        J.s(() -> {
                           StructureGrowEvent var5 = new StructureGrowEvent(var1.getLocation(), var1.getSpecies(), var1.isFromBonemeal(), var1.getPlayer(), var9);
                           this.block = true;
                           Bukkit.getServer().getPluginManager().callEvent(var5);
                           this.block = false;
                           if (!var5.isCancelled()) {
                              Iterator var6 = var5.getBlocks().iterator();

                              while(var6.hasNext()) {
                                 BlockState var7 = (BlockState)var6.next();
                                 Location var8 = var7.getLocation();
                                 BlockData var9x = (BlockData)var10.get(var8);
                                 if (var9x != null) {
                                    Block var10x = var8.getBlock();
                                    if (var9x instanceof IrisCustomData) {
                                       IrisCustomData var11 = (IrisCustomData)var9x;
                                       var10x.setBlockData(var11.getBase(), false);
                                       ((ExternalDataSVC)Iris.service(ExternalDataSVC.class)).processUpdate(var3, var10x, var11.getCustom());
                                    } else {
                                       var10x.setBlockData(var9x, false);
                                    }
                                 }
                              }
                           }

                        });
                     }
                  }
               }
            }
         }
      }
   }

   private IrisObjectPlacement getObjectPlacement(PlatformChunkGenerator worldAccess, Location location, TreeType type, IrisTreeSize size) {
      KList var5 = new KList();
      boolean var6 = var1.getEngine().getDimension().getTreeSettings().getMode().equals(IrisTreeModes.ALL);
      IrisBiome var7 = var1.getEngine().getBiome(var2.getBlockX(), var2.getBlockY() - var1.getTarget().getWorld().minHeight(), var2.getBlockZ());
      var5.addAll(this.matchObjectPlacements(var7.getObjects(), var4, var3));
      if (var6 || var5.isEmpty()) {
         IrisRegion var8 = var1.getEngine().getRegion(var2.getBlockX(), var2.getBlockZ());
         var5.addAll(this.matchObjectPlacements(var8.getObjects(), var4, var3));
      }

      return var5.isNotEmpty() ? (IrisObjectPlacement)var5.getRandom() : null;
   }

   private KList<IrisObjectPlacement> matchObjectPlacements(KList<IrisObjectPlacement> objects, IrisTreeSize size, TreeType type) {
      KList var4 = new KList();
      Iterator var5 = var1.iterator();

      while(var5.hasNext()) {
         IrisObjectPlacement var6 = (IrisObjectPlacement)var5.next();
         if (var6.matches(var2, var3)) {
            var4.add((Object)var6);
         }
      }

      return var4;
   }

   public Cuboid getSaplings(Location at, Predicate<BlockData> valid, World world) {
      KList var4 = new KList();
      this.grow(var1.getWorld(), new BlockPosition(var1.getBlockX(), var1.getBlockY(), var1.getBlockZ()), var2, var4);
      BlockPosition var5 = new BlockPosition(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
      BlockPosition var6 = new BlockPosition(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
      Iterator var7 = var4.iterator();

      while(var7.hasNext()) {
         BlockPosition var8 = (BlockPosition)var7.next();
         var5.max(var8);
         var6.min(var8);
      }

      Iris.debug("Blocks: " + var4.size());
      String var10000 = String.valueOf(var5);
      Iris.debug("Min: " + var10000 + " Max: " + String.valueOf(var6));
      Cuboid var12 = new Cuboid(var5.toBlock(var3).getLocation(), var6.toBlock(var3).getLocation());

      for(boolean var13 = true; Math.min(var12.getSizeX(), var12.getSizeZ()) > 0; var13 = true) {
         label45:
         for(int var9 = var12.getLowerX(); var9 < var12.getUpperX(); ++var9) {
            for(int var10 = var12.getLowerY(); var10 < var12.getUpperY(); ++var10) {
               for(int var11 = var12.getLowerZ(); var11 < var12.getUpperZ(); ++var11) {
                  if (!var4.contains(new BlockPosition(var9, var10, var11))) {
                     var13 = false;
                     break label45;
                  }
               }
            }
         }

         if (var13) {
            return var12;
         }

         var12 = var12.inset(Cuboid.CuboidDirection.Horizontal, 1);
      }

      return new Cuboid(var1, var1);
   }

   private void grow(World world, BlockPosition center, Predicate<BlockData> valid, KList<BlockPosition> l) {
      if (var4.size() <= 50 && !var4.contains(var2) && var3.test(var2.toBlock(var1).getBlockData())) {
         var4.add((Object)var2);
         this.grow(var1, var2.add(1, 0, 0), var3, var4);
         this.grow(var1, var2.add(-1, 0, 0), var3, var4);
         this.grow(var1, var2.add(0, 0, 1), var3, var4);
         this.grow(var1, var2.add(0, 0, -1), var3, var4);
      }

   }
}
