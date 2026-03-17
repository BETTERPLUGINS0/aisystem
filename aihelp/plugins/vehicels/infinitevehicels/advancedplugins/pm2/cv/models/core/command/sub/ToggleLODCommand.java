package advancedplugins.pm2.cv.models.core.command.sub;

import advancedplugins.pm2.cv.models.api.command.AbstractCommand;
import advancedplugins.pm2.cv.models.api.lod.AnimationLODHandler;
import advancedplugins.pm2.cv.models.api.utils.logger.LogUtil;
import advancedplugins.pm2.cv.models.core.command.InfiniteModelsCommand;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ToggleLODCommand extends AbstractCommand {
   public ToggleLODCommand(AbstractCommand var1) {
      super(var1);
   }

   public boolean onCommand(CommandSender var1, String[] var2) {
      if (var2.length >= 1) {
         AnimationLODHandler.setGlobalEnabled(Boolean.parseBoolean(var2[0]));
      } else {
         String var10001 = String.valueOf(ChatColor.GREEN);
         var10001 = var10001 + "[InfiniteModel] Animation LOD is " + (AnimationLODHandler.isGlobalEnabled() ? "enabled." : "disabled.");
         String var10002 = String.valueOf(LogUtil.LogColor.BRIGHT_GREEN);
         InfiniteModelsCommand.logSender(var1, var10001, var10002 + "Animation LOD is " + (AnimationLODHandler.isGlobalEnabled() ? "enabled." : "disabled."));
      }

      return true;
   }

   public List<String> onTabComplete(CommandSender var1, String[] var2) {
      if (var2.length != 1) {
         return null;
      } else {
         ArrayList var3 = new ArrayList();
         var3.add("true");
         var3.add("false");
         return var3;
      }
   }

   public String getPermissionNode() {
      return "infinitemodel.command.toggle-lod";
   }

   public boolean isConsoleFriendly() {
      return true;
   }

   public String getName() {
      return "toggle-lod";
   }
}
