package com.bergerkiller.bukkit.tc.pathfinding;

import com.bergerkiller.bukkit.common.BlockLocation;
import com.bergerkiller.bukkit.common.collections.BlockMap;
import com.bergerkiller.bukkit.tc.TrainCarts;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.block.Block;

public class PathWorld implements TrainCarts.Provider {
   private final PathProvider _provider;
   private final String _name;
   private final BlockMap<PathNode> _blockNodes;
   private final Map<String, PathNode> _nodes;
   private final Map<PathWorld.PathFromToKey, PathSearchResult> _cachedSearchResults;

   public PathWorld(PathProvider provider, String worldName) {
      this._provider = provider;
      this._name = worldName;
      this._blockNodes = new BlockMap();
      this._nodes = new HashMap();
      this._cachedSearchResults = new HashMap();
   }

   protected void markChanged() {
      this._cachedSearchResults.clear();
      this._provider.markChanged();
   }

   public TrainCarts getTrainCarts() {
      return this._provider.getTrainCarts();
   }

   public PathProvider getProvider() {
      return this._provider;
   }

   public String getName() {
      return this._name;
   }

   public PathNode getNodeAtRail(BlockLocation railLocation) {
      return (PathNode)this._blockNodes.get(railLocation);
   }

   public PathNode getNodeAtRail(Block railBlock) {
      return (PathNode)this._blockNodes.get(railBlock);
   }

   public PathNode getNodeByName(String name) {
      return (PathNode)this._nodes.get(name);
   }

   public PathNode tryFindNodeAgain(PathNodeSnapshot snapshot) {
      PathNode atRail = this.getNodeAtRail(snapshot.getRailLocation());
      if (atRail != null) {
         return atRail;
      } else {
         Iterator var3 = snapshot.getNames().iterator();

         PathNode foundNode;
         do {
            if (!var3.hasNext()) {
               return null;
            }

            String name = (String)var3.next();
            foundNode = this.getNodeByName(name);
         } while(foundNode == null);

         return foundNode;
      }
   }

   public Set<BlockLocation> getRailBlocks() {
      return this._blockNodes.keySet();
   }

   public Collection<PathNode> getNodes() {
      return this._blockNodes.values();
   }

   public PathNode removeAtRail(Block railBlock) {
      PathNode node = (PathNode)this._blockNodes.remove(railBlock);
      if (node != null) {
         node.remove();
      }

      return node;
   }

   public PathNode getOrCreateAtRail(BlockLocation location) {
      if (location == null) {
         return null;
      } else {
         PathNode node = this.getNodeAtRail(location);
         return node != null ? node : this.addNode(location);
      }
   }

   public PathNode addNode(BlockLocation location) {
      PathNode node = new PathNode(this, location);
      this.addToMapping(node);
      this._provider.scheduleNode(node);
      this.markChanged();
      return node;
   }

   public void rerouteAll() {
      Iterator var1 = this.getRailBlocks().iterator();

      while(var1.hasNext()) {
         BlockLocation location = (BlockLocation)var1.next();
         this._provider.discoverFromRail(location);
      }

      this.clearAll();
      this.markChanged();
   }

   public void rerouteFrom(List<String> destinationNames) {
      Iterator var2 = destinationNames.iterator();

      while(var2.hasNext()) {
         String name = (String)var2.next();
         PathNode node = this.getNodeByName(name);
         if (node != null) {
            this._provider.discoverFromNode(node);
            this.removeFromMapping(node);
         }
      }

   }

   public void clearAll() {
      this._nodes.clear();
      this._blockNodes.clear();
      this.markChanged();
   }

   protected void addNodeName(PathNode node, String name) {
      this._nodes.put(name, node);
      this.markChanged();
   }

   protected void removeNodeName(PathNode node, String name) {
      PathNode removed = (PathNode)this._nodes.remove(name);
      if (removed == node) {
         this.markChanged();
      } else if (removed != null) {
         this._nodes.put(name, removed);
      }

   }

   protected void addToMapping(PathNode node) {
      Iterator var2 = node.getNames().iterator();

      while(var2.hasNext()) {
         String name = (String)var2.next();
         this.addNodeName(node, name);
      }

      this._blockNodes.put(node.location, node);
      this._nodes.put(node.location.toString(), node);
      this.markChanged();
   }

   protected void removeFromMapping(PathNode node) {
      Iterator var2 = node.getNames().iterator();

      while(var2.hasNext()) {
         String name = (String)var2.next();
         PathNode removed = (PathNode)this._nodes.remove(name);
         if (removed != null && removed != node) {
            this._nodes.put(name, removed);
         }
      }

      PathNode removed = (PathNode)this._blockNodes.remove(node.location);
      if (removed != null && removed != node) {
         this._blockNodes.put(node.location, removed);
      } else if (removed != null) {
         this._nodes.remove(node.location.toString());
      }

      this.markChanged();
   }

   protected PathSearchResult findCachedSearchResult(PathNode node, PathNode destination) {
      return (PathSearchResult)this._cachedSearchResults.getOrDefault(new PathWorld.PathFromToKey(node, destination), PathSearchResult.DUMMY_NOT_FOUND);
   }

   protected void cacheSearchResult(PathSearchResult result) {
      this._cachedSearchResults.put(new PathWorld.PathFromToKey(result.node, result.destination), result);
   }

   private static final class PathFromToKey {
      private final PathNode node;
      private final PathNode destination;

      public PathFromToKey(PathNode node, PathNode destination) {
         this.node = node;
         this.destination = destination;
      }

      public int hashCode() {
         return this.node.hashCode() + 31 * this.destination.hashCode();
      }

      public boolean equals(Object o) {
         PathWorld.PathFromToKey other = (PathWorld.PathFromToKey)o;
         return this.node == other.node && this.destination == other.destination;
      }
   }
}
