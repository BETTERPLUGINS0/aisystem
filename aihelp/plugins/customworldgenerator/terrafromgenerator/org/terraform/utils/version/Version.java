package org.terraform.utils.version;

import java.lang.reflect.InvocationTargetException;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.NMSInjectorAbstract;
import org.terraform.main.TerraformGeneratorPlugin;

public enum Version {
   v1_18_2("v1_18_R2", 0),
   v1_19_4("v1_19_R3", 1),
   v1_20("v1_20_R1", 2),
   v1_20_1("v1_20_R1", 3),
   v1_20_2("v1_20_R2", 4),
   v1_20_3("v1_20_R3", 5),
   v1_20_4("v1_20_R3", 6),
   v1_20_5("v1_20_R4", 7),
   v1_20_6("v1_20_R4", 8),
   v1_21("v1_21_R1", 9),
   v1_21_1("v1_21_R1", 10),
   v1_21_2("v1_21_R2", 11),
   v1_21_3("v1_21_R2", 12),
   v1_21_4("v1_21_R3", 13),
   v1_21_5("v1_21_R4", 14),
   v1_21_6("v1_21_R5", 15),
   v1_21_7("v1_21_R5", 16),
   v1_21_8("v1_21_R5", 17),
   v1_21_9("v1_21_R6", 18),
   v1_21_10("v1_21_R6", 19),
   v1_21_11("v1_21_R7", 20);

   final String packName;
   final int priority;
   public static final Version VERSION = toVersion(Bukkit.getServer().getBukkitVersion().split("-")[0]);

   private Version(String param3, int param4) {
      this.packName = packName;
      this.priority = priority;
   }

   public String getSchematicHeader() {
      return this.toString().replace("v1_", "").replace("_", ".");
   }

   public String getPackName() {
      return this.packName;
   }

   public boolean isAtLeast(Version other) {
      return this.priority >= other.priority;
   }

   private static Version toVersion(@NotNull String version) {
      try {
         return valueOf("v" + version.replace(".", "_"));
      } catch (IllegalArgumentException var7) {
         TerraformGeneratorPlugin.logger.stdout("Unknown version " + version + ", trying failsafe.");
         Version highest = v1_18_2;
         Version[] var3 = values();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Version v = var3[var5];
            if (v.isAtLeast(highest)) {
               highest = v;
            }
         }

         return highest;
      }
   }

   @NotNull
   public static NMSInjectorAbstract getInjector() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
      String spigotAppend;
      try {
         Class.forName("com.destroystokyo.paper.ParticleBuilder");
         spigotAppend = "";
      } catch (ClassNotFoundException var4) {
         TerraformGeneratorPlugin.logger.info("Spigot detected");
         spigotAppend = "spigot.";

         try {
            Class.forName("org.terraform." + spigotAppend + VERSION.getPackName() + ".NMSInjector").getDeclaredConstructor().newInstance();
         } catch (ClassNotFoundException var3) {
            TerraformGeneratorPlugin.logger.stdout("There was no spigot package for this version. This is fine if you are BELOW 1.21.9.");
            spigotAppend = "";
         }
      }

      return (NMSInjectorAbstract)Class.forName("org.terraform." + spigotAppend + VERSION.getPackName() + ".NMSInjector").getDeclaredConstructor().newInstance();
   }

   // $FF: synthetic method
   private static Version[] $values() {
      return new Version[]{v1_18_2, v1_19_4, v1_20, v1_20_1, v1_20_2, v1_20_3, v1_20_4, v1_20_5, v1_20_6, v1_21, v1_21_1, v1_21_2, v1_21_3, v1_21_4, v1_21_5, v1_21_6, v1_21_7, v1_21_8, v1_21_9, v1_21_10, v1_21_11};
   }
}
