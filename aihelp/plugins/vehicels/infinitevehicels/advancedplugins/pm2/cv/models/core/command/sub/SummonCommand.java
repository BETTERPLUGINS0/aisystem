package advancedplugins.pm2.cv.models.core.command.sub;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.command.AbstractCommand;
import advancedplugins.pm2.cv.models.api.model.rpc.IModelContainer;
import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.BaseEntity;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.BukkitEntity;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.blueprint.ModelBlueprint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.JointBehaviorTypes;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.PlayerLimb;
import advancedplugins.pm2.cv.models.core.command.InfiniteModelsCommand;
import advancedplugins.pm2.cv.models.core.command.ModelOptionParser;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SummonCommand extends AbstractCommand {
   public SummonCommand(AbstractCommand var1) {
      super(var1);
   }

   public boolean onCommand(CommandSender var1, String[] var2) {
      if (var2.length < 1) {
         return false;
      } else {
         EntityType var3 = EntityType.PIG;
         if (var2.length >= 2) {
            try {
               var3 = EntityType.valueOf(var2[1].toUpperCase(Locale.ENGLISH));
            } catch (IllegalArgumentException var9) {
            }
         }

         Class var4 = var3.getEntityClass();
         if (var4 == null) {
            return false;
         } else {
            ModelBlueprint var5 = ModelAPI.getBlueprint(var2[0]);
            if (var5 == null) {
               return false;
            } else {
               ModelOptionParser var6 = ModelOptionParser.parse(2, var2);
               Player var7 = (Player)var1;
               Location var8 = var7.getLocation();
               var7.getWorld().spawn(var8, var4, (var4x) -> {
                  BukkitEntity var5x = new BukkitEntity(var4x);
                  var5x.getBodyRotationController().setYBodyRot(var8.getYaw());
                  IModelContainer var6x = ModelAPI.create((BaseEntity)var5x);
                  var6x.setBaseEntityVisible(false);
                  IVisualModel var7x = ModelAPI.create(var5);
                  var7x.setAutoRendererInitialization(false);
                  var6.applyDisguiseOptions(var7x);
                  var6x.addModel(var7x, true).ifPresent(IVisualModel::destroy);
                  var7x.getJoints().values().forEach((var1) -> {
                     var1.getJointAction(JointBehaviorTypes.PLAYER_LIMB).ifPresent((var1x) -> {
                        ((PlayerLimb)var1x).setTexture(var7);
                     });
                  });
                  var7x.initializeRenderer();
               });
               return true;
            }
         }
      }
   }

   public List<String> onTabComplete(CommandSender var1, String[] var2) {
      ArrayList var3 = new ArrayList();
      switch(var2.length) {
      case 1:
         InfiniteModelsCommand.getModelIdTabComplete(var3, var2[0]);
         break;
      case 2:
         String var4 = var2[1];
         EntityType[] var5 = EntityType.values();
         EntityType[] var6 = var5;
         int var7 = var5.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            EntityType var9 = var6[var8];
            String var10 = var9.name();
            if (var10.startsWith(var4.toUpperCase(Locale.ENGLISH))) {
               var3.add(var10);
            }
         }

         return var3;
      default:
         var3.addAll(ModelOptionParser.getTabCompletion(var2.length > 1 ? 2 : 1, var2));
      }

      return var3;
   }

   public String getPermissionNode() {
      return "infinitemodel.command.summon";
   }

   public boolean isConsoleFriendly() {
      return false;
   }

   public String getName() {
      return "summon";
   }
}
