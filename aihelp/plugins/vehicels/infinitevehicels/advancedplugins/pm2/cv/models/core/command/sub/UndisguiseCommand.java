package advancedplugins.pm2.cv.models.core.command.sub;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.command.AbstractCommand;
import advancedplugins.pm2.cv.models.api.model.rpc.IModelContainer;
import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.core.command.InfiniteModelsCommand;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UndisguiseCommand extends AbstractCommand {
   public UndisguiseCommand(AbstractCommand var1) {
      super(var1);
   }

   public boolean onCommand(CommandSender var1, String[] var2) {
      Player var3 = (Player)var1;
      IModelContainer var4 = ModelAPI.getModeledEntity(var3.getUniqueId());
      if (var4 == null) {
         return true;
      } else {
         if (var2.length != 0) {
            String[] var5 = var2;
            int var6 = var2.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               String var8 = var5[var7];
               var4.removeModel(var8).ifPresent(IVisualModel::destroy);
            }

            if (var4.getModels().isEmpty()) {
               var4.markRemoved();
               ModelAPI.getEntityHandler().setForcedInvisible(var3, false);
               ModelAPI.getEntityHandler().forceSpawn(var3);
            }
         } else {
            var4.markRemoved();
            ModelAPI.getEntityHandler().setForcedInvisible(var3, false);
            ModelAPI.getEntityHandler().forceSpawn(var3);
         }

         return true;
      }
   }

   public List<String> onTabComplete(CommandSender var1, String[] var2) {
      ArrayList var3 = new ArrayList();
      Player var4 = (Player)var1;
      IModelContainer var5 = ModelAPI.getModeledEntity(var4.getUniqueId());
      if (var5 == null) {
         return var3;
      } else {
         if (var2.length > 0) {
            String var6 = var2[var2.length - 1];
            InfiniteModelsCommand.getModelIdTabComplete(var3, var6, var5);
         }

         return var3;
      }
   }

   public String getPermissionNode() {
      return "infinitemodel.command.undisguise";
   }

   public boolean isConsoleFriendly() {
      return false;
   }

   public String getName() {
      return "undisguise";
   }
}
