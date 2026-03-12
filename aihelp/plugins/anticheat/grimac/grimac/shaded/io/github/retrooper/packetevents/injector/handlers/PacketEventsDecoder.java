package ac.grim.grimac.shaded.io.github.retrooper.packetevents.injector.handlers;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.exception.PacketProcessException;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.ConnectionState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.ExceptionUtil;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDisconnect;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.injector.connection.ServerConnectionInitializer;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.viaversion.ViaVersionUtil;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PacketEventsDecoder extends MessageToMessageDecoder<ByteBuf> {
   public User user;
   public Player player;
   public boolean hasBeenRelocated;
   public boolean preViaVersion;

   public PacketEventsDecoder(User user, boolean preViaVersion) {
      this.user = user;
      this.preViaVersion = preViaVersion;
   }

   public PacketEventsDecoder(PacketEventsDecoder decoder) {
      this.user = decoder.user;
      this.player = decoder.player;
      this.hasBeenRelocated = decoder.hasBeenRelocated;
      this.preViaVersion = decoder.preViaVersion;
   }

   public void read(ChannelHandlerContext ctx, ByteBuf input, List<Object> out) throws Exception {
      try {
         if (!this.preViaVersion && PacketEvents.getAPI().getSettings().isPreViaInjection() && !ViaVersionUtil.isAvailable()) {
            PacketEventsImplHelper.handleServerBoundPacket(ctx.channel(), this.user, this.player, input, this.preViaVersion);
         }

         PacketEventsImplHelper.handleServerBoundPacket(ctx.channel(), this.user, this.player, input, !this.preViaVersion);
         out.add(ByteBufHelper.retain(input));
      } catch (Throwable var5) {
         if (ExceptionUtil.isException(var5, PacketProcessException.class)) {
            throw var5;
         } else {
            throw new PacketProcessException(var5);
         }
      }
   }

   public void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
      if (buffer.isReadable()) {
         this.read(ctx, buffer, out);
      }

   }

   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      if (!ExceptionUtil.isException(cause, PacketProcessException.class)) {
         super.exceptionCaught(ctx, cause);
      } else {
         boolean debug = PacketEvents.getAPI().getSettings().isDebugEnabled() || SpigotReflectionUtil.isMinecraftServerInstanceDebugging();
         String username;
         if (debug || this.user != null && this.user.getDecoderState() != ConnectionState.HANDSHAKING) {
            if (PacketEvents.getAPI().getSettings().isFullStackTraceEnabled()) {
               username = this.user != null ? this.user.getDecoderState().name() : "null";
               String clientVersion = this.user != null ? this.user.getClientVersion().getReleaseName() : "null";
               String username = this.user != null && this.user.getProfile().getName() != null ? this.user.getProfile().getName() : (this.player != null ? this.player.getName() : "null");
               PacketEvents.getAPI().getLogger().log(Level.WARNING, cause, () -> {
                  return "An error occurred while processing a packet from " + username + " (state: " + username + ", clientVersion: " + clientVersion + ", serverVersion: " + PacketEvents.getAPI().getServerManager().getVersion().getReleaseName() + ", preVia: " + this.preViaVersion + ")";
               });
            } else {
               PacketEvents.getAPI().getLogManager().warn(cause.getMessage());
            }
         }

         if (PacketEvents.getAPI().getSettings().isKickOnPacketExceptionEnabled()) {
            try {
               if (this.user != null) {
                  this.user.sendPacket((PacketWrapper)(new WrapperPlayServerDisconnect(Component.text("Invalid packet"))));
               }
            } catch (Exception var7) {
            }

            ctx.channel().close();
            if (this.player != null) {
               FoliaScheduler.getEntityScheduler().runDelayed(this.player, (Plugin)PacketEvents.getAPI().getPlugin(), (o) -> {
                  this.player.kickPlayer("Invalid packet");
               }, (Runnable)null, 1L);
            }

            username = this.user != null && this.user.getProfile().getName() != null ? this.user.getProfile().getName() : (this.player != null ? this.player.getName() : "null");
            PacketEvents.getAPI().getLogManager().warn("Disconnected " + username + " due to an invalid packet!");
         }

      }
   }

   public void userEventTriggered(final ChannelHandlerContext ctx, final Object event) throws Exception {
      if (PacketEventsEncoder.COMPRESSION_ENABLED_EVENT != null && event == PacketEventsEncoder.COMPRESSION_ENABLED_EVENT) {
         if (!this.preViaVersion) {
            ServerConnectionInitializer.relocateHandlers(ctx.channel(), this.user, false, true);
            if (PacketEvents.getAPI().getSettings().isPreViaInjection() && ViaVersionUtil.isAvailable()) {
               ServerConnectionInitializer.relocateHandlers(ctx.channel(), this.user, true, true);
            }
         }

         super.userEventTriggered(ctx, event);
      } else {
         super.userEventTriggered(ctx, event);
      }
   }
}
