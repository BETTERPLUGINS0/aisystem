package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.login.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.TextureProperty;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.UserProfile;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Iterator;
import java.util.UUID;

public class WrapperLoginServerLoginSuccess extends PacketWrapper<WrapperLoginServerLoginSuccess> {
   private UserProfile userProfile;
   private boolean strictErrorHandling;

   public WrapperLoginServerLoginSuccess(PacketSendEvent event) {
      super(event);
   }

   public WrapperLoginServerLoginSuccess(UUID uuid, String username) {
      this(new UserProfile(uuid, username));
   }

   public WrapperLoginServerLoginSuccess(UserProfile userProfile) {
      this(userProfile, true);
   }

   public WrapperLoginServerLoginSuccess(UserProfile userProfile, boolean strictErrorHandling) {
      super((PacketTypeCommon)PacketType.Login.Server.LOGIN_SUCCESS);
      this.userProfile = userProfile;
      this.strictErrorHandling = strictErrorHandling;
   }

   public void read() {
      UUID uuid;
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
         uuid = this.readUUID();
      } else {
         uuid = UUID.fromString(this.readString(36));
      }

      String username = this.readString(16);
      this.userProfile = new UserProfile(uuid, username);
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
         int propertyCount = this.readVarInt();

         for(int i = 0; i < propertyCount; ++i) {
            String propertyName = this.readString();
            String propertyValue = this.readString();
            String propertySignature = (String)this.readOptional(PacketWrapper::readString);
            TextureProperty textureProperty = new TextureProperty(propertyName, propertyValue, propertySignature);
            this.userProfile.getTextureProperties().add(textureProperty);
         }
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_5) && this.serverVersion.isOlderThan(ServerVersion.V_1_21_2)) {
         this.strictErrorHandling = this.readBoolean();
      }

   }

   public void write() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
         this.writeUUID(this.userProfile.getUUID());
      } else {
         this.writeString(this.userProfile.getUUID().toString(), 36);
      }

      this.writeString(this.userProfile.getName(), 16);
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
         this.writeVarInt(this.userProfile.getTextureProperties().size());
         Iterator var1 = this.userProfile.getTextureProperties().iterator();

         while(var1.hasNext()) {
            TextureProperty textureProperty = (TextureProperty)var1.next();
            this.writeString(textureProperty.getName());
            this.writeString(textureProperty.getValue());
            this.writeOptional(textureProperty.getSignature(), PacketWrapper::writeString);
         }
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_5) && this.serverVersion.isOlderThan(ServerVersion.V_1_21_2)) {
         this.writeBoolean(this.strictErrorHandling);
      }

   }

   public void copy(WrapperLoginServerLoginSuccess wrapper) {
      this.userProfile = wrapper.userProfile;
   }

   public UserProfile getUserProfile() {
      return this.userProfile;
   }

   public void setUserProfile(UserProfile userProfile) {
      this.userProfile = userProfile;
   }

   @ApiStatus.Obsolete
   public boolean isStrictErrorHandling() {
      return this.strictErrorHandling;
   }

   @ApiStatus.Obsolete
   public void setStrictErrorHandling(boolean strictErrorHandling) {
      this.strictErrorHandling = strictErrorHandling;
   }
}
