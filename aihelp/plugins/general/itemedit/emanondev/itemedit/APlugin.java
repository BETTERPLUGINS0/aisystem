package emanondev.itemedit;

import emanondev.itemedit.command.AbstractCommand;
import emanondev.itemedit.compability.Metrics;
import emanondev.itemedit.plugin.PluginAdditionalInfo;
import emanondev.itemedit.utility.ReflectionUtils;
import emanondev.itemedit.utility.VersionUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class APlugin extends JavaPlugin {
   private final Map<String, YMLConfig> configs = VersionUtils.hasFoliaAPI() ? new ConcurrentHashMap() : new HashMap();
   private final Map<String, YMLConfig> languageConfigs = VersionUtils.hasFoliaAPI() ? new ConcurrentHashMap() : new HashMap();
   private final PluginAdditionalInfo pluginAdditionalInfo = new PluginAdditionalInfo(this);
   private boolean useMultiLanguage;
   private String defaultLanguage;
   private CooldownAPI cooldownApi = null;
   private Metrics bstatsMetrics;

   protected APlugin() {
   }

   @NotNull
   public YMLConfig getConfig() {
      return this.getConfig("config.yml");
   }

   @NotNull
   public YMLConfig getConfig(@NotNull String fileName) {
      fileName = YMLConfig.fixName(fileName);
      if (this.configs.containsKey(fileName)) {
         return (YMLConfig)this.configs.get(fileName);
      } else {
         YMLConfig conf = new YMLConfig(this, fileName);
         this.configs.put(fileName, conf);
         return conf;
      }
   }

   public void log(@NotNull String log) {
      Bukkit.getConsoleSender().sendMessage(UtilsString.fix((String)(ChatColor.DARK_BLUE + "[" + ChatColor.WHITE + this.getName() + ChatColor.DARK_BLUE + "] " + ChatColor.WHITE + log), (Player)null, true));
   }

   public void log(@NotNull ChatColor color, @NotNull String prefix, @NotNull String log) {
      this.log(color + prefix + " " + ChatColor.WHITE + log);
   }

   public void registerListener(@NotNull Listener listener) {
      this.getServer().getPluginManager().registerEvents(listener, this);
   }

   public void registerCommand(@NotNull AbstractCommand executor, @Nullable List<String> aliases) {
      this.registerCommand(executor.getName(), executor, aliases);
   }

   public void registerCommand(@NotNull String commandName, @NotNull TabExecutor executor, @Nullable List<String> aliases) {
      PluginCommand command = this.getCommand(commandName);
      if (command == null) {
         this.log("&cUnable to register Command &e" + commandName);
      } else {
         command.setExecutor(executor);
         command.setTabCompleter(executor);
         if (aliases != null) {
            command.setAliases(aliases);
         }

      }
   }

   @NotNull
   public YMLConfig getLanguageConfig(@Nullable CommandSender sender) {
      String locale = this.getLocale(sender);
      if (this.languageConfigs.containsKey(locale)) {
         return (YMLConfig)this.languageConfigs.get(locale);
      } else {
         String fileName = "languages" + File.separator + locale + ".yml";
         YMLConfig conf;
         if (!locale.equals(this.defaultLanguage) && !(new File(this.getDataFolder(), fileName)).exists() && this.getResource("languages/" + locale + ".yml") == null) {
            conf = this.getLanguageConfig((CommandSender)null);
            this.languageConfigs.put(locale, conf);
            return conf;
         } else {
            conf = new YMLConfig(this, fileName);
            this.languageConfigs.put(locale, conf);
            return conf;
         }
      }
   }

   public abstract void enable();

   public abstract void reload();

   public abstract void disable();

   protected void updateConfigurations(int oldConfigVersion) {
   }

   protected boolean addLanguagesMetrics() {
      return false;
   }

   @NotNull
   protected Predicate<Player> languagesMetricsIsAdmin() {
      return ServerOperator::isOp;
   }

   @NotNull
   protected Predicate<Player> languagesMetricsIsUser() {
      return (player) -> {
         return true;
      };
   }

   protected void reloadConfigs() {
      boolean check = false;
      Iterator var2 = this.configs.values().iterator();

      while(var2.hasNext()) {
         YMLConfig conf = (YMLConfig)var2.next();

         try {
            if (conf.getFile().exists()) {
               conf.reload();
            } else {
               check = true;
            }
         } catch (Exception var5) {
            var5.printStackTrace();
         }
      }

      if (check) {
         ArrayList<String> toRemove = new ArrayList();
         this.configs.forEach((k, v) -> {
            try {
               if (!v.getFile().exists()) {
                  toRemove.add(k);
               }
            } catch (Exception var4) {
            }

         });
         Iterator var7 = toRemove.iterator();

         while(var7.hasNext()) {
            String key = (String)var7.next();
            this.configs.remove(key);
         }
      }

      this.languageConfigs.clear();
      this.getLanguageConfig((CommandSender)null);
   }

   @NotNull
   public CooldownAPI getCooldownAPI() {
      if (this.cooldownApi == null) {
         this.cooldownApi = new CooldownAPI(this);
      }

      return this.cooldownApi;
   }

   /** @deprecated */
   @Deprecated
   public void onEnable() {
      try {
         long now = System.currentTimeMillis();
         if (!ReflectionUtils.isClassPresent("org.spigotmc.SpigotConfig")) {
            this.enableWithError("CraftBukkit is not supported!!! use Spigot or Paper");
            this.log(ChatColor.GREEN, "#", "Enabled (took &e" + (System.currentTimeMillis() - now) + "&f ms)");
            return;
         }

         if (!VersionUtils.isVersionAfter(1, 8)) {
            this.enableWithError("1.7.x is not supported!!! use 1.8+");
            this.log(ChatColor.GREEN, "#", "Enabled (took &e" + (System.currentTimeMillis() - now) + "&f ms)");
            return;
         }

         this.initLanguages();
         if (this.getPluginAdditionalInfo().getSpigotResourceId() != null && this.getConfig().getBoolean("check-updates", true)) {
            (new UpdateChecker(this)).logUpdates();
         }

         this.initConfigUpdater();
         this.initMetrics();
         this.enable();
         this.log(ChatColor.GREEN, "#", "Enabled (took &e" + (System.currentTimeMillis() - now) + "&f ms)");
      } catch (Throwable var3) {
         this.log(ChatColor.RED + "Error while loading " + this.getName() + ", disabling it");
         var3.printStackTrace();
         Bukkit.getServer().getPluginManager().disablePlugin(this);
      }

   }

   public final void onReload() {
      long now = System.currentTimeMillis();
      this.useMultiLanguage = this.getConfig().getBoolean("language.use_multilanguage", true);
      this.defaultLanguage = this.getConfig().getString("language.default_language", "en");
      this.reloadConfigs();
      this.reload();
      this.log(ChatColor.GREEN, "#", "Reloaded (took &e" + (System.currentTimeMillis() - now) + "&f ms)");
   }

   public void onDisable() {
      this.disable();
   }

   protected final void enableWithError(@NotNull String error) {
      APlugin.TabExecutorError exec = new APlugin.TabExecutorError(ChatColor.RED + error);
      Iterator var3 = this.getDescription().getCommands().keySet().iterator();

      while(var3.hasNext()) {
         String command = (String)var3.next();
         this.registerCommand(command, exec, (List)null);
      }

      this.log(ChatColor.RED + error);
   }

   @NotNull
   private String getLocale(@Nullable CommandSender sender) {
      String locale;
      if (!(sender instanceof Player)) {
         locale = this.defaultLanguage;
      } else if (VersionUtils.isVersionAfter(1, 12) && this.useMultiLanguage) {
         locale = ((Player)sender).getLocale().equals("zh_tw") ? ((Player)sender).getLocale() : ((Player)sender).getLocale().split("_")[0];
      } else {
         locale = this.defaultLanguage;
      }

      return locale;
   }

   private void initMetrics() {
      Integer pluginId = this.getPluginAdditionalInfo().getBstatsPluginId();
      if (pluginId == null) {
         this.bstatsMetrics = null;
      } else {
         try {
            this.bstatsMetrics = new Metrics(this, pluginId);
            if (this.addLanguagesMetrics()) {
               Predicate<Player> isAdmin = this.languagesMetricsIsAdmin();
               Predicate<Player> isUser = this.languagesMetricsIsUser();
               if (!VersionUtils.isVersionAfter(1, 12)) {
                  return;
               }

               this.bstatsMetrics.addCustomChart(new Metrics.DrilldownPie("admins_languages", () -> {
                  Map<String, Map<String, Integer>> mainMap = new HashMap();
                  Iterator var2 = Bukkit.getOnlinePlayers().iterator();

                  while(var2.hasNext()) {
                     Player player = (Player)var2.next();
                     if (isAdmin.test(player)) {
                        String locale = player.getLocale().toLowerCase(Locale.ENGLISH);
                        String pre = locale.split("_")[0];
                        if (!mainMap.containsKey(pre)) {
                           mainMap.put(pre, new HashMap());
                        }

                        Map<String, Integer> subMap = (Map)mainMap.get(pre);
                        subMap.put(locale, (Integer)subMap.getOrDefault(locale, 0) + 1);
                     }
                  }

                  return mainMap;
               }));
               this.bstatsMetrics.addCustomChart(new Metrics.DrilldownPie("users_languages", () -> {
                  Map<String, Map<String, Integer>> mainMap = new HashMap();
                  Iterator var2 = Bukkit.getOnlinePlayers().iterator();

                  while(var2.hasNext()) {
                     Player player = (Player)var2.next();
                     if (isUser.test(player)) {
                        String locale = player.getLocale().toLowerCase(Locale.ENGLISH);
                        String pre = locale.split("_")[0];
                        if (!mainMap.containsKey(pre)) {
                           mainMap.put(pre, new HashMap());
                        }

                        Map<String, Integer> subMap = (Map)mainMap.get(pre);
                        subMap.put(locale, (Integer)subMap.getOrDefault(locale, 0) + 1);
                     }
                  }

                  return mainMap;
               }));
            }
         } catch (Throwable var4) {
            var4.printStackTrace();
         }

         this.bstatsMetrics = null;
      }
   }

   private void initConfigUpdater() {
      ConfigurationSection def = this.getConfig().getDefaultSection();
      int currentVersion = def == null ? 1 : def.getInt("config-version", 1);
      int oldVersion = this.getConfig().getInt("config-version", 1);
      if (oldVersion < currentVersion) {
         this.log("Updating configuration version (" + oldVersion + " -> " + currentVersion + ")");
         this.updateConfigurations(oldVersion);
         this.getConfig().set("config-version", currentVersion);
         this.log("Updated configuration version (" + oldVersion + " -> " + currentVersion + ")");
         this.getConfig().save();
      }
   }

   private void initLanguages() {
      this.useMultiLanguage = this.getConfig().getBoolean("language.use_multilanguage", true);
      this.defaultLanguage = this.getConfig().getString("language.default", "en");
      if (this.getConfig().getBoolean("language.regen_files", true)) {
         YMLConfig version = this.getConfig("version.yml");
         if (!this.getDescription().getVersion().equals(version.loadMessage("previous_version", "1"))) {
            version.set("previous_version", this.getDescription().getVersion());
            version.save();
            File langFolder = new File(this.getDataFolder(), "languages");
            if (langFolder.exists()) {
               File[] list = langFolder.listFiles();
               if (list != null) {
                  File[] var4 = list;
                  int var5 = list.length;

                  for(int var6 = 0; var6 < var5; ++var6) {
                     File file = var4[var6];
                     if (this.getResource("languages/" + file.getName()) != null) {
                        this.saveResource("languages/" + file.getName(), true);
                     }
                  }
               }
            }
         }
      }

      this.getLanguageConfig((CommandSender)null);
   }

   public PluginAdditionalInfo getPluginAdditionalInfo() {
      return this.pluginAdditionalInfo;
   }

   public Metrics getBstatsMetrics() {
      return this.bstatsMetrics;
   }

   protected final class TabExecutorError implements TabExecutor {
      private final String msg;

      public TabExecutorError(@NotNull String param2) {
         this.msg = msg;

         for(int i = 0; i < 20; ++i) {
            APlugin.this.log(msg);
         }

      }

      public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
         return Collections.emptyList();
      }

      public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
         sender.sendMessage(this.msg);
         return true;
      }
   }
}
