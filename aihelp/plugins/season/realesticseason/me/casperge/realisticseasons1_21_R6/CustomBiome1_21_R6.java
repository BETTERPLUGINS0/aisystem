package me.casperge.realisticseasons1_21_R6;

import java.lang.reflect.Field;
import java.util.IdentityHashMap;
import java.util.Optional;
import me.casperge.enums.GrassType;
import me.casperge.interfaces.CustomBiome;
import net.minecraft.core.IRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.RegistryMaterials;
import net.minecraft.core.Holder.c;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.BiomeFog;
import net.minecraft.world.level.biome.BiomeSettingsGeneration;
import net.minecraft.world.level.biome.BiomeSettingsMobs;
import net.minecraft.world.level.biome.BiomeBase.TemperatureModifier;
import net.minecraft.world.level.biome.BiomeBase.a;
import net.minecraft.world.level.biome.BiomeFog.GrassColor;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_21_R6.CraftServer;

public class CustomBiome1_21_R6 implements CustomBiome {
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
   private Boolean isFrozen = false;
   private float depth;
   private float scale;
   private float temperature;
   private float downfall;
   private int biomeID = 0;
   private GrassType grassType;
   private BiomeBase biome;
   private BiomeFog oldFog;
   private BiomeSettingsGeneration biomeSettingGen;

   public CustomBiome1_21_R6(String var1, String var2, String var3) {
      this.grassType = GrassType.NORMAL;
      this.name = var1;
      this.prename = var2;
      this.originalname = var3;
      if (var3.equals("SNOWY_TUNDRA")) {
         var3 = "SNOWY_PLAINS";
      }

      if (var3.equals("MOUNTAINS")) {
         var3 = "WINDSWEPT_HILLS";
      }

      Server var4 = Bukkit.getServer();
      CraftServer var5 = (CraftServer)var4;
      this.dedicatedserver = var5.getServer();
      this.newKey = ResourceKey.a(Registries.aN, MinecraftKey.a(var2, var1));
      ResourceKey var6;
      if (var3.contains(":")) {
         String[] var7 = var3.split(":");
         var6 = ResourceKey.a(Registries.aN, MinecraftKey.a(var7[0].toLowerCase(), var7[1].toLowerCase()));
      } else {
         var6 = ResourceKey.a(Registries.aN, MinecraftKey.b(var3.toLowerCase()));
      }

      this.biome = (BiomeBase)((c)this.getBiomeRegistry().a(var6).get()).a();
      if (this.biome == null) {
         Bukkit.getLogger().severe("Biome: " + var3 + " could not be found. Aborting custom biome creation");
      } else {
         this.newBiome.a(true);
         this.newBiome.a(TemperatureModifier.a);

         try {
            Field var12 = BiomeBase.class.getDeclaredField("l");
            var12.setAccessible(true);
            this.oldFog = (BiomeFog)var12.get(this.biome);
            this.convertBiomeFog(this.oldFog);
            Field var8 = BiomeBase.class.getDeclaredField("j");
            var8.setAccessible(true);
            this.biomeSettingGen = (BiomeSettingsGeneration)var8.get(this.biome);
            this.newBiome.a(this.biomeSettingGen);
            Field var9 = BiomeBase.class.getDeclaredField("k");
            var9.setAccessible(true);
            BiomeSettingsMobs var10 = (BiomeSettingsMobs)var9.get(this.biome);
            this.newBiome.a(var10);
         } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var11) {
            var11.printStackTrace();
         }

         this.depth = 0.2F;
         this.scale = 0.05F;
         this.newBiome.a(0.7F);
         this.temperature = 0.7F;
         this.newBiome.b(0.8F);
         this.downfall = 0.8F;
      }
   }

   public GrassType getGrassType() {
      return this.grassType;
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

   public int hexToDecimal(String var1) {
      return Integer.parseInt(var1, 16);
   }

   public void setGrassColor(String var1) {
      if (var1.equals("CUSTOM")) {
         Optional var2 = this.getOptionalFieldInBiomea(this.oldFog, "g");
         if (var2.isPresent()) {
            this.newFog.g((Integer)var2.get());
            var1 = Integer.toHexString((Integer)var2.get());
         }
      } else {
         this.newFog.g(this.hexToDecimal(var1));
      }

      this.newFog.a(GrassColor.a);
      this.grassType = GrassType.NORMAL;
      this.grassColor = var1;
   }

   public void register() {
      try {
         if (this.isRegistered) {
            return;
         }

         this.newBiome.a(this.newFog.b());
         BiomeBase var1 = this.newBiome.a();
         RegistryMaterials var2 = (RegistryMaterials)this.getBiomeRegistry();

         try {
            Field var3 = RegistryMaterials.class.getDeclaredField("m");
            var3.setAccessible(true);
            var3.set(var2, new IdentityHashMap());
            var2.f(var1);
            var2.a(this.newKey, var1, RegistrationInfo.a);
            var3.set(var2, (Object)null);
         } catch (IllegalAccessException | NoSuchFieldException | SecurityException | IllegalArgumentException var4) {
            var4.printStackTrace();
         }

         this.isRegistered = true;
         this.biomeID = this.getBiomeRegistry().a(var1);
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public void setFrozen(Boolean var1) {
      if (var1) {
         this.newBiome.a(true).a(0.0F).b(0.5F);
         this.isFrozen = true;
      } else {
         this.newBiome.a(TemperatureModifier.a);
         this.isFrozen = false;
      }

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
      this.depth = var1;
   }

   public void setScale(Float var1) {
      this.scale = var1;
   }

   public void setTemperature(Float var1) {
      this.newBiome.a(var1);
      this.temperature = var1;
   }

   public void setDownfall(Float var1) {
      this.newBiome.b(var1);
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

   private IRegistry<BiomeBase> getBiomeRegistry() {
      return this.dedicatedserver.bg().f(Registries.aN);
   }
}
