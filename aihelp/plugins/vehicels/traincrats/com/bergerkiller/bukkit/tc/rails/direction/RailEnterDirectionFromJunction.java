package com.bergerkiller.bukkit.tc.rails.direction;

import com.bergerkiller.bukkit.tc.controller.components.RailJunction;
import com.bergerkiller.bukkit.tc.controller.components.RailPath;
import com.bergerkiller.bukkit.tc.controller.components.RailState;
import org.bukkit.util.Vector;

public final class RailEnterDirectionFromJunction implements RailEnterDirection {
   private final RailJunction junction;

   RailEnterDirectionFromJunction(RailJunction junction) {
      this.junction = junction;
   }

   public RailJunction getJunction() {
      return this.junction;
   }

   public String name() {
      return this.junction.name();
   }

   public double motionDot(Vector motion) {
      return -this.junction.position().motDot(motion);
   }

   public boolean match(RailState state) {
      RailPath path = state.loadRailLogic().getPath();
      RailPath.Position pos = state.position().clone();
      pos.makeRelative(state.railBlock());
      pos.invertMotion();
      path.moveRelative(pos, Double.MAX_VALUE);
      if (pos.motDot(this.junction.position()) <= 0.0D) {
         return false;
      } else {
         return pos.distanceSquaredAtRail(state.railBlock(), this.junction.position()) < 1.0E-10D;
      }
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else {
         return o instanceof RailEnterDirectionFromJunction ? this.junction.equals(((RailEnterDirectionFromJunction)o).getJunction()) : false;
      }
   }

   public String toString() {
      return "EnterFrom{junction=" + this.junction.name() + "}";
   }
}
