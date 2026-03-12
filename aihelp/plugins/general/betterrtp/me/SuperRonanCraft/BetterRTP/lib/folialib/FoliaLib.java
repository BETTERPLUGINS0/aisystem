package me.SuperRonanCraft.BetterRTP.lib.folialib;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;
import me.SuperRonanCraft.BetterRTP.lib.folialib.enums.ImplementationType;
import me.SuperRonanCraft.BetterRTP.lib.folialib.impl.ServerImplementation;
import me.SuperRonanCraft.BetterRTP.lib.folialib.util.InvalidTickDelayNotifier;
import org.bukkit.plugin.java.JavaPlugin;

public class FoliaLib {
   private final JavaPlugin plugin;
   private final ImplementationType implementationType;
   private final ServerImplementation implementation;

   public FoliaLib(JavaPlugin plugin) {
      this.plugin = plugin;
      ImplementationType foundType = ImplementationType.UNKNOWN;
      ImplementationType[] var3 = ImplementationType.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         ImplementationType type = var3[var5];
         if (type.selfCheck()) {
            foundType = type;
            break;
         }
      }

      this.implementationType = foundType;
      this.implementation = this.createServerImpl(this.implementationType.getImplementationClassName());
      if (this.implementation == null) {
         throw new IllegalStateException("Failed to create server implementation. Please report this to the FoliaLib GitHub issues page. Forks of server software may not all be supported. If you are using an unofficial fork, please report this to the fork's developers first.");
      } else {
         String originalLocation = "com,tcoded,folialib,".replace(",", ".");
         if (this.getClass().getName().startsWith(originalLocation)) {
            Logger logger = this.plugin.getLogger();
            logger.severe("****************************************************************");
            logger.severe("FoliaLib is not be relocated correctly! This will cause conflicts");
            logger.severe("with other plugins using FoliaLib. Please contact the developers");
            logger.severe(String.format("of '%s' and inform them of this issue immediately!", this.plugin.getDescription().getName()));
            logger.severe("****************************************************************");
         }

      }
   }

   public ImplementationType getImplType() {
      return this.implementationType;
   }

   public ServerImplementation getImpl() {
      return this.implementation;
   }

   public boolean isFolia() {
      return this.implementationType == ImplementationType.FOLIA;
   }

   public boolean isPaper() {
      return this.implementationType == ImplementationType.PAPER || this.implementationType == ImplementationType.LEGACY_PAPER;
   }

   public boolean isSpigot() {
      return this.implementationType == ImplementationType.SPIGOT || this.implementationType == ImplementationType.LEGACY_SPIGOT;
   }

   public boolean isUnsupported() {
      return this.implementationType == ImplementationType.UNKNOWN;
   }

   public JavaPlugin getPlugin() {
      return this.plugin;
   }

   public void disableInvalidTickValueWarning() {
      InvalidTickDelayNotifier.disableNotifications = true;
   }

   public void enableInvalidTickValueDebug() {
      InvalidTickDelayNotifier.debugMode = true;
   }

   private ServerImplementation createServerImpl(String implName) {
      String basePackage = this.getClass().getPackage().getName() + ".impl.";

      try {
         return (ServerImplementation)Class.forName(basePackage + implName).getConstructor(this.getClass()).newInstance(this);
      } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException var4) {
         var4.printStackTrace();
         return null;
      }
   }
}
