package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.ChatType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.ChatTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;

public class WrapperPlayServerSystemChatMessage extends PacketWrapper<WrapperPlayServerSystemChatMessage> {
   /** @deprecated */
   @Deprecated
   public static boolean HANDLE_JSON = true;
   @Nullable
   private ChatType type;
   private boolean overlay;
   private Component message;

   public WrapperPlayServerSystemChatMessage(PacketSendEvent event) {
      super(event);
   }

   /** @deprecated */
   @Deprecated
   public WrapperPlayServerSystemChatMessage(@NotNull ChatType type, Component message) {
      super((PacketTypeCommon)PacketType.Play.Server.SYSTEM_CHAT_MESSAGE);
      this.type = type;
      if (type == ChatTypes.GAME_INFO) {
         this.overlay = true;
      }

      this.message = message;
   }

   /** @deprecated */
   @Deprecated
   public WrapperPlayServerSystemChatMessage(@NotNull ChatType type, String messageJson) {
      super((PacketTypeCommon)PacketType.Play.Server.SYSTEM_CHAT_MESSAGE);
      this.message = this.getSerializers().fromJson(messageJson);
      this.type = type;
      if (type == ChatTypes.GAME_INFO) {
         this.overlay = true;
      }

   }

   public WrapperPlayServerSystemChatMessage(boolean overlay, Component message) {
      super((PacketTypeCommon)PacketType.Play.Server.SYSTEM_CHAT_MESSAGE);
      this.message = message;
      this.overlay = overlay;
      this.type = overlay ? ChatTypes.GAME_INFO : ChatTypes.SYSTEM;
   }

   /** @deprecated */
   @Deprecated
   public WrapperPlayServerSystemChatMessage(boolean overlay, String messageJson) {
      super((PacketTypeCommon)PacketType.Play.Server.SYSTEM_CHAT_MESSAGE);
      this.message = this.getSerializers().fromJson(messageJson);
      this.overlay = overlay;
      this.type = overlay ? ChatTypes.GAME_INFO : ChatTypes.SYSTEM;
   }

   public void read() {
      this.message = this.readComponent();
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_1)) {
         this.overlay = this.readBoolean();
      } else {
         this.type = (ChatType)this.readMappedEntity(ChatTypes.getRegistry());
      }

   }

   public void write() {
      this.writeComponent(this.message);
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_1)) {
         this.writeBoolean(this.overlay);
      } else if (this.type == null) {
         if (this.overlay) {
            this.writeMappedEntity(ChatTypes.GAME_INFO);
         } else {
            this.writeMappedEntity(ChatTypes.SYSTEM);
         }
      } else {
         this.writeMappedEntity(this.type);
      }

   }

   public void copy(WrapperPlayServerSystemChatMessage wrapper) {
      this.type = wrapper.type;
      this.overlay = wrapper.overlay;
      this.message = wrapper.message;
   }

   /** @deprecated */
   @Deprecated
   @Nullable
   public ChatType getType() {
      return this.type;
   }

   /** @deprecated */
   @Deprecated
   public void setType(@Nullable ChatType type) {
      this.type = type;
   }

   /** @deprecated */
   @Deprecated
   public String getMessageJson() {
      return this.getSerializers().asJson(this.getMessage());
   }

   /** @deprecated */
   @Deprecated
   public void setMessageJson(String messageJson) {
      this.setMessage(this.getSerializers().fromJson(messageJson));
   }

   public Component getMessage() {
      return this.message;
   }

   public void setMessage(Component message) {
      this.message = message;
   }

   public boolean isOverlay() {
      return this.overlay;
   }

   public void setOverlay(boolean overlay) {
      this.overlay = overlay;
   }
}
