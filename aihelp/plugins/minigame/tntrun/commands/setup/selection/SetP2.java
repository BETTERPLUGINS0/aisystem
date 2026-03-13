package tntrun.commands.setup.selection;

import org.bukkit.entity.Player;
import tntrun.commands.setup.CommandHandlerInterface;
import tntrun.messages.Messages;
import tntrun.selectionget.PlayerSelection;

public class SetP2 implements CommandHandlerInterface {
   private PlayerSelection selection;

   public SetP2(PlayerSelection selection) {
      this.selection = selection;
   }

   public boolean handleCommand(Player player, String[] args) {
      this.selection.setSelectionPoint2(player);
      long var10001 = Math.round(this.selection.getSelectionPoint2(player).getX());
      Messages.sendMessage(player, "&7 Point &62 &7has been set to &6X: &7" + var10001 + " &6Y: &7" + Math.round(this.selection.getSelectionPoint2(player).getY()) + " &6Z: &7" + Math.round(this.selection.getSelectionPoint2(player).getZ()));
      return true;
   }

   public int getMinArgsLength() {
      return 0;
   }
}
