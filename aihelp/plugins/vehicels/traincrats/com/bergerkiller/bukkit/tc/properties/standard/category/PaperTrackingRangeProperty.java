package com.bergerkiller.bukkit.tc.properties.standard.category;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Argument;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Command;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.CommandDescription;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.commands.annotations.CommandTargetTrain;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.properties.CartProperties;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.api.ICartProperty;
import com.bergerkiller.bukkit.tc.properties.api.PropertyCheckPermission;
import com.bergerkiller.bukkit.tc.properties.api.PropertyParser;
import com.bergerkiller.bukkit.tc.properties.api.context.PropertyParseContext;
import com.bergerkiller.mountiplex.reflection.util.FastMethod;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public final class PaperTrackingRangeProperty implements ICartProperty<Integer> {
   public static final PaperTrackingRangeProperty INSTANCE = new PaperTrackingRangeProperty();
   private final FastMethod<Void> setCustomTrackingRange = new FastMethod();

   @CommandTargetTrain
   @PropertyCheckPermission("trackingrange")
   @Command("train trackingrange reset")
   @CommandDescription("Resets the view distance players inside the train have to the defaults")
   private void resetProperty(CommandSender sender, TrainProperties properties) {
      this.setProperty(sender, (TrainProperties)properties, -1);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("trackingrange")
   @Command("train trackingrange <num_blocks>")
   @CommandDescription("Sets the view distance players inside the train have")
   private void setProperty(CommandSender sender, TrainProperties properties, @Argument("num_blocks") int distance) {
      properties.set(this, distance);
      this.getProperty(sender, properties);
   }

   @Command("train trackingrange")
   @CommandDescription("Displays the view distance players inside the train have")
   private void getProperty(CommandSender sender, TrainProperties properties) {
      int distance = (Integer)properties.get(this);
      if (distance >= 0) {
         sender.sendMessage(ChatColor.YELLOW + "Train is visible from: " + ChatColor.WHITE + distance + " blocks");
      } else {
         sender.sendMessage(ChatColor.YELLOW + "Train is visible from: " + ChatColor.RED + "Default (not set)");
      }

   }

   @CommandTargetTrain
   @PropertyCheckPermission("trackingrange")
   @Command("cart trackingrange reset")
   @CommandDescription("Resets the view distance players inside the cart have to the defaults")
   private void resetProperty(CommandSender sender, CartProperties properties) {
      this.setProperty(sender, (CartProperties)properties, -1);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("trackingrange")
   @Command("cart trackingrange <num_blocks>")
   @CommandDescription("Sets the view distance players inside the cart have")
   private void setProperty(CommandSender sender, CartProperties properties, @Argument("num_blocks") int distance) {
      properties.set(this, distance);
      this.getProperty(sender, properties);
   }

   @Command("cart trackingrange")
   @CommandDescription("Displays the view distance players inside the cart have")
   private void getProperty(CommandSender sender, CartProperties properties) {
      int distance = (Integer)properties.get(this);
      if (distance >= 0) {
         sender.sendMessage(ChatColor.YELLOW + "Cart is visible from: " + ChatColor.WHITE + distance + " blocks");
      } else {
         sender.sendMessage(ChatColor.YELLOW + "Cart is visible from: " + ChatColor.RED + "Default (not set)");
      }

   }

   @PropertyParser("trackingrange")
   public int parseTrackingRange(PropertyParseContext<Integer> context) {
      return context.inputInteger();
   }

   public boolean hasPermission(CommandSender sender, String name) {
      return Permission.PROPERTY_VIEW_DISTANCE.has(sender);
   }

   public Integer getDefault() {
      return -1;
   }

   public Optional<Integer> readFromConfig(ConfigurationNode config) {
      return Util.getConfigOptional(config, "paperTrackingRange", Integer.TYPE);
   }

   public void writeToConfig(ConfigurationNode config, Optional<Integer> value) {
      Util.setConfigOptional(config, "paperTrackingRange", value);
   }

   public void set(CartProperties properties, Integer value) {
      ICartProperty.super.set((CartProperties)properties, value);
      MinecartMember<?> member = properties.getHolder();
      if (member != null && !member.isUnloaded()) {
         this.setCustomTrackingRange.invoke(((CommonMinecart)member.getEntity()).getEntity(), value);
      }

      Iterator var4 = Bukkit.getOnlinePlayers().iterator();

      while(var4.hasNext()) {
         Player var5 = (Player)var4.next();

         try {
            Method var6 = Player.class.getMethod("setSendViewDistance", Integer.TYPE);
         } catch (Throwable var7) {
            var7.printStackTrace();
         }
      }

   }

   public void enable(TrainCarts plugin) throws Throwable {
      this.setCustomTrackingRange.init(Entity.class.getMethod("setCustomTrackingRange", Integer.TYPE));
      this.setCustomTrackingRange.forceInitialization();
   }

   public void disable(TrainCarts plugin) {
   }
}
