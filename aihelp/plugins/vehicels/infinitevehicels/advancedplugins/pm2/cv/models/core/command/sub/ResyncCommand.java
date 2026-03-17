package advancedplugins.pm2.cv.models.core.command.sub;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.command.AbstractCommand;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResyncCommand extends AbstractCommand {
   public ResyncCommand(AbstractCommand var1) {
      super(var1);
   }

   public boolean onCommand(CommandSender var1, String[] var2) {
      if (var1 instanceof Player) {
         Player var3 = (Player)var1;
         ModelAPI.getNetworkHandler().getPipeline(var3.getUniqueId()).ifPresent((var1x) -> {
            if (var2.length >= 1) {
               var1x.setDelay(Long.parseLong(var2[0]));
            } else {
               var1x.getDesyncMonitor().startTest();
            }

         });
      }

      return true;
   }

   public List<String> onTabComplete(CommandSender var1, String[] var2) {
      return null;
   }

   public String getPermissionNode() {
      return "infinitemodel.command.resync";
   }

   public boolean isConsoleFriendly() {
      return true;
   }

   public String getName() {
      return "resync";
   }
}
