package com.bergerkiller.bukkit.tc.signactions;

import com.bergerkiller.bukkit.common.permissions.IPermissionEnum;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.attachments.api.Attachment;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentNameLookup;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent;
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SignActionEffect extends TrainCartsSignAction {
   public SignActionEffect() {
      super("effect");
   }

   public void execute(SignActionEvent info) {
      if (info.isPowered()) {
         if (info.isTrainSign() && info.isAction(SignActionType.REDSTONE_ON, SignActionType.GROUP_ENTER) && info.hasGroup()) {
            SignActionEffect.EffectAction.parse(info).run(info.getGroup().getAttachments().getNameLookup());
         } else if (info.isCartSign() && info.isAction(SignActionType.REDSTONE_ON, SignActionType.MEMBER_ENTER) && info.hasMember()) {
            SignActionEffect.EffectAction.parse(info).run(info.getMember().getAttachments().getNameLookup());
         } else if (info.isRCSign() && info.isAction(SignActionType.REDSTONE_ON)) {
            SignActionEffect.EffectAction action = SignActionEffect.EffectAction.parse(info);
            Iterator var3 = info.getRCTrainGroups().iterator();

            while(var3.hasNext()) {
               MinecartGroup group = (MinecartGroup)var3.next();
               action.run(group.getAttachments().getNameLookup());
            }
         }

      }
   }

   public boolean build(SignChangeActionEvent event) {
      SignBuildOptions opt = SignBuildOptions.create().setPermission((IPermissionEnum)Permission.BUILD_EFFECT).setName(event.isCartSign() ? "cart effect player" : "train effect player").setTraincartsWIKIHelp("TrainCarts/Signs/Effect");
      if (event.isTrainSign()) {
         opt.setDescription("play effects configured in attachments of all carts of the train");
      } else if (event.isCartSign()) {
         opt.setDescription("play effects configured in attachments of the cart");
      } else if (event.isRCSign()) {
         opt.setDescription("remotely play effects configured in attachments of all carts of the train");
      }

      return opt.handle(event);
   }

   public boolean canSupportRC() {
      return true;
   }

   private static class EffectAction {
      public final Consumer<Attachment.EffectAttachment> action;
      public final List<String> effects;

      public static SignActionEffect.EffectAction parse(SignActionEvent event) {
         return new SignActionEffect.EffectAction(event);
      }

      private EffectAction(SignActionEvent event) {
         String[] args = StringUtil.getAfter(event.getLine(1), " ").trim().split(" ", -1);
         double speed = 1.0D;
         double volume = 1.0D;
         boolean decodedSpeed = false;
         boolean stop = false;
         String[] var9 = args;
         int var10 = args.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            String arg = var9[var11];
            if (arg.equalsIgnoreCase("stop")) {
               stop = true;
               break;
            }

            if (ParseUtil.isNumeric(arg)) {
               if (decodedSpeed) {
                  volume = ParseUtil.parseDouble(arg, 1.0D);
               } else {
                  decodedSpeed = true;
                  speed = ParseUtil.parseDouble(arg, 1.0D);
               }
            }
         }

         if (stop) {
            this.action = Attachment.EffectAttachment::stopEffect;
         } else {
            Attachment.EffectAttachment.EffectOptions opt = Attachment.EffectAttachment.EffectOptions.of(volume, speed);
            this.action = (e) -> {
               e.playEffect(opt);
            };
         }

         this.effects = (List)Stream.of(event.getLine(2), event.getLine(3)).map(String::trim).filter((s) -> {
            return !s.isEmpty();
         }).collect(Collectors.toList());
      }

      public void run(AttachmentNameLookup lookup) {
         Iterator var2 = this.effects.iterator();

         while(var2.hasNext()) {
            String effect = (String)var2.next();
            lookup.getOfType(effect, Attachment.EffectAttachment.class).forEach(this.action);
         }

      }
   }
}
