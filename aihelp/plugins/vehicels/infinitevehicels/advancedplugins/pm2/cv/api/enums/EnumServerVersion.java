package advancedplugins.pm2.cv.api.enums;

import advancedplugins.pm2.cv.api.interfaces.VersionSensible;
import advancedplugins.pm2.cv.api.util.reflection.EnumReflection;
import org.bukkit.Bukkit;

@VersionSensible
public enum EnumServerVersion {
   v1_9_R1,
   v1_9_R2,
   v1_10_R1,
   v1_11_R1,
   v1_12_R1,
   v1_13_R1,
   v1_13_R2,
   v1_14_R1,
   v1_15_R1,
   v1_16_R1,
   v1_16_R2,
   v1_16_R3,
   v1_17_R1,
   v1_18_R1,
   v1_18_R2,
   v1_19_R1,
   v1_19_R2,
   v1_19_R3,
   v1_20_R1,
   v1_20_R2,
   v1_20_R3,
   v1_20_R4,
   v1_21_R1;

   private final int id;

   private EnumServerVersion() {
      StringBuilder var3 = new StringBuilder();
      String var4 = this.name();

      for(int var5 = 0; var5 < var4.length(); ++var5) {
         char var6 = var4.charAt(var5);
         if (Character.isDigit(var6)) {
            var3.append(var6);
         }
      }

      this.id = Integer.parseInt(var3.toString());
   }

   public static EnumServerVersion getServerVersion() {
      String var0 = Bukkit.getServer().getClass().getPackage().getName();
      String var1 = var0.substring(var0.lastIndexOf(".") + 1);
      return (EnumServerVersion)EnumReflection.getEnumConstant(EnumServerVersion.class, var1);
   }

   public int getId() {
      return this.id;
   }

   public boolean isOlder(EnumServerVersion var1) {
      return this.getId() < var1.getId();
   }

   public boolean isOlderEquals(EnumServerVersion var1) {
      return this.getId() <= var1.getId();
   }

   public boolean isNewer(EnumServerVersion var1) {
      return this.getId() > var1.getId();
   }

   public boolean isNewerEquals(EnumServerVersion var1) {
      return this.getId() >= var1.getId();
   }

   public boolean isSameVersion(EnumServerVersion var1) {
      String var2 = this.name().substring(0, this.name().indexOf("_R"));
      String var3 = var1.name().substring(0, var1.name().indexOf("_R"));
      return var2.equals(var3);
   }

   public boolean isSameRevision(EnumServerVersion var1) {
      String var2 = this.name().substring(this.name().indexOf("R") + 1);
      String var3 = var1.name().substring(var1.name().indexOf("R") + 1);
      return var2.equals(var3);
   }

   public boolean isSupportsDisplayEntities() {
      return this.id >= v1_19_R3.id;
   }

   // $FF: synthetic method
   private static EnumServerVersion[] $values() {
      return new EnumServerVersion[]{v1_9_R1, v1_9_R2, v1_10_R1, v1_11_R1, v1_12_R1, v1_13_R1, v1_13_R2, v1_14_R1, v1_15_R1, v1_16_R1, v1_16_R2, v1_16_R3, v1_17_R1, v1_18_R1, v1_18_R2, v1_19_R1, v1_19_R2, v1_19_R3, v1_20_R1, v1_20_R2, v1_20_R3, v1_20_R4, v1_21_R1};
   }
}
