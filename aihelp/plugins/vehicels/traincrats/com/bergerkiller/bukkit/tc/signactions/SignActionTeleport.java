package com.bergerkiller.bukkit.tc.signactions;

import com.bergerkiller.bukkit.common.permissions.IPermissionEnum;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent;
import com.bergerkiller.bukkit.tc.portals.PortalDestination;
import com.bergerkiller.bukkit.tc.portals.TCPortalManager;
import com.bergerkiller.bukkit.tc.portals.plugins.MyWorldsPortalsProvider;
import com.bergerkiller.bukkit.tc.rails.type.RailType;
import com.bergerkiller.bukkit.tc.utils.BlockTimeoutMap;
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions;
import com.bergerkiller.bukkit.tc.utils.TrackIterator;
import java.util.ArrayList;
import java.util.Collection;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class SignActionTeleport extends SignAction {
   private BlockTimeoutMap teleportTimes = new BlockTimeoutMap();

   public boolean canSupportRC() {
      return true;
   }

   public boolean verify(SignActionEvent info) {
      return this.matchMyWorlds(info) || super.verify(info);
   }

   public boolean match(SignActionEvent info) {
      return this.matchMyWorlds(info) || info.isType("teleport");
   }

   public void execute(SignActionEvent info) {
      if (info.isAction(SignActionType.GROUP_ENTER, SignActionType.REDSTONE_ON) && info.isPowered()) {
         final MinecartGroup group = null;
         if (!info.isRCSign()) {
            if (!info.hasGroup()) {
               return;
            }

            group = info.getGroup();
         } else {
            Collection<MinecartGroup> groups = info.getRCTrainGroups();
            if (groups.isEmpty()) {
               return;
            }

            group = (MinecartGroup)groups.iterator().next();
         }

         String destName;
         if (this.matchMyWorlds(info)) {
            if (!TCPortalManager.isAvailable("My_Worlds")) {
               return;
            }

            destName = MyWorldsPortalsProvider.getPortalDestination(info.getLocation());
         } else {
            destName = info.getLine(2);
         }

         if (destName != null) {
            PortalDestination dest = TCPortalManager.getPortalDestination(group.getWorld(), destName);
            if (dest != null && dest.getRailsBlock() != null) {
               if (info.hasRails() && this.teleportTimes.isMarked(info.getRails(), 2000L)) {
                  return;
               }

               this.teleportTimes.mark(dest.getRailsBlock());
               ArrayList<BlockFace> possibleDirs = new ArrayList();
               ArrayList<TrackIterator> possibleIters = new ArrayList();
               BlockFace[] railDirections = RailType.getType(dest.getRailsBlock()).getPossibleDirections(dest.getRailsBlock());
               BlockFace[] spawnDirection = railDirections;
               int n = railDirections.length;

               int num_succ;
               for(num_succ = 0; num_succ < n; ++num_succ) {
                  BlockFace dir = spawnDirection[num_succ];
                  if (!dest.hasDirections() || LogicUtil.contains(dir, dest.getDirections())) {
                     possibleDirs.add(dir);
                     possibleIters.add(new TrackIterator(dest.getRailsBlock(), dir));
                  }
               }

               spawnDirection = null;
               final BlockFace spawnDirection;
               if (possibleIters.isEmpty()) {
                  if (railDirections.length > 0) {
                     spawnDirection = railDirections[0];
                  } else {
                     if (!dest.hasDirections()) {
                        return;
                     }

                     spawnDirection = dest.getDirections()[0];
                  }
               } else {
                  spawnDirection = (BlockFace)possibleDirs.get(0);
                  if (possibleDirs.size() > 1) {
                     for(n = 0; n < 30; ++n) {
                        num_succ = 0;

                        for(int i = 0; i < possibleIters.size(); ++i) {
                           TrackIterator iter = (TrackIterator)possibleIters.get(i);
                           if (iter.hasNext()) {
                              iter.next();
                              ++num_succ;
                              spawnDirection = (BlockFace)possibleDirs.get(i);
                           }
                        }

                        if (num_succ <= 1) {
                           break;
                        }
                     }
                  }
               }

               if (dest.getRailsBlock().getWorld() == group.getWorld()) {
                  group.teleportAndGo(dest.getRailsBlock(), spawnDirection);
               } else {
                  final Block destRail = dest.getRailsBlock();
                  CommonUtil.nextTick(new Runnable() {
                     public void run() {
                        group.teleportAndGo(destRail, spawnDirection);
                     }
                  });
               }
            }

         }
      }
   }

   public boolean build(SignChangeActionEvent event) {
      if (this.matchMyWorlds(event) && !TCPortalManager.isAvailable("My_Worlds")) {
         event.getPlayer().sendMessage(ChatColor.RED + "MyWorlds" + ChatColor.YELLOW + " is not enabled on this server. Teleporter signs will not function as a result.");
         return false;
      } else {
         return SignBuildOptions.create().setPermission((IPermissionEnum)Permission.BUILD_TELEPORTER).setName("train teleporter").setDescription("teleport trains large distances to another teleporter sign").setTraincartsWIKIHelp("TrainCarts/Signs/Teleporter").setShowBuildMessage(event.hasRails()).handle(event);
      }
   }

   private boolean matchMyWorlds(SignActionEvent info) {
      return info.getHeader().getModeText().equals("portal");
   }
}
