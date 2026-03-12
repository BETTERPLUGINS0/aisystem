package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.type;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.data.ParticleData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface ParticleType<T extends ParticleData> extends MappedEntity {
   T readData(PacketWrapper<?> wrapper);

   void writeData(PacketWrapper<?> wrapper, T data);

   T decodeData(NBTCompound compound, ClientVersion version);

   void encodeData(T value, ClientVersion version, NBTCompound compound);

   /** @deprecated */
   @Deprecated
   default Function<PacketWrapper<?>, ParticleData> readDataFunction() {
      return this::readData;
   }

   /** @deprecated */
   @Deprecated
   default BiConsumer<PacketWrapper<?>, ParticleData> writeDataFunction() {
      return (wrapper, data) -> {
         this.writeData(wrapper, data);
      };
   }
}
