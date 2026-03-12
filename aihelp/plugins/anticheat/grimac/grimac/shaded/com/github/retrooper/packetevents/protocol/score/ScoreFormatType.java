package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.score;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.StaticMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public interface ScoreFormatType<T extends ScoreFormat> extends StaticMappedEntity {
   /** @deprecated */
   @Deprecated
   int getId();

   T read(PacketWrapper<?> wrapper);

   void write(PacketWrapper<?> wrapper, T format);
}
