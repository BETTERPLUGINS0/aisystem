package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.positionsource;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public interface PositionSourceType<T extends PositionSource> extends MappedEntity {
   T read(PacketWrapper<?> wrapper);

   void write(PacketWrapper<?> wrapper, T source);

   T decode(NBTCompound compound, ClientVersion version);

   void encode(T source, ClientVersion version, NBTCompound compound);
}
