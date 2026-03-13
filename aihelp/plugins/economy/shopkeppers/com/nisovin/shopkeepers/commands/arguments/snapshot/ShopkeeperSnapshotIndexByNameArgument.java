package com.nisovin.shopkeepers.commands.arguments.snapshot;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentParseException;
import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.commands.lib.argument.filter.ArgumentFilter;
import com.nisovin.shopkeepers.commands.lib.arguments.ObjectByIdArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.ObjectIdArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.ObjectNameArgument;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.java.StringUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ShopkeeperSnapshotIndexByNameArgument extends ObjectByIdArgument<String, Integer> {
   public static final int DEFAULT_MINIMUM_COMPLETION_INPUT = 0;
   private final CommandArgument<? extends Shopkeeper> shopkeeperArgument;

   public ShopkeeperSnapshotIndexByNameArgument(String name, CommandArgument<? extends Shopkeeper> shopkeeperArgument) {
      this(name, shopkeeperArgument, false, 0);
   }

   public ShopkeeperSnapshotIndexByNameArgument(String name, CommandArgument<? extends Shopkeeper> shopkeeperArgument, boolean joinRemainingArgs, int minimumCompletionInput) {
      super(name, ArgumentFilter.acceptAny(), new ObjectByIdArgument.IdArgumentArgs(minimumCompletionInput, joinRemainingArgs));
      Validate.notNull(shopkeeperArgument, (String)"shopkeeperArgument is null");
      this.shopkeeperArgument = shopkeeperArgument;
   }

   protected ObjectIdArgument<String> createIdArgument(String name, ObjectByIdArgument.IdArgumentArgs args) {
      return new ObjectNameArgument(name, args.joinRemainingArgs, ArgumentFilter.acceptAny(), args.minimumCompletionInput) {
         protected Iterable<? extends String> getCompletionSuggestions(CommandInput input, CommandContextView context, String idPrefix) {
            return ShopkeeperSnapshotIndexByNameArgument.this.getCompletionSuggestions(input, context, this.minimumCompletionInput, idPrefix);
         }
      };
   }

   @Nullable
   private Shopkeeper getShopkeeperScope(CommandInput input, CommandContextView context) {
      Object shopkeeper = context.getOrNull(this.shopkeeperArgument.getName());
      return shopkeeper instanceof Shopkeeper ? (Shopkeeper)shopkeeper : null;
   }

   protected Text getInvalidArgumentErrorMsgText() {
      return Messages.invalidSnapshotName;
   }

   @Nullable
   protected Integer getObject(CommandInput input, CommandContextView context, String id) throws ArgumentParseException {
      assert id != null;

      if (id.isEmpty()) {
         return null;
      } else {
         Shopkeeper shopkeeper = this.getShopkeeperScope(input, context);
         if (shopkeeper == null) {
            return null;
         } else {
            int snapshotIndex = shopkeeper.getSnapshotIndex(id);
            return snapshotIndex != -1 ? snapshotIndex : null;
         }
      }
   }

   protected Iterable<? extends String> getCompletionSuggestions(CommandInput input, CommandContextView context, int minimumCompletionInput, String idPrefix) {
      if (idPrefix.length() < minimumCompletionInput) {
         return Collections.emptyList();
      } else {
         Shopkeeper shopkeeper = this.getShopkeeperScope(input, context);
         if (shopkeeper == null) {
            return Collections.emptyList();
         } else {
            String normalizedNamePrefix = StringUtils.normalize(idPrefix);
            Stream var10000 = shopkeeper.getSnapshots().stream().map((snapshot) -> {
               String normalizedWithCase = StringUtils.normalizeKeepCase(snapshot.getName());
               String normalized = normalizedWithCase.toLowerCase(Locale.ROOT);
               return normalized.startsWith(normalizedNamePrefix) ? normalizedWithCase : null;
            }).filter(Objects::nonNull).map(Unsafe::assertNonNull);
            Objects.requireNonNull(var10000);
            Iterable<String> suggestions = var10000::iterator;
            return suggestions;
         }
      }
   }
}
