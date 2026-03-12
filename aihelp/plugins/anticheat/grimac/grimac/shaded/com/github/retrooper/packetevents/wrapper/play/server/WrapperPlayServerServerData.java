package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

public class WrapperPlayServerServerData extends PacketWrapper<WrapperPlayServerServerData> {
   private static final String BASE64_IMAGE_HEADER = "data:image/png;base64,";
   @Nullable
   private Component motd;
   @Nullable
   private String icon;
   private boolean previewsChat;
   private boolean enforceSecureChat;

   public WrapperPlayServerServerData(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerServerData(@Nullable Component motd, @Nullable String icon, boolean previewsChat) {
      this(motd, icon, previewsChat, false);
   }

   public WrapperPlayServerServerData(@Nullable Component motd, @Nullable String icon, boolean previewsChat, boolean enforceSecureChat) {
      super((PacketTypeCommon)PacketType.Play.Server.SERVER_DATA);
      this.motd = motd;
      this.icon = icon;
      this.previewsChat = previewsChat;
      this.enforceSecureChat = enforceSecureChat;
   }

   public void read() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_4) || this.readBoolean()) {
         this.motd = this.readComponent();
      }

      if (this.readBoolean()) {
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_4)) {
            byte[] iconByteArray = this.readByteArray();
            this.icon = "data:image/png;base64," + new String(Base64.getEncoder().encode(iconByteArray), StandardCharsets.UTF_8);
         } else {
            this.icon = this.readString();
         }
      }

      if (this.serverVersion.isOlderThan(ServerVersion.V_1_19_3)) {
         this.previewsChat = this.readBoolean();
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_1) && this.serverVersion.isOlderThan(ServerVersion.V_1_20_5)) {
         this.enforceSecureChat = this.readBoolean();
      }

   }

   public void write() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_4)) {
         this.writeComponent(this.motd);
         byte[] iconByteArray;
         if (this.icon == null) {
            iconByteArray = null;
         } else {
            String iconData = this.icon.substring("data:image/png;base64,".length());
            iconByteArray = Base64.getDecoder().decode(iconData.getBytes(StandardCharsets.UTF_8));
         }

         this.writeOptional(iconByteArray, PacketWrapper::writeByteArray);
      } else {
         this.writeOptional(this.motd, PacketWrapper::writeComponent);
         this.writeOptional(this.icon, PacketWrapper::writeString);
      }

      if (this.serverVersion.isOlderThan(ServerVersion.V_1_19_3)) {
         this.writeBoolean(this.previewsChat);
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_1) && this.serverVersion.isOlderThan(ServerVersion.V_1_20_5)) {
         this.writeBoolean(this.enforceSecureChat);
      }

   }

   public void copy(WrapperPlayServerServerData wrapper) {
      this.motd = wrapper.motd;
      this.icon = wrapper.icon;
      this.previewsChat = wrapper.previewsChat;
      this.enforceSecureChat = wrapper.enforceSecureChat;
   }

   @Nullable
   public Component getMOTD() {
      return this.motd;
   }

   public void setMOTD(@Nullable Component motd) {
      this.motd = motd;
   }

   public Optional<String> getIcon() {
      return Optional.ofNullable(this.icon);
   }

   public void setIcon(@Nullable String icon) {
      this.icon = icon;
   }

   public boolean isPreviewsChat() {
      return this.previewsChat;
   }

   public void setPreviewsChat(boolean previewsChat) {
      this.previewsChat = previewsChat;
   }

   public boolean isEnforceSecureChat() {
      return this.enforceSecureChat;
   }

   public void setEnforceSecureChat(boolean enforceSecureChat) {
      this.enforceSecureChat = enforceSecureChat;
   }
}
