package advancedplugins.pm2.cv.models.core.command.sub;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.command.AbstractCommand;
import advancedplugins.pm2.cv.models.api.model.rpc.IModelContainer;
import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.data.BukkitEntityData;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.data.IEntityData;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.blueprint.ModelBlueprint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.JointBehaviorTypes;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.PlayerLimb;
import advancedplugins.pm2.cv.models.core.command.InfiniteModelsCommand;
import advancedplugins.pm2.cv.models.core.command.ModelOptionParser;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DisguiseCommand extends AbstractCommand {
   public DisguiseCommand(AbstractCommand var1) {
      super(var1);
   }

   public boolean onCommand(CommandSender var1, String[] var2) {
      Player var3 = (Player)var1;
      if (var2.length < 1) {
         return false;
      } else {
         ModelBlueprint var4 = ModelAPI.getBlueprint(var2[0]);
         if (var4 == null) {
            return false;
         } else {
            ModelOptionParser var5 = ModelOptionParser.parse(1, var2);
            IModelContainer var6 = ModelAPI.getOrCreateModeledEntity(var3);
            var6.getBase().getBodyRotationController().setPlayerMode(true);
            var6.setBaseEntityVisible(false);
            IEntityData var7 = var6.getBase().getData();
            if (var7 instanceof BukkitEntityData) {
               BukkitEntityData var8 = (BukkitEntityData)var7;
               if (var2.length < 2 || !var5.hideSelfDisguise) {
                  var8.getTracked().addForcedPairing(var3.getUniqueId());
               }
            }

            ModelAPI.getEntityHandler().setForcedInvisible(var3, true);
            if (var6.getModel(var2[0]).isEmpty()) {
               IVisualModel var9 = ModelAPI.create(var4);
               var5.applyDisguiseOptions(var9);
               var6.addModel(var9, false).ifPresent(IVisualModel::destroy);
               var9.getJoints().values().forEach((var1x) -> {
                  var1x.getJointAction(JointBehaviorTypes.PLAYER_LIMB).ifPresent((var1) -> {
                     ((PlayerLimb)var1).setTexture(var3);
                  });
               });
            }

            return true;
         }
      }
   }

   public List<String> onTabComplete(CommandSender var1, String[] var2) {
      ArrayList var3 = new ArrayList();
      if (var2.length == 1) {
         InfiniteModelsCommand.getModelIdTabComplete(var3, var2[0]);
      } else {
         var3.addAll(ModelOptionParser.getTabCompletion(1, var2));
      }

      return var3;
   }

   public String getPermissionNode() {
      return "infinitemodel.command.disguise";
   }

   public boolean isConsoleFriendly() {
      return false;
   }

   public String getName() {
      return "disguise";
   }
}
