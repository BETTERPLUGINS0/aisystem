package com.lenis0012.bukkit.loginsecurity.modules.general;

import com.lenis0012.bukkit.loginsecurity.LoginSecurity;
import com.lenis0012.bukkit.loginsecurity.LoginSecurityConfig;
import com.lenis0012.bukkit.loginsecurity.commands.CommandAdmin;
import com.lenis0012.bukkit.loginsecurity.commands.CommandChangePass;
import com.lenis0012.bukkit.loginsecurity.commands.CommandLogin;
import com.lenis0012.bukkit.loginsecurity.commands.CommandLogout;
import com.lenis0012.bukkit.loginsecurity.commands.CommandRegister;
import com.lenis0012.bukkit.loginsecurity.commands.CommandUnregister;
import com.lenis0012.bukkit.loginsecurity.libs.bstats.bukkit.Metrics;
import com.lenis0012.bukkit.loginsecurity.libs.bstats.charts.SimplePie;
import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.modules.Module;
import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.updater.Updater;
import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.updater.UpdaterFactory;
import com.lenis0012.bukkit.loginsecurity.modules.language.LanguageModule;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GeneralModule extends Module<LoginSecurity> {
   private LocationMode locationMode;
   private Updater updater;

   public GeneralModule(LoginSecurity plugin) {
      super(plugin);
      this.locationMode = LocationMode.DEFAULT;
   }

   public void enable() {
      this.registerCommands();
      this.registerListeners();
      this.setupUpdater();
      this.setupMetrics();
      String locationMode = LoginSecurity.getConfiguration().getLocation();

      try {
         this.locationMode = LocationMode.valueOf(locationMode.toUpperCase());
      } catch (IllegalArgumentException var3) {
         this.logger().log(Level.WARNING, "Using unsupported location mode '{0}'. This will do noting.", locationMode);
      }

   }

   public void disable() {
   }

   public LocationMode getLocationMode() {
      return this.locationMode;
   }

   private void setupMetrics() {
      Metrics metrics = new Metrics(this.plugin, 4637);
      metrics.addCustomChart(new SimplePie("language", () -> {
         return ((LanguageModule)((LoginSecurity)this.plugin).getModule(LanguageModule.class)).getTranslation().getName();
      }));
   }

   private void setupUpdater() {
      LoginSecurityConfig config = LoginSecurity.getConfiguration();
      if (config.isUpdaterEnabled()) {
         this.updater = UpdaterFactory.provideBest(this.plugin, ((LoginSecurity)this.plugin).getInternalClassLoader()).getUpdater(this.plugin);
      } else {
         this.updater = null;
      }

   }

   private void registerCommands() {
      this.logger().log(Level.INFO, "Registering commands...");
      this.register(new CommandLogin((LoginSecurity)this.plugin), new String[]{"login"});
      this.register(new CommandRegister((LoginSecurity)this.plugin), new String[]{"register"});
      this.register(new CommandChangePass((LoginSecurity)this.plugin), new String[]{"changepassword"});
      this.register(new CommandLogout((LoginSecurity)this.plugin), new String[]{"logout"});
      this.register(new CommandUnregister((LoginSecurity)this.plugin), new String[]{"unregister"});
      this.register(new CommandAdmin((LoginSecurity)this.plugin), new String[]{"lac"});
   }

   private void registerListeners() {
      this.logger().log(Level.INFO, "Registering listeners...");
      this.register(new PlayerListener(this));
      if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
         InventoryPacketListener.register(this.plugin);
      }

   }

   public void checkUpdates(Player player) {
      if (this.updater != null) {
         this.updater.notifyIfUpdateAvailable(player);
      }

   }
}
