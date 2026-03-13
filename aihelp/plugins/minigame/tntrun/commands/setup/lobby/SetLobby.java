package tntrun.commands.setup.lobby;

import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.commands.setup.CommandHandlerInterface;
import tntrun.messages.Messages;

public class SetLobby implements CommandHandlerInterface {
   private TNTRun plugin;

   public SetLobby(TNTRun plugin) {
      this.plugin = plugin;
   }

   public boolean handleCommand(Player player, String[] args) {
      this.plugin.getGlobalLobby().setLobbyLocation(player.getLocation());
      Messages.sendMessage(player, "&7 Lobby set");
      return true;
   }

   public int getMinArgsLength() {
      return 0;
   }
}
