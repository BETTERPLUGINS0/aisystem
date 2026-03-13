package com.volmit.iris.core;

import com.google.gson.Gson;
import com.volmit.iris.Iris;
import com.volmit.iris.util.io.IO;
import com.volmit.iris.util.json.JSONException;
import com.volmit.iris.util.json.JSONObject;
import com.volmit.iris.util.misc.getHardware;
import com.volmit.iris.util.plugin.VolmitSender;
import java.io.File;
import java.io.IOException;
import lombok.Generated;

public class IrisSettings {
   public static IrisSettings settings;
   private IrisSettings.IrisSettingsGeneral general = new IrisSettings.IrisSettingsGeneral();
   private IrisSettings.IrisSettingsWorld world = new IrisSettings.IrisSettingsWorld();
   private IrisSettings.IrisSettingsGUI gui = new IrisSettings.IrisSettingsGUI();
   private IrisSettings.IrisSettingsAutoconfiguration autoConfiguration = new IrisSettings.IrisSettingsAutoconfiguration();
   private IrisSettings.IrisSettingsGenerator generator = new IrisSettings.IrisSettingsGenerator();
   private IrisSettings.IrisSettingsConcurrency concurrency = new IrisSettings.IrisSettingsConcurrency();
   private IrisSettings.IrisSettingsStudio studio = new IrisSettings.IrisSettingsStudio();
   private IrisSettings.IrisSettingsPerformance performance = new IrisSettings.IrisSettingsPerformance();
   private IrisSettings.IrisSettingsUpdater updater = new IrisSettings.IrisSettingsUpdater();
   private IrisSettings.IrisSettingsPregen pregen = new IrisSettings.IrisSettingsPregen();
   private IrisSettings.IrisSettingsSentry sentry = new IrisSettings.IrisSettingsSentry();

   public static int getThreadCount(int c) {
      int var10000;
      switch(var0) {
      case -4:
      case -2:
      case -1:
         var10000 = Runtime.getRuntime().availableProcessors() / -var0;
         break;
      case -3:
      default:
         var10000 = Math.max(var0, 2);
      }

      return Math.max(var10000, 1);
   }

   public static IrisSettings get() {
      if (settings != null) {
         return settings;
      } else {
         settings = new IrisSettings();
         File var0 = Iris.instance.getDataFile(new String[]{"settings.json"});
         if (!var0.exists()) {
            try {
               IO.writeAll(var0, (Object)(new JSONObject((new Gson()).toJson(settings))).toString(4));
            } catch (IOException | JSONException var5) {
               var5.printStackTrace();
               Iris.reportError(var5);
            }
         } else {
            try {
               String var1 = IO.readAll(var0);
               settings = (IrisSettings)(new Gson()).fromJson(var1, IrisSettings.class);

               try {
                  IO.writeAll(var0, (Object)(new JSONObject((new Gson()).toJson(settings))).toString(4));
               } catch (IOException var3) {
                  var3.printStackTrace();
               }
            } catch (Throwable var4) {
               Iris.error("Configuration Error in settings.json! " + var4.getClass().getSimpleName() + ": " + var4.getMessage());
            }
         }

         return settings;
      }
   }

   public static void invalidate() {
      synchronized(settings) {
         settings = null;
      }
   }

   public void forceSave() {
      File var1 = Iris.instance.getDataFile(new String[]{"settings.json"});

      try {
         IO.writeAll(var1, (Object)(new JSONObject((new Gson()).toJson(settings))).toString(4));
      } catch (IOException | JSONException var3) {
         var3.printStackTrace();
         Iris.reportError(var3);
      }

   }

   @Generated
   public IrisSettings.IrisSettingsGeneral getGeneral() {
      return this.general;
   }

   @Generated
   public IrisSettings.IrisSettingsWorld getWorld() {
      return this.world;
   }

   @Generated
   public IrisSettings.IrisSettingsGUI getGui() {
      return this.gui;
   }

   @Generated
   public IrisSettings.IrisSettingsAutoconfiguration getAutoConfiguration() {
      return this.autoConfiguration;
   }

   @Generated
   public IrisSettings.IrisSettingsGenerator getGenerator() {
      return this.generator;
   }

   @Generated
   public IrisSettings.IrisSettingsConcurrency getConcurrency() {
      return this.concurrency;
   }

   @Generated
   public IrisSettings.IrisSettingsStudio getStudio() {
      return this.studio;
   }

   @Generated
   public IrisSettings.IrisSettingsPerformance getPerformance() {
      return this.performance;
   }

   @Generated
   public IrisSettings.IrisSettingsUpdater getUpdater() {
      return this.updater;
   }

   @Generated
   public IrisSettings.IrisSettingsPregen getPregen() {
      return this.pregen;
   }

   @Generated
   public IrisSettings.IrisSettingsSentry getSentry() {
      return this.sentry;
   }

   @Generated
   public void setGeneral(final IrisSettings.IrisSettingsGeneral general) {
      this.general = var1;
   }

   @Generated
   public void setWorld(final IrisSettings.IrisSettingsWorld world) {
      this.world = var1;
   }

   @Generated
   public void setGui(final IrisSettings.IrisSettingsGUI gui) {
      this.gui = var1;
   }

   @Generated
   public void setAutoConfiguration(final IrisSettings.IrisSettingsAutoconfiguration autoConfiguration) {
      this.autoConfiguration = var1;
   }

   @Generated
   public void setGenerator(final IrisSettings.IrisSettingsGenerator generator) {
      this.generator = var1;
   }

   @Generated
   public void setConcurrency(final IrisSettings.IrisSettingsConcurrency concurrency) {
      this.concurrency = var1;
   }

   @Generated
   public void setStudio(final IrisSettings.IrisSettingsStudio studio) {
      this.studio = var1;
   }

   @Generated
   public void setPerformance(final IrisSettings.IrisSettingsPerformance performance) {
      this.performance = var1;
   }

   @Generated
   public void setUpdater(final IrisSettings.IrisSettingsUpdater updater) {
      this.updater = var1;
   }

   @Generated
   public void setPregen(final IrisSettings.IrisSettingsPregen pregen) {
      this.pregen = var1;
   }

   @Generated
   public void setSentry(final IrisSettings.IrisSettingsSentry sentry) {
      this.sentry = var1;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisSettings)) {
         return false;
      } else {
         IrisSettings var2 = (IrisSettings)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            label143: {
               IrisSettings.IrisSettingsGeneral var3 = this.getGeneral();
               IrisSettings.IrisSettingsGeneral var4 = var2.getGeneral();
               if (var3 == null) {
                  if (var4 == null) {
                     break label143;
                  }
               } else if (var3.equals(var4)) {
                  break label143;
               }

               return false;
            }

            IrisSettings.IrisSettingsWorld var5 = this.getWorld();
            IrisSettings.IrisSettingsWorld var6 = var2.getWorld();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            IrisSettings.IrisSettingsGUI var7 = this.getGui();
            IrisSettings.IrisSettingsGUI var8 = var2.getGui();
            if (var7 == null) {
               if (var8 != null) {
                  return false;
               }
            } else if (!var7.equals(var8)) {
               return false;
            }

            label122: {
               IrisSettings.IrisSettingsAutoconfiguration var9 = this.getAutoConfiguration();
               IrisSettings.IrisSettingsAutoconfiguration var10 = var2.getAutoConfiguration();
               if (var9 == null) {
                  if (var10 == null) {
                     break label122;
                  }
               } else if (var9.equals(var10)) {
                  break label122;
               }

               return false;
            }

            label115: {
               IrisSettings.IrisSettingsGenerator var11 = this.getGenerator();
               IrisSettings.IrisSettingsGenerator var12 = var2.getGenerator();
               if (var11 == null) {
                  if (var12 == null) {
                     break label115;
                  }
               } else if (var11.equals(var12)) {
                  break label115;
               }

               return false;
            }

            IrisSettings.IrisSettingsConcurrency var13 = this.getConcurrency();
            IrisSettings.IrisSettingsConcurrency var14 = var2.getConcurrency();
            if (var13 == null) {
               if (var14 != null) {
                  return false;
               }
            } else if (!var13.equals(var14)) {
               return false;
            }

            IrisSettings.IrisSettingsStudio var15 = this.getStudio();
            IrisSettings.IrisSettingsStudio var16 = var2.getStudio();
            if (var15 == null) {
               if (var16 != null) {
                  return false;
               }
            } else if (!var15.equals(var16)) {
               return false;
            }

            label94: {
               IrisSettings.IrisSettingsPerformance var17 = this.getPerformance();
               IrisSettings.IrisSettingsPerformance var18 = var2.getPerformance();
               if (var17 == null) {
                  if (var18 == null) {
                     break label94;
                  }
               } else if (var17.equals(var18)) {
                  break label94;
               }

               return false;
            }

            label87: {
               IrisSettings.IrisSettingsUpdater var19 = this.getUpdater();
               IrisSettings.IrisSettingsUpdater var20 = var2.getUpdater();
               if (var19 == null) {
                  if (var20 == null) {
                     break label87;
                  }
               } else if (var19.equals(var20)) {
                  break label87;
               }

               return false;
            }

            IrisSettings.IrisSettingsPregen var21 = this.getPregen();
            IrisSettings.IrisSettingsPregen var22 = var2.getPregen();
            if (var21 == null) {
               if (var22 != null) {
                  return false;
               }
            } else if (!var21.equals(var22)) {
               return false;
            }

            IrisSettings.IrisSettingsSentry var23 = this.getSentry();
            IrisSettings.IrisSettingsSentry var24 = var2.getSentry();
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
      return var1 instanceof IrisSettings;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      IrisSettings.IrisSettingsGeneral var3 = this.getGeneral();
      int var14 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      IrisSettings.IrisSettingsWorld var4 = this.getWorld();
      var14 = var14 * 59 + (var4 == null ? 43 : var4.hashCode());
      IrisSettings.IrisSettingsGUI var5 = this.getGui();
      var14 = var14 * 59 + (var5 == null ? 43 : var5.hashCode());
      IrisSettings.IrisSettingsAutoconfiguration var6 = this.getAutoConfiguration();
      var14 = var14 * 59 + (var6 == null ? 43 : var6.hashCode());
      IrisSettings.IrisSettingsGenerator var7 = this.getGenerator();
      var14 = var14 * 59 + (var7 == null ? 43 : var7.hashCode());
      IrisSettings.IrisSettingsConcurrency var8 = this.getConcurrency();
      var14 = var14 * 59 + (var8 == null ? 43 : var8.hashCode());
      IrisSettings.IrisSettingsStudio var9 = this.getStudio();
      var14 = var14 * 59 + (var9 == null ? 43 : var9.hashCode());
      IrisSettings.IrisSettingsPerformance var10 = this.getPerformance();
      var14 = var14 * 59 + (var10 == null ? 43 : var10.hashCode());
      IrisSettings.IrisSettingsUpdater var11 = this.getUpdater();
      var14 = var14 * 59 + (var11 == null ? 43 : var11.hashCode());
      IrisSettings.IrisSettingsPregen var12 = this.getPregen();
      var14 = var14 * 59 + (var12 == null ? 43 : var12.hashCode());
      IrisSettings.IrisSettingsSentry var13 = this.getSentry();
      var14 = var14 * 59 + (var13 == null ? 43 : var13.hashCode());
      return var14;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getGeneral());
      return "IrisSettings(general=" + var10000 + ", world=" + String.valueOf(this.getWorld()) + ", gui=" + String.valueOf(this.getGui()) + ", autoConfiguration=" + String.valueOf(this.getAutoConfiguration()) + ", generator=" + String.valueOf(this.getGenerator()) + ", concurrency=" + String.valueOf(this.getConcurrency()) + ", studio=" + String.valueOf(this.getStudio()) + ", performance=" + String.valueOf(this.getPerformance()) + ", updater=" + String.valueOf(this.getUpdater()) + ", pregen=" + String.valueOf(this.getPregen()) + ", sentry=" + String.valueOf(this.getSentry()) + ")";
   }

   public static class IrisSettingsGeneral {
      public boolean DoomsdayAnnihilationSelfDestructMode = false;
      public boolean commandSounds = true;
      public boolean debug = false;
      public boolean dumpMantleOnError = false;
      public boolean disableNMS = false;
      public boolean pluginMetrics = true;
      public boolean splashLogoStartup = true;
      public boolean useConsoleCustomColors = true;
      public boolean useCustomColorsIngame = true;
      public boolean adjustVanillaHeight = false;
      public String forceMainWorld = "";
      public int spinh = -20;
      public int spins = 7;
      public int spinb = 8;
      public String cartographerMessage = "Iris does not allow cartographers in its world due to crashes.";

      public boolean canUseCustomColors(VolmitSender volmitSender) {
         return var1.isPlayer() ? this.useCustomColorsIngame : this.useConsoleCustomColors;
      }

      @Generated
      public boolean isDoomsdayAnnihilationSelfDestructMode() {
         return this.DoomsdayAnnihilationSelfDestructMode;
      }

      @Generated
      public boolean isCommandSounds() {
         return this.commandSounds;
      }

      @Generated
      public boolean isDebug() {
         return this.debug;
      }

      @Generated
      public boolean isDumpMantleOnError() {
         return this.dumpMantleOnError;
      }

      @Generated
      public boolean isDisableNMS() {
         return this.disableNMS;
      }

      @Generated
      public boolean isPluginMetrics() {
         return this.pluginMetrics;
      }

      @Generated
      public boolean isSplashLogoStartup() {
         return this.splashLogoStartup;
      }

      @Generated
      public boolean isUseConsoleCustomColors() {
         return this.useConsoleCustomColors;
      }

      @Generated
      public boolean isUseCustomColorsIngame() {
         return this.useCustomColorsIngame;
      }

      @Generated
      public boolean isAdjustVanillaHeight() {
         return this.adjustVanillaHeight;
      }

      @Generated
      public String getForceMainWorld() {
         return this.forceMainWorld;
      }

      @Generated
      public int getSpinh() {
         return this.spinh;
      }

      @Generated
      public int getSpins() {
         return this.spins;
      }

      @Generated
      public int getSpinb() {
         return this.spinb;
      }

      @Generated
      public String getCartographerMessage() {
         return this.cartographerMessage;
      }

      @Generated
      public void setDoomsdayAnnihilationSelfDestructMode(final boolean DoomsdayAnnihilationSelfDestructMode) {
         this.DoomsdayAnnihilationSelfDestructMode = var1;
      }

      @Generated
      public void setCommandSounds(final boolean commandSounds) {
         this.commandSounds = var1;
      }

      @Generated
      public void setDebug(final boolean debug) {
         this.debug = var1;
      }

      @Generated
      public void setDumpMantleOnError(final boolean dumpMantleOnError) {
         this.dumpMantleOnError = var1;
      }

      @Generated
      public void setDisableNMS(final boolean disableNMS) {
         this.disableNMS = var1;
      }

      @Generated
      public void setPluginMetrics(final boolean pluginMetrics) {
         this.pluginMetrics = var1;
      }

      @Generated
      public void setSplashLogoStartup(final boolean splashLogoStartup) {
         this.splashLogoStartup = var1;
      }

      @Generated
      public void setUseConsoleCustomColors(final boolean useConsoleCustomColors) {
         this.useConsoleCustomColors = var1;
      }

      @Generated
      public void setUseCustomColorsIngame(final boolean useCustomColorsIngame) {
         this.useCustomColorsIngame = var1;
      }

      @Generated
      public void setAdjustVanillaHeight(final boolean adjustVanillaHeight) {
         this.adjustVanillaHeight = var1;
      }

      @Generated
      public void setForceMainWorld(final String forceMainWorld) {
         this.forceMainWorld = var1;
      }

      @Generated
      public void setSpinh(final int spinh) {
         this.spinh = var1;
      }

      @Generated
      public void setSpins(final int spins) {
         this.spins = var1;
      }

      @Generated
      public void setSpinb(final int spinb) {
         this.spinb = var1;
      }

      @Generated
      public void setCartographerMessage(final String cartographerMessage) {
         this.cartographerMessage = var1;
      }

      @Generated
      public boolean equals(final Object o) {
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof IrisSettings.IrisSettingsGeneral)) {
            return false;
         } else {
            IrisSettings.IrisSettingsGeneral var2 = (IrisSettings.IrisSettingsGeneral)var1;
            if (!var2.canEqual(this)) {
               return false;
            } else if (this.isDoomsdayAnnihilationSelfDestructMode() != var2.isDoomsdayAnnihilationSelfDestructMode()) {
               return false;
            } else if (this.isCommandSounds() != var2.isCommandSounds()) {
               return false;
            } else if (this.isDebug() != var2.isDebug()) {
               return false;
            } else if (this.isDumpMantleOnError() != var2.isDumpMantleOnError()) {
               return false;
            } else if (this.isDisableNMS() != var2.isDisableNMS()) {
               return false;
            } else if (this.isPluginMetrics() != var2.isPluginMetrics()) {
               return false;
            } else if (this.isSplashLogoStartup() != var2.isSplashLogoStartup()) {
               return false;
            } else if (this.isUseConsoleCustomColors() != var2.isUseConsoleCustomColors()) {
               return false;
            } else if (this.isUseCustomColorsIngame() != var2.isUseCustomColorsIngame()) {
               return false;
            } else if (this.isAdjustVanillaHeight() != var2.isAdjustVanillaHeight()) {
               return false;
            } else if (this.getSpinh() != var2.getSpinh()) {
               return false;
            } else if (this.getSpins() != var2.getSpins()) {
               return false;
            } else if (this.getSpinb() != var2.getSpinb()) {
               return false;
            } else {
               String var3 = this.getForceMainWorld();
               String var4 = var2.getForceMainWorld();
               if (var3 == null) {
                  if (var4 != null) {
                     return false;
                  }
               } else if (!var3.equals(var4)) {
                  return false;
               }

               String var5 = this.getCartographerMessage();
               String var6 = var2.getCartographerMessage();
               if (var5 == null) {
                  if (var6 != null) {
                     return false;
                  }
               } else if (!var5.equals(var6)) {
                  return false;
               }

               return true;
            }
         }
      }

      @Generated
      protected boolean canEqual(final Object other) {
         return var1 instanceof IrisSettings.IrisSettingsGeneral;
      }

      @Generated
      public int hashCode() {
         boolean var1 = true;
         byte var2 = 1;
         int var5 = var2 * 59 + (this.isDoomsdayAnnihilationSelfDestructMode() ? 79 : 97);
         var5 = var5 * 59 + (this.isCommandSounds() ? 79 : 97);
         var5 = var5 * 59 + (this.isDebug() ? 79 : 97);
         var5 = var5 * 59 + (this.isDumpMantleOnError() ? 79 : 97);
         var5 = var5 * 59 + (this.isDisableNMS() ? 79 : 97);
         var5 = var5 * 59 + (this.isPluginMetrics() ? 79 : 97);
         var5 = var5 * 59 + (this.isSplashLogoStartup() ? 79 : 97);
         var5 = var5 * 59 + (this.isUseConsoleCustomColors() ? 79 : 97);
         var5 = var5 * 59 + (this.isUseCustomColorsIngame() ? 79 : 97);
         var5 = var5 * 59 + (this.isAdjustVanillaHeight() ? 79 : 97);
         var5 = var5 * 59 + this.getSpinh();
         var5 = var5 * 59 + this.getSpins();
         var5 = var5 * 59 + this.getSpinb();
         String var3 = this.getForceMainWorld();
         var5 = var5 * 59 + (var3 == null ? 43 : var3.hashCode());
         String var4 = this.getCartographerMessage();
         var5 = var5 * 59 + (var4 == null ? 43 : var4.hashCode());
         return var5;
      }

      @Generated
      public String toString() {
         boolean var10000 = this.isDoomsdayAnnihilationSelfDestructMode();
         return "IrisSettings.IrisSettingsGeneral(DoomsdayAnnihilationSelfDestructMode=" + var10000 + ", commandSounds=" + this.isCommandSounds() + ", debug=" + this.isDebug() + ", dumpMantleOnError=" + this.isDumpMantleOnError() + ", disableNMS=" + this.isDisableNMS() + ", pluginMetrics=" + this.isPluginMetrics() + ", splashLogoStartup=" + this.isSplashLogoStartup() + ", useConsoleCustomColors=" + this.isUseConsoleCustomColors() + ", useCustomColorsIngame=" + this.isUseCustomColorsIngame() + ", adjustVanillaHeight=" + this.isAdjustVanillaHeight() + ", forceMainWorld=" + this.getForceMainWorld() + ", spinh=" + this.getSpinh() + ", spins=" + this.getSpins() + ", spinb=" + this.getSpinb() + ", cartographerMessage=" + this.getCartographerMessage() + ")";
      }
   }

   public static class IrisSettingsWorld {
      public IrisSettings.IrisAsyncTeleport asyncTeleport = new IrisSettings.IrisAsyncTeleport();
      public boolean postLoadBlockUpdates = true;
      public boolean forcePersistEntities = true;
      public boolean anbientEntitySpawningSystem = true;
      public long asyncTickIntervalMS = 700L;
      public double targetSpawnEntitiesPerChunk = 0.95D;
      public boolean markerEntitySpawningSystem = true;
      public boolean effectSystem = true;
      public boolean worldEditWandCUI = true;
      public boolean globalPregenCache = false;

      @Generated
      public IrisSettings.IrisAsyncTeleport getAsyncTeleport() {
         return this.asyncTeleport;
      }

      @Generated
      public boolean isPostLoadBlockUpdates() {
         return this.postLoadBlockUpdates;
      }

      @Generated
      public boolean isForcePersistEntities() {
         return this.forcePersistEntities;
      }

      @Generated
      public boolean isAnbientEntitySpawningSystem() {
         return this.anbientEntitySpawningSystem;
      }

      @Generated
      public long getAsyncTickIntervalMS() {
         return this.asyncTickIntervalMS;
      }

      @Generated
      public double getTargetSpawnEntitiesPerChunk() {
         return this.targetSpawnEntitiesPerChunk;
      }

      @Generated
      public boolean isMarkerEntitySpawningSystem() {
         return this.markerEntitySpawningSystem;
      }

      @Generated
      public boolean isEffectSystem() {
         return this.effectSystem;
      }

      @Generated
      public boolean isWorldEditWandCUI() {
         return this.worldEditWandCUI;
      }

      @Generated
      public boolean isGlobalPregenCache() {
         return this.globalPregenCache;
      }

      @Generated
      public void setAsyncTeleport(final IrisSettings.IrisAsyncTeleport asyncTeleport) {
         this.asyncTeleport = var1;
      }

      @Generated
      public void setPostLoadBlockUpdates(final boolean postLoadBlockUpdates) {
         this.postLoadBlockUpdates = var1;
      }

      @Generated
      public void setForcePersistEntities(final boolean forcePersistEntities) {
         this.forcePersistEntities = var1;
      }

      @Generated
      public void setAnbientEntitySpawningSystem(final boolean anbientEntitySpawningSystem) {
         this.anbientEntitySpawningSystem = var1;
      }

      @Generated
      public void setAsyncTickIntervalMS(final long asyncTickIntervalMS) {
         this.asyncTickIntervalMS = var1;
      }

      @Generated
      public void setTargetSpawnEntitiesPerChunk(final double targetSpawnEntitiesPerChunk) {
         this.targetSpawnEntitiesPerChunk = var1;
      }

      @Generated
      public void setMarkerEntitySpawningSystem(final boolean markerEntitySpawningSystem) {
         this.markerEntitySpawningSystem = var1;
      }

      @Generated
      public void setEffectSystem(final boolean effectSystem) {
         this.effectSystem = var1;
      }

      @Generated
      public void setWorldEditWandCUI(final boolean worldEditWandCUI) {
         this.worldEditWandCUI = var1;
      }

      @Generated
      public void setGlobalPregenCache(final boolean globalPregenCache) {
         this.globalPregenCache = var1;
      }

      @Generated
      public boolean equals(final Object o) {
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof IrisSettings.IrisSettingsWorld)) {
            return false;
         } else {
            IrisSettings.IrisSettingsWorld var2 = (IrisSettings.IrisSettingsWorld)var1;
            if (!var2.canEqual(this)) {
               return false;
            } else if (this.isPostLoadBlockUpdates() != var2.isPostLoadBlockUpdates()) {
               return false;
            } else if (this.isForcePersistEntities() != var2.isForcePersistEntities()) {
               return false;
            } else if (this.isAnbientEntitySpawningSystem() != var2.isAnbientEntitySpawningSystem()) {
               return false;
            } else if (this.getAsyncTickIntervalMS() != var2.getAsyncTickIntervalMS()) {
               return false;
            } else if (Double.compare(this.getTargetSpawnEntitiesPerChunk(), var2.getTargetSpawnEntitiesPerChunk()) != 0) {
               return false;
            } else if (this.isMarkerEntitySpawningSystem() != var2.isMarkerEntitySpawningSystem()) {
               return false;
            } else if (this.isEffectSystem() != var2.isEffectSystem()) {
               return false;
            } else if (this.isWorldEditWandCUI() != var2.isWorldEditWandCUI()) {
               return false;
            } else if (this.isGlobalPregenCache() != var2.isGlobalPregenCache()) {
               return false;
            } else {
               IrisSettings.IrisAsyncTeleport var3 = this.getAsyncTeleport();
               IrisSettings.IrisAsyncTeleport var4 = var2.getAsyncTeleport();
               if (var3 == null) {
                  if (var4 != null) {
                     return false;
                  }
               } else if (!var3.equals(var4)) {
                  return false;
               }

               return true;
            }
         }
      }

      @Generated
      protected boolean canEqual(final Object other) {
         return var1 instanceof IrisSettings.IrisSettingsWorld;
      }

      @Generated
      public int hashCode() {
         boolean var1 = true;
         byte var2 = 1;
         int var8 = var2 * 59 + (this.isPostLoadBlockUpdates() ? 79 : 97);
         var8 = var8 * 59 + (this.isForcePersistEntities() ? 79 : 97);
         var8 = var8 * 59 + (this.isAnbientEntitySpawningSystem() ? 79 : 97);
         long var3 = this.getAsyncTickIntervalMS();
         var8 = var8 * 59 + (int)(var3 >>> 32 ^ var3);
         long var5 = Double.doubleToLongBits(this.getTargetSpawnEntitiesPerChunk());
         var8 = var8 * 59 + (int)(var5 >>> 32 ^ var5);
         var8 = var8 * 59 + (this.isMarkerEntitySpawningSystem() ? 79 : 97);
         var8 = var8 * 59 + (this.isEffectSystem() ? 79 : 97);
         var8 = var8 * 59 + (this.isWorldEditWandCUI() ? 79 : 97);
         var8 = var8 * 59 + (this.isGlobalPregenCache() ? 79 : 97);
         IrisSettings.IrisAsyncTeleport var7 = this.getAsyncTeleport();
         var8 = var8 * 59 + (var7 == null ? 43 : var7.hashCode());
         return var8;
      }

      @Generated
      public String toString() {
         String var10000 = String.valueOf(this.getAsyncTeleport());
         return "IrisSettings.IrisSettingsWorld(asyncTeleport=" + var10000 + ", postLoadBlockUpdates=" + this.isPostLoadBlockUpdates() + ", forcePersistEntities=" + this.isForcePersistEntities() + ", anbientEntitySpawningSystem=" + this.isAnbientEntitySpawningSystem() + ", asyncTickIntervalMS=" + this.getAsyncTickIntervalMS() + ", targetSpawnEntitiesPerChunk=" + this.getTargetSpawnEntitiesPerChunk() + ", markerEntitySpawningSystem=" + this.isMarkerEntitySpawningSystem() + ", effectSystem=" + this.isEffectSystem() + ", worldEditWandCUI=" + this.isWorldEditWandCUI() + ", globalPregenCache=" + this.isGlobalPregenCache() + ")";
      }
   }

   public static class IrisSettingsGUI {
      public boolean useServerLaunchedGuis = true;
      public boolean maximumPregenGuiFPS = false;
      public boolean colorMode = true;

      @Generated
      public boolean isUseServerLaunchedGuis() {
         return this.useServerLaunchedGuis;
      }

      @Generated
      public boolean isMaximumPregenGuiFPS() {
         return this.maximumPregenGuiFPS;
      }

      @Generated
      public boolean isColorMode() {
         return this.colorMode;
      }

      @Generated
      public void setUseServerLaunchedGuis(final boolean useServerLaunchedGuis) {
         this.useServerLaunchedGuis = var1;
      }

      @Generated
      public void setMaximumPregenGuiFPS(final boolean maximumPregenGuiFPS) {
         this.maximumPregenGuiFPS = var1;
      }

      @Generated
      public void setColorMode(final boolean colorMode) {
         this.colorMode = var1;
      }

      @Generated
      public boolean equals(final Object o) {
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof IrisSettings.IrisSettingsGUI)) {
            return false;
         } else {
            IrisSettings.IrisSettingsGUI var2 = (IrisSettings.IrisSettingsGUI)var1;
            if (!var2.canEqual(this)) {
               return false;
            } else if (this.isUseServerLaunchedGuis() != var2.isUseServerLaunchedGuis()) {
               return false;
            } else if (this.isMaximumPregenGuiFPS() != var2.isMaximumPregenGuiFPS()) {
               return false;
            } else {
               return this.isColorMode() == var2.isColorMode();
            }
         }
      }

      @Generated
      protected boolean canEqual(final Object other) {
         return var1 instanceof IrisSettings.IrisSettingsGUI;
      }

      @Generated
      public int hashCode() {
         boolean var1 = true;
         byte var2 = 1;
         int var3 = var2 * 59 + (this.isUseServerLaunchedGuis() ? 79 : 97);
         var3 = var3 * 59 + (this.isMaximumPregenGuiFPS() ? 79 : 97);
         var3 = var3 * 59 + (this.isColorMode() ? 79 : 97);
         return var3;
      }

      @Generated
      public String toString() {
         boolean var10000 = this.isUseServerLaunchedGuis();
         return "IrisSettings.IrisSettingsGUI(useServerLaunchedGuis=" + var10000 + ", maximumPregenGuiFPS=" + this.isMaximumPregenGuiFPS() + ", colorMode=" + this.isColorMode() + ")";
      }
   }

   public static class IrisSettingsAutoconfiguration {
      public boolean configureSpigotTimeoutTime = true;
      public boolean configurePaperWatchdogDelay = true;
      public boolean autoRestartOnCustomBiomeInstall = true;

      @Generated
      public boolean isConfigureSpigotTimeoutTime() {
         return this.configureSpigotTimeoutTime;
      }

      @Generated
      public boolean isConfigurePaperWatchdogDelay() {
         return this.configurePaperWatchdogDelay;
      }

      @Generated
      public boolean isAutoRestartOnCustomBiomeInstall() {
         return this.autoRestartOnCustomBiomeInstall;
      }

      @Generated
      public void setConfigureSpigotTimeoutTime(final boolean configureSpigotTimeoutTime) {
         this.configureSpigotTimeoutTime = var1;
      }

      @Generated
      public void setConfigurePaperWatchdogDelay(final boolean configurePaperWatchdogDelay) {
         this.configurePaperWatchdogDelay = var1;
      }

      @Generated
      public void setAutoRestartOnCustomBiomeInstall(final boolean autoRestartOnCustomBiomeInstall) {
         this.autoRestartOnCustomBiomeInstall = var1;
      }

      @Generated
      public boolean equals(final Object o) {
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof IrisSettings.IrisSettingsAutoconfiguration)) {
            return false;
         } else {
            IrisSettings.IrisSettingsAutoconfiguration var2 = (IrisSettings.IrisSettingsAutoconfiguration)var1;
            if (!var2.canEqual(this)) {
               return false;
            } else if (this.isConfigureSpigotTimeoutTime() != var2.isConfigureSpigotTimeoutTime()) {
               return false;
            } else if (this.isConfigurePaperWatchdogDelay() != var2.isConfigurePaperWatchdogDelay()) {
               return false;
            } else {
               return this.isAutoRestartOnCustomBiomeInstall() == var2.isAutoRestartOnCustomBiomeInstall();
            }
         }
      }

      @Generated
      protected boolean canEqual(final Object other) {
         return var1 instanceof IrisSettings.IrisSettingsAutoconfiguration;
      }

      @Generated
      public int hashCode() {
         boolean var1 = true;
         byte var2 = 1;
         int var3 = var2 * 59 + (this.isConfigureSpigotTimeoutTime() ? 79 : 97);
         var3 = var3 * 59 + (this.isConfigurePaperWatchdogDelay() ? 79 : 97);
         var3 = var3 * 59 + (this.isAutoRestartOnCustomBiomeInstall() ? 79 : 97);
         return var3;
      }

      @Generated
      public String toString() {
         boolean var10000 = this.isConfigureSpigotTimeoutTime();
         return "IrisSettings.IrisSettingsAutoconfiguration(configureSpigotTimeoutTime=" + var10000 + ", configurePaperWatchdogDelay=" + this.isConfigurePaperWatchdogDelay() + ", autoRestartOnCustomBiomeInstall=" + this.isAutoRestartOnCustomBiomeInstall() + ")";
      }
   }

   public static class IrisSettingsGenerator {
      public String defaultWorldType = "overworld";
      public int maxBiomeChildDepth = 4;
      public boolean preventLeafDecay = true;
      public boolean useMulticore = false;
      public boolean useMulticoreMantle = false;
      public boolean offsetNoiseTypes = false;
      public boolean earlyCustomBlocks = false;

      @Generated
      public String getDefaultWorldType() {
         return this.defaultWorldType;
      }

      @Generated
      public int getMaxBiomeChildDepth() {
         return this.maxBiomeChildDepth;
      }

      @Generated
      public boolean isPreventLeafDecay() {
         return this.preventLeafDecay;
      }

      @Generated
      public boolean isUseMulticore() {
         return this.useMulticore;
      }

      @Generated
      public boolean isUseMulticoreMantle() {
         return this.useMulticoreMantle;
      }

      @Generated
      public boolean isOffsetNoiseTypes() {
         return this.offsetNoiseTypes;
      }

      @Generated
      public boolean isEarlyCustomBlocks() {
         return this.earlyCustomBlocks;
      }

      @Generated
      public void setDefaultWorldType(final String defaultWorldType) {
         this.defaultWorldType = var1;
      }

      @Generated
      public void setMaxBiomeChildDepth(final int maxBiomeChildDepth) {
         this.maxBiomeChildDepth = var1;
      }

      @Generated
      public void setPreventLeafDecay(final boolean preventLeafDecay) {
         this.preventLeafDecay = var1;
      }

      @Generated
      public void setUseMulticore(final boolean useMulticore) {
         this.useMulticore = var1;
      }

      @Generated
      public void setUseMulticoreMantle(final boolean useMulticoreMantle) {
         this.useMulticoreMantle = var1;
      }

      @Generated
      public void setOffsetNoiseTypes(final boolean offsetNoiseTypes) {
         this.offsetNoiseTypes = var1;
      }

      @Generated
      public void setEarlyCustomBlocks(final boolean earlyCustomBlocks) {
         this.earlyCustomBlocks = var1;
      }

      @Generated
      public boolean equals(final Object o) {
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof IrisSettings.IrisSettingsGenerator)) {
            return false;
         } else {
            IrisSettings.IrisSettingsGenerator var2 = (IrisSettings.IrisSettingsGenerator)var1;
            if (!var2.canEqual(this)) {
               return false;
            } else if (this.getMaxBiomeChildDepth() != var2.getMaxBiomeChildDepth()) {
               return false;
            } else if (this.isPreventLeafDecay() != var2.isPreventLeafDecay()) {
               return false;
            } else if (this.isUseMulticore() != var2.isUseMulticore()) {
               return false;
            } else if (this.isUseMulticoreMantle() != var2.isUseMulticoreMantle()) {
               return false;
            } else if (this.isOffsetNoiseTypes() != var2.isOffsetNoiseTypes()) {
               return false;
            } else if (this.isEarlyCustomBlocks() != var2.isEarlyCustomBlocks()) {
               return false;
            } else {
               String var3 = this.getDefaultWorldType();
               String var4 = var2.getDefaultWorldType();
               if (var3 == null) {
                  if (var4 != null) {
                     return false;
                  }
               } else if (!var3.equals(var4)) {
                  return false;
               }

               return true;
            }
         }
      }

      @Generated
      protected boolean canEqual(final Object other) {
         return var1 instanceof IrisSettings.IrisSettingsGenerator;
      }

      @Generated
      public int hashCode() {
         boolean var1 = true;
         byte var2 = 1;
         int var4 = var2 * 59 + this.getMaxBiomeChildDepth();
         var4 = var4 * 59 + (this.isPreventLeafDecay() ? 79 : 97);
         var4 = var4 * 59 + (this.isUseMulticore() ? 79 : 97);
         var4 = var4 * 59 + (this.isUseMulticoreMantle() ? 79 : 97);
         var4 = var4 * 59 + (this.isOffsetNoiseTypes() ? 79 : 97);
         var4 = var4 * 59 + (this.isEarlyCustomBlocks() ? 79 : 97);
         String var3 = this.getDefaultWorldType();
         var4 = var4 * 59 + (var3 == null ? 43 : var3.hashCode());
         return var4;
      }

      @Generated
      public String toString() {
         String var10000 = this.getDefaultWorldType();
         return "IrisSettings.IrisSettingsGenerator(defaultWorldType=" + var10000 + ", maxBiomeChildDepth=" + this.getMaxBiomeChildDepth() + ", preventLeafDecay=" + this.isPreventLeafDecay() + ", useMulticore=" + this.isUseMulticore() + ", useMulticoreMantle=" + this.isUseMulticoreMantle() + ", offsetNoiseTypes=" + this.isOffsetNoiseTypes() + ", earlyCustomBlocks=" + this.isEarlyCustomBlocks() + ")";
      }
   }

   public static class IrisSettingsConcurrency {
      public int parallelism = -1;
      public int ioParallelism = -2;
      public int worldGenParallelism = -1;

      public int getWorldGenThreads() {
         return IrisSettings.getThreadCount(this.worldGenParallelism);
      }

      @Generated
      public int getParallelism() {
         return this.parallelism;
      }

      @Generated
      public int getIoParallelism() {
         return this.ioParallelism;
      }

      @Generated
      public int getWorldGenParallelism() {
         return this.worldGenParallelism;
      }

      @Generated
      public void setParallelism(final int parallelism) {
         this.parallelism = var1;
      }

      @Generated
      public void setIoParallelism(final int ioParallelism) {
         this.ioParallelism = var1;
      }

      @Generated
      public void setWorldGenParallelism(final int worldGenParallelism) {
         this.worldGenParallelism = var1;
      }

      @Generated
      public boolean equals(final Object o) {
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof IrisSettings.IrisSettingsConcurrency)) {
            return false;
         } else {
            IrisSettings.IrisSettingsConcurrency var2 = (IrisSettings.IrisSettingsConcurrency)var1;
            if (!var2.canEqual(this)) {
               return false;
            } else if (this.getParallelism() != var2.getParallelism()) {
               return false;
            } else if (this.getIoParallelism() != var2.getIoParallelism()) {
               return false;
            } else {
               return this.getWorldGenParallelism() == var2.getWorldGenParallelism();
            }
         }
      }

      @Generated
      protected boolean canEqual(final Object other) {
         return var1 instanceof IrisSettings.IrisSettingsConcurrency;
      }

      @Generated
      public int hashCode() {
         boolean var1 = true;
         byte var2 = 1;
         int var3 = var2 * 59 + this.getParallelism();
         var3 = var3 * 59 + this.getIoParallelism();
         var3 = var3 * 59 + this.getWorldGenParallelism();
         return var3;
      }

      @Generated
      public String toString() {
         int var10000 = this.getParallelism();
         return "IrisSettings.IrisSettingsConcurrency(parallelism=" + var10000 + ", ioParallelism=" + this.getIoParallelism() + ", worldGenParallelism=" + this.getWorldGenParallelism() + ")";
      }
   }

   public static class IrisSettingsStudio {
      public boolean studio = true;
      public boolean openVSCode = true;
      public boolean disableTimeAndWeather = true;
      public boolean autoStartDefaultStudio = false;

      @Generated
      public boolean isStudio() {
         return this.studio;
      }

      @Generated
      public boolean isOpenVSCode() {
         return this.openVSCode;
      }

      @Generated
      public boolean isDisableTimeAndWeather() {
         return this.disableTimeAndWeather;
      }

      @Generated
      public boolean isAutoStartDefaultStudio() {
         return this.autoStartDefaultStudio;
      }

      @Generated
      public void setStudio(final boolean studio) {
         this.studio = var1;
      }

      @Generated
      public void setOpenVSCode(final boolean openVSCode) {
         this.openVSCode = var1;
      }

      @Generated
      public void setDisableTimeAndWeather(final boolean disableTimeAndWeather) {
         this.disableTimeAndWeather = var1;
      }

      @Generated
      public void setAutoStartDefaultStudio(final boolean autoStartDefaultStudio) {
         this.autoStartDefaultStudio = var1;
      }

      @Generated
      public boolean equals(final Object o) {
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof IrisSettings.IrisSettingsStudio)) {
            return false;
         } else {
            IrisSettings.IrisSettingsStudio var2 = (IrisSettings.IrisSettingsStudio)var1;
            if (!var2.canEqual(this)) {
               return false;
            } else if (this.isStudio() != var2.isStudio()) {
               return false;
            } else if (this.isOpenVSCode() != var2.isOpenVSCode()) {
               return false;
            } else if (this.isDisableTimeAndWeather() != var2.isDisableTimeAndWeather()) {
               return false;
            } else {
               return this.isAutoStartDefaultStudio() == var2.isAutoStartDefaultStudio();
            }
         }
      }

      @Generated
      protected boolean canEqual(final Object other) {
         return var1 instanceof IrisSettings.IrisSettingsStudio;
      }

      @Generated
      public int hashCode() {
         boolean var1 = true;
         byte var2 = 1;
         int var3 = var2 * 59 + (this.isStudio() ? 79 : 97);
         var3 = var3 * 59 + (this.isOpenVSCode() ? 79 : 97);
         var3 = var3 * 59 + (this.isDisableTimeAndWeather() ? 79 : 97);
         var3 = var3 * 59 + (this.isAutoStartDefaultStudio() ? 79 : 97);
         return var3;
      }

      @Generated
      public String toString() {
         boolean var10000 = this.isStudio();
         return "IrisSettings.IrisSettingsStudio(studio=" + var10000 + ", openVSCode=" + this.isOpenVSCode() + ", disableTimeAndWeather=" + this.isDisableTimeAndWeather() + ", autoStartDefaultStudio=" + this.isAutoStartDefaultStudio() + ")";
      }
   }

   public static class IrisSettingsPerformance {
      private IrisSettings.IrisSettingsEngineSVC engineSVC = new IrisSettings.IrisSettingsEngineSVC();
      public boolean trimMantleInStudio = false;
      public int mantleKeepAlive = 30;
      public int noiseCacheSize = 1024;
      public int resourceLoaderCacheSize = 1024;
      public int objectLoaderCacheSize = 4096;
      public int scriptLoaderCacheSize = 512;
      public int tectonicPlateSize = -1;
      public int mantleCleanupDelay = 200;

      public int getTectonicPlateSize() {
         return this.tectonicPlateSize > 0 ? this.tectonicPlateSize : (int)(getHardware.getProcessMemory() / 512L);
      }

      @Generated
      public IrisSettings.IrisSettingsEngineSVC getEngineSVC() {
         return this.engineSVC;
      }

      @Generated
      public boolean isTrimMantleInStudio() {
         return this.trimMantleInStudio;
      }

      @Generated
      public int getMantleKeepAlive() {
         return this.mantleKeepAlive;
      }

      @Generated
      public int getNoiseCacheSize() {
         return this.noiseCacheSize;
      }

      @Generated
      public int getResourceLoaderCacheSize() {
         return this.resourceLoaderCacheSize;
      }

      @Generated
      public int getObjectLoaderCacheSize() {
         return this.objectLoaderCacheSize;
      }

      @Generated
      public int getScriptLoaderCacheSize() {
         return this.scriptLoaderCacheSize;
      }

      @Generated
      public int getMantleCleanupDelay() {
         return this.mantleCleanupDelay;
      }

      @Generated
      public void setEngineSVC(final IrisSettings.IrisSettingsEngineSVC engineSVC) {
         this.engineSVC = var1;
      }

      @Generated
      public void setTrimMantleInStudio(final boolean trimMantleInStudio) {
         this.trimMantleInStudio = var1;
      }

      @Generated
      public void setMantleKeepAlive(final int mantleKeepAlive) {
         this.mantleKeepAlive = var1;
      }

      @Generated
      public void setNoiseCacheSize(final int noiseCacheSize) {
         this.noiseCacheSize = var1;
      }

      @Generated
      public void setResourceLoaderCacheSize(final int resourceLoaderCacheSize) {
         this.resourceLoaderCacheSize = var1;
      }

      @Generated
      public void setObjectLoaderCacheSize(final int objectLoaderCacheSize) {
         this.objectLoaderCacheSize = var1;
      }

      @Generated
      public void setScriptLoaderCacheSize(final int scriptLoaderCacheSize) {
         this.scriptLoaderCacheSize = var1;
      }

      @Generated
      public void setTectonicPlateSize(final int tectonicPlateSize) {
         this.tectonicPlateSize = var1;
      }

      @Generated
      public void setMantleCleanupDelay(final int mantleCleanupDelay) {
         this.mantleCleanupDelay = var1;
      }

      @Generated
      public boolean equals(final Object o) {
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof IrisSettings.IrisSettingsPerformance)) {
            return false;
         } else {
            IrisSettings.IrisSettingsPerformance var2 = (IrisSettings.IrisSettingsPerformance)var1;
            if (!var2.canEqual(this)) {
               return false;
            } else if (this.isTrimMantleInStudio() != var2.isTrimMantleInStudio()) {
               return false;
            } else if (this.getMantleKeepAlive() != var2.getMantleKeepAlive()) {
               return false;
            } else if (this.getNoiseCacheSize() != var2.getNoiseCacheSize()) {
               return false;
            } else if (this.getResourceLoaderCacheSize() != var2.getResourceLoaderCacheSize()) {
               return false;
            } else if (this.getObjectLoaderCacheSize() != var2.getObjectLoaderCacheSize()) {
               return false;
            } else if (this.getScriptLoaderCacheSize() != var2.getScriptLoaderCacheSize()) {
               return false;
            } else if (this.getTectonicPlateSize() != var2.getTectonicPlateSize()) {
               return false;
            } else if (this.getMantleCleanupDelay() != var2.getMantleCleanupDelay()) {
               return false;
            } else {
               IrisSettings.IrisSettingsEngineSVC var3 = this.getEngineSVC();
               IrisSettings.IrisSettingsEngineSVC var4 = var2.getEngineSVC();
               if (var3 == null) {
                  if (var4 != null) {
                     return false;
                  }
               } else if (!var3.equals(var4)) {
                  return false;
               }

               return true;
            }
         }
      }

      @Generated
      protected boolean canEqual(final Object other) {
         return var1 instanceof IrisSettings.IrisSettingsPerformance;
      }

      @Generated
      public int hashCode() {
         boolean var1 = true;
         byte var2 = 1;
         int var4 = var2 * 59 + (this.isTrimMantleInStudio() ? 79 : 97);
         var4 = var4 * 59 + this.getMantleKeepAlive();
         var4 = var4 * 59 + this.getNoiseCacheSize();
         var4 = var4 * 59 + this.getResourceLoaderCacheSize();
         var4 = var4 * 59 + this.getObjectLoaderCacheSize();
         var4 = var4 * 59 + this.getScriptLoaderCacheSize();
         var4 = var4 * 59 + this.getTectonicPlateSize();
         var4 = var4 * 59 + this.getMantleCleanupDelay();
         IrisSettings.IrisSettingsEngineSVC var3 = this.getEngineSVC();
         var4 = var4 * 59 + (var3 == null ? 43 : var3.hashCode());
         return var4;
      }

      @Generated
      public String toString() {
         String var10000 = String.valueOf(this.getEngineSVC());
         return "IrisSettings.IrisSettingsPerformance(engineSVC=" + var10000 + ", trimMantleInStudio=" + this.isTrimMantleInStudio() + ", mantleKeepAlive=" + this.getMantleKeepAlive() + ", noiseCacheSize=" + this.getNoiseCacheSize() + ", resourceLoaderCacheSize=" + this.getResourceLoaderCacheSize() + ", objectLoaderCacheSize=" + this.getObjectLoaderCacheSize() + ", scriptLoaderCacheSize=" + this.getScriptLoaderCacheSize() + ", tectonicPlateSize=" + this.getTectonicPlateSize() + ", mantleCleanupDelay=" + this.getMantleCleanupDelay() + ")";
      }
   }

   public static class IrisSettingsUpdater {
      public int maxConcurrency = 256;
      public boolean nativeThreads = false;
      public double threadMultiplier = 2.0D;
      public double chunkLoadSensitivity = 0.7D;
      public IrisSettings.MsRange emptyMsRange = new IrisSettings.MsRange(80, 100);
      public IrisSettings.MsRange defaultMsRange = new IrisSettings.MsRange(20, 40);

      public int getMaxConcurrency() {
         return Math.max(Math.abs(this.maxConcurrency), 1);
      }

      public double getThreadMultiplier() {
         return Math.min(Math.abs(this.threadMultiplier), 0.1D);
      }

      public double getChunkLoadSensitivity() {
         return Math.min(this.chunkLoadSensitivity, 0.9D);
      }

      @Generated
      public boolean isNativeThreads() {
         return this.nativeThreads;
      }

      @Generated
      public IrisSettings.MsRange getEmptyMsRange() {
         return this.emptyMsRange;
      }

      @Generated
      public IrisSettings.MsRange getDefaultMsRange() {
         return this.defaultMsRange;
      }

      @Generated
      public void setMaxConcurrency(final int maxConcurrency) {
         this.maxConcurrency = var1;
      }

      @Generated
      public void setNativeThreads(final boolean nativeThreads) {
         this.nativeThreads = var1;
      }

      @Generated
      public void setThreadMultiplier(final double threadMultiplier) {
         this.threadMultiplier = var1;
      }

      @Generated
      public void setChunkLoadSensitivity(final double chunkLoadSensitivity) {
         this.chunkLoadSensitivity = var1;
      }

      @Generated
      public void setEmptyMsRange(final IrisSettings.MsRange emptyMsRange) {
         this.emptyMsRange = var1;
      }

      @Generated
      public void setDefaultMsRange(final IrisSettings.MsRange defaultMsRange) {
         this.defaultMsRange = var1;
      }

      @Generated
      public boolean equals(final Object o) {
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof IrisSettings.IrisSettingsUpdater)) {
            return false;
         } else {
            IrisSettings.IrisSettingsUpdater var2 = (IrisSettings.IrisSettingsUpdater)var1;
            if (!var2.canEqual(this)) {
               return false;
            } else if (this.getMaxConcurrency() != var2.getMaxConcurrency()) {
               return false;
            } else if (this.isNativeThreads() != var2.isNativeThreads()) {
               return false;
            } else if (Double.compare(this.getThreadMultiplier(), var2.getThreadMultiplier()) != 0) {
               return false;
            } else if (Double.compare(this.getChunkLoadSensitivity(), var2.getChunkLoadSensitivity()) != 0) {
               return false;
            } else {
               IrisSettings.MsRange var3 = this.getEmptyMsRange();
               IrisSettings.MsRange var4 = var2.getEmptyMsRange();
               if (var3 == null) {
                  if (var4 != null) {
                     return false;
                  }
               } else if (!var3.equals(var4)) {
                  return false;
               }

               IrisSettings.MsRange var5 = this.getDefaultMsRange();
               IrisSettings.MsRange var6 = var2.getDefaultMsRange();
               if (var5 == null) {
                  if (var6 != null) {
                     return false;
                  }
               } else if (!var5.equals(var6)) {
                  return false;
               }

               return true;
            }
         }
      }

      @Generated
      protected boolean canEqual(final Object other) {
         return var1 instanceof IrisSettings.IrisSettingsUpdater;
      }

      @Generated
      public int hashCode() {
         boolean var1 = true;
         byte var2 = 1;
         int var9 = var2 * 59 + this.getMaxConcurrency();
         var9 = var9 * 59 + (this.isNativeThreads() ? 79 : 97);
         long var3 = Double.doubleToLongBits(this.getThreadMultiplier());
         var9 = var9 * 59 + (int)(var3 >>> 32 ^ var3);
         long var5 = Double.doubleToLongBits(this.getChunkLoadSensitivity());
         var9 = var9 * 59 + (int)(var5 >>> 32 ^ var5);
         IrisSettings.MsRange var7 = this.getEmptyMsRange();
         var9 = var9 * 59 + (var7 == null ? 43 : var7.hashCode());
         IrisSettings.MsRange var8 = this.getDefaultMsRange();
         var9 = var9 * 59 + (var8 == null ? 43 : var8.hashCode());
         return var9;
      }

      @Generated
      public String toString() {
         int var10000 = this.getMaxConcurrency();
         return "IrisSettings.IrisSettingsUpdater(maxConcurrency=" + var10000 + ", nativeThreads=" + this.isNativeThreads() + ", threadMultiplier=" + this.getThreadMultiplier() + ", chunkLoadSensitivity=" + this.getChunkLoadSensitivity() + ", emptyMsRange=" + String.valueOf(this.getEmptyMsRange()) + ", defaultMsRange=" + String.valueOf(this.getDefaultMsRange()) + ")";
      }
   }

   public static class IrisSettingsPregen {
      public boolean useCacheByDefault = true;
      public boolean useHighPriority = false;
      public boolean useVirtualThreads = false;
      public boolean useTicketQueue = true;
      public int maxConcurrency = 256;

      @Generated
      public boolean isUseCacheByDefault() {
         return this.useCacheByDefault;
      }

      @Generated
      public boolean isUseHighPriority() {
         return this.useHighPriority;
      }

      @Generated
      public boolean isUseVirtualThreads() {
         return this.useVirtualThreads;
      }

      @Generated
      public boolean isUseTicketQueue() {
         return this.useTicketQueue;
      }

      @Generated
      public int getMaxConcurrency() {
         return this.maxConcurrency;
      }

      @Generated
      public void setUseCacheByDefault(final boolean useCacheByDefault) {
         this.useCacheByDefault = var1;
      }

      @Generated
      public void setUseHighPriority(final boolean useHighPriority) {
         this.useHighPriority = var1;
      }

      @Generated
      public void setUseVirtualThreads(final boolean useVirtualThreads) {
         this.useVirtualThreads = var1;
      }

      @Generated
      public void setUseTicketQueue(final boolean useTicketQueue) {
         this.useTicketQueue = var1;
      }

      @Generated
      public void setMaxConcurrency(final int maxConcurrency) {
         this.maxConcurrency = var1;
      }

      @Generated
      public boolean equals(final Object o) {
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof IrisSettings.IrisSettingsPregen)) {
            return false;
         } else {
            IrisSettings.IrisSettingsPregen var2 = (IrisSettings.IrisSettingsPregen)var1;
            if (!var2.canEqual(this)) {
               return false;
            } else if (this.isUseCacheByDefault() != var2.isUseCacheByDefault()) {
               return false;
            } else if (this.isUseHighPriority() != var2.isUseHighPriority()) {
               return false;
            } else if (this.isUseVirtualThreads() != var2.isUseVirtualThreads()) {
               return false;
            } else if (this.isUseTicketQueue() != var2.isUseTicketQueue()) {
               return false;
            } else {
               return this.getMaxConcurrency() == var2.getMaxConcurrency();
            }
         }
      }

      @Generated
      protected boolean canEqual(final Object other) {
         return var1 instanceof IrisSettings.IrisSettingsPregen;
      }

      @Generated
      public int hashCode() {
         boolean var1 = true;
         byte var2 = 1;
         int var3 = var2 * 59 + (this.isUseCacheByDefault() ? 79 : 97);
         var3 = var3 * 59 + (this.isUseHighPriority() ? 79 : 97);
         var3 = var3 * 59 + (this.isUseVirtualThreads() ? 79 : 97);
         var3 = var3 * 59 + (this.isUseTicketQueue() ? 79 : 97);
         var3 = var3 * 59 + this.getMaxConcurrency();
         return var3;
      }

      @Generated
      public String toString() {
         boolean var10000 = this.isUseCacheByDefault();
         return "IrisSettings.IrisSettingsPregen(useCacheByDefault=" + var10000 + ", useHighPriority=" + this.isUseHighPriority() + ", useVirtualThreads=" + this.isUseVirtualThreads() + ", useTicketQueue=" + this.isUseTicketQueue() + ", maxConcurrency=" + this.getMaxConcurrency() + ")";
      }
   }

   public static class IrisSettingsSentry {
      public boolean includeServerId = true;
      public boolean disableAutoReporting = false;
      public boolean debug = false;

      @Generated
      public boolean isIncludeServerId() {
         return this.includeServerId;
      }

      @Generated
      public boolean isDisableAutoReporting() {
         return this.disableAutoReporting;
      }

      @Generated
      public boolean isDebug() {
         return this.debug;
      }

      @Generated
      public void setIncludeServerId(final boolean includeServerId) {
         this.includeServerId = var1;
      }

      @Generated
      public void setDisableAutoReporting(final boolean disableAutoReporting) {
         this.disableAutoReporting = var1;
      }

      @Generated
      public void setDebug(final boolean debug) {
         this.debug = var1;
      }

      @Generated
      public boolean equals(final Object o) {
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof IrisSettings.IrisSettingsSentry)) {
            return false;
         } else {
            IrisSettings.IrisSettingsSentry var2 = (IrisSettings.IrisSettingsSentry)var1;
            if (!var2.canEqual(this)) {
               return false;
            } else if (this.isIncludeServerId() != var2.isIncludeServerId()) {
               return false;
            } else if (this.isDisableAutoReporting() != var2.isDisableAutoReporting()) {
               return false;
            } else {
               return this.isDebug() == var2.isDebug();
            }
         }
      }

      @Generated
      protected boolean canEqual(final Object other) {
         return var1 instanceof IrisSettings.IrisSettingsSentry;
      }

      @Generated
      public int hashCode() {
         boolean var1 = true;
         byte var2 = 1;
         int var3 = var2 * 59 + (this.isIncludeServerId() ? 79 : 97);
         var3 = var3 * 59 + (this.isDisableAutoReporting() ? 79 : 97);
         var3 = var3 * 59 + (this.isDebug() ? 79 : 97);
         return var3;
      }

      @Generated
      public String toString() {
         boolean var10000 = this.isIncludeServerId();
         return "IrisSettings.IrisSettingsSentry(includeServerId=" + var10000 + ", disableAutoReporting=" + this.isDisableAutoReporting() + ", debug=" + this.isDebug() + ")";
      }
   }

   public static class IrisSettingsEngineSVC {
      public boolean useVirtualThreads = true;
      public boolean forceMulticoreWrite = false;
      public int priority = 5;

      public int getPriority() {
         return Math.max(Math.min(this.priority, 10), 1);
      }

      @Generated
      public boolean isUseVirtualThreads() {
         return this.useVirtualThreads;
      }

      @Generated
      public boolean isForceMulticoreWrite() {
         return this.forceMulticoreWrite;
      }

      @Generated
      public void setUseVirtualThreads(final boolean useVirtualThreads) {
         this.useVirtualThreads = var1;
      }

      @Generated
      public void setForceMulticoreWrite(final boolean forceMulticoreWrite) {
         this.forceMulticoreWrite = var1;
      }

      @Generated
      public void setPriority(final int priority) {
         this.priority = var1;
      }

      @Generated
      public boolean equals(final Object o) {
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof IrisSettings.IrisSettingsEngineSVC)) {
            return false;
         } else {
            IrisSettings.IrisSettingsEngineSVC var2 = (IrisSettings.IrisSettingsEngineSVC)var1;
            if (!var2.canEqual(this)) {
               return false;
            } else if (this.isUseVirtualThreads() != var2.isUseVirtualThreads()) {
               return false;
            } else if (this.isForceMulticoreWrite() != var2.isForceMulticoreWrite()) {
               return false;
            } else {
               return this.getPriority() == var2.getPriority();
            }
         }
      }

      @Generated
      protected boolean canEqual(final Object other) {
         return var1 instanceof IrisSettings.IrisSettingsEngineSVC;
      }

      @Generated
      public int hashCode() {
         boolean var1 = true;
         byte var2 = 1;
         int var3 = var2 * 59 + (this.isUseVirtualThreads() ? 79 : 97);
         var3 = var3 * 59 + (this.isForceMulticoreWrite() ? 79 : 97);
         var3 = var3 * 59 + this.getPriority();
         return var3;
      }

      @Generated
      public String toString() {
         boolean var10000 = this.isUseVirtualThreads();
         return "IrisSettings.IrisSettingsEngineSVC(useVirtualThreads=" + var10000 + ", forceMulticoreWrite=" + this.isForceMulticoreWrite() + ", priority=" + this.getPriority() + ")";
      }
   }

   public static class MsRange {
      public int min = 20;
      public int max = 40;

      @Generated
      public int getMin() {
         return this.min;
      }

      @Generated
      public int getMax() {
         return this.max;
      }

      @Generated
      public void setMin(final int min) {
         this.min = var1;
      }

      @Generated
      public void setMax(final int max) {
         this.max = var1;
      }

      @Generated
      public boolean equals(final Object o) {
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof IrisSettings.MsRange)) {
            return false;
         } else {
            IrisSettings.MsRange var2 = (IrisSettings.MsRange)var1;
            if (!var2.canEqual(this)) {
               return false;
            } else if (this.getMin() != var2.getMin()) {
               return false;
            } else {
               return this.getMax() == var2.getMax();
            }
         }
      }

      @Generated
      protected boolean canEqual(final Object other) {
         return var1 instanceof IrisSettings.MsRange;
      }

      @Generated
      public int hashCode() {
         boolean var1 = true;
         byte var2 = 1;
         int var3 = var2 * 59 + this.getMin();
         var3 = var3 * 59 + this.getMax();
         return var3;
      }

      @Generated
      public String toString() {
         int var10000 = this.getMin();
         return "IrisSettings.MsRange(min=" + var10000 + ", max=" + this.getMax() + ")";
      }

      @Generated
      public MsRange() {
      }

      @Generated
      public MsRange(final int min, final int max) {
         this.min = var1;
         this.max = var2;
      }
   }

   public static class IrisAsyncTeleport {
      public boolean enabled = false;
      public int loadViewDistance = 2;
      public boolean urgent = false;

      @Generated
      public boolean isEnabled() {
         return this.enabled;
      }

      @Generated
      public int getLoadViewDistance() {
         return this.loadViewDistance;
      }

      @Generated
      public boolean isUrgent() {
         return this.urgent;
      }

      @Generated
      public void setEnabled(final boolean enabled) {
         this.enabled = var1;
      }

      @Generated
      public void setLoadViewDistance(final int loadViewDistance) {
         this.loadViewDistance = var1;
      }

      @Generated
      public void setUrgent(final boolean urgent) {
         this.urgent = var1;
      }

      @Generated
      public boolean equals(final Object o) {
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof IrisSettings.IrisAsyncTeleport)) {
            return false;
         } else {
            IrisSettings.IrisAsyncTeleport var2 = (IrisSettings.IrisAsyncTeleport)var1;
            if (!var2.canEqual(this)) {
               return false;
            } else if (this.isEnabled() != var2.isEnabled()) {
               return false;
            } else if (this.getLoadViewDistance() != var2.getLoadViewDistance()) {
               return false;
            } else {
               return this.isUrgent() == var2.isUrgent();
            }
         }
      }

      @Generated
      protected boolean canEqual(final Object other) {
         return var1 instanceof IrisSettings.IrisAsyncTeleport;
      }

      @Generated
      public int hashCode() {
         boolean var1 = true;
         byte var2 = 1;
         int var3 = var2 * 59 + (this.isEnabled() ? 79 : 97);
         var3 = var3 * 59 + this.getLoadViewDistance();
         var3 = var3 * 59 + (this.isUrgent() ? 79 : 97);
         return var3;
      }

      @Generated
      public String toString() {
         boolean var10000 = this.isEnabled();
         return "IrisSettings.IrisAsyncTeleport(enabled=" + var10000 + ", loadViewDistance=" + this.getLoadViewDistance() + ", urgent=" + this.isUrgent() + ")";
      }
   }
}
