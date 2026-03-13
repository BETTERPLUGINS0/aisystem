package com.volmit.iris.engine.object;

import com.volmit.iris.Iris;
import com.volmit.iris.core.gui.components.RenderType;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.core.loader.IrisRegistrant;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.RegistryListResource;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.collection.KSet;
import com.volmit.iris.util.data.DataProvider;
import com.volmit.iris.util.data.VanillaBiomeMap;
import com.volmit.iris.util.inventorygui.RandomColor;
import com.volmit.iris.util.json.JSONObject;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.noise.CNG;
import com.volmit.iris.util.plugin.VolmitSender;
import java.awt.Color;
import java.util.Iterator;
import java.util.Random;
import lombok.Generated;
import org.bukkit.block.data.BlockData;

@Desc("Represents an iris region")
public class IrisRegion extends IrisRegistrant implements IRare {
   private final transient AtomicCache<KList<IrisObjectPlacement>> surfaceObjectsCache = new AtomicCache();
   private final transient AtomicCache<KList<IrisObjectPlacement>> carveObjectsCache = new AtomicCache();
   private final transient AtomicCache<KList<String>> cacheRidge = new AtomicCache();
   private final transient AtomicCache<KList<String>> cacheSpot = new AtomicCache();
   private final transient AtomicCache<CNG> shoreHeightGenerator = new AtomicCache();
   private final transient AtomicCache<KList<IrisBiome>> realLandBiomes = new AtomicCache();
   private final transient AtomicCache<KList<IrisBiome>> realLakeBiomes = new AtomicCache();
   private final transient AtomicCache<KList<IrisBiome>> realRiverBiomes = new AtomicCache();
   private final transient AtomicCache<KList<IrisBiome>> realSeaBiomes = new AtomicCache();
   private final transient AtomicCache<KList<IrisBiome>> realShoreBiomes = new AtomicCache();
   private final transient AtomicCache<KList<IrisBiome>> realCaveBiomes = new AtomicCache();
   private final transient AtomicCache<CNG> lakeGen = new AtomicCache();
   private final transient AtomicCache<CNG> riverGen = new AtomicCache();
   private final transient AtomicCache<CNG> riverChanceGen = new AtomicCache();
   private final transient AtomicCache<Color> cacheColor = new AtomicCache();
   @MinNumber(2.0D)
   @Required
   @Desc("The name of the region")
   private String name = "A Region";
   @ArrayType(
      min = 1,
      type = IrisJigsawStructurePlacement.class
   )
   @Desc("Jigsaw structures")
   private KList<IrisJigsawStructurePlacement> jigsawStructures = new KList();
   @ArrayType(
      min = 1,
      type = IrisEffect.class
   )
   @Desc("Effects are ambient effects such as potion effects, random sounds, or even particles around each player. All of these effects are played via packets so two players won't see/hear each others effects.\nDue to performance reasons, effects will play arround the player even if where the effect was played is no longer in the biome the player is in.")
   private KList<IrisEffect> effects = new KList();
   @Desc("Spawn Entities in this region over time. Iris will continually replenish these mobs just like vanilla does.")
   @ArrayType(
      min = 1,
      type = String.class
   )
   @RegistryListResource(IrisSpawner.class)
   private KList<String> entitySpawners = new KList();
   @MinNumber(1.0D)
   @MaxNumber(128.0D)
   @Desc("The rarity of the region")
   private int rarity = 1;
   @ArrayType(
      min = 1,
      type = IrisBlockDrops.class
   )
   @Desc("Define custom block drops for this region")
   private KList<IrisBlockDrops> blockDrops = new KList();
   @RegistryListResource(IrisSpawner.class)
   @ArrayType(
      min = 1,
      type = IrisObjectPlacement.class
   )
   @Desc("Objects define what schematics (iob files) iris will place in this region")
   private KList<IrisObjectPlacement> objects = new KList();
   @MinNumber(0.0D)
   @Desc("The min shore height")
   private double shoreHeightMin = 1.2D;
   @Desc("Reference loot tables in this area")
   private IrisLootReference loot = new IrisLootReference();
   @MinNumber(0.0D)
   @Desc("The the max shore height")
   private double shoreHeightMax = 3.2D;
   @MinNumber(1.0E-4D)
   @Desc("The varience of the shore height")
   private double shoreHeightZoom = 3.14D;
   @MinNumber(1.0E-4D)
   @Desc("How large land biomes are in this region")
   private double landBiomeZoom = 1.0D;
   @MinNumber(1.0E-4D)
   @Desc("How large shore biomes are in this region")
   private double shoreBiomeZoom = 1.0D;
   @MinNumber(1.0E-4D)
   @Desc("How large sea biomes are in this region")
   private double seaBiomeZoom = 1.0D;
   @MinNumber(1.0E-4D)
   @Desc("How large cave biomes are in this region")
   private double caveBiomeZoom = 1.0D;
   @Desc("Carving configuration for the dimension")
   private IrisCarving carving = new IrisCarving();
   @Desc("Configuration of fluid bodies such as rivers & lakes")
   private IrisFluidBodies fluidBodies = new IrisFluidBodies();
   @RegistryListResource(IrisBiome.class)
   @Required
   @ArrayType(
      min = 1,
      type = String.class
   )
   @Desc("A list of root-level biomes in this region. Don't specify child biomes of other biomes here. Just the root parents.")
   private KList<String> landBiomes = new KList();
   @RegistryListResource(IrisBiome.class)
   @Required
   @ArrayType(
      min = 1,
      type = String.class
   )
   @Desc("A list of root-level biomes in this region. Don't specify child biomes of other biomes here. Just the root parents.")
   private KList<String> seaBiomes = new KList();
   @RegistryListResource(IrisBiome.class)
   @Required
   @ArrayType(
      min = 1,
      type = String.class
   )
   @Desc("A list of root-level biomes in this region. Don't specify child biomes of other biomes here. Just the root parents.")
   private KList<String> shoreBiomes = new KList();
   @RegistryListResource(IrisBiome.class)
   @ArrayType(
      min = 1,
      type = String.class
   )
   @Desc("A list of root-level biomes in this region. Don't specify child biomes of other biomes here. Just the root parents.")
   private KList<String> caveBiomes = new KList();
   @ArrayType(
      min = 1,
      type = IrisDepositGenerator.class
   )
   @Desc("Define regional deposit generators that add onto the global deposit generators")
   private KList<IrisDepositGenerator> deposits = new KList();
   @Desc("The style of rivers")
   private IrisGeneratorStyle riverStyle;
   @Desc("The style of lakes")
   private IrisGeneratorStyle lakeStyle;
   @Desc("A color for visualizing this region with a color. I.e. #F13AF5. This will show up on the map.")
   private String color;
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

   public String getName() {
      return this.name;
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

   public double getBiomeZoom(InferredType t) {
      switch(var1) {
      case CAVE:
         return this.caveBiomeZoom;
      case LAND:
         return this.landBiomeZoom;
      case SEA:
         return this.seaBiomeZoom;
      case SHORE:
         return this.shoreBiomeZoom;
      default:
         return 1.0D;
      }
   }

   public CNG getShoreHeightGenerator() {
      return (CNG)this.shoreHeightGenerator.aquire(() -> {
         return CNG.signature(new RNG((long)((double)this.getName().length() + this.getLandBiomeZoom() + (double)this.getLandBiomes().size() + 3458612.0D)));
      });
   }

   public double getShoreHeight(double x, double z) {
      return this.getShoreHeightGenerator().fitDouble(this.shoreHeightMin, this.shoreHeightMax, var1 / this.shoreHeightZoom, var3 / this.shoreHeightZoom);
   }

   public KSet<String> getAllBiomeIds() {
      KSet var1 = new KSet(new String[0]);
      var1.addAll(this.landBiomes);
      var1.addAll(this.caveBiomes);
      var1.addAll(this.seaBiomes);
      var1.addAll(this.shoreBiomes);
      return var1;
   }

   public KList<IrisBiome> getAllBiomes(DataProvider g) {
      KMap var2 = new KMap();
      KSet var3 = this.getAllBiomeIds();

      while(!var3.isEmpty()) {
         Iterator var4 = (new KList(var3)).iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            if (var2.containsKey(var5)) {
               var3.remove(var5);
            } else {
               IrisBiome var6 = (IrisBiome)var1.getData().getBiomeLoader().load(var5);
               var3.remove(var5);
               if (var6 != null) {
                  var3.add(var6.getCarvingBiome());
                  var2.put(var6.getLoadKey(), var6);
                  var3.addAll(var6.getChildren());
               }
            }
         }
      }

      return var2.v();
   }

   public KList<IrisBiome> getBiomes(DataProvider g, InferredType type) {
      if (var2.equals(InferredType.LAND)) {
         return this.getRealLandBiomes(var1);
      } else if (var2.equals(InferredType.SEA)) {
         return this.getRealSeaBiomes(var1);
      } else if (var2.equals(InferredType.SHORE)) {
         return this.getRealShoreBiomes(var1);
      } else {
         return var2.equals(InferredType.CAVE) ? this.getRealCaveBiomes(var1) : new KList();
      }
   }

   public KList<IrisBiome> getRealCaveBiomes(DataProvider g) {
      return (KList)this.realCaveBiomes.aquire(() -> {
         KList var2 = new KList();
         Iterator var3 = this.getCaveBiomes().iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            var2.add((Object)((IrisBiome)var1.getData().getBiomeLoader().load(var4)));
         }

         return var2;
      });
   }

   public KList<IrisBiome> getRealShoreBiomes(DataProvider g) {
      return (KList)this.realShoreBiomes.aquire(() -> {
         KList var2 = new KList();
         Iterator var3 = this.getShoreBiomes().iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            var2.add((Object)((IrisBiome)var1.getData().getBiomeLoader().load(var4)));
         }

         return var2;
      });
   }

   public KList<IrisBiome> getRealSeaBiomes(DataProvider g) {
      return (KList)this.realSeaBiomes.aquire(() -> {
         KList var2 = new KList();
         Iterator var3 = this.getSeaBiomes().iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            var2.add((Object)((IrisBiome)var1.getData().getBiomeLoader().load(var4)));
         }

         return var2;
      });
   }

   public KList<IrisBiome> getRealLandBiomes(DataProvider g) {
      return (KList)this.realLandBiomes.aquire(() -> {
         KList var2 = new KList();
         Iterator var3 = this.getLandBiomes().iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            var2.add((Object)((IrisBiome)var1.getData().getBiomeLoader().load(var4)));
         }

         return var2;
      });
   }

   public KList<IrisBiome> getAllAnyBiomes() {
      KMap var1 = new KMap();
      KSet var2 = new KSet(new String[0]);
      var2.addAll(this.landBiomes);
      var2.addAll(this.caveBiomes);
      var2.addAll(this.seaBiomes);
      var2.addAll(this.shoreBiomes);

      while(!var2.isEmpty()) {
         Iterator var3 = (new KList(var2)).iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            if (var1.containsKey(var4)) {
               var2.remove(var4);
            } else {
               IrisBiome var5 = IrisData.loadAnyBiome(var4, this.getLoader());
               var2.remove(var4);
               if (var5 != null) {
                  var2.add(var5.getCarvingBiome());
                  var1.put(var5.getLoadKey(), var5);
                  var2.addAll(var5.getChildren());
               }
            }
         }
      }

      return var1.v();
   }

   public Color getColor(DataProvider dataProvider, RenderType type) {
      return (Color)this.cacheColor.aquire(() -> {
         if (this.color == null) {
            Random var2 = new Random((long)(this.getName().hashCode() + this.getAllBiomeIds().hashCode()));
            RandomColor var3 = new RandomColor(var2);
            KList var4 = this.getRealLandBiomes(var1);

            while(var4.size() > 0) {
               int var5 = var2.nextInt(var4.size());
               IrisBiome var6 = (IrisBiome)var4.get(var5);
               if (var6.getVanillaDerivative() != null) {
                  RandomColor.Color var7 = VanillaBiomeMap.getColorType(var6.getVanillaDerivative());
                  RandomColor.Luminosity var8 = VanillaBiomeMap.getColorLuminosity(var6.getVanillaDerivative());
                  RandomColor.SaturationType var9 = VanillaBiomeMap.getColorSaturatiom(var6.getVanillaDerivative());
                  int var10 = var3.randomColor(var7, var7 == RandomColor.Color.MONOCHROME ? RandomColor.SaturationType.MONOCHROME : var9, var8);
                  return new Color(var10);
               }

               var4.remove(var5);
            }

            Iris.warn("Couldn't find a suitable color for region " + this.getName());
            return new Color((new RandomColor(var2)).randomColor());
         } else {
            try {
               return Color.decode(this.color);
            } catch (NumberFormatException var11) {
               Iris.warn("Could not parse color \"" + this.color + "\" for region " + this.getName());
               return Color.WHITE;
            }
         }
      });
   }

   public void pickRandomColor(DataProvider data) {
   }

   public String getFolderName() {
      return "regions";
   }

   public String getTypeName() {
      return "Region";
   }

   public void scanForErrors(JSONObject p, VolmitSender sender) {
   }

   @Generated
   public IrisRegion() {
      this.riverStyle = NoiseStyle.VASCULAR_THIN.style().zoomed(7.77D);
      this.lakeStyle = NoiseStyle.CELLULAR_IRIS_THICK.style();
      this.color = null;
      this.ores = new KList();
   }

   @Generated
   public IrisRegion(final String name, final KList<IrisJigsawStructurePlacement> jigsawStructures, final KList<IrisEffect> effects, final KList<String> entitySpawners, final int rarity, final KList<IrisBlockDrops> blockDrops, final KList<IrisObjectPlacement> objects, final double shoreHeightMin, final IrisLootReference loot, final double shoreHeightMax, final double shoreHeightZoom, final double landBiomeZoom, final double shoreBiomeZoom, final double seaBiomeZoom, final double caveBiomeZoom, final IrisCarving carving, final IrisFluidBodies fluidBodies, final KList<String> landBiomes, final KList<String> seaBiomes, final KList<String> shoreBiomes, final KList<String> caveBiomes, final KList<IrisDepositGenerator> deposits, final IrisGeneratorStyle riverStyle, final IrisGeneratorStyle lakeStyle, final String color, final KList<IrisOreGenerator> ores) {
      this.riverStyle = NoiseStyle.VASCULAR_THIN.style().zoomed(7.77D);
      this.lakeStyle = NoiseStyle.CELLULAR_IRIS_THICK.style();
      this.color = null;
      this.ores = new KList();
      this.name = var1;
      this.jigsawStructures = var2;
      this.effects = var3;
      this.entitySpawners = var4;
      this.rarity = var5;
      this.blockDrops = var6;
      this.objects = var7;
      this.shoreHeightMin = var8;
      this.loot = var10;
      this.shoreHeightMax = var11;
      this.shoreHeightZoom = var13;
      this.landBiomeZoom = var15;
      this.shoreBiomeZoom = var17;
      this.seaBiomeZoom = var19;
      this.caveBiomeZoom = var21;
      this.carving = var23;
      this.fluidBodies = var24;
      this.landBiomes = var25;
      this.seaBiomes = var26;
      this.shoreBiomes = var27;
      this.caveBiomes = var28;
      this.deposits = var29;
      this.riverStyle = var30;
      this.lakeStyle = var31;
      this.color = var32;
      this.ores = var33;
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
   public AtomicCache<KList<String>> getCacheRidge() {
      return this.cacheRidge;
   }

   @Generated
   public AtomicCache<KList<String>> getCacheSpot() {
      return this.cacheSpot;
   }

   @Generated
   public AtomicCache<KList<IrisBiome>> getRealLandBiomes() {
      return this.realLandBiomes;
   }

   @Generated
   public AtomicCache<KList<IrisBiome>> getRealLakeBiomes() {
      return this.realLakeBiomes;
   }

   @Generated
   public AtomicCache<KList<IrisBiome>> getRealRiverBiomes() {
      return this.realRiverBiomes;
   }

   @Generated
   public AtomicCache<KList<IrisBiome>> getRealSeaBiomes() {
      return this.realSeaBiomes;
   }

   @Generated
   public AtomicCache<KList<IrisBiome>> getRealShoreBiomes() {
      return this.realShoreBiomes;
   }

   @Generated
   public AtomicCache<KList<IrisBiome>> getRealCaveBiomes() {
      return this.realCaveBiomes;
   }

   @Generated
   public AtomicCache<CNG> getLakeGen() {
      return this.lakeGen;
   }

   @Generated
   public AtomicCache<CNG> getRiverGen() {
      return this.riverGen;
   }

   @Generated
   public AtomicCache<CNG> getRiverChanceGen() {
      return this.riverChanceGen;
   }

   @Generated
   public AtomicCache<Color> getCacheColor() {
      return this.cacheColor;
   }

   @Generated
   public KList<IrisJigsawStructurePlacement> getJigsawStructures() {
      return this.jigsawStructures;
   }

   @Generated
   public KList<IrisEffect> getEffects() {
      return this.effects;
   }

   @Generated
   public KList<String> getEntitySpawners() {
      return this.entitySpawners;
   }

   @Generated
   public int getRarity() {
      return this.rarity;
   }

   @Generated
   public KList<IrisBlockDrops> getBlockDrops() {
      return this.blockDrops;
   }

   @Generated
   public KList<IrisObjectPlacement> getObjects() {
      return this.objects;
   }

   @Generated
   public double getShoreHeightMin() {
      return this.shoreHeightMin;
   }

   @Generated
   public IrisLootReference getLoot() {
      return this.loot;
   }

   @Generated
   public double getShoreHeightMax() {
      return this.shoreHeightMax;
   }

   @Generated
   public double getShoreHeightZoom() {
      return this.shoreHeightZoom;
   }

   @Generated
   public double getLandBiomeZoom() {
      return this.landBiomeZoom;
   }

   @Generated
   public double getShoreBiomeZoom() {
      return this.shoreBiomeZoom;
   }

   @Generated
   public double getSeaBiomeZoom() {
      return this.seaBiomeZoom;
   }

   @Generated
   public double getCaveBiomeZoom() {
      return this.caveBiomeZoom;
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
   public KList<String> getLandBiomes() {
      return this.landBiomes;
   }

   @Generated
   public KList<String> getSeaBiomes() {
      return this.seaBiomes;
   }

   @Generated
   public KList<String> getShoreBiomes() {
      return this.shoreBiomes;
   }

   @Generated
   public KList<String> getCaveBiomes() {
      return this.caveBiomes;
   }

   @Generated
   public KList<IrisDepositGenerator> getDeposits() {
      return this.deposits;
   }

   @Generated
   public IrisGeneratorStyle getRiverStyle() {
      return this.riverStyle;
   }

   @Generated
   public IrisGeneratorStyle getLakeStyle() {
      return this.lakeStyle;
   }

   @Generated
   public String getColor() {
      return this.color;
   }

   @Generated
   public KList<IrisOreGenerator> getOres() {
      return this.ores;
   }

   @Generated
   public IrisRegion setName(final String name) {
      this.name = var1;
      return this;
   }

   @Generated
   public IrisRegion setJigsawStructures(final KList<IrisJigsawStructurePlacement> jigsawStructures) {
      this.jigsawStructures = var1;
      return this;
   }

   @Generated
   public IrisRegion setEffects(final KList<IrisEffect> effects) {
      this.effects = var1;
      return this;
   }

   @Generated
   public IrisRegion setEntitySpawners(final KList<String> entitySpawners) {
      this.entitySpawners = var1;
      return this;
   }

   @Generated
   public IrisRegion setRarity(final int rarity) {
      this.rarity = var1;
      return this;
   }

   @Generated
   public IrisRegion setBlockDrops(final KList<IrisBlockDrops> blockDrops) {
      this.blockDrops = var1;
      return this;
   }

   @Generated
   public IrisRegion setObjects(final KList<IrisObjectPlacement> objects) {
      this.objects = var1;
      return this;
   }

   @Generated
   public IrisRegion setShoreHeightMin(final double shoreHeightMin) {
      this.shoreHeightMin = var1;
      return this;
   }

   @Generated
   public IrisRegion setLoot(final IrisLootReference loot) {
      this.loot = var1;
      return this;
   }

   @Generated
   public IrisRegion setShoreHeightMax(final double shoreHeightMax) {
      this.shoreHeightMax = var1;
      return this;
   }

   @Generated
   public IrisRegion setShoreHeightZoom(final double shoreHeightZoom) {
      this.shoreHeightZoom = var1;
      return this;
   }

   @Generated
   public IrisRegion setLandBiomeZoom(final double landBiomeZoom) {
      this.landBiomeZoom = var1;
      return this;
   }

   @Generated
   public IrisRegion setShoreBiomeZoom(final double shoreBiomeZoom) {
      this.shoreBiomeZoom = var1;
      return this;
   }

   @Generated
   public IrisRegion setSeaBiomeZoom(final double seaBiomeZoom) {
      this.seaBiomeZoom = var1;
      return this;
   }

   @Generated
   public IrisRegion setCaveBiomeZoom(final double caveBiomeZoom) {
      this.caveBiomeZoom = var1;
      return this;
   }

   @Generated
   public IrisRegion setCarving(final IrisCarving carving) {
      this.carving = var1;
      return this;
   }

   @Generated
   public IrisRegion setFluidBodies(final IrisFluidBodies fluidBodies) {
      this.fluidBodies = var1;
      return this;
   }

   @Generated
   public IrisRegion setLandBiomes(final KList<String> landBiomes) {
      this.landBiomes = var1;
      return this;
   }

   @Generated
   public IrisRegion setSeaBiomes(final KList<String> seaBiomes) {
      this.seaBiomes = var1;
      return this;
   }

   @Generated
   public IrisRegion setShoreBiomes(final KList<String> shoreBiomes) {
      this.shoreBiomes = var1;
      return this;
   }

   @Generated
   public IrisRegion setCaveBiomes(final KList<String> caveBiomes) {
      this.caveBiomes = var1;
      return this;
   }

   @Generated
   public IrisRegion setDeposits(final KList<IrisDepositGenerator> deposits) {
      this.deposits = var1;
      return this;
   }

   @Generated
   public IrisRegion setRiverStyle(final IrisGeneratorStyle riverStyle) {
      this.riverStyle = var1;
      return this;
   }

   @Generated
   public IrisRegion setLakeStyle(final IrisGeneratorStyle lakeStyle) {
      this.lakeStyle = var1;
      return this;
   }

   @Generated
   public IrisRegion setColor(final String color) {
      this.color = var1;
      return this;
   }

   @Generated
   public IrisRegion setOres(final KList<IrisOreGenerator> ores) {
      this.ores = var1;
      return this;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getSurfaceObjectsCache());
      return "IrisRegion(surfaceObjectsCache=" + var10000 + ", carveObjectsCache=" + String.valueOf(this.getCarveObjectsCache()) + ", cacheRidge=" + String.valueOf(this.getCacheRidge()) + ", cacheSpot=" + String.valueOf(this.getCacheSpot()) + ", shoreHeightGenerator=" + String.valueOf(this.getShoreHeightGenerator()) + ", realLandBiomes=" + String.valueOf(this.getRealLandBiomes()) + ", realLakeBiomes=" + String.valueOf(this.getRealLakeBiomes()) + ", realRiverBiomes=" + String.valueOf(this.getRealRiverBiomes()) + ", realSeaBiomes=" + String.valueOf(this.getRealSeaBiomes()) + ", realShoreBiomes=" + String.valueOf(this.getRealShoreBiomes()) + ", realCaveBiomes=" + String.valueOf(this.getRealCaveBiomes()) + ", lakeGen=" + String.valueOf(this.getLakeGen()) + ", riverGen=" + String.valueOf(this.getRiverGen()) + ", riverChanceGen=" + String.valueOf(this.getRiverChanceGen()) + ", cacheColor=" + String.valueOf(this.getCacheColor()) + ", name=" + this.getName() + ", jigsawStructures=" + String.valueOf(this.getJigsawStructures()) + ", effects=" + String.valueOf(this.getEffects()) + ", entitySpawners=" + String.valueOf(this.getEntitySpawners()) + ", rarity=" + this.getRarity() + ", blockDrops=" + String.valueOf(this.getBlockDrops()) + ", objects=" + String.valueOf(this.getObjects()) + ", shoreHeightMin=" + this.getShoreHeightMin() + ", loot=" + String.valueOf(this.getLoot()) + ", shoreHeightMax=" + this.getShoreHeightMax() + ", shoreHeightZoom=" + this.getShoreHeightZoom() + ", landBiomeZoom=" + this.getLandBiomeZoom() + ", shoreBiomeZoom=" + this.getShoreBiomeZoom() + ", seaBiomeZoom=" + this.getSeaBiomeZoom() + ", caveBiomeZoom=" + this.getCaveBiomeZoom() + ", carving=" + String.valueOf(this.getCarving()) + ", fluidBodies=" + String.valueOf(this.getFluidBodies()) + ", landBiomes=" + String.valueOf(this.getLandBiomes()) + ", seaBiomes=" + String.valueOf(this.getSeaBiomes()) + ", shoreBiomes=" + String.valueOf(this.getShoreBiomes()) + ", caveBiomes=" + String.valueOf(this.getCaveBiomes()) + ", deposits=" + String.valueOf(this.getDeposits()) + ", riverStyle=" + String.valueOf(this.getRiverStyle()) + ", lakeStyle=" + String.valueOf(this.getLakeStyle()) + ", color=" + this.getColor() + ", ores=" + String.valueOf(this.getOres()) + ")";
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisRegion)) {
         return false;
      } else {
         IrisRegion var2 = (IrisRegion)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getRarity() != var2.getRarity()) {
            return false;
         } else if (Double.compare(this.getShoreHeightMin(), var2.getShoreHeightMin()) != 0) {
            return false;
         } else if (Double.compare(this.getShoreHeightMax(), var2.getShoreHeightMax()) != 0) {
            return false;
         } else if (Double.compare(this.getShoreHeightZoom(), var2.getShoreHeightZoom()) != 0) {
            return false;
         } else if (Double.compare(this.getLandBiomeZoom(), var2.getLandBiomeZoom()) != 0) {
            return false;
         } else if (Double.compare(this.getShoreBiomeZoom(), var2.getShoreBiomeZoom()) != 0) {
            return false;
         } else if (Double.compare(this.getSeaBiomeZoom(), var2.getSeaBiomeZoom()) != 0) {
            return false;
         } else if (Double.compare(this.getCaveBiomeZoom(), var2.getCaveBiomeZoom()) != 0) {
            return false;
         } else {
            label247: {
               String var3 = this.getName();
               String var4 = var2.getName();
               if (var3 == null) {
                  if (var4 == null) {
                     break label247;
                  }
               } else if (var3.equals(var4)) {
                  break label247;
               }

               return false;
            }

            KList var5 = this.getJigsawStructures();
            KList var6 = var2.getJigsawStructures();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            label233: {
               KList var7 = this.getEffects();
               KList var8 = var2.getEffects();
               if (var7 == null) {
                  if (var8 == null) {
                     break label233;
                  }
               } else if (var7.equals(var8)) {
                  break label233;
               }

               return false;
            }

            KList var9 = this.getEntitySpawners();
            KList var10 = var2.getEntitySpawners();
            if (var9 == null) {
               if (var10 != null) {
                  return false;
               }
            } else if (!var9.equals(var10)) {
               return false;
            }

            label219: {
               KList var11 = this.getBlockDrops();
               KList var12 = var2.getBlockDrops();
               if (var11 == null) {
                  if (var12 == null) {
                     break label219;
                  }
               } else if (var11.equals(var12)) {
                  break label219;
               }

               return false;
            }

            KList var13 = this.getObjects();
            KList var14 = var2.getObjects();
            if (var13 == null) {
               if (var14 != null) {
                  return false;
               }
            } else if (!var13.equals(var14)) {
               return false;
            }

            label205: {
               IrisLootReference var15 = this.getLoot();
               IrisLootReference var16 = var2.getLoot();
               if (var15 == null) {
                  if (var16 == null) {
                     break label205;
                  }
               } else if (var15.equals(var16)) {
                  break label205;
               }

               return false;
            }

            label198: {
               IrisCarving var17 = this.getCarving();
               IrisCarving var18 = var2.getCarving();
               if (var17 == null) {
                  if (var18 == null) {
                     break label198;
                  }
               } else if (var17.equals(var18)) {
                  break label198;
               }

               return false;
            }

            IrisFluidBodies var19 = this.getFluidBodies();
            IrisFluidBodies var20 = var2.getFluidBodies();
            if (var19 == null) {
               if (var20 != null) {
                  return false;
               }
            } else if (!var19.equals(var20)) {
               return false;
            }

            label184: {
               KList var21 = this.getLandBiomes();
               KList var22 = var2.getLandBiomes();
               if (var21 == null) {
                  if (var22 == null) {
                     break label184;
                  }
               } else if (var21.equals(var22)) {
                  break label184;
               }

               return false;
            }

            label177: {
               KList var23 = this.getSeaBiomes();
               KList var24 = var2.getSeaBiomes();
               if (var23 == null) {
                  if (var24 == null) {
                     break label177;
                  }
               } else if (var23.equals(var24)) {
                  break label177;
               }

               return false;
            }

            KList var25 = this.getShoreBiomes();
            KList var26 = var2.getShoreBiomes();
            if (var25 == null) {
               if (var26 != null) {
                  return false;
               }
            } else if (!var25.equals(var26)) {
               return false;
            }

            KList var27 = this.getCaveBiomes();
            KList var28 = var2.getCaveBiomes();
            if (var27 == null) {
               if (var28 != null) {
                  return false;
               }
            } else if (!var27.equals(var28)) {
               return false;
            }

            label156: {
               KList var29 = this.getDeposits();
               KList var30 = var2.getDeposits();
               if (var29 == null) {
                  if (var30 == null) {
                     break label156;
                  }
               } else if (var29.equals(var30)) {
                  break label156;
               }

               return false;
            }

            IrisGeneratorStyle var31 = this.getRiverStyle();
            IrisGeneratorStyle var32 = var2.getRiverStyle();
            if (var31 == null) {
               if (var32 != null) {
                  return false;
               }
            } else if (!var31.equals(var32)) {
               return false;
            }

            IrisGeneratorStyle var33 = this.getLakeStyle();
            IrisGeneratorStyle var34 = var2.getLakeStyle();
            if (var33 == null) {
               if (var34 != null) {
                  return false;
               }
            } else if (!var33.equals(var34)) {
               return false;
            }

            label135: {
               String var35 = this.getColor();
               String var36 = var2.getColor();
               if (var35 == null) {
                  if (var36 == null) {
                     break label135;
                  }
               } else if (var35.equals(var36)) {
                  break label135;
               }

               return false;
            }

            KList var37 = this.getOres();
            KList var38 = var2.getOres();
            if (var37 == null) {
               if (var38 != null) {
                  return false;
               }
            } else if (!var37.equals(var38)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisRegion;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var35 = var2 * 59 + this.getRarity();
      long var3 = Double.doubleToLongBits(this.getShoreHeightMin());
      var35 = var35 * 59 + (int)(var3 >>> 32 ^ var3);
      long var5 = Double.doubleToLongBits(this.getShoreHeightMax());
      var35 = var35 * 59 + (int)(var5 >>> 32 ^ var5);
      long var7 = Double.doubleToLongBits(this.getShoreHeightZoom());
      var35 = var35 * 59 + (int)(var7 >>> 32 ^ var7);
      long var9 = Double.doubleToLongBits(this.getLandBiomeZoom());
      var35 = var35 * 59 + (int)(var9 >>> 32 ^ var9);
      long var11 = Double.doubleToLongBits(this.getShoreBiomeZoom());
      var35 = var35 * 59 + (int)(var11 >>> 32 ^ var11);
      long var13 = Double.doubleToLongBits(this.getSeaBiomeZoom());
      var35 = var35 * 59 + (int)(var13 >>> 32 ^ var13);
      long var15 = Double.doubleToLongBits(this.getCaveBiomeZoom());
      var35 = var35 * 59 + (int)(var15 >>> 32 ^ var15);
      String var17 = this.getName();
      var35 = var35 * 59 + (var17 == null ? 43 : var17.hashCode());
      KList var18 = this.getJigsawStructures();
      var35 = var35 * 59 + (var18 == null ? 43 : var18.hashCode());
      KList var19 = this.getEffects();
      var35 = var35 * 59 + (var19 == null ? 43 : var19.hashCode());
      KList var20 = this.getEntitySpawners();
      var35 = var35 * 59 + (var20 == null ? 43 : var20.hashCode());
      KList var21 = this.getBlockDrops();
      var35 = var35 * 59 + (var21 == null ? 43 : var21.hashCode());
      KList var22 = this.getObjects();
      var35 = var35 * 59 + (var22 == null ? 43 : var22.hashCode());
      IrisLootReference var23 = this.getLoot();
      var35 = var35 * 59 + (var23 == null ? 43 : var23.hashCode());
      IrisCarving var24 = this.getCarving();
      var35 = var35 * 59 + (var24 == null ? 43 : var24.hashCode());
      IrisFluidBodies var25 = this.getFluidBodies();
      var35 = var35 * 59 + (var25 == null ? 43 : var25.hashCode());
      KList var26 = this.getLandBiomes();
      var35 = var35 * 59 + (var26 == null ? 43 : var26.hashCode());
      KList var27 = this.getSeaBiomes();
      var35 = var35 * 59 + (var27 == null ? 43 : var27.hashCode());
      KList var28 = this.getShoreBiomes();
      var35 = var35 * 59 + (var28 == null ? 43 : var28.hashCode());
      KList var29 = this.getCaveBiomes();
      var35 = var35 * 59 + (var29 == null ? 43 : var29.hashCode());
      KList var30 = this.getDeposits();
      var35 = var35 * 59 + (var30 == null ? 43 : var30.hashCode());
      IrisGeneratorStyle var31 = this.getRiverStyle();
      var35 = var35 * 59 + (var31 == null ? 43 : var31.hashCode());
      IrisGeneratorStyle var32 = this.getLakeStyle();
      var35 = var35 * 59 + (var32 == null ? 43 : var32.hashCode());
      String var33 = this.getColor();
      var35 = var35 * 59 + (var33 == null ? 43 : var33.hashCode());
      KList var34 = this.getOres();
      var35 = var35 * 59 + (var34 == null ? 43 : var34.hashCode());
      return var35;
   }
}
