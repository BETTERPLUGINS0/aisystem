package com.bergerkiller.bukkit.tc.signactions;

import com.bergerkiller.bukkit.common.bases.IntVector3;
import com.bergerkiller.bukkit.common.permissions.IPermissionEnum;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.controller.components.RailPath;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent;
import com.bergerkiller.bukkit.tc.pathfinding.PathPredictEvent;
import com.bergerkiller.bukkit.tc.signactions.mutex.MutexZoneCache;
import com.bergerkiller.bukkit.tc.signactions.mutex.MutexZonePath;
import com.bergerkiller.bukkit.tc.signactions.mutex.MutexZoneSlotType;
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions;
import java.util.Objects;

public class SignActionPathingMutex extends TrainCartsSignAction {
   public SignActionPathingMutex() {
      super("pmutex", "spmutex", "psmutex", "pathmutex", "pathingmutex");
   }

   public void predictPathFinding(SignActionEvent info, PathPredictEvent prediction) {
      if (info.isEnterActivated() && info.isPowered()) {
         MutexZonePath path = MutexZoneCache.getOrCreatePathingMutex(info.getTrackedSign(), prediction.group(), prediction.railState().positionOfflineBlock().getPosition(), (opt) -> {
            return this.loadOptions(info, opt);
         });
         path.onUsed(prediction.group());
         prediction.trackBlock((p, d) -> {
            RailPath var10000 = p.railPath();
            IntVector3 var10001 = p.railPiece().blockPosition();
            Objects.requireNonNull(path);
            var10000.forAllBlocks(var10001, path::addBlock);
            return true;
         }, path, path.getMaxDistance());
      }
   }

   private MutexZonePath.OptionsBuilder loadOptions(SignActionEvent info, MutexZonePath.OptionsBuilder opt) {
      opt.type(info.isType("spmutex", "psmutex") ? MutexZoneSlotType.SMART : MutexZoneSlotType.NORMAL);
      String options = info.getLine(1);
      int firstSpace = options.indexOf(32);
      if (firstSpace != -1) {
         boolean hasDistance = false;
         String[] var6 = options.substring(firstSpace + 1).split(" ");
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            String part = var6[var8];
            if (!part.isEmpty()) {
               if (!hasDistance) {
                  opt.maxDistance(ParseUtil.parseDouble(part, opt.maxDistance()));
                  hasDistance = true;
               } else {
                  opt.spacing(ParseUtil.parseDouble(part, opt.spacing()));
               }
            }
         }
      }

      String name = info.getLine(2).trim();
      if (!name.isEmpty()) {
         name = info.getWorld().getUID().toString() + "_" + name;
      }

      opt.name(name);
      opt.statement(info.getLine(3).trim());
      return opt;
   }

   public void execute(SignActionEvent info) {
   }

   public String getDescriptiveOutputName(SignActionEvent event) {
      return "Train is activated pathing mutex";
   }

   public boolean build(SignChangeActionEvent event) {
      return SignBuildOptions.create().setPermission((IPermissionEnum)Permission.BUILD_MUTEX).setName("pathing mutex zone").setDescription("prevent more than one train entering a stretch of track ahead").setTraincartsWIKIHelp("TrainCarts/Signs/Mutex").handle(event);
   }
}
