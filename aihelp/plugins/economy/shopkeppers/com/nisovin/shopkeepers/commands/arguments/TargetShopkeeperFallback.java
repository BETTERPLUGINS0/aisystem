package com.nisovin.shopkeepers.commands.arguments;

import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.TypedFallbackArgument;
import com.nisovin.shopkeepers.commands.util.ShopkeeperArgumentUtils;
import com.nisovin.shopkeepers.util.java.Validate;

public class TargetShopkeeperFallback extends TypedFallbackArgument<Shopkeeper> {
   public TargetShopkeeperFallback(CommandArgument<Shopkeeper> argument) {
      this(argument, ShopkeeperArgumentUtils.TargetShopkeeperFilter.ANY);
   }

   public TargetShopkeeperFallback(CommandArgument<Shopkeeper> argument, ShopkeeperArgumentUtils.TargetShopkeeperFilter filter) {
      super((CommandArgument)Validate.notNull(argument, (String)"argument is null"), new TargetShopkeeperArgument(argument.getName(), filter));
   }
}
