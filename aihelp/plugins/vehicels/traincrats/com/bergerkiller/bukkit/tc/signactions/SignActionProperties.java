package com.bergerkiller.bukkit.tc.signactions;

import com.bergerkiller.bukkit.common.permissions.IPermissionEnum;
import com.bergerkiller.bukkit.common.resources.SoundEffect;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.bergerkiller.bukkit.tc.Localization;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent;
import com.bergerkiller.bukkit.tc.properties.IProperties;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.api.IPropertyRegistry;
import com.bergerkiller.bukkit.tc.properties.api.PropertyParseResult;
import com.bergerkiller.bukkit.tc.properties.api.context.PropertyInputContext;
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions;
import java.util.Iterator;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

public class SignActionProperties extends TrainCartsSignAction {
   private static PropertyParseResult.Reason parseAndSet(IProperties properties, SignActionEvent info, boolean conditional) {
      return properties.parseAndSet(info.getLine(2), PropertyInputContext.of(info.getLine(3)).signEvent(info).beforeSet((result) -> {
         return conditional && !result.getInputContext().hasParsedStatements() ? PropertyParseResult.failSuppressed(result.getInputContext(), result.getProperty(), result.getName()) : result;
      })).getReason();
   }

   public SignActionProperties() {
      super("property");
   }

   public void execute(SignActionEvent info) {
      if (info.isPowered()) {
         boolean isConditionalCart = false;
         boolean isConditionalTrain = false;
         if (!info.getHeader().onPowerFalling() && !info.getHeader().onPowerRising()) {
            if (info.isAction(SignActionType.REDSTONE_CHANGE)) {
               isConditionalCart = true;
               isConditionalTrain = true;
            } else if (info.isAction(SignActionType.MEMBER_UPDATE)) {
               isConditionalCart = true;
            } else if (info.isAction(SignActionType.GROUP_UPDATE)) {
               isConditionalTrain = true;
            }
         }

         PropertyParseResult.Reason result;
         if ((isConditionalCart || info.isAction(SignActionType.MEMBER_ENTER, SignActionType.REDSTONE_ON)) && info.isCartSign() && info.hasMember()) {
            result = parseAndSet(info.getMember().getProperties(), info, isConditionalCart);
         } else if ((isConditionalTrain || info.isAction(SignActionType.GROUP_ENTER, SignActionType.REDSTONE_ON)) && info.isTrainSign() && info.hasGroup()) {
            result = parseAndSet(info.getGroup().getProperties(), info, isConditionalTrain);
         } else {
            if (!info.isAction(SignActionType.REDSTONE_ON) || !info.isRCSign()) {
               return;
            }

            result = PropertyParseResult.Reason.NONE;
            Iterator var5 = info.getRCTrainProperties().iterator();

            while(var5.hasNext()) {
               TrainProperties prop = (TrainProperties)var5.next();
               PropertyParseResult.Reason singleResult = parseAndSet(prop, info, false);
               if (singleResult != PropertyParseResult.Reason.NONE) {
                  result = singleResult;
               }
            }
         }

         BlockFace facingInv = info.getFacing().getOppositeFace();
         Location effectLocation = info.getSign().getLocation().add(0.5D, 0.5D, 0.5D).add(0.3D * (double)facingInv.getModX(), 0.0D, 0.3D * (double)facingInv.getModZ());
         switch(result) {
         case PROPERTY_NOT_FOUND:
            Util.spawnDustParticle(effectLocation, 0.0D, 0.0D, 0.0D);
            WorldUtil.playSound(effectLocation, SoundEffect.EXTINGUISH, 1.0F, 2.0F);
            break;
         case INVALID_INPUT:
            Util.spawnDustParticle(effectLocation, 255.0D, 255.0D, 0.0D);
            WorldUtil.playSound(effectLocation, SoundEffect.EXTINGUISH, 1.0F, 2.0F);
            break;
         case ERROR:
            Util.spawnDustParticle(effectLocation, 255.0D, 0.0D, 0.0D);
            WorldUtil.playSound(effectLocation, SoundEffect.EXTINGUISH, 1.0F, 2.0F);
         case SUPPRESSED:
         }

      }
   }

   public boolean build(SignChangeActionEvent event) {
      SignBuildOptions opt = SignBuildOptions.create().setPermission((IPermissionEnum)Permission.BUILD_PROPERTY).setName(event.isCartSign() ? "cart property setter" : "train property setter").setTraincartsWIKIHelp("TrainCarts/Signs/Property");
      if (!Permission.COMMAND_PROPERTIES.has(event.getPlayer()) && !Permission.COMMAND_GLOBALPROPERTIES.has(event.getPlayer())) {
         Localization.PROPERTY_NOPERM_ANY.message(event.getPlayer(), new String[0]);
         return false;
      } else {
         PropertyParseResult<Object> result = IPropertyRegistry.instance().parse((IProperties)null, event.getLine(2), (String)event.getLine(3));
         if (!result.hasPermission(event.getPlayer())) {
            Localization.PROPERTY_NOPERM.message(event.getPlayer(), new String[]{result.getName()});
            return false;
         } else {
            if (event.isTrainSign()) {
               opt.setDescription("set properties on the train above");
            } else if (event.isCartSign()) {
               opt.setDescription("set properties on the cart above");
            } else if (event.isRCSign()) {
               opt.setDescription("remotely set properties on the train specified");
            }

            if (!opt.handle(event)) {
               return false;
            } else {
               if (!result.isSuccessful()) {
                  event.getPlayer().sendMessage(result.getMessage());
               }

               return true;
            }
         }
      }
   }

   public boolean canSupportRC() {
      return true;
   }
}
