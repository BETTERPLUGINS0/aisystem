package com.nisovin.shopkeepers.commands.shopkeepers.snapshot;

import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopkeeper.ShopkeeperLoadException;
import com.nisovin.shopkeepers.api.ui.DefaultUITypes;
import com.nisovin.shopkeepers.commands.arguments.ShopkeeperArgument;
import com.nisovin.shopkeepers.commands.arguments.ShopkeeperFilter;
import com.nisovin.shopkeepers.commands.arguments.TargetShopkeeperFallback;
import com.nisovin.shopkeepers.commands.arguments.snapshot.ShopkeeperSnapshotIndexArgument;
import com.nisovin.shopkeepers.commands.lib.Command;
import com.nisovin.shopkeepers.commands.lib.CommandException;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.commands.util.ShopkeeperArgumentUtils;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopkeeper.SKShopkeeperSnapshot;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.logging.Log;
import org.bukkit.command.CommandSender;

class CommandSnapshotRestore extends Command {
   private static final String ARGUMENT_SHOPKEEPER = "shopkeeper";
   private static final String ARGUMENT_SNAPSHOT = "snaphot";

   CommandSnapshotRestore() {
      super("restore");
      this.setPermission("shopkeeper.snapshot");
      this.setDescription(Messages.commandDescriptionSnapshotRestore);
      CommandArgument<Shopkeeper> shopkeeperArgument = new TargetShopkeeperFallback(new ShopkeeperArgument("shopkeeper", ShopkeeperFilter.withAccess(DefaultUITypes.EDITOR())), ShopkeeperArgumentUtils.TargetShopkeeperFilter.ANY);
      this.addArgument(shopkeeperArgument);
      this.addArgument(new ShopkeeperSnapshotIndexArgument("snaphot", shopkeeperArgument));
   }

   protected void execute(CommandInput input, CommandContextView context) throws CommandException {
      CommandSender sender = input.getSender();
      AbstractShopkeeper shopkeeper = (AbstractShopkeeper)context.get("shopkeeper");
      int snapshotIndex = (Integer)context.get("snaphot");

      assert snapshotIndex >= 0 && snapshotIndex < shopkeeper.getSnapshots().size();

      int snapshotId = snapshotIndex + 1;
      if (shopkeeper.canEdit(sender, false)) {
         SKShopkeeperSnapshot snapshot = shopkeeper.getSnapshot(snapshotIndex);

         try {
            shopkeeper.applySnapshot(snapshot);
            shopkeeper.save();
         } catch (ShopkeeperLoadException var9) {
            TextUtils.sendMessage(sender, Messages.snapshotRestoreFailed, "id", snapshotId, "name", snapshot.getName(), "timestamp", () -> {
               return Settings.DerivedSettings.dateTimeFormatter.format(snapshot.getTimestamp());
            });
            Log.warning((String)(shopkeeper.getLogPrefix() + "Failed to restore snapshot " + snapshotId + " ('" + snapshot.getName() + "')!"), (Throwable)var9);
            return;
         }

         TextUtils.sendMessage(sender, Messages.snapshotRestored, "id", snapshotId, "name", snapshot.getName(), "timestamp", () -> {
            return Settings.DerivedSettings.dateTimeFormatter.format(snapshot.getTimestamp());
         });
      }
   }
}
