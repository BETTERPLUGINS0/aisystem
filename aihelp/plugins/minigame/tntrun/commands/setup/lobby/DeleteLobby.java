package tntrun.commands.setup.lobby;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.commands.setup.CommandHandlerInterface;
import tntrun.messages.Messages;

public class DeleteLobby implements CommandHandlerInterface {
   private TNTRun plugin;

   public DeleteLobby(TNTRun plugin) {
      this.plugin = plugin;
   }

   public boolean handleCommand(Player player, String[] args) {
      this.plugin.getGlobalLobby().setLobbyLocation((Location)null);
      Messages.sendMessage(player, "&7 Lobby deleted");
      return true;
   }

   public int getMinArgsLength() {
      return 0;
   }
}
