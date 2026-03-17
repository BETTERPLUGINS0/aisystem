package com.bergerkiller.bukkit.tc;

import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.Task;
import com.bergerkiller.bukkit.common.collections.ImplicitlySharedSet;
import com.bergerkiller.bukkit.common.conversion.type.HandleConversion;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.events.PacketReceiveEvent;
import com.bergerkiller.bukkit.common.events.PacketSendEvent;
import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketListener;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.bergerkiller.bukkit.common.wrappers.HumanHand;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartGroupStore;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayInSteerVehicleHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayInUseEntityHandle;
import com.bergerkiller.generated.net.minecraft.world.EnumHandHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.player.EntityHumanHandle;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;

class TCPacketListener implements PacketListener {
   public static final int ATTACK_SUPPRESS_DURATION = 250;
   public static final PacketType[] LISTENED_TYPES;
   private final TrainCarts traincarts;
   private final Map<Player, Long> lastHitTime = new HashMap();
   private final Consumer<PacketReceiveEvent> steerHandler = Common.hasCapability("Common:PacketListener:SetPacket") ? this::handleSteerNew : this::handleSteerLegacy;

   public TCPacketListener(TrainCarts traincarts) {
      this.traincarts = traincarts;
   }

   public void suppressAttacksFor(Player player, int durationMillis) {
      synchronized(this.lastHitTime) {
         if (this.lastHitTime.isEmpty()) {
            (new TCPacketListener.HitTimeCleanTask(this.traincarts)).start(1L, 1L);
         }

         this.lastHitTime.put(player, System.currentTimeMillis() + (long)durationMillis);
      }
   }

   public boolean isAttackSuppressed(Player player) {
      synchronized(this.lastHitTime) {
         return this.lastHitTime.containsKey(player);
      }
   }

   public void onPacketSend(PacketSendEvent event) {
      Thread.dumpStack();
   }

   private void handleSteerLegacy(PacketReceiveEvent event) {
      CommonPacket packet = event.getPacket();
      if ((Boolean)packet.read(PacketType.IN_STEER_VEHICLE.unmount)) {
         Player player = event.getPlayer();
         if (player.getVehicle() == null) {
            TCSeatChangeListener.markForUnmounting(this.traincarts, player);
         } else if (!this.traincarts.handlePlayerVehicleChange(player, (Entity)null)) {
            packet.write(PacketType.IN_STEER_VEHICLE.unmount, false);
         }
      }

   }

   private void handleSteerNew(PacketReceiveEvent event) {
      PacketPlayInSteerVehicleHandle steerPacket = PacketPlayInSteerVehicleHandle.createHandle(event.getPacket().getHandle());
      if (steerPacket.isUnmount()) {
         Player player = event.getPlayer();
         if (player.getVehicle() == null) {
            TCSeatChangeListener.markForUnmounting(this.traincarts, player);
         } else if (!this.traincarts.handlePlayerVehicleChange(player, (Entity)null)) {
            event.setPacket(PacketPlayInSteerVehicleHandle.createNew(steerPacket.isLeft(), steerPacket.isRight(), steerPacket.isForward(), steerPacket.isBackward(), steerPacket.isJump(), false, steerPacket.isSprint()));
         }
      }

   }

   public void onPacketReceive(PacketReceiveEvent event) {
      CommonPacket packet = event.getPacket();
      Player player = event.getPlayer();
      if (event.getType() == PacketType.IN_ENTITY_ACTION) {
         String action = ((Enum)packet.read(PacketType.IN_ENTITY_ACTION.action)).name();
         if (action.equals("START_SNEAKING") || action.equals("PRESS_SHIFT_KEY")) {
            if (player.getVehicle() == null) {
               TCSeatChangeListener.markForUnmounting(this.traincarts, player);
            } else if (!this.traincarts.handlePlayerVehicleChange(player, (Entity)null)) {
               event.setCancelled(true);
            }
         }
      }

      if (event.getType() == PacketType.IN_STEER_VEHICLE) {
         this.steerHandler.accept(event);
      } else {
         ImplicitlySharedSet groups;
         label150: {
            label151: {
               label159: {
                  if (event.getType() == PacketType.IN_USE_ENTITY) {
                     PacketPlayInUseEntityHandle packet_use = PacketPlayInUseEntityHandle.createHandle(event.getPacket().getHandle());
                     if (packet_use.isUsingSecondaryAction()) {
                        if (player.getVehicle() == null) {
                           TCSeatChangeListener.markForUnmounting(this.traincarts, player);
                        } else if (!this.traincarts.handlePlayerVehicleChange(player, (Entity)null)) {
                           packet_use.setUsingSecondaryAction(false);
                        }
                     }

                     int entityId = packet_use.getUsedEntityId();
                     if (WorldUtil.getEntityById(event.getPlayer().getWorld(), entityId) != null) {
                        return;
                     }

                     if (event.getPlayer().getGameMode().name().equals("SPECTATOR")) {
                        return;
                     }

                     Location eyeLoc = event.getPlayer().getEyeLocation();
                     groups = MinecartGroupStore.getGroups().clone();

                     try {
                        Iterator var8 = groups.iterator();

                        while(var8.hasNext()) {
                           MinecartGroup group = (MinecartGroup)var8.next();
                           if (group.getWorld() == eyeLoc.getWorld()) {
                              Iterator var10 = group.iterator();

                              while(var10.hasNext()) {
                                 MinecartMember<?> member = (MinecartMember)var10.next();
                                 if (member.getAttachments().isViewer(event.getPlayer()) && member.getAttachments().isAttachment(entityId)) {
                                    if (packet_use.isInteract()) {
                                       event.setCancelled(true);
                                       break label150;
                                    }

                                    HumanHand hand;
                                    if (((CommonMinecart)member.getEntity()).loc.distanceSquared(eyeLoc) < 9.0D) {
                                       if (packet_use.isInteractAt()) {
                                          hand = packet_use.getInteractHand(event.getPlayer());
                                          packet_use.setInteract(event.getPlayer(), hand);
                                       }

                                       if (packet_use.isInteract() || packet_use.isInteractAt()) {
                                          this.suppressAttacksFor(event.getPlayer(), 250);
                                       }

                                       packet_use.setUsedEntityId(((CommonMinecart)member.getEntity()).getEntityId());
                                       break label151;
                                    }

                                    if (!packet_use.isInteract() && !packet_use.isInteractAt()) {
                                       if (packet_use.isAttack()) {
                                          fakeAttack(member, event.getPlayer());
                                          event.setCancelled(true);
                                       }
                                       break label159;
                                    }

                                    hand = packet_use.getInteractHand(event.getPlayer());
                                    this.fakeInteraction(member, event.getPlayer(), hand);
                                    event.setCancelled(true);
                                    break label159;
                                 }
                              }
                           }
                        }
                     } catch (Throwable var14) {
                        if (groups != null) {
                           try {
                              groups.close();
                           } catch (Throwable var13) {
                              var14.addSuppressed(var13);
                           }
                        }

                        throw var14;
                     }

                     if (groups != null) {
                        groups.close();
                     }
                  }

                  return;
               }

               if (groups != null) {
                  groups.close();
               }

               return;
            }

            if (groups != null) {
               groups.close();
            }

            return;
         }

         if (groups != null) {
            groups.close();
         }

      }
   }

   public static void fakeAttack(final MinecartMember<?> member, final Player player) {
      if (!CommonUtil.isMainThread()) {
         CommonUtil.nextTick(new Runnable() {
            public void run() {
               TCPacketListener.fakeAttack(member, player);
            }
         });
      } else if (member != null && !member.isUnloaded() && player != null && player.isValid()) {
         Object playerHandleRaw = HandleConversion.toEntityHandle(player);
         EntityHumanHandle.createHandle(playerHandleRaw).attack(((CommonMinecart)member.getEntity()).getEntity());
      }
   }

   public void fakeInteraction(final MinecartMember<?> member, final Player player, final HumanHand hand) {
      this.suppressAttacksFor(player, 250);
      if (!CommonUtil.isMainThread()) {
         CommonUtil.nextTick(new Runnable() {
            public void run() {
               TCPacketListener.this.fakeInteraction(member, player, hand);
            }
         });
      } else if (member != null && !member.isUnloaded() && player != null && player.isValid()) {
         if (EnumHandHandle.T.isAvailable()) {
            HumanHand mainHand = HumanHand.getMainHand(player);
            EquipmentSlot slot = EquipmentSlot.HAND;
            if (hand != mainHand) {
               try {
                  slot = EquipmentSlot.OFF_HAND;
               } catch (Throwable var7) {
               }
            }

            PlayerInteractEntityEvent interactEvent = new PlayerInteractEntityEvent(player, ((CommonMinecart)member.getEntity()).getEntity(), slot);
            if (((PlayerInteractEntityEvent)CommonUtil.callEvent(interactEvent)).isCancelled()) {
               return;
            }
         } else {
            PlayerInteractEntityEvent interactEvent = new PlayerInteractEntityEvent(player, ((CommonMinecart)member.getEntity()).getEntity());
            if (((PlayerInteractEntityEvent)CommonUtil.callEvent(interactEvent)).isCancelled()) {
               return;
            }
         }

         member.onInteractBy(player, hand);
      }
   }

   static {
      LISTENED_TYPES = new PacketType[]{PacketType.IN_STEER_VEHICLE, PacketType.IN_USE_ENTITY, PacketType.IN_ENTITY_ACTION};
   }

   private final class HitTimeCleanTask extends Task {
      public HitTimeCleanTask(JavaPlugin plugin) {
         super(plugin);
      }

      public void run() {
         synchronized(TCPacketListener.this.lastHitTime) {
            long timeout = System.currentTimeMillis();
            Iterator iter = TCPacketListener.this.lastHitTime.values().iterator();

            while(iter.hasNext()) {
               if (timeout >= (Long)iter.next()) {
                  iter.remove();
               }
            }

            if (TCPacketListener.this.lastHitTime.isEmpty()) {
               this.stop();
            }

         }
      }
   }
}
