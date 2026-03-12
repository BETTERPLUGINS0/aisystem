package ac.grim.grimac.shaded.incendo.cloud.services.type;

import ac.grim.grimac.shaded.incendo.cloud.services.ExecutionOrder;
import ac.grim.grimac.shaded.incendo.cloud.services.PipelineException;
import java.util.function.Function;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@FunctionalInterface
public interface Service<Context, Result> extends Function<Context, Result> {
   @Nullable
   Result handle(@NonNull Context context) throws Exception;

   @Nullable
   default Result apply(@NonNull Context context) {
      try {
         return this.handle(context);
      } catch (Exception var3) {
         throw new PipelineException(var3);
      }
   }

   @Nullable
   default ExecutionOrder order() {
      return null;
   }
}
