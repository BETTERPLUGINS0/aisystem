package ac.grim.grimac.shaded.incendo.cloud.services.type;

import ac.grim.grimac.shaded.incendo.cloud.services.State;
import org.checkerframework.checker.nullness.qual.NonNull;

@FunctionalInterface
public interface SideEffectService<Context> extends Service<Context, State> {
   @NonNull
   State handle(@NonNull Context context) throws Exception;
}
