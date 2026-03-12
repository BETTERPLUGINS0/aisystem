package fr.xephi.authme.libs.net.kyori.adventure.text.serializer.gson.impl;

import com.google.auto.service.AutoService;
import fr.xephi.authme.libs.com.google.gson.JsonNull;
import fr.xephi.authme.libs.net.kyori.adventure.key.Key;
import fr.xephi.authme.libs.net.kyori.adventure.text.event.DataComponentValue;
import fr.xephi.authme.libs.net.kyori.adventure.text.event.DataComponentValueConverterRegistry;
import fr.xephi.authme.libs.net.kyori.adventure.text.serializer.gson.GsonDataComponentValue;
import java.util.Collections;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@AutoService({DataComponentValueConverterRegistry.Provider.class})
@ApiStatus.Internal
public final class GsonDataComponentValueConverterProvider implements DataComponentValueConverterRegistry.Provider {
   private static final Key ID = Key.key("adventure", "serializer/gson");

   @NotNull
   public Key id() {
      return ID;
   }

   @NotNull
   public Iterable<DataComponentValueConverterRegistry.Conversion<?, ?>> conversions() {
      return Collections.singletonList(DataComponentValueConverterRegistry.Conversion.convert(DataComponentValue.Removed.class, GsonDataComponentValue.class, (k, removed) -> {
         return GsonDataComponentValue.gsonDataComponentValue(JsonNull.INSTANCE);
      }));
   }
}
