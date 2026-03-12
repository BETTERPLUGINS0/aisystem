package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Optional;
import java.util.UUID;

public class WrapperPlayServerPlayerChatHeader extends PacketWrapper<WrapperPlayServerPlayerChatHeader> {
   @Nullable
   private byte[] previousSignature;
   private UUID playerUUID;
   private byte[] signature;
   private byte[] hash;

   public WrapperPlayServerPlayerChatHeader(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerPlayerChatHeader(@Nullable byte[] previousSignature, UUID playerUUID, byte[] signature, byte[] hash) {
      super((PacketTypeCommon)PacketType.Play.Server.PLAYER_CHAT_HEADER);
      this.previousSignature = previousSignature;
      this.playerUUID = playerUUID;
      this.signature = signature;
      this.hash = hash;
   }

   public void read() {
      this.previousSignature = (byte[])this.readOptional(PacketWrapper::readByteArray);
      this.playerUUID = this.readUUID();
      this.signature = this.readByteArray();
      this.hash = this.readByteArray();
   }

   public void write() {
      this.writeOptional(this.previousSignature, PacketWrapper::writeByteArray);
      this.writeUUID(this.playerUUID);
      this.writeByteArray(this.signature);
      this.writeByteArray(this.hash);
   }

   public void copy(WrapperPlayServerPlayerChatHeader wrapper) {
      this.previousSignature = wrapper.previousSignature;
      this.playerUUID = wrapper.playerUUID;
      this.signature = wrapper.signature;
      this.hash = wrapper.hash;
   }

   public Optional<byte[]> getPreviousSignature() {
      return Optional.ofNullable(this.previousSignature);
   }

   public void setPreviousSignature(@Nullable byte[] previousSignature) {
      this.previousSignature = previousSignature;
   }

   public UUID getPlayerUUID() {
      return this.playerUUID;
   }

   public void setPlayerUUID(UUID playerUUID) {
      this.playerUUID = playerUUID;
   }

   public byte[] getSignature() {
      return this.signature;
   }

   public void setSignature(byte[] signature) {
      this.signature = signature;
   }

   public byte[] getHash() {
      return this.hash;
   }

   public void setHash(byte[] hash) {
      this.hash = hash;
   }
}
