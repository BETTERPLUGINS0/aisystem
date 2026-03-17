package advancedplugins.pm2.cv.api.registry;

import advancedplugins.pm2.cv.api.interfaces.IDeyed;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Registry<T extends IDeyed> extends Iterable<T> {
   @Nullable
   T get(@NotNull String var1);

   @NotNull
   Collection<T> getEntries();

   @NotNull
   Collection<String> getIds();

   void register(@NotNull T var1);

   void unregister(@NotNull String var1);

   default void load() {
   }
}
