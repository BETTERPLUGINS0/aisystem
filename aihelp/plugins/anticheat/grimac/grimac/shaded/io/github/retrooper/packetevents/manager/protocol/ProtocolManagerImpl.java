package ac.grim.grimac.shaded.io.github.retrooper.packetevents.manager.protocol;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.ProtocolVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.protocolsupport.ProtocolSupportUtil;
import io.netty.buffer.ByteBuf;
import java.util.List;

public class ProtocolManagerImpl implements ProtocolManager {
   private ProtocolVersion platformVersion;

   private ProtocolVersion resolveVersionNoCache() {
      return ProtocolVersion.UNKNOWN;
   }

   public ProtocolVersion getPlatformVersion() {
      if (this.platformVersion == null) {
         this.platformVersion = this.resolveVersionNoCache();
      }

      return this.platformVersion;
   }

   public void sendPacket(Object channel, Object byteBuf) {
      if (ChannelHelper.isOpen(channel)) {
         if (ProtocolSupportUtil.isAvailable() && byteBuf instanceof ByteBuf) {
            ((ByteBuf)byteBuf).retain();
         }

         ChannelHelper.writeAndFlush(channel, byteBuf);
      } else {
         ((ByteBuf)byteBuf).release();
      }

   }

   public void sendPacketSilently(Object channel, Object byteBuf) {
      if (ChannelHelper.isOpen(channel)) {
         ChannelHelper.writeAndFlushInContext(channel, PacketEvents.ENCODER_NAME, byteBuf);
      } else {
         ((ByteBuf)byteBuf).release();
      }

   }

   public void writePacket(Object channel, Object byteBuf) {
      if (ChannelHelper.isOpen(channel)) {
         if (ProtocolSupportUtil.isAvailable() && byteBuf instanceof ByteBuf) {
            ((ByteBuf)byteBuf).retain();
         }

         ChannelHelper.write(channel, byteBuf);
      } else {
         ((ByteBuf)byteBuf).release();
      }

   }

   public void writePacketSilently(Object channel, Object byteBuf) {
      if (ChannelHelper.isOpen(channel)) {
         ChannelHelper.writeInContext(channel, PacketEvents.ENCODER_NAME, byteBuf);
      } else {
         ((ByteBuf)byteBuf).release();
      }

   }

   public void receivePacket(Object channel, Object byteBuf) {
      if (ChannelHelper.isOpen(channel)) {
         List<String> handlerNames = ChannelHelper.pipelineHandlerNames(channel);
         if (handlerNames.contains("via-encoder")) {
            ChannelHelper.fireChannelReadInContext(channel, "via-decoder", byteBuf);
         } else if (handlerNames.contains("ps_decoder_transformer")) {
            ChannelHelper.fireChannelReadInContext(channel, "ps_decoder_transformer", byteBuf);
         } else if (handlerNames.contains("decompress")) {
            ChannelHelper.fireChannelReadInContext(channel, "decompress", byteBuf);
         } else if (handlerNames.contains("decrypt")) {
            ChannelHelper.fireChannelReadInContext(channel, "decrypt", byteBuf);
         } else {
            ChannelHelper.fireChannelReadInContext(channel, "splitter", byteBuf);
         }
      } else {
         ((ByteBuf)byteBuf).release();
      }

   }

   public void receivePacketSilently(Object channel, Object byteBuf) {
      if (ChannelHelper.isOpen(channel)) {
         ChannelHelper.fireChannelReadInContext(channel, PacketEvents.DECODER_NAME, byteBuf);
      } else {
         ((ByteBuf)byteBuf).release();
      }

   }

   public ClientVersion getClientVersion(Object channel) {
      User user = this.getUser(channel);
      return user.getClientVersion() == null ? ClientVersion.UNKNOWN : user.getClientVersion();
   }
}
