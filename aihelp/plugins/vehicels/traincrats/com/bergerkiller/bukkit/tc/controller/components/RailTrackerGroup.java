package com.bergerkiller.bukkit.tc.controller.components;

import com.bergerkiller.bukkit.common.bases.IntVector3;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.rails.RailLookup;
import com.bergerkiller.bukkit.tc.rails.type.RailType;
import com.bergerkiller.bukkit.tc.utils.TrackWalkingPoint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class RailTrackerGroup extends RailTracker {
   private final MinecartGroup owner;
   private final ArrayList<RailTracker.TrackedRail> prevRails = new ArrayList();
   private final ArrayList<RailTracker.TrackedRail> rails = new ArrayList();

   public RailTrackerGroup(MinecartGroup owner) {
      this.owner = owner;
   }

   public void unload() {
      this.rails.forEach(RailTracker.TrackedRail::handleMemberRemove);
      this.rails.clear();
      this.prevRails.clear();
   }

   public void removeMemberRails(MinecartMember<?> member) {
      removeMemberRails(this.prevRails, member);
      removeMemberRails(this.rails, member);
   }

   private static void removeMemberRails(List<RailTracker.TrackedRail> rails, MinecartMember<?> member) {
      Iterator iter = rails.iterator();

      while(iter.hasNext()) {
         RailTracker.TrackedRail rail = (RailTracker.TrackedRail)iter.next();
         if (rail.member == member) {
            if (rail.memberAddedToRailPiece) {
               rail.handleMemberRemove();
            }

            iter.remove();
         }
      }

   }

   public void reverseRailData() {
      Collections.reverse(this.rails);
      Iterator var1 = this.rails.iterator();

      while(var1.hasNext()) {
         RailTracker.TrackedRail rail = (RailTracker.TrackedRail)var1.next();
         rail.state.position().invertMotion();
         rail.state.initEnterDirection();
      }

   }

   public List<RailTracker.TrackedRail> getRailInformation() {
      return this.rails;
   }

   public boolean isOnRails(Block railsBlock) {
      return this.getMemberFromRails(railsBlock) != null;
   }

   public MinecartMember<?> getMemberFromRails(Block railsBlock) {
      return railsBlock.getWorld() != this.owner.getWorld() ? null : this.getMemberFromRails(new IntVector3(railsBlock));
   }

   public MinecartMember<?> getMemberFromRails(IntVector3 railsBlockPosition) {
      Iterator var2 = this.rails.iterator();

      RailTracker.TrackedRail info;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         info = (RailTracker.TrackedRail)var2.next();
      } while(!railsBlockPosition.equals(info.state.railPiece().blockPosition()));

      return info.member;
   }

   public void refresh() {
      this.prevRails.clear();
      this.prevRails.addAll(this.rails);
      this.rails.clear();
      this.refreshFrom(this.owner.size() - 1, false);
      if (TCConfig.railTrackerDebugEnabled) {
         List<RailTracker.TrackedRail> behindRails = new ArrayList();
         List<RailTracker.TrackedRail> midRails = new ArrayList(this.rails);
         List<RailTracker.TrackedRail> aheadRails = new ArrayList();
         this.calcWheelTracks();
         boolean gotToAhead = false;
         Iterator var5 = this.rails.iterator();

         while(var5.hasNext()) {
            RailTracker.TrackedRail rail = (RailTracker.TrackedRail)var5.next();
            if (midRails.contains(rail)) {
               gotToAhead = true;
            } else if (gotToAhead) {
               aheadRails.add(rail);
            } else {
               behindRails.add(rail);
            }
         }

         double theta;
         int i;
         Location loc;
         for(i = 0; i < behindRails.size(); ++i) {
            loc = ((RailTracker.TrackedRail)behindRails.get(i)).state.positionLocation();
            theta = (double)i / (double)(behindRails.size() - 1);
            Util.spawnDustParticle(loc, 0.5D * theta + 0.5D, 0.0D, 0.0D);
         }

         for(i = 0; i < midRails.size(); ++i) {
            loc = ((RailTracker.TrackedRail)midRails.get(i)).state.positionLocation();
            theta = (double)i / (double)(midRails.size() - 1);
            Util.spawnDustParticle(loc, 0.5D * (1.0D - theta), 0.5D * theta, 1.0D);
         }

         for(i = 0; i < aheadRails.size(); ++i) {
            loc = ((RailTracker.TrackedRail)aheadRails.get(i)).state.positionLocation();
            theta = (double)i / (double)(aheadRails.size() - 1);
            Util.spawnDustParticle(loc, 0.0D, 0.5D * (1.0D - theta) + 0.5D, 0.0D);
         }
      } else {
         this.calcWheelTracks();
      }

      Collections.reverse(this.rails);
      this.owner.getSignTracker().updatePosition();
      Iterator var9;
      RailTracker.TrackedRail prevRail;
      if (this.prevRails.isEmpty() && !this.rails.isEmpty()) {
         var9 = this.owner.iterator();

         while(var9.hasNext()) {
            MinecartMember<?> member = (MinecartMember)var9.next();
            RailLookup.removeMemberFromAll(member);
         }

         var9 = this.rails.iterator();

         while(var9.hasNext()) {
            prevRail = (RailTracker.TrackedRail)var9.next();
            prevRail.handleMemberAdd();
         }
      } else {
         var9 = this.prevRails.iterator();

         label84:
         while(true) {
            do {
               if (!var9.hasNext()) {
                  var9 = this.rails.iterator();

                  while(var9.hasNext()) {
                     prevRail = (RailTracker.TrackedRail)var9.next();
                     if (!prevRail.memberAddedToRailPiece) {
                        prevRail.handleMemberAdd();
                     }
                  }
                  break label84;
               }

               prevRail = (RailTracker.TrackedRail)var9.next();
            } while(!prevRail.memberAddedToRailPiece);

            MinecartMember<?> memberToFind = prevRail.member;
            Iterator newRailIter = this.rails.iterator();

            label81:
            while(newRailIter.hasNext()) {
               RailTracker.TrackedRail newRail = (RailTracker.TrackedRail)newRailIter.next();
               if (newRail.member == memberToFind) {
                  while(true) {
                     if (prevRail.state.isSameRails(newRail.state)) {
                        prevRail.memberAddedToRailPiece = false;
                        newRail.memberAddedToRailPiece = true;
                     }

                     if (!newRailIter.hasNext() || (newRail = (RailTracker.TrackedRail)newRailIter.next()).member != memberToFind) {
                        break label81;
                     }
                  }
               }
            }

            if (prevRail.memberAddedToRailPiece) {
               prevRail.handleMemberRemove();
            }
         }
      }

   }

   private final void calcWheelTracks() {
      if (!this.rails.isEmpty()) {
         boolean hasPreviousMember = false;

         for(int i = 0; i < this.rails.size(); ++i) {
            RailTracker.TrackedRail rail = (RailTracker.TrackedRail)this.rails.get(i);
            if (rail.state.railType() == RailType.NONE) {
               if (hasPreviousMember) {
                  this.calcWheelTracksAhead(i - 1);

                  for(hasPreviousMember = false; this.rails.get(i) != rail && i < this.rails.size(); ++i) {
                  }
               }
            } else if (!hasPreviousMember || rail.disconnected) {
               this.calcWheelTracksBehind(i);

               for(hasPreviousMember = true; this.rails.get(i) != rail && i < this.rails.size(); ++i) {
               }
            }
         }

         this.calcWheelTracksAhead(this.rails.size() - 1);
      }
   }

   private final void calcWheelTracksAhead(int railIndex) {
      RailTracker.TrackedRail startInfo = (RailTracker.TrackedRail)this.rails.get(railIndex);
      MinecartMember<?> tail = startInfo.member;
      if (startInfo.state.railType() != RailType.NONE) {
         if (tail.getWheels().hasWheelDistance()) {
            RailPath.Position position = startInfo.state.position().clone();
            double wheelDistance;
            if (position.motDot(tail.getOrientationForward()) > 0.0D) {
               wheelDistance = tail.getWheels().front().getDistance();
            } else {
               wheelDistance = tail.getWheels().back().getDistance();
            }

            if (wheelDistance > 1.0E-5D) {
               TrackWalkingPoint p = new TrackWalkingPoint(startInfo.state);
               int limit = 1000;

               do {
                  if (p.moveStep(wheelDistance - p.movedTotal)) {
                     --limit;
                     if (limit == 0) {
                        this.owner.getTrainCarts().log(Level.WARNING, "Reached maximum loops refreshing front wheel position (train=" + this.owner.getProperties().getTrainName() + " x=" + ((CommonMinecart)tail.getEntity()).loc.getX() + " y=" + ((CommonMinecart)tail.getEntity()).loc.getY() + " z=" + ((CommonMinecart)tail.getEntity()).loc.getZ() + ")");
                        break;
                     }
                  }

                  ++railIndex;
                  this.rails.add(railIndex, new RailTracker.TrackedRail(tail, p, false));
               } while(p.failReason == TrackWalkingPoint.FailReason.NONE);
            }

         }
      }
   }

   private final void calcWheelTracksBehind(int railIndex) {
      RailTracker.TrackedRail startInfo = (RailTracker.TrackedRail)this.rails.get(railIndex);
      MinecartMember<?> tail = startInfo.member;
      if (startInfo.state.railType() != RailType.NONE) {
         if (tail.getWheels().hasWheelDistance()) {
            Vector movementDirection = startInfo.state.motionVector();
            movementDirection.multiply(-1.0D);
            Vector ownDirection = tail.getOrientationForward();
            double wheelDistance;
            if (MathUtil.isHeadingTo(movementDirection, ownDirection)) {
               wheelDistance = tail.getWheels().front().getDistance();
            } else {
               wheelDistance = tail.getWheels().back().getDistance();
            }

            if (wheelDistance > 1.0E-5D) {
               RailPath.Position position = RailPath.Position.fromPosDir(((CommonMinecart)tail.getEntity()).loc.vector(), movementDirection);
               position.reverse = true;
               int prevRailStartIndex = -1;

               int i;
               for(i = this.prevRails.size() - 1; i >= 0; --i) {
                  if (((RailTracker.TrackedRail)this.prevRails.get(i)).isSameTrack(startInfo)) {
                     prevRailStartIndex = i;
                     break;
                  }
               }

               if (prevRailStartIndex == -1 && !this.prevRails.isEmpty()) {
                  for(i = 0; i < this.prevRails.size(); ++i) {
                     if (((RailTracker.TrackedRail)this.prevRails.get(i)).member == startInfo.member) {
                        RailTracker.TrackedRail prev = (RailTracker.TrackedRail)this.prevRails.get(i);
                        TrackWalkingPoint p = new TrackWalkingPoint(prev.state);
                        p.skipFirst();
                        if (p.moveFull() && p.state.isSameRails(startInfo.state) && p.currentRailPath.equals(startInfo.getPath())) {
                           this.prevRails.add(i, startInfo.clone());
                           prevRailStartIndex = i;
                        }
                        break;
                     }
                  }
               }

               if (prevRailStartIndex != -1) {
                  RailTracker.TrackedRail startRail = (RailTracker.TrackedRail)this.prevRails.get(prevRailStartIndex);
                  RailPath startPath = startRail.getPath();
                  double startMoved = startPath.move(position, startRail.state.railBlock(), wheelDistance);
                  wheelDistance -= startMoved;
                  if (wheelDistance > 1.0E-10D) {
                     byte order;
                     if (startRail.state.position().motDot(position) > 0.0D) {
                        order = -1;
                     } else {
                        order = 1;
                     }

                     for(int prevRailIndex = prevRailStartIndex + order; prevRailIndex >= 0 && prevRailIndex < this.prevRails.size() && wheelDistance > 1.0E-4D; prevRailIndex += order) {
                        RailTracker.TrackedRail rail = (RailTracker.TrackedRail)this.prevRails.get(prevRailIndex);
                        if (!rail.isSameTrack(startInfo)) {
                           RailPath path = rail.getPath();
                           double moved = path.move(position, rail.state.railBlock(), wheelDistance);
                           wheelDistance -= moved;
                           rail = rail.changeMember(startInfo.member);
                           if (order < 0) {
                              rail = rail.invertMotionVector();
                           }

                           rail.cachedPath = path;
                           this.rails.add(railIndex, rail);
                           startInfo = rail;
                        }
                     }
                  }
               }

               if (wheelDistance > 0.0D) {
                  RailState state = new RailState();
                  state.setPosition(position);
                  state.setMember(tail);
                  state.setRailPiece(startInfo.state.railPiece());
                  RailType.loadRailInformation(state);
                  TrackWalkingPoint p = new TrackWalkingPoint(state);
                  int limit = 1000;

                  do {
                     if (p.moveStep(wheelDistance - p.movedTotal)) {
                        --limit;
                        if (limit == 1000) {
                           this.owner.getTrainCarts().log(Level.WARNING, "Reached maximum loops refreshing back wheel position (train=" + this.owner.getProperties().getTrainName() + " x=" + ((CommonMinecart)tail.getEntity()).loc.getX() + " y=" + ((CommonMinecart)tail.getEntity()).loc.getY() + " z=" + ((CommonMinecart)tail.getEntity()).loc.getZ() + ")");
                           break;
                        }
                     }

                     RailTracker.TrackedRail rail = new RailTracker.TrackedRail(tail, p, false);
                     rail = rail.invertMotionVector();
                     rail.cachedPath = p.currentRailPath;
                     this.rails.add(railIndex, rail);
                  } while(p.failReason == TrackWalkingPoint.FailReason.NONE);
               }

               if (position != null) {
               }
            }

         }
      }
   }

   private final void refreshFrom(int memberIndex, boolean disconnected) {
      RailTrackerGroup.RailFinder finder = new RailTrackerGroup.RailFinder(memberIndex, disconnected);
      if (finder.startIndex < 0) {
         finder.tail.getRailTracker().refresh(finder.startInfo);
         this.rails.add(finder.startInfo);
      } else if (finder.startInfo.state.railType() == RailType.NONE) {
         finder.tail.getRailTracker().refresh(finder.startInfo);
         this.rails.add(finder.startInfo);
         this.refreshFrom(finder.startIndex, false);
      } else {
         boolean isAbormal = false;
         RailTrackerGroup.RailFinderResult result;
         if (this.rails.isEmpty()) {
            result = finder.test(finder.startInfo, this.rails);
            if (result.numMembers < memberIndex && !result.endIsDerailed) {
               isAbormal = true;
               result.rails = new ArrayList(result.rails);
               this.rails.clear();
            }
         } else {
            result = finder.test(finder.startInfo);
            isAbormal = true;
         }

         if (isAbormal) {
            if (result.numMembers < memberIndex && !result.endIsDerailed) {
               RailTrackerGroup.RailFinderResult alter = finder.test(finder.startInfo.invertMotionVector());
               if (alter.numMembers > result.numMembers) {
                  result = alter;
               }
            }

            this.rails.addAll(result.rails);
         }

         if (TCConfig.logTrainSplitting && result.status != RailTrackerGroup.RailFinderResult.Status.OK) {
            Logger logger = this.owner.getTrainCarts().getLogger();
            logger.warning("Train '" + this.owner.getProperties().getTrainName() + "' split apart because: " + result.status.getReason());
            logger.warning("Search start: " + result.failSearchStart);
            logger.warning("Search end: " + result.failSearchEnd);
            if (result.nextMemberIndex >= 0) {
               MinecartMember<?> member = (MinecartMember)this.owner.get(result.nextMemberIndex);
               Location mloc = ((CommonMinecart)member.getEntity()).getLocation();
               logger.warning("Cart that could not be reached: cart #" + (result.nextMemberIndex + 1) + " of " + this.owner.size() + " [" + ((CommonMinecart)member.getEntity()).getUniqueId() + "] at x=" + mloc.getX() + " y=" + mloc.getY() + " z=" + mloc.getZ());
            }
         }

         Iterator<RailTracker.TrackedRail> iter = result.rails.iterator();
         if (iter.hasNext()) {
            RailTracker.TrackedRail prev;
            RailTracker.TrackedRail next;
            for(prev = (RailTracker.TrackedRail)iter.next(); iter.hasNext(); prev = next) {
               next = (RailTracker.TrackedRail)iter.next();
               if (prev.member != next.member) {
                  prev.member.getRailTracker().refresh(prev);
               }
            }

            prev.member.getRailTracker().refresh(prev);
         }

         if (result.nextMemberIndex >= 0) {
            this.refreshFrom(result.nextMemberIndex, !result.endIsDerailed);
         }

      }
   }

   private class RailFinder {
      private MinecartMember<?> tail;
      private final RailTracker.TrackedRail startInfo;
      private final int startIndex;

      public RailFinder(int index, boolean disconnected) {
         this.tail = (MinecartMember)RailTrackerGroup.this.owner.get(index);
         this.startInfo = RailTracker.TrackedRail.create(this.tail, disconnected);
         this.startIndex = index - 1;
      }

      public RailTrackerGroup.RailFinderResult test(RailTracker.TrackedRail moveInfo) {
         return this.test(moveInfo, new LinkedList());
      }

      public RailTrackerGroup.RailFinderResult test(RailTracker.TrackedRail moveInfo, List<RailTracker.TrackedRail> buffer) {
         RailTrackerGroup.RailFinderResult result = new RailTrackerGroup.RailFinderResult(this.startIndex, buffer);
         result.rails.add(moveInfo);
         MinecartMember<?> nextMember = (MinecartMember)RailTrackerGroup.this.owner.get(result.nextMemberIndex);
         RailState nextPos = nextMember.discoverRail();
         if (nextPos.railType() == RailType.NONE) {
            result.status = RailTrackerGroup.RailFinderResult.Status.DERAILED;
            result.failSearchStart = moveInfo.state;
            result.failSearchEnd = nextPos;
            result.endIsDerailed = true;
            return result;
         } else {
            int moveLimitCtrx = false;
            int maximumDistanceBlocks = this.tail.getMaximumBlockDistance(nextMember);
            TrackWalkingPoint p = new TrackWalkingPoint(moveInfo.state);
            if (p.moveFull()) {
               int moveLimitCtr = 0;
               boolean isFirstBlock = true;
               int nrCachedRails = 0;

               do {
                  while(p.state.isSameRails(nextPos)) {
                     boolean useFastMethod = p.currentRailPath.getSegments().length <= 1;
                     RailTracker.TrackedRail currInfo;
                     if (useFastMethod) {
                        if (p.state.position().motDot(nextPos.motionVector()) < 0.0D) {
                           nextPos.position().invertMotion();
                        }

                        currInfo = new RailTracker.TrackedRail(nextMember, nextPos, false);
                     } else {
                        double ERR_EPSILON = 1.0E-8D;
                        int cycle_limit = 10000;
                        double initial_distance = p.state.position().distance(nextPos.position());
                        double curr_distance = initial_distance;

                        while(!(curr_distance <= 1.0E-8D)) {
                           if (!p.move(curr_distance) || p.moved <= 1.0E-8D) {
                              curr_distance = p.state.position().distance(nextPos.position());
                              break;
                           }

                           double new_distance = p.state.position().distance(nextPos.position());
                           if (new_distance >= curr_distance) {
                              break;
                           }

                           curr_distance = new_distance;
                           --cycle_limit;
                           if (cycle_limit <= 0) {
                              break;
                           }
                        }

                        if (curr_distance > 1.0E-8D && curr_distance > 0.5D * initial_distance) {
                           result.status = RailTrackerGroup.RailFinderResult.Status.DIVERGING;
                           result.failSearchStart = p.state;
                           result.failSearchEnd = nextPos;
                           return result;
                        }

                        RailState currInfoState = p.state.clone();
                        currInfoState.setRailPiece(nextPos.railPiece());
                        currInfo = new RailTracker.TrackedRail(nextMember, currInfoState, false);
                     }

                     ++result.numMembers;
                     nrCachedRails = 0;
                     result.rails.add(currInfo);
                     if (--result.nextMemberIndex < 0) {
                        nextMember = null;
                        nextPos = null;
                        return result;
                     }

                     moveLimitCtr = 0;
                     nextMember = (MinecartMember)RailTrackerGroup.this.owner.get(result.nextMemberIndex);
                     nextPos = nextMember.discoverRail();
                     maximumDistanceBlocks = currInfo.member.getMaximumBlockDistance(nextMember);
                     isFirstBlock = true;
                     if (nextPos.railType() == RailType.NONE) {
                        result.status = RailTrackerGroup.RailFinderResult.Status.DERAILED;
                        result.endIsDerailed = true;
                        result.failSearchStart = p.state;
                        result.failSearchEnd = nextPos;
                        return result;
                     }
                  }

                  if (isFirstBlock) {
                     isFirstBlock = false;
                  } else {
                     result.rails.add(new RailTracker.TrackedRail(nextMember, p, false));
                     ++nrCachedRails;
                  }

                  ++moveLimitCtr;
               } while(moveLimitCtr <= maximumDistanceBlocks && p.moveFull());

               while(nrCachedRails > 0) {
                  --nrCachedRails;
                  result.rails.remove(result.rails.size() - 1);
               }

               result.status = moveLimitCtr > maximumDistanceBlocks ? RailTrackerGroup.RailFinderResult.Status.LIMIT_REACHED : RailTrackerGroup.RailFinderResult.Status.END_OF_TRACK;
               result.failSearchStart = result.rails.isEmpty() ? p.state : ((RailTracker.TrackedRail)result.rails.get(result.rails.size() - 1)).state;
               result.failSearchEnd = p.state;
            }

            return result;
         }
      }
   }

   public static class RailFinderResult {
      public RailTrackerGroup.RailFinderResult.Status status;
      public List<RailTracker.TrackedRail> rails;
      public int numMembers;
      public int nextMemberIndex;
      public boolean endIsDerailed;
      public RailState failSearchStart;
      public RailState failSearchEnd;

      public RailFinderResult(int nextMemberIndex, List<RailTracker.TrackedRail> buffer) {
         this.status = RailTrackerGroup.RailFinderResult.Status.OK;
         this.rails = buffer;
         this.numMembers = 0;
         this.nextMemberIndex = nextMemberIndex;
         this.endIsDerailed = false;
         this.failSearchStart = null;
         this.failSearchEnd = null;
      }

      public static enum Status {
         OK("OK"),
         DIVERGING("Path moving away from the next cart in the chain"),
         DERAILED("Next cart is derailed"),
         LIMIT_REACHED("Maximum distance reached searching next cart"),
         END_OF_TRACK("End of the rails reached before finding next cart");

         private final String reason;

         private Status(String reason) {
            this.reason = reason;
         }

         public String getReason() {
            return this.reason;
         }

         // $FF: synthetic method
         private static RailTrackerGroup.RailFinderResult.Status[] $values() {
            return new RailTrackerGroup.RailFinderResult.Status[]{OK, DIVERGING, DERAILED, LIMIT_REACHED, END_OF_TRACK};
         }
      }
   }
}
