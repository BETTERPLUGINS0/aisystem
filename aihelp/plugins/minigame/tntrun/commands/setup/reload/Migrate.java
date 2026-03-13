package tntrun.commands.setup.reload;

import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.commands.setup.CommandHandlerInterface;

public class Migrate implements CommandHandlerInterface {
   private TNTRun plugin;

   public Migrate(TNTRun plugin) {
      this.plugin = plugin;
   }

   public boolean handleCommand(Player player, String[] args) {
      ConfigurationSection section = this.plugin.getConfig().getConfigurationSection("doublejumps");
      if (section == null) {
         player.sendMessage("[TNTRun_reloaded] There is nothing to migrate");
         return true;
      } else {
         Iterator var4 = section.getKeys(false).iterator();

         while(var4.hasNext()) {
            String name = (String)var4.next();
            int amount = this.plugin.getConfig().getInt("doublejumps." + name, 0);
            if (amount > 0) {
               if (this.plugin.useUuid()) {
                  OfflinePlayer oplayer = Bukkit.getOfflinePlayer(name);
                  amount += this.plugin.getPData().getDoubleJumpsFromFile(oplayer);
                  this.plugin.getPData().saveDoubleJumpsToFile(oplayer, amount);
               } else {
                  amount += this.plugin.getPData().getDoubleJumpsFromFile(name);
                  this.plugin.getPData().saveDoubleJumpsToFile(name, amount);
               }
            }
         }

         this.plugin.getConfig().set("doublejumps", (Object)null);
         this.plugin.saveConfig();
         player.sendMessage("[TNTRun_reloaded] All doublejump entries have been migrated");
         return true;
      }
   }

   public int getMinArgsLength() {
      return 0;
   }
}
