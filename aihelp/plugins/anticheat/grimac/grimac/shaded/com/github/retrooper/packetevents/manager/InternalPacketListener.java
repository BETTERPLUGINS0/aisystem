package ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerAbstract;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerPriority;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.ConnectionState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.UserProfile;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.LogManager;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.SynchronizedRegistriesHandler;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerConfigurationEnd;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerRegistryData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.handshaking.client.WrapperHandshakingClientHandshake;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.login.server.WrapperLoginServerLoginSuccess;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerJoinGame;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerRespawn;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class InternalPacketListener extends PacketListenerAbstract {
   public InternalPacketListener() {
      this(PacketListenerPriority.LOWEST);
   }

   public InternalPacketListener(PacketListenerPriority priority) {
      super(priority);
   }

   public void onPacketSend(PacketSendEvent event) {
      User user = event.getUser();
      if (event.getPacketType() == PacketType.Login.Server.LOGIN_SUCCESS) {
         Object channel = event.getChannel();
         WrapperLoginServerLoginSuccess loginSuccess = new WrapperLoginServerLoginSuccess(event);
         UserProfile profile = loginSuccess.getUserProfile();
         user.getProfile().setUUID(profile.getUUID());
         user.getProfile().setName(profile.getName());
         user.getProfile().setTextureProperties(profile.getTextureProperties());
         synchronized(channel) {
            PacketEvents.getAPI().getProtocolManager().setChannel(profile.getUUID(), channel);
         }

         if (PacketEvents.getAPI().getLogManager().isDebug()) {
            PacketEvents.getAPI().getLogManager().debug("Mapped player UUID with their channel " + profile.getUUID() + " " + channel);
         }

         label67: {
            boolean proxy = PacketEvents.getAPI().getInjector().isProxy();
            if (proxy) {
               if (event.getUser().getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_20_2)) {
                  break label67;
               }
            } else if (event.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20_2)) {
               break label67;
            }

            user.setConnectionState(ConnectionState.PLAY);
            return;
         }

         user.setEncoderState(ConnectionState.CONFIGURATION);
      } else if (event.getPacketType() == PacketType.Configuration.Server.REGISTRY_DATA) {
         WrapperConfigServerRegistryData packet = new WrapperConfigServerRegistryData(event);
         if (packet.getElements() != null) {
            SynchronizedRegistriesHandler.handleRegistry(user, packet, packet.getRegistryKey(), packet.getElements());
         }

         if (packet.getRegistryData() != null) {
            SynchronizedRegistriesHandler.handleLegacyRegistries(user, packet, packet.getRegistryData());
         }
      } else if (event.getPacketType() == PacketType.Play.Server.JOIN_GAME) {
         WrapperPlayServerJoinGame joinGame = new WrapperPlayServerJoinGame(event);
         user.setEntityId(joinGame.getEntityId());
         if (joinGame.getDimensionCodec() != null) {
            SynchronizedRegistriesHandler.handleLegacyRegistries(user, joinGame, joinGame.getDimensionCodec());
            user.finalizeRegistries(joinGame);
         }

         user.setDimensionType(joinGame.getDimensionType());
      } else if (event.getPacketType() == PacketType.Play.Server.RESPAWN) {
         WrapperPlayServerRespawn packet = new WrapperPlayServerRespawn(event);
         user.setDimensionType(packet.getDimensionType());
      } else if (event.getPacketType() == PacketType.Play.Server.CONFIGURATION_START) {
         user.setEncoderState(ConnectionState.CONFIGURATION);
      } else if (event.getPacketType() == PacketType.Configuration.Server.CONFIGURATION_END) {
         user.setEncoderState(ConnectionState.PLAY);
         user.finalizeRegistries(new WrapperConfigServerConfigurationEnd(event));
      }

   }

   public void onPacketReceive(PacketReceiveEvent event) {
      User user = event.getUser();
      if (event.getPacketType() == PacketType.Handshaking.Client.HANDSHAKE) {
         WrapperHandshakingClientHandshake packet = new WrapperHandshakingClientHandshake(event);
         ClientVersion clientVersion = packet.getClientVersion();
         ConnectionState state = packet.getNextConnectionState();
         LogManager logger = PacketEvents.getAPI().getLogManager();
         if (logger.isDebug()) {
            logger.debug("Processed handshake for " + event.getAddress() + ": " + state.name() + " / " + packet.getClientVersion().getReleaseName());
         }

         user.setClientVersion(clientVersion);
         user.setConnectionState(state);
      } else if (event.getPacketType() == PacketType.Login.Client.LOGIN_SUCCESS_ACK) {
         user.setDecoderState(ConnectionState.CONFIGURATION);
      } else if (event.getPacketType() == PacketType.Play.Client.CONFIGURATION_ACK) {
         user.setDecoderState(ConnectionState.CONFIGURATION);
      } else if (event.getPacketType() == PacketType.Configuration.Client.CONFIGURATION_END_ACK) {
         user.setDecoderState(ConnectionState.PLAY);
      }

   }
}
