package me.casperge.realisticseasons1_17_R1;

import com.mojang.serialization.Lifecycle;
import java.lang.reflect.Field;
import java.util.Optional;
import me.casperge.enums.GrassType;
import me.casperge.interfaces.CustomBiome;
import net.minecraft.core.IRegistry;
import net.minecraft.core.IRegistryWritable;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.BiomeFog;
import net.minecraft.world.level.biome.BiomeSettingsGeneration;
import net.minecraft.world.level.biome.BiomeSettingsMobs;
import net.minecraft.world.level.biome.BiomeBase.Geography;
import net.minecraft.world.level.biome.BiomeBase.Precipitation;
import net.minecraft.world.level.biome.BiomeBase.TemperatureModifier;
import net.minecraft.world.level.biome.BiomeBase.a;
import net.minecraft.world.level.biome.BiomeFog.GrassColor;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;

public class CustomBiome1_17_R1 implements CustomBiome {
   private a newBiome = new a();
   private net.minecraft.world.level.biome.BiomeFog.a newFog = new net.minecraft.world.level.biome.BiomeFog.a();
   private ResourceKey<BiomeBase> newKey;
   private DedicatedServer dedicatedserver;
   private boolean isRegistered = false;
   private String name;
   private String prename;
   private String originalname;
   private String fogColor;
   private String waterColor;
   private String waterFogColor;
   private String skyColor;
   private String foliageColor;
   private String grassColor;
   private GrassType grassType;
   private Boolean isFrozen;
   private float depth;
   private float scale;
   private float temperature;
   private float downfall;
   private int biomeID;
   private BiomeBase biome;
   private BiomeFog oldFog;
   private BiomeSettingsGeneration biomeSettingGen;

   public CustomBiome1_17_R1(String var1, String var2, String var3) {
      this.grassType = GrassType.NORMAL;
      this.isFrozen = false;
      this.biomeID = 0;
      this.name = var1;
      this.prename = var2;
      this.originalname = var3;
      Server var4 = Bukkit.getServer();
      CraftServer var5 = (CraftServer)var4;
      this.dedicatedserver = var5.getServer();
      this.newKey = ResourceKey.a(IRegistry.aO, new MinecraftKey(var2, var1));
      ResourceKey var6;
      if (var3.contains(":")) {
         String[] var7 = var3.split(":");
         var6 = ResourceKey.a(IRegistry.aO, new MinecraftKey(var7[0].toLowerCase(), var7[1].toLowerCase()));
      } else {
         var6 = ResourceKey.a(IRegistry.aO, new MinecraftKey(var3.toLowerCase()));
      }

      IRegistryWritable var13 = this.dedicatedserver.getCustomRegistry().b(IRegistry.aO);
      this.biome = (BiomeBase)var13.a(var6);
      if (this.biome == null) {
         Bukkit.getLogger().severe("Biome: " + var3 + " could not be found. Aborting custom biome creation");
      } else {
         this.newBiome.a(this.biome.t());
         this.newBiome.a(this.biome.c());
         this.newBiome.a(TemperatureModifier.a);

         try {
            Field var8 = BiomeBase.class.getDeclaredField("q");
            var8.setAccessible(true);
            this.oldFog = (BiomeFog)var8.get(this.biome);
            this.convertBiomeFog(this.oldFog);
            Field var9 = BiomeBase.class.getDeclaredField("l");
            var9.setAccessible(true);
            this.biomeSettingGen = (BiomeSettingsGeneration)var9.get(this.biome);
            this.newBiome.a(this.biomeSettingGen);
            Field var10 = BiomeBase.class.getDeclaredField("m");
            var10.setAccessible(true);
            BiomeSettingsMobs var11 = (BiomeSettingsMobs)var10.get(this.biome);
            this.newBiome.a(var11);
         } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var12) {
            var12.printStackTrace();
         }

         this.newBiome.a(0.2F);
         this.depth = 0.2F;
         this.newBiome.b(0.05F);
         this.scale = 0.05F;
         this.newBiome.c(0.7F);
         this.temperature = 0.7F;
         this.newBiome.d(0.8F);
         this.downfall = 0.8F;
      }
   }

   public void setFogColor(String var1) {
      this.newFog.a(this.hexToDecimal(var1));
      this.fogColor = var1;
   }

   public void setWaterColor(String var1) {
      this.newFog.b(this.hexToDecimal(var1));
      this.waterColor = var1;
   }

   public void setWaterFogColor(String var1) {
      this.newFog.c(this.hexToDecimal(var1));
      this.waterFogColor = var1;
   }

   public void setSkyColor(String var1) {
      this.newFog.d(this.hexToDecimal(var1));
      this.skyColor = var1;
   }

   public void setFoliageColor(String var1) {
      this.newFog.e(this.hexToDecimal(var1));
      this.foliageColor = var1;
   }

   public void setGrassColor(String var1) {
      if (var1.equals("CUSTOM")) {
         Optional var2 = this.getOptionalFieldInBiomea(this.oldFog, "g");
         if (!var2.isEmpty()) {
            this.newFog.f((Integer)var2.get());
            var1 = Integer.toHexString((Integer)var2.get());
         }
      } else {
         this.newFog.f(this.hexToDecimal(var1));
      }

      this.newFog.a(GrassColor.a);
      this.grassType = GrassType.NORMAL;
      this.grassColor = var1;
   }

   public void register() {
      if (!this.isRegistered) {
         this.newBiome.a(this.newFog.a());
         BiomeBase var1 = (BiomeBase)this.dedicatedserver.getCustomRegistry().b(IRegistry.aO).a(this.newKey, this.newBiome.a(), Lifecycle.stable());
         this.isRegistered = true;
         this.biomeID = this.dedicatedserver.getCustomRegistry().d(IRegistry.aO).getId(var1);
      }
   }

   public void setFrozen(Boolean var1) {
      if (var1) {
         net.minecraft.world.level.biome.BiomeSettingsMobs.a var2 = (new net.minecraft.world.level.biome.BiomeSettingsMobs.a()).a(0.07F);
         this.newBiome.a(Precipitation.c).a(Geography.h).a(0.2F).b(0.05F).c(0.0F).d(0.5F).a(var2.b());
         this.isFrozen = true;
      } else {
         this.newBiome.a(TemperatureModifier.a);
         this.isFrozen = false;
      }

   }

   public GrassType getGrassType() {
      return this.grassType;
   }

   private void convertBiomeFog(BiomeFog var1) {
      this.fogColor = Integer.toHexString(this.getFieldInBiomea(var1, "b"));
      this.waterColor = Integer.toHexString(this.getFieldInBiomea(var1, "c"));
      this.waterFogColor = Integer.toHexString(this.getFieldInBiomea(var1, "d"));
      this.skyColor = Integer.toHexString(this.getFieldInBiomea(var1, "e"));
      Optional var2 = this.getOptionalFieldInBiomea(var1, "f");
      if (var2.isPresent()) {
         this.foliageColor = Integer.toHexString((Integer)var2.get());
         this.newFog.e((Integer)var2.get());
      }

      this.newFog.a(this.getFieldInBiomea(var1, "b"));
      this.newFog.b(this.getFieldInBiomea(var1, "c"));
      this.newFog.c(this.getFieldInBiomea(var1, "d"));
      this.newFog.d(this.getFieldInBiomea(var1, "e"));
      if (this.originalname.equalsIgnoreCase("SWAMP")) {
         this.newFog.a(GrassColor.c);
         this.grassType = GrassType.SWAMP;
      } else if (this.originalname.equalsIgnoreCase("DARK_FOREST")) {
         this.newFog.a(GrassColor.b);
         this.grassType = GrassType.DARK_FOREST;
      } else {
         this.newFog.a(GrassColor.a);
         this.grassType = GrassType.NORMAL;
      }

   }

   private int getFieldInBiomea(BiomeFog var1, String var2) {
      try {
         Field var3 = var1.getClass().getDeclaredField(var2);
         var3.setAccessible(true);
         return (Integer)var3.get(var1);
      } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var4) {
         var4.printStackTrace();
         return 6;
      }
   }

   private Optional<Integer> getOptionalFieldInBiomea(BiomeFog var1, String var2) {
      try {
         Field var3 = var1.getClass().getDeclaredField(var2);
         var3.setAccessible(true);
         return (Optional)var3.get(var1);
      } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var4) {
         var4.printStackTrace();
         return Optional.of(22222);
      }
   }

   public void setDepth(Float var1) {
      this.newBiome.a(var1);
      this.depth = var1;
   }

   public void setScale(Float var1) {
      this.newBiome.b(var1);
      this.scale = var1;
   }

   public void setTemperature(Float var1) {
      this.newBiome.c(var1);
      this.temperature = var1;
   }

   public void setDownfall(Float var1) {
      this.newBiome.d(var1);
      this.downfall = var1;
   }

   public String getName() {
      return this.name;
   }

   public String getPreName() {
      return this.prename;
   }

   public String getFullName() {
      return this.prename + ":" + this.name;
   }

   public boolean isRegistered() {
      return this.isRegistered;
   }

   public String getFogColor() {
      return this.fogColor;
   }

   public String getWaterColor() {
      return this.waterColor;
   }

   public String getWaterFogColor() {
      return this.waterFogColor;
   }

   public String getSkyColor() {
      return this.skyColor;
   }

   public String getFoliageColor() {
      return this.foliageColor == null ? "" : this.foliageColor;
   }

   public String getGrassColor() {
      return this.grassColor;
   }

   public boolean isFrozen() {
      return this.isFrozen;
   }

   public float getDepth() {
      return this.depth;
   }

   public float getScale() {
      return this.scale;
   }

   public float getTemperature() {
      return this.temperature;
   }

   public float getDownfall() {
      return this.downfall;
   }

   public int getBiomeID() {
      if (this.isRegistered) {
         return this.biomeID;
      } else {
         Bukkit.getLogger().severe("[RealisticSeasons] Can't get BiomeID of an unregistered biome!");
         return 0;
      }
   }

   public int hexToDecimal(String var1) {
      return Integer.parseInt(var1, 16);
   }
}
