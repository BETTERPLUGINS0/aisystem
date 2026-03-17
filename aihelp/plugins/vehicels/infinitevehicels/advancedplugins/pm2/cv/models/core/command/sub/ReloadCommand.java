package advancedplugins.pm2.cv.models.core.command.sub;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.command.AbstractCommand;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.ModelGenerator;
import advancedplugins.pm2.cv.models.api.utils.config.ConfigManager;
import advancedplugins.pm2.cv.models.api.utils.logger.LogUtil;
import advancedplugins.pm2.cv.models.core.command.InfiniteModelsCommand;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

public class ReloadCommand extends AbstractCommand {
   public ReloadCommand(AbstractCommand var1) {
      super(var1);
   }

   public boolean onCommand(CommandSender var1, String[] var2) {
      if (var2.length == 0) {
         this.reloadConfig(var1);
         this.reloadModels(var1);
         return true;
      } else {
         String var3 = var2[0].toLowerCase();
         byte var4 = -1;
         switch(var3.hashCode()) {
         case -1354792126:
            if (var3.equals("config")) {
               var4 = 1;
            }
            break;
         case -1068799382:
            if (var3.equals("models")) {
               var4 = 0;
            }
         }

         boolean var10000;
         switch(var4) {
         case 0:
            this.reloadModels(var1);
            var10000 = true;
            break;
         case 1:
            this.reloadConfig(var1);
            var10000 = true;
            break;
         default:
            var10000 = false;
         }

         return var10000;
      }
   }

   private void reloadConfig(CommandSender var1) {
      ConfigManager var2 = ModelAPI.getAPI().getConfigManager();
      var2.reload();
      var2.updateReferences();
      InfiniteModelsCommand.logSender(var1, String.valueOf(ChatColor.GREEN) + "[InfiniteModel] Config reloaded.", String.valueOf(LogUtil.LogColor.BRIGHT_GREEN) + "Config reloaded.");
   }

   private void reloadModels(CommandSender var1) {
      ModelGenerator var2 = ModelAPI.getModelGenerator();
      var2.importModels(false);
      var2.queueTask(ModelGenerator.Phase.POST_IMPORT, () -> {
         String var1x = ModelAPI.getAPI().getModelArchive().getKeys().size() + " models loaded.";
         if (var1 instanceof Entity) {
            String var10001 = String.valueOf(ChatColor.GREEN);
            var1.sendMessage(var10001 + "[InfiniteModel] " + var1x);
         } else {
            String var10000 = String.valueOf(LogUtil.LogColor.BRIGHT_GREEN);
            LogUtil.log(var10000 + var1x);
         }

      });
   }

   public List<String> onTabComplete(CommandSender var1, String[] var2) {
      return List.of("models", "config");
   }

   public String getPermissionNode() {
      return "infinitemodel.command.reload";
   }

   public boolean isConsoleFriendly() {
      return true;
   }

   public String getName() {
      return "reload";
   }
}
