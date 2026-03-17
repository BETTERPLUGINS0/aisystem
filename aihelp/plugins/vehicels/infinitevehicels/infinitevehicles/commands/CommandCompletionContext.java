package me.PM2.infinitevehicles.commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CommandCompletionContext<I extends CommandIssuer> {
   private final RegisteredCommand command;
   protected final I issuer;
   private final String input;
   private final String config;
   private final Map<String, String> configs = new HashMap();
   private final List<String> args;

   CommandCompletionContext(RegisteredCommand command, I issuer, String input, String config, String[] args) {
      this.command = var1;
      this.issuer = var2;
      this.input = var3;
      if (var4 != null) {
         String[] var6 = ACFPatterns.COMMA.split(var4);
         String[] var7 = var6;
         int var8 = var6.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            String var10 = var7[var9];
            String[] var11 = ACFPatterns.EQUALS.split(var10, 2);
            this.configs.put(var11[0].toLowerCase(Locale.ENGLISH), var11.length > 1 ? var11[1] : null);
         }

         this.config = var6[0];
      } else {
         this.config = null;
      }

      this.args = Arrays.asList(var5);
   }

   public Map<String, String> getConfigs() {
      return this.configs;
   }

   public String getConfig(String key) {
      return this.getConfig(var1, (String)null);
   }

   public String getConfig(String key, String def) {
      return (String)this.configs.getOrDefault(var1.toLowerCase(Locale.ENGLISH), var2);
   }

   public boolean hasConfig(String key) {
      return this.configs.containsKey(var1.toLowerCase(Locale.ENGLISH));
   }

   public <T> T getContextValue(Class<? extends T> clazz) {
      return this.getContextValue(var1, (Integer)null);
   }

   public <T> T getContextValue(Class<? extends T> clazz, Integer paramIdx) {
      String var3 = null;
      if (var2 != null) {
         if (var2 >= this.command.parameters.length) {
            throw new IllegalArgumentException("Param index is higher than number of parameters");
         }

         CommandParameter var4 = this.command.parameters[var2];
         Class var5 = var4.getType();
         if (!var1.isAssignableFrom(var5)) {
            throw new IllegalArgumentException(var4.getName() + ":" + var5.getName() + " can not satisfy " + var1.getName());
         }

         var3 = var4.getName();
      } else {
         CommandParameter[] var7 = this.command.parameters;

         for(int var8 = 0; var8 < var7.length; ++var8) {
            CommandParameter var6 = var7[var8];
            if (var1.isAssignableFrom(var6.getType())) {
               var2 = var8;
               var3 = var6.getName();
               break;
            }
         }

         if (var2 == null) {
            throw new IllegalStateException("Can not find any parameter that can satisfy " + var1.getName());
         }
      }

      return this.getContextValueByName(var1, var3);
   }

   public <T> T getContextValueByName(Class<? extends T> clazz, String name) {
      Map var3 = this.command.resolveContexts(this.issuer, this.args, var2);
      if (var3 == null || !var3.containsKey(var2)) {
         ACFUtil.sneaky(new CommandCompletionTextLookupException());
      }

      return var3.get(var2);
   }

   public CommandIssuer getIssuer() {
      return this.issuer;
   }

   public String getInput() {
      return this.input;
   }

   public String getConfig() {
      return this.config;
   }

   public boolean isAsync() {
      return CommandManager.getCurrentCommandOperationContext().isAsync();
   }
}
