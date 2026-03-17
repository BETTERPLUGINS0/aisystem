package me.PM2.infinitevehicles.commands;

import java.util.HashMap;
import java.util.Map;

public class ConditionContext<I extends CommandIssuer> {
   private final I issuer;
   private final String config;
   private final Map<String, String> configs;

   ConditionContext(I issuer, String config) {
      this.issuer = var1;
      this.config = var2;
      this.configs = new HashMap();
      if (var2 != null) {
         String[] var3 = ACFPatterns.COMMA.split(var2);
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            String[] var7 = ACFPatterns.EQUALS.split(var6, 2);
            this.configs.put(var7[0], var7.length > 1 ? var7[1] : null);
         }
      }

   }

   public I getIssuer() {
      return this.issuer;
   }

   public String getConfig() {
      return this.config;
   }

   public boolean hasConfig(String flag) {
      return this.configs.containsKey(var1);
   }

   public String getConfigValue(String flag, String def) {
      return (String)this.configs.getOrDefault(var1, var2);
   }

   public Integer getConfigValue(String flag, Integer def) {
      return ACFUtil.parseInt((String)this.configs.get(var1), var2);
   }
}
