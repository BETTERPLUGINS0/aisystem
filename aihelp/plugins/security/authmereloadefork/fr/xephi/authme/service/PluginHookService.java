package fr.xephi.authme.service;

import com.earth2me.essentials.Essentials;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.libs.ch.jalu.injector.annotations.NoFieldScan;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.service.hook.papi.AuthMeExpansion;
import java.io.File;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

@NoFieldScan
public class PluginHookService {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(PluginHookService.class);
   private final PluginManager pluginManager;
   private Essentials essentials;
   private Plugin cmi;
   private MultiverseCore multiverse;
   private PlaceholderAPIPlugin placeholderApi;
   private AuthMeExpansion authMeExpansion;

   @Inject
   public PluginHookService(PluginManager pluginManager) {
      this.pluginManager = pluginManager;
      this.tryHookToEssentials();
      this.tryHookToCmi();
      this.tryHookToMultiverse();
      this.tryHookToPlaceholderApi();
   }

   public void setEssentialsSocialSpyStatus(Player player, boolean socialSpyStatus) {
      if (this.essentials != null) {
         this.essentials.getUser(player).setSocialSpyEnabled(socialSpyStatus);
      }

   }

   public File getEssentialsDataFolder() {
      return this.essentials != null ? this.essentials.getDataFolder() : null;
   }

   public File getCmiDataFolder() {
      Plugin plugin = this.pluginManager.getPlugin("CMI");
      return plugin == null ? null : plugin.getDataFolder();
   }

   public Location getMultiverseSpawn(World world) {
      if (this.multiverse != null) {
         MVWorldManager manager = this.multiverse.getMVWorldManager();
         if (manager.isMVWorld(world)) {
            return manager.getMVWorld(world).getSpawnLocation();
         }
      }

      return null;
   }

   public boolean isEssentialsAvailable() {
      return this.essentials != null;
   }

   public boolean isCmiAvailable() {
      return this.cmi != null;
   }

   public boolean isMultiverseAvailable() {
      return this.multiverse != null;
   }

   public void tryHookToEssentials() {
      try {
         this.essentials = (Essentials)this.getPlugin(this.pluginManager, "Essentials", Essentials.class);
      } catch (NoClassDefFoundError | Exception var2) {
         this.essentials = null;
      }

   }

   public void tryHookToPlaceholderApi() {
      try {
         this.placeholderApi = (PlaceholderAPIPlugin)this.getPlugin(this.pluginManager, "PlaceholderAPI", PlaceholderAPIPlugin.class);
         this.authMeExpansion = new AuthMeExpansion();
         this.authMeExpansion.register();
      } catch (NoClassDefFoundError | Exception var2) {
         this.placeholderApi = null;
         this.authMeExpansion = null;
      }

   }

   public void tryHookToCmi() {
      try {
         this.cmi = this.getPlugin(this.pluginManager, "CMI", Plugin.class);
      } catch (NoClassDefFoundError | Exception var2) {
         this.cmi = null;
      }

   }

   public void tryHookToMultiverse() {
      try {
         this.multiverse = (MultiverseCore)this.getPlugin(this.pluginManager, "Multiverse-Core", MultiverseCore.class);
      } catch (NoClassDefFoundError | Exception var2) {
         this.multiverse = null;
      }

   }

   public void unhookEssentials() {
      this.essentials = null;
   }

   public void unhookCmi() {
      this.cmi = null;
   }

   public void unhookMultiverse() {
      this.multiverse = null;
   }

   public void unhookPlaceholderApi() {
      if (this.placeholderApi != null) {
         this.authMeExpansion.unregister();
         this.placeholderApi = null;
      }

   }

   private <T extends Plugin> T getPlugin(PluginManager pluginManager, String name, Class<T> clazz) throws Exception, NoClassDefFoundError {
      if (pluginManager.isPluginEnabled(name)) {
         T plugin = (Plugin)clazz.cast(pluginManager.getPlugin(name));
         this.logger.info("Hooked successfully into " + name);
         return plugin;
      } else {
         return null;
      }
   }
}
