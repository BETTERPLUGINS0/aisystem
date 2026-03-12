package ac.grim.grimac.events.packets;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.platform.api.player.PlatformPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerAbstract;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.UserConnectEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.UserDisconnectEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.UserLoginEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.ConnectionState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.utils.anticheat.LogUtil;
import java.util.Objects;

public class PacketPlayerJoinQuit extends PacketListenerAbstract {
   public void onPacketSend(PacketSendEvent event) {
      if (event.getPacketType() == PacketType.Login.Server.LOGIN_SUCCESS) {
         event.getTasksAfterSend().add(() -> {
            GrimAPI.INSTANCE.getPlayerDataManager().addUser(event.getUser());
         });
      }

   }

   public void onUserConnect(UserConnectEvent event) {
      if (event.getUser().getConnectionState() == ConnectionState.PLAY && !GrimAPI.INSTANCE.getPlayerDataManager().exemptUsers.contains(event.getUser())) {
         event.setCancelled(true);
      }

   }

   public void onUserLogin(UserLoginEvent event) {
      Object nativePlayerObject = Objects.requireNonNull(event.getPlayer());
      PlatformPlayer platformPlayer = GrimAPI.INSTANCE.getPlatformPlayerFactory().getFromNativePlayerType(nativePlayerObject);
      if (GrimAPI.INSTANCE.getConfigManager().getConfig().getBooleanElse("debug-pipeline-on-join", false)) {
         LogUtil.info("Pipeline: " + ChannelHelper.pipelineHandlerNamesAsString(event.getUser().getChannel()));
      }

      if (platformPlayer.hasPermission("grim.alerts.enable-on-join") && platformPlayer.hasPermission("grim.alerts")) {
         GrimAPI.INSTANCE.getAlertManager().toggleAlerts(platformPlayer, platformPlayer.hasPermission("grim.alerts.enable-on-join.silent"));
      }

      if (platformPlayer.hasPermission("grim.verbose.enable-on-join") && platformPlayer.hasPermission("grim.verbose")) {
         GrimAPI.INSTANCE.getAlertManager().toggleVerbose(platformPlayer, platformPlayer.hasPermission("grim.verbose.enable-on-join.silent"));
      }

      if (platformPlayer.hasPermission("grim.brand.enable-on-join") && platformPlayer.hasPermission("grim.brand")) {
         GrimAPI.INSTANCE.getAlertManager().toggleBrands(platformPlayer, platformPlayer.hasPermission("grim.brand.enable-on-join.silent"));
      }

      if (platformPlayer.hasPermission("grim.spectate") && GrimAPI.INSTANCE.getConfigManager().getConfig().getBooleanElse("spectators.hide-regardless", false)) {
         GrimAPI.INSTANCE.getSpectateManager().onLogin(platformPlayer.getUniqueId());
      }

   }

   public void onUserDisconnect(UserDisconnectEvent event) {
      GrimAPI.INSTANCE.getPlayerDataManager().onDisconnect(event.getUser());
   }
}
