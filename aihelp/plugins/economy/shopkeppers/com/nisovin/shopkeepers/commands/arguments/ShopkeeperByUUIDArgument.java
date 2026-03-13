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
import java.util.UUID;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ShopkeeperByUUIDArgument extends ObjectByIdArgument<UUID, Shopkeeper> {
   public ShopkeeperByUUIDArgument(String name) {
      this(name, ArgumentFilter.acceptAny());
   }

   public ShopkeeperByUUIDArgument(String name, ArgumentFilter<? super Shopkeeper> filter) {
      this(name, filter, 3);
   }

   public ShopkeeperByUUIDArgument(String name, ArgumentFilter<? super Shopkeeper> filter, int minimumCompletionInput) {
      super(name, filter, new ObjectByIdArgument.IdArgumentArgs(minimumCompletionInput));
   }

   protected ObjectIdArgument<UUID> createIdArgument(String name, ObjectByIdArgument.IdArgumentArgs args) {
      return new ShopkeeperUUIDArgument(name, ArgumentFilter.acceptAny(), args.minimumCompletionInput) {
         protected Iterable<? extends UUID> getCompletionSuggestions(CommandInput input, CommandContextView context, String idPrefix) {
            return ShopkeeperByUUIDArgument.this.getCompletionSuggestions(input, context, this.minimumCompletionInput, idPrefix);
         }
      };
   }

   protected Text getInvalidArgumentErrorMsgText() {
      return Messages.commandShopkeeperArgumentInvalid;
   }

   @Nullable
   protected Shopkeeper getObject(CommandInput input, CommandContextView context, UUID uuid) throws ArgumentParseException {
      return ShopkeepersAPI.getShopkeeperRegistry().getShopkeeperByUniqueId(uuid);
   }

   protected Iterable<? extends UUID> getCompletionSuggestions(CommandInput input, CommandContextView context, int minimumCompletionInput, String idPrefix) {
      return ShopkeeperUUIDArgument.getDefaultCompletionSuggestions(input, context, minimumCompletionInput, idPrefix, this.filter);
   }
}
