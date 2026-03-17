package me.PM2.infinitevehicles.commands;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import me.PM2.infinitevehicles.commands.annotation.CommandAlias;
import me.PM2.infinitevehicles.commands.annotation.CommandCompletion;
import me.PM2.infinitevehicles.commands.annotation.CommandPermission;
import me.PM2.infinitevehicles.commands.annotation.Conditions;
import me.PM2.infinitevehicles.commands.annotation.Description;
import me.PM2.infinitevehicles.commands.annotation.HelpSearchTags;
import me.PM2.infinitevehicles.commands.annotation.Private;
import me.PM2.infinitevehicles.commands.annotation.Syntax;
import me.PM2.infinitevehicles.commands.contexts.ContextResolver;
import me.PM2.infinitevehicles.locales.MessageKeyProvider;
import org.jetbrains.annotations.Nullable;

public class RegisteredCommand<CEC extends CommandExecutionContext<CEC, ? extends CommandIssuer>> {
   final BaseCommand scope;
   final Method method;
   final CommandParameter<CEC>[] parameters;
   final CommandManager manager;
   final List<String> registeredSubcommands = new ArrayList();
   String command;
   String prefSubCommand;
   String syntaxText;
   String helpText;
   String permission;
   String complete;
   String conditions;
   public String helpSearchTags;
   boolean isPrivate;
   final int requiredResolvers;
   final int consumeInputResolvers;
   final int doesNotConsumeInputResolvers;
   final int optionalResolvers;
   final Set<String> permissions = new HashSet();

   RegisteredCommand(BaseCommand scope, String command, Method method, String prefSubCommand) {
      this.scope = var1;
      this.manager = this.scope.manager;
      Annotations var5 = this.manager.getAnnotations();
      if (BaseCommand.isSpecialSubcommand(var4)) {
         var4 = "";
         var2 = var2.trim();
      }

      this.command = var2 + (!var5.hasAnnotation(var3, CommandAlias.class, false) && !var4.isEmpty() ? var4 : "");
      this.method = var3;
      this.prefSubCommand = var4;
      this.permission = var5.getAnnotationValue(var3, CommandPermission.class, 9);
      this.complete = var5.getAnnotationValue(var3, CommandCompletion.class, 17);
      this.helpText = var5.getAnnotationValue(var3, Description.class, 17);
      this.conditions = var5.getAnnotationValue(var3, Conditions.class, 9);
      this.helpSearchTags = var5.getAnnotationValue(var3, HelpSearchTags.class, 9);
      this.syntaxText = var5.getAnnotationValue(var3, Syntax.class, 1);
      Parameter[] var6 = var3.getParameters();
      this.parameters = new CommandParameter[var6.length];
      this.isPrivate = var5.hasAnnotation(var3, Private.class) || var5.getAnnotationFromClass(var1.getClass(), Private.class) != null;
      int var7 = 0;
      int var8 = 0;
      int var9 = 0;
      int var10 = 0;
      CommandParameter var11 = null;

      for(int var12 = 0; var12 < var6.length; ++var12) {
         CommandParameter var13 = this.parameters[var12] = new CommandParameter(this, var6[var12], var12, var12 == var6.length - 1);
         if (var11 != null) {
            var11.setNextParam(var13);
         }

         var11 = var13;
         if (!var13.isCommandIssuer()) {
            if (!var13.requiresInput()) {
               ++var10;
            } else {
               ++var7;
            }

            if (var13.canConsumeInput()) {
               ++var8;
            } else {
               ++var9;
            }
         }
      }

      this.requiredResolvers = var7;
      this.consumeInputResolvers = var8;
      this.doesNotConsumeInputResolvers = var9;
      this.optionalResolvers = var10;
      this.computePermissions();
   }

   void invoke(CommandIssuer sender, List<String> args, CommandOperationContext context) {
      if (this.scope.canExecute(var1, this)) {
         this.preCommand();

         try {
            this.manager.getCommandConditions().validateConditions(var3);
            Map var4 = this.resolveContexts(var1, var2);
            if (var4 == null) {
               return;
            }

            Object var5 = this.method.invoke(this.scope, var4.values().toArray());
            if (var5 instanceof CompletionStage) {
               CompletionStage var6 = (CompletionStage)var5;
               var6.exceptionally((var3x) -> {
                  this.handleException(var1, var2, var3x);
                  return null;
               });
            }
         } catch (Exception var10) {
            this.handleException(var1, var2, var10);
         } finally {
            this.postCommand();
         }

      }
   }

   public void preCommand() {
   }

   public void postCommand() {
   }

   void handleException(CommandIssuer sender, List<String> args, Throwable e) {
      while(var3 instanceof ExecutionException || var3 instanceof CompletionException || var3 instanceof InvocationTargetException) {
         var3 = var3.getCause();
      }

      if (var3 instanceof ShowCommandHelp) {
         ShowCommandHelp var4 = (ShowCommandHelp)var3;
         CommandHelp var5 = this.manager.generateCommandHelp();
         if (var4.search) {
            var5.setSearch(var4.searchArgs == null ? var2 : var4.searchArgs);
         }

         var5.showHelp(var1);
      } else if (var3 instanceof InvalidCommandArgument) {
         InvalidCommandArgument var7 = (InvalidCommandArgument)var3;
         if (var7.key != null) {
            var1.sendMessage(MessageType.ERROR, var7.key, var7.replacements);
         } else if (var3.getMessage() != null && !var3.getMessage().isEmpty()) {
            var1.sendMessage(MessageType.ERROR, (MessageKeyProvider)MessageKeys.ERROR_PREFIX, "{message}", var3.getMessage());
         }

         if (var7.showSyntax) {
            this.scope.showSyntax(var1, this);
         }
      } else {
         try {
            if (!this.manager.handleUncaughtException(this.scope, this, var1, var2, var3)) {
               var1.sendMessage(MessageType.ERROR, (MessageKeyProvider)MessageKeys.ERROR_PERFORMING_COMMAND);
            }

            boolean var8 = this.manager.defaultExceptionHandler != null || this.scope.getExceptionHandler() != null;
            if (!var8 || this.manager.logUnhandledExceptions) {
               this.manager.log(LogLevel.ERROR, "Exception in command: " + this.command + " " + ACFUtil.join((Collection)var2), var3);
            }
         } catch (Exception var6) {
            this.manager.log(LogLevel.ERROR, "Exception in handleException for command: " + this.command + " " + ACFUtil.join((Collection)var2), var3);
            this.manager.log(LogLevel.ERROR, "Exception triggered by exception handler:", var6);
         }
      }

   }

   @Nullable
   Map<String, Object> resolveContexts(CommandIssuer sender, List<String> args) {
      return this.resolveContexts(var1, var2, (String)null);
   }

   @Nullable
   Map<String, Object> resolveContexts(CommandIssuer sender, List<String> args, String name) {
      ArrayList var26 = new ArrayList(var2);
      String[] var4 = (String[])var26.toArray(new String[var26.size()]);
      LinkedHashMap var5 = new LinkedHashMap();
      int var6 = this.requiredResolvers;
      CommandOperationContext var7 = CommandManager.getCurrentCommandOperationContext();

      for(int var8 = 0; var8 < this.parameters.length && (var3 == null || !var5.containsKey(var3)); ++var8) {
         boolean var9 = var8 == this.parameters.length - 1;
         boolean var10 = var6 == 0;
         CommandParameter var11 = this.parameters[var8];
         String var12 = var11.getName();
         Class var13 = var11.getType();
         ContextResolver var14 = var11.getResolver();
         CommandExecutionContext var15 = this.manager.createCommandContext(this, var11, var1, var26, var8, var5);
         boolean var16 = var11.requiresInput();
         if (var16 && var6 > 0) {
            --var6;
         }

         Set var17 = var11.getRequiredPermissions();
         Object var18;
         if (!var26.isEmpty() || var9 && var13 == String[].class) {
            if (!this.manager.hasPermission(var1, var17)) {
               var1.sendMessage(MessageType.ERROR, (MessageKeyProvider)MessageKeys.PERMISSION_DENIED_PARAMETER, "{param}", var12);
               throw new InvalidCommandArgument(false);
            }
         } else if (var10 && var11.getDefaultValue() != null) {
            var26.add(var11.getDefaultValue());
         } else {
            if (var10 && var11.isOptional()) {
               if (var11.isOptionalResolver() && this.manager.hasPermission(var1, var17)) {
                  var18 = var14.getContext(var15);
               } else {
                  var18 = null;
               }

               if (var18 == null && var11.getClass().isPrimitive()) {
                  throw new IllegalStateException("Parameter " + var11.getName() + " is primitive and does not support Optional.");
               }

               this.manager.getCommandConditions().validateConditions(var15, var18);
               var5.put(var12, var18);
               continue;
            }

            if (var16) {
               this.scope.showSyntax(var1, this);
               return null;
            }
         }

         if (var11.getValues() != null) {
            String var27 = !var26.isEmpty() ? (String)var26.get(0) : "";
            HashSet var19 = new HashSet();
            CommandCompletions var20 = this.manager.getCommandCompletions();
            String[] var21 = var11.getValues();
            int var22 = var21.length;
            int var23 = 0;

            while(true) {
               if (var23 >= var22) {
                  if (!var19.contains(var27.toLowerCase(Locale.ENGLISH))) {
                     throw new InvalidCommandArgument(MessageKeys.PLEASE_SPECIFY_ONE_OF, new String[]{"{valid}", ACFUtil.join((Collection)var19, ", ")});
                  }
                  break;
               }

               String var24 = var21[var23];
               if ("*".equals(var24) || "@completions".equals(var24)) {
                  var24 = var20.findDefaultCompletion(this, var4);
               }

               List var25 = var20.getCompletionValues(this, var1, var24, var4, var7.isAsync());
               if (!var25.isEmpty()) {
                  var19.addAll((Collection)var25.stream().filter(Objects::nonNull).map(String::toLowerCase).collect(Collectors.toList()));
               } else {
                  var19.add(var24.toLowerCase(Locale.ENGLISH));
               }

               ++var23;
            }
         }

         var18 = var14.getContext(var15);
         this.manager.getCommandConditions().validateConditions(var15, var18);
         var5.put(var12, var18);
      }

      return var5;
   }

   boolean hasPermission(CommandIssuer issuer) {
      return this.manager.hasPermission(var1, this.getRequiredPermissions());
   }

   /** @deprecated */
   @Deprecated
   public String getPermission() {
      return this.permission != null && !this.permission.isEmpty() ? ACFPatterns.COMMA.split(this.permission)[0] : null;
   }

   void computePermissions() {
      this.permissions.clear();
      this.permissions.addAll(this.scope.getRequiredPermissions());
      if (this.permission != null && !this.permission.isEmpty()) {
         this.permissions.addAll(Arrays.asList(ACFPatterns.COMMA.split(this.permission)));
      }

   }

   public Set<String> getRequiredPermissions() {
      return this.permissions;
   }

   public boolean requiresPermission(String permission) {
      return this.getRequiredPermissions().contains(var1);
   }

   public String getPrefSubCommand() {
      return this.prefSubCommand;
   }

   public String getSyntaxText() {
      return this.getSyntaxText((CommandIssuer)null);
   }

   public String getSyntaxText(CommandIssuer issuer) {
      if (this.syntaxText != null) {
         return this.syntaxText;
      } else {
         StringBuilder var2 = new StringBuilder(64);
         CommandParameter[] var3 = this.parameters;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            CommandParameter var6 = var3[var5];
            String var7 = var6.getSyntax(var1);
            if (var7 != null) {
               if (var2.length() > 0) {
                  var2.append(' ');
               }

               var2.append(var7);
            }
         }

         return var2.toString().trim();
      }
   }

   public String getHelpText() {
      return this.helpText != null ? this.helpText : "";
   }

   public boolean isPrivate() {
      return this.isPrivate;
   }

   public String getCommand() {
      return this.command;
   }

   public void addSubcommand(String cmd) {
      this.registeredSubcommands.add(var1);
   }

   public void addSubcommands(Collection<String> cmd) {
      this.registeredSubcommands.addAll(var1);
   }

   public <T extends Annotation> T getAnnotation(Class<T> annotation) {
      return this.method.getAnnotation(var1);
   }
}
