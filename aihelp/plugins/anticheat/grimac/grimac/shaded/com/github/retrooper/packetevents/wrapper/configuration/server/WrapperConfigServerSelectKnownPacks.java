package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.KnownPack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.List;

public class WrapperConfigServerSelectKnownPacks extends PacketWrapper<WrapperConfigServerSelectKnownPacks> {
   private List<KnownPack> knownPacks;

   public WrapperConfigServerSelectKnownPacks(PacketSendEvent event) {
      super(event);
   }

   public WrapperConfigServerSelectKnownPacks(List<KnownPack> knownPacks) {
      super((PacketTypeCommon)PacketType.Configuration.Server.SELECT_KNOWN_PACKS);
      this.knownPacks = knownPacks;
   }

   public void read() {
      this.knownPacks = this.readList(PacketWrapper::readKnownPack);
   }

   public void write() {
      this.writeList(this.knownPacks, PacketWrapper::writeKnownPack);
   }

   public void copy(WrapperConfigServerSelectKnownPacks wrapper) {
      this.knownPacks = wrapper.knownPacks;
   }

   public List<KnownPack> getKnownPacks() {
      return this.knownPacks;
   }

   public void setKnownPacks(List<KnownPack> knownPacks) {
      this.knownPacks = knownPacks;
   }
}
