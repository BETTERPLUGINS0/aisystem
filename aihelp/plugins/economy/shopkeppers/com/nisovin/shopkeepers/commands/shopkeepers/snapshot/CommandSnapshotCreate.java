package com.nisovin.shopkeepers.commands.shopkeepers.snapshot;

import com.nisovin.shopkeepers.api.shopkeeper.ShopkeeperSnapshot;
import com.nisovin.shopkeepers.api.ui.DefaultUITypes;
import com.nisovin.shopkeepers.commands.arguments.ShopkeeperArgument;
import com.nisovin.shopkeepers.commands.arguments.ShopkeeperFilter;
import com.nisovin.shopkeepers.commands.arguments.TargetShopkeeperFallback;
import com.nisovin.shopkeepers.commands.lib.Command;
import com.nisovin.shopkeepers.commands.lib.CommandException;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.arguments.StringArgument;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.commands.util.ShopkeeperArgumentUtils;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import org.bukkit.command.CommandSender;

class CommandSnapshotCreate extends Command {
   private static final String ARGUMENT_SHOPKEEPER = "shopkeeper";
   private static final String ARGUMENT_SNAPSHOT_NAME = "snapshot-name";

   CommandSnapshotCreate() {
      super("create");
      this.setPermission("shopkeeper.snapshot");
      this.setDescription(Messages.commandDescriptionSnapshotCreate);
      this.addArgument(new TargetShopkeeperFallback(new ShopkeeperArgument("shopkeeper", ShopkeeperFilter.withAccess(DefaultUITypes.EDITOR())), ShopkeeperArgumentUtils.TargetShopkeeperFilter.ANY));
      this.addArgument(new StringArgument("snapshot-name"));
   }

   protected void execute(CommandInput input, CommandContextView context) throws CommandException {
      CommandSender sender = input.getSender();
      AbstractShopkeeper shopkeeper = (AbstractShopkeeper)context.get("shopkeeper");
      String snapshotName = (String)context.get("snapshot-name");

      assert !snapshotName.isEmpty();

      if (shopkeeper.canEdit(sender, false)) {
         if (snapshotName.length() > ShopkeeperSnapshot.getMaxNameLength()) {
            TextUtils.sendMessage(sender, Messages.snapshotNameTooLong, "maxLength", ShopkeeperSnapshot.getMaxNameLength(), "name", snapshotName);
         } else if (!ShopkeeperSnapshot.isNameValid(snapshotName)) {
            TextUtils.sendMessage(sender, Messages.snapshotNameInvalid, "name", snapshotName);
         } else if (shopkeeper.getSnapshot(snapshotName) != null) {
            TextUtils.sendMessage(sender, Messages.snapshotNameAlreadyExists, "name", snapshotName);
         } else {
            ShopkeeperSnapshot snapshot = shopkeeper.createSnapshot(snapshotName);
            shopkeeper.addSnapshot(snapshot);
            shopkeeper.save();
            TextUtils.sendMessage(sender, Messages.snapshotCreated, "name", snapshotName, "id", shopkeeper.getSnapshots().size(), "timestamp", () -> {
               return Settings.DerivedSettings.dateTimeFormatter.format(snapshot.getTimestamp());
            });
         }
      }
   }
}
