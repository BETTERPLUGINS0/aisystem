package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.action;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtMapDecoder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtMapEncoder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class StaticActionType<T extends Action> extends AbstractMappedEntity implements ActionType<T> {
   private final NbtMapDecoder<T> decoder;
   private final NbtMapEncoder<T> encoder;

   @ApiStatus.Internal
   public StaticActionType(@Nullable TypesBuilderData data, NbtMapDecoder<T> decoder, NbtMapEncoder<T> encoder) {
      super(data);
      this.decoder = decoder;
      this.encoder = encoder;
   }

   public T decode(NBTCompound compound, PacketWrapper<?> wrapper) {
      return (Action)this.decoder.decode(compound, wrapper);
   }

   public void encode(NBTCompound compound, PacketWrapper<?> wrapper, T action) {
      this.encoder.encode(compound, wrapper, action);
   }
}
