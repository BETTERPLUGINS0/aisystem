package com.nisovin.shopkeepers.commands.shopkeepers.snapshot;

import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopkeeper.ShopkeeperSnapshot;
import com.nisovin.shopkeepers.api.ui.DefaultUITypes;
import com.nisovin.shopkeepers.commands.Confirmations;
import com.nisovin.shopkeepers.commands.arguments.ShopkeeperArgument;
import com.nisovin.shopkeepers.commands.arguments.ShopkeeperFilter;
import com.nisovin.shopkeepers.commands.arguments.TargetShopkeeperFallback;
import com.nisovin.shopkeepers.commands.arguments.snapshot.ShopkeeperSnapshotIndexArgument;
import com.nisovin.shopkeepers.commands.lib.Command;
import com.nisovin.shopkeepers.commands.lib.CommandException;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.FirstOfArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.LiteralArgument;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.commands.util.ShopkeeperArgumentUtils;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.CommandSender;

class CommandSnapshotRemove extends Command {
   private static final String ARGUMENT_SHOPKEEPER = "shopkeeper";
   private static final String ARGUMENT_SNAPSHOT = "snapshot";
   private static final String ARGUMENT_ALL = "all";
   private final Confirmations confirmations;

   CommandSnapshotRemove(Confirmations confirmations) {
      super("remove", Arrays.asList("delete"));
      Validate.notNull(confirmations, (String)"confirmations is null");
      this.confirmations = confirmations;
      this.setPermission("shopkeeper.snapshot");
      this.setDescription(Messages.commandDescriptionSnapshotRemove);
      CommandArgument<Shopkeeper> shopkeeperArgument = new TargetShopkeeperFallback(new ShopkeeperArgument("shopkeeper", ShopkeeperFilter.withAccess(DefaultUITypes.EDITOR())), ShopkeeperArgumentUtils.TargetShopkeeperFilter.ANY);
      this.addArgument(shopkeeperArgument);
      this.addArgument(new FirstOfArgument("snapshot:firstOf", Arrays.asList(new LiteralArgument("all"), new ShopkeeperSnapshotIndexArgument("snapshot", shopkeeperArgument))));
   }

   protected void execute(CommandInput input, CommandContextView context) throws CommandException {
      CommandSender sender = input.getSender();
      AbstractShopkeeper shopkeeper = (AbstractShopkeeper)context.get("shopkeeper");
      if (shopkeeper.canEdit(sender, false)) {
         List<? extends ShopkeeperSnapshot> snapshots = shopkeeper.getSnapshots();
         int snapshotIndex;
         if (context.has("all")) {
            snapshotIndex = shopkeeper.getSnapshots().size();
            this.confirmations.awaitConfirmation(sender, () -> {
               if (!shopkeeper.isValid()) {
                  TextUtils.sendMessage(sender, Messages.shopNoLongerExists);
               } else if (!shopkeeper.getSnapshots().equals(snapshots)) {
                  TextUtils.sendMessage(sender, Messages.actionAbortedSnapshotsChanged);
               } else {
                  shopkeeper.removeAllSnapshots();
                  shopkeeper.save();
                  Messages.snapshotRemovedAll.setPlaceholderArguments(shopkeeper.getMessageArguments("shop_"));
                  TextUtils.sendMessage(sender, Messages.snapshotRemovedAll, "snapshotsCount", snapshotIndex);
               }
            });
            Messages.confirmRemoveAllSnapshots.setPlaceholderArguments(shopkeeper.getMessageArguments("shop_"));
            TextUtils.sendMessage(sender, Messages.confirmRemoveAllSnapshots, "snapshotsCount", snapshotIndex);
            TextUtils.sendMessage(sender, Messages.confirmationRequired);
         } else {
            assert context.has("snapshot");

            snapshotIndex = (Integer)context.get("snapshot");

            assert snapshotIndex >= 0 && snapshotIndex < shopkeeper.getSnapshots().size();

            int snapshotId = snapshotIndex + 1;
            ShopkeeperSnapshot snapshot = shopkeeper.getSnapshot(snapshotIndex);
            this.confirmations.awaitConfirmation(sender, () -> {
               if (!shopkeeper.isValid()) {
                  TextUtils.sendMessage(sender, Messages.shopNoLongerExists);
               } else if (!shopkeeper.getSnapshots().equals(snapshots)) {
                  TextUtils.sendMessage(sender, Messages.actionAbortedSnapshotsChanged);
               } else {
                  assert snapshotIndex >= 0 && snapshotIndex < shopkeeper.getSnapshots().size();

                  ShopkeeperSnapshot removedSnapshot = shopkeeper.removeSnapshot(snapshotIndex);

                  assert removedSnapshot == snapshot;

                  shopkeeper.save();
                  TextUtils.sendMessage(sender, Messages.snapshotRemoved, "id", snapshotId, "name", snapshot.getName(), "timestamp", () -> {
                     return Settings.DerivedSettings.dateTimeFormatter.format(snapshot.getTimestamp());
                  });
               }
            });
            TextUtils.sendMessage(sender, Messages.confirmRemoveSnapshot, "id", snapshotId, "name", snapshot.getName(), "timestamp", () -> {
               return Settings.DerivedSettings.dateTimeFormatter.format(snapshot.getTimestamp());
            });
            TextUtils.sendMessage(sender, Messages.confirmationRequired);
         }
      }
   }
}
