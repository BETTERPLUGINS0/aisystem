package com.bergerkiller.bukkit.tc.signactions;

import com.bergerkiller.bukkit.common.collections.BlockMap;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.permissions.IPermissionEnum;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.tc.Direction;
import com.bergerkiller.bukkit.tc.DirectionStatement;
import com.bergerkiller.bukkit.tc.Localization;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.actions.GroupActionWaitPathFinding;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.components.RailJunction;
import com.bergerkiller.bukkit.tc.controller.components.RailPath;
import com.bergerkiller.bukkit.tc.controller.components.RailPiece;
import com.bergerkiller.bukkit.tc.events.MissingPathConnectionEvent;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent;
import com.bergerkiller.bukkit.tc.pathfinding.PathConnection;
import com.bergerkiller.bukkit.tc.pathfinding.PathNavigateEvent;
import com.bergerkiller.bukkit.tc.pathfinding.PathNode;
import com.bergerkiller.bukkit.tc.pathfinding.PathPredictEvent;
import com.bergerkiller.bukkit.tc.pathfinding.SignRoutingEvent;
import com.bergerkiller.bukkit.tc.properties.IProperties;
import com.bergerkiller.bukkit.tc.statements.Statement;
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.bukkit.block.Block;

public class SignActionSwitcher extends TrainCartsSignAction {
   private BlockMap<SignActionSwitcher.CounterState> switchedTimes = new BlockMap();

   private SignActionSwitcher.CounterState getSwitchedTimes(Block signblock) {
      SignActionSwitcher.CounterState i = (SignActionSwitcher.CounterState)this.switchedTimes.get(signblock);
      if (i == null) {
         i = new SignActionSwitcher.CounterState();
         this.switchedTimes.put(signblock, i);
      }

      return i;
   }

   private void cleanupCountersOnLeave(SignActionEvent info) {
      if (info.isAction(SignActionType.GROUP_LEAVE)) {
         SignActionSwitcher.CounterState state = (SignActionSwitcher.CounterState)this.switchedTimes.get(info.getBlock());
         if (state != null) {
            Iterator var3 = info.getGroup().iterator();

            while(var3.hasNext()) {
               MinecartMember<?> member = (MinecartMember)var3.next();
               state.syncLeave(member);
            }
         }
      }

   }

   private static List<DirectionStatement> parseDirectionStatements(SignActionEvent info) {
      List<DirectionStatement> statements = new ArrayList();
      if (!info.getLine(2).isEmpty() || !info.getLine(3).isEmpty()) {
         String left_str = Direction.IMPLICIT_LEFT.aliases()[0];
         if (info.getLine(2).isEmpty()) {
            statements.add(new DirectionStatement("default", left_str));
         } else {
            statements.add(new DirectionStatement(info.getLine(2), left_str));
         }

         String right_str = Direction.IMPLICIT_RIGHT.aliases()[0];
         if (info.getLine(3).isEmpty()) {
            statements.add(new DirectionStatement("default", right_str));
         } else {
            statements.add(new DirectionStatement(info.getLine(3), right_str));
         }
      }

      String[] var7 = info.getExtraLinesBelow();
      int var8 = var7.length;

      for(int var4 = 0; var4 < var8; ++var4) {
         String line = var7[var4];
         if (!line.isEmpty()) {
            DirectionStatement stat = new DirectionStatement(line, "");
            if (!stat.direction.isEmpty()) {
               statements.add(stat);
            }
         }
      }

      return statements;
   }

   public SignActionSwitcher() {
      super("switcher", "tag");
   }

   public void execute(SignActionEvent info) {
      (new SignActionSwitcher.SwitcherLogic(info)).run();
   }

   public boolean build(SignChangeActionEvent event) {
      if (event.isCartSign()) {
         return SignBuildOptions.create().setPermission((IPermissionEnum)Permission.BUILD_SWITCHER).setName("cart switcher").setDescription("switch between tracks based on properties of the cart above").setTraincartsWIKIHelp("TrainCarts/Signs/Switcher").handle(event);
      } else {
         return event.isTrainSign() ? SignBuildOptions.create().setPermission((IPermissionEnum)Permission.BUILD_SWITCHER).setName("train switcher").setDescription("switch between tracks based on properties of the train above").setTraincartsWIKIHelp("TrainCarts/Signs/Switcher").handle(event) : false;
      }
   }

   public boolean isRailSwitcher(SignActionEvent info) {
      return !TCConfig.onlyPoweredSwitchersDoPathFinding || !info.getHeader().isAlwaysOff();
   }

   public void predictPathFinding(SignActionEvent info, PathPredictEvent prediction) {
      (new SignActionSwitcher.SwitcherLogic(info)).predict(prediction);
   }

   public String getDescriptiveOutputName(SignActionEvent event) {
      return "Train activates switcher";
   }

   public void route(SignRoutingEvent event) {
      if (!TCConfig.onlyPoweredSwitchersDoPathFinding || !event.getHeader().isAlwaysOff()) {
         (new SignActionSwitcher.SwitcherLogic(event)).route(event);
      }
   }

   public boolean overrideFacing() {
      return true;
   }

   private static class CounterState {
      public int counter;
      public int startLength;
      public Set<UUID> uuidsToIgnore;

      private CounterState() {
         this.counter = 0;
         this.startLength = 0;
         this.uuidsToIgnore = Collections.emptySet();
      }

      public void syncCartSignEnter(MinecartGroup group, RailPiece railPiece) {
         if (TCConfig.switcherResetCountersOnFirstCart) {
            boolean isNewGroup = !this.isGroupTracked(group);
            this.addAll(group);
            if (isNewGroup) {
               this.startLength = group.size();
               if (TCConfig.switcherResetCountersOnFirstCart) {
                  this.counter = 0;
               }

               if (this.uuidsToIgnore.size() != group.size()) {
                  this.uuidsToIgnore.clear();
                  this.addAll(group);
                  if (railPiece != RailPiece.NONE) {
                     railPiece.members().stream().map(MinecartMember::getGroup).distinct().forEach(this::addAll);
                  }
               }
            }

         }
      }

      public void syncLeave(MinecartMember<?> member) {
         if (!this.uuidsToIgnore.isEmpty()) {
            this.uuidsToIgnore.remove(((CommonMinecart)member.getEntity()).getUniqueId());
            if (this.uuidsToIgnore.isEmpty()) {
               this.uuidsToIgnore = Collections.emptySet();
            }
         }

      }

      private void addAll(MinecartGroup group) {
         if (this.uuidsToIgnore.isEmpty()) {
            this.uuidsToIgnore = new HashSet();
         }

         Iterator var2 = group.iterator();

         while(var2.hasNext()) {
            MinecartMember<?> member = (MinecartMember)var2.next();
            this.uuidsToIgnore.add(((CommonMinecart)member.getEntity()).getUniqueId());
         }

      }

      private boolean isGroupTracked(MinecartGroup group) {
         Iterator var2 = group.iterator();

         MinecartMember member;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            member = (MinecartMember)var2.next();
         } while(!this.uuidsToIgnore.contains(((CommonMinecart)member.getEntity()).getUniqueId()));

         return true;
      }

      // $FF: synthetic method
      CounterState(Object x0) {
         this();
      }
   }

   private class SwitcherLogic {
      private final SignActionEvent info;
      private final List<DirectionStatement> statements;
      private final boolean hasFromDirections;
      private final boolean doCart;
      private final boolean doTrain;
      private final boolean canToggleRails;

      public SwitcherLogic(SignActionEvent info) {
         this.info = info;
         this.statements = SignActionSwitcher.parseDirectionStatements(info);
         boolean calcHasFromDirections = false;
         Iterator var4 = this.statements.iterator();

         while(var4.hasNext()) {
            DirectionStatement statement = (DirectionStatement)var4.next();
            if (!statement.isSwitchedFromSelf() && !statement.isDefault()) {
               calcHasFromDirections = true;
               break;
            }
         }

         boolean var10001;
         label40: {
            label64: {
               this.hasFromDirections = calcHasFromDirections;
               this.doTrain = info.isTrainSign() && info.isAction(SignActionType.GROUP_ENTER, SignActionType.GROUP_UPDATE);
               this.doCart = info.isCartSign() && info.isAction(SignActionType.MEMBER_ENTER, SignActionType.MEMBER_UPDATE);
               if (info.isCartSign()) {
                  if (info.isAction(SignActionType.MEMBER_ENTER)) {
                     break label64;
                  }
               } else if (info.isAction(SignActionType.GROUP_ENTER)) {
                  break label64;
               }

               if (!this.hasFromDirections || !info.isAction(SignActionType.REDSTONE_CHANGE) || !info.hasRails() || !info.isPowered()) {
                  var10001 = false;
                  break label40;
               }
            }

            var10001 = true;
         }

         this.canToggleRails = var10001;
      }

      public void route(SignRoutingEvent event) {
         if (!this.statements.isEmpty() && this.info.isEnterActivated()) {
            DirectionStatement activeDirection = this.selectStatement(true, false);
            if (activeDirection != null) {
               this.predictRails(event, activeDirection);
               return;
            }
         }

         event.setRouteSwitchable(true);
      }

      public void predict(PathPredictEvent prediction) {
         if (this.canToggleRails) {
            boolean facing = this.info.isEnterActivated();
            DirectionStatement activeDirection = null;
            if (!this.statements.isEmpty() && facing) {
               activeDirection = this.selectStatement(false, false);
               if (activeDirection != null && (!this.canToggleRails || !this.info.isPowered())) {
                  activeDirection = null;
               }

               if (activeDirection != null && !activeDirection.isDefault()) {
                  this.predictRails(prediction, activeDirection);
                  return;
               }
            }

            boolean handlePathfinding = true;
            if (TCConfig.onlyPoweredSwitchersDoPathFinding && !this.info.isPowered()) {
               handlePathfinding = false;
            }

            if (TCConfig.onlyEmptySwitchersDoPathFinding && !this.statements.isEmpty()) {
               handlePathfinding = false;
            }

            if (!handlePathfinding || !this.predictPathFinding(prediction, facing)) {
               if (activeDirection != null) {
                  this.predictRails(prediction, activeDirection);
               }

            }
         }
      }

      public void run() {
         SignActionSwitcher.this.cleanupCountersOnLeave(this.info);
         if (!this.doTrain && !this.doCart) {
            if (this.info.isAction(SignActionType.MEMBER_LEAVE) && this.info.isCartSign()) {
               this.info.setLevers(false);
               return;
            }

            if (this.info.isAction(SignActionType.GROUP_LEAVE) && this.info.isTrainSign()) {
               this.info.setLevers(false);
               return;
            }

            if (!this.canToggleRails) {
               return;
            }
         }

         boolean hasMember = this.info.hasRailedMember();
         boolean facing = !hasMember || this.info.isFacing();
         DirectionStatement activeDirection = null;
         if (facing) {
            if (this.statements.isEmpty()) {
               if (hasMember) {
                  this.info.setLevers(true);
               }
            } else {
               activeDirection = this.selectStatement(false, true);
               if (hasMember) {
                  this.info.setLevers(activeDirection != null && !activeDirection.isDefault());
               }

               if (activeDirection != null && (!this.canToggleRails || !this.info.isPowered())) {
                  activeDirection = null;
               }

               if (activeDirection != null && !activeDirection.isDefault()) {
                  this.switchRails(activeDirection);
                  return;
               }
            }
         }

         boolean handlePathfinding = true;
         if (TCConfig.onlyPoweredSwitchersDoPathFinding && !this.info.isPowered()) {
            handlePathfinding = false;
         }

         if (TCConfig.onlyEmptySwitchersDoPathFinding && !this.statements.isEmpty()) {
            handlePathfinding = false;
         }

         if (!handlePathfinding || !this.handlePathFinding(facing)) {
            if (activeDirection != null) {
               this.switchRails(activeDirection);
            }

         }
      }

      private void switchRails(DirectionStatement direction) {
         if (direction.isSwitchedFromSelf()) {
            this.info.setRailsTo(direction.direction);
         } else {
            this.info.setRailsFromTo(direction.directionFrom, direction.direction);
         }

      }

      private void predictRails(PathNavigateEvent navigateEvent, DirectionStatement direction) {
         RailJunction a = this.info.findJunction(direction.direction);
         RailJunction b = direction.isSwitchedFromSelf() ? null : this.info.findJunction(direction.directionFrom);
         if (b == null) {
            if (a == null) {
               return;
            }

            navigateEvent.setSwitchedJunction(a);
         } else if (a == null) {
            navigateEvent.setSwitchedJunction(b);
         } else {
            RailPath.Position pos = navigateEvent.railState().position();
            if (a.position().motDot(pos) > b.position().motDot(pos)) {
               navigateEvent.setSwitchedJunction(a);
            } else {
               navigateEvent.setSwitchedJunction(b);
            }
         }

      }

      private void predictRailsTo(PathNavigateEvent prediction, String name) {
         RailJunction junction = this.info.findJunction(name);
         if (junction != null) {
            prediction.setSwitchedJunction(junction);
         }

      }

      private boolean handlePathFinding(boolean facing) {
         if (this.info.isAction(SignActionType.MEMBER_ENTER, SignActionType.GROUP_ENTER) && (facing || !this.info.isWatchedDirectionsDefined())) {
            PathNode node = PathNode.getOrCreate(this.info);
            if (node != null) {
               String destination = null;
               IProperties prop = null;
               if (this.doCart && this.info.hasMember()) {
                  prop = this.info.getMember().getProperties();
               } else if (this.doTrain && this.info.hasGroup()) {
                  prop = this.info.getGroup().getProperties();
               }

               if (prop != null) {
                  destination = ((IProperties)prop).getDestination();
                  ((IProperties)prop).setLastPathNode(node.getName());
               }

               if (!LogicUtil.nullOrEmpty(destination) && !node.containsName(destination)) {
                  if (this.info.getTrainCarts().getPathProvider().isProcessing()) {
                     double currentForce = this.info.getGroup().getAverageForce();
                     this.info.getGroup().getActions().addAction(new GroupActionWaitPathFinding(this.info, node, destination));
                     this.info.getMember().getActions().addActionLaunch(this.info.getMember().getDirectionFrom(), 1.0D, currentForce);
                     this.info.getGroup().stop();
                  } else {
                     PathConnection conn = node.findConnection(destination);
                     if (conn != null) {
                        if (this.canToggleRails) {
                           this.info.setRailsTo(conn.junctionName);
                        }
                     } else {
                        CommonUtil.callEvent(new MissingPathConnectionEvent(this.info.getRailPiece(), node, this.info.getGroup(), destination));
                        Localization.PATHING_FAILED.broadcast(this.info.getGroup(), destination);
                     }
                  }

                  return true;
               }
            }
         }

         return false;
      }

      private boolean predictPathFinding(PathPredictEvent prediction, boolean facing) {
         if (facing || !this.info.isWatchedDirectionsDefined()) {
            PathNode node = PathNode.getOrCreate(this.info);
            if (node != null) {
               if (this.info.getTrainCarts().getPathProvider().isProcessing()) {
                  prediction.setSpeedLimit(0.0D);
               } else {
                  String destination = null;
                  if (this.doCart) {
                     destination = this.info.getMember().getProperties().getDestination();
                  } else if (this.doTrain) {
                     destination = this.info.getGroup().getProperties().getDestination();
                  }

                  if (!LogicUtil.nullOrEmpty(destination) && !node.containsName(destination)) {
                     PathConnection conn = node.findConnection(destination);
                     if (conn != null) {
                        this.predictRailsTo(prediction, conn.junctionName);
                     }
                  }
               }
            }
         }

         return false;
      }

      private DirectionStatement selectStatement(boolean isPathRouting, boolean incrementCounters) {
         boolean hasMember = this.info.hasRailedMember();
         if (this.statements.isEmpty()) {
            if (hasMember) {
               this.info.setLevers(true);
            }

            return null;
         } else {
            int maxcount = 0;
            SignActionSwitcher.CounterState signcounter = null;
            Iterator var6 = this.statements.iterator();

            DirectionStatement dir;
            while(var6.hasNext()) {
               dir = (DirectionStatement)var6.next();
               if (dir.hasCounter()) {
                  if (signcounter == null) {
                     signcounter = SignActionSwitcher.this.getSwitchedTimes(this.info.getBlock());
                     if (this.info.isCartSign() && incrementCounters && this.info.hasGroup()) {
                        signcounter.syncCartSignEnter(this.info.getGroup(), this.info.getRailPiece());
                     }
                  }

                  maxcount += dir.counter.get(signcounter.startLength);
               }
            }

            int counter = 0;
            if (signcounter != null && incrementCounters) {
               if (this.info.isAction(SignActionType.MEMBER_ENTER, SignActionType.GROUP_ENTER)) {
                  ++signcounter.counter;
               } else if (this.info.isAction(SignActionType.REDSTONE_ON) && this.hasFromDirections) {
                  ++signcounter.counter;
               }

               if (signcounter.counter > maxcount) {
                  signcounter.counter = 1;
               }

               counter = 1;
            }

            dir = null;
            Iterator var8 = this.statements.iterator();

            DirectionStatement stat;
            while(var8.hasNext()) {
               stat = (DirectionStatement)var8.next();
               if (!stat.isDefault()) {
                  if (stat.hasCounter()) {
                     if (isPathRouting) {
                        return null;
                     }

                     if ((counter += stat.counter.get(signcounter.startLength)) > signcounter.counter) {
                        dir = stat;
                        break;
                     }
                  }

                  if (isPathRouting) {
                     Statement.MatchResult result = Statement.Matcher.of(stat.text).withSignEvent(this.info).match();
                     if (!result.isConstant()) {
                        return null;
                     }

                     if (result.has()) {
                        dir = stat;
                        break;
                     }
                  }

                  if ((!this.doCart || !stat.has(this.info, this.info.getMember())) && (!this.doTrain || !stat.has(this.info, this.info.getGroup()))) {
                     if (stat.isSwitchedFromSelf() || !stat.has(this.info, (MinecartMember)null)) {
                        continue;
                     }

                     dir = stat;
                     break;
                  }

                  dir = stat;
                  break;
               }
            }

            if (dir == null && !isPathRouting) {
               label144: {
                  var8 = this.statements.iterator();

                  do {
                     do {
                        if (!var8.hasNext()) {
                           break label144;
                        }

                        stat = (DirectionStatement)var8.next();
                     } while(!stat.isDefault());
                  } while(!hasMember && stat.isSwitchedFromSelf());

                  dir = stat;
               }
            }

            if (dir != null && dir.direction.isEmpty()) {
               dir = null;
            }

            return dir;
         }
      }
   }
}
