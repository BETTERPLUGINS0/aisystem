package com.bergerkiller.bukkit.tc.signactions;

import com.bergerkiller.bukkit.common.permissions.IPermissionEnum;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.standard.type.SignSkipOptions;
import com.bergerkiller.bukkit.tc.statements.Statement;
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class SignActionSkip extends TrainCartsSignAction {
   public SignActionSkip() {
      super("skip");
   }

   public void execute(SignActionEvent info) {
      if (info.isPowered()) {
         List<String> statements = this.getStatements(info);
         if (info.isCartSign() && info.isAction(SignActionType.REDSTONE_CHANGE, SignActionType.MEMBER_ENTER)) {
            if (!info.hasRailedMember()) {
               return;
            }

            if (Statement.hasMultiple((MinecartMember)info.getMember(), statements, info)) {
               info.getMember().getProperties().setSkipOptions(this.getOptions(info));
            }
         } else if (info.isTrainSign() && info.isAction(SignActionType.REDSTONE_CHANGE, SignActionType.GROUP_ENTER)) {
            if (!info.hasRailedMember()) {
               return;
            }

            if (Statement.hasMultiple((MinecartGroup)info.getGroup(), statements, info)) {
               info.getGroup().getProperties().setSkipOptions(this.getOptions(info));
            }
         } else if (info.isRCSign() && info.isAction(SignActionType.REDSTONE_ON)) {
            SignSkipOptions opt = this.getOptions(info);
            Iterator var4 = info.getRCTrainProperties().iterator();

            while(var4.hasNext()) {
               TrainProperties prop = (TrainProperties)var4.next();
               if (Statement.hasMultiple((MinecartGroup)prop.getHolder(), statements, info)) {
                  prop.setSkipOptions(opt);
               }
            }
         }

      }
   }

   public List<String> getStatements(SignActionEvent info) {
      String line1 = info.getLine(2);
      String line2 = info.getLine(3);
      if (line2.isEmpty()) {
         return line1.isEmpty() ? Collections.emptyList() : Collections.singletonList(line1);
      } else {
         String[] extraLines = info.getExtraLinesBelow();
         if (extraLines.length <= 0) {
            return line1.isEmpty() ? Collections.singletonList(line2) : Arrays.asList(line1, line2);
         } else {
            ArrayList<String> statements = new ArrayList(extraLines.length + 2);
            if (!line1.isEmpty()) {
               statements.add(line1);
            }

            statements.add(line2);
            String[] var6 = extraLines;
            int var7 = extraLines.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               String line = var6[var8];
               if (line.isEmpty()) {
                  break;
               }

               statements.add(line);
            }

            return statements;
         }
      }
   }

   public boolean build(SignChangeActionEvent event) {
      SignBuildOptions opt = SignBuildOptions.create().setPermission((IPermissionEnum)Permission.BUILD_SKIPPER).setName(event.isCartSign() ? "cart skipper" : "train skipper").setTraincartsWIKIHelp("TrainCarts/Signs/Skip");
      if (event.isTrainSign()) {
         opt.setDescription("tell a train to skip upcoming signs");
      } else if (event.isCartSign()) {
         opt.setDescription("tell a cart to skip upcoming signs");
      } else if (event.isRCSign()) {
         opt.setDescription("tell a remote train to skip signs");
      }

      return opt.handle(event);
   }

   public boolean canSupportRC() {
      return true;
   }

   private SignSkipOptions getOptions(SignActionEvent info) {
      String[] args = info.getLine(1).toLowerCase(Locale.ENGLISH).split(" ");
      args = StringUtil.remove(args, 0);
      int skipCtr = true;
      int ignoreCtr = 0;
      String filter = "";
      int skipCtr = 1;
      if (args.length >= 1) {
         if (!args[0].equals("none") && !args[0].equals("disable")) {
            if (!ParseUtil.isNumeric(args[0])) {
               filter = args[0];
               args = StringUtil.remove(args, 0);
            }

            if (args.length >= 2) {
               ignoreCtr = ParseUtil.parseInt(args[0], 0);
            }

            if (args.length >= 1) {
               skipCtr = ParseUtil.parseInt(args[args.length - 1], 1);
            }
         } else {
            skipCtr = 0;
         }
      }

      return SignSkipOptions.create(ignoreCtr, skipCtr, filter);
   }
}
