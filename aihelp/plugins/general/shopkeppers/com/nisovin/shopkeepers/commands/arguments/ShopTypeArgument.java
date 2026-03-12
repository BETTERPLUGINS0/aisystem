package com.nisovin.shopkeepers.commands.arguments;

import com.nisovin.shopkeepers.api.ShopkeepersPlugin;
import com.nisovin.shopkeepers.api.shopkeeper.ShopType;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentParseException;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentsReader;
import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.commands.lib.argument.filter.ArgumentFilter;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.java.ObjectUtils;
import com.nisovin.shopkeepers.util.java.StringUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.bukkit.entity.Player;

public class ShopTypeArgument extends CommandArgument<ShopType<?>> {
   private final ArgumentFilter<? super ShopType<?>> filter;

   public ShopTypeArgument(String name) {
      this(name, ShopTypeFilter.ANY);
   }

   public ShopTypeArgument(String name, ArgumentFilter<? super ShopType<?>> filter) {
      super(name);
      Validate.notNull(filter, (String)"filter is null");
      this.filter = filter;
   }

   protected Text getInvalidArgumentErrorMsgText() {
      return Messages.commandShopTypeArgumentInvalid;
   }

   public ShopType<?> parseValue(CommandInput input, CommandContextView context, ArgumentsReader argsReader) throws ArgumentParseException {
      if (!argsReader.hasNext()) {
         throw this.missingArgumentError();
      } else {
         String argument = argsReader.next();
         ShopType<?> value = (ShopType)ShopkeepersPlugin.getInstance().getShopTypeRegistry().match(argument);
         if (value == null) {
            throw this.invalidArgumentError(argument);
         } else if (!this.filter.test(input, context, value)) {
            throw this.filter.rejectedArgumentException(this, argument, value);
         } else {
            return value;
         }
      }
   }

   public List<? extends String> complete(CommandInput input, CommandContextView context, ArgumentsReader argsReader) {
      if (argsReader.getRemainingSize() != 1) {
         return Collections.emptyList();
      } else {
         Player senderPlayer = (Player)ObjectUtils.castOrNull(input.getSender(), Player.class);
         List<String> suggestions = new ArrayList();
         String partialArg = StringUtils.normalize(argsReader.next());
         Iterator var7 = ShopkeepersPlugin.getInstance().getShopTypeRegistry().getRegisteredTypes().iterator();

         while(var7.hasNext()) {
            ShopType<?> shopType = (ShopType)var7.next();
            if (suggestions.size() >= 20) {
               break;
            }

            if (shopType.isEnabled() && this.filter.test(input, context, shopType) && (senderPlayer == null || shopType.hasPermission(senderPlayer))) {
               String displayName = shopType.getDisplayName();
               displayName = StringUtils.normalizeKeepCase(displayName);
               String displayNameNorm = displayName.toLowerCase(Locale.ROOT);
               if (displayNameNorm.startsWith(partialArg)) {
                  suggestions.add(displayName);
               } else {
                  String identifier = shopType.getIdentifier();
                  if (identifier.startsWith(partialArg)) {
                     suggestions.add(identifier);
                  }
               }
            }
         }

         return Collections.unmodifiableList(suggestions);
      }
   }
}
