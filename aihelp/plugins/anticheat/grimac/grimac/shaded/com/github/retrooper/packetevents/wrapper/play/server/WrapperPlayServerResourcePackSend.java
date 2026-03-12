package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.UUID;

public class WrapperPlayServerResourcePackSend extends PacketWrapper<WrapperPlayServerResourcePackSend> {
   public static final int MAX_HASH_LENGTH = 40;
   private UUID packId;
   private String url;
   private String hash;
   private boolean required;
   private Component prompt;

   public WrapperPlayServerResourcePackSend(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerResourcePackSend(String url, String hash, boolean required, @Nullable Component prompt) {
      this(UUID.randomUUID(), url, hash, required, prompt);
   }

   public WrapperPlayServerResourcePackSend(UUID packId, String url, String hash, boolean required, @Nullable Component prompt) {
      super((PacketTypeCommon)PacketType.Play.Server.RESOURCE_PACK_SEND);
      if (hash.length() > 40) {
         throw new IllegalArgumentException("Hash is too long (max 40, was " + hash.length() + ")");
      } else {
         this.packId = packId;
         this.url = url;
         this.hash = hash;
         this.required = required;
         this.prompt = prompt;
      }
   }

   public void read() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_3)) {
         this.packId = this.readUUID();
      }

      this.url = this.readString();
      this.hash = this.readString(40);
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
         this.required = this.readBoolean();
         boolean hasPrompt = this.readBoolean();
         if (hasPrompt) {
            this.prompt = this.readComponent();
         }
      }

   }

   public void write() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_3)) {
         this.writeUUID(this.packId);
      }

      this.writeString(this.url);
      this.writeString(this.hash, 40);
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
         this.writeBoolean(this.required);
         this.writeBoolean(this.prompt != null);
         if (this.prompt != null) {
            this.writeComponent(this.prompt);
         }
      }

   }

   public void copy(WrapperPlayServerResourcePackSend wrapper) {
      this.packId = wrapper.packId;
      this.url = wrapper.url;
      this.hash = wrapper.hash;
      this.required = wrapper.required;
      this.prompt = wrapper.prompt;
   }

   public UUID getPackId() {
      return this.packId;
   }

   public void setPackId(UUID packId) {
      this.packId = packId;
   }

   public String getUrl() {
      return this.url;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public String getHash() {
      return this.hash;
   }

   public void setHash(String hash) {
      this.hash = hash;
   }

   public boolean isRequired() {
      return this.required;
   }

   public void setRequired(boolean required) {
      this.required = required;
   }

   public Component getPrompt() {
      return this.prompt;
   }

   public void setPrompt(Component prompt) {
      this.prompt = prompt;
   }
}
