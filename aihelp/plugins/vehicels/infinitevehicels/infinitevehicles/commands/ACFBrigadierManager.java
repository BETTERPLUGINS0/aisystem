package me.PM2.infinitevehicles.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/** @deprecated */
@Deprecated
@UnstableAPI
public class ACFBrigadierManager<S> {
   protected final CommandManager<?, ?, ?, ?, ?, ?> manager;
   private final Map<Class<?>, ArgumentType<?>> arguments = new HashMap();

   ACFBrigadierManager(CommandManager<?, ?, ?, ?, ?, ?> manager) {
      var1.verifyUnstableAPI("brigadier");
      this.manager = var1;
      this.registerArgument(String.class, StringArgumentType.word());
      this.registerArgument(Float.TYPE, FloatArgumentType.floatArg());
      this.registerArgument(Float.class, FloatArgumentType.floatArg());
      this.registerArgument(Double.TYPE, DoubleArgumentType.doubleArg());
      this.registerArgument(Double.class, DoubleArgumentType.doubleArg());
      this.registerArgument(Boolean.TYPE, BoolArgumentType.bool());
      this.registerArgument(Boolean.class, BoolArgumentType.bool());
      this.registerArgument(Integer.TYPE, IntegerArgumentType.integer());
      this.registerArgument(Integer.class, IntegerArgumentType.integer());
      this.registerArgument(Long.TYPE, IntegerArgumentType.integer());
      this.registerArgument(Long.class, IntegerArgumentType.integer());
   }

   <T> void registerArgument(Class<T> clazz, ArgumentType<?> type) {
      this.arguments.put(var1, var2);
   }

   ArgumentType<Object> getArgumentTypeByClazz(CommandParameter param) {
      return (ArgumentType)(var1.consumesRest ? StringArgumentType.greedyString() : (ArgumentType)this.arguments.getOrDefault(var1.getType(), StringArgumentType.string()));
   }

   LiteralCommandNode<S> register(RootCommand rootCommand, LiteralCommandNode<S> root, SuggestionProvider<S> suggestionProvider, Command<S> executor, BiPredicate<RootCommand, S> permCheckerRoot, BiPredicate<RegisteredCommand, S> permCheckerSub) {
      LiteralArgumentBuilder var7 = (LiteralArgumentBuilder)LiteralArgumentBuilder.literal(var2.getLiteral()).requires((var2x) -> {
         return var5.test(var1, var2x);
      });
      RegisteredCommand var8 = var1.getDefaultRegisteredCommand();
      if (var8 != null && var8.requiredResolvers == 0) {
         var7.executes(var4);
      }

      var2 = var7.build();
      boolean var9 = var1.getDefCommand() instanceof ForwardingCommand;
      if (var8 != null) {
         this.registerParameters(var8, var2, var3, var4, var6);
      }

      Iterator var10 = var1.getSubCommands().entries().iterator();

      while(true) {
         Entry var11;
         do {
            do {
               if (!var10.hasNext()) {
                  return var2;
               }

               var11 = (Entry)var10.next();
            } while(BaseCommand.isSpecialSubcommand((String)var11.getKey()) && !var9);
         } while(!((String)var11.getKey()).equals("help") && ((RegisteredCommand)var11.getValue()).prefSubCommand.equals("help"));

         String var12 = (String)var11.getKey();
         Object var13 = var2;
         Predicate var15 = (var2x) -> {
            return var6.test((RegisteredCommand)var11.getValue(), var2x);
         };
         Object var14;
         if (var9) {
            var14 = var2;
         } else {
            if (var12.contains(" ")) {
               String[] var16 = ACFPatterns.SPACE.split(var12);

               for(int var17 = 0; var17 < var16.length - 1; ++var17) {
                  if (((CommandNode)var13).getChild(var16[var17]) == null) {
                     LiteralCommandNode var18 = ((LiteralArgumentBuilder)LiteralArgumentBuilder.literal(var16[var17]).requires(var15)).build();
                     ((CommandNode)var13).addChild(var18);
                     var13 = var18;
                  } else {
                     var13 = ((CommandNode)var13).getChild(var16[var17]);
                  }
               }

               var12 = var16[var16.length - 1];
            }

            var14 = ((CommandNode)var13).getChild(var12);
            if (var14 == null) {
               LiteralArgumentBuilder var19 = (LiteralArgumentBuilder)LiteralArgumentBuilder.literal(var12).requires(var15);
               if (((RegisteredCommand)var11.getValue()).requiredResolvers == 0) {
                  var19.executes(var4);
               }

               var14 = var19.build();
            }
         }

         this.registerParameters((RegisteredCommand)var11.getValue(), (CommandNode)var14, var3, var4, var6);
         if (!var9) {
            ((CommandNode)var13).addChild((CommandNode)var14);
         }
      }
   }

   void registerParameters(RegisteredCommand command, CommandNode<S> node, SuggestionProvider<S> suggestionProvider, Command<S> executor, BiPredicate<RegisteredCommand, S> permChecker) {
      for(int var6 = 0; var6 < var1.parameters.length; ++var6) {
         CommandParameter var7 = var1.parameters[var6];
         CommandParameter var8 = var7.getNextParam();
         if (!var7.isCommandIssuer() && (!var7.canExecuteWithoutInput() || var8 == null || var8.canExecuteWithoutInput())) {
            RequiredArgumentBuilder var9 = (RequiredArgumentBuilder)RequiredArgumentBuilder.argument(var7.getName(), this.getArgumentTypeByClazz(var7)).suggests(var3).requires((var2x) -> {
               return var5.test(var1, var2x);
            });
            if (var8 == null || var8.canExecuteWithoutInput()) {
               var9.executes(var4);
            }

            ArgumentCommandNode var10 = var9.build();
            ((CommandNode)var2).addChild(var10);
            var2 = var10;
         }
      }

   }
}
