package advancedplugins.pm2.cv.models.core.command.sub;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.command.AbstractCommand;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.EntityDataTrackers;
import advancedplugins.pm2.cv.models.api.utils.Profiler;
import advancedplugins.pm2.cv.models.core.command.InfiniteModelsCommand;
import java.util.Iterator;
import java.util.List;
import org.bukkit.command.CommandSender;

public class PluginHealthCommand extends AbstractCommand {
   public PluginHealthCommand(AbstractCommand var1) {
      super(var1);
   }

   public boolean onCommand(CommandSender var1, String[] var2) {
      Profiler var3 = ModelAPI.getAPI().getModelUpdaters().getProfiler();
      InfiniteModelsCommand.logSender(var1, String.format("Model Updaters: Min: %.3f ms, Max: %.3f ms, Avg: %.3f ms", (double)var3.getMinTime() / 1000000.0D, (double)var3.getMaxTime() / 1000000.0D, var3.getAverageTime() / 1000000.0D));
      EntityDataTrackers var4 = ModelAPI.getAPI().getDataTrackers();
      InfiniteModelsCommand.logSender(var1, "Entity Data Trackers:");
      Iterator var5 = var4.getAvailable().iterator();

      while(var5.hasNext()) {
         EntityDataTrackers.Tracker var6 = (EntityDataTrackers.Tracker)var5.next();
         InfiniteModelsCommand.logSender(var1, String.format("- %s: %s (%sms)", var6.getId(), var6.getLoad(), var6.getTimings()));
      }

      return true;
   }

   public List<String> onTabComplete(CommandSender var1, String[] var2) {
      return null;
   }

   public String getPermissionNode() {
      return "infinitemodel.command.health";
   }

   public boolean isConsoleFriendly() {
      return true;
   }

   public String getName() {
      return "health";
   }
}
