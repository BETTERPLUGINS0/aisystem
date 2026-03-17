package me.PM2.infinitevehicles.commands;

import com.google.common.collect.SetMultimap;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import me.PM2.infinitevehicles.commands.apachecommonslang.ApacheCommonsLangUtil;

class CommandRouter {
   private final CommandManager manager;

   CommandRouter(CommandManager manager) {
      this.manager = var1;
   }

   CommandRouter.CommandRouteResult matchCommand(CommandRouter.RouteSearch search, boolean completion) {
      Set var3 = var1.commands;
      String[] var4 = var1.args;
      if (!var3.isEmpty()) {
         if (var3.size() == 1) {
            return new CommandRouter.CommandRouteResult((RegisteredCommand)ACFUtil.getFirstElement(var3), var1);
         }

         Optional var5 = var3.stream().filter((var3x) -> {
            return this.isProbableMatch(var3x, var4, var2);
         }).min((var0, var1x) -> {
            int var2 = var0.consumeInputResolvers;
            int var3 = var1x.consumeInputResolvers;
            if (var2 == var3) {
               return 0;
            } else {
               return var2 < var3 ? 1 : -1;
            }
         });
         if (var5.isPresent()) {
            return new CommandRouter.CommandRouteResult((RegisteredCommand)var5.get(), var1);
         }
      }

      return null;
   }

   private boolean isProbableMatch(RegisteredCommand c, String[] args, boolean completion) {
      int var4 = var1.requiredResolvers;
      int var5 = var1.optionalResolvers;
      if (var3) {
         return var2.length <= var4 + var5;
      } else {
         return var2.length >= var4;
      }
   }

   CommandRouter.RouteSearch routeCommand(RootCommand command, String commandLabel, String[] args, boolean completion) {
      SetMultimap var5 = var1.getSubCommands();
      int var6 = var3.length;

      for(int var7 = var6; var7 >= 0; --var7) {
         String var8 = ApacheCommonsLangUtil.join(var3, " ", 0, var7).toLowerCase(Locale.ENGLISH);
         Set var9 = var5.get(var8);
         if (!var9.isEmpty()) {
            return new CommandRouter.RouteSearch(var9, (String[])Arrays.copyOfRange(var3, var7, var6), var2, var8, var4);
         }
      }

      Set var15 = var5.get("__default");
      Set var16 = var5.get("__catchunknown");
      if (!var15.isEmpty()) {
         HashSet var17 = new HashSet();
         Iterator var10 = var15.iterator();

         while(true) {
            RegisteredCommand var11;
            int var12;
            int var13;
            CommandParameter var14;
            do {
               if (!var10.hasNext()) {
                  if (!var17.isEmpty()) {
                     return new CommandRouter.RouteSearch(var17, var3, var2, (String)null, var4);
                  }

                  return !var16.isEmpty() ? new CommandRouter.RouteSearch(var16, var3, var2, (String)null, var4) : null;
               }

               var11 = (RegisteredCommand)var10.next();
               var12 = var11.requiredResolvers;
               var13 = var11.optionalResolvers;
               var14 = var11.parameters.length > 0 ? var11.parameters[var11.parameters.length - 1] : null;
            } while(var6 > var12 + var13 && (var14 == null || var14.getType() != String[].class && (var6 < var12 || !var14.consumesRest)));

            var17.add(var11);
         }
      } else {
         return !var16.isEmpty() ? new CommandRouter.RouteSearch(var16, var3, var2, (String)null, var4) : null;
      }
   }

   static class RouteSearch {
      final String[] args;
      final Set<RegisteredCommand> commands;
      final String commandLabel;
      final String subcommand;

      RouteSearch(Set<RegisteredCommand> commands, String[] args, String commandLabel, String subcommand, boolean completion) {
         this.commands = var1;
         this.args = var2;
         this.commandLabel = var3.toLowerCase(Locale.ENGLISH);
         this.subcommand = var4;
      }
   }

   static class CommandRouteResult {
      final RegisteredCommand cmd;
      final String[] args;
      final String commandLabel;
      final String subcommand;

      CommandRouteResult(RegisteredCommand cmd, CommandRouter.RouteSearch search) {
         this(var1, var2.args, var2.subcommand, var2.commandLabel);
      }

      CommandRouteResult(RegisteredCommand cmd, String[] args, String subcommand, String commandLabel) {
         this.cmd = var1;
         this.args = var2;
         this.commandLabel = var4;
         this.subcommand = var3;
      }
   }
}
