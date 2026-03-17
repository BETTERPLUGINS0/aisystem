package com.bergerkiller.bukkit.tc.properties.standard.category;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.dep.cloud.annotation.specifier.FlagYielding;
import com.bergerkiller.bukkit.common.dep.cloud.annotation.specifier.Greedy;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Argument;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Command;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.CommandDescription;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.suggestion.Suggestions;
import com.bergerkiller.bukkit.common.dep.cloud.context.CommandContext;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.commands.annotations.CommandRequiresPermission;
import com.bergerkiller.bukkit.tc.commands.annotations.CommandTargetTrain;
import com.bergerkiller.bukkit.tc.pathfinding.PathConnection;
import com.bergerkiller.bukkit.tc.pathfinding.PathNode;
import com.bergerkiller.bukkit.tc.pathfinding.PathWorld;
import com.bergerkiller.bukkit.tc.properties.CartProperties;
import com.bergerkiller.bukkit.tc.properties.IProperties;
import com.bergerkiller.bukkit.tc.properties.IPropertiesHolder;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.api.ICartProperty;
import com.bergerkiller.bukkit.tc.properties.api.PropertyCheckPermission;
import com.bergerkiller.bukkit.tc.properties.api.PropertyParser;
import com.bergerkiller.bukkit.tc.properties.api.context.PropertyParseContext;
import com.bergerkiller.bukkit.tc.properties.standard.StandardProperties;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public final class DestinationRouteProperty implements ICartProperty<List<String>> {
   private static final String ROUTE_SEP = " → ";

   public void showDestinationPathInfo(CommandSender sender, IProperties prop) {
      MessageBuilder msg = new MessageBuilder();
      msg.yellow(new Object[]{"This "}).append(new String[]{prop.getTypeName()});
      String lastName = prop.getDestination();
      if (LogicUtil.nullOrEmpty(lastName)) {
         msg.append(new String[]{" is not trying to reach a destination."});
      } else {
         IPropertiesHolder holder;
         if ((holder = prop.getHolder()) == null) {
            msg.append(new String[]{" is not currently loaded."});
         } else {
            msg.append(new String[]{" is trying to reach "}).green(new Object[]{lastName}).newLine();
            PathWorld pathWorld = prop.getTrainCarts().getPathProvider().getWorld(holder.getWorld());
            PathNode first = pathWorld.getNodeByName(prop.getLastPathNode());
            if (first == null) {
               msg.yellow(new Object[]{"It has not yet visited a destination or switcher, so no route is available yet."});
            } else {
               PathNode last = pathWorld.getNodeByName(lastName);
               if (last == null) {
                  msg.red(new Object[]{"The destination position to reach can not be found!"});
               } else {
                  PathConnection[] route = first.findRoute(last);
                  msg.yellow(new Object[]{"Route: "});
                  if (route.length == 0) {
                     msg.red(new Object[]{first.getDisplayName() + " /=/ " + last.getDisplayName() + " (not found)"});
                  } else {
                     msg.setSeparator(ChatColor.YELLOW, " -> ");
                     PathConnection[] var10 = route;
                     int var11 = route.length;

                     for(int var12 = 0; var12 < var11; ++var12) {
                        PathConnection connection = var10[var12];
                        msg.green(new Object[]{connection.destination.getDisplayName()});
                     }
                  }
               }
            }
         }
      }

      msg.send(sender);
   }

   public void showRoute(String cmd_prefix, CommandSender sender, IProperties properties) {
      List<String> route = properties.getDestinationRoute();
      if (route.isEmpty()) {
         sender.sendMessage(ChatColor.RED + "No route is currently set!");
         sender.sendMessage(ChatColor.RED + "For help, use " + cmd_prefix + " help");
      } else {
         MessageBuilder builder = new MessageBuilder();
         builder.yellow(new Object[]{"The following route is currently set:"});
         builder.newLine().setSeparator(ChatColor.WHITE, " → ");
         int currentRouteIndex = properties.getCurrentRouteDestinationIndex();

         for(int i = 0; i < route.size(); ++i) {
            if (i == currentRouteIndex) {
               builder.green(new Object[]{route.get(i)});
            } else {
               builder.yellow(new Object[]{route.get(i)});
            }
         }

         builder.send(sender);
      }

      if (Permission.COMMAND_PATHINFO.has(sender)) {
         this.showDestinationPathInfo(sender, properties);
      }

   }

   @Command("cart route")
   @CommandDescription("Displays the current route set for a cart")
   private void getProperty(CommandSender sender, CartProperties properties) {
      this.showRoute("/cart route", sender, properties);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("route")
   @Command("cart route add <destinations>")
   @CommandDescription("Adds one or more destinations to the route set for a cart")
   private void setPropertyAdd(CommandSender sender, CartProperties properties, @FlagYielding @Argument(value = "destinations",suggestions = "destinations") String[] destinations) {
      this.setPropertyAddGeneric(sender, properties, destinations);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("route")
   @Command("cart route set <destinations>")
   @CommandDescription("Resets the route to one or more destinations for a cart")
   private void setPropertySet(CommandSender sender, CartProperties properties, @FlagYielding @Argument(value = "destinations",suggestions = "destinations") String[] destinations) {
      this.setPropertySetGeneric(sender, properties, destinations);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("route")
   @Command("cart route remove <destinations>")
   @CommandDescription("Removes one or more destinations from the route of a cart")
   private void setPropertyRemove(CommandSender sender, CartProperties properties, @FlagYielding @Argument(value = "destinations",suggestions = "destinations") String[] destinations) {
      this.setPropertyRemoveGeneric(sender, properties, destinations);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("route")
   @Command("cart route clear")
   @CommandDescription("Clears the route set for a cart")
   private void setPropertyClear(CommandSender sender, CartProperties properties) {
      properties.clearDestinationRoute();
      sender.sendMessage(ChatColor.YELLOW + "Route cleared!");
   }

   @CommandTargetTrain
   @CommandRequiresPermission(Permission.COMMAND_SAVE_ROUTE)
   @Command("cart route save <route_name>")
   @CommandDescription("Saves the current route of the cart with a name, which can then be loaded by that name")
   private void getPropertySaveRoute(TrainCarts plugin, CommandSender sender, CartProperties properties, @Argument("route_name") @Greedy String routeName) {
      plugin.getRouteManager().storeRoute(routeName, properties.getDestinationRoute());
      sender.sendMessage(ChatColor.YELLOW + "Route saved as '" + ChatColor.WHITE + routeName + ChatColor.YELLOW + "'!");
   }

   @CommandTargetTrain
   @PropertyCheckPermission("route")
   @Command("cart route load <route_name>")
   @CommandDescription("Resets the route and loads a new route by name for a cart")
   private void setPropertyLoadRoute(CommandSender sender, CartProperties properties, @Argument(value = "route_name",suggestions = "savedRouteNames") @Greedy String routeName) {
      this.setPropertyLoadRouteGeneric(sender, properties, routeName);
   }

   @Command("train route")
   @CommandDescription("Displays the current route set for a train")
   private void getProperty(CommandSender sender, TrainProperties properties) {
      this.showRoute("/train route", sender, properties);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("route")
   @Command("train route add <destinations>")
   @CommandDescription("Adds one or more destinations to the route set for a train")
   private void setPropertyAdd(CommandSender sender, TrainProperties properties, @FlagYielding @Argument(value = "destinations",suggestions = "destinations") String[] destinations) {
      this.setPropertyAddGeneric(sender, properties, destinations);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("route")
   @Command("train route set <destinations>")
   @CommandDescription("Resets the route to one or more destinations for a train")
   private void setPropertySet(CommandSender sender, TrainProperties properties, @FlagYielding @Argument(value = "destinations",suggestions = "destinations") String[] destinations) {
      this.setPropertySetGeneric(sender, properties, destinations);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("route")
   @Command("train route remove <destinations>")
   @CommandDescription("Removes one or more destinations from the route of a train")
   private void setPropertyRemove(CommandSender sender, TrainProperties properties, @FlagYielding @Argument(value = "destinations",suggestions = "destinations") String[] destinations) {
      this.setPropertyRemoveGeneric(sender, properties, destinations);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("route")
   @Command("train route clear")
   @CommandDescription("Clears the route set for a train")
   private void setPropertyClear(CommandSender sender, TrainProperties properties) {
      properties.clearDestinationRoute();
      sender.sendMessage(ChatColor.YELLOW + "Route cleared!");
   }

   @CommandTargetTrain
   @CommandRequiresPermission(Permission.COMMAND_SAVE_ROUTE)
   @Command("train route save <route_name>")
   @CommandDescription("Saves the current route of the train with a name, which can then be loaded by that name")
   private void getPropertySaveRoute(TrainCarts plugin, CommandSender sender, TrainProperties properties, @Argument("route_name") @Greedy String routeName) {
      plugin.getRouteManager().storeRoute(routeName, properties.getDestinationRoute());
      sender.sendMessage(ChatColor.YELLOW + "Route saved as '" + ChatColor.WHITE + routeName + ChatColor.YELLOW + "'!");
   }

   @CommandTargetTrain
   @PropertyCheckPermission("route")
   @Command("train route load <route_name>")
   @CommandDescription("Resets the route and loads a new route by name for a train")
   private void setPropertyLoadRoute(CommandSender sender, TrainProperties properties, @Argument(value = "route_name",suggestions = "savedRouteNames") @Greedy String routeName) {
      this.setPropertyLoadRouteGeneric(sender, properties, routeName);
   }

   @Suggestions("savedRouteNames")
   public List<String> getSavedRouteNames(CommandContext<CommandSender> context, String input) {
      TrainCarts plugin = (TrainCarts)context.inject(TrainCarts.class).get();
      return plugin.getRouteManager().getRouteNames();
   }

   private void setPropertySetGeneric(CommandSender sender, IProperties properties, String[] destinations) {
      MessageBuilder builder = new MessageBuilder();
      builder.yellow(new Object[]{"Discarded the previous route and set the destinations:"});
      builder.newLine().setSeparator(ChatColor.WHITE, " → ");
      properties.clearDestinationRoute();
      String[] var5 = destinations;
      int var6 = destinations.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         String destination = var5[var7];
         builder.green(new Object[]{destination});
         properties.addDestinationToRoute(destination);
      }

      builder.send(sender);
   }

   private void setPropertyAddGeneric(CommandSender sender, IProperties properties, String[] destinations) {
      MessageBuilder builder = new MessageBuilder();
      builder.yellow(new Object[]{"Added the destinations to the end of the route:"}).newLine();
      builder.setSeparator(ChatColor.WHITE, " → ");
      builder.green(new Object[]{""});
      String[] var5 = destinations;
      int var6 = destinations.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         String destination = var5[var7];
         builder.green(new Object[]{destination});
         properties.addDestinationToRoute(destination);
      }

      builder.clearSeparator().newLine();
      this.afterSetBuildCurrentRoute(builder, properties);
      builder.send(sender);
   }

   private void setPropertyRemoveGeneric(CommandSender sender, IProperties properties, String[] destinations) {
      MessageBuilder builder = new MessageBuilder();
      builder.yellow(new Object[]{"Removed the destinations from the route:"}).newLine();
      builder.setSeparator(ChatColor.WHITE, " ");
      String[] var5 = destinations;
      int var6 = destinations.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         String destination = var5[var7];
         builder.green(new Object[]{destination});
         properties.removeDestinationFromRoute(destination);
      }

      builder.clearSeparator().newLine();
      this.afterSetBuildCurrentRoute(builder, properties);
      builder.send(sender);
   }

   private void setPropertyLoadRouteGeneric(CommandSender sender, IProperties properties, String routeName) {
      List<String> newRoute = properties.getTrainCarts().getRouteManager().findRoute(routeName);
      properties.setDestinationRoute(newRoute);
      if (newRoute.isEmpty()) {
         sender.sendMessage(ChatColor.RED + "Route '" + routeName + "' is empty or does not exist!");
      } else {
         MessageBuilder builder = new MessageBuilder();
         builder.yellow(new Object[]{"Loaded route '"}).white(new Object[]{routeName}).yellow(new Object[]{"':"});
         builder.newLine().setSeparator(ChatColor.WHITE, " → ");
         Iterator var6 = newRoute.iterator();

         while(var6.hasNext()) {
            String destination = (String)var6.next();
            builder.green(new Object[]{destination});
         }

         builder.send(sender);
      }

   }

   private void afterSetBuildCurrentRoute(MessageBuilder builder, IProperties properties) {
      builder.newLine().yellow(new Object[]{"New route: "});
      builder.setSeparator(ChatColor.WHITE, " → ");
      Iterator var3 = properties.getDestinationRoute().iterator();

      while(var3.hasNext()) {
         String destination = (String)var3.next();
         builder.green(new Object[]{destination});
      }

   }

   @PropertyParser("clearroute|route clear")
   public List<String> parseClear(String input) {
      return Collections.emptyList();
   }

   @PropertyParser("setroute|route set")
   public List<String> parseSet(String input) {
      return input.isEmpty() ? Collections.emptyList() : Collections.singletonList(input);
   }

   @PropertyParser("loadroute|route load")
   public List<String> parseLoad(PropertyParseContext<String> context) {
      return context.getTrainCarts().getRouteManager().findRoute(context.input());
   }

   @PropertyParser(
      value = "addroute|route add",
      processPerCart = true
   )
   public List<String> parseAdd(PropertyParseContext<List<String>> context) {
      if (context.input().isEmpty()) {
         return (List)context.current();
      } else if (((List)context.current()).isEmpty()) {
         return Collections.singletonList(context.input());
      } else {
         ArrayList<String> newRoute = new ArrayList((Collection)context.current());
         newRoute.add(context.input());
         return Collections.unmodifiableList(newRoute);
      }
   }

   @PropertyParser(
      value = "route add route",
      processPerCart = true
   )
   public List<String> parseAddRoute(PropertyParseContext<List<String>> context) {
      List<String> route = context.getTrainCarts().getRouteManager().findRoute(context.input());
      if (route.isEmpty()) {
         return (List)context.current();
      } else {
         ArrayList<String> newRoute = new ArrayList((Collection)context.current());
         newRoute.addAll(route);
         return Collections.unmodifiableList(newRoute);
      }
   }

   @PropertyParser(
      value = "remroute|removeroute|route rem|route remove",
      processPerCart = true
   )
   public List<String> parseRemove(PropertyParseContext<List<String>> context) {
      if (!context.input().isEmpty() && ((List)context.current()).contains(context.input())) {
         ArrayList newRoute = new ArrayList((Collection)context.current());

         while(newRoute.remove(context.input())) {
         }

         return Collections.unmodifiableList(newRoute);
      } else {
         return (List)context.current();
      }
   }

   @PropertyParser(
      value = "route remove route|route rem route",
      processPerCart = true
   )
   public List<String> parseRemoveRoute(PropertyParseContext<List<String>> context) {
      List<String> route = context.getTrainCarts().getRouteManager().findRoute(context.input());
      if (route.isEmpty()) {
         return (List)context.current();
      } else {
         ArrayList newRoute = new ArrayList((Collection)context.current());

         while(newRoute.removeAll(route)) {
         }

         return Collections.unmodifiableList(newRoute);
      }
   }

   public boolean hasPermission(CommandSender sender, String name) {
      return Permission.PROPERTY_ROUTE.has(sender);
   }

   public List<String> getDefault() {
      return Collections.emptyList();
   }

   public Optional<List<String>> readFromConfig(ConfigurationNode config) {
      return config.contains("route") ? Optional.of(Collections.unmodifiableList(new ArrayList(config.getList("route", String.class)))) : Optional.empty();
   }

   public void writeToConfig(ConfigurationNode config, Optional<List<String>> value) {
      if (value.isPresent()) {
         config.set("route", value.get());
      } else {
         config.remove("route");
      }

   }

   public void set(CartProperties properties, List<String> value) {
      ICartProperty.super.set((CartProperties)properties, value);
      if (!value.isEmpty() && properties.hasDestination()) {
         int new_index = value.indexOf(properties.getDestination());
         if (new_index == -1) {
            new_index = 0;
         }

         properties.set(StandardProperties.DESTINATION_ROUTE_INDEX, new_index);
      } else {
         properties.set(StandardProperties.DESTINATION_ROUTE_INDEX, 0);
      }

   }

   public List<String> get(TrainProperties properties) {
      Iterator var2 = properties.iterator();

      List route;
      do {
         if (!var2.hasNext()) {
            return Collections.emptyList();
         }

         CartProperties cprop = (CartProperties)var2.next();
         route = (List)this.get((CartProperties)cprop);
      } while(route.isEmpty());

      return route;
   }

   public static final class IndexProperty implements ICartProperty<Integer> {
      private final Integer DEFAULT = 0;

      public Integer getDefault() {
         return this.DEFAULT;
      }

      public Optional<Integer> readFromConfig(ConfigurationNode config) {
         return Util.getConfigOptional(config, "routeIndex", Integer.TYPE);
      }

      public void writeToConfig(ConfigurationNode config, Optional<Integer> value) {
         Util.setConfigOptional(config, "routeIndex", value);
      }
   }
}
