package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;

public class WrapperPlayServerSetTitleSubtitle extends PacketWrapper<WrapperPlayServerSetTitleSubtitle> {
   /** @deprecated */
   @Deprecated
   public static boolean HANDLE_JSON = true;
   private Component subtitle;

   public WrapperPlayServerSetTitleSubtitle(PacketSendEvent event) {
      super(event);
   }

   /** @deprecated */
   @Deprecated
   public WrapperPlayServerSetTitleSubtitle(String subtitleJson) {
      super((PacketTypeCommon)PacketType.Play.Server.SET_TITLE_SUBTITLE);
      this.subtitle = this.getSerializers().fromJson(subtitleJson);
   }

   public WrapperPlayServerSetTitleSubtitle(Component subtitle) {
      super((PacketTypeCommon)PacketType.Play.Server.SET_TITLE_SUBTITLE);
      this.subtitle = subtitle;
   }

   public void read() {
      this.subtitle = this.readComponent();
   }

   public void write() {
      this.writeComponent(this.subtitle);
   }

   public void copy(WrapperPlayServerSetTitleSubtitle wrapper) {
      this.subtitle = wrapper.subtitle;
   }

   public Component getSubtitle() {
      return this.subtitle;
   }

   public void setSubtitle(Component subtitle) {
      this.subtitle = subtitle;
   }

   /** @deprecated */
   @Deprecated
   public String getSubtitleJson() {
      return this.getSerializers().asJson(this.getSubtitle());
   }

   /** @deprecated */
   @Deprecated
   public void setSubtitleJson(String subtitleJson) {
      this.setSubtitle(this.getSerializers().fromJson(subtitleJson));
   }
}
