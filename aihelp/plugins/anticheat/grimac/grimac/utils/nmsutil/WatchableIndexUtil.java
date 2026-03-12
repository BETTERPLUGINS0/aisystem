package ac.grim.grimac.utils.nmsutil;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Iterator;
import java.util.List;
import lombok.Generated;

public final class WatchableIndexUtil {
   @Nullable
   public static EntityData<?> getIndex(@NotNull List<EntityData<?>> objects, int index) {
      Iterator var2 = objects.iterator();

      EntityData object;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         object = (EntityData)var2.next();
      } while(object.getIndex() != index);

      return object;
   }

   @Generated
   private WatchableIndexUtil() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
