package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.positionsource;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class PositionSource {
   protected final PositionSourceType<?> type;

   public PositionSource(PositionSourceType<?> type) {
      this.type = type;
   }

   public static PositionSource decode(NBT nbt, ClientVersion version) {
      NBTCompound compound = (NBTCompound)nbt;
      String typeId = compound.getStringTagValueOrThrow("type");
      PositionSourceType<?> sourceType = PositionSourceTypes.getByName(typeId);
      if (sourceType == null) {
         throw new IllegalStateException("Can't find position source type with id " + typeId);
      } else {
         return sourceType.decode(compound, version);
      }
   }

   public static NBT encode(PositionSource source, ClientVersion version) {
      return encode(source, source.getType(), version);
   }

   public static <T extends PositionSource> NBT encode(T source, PositionSourceType<T> type, ClientVersion version) {
      NBTCompound compound = new NBTCompound();
      compound.setTag("type", new NBTString(type.getName().toString()));
      type.encode(source, version, compound);
      return compound;
   }

   public PositionSourceType<?> getType() {
      return this.type;
   }
}
