package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;

public class WrapperPlayServerSetTitleText extends PacketWrapper<WrapperPlayServerSetTitleText> {
   /** @deprecated */
   @Deprecated
   public static boolean HANDLE_JSON = true;
   private Component title;

   public WrapperPlayServerSetTitleText(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerSetTitleText(String titleJson) {
      super((PacketTypeCommon)PacketType.Play.Server.SET_TITLE_TEXT);
      this.title = this.getSerializers().fromJson(titleJson);
   }

   public WrapperPlayServerSetTitleText(Component title) {
      super((PacketTypeCommon)PacketType.Play.Server.SET_TITLE_TEXT);
      this.title = title;
   }

   public void read() {
      this.title = this.readComponent();
   }

   public void write() {
      this.writeComponent(this.title);
   }

   public void copy(WrapperPlayServerSetTitleText wrapper) {
      this.title = wrapper.title;
   }

   public Component getTitle() {
      return this.title;
   }

   public void setTitle(Component title) {
      this.title = title;
   }

   /** @deprecated */
   @Deprecated
   public String getTitleJson() {
      return this.getSerializers().asJson(this.getTitle());
   }

   /** @deprecated */
   @Deprecated
   public void setTitleJson(String titleJson) {
      this.setTitle(this.getSerializers().fromJson(titleJson));
   }
}
