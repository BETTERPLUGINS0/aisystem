package me.SuperRonanCraft.BetterRTP.references.depends;

import me.SuperRonanCraft.BetterRTP.BetterRTP;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;

public class DepPerms {
   public Permission p = null;

   public boolean hasPerm(String perm, CommandSender sendi) {
      return this.p != null ? this.p.has(sendi, perm) : sendi.hasPermission(perm);
   }

   public void register() {
      try {
         if (BetterRTP.getInstance().getServer().getPluginManager().isPluginEnabled("Vault")) {
            RegisteredServiceProvider<Permission> permissionProvider = BetterRTP.getInstance().getServer().getServicesManager().getRegistration(Permission.class);
            this.p = (Permission)permissionProvider.getProvider();
         } else {
            this.p = null;
         }
      } catch (NullPointerException var2) {
      }

   }
}
