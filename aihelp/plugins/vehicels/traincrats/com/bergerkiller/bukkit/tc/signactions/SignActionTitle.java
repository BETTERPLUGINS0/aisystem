package com.bergerkiller.bukkit.tc.signactions;

import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.permissions.IPermissionEnum;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent;
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.entity.Player;

public class SignActionTitle extends TrainCartsSignAction {
   private static void sendTitle(MinecartGroup group, SignActionTitle.TitleMessage message) {
      Iterator var2 = group.iterator();

      while(var2.hasNext()) {
         MinecartMember<?> member = (MinecartMember)var2.next();
         sendTitle(member, message);
      }

   }

   private static void sendTitle(MinecartMember<?> member, SignActionTitle.TitleMessage message) {
      Iterator var2 = ((CommonMinecart)member.getEntity()).getPlayerPassengers().iterator();

      while(var2.hasNext()) {
         Player player = (Player)var2.next();
         player.sendTitle(message.title, message.subtitle, message.fadeIn, message.stay, message.fadeOut);
      }

   }

   public SignActionTitle() {
      super("title");
   }

   public void execute(SignActionEvent info) {
      SignActionTitle.TitleMessage message = new SignActionTitle.TitleMessage(info);
      if (info.isTrainSign() && info.isAction(SignActionType.GROUP_ENTER, SignActionType.REDSTONE_ON)) {
         if (info.hasRailedMember() && info.isPowered()) {
            sendTitle(info.getGroup(), message);
         }
      } else if (info.isCartSign() && info.isAction(SignActionType.MEMBER_ENTER, SignActionType.REDSTONE_ON)) {
         if (info.hasRailedMember() && info.isPowered()) {
            sendTitle(info.getMember(), message);
         }
      } else if (info.isRCSign() && info.isAction(SignActionType.REDSTONE_ON)) {
         Iterator var3 = info.getRCTrainGroups().iterator();

         while(var3.hasNext()) {
            MinecartGroup group = (MinecartGroup)var3.next();
            sendTitle(group, message);
         }
      }

   }

   public boolean canSupportRC() {
      return true;
   }

   public boolean build(SignChangeActionEvent event) {
      return SignBuildOptions.create().setPermission((IPermissionEnum)Permission.BUILD_TITLE).setName("title").setDescription(event.isRCSign() ? "remotely send title to all the players in the train" : "send a title to players in a train").setTraincartsWIKIHelp("TrainCarts/Signs/Title").handle(event.getPlayer());
   }

   private static class TitleMessage {
      public String title;
      public String subtitle;
      public int fadeIn = 10;
      public int stay = 70;
      public int fadeOut = 10;
      private final Pattern TITLE_PATTERN = Pattern.compile("title\\s(\\d+)(?:\\s(\\d+))?(?:\\s(\\d+))?");

      public TitleMessage(SignActionEvent info) {
         this.title = TrainCarts.getMessage(info.getLine(2));
         this.subtitle = TrainCarts.getMessage(info.getLine(3));
         Matcher matcher = this.TITLE_PATTERN.matcher(info.getLine(1));
         if (matcher.find()) {
            this.fadeIn = ParseUtil.parseInt(matcher.group(1), 10);
            this.stay = ParseUtil.parseInt(matcher.group(2), 70);
            this.fadeOut = ParseUtil.parseInt(matcher.group(3), 10);
         }

      }
   }
}
