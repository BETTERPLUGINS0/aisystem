package com.bergerkiller.bukkit.tc.controller.components;

import java.util.Iterator;
import java.util.Optional;
import org.bukkit.util.Vector;

public final class RailJunction {
   private final String _name;
   private final RailPath.Position _position;

   public RailJunction(String name, RailPath.Position position) {
      this._name = name;
      this._position = position;
   }

   public String name() {
      return this._name;
   }

   public RailPath.Position position() {
      return this._position;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof RailJunction)) {
         return false;
      } else {
         RailJunction other = (RailJunction)o;
         return this.name().equals(other.name()) && this.position().equals(other.position());
      }
   }

   public String toString() {
      return "{" + this._name + ": " + this._position.toString() + "}";
   }

   public static Optional<RailJunction> findBest(Iterable<RailJunction> junctions, Vector direction) {
      double bestDot = 0.0D;
      RailJunction best = null;
      Iterator var5 = junctions.iterator();

      while(var5.hasNext()) {
         RailJunction junction = (RailJunction)var5.next();
         double dot = junction.position().motDot(direction);
         if (dot > bestDot) {
            bestDot = dot;
            best = junction;
         }
      }

      return Optional.ofNullable(best);
   }
}
