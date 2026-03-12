package com.nisovin.shopkeepers.commands.arguments;

import com.nisovin.shopkeepers.api.ShopkeepersAPI;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentParseException;
import com.nisovin.shopkeepers.commands.lib.argument.filter.ArgumentFilter;
import com.nisovin.shopkeepers.commands.lib.arguments.ObjectByIdArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.ObjectIdArgument;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.text.Text;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ShopkeeperByIdArgument extends ObjectByIdArgument<Integer, Shopkeeper> {
   public ShopkeeperByIdArgument(String name) {
      this(name, ArgumentFilter.acceptAny());
   }

   public ShopkeeperByIdArgument(String name, ArgumentFilter<? super Shopkeeper> filter) {
      this(name, filter, 1);
   }

   public ShopkeeperByIdArgument(String name, ArgumentFilter<? super Shopkeeper> filter, int minimumCompletionInput) {
      super(name, filter, new ObjectByIdArgument.IdArgumentArgs(minimumCompletionInput));
   }

   protected ObjectIdArgument<Integer> createIdArgument(String name, ObjectByIdArgument.IdArgumentArgs args) {
      return new ShopkeeperIdArgument(name, ArgumentFilter.acceptAny(), args.minimumCompletionInput) {
         protected Iterable<? extends Integer> getCompletionSuggestions(CommandInput input, CommandContextView context, String idPrefix) {
            return ShopkeeperByIdArgument.this.getCompletionSuggestions(input, context, this.minimumCompletionInput, idPrefix);
         }
      };
   }

   protected Text getInvalidArgumentErrorMsgText() {
      return Messages.commandShopkeeperArgumentInvalid;
   }

   @Nullable
   protected Shopkeeper getObject(CommandInput input, CommandContextView context, Integer id) throws ArgumentParseException {
      return ShopkeepersAPI.getShopkeeperRegistry().getShopkeeperById(id);
   }

   protected Iterable<? extends Integer> getCompletionSuggestions(CommandInput input, CommandContextView context, int minimumCompletionInput, String idPrefix) {
      return ShopkeeperIdArgument.getDefaultCompletionSuggestions(input, context, minimumCompletionInput, idPrefix, this.filter);
   }
}
