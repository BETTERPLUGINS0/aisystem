package tntrun.commands.setup.arena;

import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.commands.setup.CommandHandlerInterface;
import tntrun.messages.Messages;
import tntrun.utils.Utils;

public class SetupHelp implements CommandHandlerInterface {
   private TNTRun plugin;

   public SetupHelp(TNTRun plugin) {
      this.plugin = plugin;
   }

   public boolean handleCommand(Player player, String[] args) {
      Messages.sendMessage(player, "&7============" + Messages.trprefix + "============", false);
      Messages.sendMessage(player, Messages.setuphelp, false);
      Utils.displayHelp(player);
      player.sendMessage("Create a join sign for the arena to complete the setup.");
      return true;
   }

   public int getMinArgsLength() {
      return 0;
   }
}
