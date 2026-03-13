package com.nisovin.shopkeepers.commands.arguments;

import com.nisovin.shopkeepers.api.ShopkeepersAPI;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.filter.ArgumentFilter;
import com.nisovin.shopkeepers.commands.lib.arguments.ObjectUUIDArgument;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.commands.util.ShopkeeperArgumentUtils;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;
import org.bukkit.command.CommandSender;

public class ShopkeeperUUIDArgument extends ObjectUUIDArgument {
   public static final int DEFAULT_MINIMUM_COMPLETION_INPUT = 3;

   public ShopkeeperUUIDArgument(String name) {
      this(name, ArgumentFilter.acceptAny());
   }

   public ShopkeeperUUIDArgument(String name, ArgumentFilter<? super UUID> filter) {
      this(name, filter, 3);
   }

   public ShopkeeperUUIDArgument(String name, ArgumentFilter<? super UUID> filter, int minimumCompletionInput) {
      super(name, filter, minimumCompletionInput);
   }

   public static Iterable<? extends UUID> getDefaultCompletionSuggestions(CommandInput input, CommandContextView context, int minimumCompletionInput, String uuidPrefix, ArgumentFilter<? super Shopkeeper> filter) {
      String normalizedUUIDPrefix = uuidPrefix.toLowerCase(Locale.ROOT);
      CommandSender sender = input.getSender();
      List<? extends Shopkeeper> targetedShopkeepers = ShopkeeperArgumentUtils.getTargetedShopkeepers(sender, ShopkeeperArgumentUtils.TargetShopkeeperFilter.ANY);
      Stream shopkeepersStream;
      if (uuidPrefix.length() >= minimumCompletionInput) {
         shopkeepersStream = Stream.concat(targetedShopkeepers.stream(), ShopkeepersAPI.getShopkeeperRegistry().getAllShopkeepers().stream().filter((shopkeeper) -> {
            return !targetedShopkeepers.contains(shopkeeper);
         }));
      } else {
         shopkeepersStream = (Stream)Unsafe.castNonNull(targetedShopkeepers.stream());
      }

      Stream var10000 = shopkeepersStream.filter((shopkeeper) -> {
         return filter.test(input, context, shopkeeper);
      }).map(Shopkeeper::getUniqueId).filter((uuid) -> {
         return uuid.toString().startsWith(normalizedUUIDPrefix);
      });
      Objects.requireNonNull(var10000);
      return var10000::iterator;
   }

   protected Iterable<? extends UUID> getCompletionSuggestions(CommandInput input, CommandContextView context, String idPrefix) {
      return getDefaultCompletionSuggestions(input, context, this.minimumCompletionInput, idPrefix, ArgumentFilter.acceptAny());
   }
}
