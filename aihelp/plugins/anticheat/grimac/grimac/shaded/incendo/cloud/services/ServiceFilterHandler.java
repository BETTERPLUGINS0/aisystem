package ac.grim.grimac.shaded.incendo.cloud.services;

import ac.grim.grimac.shaded.geantyref.TypeToken;
import ac.grim.grimac.shaded.incendo.cloud.services.type.Service;
import java.util.Iterator;
import java.util.function.Predicate;
import org.checkerframework.checker.nullness.qual.NonNull;

enum ServiceFilterHandler {
   INSTANCE;

   <Context> boolean passes(final ServiceRepository<Context, ?>.ServiceWrapper<? extends Service<Context, ?>> service, @NonNull final Context context) {
      if (!service.isDefaultImplementation()) {
         Iterator var3 = service.filters().iterator();

         while(var3.hasNext()) {
            Predicate predicate = (Predicate)var3.next();

            try {
               if (!predicate.test(context)) {
                  return false;
               }
            } catch (Exception var6) {
               throw new PipelineException(String.format("Failed to evaluate filter '%s' for '%s'", TypeToken.get(predicate.getClass()).getType().getTypeName(), service), var6);
            }
         }
      }

      return true;
   }

   // $FF: synthetic method
   private static ServiceFilterHandler[] $values() {
      return new ServiceFilterHandler[]{INSTANCE};
   }
}
