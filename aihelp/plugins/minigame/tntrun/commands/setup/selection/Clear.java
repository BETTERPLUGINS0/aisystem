package tntrun.commands.setup.selection;

import org.bukkit.entity.Player;
import tntrun.commands.setup.CommandHandlerInterface;
import tntrun.messages.Messages;
import tntrun.selectionget.PlayerSelection;

public class Clear implements CommandHandlerInterface {
   private PlayerSelection selection;

   public Clear(PlayerSelection selection) {
      this.selection = selection;
   }

   public boolean handleCommand(Player player, String[] args) {
      this.selection.clearSelectionPoints(player);
      Messages.sendMessage(player, "&7 Points cleared");
      return true;
   }

   public int getMinArgsLength() {
      return 0;
   }
}
