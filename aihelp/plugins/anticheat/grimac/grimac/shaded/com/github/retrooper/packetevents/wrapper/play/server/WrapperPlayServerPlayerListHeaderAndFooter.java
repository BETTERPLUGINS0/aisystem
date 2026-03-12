package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.serializer.gson.GsonComponentSerializer;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;

public class WrapperPlayServerPlayerListHeaderAndFooter extends PacketWrapper<WrapperPlayServerPlayerListHeaderAndFooter> {
   /** @deprecated */
   @Deprecated
   public static boolean HANDLE_JSON = true;
   private Component header;
   private Component footer;

   public WrapperPlayServerPlayerListHeaderAndFooter(PacketSendEvent event) {
      super(event);
   }

   /** @deprecated */
   @Deprecated
   public WrapperPlayServerPlayerListHeaderAndFooter(String headerJson, String footerJson) {
      super((PacketTypeCommon)PacketType.Play.Server.PLAYER_LIST_HEADER_AND_FOOTER);
      GsonComponentSerializer gson = this.getSerializers().gson();
      this.header = gson.deserializeOrNull(headerJson);
      this.footer = gson.deserializeOrNull(footerJson);
   }

   public WrapperPlayServerPlayerListHeaderAndFooter(Component header, Component footer) {
      super((PacketTypeCommon)PacketType.Play.Server.PLAYER_LIST_HEADER_AND_FOOTER);
      this.header = header;
      this.footer = footer;
   }

   public void read() {
      this.header = this.readComponent();
      this.footer = this.readComponent();
   }

   public void write() {
      this.writeComponent(this.header);
      this.writeComponent(this.footer);
   }

   public void copy(WrapperPlayServerPlayerListHeaderAndFooter wrapper) {
      this.header = wrapper.header;
      this.footer = wrapper.footer;
   }

   public Component getHeader() {
      return this.header;
   }

   public void setHeader(Component header) {
      this.header = header;
   }

   public Component getFooter() {
      return this.footer;
   }

   public void setFooter(Component footer) {
      this.footer = footer;
   }

   /** @deprecated */
   @Deprecated
   public String getHeaderJson() {
      return this.getSerializers().asJson(this.getHeader());
   }

   /** @deprecated */
   @Deprecated
   public void setHeaderJson(String headerJson) {
      this.setHeader(this.getSerializers().fromJson(headerJson));
   }

   /** @deprecated */
   @Deprecated
   public String getFooterJson() {
      return this.getSerializers().asJson(this.getFooter());
   }

   /** @deprecated */
   @Deprecated
   public void setFooterJson(String footerJson) {
      this.setFooter(this.getSerializers().fromJson(footerJson));
   }

   /** @deprecated */
   @Deprecated
   public Component getHeaderComponent() {
      return this.getHeader();
   }

   /** @deprecated */
   @Deprecated
   public void setHeaderComponent(Component headerComponent) {
      this.setHeader(headerComponent);
   }

   /** @deprecated */
   @Deprecated
   public Component getFooterComponent() {
      return this.getFooter();
   }

   /** @deprecated */
   @Deprecated
   public void setFooterComponent(Component footerComponent) {
      this.setFooter(footerComponent);
   }
}
