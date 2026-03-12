package com.nisovin.shopkeepers.commands.arguments;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentParseException;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentsReader;
import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.commands.lib.argument.ambiguity.AmbiguousInputHandler;
import com.nisovin.shopkeepers.commands.lib.argument.filter.ArgumentRejectedException;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.commands.util.ShopkeeperArgumentUtils;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.java.ObjectUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Collections;
import java.util.List;
import org.bukkit.entity.Player;

public class TargetShopkeeperArgument extends CommandArgument<Shopkeeper> {
   private final ShopkeeperArgumentUtils.TargetShopkeeperFilter filter;

   public TargetShopkeeperArgument(String name) {
      this(name, ShopkeeperArgumentUtils.TargetShopkeeperFilter.ANY);
   }

   public TargetShopkeeperArgument(String name, ShopkeeperArgumentUtils.TargetShopkeeperFilter filter) {
      super(name);
      Validate.notNull(filter, (String)"filter is null");
      this.filter = filter;
   }

   public boolean isOptional() {
      return true;
   }

   protected AmbiguousInputHandler<Shopkeeper> getAmbiguousTargetShopkeeperHandler(Iterable<? extends Shopkeeper> matchedShopkeepers) {
      AmbiguousTargetShopkeeperHandler ambiguousTargetShopkeeperHandler = new AmbiguousTargetShopkeeperHandler(matchedShopkeepers);
      if (ambiguousTargetShopkeeperHandler.isInputAmbiguous()) {
         Text errorMsg = ambiguousTargetShopkeeperHandler.getErrorMsg();

         assert errorMsg != null;

         errorMsg.setPlaceholderArguments(this.getDefaultErrorMsgArgs());
      }

      return ambiguousTargetShopkeeperHandler;
   }

   public Shopkeeper parseValue(CommandInput input, CommandContextView context, ArgumentsReader argsReader) throws ArgumentParseException {
      Player player = (Player)ObjectUtils.castOrNull(input.getSender(), Player.class);
      if (player == null) {
         throw this.requiresPlayerError();
      } else {
         ShopkeeperArgumentUtils.TargetShopkeepersResult result = ShopkeeperArgumentUtils.findTargetedShopkeepers(player, this.filter);
         if (!result.isSuccess()) {
            Text error = (Text)Unsafe.assertNonNull(result.getErrorMessage());
            throw new ArgumentParseException(this, error);
         } else {
            assert !result.getShopkeepers().isEmpty();

            AmbiguousInputHandler<Shopkeeper> ambiguousTargetShopkeeperHandler = this.getAmbiguousTargetShopkeeperHandler(result.getShopkeepers());
            if (ambiguousTargetShopkeeperHandler.isInputAmbiguous()) {
               Text errorMsg = ambiguousTargetShopkeeperHandler.getErrorMsg();

               assert errorMsg != null;

               throw new ArgumentRejectedException(this, errorMsg);
            } else {
               Shopkeeper shopkeeper = (Shopkeeper)ambiguousTargetShopkeeperHandler.getFirstMatch();

               assert shopkeeper != null;

               return shopkeeper;
            }
         }
      }
   }

   public List<? extends String> complete(CommandInput input, CommandContextView context, ArgumentsReader argsReader) {
      return Collections.emptyList();
   }
}
