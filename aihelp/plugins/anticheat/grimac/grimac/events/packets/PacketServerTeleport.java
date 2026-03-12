package ac.grim.grimac.events.packets;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.platform.api.world.PlatformWorld;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerAbstract;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerPriority;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.teleport.RelativeFlag;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerPositionAndLook;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerRotation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerVehicleMove;
import ac.grim.grimac.utils.data.Pair;
import ac.grim.grimac.utils.data.RotationData;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.math.Location;
import java.util.List;
import java.util.Objects;

public class PacketServerTeleport extends PacketListenerAbstract {
   private static final boolean STUPID_TELEPORT_SYSTEM;

   public PacketServerTeleport() {
      super(PacketListenerPriority.LOW);
   }

   public void onPacketSend(PacketSendEvent event) {
      List var10000;
      GrimPlayer player;
      if (event.getPacketType() == PacketType.Play.Server.PLAYER_POSITION_AND_LOOK) {
         player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
         if (player == null) {
            return;
         }

         WrapperPlayServerPlayerPositionAndLook teleport = new WrapperPlayServerPlayerPositionAndLook(event);
         Vector3d pos = new Vector3d(teleport.getX(), teleport.getY(), teleport.getZ());
         if (player.getSetbackTeleportUtil().getRequiredSetBack() == null) {
            player.x = teleport.getX();
            player.y = teleport.getY();
            player.z = teleport.getZ();
            player.yaw = teleport.getYaw();
            player.pitch = teleport.getPitch();
            player.lastX = teleport.getX();
            player.lastY = teleport.getY();
            player.lastZ = teleport.getZ();
            player.lastYaw = teleport.getYaw();
            player.lastPitch = teleport.getPitch();
            player.pollData();
         }

         boolean relativeDeltaX;
         boolean relativeDeltaY;
         boolean relativeDeltaZ;
         if (player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_8) || player.inVehicle()) {
            relativeDeltaX = teleport.isRelativeFlag(RelativeFlag.X);
            relativeDeltaY = teleport.isRelativeFlag(RelativeFlag.Y);
            relativeDeltaZ = teleport.isRelativeFlag(RelativeFlag.Z);
            if (relativeDeltaX) {
               pos = pos.add(new Vector3d(player.x, 0.0D, 0.0D));
               teleport.setRelative(RelativeFlag.X, false);
            }

            if (relativeDeltaY) {
               pos = pos.add(new Vector3d(0.0D, player.y, 0.0D));
               teleport.setRelative(RelativeFlag.Y, false);
            }

            if (relativeDeltaZ) {
               pos = pos.add(new Vector3d(0.0D, 0.0D, player.z));
               teleport.setRelative(RelativeFlag.Z, false);
            }

            if (relativeDeltaX || relativeDeltaY || relativeDeltaZ) {
               teleport.setX(pos.getX());
               teleport.setY(pos.getY());
               teleport.setZ(pos.getZ());
               event.markForReEncode(true);
            }
         }

         if (STUPID_TELEPORT_SYSTEM && player.inVehicle()) {
            relativeDeltaX = teleport.isRelativeFlag(RelativeFlag.DELTA_X);
            relativeDeltaY = teleport.isRelativeFlag(RelativeFlag.DELTA_Y);
            relativeDeltaZ = teleport.isRelativeFlag(RelativeFlag.DELTA_Z);
            if (relativeDeltaX) {
               teleport.setRelative(RelativeFlag.DELTA_X, false);
            }

            if (relativeDeltaY) {
               teleport.setRelative(RelativeFlag.DELTA_Y, false);
            }

            if (relativeDeltaZ) {
               teleport.setRelative(RelativeFlag.DELTA_Z, false);
            }

            if (relativeDeltaX || relativeDeltaY || relativeDeltaZ) {
               teleport.setDeltaMovement(Vector3d.zero());
               event.markForReEncode(true);
            }
         }

         player.sendTransaction();
         int lastTransactionSent = player.lastTransactionSent.get();
         var10000 = event.getTasksAfterSend();
         Objects.requireNonNull(player);
         var10000.add(player::sendTransaction);
         if (teleport.isDismountVehicle()) {
            event.getTasksAfterSend().add(() -> {
               player.compensatedEntities.self.eject();
            });
         }

         if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_8)) {
            pos = pos.withY(pos.getY() - 1.62D);
         }

         Location target = new Location((PlatformWorld)null, pos.getX(), pos.getY(), pos.getZ());
         player.getSetbackTeleportUtil().addSentTeleport(target, teleport.getDeltaMovement(), lastTransactionSent, teleport.getRelativeFlags(), true, teleport.getTeleportId());
      }

      if (event.getPacketType() == PacketType.Play.Server.PLAYER_ROTATION) {
         player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
         if (player == null) {
            return;
         }

         WrapperPlayServerPlayerRotation packet = new WrapperPlayServerPlayerRotation(event);
         if (!Float.isFinite(packet.getPitch())) {
            packet.setPitch(0.0F);
            event.markForReEncode(true);
         }

         if (!Float.isFinite(packet.getYaw())) {
            packet.setYaw(0.0F);
            event.markForReEncode(true);
         }

         player.sendTransaction();
         player.pendingRotations.add(new RotationData(packet.getYaw(), GrimMath.clamp(packet.getPitch() % 360.0F, -90.0F, 90.0F), player.getLastTransactionSent()));
         var10000 = event.getTasksAfterSend();
         Objects.requireNonNull(player);
         var10000.add(player::sendTransaction);
      }

      if (event.getPacketType() == PacketType.Play.Server.VEHICLE_MOVE) {
         player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
         if (player == null) {
            return;
         }

         player.sendTransaction();
         var10000 = event.getTasksAfterSend();
         Objects.requireNonNull(player);
         var10000.add(player::sendTransaction);
         player.vehicleData.vehicleTeleports.add(new Pair(player.lastTransactionSent.get(), (new WrapperPlayServerVehicleMove(event)).getPosition()));
      }

   }

   static {
      STUPID_TELEPORT_SYSTEM = PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_21_2);
   }
}
