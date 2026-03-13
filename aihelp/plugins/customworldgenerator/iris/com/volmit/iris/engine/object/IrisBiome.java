package com.volmit.iris.engine.object;

import com.volmit.iris.Iris;
import com.volmit.iris.core.gui.components.RenderType;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.core.loader.IrisRegistrant;
import com.volmit.iris.engine.IrisComplex;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.DependsOn;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.RegistryListResource;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.collection.KSet;
import com.volmit.iris.util.data.B;
import com.volmit.iris.util.data.DataProvider;
import com.volmit.iris.util.data.VanillaBiomeMap;
import com.volmit.iris.util.inventorygui.RandomColor;
import com.volmit.iris.util.json.JSONObject;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.noise.CNG;
import com.volmit.iris.util.plugin.VolmitSender;
import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import lombok.Generated;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;

@Desc("Represents a biome in iris. Biomes are placed inside of regions and hold objects.\nA biome consists of layers (block palletes), decorations, objects & generators.")
public class IrisBiome extends IrisRegistrant implements IRare {
   private static final BlockData BARRIER;
   private final transient AtomicCache<KMap<String, IrisBiomeGeneratorLink>> genCache = new AtomicCache();
   private final transient AtomicCache<KMap<String, Integer>> genCacheMax = new AtomicCache();
   private final transient AtomicCache<KMap<String, Integer>> genCacheMin = new AtomicCache();
   private final transient AtomicCache<KList<IrisObjectPlacement>> surfaceObjectsCache = new AtomicCache();
   private final transient AtomicCache<KList<IrisObjectPlacement>> carveObjectsCache = new AtomicCache();
   private final transient AtomicCache<Color> cacheColor = new AtomicCache();
   private final transient AtomicCache<Color> cacheColorObjectDensity = new AtomicCache();
   private final transient AtomicCache<Color> cacheColorDecoratorLoad = new AtomicCache();
   private final transient AtomicCache<Color> cacheColorLayerLoad = new AtomicCache();
   private final transient AtomicCache<Color> cacheColorDepositLoad = new AtomicCache();
   private final transient AtomicCache<CNG> childrenCell = new AtomicCache();
   private final transient AtomicCache<CNG> biomeGenerator = new AtomicCache();
   private final transient AtomicCache<Integer> maxHeight = new AtomicCache();
   private final transient AtomicCache<Integer> maxWithObjectHeight = new AtomicCache();
   private final transient AtomicCache<IrisBiome> realCarveBiome = new AtomicCache();
   private final transient AtomicCache<KList<IrisBiome>> realChildren = new AtomicCache();
   private final transient AtomicCache<KList<CNG>> layerHeightGenerators = new AtomicCache();
   private final transient AtomicCache<KList<CNG>> layerSeaHeightGenerators = new AtomicCache();
   @MinNumber(2.0D)
   @Required
   @Desc("This is the human readable name for this biome. This can and should be different than the file name. This is not used for loading biomes in other objects.")
   private String name = "Subterranean Land";
   @ArrayType(
      min = 1,
      type = IrisBiomeCustom.class
   )
   @Desc("If the biome type custom is defined, specify this")
   private KList<IrisBiomeCustom> customDerivitives;
   @Desc("Spawn Entities in this area over time. Iris will continually replenish these mobs just like vanilla does.")
   @ArrayType(
      min = 1,
      type = String.class
   )
   @RegistryListResource(IrisSpawner.class)
   private KList<String> entitySpawners = new KList();
   @ArrayType(
      min = 1,
      type = IrisEffect.class
   )
   @Desc("Effects are ambient effects such as potion effects, random sounds, or even particles around each player. All of these effects are played via packets so two players won't see/hear each others effects.\nDue to performance reasons, effects will play around the player even if where the effect was played is no longer in the biome the player is in.")
   private KList<IrisEffect> effects = new KList();
   @DependsOn({"biomeStyle", "biomeZoom", "biomeScatter"})
   @Desc("This changes the dispersion of the biome colors if multiple derivatives are chosen.")
   private IrisGeneratorStyle biomeStyle;
   @ArrayType(
      min = 1,
      type = IrisBlockDrops.class
   )
   @Desc("Define custom block drops for this biome")
   private KList<IrisBlockDrops> blockDrops;
   @Desc("Reference loot tables in this area")
   private IrisLootReference loot;
   @Desc("Layers no longer descend from the surface block, they descend from the max possible height the biome can produce (constant) creating mesa like layers.")
   private boolean lockLayers;
   @Desc("The max layers to iterate below the surface for locked layer biomes (mesa).")
   private int lockLayersMax;
   @Desc("Carving configuration for the dimension")
   private IrisCarving carving;
   @Desc("Configuration of fluid bodies such as rivers & lakes")
   private IrisFluidBodies fluidBodies;
   @MinNumber(1.0D)
   @MaxNumber(512.0D)
   @Desc("The rarity of this biome (integer)")
   private int rarity;
   @Desc("A color for visualizing this biome with a color. I.e. #F13AF5. This will show up on the map.")
   private String color;
   @Required
   @Desc("The raw derivative of this biome. This is required or the terrain will not properly generate. Use any vanilla biome type. Look in examples/biome-list.txt")
   private Biome derivative;
   @Required
   @Desc("Override the derivative when vanilla places structures to this derivative. This is useful for example if you have an ocean biome, but you have set the derivative to desert to get a brown-ish color. To prevent desert structures from spawning on top of your ocean, you can set your vanillaDerivative to ocean, to allow for vanilla structures. Not defining this value will simply select the derivative.")
   private Biome vanillaDerivative;
   @ArrayType(
      min = 1,
      type = Biome.class
   )
   @Desc("You can instead specify multiple biome derivatives to randomly scatter colors in this biome")
   private KList<Biome> biomeScatter;
   @ArrayType(
      min = 1,
      type = Biome.class
   )
   @Desc("Since 1.13 supports 3D biomes, you can add different derivative colors for anything above the terrain. (Think swampy tree leaves with a desert looking grass surface)")
   private KList<Biome> biomeSkyScatter;
   @DependsOn({"children"})
   @Desc("If this biome has children biomes, and the gen layer chooses one of this biomes children, how much smaller will it be (inside of this biome). Higher values means a smaller biome relative to this biome's size. Set higher than 1.0 and below 3.0 for best results.")
   private double childShrinkFactor;
   @DependsOn({"children"})
   @Desc("If this biome has children biomes, and the gen layer chooses one of this biomes children, How will it be shaped?")
   private IrisGeneratorStyle childStyle;
   @RegistryListResource(IrisBiome.class)
   @ArrayType(
      min = 1,
      type = String.class
   )
   @Desc("List any biome names (file names without.json) here as children. Portions of this biome can sometimes morph into their children. Iris supports cyclic relationships such as A > B > A > B. Iris will stop checking 9 biomes down the tree.")
   private KList<String> children;
   @ArrayType(
      min = 1,
      type = IrisJigsawStructurePlacement.class
   )
   @Desc("Jigsaw structures")
   private KList<IrisJigsawStructurePlacement> jigsawStructures;
   @RegistryListResource(IrisBiome.class)
   @Desc("The carving biome. If specified the biome will be used when under a carving instead of this current biome.")
   private String carvingBiome;
   @Desc("The default slab if iris decides to place a slab in this biome. Default is no slab.")
   private IrisBiomePaletteLayer slab;
   @Desc("The default wall if iris decides to place a wall higher than 2 blocks (steep hills or possibly cliffs)")
   private IrisBiomePaletteLayer wall;
   @Required
   @ArrayType(
      type = IrisBiomePaletteLayer.class
   )
   @Desc("This defines the layers of materials in this biome. Each layer has a palette and min/max height and some other properties. Usually a grassy/sandy layer then a dirt layer then a stone layer. Iris will fill in the remaining blocks below your layers with stone.")
   private KList<IrisBiomePaletteLayer> layers;
   @Required
   @ArrayType(
      type = IrisBiomePaletteLayer.class
   )
   @Desc("This defines the layers of materials in this biome. Each layer has a palette and min/max height and some other properties. Usually a grassy/sandy layer then a dirt layer then a stone layer. Iris will fill in the remaining blocks below your layers with stone.")
   private KList<IrisBiomePaletteLayer> caveCeilingLayers;
   @ArrayType(
      type = IrisBiomePaletteLayer.class
   )
   @Desc("This defines the layers of materials in this biome. Each layer has a palette and min/max height and some other properties. Usually a grassy/sandy layer then a dirt layer then a stone layer. Iris will fill in the remaining blocks below your layers with stone.")
   private KList<IrisBiomePaletteLayer> seaLayers;
   @ArrayType(
      min = 1,
      type = IrisDecorator.class
   )
   @Desc("Decorators are used for things like tall grass, bisected flowers, and even kelp or cactus (random heights)")
   private KList<IrisDecorator> decorators;
   @ArrayType(
      min = 1,
      type = IrisObjectPlacement.class
   )
   @Desc("Objects define what schematics (iob files) iris will place in this biome")
   private KList<IrisObjectPlacement> objects;
   @Required
   @ArrayType(
      min = 1,
      type = IrisBiomeGeneratorLink.class
   )
   @Desc("Generators for this biome. Multiple generators with different interpolation sizes will mix with other biomes how you would expect. This defines your biome height relative to the fluid height. Use negative for oceans.")
   private KList<IrisBiomeGeneratorLink> generators;
   @ArrayType(
      min = 1,
      type = IrisDepositGenerator.class
   )
   @Desc("Define biome deposit generators that add onto the existing regional and global deposit generators")
   private KList<IrisDepositGenerator> deposits;
   private transient InferredType inferredType;
   @Desc("Collection of ores to be generated")
   @ArrayType(
      type = IrisOreGenerator.class,
      min = 1
   )
   private KList<IrisOreGenerator> ores;

   public BlockData generateOres(int x, int y, int z, RNG rng, IrisData data, boolean surface) {
      if (this.ores.isEmpty()) {
         return null;
      } else {
         BlockData var7 = null;
         Iterator var8 = this.ores.iterator();

         while(var8.hasNext()) {
            IrisOreGenerator var9 = (IrisOreGenerator)var8.next();
            if (var9.isGenerateSurface() == var6) {
               var7 = var9.generate(var1, var2, var3, var4, var5);
               if (var7 != null) {
                  return var7;
               }
            }
         }

         return null;
      }
   }

   public Biome getVanillaDerivative() {
      return this.vanillaDerivative == null ? this.derivative : this.vanillaDerivative;
   }

   public boolean isCustom() {
      return this.getCustomDerivitives() != null && this.getCustomDerivitives().isNotEmpty();
   }

   public double getGenLinkMax(String loadKey, Engine engine) {
      Integer var3 = (Integer)((KMap)this.genCacheMax.aquire(() -> {
         KMap var1 = new KMap();
         Iterator var2 = this.getGenerators().iterator();

         while(var2.hasNext()) {
            IrisBiomeGeneratorLink var3 = (IrisBiomeGeneratorLink)var2.next();
            var1.put(var3.getGenerator(), var3.getMax());
         }

         return var1;
      })).get(var1);
      return var3 == null ? 0.0D : (double)var3;
   }

   public double getGenLinkMin(String loadKey, Engine engine) {
      Integer var3 = (Integer)((KMap)this.genCacheMin.aquire(() -> {
         KMap var1 = new KMap();
         Iterator var2 = this.getGenerators().iterator();

         while(var2.hasNext()) {
            IrisBiomeGeneratorLink var3 = (IrisBiomeGeneratorLink)var2.next();
            var1.put(var3.getGenerator(), var3.getMin());
         }

         return var1;
      })).get(var1);
      return var3 == null ? 0.0D : (double)var3;
   }

   public IrisBiomeGeneratorLink getGenLink(String loadKey) {
      return (IrisBiomeGeneratorLink)((KMap)this.genCache.aquire(() -> {
         KMap var1 = new KMap();
         Iterator var2 = this.getGenerators().iterator();

         while(var2.hasNext()) {
            IrisBiomeGeneratorLink var3 = (IrisBiomeGeneratorLink)var2.next();
            var1.put(var3.getGenerator(), var3);
         }

         return var1;
      })).get(var1);
   }

   public IrisBiome getRealCarvingBiome(IrisData data) {
      return (IrisBiome)this.realCarveBiome.aquire(() -> {
         IrisBiome var2 = (IrisBiome)var1.getBiomeLoader().load(this.getCarvingBiome());
         if (var2 == null) {
            var2 = this;
         }

         return var2;
      });
   }

   public KList<IrisObjectPlacement> getSurfaceObjects() {
      return (KList)this.getSurfaceObjectsCache().aquire(() -> {
         KList var1 = this.getObjects().copy();
         Iterator var2 = var1.copy().iterator();

         while(var2.hasNext()) {
            IrisObjectPlacement var3 = (IrisObjectPlacement)var2.next();
            if (!var3.getCarvingSupport().supportsSurface()) {
               var1.remove(var3);
            }
         }

         return var1;
      });
   }

   public KList<IrisObjectPlacement> getCarvingObjects() {
      return (KList)this.getCarveObjectsCache().aquire(() -> {
         KList var1 = this.getObjects().copy();
         Iterator var2 = var1.copy().iterator();

         while(var2.hasNext()) {
            IrisObjectPlacement var3 = (IrisObjectPlacement)var2.next();
            if (!var3.getCarvingSupport().supportsCarving()) {
               var1.remove(var3);
            }
         }

         return var1;
      });
   }

   public double getHeight(Engine xg, double x, double z, long seed) {
      double var8 = 0.0D;

      IrisBiomeGeneratorLink var11;
      for(Iterator var10 = this.generators.iterator(); var10.hasNext(); var8 += var11.getHeight(var1, var2, var4, var6)) {
         var11 = (IrisBiomeGeneratorLink)var10.next();
      }

      return Math.max(0.0D, Math.min(var8, (double)var1.getHeight()));
   }

   public CNG getBiomeGenerator(RNG random) {
      return (CNG)this.biomeGenerator.aquire(() -> {
         return this.biomeStyle.create(var1.nextParallelRNG(442837 + this.getRarity() + this.getName().length()), this.getLoader());
      });
   }

   public CNG getChildrenGenerator(RNG random, int sig, double scale) {
      return (CNG)this.childrenCell.aquire(() -> {
         return this.getChildStyle().create(var1.nextParallelRNG(var2 * 2137), this.getLoader()).bake().scale(var3).bake();
      });
   }

   public KList<BlockData> generateLayers(IrisDimension dim, double wx, double wz, RNG random, int maxDepth, int height, IrisData rdata, IrisComplex complex) {
      if (this.isLockLayers()) {
         return this.generateLockedLayers(var2, var4, var6, var7, var8, var9, var10);
      } else {
         KList var11 = new KList();
         if (var7 <= 0) {
            return var11;
         } else {
            for(int var12 = 0; var12 < this.layers.size(); ++var12) {
               CNG var13 = (CNG)this.getLayerHeightGenerators(var6, var9).get(var12);
               double var14 = (double)var13.fit(((IrisBiomePaletteLayer)this.layers.get(var12)).getMinHeight(), ((IrisBiomePaletteLayer)this.layers.get(var12)).getMaxHeight(), var2 / ((IrisBiomePaletteLayer)this.layers.get(var12)).getZoom(), var4 / ((IrisBiomePaletteLayer)this.layers.get(var12)).getZoom());
               IrisSlopeClip var16 = ((IrisBiomePaletteLayer)this.getLayers().get(var12)).getSlopeCondition();
               if (!var16.isDefault() && !var16.isValid((Double)var10.getSlopeStream().get(var2, var4))) {
                  var14 = 0.0D;
               }

               if (!(var14 <= 0.0D)) {
                  int var17;
                  for(var17 = 0; (double)var17 < var14 && var11.size() < var7; ++var17) {
                     try {
                        var11.add((Object)((IrisBiomePaletteLayer)this.getLayers().get(var12)).get(var6.nextParallelRNG(var12 + var17), (var2 + (double)var17) / ((IrisBiomePaletteLayer)this.layers.get(var12)).getZoom(), (double)var17, (var4 - (double)var17) / ((IrisBiomePaletteLayer)this.layers.get(var12)).getZoom(), var9));
                     } catch (Throwable var19) {
                        Iris.reportError(var19);
                        var19.printStackTrace();
                     }
                  }

                  if (var11.size() >= var7) {
                     break;
                  }

                  if (var1.isExplodeBiomePalettes()) {
                     for(var17 = 0; var17 < var1.getExplodeBiomePaletteSize(); ++var17) {
                        var11.add((Object)BARRIER);
                        if (var11.size() >= var7) {
                           break;
                        }
                     }
                  }
               }
            }

            return var11;
         }
      }
   }

   public KList<BlockData> generateCeilingLayers(IrisDimension dim, double wx, double wz, RNG random, int maxDepth, int height, IrisData rdata, IrisComplex complex) {
      KList var11 = new KList();
      if (var7 <= 0) {
         return var11;
      } else {
         for(int var12 = 0; var12 < this.caveCeilingLayers.size(); ++var12) {
            CNG var13 = (CNG)this.getLayerHeightGenerators(var6, var9).get(var12);
            double var14 = (double)var13.fit(((IrisBiomePaletteLayer)this.caveCeilingLayers.get(var12)).getMinHeight(), ((IrisBiomePaletteLayer)this.caveCeilingLayers.get(var12)).getMaxHeight(), var2 / ((IrisBiomePaletteLayer)this.caveCeilingLayers.get(var12)).getZoom(), var4 / ((IrisBiomePaletteLayer)this.caveCeilingLayers.get(var12)).getZoom());
            if (!(var14 <= 0.0D)) {
               int var16;
               for(var16 = 0; (double)var16 < var14 && var11.size() < var7; ++var16) {
                  try {
                     var11.add((Object)((IrisBiomePaletteLayer)this.getCaveCeilingLayers().get(var12)).get(var6.nextParallelRNG(var12 + var16), (var2 + (double)var16) / ((IrisBiomePaletteLayer)this.caveCeilingLayers.get(var12)).getZoom(), (double)var16, (var4 - (double)var16) / ((IrisBiomePaletteLayer)this.caveCeilingLayers.get(var12)).getZoom(), var9));
                  } catch (Throwable var18) {
                     Iris.reportError(var18);
                     var18.printStackTrace();
                  }
               }

               if (var11.size() >= var7) {
                  break;
               }

               if (var1.isExplodeBiomePalettes()) {
                  for(var16 = 0; var16 < var1.getExplodeBiomePaletteSize(); ++var16) {
                     var11.add((Object)BARRIER);
                     if (var11.size() >= var7) {
                        break;
                     }
                  }
               }
            }
         }

         return var11;
      }
   }

   public KList<BlockData> generateLockedLayers(double wx, double wz, RNG random, int maxDepthf, int height, IrisData rdata, IrisComplex complex) {
      KList var10 = new KList();
      KList var11 = new KList();
      int var12 = Math.min(var6, this.getLockLayersMax());
      if (var12 <= 0) {
         return var10;
      } else {
         int var13;
         for(var13 = 0; var13 < this.layers.size(); ++var13) {
            CNG var14 = (CNG)this.getLayerHeightGenerators(var5, var8).get(var13);
            double var15 = (double)var14.fit(((IrisBiomePaletteLayer)this.layers.get(var13)).getMinHeight(), ((IrisBiomePaletteLayer)this.layers.get(var13)).getMaxHeight(), var1 / ((IrisBiomePaletteLayer)this.layers.get(var13)).getZoom(), var3 / ((IrisBiomePaletteLayer)this.layers.get(var13)).getZoom());
            IrisSlopeClip var17 = ((IrisBiomePaletteLayer)this.getLayers().get(var13)).getSlopeCondition();
            if (!var17.isDefault() && !var17.isValid((Double)var9.getSlopeStream().get(var1, var3))) {
               var15 = 0.0D;
            }

            if (!(var15 <= 0.0D)) {
               for(int var18 = 0; (double)var18 < var15; ++var18) {
                  try {
                     var10.add((Object)((IrisBiomePaletteLayer)this.getLayers().get(var13)).get(var5.nextParallelRNG(var13 + var18), (var1 + (double)var18) / ((IrisBiomePaletteLayer)this.layers.get(var13)).getZoom(), (double)var18, (var3 - (double)var18) / ((IrisBiomePaletteLayer)this.layers.get(var13)).getZoom(), var8));
                  } catch (Throwable var20) {
                     Iris.reportError(var20);
                     var20.printStackTrace();
                  }
               }
            }
         }

         if (var10.isEmpty()) {
            return var11;
         } else {
            for(var13 = 0; var13 < var12; ++var13) {
               int var21 = 512 - var7 - var13;
               int var22 = var21 % var10.size();
               var11.add((Object)((BlockData)var10.get(Math.max(var22, 0))));
            }

            return var11;
         }
      }
   }

   public int getMaxHeight(Engine engine) {
      return (Integer)this.maxHeight.aquire(() -> {
         int var1 = 0;

         IrisBiomeGeneratorLink var3;
         for(Iterator var2 = this.getGenerators().iterator(); var2.hasNext(); var1 += var3.getMax()) {
            var3 = (IrisBiomeGeneratorLink)var2.next();
         }

         return var1;
      });
   }

   public int getMaxWithObjectHeight(IrisData data, Engine engine) {
      return (Integer)this.maxWithObjectHeight.aquire(() -> {
         int var2 = 0;

         IrisBiomeGeneratorLink var4;
         for(Iterator var3 = this.getGenerators().iterator(); var3.hasNext(); var2 += var4.getMax()) {
            var4 = (IrisBiomeGeneratorLink)var3.next();
         }

         int var8 = 0;
         Iterator var9 = this.getObjects().iterator();

         while(var9.hasNext()) {
            IrisObjectPlacement var5 = (IrisObjectPlacement)var9.next();

            IrisObject var7;
            for(Iterator var6 = var1.getObjectLoader().loadAll(var5.getPlace()).iterator(); var6.hasNext(); var8 = Math.max(var8, var7.getH())) {
               var7 = (IrisObject)var6.next();
            }
         }

         return var2 + var8 + 3;
      });
   }

   public KList<BlockData> generateSeaLayers(double wx, double wz, RNG random, int maxDepth, IrisData rdata) {
      KList var8 = new KList();

      for(int var9 = 0; var9 < this.seaLayers.size(); ++var9) {
         CNG var10 = (CNG)this.getLayerSeaHeightGenerators(var5, var7).get(var9);
         int var11 = var10.fit(((IrisBiomePaletteLayer)this.seaLayers.get(var9)).getMinHeight(), ((IrisBiomePaletteLayer)this.seaLayers.get(var9)).getMaxHeight(), var1 / ((IrisBiomePaletteLayer)this.seaLayers.get(var9)).getZoom(), var3 / ((IrisBiomePaletteLayer)this.seaLayers.get(var9)).getZoom());
         if (var11 >= 0) {
            for(int var12 = 0; var12 < var11 && var8.size() < var6; ++var12) {
               try {
                  var8.add((Object)((IrisBiomePaletteLayer)this.getSeaLayers().get(var9)).get(var5.nextParallelRNG(var9 + var12), (var1 + (double)var12) / ((IrisBiomePaletteLayer)this.seaLayers.get(var9)).getZoom(), (double)var12, (var3 - (double)var12) / ((IrisBiomePaletteLayer)this.seaLayers.get(var9)).getZoom(), var7));
               } catch (Throwable var14) {
                  Iris.reportError(var14);
                  var14.printStackTrace();
               }
            }

            if (var8.size() >= var6) {
               break;
            }
         }
      }

      return var8;
   }

   public KList<CNG> getLayerHeightGenerators(RNG rng, IrisData rdata) {
      return (KList)this.layerHeightGenerators.aquire(() -> {
         KList var3 = new KList();
         int var4 = 7235;
         Iterator var5 = this.getLayers().iterator();

         while(var5.hasNext()) {
            IrisBiomePaletteLayer var6 = (IrisBiomePaletteLayer)var5.next();
            var3.add((Object)var6.getHeightGenerator(var1.nextParallelRNG(var4++ * var4 * var4 * var4), var2));
         }

         return var3;
      });
   }

   public KList<CNG> getLayerSeaHeightGenerators(RNG rng, IrisData data) {
      return (KList)this.layerSeaHeightGenerators.aquire(() -> {
         KList var3 = new KList();
         int var4 = 7735;
         Iterator var5 = this.getSeaLayers().iterator();

         while(var5.hasNext()) {
            IrisBiomePaletteLayer var6 = (IrisBiomePaletteLayer)var5.next();
            var3.add((Object)var6.getHeightGenerator(var1.nextParallelRNG(var4++ * var4 * var4 * var4), var2));
         }

         return var3;
      });
   }

   public boolean isLand() {
      return this.inferredType == null ? true : this.inferredType.equals(InferredType.LAND);
   }

   public boolean isSea() {
      return this.inferredType == null ? false : this.inferredType.equals(InferredType.SEA);
   }

   public boolean isAquatic() {
      return this.isSea();
   }

   public boolean isShore() {
      return this.inferredType == null ? false : this.inferredType.equals(InferredType.SHORE);
   }

   public Biome getSkyBiome(RNG rng, double x, double y, double z) {
      if (this.biomeSkyScatter.size() == 1) {
         return (Biome)this.biomeSkyScatter.get(0);
      } else {
         return this.biomeSkyScatter.isEmpty() ? this.getGroundBiome(var1, var2, var4, var6) : (Biome)this.biomeSkyScatter.get(this.getBiomeGenerator(var1).fit(0, this.biomeSkyScatter.size() - 1, var2, var4, var6));
      }
   }

   public IrisBiomeCustom getCustomBiome(RNG rng, double x, double y, double z) {
      return this.customDerivitives.size() == 1 ? (IrisBiomeCustom)this.customDerivitives.get(0) : (IrisBiomeCustom)this.customDerivitives.get(this.getBiomeGenerator(var1).fit(0, this.customDerivitives.size() - 1, var2, var4, var6));
   }

   public KList<IrisBiome> getRealChildren(DataProvider g) {
      return (KList)this.realChildren.aquire(() -> {
         KList var2 = new KList();
         Iterator var3 = this.getChildren().iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            var2.add((Object)((IrisBiome)var1.getData().getBiomeLoader().load(var4)));
         }

         return var2;
      });
   }

   public KList<String> getAllChildren(DataProvider g, int limit) {
      KSet var3 = new KSet(new String[0]);
      var3.addAll(this.getChildren());
      --var2;
      if (var2 > 0) {
         Iterator var4 = this.getChildren().iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            IrisBiome var6 = (IrisBiome)var1.getData().getBiomeLoader().load(var5);
            var3.addAll(var6.getAllChildren(var1, var2));
         }
      }

      return new KList(var3);
   }

   public Biome getGroundBiome(RNG rng, double x, double y, double z) {
      if (this.biomeScatter.isEmpty()) {
         return this.getDerivative();
      } else {
         return this.biomeScatter.size() == 1 ? (Biome)this.biomeScatter.get(0) : (Biome)this.getBiomeGenerator(var1).fit((List)this.biomeScatter, var2, var4, var6);
      }
   }

   public BlockData getSurfaceBlock(int x, int z, RNG rng, IrisData idm) {
      return this.getLayers().isEmpty() ? B.get("AIR") : ((IrisBiomePaletteLayer)this.getLayers().get(0)).get(var3, (double)var1, 0.0D, (double)var2, var4);
   }

   public Color getColor(Engine engine, RenderType type) {
      switch(var2) {
      case BIOME:
      case HEIGHT:
      case CAVE_LAND:
      case REGION:
      case BIOME_SEA:
      case BIOME_LAND:
         return (Color)this.cacheColor.aquire(() -> {
            if (this.color == null) {
               RandomColor var1 = new RandomColor((long)this.getName().hashCode());
               if (this.getVanillaDerivative() == null) {
                  Iris.warn("No vanilla biome found for " + this.getName());
                  return new Color(var1.randomColor());
               } else {
                  RandomColor.Color var2 = VanillaBiomeMap.getColorType(this.getVanillaDerivative());
                  RandomColor.Luminosity var3 = VanillaBiomeMap.getColorLuminosity(this.getVanillaDerivative());
                  RandomColor.SaturationType var4 = VanillaBiomeMap.getColorSaturatiom(this.getVanillaDerivative());
                  int var5 = var1.randomColor(var2, var2 == RandomColor.Color.MONOCHROME ? RandomColor.SaturationType.MONOCHROME : var4, var3);
                  return new Color(var5);
               }
            } else {
               try {
                  return Color.decode(this.color);
               } catch (NumberFormatException var6) {
                  Iris.warn("Could not parse color \"" + this.color + "\" for biome " + this.getName());
                  return new Color((new RandomColor((long)this.getName().hashCode())).randomColor());
               }
            }
         });
      case OBJECT_LOAD:
         return (Color)this.cacheColorObjectDensity.aquire(() -> {
            double var2 = 0.0D;

            IrisObjectPlacement var5;
            for(Iterator var4 = this.getObjects().iterator(); var4.hasNext(); var2 += (double)var5.getDensity() * var5.getChance()) {
               var5 = (IrisObjectPlacement)var4.next();
            }

            return Color.getHSBColor(0.225F, (float)(var2 / var1.getMaxBiomeObjectDensity()), 1.0F);
         });
      case DECORATOR_LOAD:
         return (Color)this.cacheColorDecoratorLoad.aquire(() -> {
            double var2 = 0.0D;

            IrisDecorator var5;
            for(Iterator var4 = this.getDecorators().iterator(); var4.hasNext(); var2 += var5.getChance() * (double)Math.min(1, var5.getStackMax()) * 256.0D) {
               var5 = (IrisDecorator)var4.next();
            }

            return Color.getHSBColor(0.41F, (float)(var2 / var1.getMaxBiomeDecoratorDensity()), 1.0F);
         });
      case LAYER_LOAD:
         return (Color)this.cacheColorLayerLoad.aquire(() -> {
            return Color.getHSBColor(0.625F, (float)((double)this.getLayers().size() / var1.getMaxBiomeLayerDensity()), 1.0F);
         });
      default:
         return Color.black;
      }
   }

   public String getFolderName() {
      return "biomes";
   }

   public String getTypeName() {
      return "Biome";
   }

   public void scanForErrors(JSONObject p, VolmitSender sender) {
   }

   @Generated
   public IrisBiome() {
      this.biomeStyle = NoiseStyle.SIMPLEX.style();
      this.blockDrops = new KList();
      this.loot = new IrisLootReference();
      this.lockLayers = false;
      this.lockLayersMax = 7;
      this.carving = new IrisCarving();
      this.fluidBodies = new IrisFluidBodies();
      this.rarity = 1;
      this.color = null;
      this.derivative = Biome.THE_VOID;
      this.vanillaDerivative = null;
      this.biomeScatter = new KList();
      this.biomeSkyScatter = new KList();
      this.childShrinkFactor = 1.5D;
      this.childStyle = NoiseStyle.CELLULAR_IRIS_DOUBLE.style();
      this.children = new KList();
      this.jigsawStructures = new KList();
      this.carvingBiome = "";
      this.slab = (new IrisBiomePaletteLayer()).zero();
      this.wall = (new IrisBiomePaletteLayer()).zero();
      this.layers = (new KList()).qadd(new IrisBiomePaletteLayer());
      this.caveCeilingLayers = (new KList()).qadd(new IrisBiomePaletteLayer());
      this.seaLayers = new KList();
      this.decorators = new KList();
      this.objects = new KList();
      this.generators = (new KList()).qadd(new IrisBiomeGeneratorLink());
      this.deposits = new KList();
      this.ores = new KList();
   }

   @Generated
   public IrisBiome(final String name, final KList<IrisBiomeCustom> customDerivitives, final KList<String> entitySpawners, final KList<IrisEffect> effects, final IrisGeneratorStyle biomeStyle, final KList<IrisBlockDrops> blockDrops, final IrisLootReference loot, final boolean lockLayers, final int lockLayersMax, final IrisCarving carving, final IrisFluidBodies fluidBodies, final int rarity, final String color, final Biome derivative, final Biome vanillaDerivative, final KList<Biome> biomeScatter, final KList<Biome> biomeSkyScatter, final double childShrinkFactor, final IrisGeneratorStyle childStyle, final KList<String> children, final KList<IrisJigsawStructurePlacement> jigsawStructures, final String carvingBiome, final IrisBiomePaletteLayer slab, final IrisBiomePaletteLayer wall, final KList<IrisBiomePaletteLayer> layers, final KList<IrisBiomePaletteLayer> caveCeilingLayers, final KList<IrisBiomePaletteLayer> seaLayers, final KList<IrisDecorator> decorators, final KList<IrisObjectPlacement> objects, final KList<IrisBiomeGeneratorLink> generators, final KList<IrisDepositGenerator> deposits, final InferredType inferredType, final KList<IrisOreGenerator> ores) {
      this.biomeStyle = NoiseStyle.SIMPLEX.style();
      this.blockDrops = new KList();
      this.loot = new IrisLootReference();
      this.lockLayers = false;
      this.lockLayersMax = 7;
      this.carving = new IrisCarving();
      this.fluidBodies = new IrisFluidBodies();
      this.rarity = 1;
      this.color = null;
      this.derivative = Biome.THE_VOID;
      this.vanillaDerivative = null;
      this.biomeScatter = new KList();
      this.biomeSkyScatter = new KList();
      this.childShrinkFactor = 1.5D;
      this.childStyle = NoiseStyle.CELLULAR_IRIS_DOUBLE.style();
      this.children = new KList();
      this.jigsawStructures = new KList();
      this.carvingBiome = "";
      this.slab = (new IrisBiomePaletteLayer()).zero();
      this.wall = (new IrisBiomePaletteLayer()).zero();
      this.layers = (new KList()).qadd(new IrisBiomePaletteLayer());
      this.caveCeilingLayers = (new KList()).qadd(new IrisBiomePaletteLayer());
      this.seaLayers = new KList();
      this.decorators = new KList();
      this.objects = new KList();
      this.generators = (new KList()).qadd(new IrisBiomeGeneratorLink());
      this.deposits = new KList();
      this.ores = new KList();
      this.name = var1;
      this.customDerivitives = var2;
      this.entitySpawners = var3;
      this.effects = var4;
      this.biomeStyle = var5;
      this.blockDrops = var6;
      this.loot = var7;
      this.lockLayers = var8;
      this.lockLayersMax = var9;
      this.carving = var10;
      this.fluidBodies = var11;
      this.rarity = var12;
      this.color = var13;
      this.derivative = var14;
      this.vanillaDerivative = var15;
      this.biomeScatter = var16;
      this.biomeSkyScatter = var17;
      this.childShrinkFactor = var18;
      this.childStyle = var20;
      this.children = var21;
      this.jigsawStructures = var22;
      this.carvingBiome = var23;
      this.slab = var24;
      this.wall = var25;
      this.layers = var26;
      this.caveCeilingLayers = var27;
      this.seaLayers = var28;
      this.decorators = var29;
      this.objects = var30;
      this.generators = var31;
      this.deposits = var32;
      this.inferredType = var33;
      this.ores = var34;
   }

   @Generated
   public AtomicCache<KMap<String, IrisBiomeGeneratorLink>> getGenCache() {
      return this.genCache;
   }

   @Generated
   public AtomicCache<KMap<String, Integer>> getGenCacheMax() {
      return this.genCacheMax;
   }

   @Generated
   public AtomicCache<KMap<String, Integer>> getGenCacheMin() {
      return this.genCacheMin;
   }

   @Generated
   public AtomicCache<KList<IrisObjectPlacement>> getSurfaceObjectsCache() {
      return this.surfaceObjectsCache;
   }

   @Generated
   public AtomicCache<KList<IrisObjectPlacement>> getCarveObjectsCache() {
      return this.carveObjectsCache;
   }

   @Generated
   public AtomicCache<Color> getCacheColor() {
      return this.cacheColor;
   }

   @Generated
   public AtomicCache<Color> getCacheColorObjectDensity() {
      return this.cacheColorObjectDensity;
   }

   @Generated
   public AtomicCache<Color> getCacheColorDecoratorLoad() {
      return this.cacheColorDecoratorLoad;
   }

   @Generated
   public AtomicCache<Color> getCacheColorLayerLoad() {
      return this.cacheColorLayerLoad;
   }

   @Generated
   public AtomicCache<Color> getCacheColorDepositLoad() {
      return this.cacheColorDepositLoad;
   }

   @Generated
   public AtomicCache<CNG> getChildrenCell() {
      return this.childrenCell;
   }

   @Generated
   public AtomicCache<CNG> getBiomeGenerator() {
      return this.biomeGenerator;
   }

   @Generated
   public AtomicCache<Integer> getMaxHeight() {
      return this.maxHeight;
   }

   @Generated
   public AtomicCache<Integer> getMaxWithObjectHeight() {
      return this.maxWithObjectHeight;
   }

   @Generated
   public AtomicCache<IrisBiome> getRealCarveBiome() {
      return this.realCarveBiome;
   }

   @Generated
   public AtomicCache<KList<IrisBiome>> getRealChildren() {
      return this.realChildren;
   }

   @Generated
   public AtomicCache<KList<CNG>> getLayerHeightGenerators() {
      return this.layerHeightGenerators;
   }

   @Generated
   public AtomicCache<KList<CNG>> getLayerSeaHeightGenerators() {
      return this.layerSeaHeightGenerators;
   }

   @Generated
   public String getName() {
      return this.name;
   }

   @Generated
   public KList<IrisBiomeCustom> getCustomDerivitives() {
      return this.customDerivitives;
   }

   @Generated
   public KList<String> getEntitySpawners() {
      return this.entitySpawners;
   }

   @Generated
   public KList<IrisEffect> getEffects() {
      return this.effects;
   }

   @Generated
   public IrisGeneratorStyle getBiomeStyle() {
      return this.biomeStyle;
   }

   @Generated
   public KList<IrisBlockDrops> getBlockDrops() {
      return this.blockDrops;
   }

   @Generated
   public IrisLootReference getLoot() {
      return this.loot;
   }

   @Generated
   public boolean isLockLayers() {
      return this.lockLayers;
   }

   @Generated
   public int getLockLayersMax() {
      return this.lockLayersMax;
   }

   @Generated
   public IrisCarving getCarving() {
      return this.carving;
   }

   @Generated
   public IrisFluidBodies getFluidBodies() {
      return this.fluidBodies;
   }

   @Generated
   public int getRarity() {
      return this.rarity;
   }

   @Generated
   public String getColor() {
      return this.color;
   }

   @Generated
   public Biome getDerivative() {
      return this.derivative;
   }

   @Generated
   public KList<Biome> getBiomeScatter() {
      return this.biomeScatter;
   }

   @Generated
   public KList<Biome> getBiomeSkyScatter() {
      return this.biomeSkyScatter;
   }

   @Generated
   public double getChildShrinkFactor() {
      return this.childShrinkFactor;
   }

   @Generated
   public IrisGeneratorStyle getChildStyle() {
      return this.childStyle;
   }

   @Generated
   public KList<String> getChildren() {
      return this.children;
   }

   @Generated
   public KList<IrisJigsawStructurePlacement> getJigsawStructures() {
      return this.jigsawStructures;
   }

   @Generated
   public String getCarvingBiome() {
      return this.carvingBiome;
   }

   @Generated
   public IrisBiomePaletteLayer getSlab() {
      return this.slab;
   }

   @Generated
   public IrisBiomePaletteLayer getWall() {
      return this.wall;
   }

   @Generated
   public KList<IrisBiomePaletteLayer> getLayers() {
      return this.layers;
   }

   @Generated
   public KList<IrisBiomePaletteLayer> getCaveCeilingLayers() {
      return this.caveCeilingLayers;
   }

   @Generated
   public KList<IrisBiomePaletteLayer> getSeaLayers() {
      return this.seaLayers;
   }

   @Generated
   public KList<IrisDecorator> getDecorators() {
      return this.decorators;
   }

   @Generated
   public KList<IrisObjectPlacement> getObjects() {
      return this.objects;
   }

   @Generated
   public KList<IrisBiomeGeneratorLink> getGenerators() {
      return this.generators;
   }

   @Generated
   public KList<IrisDepositGenerator> getDeposits() {
      return this.deposits;
   }

   @Generated
   public InferredType getInferredType() {
      return this.inferredType;
   }

   @Generated
   public KList<IrisOreGenerator> getOres() {
      return this.ores;
   }

   @Generated
   public IrisBiome setName(final String name) {
      this.name = var1;
      return this;
   }

   @Generated
   public IrisBiome setCustomDerivitives(final KList<IrisBiomeCustom> customDerivitives) {
      this.customDerivitives = var1;
      return this;
   }

   @Generated
   public IrisBiome setEntitySpawners(final KList<String> entitySpawners) {
      this.entitySpawners = var1;
      return this;
   }

   @Generated
   public IrisBiome setEffects(final KList<IrisEffect> effects) {
      this.effects = var1;
      return this;
   }

   @Generated
   public IrisBiome setBiomeStyle(final IrisGeneratorStyle biomeStyle) {
      this.biomeStyle = var1;
      return this;
   }

   @Generated
   public IrisBiome setBlockDrops(final KList<IrisBlockDrops> blockDrops) {
      this.blockDrops = var1;
      return this;
   }

   @Generated
   public IrisBiome setLoot(final IrisLootReference loot) {
      this.loot = var1;
      return this;
   }

   @Generated
   public IrisBiome setLockLayers(final boolean lockLayers) {
      this.lockLayers = var1;
      return this;
   }

   @Generated
   public IrisBiome setLockLayersMax(final int lockLayersMax) {
      this.lockLayersMax = var1;
      return this;
   }

   @Generated
   public IrisBiome setCarving(final IrisCarving carving) {
      this.carving = var1;
      return this;
   }

   @Generated
   public IrisBiome setFluidBodies(final IrisFluidBodies fluidBodies) {
      this.fluidBodies = var1;
      return this;
   }

   @Generated
   public IrisBiome setRarity(final int rarity) {
      this.rarity = var1;
      return this;
   }

   @Generated
   public IrisBiome setColor(final String color) {
      this.color = var1;
      return this;
   }

   @Generated
   public IrisBiome setDerivative(final Biome derivative) {
      this.derivative = var1;
      return this;
   }

   @Generated
   public IrisBiome setVanillaDerivative(final Biome vanillaDerivative) {
      this.vanillaDerivative = var1;
      return this;
   }

   @Generated
   public IrisBiome setBiomeScatter(final KList<Biome> biomeScatter) {
      this.biomeScatter = var1;
      return this;
   }

   @Generated
   public IrisBiome setBiomeSkyScatter(final KList<Biome> biomeSkyScatter) {
      this.biomeSkyScatter = var1;
      return this;
   }

   @Generated
   public IrisBiome setChildShrinkFactor(final double childShrinkFactor) {
      this.childShrinkFactor = var1;
      return this;
   }

   @Generated
   public IrisBiome setChildStyle(final IrisGeneratorStyle childStyle) {
      this.childStyle = var1;
      return this;
   }

   @Generated
   public IrisBiome setChildren(final KList<String> children) {
      this.children = var1;
      return this;
   }

   @Generated
   public IrisBiome setJigsawStructures(final KList<IrisJigsawStructurePlacement> jigsawStructures) {
      this.jigsawStructures = var1;
      return this;
   }

   @Generated
   public IrisBiome setCarvingBiome(final String carvingBiome) {
      this.carvingBiome = var1;
      return this;
   }

   @Generated
   public IrisBiome setSlab(final IrisBiomePaletteLayer slab) {
      this.slab = var1;
      return this;
   }

   @Generated
   public IrisBiome setWall(final IrisBiomePaletteLayer wall) {
      this.wall = var1;
      return this;
   }

   @Generated
   public IrisBiome setLayers(final KList<IrisBiomePaletteLayer> layers) {
      this.layers = var1;
      return this;
   }

   @Generated
   public IrisBiome setCaveCeilingLayers(final KList<IrisBiomePaletteLayer> caveCeilingLayers) {
      this.caveCeilingLayers = var1;
      return this;
   }

   @Generated
   public IrisBiome setSeaLayers(final KList<IrisBiomePaletteLayer> seaLayers) {
      this.seaLayers = var1;
      return this;
   }

   @Generated
   public IrisBiome setDecorators(final KList<IrisDecorator> decorators) {
      this.decorators = var1;
      return this;
   }

   @Generated
   public IrisBiome setObjects(final KList<IrisObjectPlacement> objects) {
      this.objects = var1;
      return this;
   }

   @Generated
   public IrisBiome setGenerators(final KList<IrisBiomeGeneratorLink> generators) {
      this.generators = var1;
      return this;
   }

   @Generated
   public IrisBiome setDeposits(final KList<IrisDepositGenerator> deposits) {
      this.deposits = var1;
      return this;
   }

   @Generated
   public IrisBiome setInferredType(final InferredType inferredType) {
      this.inferredType = var1;
      return this;
   }

   @Generated
   public IrisBiome setOres(final KList<IrisOreGenerator> ores) {
      this.ores = var1;
      return this;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getGenCache());
      return "IrisBiome(genCache=" + var10000 + ", genCacheMax=" + String.valueOf(this.getGenCacheMax()) + ", genCacheMin=" + String.valueOf(this.getGenCacheMin()) + ", surfaceObjectsCache=" + String.valueOf(this.getSurfaceObjectsCache()) + ", carveObjectsCache=" + String.valueOf(this.getCarveObjectsCache()) + ", cacheColor=" + String.valueOf(this.getCacheColor()) + ", cacheColorObjectDensity=" + String.valueOf(this.getCacheColorObjectDensity()) + ", cacheColorDecoratorLoad=" + String.valueOf(this.getCacheColorDecoratorLoad()) + ", cacheColorLayerLoad=" + String.valueOf(this.getCacheColorLayerLoad()) + ", cacheColorDepositLoad=" + String.valueOf(this.getCacheColorDepositLoad()) + ", childrenCell=" + String.valueOf(this.getChildrenCell()) + ", biomeGenerator=" + String.valueOf(this.getBiomeGenerator()) + ", maxHeight=" + String.valueOf(this.getMaxHeight()) + ", maxWithObjectHeight=" + String.valueOf(this.getMaxWithObjectHeight()) + ", realCarveBiome=" + String.valueOf(this.getRealCarveBiome()) + ", realChildren=" + String.valueOf(this.getRealChildren()) + ", layerHeightGenerators=" + String.valueOf(this.getLayerHeightGenerators()) + ", layerSeaHeightGenerators=" + String.valueOf(this.getLayerSeaHeightGenerators()) + ", name=" + this.getName() + ", customDerivitives=" + String.valueOf(this.getCustomDerivitives()) + ", entitySpawners=" + String.valueOf(this.getEntitySpawners()) + ", effects=" + String.valueOf(this.getEffects()) + ", biomeStyle=" + String.valueOf(this.getBiomeStyle()) + ", blockDrops=" + String.valueOf(this.getBlockDrops()) + ", loot=" + String.valueOf(this.getLoot()) + ", lockLayers=" + this.isLockLayers() + ", lockLayersMax=" + this.getLockLayersMax() + ", carving=" + String.valueOf(this.getCarving()) + ", fluidBodies=" + String.valueOf(this.getFluidBodies()) + ", rarity=" + this.getRarity() + ", color=" + this.getColor() + ", derivative=" + String.valueOf(this.getDerivative()) + ", vanillaDerivative=" + String.valueOf(this.getVanillaDerivative()) + ", biomeScatter=" + String.valueOf(this.getBiomeScatter()) + ", biomeSkyScatter=" + String.valueOf(this.getBiomeSkyScatter()) + ", childShrinkFactor=" + this.getChildShrinkFactor() + ", childStyle=" + String.valueOf(this.getChildStyle()) + ", children=" + String.valueOf(this.getChildren()) + ", jigsawStructures=" + String.valueOf(this.getJigsawStructures()) + ", carvingBiome=" + this.getCarvingBiome() + ", slab=" + String.valueOf(this.getSlab()) + ", wall=" + String.valueOf(this.getWall()) + ", layers=" + String.valueOf(this.getLayers()) + ", caveCeilingLayers=" + String.valueOf(this.getCaveCeilingLayers()) + ", seaLayers=" + String.valueOf(this.getSeaLayers()) + ", decorators=" + String.valueOf(this.getDecorators()) + ", objects=" + String.valueOf(this.getObjects()) + ", generators=" + String.valueOf(this.getGenerators()) + ", deposits=" + String.valueOf(this.getDeposits()) + ", inferredType=" + String.valueOf(this.getInferredType()) + ", ores=" + String.valueOf(this.getOres()) + ")";
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisBiome)) {
         return false;
      } else {
         IrisBiome var2 = (IrisBiome)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.isLockLayers() != var2.isLockLayers()) {
            return false;
         } else if (this.getLockLayersMax() != var2.getLockLayersMax()) {
            return false;
         } else if (this.getRarity() != var2.getRarity()) {
            return false;
         } else if (Double.compare(this.getChildShrinkFactor(), var2.getChildShrinkFactor()) != 0) {
            return false;
         } else {
            String var3 = this.getName();
            String var4 = var2.getName();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            label350: {
               KList var5 = this.getCustomDerivitives();
               KList var6 = var2.getCustomDerivitives();
               if (var5 == null) {
                  if (var6 == null) {
                     break label350;
                  }
               } else if (var5.equals(var6)) {
                  break label350;
               }

               return false;
            }

            label343: {
               KList var7 = this.getEntitySpawners();
               KList var8 = var2.getEntitySpawners();
               if (var7 == null) {
                  if (var8 == null) {
                     break label343;
                  }
               } else if (var7.equals(var8)) {
                  break label343;
               }

               return false;
            }

            KList var9 = this.getEffects();
            KList var10 = var2.getEffects();
            if (var9 == null) {
               if (var10 != null) {
                  return false;
               }
            } else if (!var9.equals(var10)) {
               return false;
            }

            label329: {
               IrisGeneratorStyle var11 = this.getBiomeStyle();
               IrisGeneratorStyle var12 = var2.getBiomeStyle();
               if (var11 == null) {
                  if (var12 == null) {
                     break label329;
                  }
               } else if (var11.equals(var12)) {
                  break label329;
               }

               return false;
            }

            KList var13 = this.getBlockDrops();
            KList var14 = var2.getBlockDrops();
            if (var13 == null) {
               if (var14 != null) {
                  return false;
               }
            } else if (!var13.equals(var14)) {
               return false;
            }

            IrisLootReference var15 = this.getLoot();
            IrisLootReference var16 = var2.getLoot();
            if (var15 == null) {
               if (var16 != null) {
                  return false;
               }
            } else if (!var15.equals(var16)) {
               return false;
            }

            IrisCarving var17 = this.getCarving();
            IrisCarving var18 = var2.getCarving();
            if (var17 == null) {
               if (var18 != null) {
                  return false;
               }
            } else if (!var17.equals(var18)) {
               return false;
            }

            label301: {
               IrisFluidBodies var19 = this.getFluidBodies();
               IrisFluidBodies var20 = var2.getFluidBodies();
               if (var19 == null) {
                  if (var20 == null) {
                     break label301;
                  }
               } else if (var19.equals(var20)) {
                  break label301;
               }

               return false;
            }

            label294: {
               String var21 = this.getColor();
               String var22 = var2.getColor();
               if (var21 == null) {
                  if (var22 == null) {
                     break label294;
                  }
               } else if (var21.equals(var22)) {
                  break label294;
               }

               return false;
            }

            Biome var23 = this.getDerivative();
            Biome var24 = var2.getDerivative();
            if (var23 == null) {
               if (var24 != null) {
                  return false;
               }
            } else if (!var23.equals(var24)) {
               return false;
            }

            label280: {
               Biome var25 = this.getVanillaDerivative();
               Biome var26 = var2.getVanillaDerivative();
               if (var25 == null) {
                  if (var26 == null) {
                     break label280;
                  }
               } else if (var25.equals(var26)) {
                  break label280;
               }

               return false;
            }

            label273: {
               KList var27 = this.getBiomeScatter();
               KList var28 = var2.getBiomeScatter();
               if (var27 == null) {
                  if (var28 == null) {
                     break label273;
                  }
               } else if (var27.equals(var28)) {
                  break label273;
               }

               return false;
            }

            KList var29 = this.getBiomeSkyScatter();
            KList var30 = var2.getBiomeSkyScatter();
            if (var29 == null) {
               if (var30 != null) {
                  return false;
               }
            } else if (!var29.equals(var30)) {
               return false;
            }

            IrisGeneratorStyle var31 = this.getChildStyle();
            IrisGeneratorStyle var32 = var2.getChildStyle();
            if (var31 == null) {
               if (var32 != null) {
                  return false;
               }
            } else if (!var31.equals(var32)) {
               return false;
            }

            label252: {
               KList var33 = this.getChildren();
               KList var34 = var2.getChildren();
               if (var33 == null) {
                  if (var34 == null) {
                     break label252;
                  }
               } else if (var33.equals(var34)) {
                  break label252;
               }

               return false;
            }

            KList var35 = this.getJigsawStructures();
            KList var36 = var2.getJigsawStructures();
            if (var35 == null) {
               if (var36 != null) {
                  return false;
               }
            } else if (!var35.equals(var36)) {
               return false;
            }

            String var37 = this.getCarvingBiome();
            String var38 = var2.getCarvingBiome();
            if (var37 == null) {
               if (var38 != null) {
                  return false;
               }
            } else if (!var37.equals(var38)) {
               return false;
            }

            label231: {
               IrisBiomePaletteLayer var39 = this.getSlab();
               IrisBiomePaletteLayer var40 = var2.getSlab();
               if (var39 == null) {
                  if (var40 == null) {
                     break label231;
                  }
               } else if (var39.equals(var40)) {
                  break label231;
               }

               return false;
            }

            IrisBiomePaletteLayer var41 = this.getWall();
            IrisBiomePaletteLayer var42 = var2.getWall();
            if (var41 == null) {
               if (var42 != null) {
                  return false;
               }
            } else if (!var41.equals(var42)) {
               return false;
            }

            label217: {
               KList var43 = this.getLayers();
               KList var44 = var2.getLayers();
               if (var43 == null) {
                  if (var44 == null) {
                     break label217;
                  }
               } else if (var43.equals(var44)) {
                  break label217;
               }

               return false;
            }

            label210: {
               KList var45 = this.getCaveCeilingLayers();
               KList var46 = var2.getCaveCeilingLayers();
               if (var45 == null) {
                  if (var46 == null) {
                     break label210;
                  }
               } else if (var45.equals(var46)) {
                  break label210;
               }

               return false;
            }

            KList var47 = this.getSeaLayers();
            KList var48 = var2.getSeaLayers();
            if (var47 == null) {
               if (var48 != null) {
                  return false;
               }
            } else if (!var47.equals(var48)) {
               return false;
            }

            KList var49 = this.getDecorators();
            KList var50 = var2.getDecorators();
            if (var49 == null) {
               if (var50 != null) {
                  return false;
               }
            } else if (!var49.equals(var50)) {
               return false;
            }

            label189: {
               KList var51 = this.getObjects();
               KList var52 = var2.getObjects();
               if (var51 == null) {
                  if (var52 == null) {
                     break label189;
                  }
               } else if (var51.equals(var52)) {
                  break label189;
               }

               return false;
            }

            label182: {
               KList var53 = this.getGenerators();
               KList var54 = var2.getGenerators();
               if (var53 == null) {
                  if (var54 == null) {
                     break label182;
                  }
               } else if (var53.equals(var54)) {
                  break label182;
               }

               return false;
            }

            KList var55 = this.getDeposits();
            KList var56 = var2.getDeposits();
            if (var55 == null) {
               if (var56 != null) {
                  return false;
               }
            } else if (!var55.equals(var56)) {
               return false;
            }

            KList var57 = this.getOres();
            KList var58 = var2.getOres();
            if (var57 == null) {
               if (var58 != null) {
                  return false;
               }
            } else if (!var57.equals(var58)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisBiome;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var33 = var2 * 59 + (this.isLockLayers() ? 79 : 97);
      var33 = var33 * 59 + this.getLockLayersMax();
      var33 = var33 * 59 + this.getRarity();
      long var3 = Double.doubleToLongBits(this.getChildShrinkFactor());
      var33 = var33 * 59 + (int)(var3 >>> 32 ^ var3);
      String var5 = this.getName();
      var33 = var33 * 59 + (var5 == null ? 43 : var5.hashCode());
      KList var6 = this.getCustomDerivitives();
      var33 = var33 * 59 + (var6 == null ? 43 : var6.hashCode());
      KList var7 = this.getEntitySpawners();
      var33 = var33 * 59 + (var7 == null ? 43 : var7.hashCode());
      KList var8 = this.getEffects();
      var33 = var33 * 59 + (var8 == null ? 43 : var8.hashCode());
      IrisGeneratorStyle var9 = this.getBiomeStyle();
      var33 = var33 * 59 + (var9 == null ? 43 : var9.hashCode());
      KList var10 = this.getBlockDrops();
      var33 = var33 * 59 + (var10 == null ? 43 : var10.hashCode());
      IrisLootReference var11 = this.getLoot();
      var33 = var33 * 59 + (var11 == null ? 43 : var11.hashCode());
      IrisCarving var12 = this.getCarving();
      var33 = var33 * 59 + (var12 == null ? 43 : var12.hashCode());
      IrisFluidBodies var13 = this.getFluidBodies();
      var33 = var33 * 59 + (var13 == null ? 43 : var13.hashCode());
      String var14 = this.getColor();
      var33 = var33 * 59 + (var14 == null ? 43 : var14.hashCode());
      Biome var15 = this.getDerivative();
      var33 = var33 * 59 + (var15 == null ? 43 : var15.hashCode());
      Biome var16 = this.getVanillaDerivative();
      var33 = var33 * 59 + (var16 == null ? 43 : var16.hashCode());
      KList var17 = this.getBiomeScatter();
      var33 = var33 * 59 + (var17 == null ? 43 : var17.hashCode());
      KList var18 = this.getBiomeSkyScatter();
      var33 = var33 * 59 + (var18 == null ? 43 : var18.hashCode());
      IrisGeneratorStyle var19 = this.getChildStyle();
      var33 = var33 * 59 + (var19 == null ? 43 : var19.hashCode());
      KList var20 = this.getChildren();
      var33 = var33 * 59 + (var20 == null ? 43 : var20.hashCode());
      KList var21 = this.getJigsawStructures();
      var33 = var33 * 59 + (var21 == null ? 43 : var21.hashCode());
      String var22 = this.getCarvingBiome();
      var33 = var33 * 59 + (var22 == null ? 43 : var22.hashCode());
      IrisBiomePaletteLayer var23 = this.getSlab();
      var33 = var33 * 59 + (var23 == null ? 43 : var23.hashCode());
      IrisBiomePaletteLayer var24 = this.getWall();
      var33 = var33 * 59 + (var24 == null ? 43 : var24.hashCode());
      KList var25 = this.getLayers();
      var33 = var33 * 59 + (var25 == null ? 43 : var25.hashCode());
      KList var26 = this.getCaveCeilingLayers();
      var33 = var33 * 59 + (var26 == null ? 43 : var26.hashCode());
      KList var27 = this.getSeaLayers();
      var33 = var33 * 59 + (var27 == null ? 43 : var27.hashCode());
      KList var28 = this.getDecorators();
      var33 = var33 * 59 + (var28 == null ? 43 : var28.hashCode());
      KList var29 = this.getObjects();
      var33 = var33 * 59 + (var29 == null ? 43 : var29.hashCode());
      KList var30 = this.getGenerators();
      var33 = var33 * 59 + (var30 == null ? 43 : var30.hashCode());
      KList var31 = this.getDeposits();
      var33 = var33 * 59 + (var31 == null ? 43 : var31.hashCode());
      KList var32 = this.getOres();
      var33 = var33 * 59 + (var32 == null ? 43 : var32.hashCode());
      return var33;
   }

   static {
      BARRIER = Material.BARRIER.createBlockData();
   }
}
