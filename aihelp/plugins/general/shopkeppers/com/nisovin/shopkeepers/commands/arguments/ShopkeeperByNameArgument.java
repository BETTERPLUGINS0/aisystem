package com.nisovin.shopkeepers.commands.arguments;

import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentParseException;
import com.nisovin.shopkeepers.commands.lib.argument.ambiguity.AmbiguousInputHandler;
import com.nisovin.shopkeepers.commands.lib.argument.filter.ArgumentFilter;
import com.nisovin.shopkeepers.commands.lib.argument.filter.ArgumentRejectedException;
import com.nisovin.shopkeepers.commands.lib.arguments.ObjectByIdArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.ObjectIdArgument;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.commands.util.ShopkeeperArgumentUtils;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.java.CollectionUtils;
import java.util.stream.Stream;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ShopkeeperByNameArgument extends ObjectByIdArgument<String, Shopkeeper> {
   public ShopkeeperByNameArgument(String name) {
      this(name, ArgumentFilter.acceptAny());
   }

   public ShopkeeperByNameArgument(String name, ArgumentFilter<? super Shopkeeper> filter) {
      this(name, false, filter, 0);
   }

   public ShopkeeperByNameArgument(String name, boolean joinRemainingArgs, ArgumentFilter<? super Shopkeeper> filter, int minimumCompletionInput) {
      super(name, filter, new ObjectByIdArgument.IdArgumentArgs(minimumCompletionInput, joinRemainingArgs));
   }

   protected ObjectIdArgument<String> createIdArgument(String name, ObjectByIdArgument.IdArgumentArgs args) {
      return new ShopkeeperNameArgument(name, args.joinRemainingArgs, ArgumentFilter.acceptAny(), args.minimumCompletionInput) {
         protected Iterable<? extends String> getCompletionSuggestions(CommandInput input, CommandContextView context, String idPrefix) {
            return ShopkeeperByNameArgument.this.getCompletionSuggestions(input, context, this.minimumCompletionInput, idPrefix);
         }
      };
   }

   protected Text getInvalidArgumentErrorMsgText() {
      return Messages.commandShopkeeperArgumentInvalid;
   }

   protected AmbiguousInputHandler<Shopkeeper> getAmbiguousShopkeeperNameHandler(String argumentInput, Iterable<? extends Shopkeeper> matchedShopkeepers) {
      AmbiguousShopkeeperNameHandler ambiguousShopkeeperNameHandler = new AmbiguousShopkeeperNameHandler(argumentInput, matchedShopkeepers);
      if (ambiguousShopkeeperNameHandler.isInputAmbiguous()) {
         Text errorMsg = ambiguousShopkeeperNameHandler.getErrorMsg();

         assert errorMsg != null;

         errorMsg.setPlaceholderArguments(this.getDefaultErrorMsgArgs());
         errorMsg.setPlaceholderArguments("argument", argumentInput);
      }

      return ambiguousShopkeeperNameHandler;
   }

   @Nullable
   public final Shopkeeper getDefaultShopkeeperByName(String nameInput) throws ArgumentRejectedException {
      Stream<? extends Shopkeeper> shopkeepers = ShopkeeperArgumentUtils.ShopkeeperNameMatchers.DEFAULT.match(nameInput);
      AmbiguousInputHandler<Shopkeeper> ambiguousShopkeeperNameHandler = this.getAmbiguousShopkeeperNameHandler(nameInput, CollectionUtils.toIterable(shopkeepers));
      if (ambiguousShopkeeperNameHandler.isInputAmbiguous()) {
         Text errorMsg = ambiguousShopkeeperNameHandler.getErrorMsg();

         assert errorMsg != null;

         throw new ArgumentRejectedException(this, errorMsg);
      } else {
         return (Shopkeeper)ambiguousShopkeeperNameHandler.getFirstMatch();
      }
   }

   @Nullable
   protected Shopkeeper getObject(CommandInput input, CommandContextView context, String nameInput) throws ArgumentParseException {
      return this.getDefaultShopkeeperByName(nameInput);
   }

   protected Iterable<? extends String> getCompletionSuggestions(CommandInput input, CommandContextView context, int minimumCompletionInput, String idPrefix) {
      return ShopkeeperNameArgument.getDefaultCompletionSuggestions(input, context, minimumCompletionInput, idPrefix, this.filter);
   }
}
