package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.PacketSide;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public interface PacketTypeCommon {
   default String getName() {
      return ((Enum)this).name();
   }

   int getId(ClientVersion version);

   PacketSide getSide();

   @Nullable
   Class<? extends PacketWrapper<?>> getWrapperClass();
}
