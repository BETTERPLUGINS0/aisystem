package ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.serializer.gson;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.event.DataComponentValue;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import java.util.Objects;

@ApiStatus.NonExtendable
public interface GsonDataComponentValue extends DataComponentValue {
   static GsonDataComponentValue gsonDataComponentValue(@NotNull final JsonElement data) {
      return (GsonDataComponentValue)(data instanceof JsonNull ? GsonDataComponentValueImpl.RemovedGsonComponentValueImpl.INSTANCE : new GsonDataComponentValueImpl((JsonElement)Objects.requireNonNull(data, "data")));
   }

   @NotNull
   JsonElement element();
}
