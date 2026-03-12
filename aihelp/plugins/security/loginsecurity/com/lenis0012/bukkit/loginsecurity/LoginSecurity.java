package com.lenis0012.bukkit.loginsecurity;

import com.lenis0012.bukkit.loginsecurity.database.LoginSecurityDatabase;
import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.config.ConfigurationModule;
import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.modules.ModularPlugin;
import com.lenis0012.bukkit.loginsecurity.modules.captcha.CaptchaManager;
import com.lenis0012.bukkit.loginsecurity.modules.general.GeneralModule;
import com.lenis0012.bukkit.loginsecurity.modules.language.LanguageKeys;
import com.lenis0012.bukkit.loginsecurity.modules.language.LanguageModule;
import com.lenis0012.bukkit.loginsecurity.modules.language.TranslatedMessage;
import com.lenis0012.bukkit.loginsecurity.modules.storage.NewStorageModule;
import com.lenis0012.bukkit.loginsecurity.modules.threading.ThreadingModule;
import com.lenis0012.bukkit.loginsecurity.session.SessionManager;
import com.lenis0012.bukkit.loginsecurity.util.LoggingFilter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public class LoginSecurity extends ModularPlugin {
   private static final ExecutorService executorService = Executors.newCachedThreadPool();
   private LoginSecurityConfig config;
   private SessionManager sessionManager;

   public static ExecutorService getExecutorService() {
      return executorService;
   }

   public static SessionManager getSessionManager() {
      return ((LoginSecurity)getInstance()).sessionManager;
   }

   public static LoginSecurityConfig getConfiguration() {
      return ((LoginSecurity)getInstance()).config();
   }

   public static LoginSecurityDatabase getDatastore() {
      return ((LoginSecurity)getInstance()).datastore();
   }

   public static TranslatedMessage translate(LanguageKeys key) {
      return ((LanguageModule)getInstance().getModule(LanguageModule.class)).translate(key);
   }

   public static TranslatedMessage translate(String key) {
      return ((LanguageModule)getInstance().getModule(LanguageModule.class)).translate(key);
   }

   public LoginSecurity() {
      super(ConfigurationModule.class);
   }

   public void enable() {
      ConfigurationModule module = (ConfigurationModule)this.getModule(ConfigurationModule.class);
      this.config = (LoginSecurityConfig)module.createCustomConfig(LoginSecurityConfig.class);
      this.config.reload();
      this.config.save();
      this.sessionManager = new SessionManager();
      Logger consoleLogger = (Logger)LogManager.getRootLogger();
      consoleLogger.addFilter(new LoggingFilter(this.config));
      this.registry.registerModules(LanguageModule.class, NewStorageModule.class, GeneralModule.class, ThreadingModule.class, CaptchaManager.class);
   }

   public void disable() {
      this.getLogger().log(Level.INFO, "Waiting for queued tasks...");
      executorService.shutdown();
      this.getLogger().log(Level.INFO, "ExecutorService shut down, ready to disable.");
   }

   public LoginSecurityConfig config() {
      return this.config;
   }

   public LoginSecurityDatabase datastore() {
      return ((NewStorageModule)this.getModule(NewStorageModule.class)).getDatabase();
   }

   public ClassLoader getInternalClassLoader() {
      return this.getClassLoader();
   }
}
