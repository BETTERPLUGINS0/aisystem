package com.nisovin.shopkeepers.commands.arguments;

import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentParseException;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentsReader;
import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.commands.lib.argument.filter.ArgumentFilter;
import com.nisovin.shopkeepers.commands.lib.argument.filter.ArgumentRejectedException;
import com.nisovin.shopkeepers.commands.lib.arguments.TypedFirstOfArgument;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import java.util.Arrays;
import java.util.List;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ShopkeeperArgument extends CommandArgument<Shopkeeper> {
   private final ShopkeeperByUUIDArgument shopUUIDArgument;
   private final ShopkeeperByIdArgument shopIdArgument;
   private final ShopkeeperByNameArgument shopNameArgument;
   private final TypedFirstOfArgument<Shopkeeper> firstOfArgument;

   public ShopkeeperArgument(String name) {
      this(name, ArgumentFilter.acceptAny());
   }

   public ShopkeeperArgument(String name, boolean joinRemainingArgs) {
      this(name, joinRemainingArgs, ArgumentFilter.acceptAny());
   }

   public ShopkeeperArgument(String name, ArgumentFilter<? super Shopkeeper> filter) {
      this(name, false, filter);
   }

   public ShopkeeperArgument(String name, boolean joinRemainingArgs, ArgumentFilter<? super Shopkeeper> filter) {
      this(name, joinRemainingArgs, filter, 0, 3);
   }

   public ShopkeeperArgument(String name, boolean joinRemainingArgs, ArgumentFilter<? super Shopkeeper> filter, int minimumNameCompletionInput, int minimumUUIDCompletionInput) {
      super(name);
      this.shopUUIDArgument = new ShopkeeperByUUIDArgument(name + ":uuid", filter, minimumUUIDCompletionInput);
      this.shopIdArgument = new ShopkeeperByIdArgument(name + ":id", filter);
      this.shopNameArgument = new ShopkeeperByNameArgument(name + ":name", joinRemainingArgs, filter, minimumNameCompletionInput) {
         @Nullable
         protected Shopkeeper getObject(CommandInput input, CommandContextView context, String nameInput) throws ArgumentParseException {
            return ShopkeeperArgument.this.getShopkeeper(nameInput);
         }
      };
      this.firstOfArgument = new TypedFirstOfArgument(name + ":firstOf", Arrays.asList(this.shopUUIDArgument, this.shopIdArgument, this.shopNameArgument), false, false);
      this.firstOfArgument.setParent(this);
   }

   @Nullable
   public Shopkeeper getShopkeeper(String nameInput) throws ArgumentRejectedException {
      return this.shopNameArgument.getDefaultShopkeeperByName(nameInput);
   }

   public Shopkeeper parseValue(CommandInput input, CommandContextView context, ArgumentsReader argsReader) throws ArgumentParseException {
      return (Shopkeeper)this.firstOfArgument.parseValue(input, context, argsReader);
   }

   public List<? extends String> complete(CommandInput input, CommandContextView context, ArgumentsReader argsReader) {
      return this.firstOfArgument.complete(input, context, argsReader);
   }
}
