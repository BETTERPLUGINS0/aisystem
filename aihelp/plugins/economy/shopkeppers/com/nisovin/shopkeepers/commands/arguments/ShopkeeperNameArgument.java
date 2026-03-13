package com.nisovin.shopkeepers.commands.arguments;

import com.nisovin.shopkeepers.api.ShopkeepersAPI;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopkeeper.ShopkeeperRegistry;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.filter.ArgumentFilter;
import com.nisovin.shopkeepers.commands.lib.arguments.ObjectNameArgument;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.java.StringUtils;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;

public class ShopkeeperNameArgument extends ObjectNameArgument {
   public static final int DEFAULT_MINIMUM_COMPLETION_INPUT = 0;

   public ShopkeeperNameArgument(String name) {
      this(name, ArgumentFilter.acceptAny());
   }

   public ShopkeeperNameArgument(String name, ArgumentFilter<? super String> filter) {
      this(name, false, filter, 0);
   }

   public ShopkeeperNameArgument(String name, boolean joinRemainingArgs, ArgumentFilter<? super String> filter, int minimumCompletionInput) {
      super(name, joinRemainingArgs, filter, minimumCompletionInput);
   }

   public static Iterable<? extends String> getDefaultCompletionSuggestions(CommandInput input, CommandContextView context, int minimumCompletionInput, String namePrefix, ArgumentFilter<? super Shopkeeper> shopkeeperFilter) {
      if (namePrefix.length() < minimumCompletionInput) {
         return Collections.emptyList();
      } else {
         String normalizedNamePrefix = StringUtils.normalize(TextUtils.stripColor(namePrefix));
         ShopkeeperRegistry shopkeeperRegistry = ShopkeepersAPI.getShopkeeperRegistry();
         Stream<Shopkeeper> shopkeepers = (Stream)Unsafe.castNonNull(shopkeeperRegistry.getAllShopkeepers().stream());
         Stream var10000 = shopkeepers.filter((shopkeeper) -> {
            return shopkeeperFilter.test(input, context, shopkeeper);
         }).map((shopkeeper) -> {
            String name = TextUtils.stripColor(shopkeeper.getName());
            if (name.isEmpty()) {
               return null;
            } else {
               String normalizedWithCase = StringUtils.normalizeKeepCase(name);
               String normalized = normalizedWithCase.toLowerCase(Locale.ROOT);
               return normalized.startsWith(normalizedNamePrefix) ? normalizedWithCase : null;
            }
         }).filter(Objects::nonNull).map(Unsafe::assertNonNull);
         Objects.requireNonNull(var10000);
         Iterable<String> suggestions = var10000::iterator;
         return suggestions;
      }
   }

   protected Iterable<? extends String> getCompletionSuggestions(CommandInput input, CommandContextView context, String idPrefix) {
      return getDefaultCompletionSuggestions(input, context, this.minimumCompletionInput, idPrefix, ArgumentFilter.acceptAny());
   }
}
