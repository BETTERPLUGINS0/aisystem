package com.nisovin.shopkeepers.commands.arguments;

import com.nisovin.shopkeepers.api.ShopkeepersPlugin;
import com.nisovin.shopkeepers.api.shopobjects.ShopObjectType;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentParseException;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentsReader;
import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.java.ObjectUtils;
import com.nisovin.shopkeepers.util.java.StringUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.bukkit.entity.Player;

public class ShopObjectTypeArgument extends CommandArgument<ShopObjectType<?>> {
   public ShopObjectTypeArgument(String name) {
      super(name);
   }

   protected Text getInvalidArgumentErrorMsgText() {
      return Messages.commandShopObjectTypeArgumentInvalid;
   }

   public ShopObjectType<?> parseValue(CommandInput input, CommandContextView context, ArgumentsReader argsReader) throws ArgumentParseException {
      if (!argsReader.hasNext()) {
         throw this.missingArgumentError();
      } else {
         String argument = argsReader.next();
         ShopObjectType<?> value = (ShopObjectType)ShopkeepersPlugin.getInstance().getShopObjectTypeRegistry().match(argument);
         if (value == null) {
            throw this.invalidArgumentError(argument);
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
         Iterator var7 = ShopkeepersPlugin.getInstance().getShopObjectTypeRegistry().getRegisteredTypes().iterator();

         while(var7.hasNext()) {
            ShopObjectType<?> shopObjectType = (ShopObjectType)var7.next();
            if (suggestions.size() >= 20) {
               break;
            }

            if (shopObjectType.isEnabled() && (senderPlayer == null || shopObjectType.hasPermission(senderPlayer))) {
               String displayName = shopObjectType.getDisplayName();
               displayName = StringUtils.normalizeKeepCase(displayName);
               String displayNameNorm = displayName.toLowerCase(Locale.ROOT);
               if (displayNameNorm.startsWith(partialArg)) {
                  suggestions.add(displayName);
               } else {
                  String identifier = shopObjectType.getIdentifier();
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
