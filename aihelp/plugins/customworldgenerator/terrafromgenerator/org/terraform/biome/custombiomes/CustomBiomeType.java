package org.terraform.biome.custombiomes;

import java.util.Locale;
import org.jetbrains.annotations.NotNull;
import org.terraform.utils.version.Version;

public enum CustomBiomeType {
   NONE,
   MUDDY_BOG("b8ad49", "9c8046", "b8ad49", "d9cd62", "ad8445", "ad8445"),
   CHERRY_GROVE("", "69faff", "", "87fffb", "ffa1fc", "acff96"),
   SCARLET_FOREST("", "", "", "", "fc3103", "ff7700"),
   CRYSTALLINE_CLUSTER("e54fff", "c599ff", "e54fff", "", "", "");

   @NotNull
   private final String key;
   private final String fogColor;
   private final String waterColor;
   private final String waterFogColor;
   private final String skyColor;
   private final String grassColor;
   private String foliageColor;
   private float rainFall = 0.8F;
   private boolean isCold = false;

   private CustomBiomeType() {
      String var10001 = this.toString();
      this.key = "terraformgenerator:" + var10001.toLowerCase(Locale.ENGLISH);
      this.fogColor = "";
      this.waterColor = "";
      this.waterFogColor = "";
      this.skyColor = "";
      this.foliageColor = "";
      this.grassColor = "";
   }

   private CustomBiomeType(String param3, String param4, String param5, String param6, String param7, String param8) {
      String var10001 = this.toString();
      this.key = "terraformgenerator:" + var10001.toLowerCase(Locale.ENGLISH);
      this.fogColor = fogColor;
      this.waterColor = waterColor;
      this.waterFogColor = waterFogColor;
      this.skyColor = skyColor;
      this.foliageColor = foliageColor;
      this.grassColor = grassColor;
      this.rainFall = 0.8F;
      this.isCold = false;
      if (Version.VERSION.isAtLeast(Version.v1_20) && this.foliageColor.equals("ffa1fc")) {
         this.foliageColor = "acff96";
      }

   }

   @NotNull
   public String getKey() {
      return this.key;
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
      return this.foliageColor;
   }

   public String getGrassColor() {
      return this.grassColor;
   }

   public float getRainFall() {
      return this.rainFall;
   }

   public boolean isCold() {
      return this.isCold;
   }

   // $FF: synthetic method
   private static CustomBiomeType[] $values() {
      return new CustomBiomeType[]{NONE, MUDDY_BOG, CHERRY_GROVE, SCARLET_FOREST, CRYSTALLINE_CLUSTER};
   }
}
