package com.nisovin.shopkeepers.commands.arguments;

import com.nisovin.shopkeepers.api.ShopkeepersAPI;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.filter.ArgumentFilter;
import com.nisovin.shopkeepers.commands.lib.arguments.IntegerArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.ObjectIdArgument;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.commands.util.ShopkeeperArgumentUtils;
import com.nisovin.shopkeepers.util.java.ConversionUtils;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.bukkit.command.CommandSender;

public class ShopkeeperIdArgument extends ObjectIdArgument<Integer> {
   public static final int DEFAULT_MINIMUM_COMPLETION_INPUT = 1;

   public ShopkeeperIdArgument(String name) {
      this(name, ArgumentFilter.acceptAny());
   }

   public ShopkeeperIdArgument(String name, ArgumentFilter<? super Integer> filter) {
      this(name, filter, 1);
   }

   public ShopkeeperIdArgument(String name, ArgumentFilter<? super Integer> filter, int minimumCompletionInput) {
      super(name, new IntegerArgument(name + ":id"), filter, minimumCompletionInput);
   }

   protected String toString(Integer id) {
      return id.toString();
   }

   public static Iterable<? extends Integer> getDefaultCompletionSuggestions(CommandInput input, CommandContextView context, int minimumCompletionInput, String idPrefix, ArgumentFilter<? super Shopkeeper> filter) {
      if (!idPrefix.isEmpty() && ConversionUtils.parseInt(idPrefix) == null) {
         return Collections.emptyList();
      } else {
         CommandSender sender = input.getSender();
         List<? extends Shopkeeper> targetedShopkeepers = ShopkeeperArgumentUtils.getTargetedShopkeepers(sender, ShopkeeperArgumentUtils.TargetShopkeeperFilter.ANY);
         Stream shopkeepersStream;
         if (idPrefix.length() >= minimumCompletionInput) {
            shopkeepersStream = Stream.concat(targetedShopkeepers.stream(), ShopkeepersAPI.getShopkeeperRegistry().getAllShopkeepers().stream().filter((shopkeeper) -> {
               return !targetedShopkeepers.contains(shopkeeper);
            }));
         } else {
            shopkeepersStream = (Stream)Unsafe.castNonNull(targetedShopkeepers.stream());
         }

         IntStream var10000 = shopkeepersStream.filter((shopkeeper) -> {
            return filter.test(input, context, shopkeeper);
         }).mapToInt(Shopkeeper::getId).filter((id) -> {
            return String.valueOf(id).startsWith(idPrefix);
         });
         Objects.requireNonNull(var10000);
         return var10000::iterator;
      }
   }

   protected Iterable<? extends Integer> getCompletionSuggestions(CommandInput input, CommandContextView context, String idPrefix) {
      return getDefaultCompletionSuggestions(input, context, this.minimumCompletionInput, idPrefix, ArgumentFilter.acceptAny());
   }
}
