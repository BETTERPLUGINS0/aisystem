package fr.xephi.authme.libs.net.kyori.adventure.text.serializer.gson.impl;

import com.google.auto.service.AutoService;
import fr.xephi.authme.libs.net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import fr.xephi.authme.libs.net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import fr.xephi.authme.libs.net.kyori.adventure.util.Services;
import java.util.function.Supplier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
@AutoService({JSONComponentSerializer.Provider.class})
public final class JSONComponentSerializerProviderImpl implements JSONComponentSerializer.Provider, Services.Fallback {
   @NotNull
   public JSONComponentSerializer instance() {
      return GsonComponentSerializer.gson();
   }

   @NotNull
   public Supplier<JSONComponentSerializer.Builder> builder() {
      return GsonComponentSerializer::builder;
   }

   public String toString() {
      return "JSONComponentSerializerProviderImpl[GsonComponentSerializer]";
   }
}
