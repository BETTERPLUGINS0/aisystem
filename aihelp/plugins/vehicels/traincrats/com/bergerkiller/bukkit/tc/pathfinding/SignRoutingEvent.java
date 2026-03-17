package com.bergerkiller.bukkit.tc.pathfinding;

import com.bergerkiller.bukkit.tc.controller.components.RailPath;
import com.bergerkiller.bukkit.tc.controller.components.RailState;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.rails.RailLookup;
import com.bergerkiller.bukkit.tc.signactions.SignActionType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SignRoutingEvent extends SignActionEvent implements PathNavigateEvent {
   private RailState railState;
   private RailPath railPath;
   private RailPath.Position nextPosition;
   private double currentDistance;
   private boolean abortNavigation;
   private boolean switchable = false;
   private List<String> destinationNames = Collections.emptyList();

   public SignRoutingEvent(RailLookup.TrackedSign sign) {
      super(sign);
      this.setAction(SignActionType.GROUP_ENTER);
   }

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

   public boolean isRouteSwitchable() {
      return this.switchable;
   }

   public void setRouteSwitchable(boolean switchable) {
      this.switchable = switchable;
   }

   public List<String> getDestinationNames() {
      return this.destinationNames;
   }

   public void addDestinationName(String name) {
      if (this.destinationNames.isEmpty()) {
         this.destinationNames = new ArrayList(2);
      }

      this.destinationNames.add(name);
   }
}
