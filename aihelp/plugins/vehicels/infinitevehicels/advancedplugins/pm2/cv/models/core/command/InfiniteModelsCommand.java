package advancedplugins.pm2.cv.models.core.command;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.command.AbstractCommand;
import advancedplugins.pm2.cv.models.api.model.rpc.IModelContainer;
import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.property.IAnimationProperty;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.blueprint.ModelBlueprint;
import advancedplugins.pm2.cv.models.api.utils.logger.LogUtil;
import advancedplugins.pm2.cv.models.core.command.sub.DebugCommand;
import advancedplugins.pm2.cv.models.core.command.sub.DevCommand;
import advancedplugins.pm2.cv.models.core.command.sub.DisguiseCommand;
import advancedplugins.pm2.cv.models.core.command.sub.PluginHealthCommand;
import advancedplugins.pm2.cv.models.core.command.sub.ReloadCommand;
import advancedplugins.pm2.cv.models.core.command.sub.ResyncCommand;
import advancedplugins.pm2.cv.models.core.command.sub.SummonCommand;
import advancedplugins.pm2.cv.models.core.command.sub.ToggleLODCommand;
import advancedplugins.pm2.cv.models.core.command.sub.UndisguiseCommand;
import java.util.Iterator;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

public class InfiniteModelsCommand extends AbstractCommand {
   public InfiniteModelsCommand(JavaPlugin var1) {
      super(var1);
      this.addSubCommands(new AbstractCommand[]{new ReloadCommand(this)});
      this.addSubCommands(new AbstractCommand[]{new SummonCommand(this)});
      this.addSubCommands(new AbstractCommand[]{new DevCommand(this)});
      this.addSubCommands(new AbstractCommand[]{new DisguiseCommand(this)});
      this.addSubCommands(new AbstractCommand[]{new UndisguiseCommand(this)});
      this.addSubCommands(new AbstractCommand[]{new DebugCommand(this)});
      this.addSubCommands(new AbstractCommand[]{new PluginHealthCommand(this)});
      this.addSubCommands(new AbstractCommand[]{new ResyncCommand(this)});
      this.addSubCommands(new AbstractCommand[]{new ToggleLODCommand(this)});
   }

   public static void getModelIdTabComplete(List<String> var0, String var1) {
      Iterator var2 = ModelAPI.getAPI().getModelArchive().getKeys().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         if (var3.startsWith(var1)) {
            var0.add(var3);
         }
      }

   }

   public static void getModelIdTabComplete(List<String> var0, String var1, IModelContainer var2) {
      Iterator var3 = var2.getModels().keySet().iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         if (var4.startsWith(var1)) {
            var0.add(var4);
         }
      }

   }

   public static void getStateTabComplete(List<String> var0, String var1, IVisualModel var2) {
      Iterator var3 = var2.getAnimationHandler().getAnimations().values().iterator();

      while(var3.hasNext()) {
         IAnimationProperty var4 = (IAnimationProperty)var3.next();
         String var5 = var4.getName();
         if (var5.startsWith(var1)) {
            var0.add(var5);
         }
      }

   }

   public static void getStateTabComplete(List<String> var0, String var1, ModelBlueprint var2) {
      Iterator var3 = var2.getAnimations().keySet().iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         if (var4.startsWith(var1)) {
            var0.add(var4);
         }
      }

   }

   public static void logSender(CommandSender var0, String var1) {
      logSender(var0, var1, var1);
   }

   public static void logSender(CommandSender var0, String var1, String var2) {
      if (var0 instanceof Entity) {
         var0.sendMessage(var1);
      } else {
         LogUtil.log(var2);
      }

   }

   public boolean onCommand(CommandSender var1, String[] var2) {
      return false;
   }

   public List<String> onTabComplete(CommandSender var1, String[] var2) {
      return null;
   }

   public String getPermissionNode() {
      return "infinite.command";
   }

   public boolean isConsoleFriendly() {
      return true;
   }

   public String getName() {
      return null;
   }
}
