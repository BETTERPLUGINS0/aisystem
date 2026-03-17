package com.bergerkiller.bukkit.tc.signactions;

import com.bergerkiller.bukkit.common.offline.OfflineBlock;
import com.bergerkiller.bukkit.common.permissions.IPermissionEnum;
import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.tc.Localization;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartGroupStore;
import com.bergerkiller.bukkit.tc.controller.MinecartMemberStore;
import com.bergerkiller.bukkit.tc.controller.components.RailState;
import com.bergerkiller.bukkit.tc.controller.spawnable.SpawnableGroup;
import com.bergerkiller.bukkit.tc.controller.spawnable.SpawnableMember;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent;
import com.bergerkiller.bukkit.tc.signactions.spawner.SpawnSign;
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions;
import com.bergerkiller.bukkit.tc.utils.TrackWalkingPoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public class SignActionSpawn extends TrainCartsSignAction {
   private static Map<OfflineBlock, Long> cooldownSpawnTimesBySign = new HashMap();

   public SignActionSpawn() {
      super("spawn");
   }

   public boolean canSupportFakeSign(SignActionEvent info) {
      return SpawnSign.SpawnOptions.fromEvent(info).autoSpawnInterval == 0L;
   }

   public void execute(SignActionEvent info) {
      if (info.isAction(SignActionType.REDSTONE_ON, SignActionType.REDSTONE_OFF)) {
         SpawnSign sign = info.getTrainCarts().getSpawnSignManager().create(info);
         if (sign.isActive()) {
            sign.spawn(info);
            sign.resetSpawnTime();
         }

      }
   }

   public boolean build(SignChangeActionEvent event) {
      SignBuildOptions buildOpts = SignBuildOptions.create().setPermission((IPermissionEnum)Permission.BUILD_SPAWNER).setName("train spawner").setDescription("spawn trains on the tracks above when powered by redstone").setTraincartsWIKIHelp("TrainCarts/Signs/Spawner");
      if (!buildOpts.checkBuildPermission(event.getPlayer())) {
         return false;
      } else {
         SpawnSign sign = event.getTrainCarts().getSpawnSignManager().create(event);
         if (sign.hasInterval() && !Permission.SPAWNER_AUTOMATIC.handleMsg(event.getPlayer(), ChatColor.RED + "You do not have permission to use automatic signs")) {
            sign.remove();
            return false;
         } else if (!sign.getSpawnableGroup().checkSpawnPermissions(event.getPlayer())) {
            Localization.SPAWN_FORBIDDEN_CONTENTS.message(event.getPlayer(), new String[0]);
            sign.remove();
            return false;
         } else {
            if (event.isInteractive()) {
               buildOpts.showBuildMessage(event.getPlayer());
               if (sign.hasInterval()) {
                  event.getPlayer().sendMessage(ChatColor.YELLOW + "This spawner will automatically spawn trains every " + Util.getTimeString(sign.getInterval()) + " while powered");
               }
            }

            return true;
         }
      }
   }

   public void destroy(SignActionEvent info) {
      info.getTrainCarts().getSpawnSignManager().remove(info);
   }

   public static SpawnableGroup.SpawnLocationList spawn(SpawnSign spawnSign, SignActionEvent info) {
      if ((info.isTrainSign() || info.isCartSign()) && info.hasRails()) {
         SpawnableGroup spawnable = spawnSign.getSpawnableGroup();
         if (spawnable.getMembers().isEmpty()) {
            return null;
         } else if (TCConfig.maxCartsPerTrain >= 0 && spawnable.getMembers().size() > TCConfig.maxCartsPerTrain) {
            spawnSign.showFailParticles(Color.MAROON);
            return null;
         } else if (spawnable.isExceedingSpawnLimit()) {
            spawnSign.showFailParticles(Color.RED);
            return null;
         } else if (MinecartGroupStore.isPerWorldSpawnLimitReached(spawnSign.getLocation().getLoadedBlock(), spawnable.getMembers().size())) {
            spawnSign.showFailParticles(Color.ORANGE);
            return null;
         } else {
            if (TCConfig.spawnSignCooldown >= 0.0D) {
               Long lastSpawnTime = (Long)cooldownSpawnTimesBySign.get(spawnSign.getLocation());
               long cooldown = (long)(TCConfig.spawnSignCooldown * 1000.0D);
               long now = System.currentTimeMillis();
               if (lastSpawnTime != null && now - lastSpawnTime < cooldown) {
                  spawnSign.showFailParticles(Color.YELLOW);
                  return null;
               }

               cooldownSpawnTimesBySign.put(spawnSign.getLocation(), now);
            }

            RailState state = RailState.getSpawnState(info.getRailPiece());
            Vector railDirection = state.motionVector();
            boolean spawnA = info.isWatchedDirection(railDirection.clone().multiply(-1.0D));
            boolean spawnB = info.isWatchedDirection(railDirection);
            boolean isBothDirections;
            if (isBothDirections = spawnA && spawnB) {
               BlockFace face = Util.vecToFace(railDirection, false);
               spawnA = info.isPowered(face);
               spawnB = info.isPowered(face.getOppositeFace());
            }

            boolean useCentering;
            Vector spawnDirection;
            Vector opposite;
            if (spawnA && !spawnB) {
               spawnDirection = railDirection;
               useCentering = false;
            } else if (!spawnA && spawnB) {
               spawnDirection = railDirection.clone().multiply(-1.0D);
               useCentering = false;
            } else {
               if (FaceUtil.isVertical(Util.vecToFace(railDirection, false))) {
                  if (railDirection.getY() < 0.0D) {
                     spawnDirection = railDirection;
                  } else {
                     spawnDirection = railDirection.clone().multiply(-1.0D);
                  }
               } else {
                  opposite = FaceUtil.faceToVector(FaceUtil.rotate(info.getFacing(), -2));
                  if (railDirection.dot(opposite) >= 0.0D) {
                     spawnDirection = railDirection;
                  } else {
                     spawnDirection = railDirection.clone().multiply(-1.0D);
                  }
               }

               useCentering = true;
            }

            if (spawnable.getCenterMode() == SpawnableGroup.CenterMode.MIDDLE) {
               useCentering = true;
            } else if (spawnable.getCenterMode() == SpawnableGroup.CenterMode.LEFT || spawnable.getCenterMode() == SpawnableGroup.CenterMode.RIGHT) {
               useCentering = false;
            }

            SpawnableGroup.SpawnMode directionalSpawnMode = SpawnableGroup.SpawnMode.DEFAULT;
            if (spawnable.getCenterMode() == SpawnableGroup.CenterMode.LEFT) {
               directionalSpawnMode = SpawnableGroup.SpawnMode.REVERSE;
            }

            SpawnableGroup.SpawnLocationList spawnLocations = null;
            SpawnableGroup.SpawnLocationList spawnOpposite;
            if (useCentering) {
               spawnLocations = spawnable.findSpawnLocations(info.getRailPiece(), spawnDirection, SpawnableGroup.SpawnMode.CENTER);
               if (spawnLocations != null && !spawnLocations.can_move) {
                  opposite = spawnDirection.clone().multiply(-1.0D);
                  spawnOpposite = spawnable.findSpawnLocations(info.getRailPiece(), opposite, SpawnableGroup.SpawnMode.CENTER);
                  if (spawnOpposite != null && spawnOpposite.can_move) {
                     spawnDirection = opposite;
                     spawnLocations = spawnOpposite;
                  }
               }
            }

            if (spawnLocations == null) {
               spawnLocations = spawnable.findSpawnLocations(info.getRailPiece(), spawnDirection, directionalSpawnMode);
            }

            if (spawnLocations == null || !spawnLocations.can_move && isBothDirections) {
               opposite = spawnDirection.clone().multiply(-1.0D);
               spawnOpposite = spawnable.findSpawnLocations(info.getRailPiece(), opposite, directionalSpawnMode);
               if (spawnOpposite != null && (spawnLocations == null || spawnOpposite.can_move)) {
                  spawnDirection = opposite;
                  spawnLocations = spawnOpposite;
               }
            }

            if (spawnLocations == null && !useCentering) {
               spawnLocations = spawnable.findSpawnLocations(info.getRailPiece(), spawnDirection, SpawnableGroup.SpawnMode.CENTER);
            }

            if (spawnLocations == null) {
               spawnSign.showFailParticles(Color.BLUE);
               return null;
            } else {
               spawnLocations.loadChunks();
               if (spawnLocations.isOccupied()) {
                  spawnSign.showFailParticles(Color.PURPLE);
                  return null;
               } else {
                  MinecartGroup group = spawnable.spawn(spawnLocations);
                  double spawnForce = spawnSign.getSpawnForce();
                  if (group != null && spawnForce != 0.0D) {
                     Vector headDirection = ((SpawnableMember.SpawnLocation)spawnLocations.locations.get(spawnLocations.locations.size() - 1)).forward;
                     BlockFace launchDirection = Util.vecToFace(headDirection, false);
                     if (spawnForce < 0.0D) {
                        launchDirection = launchDirection.getOppositeFace();
                        spawnForce = -spawnForce;
                     }

                     group.head().getActions().addActionLaunch(launchDirection, 2.0D, spawnForce);
                  }

                  return spawnLocations;
               }
            }
         }
      } else {
         return null;
      }
   }

   /** @deprecated */
   @Deprecated
   public static List<Location> getSpawnPositions(Location startLoc, boolean atCenter, BlockFace directionFace, List<SpawnableMember> types) {
      return getSpawnPositions(startLoc, atCenter, FaceUtil.faceToVector(directionFace), types);
   }

   /** @deprecated */
   @Deprecated
   public static List<Location> getSpawnPositions(Location startLoc, boolean atCenter, Vector direction, List<SpawnableMember> types) {
      List<Location> result = new ArrayList(types.size());
      TrackWalkingPoint walker;
      if (atCenter && types.size() == 1) {
         if (MinecartMemberStore.getAt(startLoc) == null) {
            walker = new TrackWalkingPoint(startLoc, direction);
            Location firstPos = walker.state.positionLocation();
            walker.skipFirst();
            if (walker.moveFull()) {
               result.add(firstPos);
            }
         }
      } else {
         walker = new TrackWalkingPoint(startLoc, direction);
         walker.skipFirst();

         for(int i = 0; i < types.size(); ++i) {
            SpawnableMember type = (SpawnableMember)types.get(i);
            if (atCenter && i == 0) {
               if (!walker.move(0.0D)) {
                  break;
               }
            } else if (!walker.move(0.5D * type.getLength() - (i == 0 ? 0.5D : 0.0D))) {
               break;
            }

            result.add(walker.state.positionLocation());
            if (i == types.size() - 1) {
               break;
            }

            double cartGap = type.getCartCouplerLength() + ((SpawnableMember)types.get(i + 1)).getCartCouplerLength();
            if (!walker.move(0.5D * type.getLength() + cartGap)) {
               break;
            }
         }
      }

      return result;
   }
}
