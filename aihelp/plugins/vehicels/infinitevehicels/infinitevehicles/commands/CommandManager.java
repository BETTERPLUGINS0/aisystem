package me.PM2.infinitevehicles.commands;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import me.PM2.infinitevehicles.commands.annotation.Dependency;
import me.PM2.infinitevehicles.commands.lib.util.Table;
import me.PM2.infinitevehicles.locales.MessageKeyProvider;
import org.jetbrains.annotations.NotNull;

public abstract class CommandManager<IT, I extends CommandIssuer, FT, MF extends MessageFormatter<FT>, CEC extends CommandExecutionContext<CEC, I>, CC extends ConditionContext<I>> {
   static ThreadLocal<Stack<CommandOperationContext>> commandOperationContext = ThreadLocal.withInitial(() -> {
      return new Stack<CommandOperationContext>() {
         public synchronized CommandOperationContext peek() {
            return super.size() == 0 ? null : (CommandOperationContext)super.peek();
         }
      };
   });
   protected Map<String, RootCommand> rootCommands = new HashMap();
   protected final CommandReplacements replacements = new CommandReplacements(this);
   protected final CommandConditions<I, CEC, CC> conditions = new CommandConditions(this);
   protected ExceptionHandler defaultExceptionHandler = null;
   boolean logUnhandledExceptions = true;
   protected Table<Class<?>, String, Object> dependencies = new Table();
   protected CommandHelpFormatter helpFormatter = new CommandHelpFormatter(this);
   protected boolean usePerIssuerLocale = false;
   protected List<IssuerLocaleChangedCallback<I>> localeChangedCallbacks = new ArrayList();
   protected Set<Locale> supportedLanguages;
   protected Predicate<String> validNamePredicate;
   protected Map<MessageType, MF> formatters;
   protected MF defaultFormatter;
   protected int defaultHelpPerPage;
   protected Map<UUID, Locale> issuersLocale;
   private Set<String> unstableAPIs;
   private Annotations annotations;
   private CommandRouter router;

   public CommandManager() {
      this.supportedLanguages = new HashSet(Arrays.asList(Locales.ENGLISH, Locales.DUTCH, Locales.GERMAN, Locales.SPANISH, Locales.FRENCH, Locales.CZECH, Locales.PORTUGUESE, Locales.SWEDISH, Locales.NORWEGIAN_BOKMAAL, Locales.NORWEGIAN_NYNORSK, Locales.RUSSIAN, Locales.BULGARIAN, Locales.HUNGARIAN, Locales.TURKISH, Locales.JAPANESE, Locales.CHINESE, Locales.SIMPLIFIED_CHINESE, Locales.TRADITIONAL_CHINESE, Locales.KOREAN, Locales.ITALIAN));
      this.validNamePredicate = (var0) -> {
         return true;
      };
      this.formatters = new IdentityHashMap();
      this.defaultHelpPerPage = 10;
      this.issuersLocale = new ConcurrentHashMap();
      this.unstableAPIs = new HashSet();
      this.annotations = new Annotations(this);
      this.router = new CommandRouter(this);
   }

   public static CommandOperationContext getCurrentCommandOperationContext() {
      return (CommandOperationContext)((Stack)commandOperationContext.get()).peek();
   }

   public static CommandIssuer getCurrentCommandIssuer() {
      CommandOperationContext var0 = (CommandOperationContext)((Stack)commandOperationContext.get()).peek();
      return var0 != null ? var0.getCommandIssuer() : null;
   }

   public static CommandManager getCurrentCommandManager() {
      CommandOperationContext var0 = (CommandOperationContext)((Stack)commandOperationContext.get()).peek();
      return var0 != null ? var0.getCommandManager() : null;
   }

   public MF setFormat(MessageType type, MF formatter) {
      return (MessageFormatter)this.formatters.put(var1, var2);
   }

   public MF getFormat(MessageType type) {
      return (MessageFormatter)this.formatters.getOrDefault(var1, this.defaultFormatter);
   }

   public void setFormat(MessageType type, FT... colors) {
      MessageFormatter var3 = this.getFormat(var1);

      for(int var4 = 1; var4 <= var2.length; ++var4) {
         var3.setColor(var4, var2[var4 - 1]);
      }

   }

   public void setFormat(MessageType type, int i, FT color) {
      MessageFormatter var4 = this.getFormat(var1);
      var4.setColor(var2, var3);
   }

   public MF getDefaultFormatter() {
      return this.defaultFormatter;
   }

   public void setDefaultFormatter(MF defaultFormatter) {
      this.defaultFormatter = var1;
   }

   public CommandConditions<I, CEC, CC> getCommandConditions() {
      return this.conditions;
   }

   public abstract CommandContexts<?> getCommandContexts();

   public abstract CommandCompletions<?> getCommandCompletions();

   /** @deprecated */
   @Deprecated
   @UnstableAPI
   public CommandHelp generateCommandHelp(@NotNull String command) {
      this.verifyUnstableAPI("help");
      CommandOperationContext var2 = getCurrentCommandOperationContext();
      if (var2 == null) {
         throw new IllegalStateException("This method can only be called as part of a command execution.");
      } else {
         return this.generateCommandHelp(var2.getCommandIssuer(), var1);
      }
   }

   /** @deprecated */
   @Deprecated
   @UnstableAPI
   public CommandHelp generateCommandHelp(CommandIssuer issuer, @NotNull String command) {
      this.verifyUnstableAPI("help");
      return this.generateCommandHelp(var1, this.obtainRootCommand(var2));
   }

   /** @deprecated */
   @Deprecated
   @UnstableAPI
   public CommandHelp generateCommandHelp() {
      this.verifyUnstableAPI("help");
      CommandOperationContext var1 = getCurrentCommandOperationContext();
      if (var1 == null) {
         throw new IllegalStateException("This method can only be called as part of a command execution.");
      } else {
         String var2 = var1.getCommandLabel();
         return this.generateCommandHelp(var1.getCommandIssuer(), this.obtainRootCommand(var2));
      }
   }

   /** @deprecated */
   @Deprecated
   @UnstableAPI
   public CommandHelp generateCommandHelp(CommandIssuer issuer, RootCommand rootCommand) {
      this.verifyUnstableAPI("help");
      return new CommandHelp(this, var2, var1);
   }

   /** @deprecated */
   @Deprecated
   @UnstableAPI
   public int getDefaultHelpPerPage() {
      this.verifyUnstableAPI("help");
      return this.defaultHelpPerPage;
   }

   /** @deprecated */
   @Deprecated
   @UnstableAPI
   public void setDefaultHelpPerPage(int defaultHelpPerPage) {
      this.verifyUnstableAPI("help");
      this.defaultHelpPerPage = var1;
   }

   /** @deprecated */
   @Deprecated
   @UnstableAPI
   public void setHelpFormatter(CommandHelpFormatter helpFormatter) {
      this.helpFormatter = var1;
   }

   /** @deprecated */
   @Deprecated
   @UnstableAPI
   public CommandHelpFormatter getHelpFormatter() {
      return this.helpFormatter;
   }

   CommandRouter getRouter() {
      return this.router;
   }

   public abstract void registerCommand(BaseCommand command);

   public abstract boolean hasRegisteredCommands();

   public abstract boolean isCommandIssuer(Class<?> type);

   public abstract I getCommandIssuer(Object issuer);

   public abstract RootCommand createRootCommand(String cmd);

   public abstract Locales getLocales();

   public boolean usingPerIssuerLocale() {
      return this.usePerIssuerLocale;
   }

   public boolean usePerIssuerLocale(boolean setting) {
      boolean var2 = this.usePerIssuerLocale;
      this.usePerIssuerLocale = var1;
      return var2;
   }

   public boolean isValidName(@NotNull String name) {
      return this.validNamePredicate.test(var1);
   }

   @NotNull
   public Predicate<String> getValidNamePredicate() {
      return this.validNamePredicate;
   }

   public void setValidNamePredicate(@NotNull Predicate<String> isValidName) {
      this.validNamePredicate = var1;
   }

   public ConditionContext createConditionContext(CommandIssuer issuer, String config) {
      return new ConditionContext(var1, var2);
   }

   public abstract CommandExecutionContext createCommandContext(RegisteredCommand command, CommandParameter parameter, CommandIssuer sender, List<String> args, int i, Map<String, Object> passedArgs);

   public abstract CommandCompletionContext createCompletionContext(RegisteredCommand command, CommandIssuer sender, String input, String config, String[] args);

   public abstract void log(final LogLevel level, final String message, final Throwable throwable);

   public void log(final LogLevel level, final String message) {
      this.log(var1, var2, (Throwable)null);
   }

   public CommandReplacements getCommandReplacements() {
      return this.replacements;
   }

   public boolean hasPermission(CommandIssuer issuer, Set<String> permissions) {
      Iterator var3 = var2.iterator();

      String var4;
      do {
         if (!var3.hasNext()) {
            return true;
         }

         var4 = (String)var3.next();
      } while(this.hasPermission(var1, var4));

      return false;
   }

   public boolean hasPermission(CommandIssuer issuer, String permission) {
      if (var2 != null && !var2.isEmpty()) {
         String[] var3 = ACFPatterns.PIPE.split(var2);
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            if (!var6.isEmpty()) {
               boolean var7 = false;
               String[] var8 = ACFPatterns.COMMA.split(var6);
               int var9 = var8.length;

               for(int var10 = 0; var10 < var9; ++var10) {
                  String var11 = var8[var10];
                  if (!var11.isEmpty()) {
                     var7 = var1.hasPermission(var11);
                     if (!var7) {
                        break;
                     }
                  }
               }

               if (var7) {
                  return true;
               }
            }
         }

         return false;
      } else {
         return true;
      }
   }

   public synchronized RootCommand getRootCommand(@NotNull String cmd) {
      return (RootCommand)this.rootCommands.get(ACFPatterns.SPACE.split(var1.toLowerCase(Locale.ENGLISH), 2)[0]);
   }

   public synchronized RootCommand obtainRootCommand(@NotNull String cmd) {
      return (RootCommand)this.rootCommands.computeIfAbsent(ACFPatterns.SPACE.split(var1.toLowerCase(Locale.ENGLISH), 2)[0], this::createRootCommand);
   }

   public abstract Collection<RootCommand> getRegisteredRootCommands();

   public RegisteredCommand createRegisteredCommand(BaseCommand command, String cmdName, Method method, String prefSubCommand) {
      return new RegisteredCommand(var1, var2, var3, var4);
   }

   public void setDefaultExceptionHandler(ExceptionHandler exceptionHandler) {
      if (var1 == null && !this.logUnhandledExceptions) {
         throw new IllegalArgumentException("You may not disable the default exception handler and have logging of unhandled exceptions disabled");
      } else {
         this.defaultExceptionHandler = var1;
      }
   }

   public void setDefaultExceptionHandler(ExceptionHandler exceptionHandler, boolean logExceptions) {
      if (var1 == null && !var2) {
         throw new IllegalArgumentException("You may not disable the default exception handler and have logging of unhandled exceptions disabled");
      } else {
         this.logUnhandledExceptions = var2;
         this.defaultExceptionHandler = var1;
      }
   }

   public boolean isLoggingUnhandledExceptions() {
      return this.logUnhandledExceptions;
   }

   public ExceptionHandler getDefaultExceptionHandler() {
      return this.defaultExceptionHandler;
   }

   protected boolean handleUncaughtException(BaseCommand scope, RegisteredCommand registeredCommand, CommandIssuer sender, List<String> args, Throwable t) {
      if (var5 instanceof InvocationTargetException && var5.getCause() != null) {
         var5 = var5.getCause();
      }

      boolean var6 = false;
      if (var1.getExceptionHandler() != null) {
         var6 = var1.getExceptionHandler().execute(var1, var2, var3, var4, var5);
      } else if (this.defaultExceptionHandler != null) {
         var6 = this.defaultExceptionHandler.execute(var1, var2, var3, var4, var5);
      }

      return var6;
   }

   public void sendMessage(IT issuerArg, MessageType type, MessageKeyProvider key, String... replacements) {
      this.sendMessage(this.getCommandIssuer(var1), var2, var3, var4);
   }

   public void sendMessage(CommandIssuer issuer, MessageType type, MessageKeyProvider key, String... replacements) {
      String var5 = this.formatMessage(var1, var2, var3, var4);
      String[] var6 = ACFPatterns.NEWLINE.split(var5);
      int var7 = var6.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         String var9 = var6[var8];
         var1.sendMessageInternal(ACFUtil.rtrim(var9));
      }

   }

   public String formatMessage(CommandIssuer issuer, MessageType type, MessageKeyProvider key, String... replacements) {
      String var5 = this.getLocales().getMessage(var1, var3.getMessageKey());
      if (var4.length > 0) {
         var5 = ACFUtil.replaceStrings(var5, var4);
      }

      var5 = this.getCommandReplacements().replace(var5);
      var5 = this.getLocales().replaceI18NStrings(var5);
      MessageFormatter var6 = (MessageFormatter)this.formatters.getOrDefault(var2, this.defaultFormatter);
      if (var6 != null) {
         var5 = var6.format(var5);
      }

      return var5;
   }

   public void onLocaleChange(IssuerLocaleChangedCallback<I> onChange) {
      this.localeChangedCallbacks.add(var1);
   }

   public void notifyLocaleChange(I issuer, Locale oldLocale, Locale newLocale) {
      this.localeChangedCallbacks.forEach((var4) -> {
         try {
            var4.onIssuerLocaleChange(var1, var2, var3);
         } catch (Exception var6) {
            this.log(LogLevel.ERROR, "Error in notifyLocaleChange", var6);
         }

      });
   }

   public Locale setIssuerLocale(IT issuer, Locale locale) {
      CommandIssuer var3 = this.getCommandIssuer(var1);
      Locale var4 = (Locale)this.issuersLocale.put(var3.getUniqueId(), var2);
      if (!Objects.equals(var4, var2)) {
         this.notifyLocaleChange(var3, var4, var2);
      }

      return var4;
   }

   public Locale getIssuerLocale(CommandIssuer issuer) {
      if (this.usingPerIssuerLocale() && var1 != null) {
         Locale var2 = (Locale)this.issuersLocale.get(var1.getUniqueId());
         if (var2 != null) {
            return var2;
         }
      }

      return this.getLocales().getDefaultLocale();
   }

   CommandOperationContext<I> createCommandOperationContext(BaseCommand command, CommandIssuer issuer, String commandLabel, String[] args, boolean isAsync) {
      return new CommandOperationContext(this, var2, var1, var3, var4, var5);
   }

   public Set<Locale> getSupportedLanguages() {
      return this.supportedLanguages;
   }

   public void addSupportedLanguage(Locale locale) {
      this.supportedLanguages.add(var1);
      this.getLocales().loadMissingBundles();
   }

   public <T> void registerDependency(Class<? extends T> clazz, T instance) {
      this.registerDependency(var1, var1.getName(), var2);
   }

   public <T> void registerDependency(Class<? extends T> clazz, String key, T instance) {
      if (this.dependencies.containsKey(var1, var2)) {
         throw new IllegalStateException("There is already an instance of " + var1.getName() + " with the key " + var2 + " registered!");
      } else {
         this.dependencies.put(var1, var2, var3);
      }
   }

   public <T> void unregisterDependency(Class<? extends T> clazz) {
      this.unregisterDependency(var1, var1.getName());
   }

   public <T> void unregisterDependency(Class<? extends T> clazz, String key) {
      if (!this.dependencies.containsKey(var1, var2)) {
         throw new IllegalStateException("Unable to unregister a dependency of " + var1.getName() + " with the key " + var2 + " because it wasn't registered");
      } else {
         this.dependencies.remove(var1, var2);
      }
   }

   void injectDependencies(BaseCommand baseCommand) {
      Class var2 = var1.getClass();

      do {
         Field[] var3 = var2.getDeclaredFields();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Field var6 = var3[var5];
            if (this.annotations.hasAnnotation(var6, Dependency.class)) {
               String var7 = this.annotations.getAnnotationValue(var6, Dependency.class);
               String var8 = var7.isEmpty() ? var6.getType().getName() : var7;
               Object var9 = this.dependencies.row(var6.getType()).get(var8);
               if (var9 == null) {
                  throw new UnresolvedDependencyException("Could not find a registered instance of " + var6.getType().getName() + " with key " + var8 + " for field " + var6.getName() + " in class " + var1.getClass().getName());
               }

               try {
                  boolean var10 = var6.isAccessible();
                  if (!var10) {
                     var6.setAccessible(true);
                  }

                  var6.set(var1, var9);
                  var6.setAccessible(var10);
               } catch (IllegalAccessException var11) {
                  var11.printStackTrace();
               }
            }
         }

         var2 = var2.getSuperclass();
      } while(!var2.equals(BaseCommand.class));

   }

   /** @deprecated */
   @Deprecated
   public void enableUnstableAPI(String api) {
      this.unstableAPIs.add(var1);
   }

   void verifyUnstableAPI(String api) {
      if (!this.unstableAPIs.contains(var1)) {
         throw new IllegalStateException("Using an unstable API that has not been enabled ( " + var1 + "). See https://acfunstable.emc.gs");
      }
   }

   boolean hasUnstableAPI(String api) {
      return this.unstableAPIs.contains(var1);
   }

   Annotations getAnnotations() {
      return this.annotations;
   }

   public String getCommandPrefix(CommandIssuer issuer) {
      return "";
   }
}
