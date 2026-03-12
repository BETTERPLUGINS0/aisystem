package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.serializer;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTLimiter;
import java.io.IOException;

public interface NBTReader<T extends NBT, IN> {
   default T deserializeTag(NBTLimiter limiter, IN from) throws IOException {
      return this.deserializeTag(limiter, from, true);
   }

   T deserializeTag(NBTLimiter limiter, IN from, boolean named) throws IOException;
}
