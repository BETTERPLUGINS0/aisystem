package me.SuperRonanCraft.BetterRTP.lib.folialib.enums;

import java.util.function.Supplier;
import me.SuperRonanCraft.BetterRTP.lib.folialib.util.ImplementationTestsUtil;

public enum ImplementationType {
   FOLIA("FoliaImplementation", new Supplier[0], new String[]{"io.papermc.paper.threadedregions.RegionizedServer"}),
   PAPER("PaperImplementation", new Supplier[]{ImplementationTestsUtil::isTaskConsumersSupported}, new String[]{"com.destroystokyo.paper.PaperConfig", "io.papermc.paper.configuration.Configuration"}),
   LEGACY_PAPER("LegacyPaperImplementation", new Supplier[0], new String[]{"com.destroystokyo.paper.PaperConfig", "io.papermc.paper.configuration.Configuration"}),
   SPIGOT("SpigotImplementation", new Supplier[]{ImplementationTestsUtil::isTaskConsumersSupported}, new String[]{"org.spigotmc.SpigotConfig"}),
   LEGACY_SPIGOT("LegacySpigotImplementation", new Supplier[0], new String[]{"org.spigotmc.SpigotConfig"}),
   UNKNOWN("UnsupportedImplementation", new Supplier[0], new String[0]);

   private final String implementationClassName;
   private final Supplier<Boolean>[] tests;
   private final String[] classNames;

   private ImplementationType(String implementationClassName, Supplier<Boolean>[] tests, String... classNames) {
      this.implementationClassName = implementationClassName;
      this.tests = tests;
      this.classNames = classNames;
   }

   public String getImplementationClassName() {
      return this.implementationClassName;
   }

   public Supplier<Boolean>[] getTests() {
      return this.tests;
   }

   public String[] getClassNames() {
      return this.classNames;
   }

   public boolean selfCheck() {
      Supplier[] var1 = this.getTests();
      int var2 = var1.length;

      int var3;
      for(var3 = 0; var3 < var2; ++var3) {
         Supplier<Boolean> test = var1[var3];
         if (!(Boolean)test.get()) {
            return false;
         }
      }

      String[] classNames = this.getClassNames();
      String[] var9 = classNames;
      var3 = classNames.length;
      int var10 = 0;

      while(var10 < var3) {
         String className = var9[var10];

         try {
            Class.forName(className);
            return true;
         } catch (ClassNotFoundException var7) {
            ++var10;
         }
      }

      return false;
   }

   // $FF: synthetic method
   private static ImplementationType[] $values() {
      return new ImplementationType[]{FOLIA, PAPER, LEGACY_PAPER, SPIGOT, LEGACY_SPIGOT, UNKNOWN};
   }
}
