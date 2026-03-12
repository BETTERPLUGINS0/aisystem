package ac.grim.grimac.shaded.incendo.cloud.parser;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE
)
public final class ParserParameters {
   private final Map<ParserParameter<?>, Object> internalMap = new HashMap();

   @NonNull
   public static ParserParameters empty() {
      return new ParserParameters();
   }

   @NonNull
   public static <T> ParserParameters single(@NonNull final ParserParameter<T> parameter, @NonNull final T value) {
      ParserParameters parameters = new ParserParameters();
      parameters.store(parameter, value);
      return parameters;
   }

   public <T> boolean has(@NonNull final ParserParameter<T> parameter) {
      return this.internalMap.containsKey(parameter);
   }

   public <T> void store(@NonNull final ParserParameter<T> parameter, @NonNull final T value) {
      this.internalMap.put(parameter, value);
   }

   @NonNull
   public <T> T get(@NonNull final ParserParameter<T> parameter, @NonNull final T defaultValue) {
      return this.internalMap.getOrDefault(parameter, defaultValue);
   }

   public void merge(@NonNull final ParserParameters other) {
      this.internalMap.putAll(other.internalMap);
   }

   @NonNull
   public Map<ParserParameter<?>, Object> parameters() {
      return Collections.unmodifiableMap(this.internalMap);
   }
}
