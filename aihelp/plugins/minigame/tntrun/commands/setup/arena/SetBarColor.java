package tntrun.commands.setup.arena;

import com.google.common.base.Enums;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.commands.setup.CommandHandlerInterface;
import tntrun.messages.Messages;

public class SetBarColor implements CommandHandlerInterface {
   private TNTRun plugin;

   public SetBarColor(TNTRun plugin) {
      this.plugin = plugin;
   }

   public boolean handleCommand(Player player, String[] args) {
      String colour = args[0].toUpperCase();
      if (!colour.equals("RANDOM") && Enums.getIfPresent(BarColor.class, colour).orNull() == null) {
         Messages.sendMessage(player, "&c Invalid bar colour: &6" + args[0]);
         return true;
      } else {
         this.plugin.getConfig().set("special.BossBarColor", colour);
         this.plugin.saveConfig();
         Messages.sendMessage(player, "&7 Bar colour set to: &6" + colour);
         return true;
      }
   }

   public int getMinArgsLength() {
      return 1;
   }
}
