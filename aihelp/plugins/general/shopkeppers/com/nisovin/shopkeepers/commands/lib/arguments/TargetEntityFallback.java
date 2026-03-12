package com.nisovin.shopkeepers.commands.lib.arguments;

import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.entity.Entity;

public class TargetEntityFallback extends TypedFallbackArgument<Entity> {
   public TargetEntityFallback(CommandArgument<Entity> argument) {
      this(argument, TargetEntityArgument.TargetEntityFilter.ANY);
   }

   public TargetEntityFallback(CommandArgument<Entity> argument, TargetEntityArgument.TargetEntityFilter filter) {
      super((CommandArgument)Validate.notNull(argument, (String)"argument is null"), new TargetEntityArgument(argument.getName(), filter));
   }
}
