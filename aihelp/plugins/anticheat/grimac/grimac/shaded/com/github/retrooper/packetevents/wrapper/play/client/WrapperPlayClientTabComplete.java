package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Optional;

public class WrapperPlayClientTabComplete extends PacketWrapper<WrapperPlayClientTabComplete> {
   private Optional<Integer> transactionId;
   private boolean assumeCommand;
   private String text;
   @Nullable
   private Vector3i blockPosition;

   public WrapperPlayClientTabComplete(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientTabComplete(int transactionId, String text, @Nullable Vector3i blockPosition) {
      super((PacketTypeCommon)PacketType.Play.Client.TAB_COMPLETE);
      this.transactionId = Optional.of(transactionId);
      this.assumeCommand = true;
      this.text = text;
      this.blockPosition = blockPosition;
   }

   /** @deprecated */
   @Deprecated
   public WrapperPlayClientTabComplete(String text, boolean assumeCommand, @Nullable Vector3i blockPosition) {
      super((PacketTypeCommon)PacketType.Play.Client.TAB_COMPLETE);
      this.transactionId = Optional.empty();
      this.text = text;
      this.assumeCommand = assumeCommand;
      this.blockPosition = blockPosition;
   }

   public void read() {
      boolean v1_13 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13);
      short textLength;
      if (v1_13) {
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13_1)) {
            textLength = 32500;
         } else {
            textLength = 256;
         }

         this.transactionId = Optional.of(this.readVarInt());
         this.text = this.readString(textLength);
      } else {
         this.transactionId = Optional.empty();
         textLength = 32767;
         this.text = this.readString(textLength);
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            this.assumeCommand = this.readBoolean();
         }

         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            this.blockPosition = (Vector3i)this.readOptional(PacketWrapper::readBlockPosition);
         }
      }

   }

   public void write() {
      boolean v1_13 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13);
      short textLength;
      if (v1_13) {
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13_1)) {
            textLength = 32500;
         } else {
            textLength = 256;
         }

         this.writeVarInt((Integer)this.transactionId.orElse(0));
         this.writeString(this.text, textLength);
      } else {
         textLength = 32767;
         this.writeString(this.text, textLength);
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            this.writeBoolean(this.assumeCommand);
         }

         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            this.writeOptional(this.blockPosition, PacketWrapper::writeBlockPosition);
         }
      }

   }

   public void copy(WrapperPlayClientTabComplete wrapper) {
      this.text = wrapper.text;
      this.assumeCommand = wrapper.assumeCommand;
      this.transactionId = wrapper.transactionId;
      this.blockPosition = wrapper.blockPosition;
   }

   public String getText() {
      return this.text;
   }

   public void setText(String text) {
      this.text = text;
   }

   public Optional<Integer> getTransactionId() {
      return this.transactionId;
   }

   public void setTransactionId(@Nullable Integer transactionID) {
      this.transactionId = Optional.ofNullable(transactionID);
   }

   /** @deprecated */
   @Deprecated
   public boolean isAssumeCommand() {
      return this.assumeCommand;
   }

   /** @deprecated */
   @Deprecated
   public void setAssumeCommand(boolean assumeCommand) {
      this.assumeCommand = assumeCommand;
   }

   public Optional<Vector3i> getBlockPosition() {
      return Optional.ofNullable(this.blockPosition);
   }

   public void setBlockPosition(@Nullable Vector3i blockPosition) {
      this.blockPosition = blockPosition;
   }
}
