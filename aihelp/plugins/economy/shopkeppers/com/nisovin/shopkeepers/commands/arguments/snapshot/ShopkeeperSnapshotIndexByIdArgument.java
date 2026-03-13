package com.nisovin.shopkeepers.commands.arguments.snapshot;

import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopkeeper.ShopkeeperSnapshot;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentParseException;
import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.commands.lib.argument.filter.ArgumentFilter;
import com.nisovin.shopkeepers.commands.lib.arguments.ObjectByIdArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.ObjectIdArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.PositiveIntegerArgument;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.java.ConversionUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ShopkeeperSnapshotIndexByIdArgument extends ObjectByIdArgument<Integer, Integer> {
   public static final int DEFAULT_MINIMUM_COMPLETION_INPUT = 1;
   private final CommandArgument<? extends Shopkeeper> shopkeeperArgument;

   public ShopkeeperSnapshotIndexByIdArgument(String name, CommandArgument<? extends Shopkeeper> shopkeeperArgument) {
      this(name, shopkeeperArgument, 1);
   }

   public ShopkeeperSnapshotIndexByIdArgument(String name, CommandArgument<? extends Shopkeeper> shopkeeperArgument, int minimumCompletionInput) {
      super(name, ArgumentFilter.acceptAny(), new ObjectByIdArgument.IdArgumentArgs(minimumCompletionInput));
      Validate.notNull(shopkeeperArgument, (String)"shopkeeperArgument is null");
      this.shopkeeperArgument = shopkeeperArgument;
   }

   protected ObjectIdArgument<Integer> createIdArgument(String name, ObjectByIdArgument.IdArgumentArgs args) {
      return new ObjectIdArgument<Integer>(name, new PositiveIntegerArgument(name + ":id"), ArgumentFilter.acceptAny(), args.minimumCompletionInput) {
         protected Iterable<? extends Integer> getCompletionSuggestions(CommandInput input, CommandContextView context, String idPrefix) {
            return ShopkeeperSnapshotIndexByIdArgument.this.getCompletionSuggestions(input, context, this.minimumCompletionInput, idPrefix);
         }

         protected String toString(Integer id) {
            return id.toString();
         }
      };
   }

   @Nullable
   private Shopkeeper getShopkeeperScope(CommandInput input, CommandContextView context) {
      Object shopkeeper = context.getOrNull(this.shopkeeperArgument.getName());
      return shopkeeper instanceof Shopkeeper ? (Shopkeeper)shopkeeper : null;
   }

   protected Text getInvalidArgumentErrorMsgText() {
      return Messages.invalidSnapshotId;
   }

   @Nullable
   protected Integer getObject(CommandInput input, CommandContextView context, Integer id) throws ArgumentParseException {
      assert id != null && id > 0;

      Shopkeeper shopkeeper = this.getShopkeeperScope(input, context);
      if (shopkeeper == null) {
         return null;
      } else {
         return id > shopkeeper.getSnapshots().size() ? null : id - 1;
      }
   }

   protected Iterable<? extends Integer> getCompletionSuggestions(CommandInput input, CommandContextView context, int minimumCompletionInput, String idPrefix) {
      if (idPrefix.length() < minimumCompletionInput) {
         return Collections.emptyList();
      } else if (!idPrefix.isEmpty() && ConversionUtils.parseInt(idPrefix) == null) {
         return Collections.emptyList();
      } else {
         Shopkeeper shopkeeper = this.getShopkeeperScope(input, context);
         if (shopkeeper == null) {
            return Collections.emptyList();
         } else {
            List<? extends ShopkeeperSnapshot> snapshots = shopkeeper.getSnapshots();
            IntStream var10000 = IntStream.rangeClosed(1, snapshots.size()).filter((id) -> {
               return String.valueOf(id).startsWith(idPrefix);
            });
            Objects.requireNonNull(var10000);
            return var10000::iterator;
         }
      }
   }
}
