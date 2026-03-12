package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.MathUtil;

public interface Attribute extends MappedEntity {
   default ResourceLocation getName() {
      return this.getName(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
   }

   ResourceLocation getName(ClientVersion version);

   default double sanitizeValue(double value) {
      return this.sanitizeValue(value, PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
   }

   default double sanitizeValue(double value, ClientVersion version) {
      return !Double.isNaN(value) ? MathUtil.clamp(value, this.getMinValue(), this.getMaxValue()) : this.getMinValue();
   }

   double getDefaultValue();

   double getMinValue();

   double getMaxValue();
}
