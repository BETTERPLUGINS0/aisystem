package com.bergerkiller.bukkit.tc.chest;

import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.components.RailState;
import com.bergerkiller.bukkit.tc.controller.spawnable.SpawnableGroup;
import com.bergerkiller.bukkit.tc.controller.spawnable.SpawnableMember;
import com.bergerkiller.bukkit.tc.utils.TrackWalkingPoint;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class TrainChestExtendableTrain {
   public final MinecartMember<?> member;
   public final RailState startState;

   public TrainChestExtendableTrain(MinecartMember<?> member, RailState startState) {
      this.member = member;
      this.startState = startState;
   }

   public static TrainChestExtendableTrain findOccupied(List<SpawnableGroup.OccupiedLocation> occupiedLocations, SpawnableMember connectedMember) {
      if (occupiedLocations.isEmpty()) {
         return null;
      } else {
         SpawnableGroup.OccupiedLocation firstOccupied = (SpawnableGroup.OccupiedLocation)occupiedLocations.get(0);
         return findEndOfTrain(firstOccupied.member, firstOccupied.spawnLocation.forward.clone().multiply(-1.0D), connectedMember);
      }
   }

   public static TrainChestExtendableTrain find(RailState startState, double searchDistance, SpawnableMember connectedMember) {
      if (startState.railPiece().isNone()) {
         return null;
      } else {
         TrackWalkingPoint p = new TrackWalkingPoint(startState);

         do {
            List<MinecartMember<?>> members = p.state.railPiece().members();
            if (!members.isEmpty()) {
               MinecartMember<?> bestMember = (MinecartMember)members.get(0);
               if (members.size() >= 2) {
                  double lowestDistanceSq = Double.MAX_VALUE;
                  Location startLoc = startState.positionLocation();
                  Iterator var10 = members.iterator();

                  while(var10.hasNext()) {
                     MinecartMember<?> member = (MinecartMember)var10.next();
                     double distanceSq = ((CommonMinecart)member.getEntity()).loc.distanceSquared(startLoc);
                     if (distanceSq < lowestDistanceSq) {
                        lowestDistanceSq = distanceSq;
                        bestMember = member;
                     }
                  }
               }

               return findEndOfTrain(bestMember, p.state.motionVector().clone().multiply(-1.0D), connectedMember);
            }
         } while(p.moveStep(searchDistance - p.movedTotal));

         return null;
      }
   }

   private static TrainChestExtendableTrain findEndOfTrain(MinecartMember<?> member, Vector spawnDirection, SpawnableMember connectedMember) {
      RailState memberStartState;
      if (spawnDirection.dot(member.getRailTracker().getMotionVector()) >= 0.0D) {
         member = member.getGroup().head();
         memberStartState = member.getRailTracker().getState().clone();
      } else {
         member = member.getGroup().tail();
         memberStartState = member.getRailTracker().getState().cloneAndInvertMotion();
      }

      double extraDistance = 0.5D * (double)((CommonMinecart)member.getEntity()).getWidth() + member.getCartCouplerLength() + connectedMember.getCartCouplerLength();
      TrackWalkingPoint p = new TrackWalkingPoint(memberStartState);
      p.skipFirst();
      return !p.move(extraDistance) ? null : new TrainChestExtendableTrain(member, p.state);
   }
}
