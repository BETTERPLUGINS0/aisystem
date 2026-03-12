package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.CodecNameable;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public enum PlayerModelType implements CodecNameable {
   SLIM("slim"),
   WIDE("wide");

   public static final NbtCodec<PlayerModelType> CODEC = NbtCodecs.forEnum(values());
   private final String codecName;

   private PlayerModelType(String codecName) {
      this.codecName = codecName;
   }

   public static PlayerModelType read(PacketWrapper<?> wrapper) {
      return wrapper.readBoolean() ? SLIM : WIDE;
   }

   public static void write(PacketWrapper<?> wrapper, PlayerModelType type) {
      wrapper.writeBoolean(type == SLIM);
   }

   public String getCodecName() {
      return this.codecName;
   }

   // $FF: synthetic method
   private static PlayerModelType[] $values() {
      return new PlayerModelType[]{SLIM, WIDE};
   }
}
