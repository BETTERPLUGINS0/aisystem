package me.PM2.infinitevehicles.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BukkitCommandCompletionContext extends CommandCompletionContext<BukkitCommandIssuer> {
   protected BukkitCommandCompletionContext(RegisteredCommand command, BukkitCommandIssuer issuer, String input, String config, String[] args) {
      super(var1, var2, var3, var4, var5);
   }

   public CommandSender getSender() {
      return (CommandSender)this.getIssuer().getIssuer();
   }

   public Player getPlayer() {
      return ((BukkitCommandIssuer)this.issuer).getPlayer();
   }
}
