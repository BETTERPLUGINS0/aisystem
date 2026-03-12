package me.SuperRonanCraft.BetterRTP.references;

import me.SuperRonanCraft.BetterRTP.references.depends.DepPerms;
import org.bukkit.command.CommandSender;

public class Permissions {
   private final DepPerms depPerms = new DepPerms();

   public void register() {
      this.depPerms.register();
   }

   public boolean checkPerm(String str, CommandSender sendi) {
      return this.depPerms.hasPerm(str, sendi);
   }
}
