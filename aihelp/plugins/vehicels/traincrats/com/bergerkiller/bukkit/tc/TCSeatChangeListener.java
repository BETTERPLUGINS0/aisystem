package com.bergerkiller.bukkit.tc;

import com.bergerkiller.bukkit.common.Task;
import com.bergerkiller.bukkit.common.controller.EntityNetworkController;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.tc.attachments.control.CartAttachmentSeat;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.MinecartMemberStore;
import com.bergerkiller.bukkit.tc.events.seat.MemberBeforeSeatChangeEvent;
import com.bergerkiller.bukkit.tc.events.seat.MemberBeforeSeatEnterEvent;
import com.bergerkiller.bukkit.tc.events.seat.MemberBeforeSeatExitEvent;
import com.bergerkiller.bukkit.tc.events.seat.MemberSeatEnterEvent;
import com.bergerkiller.bukkit.tc.events.seat.MemberSeatExitEvent;
import com.bergerkiller.bukkit.tc.properties.CartProperties;
import com.bergerkiller.bukkit.tc.tickets.TicketStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class TCSeatChangeListener implements Listener {
   public static boolean suppressSeatChangeEvents = false;
   public static List<Entity> exemptFromEjectOffset = new ArrayList();
   private static Map<Player, Integer> markedForUnmounting = new HashMap();

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   public void onMemberSeatChangeFireEnterEvent(MemberBeforeSeatChangeEvent event) {
      MemberBeforeSeatEnterEvent enterEvent = new MemberBeforeSeatEnterEvent(event.getEnteredSeat(), event.getEntity(), event.isPlayerInitiated(), true, event.isMemberVehicleChange());
      CommonUtil.callEvent(enterEvent);
      event.setCancelled(enterEvent.isCancelled());
      event.setEnteredSeat(enterEvent.getSeat());
   }

   @EventHandler(
      priority = EventPriority.NORMAL,
      ignoreCancelled = true
   )
   public void onMemberSeatExit(MemberBeforeSeatExitEvent event) {
      if (!event.isSeatChange()) {
         this.handleVehicleChange(event.getMember(), (MinecartMember)null, event.getEntity(), event.isPlayerInitiated());
      }

   }

   @EventHandler(
      priority = EventPriority.NORMAL,
      ignoreCancelled = true
   )
   public void onMemberSeatChange(MemberBeforeSeatChangeEvent event) {
      event.setCancelled(!this.handleVehicleChange(event.getMember(), event.getEnteredMember(), event.getEntity(), event.isPlayerInitiated()));
   }

   @EventHandler(
      priority = EventPriority.NORMAL,
      ignoreCancelled = true
   )
   public void onMemberSeatEnter(MemberBeforeSeatEnterEvent event) {
      if (!event.wasSeatChange()) {
         event.setCancelled(!this.handleVehicleChange((MinecartMember)null, event.getMember(), event.getEntity(), event.isPlayerInitiated()));
      }
   }

   private boolean handleVehicleChange(MinecartMember<?> old_member, MinecartMember<?> new_member, Entity passenger, boolean playerInitiated) {
      if (new_member != null && !new_member.isInteractable()) {
         return false;
      } else {
         if (playerInitiated && passenger instanceof Player) {
            if (old_member != null && new_member != null && !old_member.getProperties().getPlayersExit()) {
               return false;
            }

            if (new_member != null) {
               CartProperties prop = new_member.getProperties();
               if (!prop.getPlayersEnter()) {
                  return false;
               }

               if (prop.getCanOnlyOwnersEnter() && !prop.hasOwnership((Player)passenger)) {
                  return false;
               }

               if ((old_member == null || old_member.getGroup() != new_member.getGroup()) && !TicketStore.handleTickets((Player)passenger, new_member.getGroup().getProperties())) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onMemberSeatExitHandleEjectOffset(MemberSeatExitEvent event) {
      if (!event.isSeatChange() && !exemptFromEjectOffset.contains(event.getEntity())) {
         Entity e = event.getEntity();
         if (e.isDead() || e.getVehicle() != null || event.getMember().getEntity() == null) {
            return;
         }

         Location old_entity_location = ((CommonMinecart)event.getMember().getEntity()).getLocation();
         Location old_seat_location = event.getSeatPosition();
         Location loc = event.getExitPosition();
         Location new_location = e.getLocation();
         if (!isPossibleExit(new_location, old_entity_location) && !isPossibleExit(new_location, old_seat_location)) {
            return;
         }

         Util.correctTeleportPosition(loc);
         if (event.isExitRotationPreserved()) {
            Util.teleportPosition(e, loc);
         } else {
            e.teleport(loc);
         }
      }

   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onMemberSeatExitMonitor(MemberSeatExitEvent event) {
      if (event.getMember().isInteractable()) {
         if (event.isMemberVehicleChange()) {
            event.getMember().resetCollisionEnter();
         }

         event.getMember().onPropertiesChanged();
      }
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onMemberSeatEnterMonitor(MemberSeatEnterEvent event) {
      if (event.getEntity() instanceof Player) {
         Player player = (Player)event.getEntity();
         CartProperties cprop = event.getMember().getProperties();
         cprop.getTrainCarts().getPlayer(player).editCart(cprop);
         if (event.wasMemberVehicleChange()) {
            cprop.showEnterMessage(player);
         }
      }

      event.getMember().onPropertiesChanged();
   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   public void onVehicleEnter(VehicleEnterEvent event) {
      MinecartMember member;
      if (!suppressSeatChangeEvents && (member = MinecartMemberStore.getFromEntity(event.getVehicle())) != null) {
         CartAttachmentSeat seat = member.getAttachments().findNewSeatForEntity(event.getEntered());
         if (seat == null) {
            event.setCancelled(true);
         } else {
            MemberBeforeSeatEnterEvent memberEnterEvent = new MemberBeforeSeatEnterEvent(seat, event.getEntered(), false, false, true);
            if (((MemberBeforeSeatEnterEvent)CommonUtil.callEvent(memberEnterEvent)).isCancelled()) {
               event.setCancelled(true);
            } else {
               if (memberEnterEvent.getSeat() != seat) {
                  memberEnterEvent.getSeat().getController().storeSeatHint(event.getEntered(), memberEnterEvent.getSeat());
                  if (memberEnterEvent.getSeat().getMember() != member) {
                     event.setCancelled(true);

                     try {
                        suppressSeatChangeEvents = true;
                        event.getEntered().eject();
                        if (event.getEntered().getVehicle() == null && !((CommonMinecart)memberEnterEvent.getSeat().getMember().getEntity()).addPassenger(event.getEntered())) {
                           return;
                        }
                     } finally {
                        suppressSeatChangeEvents = false;
                     }
                  }
               }

               Entity vehicle = event.getVehicle();
               Entity passenger = event.getEntered();
               CommonUtil.nextTick(() -> {
                  if (passenger.getVehicle() == vehicle) {
                     CommonUtil.callEvent(new MemberSeatEnterEvent(seat, passenger, false, false, true));
                  }

               });
            }
         }
      }
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = false
   )
   public void onVehicleEnterCheck(VehicleEnterEvent event) {
      if (event.isCancelled()) {
         final Entity entered = event.getEntered();
         exemptFromEjectOffset.add(entered);
         CommonUtil.nextTick(new Runnable() {
            public void run() {
               TCSeatChangeListener.exemptFromEjectOffset.remove(entered);
            }
         });
      }

   }

   public static void markForUnmounting(TrainCarts traincarts, Player player) {
      synchronized(markedForUnmounting) {
         if (markedForUnmounting.isEmpty()) {
            (new Task(traincarts) {
               public void run() {
                  synchronized(TCSeatChangeListener.markedForUnmounting) {
                     int curr_ticks = CommonUtil.getServerTicks();
                     Iterator iter = TCSeatChangeListener.markedForUnmounting.entrySet().iterator();

                     while(true) {
                        while(iter.hasNext()) {
                           Entry<Player, Integer> e = (Entry)iter.next();
                           if (((Player)e.getKey()).isSneaking() && ((Player)e.getKey()).getVehicle() == null) {
                              e.setValue(curr_ticks);
                           } else if (curr_ticks - (Integer)e.getValue() >= 2) {
                              iter.remove();
                           }
                        }

                        if (TCSeatChangeListener.markedForUnmounting.isEmpty()) {
                           this.stop();
                        }

                        return;
                     }
                  }
               }
            }).start(1L, 1L);
         }

         markedForUnmounting.put(player, CommonUtil.getServerTicks());
      }
   }

   @EventHandler(
      priority = EventPriority.LOWEST,
      ignoreCancelled = true
   )
   public void onVehicleExitCheck(VehicleExitEvent event) {
      synchronized(markedForUnmounting) {
         if (!markedForUnmounting.containsKey(event.getExited())) {
            return;
         }
      }

      MinecartMember<?> mm = MinecartMemberStore.getFromEntity(event.getVehicle());
      if (mm != null && !mm.getProperties().getPlayersExit()) {
         event.setCancelled(true);
      }

   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   public void onVehicleExit(VehicleExitEvent event) {
      MinecartMember member;
      if (!suppressSeatChangeEvents && (member = MinecartMemberStore.getFromEntity(event.getVehicle())) != null) {
         CartAttachmentSeat seat = member.getAttachments().findSeat(event.getExited());
         if (seat != null) {
            boolean playerInitiated = event.getExited() instanceof Player && ((Player)event.getExited()).isSneaking();
            Location seatPosition = seat.getPosition(event.getExited());
            MemberBeforeSeatExitEvent memberExitEvent = new MemberBeforeSeatExitEvent(seat, event.getExited(), seatPosition, seat.getEjectPosition(event.getExited()), seat.isEjectRotationPreserved(), playerInitiated);
            if (((MemberBeforeSeatExitEvent)CommonUtil.callEvent(memberExitEvent)).isCancelled()) {
               event.setCancelled(true);
               return;
            }

            Location exitPosition = memberExitEvent.getExitPosition();
            boolean exitPreservePlayerRotation = memberExitEvent.isExitPlayerRotationPreserved();
            Entity vehicle = event.getVehicle();
            Entity passenger = event.getExited();
            CommonUtil.nextTick(() -> {
               if (!member.isUnloaded()) {
                  if (!((CommonMinecart)member.getEntity()).isRemoved()) {
                     EntityNetworkController<?> controller = ((CommonMinecart)member.getEntity()).getNetworkController();
                     if (controller != null) {
                        controller.syncPassengers();
                     }
                  }

                  if (passenger.getVehicle() != vehicle) {
                     CommonUtil.callEvent(new MemberSeatExitEvent(seat, passenger, seatPosition, exitPosition, exitPreservePlayerRotation, playerInitiated));
                  }

               }
            });
         }
      }

   }

   private static boolean isPossibleExit(Location a, Location b) {
      return a.getWorld() == b.getWorld() && Math.abs(a.getBlockX() - b.getBlockX()) <= 2 && Math.abs(a.getBlockY() - b.getBlockY()) <= 5 && Math.abs(a.getBlockZ() - b.getBlockZ()) <= 2;
   }
}
