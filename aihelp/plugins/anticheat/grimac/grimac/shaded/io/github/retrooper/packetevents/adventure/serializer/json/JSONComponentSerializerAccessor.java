package ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.serializer.json;

import ac.grim.grimac.shaded.kyori.adventure.util.Services;
import java.util.Optional;
import java.util.function.Supplier;

final class JSONComponentSerializerAccessor {
   private static final Optional<JSONComponentSerializer.Provider> SERVICE = Services.serviceWithFallback(JSONComponentSerializer.Provider.class);

   private JSONComponentSerializerAccessor() {
   }

   static final class Instances {
      static final JSONComponentSerializer INSTANCE;
      static final Supplier<JSONComponentSerializer.Builder> BUILDER_SUPPLIER;

      static {
         INSTANCE = (JSONComponentSerializer)JSONComponentSerializerAccessor.SERVICE.map(JSONComponentSerializer.Provider::instance).orElse(DummyJSONComponentSerializer.INSTANCE);
         BUILDER_SUPPLIER = (Supplier)JSONComponentSerializerAccessor.SERVICE.map(JSONComponentSerializer.Provider::builder).orElse(DummyJSONComponentSerializer.BuilderImpl::new);
      }
   }
}
