package com.bergerkiller.bukkit.tc.signactions;

import com.bergerkiller.bukkit.common.bases.IntVector3;
import com.bergerkiller.bukkit.common.permissions.IPermissionEnum;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent;
import com.bergerkiller.bukkit.tc.signactions.mutex.MutexSignMetadata;
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions;
import org.bukkit.ChatColor;

public class SignActionMutex extends TrainCartsSignAction {
   public SignActionMutex() {
      super("mutex", "smartmutex", "smutex");
   }

   public void execute(SignActionEvent info) {
   }

   public boolean canSupportFakeSign(SignActionEvent info) {
      return false;
   }

   public boolean build(SignChangeActionEvent event) {
      MutexSignMetadata meta = MutexSignMetadata.fromSign(event);
      IntVector3 dim = meta.end.subtract(meta.start);
      if (dim.x <= TCConfig.maxMutexSize && dim.y <= TCConfig.maxMutexSize && dim.z <= TCConfig.maxMutexSize) {
         return event.isType(new String[]{"smartmutex", "smutex"}) ? SignBuildOptions.create().setPermission((IPermissionEnum)Permission.BUILD_MUTEX).setName("smart mutex zone").setDescription("prevent more than one train occupying the same rail blocks within a zone").setTraincartsWIKIHelp("TrainCarts/Signs/Mutex").handle(event) : SignBuildOptions.create().setPermission((IPermissionEnum)Permission.BUILD_MUTEX).setName("mutex zone").setDescription("prevent more than one train entering a zone").setTraincartsWIKIHelp("TrainCarts/Signs/Mutex").handle(event);
      } else {
         event.getPlayer().sendMessage(ChatColor.RED + "Mutex zone is too large! Maximum size is " + TCConfig.maxMutexSize);
         return false;
      }
   }

   public String getDescriptiveOutputName(SignActionEvent event) {
      return "Train is inside mutex zone";
   }

   public void loadedChanged(SignActionEvent info, boolean loaded) {
      if (loaded && info.getTrackedSign().isRealSign()) {
         info.getTrainCarts().getOfflineSigns().computeIfAbsent(info.getTrackedSign(), MutexSignMetadata.class, (offline) -> {
            return MutexSignMetadata.fromSign(info);
         });
      }

   }
}
