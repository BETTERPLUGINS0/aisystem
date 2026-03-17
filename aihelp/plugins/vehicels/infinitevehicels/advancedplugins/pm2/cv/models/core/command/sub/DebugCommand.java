package advancedplugins.pm2.cv.models.core.command.sub;

import advancedplugins.pm2.cv.models.api.command.AbstractCommand;
import advancedplugins.pm2.cv.models.api.utils.config.DebugToggle;
import advancedplugins.pm2.cv.models.api.utils.logger.LogUtil;
import advancedplugins.pm2.cv.models.core.command.InfiniteModelsCommand;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class DebugCommand extends AbstractCommand {
   public DebugCommand(AbstractCommand var1) {
      super(var1);
   }

   public boolean onCommand(CommandSender var1, String[] var2) {
      if (var2.length == 0) {
         return false;
      } else {
         DebugToggle var3 = DebugToggle.get(var2[0]);
         String var10000;
         String var5;
         String var7;
         if (var3 == null) {
            var10000 = String.valueOf(ChatColor.RED);
            var7 = var10000 + "[InfiniteModel] Unknown debug: " + var2[0] + ".";
            var10000 = String.valueOf(LogUtil.LogColor.RED);
            var5 = var10000 + "Unknown debug: " + var2[0] + ".";
            InfiniteModelsCommand.logSender(var1, var7, var5);
            return false;
         } else {
            String var6;
            if (var2.length == 1) {
               var7 = DebugToggle.isDebugging(var3) ? "enabled." : "disabled.";
               var10000 = String.valueOf(ChatColor.GREEN);
               var5 = var10000 + "[InfiniteModel] " + var3.name() + " is " + var7;
               var10000 = String.valueOf(LogUtil.LogColor.BRIGHT_GREEN);
               var6 = var10000 + var3.name() + " is " + var7;
               InfiniteModelsCommand.logSender(var1, var5, var6);
               return true;
            } else {
               boolean var4 = Boolean.parseBoolean(var2[1]);
               DebugToggle.setDebug(var3, var4);
               var10000 = String.valueOf(ChatColor.GREEN);
               var5 = var10000 + "[InfiniteModel] Set " + var3.name() + " to " + var4 + ".";
               var10000 = String.valueOf(LogUtil.LogColor.BRIGHT_GREEN);
               var6 = var10000 + "Set " + var3.name() + " to " + var4 + ".";
               InfiniteModelsCommand.logSender(var1, var5, var6);
               return true;
            }
         }
      }
   }

   public List<String> onTabComplete(CommandSender var1, String[] var2) {
      ArrayList var3 = new ArrayList();
      switch(var2.length) {
      case 1:
         DebugToggle[] var4 = DebugToggle.values();
         DebugToggle[] var5 = var4;
         int var6 = var4.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            DebugToggle var8 = var5[var7];
            var3.add(var8.name());
         }

         return var3;
      case 2:
         var3.add("true");
         var3.add("false");
      default:
         return var3;
      }
   }

   public String getPermissionNode() {
      return "infinitemodel.command.debug";
   }

   public boolean isConsoleFriendly() {
      return true;
   }

   public String getName() {
      return "debug";
   }
}
