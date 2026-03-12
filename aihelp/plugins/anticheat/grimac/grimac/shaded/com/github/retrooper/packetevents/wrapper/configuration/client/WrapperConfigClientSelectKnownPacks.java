package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.KnownPack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.List;

public class WrapperConfigClientSelectKnownPacks extends PacketWrapper<WrapperConfigClientSelectKnownPacks> {
   private List<KnownPack> knownPacks;

   public WrapperConfigClientSelectKnownPacks(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperConfigClientSelectKnownPacks(List<KnownPack> knownPacks) {
      super((PacketTypeCommon)PacketType.Configuration.Client.SELECT_KNOWN_PACKS);
      this.knownPacks = knownPacks;
   }

   public void read() {
      this.knownPacks = this.readList(PacketWrapper::readKnownPack);
   }

   public void write() {
      this.writeList(this.knownPacks, PacketWrapper::writeKnownPack);
   }

   public void copy(WrapperConfigClientSelectKnownPacks wrapper) {
      this.knownPacks = wrapper.knownPacks;
   }

   public List<KnownPack> getKnownPacks() {
      return this.knownPacks;
   }

   public void setKnownPacks(List<KnownPack> knownPacks) {
      this.knownPacks = knownPacks;
   }
}
