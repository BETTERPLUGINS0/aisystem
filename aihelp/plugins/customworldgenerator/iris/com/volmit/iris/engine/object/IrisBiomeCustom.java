package com.volmit.iris.engine.object;

import com.volmit.iris.Iris;
import com.volmit.iris.core.nms.datapack.IDataFixer;
import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.DependsOn;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.json.JSONArray;
import com.volmit.iris.util.json.JSONObject;
import java.awt.Color;
import java.util.Iterator;
import java.util.Locale;
import lombok.Generated;

@Snippet("custom-biome")
@Desc("A custom biome, generated through a datapack")
public class IrisBiomeCustom {
   @Required
   @Desc("The resource key of this biome. Just a simple id such as 'plains' or something.")
   private String id = "";
   @MinNumber(-3.0D)
   @MaxNumber(3.0D)
   @Desc("The biome's temperature")
   private double temperature = 0.8D;
   @MinNumber(-3.0D)
   @MaxNumber(3.0D)
   @Desc("The biome's downfall amount (snow / rain), see preci")
   private double humidity = 0.4D;
   @DependsOn({"spawnRarity"})
   @ArrayType(
      min = 1,
      type = IrisBiomeCustomSpawn.class
   )
   @Desc("The biome's mob spawns")
   private KList<IrisBiomeCustomSpawn> spawns = new KList();
   @Desc("The biome's downfall type")
   private IrisBiomeCustomPrecipType downfallType;
   @Desc("Define an ambient particle to be rendered clientside (no server cost!)")
   private IrisBiomeCustomParticle ambientParticle;
   @Required
   @Desc("The biome's category type")
   private IrisBiomeCustomCategory category;
   @MinNumber(0.0D)
   @MaxNumber(20.0D)
   @Desc("The spawn rarity of any defined spawners")
   private int spawnRarity;
   @Desc("The color of the sky, top half of sky. (hex format)")
   private String skyColor;
   @Desc("The color of the fog, bottom half of sky. (hex format)")
   private String fogColor;
   @Desc("The color of the water (hex format). Leave blank / don't define to not change")
   private String waterColor;
   @Desc("The color of water fog (hex format). Leave blank / don't define to not change")
   private String waterFogColor;
   @Desc("The color of the grass (hex format). Leave blank / don't define to not change")
   private String grassColor;
   @Desc("The color of foliage (hex format). Leave blank / don't define to not change")
   private String foliageColor;

   public String generateJson(IDataFixer fixer) {
      JSONObject var2 = new JSONObject();
      var2.put("sky_color", this.parseColor(this.getSkyColor()));
      var2.put("fog_color", this.parseColor(this.getFogColor()));
      var2.put("water_color", this.parseColor(this.getWaterColor()));
      var2.put("water_fog_color", this.parseColor(this.getWaterFogColor()));
      JSONObject var3;
      JSONObject var4;
      if (this.ambientParticle != null) {
         var3 = new JSONObject();
         var4 = new JSONObject();
         var4.put("type", (Object)this.ambientParticle.getParticle().name().toLowerCase());
         var3.put("options", (Object)var4);
         var3.put("probability", (double)(1.0F / (float)this.ambientParticle.getRarity()));
         var2.put("particle", (Object)var3);
      }

      if (!this.getGrassColor().isEmpty()) {
         var2.put("grass_color", this.parseColor(this.getGrassColor()));
      }

      if (!this.getFoliageColor().isEmpty()) {
         var2.put("foliage_color", this.parseColor(this.getFoliageColor()));
      }

      var3 = new JSONObject();
      var3.put("surface_builder", (Object)"minecraft:grass");
      var3.put("depth", 0.125D);
      var3.put("scale", 0.05D);
      var3.put("temperature", this.getTemperature());
      var3.put("downfall", this.getHumidity());
      var3.put("creature_spawn_probability", this.getSpawnRarity());
      var3.put("has_precipitation", this.getDownfallType() != IrisBiomeCustomPrecipType.none);
      var3.put("precipitation", (Object)this.getDownfallType().toString().toLowerCase());
      var3.put("category", (Object)this.getCategory().toString().toLowerCase());
      var3.put("effects", (Object)var2);
      var3.put("starts", (Object)(new JSONArray()));
      var3.put("spawners", (Object)(new JSONObject()));
      var3.put("spawn_costs", (Object)(new JSONObject()));
      var3.put("carvers", (Object)(new JSONObject()));
      var3.put("features", (Object)(new JSONArray()));
      if (this.spawnRarity > 0) {
         var3.put("creature_spawn_probability", this.spawnRarity);
      }

      if (this.getSpawns() != null && this.getSpawns().isNotEmpty()) {
         var4 = new JSONObject();
         KMap var5 = new KMap();
         Iterator var6 = this.getSpawns().iterator();

         while(var6.hasNext()) {
            IrisBiomeCustomSpawn var7 = (IrisBiomeCustomSpawn)var6.next();
            JSONArray var8 = (JSONArray)var5.computeIfAbsent(var7.getGroup(), (var0) -> {
               return new JSONArray();
            });
            JSONObject var9 = new JSONObject();
            var9.put("type", (Object)("minecraft:" + var7.getType().name().toLowerCase()));
            var9.put("weight", var7.getWeight());
            var9.put("minCount", var7.getMinCount());
            var9.put("maxCount", var7.getMaxCount());
            var8.put((Object)var9);
         }

         var6 = var5.k().iterator();

         while(var6.hasNext()) {
            IrisBiomeCustomSpawnType var10 = (IrisBiomeCustomSpawnType)var6.next();
            var4.put(var10.name().toLowerCase(Locale.ROOT), var5.get(var10));
         }

         var3.put("spawners", (Object)var4);
      }

      return var1.fixCustomBiome(this, var3).toString(4);
   }

   private int parseColor(String c) {
      String var2 = (var1.startsWith("#") ? var1 : "#" + var1).trim();

      try {
         return Color.decode(var2).getRGB();
      } catch (Throwable var4) {
         Iris.reportError(var4);
         Iris.error("Error Parsing '''color''', (" + var1 + ")");
         return 0;
      }
   }

   public String getId() {
      return this.id.toLowerCase();
   }

   @Generated
   public IrisBiomeCustom() {
      this.downfallType = IrisBiomeCustomPrecipType.rain;
      this.ambientParticle = null;
      this.category = IrisBiomeCustomCategory.plains;
      this.spawnRarity = 0;
      this.skyColor = "#79a8e1";
      this.fogColor = "#c0d8e1";
      this.waterColor = "#3f76e4";
      this.waterFogColor = "#050533";
      this.grassColor = "";
      this.foliageColor = "";
   }

   @Generated
   public IrisBiomeCustom(final String id, final double temperature, final double humidity, final KList<IrisBiomeCustomSpawn> spawns, final IrisBiomeCustomPrecipType downfallType, final IrisBiomeCustomParticle ambientParticle, final IrisBiomeCustomCategory category, final int spawnRarity, final String skyColor, final String fogColor, final String waterColor, final String waterFogColor, final String grassColor, final String foliageColor) {
      this.downfallType = IrisBiomeCustomPrecipType.rain;
      this.ambientParticle = null;
      this.category = IrisBiomeCustomCategory.plains;
      this.spawnRarity = 0;
      this.skyColor = "#79a8e1";
      this.fogColor = "#c0d8e1";
      this.waterColor = "#3f76e4";
      this.waterFogColor = "#050533";
      this.grassColor = "";
      this.foliageColor = "";
      this.id = var1;
      this.temperature = var2;
      this.humidity = var4;
      this.spawns = var6;
      this.downfallType = var7;
      this.ambientParticle = var8;
      this.category = var9;
      this.spawnRarity = var10;
      this.skyColor = var11;
      this.fogColor = var12;
      this.waterColor = var13;
      this.waterFogColor = var14;
      this.grassColor = var15;
      this.foliageColor = var16;
   }

   @Generated
   public double getTemperature() {
      return this.temperature;
   }

   @Generated
   public double getHumidity() {
      return this.humidity;
   }

   @Generated
   public KList<IrisBiomeCustomSpawn> getSpawns() {
      return this.spawns;
   }

   @Generated
   public IrisBiomeCustomPrecipType getDownfallType() {
      return this.downfallType;
   }

   @Generated
   public IrisBiomeCustomParticle getAmbientParticle() {
      return this.ambientParticle;
   }

   @Generated
   public IrisBiomeCustomCategory getCategory() {
      return this.category;
   }

   @Generated
   public int getSpawnRarity() {
      return this.spawnRarity;
   }

   @Generated
   public String getSkyColor() {
      return this.skyColor;
   }

   @Generated
   public String getFogColor() {
      return this.fogColor;
   }

   @Generated
   public String getWaterColor() {
      return this.waterColor;
   }

   @Generated
   public String getWaterFogColor() {
      return this.waterFogColor;
   }

   @Generated
   public String getGrassColor() {
      return this.grassColor;
   }

   @Generated
   public String getFoliageColor() {
      return this.foliageColor;
   }

   @Generated
   public IrisBiomeCustom setId(final String id) {
      this.id = var1;
      return this;
   }

   @Generated
   public IrisBiomeCustom setTemperature(final double temperature) {
      this.temperature = var1;
      return this;
   }

   @Generated
   public IrisBiomeCustom setHumidity(final double humidity) {
      this.humidity = var1;
      return this;
   }

   @Generated
   public IrisBiomeCustom setSpawns(final KList<IrisBiomeCustomSpawn> spawns) {
      this.spawns = var1;
      return this;
   }

   @Generated
   public IrisBiomeCustom setDownfallType(final IrisBiomeCustomPrecipType downfallType) {
      this.downfallType = var1;
      return this;
   }

   @Generated
   public IrisBiomeCustom setAmbientParticle(final IrisBiomeCustomParticle ambientParticle) {
      this.ambientParticle = var1;
      return this;
   }

   @Generated
   public IrisBiomeCustom setCategory(final IrisBiomeCustomCategory category) {
      this.category = var1;
      return this;
   }

   @Generated
   public IrisBiomeCustom setSpawnRarity(final int spawnRarity) {
      this.spawnRarity = var1;
      return this;
   }

   @Generated
   public IrisBiomeCustom setSkyColor(final String skyColor) {
      this.skyColor = var1;
      return this;
   }

   @Generated
   public IrisBiomeCustom setFogColor(final String fogColor) {
      this.fogColor = var1;
      return this;
   }

   @Generated
   public IrisBiomeCustom setWaterColor(final String waterColor) {
      this.waterColor = var1;
      return this;
   }

   @Generated
   public IrisBiomeCustom setWaterFogColor(final String waterFogColor) {
      this.waterFogColor = var1;
      return this;
   }

   @Generated
   public IrisBiomeCustom setGrassColor(final String grassColor) {
      this.grassColor = var1;
      return this;
   }

   @Generated
   public IrisBiomeCustom setFoliageColor(final String foliageColor) {
      this.foliageColor = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisBiomeCustom)) {
         return false;
      } else {
         IrisBiomeCustom var2 = (IrisBiomeCustom)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (Double.compare(this.getTemperature(), var2.getTemperature()) != 0) {
            return false;
         } else if (Double.compare(this.getHumidity(), var2.getHumidity()) != 0) {
            return false;
         } else if (this.getSpawnRarity() != var2.getSpawnRarity()) {
            return false;
         } else {
            label150: {
               String var3 = this.getId();
               String var4 = var2.getId();
               if (var3 == null) {
                  if (var4 == null) {
                     break label150;
                  }
               } else if (var3.equals(var4)) {
                  break label150;
               }

               return false;
            }

            KList var5 = this.getSpawns();
            KList var6 = var2.getSpawns();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            label136: {
               IrisBiomeCustomPrecipType var7 = this.getDownfallType();
               IrisBiomeCustomPrecipType var8 = var2.getDownfallType();
               if (var7 == null) {
                  if (var8 == null) {
                     break label136;
                  }
               } else if (var7.equals(var8)) {
                  break label136;
               }

               return false;
            }

            IrisBiomeCustomParticle var9 = this.getAmbientParticle();
            IrisBiomeCustomParticle var10 = var2.getAmbientParticle();
            if (var9 == null) {
               if (var10 != null) {
                  return false;
               }
            } else if (!var9.equals(var10)) {
               return false;
            }

            label122: {
               IrisBiomeCustomCategory var11 = this.getCategory();
               IrisBiomeCustomCategory var12 = var2.getCategory();
               if (var11 == null) {
                  if (var12 == null) {
                     break label122;
                  }
               } else if (var11.equals(var12)) {
                  break label122;
               }

               return false;
            }

            String var13 = this.getSkyColor();
            String var14 = var2.getSkyColor();
            if (var13 == null) {
               if (var14 != null) {
                  return false;
               }
            } else if (!var13.equals(var14)) {
               return false;
            }

            label108: {
               String var15 = this.getFogColor();
               String var16 = var2.getFogColor();
               if (var15 == null) {
                  if (var16 == null) {
                     break label108;
                  }
               } else if (var15.equals(var16)) {
                  break label108;
               }

               return false;
            }

            String var17 = this.getWaterColor();
            String var18 = var2.getWaterColor();
            if (var17 == null) {
               if (var18 != null) {
                  return false;
               }
            } else if (!var17.equals(var18)) {
               return false;
            }

            label94: {
               String var19 = this.getWaterFogColor();
               String var20 = var2.getWaterFogColor();
               if (var19 == null) {
                  if (var20 == null) {
                     break label94;
                  }
               } else if (var19.equals(var20)) {
                  break label94;
               }

               return false;
            }

            label87: {
               String var21 = this.getGrassColor();
               String var22 = var2.getGrassColor();
               if (var21 == null) {
                  if (var22 == null) {
                     break label87;
                  }
               } else if (var21.equals(var22)) {
                  break label87;
               }

               return false;
            }

            String var23 = this.getFoliageColor();
            String var24 = var2.getFoliageColor();
            if (var23 == null) {
               if (var24 != null) {
                  return false;
               }
            } else if (!var23.equals(var24)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisBiomeCustom;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      long var3 = Double.doubleToLongBits(this.getTemperature());
      int var18 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
      long var5 = Double.doubleToLongBits(this.getHumidity());
      var18 = var18 * 59 + (int)(var5 >>> 32 ^ var5);
      var18 = var18 * 59 + this.getSpawnRarity();
      String var7 = this.getId();
      var18 = var18 * 59 + (var7 == null ? 43 : var7.hashCode());
      KList var8 = this.getSpawns();
      var18 = var18 * 59 + (var8 == null ? 43 : var8.hashCode());
      IrisBiomeCustomPrecipType var9 = this.getDownfallType();
      var18 = var18 * 59 + (var9 == null ? 43 : var9.hashCode());
      IrisBiomeCustomParticle var10 = this.getAmbientParticle();
      var18 = var18 * 59 + (var10 == null ? 43 : var10.hashCode());
      IrisBiomeCustomCategory var11 = this.getCategory();
      var18 = var18 * 59 + (var11 == null ? 43 : var11.hashCode());
      String var12 = this.getSkyColor();
      var18 = var18 * 59 + (var12 == null ? 43 : var12.hashCode());
      String var13 = this.getFogColor();
      var18 = var18 * 59 + (var13 == null ? 43 : var13.hashCode());
      String var14 = this.getWaterColor();
      var18 = var18 * 59 + (var14 == null ? 43 : var14.hashCode());
      String var15 = this.getWaterFogColor();
      var18 = var18 * 59 + (var15 == null ? 43 : var15.hashCode());
      String var16 = this.getGrassColor();
      var18 = var18 * 59 + (var16 == null ? 43 : var16.hashCode());
      String var17 = this.getFoliageColor();
      var18 = var18 * 59 + (var17 == null ? 43 : var17.hashCode());
      return var18;
   }

   @Generated
   public String toString() {
      String var10000 = this.getId();
      return "IrisBiomeCustom(id=" + var10000 + ", temperature=" + this.getTemperature() + ", humidity=" + this.getHumidity() + ", spawns=" + String.valueOf(this.getSpawns()) + ", downfallType=" + String.valueOf(this.getDownfallType()) + ", ambientParticle=" + String.valueOf(this.getAmbientParticle()) + ", category=" + String.valueOf(this.getCategory()) + ", spawnRarity=" + this.getSpawnRarity() + ", skyColor=" + this.getSkyColor() + ", fogColor=" + this.getFogColor() + ", waterColor=" + this.getWaterColor() + ", waterFogColor=" + this.getWaterFogColor() + ", grassColor=" + this.getGrassColor() + ", foliageColor=" + this.getFoliageColor() + ")";
   }
}
