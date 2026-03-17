package com.bergerkiller.bukkit.tc.pathfinding;

import com.bergerkiller.bukkit.common.BlockLocation;
import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.Task;
import com.bergerkiller.bukkit.common.component.LibraryComponent;
import com.bergerkiller.bukkit.common.config.CompressedDataReader;
import com.bergerkiller.bukkit.common.config.CompressedDataWriter;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.controller.components.RailJunction;
import com.bergerkiller.bukkit.tc.controller.components.RailPath;
import com.bergerkiller.bukkit.tc.controller.components.RailPiece;
import com.bergerkiller.bukkit.tc.controller.components.RailState;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.rails.RailLookup;
import com.bergerkiller.bukkit.tc.rails.type.RailType;
import com.bergerkiller.bukkit.tc.signactions.SignAction;
import com.bergerkiller.bukkit.tc.signactions.SignActionType;
import com.bergerkiller.bukkit.tc.utils.TrackWalkingPoint;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class PathProvider extends Task implements LibraryComponent, TrainCarts.Provider {
   private static final String SWITCHER_NAME_FALLBACK = "::traincarts::switchable::";
   public static final int DEFAULT_MAX_PROCESSING_PER_TICK = 30;
   public static boolean DEBUG_MODE = false;
   private final String fileName;
   private final Map<String, PathWorld> worlds = new HashMap();
   private final List<PathRoutingHandler> handlers = new ArrayList();
   private final Queue<BlockLocation> pendingDiscovery = new LinkedList();
   private final Set<PathNode> pendingNodes = new LinkedHashSet();
   private final Queue<PathProvider.PathFindOperation> pendingOperations = new LinkedList();
   private final Set<PathNode> scheduledNodesSinceIdle = new HashSet();
   private final Set<PathNodeSnapshot> pathNodesBeforeDiscovery = new HashSet();
   private final Set<CommandSender> sendersToNotifyOfCompletion = new HashSet();
   private boolean hasChanges = false;
   private int maxProcessingPerTick = 30;

   public PathProvider(TrainCarts plugin, String fileName) {
      super(plugin);
      this.fileName = fileName;
      this.registerRoutingHandler(new PathRoutingHandler() {
         public void process(PathRoutingHandler.PathRouteEvent event) {
            boolean switchable = false;
            List<String> destinationNames = Collections.emptyList();
            RailLookup.TrackedSign[] var4 = event.railPiece().signs();
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               RailLookup.TrackedSign trackedSign = var4[var6];
               SignAction action = trackedSign.getAction();
               if (!trackedSign.isRemoved() && action != null) {
                  SignRoutingEvent signEvent = new SignRoutingEvent(trackedSign);
                  signEvent.resetToInitialState(event.railState(), event.railPath(), event.currentDistance());
                  signEvent.overrideCartEnterState(event.railState());
                  action.route(signEvent);
                  if (signEvent.isBlocked()) {
                     event.setBlocked();
                     return;
                  }

                  if (!signEvent.isRouteSwitchable() && signEvent.getDestinationNames().isEmpty()) {
                     if (signEvent.hasSwitchedPosition()) {
                        event.setSwitchedPosition(signEvent.getSwitchedPosition());
                     }
                  } else {
                     switchable |= signEvent.isRouteSwitchable();
                     if (!signEvent.getDestinationNames().isEmpty()) {
                        if (((List)destinationNames).isEmpty()) {
                           destinationNames = new ArrayList();
                        }

                        ((List)destinationNames).addAll(signEvent.getDestinationNames());
                     }

                     event.setSwitchedPosition((RailPath.Position)null);
                  }
               }
            }

            if (switchable || !((List)destinationNames).isEmpty()) {
               PathNode newFoundNode = event.createNode();
               if (switchable) {
                  newFoundNode.addSwitcher();
               }

               Objects.requireNonNull(newFoundNode);
               ((List)destinationNames).forEach(newFoundNode::addName);
            }

         }

         public void predict(PathPredictEvent event) {
            RailLookup.TrackedSign[] var2 = event.railPiece().signs();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               RailLookup.TrackedSign trackedSign = var2[var4];
               SignAction action = trackedSign.getAction();
               if (!trackedSign.isRemoved() && action != null && action.hasPathFindingPrediction()) {
                  SignActionEvent signEvent = trackedSign.createEvent(SignActionType.GROUP_ENTER);
                  signEvent.setMember(event.member());
                  signEvent.overrideCartEnterState(event.railState());
                  action.predictPathFinding(signEvent, event);
               }
            }

         }
      });
   }

   public TrainCarts getTrainCarts() {
      return (TrainCarts)super.getPlugin();
   }

   public void registerRoutingHandler(PathRoutingHandler handler) {
      this.handlers.add(handler);
   }

   public void unregisterRoutingHandler(PathRoutingHandler handler) {
      this.handlers.remove(handler);
   }

   public void predictRoutingHandler(PathPredictEvent event) {
      this.handlers.forEach((handler) -> {
         handler.predict(event);
      });
   }

   public void setMaxProcessingPerTick(int durationMillis) {
      this.maxProcessingPerTick = durationMillis;
   }

   public int getNumPendingNodes() {
      return this.pendingDiscovery.size() + this.pendingNodes.size();
   }

   public int getNumPendingOperations() {
      return this.pendingOperations.size();
   }

   public void notifyOfCompletion(CommandSender sender) {
      this.sendersToNotifyOfCompletion.add(sender);
   }

   public void enable() {
      this.start(1L, 1L);
      (new CompressedDataReader(this.fileName) {
         public void read(DataInputStream stream) throws IOException {
            PathProvider.this.worlds.clear();
            int count = stream.readInt();
            PathNode[] parr = new PathNode[count];

            int ncount;
            int ix;
            for(int i = 0; i < count; ++i) {
               String name = stream.readUTF();
               BlockLocation loc = new BlockLocation(stream.readUTF(), stream.readInt(), stream.readInt(), stream.readInt());
               parr[i] = PathProvider.this.getWorld(loc.world).addNode(loc);
               if (name.isEmpty()) {
                  parr[i].addSwitcher();
               } else {
                  String[] var7 = name.split("\n");
                  ncount = var7.length;

                  for(ix = 0; ix < ncount; ++ix) {
                     String name_part = var7[ix];
                     if (name_part.equals("::traincarts::switchable::")) {
                        parr[i].addSwitcher();
                     } else {
                        parr[i].addName(name_part);
                     }
                  }
               }
            }

            PathNode[] var11 = parr;
            int var12 = parr.length;

            for(int var13 = 0; var13 < var12; ++var13) {
               PathNode node = var11[var13];
               ncount = stream.readInt();

               for(ix = 0; ix < ncount; ++ix) {
                  node.addNeighbourFast(new PathConnection(parr[stream.readInt()], stream));
               }
            }

            PathProvider.this.pendingNodes.clear();
            PathProvider.this.scheduledNodesSinceIdle.clear();
         }
      }).read();
      this.hasChanges = false;
      if (TCConfig.rerouteOnStartup) {
         this.reroute();
      }

   }

   public void disable() {
      this.stop();
      Iterator var1 = this.getWorlds().iterator();

      while(var1.hasNext()) {
         PathWorld world = (PathWorld)var1.next();
         world.clearAll();
      }

   }

   public void save(boolean autosave, String filename) {
      if (!autosave || this.hasChanges) {
         (new CompressedDataWriter(filename) {
            public void write(DataOutputStream stream) throws IOException {
               int totalNodeCount = 0;

               PathWorld world;
               for(Iterator var3 = PathProvider.this.getWorlds().iterator(); var3.hasNext(); totalNodeCount += world.getNodes().size()) {
                  world = (PathWorld)var3.next();
               }

               stream.writeInt(totalNodeCount);
               int i = 0;
               Iterator var11 = PathProvider.this.getWorlds().iterator();

               PathWorld worldx;
               Iterator var6;
               PathNode node;
               while(var11.hasNext()) {
                  worldx = (PathWorld)var11.next();

                  for(var6 = worldx.getNodes().iterator(); var6.hasNext(); ++i) {
                     node = (PathNode)var6.next();
                     node.index = i;
                     if (node.containsSwitcher()) {
                        if (node.getNames().isEmpty()) {
                           stream.writeUTF("");
                        } else {
                           stream.writeUTF("::traincarts::switchable::\n" + StringUtil.join("\n", node.getNames()));
                        }
                     } else {
                        stream.writeUTF(StringUtil.join("\n", node.getNames()));
                     }

                     stream.writeUTF(node.location.world);
                     stream.writeInt(node.location.x);
                     stream.writeInt(node.location.y);
                     stream.writeInt(node.location.z);
                  }
               }

               var11 = PathProvider.this.getWorlds().iterator();

               while(var11.hasNext()) {
                  worldx = (PathWorld)var11.next();
                  var6 = worldx.getNodes().iterator();

                  while(var6.hasNext()) {
                     node = (PathNode)var6.next();
                     stream.writeInt(node.getNeighbours().size());
                     Iterator var8 = node.getNeighbours().iterator();

                     while(var8.hasNext()) {
                        PathConnection conn = (PathConnection)var8.next();
                        conn.writeTo(stream);
                     }
                  }
               }

            }
         }).write();
         this.hasChanges = false;
      }
   }

   public Collection<PathWorld> getWorlds() {
      return this.worlds.values();
   }

   public PathWorld getWorld(String worldName) {
      return (PathWorld)this.worlds.computeIfAbsent(worldName, (name) -> {
         return new PathWorld(this, name);
      });
   }

   public PathWorld getWorld(World world) {
      return this.getWorld(world.getName());
   }

   public boolean nodeExistsOnAnyWorld(String name) {
      Iterator var2 = this.getWorlds().iterator();

      PathWorld world;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         world = (PathWorld)var2.next();
      } while(world.getNodeByName(name) == null);

      return true;
   }

   public PathNode tryFindNodeAgain(PathNodeSnapshot snapshot) {
      PathWorld world = this.getWorld(snapshot.getWorldName());
      return world != null ? world.tryFindNodeAgain(snapshot) : null;
   }

   public void clearAll() {
      Iterator var1 = this.getWorlds().iterator();

      while(var1.hasNext()) {
         PathWorld world = (PathWorld)var1.next();
         world.clearAll();
      }

   }

   public void reroute() {
      Iterator var1 = this.getWorlds().iterator();

      while(var1.hasNext()) {
         PathWorld world = (PathWorld)var1.next();
         world.rerouteAll();
      }

   }

   public void rerouteFrom(List<String> destinationNames) {
      Iterator var2 = this.getWorlds().iterator();

      while(var2.hasNext()) {
         PathWorld world = (PathWorld)var2.next();
         world.rerouteFrom(destinationNames);
      }

   }

   public void stopRouting() {
      this.pendingDiscovery.clear();
      this.pendingNodes.clear();
      this.pendingOperations.clear();
      this.scheduledNodesSinceIdle.clear();
   }

   protected void markChanged() {
      this.hasChanges = true;
   }

   /** @deprecated */
   @Deprecated
   public static void schedule(PathNode startNode) {
      TrainCarts.plugin.getPathProvider().scheduleNode(startNode);
   }

   public void scheduleNode(PathNode startNode) {
      this.pendingNodes.add(startNode);
      this.scheduledNodesSinceIdle.add(startNode);
   }

   public void scheduleNodeIfNotRecentlyRouted(PathNode startNode) {
      if (this.scheduledNodesSinceIdle.add(startNode)) {
         this.pendingNodes.add(startNode);
      }

   }

   /** @deprecated */
   @Deprecated
   public static void discover(BlockLocation railLocation) {
      TrainCarts.plugin.getPathProvider().discoverFromRail(railLocation);
   }

   public void discoverFromRail(BlockLocation railLocation) {
      PathWorld world = this.getWorld(railLocation.world);
      if (world != null) {
         PathNode atRail = world.getNodeAtRail(railLocation);
         if (atRail != null) {
            this.pathNodesBeforeDiscovery.add(atRail.getSnapshot());
         }
      }

      this.pendingDiscovery.add(railLocation);
   }

   public void discoverFromNode(PathNode node) {
      this.pathNodesBeforeDiscovery.add(node.getSnapshot());
      this.discoverFromRail(node.location);
   }

   public boolean isProcessing() {
      return !this.pendingDiscovery.isEmpty() || !this.pendingOperations.isEmpty() || !this.pendingNodes.isEmpty();
   }

   public Task stop() {
      this.addPendingNodes();
      if (!this.pendingOperations.isEmpty()) {
         this.getTrainCarts().log(Level.INFO, "Performing " + this.pendingOperations.size() + " pending path finding operations (can take a while)...");

         while(!this.pendingOperations.isEmpty()) {
            PathProvider.PathFindOperation operation = (PathProvider.PathFindOperation)this.pendingOperations.poll();

            while(true) {
               if (operation.next()) {
                  continue;
               }
            }
         }
      }

      return super.stop();
   }

   public void run() {
      if (this.pendingOperations.isEmpty() && !this.pendingDiscovery.isEmpty()) {
         this.addNewlyDiscovered();
      }

      if (this.pendingOperations.isEmpty()) {
         this.addPendingNodes();
      }

      boolean done;
      if (this.pendingOperations.isEmpty()) {
         this.scheduledNodesSinceIdle.clear();
         if (!this.sendersToNotifyOfCompletion.isEmpty()) {
            done = true;
            List<MessageBuilder> updateMessages = (List)this.pathNodesBeforeDiscovery.stream().sorted().map((snapshot) -> {
               return snapshot.getUpdateMessage(this.tryFindNodeAgain(snapshot));
            }).filter(Objects::nonNull).limit(11L).collect(Collectors.toList());
            boolean hasMore = updateMessages.size() > 10;
            if (hasMore) {
               updateMessages = updateMessages.subList(0, 10);
            }

            this.pathNodesBeforeDiscovery.clear();
            List<CommandSender> senders = new ArrayList(this.sendersToNotifyOfCompletion);
            this.sendersToNotifyOfCompletion.clear();
            Iterator var5 = senders.iterator();

            while(true) {
               CommandSender sender;
               do {
                  do {
                     if (!var5.hasNext()) {
                        return;
                     }

                     sender = (CommandSender)var5.next();
                  } while(!(sender instanceof ConsoleCommandSender) && !(sender instanceof Player));
               } while(sender instanceof Player && !((Player)sender).isValid());

               Iterator var7 = updateMessages.iterator();

               while(var7.hasNext()) {
                  MessageBuilder message = (MessageBuilder)var7.next();
                  message.send(sender);
               }

               if (hasMore) {
                  sender.sendMessage(ChatColor.YELLOW + "...and more changes");
               }

               sender.sendMessage(ChatColor.GREEN + "Train rerouting completed!");
            }
         }
      } else {
         long startTime = System.currentTimeMillis();

         while(!this.pendingOperations.isEmpty()) {
            PathProvider.PathFindOperation operation = (PathProvider.PathFindOperation)this.pendingOperations.peek();
            done = false;
            if (DEBUG_MODE) {
               this.getTrainCarts().log(Level.INFO, "DISCOVERING EVERYTHING FROM " + operation.startNode.getDisplayName() + " INTO " + operation.getJunctionName());
            }

            do {
               done = operation.next();
            } while(!done && System.currentTimeMillis() - startTime <= (long)this.maxProcessingPerTick);

            if (!done) {
               break;
            }

            this.pendingOperations.poll();
         }

         RailLookup.forceRecalculation();
      }
   }

   private void addNewlyDiscovered() {
      long startTime = System.currentTimeMillis();

      do {
         BlockLocation railLocation = (BlockLocation)this.pendingDiscovery.poll();
         if (railLocation == null) {
            break;
         }

         if (PathNode.get(railLocation) == null) {
            Block railBlock = railLocation.getBlock();
            if (railBlock != null) {
               RailType railType = RailType.getType(railBlock);
               if (railType != RailType.NONE) {
                  RailState initialState = RailState.getSpawnState(RailPiece.create(railType, railBlock));
                  PathRoutingHandler.PathRouteEvent routeEvent = new PathRoutingHandler.PathRouteEvent(this, initialState.railWorld());
                  routeEvent.resetToInitialState(initialState, initialState.loadRailLogic().getPath(), 0.0D);
                  Iterator var8 = this.handlers.iterator();

                  while(var8.hasNext()) {
                     PathRoutingHandler handler = (PathRoutingHandler)var8.next();
                     handler.process(routeEvent);
                  }
               }
            }
         }
      } while(System.currentTimeMillis() - startTime <= (long)this.maxProcessingPerTick);

   }

   private void addPendingNodes() {
      if (!this.pendingNodes.isEmpty()) {
         Set<PathNode> newPending = new LinkedHashSet(this.pendingNodes);
         Iterator var2 = newPending.iterator();

         while(true) {
            while(true) {
               PathNode node;
               Block startRail;
               RailType startType;
               do {
                  if (!var2.hasNext()) {
                     this.pendingNodes.removeAll(newPending);
                     return;
                  }

                  node = (PathNode)var2.next();
                  startRail = node.location.getBlock();
                  startType = RailType.getType(startRail);
               } while(startType == RailType.NONE);

               if (node.containsSwitcher()) {
                  if (DEBUG_MODE) {
                     this.getTrainCarts().log(Level.INFO, "NODE " + node.getDisplayName() + " CONTAINS A SWITCHER, BRANCHING OFF");
                  }

                  Iterator var10 = startType.getJunctions(startRail).iterator();

                  while(var10.hasNext()) {
                     RailJunction junc = (RailJunction)var10.next();
                     RailState state = startType.takeJunction(startRail, junc);
                     if (state != null) {
                        this.scheduleNode(node, state, junc);
                     }
                  }
               } else {
                  RailState state1 = new RailState();
                  state1.setRailPiece(RailPiece.create(startType, startRail));
                  state1.position().setLocation(startType.getSpawnLocation(startRail, BlockFace.NORTH));
                  if (RailType.loadRailInformation(state1)) {
                     state1.loadRailLogic().getPath().snap(state1.position(), state1.railBlock());
                     Block railBlock = state1.railBlock();
                     List<RailJunction> junctions = state1.railPiece().getJunctions();
                     if (!junctions.isEmpty()) {
                        RailState state2 = state1.clone();
                        state2.position().invertMotion();
                        state2.initEnterDirection();
                        state1.loadRailLogic().getPath().move(state1, Double.MAX_VALUE);
                        state2.loadRailLogic().getPath().move(state2, Double.MAX_VALUE);
                        this.scheduleNode(node, state1, findBestJunction(junctions, railBlock, state1.position()));
                        this.scheduleNode(node, state2, findBestJunction(junctions, railBlock, state2.position()));
                     }
                  }
               }
            }
         }
      }
   }

   private static RailJunction findBestJunction(List<RailJunction> junctions, Block railBlock, RailPath.Position position) {
      if (junctions.isEmpty()) {
         throw new IllegalArgumentException("Junctions list is empty");
      } else {
         RailJunction best = null;
         double bestDistanceSq = Double.MAX_VALUE;
         Iterator var6 = junctions.iterator();

         while(var6.hasNext()) {
            RailJunction junction = (RailJunction)var6.next();
            double dist_sq = junction.position().distanceSquaredAtRail(railBlock, position);
            if (dist_sq < bestDistanceSq) {
               bestDistanceSq = dist_sq;
               best = junction;
            }
         }

         return best;
      }
   }

   private void scheduleNode(PathNode node, RailState state, RailJunction junction) {
      if (state.railPiece().offlineWorld().isLoaded()) {
         try {
            this.pendingOperations.offer(new PathProvider.PathFindOperation(this, node, state, junction));
         } catch (Throwable var5) {
            this.getTrainCarts().getLogger().log(Level.SEVERE, "Failed to schedule path finding operation for node at " + node.location, var5);
         }

      }
   }

   public void handleRouting(PathRoutingHandler.PathRouteEvent routeEvent) {
      Iterator var2 = this.handlers.iterator();

      while(var2.hasNext()) {
         PathRoutingHandler handler = (PathRoutingHandler)var2.next();
         handler.process(routeEvent);
      }

   }

   public PathRoutingHandler.PathRouteEvent handleRouting(RailState railState, RailPath railPath, double currentDistance) {
      PathRoutingHandler.PathRouteEvent routeEvent = new PathRoutingHandler.PathRouteEvent(this, railState.railWorld());
      routeEvent.resetToInitialState(railState, railPath, currentDistance);
      this.handleRouting(routeEvent);
      return routeEvent;
   }

   private static class PathFindOperation {
      private final PathProvider provider;
      private final World world;
      private final TrackWalkingPoint p;
      private final PathNode startNode;
      private final String junctionName;

      public PathFindOperation(final PathProvider provider, final PathNode startNode, RailState state, RailJunction junction) {
         this.provider = provider;
         this.world = state.railWorld();
         this.junctionName = junction.name();
         this.startNode = startNode;
         this.p = new TrackWalkingPoint(state);
         this.p.setNavigator(new TrackWalkingPoint.Navigator<PathRoutingHandler.PathRouteEvent>() {
            public void navigate(PathRoutingHandler.PathRouteEvent event) {
               Iterator var2 = event.provider().handlers.iterator();

               while(var2.hasNext()) {
                  PathRoutingHandler handler = (PathRoutingHandler)var2.next();
                  handler.process(event);
               }

               PathNode foundNode = event.getLastSetNode();
               if (foundNode != null && !startNode.location.equals(foundNode.location)) {
                  double totalDistance = PathFindOperation.this.p.movedTotal;
                  Location spawnPos = PathFindOperation.this.p.state.railType().getSpawnLocation(PathFindOperation.this.p.state.railBlock(), PathFindOperation.this.p.state.position().getMotionFace());
                  totalDistance += spawnPos.distance(PathFindOperation.this.p.state.positionLocation());
                  startNode.addNeighbour(foundNode, totalDistance, PathFindOperation.this.getJunctionName());
                  if (PathProvider.DEBUG_MODE) {
                     event.provider().getTrainCarts().log(Level.INFO, "MADE CONNECTION FROM " + startNode.getDisplayName() + " TO " + foundNode.getDisplayName());
                  }

                  event.abortNavigation();
               }
            }

            public PathRoutingHandler.PathRouteEvent createNewEvent() {
               return new PathRoutingHandler.PathRouteEvent(provider, PathFindOperation.this.world);
            }
         });
         this.p.setLoopFilter(true);
         Location spawnPos = state.railType().getSpawnLocation(state.railBlock(), state.position().getMotionFace());
         TrackWalkingPoint var10000 = this.p;
         var10000.movedTotal += state.positionLocation().distance(spawnPos);
      }

      public String getJunctionName() {
         return this.junctionName;
      }

      public boolean next() {
         if (!this.p.state.railLookup().isValid()) {
            return true;
         } else {
            return !this.p.moveFull();
         }
      }
   }
}
