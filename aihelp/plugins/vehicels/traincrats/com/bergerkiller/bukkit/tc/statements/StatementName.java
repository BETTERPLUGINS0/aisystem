package com.bergerkiller.bukkit.tc.statements;

import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import java.util.Collection;
import java.util.Collections;

public class StatementName extends Statement {
   public boolean match(String text) {
      return LogicUtil.contains(text, new String[]{"renamed", "rename", "ren", "name", "named"});
   }

   public boolean matchArray(String text) {
      return text.equals("name") || text.equals("n");
   }

   public boolean handle(MinecartGroup group, String text, SignActionEvent event) {
      return group.getProperties().isTrainRenamed();
   }

   public boolean handleArray(MinecartGroup group, String[] text, SignActionEvent event) {
      TrainProperties prop = group.getProperties();
      String[] var5 = text;
      int var6 = text.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         String name = var5[var7];
         if (Util.matchText((Collection)Collections.singletonList(prop.getTrainName()), name)) {
            return true;
         }
      }

      return false;
   }
}
