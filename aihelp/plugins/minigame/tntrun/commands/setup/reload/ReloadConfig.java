package tntrun.commands.setup.reload;

import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.commands.setup.CommandHandlerInterface;

public class ReloadConfig implements CommandHandlerInterface {
   private TNTRun plugin;

   public ReloadConfig(TNTRun plugin) {
      this.plugin = plugin;
   }

   public boolean handleCommand(Player player, String[] args) {
      this.plugin.reloadConfig();
      this.plugin.getSignEditor().loadConfiguration();
      this.plugin.setupScoreboards();
      this.plugin.setupShop();
      player.sendMessage("Config reloaded");
      return true;
   }

   public int getMinArgsLength() {
      return 0;
   }
}
