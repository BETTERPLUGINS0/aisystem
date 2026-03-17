package me.PM2.infinitevehicles.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import me.PM2.infinitevehicles.commands.apachecommonslang.ApacheCommonsLangUtil;
import org.jetbrains.annotations.NotNull;

public class CommandCompletions<C extends CommandCompletionContext> {
   private static final String DEFAULT_ENUM_ID = "@__defaultenum__";
   private final CommandManager manager;
   private Map<String, CommandCompletions.CommandCompletionHandler> completionMap = new HashMap();
   private Map<Class, String> defaultCompletions = new HashMap();

   public CommandCompletions(CommandManager manager) {
      this.manager = var1;
      this.registerStaticCompletion("empty", (Collection)Collections.emptyList());
      this.registerStaticCompletion("nothing", (Collection)Collections.emptyList());
      this.registerStaticCompletion("timeunits", (Collection)Arrays.asList("minutes", "hours", "days", "weeks", "months", "years"));
      this.registerAsyncCompletion("range", (var0) -> {
         String var1 = var0.getConfig();
         if (var1 == null) {
            return Collections.emptyList();
         } else {
            String[] var2 = ACFPatterns.DASH.split(var1);
            int var3;
            int var4;
            if (var2.length != 2) {
               var3 = 0;
               var4 = ACFUtil.parseInt(var2[0], 0);
            } else {
               var3 = ACFUtil.parseInt(var2[0], 0);
               var4 = ACFUtil.parseInt(var2[1], 0);
            }

            return (Collection)IntStream.rangeClosed(var3, var4).mapToObj(Integer::toString).collect(Collectors.toList());
         }
      });
   }

   public CommandCompletions.CommandCompletionHandler registerCompletion(String id, CommandCompletions.CommandCompletionHandler<C> handler) {
      return (CommandCompletions.CommandCompletionHandler)this.completionMap.put(prepareCompletionId(var1), var2);
   }

   public CommandCompletions.CommandCompletionHandler unregisterCompletion(String id) {
      if (!this.completionMap.containsKey(var1)) {
         throw new IllegalStateException("The supplied key " + var1 + " does not exist in any completions");
      } else {
         return (CommandCompletions.CommandCompletionHandler)this.completionMap.remove(var1);
      }
   }

   public CommandCompletions.CommandCompletionHandler registerAsyncCompletion(String id, CommandCompletions.AsyncCommandCompletionHandler<C> handler) {
      return (CommandCompletions.CommandCompletionHandler)this.completionMap.put(prepareCompletionId(var1), var2);
   }

   public CommandCompletions.CommandCompletionHandler registerStaticCompletion(String id, String list) {
      return this.registerStaticCompletion(var1, ACFPatterns.PIPE.split(var2));
   }

   public CommandCompletions.CommandCompletionHandler registerStaticCompletion(String id, String[] completions) {
      return this.registerStaticCompletion(var1, (Collection)Arrays.asList(var2));
   }

   public CommandCompletions.CommandCompletionHandler registerStaticCompletion(String id, Supplier<Collection<String>> supplier) {
      return this.registerStaticCompletion(var1, (Collection)var2.get());
   }

   public CommandCompletions.CommandCompletionHandler registerStaticCompletion(String id, Collection<String> completions) {
      return this.registerAsyncCompletion(var1, (var1x) -> {
         return var2;
      });
   }

   public void setDefaultCompletion(String id, Class... classes) {
      var1 = prepareCompletionId(var1);
      CommandCompletions.CommandCompletionHandler var3 = (CommandCompletions.CommandCompletionHandler)this.completionMap.get(var1);
      if (var3 == null) {
         throw new IllegalStateException("Completion not registered for " + var1);
      } else {
         Class[] var4 = var2;
         int var5 = var2.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Class var7 = var4[var6];
            this.defaultCompletions.put(var7, var1);
         }

      }
   }

   @NotNull
   private static String prepareCompletionId(String id) {
      return (var0.startsWith("@") ? "" : "@") + var0.toLowerCase(Locale.ENGLISH);
   }

   @NotNull
   List<String> of(RegisteredCommand cmd, CommandIssuer sender, String[] args, boolean isAsync) {
      String[] var5 = ACFPatterns.SPACE.split(var1.complete);
      int var6 = var3.length - 1;
      String var7 = var6 < var5.length ? var5[var6] : null;
      if (var7 == null || var7.isEmpty() || "*".equals(var7)) {
         var7 = this.findDefaultCompletion(var1, var3);
      }

      if (var7 == null && var5.length > 0) {
         String var8 = var5[var5.length - 1];
         if (var8.startsWith("repeat@")) {
            var7 = var8;
         } else if (var6 >= var5.length && var1.parameters[var1.parameters.length - 1].consumesRest) {
            var7 = var8;
         }
      }

      return var7 == null ? Collections.emptyList() : this.getCompletionValues(var1, var2, var7, var3, var4);
   }

   String findDefaultCompletion(RegisteredCommand cmd, String[] args) {
      int var3 = 0;
      CommandParameter[] var4 = var1.parameters;
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         CommandParameter var7 = var4[var6];
         if (var7.canConsumeInput()) {
            ++var3;
            if (var3 == var2.length) {
               for(Class var8 = var7.getType(); var8 != null; var8 = var8.getSuperclass()) {
                  String var9 = (String)this.defaultCompletions.get(var8);
                  if (var9 != null) {
                     return var9;
                  }
               }

               if (var7.getType().isEnum()) {
                  CommandOperationContext var10 = CommandManager.getCurrentCommandOperationContext();
                  var10.enumCompletionValues = ACFUtil.enumNames(var7.getType());
                  return "@__defaultenum__";
               }
               break;
            }
         }
      }

      return null;
   }

   List<String> getCompletionValues(RegisteredCommand command, CommandIssuer sender, String completion, String[] args, boolean isAsync) {
      if ("@__defaultenum__".equals(var3)) {
         CommandOperationContext var21 = CommandManager.getCurrentCommandOperationContext();
         return var21.enumCompletionValues;
      } else {
         boolean var6 = var3.startsWith("repeat@");
         if (var6) {
            var3 = var3.substring(6);
         }

         var3 = this.manager.getCommandReplacements().replace(var3);
         ArrayList var7 = new ArrayList();
         String var8 = var4.length > 0 ? var4[var4.length - 1] : "";
         String[] var9 = ACFPatterns.PIPE.split(var3);
         int var10 = var9.length;
         int var11 = 0;

         while(true) {
            if (var11 >= var10) {
               return var7;
            }

            String var12 = var9[var11];
            String[] var13 = ACFPatterns.COLONEQUALS.split(var12, 2);
            CommandCompletions.CommandCompletionHandler var14 = (CommandCompletions.CommandCompletionHandler)this.completionMap.get(var13[0].toLowerCase(Locale.ENGLISH));
            if (var14 != null) {
               if (var5 && !(var14 instanceof CommandCompletions.AsyncCommandCompletionHandler)) {
                  ACFUtil.sneaky(new CommandCompletions.SyncCompletionRequired());
                  return null;
               }

               String var15 = var13.length == 1 ? null : var13[1];
               CommandCompletionContext var16 = this.manager.createCompletionContext(var1, var2, var8, var15, var4);

               try {
                  Collection var17 = var14.getCompletions(var16);
                  if (!var6 && var17 != null && var1.parameters[var1.parameters.length - 1].consumesRest && var4.length > ACFPatterns.SPACE.split(var1.complete).length) {
                     String var18 = String.join(" ", var4);
                     var17 = (Collection)var17.stream().map((var2x) -> {
                        if (var2x != null && var2x.split(" ").length >= var4.length && ApacheCommonsLangUtil.startsWithIgnoreCase(var2x, var18)) {
                           String[] var3 = var2x.split(" ");
                           return String.join(" ", (CharSequence[])Arrays.copyOfRange(var3, var4.length - 1, var3.length));
                        } else {
                           return var2x;
                        }
                     }).collect(Collectors.toList());
                  }

                  if (var17 == null) {
                     break;
                  }

                  var7.addAll(var17);
               } catch (CommandCompletionTextLookupException var19) {
                  break;
               } catch (Exception var20) {
                  var1.handleException(var2, Arrays.asList(var4), var20);
                  break;
               }
            } else {
               var7.add(var12);
            }

            ++var11;
         }

         return Collections.emptyList();
      }
   }

   public interface CommandCompletionHandler<C extends CommandCompletionContext> {
      Collection<String> getCompletions(C context) throws InvalidCommandArgument;
   }

   public interface AsyncCommandCompletionHandler<C extends CommandCompletionContext> extends CommandCompletions.CommandCompletionHandler<C> {
   }

   public static class SyncCompletionRequired extends RuntimeException {
   }
}
