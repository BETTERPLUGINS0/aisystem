package com.nisovin.shopkeepers.commands.shopkeepers.snapshot;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.commands.Confirmations;
import com.nisovin.shopkeepers.commands.lib.Command;
import com.nisovin.shopkeepers.commands.lib.CommandRegistry;
import java.util.Arrays;

public class CommandSnapshot extends Command {
   public CommandSnapshot(Confirmations confirmations) {
      super("snapshot", Arrays.asList("snapshots"));
      this.setPermission("shopkeeper.snapshot");
      this.setHiddenInOwnHelp(true);
      this.setHiddenInParentHelp(true);
      this.setIncludeChildsInParentHelp(true);
      CommandRegistry childCommands = this.getChildCommands();
      childCommands.register(new CommandSnapshotHelp((CommandSnapshot)Unsafe.initialized(this)));
      childCommands.register(new CommandSnapshotList());
      childCommands.register(new CommandSnapshotCreate());
      childCommands.register(new CommandSnapshotRemove(confirmations));
      childCommands.register(new CommandSnapshotRestore());
   }
}
