package com.nisovin.shopkeepers.commands.shopkeepers.snapshot;

import com.nisovin.shopkeepers.api.shopkeeper.ShopkeeperSnapshot;
import com.nisovin.shopkeepers.api.ui.DefaultUITypes;
import com.nisovin.shopkeepers.commands.arguments.ShopkeeperArgument;
import com.nisovin.shopkeepers.commands.arguments.ShopkeeperFilter;
import com.nisovin.shopkeepers.commands.arguments.TargetShopkeeperFallback;
import com.nisovin.shopkeepers.commands.lib.Command;
import com.nisovin.shopkeepers.commands.lib.CommandException;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.arguments.PositiveIntegerArgument;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.commands.util.ShopkeeperArgumentUtils;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import java.util.List;
import org.bukkit.command.CommandSender;

class CommandSnapshotList extends Command {
   private static final String ARGUMENT_SHOPKEEPER = "shopkeeper";
   private static final String ARGUMENT_PAGE = "page";
   private static final int ENTRIES_PER_PAGE = 8;

   CommandSnapshotList() {
      super("list");
      this.setPermission("shopkeeper.snapshot");
      this.setDescription(Messages.commandDescriptionSnapshotList);
      this.addArgument(new TargetShopkeeperFallback(new ShopkeeperArgument("shopkeeper", ShopkeeperFilter.withAccess(DefaultUITypes.EDITOR())), ShopkeeperArgumentUtils.TargetShopkeeperFilter.ANY));
      this.addArgument((new PositiveIntegerArgument("page")).orDefaultValue(1));
   }

   protected void execute(CommandInput input, CommandContextView context) throws CommandException {
      CommandSender sender = input.getSender();
      AbstractShopkeeper shopkeeper = (AbstractShopkeeper)context.get("shopkeeper");
      int page = (Integer)context.get("page");
      if (shopkeeper.canEdit(sender, false)) {
         List<? extends ShopkeeperSnapshot> snapshots = shopkeeper.getSnapshots();
         int snapshotsCount = snapshots.size();
         int maxPage = Math.max(1, (int)Math.ceil((double)snapshotsCount / 8.0D));
         page = Math.max(1, Math.min(page, maxPage));
         Messages.snapshotListHeader.setPlaceholderArguments(shopkeeper.getMessageArguments("shop_"));
         TextUtils.sendMessage(sender, Messages.snapshotListHeader, "snapshotsCount", snapshotsCount, "page", page, "maxPage", maxPage);
         int startIndex = (page - 1) * 8;
         int endIndex = Math.min(startIndex + 8, snapshotsCount);

         for(int index = startIndex; index < endIndex; ++index) {
            ShopkeeperSnapshot snapshot = (ShopkeeperSnapshot)snapshots.get(index);
            TextUtils.sendMessage(sender, Messages.snapshotListEntry, "id", index + 1, "name", snapshot.getName(), "timestamp", () -> {
               return Settings.DerivedSettings.dateTimeFormatter.format(snapshot.getTimestamp());
            });
         }

      }
   }
}
