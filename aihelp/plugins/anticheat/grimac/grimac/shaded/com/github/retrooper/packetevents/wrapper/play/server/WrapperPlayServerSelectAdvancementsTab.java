package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public class WrapperPlayServerSelectAdvancementsTab extends PacketWrapper<WrapperPlayServerSelectAdvancementsTab> {
   @Nullable
   private ResourceLocation identifier;

   public WrapperPlayServerSelectAdvancementsTab(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerSelectAdvancementsTab(@Nullable ResourceLocation identifier) {
      super((PacketTypeCommon)PacketType.Play.Server.SELECT_ADVANCEMENTS_TAB);
      this.identifier = identifier;
   }

   public void read() {
      this.identifier = (ResourceLocation)this.readOptional(PacketWrapper::readIdentifier);
   }

   public void write() {
      this.writeOptional(this.identifier, PacketWrapper::writeIdentifier);
   }

   public void copy(WrapperPlayServerSelectAdvancementsTab wrapper) {
      this.identifier = wrapper.identifier;
   }

   @Nullable
   public ResourceLocation getIdentifier() {
      return this.identifier;
   }

   public void setIdentifier(@Nullable ResourceLocation identifier) {
      this.identifier = identifier;
   }
}
