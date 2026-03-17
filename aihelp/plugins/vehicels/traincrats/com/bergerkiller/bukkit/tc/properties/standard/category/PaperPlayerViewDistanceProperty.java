package com.bergerkiller.bukkit.tc.properties.standard.category;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Argument;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Command;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.CommandDescription;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.commands.annotations.CommandTargetTrain;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.MinecartMemberStore;
import com.bergerkiller.bukkit.tc.events.seat.MemberSeatChangeEvent;
import com.bergerkiller.bukkit.tc.events.seat.MemberSeatEnterEvent;
import com.bergerkiller.bukkit.tc.events.seat.MemberSeatExitEvent;
import com.bergerkiller.bukkit.tc.properties.CartProperties;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.api.ICartProperty;
import com.bergerkiller.bukkit.tc.properties.api.PropertyCheckPermission;
import com.bergerkiller.bukkit.tc.properties.api.PropertyParser;
import com.bergerkiller.bukkit.tc.properties.api.context.PropertyParseContext;
import com.bergerkiller.mountiplex.reflection.util.FastMethod;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PaperPlayerViewDistanceProperty implements ICartProperty<Integer>, Listener {
   public static final PaperPlayerViewDistanceProperty INSTANCE = new PaperPlayerViewDistanceProperty();
   private final Map<Player, PaperPlayerViewDistanceProperty.PreviousViewSettings> previousViewSettings = new HashMap();
   private final FastMethod<Integer> getViewDistance = new FastMethod();
   private final FastMethod<Void> setViewDistance = new FastMethod();
   private final FastMethod<Integer> getSimulationDistance = new FastMethod();
   private final FastMethod<Void> setSimulationDistance = new FastMethod();
   private final FastMethod<Integer> getChunkSendDistance = new FastMethod();
   private final FastMethod<Void> setChunkSendDistance = new FastMethod();

   @CommandTargetTrain
   @PropertyCheckPermission("viewdistance")
   @Command("train viewdistance reset")
   @CommandDescription("Resets the view distance players inside the train have to the defaults")
   private void resetProperty(CommandSender sender, TrainProperties properties) {
      this.setProperty(sender, (TrainProperties)properties, -1);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("viewdistance")
   @Command("train viewdistance <num_chunks>")
   @CommandDescription("Sets the view distance players inside the train have")
   private void setProperty(CommandSender sender, TrainProperties properties, @Argument("num_chunks") int distance) {
      properties.set(this, distance);
      this.getProperty(sender, properties);
   }

   @Command("train viewdistance")
   @CommandDescription("Displays the view distance players inside the train have")
   private void getProperty(CommandSender sender, TrainProperties properties) {
      int distance = (Integer)properties.get(this);
      if (distance >= 0) {
         sender.sendMessage(ChatColor.YELLOW + "View distance of players in the train: " + ChatColor.WHITE + distance + " chunks");
      } else {
         sender.sendMessage(ChatColor.YELLOW + "View distance of players in the train: " + ChatColor.RED + "Default (not set)");
      }

   }

   @CommandTargetTrain
   @PropertyCheckPermission("viewdistance")
   @Command("cart viewdistance reset")
   @CommandDescription("Resets the view distance players inside the cart have to the defaults")
   private void resetProperty(CommandSender sender, CartProperties properties) {
      this.setProperty(sender, (CartProperties)properties, -1);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("viewdistance")
   @Command("cart viewdistance <num_chunks>")
   @CommandDescription("Sets the view distance players inside the cart have")
   private void setProperty(CommandSender sender, CartProperties properties, @Argument("num_chunks") int distance) {
      properties.set(this, distance);
      this.getProperty(sender, properties);
   }

   @Command("cart viewdistance")
   @CommandDescription("Displays the view distance players inside the cart have")
   private void getProperty(CommandSender sender, CartProperties properties) {
      int distance = (Integer)properties.get(this);
      if (distance >= 0) {
         sender.sendMessage(ChatColor.YELLOW + "View distance of players in the cart: " + ChatColor.WHITE + distance + " chunks");
      } else {
         sender.sendMessage(ChatColor.YELLOW + "View distance of players in the cart: " + ChatColor.RED + "Default (not set)");
      }

   }

   @PropertyParser("viewdistance|playerviewdistance")
   public int parseViewDistance(PropertyParseContext<Integer> context) {
      return context.inputInteger();
   }

   public boolean hasPermission(CommandSender sender, String name) {
      return Permission.PROPERTY_VIEW_DISTANCE.has(sender);
   }

   public Integer getDefault() {
      return -1;
   }

   public Optional<Integer> readFromConfig(ConfigurationNode config) {
      return Util.getConfigOptional(config, "paperPlayerViewDistance", Integer.TYPE);
   }

   public void writeToConfig(ConfigurationNode config, Optional<Integer> value) {
      Util.setConfigOptional(config, "paperPlayerViewDistance", value);
   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onPlayerQuit(PlayerQuitEvent event) {
      MinecartMember<?> member = MinecartMemberStore.getFromEntity(event.getPlayer().getVehicle());
      if (member != null && (Integer)member.getProperties().get(this) >= 0) {
         this.restore(event.getPlayer());
      }

   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onMemberSeatExit(MemberSeatExitEvent event) {
      if (event.isPlayer()) {
         if (event.isMemberVehicleChange()) {
            if (event.isSeatChange()) {
               MinecartMember<?> newMember = ((MemberSeatChangeEvent)event).getEnteredMember();
               int newViewDistance = (Integer)newMember.getProperties().get(this);
               if (newViewDistance >= 0) {
                  this.apply((Player)event.getEntity(), newViewDistance);
               } else if ((Integer)event.getMember().getProperties().get(this) >= 0) {
                  this.restore((Player)event.getEntity());
               }

            } else {
               if ((Integer)event.getMember().getProperties().get(this) >= 0) {
                  this.restore((Player)event.getEntity());
               }

            }
         }
      }
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onMemberSeatEnter(MemberSeatEnterEvent event) {
      if (event.isPlayer()) {
         if (!event.wasSeatChange()) {
            int viewDistance = (Integer)event.getMember().getProperties().get(this);
            if (viewDistance >= 0) {
               this.apply((Player)event.getEntity(), viewDistance);
            }

         }
      }
   }

   public void set(CartProperties properties, Integer value) {
      boolean hadViewDistance = (Integer)this.get(properties) >= 0;
      ICartProperty.super.set((CartProperties)properties, value);
      MinecartMember<?> member = properties.getHolder();
      if (member != null && !member.isUnloaded()) {
         Iterator var5;
         Player player;
         if (hadViewDistance && value < 0) {
            var5 = ((CommonMinecart)member.getEntity()).getPlayerPassengers().iterator();

            while(var5.hasNext()) {
               player = (Player)var5.next();
               this.restore(player);
            }
         } else if (value >= 0) {
            var5 = ((CommonMinecart)member.getEntity()).getPlayerPassengers().iterator();

            while(var5.hasNext()) {
               player = (Player)var5.next();
               this.apply(player, value);
            }
         }
      }

   }

   public void enable(TrainCarts plugin) throws Throwable {
      this.getViewDistance.init(Player.class.getMethod("getViewDistance"));
      this.setViewDistance.init(Player.class.getMethod("setViewDistance", Integer.TYPE));
      this.getSimulationDistance.init(Player.class.getMethod("getNoTickViewDistance"));
      this.setSimulationDistance.init(Player.class.getMethod("setNoTickViewDistance", Integer.TYPE));
      this.getChunkSendDistance.init(Player.class.getMethod("getSendViewDistance"));
      this.setChunkSendDistance.init(Player.class.getMethod("setSendViewDistance", Integer.TYPE));
      this.getViewDistance.forceInitialization();
      this.setViewDistance.forceInitialization();
      this.getSimulationDistance.forceInitialization();
      this.setSimulationDistance.forceInitialization();
      this.getChunkSendDistance.forceInitialization();
      this.setChunkSendDistance.forceInitialization();
      plugin.register(this);
   }

   public void disable(TrainCarts plugin) {
      Iterator var2 = this.previousViewSettings.entrySet().iterator();

      while(var2.hasNext()) {
         Entry<Player, PaperPlayerViewDistanceProperty.PreviousViewSettings> e = (Entry)var2.next();
         ((PaperPlayerViewDistanceProperty.PreviousViewSettings)e.getValue()).restore((Player)e.getKey());
      }

   }

   private void restore(Player player) {
      PaperPlayerViewDistanceProperty.PreviousViewSettings prevViewSettings = (PaperPlayerViewDistanceProperty.PreviousViewSettings)this.previousViewSettings.remove(player);
      if (prevViewSettings != null) {
         prevViewSettings.restore(player);
      }

   }

   private void apply(Player player, int viewDistance) {
      this.previousViewSettings.computeIfAbsent(player, (x$0) -> {
         return new PaperPlayerViewDistanceProperty.PreviousViewSettings(x$0);
      });
      viewDistance = MathUtil.clamp(viewDistance, 2, 31);
      this.setSimulationDistance.invoke(player, viewDistance + 1);
      this.setViewDistance.invoke(player, viewDistance + 1);
      this.setChunkSendDistance.invoke(player, viewDistance);
   }

   private class PreviousViewSettings {
      public final int viewDistance;
      public final int simulationDistance;
      public final int chunkSendDistance;

      public PreviousViewSettings(Player player) {
         this.viewDistance = (Integer)PaperPlayerViewDistanceProperty.this.getViewDistance.invoke(player);
         this.simulationDistance = (Integer)PaperPlayerViewDistanceProperty.this.getSimulationDistance.invoke(player);
         this.chunkSendDistance = (Integer)PaperPlayerViewDistanceProperty.this.getChunkSendDistance.invoke(player);
      }

      public void restore(Player player) {
         PaperPlayerViewDistanceProperty.this.setSimulationDistance.invoke(player, this.simulationDistance);
         PaperPlayerViewDistanceProperty.this.setChunkSendDistance.invoke(player, this.chunkSendDistance);
         PaperPlayerViewDistanceProperty.this.setViewDistance.invoke(player, this.viewDistance);
      }

      public String toString() {
         return "View{distance=" + this.viewDistance + ", simulation=" + this.simulationDistance + ", chunk=" + this.chunkSendDistance + "}";
      }
   }
}
