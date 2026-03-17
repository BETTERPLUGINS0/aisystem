package me.PM2.infinitevehicles.commands;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.PM2.infinitevehicles.commands.annotation.CatchAll;
import me.PM2.infinitevehicles.commands.annotation.CatchUnknown;
import me.PM2.infinitevehicles.commands.annotation.CommandAlias;
import me.PM2.infinitevehicles.commands.annotation.CommandPermission;
import me.PM2.infinitevehicles.commands.annotation.Conditions;
import me.PM2.infinitevehicles.commands.annotation.Default;
import me.PM2.infinitevehicles.commands.annotation.Description;
import me.PM2.infinitevehicles.commands.annotation.HelpCommand;
import me.PM2.infinitevehicles.commands.annotation.PreCommand;
import me.PM2.infinitevehicles.commands.annotation.Subcommand;
import me.PM2.infinitevehicles.commands.annotation.UnknownHandler;
import me.PM2.infinitevehicles.commands.apachecommonslang.ApacheCommonsLangUtil;
import me.PM2.infinitevehicles.locales.MessageKeyProvider;
import org.jetbrains.annotations.Nullable;

public abstract class BaseCommand {
   static final String CATCHUNKNOWN = "__catchunknown";
   static final String DEFAULT = "__default";
   final SetMultimap<String, RegisteredCommand> subCommands = HashMultimap.create();
   final Set<BaseCommand> subScopes = new HashSet();
   final Map<Class<?>, String> contextFlags = new HashMap();
   @Nullable
   private Method preCommandHandler;
   private String execLabel;
   private String execSubcommand;
   private String[] origArgs;
   CommandManager<?, ?, ?, ?, ?, ?> manager = null;
   BaseCommand parentCommand;
   Map<String, RootCommand> registeredCommands = new HashMap();
   @Nullable
   String description;
   @Nullable
   String commandName;
   @Nullable
   String permission;
   @Nullable
   String conditions;
   boolean hasHelpCommand;
   private ExceptionHandler exceptionHandler = null;
   private final ThreadLocal<CommandOperationContext> lastCommandOperationContext = new ThreadLocal();
   @Nullable
   private String parentSubcommand;
   private final Set<String> permissions = new HashSet();

   public BaseCommand() {
   }

   /** @deprecated */
   @Deprecated
   public BaseCommand(@Nullable String cmd) {
      this.commandName = var1;
   }

   public CommandOperationContext getLastCommandOperationContext() {
      return (CommandOperationContext)this.lastCommandOperationContext.get();
   }

   public String getExecCommandLabel() {
      return this.execLabel;
   }

   public String getExecSubcommand() {
      return this.execSubcommand;
   }

   public String[] getOrigArgs() {
      return this.origArgs;
   }

   void onRegister(CommandManager manager) {
      this.onRegister(var1, this.commandName);
   }

   private void onRegister(CommandManager manager, String cmd) {
      var1.injectDependencies(this);
      this.manager = var1;
      Annotations var3 = var1.getAnnotations();
      Class var4 = this.getClass();
      String[] var5 = var3.getAnnotationValues(var4, CommandAlias.class, 11);
      if (var2 == null && var5 != null) {
         var2 = var5[0];
      }

      this.commandName = var2 != null ? var2 : var4.getSimpleName().toLowerCase(Locale.ENGLISH);
      this.permission = var3.getAnnotationValue(var4, CommandPermission.class, 1);
      this.description = var3.getAnnotationValue(var4, Description.class, 9);
      this.parentSubcommand = this.getParentSubcommand(var4);
      this.conditions = var3.getAnnotationValue(var4, Conditions.class, 9);
      this.computePermissions();
      this.registerSubcommands();
      this.registerSubclasses(var2);
      if (var5 != null) {
         HashSet var6 = new HashSet();
         Collections.addAll(var6, var5);
         var6.remove(var2);
         Iterator var7 = var6.iterator();

         while(var7.hasNext()) {
            String var8 = (String)var7.next();
            this.registerSubclasses(var8);
            this.register(var8, this);
         }
      }

      if (var2 != null) {
         this.register(var2, this);
      }

   }

   private void registerSubclasses(String cmd) {
      Class[] var2 = this.getClass().getDeclaredClasses();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Class var5 = var2[var4];
         if (BaseCommand.class.isAssignableFrom(var5)) {
            try {
               BaseCommand var6 = null;
               Constructor[] var7 = var5.getDeclaredConstructors();
               ArrayList var8 = new ArrayList();
               Constructor[] var9 = var7;
               int var10 = var7.length;

               for(int var11 = 0; var11 < var10; ++var11) {
                  Constructor var12 = var9[var11];
                  var12.setAccessible(true);
                  Parameter[] var13 = var12.getParameters();
                  if (var13.length == 1 && var13[0].getType().isAssignableFrom(this.getClass())) {
                     var6 = (BaseCommand)var12.newInstance(this);
                  } else {
                     var8.add("Found unusable constructor: " + var12.getName() + "(" + (String)Stream.of(var13).map((var0) -> {
                        return var0.getType().getSimpleName() + " " + var0.getName();
                     }).collect(Collectors.joining("<c2>,</c2> ")) + ")");
                  }
               }

               if (var6 != null) {
                  var6.parentCommand = this;
                  this.subScopes.add(var6);
                  var6.onRegister(this.manager, var1);
                  this.subCommands.putAll(var6.subCommands);
                  this.registeredCommands.putAll(var6.registeredCommands);
               } else {
                  this.manager.log(LogLevel.ERROR, "Could not find a subcommand ctor for " + var5.getName());
                  Iterator var15 = var8.iterator();

                  while(var15.hasNext()) {
                     String var16 = (String)var15.next();
                     this.manager.log(LogLevel.INFO, var16);
                  }
               }
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException var14) {
               this.manager.log(LogLevel.ERROR, "Error registering subclass", var14);
            }
         }
      }

   }

   private void registerSubcommands() {
      Annotations var1 = this.manager.getAnnotations();
      boolean var2 = false;
      boolean var3 = this.parentSubcommand == null || this.parentSubcommand.isEmpty();
      LinkedHashSet var4 = new LinkedHashSet();
      Collections.addAll(var4, this.getClass().getDeclaredMethods());
      Collections.addAll(var4, this.getClass().getMethods());
      Iterator var5 = var4.iterator();

      while(var5.hasNext()) {
         Method var6 = (Method)var5.next();
         var6.setAccessible(true);
         String var7 = null;
         String var8 = this.getSubcommandValue(var6);
         String var9 = var1.getAnnotationValue(var6, HelpCommand.class, 0);
         String var10 = var1.getAnnotationValue(var6, CommandAlias.class, 0);
         if (var1.hasAnnotation(var6, Default.class)) {
            if (!var3) {
               var8 = this.parentSubcommand;
            } else {
               this.registerSubcommand(var6, "__default");
            }
         }

         if (var8 != null) {
            var7 = var8;
         } else if (var10 != null) {
            var7 = var10;
         } else if (var9 != null) {
            var7 = var9;
            this.hasHelpCommand = true;
         }

         boolean var11 = var1.hasAnnotation(var6, PreCommand.class);
         boolean var12 = var1.hasAnnotation(var6, CatchUnknown.class) || var1.hasAnnotation(var6, CatchAll.class) || var1.hasAnnotation(var6, UnknownHandler.class);
         if (var12 || !var2 && var9 != null) {
            if (!var2) {
               if (var12) {
                  this.subCommands.get("__catchunknown").clear();
                  var2 = true;
               }

               this.registerSubcommand(var6, "__catchunknown");
            } else {
               ACFUtil.sneaky(new IllegalStateException("Multiple @CatchUnknown/@HelpCommand commands, duplicate on " + var6.getDeclaringClass().getName() + "#" + var6.getName()));
            }
         } else if (var11) {
            if (this.preCommandHandler == null) {
               this.preCommandHandler = var6;
            } else {
               ACFUtil.sneaky(new IllegalStateException("Multiple @PreCommand commands, duplicate on " + var6.getDeclaringClass().getName() + "#" + var6.getName()));
            }
         }

         if (Objects.equals(var6.getDeclaringClass(), this.getClass()) && var7 != null) {
            this.registerSubcommand(var6, var7);
         }
      }

   }

   private void computePermissions() {
      this.permissions.clear();
      if (this.permission != null && !this.permission.isEmpty()) {
         this.permissions.addAll(Arrays.asList(ACFPatterns.COMMA.split(this.permission)));
      }

      if (this.parentCommand != null) {
         this.permissions.addAll(this.parentCommand.getRequiredPermissions());
      }

      this.subCommands.values().forEach(RegisteredCommand::computePermissions);
      this.subScopes.forEach(BaseCommand::computePermissions);
   }

   private String getSubcommandValue(Method method) {
      String var2 = this.manager.getAnnotations().getAnnotationValue(var1, Subcommand.class, 0);
      if (var2 == null) {
         return null;
      } else {
         Class var3 = var1.getDeclaringClass();
         String var4 = this.getParentSubcommand(var3);
         return var4 != null && !var4.isEmpty() ? var4 + " " + var2 : var2;
      }
   }

   private String getParentSubcommand(Class<?> clazz) {
      ArrayList var2;
      for(var2 = new ArrayList(); var1 != null; var1 = var1.getEnclosingClass()) {
         String var3 = this.manager.getAnnotations().getAnnotationValue(var1, Subcommand.class, 0);
         if (var3 != null) {
            var2.add(var3);
         }
      }

      Collections.reverse(var2);
      return ACFUtil.join((Collection)var2, " ");
   }

   private void register(String name, BaseCommand cmd) {
      String var3 = var1.toLowerCase(Locale.ENGLISH);
      RootCommand var4 = this.manager.obtainRootCommand(var3);
      var4.addChild(var2);
      this.registeredCommands.put(var3, var4);
   }

   private void registerSubcommand(Method method, String subCommand) {
      var2 = this.manager.getCommandReplacements().replace(var2.toLowerCase(Locale.ENGLISH));
      String[] var3 = ACFPatterns.SPACE.split(var2);
      Set var4 = getSubCommandPossibilityList(var3);

      String[] var6;
      for(int var5 = 0; var5 < var3.length; ++var5) {
         var6 = ACFPatterns.PIPE.split(var3[var5]);
         if (var6.length == 0 || var6[0].isEmpty()) {
            throw new IllegalArgumentException("Invalid @Subcommand configuration for " + var1.getName() + " - parts can not start with | or be empty");
         }

         var3[var5] = var6[0];
      }

      String var13 = ApacheCommonsLangUtil.join((Object[])var3, " ");
      var6 = this.manager.getAnnotations().getAnnotationValues(var1, CommandAlias.class, 3);
      String var7 = var6 != null ? var6[0] : this.commandName + " ";
      RegisteredCommand var8 = this.manager.createRegisteredCommand(this, var7, var1, var13);
      Iterator var9 = var4.iterator();

      while(var9.hasNext()) {
         String var10 = (String)var9.next();
         this.subCommands.put(var10, var8);
      }

      var8.addSubcommands(var4);
      if (var6 != null) {
         String[] var14 = var6;
         int var15 = var6.length;

         for(int var11 = 0; var11 < var15; ++var11) {
            String var12 = var14[var11];
            this.register(var12, new ForwardingCommand(this, var8, var3));
         }
      }

   }

   private static Set<String> getSubCommandPossibilityList(String[] subCommandParts) {
      int var1 = 0;
      HashSet var2 = null;

      while(true) {
         HashSet var3 = new HashSet();
         if (var1 < var0.length) {
            String[] var4 = ACFPatterns.PIPE.split(var0[var1]);
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               String var7 = var4[var6];
               if (var2 != null) {
                  var3.addAll((Collection)var2.stream().map((var1x) -> {
                     return var1x + " " + var7;
                  }).collect(Collectors.toList()));
               } else {
                  var3.add(var7);
               }
            }
         }

         if (var1 + 1 >= var0.length) {
            return var3;
         }

         var2 = var3;
         ++var1;
      }
   }

   void execute(CommandIssuer issuer, CommandRouter.CommandRouteResult command) {
      try {
         CommandOperationContext var3 = this.preCommandOperation(var1, var2.commandLabel, var2.args, false);
         this.execSubcommand = var2.subcommand;
         this.executeCommand(var3, var1, var2.args, var2.cmd);
      } finally {
         this.postCommandOperation();
      }

   }

   private void postCommandOperation() {
      ((Stack)CommandManager.commandOperationContext.get()).pop();
      this.lastCommandOperationContext.set((Object)null);
      this.execSubcommand = null;
      this.execLabel = null;
      this.origArgs = new String[0];
   }

   private CommandOperationContext preCommandOperation(CommandIssuer issuer, String commandLabel, String[] args, boolean isAsync) {
      Stack var5 = (Stack)CommandManager.commandOperationContext.get();
      CommandOperationContext var6 = this.manager.createCommandOperationContext(this, var1, var2, var3, var4);
      var5.push(var6);
      this.lastCommandOperationContext.set(var6);
      this.execSubcommand = null;
      this.execLabel = var2;
      this.origArgs = var3;
      return var6;
   }

   public CommandIssuer getCurrentCommandIssuer() {
      return CommandManager.getCurrentCommandIssuer();
   }

   public CommandManager getCurrentCommandManager() {
      return CommandManager.getCurrentCommandManager();
   }

   private void executeCommand(CommandOperationContext commandOperationContext, CommandIssuer issuer, String[] args, RegisteredCommand cmd) {
      if (var4.hasPermission(var2)) {
         var1.setRegisteredCommand(var4);
         if (this.checkPrecommand(var1, var4, var2, var3)) {
            return;
         }

         List var5 = Arrays.asList(var3);
         var4.invoke(var2, var5, var1);
      } else {
         var2.sendMessage(MessageType.ERROR, (MessageKeyProvider)MessageKeys.PERMISSION_DENIED);
      }

   }

   /** @deprecated */
   @Deprecated
   public boolean canExecute(CommandIssuer issuer, RegisteredCommand<?> cmd) {
      return true;
   }

   public List<String> tabComplete(CommandIssuer issuer, String commandLabel, String[] args) {
      return this.tabComplete(var1, var2, var3, false);
   }

   public List<String> tabComplete(CommandIssuer issuer, String commandLabel, String[] args, boolean isAsync) {
      return this.tabComplete(var1, this.manager.getRootCommand(var2.toLowerCase(Locale.ENGLISH)), var3, var4);
   }

   List<String> tabComplete(CommandIssuer issuer, RootCommand rootCommand, String[] args, boolean isAsync) {
      if (var3.length == 0) {
         var3 = new String[]{""};
      }

      String var5 = var2.getCommandName();

      List var14;
      try {
         CommandRouter var6 = this.manager.getRouter();
         this.preCommandOperation(var1, var5, var3, var4);
         CommandRouter.RouteSearch var7 = var6.routeCommand(var2, var5, var3, true);
         ArrayList var8 = new ArrayList();
         if (var7 != null) {
            Iterator var9 = var7.commands.iterator();

            while(var9.hasNext()) {
               RegisteredCommand var10 = (RegisteredCommand)var9.next();
               if (var10.scope == this) {
                  var8.addAll(this.completeCommand(var1, var10, var7.args, var5, var4));
               }
            }
         }

         var14 = filterTabComplete(var3[var3.length - 1], var8);
      } finally {
         this.postCommandOperation();
      }

      return var14;
   }

   List<String> getCommandsForCompletion(CommandIssuer issuer, String[] args) {
      HashSet var3 = new HashSet();
      int var4 = Math.max(0, var2.length - 1);
      String var5 = ApacheCommonsLangUtil.join((Object[])var2, " ").toLowerCase(Locale.ENGLISH);
      Iterator var6 = this.subCommands.entries().iterator();

      while(var6.hasNext()) {
         Entry var7 = (Entry)var6.next();
         String var8 = (String)var7.getKey();
         if (var8.startsWith(var5) && !isSpecialSubcommand(var8)) {
            RegisteredCommand var9 = (RegisteredCommand)var7.getValue();
            if (var9.hasPermission(var1) && !var9.isPrivate) {
               String[] var10 = ACFPatterns.SPACE.split(var9.prefSubCommand);
               var3.add(var10[var4]);
            }
         }
      }

      return new ArrayList(var3);
   }

   static boolean isSpecialSubcommand(String key) {
      return "__catchunknown".equals(var0) || "__default".equals(var0);
   }

   private List<String> completeCommand(CommandIssuer issuer, RegisteredCommand cmd, String[] args, String commandLabel, boolean isAsync) {
      if (var2.hasPermission(var1) && var3.length != 0 && var2.parameters.length != 0) {
         if (!var2.parameters[var2.parameters.length - 1].consumesRest && var3.length > var2.consumeInputResolvers) {
            return Collections.emptyList();
         } else {
            List var6 = this.manager.getCommandCompletions().of(var2, var1, var3, var5);
            return filterTabComplete(var3[var3.length - 1], var6);
         }
      } else {
         return Collections.emptyList();
      }
   }

   private static List<String> filterTabComplete(String arg, List<String> cmds) {
      return (List)var1.stream().distinct().filter((var1x) -> {
         return var1x != null && (var0.isEmpty() || ApacheCommonsLangUtil.startsWithIgnoreCase(var1x, var0));
      }).collect(Collectors.toList());
   }

   private boolean checkPrecommand(CommandOperationContext commandOperationContext, RegisteredCommand cmd, CommandIssuer issuer, String[] args) {
      Method var5 = this.preCommandHandler;
      if (var5 != null) {
         try {
            Class[] var6 = var5.getParameterTypes();
            Object[] var7 = new Object[var5.getParameterCount()];

            for(int var8 = 0; var8 < var7.length; ++var8) {
               Class var9 = var6[var8];
               Object var10 = var3.getIssuer();
               if (this.manager.isCommandIssuer(var9) && var9.isAssignableFrom(var10.getClass())) {
                  var7[var8] = var10;
               } else if (CommandIssuer.class.isAssignableFrom(var9)) {
                  var7[var8] = var3;
               } else if (RegisteredCommand.class.isAssignableFrom(var9)) {
                  var7[var8] = var2;
               } else if (String[].class.isAssignableFrom(var9)) {
                  var7[var8] = var4;
               } else {
                  var7[var8] = null;
               }
            }

            return (Boolean)var5.invoke(this, var7);
         } catch (InvocationTargetException | IllegalAccessException var11) {
            this.manager.log(LogLevel.ERROR, "Exception encountered while command pre-processing", var11);
         }
      }

      return false;
   }

   /** @deprecated */
   @Deprecated
   @UnstableAPI
   public CommandHelp getCommandHelp() {
      return this.manager.generateCommandHelp();
   }

   /** @deprecated */
   @Deprecated
   @UnstableAPI
   public void showCommandHelp() {
      this.getCommandHelp().showHelp();
   }

   public void help(Object issuer, String[] args) {
      this.help(this.manager.getCommandIssuer(var1), var2);
   }

   public void help(CommandIssuer issuer, String[] args) {
      var1.sendMessage(MessageType.ERROR, (MessageKeyProvider)MessageKeys.UNKNOWN_COMMAND);
   }

   public void doHelp(Object issuer, String... args) {
      this.doHelp(this.manager.getCommandIssuer(var1), var2);
   }

   public void doHelp(CommandIssuer issuer, String... args) {
      this.help(var1, var2);
   }

   public void showSyntax(CommandIssuer issuer, RegisteredCommand<?> cmd) {
      var1.sendMessage(MessageType.SYNTAX, (MessageKeyProvider)MessageKeys.INVALID_SYNTAX, "{command}", this.manager.getCommandPrefix(var1) + var2.command, "{syntax}", var2.getSyntaxText(var1));
   }

   public boolean hasPermission(Object issuer) {
      return this.hasPermission(this.manager.getCommandIssuer(var1));
   }

   public boolean hasPermission(CommandIssuer issuer) {
      return this.manager.hasPermission(var1, this.getRequiredPermissions());
   }

   public Set<String> getRequiredPermissions() {
      return this.permissions;
   }

   public boolean requiresPermission(String permission) {
      return this.getRequiredPermissions().contains(var1);
   }

   public String getName() {
      return this.commandName;
   }

   public ExceptionHandler getExceptionHandler() {
      return this.exceptionHandler;
   }

   public BaseCommand setExceptionHandler(ExceptionHandler exceptionHandler) {
      this.exceptionHandler = var1;
      return this;
   }

   public RegisteredCommand getDefaultRegisteredCommand() {
      return (RegisteredCommand)ACFUtil.getFirstElement(this.subCommands.get("__default"));
   }

   public String setContextFlags(Class<?> cls, String flags) {
      return (String)this.contextFlags.put(var1, var2);
   }

   public String getContextFlags(Class<?> cls) {
      return (String)this.contextFlags.get(var1);
   }

   public List<RegisteredCommand> getRegisteredCommands() {
      ArrayList var1 = new ArrayList();
      var1.addAll(this.subCommands.values());
      return var1;
   }

   protected SetMultimap<String, RegisteredCommand> getSubCommands() {
      return this.subCommands;
   }
}
