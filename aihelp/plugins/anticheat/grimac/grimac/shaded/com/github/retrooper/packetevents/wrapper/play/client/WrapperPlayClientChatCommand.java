package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.LastSeenMessages;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.SignedCommandArgument;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.crypto.MessageSignData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.crypto.SaltSignature;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.time.Instant;
import java.util.List;

public class WrapperPlayClientChatCommand extends PacketWrapper<WrapperPlayClientChatCommand> {
   private String command;
   private MessageSignData messageSignData;
   private List<SignedCommandArgument> signedArguments;
   @Nullable
   private LastSeenMessages.Update lastSeenMessages;
   @Nullable
   private LastSeenMessages.LegacyUpdate legacyLastSeenMessages;

   public WrapperPlayClientChatCommand(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientChatCommand(String command, MessageSignData messageSignData, List<SignedCommandArgument> signedArguments, @Nullable LastSeenMessages.LegacyUpdate lastSeenMessages) {
      super((PacketTypeCommon)PacketType.Play.Client.CHAT_COMMAND);
      this.command = command;
      this.messageSignData = messageSignData;
      this.signedArguments = signedArguments;
      this.legacyLastSeenMessages = lastSeenMessages;
   }

   public WrapperPlayClientChatCommand(String command, MessageSignData messageSignData, List<SignedCommandArgument> signedArguments, @Nullable LastSeenMessages.Update lastSeenMessages) {
      super((PacketTypeCommon)PacketType.Play.Client.CHAT_COMMAND);
      this.command = command;
      this.messageSignData = messageSignData;
      this.signedArguments = signedArguments;
      this.lastSeenMessages = lastSeenMessages;
   }

   public void read() {
      this.command = this.readString(256);
      Instant timestamp = this.readTimestamp();
      long salt = this.readLong();
      this.messageSignData = new MessageSignData(new SaltSignature(salt, new byte[0]), timestamp);
      this.signedArguments = this.readSignedCommandArguments();
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
         this.lastSeenMessages = this.readLastSeenMessagesUpdate();
      } else {
         boolean signedPreview = this.readBoolean();
         this.messageSignData.setSignedPreview(signedPreview);
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_1)) {
            this.legacyLastSeenMessages = this.readLegacyLastSeenMessagesUpdate();
         }
      }

   }

   public void write() {
      this.writeString(this.command, 256);
      this.writeTimestamp(this.messageSignData.getTimestamp());
      this.writeLong(this.messageSignData.getSaltSignature().getSalt());
      this.writeSignedCommandArguments(this.signedArguments);
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
         if (this.lastSeenMessages != null) {
            this.writeLastSeenMessagesUpdate(this.lastSeenMessages);
         }
      } else {
         this.writeBoolean(this.messageSignData.isSignedPreview());
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_1) && this.legacyLastSeenMessages != null) {
            this.writeLegacyLastSeenMessagesUpdate(this.legacyLastSeenMessages);
         }
      }

   }

   public void copy(WrapperPlayClientChatCommand wrapper) {
      this.command = wrapper.command;
      this.messageSignData = wrapper.messageSignData;
      this.signedArguments = wrapper.signedArguments;
      this.lastSeenMessages = wrapper.lastSeenMessages;
      this.legacyLastSeenMessages = wrapper.legacyLastSeenMessages;
   }

   public String getCommand() {
      return this.command;
   }

   public void setCommand(String command) {
      this.command = command;
   }

   public MessageSignData getMessageSignData() {
      return this.messageSignData;
   }

   public void setMessageSignData(MessageSignData messageSignData) {
      this.messageSignData = messageSignData;
   }

   public List<SignedCommandArgument> getSignedArguments() {
      return this.signedArguments;
   }

   public void setSignedArguments(List<SignedCommandArgument> signedArguments) {
      this.signedArguments = signedArguments;
   }

   @Nullable
   public LastSeenMessages.Update getLastSeenMessages() {
      return this.lastSeenMessages;
   }

   public void setLastSeenMessages(@Nullable LastSeenMessages.Update lastSeenMessages) {
      this.lastSeenMessages = lastSeenMessages;
   }

   @Nullable
   public LastSeenMessages.LegacyUpdate getLegacyLastSeenMessages() {
      return this.legacyLastSeenMessages;
   }

   public void setLegacyLastSeenMessages(@Nullable LastSeenMessages.LegacyUpdate lastSeenMessages) {
      this.legacyLastSeenMessages = lastSeenMessages;
   }
}
