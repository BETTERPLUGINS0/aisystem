package me.PM2.infinitevehicles.commands;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import me.PM2.infinitevehicles.commands.lib.util.Table;
import org.jetbrains.annotations.NotNull;

public class CommandConditions<I extends CommandIssuer, CEC extends CommandExecutionContext<CEC, I>, CC extends ConditionContext<I>> {
   private CommandManager manager;
   private Map<String, CommandConditions.Condition<I>> conditions = new HashMap();
   private Table<Class<?>, String, CommandConditions.ParameterCondition<?, ?, ?>> paramConditions = new Table();

   CommandConditions(CommandManager manager) {
      this.manager = var1;
   }

   public CommandConditions.Condition<I> addCondition(@NotNull String id, @NotNull CommandConditions.Condition<I> handler) {
      return (CommandConditions.Condition)this.conditions.put(var1.toLowerCase(Locale.ENGLISH), var2);
   }

   public <P> CommandConditions.ParameterCondition addCondition(Class<P> clazz, @NotNull String id, @NotNull CommandConditions.ParameterCondition<P, CEC, I> handler) {
      return (CommandConditions.ParameterCondition)this.paramConditions.put(var1, var2.toLowerCase(Locale.ENGLISH), var3);
   }

   void validateConditions(CommandOperationContext context) {
      RegisteredCommand var2 = var1.getRegisteredCommand();
      this.validateConditions(var2.conditions, var1);
      this.validateConditions(var2.scope, var1);
   }

   private void validateConditions(BaseCommand scope, CommandOperationContext operationContext) {
      this.validateConditions(var1.conditions, var2);
      if (var1.parentCommand != null) {
         this.validateConditions(var1.parentCommand, var2);
      }

   }

   private void validateConditions(String conditions, CommandOperationContext context) {
      if (var1 != null) {
         var1 = this.manager.getCommandReplacements().replace(var1);
         CommandIssuer var3 = var2.getCommandIssuer();
         String[] var4 = ACFPatterns.PIPE.split(var1);
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String var7 = var4[var6];
            String[] var8 = ACFPatterns.COLON.split(var7, 2);
            String var9 = var8[0].toLowerCase(Locale.ENGLISH);
            CommandConditions.Condition var10 = (CommandConditions.Condition)this.conditions.get(var9);
            if (var10 == null) {
               RegisteredCommand var11 = var2.getRegisteredCommand();
               this.manager.log(LogLevel.ERROR, "Could not find command condition " + var9 + " for " + var11.method.getName());
            } else {
               String var13 = var8.length == 2 ? var8[1] : null;
               ConditionContext var12 = this.manager.createConditionContext(var3, var13);
               var10.validateCondition(var12);
            }
         }

      }
   }

   void validateConditions(CEC execContext, Object value) {
      String var3 = var1.getCommandParameter().getConditions();
      if (var3 != null) {
         var3 = this.manager.getCommandReplacements().replace(var3);
         CommandIssuer var4 = var1.getIssuer();
         String[] var5 = ACFPatterns.PIPE.split(var3);
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            String var8 = var5[var7];
            String[] var9 = ACFPatterns.COLON.split(var8, 2);
            Class var11 = var1.getParam().getType();
            String var12 = var9[0].toLowerCase(Locale.ENGLISH);

            CommandConditions.ParameterCondition var10;
            do {
               var10 = (CommandConditions.ParameterCondition)this.paramConditions.get(var11, var12);
               if (var10 != null || var11.getSuperclass() == null || var11.getSuperclass() == Object.class) {
                  break;
               }

               var11 = var11.getSuperclass();
            } while(var11 != null);

            if (var10 == null) {
               RegisteredCommand var13 = var1.getCmd();
               this.manager.log(LogLevel.ERROR, "Could not find command condition " + var12 + " for " + var13.method.getName() + "::" + var1.getParam().getName());
            } else {
               String var15 = var9.length == 2 ? var9[1] : null;
               ConditionContext var14 = this.manager.createConditionContext(var4, var15);
               var10.validateCondition(var14, var1, var2);
            }
         }

      }
   }

   public interface Condition<I extends CommandIssuer> {
      void validateCondition(ConditionContext<I> context) throws InvalidCommandArgument;
   }

   public interface ParameterCondition<P, CEC extends CommandExecutionContext, I extends CommandIssuer> {
      void validateCondition(ConditionContext<I> context, CEC execContext, P value) throws InvalidCommandArgument;
   }
}
