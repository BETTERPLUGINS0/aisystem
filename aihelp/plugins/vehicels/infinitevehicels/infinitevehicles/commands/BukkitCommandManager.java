package me.PM2.infinitevehicles.commands;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.PM2.infinitevehicles.commands.apachecommonslang.ApacheCommonsExceptionUtil;
import me.PM2.infinitevehicles.commands.lib.timings.TimingManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.help.GenericCommandHelpTopic;
import org.bukkit.help.HelpTopic;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.ScoreboardManager;
import org.jetbrains.annotations.NotNull;

public class BukkitCommandManager extends CommandManager<CommandSender, BukkitCommandIssuer, ChatColor, BukkitMessageFormatter, BukkitCommandExecutionContext, BukkitConditionContext> {
   protected final Plugin plugin;
   private final CommandMap commandMap;
   /** @deprecated */
   @Deprecated
   private final TimingManager timingManager;
   protected ACFBukkitScheduler scheduler;
   private final Logger logger;
   public final Integer mcMinorVersion;
   public final Integer mcPatchVersion;
   protected Map<String, Command> knownCommands = new HashMap();
   protected Map<String, BukkitRootCommand> registeredCommands = new HashMap();
   protected BukkitCommandContexts contexts;
   protected BukkitCommandCompletions completions;
   protected BukkitLocales locales;
   protected Map<UUID, String> issuersLocaleString = new ConcurrentHashMap();
   private boolean cantReadLocale = false;
   protected boolean autoDetectFromClient = true;

   public BukkitCommandManager(Plugin plugin) {
      this.plugin = var1;

      try {
         this.scheduler = new ACFPaperScheduler(Bukkit.getAsyncScheduler());
      } catch (NoSuchMethodError var7) {
         this.scheduler = new ACFBukkitScheduler();
      }

      String var2 = this.plugin.getDescription().getPrefix();
      this.logger = Logger.getLogger(var2 != null ? var2 : this.plugin.getName());
      this.timingManager = TimingManager.of(var1);
      this.commandMap = this.hookCommandMap();
      this.formatters.put(MessageType.ERROR, (BukkitMessageFormatter)(this.defaultFormatter = new BukkitMessageFormatter(new ChatColor[]{ChatColor.RED, ChatColor.YELLOW, ChatColor.RED})));
      this.formatters.put(MessageType.SYNTAX, new BukkitMessageFormatter(new ChatColor[]{ChatColor.YELLOW, ChatColor.GREEN, ChatColor.WHITE}));
      this.formatters.put(MessageType.INFO, new BukkitMessageFormatter(new ChatColor[]{ChatColor.BLUE, ChatColor.DARK_GREEN, ChatColor.GREEN}));
      this.formatters.put(MessageType.HELP, new BukkitMessageFormatter(new ChatColor[]{ChatColor.AQUA, ChatColor.GREEN, ChatColor.YELLOW}));
      Pattern var3 = Pattern.compile("\\(MC: (\\d)\\.(\\d+)\\.?(\\d+?)?\\)");
      Matcher var4 = var3.matcher(Bukkit.getVersion());
      if (var4.find()) {
         this.mcMinorVersion = ACFUtil.parseInt(var4.toMatchResult().group(2), 0);
         this.mcPatchVersion = ACFUtil.parseInt(var4.toMatchResult().group(3), 0);
      } else {
         this.mcMinorVersion = -1;
         this.mcPatchVersion = -1;
      }

      Bukkit.getHelpMap().registerHelpTopicFactory(BukkitRootCommand.class, (var1x) -> {
         return (HelpTopic)(this.hasUnstableAPI("help") ? new ACFBukkitHelpTopic(this, (BukkitRootCommand)var1x) : new GenericCommandHelpTopic(var1x));
      });
      Bukkit.getPluginManager().registerEvents(new ACFBukkitListener(this, var1), var1);
      this.getLocales();

      try {
         Class.forName("org.bukkit.event.player.PlayerLocaleChangeEvent");
         Bukkit.getPluginManager().registerEvents(new ACFBukkitLocalesListener(this), var1);
      } catch (ClassNotFoundException var6) {
         this.scheduler.createLocaleTask(var1, () -> {
            if (!this.cantReadLocale && this.autoDetectFromClient) {
               Bukkit.getOnlinePlayers().forEach(this::readPlayerLocale);
            }
         }, 30L, 30L);
      }

      this.validNamePredicate = ACFBukkitUtil::isValidName;
      this.registerDependency(var1.getClass(), var1);
      this.registerDependency(Logger.class, var1.getLogger());
      this.registerDependency(FileConfiguration.class, var1.getConfig());
      this.registerDependency(FileConfiguration.class, "config", var1.getConfig());
      this.registerDependency(Plugin.class, var1);
      this.registerDependency(JavaPlugin.class, var1);
      this.registerDependency(PluginManager.class, Bukkit.getPluginManager());
      this.registerDependency(Server.class, Bukkit.getServer());
      this.scheduler.registerSchedulerDependencies(this);
      this.registerDependency(ScoreboardManager.class, Bukkit.getScoreboardManager());
      this.registerDependency(ItemFactory.class, Bukkit.getItemFactory());
      this.registerDependency(PluginDescriptionFile.class, var1.getDescription());
   }

   @NotNull
   private CommandMap hookCommandMap() {
      Object var1 = null;

      try {
         Server var2 = Bukkit.getServer();
         Method var3 = var2.getClass().getDeclaredMethod("getCommandMap");
         var3.setAccessible(true);
         var1 = (CommandMap)var3.invoke(var2);
         Field var4;
         if (!SimpleCommandMap.class.isAssignableFrom(((CommandMap)var1).getClass())) {
            this.log(LogLevel.ERROR, "ERROR: CommandMap has been hijacked! Offending command map is located at: " + ((CommandMap)var1).getClass().getName());
            this.log(LogLevel.ERROR, "We are going to try to hijack it back and resolve this, but you are now in dangerous territory.");
            this.log(LogLevel.ERROR, "We can not guarantee things are going to work.");
            var4 = var2.getClass().getDeclaredField("commandMap");
            var1 = new ProxyCommandMap(this, (CommandMap)var1);
            var4.set(var2, var1);
            this.log(LogLevel.INFO, "Injected Proxy Command Map... good luck...");
         }

         var4 = SimpleCommandMap.class.getDeclaredField("knownCommands");
         var4.setAccessible(true);
         this.knownCommands = (Map)var4.get(var1);
      } catch (Exception var5) {
         this.log(LogLevel.ERROR, "Failed to get Command Map. ACF will not function.");
         ACFUtil.sneaky(var5);
      }

      return (CommandMap)var1;
   }

   public Plugin getPlugin() {
      return this.plugin;
   }

   public boolean isCommandIssuer(Class<?> type) {
      return CommandSender.class.isAssignableFrom(var1);
   }

   public synchronized CommandContexts<BukkitCommandExecutionContext> getCommandContexts() {
      if (this.contexts == null) {
         this.contexts = new BukkitCommandContexts(this);
      }

      return this.contexts;
   }

   public synchronized CommandCompletions<BukkitCommandCompletionContext> getCommandCompletions() {
      if (this.completions == null) {
         this.completions = new BukkitCommandCompletions(this);
      }

      return this.completions;
   }

   public BukkitLocales getLocales() {
      if (this.locales == null) {
         this.locales = new BukkitLocales(this);
         this.locales.loadLanguages();
      }

      return this.locales;
   }

   public boolean hasRegisteredCommands() {
      return !this.registeredCommands.isEmpty();
   }

   public void registerCommand(BaseCommand command, boolean force) {
      String var3 = this.plugin.getName().toLowerCase(Locale.ENGLISH);
      var1.onRegister(this);
      Iterator var4 = var1.registeredCommands.entrySet().iterator();

      while(var4.hasNext()) {
         Entry var5 = (Entry)var4.next();
         String var6 = ((String)var5.getKey()).toLowerCase(Locale.ENGLISH);
         BukkitRootCommand var7 = (BukkitRootCommand)var5.getValue();
         if (!var7.isRegistered) {
            Command var8 = this.commandMap.getCommand(var6);
            if (var8 instanceof PluginIdentifiableCommand && ((PluginIdentifiableCommand)var8).getPlugin() == this.plugin) {
               this.knownCommands.remove(var6);
               var8.unregister(this.commandMap);
            } else if (var8 != null && var2) {
               this.knownCommands.remove(var6);
               Iterator var9 = this.knownCommands.entrySet().iterator();

               while(var9.hasNext()) {
                  Entry var10 = (Entry)var9.next();
                  String var11 = (String)var10.getKey();
                  Command var12 = (Command)var10.getValue();
                  if (var11.contains(":") && var8.equals(var12)) {
                     String[] var13 = ACFPatterns.COLON.split(var11, 2);
                     if (var13.length > 1) {
                        var8.unregister(this.commandMap);
                        var8.setLabel(var13[0] + ":" + var1.getName());
                        var8.register(this.commandMap);
                     }
                  }
               }
            }

            this.commandMap.register(var6, var3, var7);
         }

         var7.isRegistered = true;
         this.registeredCommands.put(var6, var7);
      }

   }

   public void registerCommand(BaseCommand command) {
      this.registerCommand(var1, false);
   }

   public void unregisterCommand(BaseCommand command) {
      Iterator var2 = var1.registeredCommands.values().iterator();

      while(var2.hasNext()) {
         RootCommand var3 = (RootCommand)var2.next();
         BukkitRootCommand var4 = (BukkitRootCommand)var3;
         var4.getSubCommands().values().removeAll(var1.subCommands.values());
         if (var4.isRegistered && var4.getSubCommands().isEmpty()) {
            this.unregisterCommand(var4);
            var4.isRegistered = false;
         }
      }

   }

   /** @deprecated */
   @Deprecated
   public void unregisterCommand(BukkitRootCommand command) {
      String var2 = this.plugin.getName().toLowerCase(Locale.ENGLISH);
      var1.unregister(this.commandMap);
      String var3 = var1.getName();
      Command var4 = (Command)this.knownCommands.get(var3);
      if (var1.equals(var4)) {
         this.knownCommands.remove(var3);
      }

      this.knownCommands.remove(var2 + ":" + var3);
      this.registeredCommands.remove(var3);
   }

   public void unregisterCommands() {
      Iterator var1 = (new HashSet(this.registeredCommands.keySet())).iterator();

      while(var1.hasNext()) {
         String var2 = (String)var1.next();
         this.unregisterCommand((BukkitRootCommand)this.registeredCommands.get(var2));
      }

   }

   private Field getEntityField(Player player) {
      for(Class var2 = var1.getClass(); var2 != Object.class; var2 = var2.getSuperclass()) {
         if (var2.getName().endsWith("CraftEntity")) {
            Field var3 = var2.getDeclaredField("entity");
            var3.setAccessible(true);
            return var3;
         }
      }

      return null;
   }

   public Locale setPlayerLocale(Player player, Locale locale) {
      this.issuersLocaleString.put(var1.getUniqueId(), var2.toString());
      return this.setIssuerLocale(var1, var2);
   }

   void readPlayerLocale(Player player) {
      if (var1.isOnline() && !this.cantReadLocale) {
         try {
            Locale var2 = null;

            try {
               var2 = var1.locale();
            } catch (NoSuchMethodError var10) {
               Object var4 = null;

               try {
                  var4 = var1.getLocale();
               } catch (NoSuchMethodError var9) {
                  Field var6 = this.getEntityField(var1);
                  if (var6 != null) {
                     Object var7 = var6.get(var1);
                     if (var7 != null) {
                        Field var8 = var7.getClass().getDeclaredField("locale");
                        var8.setAccessible(true);
                        var4 = var8.get(var7);
                     }
                  }
               }

               if (var4 instanceof String && !var4.equals(this.issuersLocaleString.get(var1.getUniqueId()))) {
                  var2 = ACFBukkitUtil.stringToLocale((String)var4);
               }
            }

            if (var2 != null) {
               UUID var3 = var1.getUniqueId();
               Locale var12 = (Locale)this.issuersLocale.put(var3, var2);
               this.issuersLocaleString.put(var3, var2.toString());
               if (!Objects.equals(var2, var12)) {
                  this.notifyLocaleChange(this.getCommandIssuer(var1), var12, var2);
               }
            }
         } catch (Exception var11) {
            this.cantReadLocale = true;
            this.scheduler.cancelLocaleTask();
            this.log(LogLevel.INFO, "Can't read players locale, you will be unable to automatically detect players language. Only Bukkit 1.7+ is supported for this.", var11);
         }

      }
   }

   /** @deprecated */
   @Deprecated
   public TimingManager getTimings() {
      return this.timingManager;
   }

   public ACFBukkitScheduler getScheduler() {
      return this.scheduler;
   }

   public RootCommand createRootCommand(String cmd) {
      return new BukkitRootCommand(this, var1);
   }

   public Collection<RootCommand> getRegisteredRootCommands() {
      return Collections.unmodifiableCollection(this.registeredCommands.values());
   }

   public BukkitCommandIssuer getCommandIssuer(Object issuer) {
      if (!(var1 instanceof CommandSender)) {
         throw new IllegalArgumentException(var1.getClass().getName() + " is not a Command Issuer.");
      } else {
         return new BukkitCommandIssuer(this, (CommandSender)var1);
      }
   }

   public BukkitCommandExecutionContext createCommandContext(RegisteredCommand command, CommandParameter parameter, CommandIssuer sender, List<String> args, int i, Map<String, Object> passedArgs) {
      return new BukkitCommandExecutionContext(var1, var2, (BukkitCommandIssuer)var3, var4, var5, var6);
   }

   public BukkitCommandCompletionContext createCompletionContext(RegisteredCommand command, CommandIssuer sender, String input, String config, String[] args) {
      return new BukkitCommandCompletionContext(var1, (BukkitCommandIssuer)var2, var3, var4, var5);
   }

   public RegisteredCommand createRegisteredCommand(BaseCommand command, String cmdName, Method method, String prefSubCommand) {
      return new BukkitRegisteredCommand(var1, var2, var3, var4);
   }

   public BukkitConditionContext createConditionContext(CommandIssuer issuer, String config) {
      return new BukkitConditionContext((BukkitCommandIssuer)var1, var2);
   }

   public void log(LogLevel level, String message, Throwable throwable) {
      Level var4 = var1 == LogLevel.INFO ? Level.INFO : Level.SEVERE;
      this.logger.log(var4, "[ACF] " + var2);
      if (var3 != null) {
         String[] var5 = ACFPatterns.NEWLINE.split(ApacheCommonsExceptionUtil.getFullStackTrace(var3));
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            String var8 = var5[var7];
            this.logger.log(var4, "[ACF] " + var8);
         }
      }

   }

   public boolean usePerIssuerLocale(boolean setting) {
      return this.usePerIssuerLocale(var1, var1);
   }

   public boolean usePerIssuerLocale(boolean usePerIssuerLocale, boolean autoDetectFromClient) {
      boolean var3 = this.usePerIssuerLocale;
      this.usePerIssuerLocale = var1;
      this.autoDetectFromClient = var2;
      return var3;
   }

   public String getCommandPrefix(CommandIssuer issuer) {
      return var1.isPlayer() ? "/" : "";
   }

   protected boolean handleUncaughtException(BaseCommand scope, RegisteredCommand registeredCommand, CommandIssuer sender, List<String> args, Throwable t) {
      if (var5 instanceof CommandException && var5.getCause() != null && var5.getMessage().startsWith("Unhandled exception")) {
         var5 = var5.getCause();
      }

      return super.handleUncaughtException(var1, var2, var3, var4, var5);
   }
}
