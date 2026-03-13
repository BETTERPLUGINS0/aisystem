package tntrun.commands.setup.selection;

import org.bukkit.entity.Player;
import tntrun.commands.setup.CommandHandlerInterface;
import tntrun.messages.Messages;
import tntrun.selectionget.PlayerSelection;

public class SetP1 implements CommandHandlerInterface {
   private PlayerSelection selection;

   public SetP1(PlayerSelection selection) {
      this.selection = selection;
   }

   public boolean handleCommand(Player player, String[] args) {
      this.selection.setSelectionPoint1(player);
      long var10001 = Math.round(this.selection.getSelectionPoint1(player).getX());
      Messages.sendMessage(player, "&7 Point &61 &7has been set to &6X: &7" + var10001 + " &6Y: &7" + Math.round(this.selection.getSelectionPoint1(player).getY()) + " &6Z: &7" + Math.round(this.selection.getSelectionPoint1(player).getZ()));
      return true;
   }

   public int getMinArgsLength() {
      return 0;
   }
}
