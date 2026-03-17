package com.bergerkiller.bukkit.tc.pathfinding;

import com.bergerkiller.bukkit.common.BlockLocation;
import com.bergerkiller.bukkit.tc.controller.components.RailPath;
import com.bergerkiller.bukkit.tc.controller.components.RailState;
import org.bukkit.World;

public interface PathRoutingHandler {
   void process(PathRoutingHandler.PathRouteEvent var1);

   default void predict(PathPredictEvent event) {
   }

   public static class PathRouteEvent extends PathNavigateEventBaseImpl {
      private final PathProvider provider;
      private final PathWorld world;
      private PathNode nodeAtRail;

      public PathRouteEvent(PathProvider provider, World world) {
         this.provider = provider;
         this.world = provider.getWorld(world);
      }

      public void resetToInitialState(RailState railState, RailPath railPath, double currentDistance) {
         super.resetToInitialState(railState, railPath, currentDistance);
         this.nodeAtRail = null;
      }

      public PathProvider provider() {
         return this.provider;
      }

      public PathNode createNode() {
         if (this.nodeAtRail == null) {
            this.nodeAtRail = this.world.getOrCreateAtRail(new BlockLocation(this.railBlock()));
         }

         return this.nodeAtRail;
      }

      public PathNode getLastSetNode() {
         return this.nodeAtRail;
      }

      public PathRailInfo getRailInfo() {
         if (this.isBlocked()) {
            return PathRailInfo.BLOCKED;
         } else {
            return this.getLastSetNode() != null ? PathRailInfo.NODE : PathRailInfo.NONE;
         }
      }
   }
}
