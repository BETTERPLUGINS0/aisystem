package ac.grim.grimac.platform.api;

import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import lombok.Generated;

public enum Platform {
   FABRIC("fabric"),
   BUKKIT("bukkit"),
   FOLIA("folia");

   private final String name;

   @Nullable
   public static Platform getByName(String name) {
      Platform[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Platform platform = var1[var3];
         if (platform.getName().equalsIgnoreCase(name)) {
            return platform;
         }
      }

      return null;
   }

   @Generated
   private Platform(final String param3) {
      this.name = name;
   }

   @Generated
   public String getName() {
      return this.name;
   }

   // $FF: synthetic method
   private static Platform[] $values() {
      return new Platform[]{FABRIC, BUKKIT, FOLIA};
   }
}
