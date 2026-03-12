package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.HashSet;
import java.util.Set;

public class WrapperConfigServerUpdateEnabledFeatures extends PacketWrapper<WrapperConfigServerUpdateEnabledFeatures> {
   private Set<ResourceLocation> features;

   public WrapperConfigServerUpdateEnabledFeatures(PacketSendEvent event) {
      super(event);
   }

   public WrapperConfigServerUpdateEnabledFeatures(Set<ResourceLocation> features) {
      super((PacketTypeCommon)PacketType.Configuration.Server.UPDATE_ENABLED_FEATURES);
      this.features = features;
   }

   public void read() {
      this.features = (Set)this.readCollection(HashSet::new, PacketWrapper::readIdentifier);
   }

   public void write() {
      this.writeCollection(this.features, PacketWrapper::writeIdentifier);
   }

   public void copy(WrapperConfigServerUpdateEnabledFeatures wrapper) {
      this.features = wrapper.features;
   }

   public Set<ResourceLocation> getFeatures() {
      return this.features;
   }

   public void setFeatures(Set<ResourceLocation> features) {
      this.features = features;
   }
}
