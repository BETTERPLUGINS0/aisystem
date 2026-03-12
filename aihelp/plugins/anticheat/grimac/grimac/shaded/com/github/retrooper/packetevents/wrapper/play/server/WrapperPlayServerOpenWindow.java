package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;

public class WrapperPlayServerOpenWindow extends PacketWrapper<WrapperPlayServerOpenWindow> {
   private int containerId;
   private int type;
   private String legacyType;
   private int legacySlots;
   private int horseId;
   private Component title;
   private boolean useProvidedWindowTitle;

   public WrapperPlayServerOpenWindow(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerOpenWindow(int containerId, int type, Component title) {
      super((PacketTypeCommon)PacketType.Play.Server.OPEN_WINDOW);
      this.containerId = containerId;
      this.type = type;
      this.title = title;
   }

   public WrapperPlayServerOpenWindow(int containerId, String legacyType, Component title, int legacySlots, int horseId) {
      super((PacketTypeCommon)PacketType.Play.Server.OPEN_WINDOW);
      this.containerId = containerId;
      this.legacyType = legacyType;
      this.legacySlots = legacySlots;
      this.horseId = horseId;
      this.title = title;
   }

   public WrapperPlayServerOpenWindow(int containerId, int type, Component title, int legacySlots, boolean useProvidedWindowTitle, int horseId) {
      super((PacketTypeCommon)PacketType.Play.Server.OPEN_WINDOW);
      this.containerId = containerId;
      this.type = type;
      this.title = title;
      this.legacySlots = legacySlots;
      this.useProvidedWindowTitle = useProvidedWindowTitle;
      this.horseId = horseId;
   }

   public void read() {
      if (!this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2) && !this.serverVersion.isOlderThan(ServerVersion.V_1_14)) {
         this.containerId = this.readVarInt();
      } else {
         this.containerId = this.readContainerId();
      }

      if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
         this.type = this.readUnsignedByte();
         this.title = this.getSerializers().fromLegacy(this.readString(32));
         this.legacySlots = this.readUnsignedByte();
         this.useProvidedWindowTitle = this.readBoolean();
         if (this.type == 11) {
            this.horseId = this.readInt();
         }

      } else {
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14)) {
            this.type = this.readVarInt();
            this.title = this.readComponent();
         } else {
            this.legacyType = this.readString();
            this.title = this.readComponent();
            this.legacySlots = this.readUnsignedByte();
            if (this.legacyType.equals("EntityHorse")) {
               this.horseId = this.readInt();
            }
         }

      }
   }

   public void write() {
      if (!this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2) && !this.serverVersion.isOlderThan(ServerVersion.V_1_14)) {
         this.writeVarInt(this.containerId);
      } else {
         this.writeContainerId(this.containerId);
      }

      if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
         this.writeByte(this.type);
         this.writeString(this.getSerializers().asLegacy(this.title));
         this.writeByte(this.legacySlots);
         this.writeBoolean(this.useProvidedWindowTitle);
         if (this.type == 11) {
            this.writeInt(this.horseId);
         }

      } else {
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14)) {
            this.writeVarInt(this.type);
            this.writeComponent(this.title);
         } else {
            this.writeString(this.legacyType);
            this.writeComponent(this.title);
            this.writeByte(this.legacySlots);
            if (this.legacyType.equals("EntityHorse")) {
               this.writeInt(this.horseId);
            }
         }

      }
   }

   public void copy(WrapperPlayServerOpenWindow wrapper) {
      this.containerId = wrapper.containerId;
      this.type = wrapper.type;
      this.legacyType = wrapper.legacyType;
      this.legacySlots = wrapper.legacySlots;
      this.horseId = wrapper.horseId;
      this.title = wrapper.title;
      this.useProvidedWindowTitle = wrapper.useProvidedWindowTitle;
   }

   public int getContainerId() {
      return this.containerId;
   }

   public void setContainerId(int containerId) {
      this.containerId = containerId;
   }

   public int getType() {
      return this.type;
   }

   public void setType(int type) {
      this.type = type;
   }

   public String getLegacyType() {
      return this.legacyType;
   }

   public void setLegacyType(String legacyType) {
      this.legacyType = legacyType;
   }

   public int getLegacySlots() {
      return this.legacySlots;
   }

   public void setLegacySlots(int legacySlots) {
      this.legacySlots = legacySlots;
   }

   public int getHorseId() {
      return this.horseId;
   }

   public void setHorseId(int horseId) {
      this.horseId = horseId;
   }

   public Component getTitle() {
      return this.title;
   }

   public void setTitle(Component title) {
      this.title = title;
   }

   public boolean isUseProvidedWindowTitle() {
      return this.useProvidedWindowTitle;
   }

   public void setUseProvidedWindowTitle(boolean useProvidedWindowTitle) {
      this.useProvidedWindowTitle = useProvidedWindowTitle;
   }
}
