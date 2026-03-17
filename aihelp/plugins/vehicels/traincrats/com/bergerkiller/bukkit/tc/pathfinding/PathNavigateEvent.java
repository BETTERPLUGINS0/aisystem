package com.bergerkiller.bukkit.tc.pathfinding;

import com.bergerkiller.bukkit.tc.controller.components.RailJunction;
import com.bergerkiller.bukkit.tc.controller.components.RailPath;
import com.bergerkiller.bukkit.tc.controller.components.RailPiece;
import com.bergerkiller.bukkit.tc.controller.components.RailState;
import org.bukkit.World;
import org.bukkit.block.Block;

public interface PathNavigateEvent {
   static PathNavigateEvent createNew() {
      return new PathNavigateEventBaseImpl();
   }

   void resetToInitialState(RailState var1, RailPath var2, double var3);

   double currentDistance();

   boolean isNavigationAborted();

   void abortNavigation();

   default void setBlocked() {
      this.abortNavigation();
   }

   default boolean isBlocked() {
      return this.isNavigationAborted();
   }

   RailState railState();

   RailPath railPath();

   default RailPiece railPiece() {
      return this.railState().railPiece();
   }

   default Block railBlock() {
      return this.railState().railBlock();
   }

   default World railWorld() {
      return this.railState().railWorld();
   }

   RailPath.Position getSwitchedPosition();

   boolean hasSwitchedPosition();

   void setSwitchedPosition(RailPath.Position var1);

   default void setSwitchedJunction(RailJunction junction) {
      this.setSwitchedPosition(junction.position());
   }
}
