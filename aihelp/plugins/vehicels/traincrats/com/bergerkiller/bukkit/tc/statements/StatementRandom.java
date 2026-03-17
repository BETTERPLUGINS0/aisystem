package com.bergerkiller.bukkit.tc.statements;

import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;

public class StatementRandom extends Statement {
   public boolean match(String text) {
      return text.startsWith("rand");
   }

   public boolean matchArray(String text) {
      return text.startsWith("rand");
   }

   public boolean requiresTrain() {
      return false;
   }

   private boolean handle(String... text) {
      double chance = 0.5D;
      if (text.length > 0) {
         chance = ParseUtil.parseDouble(text[0], chance);
         if (text[0].endsWith("%")) {
            chance /= 100.0D;
         }

         chance = MathUtil.clamp(chance, 0.0D, 1.0D);
      }

      return Math.random() < chance;
   }

   public boolean handle(MinecartGroup group, String text, SignActionEvent event) {
      return this.handle();
   }

   public boolean handle(MinecartMember<?> member, String text, SignActionEvent event) {
      return this.handle();
   }

   public boolean handleArray(MinecartGroup group, String[] text, SignActionEvent event) {
      return this.handle(text);
   }

   public boolean handleArray(MinecartMember<?> member, String[] text, SignActionEvent event) {
      return this.handle(text);
   }
}
