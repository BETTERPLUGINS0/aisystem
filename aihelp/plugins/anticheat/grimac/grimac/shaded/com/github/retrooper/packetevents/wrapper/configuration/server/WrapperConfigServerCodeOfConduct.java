package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class WrapperConfigServerCodeOfConduct extends PacketWrapper<WrapperConfigServerCodeOfConduct> {
   private String codeOfConduct;

   public WrapperConfigServerCodeOfConduct(PacketSendEvent event) {
      super(event);
   }

   public WrapperConfigServerCodeOfConduct(String codeOfConduct) {
      super((PacketTypeCommon)PacketType.Configuration.Server.CODE_OF_CONDUCT);
      this.codeOfConduct = codeOfConduct;
   }

   public void read() {
      this.codeOfConduct = this.readString();
   }

   public void write() {
      this.writeString(this.codeOfConduct);
   }

   public void copy(WrapperConfigServerCodeOfConduct wrapper) {
      this.codeOfConduct = wrapper.codeOfConduct;
   }

   public String getCodeOfConduct() {
      return this.codeOfConduct;
   }

   public void setCodeOfConduct(String codeOfConduct) {
      this.codeOfConduct = codeOfConduct;
   }
}
