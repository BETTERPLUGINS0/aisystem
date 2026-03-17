package com.bergerkiller.bukkit.tc.statements;

import com.bergerkiller.bukkit.common.conversion.Conversion;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import java.util.Locale;
import org.bukkit.entity.EntityType;

public class StatementType extends Statement {
   private boolean isSize(String text) {
      int index = Util.getOperatorIndex(text);
      if (index != -1) {
         text = text.substring(0, index);
      }

      return LogicUtil.contains(text, new String[]{"cartcount", "trainsize", "length", "count", "size"});
   }

   public boolean match(String text) {
      return this.isSize(text) || Conversion.toMinecartType.convert(text) != null;
   }

   public boolean handle(MinecartMember<?> member, String text, SignActionEvent event) {
      return this.isSize(text.toLowerCase(Locale.ENGLISH)) || ((CommonMinecart)member.getEntity()).getType() == Conversion.toMinecartType.convert(text);
   }

   public boolean handle(MinecartGroup group, String text, SignActionEvent event) {
      if (this.isSize(text.toLowerCase(Locale.ENGLISH))) {
         return Util.evaluate((double)group.size(), text);
      } else {
         EntityType type = (EntityType)Conversion.toMinecartType.convert(text);
         return type != null && Util.evaluate((double)group.size(type), text);
      }
   }

   public boolean matchArray(String text) {
      return false;
   }
}
