package fr.xephi.authme.libs.net.kyori.adventure.text.serializer.gson;

import fr.xephi.authme.libs.com.google.gson.JsonElement;
import fr.xephi.authme.libs.com.google.gson.JsonNull;
import fr.xephi.authme.libs.net.kyori.adventure.text.event.DataComponentValue;
import java.util.Objects;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.NonExtendable
public interface GsonDataComponentValue extends DataComponentValue {
   static GsonDataComponentValue gsonDataComponentValue(@NotNull final JsonElement data) {
      return (GsonDataComponentValue)(data instanceof JsonNull ? GsonDataComponentValueImpl.RemovedGsonComponentValueImpl.INSTANCE : new GsonDataComponentValueImpl((JsonElement)Objects.requireNonNull(data, "data")));
   }

   @NotNull
   JsonElement element();
}
