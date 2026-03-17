package com.bergerkiller.bukkit.tc.commands;

import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.Task;
import com.bergerkiller.bukkit.common.bases.IntVector3;
import com.bergerkiller.bukkit.common.cloud.parsers.SoundEffectParser;
import com.bergerkiller.bukkit.common.dep.cloud.CommandManager;
import com.bergerkiller.bukkit.common.dep.cloud.annotation.specifier.Greedy;
import com.bergerkiller.bukkit.common.dep.cloud.annotation.specifier.Quoted;
import com.bergerkiller.bukkit.common.dep.cloud.annotation.specifier.Range;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Argument;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Command;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.CommandDescription;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Flag;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Permission;
import com.bergerkiller.bukkit.common.dep.cloud.description.Description;
import com.bergerkiller.bukkit.common.entity.CommonEntity;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.internal.CommonPlugin;
import com.bergerkiller.bukkit.common.inventory.CommonItemStack;
import com.bergerkiller.bukkit.common.map.MapDisplay;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.common.resources.ResourceKey;
import com.bergerkiller.bukkit.common.resources.SoundEffect;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.bergerkiller.bukkit.common.wrappers.ChatText;
import com.bergerkiller.bukkit.tc.Localization;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.attachments.ui.AttachmentEditor;
import com.bergerkiller.bukkit.tc.attachments.ui.SetValueTarget;
import com.bergerkiller.bukkit.tc.commands.annotations.CommandRequiresPermission;
import com.bergerkiller.bukkit.tc.commands.selector.SelectorCondition;
import com.bergerkiller.bukkit.tc.commands.selector.SelectorException;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartGroupStore;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.editor.TCMapControl;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.pathfinding.PathNode;
import com.bergerkiller.bukkit.tc.pathfinding.PathWorld;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.statements.Statement;
import com.bergerkiller.bukkit.tc.tickets.TicketStore;
import com.bergerkiller.bukkit.tc.utils.QuoteEscapedString;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class GlobalCommands {
   @Command("train version")
   @CommandDescription("Shows installed version of TrainCarts and BKCommonLib")
   private void commandShowVersion(CommandSender sender, TrainCarts plugin) {
      plugin.onVersionCommand("version", sender);
   }

   @Command("train startuplog")
   @CommandDescription("Views everything logged during startup of TrainCarts")
   @Permission({"bkcommonlib.command.startuplog"})
   private void commandShowStartupLog(CommandSender sender, TrainCarts plugin) {
      plugin.onStartupLogCommand(sender, "startuplog", new String[0]);
   }

   @Command("train list destinations")
   @CommandDescription("Lists all the destination names that exist on the server")
   private void commandListDestinations(CommandSender sender, TrainCarts plugin) {
      MessageBuilder builder = new MessageBuilder();
      builder.yellow(new Object[]{"The following train destinations are available:"});
      builder.newLine().setSeparator(ChatColor.WHITE, " / ");
      Object worlds;
      if (sender instanceof Player) {
         World playerWorld = ((Player)sender).getWorld();
         worlds = Collections.singleton(plugin.getPathProvider().getWorld(playerWorld));
      } else {
         worlds = plugin.getPathProvider().getWorlds();
      }

      Iterator var9 = ((Collection)worlds).iterator();

      while(var9.hasNext()) {
         PathWorld world = (PathWorld)var9.next();
         Iterator var7 = world.getNodes().iterator();

         while(var7.hasNext()) {
            PathNode node = (PathNode)var7.next();
            if (!node.containsOnlySwitcher()) {
               builder.green(new Object[]{node.getName()});
            }
         }
      }

      builder.send(sender);
   }

   @Command("train list [filter]")
   @CommandDescription("Lists all the trains on the server that match the specified statement")
   private void commandListTrains(TrainCarts plugin, CommandSender sender, @Argument(value = "filter",suggestions = "trainlistfilter") @Greedy String filter) {
      if (filter == null || filter.isEmpty()) {
         int count = 0;
         int moving = 0;

         MinecartGroup group;
         for(Iterator var6 = MinecartGroupStore.getGroups().iterator(); var6.hasNext(); group.getProperties()) {
            group = (MinecartGroup)var6.next();
            ++count;
            if (group.isMoving()) {
               ++moving;
            }
         }

         count += plugin.getOfflineGroups().getStoredCountInLoadedWorlds();
         int minecartCount = 0;
         Iterator var12 = WorldUtil.getWorlds().iterator();

         while(var12.hasNext()) {
            World world = (World)var12.next();
            Iterator var9 = WorldUtil.getEntities(world).iterator();

            while(var9.hasNext()) {
               Entity e = (Entity)var9.next();
               if (e instanceof Minecart) {
                  ++minecartCount;
               }
            }
         }

         MessageBuilder builder = new MessageBuilder();
         builder.green(new Object[]{"There are "}).yellow(new Object[]{count}).green(new Object[]{" trains on this server (of which "});
         builder.yellow(new Object[]{moving}).green(new Object[]{" are moving)"});
         builder.newLine().green(new Object[]{"There are "}).yellow(new Object[]{minecartCount}).green(new Object[]{" minecart entities"});
         builder.send(sender);
      }

      listTrains(plugin, sender, filter == null ? "" : filter);
   }

   @CommandRequiresPermission(com.bergerkiller.bukkit.tc.Permission.COMMAND_MESSAGE)
   @Command("train message <key>")
   @CommandDescription("Checks what value is assigned to a given message key")
   private void commandGetMessage(CommandSender sender, @Argument("key") String key) {
      String value = TCConfig.messageShortcuts.get(key);
      if (value == null) {
         sender.sendMessage(ChatColor.RED + "No shortcut is set for key '" + key + "'");
      } else {
         sender.sendMessage(ChatColor.GREEN + "Shortcut value of '" + key + "' = " + ChatColor.WHITE + value);
      }

   }

   @CommandRequiresPermission(com.bergerkiller.bukkit.tc.Permission.COMMAND_MESSAGE)
   @Command("train message <key> <value>")
   @CommandDescription("Checks what value is assigned to a given message key")
   private void commandSetMessage(CommandSender sender, TrainCarts plugin, @Argument("key") String key, @Argument("value") @Greedy String value) {
      String conv_value = StringUtil.ampToColor(value);
      TCConfig.messageShortcuts.remove(key);
      TCConfig.messageShortcuts.add(key, conv_value);
      plugin.saveShortcuts();
      sender.sendMessage(ChatColor.GREEN + "Shortcut '" + key + "' set to: " + ChatColor.WHITE + conv_value);
   }

   @CommandRequiresPermission(com.bergerkiller.bukkit.tc.Permission.COMMAND_DESTROYALL)
   @Command("train removeall")
   @CommandDescription("Destroys all trains on the server or world")
   private void commandRemoveAll(CommandSender sender, TrainCarts plugin, @Flag("world") World world, @Flag(value = "vanilla",description = "Whether to destroy non-Traincarts vanilla Minecarts too") boolean destroyVanilla) {
      this.commandDestroyAll(sender, plugin, world, destroyVanilla);
   }

   @CommandRequiresPermission(com.bergerkiller.bukkit.tc.Permission.COMMAND_DESTROYALL)
   @Command("train destroyall")
   @CommandDescription("Destroys all trains on the server or world")
   private void commandDestroyAll(CommandSender sender, TrainCarts plugin, @Flag("world") World world, @Flag(value = "vanilla",description = "Whether to destroy non-Traincarts vanilla Minecarts too") boolean destroyVanilla) {
      CompletableFuture<Integer> future = world == null ? plugin.getOfflineGroups().destroyAllAsync(destroyVanilla) : plugin.getOfflineGroups().destroyAllAsync(world, destroyVanilla);
      future.thenAccept((count) -> {
         sender.sendMessage(ChatColor.RED.toString() + count + " (visible) trains have been destroyed!");
      });
   }

   public void init(CommandManager<CommandSender> manager) {
      this.initTrainMenuSetSoundCommand(manager);
   }

   private void initTrainMenuSetSoundCommand(CommandManager<CommandSender> manager) {
      if (Common.hasCapability("Common:Sound:CloudParser")) {
         manager.command(manager.commandBuilder("train", new String[0]).literal("menu", new String[0]).literal("sound", Description.of("Sets a sound effect in the TrainCarts editor map"), new String[0]).required("path", SoundEffectParser.soundEffectParser()).permission(com.bergerkiller.bukkit.tc.Permission.COMMAND_GIVE_EDITOR.cloudPermission()).senderType(Player.class).handler((context) -> {
            Player sender = (Player)context.sender();
            ResourceKey<SoundEffect> effect = (ResourceKey)context.get("path");
            this.commandMenuSet(sender, SetValueTarget.Operation.SET, effect.getPath());
         }));
      }
   }

   @CommandRequiresPermission(com.bergerkiller.bukkit.tc.Permission.COMMAND_GIVE_EDITOR)
   @Command("train menu <operation> <value>")
   @CommandDescription("Updates a menu item in a TrainCarts editor map using commands")
   private void commandMenuSet(Player sender, @Argument("operation") SetValueTarget.Operation operation, @Argument("value") @Greedy String value) {
      MapDisplay display = MapDisplay.getHeldDisplay(sender, AttachmentEditor.class);
      if (display == null) {
         display = MapDisplay.getHeldDisplay(sender);
         if (display == null) {
            sender.sendMessage(ChatColor.RED + "You do not have an editor menu open");
            return;
         }
      }

      MapWidget focused = display.getFocusedWidget();
      if (!(focused instanceof SetValueTarget)) {
         focused = display.getActivatedWidget();
      }

      if (!(focused instanceof SetValueTarget)) {
         sender.sendMessage(ChatColor.RED + "No suitable menu item is active!");
      } else {
         SetValueTarget target = (SetValueTarget)focused;
         boolean success = target.acceptTextValue(operation, value);
         String propname = target.getAcceptedPropertyName();
         if (success) {
            sender.sendMessage(ChatColor.GREEN + propname + " has been updated");
         } else {
            sender.sendMessage(ChatColor.RED + "Failed to update " + propname + "!");
         }

      }
   }

   @CommandRequiresPermission(com.bergerkiller.bukkit.tc.Permission.COMMAND_REROUTE)
   @Command("train reroute")
   @CommandDescription("Recalculates all path finding information on the server")
   private void commandReroute(CommandSender sender, TrainCarts plugin, @Flag(value = "lazy",description = "Delays recalculating routes until a train needs it") boolean lazy, @Flag(value = "stop",description = "Stops all ongoing path route discovery operations") boolean stop, @Flag(value = "status",description = "Displays what the routing manager is currently doing") boolean status, @Flag(value = "from",repeatable = true,description = "Destination name(s) to reroute from",suggestions = "destinations") List<String> fromDestinations) {
      if (status) {
         if (!plugin.getPathProvider().isProcessing()) {
            sender.sendMessage(ChatColor.GREEN + "No train routings are being calculated right now");
         } else {
            int numNodes = plugin.getPathProvider().getNumPendingNodes();
            int numTasks = plugin.getPathProvider().getNumPendingOperations();
            sender.sendMessage(ChatColor.YELLOW + "Train routings are being calculated right now:");
            sender.sendMessage(ChatColor.YELLOW + "Number of switchers/destinations remaining: " + ChatColor.RED + numNodes);
            sender.sendMessage(ChatColor.YELLOW + "Number of paths remaining: " + ChatColor.RED + numTasks);
         }
      } else if (stop) {
         plugin.getPathProvider().stopRouting();
         sender.sendMessage(ChatColor.YELLOW + "Cancelled all ongoing train route discovery operations");
      } else if (lazy) {
         PathNode.clearAll();
         sender.sendMessage(ChatColor.YELLOW + "All train routings will be recalculated when needed");
      } else if (fromDestinations != null && !fromDestinations.isEmpty()) {
         boolean hasDestinationsThatExist = false;
         Iterator var11 = fromDestinations.iterator();

         while(var11.hasNext()) {
            String destination = (String)var11.next();
            if (plugin.getPathProvider().nodeExistsOnAnyWorld(destination)) {
               hasDestinationsThatExist = true;
            } else {
               sender.sendMessage(ChatColor.RED + "Destination with name '" + ChatColor.YELLOW + destination + ChatColor.RED + "' does not exist!");
            }
         }

         if (!hasDestinationsThatExist) {
            sender.sendMessage(ChatColor.RED + "No valid destination names were specified to reroute from!");
         } else {
            plugin.getPathProvider().notifyOfCompletion(sender);
            plugin.getPathProvider().rerouteFrom(fromDestinations);
            sender.sendMessage(ChatColor.YELLOW + "All train routings will be recalculated from the destination(s) specified");
            sender.sendMessage(ChatColor.YELLOW + "If some destinations no longer exist, they will be removed");
         }
      } else {
         plugin.getPathProvider().notifyOfCompletion(sender);
         plugin.getPathProvider().reroute();
         sender.sendMessage(ChatColor.YELLOW + "All train routings will be recalculated");
      }

   }

   @CommandRequiresPermission(com.bergerkiller.bukkit.tc.Permission.COMMAND_RELOAD)
   @Command("train globalconfig reload")
   @CommandDescription("Reloads one or more global TrainCarts configuration files from disk")
   private void commandReloadConfig(CommandSender sender, TrainCarts traincarts, @Flag(value = "config",description = "Reload config.yml") boolean config, @Flag(value = "routes",description = "Reload routes.yml") boolean routes, @Flag(value = "defaulttrainproperties",description = "Reload DefaultTrainProperties.yml") boolean defaultTrainproperties, @Flag(value = "savedtrainproperties",description = "Reload SavedTrainProperties.yml and modules") boolean savedTrainproperties, @Flag(value = "modelstore",description = "Reload SavedModels.yml and modules") boolean modelStore, @Flag(value = "tickets",description = "Reload tickets.yml") boolean tickets) {
      if (!config && !routes && !defaultTrainproperties && !savedTrainproperties && !modelStore && !tickets) {
         sender.sendMessage(ChatColor.RED + "Please specify one or more configuration files to reload:");
         sender.sendMessage(ChatColor.RED + "/train globalconfig reload --config");
         sender.sendMessage(ChatColor.RED + "/train globalconfig reload --routes");
         sender.sendMessage(ChatColor.RED + "/train globalconfig reload --defaulttrainproperties");
         sender.sendMessage(ChatColor.RED + "/train globalconfig reload --savedtrainproperties");
         sender.sendMessage(ChatColor.RED + "/train globalconfig reload --modelstore");
         sender.sendMessage(ChatColor.RED + "/train globalconfig reload --tickets");
      } else {
         if (config) {
            traincarts.loadConfig();
         }

         if (routes) {
            traincarts.getRouteManager().load();
         }

         if (defaultTrainproperties) {
            TrainProperties.loadDefaults(traincarts);
         }

         if (savedTrainproperties) {
            traincarts.getSavedTrains().reload();
         }

         if (modelStore) {
            traincarts.getSavedAttachmentModels().reload();
         }

         if (tickets) {
            TicketStore.load(traincarts);
         }

         sender.sendMessage(ChatColor.YELLOW + "Configuration has been reloaded!");
      }
   }

   @CommandRequiresPermission(com.bergerkiller.bukkit.tc.Permission.COMMAND_SAVEALL)
   @Command("train globalconfig save")
   @CommandDescription("Forces a save of all configuration to disk")
   private void commandReloadConfig(CommandSender sender, TrainCarts plugin) {
      plugin.save(TrainCarts.SaveMode.COMMAND);
      sender.sendMessage(ChatColor.YELLOW + "TrainCarts' information has been saved to file.");
   }

   @CommandRequiresPermission(com.bergerkiller.bukkit.tc.Permission.COMMAND_EDIT)
   @Command("train edit")
   @CommandDescription("Selects a train the player is looking at for editing")
   private void commandEditLookingAt(TrainCarts plugin, final Player player) {
      World playerWorld = player.getWorld();
      Matrix4x4 cameraTransform = new Matrix4x4();
      cameraTransform.translateRotate(Util.getRealEyeLocation(player));
      cameraTransform.invert();
      MinecartMember<?> bestMember = null;
      Vector bestPos = null;
      double bestDistance = Double.MAX_VALUE;
      Iterator var9 = MinecartGroup.getGroups().cloneAsIterable().iterator();

      label68:
      while(true) {
         MinecartGroup group;
         do {
            if (!var9.hasNext()) {
               if (bestMember != null && !bestMember.getProperties().hasOwnership(player)) {
                  Localization.EDIT_NOTOWNED.message(player, new String[0]);
               } else if (bestMember != null) {
                  final Entity memberEntity = ((CommonMinecart)bestMember.getEntity()).getEntity();
                  (new Task(plugin) {
                     final int batch_ctr = 5;
                     double dy = 0.0D;

                     public void run() {
                        for(int i = 0; i < 5; ++i) {
                           if (this.dy > 50.0D || !player.isValid() || memberEntity.isDead()) {
                              this.stop();
                              return;
                           }

                           Location loc = memberEntity.getLocation();
                           loc.add(0.0D, this.dy, 0.0D);
                           player.playEffect(loc, Effect.SMOKE, 4);
                           ++this.dy;
                        }

                     }
                  }).start(1L, 1L);
                  plugin.getPlayer(player).editMember(bestMember);
                  Localization.EDIT_SUCCESS.message(player, new String[]{bestMember.getGroup().getProperties().getTrainName()});
               } else {
                  player.sendMessage(ChatColor.RED + "You are not looking at any Minecart right now");
                  player.sendMessage(ChatColor.RED + "Please enter the exact name of the train to edit");
                  this.commandListTrains(plugin, player, (String)null);
               }

               return;
            }

            group = (MinecartGroup)var9.next();
         } while(group.getWorld() != playerWorld);

         Iterator var11 = group.iterator();

         while(true) {
            MinecartMember member;
            Vector pos;
            double distance;
            do {
               double lim;
               do {
                  do {
                     do {
                        do {
                           if (!var11.hasNext()) {
                              continue label68;
                           }

                           member = (MinecartMember)var11.next();
                           pos = ((CommonMinecart)member.getEntity()).loc.vector();
                           cameraTransform.transformPoint(pos);
                        } while(pos.getZ() < 0.0D);
                     } while(pos.getZ() > TCConfig.maxTrainEditdistance);

                     lim = Math.max(1.0D, 0.707106781D * pos.getZ());
                  } while(Math.abs(pos.getX()) > lim);
               } while(Math.abs(pos.getY()) > lim);

               distance = Math.sqrt(pos.getX() * pos.getX() + pos.getY() * pos.getY()) / lim;
            } while(bestPos != null && !(distance < bestDistance));

            bestPos = pos;
            bestDistance = distance;
            bestMember = member;
         }
      }
   }

   @Command("train edit <trainname>")
   @CommandDescription("Forcibly removes minecarts and trackers that have glitched out")
   private void commandEditByName(TrainCarts plugin, Player sender, @Quoted @Argument(value = "trainname",suggestions = "quoted_trainnames") String trainName) {
      TrainProperties prop = TrainProperties.get(trainName);
      if (prop == null) {
         prop = TrainProperties.getRelaxed(trainName);
      }

      if (prop != null && !prop.isEmpty()) {
         if (prop.hasOwnership(sender)) {
            plugin.getPlayer(sender).editCart(prop.get(0));
            Localization.EDIT_SUCCESS.message(sender, new String[]{prop.getTrainName()});
         } else {
            Localization.EDIT_NOTOWNED.message(sender, new String[0]);
         }
      } else {
         Localization.EDIT_NOTFOUND.message(sender, new String[]{trainName});
         this.commandListTrains(plugin, sender, (String)null);
      }

   }

   @CommandRequiresPermission(com.bergerkiller.bukkit.tc.Permission.COMMAND_CHANGETICK)
   @Command("train tick disable")
   @CommandDescription("Disables ticking of all trains, causing all physics to pause")
   private void commandTickDisable(CommandSender sender, TrainCarts plugin) {
      this.commandSetTickEnabled(sender, plugin, false);
   }

   @CommandRequiresPermission(com.bergerkiller.bukkit.tc.Permission.COMMAND_CHANGETICK)
   @Command("train tick enable")
   @CommandDescription("Enables ticking of all trains, causing all physics to resume")
   private void commandTickEnable(CommandSender sender, TrainCarts plugin) {
      this.commandSetTickEnabled(sender, plugin, true);
   }

   @CommandRequiresPermission(com.bergerkiller.bukkit.tc.Permission.COMMAND_CHANGETICK)
   @Command("train tick toggle")
   @CommandDescription("Toggles ticking of all trains, causing all physics to pause or resume")
   private void commandTickToggle(CommandSender sender, TrainCarts plugin) {
      this.commandSetTickEnabled(sender, plugin, plugin.getTrainUpdateController().getTickDivider() == Integer.MAX_VALUE);
   }

   private void commandSetTickEnabled(CommandSender sender, TrainCarts plugin, boolean enabled) {
      plugin.getTrainUpdateController().setTickDivider(enabled ? 1 : Integer.MAX_VALUE);
      sender.sendMessage(ChatColor.YELLOW + "Train tick updates have been globally " + (enabled ? ChatColor.GREEN + "enabled" : ChatColor.RED + "disabled"));
   }

   @CommandRequiresPermission(com.bergerkiller.bukkit.tc.Permission.COMMAND_CHANGETICK)
   @Command("train tick div")
   @CommandDescription("Checks what kind of tick divider configuration is configured")
   private void commandGetTickDivider(CommandSender sender, TrainCarts plugin) {
      int divider = plugin.getTrainUpdateController().getTickDivider();
      if (divider == Integer.MAX_VALUE) {
         sender.sendMessage(ChatColor.YELLOW + "Automatic train tick updates are globally disabled");
      } else {
         sender.sendMessage(ChatColor.GREEN + "The tick rate divider is currently set to " + ChatColor.YELLOW + divider);
      }

   }

   @CommandRequiresPermission(com.bergerkiller.bukkit.tc.Permission.COMMAND_CHANGETICK)
   @Command("train tick div reset")
   @CommandDescription("Resets any previous global tick divider, resuming physics as normal")
   private void commandResetTickDivider(CommandSender sender, TrainCarts plugin) {
      this.commandSetTickDivider(sender, plugin, 1);
   }

   @CommandRequiresPermission(com.bergerkiller.bukkit.tc.Permission.COMMAND_CHANGETICK)
   @Command("train tick div <divider>")
   @CommandDescription("Configures a global tick divider, causing all physics to run more slowly")
   private void commandSetTickDivider(CommandSender sender, TrainCarts plugin, @Argument("divider") int divider) {
      if (divider > 1) {
         plugin.getTrainUpdateController().setTickDivider(divider);
         sender.sendMessage(ChatColor.GREEN + "The tick rate divider has been set to " + ChatColor.YELLOW + divider);
      } else {
         plugin.getTrainUpdateController().setTickDivider(1);
         sender.sendMessage(ChatColor.GREEN + "The tick rate divider has been reset to the default");
      }

   }

   @CommandRequiresPermission(com.bergerkiller.bukkit.tc.Permission.COMMAND_CHANGETICK)
   @Command("train tick")
   @CommandDescription("Performs a single update tick. Useful when automatic ticking is disabled or slowed down.")
   private void commandPerformTick(CommandSender sender, TrainCarts plugin) {
      this.commandPerformTick(sender, plugin, 1);
   }

   @CommandRequiresPermission(com.bergerkiller.bukkit.tc.Permission.COMMAND_CHANGETICK)
   @Command("train tick <times>")
   @CommandDescription("Performs a burst of update ticks. Useful when automatic ticking is disabled or slowed down.")
   private void commandPerformTick(CommandSender sender, TrainCarts plugin, @Argument("times") @Range(min = "1") int number) {
      plugin.getTrainUpdateController().step(number);
      if (number <= 1) {
         sender.sendMessage(ChatColor.GREEN + "Trains ticked once");
      } else {
         sender.sendMessage(ChatColor.GREEN + "Trains ticked " + number + " times");
      }

   }

   @CommandRequiresPermission(com.bergerkiller.bukkit.tc.Permission.COMMAND_ISSUE)
   @Command("train issue")
   @CommandDescription("Shows helpful information for posting an issue ticket on our Github")
   private void commandIssueTicket(CommandSender sender, TrainCarts plugin) {
      String bugReport;
      if (sender instanceof Player) {
         Player player = (Player)sender;
         ChatText chatText = ChatText.fromMessage(ChatColor.YELLOW.toString() + "Click one of the below options to open an issue on GitHub:");
         chatText.sendTo(player);

         try {
            bugReport = "## Info\nPlease provide the following information:\n\n- BKCommonLib Version: " + CommonPlugin.getInstance().getDebugVersion() + "\n- TrainCarts Version: " + plugin.getDebugVersion() + "\n- Server Type and Version: " + Bukkit.getVersion() + "\n\n----\n## Bug\n\n### Description\n\n### Expected Behaviour\n\n### Actual Behaviour\n\n### Steps to reproduce\n\n### Additional Information\n*This issue was created using the `/train issue` command!*";
            String featureRequest = "## Feature Request\n\n### Description\n\n### Examples";
            chatText = ChatText.empty().appendClickableURL(ChatColor.RED.toString() + ChatColor.UNDERLINE.toString() + "Bug Report", "https://github.com/bergerhealer/TrainCarts/issues/new?body=" + URLEncoder.encode(bugReport, "UTF-8"), "Click to open a Bug Report");
            chatText.sendTo(player);
            chatText = ChatText.empty().appendClickableURL(ChatColor.GREEN.toString() + ChatColor.UNDERLINE.toString() + "Feature Request", "https://github.com/bergerhealer/TrainCarts/issues/new?body=" + URLEncoder.encode(featureRequest, "UTF-8"), "Click to open a Feature Request");
            chatText.sendTo(player);
         } catch (UnsupportedEncodingException var8) {
            chatText = ChatText.empty().appendClickableURL(ChatColor.RED.toString() + ChatColor.UNDERLINE.toString() + "Bug Report", "https://github.com/bergerhealer/TrainCarts/issues/new?template=bug_report.md", "Click to open a Bug Report");
            chatText.sendTo(player);
            chatText = ChatText.empty().appendClickableURL(ChatColor.GREEN.toString() + ChatColor.UNDERLINE.toString() + "Feature Request", "https://github.com/bergerhealer/TrainCarts/issues/new?template=feature_request.md", "Click to open a Feature Request");
            chatText.sendTo(player);
         }
      } else {
         MessageBuilder builder = new MessageBuilder();
         builder.white(new Object[]{"Click one of the below URLs to open an issue on GitHub:"});

         try {
            String bugReport = "## Info\nPlease provide the following information:\n\n- BKCommonLib Version: " + CommonPlugin.getInstance().getDebugVersion() + "\n- TrainCarts Version: " + plugin.getDebugVersion() + "\n- Server Type and Version: " + Bukkit.getVersion() + "\n\n----\n## Bug\n\n### Description\n\n### Expected Behaviour\n\n### Actual Behaviour\n\n### Steps to reproduce\n\n### Additional Information\n*This issue was created using the `/train issue` command!*";
            bugReport = "## Feature Request\n\n### Description\n\n### Examples";
            builder.white(new Object[]{"Bug Report: https://github.com/bergerhealer/TrainCarts/issues/new?body=" + URLEncoder.encode(bugReport, "UTF-8")}).append(new String[]{"Feature Request: https://github.com/bergerhealer/TrainCarts/issues/new?body=" + URLEncoder.encode(bugReport, "UTF-8")});
         } catch (UnsupportedEncodingException var7) {
            builder.white(new Object[]{"Bug Report: https://github.com/bergerhealer/TrainCarts/issues/new?template=bug_report.md"}).append(new String[]{"Feature Request: https://github.com/bergerhealer/TrainCarts/issues/new?template=feature_request.md"});
         }

         builder.send(sender);
      }

   }

   @CommandRequiresPermission(com.bergerkiller.bukkit.tc.Permission.COMMAND_GIVE_EDITOR)
   @Command("train debug editor")
   @CommandDescription("Gives a legacy editor map item (broken)")
   private void commandGiveEditor(Player sender) {
      sender.getInventory().addItem(new ItemStack[]{TCMapControl.createTCMapItem()});
      sender.sendMessage("Given editor map item (note: broken)");
   }

   @CommandRequiresPermission(com.bergerkiller.bukkit.tc.Permission.COMMAND_GIVE_EDITOR)
   @Command("train attachments")
   @CommandDescription("Gives an attachment editor map item to the player")
   private void commandGiveAttachmentEditor(Player sender) {
      CommonItemStack item = CommonItemStack.of(MapDisplay.createMapItem(AttachmentEditor.class)).setCustomNameMessage("Traincarts Attachments Editor").setFilledMapColor(16711680);
      sender.getInventory().addItem(new ItemStack[]{item.toBukkit()});
      sender.sendMessage(ChatColor.GREEN + "Given a Traincarts attachments editor");
   }

   public static void listTrains(TrainCarts plugin, CommandSender sender, String filter) {
      MessageBuilder builder = new MessageBuilder();
      builder.setSeparator(" / ");
      if (!filter.startsWith("@train[")) {
         if (sender instanceof Player) {
            sender.sendMessage(ChatColor.YELLOW + "You are the proud owner of the following trains:");
         } else {
            sender.sendMessage(ChatColor.YELLOW + "The following trains exist on this server:");
         }

         boolean found = false;
         Iterator var14 = TrainProperties.getAll().iterator();

         label88:
         while(true) {
            TrainProperties prop;
            MinecartGroup group;
            do {
               do {
                  do {
                     if (!var14.hasNext()) {
                        if (!found) {
                           Localization.EDIT_NONEFOUND.message(sender, new String[0]);
                           return;
                        }
                        break label88;
                     }

                     prop = (TrainProperties)var14.next();
                  } while(sender instanceof Player && !prop.hasOwnership((Player)sender));
               } while(!prop.hasHolder() && !prop.getTrainCarts().getOfflineGroups().containsInLoadedWorld(prop.getTrainName()));

               if (!prop.hasHolder() || filter.isEmpty()) {
                  break;
               }

               group = prop.getHolder();
            } while(!Statement.has((MinecartGroup)group, filter, (SignActionEvent)null));

            found = true;
            builder.append(new String[]{prop.getTrainName()});
         }
      } else {
         while(true) {
            if (!filter.endsWith(" ")) {
               if (!filter.endsWith("]")) {
                  Localization.COMMAND_INPUT_SELECTOR_INVALID.message(sender, new String[]{filter.substring(7)});
                  return;
               }

               String conditionsString = filter.substring(7, filter.length() - 1);
               List<SelectorCondition> conditions = SelectorCondition.parseAll(conditionsString);
               if (conditions == null) {
                  Localization.COMMAND_INPUT_SELECTOR_INVALID.message(sender, new String[]{conditionsString});
                  return;
               }

               try {
                  Collection var10000 = plugin.getSelectorHandlerRegistry().find("train").handle(sender, "train", conditions);
                  Objects.requireNonNull(builder);
                  var10000.forEach((xva$0) -> {
                     builder.append(new String[]{xva$0});
                  });
               } catch (SelectorException var11) {
                  sender.sendMessage(ChatColor.RED + "[TrainCarts] " + var11.getMessage());
                  return;
               }

               ChatText.fromMessage(ChatColor.YELLOW + "The ").append(ChatText.fromClickableContent(ChatColor.BLUE.toString() + ChatColor.UNDERLINE + "selector", filter).setHoverText("Click to copy selector to Clipboard")).append(ChatColor.YELLOW + " matches the following trains:").sendTo(sender);
               break;
            }

            filter = filter.substring(0, filter.length() - 1);
         }
      }

      String[] var13 = builder.lines();
      int var15 = var13.length;

      for(int var16 = 0; var16 < var15; ++var16) {
         String line = var13[var16];
         String[] trainNames = line.split(Pattern.quote(" / "));
         ChatText combined = ChatText.empty();

         for(int i = 0; i < trainNames.length; ++i) {
            if (i > 0) {
               combined.append(ChatColor.WHITE + " / ");
            }

            combined.append(listFormatTrainName(trainNames[i]));
         }

         combined.sendTo(sender);
      }

   }

   private static ChatText listFormatTrainName(String name) {
      TrainProperties properties = TrainProperties.get(name);
      if (properties == null) {
         return ChatText.fromMessage(ChatColor.RED + name);
      } else {
         ChatText text;
         if (properties.isLoaded() && !properties.isEmpty()) {
            CommonEntity<?> head = properties.getHolder().head().getEntity();
            String worldName = head.getWorld().getName();
            IntVector3 block = head.loc.block();
            text = ChatText.fromMessage(ChatColor.GREEN.toString() + ChatColor.UNDERLINE + name);
            text.setHoverText(ChatColor.GREEN + "Loaded in world " + ChatColor.YELLOW + worldName + ChatColor.GREEN + " at " + ChatColor.WHITE + block.x + "/" + block.y + "/" + block.z);
         } else {
            text = ChatText.fromMessage(ChatColor.RED.toString() + ChatColor.UNDERLINE + name);
            text.setHoverText(ChatColor.RED + "Not loaded");
         }

         String safeEditName = StringUtil.stripChatStyle(name);
         text.setClickableRunCommand("/train edit " + QuoteEscapedString.quoteEscape(safeEditName).getEscaped());
         return text;
      }
   }
}
