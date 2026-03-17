package com.bergerkiller.bukkit.tc.debug;

import com.bergerkiller.bukkit.common.Task;
import com.bergerkiller.bukkit.common.bases.IntVector2;
import com.bergerkiller.bukkit.common.bases.IntVector3;
import com.bergerkiller.bukkit.common.dep.cloud.annotation.specifier.Quoted;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Argument;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Command;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.CommandDescription;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Flag;
import com.bergerkiller.bukkit.common.inventory.CommonItemStack;
import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.common.math.Quaternion;
import com.bergerkiller.bukkit.common.utils.DebugUtil;
import com.bergerkiller.bukkit.common.utils.MaterialUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.bergerkiller.bukkit.common.wrappers.ItemDisplayMode;
import com.bergerkiller.bukkit.tc.Localization;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.VirtualDisplayItemEntity;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentManager;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.bukkit.tc.commands.annotations.CommandRequiresPermission;
import com.bergerkiller.bukkit.tc.commands.annotations.CommandTargetTrain;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartGroupStore;
import com.bergerkiller.bukkit.tc.controller.MinecartMemberStore;
import com.bergerkiller.bukkit.tc.controller.components.RailPiece;
import com.bergerkiller.bukkit.tc.controller.components.RailState;
import com.bergerkiller.bukkit.tc.controller.global.SignControllerWorld;
import com.bergerkiller.bukkit.tc.debug.types.DebugToolTypeListDestinations;
import com.bergerkiller.bukkit.tc.debug.types.DebugToolTypeRails;
import com.bergerkiller.bukkit.tc.debug.types.DebugToolTypeTrackDistance;
import com.bergerkiller.bukkit.tc.offline.train.OfflineGroupManager;
import com.bergerkiller.bukkit.tc.pathfinding.PathNode;
import com.bergerkiller.bukkit.tc.rails.RailLookup;
import com.bergerkiller.bukkit.tc.utils.EventListenerHook;
import com.bergerkiller.bukkit.tc.utils.PlayerVelocityController;
import java.util.Collection;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.util.Vector;

public class DebugCommands {
   @CommandRequiresPermission(Permission.DEBUG_COMMAND_DEBUG)
   @Command("train debug event vehicle_enter [enabled]")
   @CommandDescription("Broadcasts a message when a vehicle enter is cancelled by a plugin")
   private void commandDebugEventVehicleEnter(CommandSender sender, @Argument("enabled") boolean enabled) {
      sender.sendMessage(ChatColor.RED + "Vehicle enter debug mode: " + Localization.boolStr(enabled));
      if (enabled) {
         EventListenerHook.hook(VehicleEnterEvent.class, (listener, callEvent, event) -> {
            boolean wasCancelled = event.isCancelled();
            callEvent.accept(event);
            if (!wasCancelled && event.isCancelled() && MinecartMemberStore.getFromEntity(event.getVehicle()) != null) {
               Bukkit.broadcastMessage("[TrainCarts] Vehicle enter by " + event.getEntered().getName() + " was cancelled by plugin " + listener.getPlugin().getName());
            }

         });
         sender.sendMessage(ChatColor.YELLOW + "A message will be broadcast when entering a traincarts minecart is cancelled by a plugin, with details");
         sender.sendMessage(ChatColor.YELLOW + "Use /train debug vehicle_enter false to turn off again");
      } else {
         EventListenerHook.unhook(VehicleEnterEvent.class);
      }

   }

   @CommandRequiresPermission(Permission.DEBUG_COMMAND_DEBUG)
   @Command("train debug event vehicle_exit [enabled]")
   @CommandDescription("Broadcasts a message when a vehicle exit is cancelled by a plugin")
   private void commandDebugEventVehicleExit(CommandSender sender, @Argument("enabled") boolean enabled) {
      sender.sendMessage(ChatColor.RED + "Vehicle exit debug mode: " + Localization.boolStr(enabled));
      if (enabled) {
         EventListenerHook.hook(VehicleExitEvent.class, (listener, callEvent, event) -> {
            boolean wasCancelled = event.isCancelled();
            callEvent.accept(event);
            if (!wasCancelled && event.isCancelled() && MinecartMemberStore.getFromEntity(event.getVehicle()) != null) {
               Bukkit.broadcastMessage("[TrainCarts] Vehicle exit by " + event.getExited().getName() + " was cancelled by plugin " + listener.getPlugin().getName());
            }

         });
         sender.sendMessage(ChatColor.YELLOW + "A message will be broadcast when exiting a traincarts minecart is cancelled by a plugin, with details");
         sender.sendMessage(ChatColor.YELLOW + "Use /train debug vehicle_exit false to turn off again");
      } else {
         EventListenerHook.unhook(VehicleExitEvent.class);
      }

   }

   @CommandRequiresPermission(Permission.DEBUG_COMMAND_DEBUG)
   @Command("train debug rails")
   @CommandDescription("Get a debug stick item to visually display what path tracks use")
   private void commandDebugRails(Player player) {
      (new DebugToolTypeRails()).giveToPlayer(player);
   }

   @CommandRequiresPermission(Permission.DEBUG_COMMAND_DEBUG)
   @Command("train debug distance")
   @CommandDescription("Get a debug stick item to display the track distance between two points")
   private void commandDebugTrackDistance(Player player) {
      (new DebugToolTypeTrackDistance()).giveToPlayer(player);
   }

   @CommandRequiresPermission(Permission.DEBUG_COMMAND_DEBUG)
   @Command("train debug destinations")
   @CommandDescription("Get a debug stick item to visually display the possible path finding routes")
   private void commandDebugDestinationAll(Player player, @Flag("max-destinations") Integer maxDestinations) {
      (new DebugToolTypeListDestinations()).setMaxDestinations(maxDestinations != null ? maxDestinations : 5).giveToPlayer(player);
   }

   @CommandRequiresPermission(Permission.DEBUG_COMMAND_DEBUG)
   @Command("train debug destination")
   @CommandDescription("Get a debug stick item to visually display the possible path finding routes")
   private void commandDebugDestinationAll(Player player) {
      (new DebugToolTypeListDestinations()).giveToPlayer(player);
   }

   @CommandRequiresPermission(Permission.DEBUG_COMMAND_DEBUG)
   @Command("train debug destination <destination>")
   @CommandDescription("Get a debug stick item to visually display the route towards a destination")
   private void commandDebugDestinationName(Player player, @Quoted @Argument(value = "destination",suggestions = "destinations") String destination) {
      (new DebugToolTypeListDestinations(destination)).giveToPlayer(player);
   }

   @CommandRequiresPermission(Permission.DEBUG_COMMAND_DEBUG)
   @Command("train debug destination <destination> teleport")
   @CommandDescription("Get a debug stick item to visually display the route towards a destination")
   private void commandDebugTeleportToDestination(Player player, TrainCarts plugin, @Quoted @Argument(value = "destination",suggestions = "destinations") String destination) {
      PathNode node = plugin.getPathProvider().getWorld(player.getWorld()).getNodeByName(destination);
      if (node == null) {
         node = (PathNode)plugin.getPathProvider().getWorlds().stream().map((w) -> {
            return w.getNodeByName(destination);
         }).findFirst().orElse((Object)null);
         if (node == null) {
            player.sendMessage(ChatColor.RED + "Destination with name '" + destination + "' not found");
            return;
         }
      }

      RailPiece rail = RailPiece.create(node.location.getBlock());
      if (rail.isNone()) {
         player.sendMessage(ChatColor.RED + "There are no rails at this destination! (No longer exists?)");
      } else {
         RailState spawnState = RailState.getSpawnState(rail);
         player.teleport(spawnState.positionLocation());
         player.sendMessage(ChatColor.GREEN + "Teleported to destination '" + ChatColor.YELLOW + destination + ChatColor.GREEN + "'!");
      }
   }

   @CommandRequiresPermission(Permission.DEBUG_COMMAND_DEBUG)
   @Command("train debug mutex")
   @CommandDescription("Displays the area of effect of all nearby mutex signs")
   private void commandDebugMutex(Player player, TrainCarts traincarts) {
      DebugTool.showMutexZones(traincarts, player);
      player.sendMessage(ChatColor.GREEN + "Displaying mutex zones near your position");
   }

   @CommandRequiresPermission(Permission.DEBUG_COMMAND_DEBUG)
   @Command("train debug railtracker <enabled>")
   @CommandDescription("Sets whether the rail tracker debugging is currently enabled")
   private void commandDebugSetRailTracker(CommandSender sender, @Argument("enabled") boolean enabled) {
      TCConfig.railTrackerDebugEnabled = enabled;
      this.commandDebugCheckRailTracker(sender);
   }

   @CommandRequiresPermission(Permission.DEBUG_COMMAND_DEBUG)
   @Command("train debug railtracker")
   @CommandDescription("Checks whether the rail tracker debugging is currently enabled")
   private void commandDebugCheckRailTracker(CommandSender sender) {
      sender.sendMessage(ChatColor.GREEN + "Displaying tracked rail positions: " + (TCConfig.railTrackerDebugEnabled ? "ENABLED" : ChatColor.RED + "DISABLED"));
   }

   @CommandRequiresPermission(Permission.DEBUG_COMMAND_DEBUG)
   @Command("train debug wheeltracker <enabled>")
   @CommandDescription("Sets whether the rail tracker debugging is currently enabled")
   private void commandDebugSetWheelTracker(CommandSender sender, @Argument("enabled") boolean enabled) {
      TCConfig.wheelTrackerDebugEnabled = enabled;
      this.commandDebugCheckWheelTracker(sender);
   }

   @CommandRequiresPermission(Permission.DEBUG_COMMAND_DEBUG)
   @Command("train debug wheeltracker")
   @CommandDescription("Checks whether the wheel tracker debugging is currently enabled")
   private void commandDebugCheckWheelTracker(CommandSender sender) {
      sender.sendMessage(ChatColor.GREEN + "Displaying tracked wheel positions: " + (TCConfig.wheelTrackerDebugEnabled ? "ENABLED" : ChatColor.RED + "DISABLED"));
   }

   @CommandRequiresPermission(Permission.DEBUG_COMMAND_DEBUG)
   @Command("train debug splitting <enabled>")
   @CommandDescription("Sets whether messages are logged when trains split apart")
   private void commandDebugSetSplitDebugEnabled(CommandSender sender, @Argument("enabled") boolean enabled) {
      TCConfig.logTrainSplitting = enabled;
      this.commandDebugCheckSplitDebugEnabled(sender);
   }

   @CommandRequiresPermission(Permission.DEBUG_COMMAND_DEBUG)
   @Command("train debug splitting")
   @CommandDescription("Checks whether messages are logged when trains split apart")
   private void commandDebugCheckSplitDebugEnabled(CommandSender sender) {
      sender.sendMessage(ChatColor.GREEN + "Logging messages when trains split apart: " + (TCConfig.logTrainSplitting ? "ENABLED" : ChatColor.RED + "DISABLED"));
   }

   @CommandRequiresPermission(Permission.DEBUG_COMMAND_DEBUG)
   @Command("train debug fix signs")
   @CommandDescription("Forcibly recalculates all cached sign information near the player")
   private void commandDebugFixSigns(Player player, TrainCarts plugin, @Flag("redetect_actions") boolean redetectSignActions) {
      if (!TCConfig.enableVanillaActionSigns) {
         player.sendMessage(ChatColor.RED + "Vanilla action signs are disabled in TrainCarts config.yml!");
      } else {
         int radius = Bukkit.getViewDistance() - 1;
         IntVector2 mid = IntVector3.blockOf(player.getLocation()).toChunkCoordinates();
         SignControllerWorld controller = plugin.getSignController().forWorld(player.getWorld());
         SignControllerWorld.RefreshResult result = SignControllerWorld.RefreshResult.NONE;

         for(int cx = -radius; cx <= radius; ++cx) {
            for(int cz = -radius; cz <= radius; ++cz) {
               Chunk chunk = WorldUtil.getChunk(player.getWorld(), mid.x + cx, mid.z + cz);
               if (chunk != null) {
                  result = result.add(controller.refreshInChunk(chunk));
               }
            }
         }

         if (result.numAdded == 0 && result.numRemoved == 0) {
            player.sendMessage(ChatColor.GREEN + "All signs are correctly cached");
         } else {
            if (result.numRemoved > 0) {
               player.sendMessage(ChatColor.RED.toString() + result.numRemoved + " signs were removed from the cache because they were incorrect!");
            }

            if (result.numAdded > 0) {
               player.sendMessage(ChatColor.YELLOW.toString() + result.numAdded + " signs were missing and have been added to the cache!");
            }
         }

         if (redetectSignActions) {
            plugin.redetectSignActions();
            player.sendMessage(ChatColor.GREEN + "Recalculated the registered sign action for all signs on the server");
         }

      }
   }

   @CommandRequiresPermission(Permission.COMMAND_FIXBUGGED)
   @Command("train debug fix buggedminecarts")
   @CommandDescription("Forcibly removes minecarts and trackers that have glitched out")
   private void commandFixBugged(CommandSender sender) {
      Iterator var2 = WorldUtil.getWorlds().iterator();

      while(var2.hasNext()) {
         World world = (World)var2.next();
         OfflineGroupManager.removeBuggedMinecarts(world);
      }

      sender.sendMessage(ChatColor.YELLOW + "Bugged minecarts have been forcibly removed.");
   }

   @CommandRequiresPermission(Permission.DEBUG_COMMAND_DEBUG)
   @Command("train debug railcache export")
   @CommandDescription("Exports the rail block coordinates inside the current player world's rail cache")
   private void commandDebugRailCacheExport(Player player, TrainCarts plugin) {
      Collection<IntVector3> blocks = RailLookup.forWorld(player.getWorld()).getBlockIndex();
      StringBuffer buffer = new StringBuffer(blocks.size() * 20);
      Iterator var5 = blocks.iterator();

      while(var5.hasNext()) {
         IntVector3 block = (IntVector3)var5.next();
         buffer.append(block.x).append(' ').append(block.y).append(' ').append(block.z).append("\r\n");
      }

      TCConfig.hastebin.upload(buffer.toString()).thenAccept((t) -> {
         if (t.success()) {
            player.sendMessage(ChatColor.GREEN + "Rail cache block index exported: " + ChatColor.WHITE + ChatColor.UNDERLINE + t.url());
         } else {
            player.sendMessage(ChatColor.RED + "Failed to export rail cache block coordinates: " + t.error());
         }

      });
   }

   @CommandTargetTrain
   @CommandRequiresPermission(Permission.DEBUG_COMMAND_DEBUG)
   @Command("train debug loading unload")
   @CommandDescription("Forces the targeted train to unload, even if it otherwise wouldn't")
   private void commandDebugUnloadTrain(CommandSender sender, MinecartGroup group) {
      String name = group.getProperties().getTrainName();
      group.unload();
      sender.sendMessage(ChatColor.YELLOW + "Train '" + ChatColor.WHITE + name + ChatColor.YELLOW + "' unloaded!");
   }

   @CommandRequiresPermission(Permission.DEBUG_COMMAND_DEBUG)
   @Command("train debug loading refresh")
   @CommandDescription("Forcibly checks all unloaded trains if they can be loaded, and loads them in")
   private void commandDebugForceLoadTrains(CommandSender sender, TrainCarts trainCarts) {
      int loadedBefore = MinecartGroupStore.getGroups().size();
      trainCarts.getOfflineGroups().refresh();
      int loadedAfter = MinecartGroupStore.getGroups().size();
      if (loadedBefore == loadedAfter) {
         sender.sendMessage(ChatColor.YELLOW + "Forcibly refreshed trains on all worlds, no trains loaded");
      } else {
         sender.sendMessage(ChatColor.YELLOW + "Forcibly refreshed trains on all worlds, " + ChatColor.WHITE + (loadedAfter - loadedBefore) + ChatColor.YELLOW + " trains loaded");
      }

   }

   @CommandRequiresPermission(Permission.DEBUG_COMMAND_DEBUG)
   @Command("train debug pvc fly")
   private void commandTestFlight(final Player player, TrainCarts plugin) {
      final Vector center = new Vector(-175.5D, 14.1D, 354.5D);
      (new Task(plugin) {
         Quaternion rotation = new Quaternion();
         PlayerVelocityController controller = new PlayerVelocityController(player);
         Location loc = player.getLocation();
         int ctr = 0;
         final int duration = 200000;
         double rotX = 0.0D;
         double rotY = 0.0D;
         double radius = 1.0D;
         double speed = 0.0D;
         final double incr = 0.5D;
         Vector lastMotion = new Vector();

         public void run() {
            ++this.ctr;
            if (this.ctr > 200005) {
               this.controller.stop();
               this.stop();
            }

            if (this.ctr <= 200002) {
               if (player.isSneaking()) {
                  player.teleport(center.toLocation(player.getWorld()));
                  this.controller.stop();
                  this.stop();
               } else if (!player.isValid()) {
                  this.stop();
                  this.controller.stop();
               } else {
                  if (this.ctr <= 1) {
                     player.setFlying(true);
                     player.teleport(this.loc);
                     player.setFlying(true);
                  }

                  this.controller.setPosition(this.loc.toVector());
                  this.speed *= 0.9D;
                  if (this.controller.horizontalInput() == PlayerVelocityController.HorizontalPlayerInput.NONE && this.controller.verticalInput() == PlayerVelocityController.VerticalPlayerInput.NONE) {
                     if (this.speed < 0.01D) {
                        this.speed = 0.0D;
                     }
                  } else {
                     this.lastMotion = new Vector();
                     this.speed += 0.2D;
                  }

                  Quaternion q = Quaternion.fromLookDirection(player.getEyeLocation().getDirection(), new Vector(0, 1, 0));
                  if (this.controller.verticalInput() == PlayerVelocityController.VerticalPlayerInput.JUMP) {
                     this.lastMotion.add(q.upVector());
                  }

                  if (this.controller.horizontalInput().forwards()) {
                     this.lastMotion.add(q.forwardVector());
                  } else if (this.controller.horizontalInput().backwards()) {
                     this.lastMotion.add(q.forwardVector().multiply(-1.0D));
                  }

                  if (this.controller.horizontalInput().left()) {
                     this.lastMotion.add(q.rightVector());
                  } else if (this.controller.horizontalInput().right()) {
                     this.lastMotion.add(q.rightVector().multiply(-1.0D));
                  }

                  this.loc.add(this.lastMotion.clone().multiply(this.speed));
               }
            }
         }
      }).start(5L, 1L);
      player.sendMessage("Started");
   }

   @CommandRequiresPermission(Permission.DEBUG_COMMAND_DEBUG)
   @Command("train debug pvc swing")
   private void commandTestSwing(final Player player, TrainCarts plugin) {
      double radius = 10.0D;
      final Vector center = new Vector(-175.5D, 14.1D, 354.5D);
      (new Task(plugin) {
         Quaternion rotation = new Quaternion();
         PlayerVelocityController controller = new PlayerVelocityController(player);
         int ctr = 0;
         final int duration = 200000;

         public void run() {
            ++this.ctr;
            if (this.ctr > 200005) {
               this.controller.stop();
               this.stop();
            }

            if (this.ctr <= 200002) {
               if (player.isSneaking()) {
                  player.teleport(center.toLocation(player.getWorld()));
                  this.controller.stop();
                  this.stop();
               } else if (!player.isValid()) {
                  this.stop();
                  this.controller.stop();
               } else {
                  Vector pos = center.clone().add(this.rotation.forwardVector().multiply(10.0D));
                  if (this.ctr <= 1) {
                     Location loc = player.getLocation();
                     loc.setX(pos.getX());
                     loc.setY(pos.getY());
                     loc.setZ(pos.getZ());
                     player.setFlying(true);
                     player.teleport(loc);
                     player.setFlying(true);
                  }

                  this.controller.setPosition(pos);
                  if (this.ctr > 0) {
                     this.rotation.rotateX(4.0D);
                  }

               }
            }
         }
      }).start(5L, 1L);
      player.sendMessage("Started");
   }

   @CommandRequiresPermission(Permission.DEBUG_COMMAND_DEBUG)
   @Command("train debug display")
   private void commandDebugDisplayEntity(Player player, TrainCarts plugin) {
      Location location = player.getEyeLocation();
      final Matrix4x4 transform = Matrix4x4.fromLocation(location);
      final VirtualDisplayItemEntity entity = new VirtualDisplayItemEntity((AttachmentManager)null);
      entity.setItem(ItemDisplayMode.HEAD, CommonItemStack.create(MaterialUtil.getFirst(new String[]{"JACK_O_LANTERN", "LEGACY_JACK_O_LANTERN"}), 1).toBukkit());
      AttachmentViewer viewer = plugin.getAttachmentViewer(player);
      entity.updatePosition(transform);
      entity.syncPosition(true);
      entity.spawn(viewer, new Vector());
      (new Task(plugin) {
         double movement = 0.0D;
         double fx = 0.1D;

         public void run() {
            boolean changed = false;
            double x = DebugUtil.getDoubleValue("x", 0.0D);
            double y = DebugUtil.getDoubleValue("y", 0.0D);
            double z = DebugUtil.getDoubleValue("z", 0.0D);
            if (x != 0.0D) {
               transform.rotateX(x);
               changed = true;
            }

            if (y != 0.0D) {
               transform.rotateY(y);
               changed = true;
            }

            if (z != 0.0D) {
               transform.rotateZ(z);
               changed = true;
            }

            if (changed) {
               Quaternion var8 = transform.getRotation();
            }

            transform.worldTranslate(this.fx, 0.0D, 0.0D);
            this.movement += this.fx;
            if (Math.abs(this.movement) > 5.0D) {
               this.fx = -this.fx;
            }

            entity.updatePosition(transform);
            entity.syncPosition(true);
         }
      }).start(1L, 1L);
   }
}
