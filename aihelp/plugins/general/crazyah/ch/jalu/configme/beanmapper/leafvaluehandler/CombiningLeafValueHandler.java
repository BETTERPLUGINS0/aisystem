package ch.jalu.configme.beanmapper.leafvaluehandler;

import ch.jalu.configme.utils.TypeInformation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CombiningLeafValueHandler implements LeafValueHandler {
   @NotNull
   private final Collection<LeafValueHandler> handlers;

   public CombiningLeafValueHandler(@NotNull LeafValueHandler... handlers) {
      this((Collection)Arrays.asList(handlers));
   }

   public CombiningLeafValueHandler(@NotNull Collection<LeafValueHandler> handlers) {
      this.handlers = Collections.unmodifiableCollection(handlers);
   }

   @Nullable
   public Object convert(@NotNull TypeInformation typeInformation, @Nullable Object value) {
      return this.getFirstNonNull((t) -> {
         return t.convert(typeInformation, value);
      });
   }

   @Nullable
   public Object toExportValue(@Nullable Object value) {
      return this.getFirstNonNull((t) -> {
         return t.toExportValue(value);
      });
   }

   @NotNull
   protected final Collection<LeafValueHandler> getHandlers() {
      return this.handlers;
   }

   @Nullable
   private Object getFirstNonNull(@NotNull Function<LeafValueHandler, Object> callback) {
      return this.handlers.stream().map(callback).filter(Objects::nonNull).findFirst().orElse((Object)null);
   }
}
