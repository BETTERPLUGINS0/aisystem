package com.bergerkiller.bukkit.tc.pathfinding;

import com.bergerkiller.bukkit.tc.controller.components.RailPath;
import com.bergerkiller.bukkit.tc.controller.components.RailState;

public class PathNavigateEventBaseImpl implements PathNavigateEvent {
   private RailState railState;
   private RailPath railPath;
   private RailPath.Position nextPosition;
   private double currentDistance;
   private boolean abortNavigation;

   public void resetToInitialState(RailState railState, RailPath railPath, double currentDistance) {
      if (railState == null) {
         throw new IllegalArgumentException("Rail state cannot be null");
      } else if (railPath == null) {
         throw new IllegalArgumentException("Rail path cannot be null");
      } else {
         this.railState = railState;
         this.railPath = railPath;
         this.nextPosition = null;
         this.currentDistance = currentDistance;
         this.abortNavigation = false;
      }
   }

   public double currentDistance() {
      return this.currentDistance;
   }

   public boolean isNavigationAborted() {
      return this.abortNavigation;
   }

   public void abortNavigation() {
      this.abortNavigation = true;
   }

   public RailState railState() {
      return this.railState;
   }

   public RailPath railPath() {
      return this.railPath;
   }

   public RailPath.Position getSwitchedPosition() {
      return this.nextPosition;
   }

   public boolean hasSwitchedPosition() {
      return this.nextPosition != null;
   }

   public void setSwitchedPosition(RailPath.Position nextPosition) {
      this.nextPosition = nextPosition;
   }
}
