package me.PM2.infinitevehicles.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BukkitConditionContext extends ConditionContext<BukkitCommandIssuer> {
   protected BukkitConditionContext(BukkitCommandIssuer issuer, String config) {
      super(var1, var2);
   }

   public CommandSender getSender() {
      return ((BukkitCommandIssuer)this.getIssuer()).getIssuer();
   }

   public Player getPlayer() {
      return ((BukkitCommandIssuer)this.getIssuer()).getPlayer();
   }
}
