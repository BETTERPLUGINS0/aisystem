package me.casperge.realisticseasons.temperature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.blockscanner.BlockEffect;
import me.casperge.realisticseasons.season.Season;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;

public class TemperatureSettings {
   private YamlConfiguration tempfile;
   private final Season[] seasons;
   private int temperatureUpdateInterval;
   private HashMap<Season, Integer> minTemperature;
   private HashMap<Season, Integer> maxTemperature;
   private HashMap<TemperatureSettings.Armor, Integer> armorModifier;
   private HashMap<TemperatureSettings.Weather, Integer> weatherModifier;
   private List<CustomTemperatureItem> customItems;
   private final String[] resilient;
   private final String[] immuneToCold;
   private final String[] immuneToHeat;
   private final String[] noTemperature;
   private HashMap<Season, Integer> swimmingModifier;
   private int waterBottleModifier;
   private int fullHungerModifier;
   private int maxSprintingModifier;
   private double velocityModifier;
   private boolean heightModifying;
   private int leatherCap;
   private boolean blockEffectsEnabled;
   private float temperatureChangeRate;
   public HashMap<Material, BlockEffect> blockEffects;
   private int coldSlownessTemp;
   private int coldHungerTemp;
   private int coldFreezingTemp;
   private int warmNoHealingTemp;
   private int warmSlownessTemp;
   private int warmFireTemp;
   private int coldSlownessLevel;
   private int coldHungerLevel;
   private int warmSlownessLevel;
   private PotionEffectType coldSlownessType;
   private PotionEffectType coldHungerType;
   private PotionEffectType warmSlownessType;
   private int boostMinTemp;
   private int boostMaxTemp;
   private int waterBottleEffectDuration;
   private List<PotionEffectType> boostEffects;
   private boolean displayTempOnActionBar;
   private boolean overwriteActionbar;
   private String actionbarDisplay;
   private boolean messageWarningsEnabled;
   private String messageOverheatingWarning;
   private String messageFreezingWarning;
   private String actionbarOverheatingWarning;
   private String actionbarFreezingWarning;
   private boolean actionbarWarningEnabled;
   private boolean convertToFahrenheit;
   private int netherTemperature;
   private int endTemperature;
   private String fahrenheit;
   private String celcius;
   private int lavaTemp;
   private boolean ipBasedTemperature;
   private boolean hasChanges;
   private int boostsMinHunger;
   private PotionEffectType hydrationEffect;
   private int hydrationLevel;
   private boolean reducedMovementSpeedEnabled;
   private int coldMovementStart;
   private int coldMovementEnd;
   private int warmMovementStart;
   private int warmMovementEnd;
   private double heightBlockIncrease;
   private HashMap<String, Double> heightBlockIncreaseOverwrites;
   private List<EntityType> entitiesImmuneToHeat;
   private List<EntityType> entitiesImmuneToCold;
   private List<EntityType> entitiesResilient;
   private List<EntityType> entitiesWithoutTemperature;
   private boolean entityTemperatureEnabled;
   private int entityColdSlownessStart;
   private PotionEffectType entityColdSlownessEffect;
   private int entityColdSlownessLevel;
   private int entityColdFreezing;
   private int entityWarmSlownessStart;
   private PotionEffectType entityWarmSlownessEffect;
   private int entityWarmSlownessLevel;
   private int entityWarmBurn;

   public TemperatureSettings(YamlConfiguration var1) {
      this.seasons = new Season[]{Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER};
      this.minTemperature = new HashMap();
      this.maxTemperature = new HashMap();
      this.armorModifier = new HashMap();
      this.weatherModifier = new HashMap();
      this.customItems = new ArrayList();
      this.resilient = new String[]{"ALLAY", "ARMADILLO", "BAT", "CAMEL", "CAT", "CHICKEN", "COW", "DONKEY", "HORSE", "MOOSHROOM", "MULE", "OCELOT", "PARROT", "PIG", "RABBIT", "SHEEP", "SNIFFER", "BEE", "FOX", "GOAT", "LLAMA", "TRADER_LLAMA", "PANDA", "SILVERFISH", "EVOKER", "ILLUSIONER", "VINDICATOR", "PILLAGER", "VEX", "VILLAGER", "WITCH", "WANDERING_TRADER"};
      this.immuneToCold = new String[]{"AXOLOTL", "COD", "FROG", "GLOW_SQUID", "PUFFERFISH", "SALMON", "SQUID", "SNOW_GOLEM", "TADPOLE", "TROPICAL_FISH", "TURTLE", "DOLPHIN", "POLAR_BEAR", "WOLF", "GUARDIAN", "ELDER_GUARDIAN", "ZOMBIE_HORSE", "GIANT", "DROWNED", "BOGGED", "HUSK", "PHANTOM", "ZOMBIE", "ZOMBIE_VILLAGER", "ZOGLIN", "ZOMBIFIED_PIGLIN"};
      this.immuneToHeat = new String[]{"STRIDER", "PIGLIN", "BLAZE", "HOGLIN", "MAGMA_CUBE", "PIGLIN_BRUTE"};
      this.noTemperature = new String[]{"BREEZE", "CREAKING", "ENDER_DRAGON", "ENDERMAN", "ENDERMITE", "GHAST", "RAVAGER", "SHULKER", "SLIME", "SKELETON", "STRAY", "SKELETON_HORSE", "WARDEN", "WITHER", "WITHER_SKELETON"};
      this.swimmingModifier = new HashMap();
      this.blockEffects = new HashMap();
      this.boostEffects = new ArrayList();
      this.tempfile = var1;
      this.load();
   }

   public void load() {
      this.hasChanges = false;
      int var1;
      if (this.tempfile.isInt("effects.cold.hunger")) {
         var1 = this.tempfile.getInt("effects.cold.hunger");
         this.tempfile.set("effects.cold.hunger", (Object)null);
         this.tempfile.set("effects.cold.hunger.temperature", var1);
         this.tempfile.set("effects.cold.hunger.effect", "HUNGER");
         this.tempfile.set("effects.cold.hunger.level", 2);
         this.hasChanges = true;
      }

      if (!this.tempfile.contains("display.overwrite-actionbar")) {
         this.tempfile.set("display.overwrite-actionbar", false);
         this.hasChanges = true;
      }

      if (this.tempfile.isInt("effects.cold.slowness")) {
         var1 = this.tempfile.getInt("effects.cold.slowness");
         this.tempfile.set("effects.cold.slowness", (Object)null);
         this.tempfile.set("effects.cold.slowness.temperature", var1);
         this.tempfile.set("effects.cold.slowness.effect", "SLOWNESS");
         this.tempfile.set("effects.cold.slowness.level", 3);
         this.hasChanges = true;
      }

      if (this.tempfile.isInt("effects.warm.slowness")) {
         var1 = this.tempfile.getInt("effects.warm.slowness");
         this.tempfile.set("effects.warm.slowness", (Object)null);
         this.tempfile.set("effects.warm.slowness.temperature", var1);
         this.tempfile.set("effects.warm.slowness.effect", "SLOWNESS");
         this.tempfile.set("effects.warm.slowness.level", 3);
         this.hasChanges = true;
      }

      if (!this.tempfile.contains("temperature-change-rate")) {
         this.tempfile.set("temperature-change-rate", 0.2D);
         this.hasChanges = true;
      }

      this.temperatureChangeRate = (float)this.tempfile.getDouble("temperature-change-rate");
      if (!this.tempfile.contains("entity-temperature")) {
         this.tempfile.set("entity-temperature.enabled", false);
         this.tempfile.set("entity-temperature.entity-effects.cold.slowness.temperature", -15);
         this.tempfile.set("entity-temperature.entity-effects.cold.slowness.effect", "SLOWNESS");
         this.tempfile.set("entity-temperature.entity-effects.cold.slowness.level", 3);
         this.tempfile.set("entity-temperature.entity-effects.cold.freezing", -20);
         this.tempfile.set("entity-temperature.entity-effects.warm.slowness.temperature", 60);
         this.tempfile.set("entity-temperature.entity-effects.warm.slowness.effect", "SLOWNESS");
         this.tempfile.set("entity-temperature.entity-effects.warm.slowness.level", 3);
         this.tempfile.set("entity-temperature.entity-effects.warm.fire", 65);
         this.tempfile.set("entity-temperature.entities.resilient", this.resilient);
         this.tempfile.set("entity-temperature.entities.immune-to-cold", this.immuneToCold);
         this.tempfile.set("entity-temperature.entities.immune-to-heat", this.immuneToHeat);
         this.tempfile.set("entity-temperature.entities.no-temperature", this.noTemperature);
         this.hasChanges = true;
      }

      this.entityTemperatureEnabled = this.tempfile.getBoolean("entity-temperature.enabled");
      this.entityColdSlownessStart = this.tempfile.getInt("entity-temperature.entity-effects.cold.slowness.temperature");
      if (!this.tempfile.getString("entity-temperature.entity-effects.cold.slowness.effect").equalsIgnoreCase("none")) {
         this.entityColdSlownessEffect = PotionEffectType.getByName(this.tempfile.getString("entity-temperature.entity-effects.cold.slowness.effect"));
      } else {
         this.entityColdSlownessEffect = null;
      }

      this.entityColdSlownessLevel = this.tempfile.getInt("entity-temperature.entity-effects.cold.slowness.level");
      this.entityColdFreezing = this.tempfile.getInt("entity-temperature.entity-effects.cold.freezing");
      this.entityWarmSlownessStart = this.tempfile.getInt("entity-temperature.entity-effects.warm.slowness.temperature");
      if (!this.tempfile.getString("entity-temperature.entity-effects.warm.slowness.effect").equalsIgnoreCase("none")) {
         this.entityWarmSlownessEffect = PotionEffectType.getByName(this.tempfile.getString("entity-temperature.entity-effects.warm.slowness.effect"));
      } else {
         this.entityWarmSlownessEffect = null;
      }

      this.entityWarmSlownessLevel = this.tempfile.getInt("entity-temperature.entity-effects.warm.slowness.level");
      this.entityWarmBurn = this.tempfile.getInt("entity-temperature.entity-effects.warm.fire");
      this.entitiesImmuneToCold = new ArrayList();
      ArrayList var7 = new ArrayList();
      EntityType[] var2 = EntityType.values();
      int var3 = var2.length;

      int var4;
      for(var4 = 0; var4 < var3; ++var4) {
         EntityType var5 = var2[var4];
         var7.add(var5.name());
      }

      Iterator var8 = this.tempfile.getStringList("entity-temperature.entities.immune-to-cold").iterator();

      String var9;
      EntityType var12;
      while(var8.hasNext()) {
         var9 = (String)var8.next();
         if (!var9.equalsIgnoreCase("none") && var7.contains(var9.toUpperCase())) {
            var12 = EntityType.valueOf(var9);
            if (var12 != null) {
               this.entitiesImmuneToCold.add(var12);
            }
         }
      }

      this.entitiesImmuneToHeat = new ArrayList();
      var8 = this.tempfile.getStringList("entity-temperature.entities.immune-to-heat").iterator();

      while(var8.hasNext()) {
         var9 = (String)var8.next();
         if (!var9.equalsIgnoreCase("none") && var7.contains(var9.toUpperCase())) {
            var12 = EntityType.valueOf(var9);
            if (var12 != null) {
               this.entitiesImmuneToHeat.add(var12);
            }
         }
      }

      this.entitiesResilient = new ArrayList();
      var8 = this.tempfile.getStringList("entity-temperature.entities.resilient").iterator();

      while(var8.hasNext()) {
         var9 = (String)var8.next();
         if (!var9.equalsIgnoreCase("none") && var7.contains(var9.toUpperCase())) {
            var12 = EntityType.valueOf(var9);
            if (var12 != null) {
               this.entitiesResilient.add(var12);
            }
         }
      }

      this.entitiesWithoutTemperature = new ArrayList();
      var8 = this.tempfile.getStringList("entity-temperature.entities.no-temperature").iterator();

      while(var8.hasNext()) {
         var9 = (String)var8.next();
         if (!var9.equalsIgnoreCase("none") && var7.contains(var9.toUpperCase())) {
            var12 = EntityType.valueOf(var9);
            if (var12 != null) {
               this.entitiesWithoutTemperature.add(var12);
            }
         }
      }

      Season[] var10 = this.seasons;
      var3 = var10.length;

      for(var4 = 0; var4 < var3; ++var4) {
         Season var13 = var10[var4];
         this.minTemperature.put(var13, this.tempfile.getInt("modifiers.season-base-temperature." + var13.getConfigName().toLowerCase() + ".min-temp"));
         this.maxTemperature.put(var13, this.tempfile.getInt("modifiers.season-base-temperature." + var13.getConfigName().toLowerCase() + ".max-temp"));
         this.swimmingModifier.put(var13, this.tempfile.getInt("modifiers.touching-water." + var13.getConfigName().toLowerCase()));
      }

      this.lavaTemp = this.tempfile.getInt("modifiers.lava-temperature");
      this.armorModifier.put(TemperatureSettings.Armor.LEATHER, this.tempfile.getInt("modifiers.armor.leather"));
      this.armorModifier.put(TemperatureSettings.Armor.IRON, this.tempfile.getInt("modifiers.armor.iron"));
      this.armorModifier.put(TemperatureSettings.Armor.GOLD, this.tempfile.getInt("modifiers.armor.gold"));
      this.armorModifier.put(TemperatureSettings.Armor.DIAMOND, this.tempfile.getInt("modifiers.armor.diamond"));
      this.armorModifier.put(TemperatureSettings.Armor.NETHERITE, this.tempfile.getInt("modifiers.armor.netherite"));
      this.leatherCap = this.tempfile.getInt("modifiers.armor.leather-temperature-cap");
      this.weatherModifier.put(TemperatureSettings.Weather.RAIN, this.tempfile.getInt("modifiers.weather.rain"));
      this.weatherModifier.put(TemperatureSettings.Weather.STORM, this.tempfile.getInt("modifiers.weather.storm"));
      this.velocityModifier = this.tempfile.getDouble("modifiers.velocity-per-ms");
      this.waterBottleModifier = this.tempfile.getInt("modifiers.foods-and-drinks.water-bottle");
      this.fullHungerModifier = this.tempfile.getInt("modifiers.foods-and-drinks.full-hunger-bar");
      this.heightModifying = this.tempfile.getBoolean("modifiers.height.enabled");
      this.waterBottleEffectDuration = this.tempfile.getInt("modifiers.foods-and-drinks.water-bottle-effect-duration");
      this.blockEffectsEnabled = this.tempfile.getBoolean("modifiers.block-effects.enabled");
      this.blockEffects.clear();
      var8 = this.tempfile.getStringList("modifiers.block-effects.blocks").iterator();

      while(var8.hasNext()) {
         var9 = (String)var8.next();
         Material var18 = Material.valueOf(var9.split("\\{")[0]);
         if (var18 == null) {
            Bukkit.getLogger().severe("[RealisticSeasons] Could not load block effect: Material " + var9.split("\\{")[0] + " could not be found");
         } else {
            BlockEffect var15 = this.blockEffectFromString(var9.split("\\{")[1]);
            this.blockEffects.put(var18, var15);
         }
      }

      this.coldSlownessTemp = this.tempfile.getInt("effects.cold.slowness.temperature");
      this.coldHungerTemp = this.tempfile.getInt("effects.cold.hunger.temperature");
      this.coldFreezingTemp = this.tempfile.getInt("effects.cold.freezing");
      this.temperatureUpdateInterval = this.tempfile.getInt("temperature-update-interval");
      this.warmNoHealingTemp = this.tempfile.getInt("effects.warm.no-healing");
      this.warmSlownessTemp = this.tempfile.getInt("effects.warm.slowness.temperature");
      this.warmFireTemp = this.tempfile.getInt("effects.warm.fire");
      this.coldSlownessLevel = this.tempfile.getInt("effects.cold.slowness.level");
      this.coldHungerLevel = this.tempfile.getInt("effects.cold.hunger.level");
      this.warmSlownessLevel = this.tempfile.getInt("effects.warm.slowness.level");
      if (!this.tempfile.getString("effects.cold.slowness.effect").equalsIgnoreCase("none")) {
         this.coldSlownessType = PotionEffectType.getByName(this.tempfile.getString("effects.cold.slowness.effect"));
      } else {
         this.coldSlownessType = null;
      }

      if (!this.tempfile.getString("effects.cold.hunger.effect").equalsIgnoreCase("none")) {
         this.coldHungerType = PotionEffectType.getByName(this.tempfile.getString("effects.cold.hunger.effect"));
      } else {
         this.coldHungerType = null;
      }

      if (!this.tempfile.getString("effects.warm.slowness.effect").equalsIgnoreCase("none")) {
         this.warmSlownessType = PotionEffectType.getByName(this.tempfile.getString("effects.warm.slowness.effect"));
      } else {
         this.coldHungerType = null;
      }

      if (!this.tempfile.contains("modifiers.foods-and-drinks.hydration-buff")) {
         this.tempfile.set("modifiers.foods-and-drinks.hydration-buff.effect", "NONE");
         this.tempfile.set("modifiers.foods-and-drinks.hydration-buff.level", 0);
      }

      String var11 = this.tempfile.getString("modifiers.foods-and-drinks.hydration-buff.effect").trim();
      if (var11 != null && !var11.equals("") && !var11.equalsIgnoreCase("none")) {
         this.hydrationEffect = PotionEffectType.getByName(var11);
      }

      this.hydrationLevel = this.tempfile.getInt("modifiers.foods-and-drinks.hydration-buff.level");
      this.netherTemperature = this.tempfile.getInt("modifiers.other-dimensions.nether");
      this.endTemperature = this.tempfile.getInt("modifiers.other-dimensions.end");
      this.maxSprintingModifier = this.tempfile.getInt("modifiers.max-sprinting-modifier");
      this.celcius = this.tempfile.getString("display.temperature.celcius");
      this.fahrenheit = this.tempfile.getString("display.temperature.fahrenheit");
      this.boostMinTemp = this.tempfile.getInt("effects.boosts.min");
      this.boostMaxTemp = this.tempfile.getInt("effects.boosts.max");
      Iterator var14 = this.tempfile.getStringList("effects.boosts.potioneffects").iterator();

      String var19;
      while(var14.hasNext()) {
         var19 = (String)var14.next();
         if (!var19.equalsIgnoreCase("NONE")) {
            PotionEffectType var16 = PotionEffectType.getByName(var19);
            if (var16 != null) {
               this.boostEffects.add(var16);
            } else {
               Bukkit.getLogger().warning("Could not find potion effect: " + var19 + " located in the temperature.yml file");
            }
         }
      }

      if (!this.tempfile.contains("effects.boosts.min-hunger")) {
         this.tempfile.set("effects.boosts.min-hunger", 7);
         this.hasChanges = true;
      }

      this.boostsMinHunger = this.tempfile.getInt("effects.boosts.min-hunger");
      var14 = this.tempfile.getConfigurationSection("custom.items").getKeys(false).iterator();

      while(var14.hasNext()) {
         var19 = (String)var14.next();
         if (this.tempfile.getBoolean("custom.items." + var19 + ".enabled")) {
            this.customItems.add(this.fromConfigurationSection(this.tempfile.getConfigurationSection("custom.items." + var19)));
         }
      }

      this.displayTempOnActionBar = this.tempfile.getBoolean("display.temperature.actionbar");
      this.actionbarDisplay = this.tempfile.getString("display.temperature.actionbar-display").replaceAll("ï¿½C", "").replaceAll("ï¿½F", "");
      this.messageWarningsEnabled = this.tempfile.getBoolean("display.warnings.messages.enabled");
      this.messageOverheatingWarning = this.tempfile.getString("display.warnings.messages.overheating");
      this.messageFreezingWarning = this.tempfile.getString("display.warnings.messages.freezing");
      this.actionbarWarningEnabled = this.tempfile.getBoolean("display.warnings.actionbar.enabled");
      this.actionbarOverheatingWarning = this.tempfile.getString("display.warnings.actionbar.overheating");
      this.actionbarFreezingWarning = this.tempfile.getString("display.warnings.actionbar.freezing");
      this.convertToFahrenheit = this.tempfile.getBoolean("display.convert-to-fahrenheit");
      this.ipBasedTemperature = this.tempfile.getBoolean("display.ip-location-temp-type");
      if (!this.tempfile.contains("effects.reduce-movement-speed")) {
         this.hasChanges = true;
         this.tempfile.set("effects.reduce-movement-speed.enabled", false);
         this.tempfile.set("effects.reduce-movement-speed.cold.below", -15);
         this.tempfile.set("effects.reduce-movement-speed.cold.until", -100);
         this.tempfile.set("effects.reduce-movement-speed.warm.above", 50);
         this.tempfile.set("effects.reduce-movement-speed.warm.until", 300);
      }

      if (!this.tempfile.contains("modifiers.height.per-block")) {
         this.hasChanges = true;
         this.tempfile.set("modifiers.height.per-block", 0.08D);
         this.tempfile.set("modifiers.height.overwrites.example-world", 0.02D);
      }

      this.overwriteActionbar = this.tempfile.getBoolean("display.overwrite-actionbar");
      this.heightBlockIncrease = this.tempfile.getDouble("modifiers.height.per-block");
      this.heightBlockIncreaseOverwrites = new HashMap();
      var14 = this.tempfile.getConfigurationSection("modifiers.height.overwrites").getKeys(false).iterator();

      while(var14.hasNext()) {
         var19 = (String)var14.next();
         this.heightBlockIncreaseOverwrites.put(var19, this.tempfile.getDouble("modifiers.height.overwrites." + var19 + ".per-block"));
      }

      this.reducedMovementSpeedEnabled = this.tempfile.getBoolean("effects.reduce-movement-speed.enabled");
      this.coldMovementStart = this.tempfile.getInt("effects.reduce-movement-speed.cold.below");
      this.coldMovementEnd = this.tempfile.getInt("effects.reduce-movement-speed.cold.until");
      this.warmMovementStart = this.tempfile.getInt("effects.reduce-movement-speed.warm.above");
      this.warmMovementEnd = this.tempfile.getInt("effects.reduce-movement-speed.warm.until");
      if (this.hasChanges) {
         this.hasChanges = false;
         File var17 = new File(RealisticSeasons.getInstance().getDataFolder(), "temperature.yml");

         try {
            this.tempfile.save(var17);
         } catch (IOException var6) {
            var6.printStackTrace();
         }
      }

   }

   public int getMinTemperature(Season var1) {
      return this.minTemperature.get(var1) == null ? (Integer)this.minTemperature.get(Season.SPRING) : (Integer)this.minTemperature.get(var1);
   }

   public int getMaxTemperature(Season var1) {
      return this.maxTemperature.get(var1) == null ? (Integer)this.maxTemperature.get(Season.SPRING) : (Integer)this.maxTemperature.get(var1);
   }

   public int getArmorModifier(TemperatureSettings.Armor var1) {
      return (Integer)this.armorModifier.get(var1);
   }

   public int getWeatherModifier(TemperatureSettings.Weather var1) {
      return (Integer)this.weatherModifier.get(var1);
   }

   public List<CustomTemperatureItem> getCustomItems() {
      return this.customItems;
   }

   public int getTemperatureUpdateInterval() {
      return this.temperatureUpdateInterval;
   }

   public int getSwimmingModifier(Season var1) {
      return this.swimmingModifier.get(var1) == null ? (Integer)this.swimmingModifier.get(Season.SPRING) : (Integer)this.swimmingModifier.get(var1);
   }

   public List<PotionEffectType> getBoostPotionEffects() {
      return this.boostEffects;
   }

   public String getActionbarFreezingWarning() {
      return this.actionbarFreezingWarning;
   }

   public String getActionbarOverheatingWarning() {
      return this.actionbarOverheatingWarning;
   }

   public HashMap<String, Double> getHeightBlockIncreaseOverwrites() {
      return this.heightBlockIncreaseOverwrites;
   }

   public String getFahrenheitMessage() {
      return this.fahrenheit;
   }

   public String getCelciusMessage() {
      return this.celcius;
   }

   public boolean isActionbarWarningEnabled() {
      return this.actionbarWarningEnabled;
   }

   public String getMessageFreezingWarning() {
      return this.messageFreezingWarning;
   }

   public String getMessageOverheatingWarning() {
      return this.messageOverheatingWarning;
   }

   public boolean isMessageWarningsEnabled() {
      return this.messageWarningsEnabled;
   }

   public String getActionbarDisplay() {
      return this.actionbarDisplay;
   }

   public boolean isDisplayTempOnActionBar() {
      return this.displayTempOnActionBar;
   }

   public int getBoostMaxTemp() {
      return this.boostMaxTemp;
   }

   public int getBoostMinTemp() {
      return this.boostMinTemp;
   }

   public int getWarmFireTemp() {
      return this.warmFireTemp;
   }

   public int getWarmSlownessTemp() {
      return this.warmSlownessTemp;
   }

   public int getWarmNoHealingTemp() {
      return this.warmNoHealingTemp;
   }

   public int getColdFreezingTemp() {
      return this.coldFreezingTemp;
   }

   public int getColdHungerTemp() {
      return this.coldHungerTemp;
   }

   public int getColdSlownessTemp() {
      return this.coldSlownessTemp;
   }

   public boolean isHeightModifying() {
      return this.heightModifying;
   }

   public int getWaterBottleModifier() {
      return this.waterBottleModifier;
   }

   public int getFullHungerModifier() {
      return this.fullHungerModifier;
   }

   public boolean blockEffectsEnabled() {
      return this.blockEffectsEnabled;
   }

   public boolean isConvertToFahrenheit() {
      return this.convertToFahrenheit;
   }

   public int getMaxSprintingModifier() {
      return this.maxSprintingModifier;
   }

   public int getNetherTemperature() {
      return this.netherTemperature;
   }

   public int getEndTemperature() {
      return this.endTemperature;
   }

   public boolean hasBlockEffect(Material var1) {
      return this.blockEffects.containsKey(var1);
   }

   public BlockEffect getBlockEffect(Material var1) {
      return (BlockEffect)this.blockEffects.get(var1);
   }

   private CustomTemperatureItem fromConfigurationSection(ConfigurationSection var1) {
      Material var2 = Material.valueOf(var1.getString("material").toUpperCase());
      if (var2 == null) {
         Bukkit.getLogger().severe("[RealisticSeasons] Material " + var1.getString("material") + " could not be found during temperature item creation.");
         return null;
      } else {
         int var3 = var1.getInt("custom-model-data");
         int var4 = var1.getInt("temperature-modifier");
         boolean var5 = var1.getBoolean("activation.wearing");
         boolean var6 = var1.getBoolean("activation.holding");
         boolean var7 = var1.getBoolean("use-custom-model-data");
         boolean var8 = true;
         if (var1.contains("works-underwater")) {
            var8 = var1.getBoolean("works-underwater");
         } else {
            var1.set("works-underwater", true);
            this.hasChanges = true;
         }

         return new CustomTemperatureItem(var2, var3, var4, var5, var6, var7, var8);
      }
   }

   public int getLeatherCap() {
      return this.leatherCap;
   }

   private BlockEffect blockEffectFromString(String var1) {
      int var2 = 5;
      int var3 = 0;
      var1 = var1.replaceAll("\\}", "");
      String[] var4 = var1.split(",");
      String[] var5 = var4;
      int var6 = var4.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         String var8 = var5[var7];
         String[] var9 = var8.split("=");
         if (var9[0].equalsIgnoreCase("range")) {
            var2 = Integer.valueOf(var9[1]);
         } else if (var9[0].equalsIgnoreCase("modifier")) {
            var3 = Integer.valueOf(var9[1]);
         }
      }

      return new BlockEffect(var2, var3);
   }

   public int getLavaTemp() {
      return this.lavaTemp;
   }

   public boolean isIpBasedTemperature() {
      return this.ipBasedTemperature;
   }

   public int getWaterBottleEffectDuration() {
      return this.waterBottleEffectDuration;
   }

   public int getColdSlownessLevel() {
      return this.coldSlownessLevel;
   }

   public int getColdHungerLevel() {
      return this.coldHungerLevel;
   }

   public int getWarmSlownessLevel() {
      return this.warmSlownessLevel;
   }

   public PotionEffectType getColdSlownessType() {
      return this.coldSlownessType;
   }

   public PotionEffectType getColdHungerType() {
      return this.coldHungerType;
   }

   public PotionEffectType getWarmSlownessType() {
      return this.warmSlownessType;
   }

   public int getBoostsMinHunger() {
      return this.boostsMinHunger;
   }

   public PotionEffectType getHydrationEffect() {
      return this.hydrationEffect;
   }

   public int getHydrationLevel() {
      return this.hydrationLevel;
   }

   public int getWarmMovementEnd() {
      return this.warmMovementEnd;
   }

   public int getWarmMovementStart() {
      return this.warmMovementStart;
   }

   public int getColdMovementEnd() {
      return this.coldMovementEnd;
   }

   public int getColdMovementStart() {
      return this.coldMovementStart;
   }

   public boolean isReducedMovementSpeedEnabled() {
      return this.reducedMovementSpeedEnabled;
   }

   public double getHeightBlockIncrease() {
      return this.heightBlockIncrease;
   }

   public double getVelocityModifier() {
      return this.velocityModifier;
   }

   public boolean isEntityTemperatureEnabled() {
      return this.entityTemperatureEnabled;
   }

   public int getEntityColdSlownessStart() {
      return this.entityColdSlownessStart;
   }

   public PotionEffectType getEntityColdSlownessEffect() {
      return this.entityColdSlownessEffect;
   }

   public int getEntityColdSlownessLevel() {
      return this.entityColdSlownessLevel;
   }

   public int getEntityColdFreezing() {
      return this.entityColdFreezing;
   }

   public int getEntityWarmSlownessStart() {
      return this.entityWarmSlownessStart;
   }

   public int getEntityWarmSlownessLevel() {
      return this.entityWarmSlownessLevel;
   }

   public PotionEffectType getEntityWarmSlownessEffect() {
      return this.entityWarmSlownessEffect;
   }

   public int getEntityWarmBurn() {
      return this.entityWarmBurn;
   }

   public List<EntityType> getEntitiesImmuneToHeat() {
      return this.entitiesImmuneToHeat;
   }

   public List<EntityType> getEntitiesImmuneToCold() {
      return this.entitiesImmuneToCold;
   }

   public List<EntityType> getEntitiesResilient() {
      return this.entitiesResilient;
   }

   public List<EntityType> getEntitiesWithoutTemperature() {
      return this.entitiesWithoutTemperature;
   }

   public boolean isOverwriteActionbar() {
      return this.overwriteActionbar;
   }

   public float getTemperatureChangeRate() {
      return this.temperatureChangeRate;
   }

   public static enum Armor {
      LEATHER,
      IRON,
      GOLD,
      DIAMOND,
      NETHERITE;

      // $FF: synthetic method
      private static TemperatureSettings.Armor[] $values() {
         return new TemperatureSettings.Armor[]{LEATHER, IRON, GOLD, DIAMOND, NETHERITE};
      }
   }

   public static enum Weather {
      RAIN,
      STORM;

      // $FF: synthetic method
      private static TemperatureSettings.Weather[] $values() {
         return new TemperatureSettings.Weather[]{RAIN, STORM};
      }
   }
}
