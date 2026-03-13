package com.nisovin.shopkeepers.commands.arguments.snapshot;

import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentParseException;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentsReader;
import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.TypedFirstOfArgument;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import java.util.Arrays;
import java.util.List;

public class ShopkeeperSnapshotIndexArgument extends CommandArgument<Integer> {
   private final ShopkeeperSnapshotIndexByIdArgument snapshotIdArgument;
   private final ShopkeeperSnapshotIndexByNameArgument snapshotNameArgument;
   private final TypedFirstOfArgument<Integer> firstOfArgument;
   private final boolean inflateFormat;

   public ShopkeeperSnapshotIndexArgument(String name, CommandArgument<? extends Shopkeeper> shopkeeperArgument) {
      this(name, shopkeeperArgument, false);
   }

   public ShopkeeperSnapshotIndexArgument(String name, CommandArgument<? extends Shopkeeper> shopkeeperArgument, boolean joinRemainingArgs) {
      this(name, shopkeeperArgument, joinRemainingArgs, false);
   }

   public ShopkeeperSnapshotIndexArgument(String name, CommandArgument<? extends Shopkeeper> shopkeeperArgument, boolean joinRemainingArgs, boolean inflateFormat) {
      this(name, shopkeeperArgument, joinRemainingArgs, inflateFormat, 1, 0);
   }

   public ShopkeeperSnapshotIndexArgument(String name, CommandArgument<? extends Shopkeeper> shopkeeperArgument, boolean joinRemainingArgs, boolean inflateFormat, int minimumIdCompletionInput, int minimalNameCompletionInput) {
      super(name);
      this.inflateFormat = inflateFormat;
      this.snapshotIdArgument = new ShopkeeperSnapshotIndexByIdArgument(name + "-id", shopkeeperArgument, minimumIdCompletionInput);
      this.snapshotNameArgument = new ShopkeeperSnapshotIndexByNameArgument(name + "-name", shopkeeperArgument, joinRemainingArgs, minimalNameCompletionInput);
      this.firstOfArgument = new TypedFirstOfArgument(name + ":firstOf", Arrays.asList(this.snapshotIdArgument, this.snapshotNameArgument), true, false);
      this.firstOfArgument.setParent(this);
   }

   public String getReducedFormat() {
      return this.inflateFormat ? this.firstOfArgument.getReducedFormat() : super.getReducedFormat();
   }

   public Integer parseValue(CommandInput input, CommandContextView context, ArgumentsReader argsReader) throws ArgumentParseException {
      return (Integer)this.firstOfArgument.parseValue(input, context, argsReader);
   }

   public List<? extends String> complete(CommandInput input, CommandContextView context, ArgumentsReader argsReader) {
      return this.firstOfArgument.complete(input, context, argsReader);
   }
}
