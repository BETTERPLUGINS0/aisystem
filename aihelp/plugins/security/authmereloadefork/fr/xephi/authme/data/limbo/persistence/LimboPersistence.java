package fr.xephi.authme.data.limbo.persistence;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.limbo.LimboPlayer;
import fr.xephi.authme.initialization.SettingsDependent;
import fr.xephi.authme.libs.ch.jalu.injector.factory.Factory;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.LimboSettings;
import org.bukkit.entity.Player;

public class LimboPersistence implements SettingsDependent {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(LimboPersistence.class);
   private final Factory<LimboPersistenceHandler> handlerFactory;
   private LimboPersistenceHandler handler;

   @Inject
   LimboPersistence(Settings settings, Factory<LimboPersistenceHandler> handlerFactory) {
      this.handlerFactory = handlerFactory;
      this.reload(settings);
   }

   public LimboPlayer getLimboPlayer(Player player) {
      try {
         return this.handler.getLimboPlayer(player);
      } catch (Exception var3) {
         this.logger.logException("Could not get LimboPlayer for '" + player.getName() + "'", var3);
         return null;
      }
   }

   public void saveLimboPlayer(Player player, LimboPlayer limbo) {
      try {
         this.handler.saveLimboPlayer(player, limbo);
      } catch (Exception var4) {
         this.logger.logException("Could not save LimboPlayer for '" + player.getName() + "'", var4);
      }

   }

   public void removeLimboPlayer(Player player) {
      try {
         this.handler.removeLimboPlayer(player);
      } catch (Exception var3) {
         this.logger.logException("Could not remove LimboPlayer for '" + player.getName() + "'", var3);
      }

   }

   public void reload(Settings settings) {
      LimboPersistenceType persistenceType = (LimboPersistenceType)settings.getProperty(LimboSettings.LIMBO_PERSISTENCE_TYPE);
      if (this.handler != null && this.handler.getType() != persistenceType) {
         this.logger.info("Limbo persistence type has changed! Note that the data is not converted.");
      }

      this.handler = (LimboPersistenceHandler)this.handlerFactory.newInstance(persistenceType.getImplementationClass());
   }
}
