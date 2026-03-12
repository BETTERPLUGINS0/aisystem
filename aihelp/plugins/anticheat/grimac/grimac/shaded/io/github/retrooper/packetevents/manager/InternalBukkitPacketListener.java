package ac.grim.grimac.shaded.io.github.retrooper.packetevents.manager;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEventsAPI;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.InternalPacketListener;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.ConnectionState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.LogManager;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.handshaking.client.WrapperHandshakingClientHandshake;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.login.server.WrapperLoginServerLoginSuccess;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.injector.SpigotChannelInjector;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.manager.player.PlayerManagerImpl;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.protocolsupport.ProtocolSupportUtil;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.viaversion.ViaVersionUtil;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.lang.ref.WeakReference;
import java.net.SocketAddress;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.Player;

@ApiStatus.Internal
public class InternalBukkitPacketListener extends InternalPacketListener {
   public void onPacketSend(PacketSendEvent event) {
      super.onPacketSend(event);
      if (event.getPacketType() == PacketType.Login.Server.LOGIN_SUCCESS) {
         WrapperLoginServerLoginSuccess packet = new WrapperLoginServerLoginSuccess(event);
         this.tryUpdatePlayerReference(event, event.getUser(), packet.getUserProfile().getUUID());
      } else if (event.getPacketType() == PacketType.Play.Server.JOIN_GAME) {
         this.tryUpdatePlayerReference(event, event.getUser(), event.getUser().getUUID());
      }

   }

   private void tryUpdatePlayerReference(PacketSendEvent event, User user, UUID playerId) {
      PacketEventsAPI<?> api = PacketEvents.getAPI();
      Map<UUID, WeakReference<Player>> map = ((PlayerManagerImpl)api.getPlayerManager()).joiningPlayers;
      WeakReference<Player> playerRef = (WeakReference)map.remove(playerId);
      Player player = playerRef != null ? (Player)playerRef.get() : null;
      if (player != null) {
         ((SpigotChannelInjector)api.getInjector()).updatePlayer(user, player);
         if (api.getLogManager().isDebug()) {
            api.getLogManager().debug("Updated player reference on packet handling for " + player.getUniqueId());
         }

         event.setPlayer(player);
      }

   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Handshaking.Client.HANDSHAKE) {
         User user = event.getUser();
         WrapperHandshakingClientHandshake packet = new WrapperHandshakingClientHandshake(event);
         ClientVersion clientVersion = packet.getClientVersion();
         ConnectionState state = packet.getNextConnectionState();
         String feature;
         if (!this.isPreVia()) {
            if (ViaVersionUtil.isAvailable()) {
               clientVersion = ClientVersion.getById(ViaVersionUtil.getProtocolVersion(user));
               feature = "ViaVersion";
            } else if (ProtocolSupportUtil.isAvailable()) {
               clientVersion = ClientVersion.getById(ProtocolSupportUtil.getProtocolVersion((SocketAddress)user.getAddress()));
               feature = "ProtocolSupport";
            } else {
               feature = null;
            }
         } else {
            feature = "Client Version Handshake";
         }

         LogManager logger = PacketEvents.getAPI().getLogManager();
         if (logger.isDebug()) {
            logger.debug("Processed handshake for " + event.getAddress() + ": " + state.name() + " / " + packet.getClientVersion().getReleaseName() + (feature != null ? " (using " + feature + ")" : ""));
         }

         user.setClientVersion(clientVersion);
         user.setConnectionState(state);
      } else {
         super.onPacketReceive(event);
      }

   }
}
