package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.body;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface DialogBodyType<T extends DialogBody> extends MappedEntity {
   T decode(NBTCompound compound, PacketWrapper<?> wrapper);

   void encode(NBTCompound compound, PacketWrapper<?> wrapper, T dialogBody);
}
