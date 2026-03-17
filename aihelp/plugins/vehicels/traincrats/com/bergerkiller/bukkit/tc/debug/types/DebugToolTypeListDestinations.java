package com.bergerkiller.bukkit.tc.debug.types;

import com.bergerkiller.bukkit.common.BlockLocation;
import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.Task;
import com.bergerkiller.bukkit.common.inventory.CommonItemStack;
import com.bergerkiller.bukkit.common.nbt.CommonTagCompound;
import com.bergerkiller.bukkit.common.utils.BlockUtil;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.tc.Localization;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.components.RailJunction;
import com.bergerkiller.bukkit.tc.controller.components.RailState;
import com.bergerkiller.bukkit.tc.debug.DebugToolUtil;
import com.bergerkiller.bukkit.tc.pathfinding.PathConnection;
import com.bergerkiller.bukkit.tc.pathfinding.PathNode;
import com.bergerkiller.bukkit.tc.pathfinding.PathProvider;
import com.bergerkiller.bukkit.tc.pathfinding.PathRailInfo;
import com.bergerkiller.bukkit.tc.pathfinding.PathRoutingHandler;
import com.bergerkiller.bukkit.tc.rails.type.RailType;
import com.bergerkiller.bukkit.tc.utils.TrackWalkingPoint;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class DebugToolTypeListDestinations extends DebugToolTrackWalkerType {
   private final String destination;
   private int maxDestinations = 5;

   public DebugToolTypeListDestinations() {
      this.destination = null;
   }

   public DebugToolTypeListDestinations(String destination) {
      this.destination = destination;
   }

   public DebugToolTypeListDestinations setMaxDestinations(int limit) {
      this.maxDestinations = limit;
      return this;
   }

   public void loadMetadata(CommonTagCompound metadata) {
      if (metadata.containsKey("maxDestinations")) {
         this.maxDestinations = (Integer)metadata.getValue("maxDestinations", 5);
      }

   }

   public void saveMetadata(CommonTagCompound metadata) {
      metadata.putValue("maxDestinations", this.maxDestinations);
   }

   public String getIdentifier() {
      return this.destination != null ? "Destination " + this.destination : "Destinations";
   }

   public String getTitle() {
      return this.destination != null ? "Pathfinding destination searcher (routes to " + this.destination + ")" : "Pathfinding destination searcher";
   }

   public String getDescription() {
      return this.destination != null ? "Identifies the route to reach destination '" + this.destination + "'" : "Identifies all the destination routes reachable from the rails clicked";
   }

   public String getInstructions() {
      return this.destination != null ? "Right-click rails to see whether and how a train would travel to " + this.destination + "." : "Right-click rails to see what destinations can be reached from there.";
   }

   public void onBlockInteract(final TrainCarts trainCarts, final Player player, final TrackWalkingPoint walker, CommonItemStack item, boolean isRightClick) {
      final PathProvider provider = trainCarts.getPathProvider();
      walker.setNavigator(new TrackWalkingPoint.Navigator<PathRoutingHandler.PathRouteEvent>() {
         public void navigate(PathRoutingHandler.PathRouteEvent routeEvent) {
            routeEvent.provider().handleRouting(routeEvent);
            PathRailInfo info = routeEvent.getRailInfo();
            if (info == PathRailInfo.BLOCKED) {
               if (DebugToolTypeListDestinations.this.destination != null) {
                  player.sendMessage(ChatColor.RED + "Destination " + DebugToolTypeListDestinations.this.destination + " can not be reached!");
               }

               player.sendMessage(ChatColor.RED + "A blocker sign at " + ChatColor.YELLOW + DebugToolUtil.coordinates(walker.state.position()) + ChatColor.RED + " is blocking trains!");
               routeEvent.abortNavigation();
            } else if (info == PathRailInfo.NODE) {
               DebugToolTypeListDestinations.this.debugListRoutesFrom(trainCarts, player, walker.state, DebugToolTypeListDestinations.this.destination, player.isSneaking(), walker.movedTotal);
               routeEvent.abortNavigation();
            }

         }

         public PathRoutingHandler.PathRouteEvent createNewEvent() {
            return new PathRoutingHandler.PathRouteEvent(provider, walker.state.railWorld());
         }
      });
      Block old_railBlock = null;
      double stopDistance = walker.movedTotal + 2000.0D;
      int lim = 10000;

      while(true) {
         --lim;
         if (lim == 0 || walker.movedTotal >= stopDistance) {
            CommonUtil.getPluginExecutor(trainCarts).execute(() -> {
               this.onBlockInteract(trainCarts, player, walker, item, isRightClick);
            });
            break;
         }

         if (this.destination != null) {
            if (!walker.move(0.3D)) {
               if (walker.failReason != TrackWalkingPoint.FailReason.NAVIGATION_ABORTED) {
                  DebugToolUtil.showEndOfTheRail(player, walker, 0.0D);
               }
               break;
            }

            Util.spawnDustParticle(walker.state.positionLocation(), Color.RED);
         } else {
            if (!walker.moveFull()) {
               if (walker.failReason != TrackWalkingPoint.FailReason.NAVIGATION_ABORTED) {
                  DebugToolUtil.showEndOfTheRail(player, walker, 0.0D);
               }
               break;
            }

            Util.spawnDustParticle(walker.state.positionLocation(), Color.GRAY);
         }

         if (!BlockUtil.equals(walker.state.railBlock(), old_railBlock)) {
            old_railBlock = walker.state.railBlock();
         }
      }

   }

   private void debugListRoutesFrom(final TrainCarts trainCarts, final Player player, final RailState state, final String destinationName, final boolean reroute, final double initialDistance) {
      final PathProvider provider = trainCarts.getPathProvider();
      if (!state.railLookup().isValid()) {
         player.sendMessage(ChatColor.RED + "Failed to list destinations - World is no longer loaded!");
      } else {
         PathNode node = provider.getWorld(state.railWorld()).getNodeAtRail(state.railBlock());
         if (node == null) {
            provider.discoverFromRail(new BlockLocation(state.railBlock()));
            player.sendMessage(ChatColor.YELLOW + "Discovering paths from " + DebugToolUtil.coordinates(state.position()));
         } else if (reroute) {
            reroute = false;
            player.sendMessage(ChatColor.YELLOW + "Rerouting the node network from " + node.getDisplayName());
            node.rerouteConnected();
         }

         if (provider.isProcessing()) {
            Localization.PATHING_BUSY.message(player, new String[0]);
            (new Task(trainCarts) {
               public void run() {
                  if (!provider.isProcessing()) {
                     this.stop();
                     DebugToolTypeListDestinations.this.debugListRoutesFrom(trainCarts, player, state, destinationName, reroute, initialDistance);
                  }

               }
            }).start(1L, 1L);
         } else if (node == null) {
            player.sendMessage(ChatColor.RED + "[Error] Path finding node is missing at " + DebugToolUtil.coordinates(state.position()) + " after " + (int)initialDistance + " blocks");
         } else {
            if (destinationName != null) {
               PathNode destination = node.getWorld().getNodeByName(destinationName);
               if (destination == null) {
                  player.sendMessage(ChatColor.RED + "Destination " + destinationName + " does not exist. Try rerouting (sneak-click)");
                  return;
               }

               debugShowRouteFromTo(player, node, state.railBlock(), destination, initialDistance);
            } else {
               this.debugListAllRoutes(player, node, state.railBlock(), initialDistance);
            }

         }
      }
   }

   private static void debugShowRouteFromTo(Player player, PathNode node, Block railBlock, PathNode destination, double initialDistance) {
      if (node == destination) {
         player.sendMessage(ChatColor.GREEN + "Route to " + ChatColor.YELLOW + destination.getDisplayName() + ChatColor.GREEN + " was found with a distance of " + ChatColor.YELLOW + MathUtil.round(initialDistance, 1) + ChatColor.GREEN + " blocks");
      } else {
         PathConnection[] route = node.findRoute(destination);
         if (route.length == 0) {
            player.sendMessage(ChatColor.RED + "Destination '" + destination.getDisplayName() + "' could not be reached from " + DebugToolUtil.coordinates(railBlock.getX(), railBlock.getY(), railBlock.getZ()));
         } else {
            double totalDistance = initialDistance;
            PathConnection[] var9 = route;
            int var10 = route.length;

            for(int var11 = 0; var11 < var10; ++var11) {
               PathConnection connection = var9[var11];
               totalDistance += connection.distance;
            }

            double maxDistance = 1600.0D;
            Color[] colors = new Color[]{Color.BLUE, Color.GREEN, Color.RED};
            int color_idx = 0;
            int lim = 10000;
            PathConnection[] var14 = route;
            int var15 = route.length;

            for(int var16 = 0; var16 < var15; ++var16) {
               PathConnection connection = var14[var16];
               TrackWalkingPoint walker = takeJunction(railBlock, connection);
               if (walker == null) {
                  player.sendMessage(ChatColor.RED + "Path broke at rail " + DebugToolUtil.coordinates(railBlock.getX(), railBlock.getY(), railBlock.getZ()));
                  return;
               }

               railBlock = connection.destination.location.getBlock();
               Color color = colors[color_idx++ % colors.length];

               do {
                  --lim;
                  if (lim <= 0 || walker.movedTotal > maxDistance) {
                     break;
                  }

                  if (!walker.move(0.3D)) {
                     DebugToolUtil.showEndOfTheRail(player, walker, initialDistance);
                     return;
                  }

                  Util.spawnDustParticle(walker.state.positionLocation(), color);
               } while(!BlockUtil.equals(railBlock, walker.state.railBlock()));

               maxDistance -= walker.movedTotal;
               if (lim <= 0 || maxDistance <= 0.0D) {
                  break;
               }
            }

            player.sendMessage(ChatColor.GREEN + "Route to " + ChatColor.YELLOW + destination.getDisplayName() + ChatColor.GREEN + " was found with a distance of " + ChatColor.YELLOW + MathUtil.round(totalDistance, 1) + ChatColor.GREEN + " blocks");
         }
      }
   }

   private void debugListAllRoutes(Player player, PathNode node, Block railBlock, double initialDistance) {
      MessageBuilder message = new MessageBuilder();
      message.gray(new Object[]{"Node "}).white(new Object[]{DebugToolUtil.coordinates(node.location.x, node.location.y, node.location.z)});
      message.gray(new Object[]{" reached after "}).white(new Object[]{MathUtil.round(initialDistance, 1)}).gray(new Object[]{" blocks"}).newLine();
      message.gray(new Object[]{"Destinations from "}).white(new Object[]{node.getDisplayName()}).gray(new Object[]{":"}).newLine();
      int color_idx = 0;
      Iterator var8 = node.getDeepNeighbours().entrySet().iterator();

      while(true) {
         PathConnection connection;
         Collection destinations;
         do {
            if (!var8.hasNext()) {
               message.send(player);
               return;
            }

            Entry<PathConnection, List<PathConnection>> entry = (Entry)var8.next();
            connection = (PathConnection)entry.getKey();
            destinations = (Collection)entry.getValue();
            Iterator iter = destinations.iterator();

            while(iter.hasNext()) {
               if (((PathConnection)iter.next()).destination.containsOnlySwitcher()) {
                  iter.remove();
               }
            }
         } while(destinations.isEmpty());

         ChatColor chatcolor = DebugToolUtil.getWheelChatColor(color_idx);
         Color color = DebugToolUtil.getWheelColor(color_idx);
         ++color_idx;
         TrackWalkingPoint walker = takeJunction(railBlock, connection);
         int limit;
         if (walker != null) {
            limit = 100;

            while(walker.move(0.3D)) {
               --limit;
               if (limit <= 0 || !(walker.movedTotal < 3.0D)) {
                  break;
               }

               Util.spawnDustParticle(walker.state.positionLocation(), color);
            }
         }

         message.append(chatcolor, new String[]{"- "});
         message.setIndent(2);
         message.setSeparator(ChatColor.GRAY, " / ");
         limit = this.maxDestinations;
         Iterator var17 = destinations.iterator();

         while(var17.hasNext()) {
            PathConnection destination = (PathConnection)var17.next();
            if (limit > 0) {
               message.append(chatcolor, new Object[]{"[", MathUtil.round(destination.distance, 1), "] ", destination.destination.getDisplayName()});
            }

            --limit;
            if (limit < 0) {
               message.append(chatcolor, new String[]{"..."});
               break;
            }
         }

         message.clearSeparator();
         message.setIndent(0);
         message.newLine();
      }
   }

   private static TrackWalkingPoint takeJunction(Block railBlock, PathConnection connection) {
      RailState state = null;
      Iterator var3 = RailType.values().iterator();

      while(var3.hasNext()) {
         RailType type = (RailType)var3.next();
         if (type.isRail(railBlock)) {
            List<RailJunction> junctions = type.getJunctions(railBlock);
            RailJunction picked = null;
            Iterator var7 = junctions.iterator();

            while(var7.hasNext()) {
               RailJunction junction = (RailJunction)var7.next();
               if (connection.junctionName.equals(junction.name())) {
                  picked = junction;
                  break;
               }
            }

            if (picked != null) {
               state = type.takeJunction(railBlock, picked);
            }
            break;
         }
      }

      if (state == null) {
         return null;
      } else {
         final PathProvider provider = connection.destination.getWorld().getProvider();
         final TrackWalkingPoint walker = new TrackWalkingPoint(state);
         walker.setLoopFilter(true);
         walker.setNavigator(new TrackWalkingPoint.Navigator<PathRoutingHandler.PathRouteEvent>() {
            public void navigate(PathRoutingHandler.PathRouteEvent routeEvent) {
               routeEvent.provider().handleRouting(routeEvent);
            }

            public PathRoutingHandler.PathRouteEvent createNewEvent() {
               return new PathRoutingHandler.PathRouteEvent(provider, walker.state.railWorld());
            }
         });
         return walker;
      }
   }
}
