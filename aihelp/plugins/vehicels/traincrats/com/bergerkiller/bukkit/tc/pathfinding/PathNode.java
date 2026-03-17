package com.bergerkiller.bukkit.tc.pathfinding;

import com.bergerkiller.bukkit.common.BlockLocation;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.Level;
import org.bukkit.block.Block;

public class PathNode {
   private final PathWorld world;
   public final BlockLocation location;
   private final Set<String> names = new HashSet();
   private final List<PathConnection> neighbors = new ArrayList(3);
   public int index;
   private boolean isRailSwitchable;
   private PathNode.PathSearchOperation lastSearch = null;
   private PathSearchResult lastSearchResult = null;
   private double lastSearchStartDistance = Double.MAX_VALUE;

   protected PathNode(PathWorld world, BlockLocation location) {
      this.world = world;
      this.location = location;
      this.isRailSwitchable = false;
   }

   public static void clearAll() {
      Iterator var0 = TrainCarts.plugin.getPathProvider().getWorlds().iterator();

      while(var0.hasNext()) {
         PathWorld world = (PathWorld)var0.next();
         world.clearAll();
      }

   }

   public static void reroute() {
      TrainCarts.plugin.getPathProvider().reroute();
   }

   public static PathNode get(BlockLocation railLocation) {
      return TrainCarts.plugin.getPathProvider().getWorld(railLocation.world).getNodeAtRail(railLocation);
   }

   public static PathNode get(Block block) {
      return block == null ? null : TrainCarts.plugin.getPathProvider().getWorld(block.getWorld()).getNodeAtRail(block);
   }

   public static PathNode remove(Block railsblock) {
      return railsblock == null ? null : TrainCarts.plugin.getPathProvider().getWorld(railsblock.getWorld()).removeAtRail(railsblock);
   }

   public static PathNode getOrCreate(SignActionEvent event) {
      if (!event.hasRails()) {
         throw new IllegalArgumentException("Sign has no rails - check hasRails()");
      } else {
         PathNode node;
         if (event.isType("destination")) {
            node = getOrCreate(event.getRails());
            node.addName(event.getLine(2));
            return node;
         } else {
            if (event.isCartSign()) {
               if (!event.hasMember() || !event.getMember().getProperties().hasDestination()) {
                  return null;
               }
            } else if (event.isTrainSign() && (!event.hasGroup() || !event.getGroup().getProperties().hasDestination())) {
               return null;
            }

            node = getOrCreate(event.getRails());
            node.addSwitcher();
            return node;
         }
      }
   }

   public static PathNode getOrCreate(Block location) {
      return getOrCreate(new BlockLocation(location));
   }

   public static PathNode getOrCreate(BlockLocation location) {
      return TrainCarts.plugin.getPathProvider().getWorld(location.world).getOrCreateAtRail(location);
   }

   public PathWorld getWorld() {
      return this.world;
   }

   public PathConnection findConnection(String destination) {
      PathNode node = this.world.getNodeByName(destination);
      return node == null ? null : this.findConnection(node);
   }

   public PathConnection[] findRoute(PathNode destination) {
      PathSearchResult result = this.findBestPath(destination);
      if (!result.found) {
         return new PathConnection[0];
      } else {
         ArrayList route;
         for(route = new ArrayList(); result.connection != null; result = result.next) {
            route.add(result.connection);
         }

         return (PathConnection[])route.toArray(new PathConnection[0]);
      }
   }

   public PathConnection findConnection(PathNode destination) {
      PathSearchResult result = this.findBestPath(destination);
      return result.found && result.connection != null ? new PathConnection(destination, result.distance, result.connection.junctionName) : null;
   }

   private PathSearchResult findBestPath(PathNode destination) {
      PathSearchResult result = this.findBestPath(new PathNode.PathSearchOperation(destination), 0.0D);
      if (result == PathSearchResult.DUMMY_NOT_FOUND) {
         result = PathSearchResult.missing(this, destination);
      }

      result.cache();
      return result;
   }

   private PathSearchResult findBestPath(PathNode.PathSearchOperation search, double startDistance) {
      if (startDistance > search.maxTotalDistance) {
         return PathSearchResult.DUMMY_NOT_FOUND;
      } else if (this.lastSearch == search && startDistance > this.lastSearchStartDistance) {
         return PathSearchResult.DUMMY_NOT_FOUND;
      } else {
         this.lastSearch = search;
         this.lastSearchStartDistance = startDistance;
         if (this == search.destination) {
            return search.acceptResult(startDistance, this.lastSearchResult = PathSearchResult.self(this));
         } else {
            PathSearchResult result = this.lastSearchResult = this.world.findCachedSearchResult(this, search.destination);
            if (result != PathSearchResult.DUMMY_NOT_FOUND) {
               return search.acceptResult(startDistance, result);
            } else {
               Iterator var7 = this.neighbors.iterator();

               while(var7.hasNext()) {
                  PathConnection neighbour = (PathConnection)var7.next();
                  PathSearchResult neigh_result = neighbour.destination.findBestPath(search, startDistance + neighbour.distance);
                  if (neigh_result.found) {
                     this.lastSearchResult = PathSearchResult.chain(this, search.destination, neighbour, neigh_result);
                  }
               }

               return this.lastSearchResult;
            }
         }
      }
   }

   public PathConnection addNeighbour(PathNode to, double distance, String junctionName) {
      Iterator iter = this.neighbors.iterator();

      PathConnection conn;
      while(iter.hasNext()) {
         conn = (PathConnection)iter.next();
         if (conn.destination == to) {
            if (conn.distance <= distance) {
               return conn;
            }

            iter.remove();
            break;
         }
      }

      conn = new PathConnection(to, distance, junctionName);
      this.addNeighbourFast(conn);
      this.world.getProvider().scheduleNodeIfNotRecentlyRouted(to);
      this.world.markChanged();
      return conn;
   }

   protected void addNeighbourFast(PathConnection connection) {
      this.neighbors.add(connection);
   }

   public void clear() {
      this.neighbors.clear();
      Iterator var1 = this.world.getNodes().iterator();

      while(var1.hasNext()) {
         PathNode node = (PathNode)var1.next();
         Iterator iter = node.neighbors.iterator();

         while(iter.hasNext()) {
            if (((PathConnection)iter.next()).destination == this) {
               iter.remove();
            }
         }
      }

      this.world.markChanged();
   }

   public void removeName(String name) {
      if (this.names.remove(name)) {
         this.world.removeNodeName(this, name);
         if (PathProvider.DEBUG_MODE) {
            String dbg = "NODE " + this.location + " NO LONGER HAS NAME " + name;
            if (this.names.isEmpty()) {
               dbg = dbg + " AND IS NOW BEING REMOVED (NO NAMES)";
            }

            this.world.getTrainCarts().log(Level.INFO, dbg);
         }

         if (this.names.isEmpty() && !this.containsSwitcher()) {
            this.remove();
         }

      }
   }

   public void remove() {
      this.clear();
      this.world.removeFromMapping(this);
   }

   public boolean containsName(String name) {
      return this.names.contains(name);
   }

   public boolean containsOnlySwitcher() {
      return this.names.isEmpty() && this.containsSwitcher();
   }

   public Collection<String> getNames() {
      return this.names;
   }

   public BlockLocation getRailLocation() {
      return this.location;
   }

   public Collection<PathConnection> getNeighbours() {
      return this.neighbors;
   }

   public Map<PathConnection, List<PathConnection>> getDeepNeighbours() {
      Map<PathNode, PathConnection> connections = new HashMap();
      Iterator var2 = this.neighbors.iterator();

      PathConnection neighbour;
      while(var2.hasNext()) {
         neighbour = (PathConnection)var2.next();
         connections.put(neighbour.destination, neighbour);
      }

      var2 = this.neighbors.iterator();

      while(var2.hasNext()) {
         neighbour = (PathConnection)var2.next();
         neighbour.destination.fillDeepNeighbours(connections, neighbour.junctionName, neighbour.distance);
      }

      connections.remove(this);
      Map<PathConnection, List<PathConnection>> result = new HashMap();
      Iterator var10 = connections.values().iterator();

      while(true) {
         while(true) {
            PathConnection connection;
            boolean found;
            Iterator var6;
            do {
               if (!var10.hasNext()) {
                  var10 = result.values().iterator();

                  while(var10.hasNext()) {
                     List<PathConnection> collection = (List)var10.next();
                     Collections.sort(collection, (c1, c2) -> {
                        return Double.compare(c1.distance, c2.distance);
                     });
                  }

                  return result;
               }

               connection = (PathConnection)var10.next();
               found = false;
               var6 = result.entrySet().iterator();

               while(var6.hasNext()) {
                  Entry<PathConnection, List<PathConnection>> entry = (Entry)var6.next();
                  if (((PathConnection)entry.getKey()).junctionName.equals(connection.junctionName)) {
                     found = true;
                     ((List)entry.getValue()).add(connection);
                     break;
                  }
               }
            } while(found);

            var6 = this.neighbors.iterator();

            while(var6.hasNext()) {
               PathConnection neighbour = (PathConnection)var6.next();
               if (neighbour.junctionName.equals(connection.junctionName)) {
                  List<PathConnection> list = new ArrayList();
                  list.add(connection);
                  result.put(neighbour, list);
                  break;
               }
            }
         }
      }
   }

   private void fillDeepNeighbours(Map<PathNode, PathConnection> connections, String junctionName, double startDistance) {
      Iterator var5 = this.neighbors.iterator();

      while(true) {
         PathConnection neighbour;
         double distance;
         PathConnection previous;
         do {
            if (!var5.hasNext()) {
               return;
            }

            neighbour = (PathConnection)var5.next();
            distance = startDistance + neighbour.distance;
            previous = (PathConnection)connections.get(neighbour.destination);
         } while(previous != null && !(previous.distance > distance));

         connections.put(neighbour.destination, new PathConnection(neighbour.destination, distance, junctionName));
         neighbour.destination.fillDeepNeighbours(connections, junctionName, distance);
      }
   }

   public void rerouteConnectedDeepRecursive() {
      HashSet<PathNode> reachable = new HashSet();
      this.addReachable(reachable);

      boolean changed;
      Iterator var3;
      PathNode node;
      label42:
      do {
         changed = false;
         var3 = this.world.getNodes().iterator();

         while(true) {
            do {
               if (!var3.hasNext()) {
                  continue label42;
               }

               node = (PathNode)var3.next();
            } while(reachable.contains(node));

            Iterator var5 = node.neighbors.iterator();

            while(var5.hasNext()) {
               PathConnection neighbour = (PathConnection)var5.next();
               if (reachable.contains(neighbour.destination)) {
                  changed = true;
                  node.addReachable(reachable);
               }
            }
         }
      } while(changed);

      var3 = reachable.iterator();

      while(var3.hasNext()) {
         node = (PathNode)var3.next();
         node.neighbors.clear();
         this.world.removeFromMapping(node);
         this.world.getProvider().discoverFromRail(node.location);
      }

   }

   public void rerouteConnected() {
      this.clear();
      this.world.removeFromMapping(this);
      this.world.getProvider().discoverFromRail(this.location);
   }

   private void addReachable(Set<PathNode> reachable) {
      if (reachable.add(this)) {
         Iterator var2 = this.neighbors.iterator();

         while(var2.hasNext()) {
            PathConnection neighbour = (PathConnection)var2.next();
            neighbour.destination.addReachable(reachable);
         }
      }

   }

   public boolean containsSwitcher() {
      return this.isRailSwitchable;
   }

   public void addSwitcher() {
      if (PathProvider.DEBUG_MODE && !this.isRailSwitchable) {
         this.world.getTrainCarts().log(Level.INFO, "NODE AT " + this.location.toString() + " ADDED SWITCHER");
      }

      this.isRailSwitchable = true;
   }

   public String getName() {
      if (!this.names.isEmpty()) {
         return (String)this.names.iterator().next();
      } else {
         return this.containsSwitcher() ? this.location.toString() : null;
      }
   }

   public String getDisplayName() {
      return formatDisplayName(this.location, this.names);
   }

   public String toString() {
      return this.getDisplayName();
   }

   public void addName(String name) {
      if (this.names.add(name)) {
         if (PathProvider.DEBUG_MODE) {
            this.world.getTrainCarts().log(Level.INFO, "NODE AT " + this.location.toString() + " ADDED DESTINATION " + name);
         }

         this.world.addNodeName(this, name);
      }

   }

   public PathNodeSnapshot getSnapshot() {
      return new PathNodeSnapshot(new HashSet(this.names), this.location, this.isRailSwitchable);
   }

   protected static String formatDisplayName(BlockLocation location, Set<String> names) {
      if (names.isEmpty()) {
         return "[" + location.x + "/" + location.y + "/" + location.z + "]";
      } else if (names.size() == 1) {
         return (String)names.iterator().next();
      } else {
         StringBuilder builder = new StringBuilder(names.size() * 15);
         builder.append('{');

         String name;
         for(Iterator var3 = names.iterator(); var3.hasNext(); builder.append(name)) {
            name = (String)var3.next();
            if (builder.length() > 1) {
               builder.append("/");
            }
         }

         builder.append('}');
         return builder.toString();
      }
   }

   private static class PathSearchOperation {
      public final PathNode destination;
      public double maxTotalDistance = Double.MAX_VALUE;

      public PathSearchOperation(PathNode destination) {
         this.destination = destination;
      }

      public PathSearchResult acceptResult(double startDistance, PathSearchResult result) {
         if (!result.found) {
            return result;
         } else {
            double total = startDistance + result.distance;
            if (total < this.maxTotalDistance) {
               this.maxTotalDistance = total;
               return result;
            } else {
               return PathSearchResult.DUMMY_NOT_FOUND;
            }
         }
      }
   }
}
