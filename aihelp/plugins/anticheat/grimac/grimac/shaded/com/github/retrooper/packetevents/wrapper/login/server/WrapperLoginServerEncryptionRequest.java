package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.login.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.crypto.MinecraftEncryptionUtil;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.security.PublicKey;

public class WrapperLoginServerEncryptionRequest extends PacketWrapper<WrapperLoginServerEncryptionRequest> {
   private String serverID;
   private byte[] publicKeyBytes;
   private byte[] verifyToken;
   private boolean shouldAuthenticate;

   public WrapperLoginServerEncryptionRequest(PacketSendEvent event) {
      super(event);
   }

   public WrapperLoginServerEncryptionRequest(String serverID, PublicKey publicKey, byte[] verifyToken) {
      this(serverID, publicKey.getEncoded(), verifyToken);
   }

   public WrapperLoginServerEncryptionRequest(String serverID, PublicKey publicKey, byte[] verifyToken, boolean shouldAuthenticate) {
      this(serverID, publicKey.getEncoded(), verifyToken, shouldAuthenticate);
   }

   public WrapperLoginServerEncryptionRequest(String serverID, byte[] publicKeyBytes, byte[] verifyToken) {
      this(serverID, publicKeyBytes, verifyToken, true);
   }

   public WrapperLoginServerEncryptionRequest(String serverID, byte[] publicKeyBytes, byte[] verifyToken, boolean shouldAuthenticate) {
      super((PacketTypeCommon)PacketType.Login.Server.ENCRYPTION_REQUEST);
      this.serverID = serverID;
      this.publicKeyBytes = publicKeyBytes;
      this.verifyToken = verifyToken;
      this.shouldAuthenticate = shouldAuthenticate;
   }

   public void read() {
      this.serverID = this.readString(20);
      this.publicKeyBytes = this.readByteArray(512);
      this.verifyToken = this.readByteArray(ByteBufHelper.readableBytes(this.buffer));
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
         this.shouldAuthenticate = this.readBoolean();
      }

   }

   public void write() {
      this.writeString(this.serverID, 20);
      this.writeByteArray(this.publicKeyBytes);
      this.writeByteArray(this.verifyToken);
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
         this.writeBoolean(this.shouldAuthenticate);
      }

   }

   public void copy(WrapperLoginServerEncryptionRequest wrapper) {
      this.serverID = wrapper.serverID;
      this.publicKeyBytes = wrapper.publicKeyBytes;
      this.verifyToken = wrapper.verifyToken;
      this.shouldAuthenticate = wrapper.shouldAuthenticate;
   }

   public String getServerId() {
      return this.serverID;
   }

   public void setServerId(String serverID) {
      this.serverID = serverID;
   }

   public byte[] getPublicKeyBytes() {
      return this.publicKeyBytes;
   }

   public void setPublicKeyBytes(byte[] publicKeyBytes) {
      this.publicKeyBytes = publicKeyBytes;
   }

   public PublicKey getPublicKey() {
      return MinecraftEncryptionUtil.publicKey(this.publicKeyBytes);
   }

   public void setPublicKey(PublicKey publicKey) {
      this.publicKeyBytes = publicKey.getEncoded();
   }

   public byte[] getVerifyToken() {
      return this.verifyToken;
   }

   public void setVerifyToken(byte[] verifyToken) {
      this.verifyToken = verifyToken;
   }

   public boolean isShouldAuthenticate() {
      return this.shouldAuthenticate;
   }

   public void setShouldAuthenticate(boolean shouldAuthenticate) {
      this.shouldAuthenticate = shouldAuthenticate;
   }
}
