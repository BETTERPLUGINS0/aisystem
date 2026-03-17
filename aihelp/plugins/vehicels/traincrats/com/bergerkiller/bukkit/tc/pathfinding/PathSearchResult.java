package com.bergerkiller.bukkit.tc.pathfinding;

final class PathSearchResult {
   public static final PathSearchResult DUMMY_NOT_FOUND = new PathSearchResult((PathNode)null, (PathNode)null, (PathConnection)null, (PathSearchResult)null, Double.MAX_VALUE, false);
   public final PathNode node;
   public final PathNode destination;
   public final PathConnection connection;
   public final PathSearchResult next;
   public final double distance;
   public final boolean found;
   private boolean needsToBeCached;

   public static PathSearchResult self(PathNode node) {
      PathSearchResult r = new PathSearchResult(node, node, (PathConnection)null, (PathSearchResult)null, 0.0D, true);
      r.needsToBeCached = false;
      return r;
   }

   public static PathSearchResult missing(PathNode node, PathNode destination) {
      return new PathSearchResult(node, destination, (PathConnection)null, (PathSearchResult)null, Double.MAX_VALUE, false);
   }

   public static PathSearchResult chain(PathNode node, PathNode destination, PathConnection connection, PathSearchResult next) {
      return new PathSearchResult(node, destination, connection, next, connection.distance + next.distance, true);
   }

   private PathSearchResult(PathNode node, PathNode destination, PathConnection connection, PathSearchResult next, double distance, boolean found) {
      this.node = node;
      this.destination = destination;
      this.connection = connection;
      this.next = next;
      this.distance = distance;
      this.found = found;
      this.needsToBeCached = true;
   }

   public void cache() {
      for(PathSearchResult r = this; r != null && r.needsToBeCached; r = r.next) {
         r.needsToBeCached = false;
         r.node.getWorld().cacheSearchResult(r);
      }

   }

   static {
      DUMMY_NOT_FOUND.needsToBeCached = false;
   }
}
