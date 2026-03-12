package fr.xephi.authme.initialization;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.datasource.DataSourceType;
import fr.xephi.authme.libs.com.github.Anon8281.universalScheduler.UniversalRunnable;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.libs.org.bstats.bukkit.Metrics;
import fr.xephi.authme.libs.org.bstats.charts.SimplePie;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.message.Messages;
import fr.xephi.authme.output.ConsoleFilter;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.output.Log4JFilter;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.DatabaseSettings;
import fr.xephi.authme.settings.properties.EmailSettings;
import fr.xephi.authme.settings.properties.PluginSettings;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class OnStartupTasks {
   private static ConsoleLogger consoleLogger = ConsoleLoggerFactory.get(OnStartupTasks.class);
   @Inject
   private DataSource dataSource;
   @Inject
   private Settings settings;
   @Inject
   private BukkitService bukkitService;
   @Inject
   private Messages messages;

   OnStartupTasks() {
   }

   public static void sendMetrics(AuthMe plugin, Settings settings) {
      Metrics metrics = new Metrics(plugin, 18479);
      metrics.addCustomChart(new SimplePie("messages_language", () -> {
         return (String)settings.getProperty(PluginSettings.MESSAGES_LANGUAGE);
      }));
      metrics.addCustomChart(new SimplePie("database_backend", () -> {
         return ((DataSourceType)settings.getProperty(DatabaseSettings.BACKEND)).toString();
      }));
   }

   public static void setupConsoleFilter(Logger logger) {
      try {
         Class.forName("org.apache.logging.log4j.core.filter.AbstractFilter");
         setLog4JFilter();
      } catch (NoClassDefFoundError | ClassNotFoundException var3) {
         consoleLogger.info("You're using Minecraft 1.6.x or older, Log4J support will be disabled");
         ConsoleFilter filter = new ConsoleFilter();
         logger.setFilter(filter);
         Bukkit.getLogger().setFilter(filter);
         Logger.getLogger("Minecraft").setFilter(filter);
      }

   }

   private static void setLog4JFilter() {
      org.apache.logging.log4j.core.Logger logger = (org.apache.logging.log4j.core.Logger)LogManager.getRootLogger();
      logger.addFilter(new Log4JFilter());
   }

   public void scheduleRecallEmailTask() {
      if ((Boolean)this.settings.getProperty(EmailSettings.RECALL_PLAYERS)) {
         this.bukkitService.runTaskTimerAsynchronously(new UniversalRunnable() {
            public void run() {
               List<String> loggedPlayersWithEmptyMail = OnStartupTasks.this.dataSource.getLoggedPlayersWithEmptyMail();
               OnStartupTasks.this.bukkitService.runTask(() -> {
                  Iterator var2 = loggedPlayersWithEmptyMail.iterator();

                  while(var2.hasNext()) {
                     String playerWithoutMail = (String)var2.next();
                     Player player = OnStartupTasks.this.bukkitService.getPlayerExact(playerWithoutMail);
                     if (player != null) {
                        OnStartupTasks.this.messages.send(player, MessageKey.ADD_EMAIL_MESSAGE);
                     }
                  }

               });
            }
         }, 1L, 1200L * (long)(Integer)this.settings.getProperty(EmailSettings.DELAY_RECALL));
      }
   }
}
