package com.nisovin.shopkeepers.commands.shopkeepers.snapshot;

import com.nisovin.shopkeepers.commands.lib.commands.HelpCommand;
import java.util.Arrays;

public class CommandSnapshotHelp extends HelpCommand {
   public CommandSnapshotHelp(CommandSnapshot helpSource) {
      super("help", Arrays.asList("?"), helpSource);
      this.setPermission("shopkeeper.snapshot");
      this.setHiddenInParentHelp(true);
   }
}
