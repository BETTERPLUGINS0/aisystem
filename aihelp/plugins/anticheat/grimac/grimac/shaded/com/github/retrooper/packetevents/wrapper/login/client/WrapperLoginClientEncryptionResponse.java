package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.login.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.crypto.MinecraftEncryptionUtil;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.crypto.SaltSignature;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Optional;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class WrapperLoginClientEncryptionResponse extends PacketWrapper<WrapperLoginClientEncryptionResponse> {
   private byte[] encryptedSharedSecret;
   private byte[] encryptedVerifyToken;
   private SaltSignature saltSignature;

   public WrapperLoginClientEncryptionResponse(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperLoginClientEncryptionResponse(ClientVersion clientVersion, byte[] encryptedSharedSecret, byte[] encryptedVerifyToken) {
      super(PacketType.Login.Client.ENCRYPTION_RESPONSE.getId(), clientVersion);
      this.encryptedSharedSecret = encryptedSharedSecret;
      this.encryptedVerifyToken = encryptedVerifyToken;
   }

   public WrapperLoginClientEncryptionResponse(ClientVersion clientVersion, SaltSignature saltSignature) {
      super(PacketType.Login.Client.ENCRYPTION_RESPONSE.getId(), clientVersion);
      this.saltSignature = saltSignature;
   }

   public void read() {
      this.encryptedSharedSecret = this.readByteArray(ByteBufHelper.readableBytes(this.buffer));
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19) && this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_19_2) && !this.readBoolean()) {
         this.saltSignature = this.readSaltSignature();
      } else {
         this.encryptedVerifyToken = this.readByteArray();
      }

   }

   public void write() {
      this.writeByteArray(this.encryptedSharedSecret);
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19) && this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_19_2)) {
         this.writeBoolean(this.saltSignature == null);
         if (this.saltSignature != null) {
            this.writeSaltSignature(this.saltSignature);
         } else {
            this.writeByteArray(this.encryptedVerifyToken);
         }
      } else {
         this.writeByteArray(this.encryptedVerifyToken);
      }

   }

   public void copy(WrapperLoginClientEncryptionResponse wrapper) {
      this.encryptedSharedSecret = wrapper.encryptedSharedSecret;
      this.encryptedVerifyToken = wrapper.encryptedVerifyToken;
      this.saltSignature = wrapper.saltSignature;
   }

   public byte[] getEncryptedSharedSecret() {
      return this.encryptedSharedSecret;
   }

   public void setEncryptedSharedSecret(byte[] encryptedSharedSecret) {
      this.encryptedSharedSecret = encryptedSharedSecret;
   }

   public SecretKey getSecretKey(PrivateKey key) {
      byte[] data = this.getEncryptedSharedSecret();
      byte[] decryptedData = MinecraftEncryptionUtil.decrypt(key.getAlgorithm(), key, data);
      return decryptedData != null ? new SecretKeySpec(decryptedData, "AES") : null;
   }

   public void setSharedKey(SecretKey key, PublicKey publicKey) {
      this.encryptedSharedSecret = MinecraftEncryptionUtil.encrypt(publicKey.getAlgorithm(), publicKey, key.getEncoded());
   }

   public Optional<byte[]> getEncryptedVerifyToken() {
      return Optional.ofNullable(this.encryptedVerifyToken);
   }

   public void setEncryptedVerifyToken(byte[] encryptedVerifyToken) {
      this.encryptedVerifyToken = encryptedVerifyToken;
   }

   public Optional<SaltSignature> getSaltSignature() {
      return Optional.ofNullable(this.saltSignature);
   }

   public void setSaltSignature(@Nullable SaltSignature saltSignature) {
      this.saltSignature = saltSignature;
   }
}
