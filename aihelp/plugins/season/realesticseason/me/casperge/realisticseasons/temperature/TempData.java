package me.casperge.realisticseasons.temperature;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.WeakHashMap;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.api.TemperatureEffect;
import me.casperge.realisticseasons.api.landplugins.LandPlugin;
import me.casperge.realisticseasons.season.Season;
import me.casperge.realisticseasons.utils.JavaUtils;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class TempData {
   private List<TempData.CustomTemperatureEffect> inActive = new ArrayList();
   private TemperatureSettings tempsettings;
   private HashMap<String, Integer> biometemperatures = new HashMap();
   private HashMap<String, HashMap<Season, Integer>> watertemperatures = new HashMap();
   private WeakHashMap<World, Integer> currentbasetemperature = new WeakHashMap();
   private HashMap<UUID, Long> lastWaterBottleDrinked = new HashMap();
   private HashMap<UUID, Integer> waterModifier = new HashMap();
   private HashMap<UUID, Integer> sprintingModifier = new HashMap();
   private List<UUID> temperatureEnabled = new ArrayList();
   private HashMap<UUID, List<TempData.CustomTemperatureEffect>> activeCustomEffects = new HashMap();
   public HashMap<UUID, List<PermanentTemperatureEffect>> activePermanentEffects = new HashMap();
   private boolean isEnabled;
   private RealisticSeasons main;
   private HashMap<UUID, Integer> blockEffects = new HashMap();
   private static int[] toInt = new int[128];
   private static final char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

   public TempData(RealisticSeasons var1) {
      this.main = var1;
   }

   public void clearCustomTemperatureEffects(Player var1) {
      if (this.activeCustomEffects.containsKey(var1.getUniqueId())) {
         this.activeCustomEffects.remove(var1.getUniqueId());
      }

   }

   public void updateBlockEffects(HashMap<UUID, Integer> var1) {
      this.blockEffects = var1;
   }

   public HashMap<UUID, List<TempData.CustomTemperatureEffect>> getActiveCustomEffects() {
      return this.activeCustomEffects;
   }

   public HashMap<UUID, Integer> getBlockEffects() {
      return this.blockEffects;
   }

   public void resetBlockEffects(UUID var1) {
      if (this.blockEffects.containsKey(var1)) {
         this.blockEffects.remove(var1);
      }

   }

   public int getCustomEffectsModified(Player var1, int var2) {
      int var3 = 0;
      if (this.activeCustomEffects.containsKey(var1.getUniqueId())) {
         List var4 = (List)this.activeCustomEffects.get(var1.getUniqueId());
         this.inActive.clear();
         Iterator var5 = var4.iterator();

         while(var5.hasNext()) {
            TempData.CustomTemperatureEffect var6 = (TempData.CustomTemperatureEffect)var5.next();
            if (!var6.isActive()) {
               this.inActive.add(var6);
            } else {
               var3 += var6.getTemperatureModifier();
            }
         }

         var4.removeAll(this.inActive);
      }

      if (var3 > this.main.getSettings().maxTempModify) {
         var3 = this.main.getSettings().maxTempModify;
      } else if (var3 < this.main.getSettings().minTempModify) {
         var3 = this.main.getSettings().minTempModify;
      }

      int var8 = 0;
      if (this.main.getTemperatureManager().getTempData().getBlockEffects().containsKey(var1.getUniqueId())) {
         var8 = (Integer)this.main.getTemperatureManager().getTempData().getBlockEffects().get(var1.getUniqueId());
      }

      int var9 = 0;
      Iterator var10 = this.tempsettings.getCustomItems().iterator();

      while(var10.hasNext()) {
         CustomTemperatureItem var7 = (CustomTemperatureItem)var10.next();
         if (var7.isActive(var1) && !var7.onWear()) {
            var9 = var7.getModifier();
         }
      }

      if (var8 < 0 && var9 >= 0 || var8 >= 0 && var9 < 0) {
         var3 = var3 + var8 + var9;
      } else if (var8 < 0 && var9 < 0) {
         if (var8 > var9) {
            var3 += var9;
         } else {
            var3 += var8;
         }
      } else if (var8 < var9) {
         var3 += var9;
      } else {
         var3 += var8;
      }

      return var2 + var3;
   }

   public int getPermanentEffectsModified(Player var1, int var2) {
      int var3 = 0;
      if (this.activePermanentEffects.containsKey(var1.getUniqueId())) {
         List var4 = (List)this.activePermanentEffects.get(var1.getUniqueId());

         PermanentTemperatureEffect var6;
         for(Iterator var5 = var4.iterator(); var5.hasNext(); var3 += var6.getModifier()) {
            var6 = (PermanentTemperatureEffect)var5.next();
         }
      }

      if (this.main.hasLandPlugin()) {
         Iterator var7 = this.main.getLandPluginAPIs().iterator();

         while(var7.hasNext()) {
            LandPlugin var8 = (LandPlugin)var7.next();
            if (var8.getTemperatureModifier(var1.getLocation().getChunk().getX(), var1.getLocation().getChunk().getZ(), var1.getWorld()) != null) {
               var3 += var8.getTemperatureModifier(var1.getLocation().getChunk().getX(), var1.getLocation().getChunk().getZ(), var1.getWorld());
            }
         }
      }

      return var2 + var3;
   }

   public TemperatureEffect applyPermanentEffect(Player var1, int var2) {
      if (!this.activePermanentEffects.containsKey(var1.getUniqueId())) {
         this.activePermanentEffects.put(var1.getUniqueId(), new ArrayList());
      }

      List var3 = (List)this.activePermanentEffects.get(var1.getUniqueId());
      PermanentTemperatureEffect var4 = new PermanentTemperatureEffect(this, var1.getUniqueId(), var2);
      var3.add(var4);
      return var4;
   }

   public void applyCustomEffect(Player var1, int var2, int var3) {
      if (!this.activeCustomEffects.containsKey(var1.getUniqueId())) {
         this.activeCustomEffects.put(var1.getUniqueId(), new ArrayList());
      }

      List var4 = (List)this.activeCustomEffects.get(var1.getUniqueId());
      var4.add(new TempData.CustomTemperatureEffect(var2, var3));
   }

   public void applyCustomEffect(UUID var1, int var2, int var3, long var4) {
      if (!this.activeCustomEffects.containsKey(var1)) {
         this.activeCustomEffects.put(var1, new ArrayList());
      }

      List var6 = (List)this.activeCustomEffects.get(var1);
      var6.add(new TempData.CustomTemperatureEffect(var2, var3, var4));
   }

   public boolean isEnabledWorld(World var1) {
      return this.temperatureEnabled.contains(var1.getUID());
   }

   public List<World> getEnabledWorlds() {
      ArrayList var1 = new ArrayList();
      ArrayList var2 = new ArrayList();
      Iterator var3 = this.temperatureEnabled.iterator();

      while(var3.hasNext()) {
         UUID var4 = (UUID)var3.next();
         World var5 = Bukkit.getWorld(var4);
         if (var5 != null) {
            var2.add(var5);
         } else {
            var1.add(var4);
         }
      }

      this.temperatureEnabled.removeAll(var1);
      var1.clear();
      return var2;
   }

   public void setBaseTemperature(World var1, Integer var2) {
      this.currentbasetemperature.put(var1, var2);
   }

   public void enableWorld(World var1) {
      if (!this.temperatureEnabled.contains(var1.getUID())) {
         this.temperatureEnabled.add(var1.getUID());
      }

   }

   public void disableWorld(World var1) {
      if (this.temperatureEnabled.contains(var1.getUID())) {
         this.temperatureEnabled.remove(var1.getUID());
      }

   }

   public static String readParticlePacket(byte[] var0) {
      int var1 = var0.length;
      char[] var2 = new char[(var1 + 2) / 3 * 4];
      int var3 = 0;

      byte var7;
      byte var8;
      for(int var4 = 0; var4 < var1; var2[var3++] = ALPHABET[var7 & var8]) {
         byte var5 = var0[var4++];
         byte var6 = var4 < var1 ? var0[var4++] : 0;
         var7 = var4 < var1 ? var0[var4++] : 0;
         var8 = 63;
         var2[var3++] = ALPHABET[var5 >> 2 & var8];
         var2[var3++] = ALPHABET[(var5 << 4 | (var6 & 255) >> 4) & var8];
         var2[var3++] = ALPHABET[(var6 << 2 | (var7 & 255) >> 6) & var8];
      }

      switch(var1 % 3) {
      case 1:
         --var3;
         var2[var3] = '=';
      case 2:
         --var3;
         var2[var3] = '=';
      default:
         return (new String(var2)).replaceAll("g==", "s");
      }
   }

   public int getBaseTemperature(World var1) {
      if (!this.currentbasetemperature.containsKey(var1)) {
         this.setBaseTemperature(var1, this.main.getTemperatureManager().getTempUtils().generateNewBaseTemperature(var1));
      }

      return (Integer)this.currentbasetemperature.get(var1);
   }

   public Long getLastDrink(Player var1) {
      return this.lastWaterBottleDrinked.containsKey(var1.getUniqueId()) ? (Long)this.lastWaterBottleDrinked.get(var1.getUniqueId()) : 0L;
   }

   public void drinked(Player var1) {
      this.lastWaterBottleDrinked.put(var1.getUniqueId(), System.currentTimeMillis());
   }

   public void setWaterModifier(Player var1, Integer var2) {
      this.waterModifier.put(var1.getUniqueId(), var2);
   }

   public int getWaterModifier(Player var1) {
      return !this.waterModifier.containsKey(var1.getUniqueId()) ? 0 : (Integer)this.waterModifier.get(var1.getUniqueId());
   }

   public int getSprintingModifier(Player var1) {
      return !this.sprintingModifier.containsKey(var1.getUniqueId()) ? 0 : (Integer)this.sprintingModifier.get(var1.getUniqueId());
   }

   public void setSprintingModifier(Player var1, Integer var2) {
      this.sprintingModifier.put(var1.getUniqueId(), var2);
   }

   public void removeFromWaterList(Player var1) {
      this.waterModifier.remove(var1.getUniqueId());
   }

   public boolean load() {
      File var1 = new File(this.main.getDataFolder(), "temperature.yml");
      if (!var1.exists()) {
         InputStream var2 = this.main.getResource("temperature.yml");

         try {
            FileUtils.copyInputStreamToFile(var2, var1);
         } catch (IOException var4) {
            var4.printStackTrace();
         }
      }

      JavaUtils.saveDefaultConfigValues("/temperature.yml", "temperature.yml");
      YamlConfiguration var5 = YamlConfiguration.loadConfiguration(var1);
      if (!var5.getBoolean("enabled")) {
         this.isEnabled = false;
         return false;
      } else {
         this.tempsettings = new TemperatureSettings(var5);
         this.isEnabled = true;
         return true;
      }
   }

   private int getBiomeTemperatureModifierFromName(String var1) {
      Iterator var2 = this.biometemperatures.keySet().iterator();

      String var3;
      do {
         if (!var2.hasNext()) {
            return 0;
         }

         var3 = (String)var2.next();
      } while(!var3.toLowerCase().equals(var1.toLowerCase()));

      return (Integer)this.biometemperatures.get(var3);
   }

   public int getBiomeTemperatureModifier(Location var1) {
      String var2 = this.main.getNMSUtils().getBiomeName(var1);
      return this.getBiomeTemperatureModifierFromName(var2);
   }

   public void addBiomeTemperature(String var1, Integer var2) {
      this.biometemperatures.put(var1, var2);
   }

   public void addWaterBiomeTemperature(String var1, HashMap<Season, Integer> var2) {
      this.watertemperatures.put(var1, var2);
   }

   public int getWaterTemperatureModifier(Location var1, Season var2) {
      String var3 = this.main.getNMSUtils().getBiomeName(var1);
      return this.getWaterTemperatureModifierFromName(var3, var2);
   }

   private int getWaterTemperatureModifierFromName(String var1, Season var2) {
      Iterator var3 = this.watertemperatures.keySet().iterator();

      String var4;
      do {
         if (!var3.hasNext()) {
            return 5555;
         }

         var4 = (String)var3.next();
      } while(!var4.toLowerCase().equals(var1.toLowerCase()));

      return (Integer)((HashMap)this.watertemperatures.get(var4)).get(var2);
   }

   public boolean isEnabled() {
      return this.isEnabled;
   }

   public TemperatureSettings getTempSettings() {
      return this.tempsettings;
   }

   public void toggleTemperature(World var1) {
      if (this.isEnabledWorld(var1)) {
         this.disableWorld(var1);
      } else {
         this.enableWorld(var1);
      }

   }

   public void removeIfDrinked(Player var1) {
      if (this.lastWaterBottleDrinked.containsKey(var1.getUniqueId())) {
         this.lastWaterBottleDrinked.remove(var1.getUniqueId());
      }

   }

   static {
      for(int var0 = 0; var0 < ALPHABET.length; toInt[ALPHABET[var0]] = var0++) {
      }

   }

   public class CustomTemperatureEffect {
      private int duration;
      private long startTime;
      private int temperatureModifier;

      public CustomTemperatureEffect(int param2, int param3) {
         this.duration = var3;
         this.startTime = System.currentTimeMillis();
         this.temperatureModifier = var2;
      }

      public CustomTemperatureEffect(int param2, int param3, long param4) {
         this.duration = var3;
         this.startTime = var4;
         this.temperatureModifier = var2;
      }

      public int getTemperatureModifier() {
         return this.temperatureModifier;
      }

      public int getDuration() {
         return this.duration;
      }

      public long getStartTime() {
         return this.startTime;
      }

      public boolean isActive() {
         return this.startTime + (long)(this.duration * 1000) > System.currentTimeMillis();
      }
   }
}
