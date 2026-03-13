package me.casperge.realisticseasons.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.Version;
import me.casperge.realisticseasons.biome.BlockReplacement;
import me.casperge.realisticseasons.biome.BlockReplacements;
import me.casperge.realisticseasons.season.Season;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

public class Settings {
   public boolean modifyBlocks = true;
   public boolean affectTime = true;
   public boolean affectWeather = true;
   public boolean affectBiomes = true;
   public boolean affectCropsWinter = true;
   public boolean affectCropsSummer = true;
   public int delayPerTick = 4;
   public float spawnChanceAnimals = 10.0F;
   public int timeSyncOffset = 0;
   public boolean removeAnimalsOnSeasonChange = true;
   public boolean sendMessageOnSeasonCycle = true;
   public boolean spawnIceInWinter = true;
   public boolean snowmanCantPlaceSnow = false;
   public boolean snowmanAttackMobs = false;
   public boolean snowmanImmuneToWater = true;
   public String biomeDisplayName = "realisticseasons";
   public boolean spawnEntities = true;
   public float flowerchanceinspring = 5.0F;
   public boolean doSeasonCycle = true;
   public int maxBerryBushes = 20;
   public boolean snowRemoval = true;
   public boolean doBerryDamage = false;
   public boolean spawnBatsInFall = false;
   public boolean spawnCaveSpidersInFall = false;
   public boolean spawnFoxesInFall = false;
   public boolean spawnMooshroomInFall = false;
   public boolean spawnExtraSpidersInFall = false;
   public boolean spawnFrogsInFall = false;
   public boolean pumpkinsInFall = true;
   public float pumpkinchance = 20.0F;
   public boolean spawnExtraAnimalsInSpring = false;
   public boolean spawnExtraBeesInSpring = false;
   public boolean spawnExtraRabitsInSpring = false;
   public boolean spawnOcelotsInSummer = false;
   public boolean spawnPandasInSummer = false;
   public boolean spawnHusksInSummer = false;
   public boolean spawnParrotsInSummer = false;
   public boolean spawnWolfsInWinter = false;
   public boolean spawnFoxesInWinter = false;
   public String timezone = "system";
   public boolean spawnStraysInWinter = false;
   public boolean spawnPolarBearsInWinter = false;
   public boolean generateLeafLitter = true;
   public boolean destroyPlantsForLeafLitter = true;
   public Material[] spawnLeafLitterUnder;
   public boolean keepPlayerPlacedPlants;
   public boolean spawnSnowmanInWinter;
   public boolean terraGenSnowDisabled;
   public boolean spawnSnowOnLeaves;
   public boolean snowmanWearPumpkin;
   public boolean calendarEnabled;
   public boolean americandateformat;
   public boolean pauseMapRenderInWinter;
   public boolean regrowGrass;
   public String timeFormat;
   public boolean is12hourClock;
   public boolean AmPm;
   public boolean snowPlacement;
   public boolean syncNewWorlds;
   public boolean keepNaturalSnow;
   public int firstSnowfall;
   public boolean spawnMushroomsInFall;
   public boolean prioritiseTPS;
   public boolean isCalendarInRealLifeDays;
   public boolean canCheckUnloadedChunks;
   public int delayPerAsyncChunk;
   public Material fallingLeafItem;
   public Integer fallingLeafCMD;
   public double minTPS;
   public List<String> syncedWorlds;
   public HashMap<Season, Settings.WeatherSettings> weathersettings;
   public boolean subSeasonsEnabled;
   public String SpringSummerAdjustment;
   public double SpringSummerWeight;
   public int minTempModify;
   public int maxTempModify;
   public String SummerFallAdjustment;
   public double SummerFallWeight;
   public String FallWinterAdjustment;
   public double FallWinterWeight;
   public String WinterSpringAdjustment;
   public double WinterSpringWeight;
   public boolean fallingLeavesEnabled;
   public float fallingLeavesChance;
   public List<Season> fallingLeavesSeasons;
   public boolean smallFallingLeavesEnabled;
   public float smallFallingLeavesChance;
   public List<Season> smallFallingLeavesSeasons;
   public boolean fallingStarsEnabled;
   public int fallingStarsHeight;
   public float fallingStarsChance;
   public List<Season> fallingStarsSeasons;
   public boolean nightSparksEnabled;
   public int nightSparksParticleCount;
   public float nightSparksChance;
   public int nightSparksHeight;
   public List<Season> nightSparksSeasons;
   public boolean fireFliesEnabled;
   public float fireFliesChance;
   public List<Season> fireFliesSeasons;
   public boolean sweatEffectEnabled;
   public int sweatMinTemperature;
   public boolean coldBreathEnabled;
   public boolean coldBreathHideOwn;
   public boolean optimizedBiomes;
   public int coldBreathMaxTemperature;
   public Season defaultSeason;
   public int snowLayers;
   public boolean syncWorldTimeWithRealWorld;
   public List<EntityType> fallAnimals;
   public List<EntityType> springAnimals;
   public List<EntityType> summerAnimals;
   public List<EntityType> winterAnimals;
   public List<EntityType> fullFallAnimals;
   public List<EntityType> fullSpringAnimals;
   public List<EntityType> fullSummerAnimals;
   public List<EntityType> fullWinterAnimals;
   public List<String> worldsWithoutEvents;
   private FileConfiguration config;
   private RealisticSeasons main;

   public Settings(RealisticSeasons var1, boolean var2) {
      this.spawnLeafLitterUnder = new Material[]{Material.OAK_LEAVES, Material.DARK_OAK_LEAVES, Material.BIRCH_LEAVES};
      this.keepPlayerPlacedPlants = true;
      this.spawnSnowmanInWinter = false;
      this.terraGenSnowDisabled = true;
      this.spawnSnowOnLeaves = true;
      this.snowmanWearPumpkin = false;
      this.calendarEnabled = true;
      this.americandateformat = false;
      this.pauseMapRenderInWinter = true;
      this.regrowGrass = false;
      this.timeFormat = "\\$hours\\$:\\$minutes\\$";
      this.is12hourClock = false;
      this.AmPm = false;
      this.snowPlacement = true;
      this.syncNewWorlds = false;
      this.keepNaturalSnow = true;
      this.firstSnowfall = 1;
      this.spawnMushroomsInFall = true;
      this.prioritiseTPS = true;
      this.isCalendarInRealLifeDays = false;
      this.canCheckUnloadedChunks = true;
      this.delayPerAsyncChunk = 40;
      this.fallingLeafItem = Material.KELP;
      this.fallingLeafCMD = 0;
      this.minTPS = 19.5D;
      this.syncedWorlds = new ArrayList();
      this.weathersettings = new HashMap();
      this.subSeasonsEnabled = true;
      this.SpringSummerAdjustment = "";
      this.SpringSummerWeight = 0.0D;
      this.minTempModify = -999999;
      this.maxTempModify = 999999;
      this.SummerFallAdjustment = "";
      this.SummerFallWeight = 0.0D;
      this.FallWinterAdjustment = "";
      this.FallWinterWeight = 0.0D;
      this.WinterSpringAdjustment = "";
      this.WinterSpringWeight = 0.0D;
      this.fallingLeavesEnabled = true;
      this.fallingLeavesChance = 0.5F;
      this.fallingLeavesSeasons = new ArrayList();
      this.smallFallingLeavesEnabled = true;
      this.smallFallingLeavesChance = 0.5F;
      this.smallFallingLeavesSeasons = new ArrayList();
      this.fallingStarsEnabled = true;
      this.fallingStarsHeight = 150;
      this.fallingStarsChance = 0.5F;
      this.fallingStarsSeasons = new ArrayList();
      this.nightSparksEnabled = true;
      this.nightSparksParticleCount = 500;
      this.nightSparksChance = 33.0F;
      this.nightSparksHeight = 40;
      this.nightSparksSeasons = new ArrayList();
      this.fireFliesEnabled = true;
      this.fireFliesChance = 0.03F;
      this.fireFliesSeasons = new ArrayList();
      this.sweatEffectEnabled = true;
      this.sweatMinTemperature = 45;
      this.coldBreathEnabled = true;
      this.coldBreathHideOwn = true;
      this.optimizedBiomes = true;
      this.coldBreathMaxTemperature = 0;
      this.defaultSeason = Season.DISABLED;
      this.snowLayers = 1;
      this.syncWorldTimeWithRealWorld = false;
      this.fallAnimals = new ArrayList();
      this.springAnimals = new ArrayList();
      this.summerAnimals = new ArrayList();
      this.winterAnimals = new ArrayList();
      this.fullFallAnimals = new ArrayList();
      this.fullSpringAnimals = new ArrayList();
      this.fullSummerAnimals = new ArrayList();
      this.fullWinterAnimals = new ArrayList();
      this.worldsWithoutEvents = new ArrayList();
      this.main = var1;
      this.config = var1.getRSConfig();
      this.load(var2);
      var1.isFreshInstall = var2;
   }

   public void reload() {
      boolean var1 = this.main.loadConfig();
      this.config = this.main.getRSConfig();
      this.load(var1);
   }

   public void load(boolean var1) {
      if (this.isOldConfig(this.config.getString("version"), "7.18") && !var1) {
         Bukkit.getLogger().info("[RealisticSeasons] Old config detected, updating chances to percentages.");
         Integer var2 = this.config.getInt("flower-chance-in-spring");
         Integer var3 = this.config.getInt("entities.fall.entities-pumpkin-chance");
         Integer var4 = this.config.getInt("entities.spawn-chance-animals");
         this.config.set("flower-chance-in-spring", this.convertChanceToPercentage(var2));
         this.config.set("entities.fall.entities-pumpkin-chance", this.convertChanceToPercentage(var3));
         this.config.set("entities.spawn-chance-animals", this.convertChanceToPercentage(var4));
      }

      if (!Version.is_1_19_3_or_up()) {
         if (!this.config.contains("max-snow-layers")) {
            this.config.set("max-snow-layers", 1);
         } else {
            this.snowLayers = this.config.getInt("max-snow-layers");
         }
      }

      this.fallingLeafItem = Material.valueOf(this.config.getString("falling-leaf-item"));
      if (this.fallingLeafItem == null) {
         this.fallingLeafItem = Material.KELP;
      }

      this.fallingLeafCMD = this.config.getInt("falling-leaf-custommodeldata");
      this.modifyBlocks = this.config.getBoolean("modify-blocks");
      this.sendMessageOnSeasonCycle = this.config.getBoolean("send-message-on-season-cycle");
      this.affectWeather = this.config.getBoolean("modify-weather-cycle");
      this.affectTime = this.config.getBoolean("modify-daynight-length");
      this.affectBiomes = this.config.getBoolean("modify-biome-on-client");
      this.snowRemoval = this.config.getBoolean("snow-and-ice-removal");
      this.delayPerTick = this.config.getInt("delay-per-operation");
      this.doSeasonCycle = this.config.getBoolean("automatic-season-cycling");
      this.maxBerryBushes = this.config.getInt("max-berrybushes-per-chunk");
      this.doBerryDamage = !this.config.getBoolean("disable-berry-damage-for-entities");
      this.regrowGrass = this.config.getBoolean("regrow-grass-everywhere");
      this.spawnMushroomsInFall = this.config.getBoolean("spawn-mushrooms-in-fall");
      this.optimizedBiomes = this.config.getBoolean("optimized-custom-biomes");
      if (this.config.contains("default-season")) {
         this.config.set("on-new-world-creation.default-season", this.config.getString("default-season"));
      }

      String var22 = this.config.getString("on-new-world-creation.default-season");
      if (!var22.equalsIgnoreCase("none")) {
         Season var23 = Season.valueOf(var22);
         if (var23 != null) {
            this.defaultSeason = var23;
         }
      }

      this.syncNewWorlds = this.config.getBoolean("on-new-world-creation.sync-with-other-worlds");
      this.prioritiseTPS = this.config.getBoolean("performance.prioritise-tps");
      this.minTPS = this.config.getDouble("performance.min-tps");
      this.keepPlayerPlacedPlants = this.config.getBoolean("keep-player-placed-plants");
      if (this.config.getKeys(false).contains("minecraft-days-per-season")) {
         int var24 = this.config.getInt("minecraft-days-per-season");
         this.config.set("length-in-minecraft-days-winter", var24);
         this.config.set("length-in-minecraft-days-fall", var24);
         this.config.set("length-in-minecraft-days-spring", var24);
         this.config.set("length-in-minecraft-days-summer", var24);
         this.config.set("minecraft-days-per-season", (Object)null);
      }

      this.snowmanCantPlaceSnow = this.config.getBoolean("disable-snow-placement-all-snowman");
      this.snowmanAttackMobs = this.config.getBoolean("snowman-attack-mobs");
      this.snowmanImmuneToWater = this.config.getBoolean("snowman-immune-to-water");
      this.snowmanWearPumpkin = this.config.getBoolean("snowman-wear-pumpkin");
      this.biomeDisplayName = this.config.getString("biome-display-name").toLowerCase();
      this.spawnEntities = this.config.getBoolean("entities.spawn-entities");
      this.subSeasonsEnabled = this.config.getBoolean("sub-seasons.enabled");
      this.delayPerAsyncChunk = this.config.getInt("unloaded-chunks-check-delay");
      this.terraGenSnowDisabled = this.config.getBoolean("no-snow-on-leaves-for-terra");
      this.flowerchanceinspring = Float.valueOf(this.config.getString("flower-chance-in-spring").replaceAll("%", ""));
      this.removeAnimalsOnSeasonChange = this.config.getBoolean("entities.despawn-season-animals");
      if (this.config.isConfigurationSection("messages")) {
         Bukkit.getLogger().severe("Your RealisticSeasons config still contains a messages section. Please note that this isn't used anymore and the lang.yml file is now used for messages. You can savely remove the messages section from the config.");
      }

      this.spawnChanceAnimals = Float.valueOf(this.config.getString("entities.spawn-chance-animals").replaceAll("%", ""));
      if (this.config.getBoolean("entities.spawn-entities")) {
         this.spawnExtraAnimalsInSpring = this.config.getBoolean("entities.spring.spawn-extra-animals");
         this.spawnExtraBeesInSpring = this.config.getBoolean("entities.spring.spawn-extra-bees");
         this.spawnExtraRabitsInSpring = this.config.getBoolean("entities.spring.spawn-extra-rabits");
         this.spawnOcelotsInSummer = this.config.getBoolean("entities.summer.spawn-ocelots");
         this.spawnPandasInSummer = this.config.getBoolean("entities.summer.spawn-pandas");
         this.spawnParrotsInSummer = this.config.getBoolean("entities.summer.spawn-parrots");
         this.spawnHusksInSummer = this.config.getBoolean("entities.summer.replace-zombies-with-husks");
         this.spawnBatsInFall = this.config.getBoolean("entities.fall.spawn-bats");
         this.spawnFrogsInFall = this.config.getBoolean("entities.fall.spawn-frogs");
         this.spawnCaveSpidersInFall = this.config.getBoolean("entities.fall.spawn-cave-spiders");
         this.spawnFoxesInFall = this.config.getBoolean("entities.fall.spawn-foxes");
         this.spawnMooshroomInFall = this.config.getBoolean("entities.fall.spawn-mooshrooms");
         this.spawnExtraSpidersInFall = this.config.getBoolean("entities.fall.spawn-extra-spiders");
         this.spawnWolfsInWinter = this.config.getBoolean("entities.winter.spawn-wolfs");
         this.spawnFoxesInWinter = this.config.getBoolean("entities.winter.spawn-foxes");
         this.spawnPolarBearsInWinter = this.config.getBoolean("entities.winter.spawn-polarbears");
         this.spawnSnowmanInWinter = this.config.getBoolean("entities.winter.spawn-snowman");
         this.spawnStraysInWinter = this.config.getBoolean("entities.winter.replace-skeletons-with-strays");
      }

      this.spawnIceInWinter = this.config.getBoolean("spawn-ice-in-winter");
      this.pumpkinsInFall = this.config.getBoolean("entities.fall.give-entities-pumpkin");
      this.pumpkinchance = Float.valueOf(this.config.getString("entities.fall.entities-pumpkin-chance").replaceAll("%", ""));
      this.keepNaturalSnow = this.config.getBoolean("keep-natural-snow");
      this.snowPlacement = this.config.getBoolean("snow-placement-enabled");
      this.firstSnowfall = this.config.getInt("first-snowfall-sub-season");
      String var25 = this.config.getString("sub-seasons.foliage-color-adjustments.WINTER-SPRING").toLowerCase().replaceAll("weight:", "");
      if (!var25.contains("none")) {
         String[] var26 = var25.split(",");
         if (var26.length == 2) {
            this.WinterSpringAdjustment = var26[0].replaceAll("#", "");
            this.WinterSpringWeight = Double.parseDouble(var26[1]);
         }
      }

      if (this.config.getString("sub-seasons.foliage-color-adjustments.SPRING-SUMMER").equals("#FFFFFF,WEIGHT:0.3")) {
         this.config.set("sub-seasons.foliage-color-adjustments.SPRING-SUMMER", "#32CD32,WEIGHT:0.3");
      }

      this.spawnSnowOnLeaves = this.config.getBoolean("spawn-snow-on-leaves");
      String var27 = this.config.getString("sub-seasons.foliage-color-adjustments.SPRING-SUMMER").toLowerCase().replaceAll("weight:", "");
      if (!var27.contains("none")) {
         String[] var5 = var27.split(",");
         if (var5.length == 2) {
            this.SpringSummerAdjustment = var5[0].replaceAll("#", "");
            this.SpringSummerWeight = Double.parseDouble(var5[1]);
         }
      }

      String var28 = this.config.getString("sub-seasons.foliage-color-adjustments.SUMMER-FALL").toLowerCase().replaceAll("weight:", "");
      if (!var28.contains("none")) {
         String[] var6 = var28.split(",");
         if (var6.length == 2) {
            this.SummerFallAdjustment = var6[0].replaceAll("#", "");
            this.SummerFallWeight = Double.parseDouble(var6[1]);
         }
      }

      String var29 = this.config.getString("sub-seasons.foliage-color-adjustments.FALL-WINTER").toLowerCase().replaceAll("weight:", "");
      if (!var29.contains("none")) {
         String[] var7 = var29.split(",");
         if (var7.length == 2) {
            this.FallWinterAdjustment = var7[0].replaceAll("#", "");
            this.FallWinterWeight = Double.parseDouble(var7[1]);
         }
      }

      this.fallAnimals.clear();
      this.springAnimals.clear();
      this.summerAnimals.clear();
      this.winterAnimals.clear();
      this.minTempModify = this.config.getInt("temperature-modify-command.min");
      this.maxTempModify = this.config.getInt("temperature-modify-command.max");
      this.generateLeafLitter = this.config.getBoolean("generate-leaf-litter");
      this.destroyPlantsForLeafLitter = this.config.getBoolean("destroy-plants-for-leaf-litter");
      String var30 = this.config.getString("spawn-leaf-litter-under");
      int var11;
      if (var30 != null && !var30.isBlank()) {
         String[] var8 = var30.split(",");
         Material[] var9 = new Material[var8.length];
         boolean var10 = false;

         for(var11 = 0; var11 < var8.length; ++var11) {
            Material var12 = Material.valueOf(var8[var11].strip());
            if (var12 == null) {
               Bukkit.getLogger().severe("[RealisticSeasons] Warning: unknown Material type in leaf litter list: " + var8[var11]);
               var10 = true;
               break;
            }

            var9[var11] = var12;
         }

         if (!var10) {
            this.spawnLeafLitterUnder = var9;
         }
      }

      if (this.spawnFoxesInFall) {
         this.fallAnimals.add(EntityType.FOX);
      }

      this.fullFallAnimals.add(EntityType.FOX);
      if (this.spawnFrogsInFall && Version.is_1_19_or_up()) {
         this.fallAnimals.add(EntityType.FROG);
      }

      if (Version.is_1_19_or_up()) {
         this.fullFallAnimals.add(EntityType.FROG);
      }

      if (this.spawnMooshroomInFall) {
         this.fallAnimals.add(EntityType.MUSHROOM_COW);
      }

      this.fullFallAnimals.add(EntityType.MUSHROOM_COW);
      if (this.spawnExtraAnimalsInSpring) {
         this.springAnimals.add(EntityType.BEE);
      }

      this.fullSpringAnimals.add(EntityType.BEE);
      if (this.spawnExtraRabitsInSpring) {
         this.springAnimals.add(EntityType.RABBIT);
      }

      this.fullSpringAnimals.add(EntityType.RABBIT);
      if (this.spawnOcelotsInSummer) {
         this.summerAnimals.add(EntityType.OCELOT);
      }

      this.fullSummerAnimals.add(EntityType.OCELOT);
      if (this.spawnPandasInSummer) {
         this.summerAnimals.add(EntityType.PANDA);
      }

      this.fullSummerAnimals.add(EntityType.PANDA);
      if (this.spawnParrotsInSummer) {
         this.summerAnimals.add(EntityType.PARROT);
      }

      this.fullSummerAnimals.add(EntityType.PARROT);
      if (this.spawnWolfsInWinter) {
         this.winterAnimals.add(EntityType.WOLF);
      }

      this.fullWinterAnimals.add(EntityType.WOLF);
      if (this.spawnFoxesInWinter) {
         this.winterAnimals.add(EntityType.FOX);
      }

      this.fullWinterAnimals.add(EntityType.FOX);
      if (this.spawnPolarBearsInWinter) {
         this.winterAnimals.add(EntityType.POLAR_BEAR);
      }

      this.fullWinterAnimals.add(EntityType.POLAR_BEAR);
      if (this.spawnSnowmanInWinter) {
         this.winterAnimals.add(EntityType.SNOWMAN);
      }

      this.fullWinterAnimals.add(EntityType.SNOWMAN);
      if (this.spawnExtraAnimalsInSpring) {
         this.springAnimals.add(EntityType.COW);
         this.springAnimals.add(EntityType.SHEEP);
         this.springAnimals.add(EntityType.CHICKEN);
         this.springAnimals.add(EntityType.PIG);
      }

      this.fullSpringAnimals.add(EntityType.COW);
      this.fullSpringAnimals.add(EntityType.SHEEP);
      this.fullSpringAnimals.add(EntityType.CHICKEN);
      this.fullSpringAnimals.add(EntityType.PIG);
      this.fallingLeavesEnabled = this.config.getBoolean("particles.FALLING_LEAVES.enabled");
      this.fallingLeavesChance = Float.valueOf(this.config.getString("particles.FALLING_LEAVES.spawn-chance").replaceAll("%", ""));
      this.fallingLeavesSeasons = this.seasonListFromStringList(this.config.getStringList("particles.FALLING_LEAVES.seasons"));
      this.smallFallingLeavesEnabled = this.config.getBoolean("particles.SMALL_FALLING_LEAVES.enabled");
      this.smallFallingLeavesChance = Float.valueOf(this.config.getString("particles.FALLING_LEAVES.spawn-chance").replaceAll("%", ""));
      this.smallFallingLeavesSeasons = this.seasonListFromStringList(this.config.getStringList("particles.SMALL_FALLING_LEAVES.seasons"));
      this.fallingStarsEnabled = this.config.getBoolean("particles.SHOOTING_STARS.enabled");
      this.fallingStarsSeasons = this.seasonListFromStringList(this.config.getStringList("particles.SHOOTING_STARS.seasons"));
      this.fallingStarsHeight = this.config.getInt("particles.SHOOTING_STARS.relative-height");
      this.fallingStarsChance = Float.valueOf(this.config.getString("particles.SHOOTING_STARS.chance").replaceAll("%", ""));
      this.nightSparksEnabled = this.config.getBoolean("particles.NIGHT_SPARKS.enabled");
      this.nightSparksSeasons = this.seasonListFromStringList(this.config.getStringList("particles.NIGHT_SPARKS.seasons"));
      this.nightSparksParticleCount = this.config.getInt("particles.NIGHT_SPARKS.particle-count");
      this.nightSparksHeight = this.config.getInt("particles.NIGHT_SPARKS.relative-height");
      this.nightSparksChance = Float.valueOf(this.config.getString("particles.NIGHT_SPARKS.event-chance").replaceAll("%", ""));
      this.fireFliesEnabled = this.config.getBoolean("particles.FIREFLIES.enabled");
      this.fireFliesSeasons = this.seasonListFromStringList(this.config.getStringList("particles.FIREFLIES.seasons"));
      this.fireFliesChance = Float.valueOf(this.config.getString("particles.FIREFLIES.spawn-chance").replaceAll("%", ""));
      this.sweatEffectEnabled = this.config.getBoolean("particles.SWEAT_EFFECT.enabled");
      this.sweatMinTemperature = this.config.getInt("particles.SWEAT_EFFECT.min-player-temperature");
      this.coldBreathEnabled = this.config.getBoolean("particles.COLD_BREATH.enabled");
      this.coldBreathHideOwn = this.config.getBoolean("particles.COLD_BREATH.hide-own-breath");
      this.coldBreathMaxTemperature = this.config.getInt("particles.COLD_BREATH.max-air-temperature");
      Season[] var31 = new Season[]{Season.FALL, Season.SUMMER, Season.SPRING, Season.WINTER};
      Season[] var32 = var31;
      int var34 = var31.length;

      for(var11 = 0; var11 < var34; ++var11) {
         Season var36 = var32[var11];
         double var13 = Double.valueOf(this.config.getString("weather." + var36.getConfigName().toLowerCase() + ".daily-extra-downfall-chance").replaceAll("%", ""));
         boolean var15 = this.config.getBoolean("weather." + var36.getConfigName().toLowerCase() + ".has-natural-downfall");
         this.weathersettings.put(var36, new Settings.WeatherSettings(var13, var15));
      }

      if (this.config.isConfigurationSection("biomes")) {
         this.config.set("biomes", (Object)null);
      }

      if (this.config.contains("length-in-minecraft-days-winter")) {
         this.config.set("length-in-minecraft-days-winter", (Object)null);
      }

      if (this.config.contains("length-in-minecraft-days-fall")) {
         this.config.set("length-in-minecraft-days-fall", (Object)null);
      }

      if (this.config.contains("length-in-minecraft-days-spring")) {
         this.config.set("length-in-minecraft-days-spring", (Object)null);
      }

      if (this.config.contains("length-in-minecraft-days-summer")) {
         this.config.set("length-in-minecraft-days-summer", (Object)null);
      }

      if (this.config.contains("affect-crop-growth")) {
         this.config.set("affect-crop-growth-summer", this.config.getBoolean("affect-crop-growth"));
         this.config.set("affect-crop-growth-winter", this.config.getBoolean("affect-crop-growth"));
         this.config.set("affect-crop-growth", (Object)null);
      }

      if (this.config.contains("dynmap-pause-renders-in-winter")) {
         this.config.set("pause-map-plugins-render-in-winter", this.config.getBoolean("dynmap-pause-renders-in-winter"));
         this.config.set("dynmap-pause-renders-in-winter", (Object)null);
      }

      this.pauseMapRenderInWinter = this.config.getBoolean("pause-map-plugins-render-in-winter");
      if (this.config.isConfigurationSection("block-replacements") && this.config.getBoolean("block-replacements.enabled")) {
         Iterator var33 = this.config.getConfigurationSection("block-replacements.replacements").getKeys(false).iterator();

         label231:
         while(true) {
            while(true) {
               String var14;
               String var37;
               String var38;
               String var39;
               do {
                  String var35;
                  do {
                     if (!var33.hasNext()) {
                        break label231;
                     }

                     var35 = (String)var33.next();
                  } while(!this.config.getBoolean("block-replacements.replacements." + var35 + ".enabled"));

                  var37 = this.config.getString("block-replacements.replacements." + var35 + ".to-replace");
                  var38 = this.config.getString("block-replacements.replacements." + var35 + ".replacement");
                  var39 = this.config.getString("block-replacements.replacements." + var35 + ".seasons");
                  var14 = this.config.getString("block-replacements.replacements." + var35 + ".sub-seasons");
               } while(var37.equalsIgnoreCase("CHERRY_LEAVES") && !Version.is_1_20_or_up());

               Material var40 = Material.getMaterial(var37);
               if (var40 == null) {
                  Bukkit.getLogger().info("[RealisticSeasons] Unknown Material in config.yml: " + var37 + ". Ignoring section.");
               } else if (!var38.equalsIgnoreCase("CHERRY_LEAVES") || Version.is_1_20_or_up()) {
                  Material var16 = Material.getMaterial(var38);
                  if (var16 == null) {
                     Bukkit.getLogger().info("[RealisticSeasons] Unknown Material in config.yml: " + var38 + ". Ignoring section.");
                  } else {
                     Object var17;
                     if (var39.contains(",")) {
                        String[] var18 = var39.split(",");
                        var17 = new ArrayList();

                        for(int var19 = 0; var19 < var18.length; ++var19) {
                           ((List)var17).add(Season.valueOf(var18[var19].trim().toUpperCase()).intValue());
                        }
                     } else {
                        var17 = Arrays.asList(Season.valueOf(var39.trim().toUpperCase()).intValue());
                     }

                     Object var41;
                     if (var14.contains(",")) {
                        String[] var42 = var14.split(",");
                        var41 = new ArrayList();

                        for(int var20 = 0; var20 < var42.length; ++var20) {
                           ((List)var41).add(Integer.valueOf(var42[var20].trim()));
                        }
                     } else {
                        var41 = Arrays.asList(Integer.valueOf(var14.trim()));
                     }

                     BlockReplacements.addBlockReplacement(new BlockReplacement((List)var17, (List)var41, var40, var16));
                  }
               }
            }
         }
      }

      this.canCheckUnloadedChunks = this.config.getBoolean("remove-ice-in-unloaded-chunks");
      if (this.config.contains("affect-crop-growth-winter")) {
         this.affectCropsWinter = this.config.getBoolean("affect-crop-growth-winter");
         this.config.set("affect-crop-growth-winter", (Object)null);
      } else {
         this.affectCropsWinter = true;
      }

      if (this.config.contains("affect-crop-growth-summer")) {
         this.affectCropsSummer = this.config.getBoolean("affect-crop-growth-summer");
         this.config.set("affect-crop-growth-summer", (Object)null);
      } else {
         this.affectCropsSummer = true;
      }

      this.config.set("version", this.main.getDescription().getVersion());

      try {
         this.config.save(this.main.configFile);
      } catch (IOException var21) {
         var21.printStackTrace();
      }

   }

   public void addSyncedWorld(String var1) {
      this.syncedWorlds.add(var1);
      File var2 = new File(this.main.getDataFolder(), "calendar.yml");
      if (var2.exists()) {
         YamlConfiguration var3 = YamlConfiguration.loadConfiguration(var2);
         if (var3.contains("synced-worlds")) {
            var3.set("synced-worlds", this.syncedWorlds);

            try {
               var3.save(var2);
            } catch (IOException var5) {
               var5.printStackTrace();
            }
         }
      }

   }

   private boolean isOldConfig(String var1, String var2) {
      if (var1 == null) {
         return true;
      } else if (var1.equals("")) {
         return true;
      } else if (var1.equals("1.0")) {
         return true;
      } else {
         String[] var3 = var1.split("\\.");
         String[] var4 = var2.split("\\.");
         int var5 = Integer.valueOf(var3[0]);
         int var6 = Integer.valueOf(var3[1]);
         int var7 = Integer.valueOf(var4[0]);
         int var8 = Integer.valueOf(var4[1]);
         if (var5 < var7) {
            return true;
         } else if (var5 > var7) {
            return false;
         } else {
            return var6 < var8;
         }
      }
   }

   private String convertChanceToPercentage(int var1) {
      float var2 = 1.0F / (float)var1 * 100.0F;
      return var2 + "%";
   }

   private List<Season> seasonListFromStringList(List<String> var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         var2.add(Season.valueOf(var4.toUpperCase()));
      }

      return var2;
   }

   public class WeatherSettings {
      private double downfall;
      private boolean rain;

      public WeatherSettings(double param2, boolean param4) {
         this.downfall = var2;
         this.rain = var4;
      }

      public double getDownfallChance() {
         return this.downfall;
      }

      public boolean hasRain() {
         return this.rain;
      }
   }
}
