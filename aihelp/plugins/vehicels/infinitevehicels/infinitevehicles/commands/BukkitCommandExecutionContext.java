package me.PM2.infinitevehicles.commands;

import java.util.List;
import java.util.Map;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BukkitCommandExecutionContext extends CommandExecutionContext<BukkitCommandExecutionContext, BukkitCommandIssuer> {
   protected BukkitCommandExecutionContext(RegisteredCommand cmd, CommandParameter param, BukkitCommandIssuer sender, List<String> args, int index, Map<String, Object> passedArgs) {
      super(var1, var2, var3, var4, var5, var6);
   }

   public CommandSender getSender() {
      return ((BukkitCommandIssuer)this.issuer).getIssuer();
   }

   public Player getPlayer() {
      return ((BukkitCommandIssuer)this.issuer).getPlayer();
   }
}
