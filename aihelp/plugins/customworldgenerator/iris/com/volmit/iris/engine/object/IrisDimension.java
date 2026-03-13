package com.volmit.iris.engine.object;

import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.core.ServerConfigurator;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.core.loader.IrisRegistrant;
import com.volmit.iris.core.nms.INMS;
import com.volmit.iris.core.nms.datapack.IDataFixer;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.RegistryListFunction;
import com.volmit.iris.engine.object.annotations.RegistryListResource;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.functions.ComponentFlagFunction;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.collection.KSet;
import com.volmit.iris.util.data.DataProvider;
import com.volmit.iris.util.io.IO;
import com.volmit.iris.util.json.JSONObject;
import com.volmit.iris.util.mantle.flag.MantleFlag;
import com.volmit.iris.util.math.Position2;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.noise.CNG;
import com.volmit.iris.util.plugin.VolmitSender;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Stream;
import lombok.Generated;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.data.BlockData;

@Desc("Represents a dimension")
public class IrisDimension extends IrisRegistrant {
   public static final BlockData STONE;
   public static final BlockData WATER;
   private final transient AtomicCache<Position2> parallaxSize = new AtomicCache();
   private final transient AtomicCache<CNG> rockLayerGenerator = new AtomicCache();
   private final transient AtomicCache<CNG> fluidLayerGenerator = new AtomicCache();
   private final transient AtomicCache<CNG> coordFracture = new AtomicCache();
   private final transient AtomicCache<Double> sinr = new AtomicCache();
   private final transient AtomicCache<Double> cosr = new AtomicCache();
   private final transient AtomicCache<Double> rad = new AtomicCache();
   private final transient AtomicCache<Boolean> featuresUsed = new AtomicCache();
   private final transient AtomicCache<KList<Position2>> strongholdsCache = new AtomicCache();
   private final transient AtomicCache<KMap<String, KList<String>>> cachedPreProcessors = new AtomicCache();
   @MinNumber(2.0D)
   @Required
   @Desc("The human readable name of this dimension")
   private String name = "A Dimension";
   @MinNumber(1.0D)
   @MaxNumber(2032.0D)
   @Desc("Maximum height at which players can be teleported to through gameplay.")
   private int logicalHeight = 256;
   @RegistryListResource(IrisJigsawStructure.class)
   @Desc("If defined, Iris will place the given jigsaw structure where minecraft should place the overworld stronghold.")
   private String stronghold;
   @Desc("If set to true, Iris will remove chunks to allow visualizing cross sections of chunks easily")
   private boolean debugChunkCrossSections = false;
   @Desc("Vertically split up the biome palettes with 3 air blocks in between to visualize them")
   private boolean explodeBiomePalettes = false;
   @Desc("Studio Mode for testing different parts of the world")
   private StudioMode studioMode;
   @MinNumber(1.0D)
   @MaxNumber(16.0D)
   @Desc("Customize the palette height explosion")
   private int explodeBiomePaletteSize;
   @MinNumber(2.0D)
   @MaxNumber(16.0D)
   @Desc("Every X/Z % debugCrossSectionsMod == 0 cuts the chunk")
   private int debugCrossSectionsMod;
   @Desc("The average distance between strongholds")
   private int strongholdJumpDistance;
   @Desc("Define the maximum strongholds to place")
   private int maxStrongholds;
   @Desc("Tree growth override settings")
   private IrisTreeSettings treeSettings;
   @Desc("Spawn Entities in this dimension over time. Iris will continually replenish these mobs just like vanilla does.")
   @ArrayType(
      min = 1,
      type = String.class
   )
   @RegistryListResource(IrisSpawner.class)
   private KList<String> entitySpawners;
   @Desc("Reference loot tables in this area")
   private IrisLootReference loot;
   @MinNumber(0.0D)
   @Desc("The version of this dimension. Changing this will stop users from accidentally upgrading (and breaking their worlds).")
   private int version;
   @ArrayType(
      min = 1,
      type = IrisBlockDrops.class
   )
   @Desc("Define custom block drops for this dimension")
   private KList<IrisBlockDrops> blockDrops;
   @Desc("Should bedrock be generated or not.")
   private boolean bedrock;
   @MinNumber(0.0D)
   @MaxNumber(1.0D)
   @Desc("The land chance. Up to 1.0 for total land or 0.0 for total sea")
   private double landChance;
   @Desc("The placement style of regions")
   private IrisGeneratorStyle regionStyle;
   @Desc("The placement style of land/sea")
   private IrisGeneratorStyle continentalStyle;
   @Desc("The placement style of biomes")
   private IrisGeneratorStyle landBiomeStyle;
   @Desc("The placement style of biomes")
   private IrisGeneratorStyle shoreBiomeStyle;
   @Desc("The placement style of biomes")
   private IrisGeneratorStyle seaBiomeStyle;
   @Desc("The placement style of biomes")
   private IrisGeneratorStyle caveBiomeStyle;
   @Desc("Instead of filling objects with air, fills them with cobweb so you can see them")
   private boolean debugSmartBore;
   @Desc("Generate decorations or not")
   private boolean decorate;
   @Desc("Use post processing or not")
   private boolean postProcessing;
   @Desc("Add slabs in post processing")
   private boolean postProcessingSlabs;
   @Desc("Add painted walls in post processing")
   private boolean postProcessingWalls;
   @Desc("Carving configuration for the dimension")
   private IrisCarving carving;
   @Desc("Configuration of fluid bodies such as rivers & lakes")
   private IrisFluidBodies fluidBodies;
   @Desc("forceConvertTo320Height")
   private Boolean forceConvertTo320Height;
   @Desc("The world environment")
   private Environment environment;
   @RegistryListResource(IrisRegion.class)
   @Required
   @ArrayType(
      min = 1,
      type = String.class
   )
   @Desc("Define all of the regions to include in this dimension. Dimensions -> Regions -> Biomes -> Objects etc")
   private KList<String> regions;
   @ArrayType(
      min = 1,
      type = IrisJigsawStructurePlacement.class
   )
   @Desc("Jigsaw structures")
   private KList<IrisJigsawStructurePlacement> jigsawStructures;
   @Desc("The jigsaw structure divisor to use when generating missing jigsaw placement values")
   private double jigsawStructureDivisor;
   @Required
   @MinNumber(0.0D)
   @MaxNumber(1024.0D)
   @Desc("The fluid height for this dimension")
   private int fluidHeight;
   @Desc("Define the min and max Y bounds of this dimension. Please keep in mind that Iris internally generates from 0 to (max - min). \n\nFor example at -64 to 320, Iris is internally generating to 0 to 384, then on outputting chunks, it shifts it down by the min height (64 blocks). The default is -64 to 320. \n\nThe fluid height is placed at (fluid height + min height). So a fluid height of 63 would actually show up in the world at 1.")
   private IrisRange dimensionHeight;
   @Desc("Define options for this dimension")
   private IrisDimensionTypeOptions dimensionOptions;
   @RegistryListResource(IrisBiome.class)
   @Desc("Keep this either undefined or empty. Setting any biome name into this will force iris to only generate the specified biome. Great for testing.")
   private String focus;
   @RegistryListResource(IrisRegion.class)
   @Desc("Keep this either undefined or empty. Setting any region name into this will force iris to only generate the specified region. Great for testing.")
   private String focusRegion;
   @MinNumber(1.0E-4D)
   @MaxNumber(512.0D)
   @Desc("Zoom in or out the biome size. Higher = bigger biomes")
   private double biomeZoom;
   @MinNumber(0.0D)
   @MaxNumber(360.0D)
   @Desc("You can rotate the input coordinates by an angle. This can make terrain appear more natural (less sharp corners and lines). This literally rotates the entire dimension by an angle. Hint: Try 12 degrees or something not on a 90 or 45 degree angle.")
   private double dimensionAngleDeg;
   @Required
   @Desc("Define the mode of this dimension (required!)")
   private IrisDimensionMode mode;
   @MinNumber(0.0D)
   @MaxNumber(8192.0D)
   @Desc("Coordinate fracturing applies noise to the input coordinates. This creates the 'iris swirls' and wavy features. The distance pushes these waves further into places they shouldnt be. This is a block value multiplier.")
   private double coordFractureDistance;
   @MinNumber(1.0E-4D)
   @MaxNumber(512.0D)
   @Desc("Coordinate fracturing zoom. Higher = less frequent warping, Lower = more frequent and rapid warping / swirls.")
   private double coordFractureZoom;
   @MinNumber(1.0E-4D)
   @MaxNumber(512.0D)
   @Desc("This zooms in the land space")
   private double landZoom;
   @MinNumber(1.0E-4D)
   @MaxNumber(512.0D)
   @Desc("This zooms oceanic biomes")
   private double seaZoom;
   @MinNumber(1.0E-4D)
   @MaxNumber(512.0D)
   @Desc("Zoom in continents")
   private double continentZoom;
   @MinNumber(1.0E-4D)
   @MaxNumber(512.0D)
   @Desc("Change the size of regions")
   private double regionZoom;
   @Desc("Disable this to stop placing objects, entities, features & updates")
   private boolean useMantle;
   @Desc("Prevent Leaf decay as if placed in creative mode")
   private boolean preventLeafDecay;
   @ArrayType(
      min = 1,
      type = IrisDepositGenerator.class
   )
   @Desc("Define global deposit generators")
   private KList<IrisDepositGenerator> deposits;
   @ArrayType(
      min = 1,
      type = IrisShapedGeneratorStyle.class
   )
   @Desc("Overlay additional noise on top of the interoplated terrain.")
   private KList<IrisShapedGeneratorStyle> overlayNoise;
   @Desc("If true, the spawner system has infinite energy. This is NOT recommended because it would allow for mobs to keep spawning over and over without a rate limit")
   private boolean infiniteEnergy;
   @MinNumber(0.0D)
   @MaxNumber(10000.0D)
   @Desc("This is the maximum energy you can have in a dimension")
   private double maximumEnergy;
   @MinNumber(1.0E-4D)
   @MaxNumber(512.0D)
   @Desc("The rock zoom mostly for zooming in on a wispy palette")
   private double rockZoom;
   @Desc("The palette of blocks for 'stone'")
   private IrisMaterialPalette rockPalette;
   @Desc("The palette of blocks for 'water'")
   private IrisMaterialPalette fluidPalette;
   @Desc("Prevent cartographers to generate explorer maps (Iris worlds only)\nONLY TOUCH IF YOUR SERVER CRASHES WHILE GENERATING EXPLORER MAPS")
   private boolean disableExplorerMaps;
   @Desc("Collection of ores to be generated")
   @ArrayType(
      type = IrisOreGenerator.class,
      min = 1
   )
   private KList<IrisOreGenerator> ores;
   @MinNumber(0.0D)
   @MaxNumber(318.0D)
   @Desc("The Subterrain Fluid Layer Height")
   private int caveLavaHeight;
   @RegistryListFunction(ComponentFlagFunction.class)
   @ArrayType(
      type = String.class
   )
   @Desc("Collection of disabled components")
   private KList<MantleFlag> disabledComponents;
   @Desc("A list of globally applied pre-processors")
   @ArrayType(
      type = IrisPreProcessors.class
   )
   private KList<IrisPreProcessors> globalPreProcessors;
   @Desc("A list of scripts executed on engine setup\nFile extension: .engine.kts")
   @RegistryListResource(IrisScript.class)
   @ArrayType(
      type = String.class,
      min = 1
   )
   private KList<String> engineScripts;
   @Desc("A list of scripts executed on data setup\nFile extension: .data.kts")
   @RegistryListResource(IrisScript.class)
   @ArrayType(
      type = String.class,
      min = 1
   )
   private KList<String> dataScripts;
   @Desc("A list of scripts executed on chunk update\nFile extension: .update.kts")
   @RegistryListResource(IrisScript.class)
   @ArrayType(
      type = String.class,
      min = 1
   )
   private KList<String> chunkUpdateScripts;
   @Desc("Use legacy rarity instead of modern one\nWARNING: Changing this may break expressions and image maps")
   private boolean legacyRarity;

   public int getMaxHeight() {
      return (int)this.getDimensionHeight().getMax();
   }

   public int getMinHeight() {
      return (int)this.getDimensionHeight().getMin();
   }

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

   public KList<Position2> getStrongholds(long seed) {
      return (KList)this.strongholdsCache.aquire(() -> {
         KList var3 = new KList();
         int var4 = this.strongholdJumpDistance;
         RNG var5 = new RNG(var1 * 223L + 12945L);

         for(int var6 = 0; var6 < this.maxStrongholds + 1; ++var6) {
            int var7 = var6 + 1;
            var3.add((Object)(new Position2((int)((double)(var5.i(var4 * var6) + var4 * var6) * (var5.b() ? -1.0D : 1.0D)), (int)((double)(var5.i(var4 * var6) + var4 * var6) * (var5.b() ? -1.0D : 1.0D)))));
         }

         var3.remove(0);
         return var3;
      });
   }

   public int getFluidHeight() {
      return this.fluidHeight - (int)this.dimensionHeight.getMin();
   }

   public CNG getCoordFracture(RNG rng, int signature) {
      return (CNG)this.coordFracture.aquire(() -> {
         CNG var3 = CNG.signature(var1.nextParallelRNG(var2));
         var3.scale(0.012D / this.coordFractureZoom);
         return var3;
      });
   }

   public double getDimensionAngle() {
      return (Double)this.rad.aquire(() -> {
         return Math.toRadians(this.dimensionAngleDeg);
      });
   }

   public Environment getEnvironment() {
      return this.environment;
   }

   public boolean hasFocusRegion() {
      return !this.focusRegion.equals("");
   }

   public String getFocusRegion() {
      return this.focusRegion;
   }

   public double sinRotate() {
      return (Double)this.sinr.aquire(() -> {
         return Math.sin(this.getDimensionAngle());
      });
   }

   public double cosRotate() {
      return (Double)this.cosr.aquire(() -> {
         return Math.cos(this.getDimensionAngle());
      });
   }

   public KList<IrisRegion> getAllRegions(DataProvider g) {
      KList var2 = new KList();
      Iterator var3 = this.getRegions().iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         var2.add((Object)((IrisRegion)var1.getData().getRegionLoader().load(var4)));
      }

      return var2;
   }

   public KList<IrisRegion> getAllAnyRegions() {
      KList var1 = new KList();
      Iterator var2 = this.getRegions().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         var1.add((Object)IrisData.loadAnyRegion(var3, this.getLoader()));
      }

      return var1;
   }

   public KList<IrisBiome> getAllBiomes(DataProvider g) {
      return var1.getData().getBiomeLoader().loadAll(var1.getData().getBiomeLoader().getPossibleKeys());
   }

   public KList<IrisBiome> getAllAnyBiomes() {
      KList var1 = new KList();
      Iterator var2 = this.getAllAnyRegions().iterator();

      while(var2.hasNext()) {
         IrisRegion var3 = (IrisRegion)var2.next();
         if (var3 != null) {
            var1.addAll(var3.getAllAnyBiomes());
         }
      }

      return var1;
   }

   public KList<String> getPreProcessors(String type) {
      return (KList)((KMap)this.cachedPreProcessors.aquire(() -> {
         KMap var1 = new KMap();
         Iterator var2 = this.globalPreProcessors.iterator();

         while(var2.hasNext()) {
            IrisPreProcessors var3 = (IrisPreProcessors)var2.next();
            ((KList)var1.computeIfAbsent(var3.getType(), (var0) -> {
               return new KList();
            })).addAll(var3.getScripts());
         }

         return var1;
      })).get(var1);
   }

   public IrisGeneratorStyle getBiomeStyle(InferredType type) {
      switch(var1) {
      case CAVE:
         return this.caveBiomeStyle;
      case LAND:
         return this.landBiomeStyle;
      case SEA:
         return this.seaBiomeStyle;
      case SHORE:
         return this.shoreBiomeStyle;
      default:
         return this.landBiomeStyle;
      }
   }

   public void installBiomes(IDataFixer fixer, DataProvider data, KList<File> folders, KSet<String> biomes) {
      ((Stream)this.getAllBiomes(var2).stream().filter(IrisBiome::isCustom).map(IrisBiome::getCustomDerivitives).flatMap(Collection::stream).parallel()).forEach((var4x) -> {
         String var5 = var4x.generateJson(var1);
         synchronized(var4) {
            if (!var4.add(var4x.getId())) {
               String var10000 = this.getLoadKey();
               Iris.verbose("Duplicate Data Pack Biome: " + var10000 + "/" + var4x.getId());
               return;
            }
         }

         Iterator var6 = var3.iterator();

         while(var6.hasNext()) {
            File var7 = (File)var6.next();
            String var10003 = this.getLoadKey().toLowerCase();
            File var8 = new File(var7, "iris/data/" + var10003 + "/worldgen/biome/" + var4x.getId() + ".json");
            Iris.verbose("    Installing Data Pack Biome: " + var8.getPath());
            var8.getParentFile().mkdirs();

            try {
               IO.writeAll(var8, (Object)var5);
            } catch (IOException var10) {
               Iris.reportError(var10);
               var10.printStackTrace();
            }
         }

      });
   }

   public IDataFixer.Dimension getBaseDimension() {
      IDataFixer.Dimension var10000;
      switch(this.getEnvironment()) {
      case NETHER:
         var10000 = IDataFixer.Dimension.NETHER;
         break;
      case THE_END:
         var10000 = IDataFixer.Dimension.END;
         break;
      default:
         var10000 = IDataFixer.Dimension.OVERWORLD;
      }

      return var10000;
   }

   public String getDimensionTypeKey() {
      return this.getDimensionType().key();
   }

   public IrisDimensionType getDimensionType() {
      return new IrisDimensionType(this.getBaseDimension(), this.getDimensionOptions(), this.getLogicalHeight(), this.getMaxHeight() - this.getMinHeight(), this.getMinHeight());
   }

   public void installDimensionType(IDataFixer fixer, KList<File> folders) {
      IrisDimensionType var3 = this.getDimensionType();
      String var4 = var3.toJson(var1);
      Iris.verbose("    Installing Data Pack Dimension Type: \"iris:" + var3.key() + "\"");
      Iterator var5 = var2.iterator();

      while(var5.hasNext()) {
         File var6 = (File)var5.next();
         File var7 = new File(var6, "iris/data/iris/dimension_type/" + var3.key() + ".json");
         var7.getParentFile().mkdirs();

         try {
            IO.writeAll(var7, (Object)var4);
         } catch (IOException var9) {
            Iris.reportError(var9);
            var9.printStackTrace();
         }
      }

   }

   public String getFolderName() {
      return "dimensions";
   }

   public String getTypeName() {
      return "Dimension";
   }

   public void scanForErrors(JSONObject p, VolmitSender sender) {
   }

   public static void writeShared(KList<File> folders, ServerConfigurator.DimensionHeight height) {
      Iris.verbose("    Installing Data Pack Vanilla Dimension Types");
      String[] var2 = var1.jsonStrings();
      Iterator var3 = var0.iterator();

      while(var3.hasNext()) {
         File var4 = (File)var3.next();
         write(var4, "overworld", var2[0]);
         write(var4, "the_nether", var2[1]);
         write(var4, "the_end", var2[2]);
      }

      String var9 = "{\n    \"pack\": {\n        \"description\": \"Iris Data Pack. This pack contains all installed Iris Packs' resources.\",\n        \"pack_format\": {}\n    }\n}\n".replace("{}", INMS.get().getDataVersion().getPackFormat().makeConcatWithConstants<invokedynamic>(INMS.get().getDataVersion().getPackFormat()));

      File var6;
      for(Iterator var10 = var0.iterator(); var10.hasNext(); Iris.verbose("    Installing Data Pack MCMeta: " + var6.getPath())) {
         File var5 = (File)var10.next();
         var6 = new File(var5, "iris/pack.mcmeta");

         try {
            IO.writeAll(var6, (Object)var9);
         } catch (IOException var8) {
            Iris.reportError(var8);
            var8.printStackTrace();
         }
      }

   }

   private static void write(File datapacks, String type, String json) {
      if (var2 != null) {
         File var3 = new File(var0, "iris/data/minecraft/dimension_type/" + var1 + ".json");
         if (IrisSettings.get().getGeneral().adjustVanillaHeight || var3.exists()) {
            var3.getParentFile().mkdirs();

            try {
               IO.writeAll(var3, (Object)var2);
            } catch (IOException var5) {
               Iris.reportError(var5);
               var5.printStackTrace();
            }
         }

      }
   }

   @Generated
   public IrisDimension(final String name, final int logicalHeight, final String stronghold, final boolean debugChunkCrossSections, final boolean explodeBiomePalettes, final StudioMode studioMode, final int explodeBiomePaletteSize, final int debugCrossSectionsMod, final int strongholdJumpDistance, final int maxStrongholds, final IrisTreeSettings treeSettings, final KList<String> entitySpawners, final IrisLootReference loot, final int version, final KList<IrisBlockDrops> blockDrops, final boolean bedrock, final double landChance, final IrisGeneratorStyle regionStyle, final IrisGeneratorStyle continentalStyle, final IrisGeneratorStyle landBiomeStyle, final IrisGeneratorStyle shoreBiomeStyle, final IrisGeneratorStyle seaBiomeStyle, final IrisGeneratorStyle caveBiomeStyle, final boolean debugSmartBore, final boolean decorate, final boolean postProcessing, final boolean postProcessingSlabs, final boolean postProcessingWalls, final IrisCarving carving, final IrisFluidBodies fluidBodies, final Boolean forceConvertTo320Height, final Environment environment, final KList<String> regions, final KList<IrisJigsawStructurePlacement> jigsawStructures, final double jigsawStructureDivisor, final int fluidHeight, final IrisRange dimensionHeight, final IrisDimensionTypeOptions dimensionOptions, final String focus, final String focusRegion, final double biomeZoom, final double dimensionAngleDeg, final IrisDimensionMode mode, final double coordFractureDistance, final double coordFractureZoom, final double landZoom, final double seaZoom, final double continentZoom, final double regionZoom, final boolean useMantle, final boolean preventLeafDecay, final KList<IrisDepositGenerator> deposits, final KList<IrisShapedGeneratorStyle> overlayNoise, final boolean infiniteEnergy, final double maximumEnergy, final double rockZoom, final IrisMaterialPalette rockPalette, final IrisMaterialPalette fluidPalette, final boolean disableExplorerMaps, final KList<IrisOreGenerator> ores, final int caveLavaHeight, final KList<MantleFlag> disabledComponents, final KList<IrisPreProcessors> globalPreProcessors, final KList<String> engineScripts, final KList<String> dataScripts, final KList<String> chunkUpdateScripts, final boolean legacyRarity) {
      this.studioMode = StudioMode.NORMAL;
      this.explodeBiomePaletteSize = 3;
      this.debugCrossSectionsMod = 3;
      this.strongholdJumpDistance = 1280;
      this.maxStrongholds = 14;
      this.treeSettings = new IrisTreeSettings();
      this.entitySpawners = new KList();
      this.loot = new IrisLootReference();
      this.version = 1;
      this.blockDrops = new KList();
      this.bedrock = true;
      this.landChance = 0.625D;
      this.regionStyle = NoiseStyle.CELLULAR_IRIS_DOUBLE.style();
      this.continentalStyle = NoiseStyle.CELLULAR_IRIS_DOUBLE.style();
      this.landBiomeStyle = NoiseStyle.CELLULAR_IRIS_DOUBLE.style();
      this.shoreBiomeStyle = NoiseStyle.CELLULAR_IRIS_DOUBLE.style();
      this.seaBiomeStyle = NoiseStyle.CELLULAR_IRIS_DOUBLE.style();
      this.caveBiomeStyle = NoiseStyle.CELLULAR_IRIS_DOUBLE.style();
      this.debugSmartBore = false;
      this.decorate = true;
      this.postProcessing = true;
      this.postProcessingSlabs = true;
      this.postProcessingWalls = true;
      this.carving = new IrisCarving();
      this.fluidBodies = new IrisFluidBodies();
      this.forceConvertTo320Height = false;
      this.environment = Environment.NORMAL;
      this.regions = new KList();
      this.jigsawStructures = new KList();
      this.jigsawStructureDivisor = 18.0D;
      this.fluidHeight = 63;
      this.dimensionHeight = new IrisRange(-64.0D, 320.0D);
      this.dimensionOptions = new IrisDimensionTypeOptions();
      this.focus = "";
      this.focusRegion = "";
      this.biomeZoom = 1.0D;
      this.dimensionAngleDeg = 0.0D;
      this.mode = new IrisDimensionMode();
      this.coordFractureDistance = 20.0D;
      this.coordFractureZoom = 8.0D;
      this.landZoom = 1.0D;
      this.seaZoom = 1.0D;
      this.continentZoom = 1.0D;
      this.regionZoom = 1.0D;
      this.useMantle = true;
      this.preventLeafDecay = false;
      this.deposits = new KList();
      this.overlayNoise = new KList();
      this.infiniteEnergy = false;
      this.maximumEnergy = 1000.0D;
      this.rockZoom = 5.0D;
      this.rockPalette = (new IrisMaterialPalette()).qclear().qadd("stone");
      this.fluidPalette = (new IrisMaterialPalette()).qclear().qadd("water");
      this.disableExplorerMaps = false;
      this.ores = new KList();
      this.caveLavaHeight = 8;
      this.disabledComponents = new KList();
      this.globalPreProcessors = new KList();
      this.engineScripts = new KList();
      this.dataScripts = new KList();
      this.chunkUpdateScripts = new KList();
      this.legacyRarity = true;
      this.name = var1;
      this.logicalHeight = var2;
      this.stronghold = var3;
      this.debugChunkCrossSections = var4;
      this.explodeBiomePalettes = var5;
      this.studioMode = var6;
      this.explodeBiomePaletteSize = var7;
      this.debugCrossSectionsMod = var8;
      this.strongholdJumpDistance = var9;
      this.maxStrongholds = var10;
      this.treeSettings = var11;
      this.entitySpawners = var12;
      this.loot = var13;
      this.version = var14;
      this.blockDrops = var15;
      this.bedrock = var16;
      this.landChance = var17;
      this.regionStyle = var19;
      this.continentalStyle = var20;
      this.landBiomeStyle = var21;
      this.shoreBiomeStyle = var22;
      this.seaBiomeStyle = var23;
      this.caveBiomeStyle = var24;
      this.debugSmartBore = var25;
      this.decorate = var26;
      this.postProcessing = var27;
      this.postProcessingSlabs = var28;
      this.postProcessingWalls = var29;
      this.carving = var30;
      this.fluidBodies = var31;
      this.forceConvertTo320Height = var32;
      this.environment = var33;
      this.regions = var34;
      this.jigsawStructures = var35;
      this.jigsawStructureDivisor = var36;
      this.fluidHeight = var38;
      this.dimensionHeight = var39;
      this.dimensionOptions = var40;
      this.focus = var41;
      this.focusRegion = var42;
      this.biomeZoom = var43;
      this.dimensionAngleDeg = var45;
      this.mode = var47;
      this.coordFractureDistance = var48;
      this.coordFractureZoom = var50;
      this.landZoom = var52;
      this.seaZoom = var54;
      this.continentZoom = var56;
      this.regionZoom = var58;
      this.useMantle = var60;
      this.preventLeafDecay = var61;
      this.deposits = var62;
      this.overlayNoise = var63;
      this.infiniteEnergy = var64;
      this.maximumEnergy = var65;
      this.rockZoom = var67;
      this.rockPalette = var69;
      this.fluidPalette = var70;
      this.disableExplorerMaps = var71;
      this.ores = var72;
      this.caveLavaHeight = var73;
      this.disabledComponents = var74;
      this.globalPreProcessors = var75;
      this.engineScripts = var76;
      this.dataScripts = var77;
      this.chunkUpdateScripts = var78;
      this.legacyRarity = var79;
   }

   @Generated
   public IrisDimension() {
      this.studioMode = StudioMode.NORMAL;
      this.explodeBiomePaletteSize = 3;
      this.debugCrossSectionsMod = 3;
      this.strongholdJumpDistance = 1280;
      this.maxStrongholds = 14;
      this.treeSettings = new IrisTreeSettings();
      this.entitySpawners = new KList();
      this.loot = new IrisLootReference();
      this.version = 1;
      this.blockDrops = new KList();
      this.bedrock = true;
      this.landChance = 0.625D;
      this.regionStyle = NoiseStyle.CELLULAR_IRIS_DOUBLE.style();
      this.continentalStyle = NoiseStyle.CELLULAR_IRIS_DOUBLE.style();
      this.landBiomeStyle = NoiseStyle.CELLULAR_IRIS_DOUBLE.style();
      this.shoreBiomeStyle = NoiseStyle.CELLULAR_IRIS_DOUBLE.style();
      this.seaBiomeStyle = NoiseStyle.CELLULAR_IRIS_DOUBLE.style();
      this.caveBiomeStyle = NoiseStyle.CELLULAR_IRIS_DOUBLE.style();
      this.debugSmartBore = false;
      this.decorate = true;
      this.postProcessing = true;
      this.postProcessingSlabs = true;
      this.postProcessingWalls = true;
      this.carving = new IrisCarving();
      this.fluidBodies = new IrisFluidBodies();
      this.forceConvertTo320Height = false;
      this.environment = Environment.NORMAL;
      this.regions = new KList();
      this.jigsawStructures = new KList();
      this.jigsawStructureDivisor = 18.0D;
      this.fluidHeight = 63;
      this.dimensionHeight = new IrisRange(-64.0D, 320.0D);
      this.dimensionOptions = new IrisDimensionTypeOptions();
      this.focus = "";
      this.focusRegion = "";
      this.biomeZoom = 1.0D;
      this.dimensionAngleDeg = 0.0D;
      this.mode = new IrisDimensionMode();
      this.coordFractureDistance = 20.0D;
      this.coordFractureZoom = 8.0D;
      this.landZoom = 1.0D;
      this.seaZoom = 1.0D;
      this.continentZoom = 1.0D;
      this.regionZoom = 1.0D;
      this.useMantle = true;
      this.preventLeafDecay = false;
      this.deposits = new KList();
      this.overlayNoise = new KList();
      this.infiniteEnergy = false;
      this.maximumEnergy = 1000.0D;
      this.rockZoom = 5.0D;
      this.rockPalette = (new IrisMaterialPalette()).qclear().qadd("stone");
      this.fluidPalette = (new IrisMaterialPalette()).qclear().qadd("water");
      this.disableExplorerMaps = false;
      this.ores = new KList();
      this.caveLavaHeight = 8;
      this.disabledComponents = new KList();
      this.globalPreProcessors = new KList();
      this.engineScripts = new KList();
      this.dataScripts = new KList();
      this.chunkUpdateScripts = new KList();
      this.legacyRarity = true;
   }

   @Generated
   public AtomicCache<Position2> getParallaxSize() {
      return this.parallaxSize;
   }

   @Generated
   public AtomicCache<CNG> getRockLayerGenerator() {
      return this.rockLayerGenerator;
   }

   @Generated
   public AtomicCache<CNG> getFluidLayerGenerator() {
      return this.fluidLayerGenerator;
   }

   @Generated
   public AtomicCache<CNG> getCoordFracture() {
      return this.coordFracture;
   }

   @Generated
   public AtomicCache<Double> getSinr() {
      return this.sinr;
   }

   @Generated
   public AtomicCache<Double> getCosr() {
      return this.cosr;
   }

   @Generated
   public AtomicCache<Double> getRad() {
      return this.rad;
   }

   @Generated
   public AtomicCache<Boolean> getFeaturesUsed() {
      return this.featuresUsed;
   }

   @Generated
   public AtomicCache<KList<Position2>> getStrongholdsCache() {
      return this.strongholdsCache;
   }

   @Generated
   public AtomicCache<KMap<String, KList<String>>> getCachedPreProcessors() {
      return this.cachedPreProcessors;
   }

   @Generated
   public String getName() {
      return this.name;
   }

   @Generated
   public int getLogicalHeight() {
      return this.logicalHeight;
   }

   @Generated
   public String getStronghold() {
      return this.stronghold;
   }

   @Generated
   public boolean isDebugChunkCrossSections() {
      return this.debugChunkCrossSections;
   }

   @Generated
   public boolean isExplodeBiomePalettes() {
      return this.explodeBiomePalettes;
   }

   @Generated
   public StudioMode getStudioMode() {
      return this.studioMode;
   }

   @Generated
   public int getExplodeBiomePaletteSize() {
      return this.explodeBiomePaletteSize;
   }

   @Generated
   public int getDebugCrossSectionsMod() {
      return this.debugCrossSectionsMod;
   }

   @Generated
   public int getStrongholdJumpDistance() {
      return this.strongholdJumpDistance;
   }

   @Generated
   public int getMaxStrongholds() {
      return this.maxStrongholds;
   }

   @Generated
   public IrisTreeSettings getTreeSettings() {
      return this.treeSettings;
   }

   @Generated
   public KList<String> getEntitySpawners() {
      return this.entitySpawners;
   }

   @Generated
   public IrisLootReference getLoot() {
      return this.loot;
   }

   @Generated
   public int getVersion() {
      return this.version;
   }

   @Generated
   public KList<IrisBlockDrops> getBlockDrops() {
      return this.blockDrops;
   }

   @Generated
   public boolean isBedrock() {
      return this.bedrock;
   }

   @Generated
   public double getLandChance() {
      return this.landChance;
   }

   @Generated
   public IrisGeneratorStyle getRegionStyle() {
      return this.regionStyle;
   }

   @Generated
   public IrisGeneratorStyle getContinentalStyle() {
      return this.continentalStyle;
   }

   @Generated
   public IrisGeneratorStyle getLandBiomeStyle() {
      return this.landBiomeStyle;
   }

   @Generated
   public IrisGeneratorStyle getShoreBiomeStyle() {
      return this.shoreBiomeStyle;
   }

   @Generated
   public IrisGeneratorStyle getSeaBiomeStyle() {
      return this.seaBiomeStyle;
   }

   @Generated
   public IrisGeneratorStyle getCaveBiomeStyle() {
      return this.caveBiomeStyle;
   }

   @Generated
   public boolean isDebugSmartBore() {
      return this.debugSmartBore;
   }

   @Generated
   public boolean isDecorate() {
      return this.decorate;
   }

   @Generated
   public boolean isPostProcessing() {
      return this.postProcessing;
   }

   @Generated
   public boolean isPostProcessingSlabs() {
      return this.postProcessingSlabs;
   }

   @Generated
   public boolean isPostProcessingWalls() {
      return this.postProcessingWalls;
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
   public Boolean getForceConvertTo320Height() {
      return this.forceConvertTo320Height;
   }

   @Generated
   public KList<String> getRegions() {
      return this.regions;
   }

   @Generated
   public KList<IrisJigsawStructurePlacement> getJigsawStructures() {
      return this.jigsawStructures;
   }

   @Generated
   public double getJigsawStructureDivisor() {
      return this.jigsawStructureDivisor;
   }

   @Generated
   public IrisRange getDimensionHeight() {
      return this.dimensionHeight;
   }

   @Generated
   public IrisDimensionTypeOptions getDimensionOptions() {
      return this.dimensionOptions;
   }

   @Generated
   public String getFocus() {
      return this.focus;
   }

   @Generated
   public double getBiomeZoom() {
      return this.biomeZoom;
   }

   @Generated
   public double getDimensionAngleDeg() {
      return this.dimensionAngleDeg;
   }

   @Generated
   public IrisDimensionMode getMode() {
      return this.mode;
   }

   @Generated
   public double getCoordFractureDistance() {
      return this.coordFractureDistance;
   }

   @Generated
   public double getCoordFractureZoom() {
      return this.coordFractureZoom;
   }

   @Generated
   public double getLandZoom() {
      return this.landZoom;
   }

   @Generated
   public double getSeaZoom() {
      return this.seaZoom;
   }

   @Generated
   public double getContinentZoom() {
      return this.continentZoom;
   }

   @Generated
   public double getRegionZoom() {
      return this.regionZoom;
   }

   @Generated
   public boolean isUseMantle() {
      return this.useMantle;
   }

   @Generated
   public boolean isPreventLeafDecay() {
      return this.preventLeafDecay;
   }

   @Generated
   public KList<IrisDepositGenerator> getDeposits() {
      return this.deposits;
   }

   @Generated
   public KList<IrisShapedGeneratorStyle> getOverlayNoise() {
      return this.overlayNoise;
   }

   @Generated
   public boolean isInfiniteEnergy() {
      return this.infiniteEnergy;
   }

   @Generated
   public double getMaximumEnergy() {
      return this.maximumEnergy;
   }

   @Generated
   public double getRockZoom() {
      return this.rockZoom;
   }

   @Generated
   public IrisMaterialPalette getRockPalette() {
      return this.rockPalette;
   }

   @Generated
   public IrisMaterialPalette getFluidPalette() {
      return this.fluidPalette;
   }

   @Generated
   public boolean isDisableExplorerMaps() {
      return this.disableExplorerMaps;
   }

   @Generated
   public KList<IrisOreGenerator> getOres() {
      return this.ores;
   }

   @Generated
   public int getCaveLavaHeight() {
      return this.caveLavaHeight;
   }

   @Generated
   public KList<MantleFlag> getDisabledComponents() {
      return this.disabledComponents;
   }

   @Generated
   public KList<IrisPreProcessors> getGlobalPreProcessors() {
      return this.globalPreProcessors;
   }

   @Generated
   public KList<String> getEngineScripts() {
      return this.engineScripts;
   }

   @Generated
   public KList<String> getDataScripts() {
      return this.dataScripts;
   }

   @Generated
   public KList<String> getChunkUpdateScripts() {
      return this.chunkUpdateScripts;
   }

   @Generated
   public boolean isLegacyRarity() {
      return this.legacyRarity;
   }

   @Generated
   public IrisDimension setName(final String name) {
      this.name = var1;
      return this;
   }

   @Generated
   public IrisDimension setLogicalHeight(final int logicalHeight) {
      this.logicalHeight = var1;
      return this;
   }

   @Generated
   public IrisDimension setStronghold(final String stronghold) {
      this.stronghold = var1;
      return this;
   }

   @Generated
   public IrisDimension setDebugChunkCrossSections(final boolean debugChunkCrossSections) {
      this.debugChunkCrossSections = var1;
      return this;
   }

   @Generated
   public IrisDimension setExplodeBiomePalettes(final boolean explodeBiomePalettes) {
      this.explodeBiomePalettes = var1;
      return this;
   }

   @Generated
   public IrisDimension setStudioMode(final StudioMode studioMode) {
      this.studioMode = var1;
      return this;
   }

   @Generated
   public IrisDimension setExplodeBiomePaletteSize(final int explodeBiomePaletteSize) {
      this.explodeBiomePaletteSize = var1;
      return this;
   }

   @Generated
   public IrisDimension setDebugCrossSectionsMod(final int debugCrossSectionsMod) {
      this.debugCrossSectionsMod = var1;
      return this;
   }

   @Generated
   public IrisDimension setStrongholdJumpDistance(final int strongholdJumpDistance) {
      this.strongholdJumpDistance = var1;
      return this;
   }

   @Generated
   public IrisDimension setMaxStrongholds(final int maxStrongholds) {
      this.maxStrongholds = var1;
      return this;
   }

   @Generated
   public IrisDimension setTreeSettings(final IrisTreeSettings treeSettings) {
      this.treeSettings = var1;
      return this;
   }

   @Generated
   public IrisDimension setEntitySpawners(final KList<String> entitySpawners) {
      this.entitySpawners = var1;
      return this;
   }

   @Generated
   public IrisDimension setLoot(final IrisLootReference loot) {
      this.loot = var1;
      return this;
   }

   @Generated
   public IrisDimension setVersion(final int version) {
      this.version = var1;
      return this;
   }

   @Generated
   public IrisDimension setBlockDrops(final KList<IrisBlockDrops> blockDrops) {
      this.blockDrops = var1;
      return this;
   }

   @Generated
   public IrisDimension setBedrock(final boolean bedrock) {
      this.bedrock = var1;
      return this;
   }

   @Generated
   public IrisDimension setLandChance(final double landChance) {
      this.landChance = var1;
      return this;
   }

   @Generated
   public IrisDimension setRegionStyle(final IrisGeneratorStyle regionStyle) {
      this.regionStyle = var1;
      return this;
   }

   @Generated
   public IrisDimension setContinentalStyle(final IrisGeneratorStyle continentalStyle) {
      this.continentalStyle = var1;
      return this;
   }

   @Generated
   public IrisDimension setLandBiomeStyle(final IrisGeneratorStyle landBiomeStyle) {
      this.landBiomeStyle = var1;
      return this;
   }

   @Generated
   public IrisDimension setShoreBiomeStyle(final IrisGeneratorStyle shoreBiomeStyle) {
      this.shoreBiomeStyle = var1;
      return this;
   }

   @Generated
   public IrisDimension setSeaBiomeStyle(final IrisGeneratorStyle seaBiomeStyle) {
      this.seaBiomeStyle = var1;
      return this;
   }

   @Generated
   public IrisDimension setCaveBiomeStyle(final IrisGeneratorStyle caveBiomeStyle) {
      this.caveBiomeStyle = var1;
      return this;
   }

   @Generated
   public IrisDimension setDebugSmartBore(final boolean debugSmartBore) {
      this.debugSmartBore = var1;
      return this;
   }

   @Generated
   public IrisDimension setDecorate(final boolean decorate) {
      this.decorate = var1;
      return this;
   }

   @Generated
   public IrisDimension setPostProcessing(final boolean postProcessing) {
      this.postProcessing = var1;
      return this;
   }

   @Generated
   public IrisDimension setPostProcessingSlabs(final boolean postProcessingSlabs) {
      this.postProcessingSlabs = var1;
      return this;
   }

   @Generated
   public IrisDimension setPostProcessingWalls(final boolean postProcessingWalls) {
      this.postProcessingWalls = var1;
      return this;
   }

   @Generated
   public IrisDimension setCarving(final IrisCarving carving) {
      this.carving = var1;
      return this;
   }

   @Generated
   public IrisDimension setFluidBodies(final IrisFluidBodies fluidBodies) {
      this.fluidBodies = var1;
      return this;
   }

   @Generated
   public IrisDimension setForceConvertTo320Height(final Boolean forceConvertTo320Height) {
      this.forceConvertTo320Height = var1;
      return this;
   }

   @Generated
   public IrisDimension setEnvironment(final Environment environment) {
      this.environment = var1;
      return this;
   }

   @Generated
   public IrisDimension setRegions(final KList<String> regions) {
      this.regions = var1;
      return this;
   }

   @Generated
   public IrisDimension setJigsawStructures(final KList<IrisJigsawStructurePlacement> jigsawStructures) {
      this.jigsawStructures = var1;
      return this;
   }

   @Generated
   public IrisDimension setJigsawStructureDivisor(final double jigsawStructureDivisor) {
      this.jigsawStructureDivisor = var1;
      return this;
   }

   @Generated
   public IrisDimension setFluidHeight(final int fluidHeight) {
      this.fluidHeight = var1;
      return this;
   }

   @Generated
   public IrisDimension setDimensionHeight(final IrisRange dimensionHeight) {
      this.dimensionHeight = var1;
      return this;
   }

   @Generated
   public IrisDimension setDimensionOptions(final IrisDimensionTypeOptions dimensionOptions) {
      this.dimensionOptions = var1;
      return this;
   }

   @Generated
   public IrisDimension setFocus(final String focus) {
      this.focus = var1;
      return this;
   }

   @Generated
   public IrisDimension setFocusRegion(final String focusRegion) {
      this.focusRegion = var1;
      return this;
   }

   @Generated
   public IrisDimension setBiomeZoom(final double biomeZoom) {
      this.biomeZoom = var1;
      return this;
   }

   @Generated
   public IrisDimension setDimensionAngleDeg(final double dimensionAngleDeg) {
      this.dimensionAngleDeg = var1;
      return this;
   }

   @Generated
   public IrisDimension setMode(final IrisDimensionMode mode) {
      this.mode = var1;
      return this;
   }

   @Generated
   public IrisDimension setCoordFractureDistance(final double coordFractureDistance) {
      this.coordFractureDistance = var1;
      return this;
   }

   @Generated
   public IrisDimension setCoordFractureZoom(final double coordFractureZoom) {
      this.coordFractureZoom = var1;
      return this;
   }

   @Generated
   public IrisDimension setLandZoom(final double landZoom) {
      this.landZoom = var1;
      return this;
   }

   @Generated
   public IrisDimension setSeaZoom(final double seaZoom) {
      this.seaZoom = var1;
      return this;
   }

   @Generated
   public IrisDimension setContinentZoom(final double continentZoom) {
      this.continentZoom = var1;
      return this;
   }

   @Generated
   public IrisDimension setRegionZoom(final double regionZoom) {
      this.regionZoom = var1;
      return this;
   }

   @Generated
   public IrisDimension setUseMantle(final boolean useMantle) {
      this.useMantle = var1;
      return this;
   }

   @Generated
   public IrisDimension setPreventLeafDecay(final boolean preventLeafDecay) {
      this.preventLeafDecay = var1;
      return this;
   }

   @Generated
   public IrisDimension setDeposits(final KList<IrisDepositGenerator> deposits) {
      this.deposits = var1;
      return this;
   }

   @Generated
   public IrisDimension setOverlayNoise(final KList<IrisShapedGeneratorStyle> overlayNoise) {
      this.overlayNoise = var1;
      return this;
   }

   @Generated
   public IrisDimension setInfiniteEnergy(final boolean infiniteEnergy) {
      this.infiniteEnergy = var1;
      return this;
   }

   @Generated
   public IrisDimension setMaximumEnergy(final double maximumEnergy) {
      this.maximumEnergy = var1;
      return this;
   }

   @Generated
   public IrisDimension setRockZoom(final double rockZoom) {
      this.rockZoom = var1;
      return this;
   }

   @Generated
   public IrisDimension setRockPalette(final IrisMaterialPalette rockPalette) {
      this.rockPalette = var1;
      return this;
   }

   @Generated
   public IrisDimension setFluidPalette(final IrisMaterialPalette fluidPalette) {
      this.fluidPalette = var1;
      return this;
   }

   @Generated
   public IrisDimension setDisableExplorerMaps(final boolean disableExplorerMaps) {
      this.disableExplorerMaps = var1;
      return this;
   }

   @Generated
   public IrisDimension setOres(final KList<IrisOreGenerator> ores) {
      this.ores = var1;
      return this;
   }

   @Generated
   public IrisDimension setCaveLavaHeight(final int caveLavaHeight) {
      this.caveLavaHeight = var1;
      return this;
   }

   @Generated
   public IrisDimension setDisabledComponents(final KList<MantleFlag> disabledComponents) {
      this.disabledComponents = var1;
      return this;
   }

   @Generated
   public IrisDimension setGlobalPreProcessors(final KList<IrisPreProcessors> globalPreProcessors) {
      this.globalPreProcessors = var1;
      return this;
   }

   @Generated
   public IrisDimension setEngineScripts(final KList<String> engineScripts) {
      this.engineScripts = var1;
      return this;
   }

   @Generated
   public IrisDimension setDataScripts(final KList<String> dataScripts) {
      this.dataScripts = var1;
      return this;
   }

   @Generated
   public IrisDimension setChunkUpdateScripts(final KList<String> chunkUpdateScripts) {
      this.chunkUpdateScripts = var1;
      return this;
   }

   @Generated
   public IrisDimension setLegacyRarity(final boolean legacyRarity) {
      this.legacyRarity = var1;
      return this;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getParallaxSize());
      return "IrisDimension(parallaxSize=" + var10000 + ", rockLayerGenerator=" + String.valueOf(this.getRockLayerGenerator()) + ", fluidLayerGenerator=" + String.valueOf(this.getFluidLayerGenerator()) + ", coordFracture=" + String.valueOf(this.getCoordFracture()) + ", sinr=" + String.valueOf(this.getSinr()) + ", cosr=" + String.valueOf(this.getCosr()) + ", rad=" + String.valueOf(this.getRad()) + ", featuresUsed=" + String.valueOf(this.getFeaturesUsed()) + ", strongholdsCache=" + String.valueOf(this.getStrongholdsCache()) + ", cachedPreProcessors=" + String.valueOf(this.getCachedPreProcessors()) + ", name=" + this.getName() + ", logicalHeight=" + this.getLogicalHeight() + ", stronghold=" + this.getStronghold() + ", debugChunkCrossSections=" + this.isDebugChunkCrossSections() + ", explodeBiomePalettes=" + this.isExplodeBiomePalettes() + ", studioMode=" + String.valueOf(this.getStudioMode()) + ", explodeBiomePaletteSize=" + this.getExplodeBiomePaletteSize() + ", debugCrossSectionsMod=" + this.getDebugCrossSectionsMod() + ", strongholdJumpDistance=" + this.getStrongholdJumpDistance() + ", maxStrongholds=" + this.getMaxStrongholds() + ", treeSettings=" + String.valueOf(this.getTreeSettings()) + ", entitySpawners=" + String.valueOf(this.getEntitySpawners()) + ", loot=" + String.valueOf(this.getLoot()) + ", version=" + this.getVersion() + ", blockDrops=" + String.valueOf(this.getBlockDrops()) + ", bedrock=" + this.isBedrock() + ", landChance=" + this.getLandChance() + ", regionStyle=" + String.valueOf(this.getRegionStyle()) + ", continentalStyle=" + String.valueOf(this.getContinentalStyle()) + ", landBiomeStyle=" + String.valueOf(this.getLandBiomeStyle()) + ", shoreBiomeStyle=" + String.valueOf(this.getShoreBiomeStyle()) + ", seaBiomeStyle=" + String.valueOf(this.getSeaBiomeStyle()) + ", caveBiomeStyle=" + String.valueOf(this.getCaveBiomeStyle()) + ", debugSmartBore=" + this.isDebugSmartBore() + ", decorate=" + this.isDecorate() + ", postProcessing=" + this.isPostProcessing() + ", postProcessingSlabs=" + this.isPostProcessingSlabs() + ", postProcessingWalls=" + this.isPostProcessingWalls() + ", carving=" + String.valueOf(this.getCarving()) + ", fluidBodies=" + String.valueOf(this.getFluidBodies()) + ", forceConvertTo320Height=" + this.getForceConvertTo320Height() + ", environment=" + String.valueOf(this.getEnvironment()) + ", regions=" + String.valueOf(this.getRegions()) + ", jigsawStructures=" + String.valueOf(this.getJigsawStructures()) + ", jigsawStructureDivisor=" + this.getJigsawStructureDivisor() + ", fluidHeight=" + this.getFluidHeight() + ", dimensionHeight=" + String.valueOf(this.getDimensionHeight()) + ", dimensionOptions=" + String.valueOf(this.getDimensionOptions()) + ", focus=" + this.getFocus() + ", focusRegion=" + this.getFocusRegion() + ", biomeZoom=" + this.getBiomeZoom() + ", dimensionAngleDeg=" + this.getDimensionAngleDeg() + ", mode=" + String.valueOf(this.getMode()) + ", coordFractureDistance=" + this.getCoordFractureDistance() + ", coordFractureZoom=" + this.getCoordFractureZoom() + ", landZoom=" + this.getLandZoom() + ", seaZoom=" + this.getSeaZoom() + ", continentZoom=" + this.getContinentZoom() + ", regionZoom=" + this.getRegionZoom() + ", useMantle=" + this.isUseMantle() + ", preventLeafDecay=" + this.isPreventLeafDecay() + ", deposits=" + String.valueOf(this.getDeposits()) + ", overlayNoise=" + String.valueOf(this.getOverlayNoise()) + ", infiniteEnergy=" + this.isInfiniteEnergy() + ", maximumEnergy=" + this.getMaximumEnergy() + ", rockZoom=" + this.getRockZoom() + ", rockPalette=" + String.valueOf(this.getRockPalette()) + ", fluidPalette=" + String.valueOf(this.getFluidPalette()) + ", disableExplorerMaps=" + this.isDisableExplorerMaps() + ", ores=" + String.valueOf(this.getOres()) + ", caveLavaHeight=" + this.getCaveLavaHeight() + ", disabledComponents=" + String.valueOf(this.getDisabledComponents()) + ", globalPreProcessors=" + String.valueOf(this.getGlobalPreProcessors()) + ", engineScripts=" + String.valueOf(this.getEngineScripts()) + ", dataScripts=" + String.valueOf(this.getDataScripts()) + ", chunkUpdateScripts=" + String.valueOf(this.getChunkUpdateScripts()) + ", legacyRarity=" + this.isLegacyRarity() + ")";
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisDimension)) {
         return false;
      } else {
         IrisDimension var2 = (IrisDimension)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getLogicalHeight() != var2.getLogicalHeight()) {
            return false;
         } else if (this.isDebugChunkCrossSections() != var2.isDebugChunkCrossSections()) {
            return false;
         } else if (this.isExplodeBiomePalettes() != var2.isExplodeBiomePalettes()) {
            return false;
         } else if (this.getExplodeBiomePaletteSize() != var2.getExplodeBiomePaletteSize()) {
            return false;
         } else if (this.getDebugCrossSectionsMod() != var2.getDebugCrossSectionsMod()) {
            return false;
         } else if (this.getStrongholdJumpDistance() != var2.getStrongholdJumpDistance()) {
            return false;
         } else if (this.getMaxStrongholds() != var2.getMaxStrongholds()) {
            return false;
         } else if (this.getVersion() != var2.getVersion()) {
            return false;
         } else if (this.isBedrock() != var2.isBedrock()) {
            return false;
         } else if (Double.compare(this.getLandChance(), var2.getLandChance()) != 0) {
            return false;
         } else if (this.isDebugSmartBore() != var2.isDebugSmartBore()) {
            return false;
         } else if (this.isDecorate() != var2.isDecorate()) {
            return false;
         } else if (this.isPostProcessing() != var2.isPostProcessing()) {
            return false;
         } else if (this.isPostProcessingSlabs() != var2.isPostProcessingSlabs()) {
            return false;
         } else if (this.isPostProcessingWalls() != var2.isPostProcessingWalls()) {
            return false;
         } else if (Double.compare(this.getJigsawStructureDivisor(), var2.getJigsawStructureDivisor()) != 0) {
            return false;
         } else if (this.getFluidHeight() != var2.getFluidHeight()) {
            return false;
         } else if (Double.compare(this.getBiomeZoom(), var2.getBiomeZoom()) != 0) {
            return false;
         } else if (Double.compare(this.getDimensionAngleDeg(), var2.getDimensionAngleDeg()) != 0) {
            return false;
         } else if (Double.compare(this.getCoordFractureDistance(), var2.getCoordFractureDistance()) != 0) {
            return false;
         } else if (Double.compare(this.getCoordFractureZoom(), var2.getCoordFractureZoom()) != 0) {
            return false;
         } else if (Double.compare(this.getLandZoom(), var2.getLandZoom()) != 0) {
            return false;
         } else if (Double.compare(this.getSeaZoom(), var2.getSeaZoom()) != 0) {
            return false;
         } else if (Double.compare(this.getContinentZoom(), var2.getContinentZoom()) != 0) {
            return false;
         } else if (Double.compare(this.getRegionZoom(), var2.getRegionZoom()) != 0) {
            return false;
         } else if (this.isUseMantle() != var2.isUseMantle()) {
            return false;
         } else if (this.isPreventLeafDecay() != var2.isPreventLeafDecay()) {
            return false;
         } else if (this.isInfiniteEnergy() != var2.isInfiniteEnergy()) {
            return false;
         } else if (Double.compare(this.getMaximumEnergy(), var2.getMaximumEnergy()) != 0) {
            return false;
         } else if (Double.compare(this.getRockZoom(), var2.getRockZoom()) != 0) {
            return false;
         } else if (this.isDisableExplorerMaps() != var2.isDisableExplorerMaps()) {
            return false;
         } else if (this.getCaveLavaHeight() != var2.getCaveLavaHeight()) {
            return false;
         } else if (this.isLegacyRarity() != var2.isLegacyRarity()) {
            return false;
         } else {
            Boolean var3 = this.getForceConvertTo320Height();
            Boolean var4 = var2.getForceConvertTo320Height();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            String var5 = this.getName();
            String var6 = var2.getName();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            label487: {
               String var7 = this.getStronghold();
               String var8 = var2.getStronghold();
               if (var7 == null) {
                  if (var8 == null) {
                     break label487;
                  }
               } else if (var7.equals(var8)) {
                  break label487;
               }

               return false;
            }

            label480: {
               StudioMode var9 = this.getStudioMode();
               StudioMode var10 = var2.getStudioMode();
               if (var9 == null) {
                  if (var10 == null) {
                     break label480;
                  }
               } else if (var9.equals(var10)) {
                  break label480;
               }

               return false;
            }

            IrisTreeSettings var11 = this.getTreeSettings();
            IrisTreeSettings var12 = var2.getTreeSettings();
            if (var11 == null) {
               if (var12 != null) {
                  return false;
               }
            } else if (!var11.equals(var12)) {
               return false;
            }

            KList var13 = this.getEntitySpawners();
            KList var14 = var2.getEntitySpawners();
            if (var13 == null) {
               if (var14 != null) {
                  return false;
               }
            } else if (!var13.equals(var14)) {
               return false;
            }

            label459: {
               IrisLootReference var15 = this.getLoot();
               IrisLootReference var16 = var2.getLoot();
               if (var15 == null) {
                  if (var16 == null) {
                     break label459;
                  }
               } else if (var15.equals(var16)) {
                  break label459;
               }

               return false;
            }

            KList var17 = this.getBlockDrops();
            KList var18 = var2.getBlockDrops();
            if (var17 == null) {
               if (var18 != null) {
                  return false;
               }
            } else if (!var17.equals(var18)) {
               return false;
            }

            IrisGeneratorStyle var19 = this.getRegionStyle();
            IrisGeneratorStyle var20 = var2.getRegionStyle();
            if (var19 == null) {
               if (var20 != null) {
                  return false;
               }
            } else if (!var19.equals(var20)) {
               return false;
            }

            label438: {
               IrisGeneratorStyle var21 = this.getContinentalStyle();
               IrisGeneratorStyle var22 = var2.getContinentalStyle();
               if (var21 == null) {
                  if (var22 == null) {
                     break label438;
                  }
               } else if (var21.equals(var22)) {
                  break label438;
               }

               return false;
            }

            label431: {
               IrisGeneratorStyle var23 = this.getLandBiomeStyle();
               IrisGeneratorStyle var24 = var2.getLandBiomeStyle();
               if (var23 == null) {
                  if (var24 == null) {
                     break label431;
                  }
               } else if (var23.equals(var24)) {
                  break label431;
               }

               return false;
            }

            label424: {
               IrisGeneratorStyle var25 = this.getShoreBiomeStyle();
               IrisGeneratorStyle var26 = var2.getShoreBiomeStyle();
               if (var25 == null) {
                  if (var26 == null) {
                     break label424;
                  }
               } else if (var25.equals(var26)) {
                  break label424;
               }

               return false;
            }

            IrisGeneratorStyle var27 = this.getSeaBiomeStyle();
            IrisGeneratorStyle var28 = var2.getSeaBiomeStyle();
            if (var27 == null) {
               if (var28 != null) {
                  return false;
               }
            } else if (!var27.equals(var28)) {
               return false;
            }

            label410: {
               IrisGeneratorStyle var29 = this.getCaveBiomeStyle();
               IrisGeneratorStyle var30 = var2.getCaveBiomeStyle();
               if (var29 == null) {
                  if (var30 == null) {
                     break label410;
                  }
               } else if (var29.equals(var30)) {
                  break label410;
               }

               return false;
            }

            IrisCarving var31 = this.getCarving();
            IrisCarving var32 = var2.getCarving();
            if (var31 == null) {
               if (var32 != null) {
                  return false;
               }
            } else if (!var31.equals(var32)) {
               return false;
            }

            label396: {
               IrisFluidBodies var33 = this.getFluidBodies();
               IrisFluidBodies var34 = var2.getFluidBodies();
               if (var33 == null) {
                  if (var34 == null) {
                     break label396;
                  }
               } else if (var33.equals(var34)) {
                  break label396;
               }

               return false;
            }

            Environment var35 = this.getEnvironment();
            Environment var36 = var2.getEnvironment();
            if (var35 == null) {
               if (var36 != null) {
                  return false;
               }
            } else if (!var35.equals(var36)) {
               return false;
            }

            KList var37 = this.getRegions();
            KList var38 = var2.getRegions();
            if (var37 == null) {
               if (var38 != null) {
                  return false;
               }
            } else if (!var37.equals(var38)) {
               return false;
            }

            label375: {
               KList var39 = this.getJigsawStructures();
               KList var40 = var2.getJigsawStructures();
               if (var39 == null) {
                  if (var40 == null) {
                     break label375;
                  }
               } else if (var39.equals(var40)) {
                  break label375;
               }

               return false;
            }

            label368: {
               IrisRange var41 = this.getDimensionHeight();
               IrisRange var42 = var2.getDimensionHeight();
               if (var41 == null) {
                  if (var42 == null) {
                     break label368;
                  }
               } else if (var41.equals(var42)) {
                  break label368;
               }

               return false;
            }

            IrisDimensionTypeOptions var43 = this.getDimensionOptions();
            IrisDimensionTypeOptions var44 = var2.getDimensionOptions();
            if (var43 == null) {
               if (var44 != null) {
                  return false;
               }
            } else if (!var43.equals(var44)) {
               return false;
            }

            String var45 = this.getFocus();
            String var46 = var2.getFocus();
            if (var45 == null) {
               if (var46 != null) {
                  return false;
               }
            } else if (!var45.equals(var46)) {
               return false;
            }

            label347: {
               String var47 = this.getFocusRegion();
               String var48 = var2.getFocusRegion();
               if (var47 == null) {
                  if (var48 == null) {
                     break label347;
                  }
               } else if (var47.equals(var48)) {
                  break label347;
               }

               return false;
            }

            IrisDimensionMode var49 = this.getMode();
            IrisDimensionMode var50 = var2.getMode();
            if (var49 == null) {
               if (var50 != null) {
                  return false;
               }
            } else if (!var49.equals(var50)) {
               return false;
            }

            KList var51 = this.getDeposits();
            KList var52 = var2.getDeposits();
            if (var51 == null) {
               if (var52 != null) {
                  return false;
               }
            } else if (!var51.equals(var52)) {
               return false;
            }

            label326: {
               KList var53 = this.getOverlayNoise();
               KList var54 = var2.getOverlayNoise();
               if (var53 == null) {
                  if (var54 == null) {
                     break label326;
                  }
               } else if (var53.equals(var54)) {
                  break label326;
               }

               return false;
            }

            label319: {
               IrisMaterialPalette var55 = this.getRockPalette();
               IrisMaterialPalette var56 = var2.getRockPalette();
               if (var55 == null) {
                  if (var56 == null) {
                     break label319;
                  }
               } else if (var55.equals(var56)) {
                  break label319;
               }

               return false;
            }

            label312: {
               IrisMaterialPalette var57 = this.getFluidPalette();
               IrisMaterialPalette var58 = var2.getFluidPalette();
               if (var57 == null) {
                  if (var58 == null) {
                     break label312;
                  }
               } else if (var57.equals(var58)) {
                  break label312;
               }

               return false;
            }

            KList var59 = this.getOres();
            KList var60 = var2.getOres();
            if (var59 == null) {
               if (var60 != null) {
                  return false;
               }
            } else if (!var59.equals(var60)) {
               return false;
            }

            label298: {
               KList var61 = this.getDisabledComponents();
               KList var62 = var2.getDisabledComponents();
               if (var61 == null) {
                  if (var62 == null) {
                     break label298;
                  }
               } else if (var61.equals(var62)) {
                  break label298;
               }

               return false;
            }

            KList var63 = this.getGlobalPreProcessors();
            KList var64 = var2.getGlobalPreProcessors();
            if (var63 == null) {
               if (var64 != null) {
                  return false;
               }
            } else if (!var63.equals(var64)) {
               return false;
            }

            label284: {
               KList var65 = this.getEngineScripts();
               KList var66 = var2.getEngineScripts();
               if (var65 == null) {
                  if (var66 == null) {
                     break label284;
                  }
               } else if (var65.equals(var66)) {
                  break label284;
               }

               return false;
            }

            KList var67 = this.getDataScripts();
            KList var68 = var2.getDataScripts();
            if (var67 == null) {
               if (var68 != null) {
                  return false;
               }
            } else if (!var67.equals(var68)) {
               return false;
            }

            KList var69 = this.getChunkUpdateScripts();
            KList var70 = var2.getChunkUpdateScripts();
            if (var69 == null) {
               if (var70 != null) {
                  return false;
               }
            } else if (!var69.equals(var70)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisDimension;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var61 = var2 * 59 + this.getLogicalHeight();
      var61 = var61 * 59 + (this.isDebugChunkCrossSections() ? 79 : 97);
      var61 = var61 * 59 + (this.isExplodeBiomePalettes() ? 79 : 97);
      var61 = var61 * 59 + this.getExplodeBiomePaletteSize();
      var61 = var61 * 59 + this.getDebugCrossSectionsMod();
      var61 = var61 * 59 + this.getStrongholdJumpDistance();
      var61 = var61 * 59 + this.getMaxStrongholds();
      var61 = var61 * 59 + this.getVersion();
      var61 = var61 * 59 + (this.isBedrock() ? 79 : 97);
      long var3 = Double.doubleToLongBits(this.getLandChance());
      var61 = var61 * 59 + (int)(var3 >>> 32 ^ var3);
      var61 = var61 * 59 + (this.isDebugSmartBore() ? 79 : 97);
      var61 = var61 * 59 + (this.isDecorate() ? 79 : 97);
      var61 = var61 * 59 + (this.isPostProcessing() ? 79 : 97);
      var61 = var61 * 59 + (this.isPostProcessingSlabs() ? 79 : 97);
      var61 = var61 * 59 + (this.isPostProcessingWalls() ? 79 : 97);
      long var5 = Double.doubleToLongBits(this.getJigsawStructureDivisor());
      var61 = var61 * 59 + (int)(var5 >>> 32 ^ var5);
      var61 = var61 * 59 + this.getFluidHeight();
      long var7 = Double.doubleToLongBits(this.getBiomeZoom());
      var61 = var61 * 59 + (int)(var7 >>> 32 ^ var7);
      long var9 = Double.doubleToLongBits(this.getDimensionAngleDeg());
      var61 = var61 * 59 + (int)(var9 >>> 32 ^ var9);
      long var11 = Double.doubleToLongBits(this.getCoordFractureDistance());
      var61 = var61 * 59 + (int)(var11 >>> 32 ^ var11);
      long var13 = Double.doubleToLongBits(this.getCoordFractureZoom());
      var61 = var61 * 59 + (int)(var13 >>> 32 ^ var13);
      long var15 = Double.doubleToLongBits(this.getLandZoom());
      var61 = var61 * 59 + (int)(var15 >>> 32 ^ var15);
      long var17 = Double.doubleToLongBits(this.getSeaZoom());
      var61 = var61 * 59 + (int)(var17 >>> 32 ^ var17);
      long var19 = Double.doubleToLongBits(this.getContinentZoom());
      var61 = var61 * 59 + (int)(var19 >>> 32 ^ var19);
      long var21 = Double.doubleToLongBits(this.getRegionZoom());
      var61 = var61 * 59 + (int)(var21 >>> 32 ^ var21);
      var61 = var61 * 59 + (this.isUseMantle() ? 79 : 97);
      var61 = var61 * 59 + (this.isPreventLeafDecay() ? 79 : 97);
      var61 = var61 * 59 + (this.isInfiniteEnergy() ? 79 : 97);
      long var23 = Double.doubleToLongBits(this.getMaximumEnergy());
      var61 = var61 * 59 + (int)(var23 >>> 32 ^ var23);
      long var25 = Double.doubleToLongBits(this.getRockZoom());
      var61 = var61 * 59 + (int)(var25 >>> 32 ^ var25);
      var61 = var61 * 59 + (this.isDisableExplorerMaps() ? 79 : 97);
      var61 = var61 * 59 + this.getCaveLavaHeight();
      var61 = var61 * 59 + (this.isLegacyRarity() ? 79 : 97);
      Boolean var27 = this.getForceConvertTo320Height();
      var61 = var61 * 59 + (var27 == null ? 43 : var27.hashCode());
      String var28 = this.getName();
      var61 = var61 * 59 + (var28 == null ? 43 : var28.hashCode());
      String var29 = this.getStronghold();
      var61 = var61 * 59 + (var29 == null ? 43 : var29.hashCode());
      StudioMode var30 = this.getStudioMode();
      var61 = var61 * 59 + (var30 == null ? 43 : var30.hashCode());
      IrisTreeSettings var31 = this.getTreeSettings();
      var61 = var61 * 59 + (var31 == null ? 43 : var31.hashCode());
      KList var32 = this.getEntitySpawners();
      var61 = var61 * 59 + (var32 == null ? 43 : var32.hashCode());
      IrisLootReference var33 = this.getLoot();
      var61 = var61 * 59 + (var33 == null ? 43 : var33.hashCode());
      KList var34 = this.getBlockDrops();
      var61 = var61 * 59 + (var34 == null ? 43 : var34.hashCode());
      IrisGeneratorStyle var35 = this.getRegionStyle();
      var61 = var61 * 59 + (var35 == null ? 43 : var35.hashCode());
      IrisGeneratorStyle var36 = this.getContinentalStyle();
      var61 = var61 * 59 + (var36 == null ? 43 : var36.hashCode());
      IrisGeneratorStyle var37 = this.getLandBiomeStyle();
      var61 = var61 * 59 + (var37 == null ? 43 : var37.hashCode());
      IrisGeneratorStyle var38 = this.getShoreBiomeStyle();
      var61 = var61 * 59 + (var38 == null ? 43 : var38.hashCode());
      IrisGeneratorStyle var39 = this.getSeaBiomeStyle();
      var61 = var61 * 59 + (var39 == null ? 43 : var39.hashCode());
      IrisGeneratorStyle var40 = this.getCaveBiomeStyle();
      var61 = var61 * 59 + (var40 == null ? 43 : var40.hashCode());
      IrisCarving var41 = this.getCarving();
      var61 = var61 * 59 + (var41 == null ? 43 : var41.hashCode());
      IrisFluidBodies var42 = this.getFluidBodies();
      var61 = var61 * 59 + (var42 == null ? 43 : var42.hashCode());
      Environment var43 = this.getEnvironment();
      var61 = var61 * 59 + (var43 == null ? 43 : var43.hashCode());
      KList var44 = this.getRegions();
      var61 = var61 * 59 + (var44 == null ? 43 : var44.hashCode());
      KList var45 = this.getJigsawStructures();
      var61 = var61 * 59 + (var45 == null ? 43 : var45.hashCode());
      IrisRange var46 = this.getDimensionHeight();
      var61 = var61 * 59 + (var46 == null ? 43 : var46.hashCode());
      IrisDimensionTypeOptions var47 = this.getDimensionOptions();
      var61 = var61 * 59 + (var47 == null ? 43 : var47.hashCode());
      String var48 = this.getFocus();
      var61 = var61 * 59 + (var48 == null ? 43 : var48.hashCode());
      String var49 = this.getFocusRegion();
      var61 = var61 * 59 + (var49 == null ? 43 : var49.hashCode());
      IrisDimensionMode var50 = this.getMode();
      var61 = var61 * 59 + (var50 == null ? 43 : var50.hashCode());
      KList var51 = this.getDeposits();
      var61 = var61 * 59 + (var51 == null ? 43 : var51.hashCode());
      KList var52 = this.getOverlayNoise();
      var61 = var61 * 59 + (var52 == null ? 43 : var52.hashCode());
      IrisMaterialPalette var53 = this.getRockPalette();
      var61 = var61 * 59 + (var53 == null ? 43 : var53.hashCode());
      IrisMaterialPalette var54 = this.getFluidPalette();
      var61 = var61 * 59 + (var54 == null ? 43 : var54.hashCode());
      KList var55 = this.getOres();
      var61 = var61 * 59 + (var55 == null ? 43 : var55.hashCode());
      KList var56 = this.getDisabledComponents();
      var61 = var61 * 59 + (var56 == null ? 43 : var56.hashCode());
      KList var57 = this.getGlobalPreProcessors();
      var61 = var61 * 59 + (var57 == null ? 43 : var57.hashCode());
      KList var58 = this.getEngineScripts();
      var61 = var61 * 59 + (var58 == null ? 43 : var58.hashCode());
      KList var59 = this.getDataScripts();
      var61 = var61 * 59 + (var59 == null ? 43 : var59.hashCode());
      KList var60 = this.getChunkUpdateScripts();
      var61 = var61 * 59 + (var60 == null ? 43 : var60.hashCode());
      return var61;
   }

   static {
      STONE = Material.STONE.createBlockData();
      WATER = Material.WATER.createBlockData();
   }
}
