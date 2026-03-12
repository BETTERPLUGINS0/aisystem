package fr.xephi.authme.listener;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.listener.protocollib.ProtocolLibService;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.permission.PermissionsManager;
import fr.xephi.authme.service.PluginHookService;
import fr.xephi.authme.settings.SpawnLoader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

public class ServerListener implements Listener {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(ServerListener.class);
   @Inject
   private PluginHookService pluginHookService;
   @Inject
   private SpawnLoader spawnLoader;
   @Inject
   private ProtocolLibService protocolLibService;
   @Inject
   private PermissionsManager permissionsManager;

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void onPluginDisable(PluginDisableEvent event) {
      String pluginName = event.getPlugin().getName();
      this.permissionsManager.onPluginDisable(pluginName);
      if ("Essentials".equalsIgnoreCase(pluginName)) {
         this.pluginHookService.unhookEssentials();
         this.logger.info("Essentials has been disabled: unhooking");
      } else if ("CMI".equalsIgnoreCase(pluginName)) {
         this.pluginHookService.unhookCmi();
         this.spawnLoader.unloadCmiSpawn();
         this.logger.info("CMI has been disabled: unhooking");
      } else if ("Multiverse-Core".equalsIgnoreCase(pluginName)) {
         this.pluginHookService.unhookMultiverse();
         this.logger.info("Multiverse-Core has been disabled: unhooking");
      } else if ("EssentialsSpawn".equalsIgnoreCase(pluginName)) {
         this.spawnLoader.unloadEssentialsSpawn();
         this.logger.info("EssentialsSpawn has been disabled: unhooking");
      } else if ("ProtocolLib".equalsIgnoreCase(pluginName)) {
         this.protocolLibService.disable();
         this.logger.warning("ProtocolLib has been disabled, unhooking packet adapters!");
      } else if ("PlaceholderAPI".equalsIgnoreCase(pluginName)) {
         this.pluginHookService.unhookPlaceholderApi();
         this.logger.info("PlaceholderAPI has been disabled: unhooking placeholders");
      }

   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void onPluginEnable(PluginEnableEvent event) {
      String pluginName = event.getPlugin().getName();
      this.permissionsManager.onPluginEnable(pluginName);
      if ("Essentials".equalsIgnoreCase(pluginName)) {
         this.pluginHookService.tryHookToEssentials();
      } else if ("Multiverse-Core".equalsIgnoreCase(pluginName)) {
         this.pluginHookService.tryHookToMultiverse();
      } else if ("EssentialsSpawn".equalsIgnoreCase(pluginName)) {
         this.spawnLoader.loadEssentialsSpawn();
      } else if ("CMI".equalsIgnoreCase(pluginName)) {
         this.pluginHookService.tryHookToCmi();
         this.spawnLoader.loadCmiSpawn();
      } else if ("ProtocolLib".equalsIgnoreCase(pluginName)) {
         this.protocolLibService.setup();
      } else if ("PlaceholderAPI".equalsIgnoreCase(pluginName)) {
         this.pluginHookService.tryHookToPlaceholderApi();
      }

   }
}
